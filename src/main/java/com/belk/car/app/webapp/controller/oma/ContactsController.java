/**
 * Class Name : ContactsController.java 
 * Version Information : v1.0 
 * Date : 12/01/2009 
 * Copyright Notice : All rights are reserved to Syntel.
 */

package com.belk.car.app.webapp.controller.oma;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.oma.Address;
import com.belk.car.app.model.oma.ContactType;
import com.belk.car.app.model.oma.Contacts;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.service.ContactsManager;
import com.belk.car.app.webapp.forms.FSContactForm;

/**
 * A Multi Action Controller to control the different actions from page
 * fulfillmentServiceContacts.jsp The actions are load, search, viewAll, lock,
 * unlock, edit
 * 
 * @author afusy13
 * @version
 */
public class ContactsController extends MultiActionController implements DropShipConstants {

	private transient final Log log = LogFactory.getLog(ContactsController.class);

	private ContactsManager contactsManager;
	private FulfillmentService fulfillmentService = null;
	private FulfillmentServiceVendor vendorFulfillmentService = null;
	private String firstName = "";
	private String lastName = "";
	private String status = "";
	private boolean lockOrUnlockClicked = false;
	private boolean viewAllClicked = false;

	public void setContactsManager(ContactsManager contactsManager) {
		this.contactsManager = contactsManager;
	}

	/**
	 * Method to display all the Contacts available for that FulillmentService
	 * and Vendor under that Fulfillment Service (in case of Vendor Contacts)
	 * 
	 * @param request the request object containing the request attributes
	 * @param response the response object that needs to be passed to view for
	 *            the request
	 * @return ModelAndView The object containing the view name and the model
	 * @throws Exception
	 */
	public ModelAndView viewAll(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Inside  viewAll method....");
		}
		// Set the global variables as null
		lockOrUnlockClicked = false;
		firstName = "";
		lastName = "";
		status = "";
		viewAllClicked = true;
		Map<String, Object> model = new HashMap<String, Object>();
		getDataFromSession(request, response); // Will check the session and get
		// the data
		Long fulfillmentServiceId = (fulfillmentService == null) ? null : fulfillmentService
				.getFulfillmentServiceID();
		Long vendorId = (vendorFulfillmentService == null) ? null : vendorFulfillmentService
				.getVendorID();
		List<Contacts> contacts = contactsManager.getAllContacts(fulfillmentServiceId, vendorId);

