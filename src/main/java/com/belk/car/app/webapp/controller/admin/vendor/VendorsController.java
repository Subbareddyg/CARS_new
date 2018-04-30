package com.belk.car.app.webapp.controller.admin.vendor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.Constants;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class VendorsController extends BaseFormController implements Controller {

	private transient final Log log = LogFactory.getLog(VendorsController.class);

	private CarLookupManager carLookupManager = null;

	/**
	 * @param carLookupManager the carLookupManager to set
	 */
	public void setCarLookupManager(CarLookupManager carLookupManager) {
		this.carLookupManager = carLookupManager;
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'handleRequest' method...");
		}

		Map<String, Object> model = new HashMap<String, Object>();
		User currentUser = this.getLoggedInUser();

		model.put("loggedInUser", currentUser);
		if (!currentUser.getBuyerRights()) {
			model.put(Constants.Vendor_LIST, new ArrayList<User>());
		} else {
			model.put(Constants.Vendor_LIST, carLookupManager.getVendors());
		}
		return new ModelAndView(getSuccessView(), model);
	}

}
