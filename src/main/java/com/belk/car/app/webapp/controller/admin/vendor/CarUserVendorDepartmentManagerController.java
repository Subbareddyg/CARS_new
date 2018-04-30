package com.belk.car.app.webapp.controller.admin.vendor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.webapp.controller.BaseFormController;

public class CarUserVendorDepartmentManagerController extends BaseFormController implements
		Controller {

	private transient final Log log = LogFactory
			.getLog(CarUserVendorDepartmentManagerController.class);
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Long venDeptId = ServletRequestUtils.getLongParameter(request, "venDeptId");
		log.debug("venDeptId:"+venDeptId);
		Long userId = ServletRequestUtils.getLongParameter(request, "id");
		log.debug("userId:"+userId);
		if (venDeptId == null) {
			log.debug("Vendor Department id was null. Redirecting...");
			return new ModelAndView("redirect:dashBoard.html");
		}

		String action = getCurrentAction(request);
		log.debug("action:"+action);
		if ("remove".equals(action)) {
			carManager.deleteVendorDepartment(venDeptId);
			saveMessage(request, getText("user.vendor.department.deleted", request.getLocale()));
		}
	
		return new ModelAndView("redirect:vendorDetails.html?id="+userId);
	}
}