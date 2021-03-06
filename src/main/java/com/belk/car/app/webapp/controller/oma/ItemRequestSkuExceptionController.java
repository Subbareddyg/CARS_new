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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.oma.VFIRStatus;
import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.service.FulfillmentServiceManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.ItemRequestForm;
import com.belk.car.util.DateUtils;

/**
 * This class acts as a controller for Sku Exception screen 
 * 
 * Initial Project: EC-2009-00092 - FY 10 eCommerce Drop-Ship Phase I
 * 
 * Initial Requirements: BR.015
 * 
 * @author afusy01
 * 
 */
public class ItemRequestSkuExceptionController extends BaseFormController
		implements Controller, DropShipConstants {

	private FulfillmentServiceManager fulfillmentServiceManager;

	/**
	 * 
	 */
	public ItemRequestSkuExceptionController() {
		setSessionForm(true);
	}

	/**
	 * This method executes after the form is submitted.
	 */
	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) {
		ItemRequestForm form = (ItemRequestForm) command;
		int pageNo = form.getPageNo();
		HttpSession session = request.getSession(false);
		Locale locale = request.getLocale();

		setAuditInfo(request, form);
		form.setUser(getLoggedInUser());

		/** Setting the status of item request */
		String status = request.getParameter(STATUS_OF_REQUEST);

		form.setRequestStatus(status);
		
		/** Saving skus in VFIR_STYLE_SKU table */
		if (null != request.getParameter(MODE)
				&& request.getParameter(MODE).equalsIgnoreCase(SKU_SAVE)) {
			if (form.getRequestStatus().equalsIgnoreCase(SAVE_POST)) {
				form.setPostedBy(getLoggedInUser().getUsername());
				form.setPostedDate(getCurrentDate());
			} else {
				form.setPostedBy(null);
				form.setPostedDate(null);
			}
			try {
				this.fulfillmentServiceManager.updateStyleSkus(form);
				form = (ItemRequestForm) this.fulfillmentServiceManager
						.getItemRequestDetails(new Long(form.getRequestId()));
				form.setPageNo(pageNo);
				errors.rejectValue(BLANK, VALID, SAVED_SUCCESSFULLY);
			} catch (Exception e) {
				log.error(ITEM_REQUEST_EXCEPTION, e);
				errors.rejectValue(BLANK, INVALID, getText(
						ITEMREQUEST_FAILEDSAVE, locale));
			}
		}

		session.removeAttribute(IS_POSTED);
		if (null != form.getPostedBy()) {
			session.setAttribute(IS_POSTED, ITEMREQUEST_TRUE);
		}

		session.removeAttribute(IS_REQUEST_APPROVE_REJECT);
		if (form.getRequestStatus().equals(WorkflowStatus.APPROVED)
				|| form.getRequestStatus().equals(VFIRStatus.REJECT)) {
			session.setAttribute(IS_REQUEST_APPROVE_REJECT, ITEMREQUEST_TRUE);
		}
		this.setRole(form);
		session.setAttribute(ITEM_REQUEST_FORM_IN_SESSION, form);
		
		Map<String, ItemRequestForm> map = new HashMap<String, ItemRequestForm>();
		try {
			map.put(ITEM_REQUEST_FORM, form);
			return showForm(request, response, errors, map);
		} catch (Exception e1) {
			log.error(ITEM_REQUEST_EXCEPTION, e1);
		}
		return new ModelAndView(this.getSuccessView(), ITEM_REQUEST_FORM, form);
	}

	/**
	 * This method executes initially when the form is loaded. Here you can set
	 * the initial vales that need to be displayed when form is loaded
	 */
	protected Object formBackingObject(HttpServletRequest request) {
		ItemRequestForm itemRequestForm = new ItemRequestForm();
		HttpSession session = request.getSession(false);

		String requestId = request.getParameter(REQUEST_ID);
		if (null != requestId) {
			session.setAttribute(REQUEST_ID, new Long(requestId));
			if(null == session.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION)) {
				try {
					List<Object> list = this.fulfillmentServiceManager.getFulfillmentServiceVendorInfo(new Long(requestId));
					session.setAttribute(FULFILLMENT_SERVICE_FROM_SESSION, list.get(0));
					session.setAttribute(VEN_INFO_FROM_SESSION, list.get(1));
				} catch (Exception e) {
					log.error(ITEM_REQUEST_EXCEPTION, e);
					itemRequestForm.setError(true);
				}
			}
		}

		if(null != request.getParameter(PAGINATION) && null != session.getAttribute(ITEM_REQUEST_FORM_IN_SESSION)) {
			int pageNo = new Integer(request.getParameter("d-448780-p"));
			itemRequestForm = (ItemRequestForm) session.getAttribute(ITEM_REQUEST_FORM_IN_SESSION);
			itemRequestForm.setPageNo(pageNo);
			session.setAttribute(ITEM_REQUEST_FORM_IN_SESSION, itemRequestForm);
			return itemRequestForm;
		}
		
		if (null != request.getParameter(MODE)
				&& request.getParameter(MODE).equalsIgnoreCase(REMOVE_SKU)) {
			try {
				this.fulfillmentServiceManager.removeStyleSku(request
						.getParameter(SKU_REMOVE_REQUESTID), request
						.getParameter(SKU_REMOVE_STYLEID), request
						.getParameter(SKU_REMOVE_COLOR), request
						.getParameter(SKU_REMOVE_SIZE), request
						.getParameter(SKU_REMOVE_UPC));
				// ++Take the value of request id to be edited from session
				Long id = (Long) session.getAttribute(REQUEST_ID);
				itemRequestForm = (ItemRequestForm) fulfillmentServiceManager
						.getItemRequestDetails(id);
			} catch (Exception e) {
				log.error(ITEM_REQUEST_EXCEPTION, e);
				itemRequestForm.setError(true);
			}
		} else if (null != request.getParameter(MODE)
				&& request.getParameter(MODE).equalsIgnoreCase(SKU_SAVE)) {
			this.setRole(itemRequestForm);
			session.setAttribute(ITEM_REQUEST_FORM_IN_SESSION, itemRequestForm);
			return itemRequestForm;
		} else {
			try {
				// ++Take the value of request id to be edited from session
				Long id = (Long) session.getAttribute(REQUEST_ID);
				if(id==null){
					return new ModelAndView("redirect:/mainMenu.html");
				}
				itemRequestForm = (ItemRequestForm) fulfillmentServiceManager
						.getItemRequestDetails(id);
				session.setAttribute(ITEM_REQUEST_FORM_IN_SESSION, itemRequestForm);
			} catch (Exception exception) {
				log.error(ITEM_REQUEST_EXCEPTION, exception);
				itemRequestForm.setError(true);
			}
		}

		session.removeAttribute(IS_POSTED);
		if (null != itemRequestForm.getPostedBy()) {
			session.setAttribute(IS_POSTED, ITEMREQUEST_TRUE);
		}
		session.removeAttribute(IS_REQUEST_APPROVE_REJECT);
		if (null != itemRequestForm.getRequestStatus() && (itemRequestForm.getRequestStatus().equals(WorkflowStatus.APPROVED)
				|| itemRequestForm.getRequestStatus().equals(
						VFIRStatus.REJECT))) {
			session.setAttribute(IS_REQUEST_APPROVE_REJECT, ITEMREQUEST_TRUE);
		}
		this.setRole(itemRequestForm);
		session.setAttribute(ITEM_REQUEST_FORM_IN_SESSION, itemRequestForm);
		return itemRequestForm;
	}

	/**
	 * This method sets the values for
	 * createdBy,createdDate,updatedBy,updatedDate
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param form
	 *            ItemRequestForm
	 */
	public void setAuditInfo(HttpServletRequest request, ItemRequestForm form) {
		String user = getLoggedInUser().getUsername();
		if (user == null) {
			user = UNKNOWN_USER;
		}
		form.setCreatedBy(user);
		form.setCreatedDate(getCurrentDate());
		form.setUpdatedBy(user);
		form.setUpdatedDate(getCurrentDate());
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

	private void setRole(ItemRequestForm form) {
		User user = getLoggedInUser();
		if(user.isOrderMgmtAdmin()) {
			form.setApproveRole("readwriteapprove");
			form.setRole("readwrite");
		} else if (user.isBuyer()) {
			form.setRole("readwrite");
		} else if ( user.isOrderMgmtUser()) {
			form.setRole("read");
		}
	}
}
