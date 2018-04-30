/**
 * Class to control the Item Request Operations.
 */

package com.belk.car.app.webapp.controller.oma;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.appfuse.model.User;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.model.oma.ItemSource;
import com.belk.car.app.model.oma.StylePopulationMethod;
import com.belk.car.app.model.oma.VFIRStatus;
import com.belk.car.app.service.FulfillmentServiceManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.ItemRequestForm;
import com.belk.car.util.DateUtils;

/**
 * Purpose : Controller for Item Request screen in order management
 * administration. Initial Project: EC-2009-00092 - FY 10 eCommerce Drop-Ship
 * Phase I Initial Requirements: BR.015
 * 
 * @author afusy01
 */
public class ItemRequestController extends BaseFormController
implements
Controller,
DropShipConstants {

	private FulfillmentServiceManager fulfillmentServiceManager;

	/**
	 * This method executes after the form is submitted.
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView onSubmit(
			HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors) {
		ItemRequestForm form = (ItemRequestForm) command;
		this.setRole(form);

		HttpSession session = request.getSession(false);
		Locale locale = request.getLocale();

		FulfillmentServiceVendor vendorfsModel = (FulfillmentServiceVendor) session
		.getAttribute(VEN_INFO_FROM_SESSION);
		if(vendorfsModel == null){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		Long vendorId = vendorfsModel.getVendorID();

		if (null != form.getRequestStatus() && form.getRequestStatus().equalsIgnoreCase(REJECT)
				&& (null == form.getRejectReason() || form.getRejectReason().equals(BLANK))) {

			errors.rejectValue(REQUEST_REJECT_REASON, INVALID, getText(ITEMREQUEST_REJECTREASON,
					locale));
			try {
				return showForm(request, response, errors);
			}
			catch (Exception e) {
				log.error(ITEM_REQUEST_EXCEPTION, e);
			}

		}
		/**
		 * This list contains the number of styles which are uploaded
		 * successfully and the no. of styles that failed to upload If the list
		 * contains only one item then the file uploaded is not in required
		 * format.
		 */
		List uploadedStylesList = null;

		/** Processing file if the file is selected on the screen */

		if (null != form.getFile() && form.getFile().length > 0) {
			// ++Take vendor id from session
			try {
				Object returnValue = doFileOperations(form, request, vendorId);
				if (returnValue instanceof String) {
					errors.rejectValue(ITEMREQUEST_FILE, INVALID, returnValue.toString());
					form.setShowTable(HIDE_STYLE_TABLE);
				}
				else {
					uploadedStylesList = (List) returnValue;
					form.setShowTable(SHOW_STYLE_TABLE);
					form.setUploadedStyles((String) uploadedStylesList.get(0));
					form.setFailedStyles((String) uploadedStylesList.get(1));
					if (!uploadedStylesList.get(0).toString().equals(UPLOADED_STYLES_IS_ZERO)) {
						form.setValidStyles(uploadedStylesList.get(2).toString());
					}
				}
			}
			catch (Exception e1) {
				log.error(ITEM_REQUEST_EXCEPTION, e1);
				form.setError(true);
			}
			try {
				return showForm(request, response, errors);
			}
			catch (Exception e) {
				log.error(ITEM_REQUEST_EXCEPTION, e);
			}
		}

		form.setUser(getLoggedInUser());

		String mode = (String) session.getAttribute(ITEMREQUEST_MODE);

		// ++Take the value of fulfillment service and vendor id from session
		try {
			if (form.getStylePopMethodId().equals(StylePopulationMethod.MANUAL)
					&& null != session.getAttribute(STYLES_FROM_STYLE_SKU)) {

				form.setSelectedStyles((List<String>) session.getAttribute(STYLES_FROM_STYLE_SKU));

			}
			Long id = fulfillmentServiceManager.saveItemRequest(form, mode);
			// ++Save the id in session
			session.setAttribute(REQUEST_ID, id);
			session.setAttribute(SHOW_MSG, "yes");
			session.removeAttribute(IS_POSTED);
			if (null != form.getPostedBy()) {
				session.setAttribute(IS_POSTED, ITEMREQUEST_TRUE);
			}
			// Remove the styles selected in style sku from session.
			session.removeAttribute(STYLES_FROM_STYLE_SKU);
		}
		catch (Exception exception) {
			log.error(ITEM_REQUEST_EXCEPTION, exception);
			errors.rejectValue(BLANK, INVALID, getText(ITEMREQUEST_FAILEDSAVE, locale));
			try {
				return showForm(request, response, errors);
			}
			catch (Exception e) {
				log.error(ITEM_REQUEST_EXCEPTION, e);
			}
		}

		if (mode.equalsIgnoreCase(EDIT_ITEMREQUEST)
				|| form.getRequestStatus().equalsIgnoreCase(VFIRStatus.APPROVED)
				|| form.getRequestStatus().equalsIgnoreCase(VFIRStatus.REJECT)
				|| form.getItemAction().equals(REMOVE_STYLES_SKUS_ACTION)) {
			Map map = new HashMap();
			map.put(ERRORS, errors);
			return new ModelAndView("redirect:/oma/addeditItemRequest.html?mode=edit", map);
		}
		return new ModelAndView(this.getSuccessView());
	}

	/**
	 * This method executes initially when the form is loaded. Here you can set
	 * the initial vales that need to be displayed when form is loaded
	 */
	protected Object formBackingObject(HttpServletRequest request) {
		ItemRequestForm itemRequestForm = new ItemRequestForm();

		HttpSession session = request.getSession(false);
		String mode = null;

		String requestId = request.getParameter(REQUEST_ID);
		if (null != requestId) {
			session.setAttribute(REQUEST_ID, new Long(requestId));
			if (null == session.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION)) {
				try {
					List<Object> list = this.fulfillmentServiceManager
					.getFulfillmentServiceVendorInfo(new Long(requestId));
					session.setAttribute(FULFILLMENT_SERVICE_FROM_SESSION, list.get(0));
					session.setAttribute(VEN_INFO_FROM_SESSION, list.get(1));
				}
				catch (Exception e) {
					log.error(ITEM_REQUEST_EXCEPTION, e);
					itemRequestForm.setError(true);
				}
			}
		}

		FulfillmentService fulfillmentService = (FulfillmentService) session
		.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION);
		FulfillmentServiceVendor vendorfsModel = (FulfillmentServiceVendor) session
		.getAttribute(VEN_INFO_FROM_SESSION);

		/**Handling the session-timeout, redirect to main page*/
		if (null == fulfillmentService || null == vendorfsModel) {
			setFormView("redirect:/mainMenu.html");
			return itemRequestForm;
		} else {
			setFormView("/oma/fulfillmentServiceItemRequestProperties");
		}

		Long serviceId = fulfillmentService.getFulfillmentServiceID();

		mode = request.getParameter(ITEMREQUEST_MODE);
		session.setAttribute(ITEMREQUEST_MODE, mode);

		session.setAttribute(IS_POSTED, ITEMREQUEST_FALSE);

		try {
			/**
			 * Setting the values of style population methods and item source
			 * retrieved from master tables into session
			 */
			if (null == session.getAttribute(STYLE_POPULATION_METHODS)) {
				session.setAttribute(STYLE_POPULATION_METHODS, this.fulfillmentServiceManager
						.getStylePopulationMethods());
			}
			if (null == session.getAttribute(SOURCE_ITEM)) {
				session.setAttribute(SOURCE_ITEM, this.fulfillmentServiceManager.getItemSource());
			}

			/*
			 * Setting default values for effective date and item source for
			 * "NEW ITEM REQUEST" screen
			 */
			if (null != request.getParameter(ITEMREQUEST_MODE)
					&& request.getParameter(ITEMREQUEST_MODE).equalsIgnoreCase(ADD_ITEMREQUEST)) {
				itemRequestForm = new ItemRequestForm();
				itemRequestForm.setCreateDate(getCurrentDate());
				itemRequestForm.setSourceId(ItemSource.MANUAL_ENTRY);
				itemRequestForm.setStylePopMethodId(StylePopulationMethod.MANUAL);
				itemRequestForm.setShowTable(HIDE_STYLE_TABLE);

				// ++Take the following values from session
				itemRequestForm.setVendorName(vendorfsModel.getVenName());
				itemRequestForm.setVendorNumber(vendorfsModel.getVenNum());
				itemRequestForm.setServiceId(serviceId.toString());
				itemRequestForm.setServiceName(fulfillmentService.getFulfillmentServiceName());
				itemRequestForm.setCreatedBy(fulfillmentService.getCreatedBy());
				itemRequestForm.setUpdatedBy(fulfillmentService.getUpdatedBy());
				itemRequestForm
				.setCreatedDate(getFormattedDate(fulfillmentService.getCreatedDate()));
				itemRequestForm
				.setUpdatedDate(getFormattedDate(fulfillmentService.getUpdatedDate()));
			}
			else if (null != request.getParameter(ITEMREQUEST_MODE)
					&& request.getParameter(ITEMREQUEST_MODE).equalsIgnoreCase(EDIT_ITEMREQUEST)) {
				/** Setting values for "EDIT ITEM REQUEST" screen */
				// ++Take the value of request id to be edited from session
				Long id = (Long) session.getAttribute(REQUEST_ID);
				itemRequestForm = (ItemRequestForm) fulfillmentServiceManager
				.getItemRequestDetails(id);
				/** COndition to check whether the request is posted */
				session.removeAttribute(IS_POSTED);
				if (null != itemRequestForm.getPostedBy()) {
					session.setAttribute(IS_POSTED, ITEMREQUEST_TRUE);
				}
			}
		}
		catch (Exception exception) {
			log.error(ITEM_REQUEST_EXCEPTION, exception);
			itemRequestForm.setError(true);
		}
		this.setRole(itemRequestForm);
		return itemRequestForm;
	}

	/**
	 * This method executes before the form is submitted. Here you can set the
	 * default values which are not entered by the user on screen before saving
	 * it into table
	 */
	public ModelAndView processFormSubmission(
			HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors)
	throws Exception {
		ItemRequestForm form = (ItemRequestForm) command;
		User user = getLoggedInUser();

		/** Processing file */
		if (null != form.getFile() && form.getFile().length > 0) {
			return super.processFormSubmission(request, response, form, errors);
		}

		/** Setting default value for minimum markup percentage if not entered */

		if (null == form.getMerchandise()) {
			form.setMerchandise(N);
			form.setMinimumMarkup(null);
		}

		/** Setting the status of item request */
		String status = request.getParameter(STATUS_OF_REQUEST);
		form.setRequestStatus(status);

		if (form.getRequestStatus().equalsIgnoreCase(APPROVE)) {
			form.setRequestStatus(VFIRStatus.APPROVED);
			form.setRejectReason(BLANK);
			form.setPostedBy(null);
			form.setPostedDate(null);
		}
		else if (form.getRequestStatus().equalsIgnoreCase(REJECT)) {
			form.setRequestStatus(VFIRStatus.REJECT);
			form.setPostedBy(null);
			form.setPostedDate(null);
			if (null == form.getRejectReason() || form.getRejectReason().equals(BLANK)) {
				return super.processFormSubmission(request, response, form, errors);
			}
		}
		else if (form.getRequestStatus().equalsIgnoreCase(SAVE_POST)) {
			form.setRequestStatus(VFIRStatus.APPROVED);
			form.setPostedBy(user.getUsername());
			form.setPostedDate(getCurrentDate());
			form.setRejectReason(BLANK);
			// request.getSession().setAttribute(IS_POSTED, ITEMREQUEST_TRUE);
		}
		else {
			form.setRequestStatus(VFIRStatus.OPEN);
			form.setPostedBy(null);
			form.setPostedDate(null);
			form.setRejectReason(BLANK);
		}

		/** Setting default value of previous request id if not entered */
		if (!(form.getStylePopMethodId().equals(StylePopulationMethod.PREVIOUS_REQUEST))) {
			form.setLocationName(null);
		}
		/**
		 * Setting values for created date,created by,updated date and updated
		 * by
		 */
		setAuditInfo(request, form);
		return super.processFormSubmission(request, response, form, errors);
	}

	/**
	 * This method gets the current date and formats it in MM/DD/YYYY format
	 * 
	 * @return String
	 */
	private String getCurrentDate() {
		Date date = new Date();
		return DateUtils.formatDate(date);
	}

	/**
	 * This method returns the formatted date.
	 * 
	 * @param date
	 * @return String
	 */
	private String getFormattedDate(Date date) {
		if (null != date) {
			return DateUtils.formatDate(date);
		}
		return null;
	}

	/**
	 * This method checks whether the file to upload is in EXCEL format. If it
	 * is then it validates and uploads the file
	 * 
	 * @param form ItemRequestForm
	 * @param request HttpServletRequest
	 * @return Object
	 */
	@SuppressWarnings("unchecked")
	private Object doFileOperations(ItemRequestForm form, HttpServletRequest request, Long vendorId)
	throws Exception {
		List list = null;
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest
		.getFile(ITEMREQUEST_FILE);

		String errorMessage = null;

		if (!file.getOriginalFilename().toLowerCase().endsWith(ITEMREQUEST_FILE_FORMAT)) {
			errorMessage = "File to be uploaded should be in .xls format";
			return errorMessage;
		}

		/** Upload the file */
		boolean isFileUploaded = this.fulfillmentServiceManager.uploadStylesFile(file);

		/** Condition for file not uploaded */
		if (!isFileUploaded) {
			errorMessage = "There was an error while uploading the file";
			return errorMessage;
		}

		/** Validate the file if it is in required format */
		list = (List) this.fulfillmentServiceManager.validateStyleFile(file, vendorId);

		/** Condition if the file is not in required format */
		if (list.size() == 1) {
			return list.get(0);
		}
		/** Setting the filename if it is in required format */
		form.setFilePath(file.getOriginalFilename());
		return list;
	}

	/**
	 * This method sets the values for
	 * createdBy,createdDate,updatedBy,updatedDate
	 * 
	 * @param request HttpServletRequest
	 * @param form ItemRequestForm
	 */
	public void setAuditInfo(HttpServletRequest request, ItemRequestForm form)
	throws Exception {
		String user = getLoggedInUser().getUsername();
		if (user == null) {
			user = UNKNOWN_USER;
		}
		String mode = (String) request.getSession().getAttribute(ITEMREQUEST_MODE);

		if (mode.equalsIgnoreCase(ADD_ITEMREQUEST)) {
			form.setCreatedBy(user);
			form.setCreatedDate(getCurrentDate());
		}
		form.setUpdatedBy(user);
		form.setUpdatedDate(getCurrentDate());
	}

	/**
	 * @param fulfillmentServiceManager the fulfillmentServiceManager to set
	 */
	public void setFulfillmentServiceManager(FulfillmentServiceManager fulfillmentServiceManager) {
		this.fulfillmentServiceManager = fulfillmentServiceManager;
	}

	private void setRole(ItemRequestForm form) {
		User user = getLoggedInUser();
		
		if(user.isOrderMgmtAdmin()) {
			form.setRole("readwriteapprove");
		} else if (user.isBuyer()) {
			form.setRole("readwrite");
		} else if ( user.isOrderMgmtUser()) {
			form.setRole("read");
		}
	}
}
