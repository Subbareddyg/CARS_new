/**
 * Class Name : VendorFeesFormController.java
 * 
 * Version Information : v1.0
 * 
 * Date : 12/30/09
 * 
 * Copyright Notice : All rights are reserved to Syntel.
 */

package com.belk.car.app.webapp.controller.oma;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.model.oma.VendorFee;
import com.belk.car.app.model.oma.VendorFeeRequest;
import com.belk.car.app.model.oma.VendorFulfillmentServiceFee;
import com.belk.car.app.service.VendorFeesManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.VendorFeesForm;

/**
 * Form Controller to save the Fees created.
 * @author afusy13
 *
 */
public class VendorFeesFormController extends BaseFormController
		implements
			Controller,
			DropShipConstants {
	private VendorFeesManager vendorFeesManager;
	private FulfillmentService fulfillmentService = null;
	private FulfillmentServiceVendor vendorFulfillmentService = null;

	private transient final Log log = LogFactory.getLog(VendorFeesController.class);

	/*
	 * Sets the instance ContactsManager in the Context at the loading of the
	 * application
	 */
	public void setVendorFeesManager(VendorFeesManager vendorFeesManager) {
		this.vendorFeesManager = vendorFeesManager;
	}

	public VendorFeesFormController() {
		setCommandName("vendorFeesForm");
		setCommandClass(VendorFeesForm.class);
		setSessionForm(true);
	}

	/**
	 * Method to check the submit type of the form
	 */
	public ModelAndView processFormSubmission(
			HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors)
			throws Exception {
		if(log.isDebugEnabled()){
			log.debug("Inside processFormSubmission() method..");
		}
		if (request.getParameter("cancel") != null) {
			Map<String, Object> model = new HashMap<String, Object>();
			return new ModelAndView("/oma/fulfillmentServiceContacts", model);
		}

		return super.processFormSubmission(request, response, command, errors);
	}
	/**
	 * Method to save the Vendor Fees.
	 */
	public ModelAndView onSubmit(
			HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Inside onSubmit() method..");
		}
		VendorFeesForm vendorFeesForm = (VendorFeesForm) command;
		// Get the data from form
		VendorFeeRequest vendorFeeRequestModel = vendorFeesForm.getVendorFeeRequestModel();
		List<VendorFee> formVendorFeeModelList = (List<VendorFee>) vendorFeesForm.getVendorFees();
		List<VendorFee> vendorFeeModelList = new ArrayList<VendorFee>();
		List<VendorFeeRequest> vendorFeeRequestList = new ArrayList<VendorFeeRequest>();

		// Get the data from session
		HttpSession session = request.getSession();
		getDataFromSession(request, response);
		//Check whether the user session is timed out 
		if(fulfillmentService == null){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		// Vendor fee request save or update first
		// then use that object and set it in vndr_fee_req along with vendor_fee
		// and save it.
		try {
			// Set the Effective Date in proper format
			setEffectiveDateInModel(vendorFeesForm, vendorFeeRequestModel);
			// Saving the vendorFeeRequestModel
			vendorFeeRequestModel = (VendorFeeRequest) vendorFeesManager
					.saveOrUpdateVendorFeeRequestModel(vendorFeeRequestModel);
			if (log.isDebugEnabled()) {
				log.debug("vendorFeeRequestModel is saved with Id : "
						+ vendorFeeRequestModel.getVendorFeeRequestId());
			}
			int index = -1;
			if (log.isInfoEnabled()  ) {
				log.info("Size of the vendorFeeModelList is :" + formVendorFeeModelList.size());
			}
			for (VendorFee vendorFeeModel : formVendorFeeModelList) {
				index++;
				// set the fees in vendorfeeModel list.
				if (vendorFeeModel.getFee() != null) {
					log.debug("Vendor Fee ID at index: " + index + " is: "+ vendorFeeModel.getFee().getFeeId());
					// Getting the Fee model by fee Id from database, as other
					// entries of Fee model in vendorFeeModel are getting null.
					vendorFeeModel.setFee(vendorFeesManager.getFeeModel(vendorFeeModel.getFee()
							.getFeeId()));
					if(vendorFeeModel.getFee().getFeeDesc().equalsIgnoreCase(TAX_FEES) 
							|| vendorFeeModel.getFee().getFeeDesc().equalsIgnoreCase(SHIPPING_FEES)
							|| vendorFeeModel.getFee().getFeeDesc().equalsIgnoreCase(INSURANCE_FEES)){
						vendorFeeModel.setPerOrderAmount(00.00);
						vendorFeeModel.setPerItemAmount(00.00);
					}
					vendorFeeModel.setAuditInfo(getLoggedInUser());
					// Add the VendorFeeModel in list.
					vendorFeeModelList.add(vendorFeeModel);
				}
			}
			//Save the Vendor Fee Model List.
			vendorFeeModelList = vendorFeesManager.saveVendorFeeModelList(vendorFeeModelList);
			List<VendorFulfillmentServiceFee> vendorFeeReqModelList = new ArrayList<VendorFulfillmentServiceFee>();
			List<VendorFulfillmentServiceFee> vendorFeeReqIntModelList = null;

			Vendor v = new Vendor();
			// Get the Vendor model
			v = vendorFeesManager.getVendor(vendorFulfillmentService.getVendorID());

			index = 0;
			log.debug("vendorFeeModelList.size() :" + vendorFeeModelList.size());
			// Creating the intermediate list
			while (vendorFeeModelList.size() > index) {
				// Creating a blank list for intermediate table, by passing the
				// Vendor and Fulfillment Service
				vendorFeeReqModelList.add(new VendorFulfillmentServiceFee(v, fulfillmentService));
				index++;
			}

			// Set the remaining entries (Vendor FeeModel and vendor Fee Request
			// Model objects saved above),to the list
			index = -1;
			for (VendorFulfillmentServiceFee venderFeeReqModel : vendorFeeReqModelList) {
				index++;
				// Set the same Vendor Fee Request Model to all, as they belong
				// to only one Fee Request.
				venderFeeReqModel.getCompositeKeyVendorFees().setVendorFeeRequest(
						vendorFeeRequestModel);
				// Set the Vendor Fee Models one by one.
				venderFeeReqModel.getCompositeKeyVendorFees().setVendorFee(
						vendorFeeModelList.get(index));
				// Set the User information.
				venderFeeReqModel.setAuditInfo(getLoggedInUser());
			}
			if (log.isDebugEnabled()) {
				log.debug("Size of vendorFeeReqModelList: " + vendorFeeReqModelList.size());
			}
			vendorFeeReqModelList = vendorFeesManager
					.saveVendorFeeReqModelList(vendorFeeReqModelList);
			// Set the Successful message in the errors.
			errors.rejectValue("", "VALID", SAVED_SUCCESSFULLY_MESSAGE);

			// Get the Intermediate table from db. This will sure to contain the
			// currently saved data as well
			vendorFeeReqIntModelList = vendorFeesManager.getVendorFeeRequestIntList(
					fulfillmentService.getFulfillmentServiceID(), vendorFulfillmentService
							.getVendorID());

			// If the currently saved fee Request is effective from today
			// itself, then update the current Fee Id in session
			Date dt = new Date();
			if (dt.compareTo(vendorFeeRequestModel.getEffectiveDate()) >= 0) {
				// set the Vendor Fee Request id in session for the logic of
				// Current column(Yes/No).
				session.setAttribute("currentVendorFeeRequestId", vendorFeeRequestModel
						.getVendorFeeRequestId());
			}
			vendorFeeRequestList.clear();
			// Populate the Vendor Fee Request model list.
			vendorFeeRequestList = vendorFeesManager.populateVendorFeeRequestList(
					vendorFeeReqIntModelList, vendorFeeRequestList);
			// Remove the existing the intermediate table
			// list(vendorFeeReqIntModelList)from session.
			session.removeAttribute("vendorFeeReqIntModelList");
		}
		catch (Exception e) {
			// e.printStackTrace();
			log.error("Error", e);
			errors.rejectValue("", "INVALID", "An Error is occured, try saving again.");
		}
		// Set the attributes required for rendering the form.
		request.setAttribute("venderFeeRequestList", vendorFeeRequestList);
		request.setAttribute("vendorFeeModel", formVendorFeeModelList);
		request.setAttribute("vendorFeesForm", vendorFeesForm);
		request.setAttribute("isEditMode", true);
		return showForm(request, response, errors);
	}

	/**
	 * Sets the Effective date in Vendor Fee Request Model in required format.
	 * 
	 * @param vendorFeesForm {@link VendorFeesForm} Form for Vendor Fees.
	 * @param vendorFeeRequestModel {@link VendorFeeRequest} Vendor Fee Request
	 *            Model to which the effective date is to be set in proper
	 *            format.
	 */
	private void setEffectiveDateInModel(
			VendorFeesForm vendorFeesForm, VendorFeeRequest vendorFeeRequestModel) {
		// Get the effective date from form.
		String formEffectiveDate = vendorFeesForm.getFormEffectiveDate();
		// Get the date fields separated.
		String[] date_fields = formEffectiveDate.split("/");
		if (log.isDebugEnabled()) {
			log.debug("Effective Date from form is :" + formEffectiveDate);
		}
		// Get the Calendar instance.
		Calendar cal = Calendar.getInstance();
		// Clear all the fields to prevent any garbage data.
		cal.clear();
		vendorFeeRequestModel.setAuditInfo(getLoggedInUser());
		// Set the updated date from Vendor Fee request Model.
		cal.setTime(vendorFeeRequestModel.getUpdatedDate());
		cal.set(Integer.parseInt(date_fields[2]), (Integer.parseInt(date_fields[0]) - 1), Integer
				.parseInt(date_fields[1]));
		if (log.isDebugEnabled()) {
			log.debug("Calendar Time : " + cal);
		}
		if (vendorFeeRequestModel != null) {
			// Set the Calendar time into Date object, as only Date object can
			// be set.
			Date date = cal.getTime();
			// Set the effective Date.
			vendorFeeRequestModel.setEffectiveDate(date);
			log.debug("Effective Date is Set," + vendorFeeRequestModel.getEffectiveDate());
		}
		else {
			log.error("vendorFeeRequestModel is null");
		}
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
		if (null != session.getAttribute("fulfillmentService")) {
			fulfillmentService = (FulfillmentService) session.getAttribute("fulfillmentService");
			if (log.isDebugEnabled()) {
				log.debug("Got the fulfillmentService Object");
			}
		}
		else {
			fulfillmentService = null;
		}
		if (null != session.getAttribute("vndrFulfillmentService")) {
			vendorFulfillmentService = (FulfillmentServiceVendor) session
					.getAttribute("vndrFulfillmentService");
			if (fulfillmentService == null) {
				fulfillmentService = vendorFeesManager
						.getFulfillmentService(vendorFulfillmentService.getFulfillmentServId());
				if (log.isDebugEnabled()) {
					log.debug("Got the fulfillmentService Object from database.");
				}
			}
		}
		else {
			vendorFulfillmentService = null;
		}

	}

}
