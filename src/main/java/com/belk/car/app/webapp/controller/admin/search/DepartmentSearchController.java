package com.belk.car.app.webapp.controller.admin.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.appfuse.model.User;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class DepartmentSearchController extends BaseFormController {

	private CarLookupManager carLookupManager;

	/**
	 * @return the carLookupManager
	 */
	public CarLookupManager getCarLookupManager() {
		return carLookupManager;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String userFName = "";
		String userLName = "";
		String emailId = "";
		if (request.getParameter("userFName") != null) {
			userFName = (String) request.getParameter("userFName");
		}
		if (request.getParameter("userLName") != null) {
			userLName = (String) request.getParameter("userLName");
		}
		if (request.getParameter("emailId") != null) {
			emailId = (String) request.getParameter("emailId");
		}

		if (logger.isDebugEnabled())
			logger.debug("Queries for: " + userFName + " : " + userLName
					+ " : " + emailId);

		List<User> users = new ArrayList<User>();
		//users = carUserManager.searchUsers(userFName, userLName, emailId);

		Map<String, Object> context = new HashMap<String, Object>();
		context.put("userfname", userFName);
		context.put("userlname", userLName);
		context.put("emailid", emailId);
		context.put("loggedInUser", getLoggedInUser());
		context.put(Constants.USER_LIST, users);

		return new ModelAndView(this.getSuccessView(), context);

	}

}
