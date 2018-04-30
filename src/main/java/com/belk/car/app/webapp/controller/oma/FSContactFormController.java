/**
 * Class Name : FSContactFormController.java 
 * Version Information : v1.0 
 * Date : 12/01/2009 
 * Copyright Notice : All rights are reserved to Syntel.
 */

package com.belk.car.app.webapp.controller.oma;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.oma.Address;
import com.belk.car.app.model.oma.Contacts;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.service.ContactsManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.FSContactForm;

/**
 * Controller for the add/edit Contacts
 * 
 * @author afusy13
 */
public class FSContactFormController extends BaseFormController implements DropShipConstants{
	private ContactsManager contactsManager;

	/**
	 * @param contactsManager
	 */
	public void setContactsManager(ContactsManager contactsManager) {
		this.contactsManager = contactsManager;
	}

	/**
	 * Constructor to set the Command Object
	 */
	public FSContactFormController() {
		if (log.isDebugEnabled()) {
			log.debug("Inside FSContactFormController() Constructor..");
		}
		setCommandName("fsContactForm");
		setCommandClass(FSContactForm.class);
	}

	/**
	 * Method to check the submit type of the form
	 */
	public ModelAndView processFormSubmission(
			HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Inside processFormSubmission() method..");
		}
			ModelAndView modelAndView=null;
		if (request.getParameter("cancel") != null) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("contactId", request.getParameter("contactId"));
			 modelAndView= new ModelAndView(this.getCancelView());
		}else{
			modelAndView= super.processFormSubmission(request, response, command, errors);
		}
		return modelAndView;
	}

	/**
	 * After submitting form, this method saves the records in database.
	 * 
	 * @param request HttpServletRequest response HttpServletResponse command
	 *            Command Object errors Error object
	 */
	public ModelAndView onSubmit(
			HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Inside onSubmit() method..");
		}
		FSContactForm contactForm = (FSContactForm) command;
		boolean isAdd = false;
		boolean isVendorContact = false;
		//Check whether the user session has expired
		// Get the fulfillment service Object from session
		HttpSession session = request.getSession();
		if (session.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION) == null) {
			log.debug("User Session has expired. Redirecting to Dashboard.");
			return new ModelAndView("redirect:/mainMenu.html");
		}
		
		// Populate the attributes from Form to bean
		Contacts contacts = populateDataFromForm(contactForm);
		// Populate Data (Fulfillment Service & Vendor) from session to bean
		contacts = populateDataFromSession(request, response, contacts);
	
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("contacts", contacts);
		model.put("fsContactForm", contactForm);
		model.put("readOnly", false); //Required for bread crumb contact link.
		request.setAttribute("readOnly", false);
		// Save the bean in the database.
		try {
			//Check for new Contact or existing contact.
			if (contacts.getContactId() == 0) {
				isAdd = true;
			}else{ 
				isAdd = false;
			}
			//Check for Fendor Contact or fulfillment service contacts.
			if( contacts.getVendor() != null){
				isVendorContact = true;
			}else {
				isVendorContact = false;
			}
			//Setting the created Date, as from form created date will be null.
			getCreatedDate(contacts,isVendorContact);
			// Setting the Base Auditable form attributes
			contacts.setAuditInfo(getLoggedInUser());
			contacts.getAddress().setAuditInfo(getLoggedInUser());
			// Set the attributes to render the form back.
			contacts = contactsManager.saveOrUpdateContact(contacts,isVendorContact);
			if (log.isDebugEnabled()) {
				log.debug("Contact Inserted Successfully..with ID"+ contacts.getContactId());
			}
			//Following logic added to retain the IDs of Contact And Address bean in case of Add.
			//This is for the scenario like, if user clicked on Add Contact and Saved it. 
			//However user updated any information on the same page and hit Save button,
			//then, the same previous object needs to be updated rather than creating a new object. 
			contactForm.setContacts(contacts); 
			if(contacts.getAddress() != null){
				contactForm.setAddress(contacts.getAddress());
			}else{
				contactForm.setAddress(new Address());
			}
			// Setting parameter to display Saved Successfully message
			errors.rejectValue("", "VALID", SAVED_SUCCESSFULLY_MESSAGE);
		}catch (DataAccessException dataAccessException) {
			if (log.isErrorEnabled()) {
				log.error("More specific Exception  :"+dataAccessException.getMostSpecificCause());
			}
			setValuesInForm(contacts,contactForm,isAdd);
			errors.rejectValue("", "errors.contact.data.invalid");
		}catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("Exception occured while saving into database.", e);
			}
			setValuesInForm(contacts,contactForm,isAdd);
			errors.rejectValue("", "errors.contact.save.exception");
		}
		
		request.setAttribute("fsContactForm", contactForm);
		return showForm(request, response, errors);
	}
	/**
	 * Gets the created Date and Set it  to the contacts object.
	 * @param contacts
	 * @param isVendorContact
	 */
	private void getCreatedDate(Contacts contacts, boolean isVendorContact) {
		if(contacts.getContactId() != 0){
			log.debug("contact id is not null");
			Date createdDt = contactsManager.getContactCreatedDateById(contacts.getContactId(), isVendorContact);;
			log.debug("Got Created Date :"+ createdDt);
			contacts.setCreatedDate(createdDt);
			log.debug("Set the created fields..");
		}
		
	}

	/**
	 * Sets the Contact bean and address bean in the form object.
	 * This helps in retaining the form values.
	 * @param contacts
	 * @param contactForm
	 * @param isAdd
	 */
	private void setValuesInForm(Contacts contacts, FSContactForm contactForm, boolean isAdd){
		// Required this check as before committing the object in database
		// the new sequence value get generated and stored as id in object
		// and if error occurred then the contact object having ID is returned.
		if (isAdd) {
			contacts.setContactId(0);
			if(contacts.getAddress() != null){
				contacts.getAddress().setAddressID(0);
			}
		}
		//Set the Contact in contact Form
		contactForm.setContacts(contacts);
		//Set Address in contact Form
		if(contacts.getAddress() != null){
			contactForm.setAddress(contacts.getAddress());
		}else{
			contactForm.setAddress(new Address());
		}
	}

	/**
	 * Populates data from session (Fulfillment Service Object and
	 * VendorFulfillment Service Object)
	 * 
	 * @param request HttpServletRequest request object
	 * @param response HttpServletResponse Object to be passed as a response
	 * @param contacts Contacts Bean in which the values are to be set
	 * @return contacts Contacts Bean having all the values set
	 */
	private Contacts populateDataFromSession(
			HttpServletRequest request, HttpServletResponse response,
			Contacts contacts) {
		FulfillmentServiceVendor vendorFulfillmentServiceModel = null;
		// Get the fulfillment service Object from session
		HttpSession session = request.getSession();
		if (session.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION) != null) {
			contacts.setFulfillmentService((FulfillmentService) session
					.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION));
		}
		else {
				log.error("Fulfillment Service Object is not in session");
		}
		if (session.getAttribute(VEN_INFO_FROM_SESSION) != null) {
			vendorFulfillmentServiceModel = (FulfillmentServiceVendor) session
					.getAttribute(VEN_INFO_FROM_SESSION);
			if (vendorFulfillmentServiceModel.getVndr() == null) {
				contacts.setVendor(contactsManager.getVendor(vendorFulfillmentServiceModel.getVendorID()));
			}
			else {
				contacts.setVendor(vendorFulfillmentServiceModel.getVndr());
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("Fulfillment bean Id:"+ contacts.getFulfillmentService().getFulfillmentServiceID());
		}
		return contacts;
	}

	/**
	 * Populates data from a Contact Form Object into Contact bean
	 * 
	 * @param contactForm FSContactForm Form object containing the form values
	 * @return contacts Contacts Updated bean with the form values
	 */
	private Contacts populateDataFromForm(
			FSContactForm contactForm) {
		Contacts contacts = null;
		if (log.isDebugEnabled()) {
			log.debug("Inside populateDataFromForm() Method..");
		}
		contacts = contactForm.getContacts();
		contacts.setContactType(contactForm.getContactType());
		contacts.setAddress(contactForm.getAddress());
		// Setting the Phone No and Alternate phone number as form will give the
		// phone numbers in different format
		String phoneNumber = contactForm.getPhoneAreaCode()
				+ contactForm.getPhoneNumber1() + contactForm.getPhoneNumber2();
		String altPhoneNumber = contactForm.getAltPhoneAreaCode()
				+ contactForm.getAltPhoneNumber1()
				+ contactForm.getAltPhoneNumber2();
		contacts.setPhoneNbr(phoneNumber);
		contacts.setAltPhoneNbr(altPhoneNumber);
		if (log.isDebugEnabled()) {
			log.debug("All the form values are set to Contacts bean.");
		}
		return contacts;
	}
}
