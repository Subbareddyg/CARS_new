package com.belk.car.app.webapp.controller.admin.vendor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.model.Vendor;
import com.belk.car.app.service.CarUserManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class VendorVendorsManagerController extends BaseFormController implements Controller {

	private transient final Log log = LogFactory.getLog(VendorVendorsManagerController.class);

	private CarUserManager carUserManager;

	/**
	 * @param carUserManager the carUserManager to set
	 */
	public void setCarUserManager(CarUserManager carUserManager) {
		this.carUserManager = carUserManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Long vendorId = ServletRequestUtils.getLongParameter(request, "vendorId");
		Long userId = ServletRequestUtils.getLongParameter(request, "id");

		if (vendorId == null || userId == null) {
			if (log.isDebugEnabled())
				log.debug("Vendor or user id was null. Redirecting...");
			return new ModelAndView("redirect:dashBoard.html");
		}

		String action = getCurrentAction(request);
		if ("remove".equals(action)) {
			User user = getUserManager().getUser(String.valueOf(userId));
			this.removeUserVendor(user, vendorId);
			getUserManager().saveUser(user);
			saveMessage(request, getText("vendor.deleted", user.getFullName(), request.getLocale()));
		}

		return new ModelAndView(getSuccessView(), "id", userId);
	}

	private void removeUserVendor(User user, Long vendorId) {
		if (user != null) {
			Vendor vendor = getCarLookupManager().getVendorById(vendorId);
			if (vendor != null) {
				user.getVendors().remove(vendor);
			} else {
				if (log.isErrorEnabled())
					log.error("Vendor with id " + vendorId + "could not be found");
			}
		}
	}
}