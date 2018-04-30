
package com.belk.car.app.webapp.controller.oma;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.acegisecurity.AccessDeniedException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.oma.Address;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.InvoiceMethods;
import com.belk.car.app.model.oma.State;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.VendorFulfillmentServiceManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.FSVendorForm;
import com.belk.car.util.DateUtils;

/**
 * @author afusy07 Priyanka Gadia Date 8-Dec-09
 */

public class FSVendorFormController extends BaseFormController implements DropShipConstants {

	private static final String SHOW_MSG = "showMsg";

	private transient final Log log = LogFactory.getLog(FSVendorFormController.class);

	private VendorFulfillmentServiceManager vendorFulfillmentServiceManager;
	private CarManager carManager;

	public VendorFulfillmentServiceManager getVendorFulfillmentServiceManager() {
		return vendorFulfillmentServiceManager;
	}

	public void setVendorFulfillmentServiceManager(
			VendorFulfillmentServiceManager vendorFulfillmentServiceManager) {
		this.vendorFulfillmentServiceManager = vendorFulfillmentServiceManager;
	}


	public CarManager getCarManager() {
		return carManager;
	}

	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	// Initializing the Command Class
	public FSVendorFormController() {
		setCommandName(FS_VENDOR_FORM);
		setCommandClass(FSVendorForm.class);
	}


