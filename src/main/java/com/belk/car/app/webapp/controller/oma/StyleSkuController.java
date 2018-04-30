/**
 * 
 */

package com.belk.car.app.webapp.controller.oma;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.appfuse.model.User;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.dto.StyleSkuDTO;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.service.FulfillmentServiceManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.StylesSkuForm;

/**
 * Purpose : Controller for Style & Sku screen in order management
 * administration. Initial Project: EC-2009-00092 - FY 10 eCommerce Drop-Ship
 * Phase I Initial Requirements: BR.015
 * 
 * @author afusy01
 */
public class StyleSkuController extends BaseFormController implements Controller,DropShipConstants {

	private FulfillmentServiceManager fulfillmentServiceManager;

	/**
	 * 
	 */
	public StyleSkuController() {
		setSessionForm(true);
	}

	/**
	 * @param fulfillmentServiceManager the fulfillmentServiceManager to set
	 */
	public void setFulfillmentServiceManager(FulfillmentServiceManager fulfillmentServiceManager) {
		this.fulfillmentServiceManager = fulfillmentServiceManager;
	}

	/**
	 * @return the fulfillmentServiceManager
	 */
	public FulfillmentServiceManager getFulfillmentServiceManager() {
		return fulfillmentServiceManager;
	}

	private void removeSessionAttributes(HttpSession session) {
		session.removeAttribute(REQUEST_ID);
		session.removeAttribute(SHOW_MSG);
		session.removeAttribute(ITEMREQUEST_MODE);
		session.removeAttribute(IS_POSTED);
		session.removeAttribute(IS_REQUEST_APPROVE_REJECT);
		session.removeAttribute(STYLE_POPULATION_METHODS);
		session.removeAttribute(SOURCE_ITEM);
		session.removeAttribute(ADD_STYLES_ERRORS);
		session.removeAttribute(ITEM_REQUEST_FORM_IN_SESSION);
	}
	protected Object formBackingObject(HttpServletRequest request) {
		StylesSkuForm stylesSkuForm = new StylesSkuForm();
		this.setRole(stylesSkuForm);

		HttpSession session = request.getSession(false);

		if(null != request.getParameter(PAGINATION) && null != session.getAttribute(STYLE_SKU_FORM_IN_SESSION)) {
			return session.getAttribute(STYLE_SKU_FORM_IN_SESSION);
		} else {
			session.removeAttribute(STYLE_SKU_FORM_IN_SESSION);
		}
		/** Removing the request id from session */
		this.removeSessionAttributes(session);

		FulfillmentService fulfillmentService = (FulfillmentService) session
				.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION);
		FulfillmentServiceVendor vendorfsModel = (FulfillmentServiceVendor) session
				.getAttribute(VEN_INFO_FROM_SESSION);

		/**Handling the session-timeout, redirect to main page*/
		if (null == fulfillmentService || null == vendorfsModel) {
			setFormView("redirect:/mainMenu.html"); 
            return stylesSkuForm; 
		} else {
			setFormView("/oma/fulfillmentServiceStyleSku"); 
		}

		Long venID = vendorfsModel.getVendorID();
		Long serviceId = fulfillmentService.getFulfillmentServiceID();

