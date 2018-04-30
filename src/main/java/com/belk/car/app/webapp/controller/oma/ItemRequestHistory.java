/**
 * 
 */
package com.belk.car.app.webapp.controller.oma;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.service.FulfillmentServiceManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.ItemRequestForm;

/**
 * This class acts as a controller for Update history screen
 * 
 * Initial Project: EC-2009-00092 - FY 10 eCommerce Drop-Ship Phase I
 * 
 * Initial Requirements: BR.015
 * 
 * @author afusy01
 * 
 */
public class ItemRequestHistory extends BaseFormController implements
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

	public ModelAndView handleRequest(HttpServletRequest arg0,
			HttpServletResponse arg1) {

		HttpSession session = arg0.getSession(false);
		// ++Take the value from session.
		Long id = (Long) session.getAttribute(REQUEST_ID);
		if(id==null){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		ItemRequestForm itemRequestForm = new ItemRequestForm();
		try {
			itemRequestForm = (ItemRequestForm) this.fulfillmentServiceManager
					.getItemRequestDetails(id);
		} catch (Exception e) {
			log.error(ITEM_REQUEST_EXCEPTION, e);
			itemRequestForm.setError(true);
		}
		return new ModelAndView("oma/fulfillmentServiceUpdateHistory",
				ITEM_REQUEST_FORM, itemRequestForm);
	}

}