	/**
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 * @see com.belk.car.app.webapp.controller.BaseFormController#processFormSubmission(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 * @Enclosing_Method  processFormSubmission
	 * @Date Feb 9, 2010 
	 * 
	 * @TODO
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView processFormSubmission(
			HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors)
	throws Exception {

		log.debug("Entering .......");
		FSVendorForm vendorForm = (FSVendorForm) command;
		HttpSession session = request.getSession();
		if(session.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION) == null){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		if (request.getParameter(CANCEL_BTN) != null) {
			log.debug(" Cancelling ......");

			session.removeAttribute(FS_VENDOR_FORM);
			session.removeAttribute(VEN_INFO_FROM_SESSION);
			session.removeAttribute(ADDR_LIST);
			session.removeAttribute(ADDR_IDS_FROM_SESSION);
			session.removeAttribute(ADDED_ADDRESS);
			return new ModelAndView(getCancelView());
		}

		if (request.getParameter("addressIdForRemove") != null
				&& StringUtils.isNotBlank(request.getParameter("addressIdForRemove"))) {
			if (log.isDebugEnabled()){
				log.debug(".......Remove address called");
			}
			String mode = request.getParameter("modeForRemove");
			long addrId = Long.parseLong(request.getParameter("addressIdForRemove"));
			if (log.isDebugEnabled()){
				log.debug(".......Remove address called mode=" + mode);
				log.debug(".......Remove address called addrId=" + addrId);
			}
			ArrayList<Address> addrList = (ArrayList<Address>) session.getAttribute(ADDR_LIST);
			Iterator<Address> itr = addrList.iterator();
			Address addr = null;
			ArrayList<Long> addrIdsFromSession = null;

			// If address is present in DB then remove address from session and
			// store the address ID in session for future use
			// else remove address only from session.
			if (addrId > 0) {

				addrIdsFromSession = (ArrayList<Long>) session.getAttribute(ADDR_IDS_FROM_SESSION);
				if (null != addrIdsFromSession && !addrIdsFromSession.isEmpty())
				{
					log.debug("............Saving address ID to session-addrIdsFromSession="
							+ addrIdsFromSession.size());
					addrIdsFromSession.add(new Long(addrId));
					session.setAttribute(ADDR_IDS_FROM_SESSION, addrIdsFromSession);
				}
				else {
					List<Long> addrIds = new ArrayList<Long>();
					addrIds.add(new Long(addrId));
					session.setAttribute(ADDR_IDS_FROM_SESSION, addrIds);
					log.debug("............Saving address ID to  new session-addrIdsFromSession");
					// log.debug("............Saving address ID to new  session-addrIdsFromSession="+addrIdsFromSession.size());
				}
			}
			log.debug("address zize before=" + addrList.size());
			while (itr.hasNext()) {
				addr = itr.next();
				if (addr.getAddressID() == addrId) {
					boolean isRemoved = addrList.remove(addr);
					log.debug(".......Address removed from Session isRemoved" + isRemoved);
					break;
				}

			}
			session.setAttribute(ADDED_ADDRESS, "yes");
			if (log.isDebugEnabled()){
				log.debug("address zize after=" + addrList.size());
			}
			session.setAttribute(ADDR_LIST, addrList);

			FulfillmentServiceVendor vendorOld = (FulfillmentServiceVendor) session
			.getAttribute(VEN_INFO_FROM_SESSION);
			if (vendorOld != null) {
				log.debug("vendorForm=" + vendorForm.toString());
				log.debug("returning after getting ven info from session");
				log.debug(".......After Deleting ADDRESS populating vendor info from session="
						+ vendorOld.toString());
				session.setAttribute(FS_VENDOR_FORM, vendorForm);
				return new ModelAndView("redirect:/oma/AddVendor.html?venfsid="
						+ vendorForm.getVndrFulfillmentServId() + "&mode=edit&remove=remove");
			}
			else {
				Enumeration<String> keys = request.getParameterNames();
				while (keys.hasMoreElements()) {
					String key = (String) keys.nextElement();
					String value = request.getParameter(key);

				}
				if (log.isDebugEnabled()){
					log.debug("vendorForm=" + vendorForm.toString());
				}
				session.setAttribute(FS_VENDOR_FORM, vendorForm);
				return new ModelAndView("redirect:/oma/AddVendor.html?name="
						+ vendorForm.getVenName() + "&venNum=" + vendorForm.getVenNum() + "&venID="
						+ vendorForm.getVndrId() + "&mode=verify");
			}
			//	

		}
		/*
		 * Verify Button was clicked from form
		 */
		if (!StringUtils.isBlank(ServletRequestUtils.getStringParameter(request, VERIFY_MODE))
				&& ServletRequestUtils.getStringParameter(request, VERIFY_MODE).equals("Verify")) {
			if (log.isDebugEnabled()){
				log.debug("For Verify Button");
				log.debug("For Verify Button VendorForm=" + vendorForm.toString());
			}
			Vendor ven = null;
			String mode = (String) request.getAttribute(MODE);
			if (log.isDebugEnabled()){
				log.debug("For Verify Button mode=" + mode);
			}
			String venNum = vendorForm.getVenNum();
			if (log.isDebugEnabled()){
				log.debug("vendorForm=" + vendorForm.toString());
			}

			/* If Vendor Number exist then get Vendor Information */
			
			if (null !=  venNum && venNum.length()>0 ) {
				
				ven = carManager.getVendor(venNum);
				if (ven != null) {
					log.debug("For Verify Button Got Ven Name=" + ven.getName());
					log.debug("Returning........ ");
					log.debug("vendorForm=" + vendorForm.toString());
					session.setAttribute(FS_VENDOR_FORM, vendorForm);
					return new ModelAndView("redirect:/oma/AddVendor.html?name=" + ven.getName()
							+ "&venNum=" + venNum + "&venID=" + ven.getVendorId() + "&mode=verify");
				}
				else {
					log.debug("ven num does not exists");
					errors.rejectValue("venNum", "invalid", "Vendor number does not exists. !");
				}
			}

			/*
			 * return new ModelAndView(
			 * "redirect:/oma/AddVendor.html?&mode=add");
			 */

		}

