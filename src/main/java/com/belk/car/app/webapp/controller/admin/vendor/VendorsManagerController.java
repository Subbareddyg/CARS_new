package com.belk.car.app.webapp.controller.admin.vendor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.Status;
import com.belk.car.app.service.CarUserManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class VendorsManagerController extends BaseFormController implements
		Controller {

	private transient final Log log = LogFactory
			.getLog(VendorsManagerController.class);
	
	private CarUserManager carUserManager;
	

	/**
	 * @param carUserManager the carUserManager to set
	 */
	public void setCarUserManager(CarUserManager carUserManager) {
		this.carUserManager = carUserManager;
	}



	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		if (log.isDebugEnabled()) {
			log.debug("entering 'handleRequest' method...");
		}
		Long userId = ServletRequestUtils.getLongParameter(request, "id");
		if (userId == null) {
			log.debug("User id was null. Redirecting...");
			return new ModelAndView("redirect:dashBoard.html");
		}

		String action = getCurrentAction(request);

		if (userId != null && "remove".equals(action)) {
			User user = getUserManager().getUser(request.getParameter("id"));
			// carUserManager.removeVendor(user);
			// Rewrite appfuse removeUser method to change the status to 'DELETED' in place of delete vendor.
			user.setStatusCd(Status.DELETED);
			carUserManager.updateVendorOrUser(user);
			saveMessage(request, getText("user.deleted", user.getFullName(),
					request.getLocale()));
		}
		return new ModelAndView("redirect:/vendor/vendors.html");
	}

}
