package com.belk.car.app.webapp.controller.admin.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.model.Status;
import com.belk.car.app.service.CarUserManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class UserManagerController extends BaseFormController implements
		Controller {

	private transient final Log log = LogFactory
			.getLog(UserManagerController.class);
	
	private CarUserManager carUserManager;
	

	public CarUserManager getCarUserManager() {
		return carUserManager;
	}


	public void setCarUserManager(CarUserManager carUserManager) {
		this.carUserManager = carUserManager;
	}


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Long userId = ServletRequestUtils.getLongParameter(request, "id");
		if (userId == null) {
			log.debug("User id was null. Redirecting...");
			return new ModelAndView("redirect:dashBoard.html");
		}

		String action = getCurrentAction(request);

		if (userId != null && "remove".equals(action)) {
			User user = getUserManager().getUser(request.getParameter("id"));
			//getUserManager().removeUser(user.getId().toString());
			// Rewrite appfuse removeUser method to change the status to 'DELETED' in place of delete user.
			user.setStatusCd(Status.DELETED);
			carUserManager.updateVendorOrUser(user);
			saveMessage(request, getText("user.deleted", user.getFullName(),
					request.getLocale()));
		}
		return new ModelAndView("redirect:/admin/user/users.html");
	}

}
