/**
 * 
 */
package com.belk.car.app.webapp.controller.oma;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.appfuse.model.User;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.service.FulfillmentServiceManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.RequestHistoryForm;
import com.belk.car.util.DateUtils;

/**
 * Purpose : Controller for Request history screen in order management administration.
 * 
 * Initial Project: EC-2009-00092 - FY 10 eCommerce Drop-Ship Phase I
 * 
 * Initial Requirements: BR.015
 * 
 * @author afusy01
 * 
 */
public class RequestHistoryController extends BaseFormController implements
		Controller, DropShipConstants {

	private FulfillmentServiceManager fulfillmentServiceManager;

	/**
	 * 
	 */
	public RequestHistoryController() {
		setSessionForm(true);
	}

	/**
	 * @param fulfillmentServiceManager
	 *            the fulfillmentServiceManager to set
	 */
	public void setFulfillmentServiceManager(
			FulfillmentServiceManager fulfillmentServiceManager) {
		this.fulfillmentServiceManager = fulfillmentServiceManager;
	}

	/**
	 * @return the fulfillmentServiceManager
	 */
	public FulfillmentServiceManager getFulfillmentServiceManager() {
		return fulfillmentServiceManager;
	}

	protected Object formBackingObject(HttpServletRequest request) {
		if(null != request.getParameter(PAGINATION) && null != request.getSession(false).getAttribute(REQUEST_HISTORY_FORM_IN_SESSION)) {
			return request.getSession(false).getAttribute(REQUEST_HISTORY_FORM_IN_SESSION);
		} else {
			request.getSession(false).removeAttribute(REQUEST_HISTORY_FORM_IN_SESSION);
		}
		RequestHistoryForm requestHistoryForm = new RequestHistoryForm();

		HttpSession session = request.getSession(false);
		FulfillmentService fulfillmentService = (FulfillmentService) session
				.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION);
		FulfillmentServiceVendor vendorfsModel = (FulfillmentServiceVendor) session
				.getAttribute(VEN_INFO_FROM_SESSION);

		/**Handling the session-timeout, redirect to main page*/
		if (null == fulfillmentService || null == vendorfsModel) {
			setFormView("redirect:/mainMenu.html");
			return requestHistoryForm;
		} else {
			setFormView("/oma/fulfillmentServiceRequestHistory");
		}

		Long venID = vendorfsModel.getVendorID();
		Long serviceId = fulfillmentService.getFulfillmentServiceID();

		requestHistoryForm.setVendorNo(vendorfsModel.getVenNum());
		requestHistoryForm.setVendorName(vendorfsModel.getVenName());
		requestHistoryForm.setServiceId(serviceId.toString());
		requestHistoryForm.setServiceName(fulfillmentService
				.getFulfillmentServiceName());
		requestHistoryForm.setCreatedBy(vendorfsModel.getCreatedBy());
		requestHistoryForm.setDateCreated(getFormattedDate(vendorfsModel
				.getCreatedDate()));
		requestHistoryForm.setUpdatedBy(vendorfsModel.getUpdatedBy());
		requestHistoryForm.setUpdatedDate(getFormattedDate(vendorfsModel
				.getUpdatedDate()));

		requestHistoryForm.setRequestId(BLANK);
		requestHistoryForm.setRequestDesc(BLANK);
		requestHistoryForm.setEffectiveEndDate(BLANK);
		requestHistoryForm.setEffectiveStartDate(BLANK);
		requestHistoryForm.setStyleId(BLANK);
		requestHistoryForm.setStyleDesc(BLANK);
		requestHistoryForm.setVendorUpc(BLANK);
		requestHistoryForm.setBelkUpc(BLANK);
		requestHistoryForm.setStatusFilter(BLANK);

		try {
			this.fulfillmentServiceManager.getRequestHistoryDetails(
					requestHistoryForm, venID, serviceId);
		} catch (Exception e) {
			log.error(ITEM_REQUEST_EXCEPTION, e);
			requestHistoryForm.setError(true);
		}
		
		this.setRole(requestHistoryForm);
		return requestHistoryForm;
	}

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) {
		RequestHistoryForm requestHistoryForm = (RequestHistoryForm) command;
		this.setRole(requestHistoryForm);
		
		HttpSession session = request.getSession(false);
		FulfillmentService fulfillmentService = (FulfillmentService) session
				.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION);
		FulfillmentServiceVendor vendorfsModel = (FulfillmentServiceVendor) session
				.getAttribute(VEN_INFO_FROM_SESSION);
		if (null == fulfillmentService || null == vendorfsModel) {
			return new ModelAndView("redirect:/mainMenu.html");
		}
		Long venID = vendorfsModel.getVendorID();
		Long serviceId = fulfillmentService.getFulfillmentServiceID();
		// ++Take vendor id and service id from session
		try {
			this.fulfillmentServiceManager.getRequestHistoryDetails(
					requestHistoryForm, venID, serviceId);
		} catch (Exception e) {
			log.error(ITEM_REQUEST_EXCEPTION, e);
			requestHistoryForm.setError(true);
		}
		request.getSession(false).setAttribute(REQUEST_HISTORY_FORM_IN_SESSION, requestHistoryForm);
		return new ModelAndView(this.getSuccessView(), REQUEST_HISTORY_FORM,
				requestHistoryForm);
	}
	
	@Override
	public ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		RequestHistoryForm form = (RequestHistoryForm) command;
		if(errors.getErrorCount() > 0) {
			form.setRequestDetails(null);
		}
		return super.processFormSubmission(request, response, command, errors);
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
	
	private void setRole(RequestHistoryForm form) {
		User user = getLoggedInUser();
		// commented as part of CARS Dropship 2012 allow only admin to edit
		//if (user.isBuyer() || user.isOrderMgmtAdmin() || user.isOrderMgmtUser()) {
		if (user.isBuyer() || user.isOrderMgmtAdmin()) {
			form.setRole("readwrite");
		} else {
			form.setRole("read");
		}
	}
}