		if (null == request.getParameter(MODE)) {
			stylesSkuForm.setServiceId(serviceId.toString());
			stylesSkuForm.setServiceName(fulfillmentService.getFulfillmentServiceName());
			stylesSkuForm.setVendorName(vendorfsModel.getVenName());
			stylesSkuForm.setVendorNo(vendorfsModel.getVenNum());
			stylesSkuForm.setStyleId(BLANK);
			stylesSkuForm.setStyleDesc(BLANK);
			stylesSkuForm.setDropShipStatus(BLANK);
			stylesSkuForm.setBelkUpc(BLANK);
			stylesSkuForm.setVendorUpc(BLANK);
			stylesSkuForm.setIdbStatus(BLANK);
			stylesSkuForm.setUserDeptsOnly(Boolean.FALSE);
			stylesSkuForm.setSelectedStyle(BLANK);
			// ++Take vendor id and service id from session
			try {
				this.fulfillmentServiceManager.getStyleSkuDetails(stylesSkuForm, venID, serviceId);
			}
			catch (Exception e) {
				log.error(ITEM_REQUEST_EXCEPTION, e);
				stylesSkuForm.setError(true);
			}
		}
		// added code as part of CARS Dropship 2012
		// allow admin to edit and user to view access
		if (getLoggedInUser().isOrderMgmtAdmin()) {
			if (log.isDebugEnabled()){
				log
				.debug("User has role order management admin..set mode to edit");
			}
			session.setAttribute("mode", EDIT);

		}
		else{
			if (log.isDebugEnabled()){
				log.debug("User has role not order management admin..set mode to viewonly");
			}
			session.setAttribute("mode", VIEW_ONLY_MODE);
		}
		return stylesSkuForm;
	}

	public ModelAndView onSubmit(
			HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors) {
		StylesSkuForm stylesSkuForm = (StylesSkuForm) command;
		this.setRole(stylesSkuForm);

		/**
		 * If any styles are selected in the style sku screen prior to the
		 * launching of item request screen, save the styles in session.
		 */
		if (null != request.getParameter(MODE)
				&& request.getParameter(MODE).equalsIgnoreCase(LOAD_ITEM_REQUEST)) {
			this.loadSelectedStylesInSession(request, stylesSkuForm.getList());
			return new ModelAndView("redirect:/oma/addeditItemRequest.html?mode=add");
		}

		HttpSession session = request.getSession(false);
		FulfillmentService fulfillmentService = (FulfillmentService) session
				.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION);
		FulfillmentServiceVendor vendorfsModel = (FulfillmentServiceVendor) session
				.getAttribute(VEN_INFO_FROM_SESSION);
		if(null == fulfillmentService || null== vendorfsModel ){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		Long venID = vendorfsModel.getVendorID();
		Long serviceId = fulfillmentService.getFulfillmentServiceID();

		// ++Take vendor id and service id from session
		try {
			stylesSkuForm.setUser(getLoggedInUser());
			stylesSkuForm.setServiceName(fulfillmentService.getFulfillmentServiceName());
			stylesSkuForm.setVendorName(vendorfsModel.getVenName());
			stylesSkuForm.setVendorNo(vendorfsModel.getVenNum());
			stylesSkuForm.setServiceId(serviceId.toString());
			this.fulfillmentServiceManager.getStyleSkuDetails(stylesSkuForm, venID, serviceId);
		}
		catch (Exception e) {
			log.error(ITEM_REQUEST_EXCEPTION, e);
			stylesSkuForm.setError(true);
		}
		session.setAttribute(STYLE_SKU_FORM_IN_SESSION, stylesSkuForm);
		return new ModelAndView(this.getSuccessView(), STYLES_SKU_FORM, stylesSkuForm);
	}

	/**
	 * This method saves all the styles that are selected on Style sku screen in
	 * the session.
	 * 
	 * @param request
	 * @param list
	 */
	private void loadSelectedStylesInSession(HttpServletRequest request, List<StyleSkuDTO> list) {
		HttpSession session = request.getSession(false);
		List<String> list2 = new ArrayList<String>();
		Iterator<StyleSkuDTO> iterator = list.iterator();

		while (iterator.hasNext()) {
			StyleSkuDTO styleSkuDTO = iterator.next();

			if(null != request.getParameter(CHECK_ALL_STYLES_SELECTED)) {
				list2.add(styleSkuDTO.getStyleId());
			} else if (null != styleSkuDTO.getStyleSelected()
					&& styleSkuDTO.getStyleSelected().equalsIgnoreCase(STYLE_IS_SELECTED)) {
				list2.add(styleSkuDTO.getStyleId());
			}
		}
		if (!list2.isEmpty()) {
			session.setAttribute(STYLES_FROM_STYLE_SKU, list2);
		}
	}

	private void setRole(StylesSkuForm form) {
		User user = getLoggedInUser();
		// commented as part of the CARS Dropship 2012 allow only admin to edit
		//if (user.isBuyer() || user.isOrderMgmtAdmin() || user.isOrderMgmtUser()) {
		if (user.isBuyer() || user.isOrderMgmtAdmin()) {
			form.setRole("readwrite");
		}
		else {
			log.debug("user======" + user.getRoles());
			form.setRole("read");
			
		}
	}
}
