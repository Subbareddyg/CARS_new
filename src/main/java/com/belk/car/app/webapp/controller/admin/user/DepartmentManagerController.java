package com.belk.car.app.webapp.controller.admin.user;

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

import com.belk.car.app.model.Department;
import com.belk.car.app.webapp.controller.BaseFormController;

public class DepartmentManagerController extends BaseFormController implements
		Controller {

	private transient final Log log = LogFactory
			.getLog(DepartmentManagerController.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Long deptId = ServletRequestUtils.getLongParameter(request, "deptId");
		Long userId = ServletRequestUtils.getLongParameter(request, "id");
		if (deptId == null || userId == null) {
			log.debug("Deparment or user id was null. Redirecting...");
			return new ModelAndView("redirect:dashBoard.html");
		}

		String action = getCurrentAction(request);

		if (deptId != null && "remove".equals(action)) {
			User user = getUserManager().getUser(String.valueOf(userId));
			removeUserDepartment(user, deptId);
			getUserManager().saveUser(user);
			saveMessage(request, getText("department.deleted", user
					.getFullName(), request.getLocale()));
		}
		
		Map model = new HashMap();
		model.put("id", userId);
		return new ModelAndView(getSuccessView(),model);
	}

	private void removeUserDepartment(User user, Long deptId) {
		if (user != null) {
			Department dept = getCarLookupManager().getDepartmentById(deptId);
			if (dept != null) {
				user.getDepartments().remove(dept);
			} else {
				log
						.error("Department with id " + deptId
								+ "could not be found");
			}
		}
	}
}