		/* If Save button is clicked then change the mode accordingly */
		if (!StringUtils.isBlank(ServletRequestUtils.getStringParameter(request, SAVE_BTN))
				&& ServletRequestUtils.getStringParameter(request, SAVE_BTN).equals("Save")) {
			if (log.isDebugEnabled()){
				log.debug(" For Save Button");
				log.debug(" For Save Button VendorForm=" + vendorForm.toString());
			}
			/* If Vendor is already added in FS, mode=edit */
			/* else mode is verify */
			if (vendorForm.getVndrFulfillmentServId() != null
					&& !StringUtils.isBlank(vendorForm.getVndrFulfillmentServId())) {
				request.setAttribute(MODE, EDIT);
			}
			else {
				request.setAttribute(MODE, VERIFY_MODE);
			}
			ArrayList<Address> addrListValidate = (ArrayList<Address>) session
			.getAttribute(ADDR_LIST_FROM_SESSION);
			if (log.isDebugEnabled()){
				log.debug("..........Checking conditions-");
				log.debug("1- null!=addrList" + (null != addrListValidate));
			}
			if (null != addrListValidate && !addrListValidate.isEmpty()	) {
				log.debug("..........Address exists in Session-addrList.size()="
						+ addrListValidate.size());
			}
			else {
				errors.rejectValue("shipAddressList", "invalid",
				"Please enter atleast one shipping location !!");
			}
		}
		log.debug("Returning .......");
		return super.processFormSubmission(request, response, command, errors);
	}

	/**
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return
	 * @throws Exception
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 * @Enclosing_Method  onSubmit
	 * @Date Feb 9, 2010 
	 * 
	 * @TODO
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView onSubmit(
			HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors)
	throws Exception {
		if (log.isDebugEnabled()){
			log.debug("Entering ");
		}
		FSVendorForm vendorForm = (FSVendorForm) command;
		if (log.isDebugEnabled()){
			log.debug("VendorForm=" + vendorForm.toString());
		}

		HttpSession session = request.getSession();

		/* Retrieving the AddressList from session */
		ArrayList<Address> addrList = (ArrayList<Address>) session.getAttribute(ADDR_LIST);
		ArrayList<Long> addrIdsFromSession = (ArrayList<Long>) session
		.getAttribute(ADDR_IDS_FROM_SESSION);
		if (addrIdsFromSession != null && !addrIdsFromSession.isEmpty()) {
			Iterator<Long> itr = addrIdsFromSession.iterator();
			while (itr.hasNext()) {
				log.debug("address id to be deleted=" + itr.next());
			}
		}

		FulfillmentServiceVendor vendor = null;

		try {
			if (log.isDebugEnabled()){
				log.debug(".Saving the Vendor in VNDR_FULFILLMENT Table  ");
			}
			vendor = populateVendor(request, vendorForm);
			setAuditInfo(request, vendor);
			vendor = vendorFulfillmentServiceManager.addVendorFulfillmentService(vendor);

			/*
			 * Checking if addrList exist in session or note If addrList is
			 * present then Save the addresses in ADDRESS AND
			 * VNDR_FULFILLMENT_SHIP Table Again retrieve the address from table
			 * for the vendor and store in session
			 */

			if (null != addrList && !addrList.isEmpty() && !(vendor == null)) {
				if (log.isDebugEnabled()){
					log.debug("size of address list from session=" + addrList.size());
				}
				ArrayList<Address> addrListNew = (ArrayList<Address>) vendorFulfillmentServiceManager
				.saveAddress(addrList, vendor.getVendorID(), vendor.getFulfillmentServId(),
						addrIdsFromSession);
				if (log.isDebugEnabled()){
					log.debug("size of new address list " + addrListNew.size());
				}

				session.setAttribute(ADDR_LIST, addrListNew);
				session.removeAttribute(ADDR_IDS_FROM_SESSION);
			}

			// Set the Vendor Info in session to be used under other tabs and on
			// same page also

			if (vendor != null && vendor.getVndrFulfillmentServId() != null) {
				vendor.setStrCreatedDate(DateUtils.formatDate(vendor.getCreatedDate(), "MM/dd/yyyy"));
				vendor.setStrUpdatedDate(DateUtils.formatDate(vendor.getUpdatedDate(), "MM/dd/yyyy"));
				session.setAttribute(VEN_INFO_FROM_SESSION, vendor);
				if (log.isDebugEnabled()){
					log.debug("Vendor Added Successfully");
					log.debug("Returning ........Got the venFSID");
				}
				
				session.setAttribute(SHOW_MSG, "yes");
				errors.rejectValue("", "VALID", "Saved Successfully!");
				return showForm(request, errors, "redirect:/oma/AddVendor.html?venfsid="
						+ vendor.getVndrFulfillmentServId() + "&mode=edit");

			}

		}
		catch (AccessDeniedException ade) {
			log.warn(ade.getMessage());
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		catch (ConstraintViolationException e) {
			errors.rejectValue("vndrFulfillmentServId", "errors.vendor.fs",
					new Object[] { vendorForm.getVenName() }, "an error has occured");
			e.printStackTrace();
			return showForm(request, response, errors);

		}
		catch (DataIntegrityViolationException e) {
			errors.rejectValue("vndrFulfillmentServId", "errors.vendor.fs.exist",
					new Object[] { vendorForm.getVenName() }, "already exisits!");
			e.printStackTrace();
			return showForm(request, response, errors);
		}
		catch (EntityExistsException e) { // needed for JPA
			errors.rejectValue("vndrFulfillmentServId", "errors.vendor.fs.exist",
					new Object[] { vendorForm.getVenName() }, "already exisits!");
			e.printStackTrace();
			return showForm(request, response, errors);
		}
		catch (Exception e) {

			errors.rejectValue("vndrFulfillmentServId", "errors.vendor.fs",
					new Object[] { vendorForm.getVenName() }, "an error has occured");
			e.printStackTrace();
			return showForm(request, response, errors);

		}
		if (log.isDebugEnabled()){
			log.debug("Returning........");
		}
		return new ModelAndView("redirect:/oma/AddVendor.html");

	}

	@SuppressWarnings("unchecked")
	protected Object formBackingObject(HttpServletRequest request)
	throws Exception {
		if (log.isDebugEnabled()){
			log.debug("Entering........");
			log.debug("........isFormSubmission=" + isFormSubmission(request));
		}
		HttpSession session = request.getSession();
		FulfillmentServiceVendor vendor = new FulfillmentServiceVendor();

		FSVendorForm vendorForm = new FSVendorForm();

		FulfillmentService fulfillmentServiceFromSession = (FulfillmentService) session
		.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION);
		//If fulfillment service is null in session , return an empty form.
		if (fulfillmentServiceFromSession == null) {
			return vendorForm;
		}

		//Set dropdown values in session
		session=setDropdownValuesInSession(session);


		/*
		 * If request has not come through from submission
		 */
		if (!isFormSubmission(request)) {
			Long venFsId = ServletRequestUtils.getLongParameter(request, VENFSID);
			String venName = ServletRequestUtils.getStringParameter(request, VENDOR_NAME);
			String venNum = ServletRequestUtils.getStringParameter(request, VEN_NUM);
			String venID = ServletRequestUtils.getStringParameter(request, VEN_ID);
			String mode = ServletRequestUtils.getStringParameter(request, MODE);
			request.setAttribute(MODE, mode);
			if (log.isDebugEnabled()){
				log.debug("...venFsId from request=" + venFsId);
			}
			// after verify button was clicked
			FSVendorForm form = (FSVendorForm) session.getAttribute(FS_VENDOR_FORM);
			if (venID != null) {
				if (form != null) {
					log.debug("FS_VENDOR_FORM" + form.toString());
					form.setVenName(venName);
					form.setVenNum(venNum);
					form.setVndrId(venID);
					return form;
				}
				else {
					vendorForm.setVenName(venName);
					vendorForm.setVenNum(venNum);
					vendorForm.setVndrId(venID);

				}
				log.debug("........vendorForm= " + vendorForm.toString());
				return vendorForm;
			}
			List<Address> addrListFromDB = null;
			// During edit vendor or save vendor
			if (venFsId != null) {

				/*
				 * If Vendor is present in session then get it from session else
				 * retrieve from database and saving it to session
				 */
				if (StringUtils.isNotBlank(request.getParameter("remove"))
						&& request.getParameter("remove").equals("remove")) {
					log.debug("Returning form object after remove address");
					return session.getAttribute(FS_VENDOR_FORM);
				}

				if (log.isDebugEnabled()){
					log.debug("............Getting vendor from DB=" + venFsId);
				}
				vendor = vendorFulfillmentServiceManager.getVendorFulfillmentServicesByID(venFsId);
				if (log.isDebugEnabled()){
					log.debug("...from DB=" + vendor.getVndr().getVendorId());
				}
				/*
				 * If user has landed first time on the page then get the
				 * address list from DB else get the address list from session.
				 * addedAddress is the parameter which is set when user adds an
				 * address in session or user removes an address.
				 */
				String tempAddress = (String) session.getAttribute(ADDED_ADDRESS);
				if (log.isDebugEnabled()){
					log.debug("tempAddress------------------" + tempAddress);
				}
				if (tempAddress != null && tempAddress.equals("yes")) {
					log.debug("-tempAddress=" + tempAddress);
					ArrayList<Address> addrListFromDBnew = (ArrayList<Address>) session
					.getAttribute("addrList");
					log.debug("address list size=" + addrListFromDBnew.size());

				}
				else {
					log.debug("............Getting addrList from DB");
					addrListFromDB = vendorFulfillmentServiceManager.getAddressForVen(vendor
							.getVendorID(), vendor.getFulfillmentServId());
					log.debug("........Got Address From DB");
					session.setAttribute("addrList", addrListFromDB);

				}
				vendor.setStrCreatedDate(DateUtils.formatDate(vendor.getCreatedDate(), "MM/dd/yyyy"));
				vendor.setStrUpdatedDate(DateUtils.formatDate(vendor.getUpdatedDate(), "MM/dd/yyyy"));
				session.setAttribute(VEN_INFO_FROM_SESSION, vendor);
				vendorFulfillmentServiceManager.lockFulfillmntServiceVendor(venFsId.toString(), getLoggedInUser());
				if (vendor != null) {
					log.debug(".......vendor= " + vendor.toString());
					log.debug("Returning........");

					return populateVendorForm(vendor);
				}
			}
			// If coming from another tab-here return tab
			// Mode= edit
			// Get the vendor Info from Session and populate the Form
			if (!StringUtils.isBlank(mode) && ("fromReturn").equals(mode)) {
				log.debug(".......Coming from another tab");
				request.setAttribute(MODE, EDIT);
				FulfillmentServiceVendor vendorOld = (FulfillmentServiceVendor) session
				.getAttribute(VEN_INFO_FROM_SESSION);
				log.debug(".......Coming from another tab vendor=" + vendorOld.toString());
				return populateVendorForm(vendorOld);
			}
		} 
		if (log.isDebugEnabled()){
			log.debug("Returning.....");
		}
		
		return super.formBackingObject(request);
	}


	/**
	 * @param session
	 * @return void
	 * @Enclosing_Method  setDropdownValuesInSession
	 * @Date Feb 10, 2010 
	 * 
	 * @TODO
	 */
	@SuppressWarnings("unchecked")
	private HttpSession setDropdownValuesInSession(HttpSession session) {
		/*
		 * Retrieving the values for States Dropdown
		 */
		ArrayList<State> statesFromSession = (ArrayList<State>) session.getAttribute(STATES);
		if (null != statesFromSession && !statesFromSession.isEmpty()	) {
			log.debug("............Getting states from session");
			session.setAttribute(STATES, statesFromSession);
		}
		else {
			List<State> states = vendorFulfillmentServiceManager.getStates();
			if (states != null) {
				log.debug("Got States from DB=........" + states.size());
				session.setAttribute(STATES, states);
			}
		}
		ArrayList<InvoiceMethods> invoiceMethodsFromSession = (ArrayList<InvoiceMethods>) session
		.getAttribute("invoiceMethods");
		if (null != invoiceMethodsFromSession && !invoiceMethodsFromSession.isEmpty()) {
			log.debug("............Getting invoiceMethods from session");
			session.setAttribute("invoiceMethods", invoiceMethodsFromSession);
		}
		else {
			List<InvoiceMethods> invoiceMethods = vendorFulfillmentServiceManager
			.getInvoiceMethods();
			if (invoiceMethods != null) {
				log.debug("Got invoiceMethods from DB=........" + invoiceMethods.size());
				session.setAttribute("invoiceMethods", invoiceMethods);
			}
		}
		return session;
	}

	/**
	 * @param vendor
	 * @return
	 * @return FSVendorForm
	 * @Enclosing_Method  populateVendorForm
	 * @Date Feb 9, 2010 
	 * 
	 * @TODO Populate Vendor Form from Vendor Model
	 */
	private FSVendorForm populateVendorForm(FulfillmentServiceVendor vendor) {
		FSVendorForm vendorForm = new FSVendorForm();
		if (vendor.getVndrFulfillmentServId() != null) {
			vendorForm.setVndrFulfillmentServId(vendor.getVndrFulfillmentServId().toString());
		}
		if (vendor.getSafetyInvAmt() != null){
			vendorForm.setSafetyInvAmt(vendor.getSafetyInvAmt().toString());
		}
		vendorForm.setSafetyInvAmtTyp(vendor.getSafetyInvAmtTyp());
		vendorForm.setVndrServLvl(vendor.getVndrServLvl());
		if (StringUtils.isNotBlank(vendor.getIsInvToRcpt()) && vendor.getIsInvToRcpt().equals("Y")){
			vendorForm.setIsInvToRcpt("true");
		}
		else{
			vendorForm.setIsInvToRcpt("false");
		}

		vendorForm.setOverrideDays(Integer.toString(vendor.getOverrideDays()));

		if (vendor.getVendorID() != null){
			vendorForm.setVndrId(vendor.getVendorID().toString());
		}
		if (vendor.getFulfillmentServId() != null){
			vendorForm.setFulfillmentServId(vendor.getFulfillmentServId().toString());
		}
		vendorForm.setStatusCd(vendor.getStatusCd());
		if (log.isDebugEnabled()){
			log.debug("vendor=" + vendor.toString());
		}

		if (vendor.getVndr().getVendorNumber() != null){
			vendorForm.setVenNum(vendor.getVndr().getVendorNumber().toString());
		}
		vendorForm.setVenName(vendor.getVndr().getName());

		vendorForm.setInvoiceMethod(vendor.getInvoiceMethodCode());
		// Expedited Shipping
		if (StringUtils.isNotBlank(vendor.getIsExpeditedShipping()) && vendor.getIsExpeditedShipping().equals("Y")){
			vendorForm.setIsExpeditedShipping(true);
		}
		else{
			vendorForm.setIsExpeditedShipping(false);
		}

		return vendorForm;
	}


	/**
	 * @param request
	 * @param vendorForm
	 * @return
	 * @throws ServletRequestBindingException
	 * @return FulfillmentServiceVendor
	 * @Enclosing_Method  populateVendor
	 * @Date Feb 9, 2010 
	 * 
	 * @TODO Populate Vendor from Vendor Form
	 */
	@SuppressWarnings("unchecked")
	private FulfillmentServiceVendor populateVendor(
			HttpServletRequest request, FSVendorForm vendorForm)
	throws ServletRequestBindingException {

		FulfillmentServiceVendor vendor = new FulfillmentServiceVendor();
		HttpSession session = request.getSession();
		FulfillmentService fulfillmentService = (FulfillmentService) session
		.getAttribute("fulfillmentService");
		long fulfillmentServiceId = 0;
		if (null != fulfillmentService) {
			fulfillmentServiceId = fulfillmentService.getFulfillmentServiceID();
		}
		if (!request.getParameterMap().isEmpty()) {
			Enumeration params = request.getParameterNames();
			while (params.hasMoreElements()) {
				String paramName = (String) params.nextElement();
				String paramValue = request.getParameter(paramName);

			}
		}
		if (StringUtils.isNotBlank(vendorForm.getVndrId())){
			vendor.setVendorID(Long.valueOf(vendorForm.getVndrId()));
		}
		if (StringUtils.isNotBlank(vendorForm.getSafetyInvAmt())){
			vendor.setSafetyInvAmt(Long.valueOf(vendorForm.getSafetyInvAmt()));
		}
		if (StringUtils.isNotBlank(vendorForm.getVndrFulfillmentServId())) {
			vendor.setVndrFulfillmentServId(new Long(vendorForm.getVndrFulfillmentServId()));
		}
		vendor.setSafetyInvAmtTyp(vendorForm.getSafetyInvAmtTyp());
		vendor.setVndrServLvl(vendorForm.getVndrServLvl());
		vendor.setInvoiceMethodCode(vendorForm.getInvoiceMethod());
		if (StringUtils.isNotBlank(vendorForm.getIsInvToRcpt())
				&& vendorForm.getIsInvToRcpt().equals("true")) {
			vendor.setIsInvToRcpt(YES);
			vendor.setOverrideDays(Integer.parseInt(vendorForm.getOverrideDays()));
		}
		else {
			vendor.setIsInvToRcpt("N");
			vendor.setOverrideDays(0);
		}
		vendor.setIsLocked(YES);
		vendor.setLockedBy(getLoggedInUser().getUsername());
		vendor.setFulfillmentServId(new Long(fulfillmentServiceId));
		vendor.setStatusCd(vendorForm.getStatusCd());
		FulfillmentServiceVendor vendorOld = (FulfillmentServiceVendor) session
		.getAttribute(VEN_INFO_FROM_SESSION);
		if (vendorOld != null) {
			vendor.setUpdatedDate(vendorOld.getUpdatedDate())	;
			vendor.setCreatedDate(vendorOld.getCreatedDate());
			vendor.setCreatedBy(vendorOld.getCreatedBy());
			log.debug("vendorOld.getCreatedDate()="+vendorOld.getCreatedDate());
		}
		// Expedited Shipping
		if (vendorForm.getIsExpeditedShipping().equals(true)) {
			vendor.setIsExpeditedShipping(YES);
					}
		else {
			vendor.setIsExpeditedShipping("N");
		}
		setAuditInfo(request, vendor);
		return vendor;
	}

}
