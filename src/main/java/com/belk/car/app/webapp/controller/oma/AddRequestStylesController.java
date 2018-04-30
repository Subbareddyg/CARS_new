/**
 * 
 */
package com.belk.car.app.webapp.controller.oma;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.service.FulfillmentServiceManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.ItemRequestStylesForm;

/**
 * This class acts as a controller for Add style screen which opens as a pop up
 * in Request styles screen.
 * 
 * Initial Project: EC-2009-00092 - FY 10 eCommerce Drop-Ship Phase I
 * 
 * Initial Requirements: BR.015
 * 
 * @author afusy01
 * 
 */
public class AddRequestStylesController extends BaseFormController implements
		Controller, DropShipConstants {

	private FulfillmentServiceManager fulfillmentServiceManager;

	/**
	 * This method executes after the form is submitted.
	 */
	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) {
		ItemRequestStylesForm form = (ItemRequestStylesForm) command;
		Locale locale = request.getLocale();
		List<String> list = new ArrayList<String>();
		try {
			String stylesAdded = this.fulfillmentServiceManager
					.addStyles(form);
			Map<String, Boolean> jsonModel = new HashMap<String, Boolean>();
			if (null != stylesAdded) {
				jsonModel.put(SUCCESS, false);
			} else {
				jsonModel.put(SUCCESS, true);
			}
			JSONObject json = new JSONObject(jsonModel);
			json.accumulate("invalid_ids", stylesAdded);
			request.setAttribute(JSON_OBJ, json);
		} catch (Exception ex) {
			log.error(ITEM_REQUEST_EXCEPTION, ex);
			list.add(getText(ITEMREQUEST_FAILEDSAVE, locale));
		}
		return new ModelAndView(AJAX_RETURN);
	}

	protected Object formBackingObject(HttpServletRequest request) {
		return new ItemRequestStylesForm();
	}

	/**
	 * @param fulfillmentServiceManager
	 *            the fulfillmentServiceManager to set
	 */
	public void setFulfillmentServiceManager(
			FulfillmentServiceManager fulfillmentServiceManager) {
		this.fulfillmentServiceManager = fulfillmentServiceManager;
	}
}
