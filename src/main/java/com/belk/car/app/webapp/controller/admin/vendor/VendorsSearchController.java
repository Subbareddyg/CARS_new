package com.belk.car.app.webapp.controller.admin.vendor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.service.CarUserManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class VendorsSearchController extends BaseFormController {

	private CarUserManager carUserManager;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String userFName = null;
		String userLName = null;
		String emailId = null;

		if (StringUtils.isNotBlank(request.getParameter("userFName"))) {
			userFName = (String) request.getParameter("userFName");
		}

		if (StringUtils.isNotBlank(request.getParameter("userLName"))) {
			userLName = (String) request.getParameter("userLName");
		}

		if (StringUtils.isNotBlank(request.getParameter("emailId"))) {
			emailId = (String) request.getParameter("emailId");
		}

		if (logger.isDebugEnabled())
			logger.debug("Queries for: " + userFName + " : " + userLName + " : " + emailId);

		List<User> users = carUserManager.searchVendors(userFName, userLName, emailId);

		Map<String, Object> context = new HashMap<String, Object>();
		context.put("userfname", userFName);
		context.put("userlname", userLName);
		context.put("emailid", emailId);
		context.put("loggedInUser", getLoggedInUser());
		context.put(Constants.USER_LIST, users);

		return new ModelAndView(this.getSuccessView(), context);

	}

	public void setCarUserManager(CarUserManager carUserManager) {
		this.carUserManager = carUserManager;
	}
}
