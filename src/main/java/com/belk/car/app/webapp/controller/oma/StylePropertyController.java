/**
 * 
 */
package com.belk.car.app.webapp.controller.oma;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.appfuse.model.User;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.service.FulfillmentServiceManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.StylesSkuForm;

/**
 * Purpose : Controller for Style property screen in order management
 * administration.
 * 
 * Initial Project: EC-2009-00092 - FY 10 eCommerce Drop-Ship Phase I
 * 
 * Initial Requirements: BR.015
 * 
 * @author afusy01
 * 
 */
public class StylePropertyController extends BaseFormController implements
		Controller, DropShipConstants {

	private FulfillmentServiceManager fulfillmentServiceManager;

	/**
	 * @return the fulfillmentServiceManager
	 */
	public FulfillmentServiceManager getFulfillmentServiceManager() {
		return fulfillmentServiceManager;
	}

	/**
	 * @param fulfillmentServiceManager
	 *            the fulfillmentServiceManager to set
	 */
	public void setFulfillmentServiceManager(
			FulfillmentServiceManager fulfillmentServiceManager) {
		this.fulfillmentServiceManager = fulfillmentServiceManager;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) {

		HttpSession session = request.getSession(false);
		
		if(null != request.getParameter(PAGINATION) && null != session.getAttribute(STYLE_SKU_FORM_IN_SESSION)) {
			return new ModelAndView("oma/fulfillmentServiceStyleProperty",
					STYLES_SKU_FORM, session.getAttribute(STYLE_SKU_FORM_IN_SESSION));
		}

		FulfillmentService fulfillmentService = (FulfillmentService) session
				.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION);
		FulfillmentServiceVendor vendorfsModel = (FulfillmentServiceVendor) session
				.getAttribute(VEN_INFO_FROM_SESSION);

		if(null == fulfillmentService || null == vendorfsModel){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		
		Long venID = vendorfsModel.getVendorID();
		Long serviceId = fulfillmentService.getFulfillmentServiceID();

		StylesSkuForm stylesSkuForm = new StylesSkuForm();

		String styleId = request.getParameter(STYLEID_PARAMETER);
		String styleDesc = request.getParameter(STYLE_DESC_PARAMETER);
		String styleStatus = request.getParameter(DSTATUS_PARAMETER);
		String idbStatus = request.getParameter(ISTATUS_PARAMETER);
		String vendorUpc = request.getParameter(UPC_PARAMETER);

		stylesSkuForm.setStyleId(styleId);
		stylesSkuForm.setStyleDesc(styleDesc);
		stylesSkuForm.setDropShipStatus(styleStatus);
		stylesSkuForm.setIdbStatus(idbStatus);
		stylesSkuForm.setVendorName(vendorfsModel.getVenName());
		stylesSkuForm.setVendorNo(vendorfsModel.getVenNum());
		stylesSkuForm.setServiceId(serviceId.toString());
		stylesSkuForm.setServiceName(fulfillmentService
				.getFulfillmentServiceName());

		try {
			this.fulfillmentServiceManager.getSkuDetails(stylesSkuForm, venID,
					serviceId, vendorUpc);
		} catch (Exception e) {
			log.error(ITEM_REQUEST_EXCEPTION, e);
			stylesSkuForm.setError(true);
		}
		
		this.setRole(stylesSkuForm);
		
		session.setAttribute(STYLE_SKU_FORM_IN_SESSION, stylesSkuForm);
		return new ModelAndView("oma/fulfillmentServiceStyleProperty",
				STYLES_SKU_FORM, stylesSkuForm);
	}

	private void setRole(StylesSkuForm form) {
		User user = getLoggedInUser();
		if (user.isBuyer()) {
			form.setRole(USER_BUYER);
		}
	}
}