		// Setting the contacts in model only if it is not null, otherwise
		// contacts not needed to set.
		if (contacts != null) {
			model.put("contacts", contacts);
		}
		// Retaining the previous values from JSP
		model.put(CONTACT_FIRST_NAME, request.getParameter(CONTACT_FIRST_NAME));
		model.put(CONTACT_LAST_NAME, request.getParameter(CONTACT_LAST_NAME));
		model.put(CONTACT_STATUS, request.getParameter(CONTACT_STATUS));
		model.put("userId", getLoggedInUser().getUsername()); // for Lock/Unlock logic on JSP
		request.setAttribute("scrollPos", 0);
		return new ModelAndView("oma/fulfillmentServiceContacts", model);
	}

	/**
	 * This method loads the Contacts Page when clicked on Tab
	 * 
	 * @param request the request object containing the request attributes
	 * @param response the response object that needs to be passed to view for
	 *            the request
	 * @return ModelAndView The object containing the view name and the model
	 * @throws Exception
	 */
	public ModelAndView load(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Inside the load() method of ContactController.");
		}
		lockOrUnlockClicked = false;
		status = ACTIVE;
		request.setAttribute(CONTACT_STATUS, status);
		// As on load page itself, need to display the associated ACTIVE
		// records.
		return search(request, response);
	}

	/**
	 * Method is used to search the contacts in database according to the
	 * parameters entered by user If no parameter is entered in search fields
	 * then all the ACTIVE contacts will get fetched.
	 * 
	 *@param request the request object containing the request attributes
	 * @param response the response object that needs to be passed to view for
	 *            the request
	 * @return ModelAndView The object containing the view name and the model
	 * @throws Exception
	 */
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Inside search() method of ContactController.");
		}
		// Set the User Roles
		// Using these roles, lock/unlock, edit and add contact buttons are
		// displayed.Display logic is implemented by Java Script function
		// disableFieldsAsPerRole(), which gets called at the time of loading
		// the contacts page.
		setRole(request, response);
		// Map to be passed to JSP
		Map<String, Object> model = new HashMap<String, Object>();
		// Map containing search params, to be passed to search method
		Map<String, Object> params = new HashMap<String, Object>();
		// Get the data available in session
		getDataFromSession(request, response);
		//Check whether the user session is timed out 
		if(fulfillmentService == null){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		//Logic for retaining the pagination
		String page=request.getParameter("d-1332818-p");
		log.debug("Pagination Parameter:"+ page);
		if(StringUtils.isBlank(page)){
			log.debug("Setting pagination parameter to 1.");
			model.put("pagination", 1);
		}else {
			log.debug("Setting pagination parameter to :"+ page);
			model.put("pagination", page);
		}
		//Check to retain the previous actions.
		if (lockOrUnlockClicked && viewAllClicked) {
			// Restore the values to origin again.
			lockOrUnlockClicked = false;
			//viewAllClicked = false;
			viewAll(request, response); // To retain the values on JSP
		} else {
			// Get the fields values from form.
			//Get first name
			if (StringUtils.isNotBlank(request.getParameter(CONTACT_FIRST_NAME))) {
				firstName = request.getParameter(CONTACT_FIRST_NAME);
			}
			else {
				firstName = "";
			}
			//Get last name
			if (StringUtils.isNotBlank(request.getParameter(CONTACT_LAST_NAME))) {
				lastName = request.getParameter(CONTACT_LAST_NAME);
			}
			else {
				lastName = "";
			}
			//Get Status
			if (StringUtils.isNotBlank(request.getParameter(CONTACT_STATUS))) {
				status = request.getParameter(CONTACT_STATUS);
			}
			else if(StringUtils.isBlank(status)){
				status = null;
			}
			// For default search status to be "ACTIVE"
			if (status == null) {
				// because after calling from lock or unlock, we
				// don't get status in the request parameter
				log.debug("status is null.");
				status = ACTIVE;
			}
		}
		// Restore the values to origin again. This is the case when else part of above condition is executed.
		if(StringUtils.isNotBlank(firstName) || StringUtils.isNotBlank(lastName) || StringUtils.isNotBlank(status)){
			lockOrUnlockClicked = false;
			viewAllClicked = false;
		}
		//Log the search parameters for support use.
		if (log.isDebugEnabled()) {
			log.debug("Parameters for Search: status:" + status);
			log.debug("First Name:" + firstName);
			log.debug("Last Name:" + lastName);
		}
		//Set the parameters for search.
		params.put("firstName", firstName);
		params.put("lastName", lastName);
		params.put("status", status);
		params.put("fsId", (fulfillmentService == null) ? null : fulfillmentService
				.getFulfillmentServiceID());
		params.put("vendorId", (vendorFulfillmentService == null) ? null : vendorFulfillmentService
				.getVendorID());

		List<Contacts> contacts = contactsManager.searchContacts(params);

		// Setting the contacts in model only if it is not null, otherwise
		// contacts not needed to set.
		if (contacts != null) {
			model.put("contacts", contacts);
		}
		// Ensuring that after lock/unlock, only one search will pick the
		// retained values form global variables.
		// Otherwise need to pick the search parameters from jsp.
		lockOrUnlockClicked = false;
		// Retaining the previous values from JSP
		model.put(CONTACT_FIRST_NAME, request.getParameter(CONTACT_FIRST_NAME));
		model.put(CONTACT_LAST_NAME, request.getParameter(CONTACT_LAST_NAME));
		model.put(CONTACT_STATUS, status);
		// userId is required for Lock/Unlock Logic on JSP
		model.put("userId", getLoggedInUser().getUsername());
		if(StringUtils.isNotBlank(request.getParameter("scrollPos"))){
			request.setAttribute("scrollPos", request.getParameter("scrollPos"));
		}
		else{
			request.setAttribute("scrollPos", 0);
		}
		// unlock Contact
		contactsManager.unlockContacts(getLoggedInUser().getUsername());
		
		return new ModelAndView("oma/fulfillmentServiceContacts", model);
	}

	/**
	 * Method get called if any record needs to be locked for editing purpose
	 * 
	 * @param request the request object containing the request attributes
	 * @param response the response object that needs to be passed to view for
	 *            the request
	 * @return ModelAndView The object containing the view name and the model
	 * @throws Exception
	 */
	public ModelAndView lock(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Inside lock() method of ContactsController.");
		}
		String contactId = null;
		String action = null;
		long vendorId = 0;
		lockOrUnlockClicked = true;
		//Check whether the user session is timed out 
		if(fulfillmentService == null){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		if (null != vendorFulfillmentService) {
			vendorId = vendorFulfillmentService.getVendorID().longValue();
		}
		else {
			vendorId = 0;
		}

		if (StringUtils.isNotBlank(request.getParameter(ID))) {
			contactId = request.getParameter(ID);
		}
		if (StringUtils.isNotBlank(request.getParameter(METHOD))) {
			action = request.getParameter(METHOD);
		}

		if (log.isDebugEnabled()) {
			log.debug("Inside Lock Method..ContactId to be locked:" + contactId);
		}
		User currentUser = getLoggedInUser();
		contactsManager.lockUnlockContact((new Long(contactId)).longValue(), action, currentUser
				.getUsername(), vendorId);


		return search(request, response);
	}

	/**
	 * Method get called if any record is locked first and needs to be unlocked.
	 * Clicking Unlock button on fulfillmentServiceContacts.jsp will execute
	 * this method. URL should contain the id of the contact and Action to be
	 * taken (i.e.unlock)
	 * 
	 * @param request the request object containing the request attributes
	 * @param response the response object that needs to be passed to view for
	 *            the request
	 * @return ModelAndView The object containing the view name and the model
	 * @throws Exception
	 */
	public ModelAndView unlock(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Inside unlock() method of ContactsController.");
		}
		long vendorId = 0;
		lockOrUnlockClicked = true;
		//Check whether the user session is timed out 
		if(fulfillmentService == null){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		if (null != vendorFulfillmentService) {
			vendorId = vendorFulfillmentService.getVendorID().longValue();
		}
		else {
			vendorId = 0;
		}
		Long contactId = ServletRequestUtils.getLongParameter(request, ID);
		String action = request.getParameter(METHOD);
		User currentUser = getLoggedInUser();
		if (log.isDebugEnabled()) {
			log.debug("Inside UnLock Method..ContactId to be unlocked:" + contactId);
		}
		contactsManager.lockUnlockContact(contactId.longValue(), action, currentUser.getUsername(),
				vendorId);
		return search(request, response);
	}

	/**
	 * Method get called if any record is locked first and Edit button is
	 * clicked on fulfillmentServiceContacts.jsp
	 * 
	 * @param request the request object containing the request attributes
	 * @param response the response object that needs to be passed to view for
	 *            the request
	 * @return ModelAndView The object containing the view name and the model
	 * @throws Exception
	 */
	public ModelAndView edit(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Inside edit() method of ContactsController.");
		}
		Long contactId = null;
		boolean isVendorContact = false;

		if (StringUtils.isNotBlank(ID)) {
			contactId = ServletRequestUtils.getLongParameter(request, ID);
		}
		Map<String, Object> model = new HashMap<String, Object>();
		Contacts contacts = null;
		FSContactForm contactForm = new FSContactForm();
		log.debug("Contact ID:" + contactId);
		// Set Data to session
		setDataToSession(request, response);
		// Get the data from session
		getDataFromSession(request, response);
		//Check whether the user session is timed out 
		if(fulfillmentService == null){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		contactForm.setFulfillmentServiceId(fulfillmentService.getFulfillmentServiceID());
		if (vendorFulfillmentService != null) {
			isVendorContact = true;
			contactForm.setVendorId(vendorFulfillmentService.getVendorID());
		}else{
			contactForm.setVendorId(new Long(0));
		}
		if (StringUtils.isNotBlank(contactId.toString())) {
			contacts = contactsManager.getContactInfoDetails(Long.valueOf(contactId),
					isVendorContact);
		}
		else {
			contacts = new Contacts();
		}

		if (contacts != null) {
			contactForm.setContacts(contacts);
			if (contacts.getAddress() != null) {
				contactForm.setAddress(contacts.getAddress());
			}
			contactForm.setContactType(contacts.getContactType());
		}
		contactForm.setFulfillmentService(fulfillmentService);
		model.put("fsContactForm", contactForm);
		model.put("contacts", contacts);
		// Logic to display the fields in viewOnly mode. For edit mode, the
		// "param" will not be passed from JSP
		if (StringUtils.isNotBlank(request.getParameter(PARAM))) {
			String param = request.getParameter(PARAM);
			if ((VIEW_ONLY_MODE).equals(param)) {
				model.put(READ_ONLY_MODE, true);
			}
			else {
				model.put(READ_ONLY_MODE, false);
			}
		}
		else {
			model.put(READ_ONLY_MODE, false);
		}
		// lock the contact
		contactsManager.lockContact(contactId.toString(), getLoggedInUser());
		
		return new ModelAndView("oma/fulfillmentServiceContactEdit", model);
	}

	/**
	 * Method get called if any new contact need to be added.
	 * 
	 * @param request the request object containing the request attributes
	 * @param response the response object that needs to be passed to view for
	 *            the request
	 * @return ModelAndView The object containing the view name and the model
	 * @throws Exception
	 */
	public ModelAndView add(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Inside add() method of ContactsController.");
		}
		FSContactForm contactForm = new FSContactForm();
		// Get the data from session (Fulfillment Service and vendor Fulfillment Service)
		getDataFromSession(request, response);
		//Check whether the user session is timed out 
		if(fulfillmentService == null){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		//Set data to form
		setDataToForm(contactForm);
		// Set Data to session (States and Contact Types)
		setDataToSession(request, response);
		// Pass the Form object to JSP
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("fsContactForm", contactForm);
		model.put("readOnly", false); // Required for bread crumb contact link.

		return new ModelAndView("oma/fulfillmentServiceContactEdit", model);
	}

	private void setDataToForm(FSContactForm contactForm) {
		if (log.isDebugEnabled()) {
			log.debug("Inside setDataToForm() method.");
		}
		contactForm.setContacts(new Contacts());
		contactForm.setAddress(new Address());
		//Set IDs in contact form
		if(fulfillmentService != null){
			contactForm.setFulfillmentServiceId(fulfillmentService.getFulfillmentServiceID());
			if (log.isDebugEnabled()) {
				log.debug("Fulfillment Service Id :"+ contactForm.getFulfillmentServiceId());
			}
		}else{
			if (log.isDebugEnabled()) {
				log.debug("Fulfillment Service is null.");
			}
			contactForm.setFulfillmentServiceId(Long.parseLong("0"));
		}
		if(vendorFulfillmentService != null	){
			contactForm.setVendorId(vendorFulfillmentService.getVendorID());
		}else{
			contactForm.setVendorId(Long.parseLong("0"));
		}
		//Setting the contact id as 0
		contactForm.getContacts().setContactId(0);
	}

	/**
	 * Returns the currently logged in user
	 * 
	 * @return user User User bean, currently logged in.
	 */
	public User getLoggedInUser() {
		User user = null;
		Authentication auth = ((SecurityContext) SecurityContextHolder.getContext())
		.getAuthentication();
		if (auth.getPrincipal() instanceof UserDetails) {
			user = (User) auth.getPrincipal();
		}
		return user;
	}

	/**
	 * Checks the session and retrieves the data from session.
	 * 
	 * @param request the request object containing the request attributes
	 * @param response the response object that needs to be passed to view for
	 *            the request
	 */
	public void getDataFromSession(HttpServletRequest request, HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("Inside getDataFromSession() method..");
		}
		HttpSession session = request.getSession();
		if (null != session.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION)) {
			fulfillmentService = (FulfillmentService) session
			.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION);
		}
		else {
			fulfillmentService = null;
		}
		if (null != session.getAttribute(VEN_INFO_FROM_SESSION)) {
			vendorFulfillmentService = (FulfillmentServiceVendor) session
			.getAttribute(VEN_INFO_FROM_SESSION);
			if (null == session.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION)) {
				fulfillmentService = contactsManager.getFulfillmentService(vendorFulfillmentService
						.getFulfillmentServId());
			}
		}
		else {
			vendorFulfillmentService = null;
		}

	}

	/**
	 * Sets the data (Contact Types and States) to session
	 * 
	 * @param request
	 * @param response
	 */
	public void setDataToSession(HttpServletRequest request, HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("Inside setDataToSession() Method.");
		}
		List<ContactType> contactTypeList = null;
		HttpSession session = request.getSession();
		// Set data to session
		if (session.getAttribute(CONTACT_TYPES) == null) {
			contactTypeList = contactsManager.getAllContactTypes();
			session.setAttribute(CONTACT_TYPES, contactTypeList);
		}
		// Set States in session
		if (session.getAttribute(CONTACT_STATES) == null) {
			session.setAttribute(CONTACT_STATES, contactsManager.getAllStates());
		}
		// Set Contact Type List
		if (contactTypeList != null && session.getAttribute("OtherContactTypeId") == null) {
			for (ContactType contactType : contactTypeList) {
				if (contactType.getContactTypeDesc().equals(OTHER_TYPE)) {
					session.setAttribute(CONTACT_OTHER_TYPE_ID, contactType.getContactTypeId());
				}
			}
		}
	}

	/**
	 * Sets the roles according to the logged in User.
	 * 
	 * @param request
	 * @param response
	 */
	public void setRole(HttpServletRequest request, HttpServletResponse response) {
		User currentUser = getLoggedInUser();
		if (log.isDebugEnabled()) {
			log.debug("CurrentUser in Contacts controller.." + currentUser);
		}
		HttpSession session = request.getSession();
		/*
		 * Check if the current user has order management Admin role or order
		 * management user role
		 */
		if ((currentUser.isOrderMgmtAdmin())) {
			session.setAttribute(DISPLAY_ROLE, ADMIN);
		}
		//Commented following code to enable the view only mode to all users except Admin
		//else if ((currentUser.isOrderMgmtUser()) || (currentUser.isBuyer())
		// || (currentUser.isWebmerchant())) {
		else {
			session.setAttribute(DISPLAY_ROLE, USER);
		}
	}
}
