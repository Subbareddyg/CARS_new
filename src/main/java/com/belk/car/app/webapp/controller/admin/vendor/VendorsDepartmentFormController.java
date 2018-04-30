/**
 * 
 */
package com.belk.car.app.webapp.controller.admin.vendor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.AccessDeniedException;
import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.model.Department;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.UserDepartmentForm;

/**
 * @author antoniog
 *
 */

public class VendorsDepartmentFormController extends BaseFormController {

	public VendorsDepartmentFormController() {
		setCommandName("userDepartmentForm");
		setCommandClass(UserDepartmentForm.class);
	}

	public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		log.debug("entering 'onSubmit' method...");

		UserDepartmentForm userForm = (UserDepartmentForm) command;
		Set<Department> departments = userForm.getDepartments();
		List<String> deptList = new ArrayList<String>();
		for (Department dept : departments) {
			deptList.add(String.valueOf(dept.getDeptId()));
		}

		if (!deptList.isEmpty()) {
			for (String deptId : deptList) {
				userForm.getUser().addDepartment(this.getCarLookupManager().getDepartmentById(Long.valueOf(deptId)));
			}
		}

		try {
			this.getCarManager().saveUser(userForm.getUser());
		} catch (AccessDeniedException ade) {
			// thrown by UserSecurityAdvice configured in aop:advisor userManagerSecurity
			if (log.isWarnEnabled())
				log.warn(ade.getMessage());
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}

		return new ModelAndView("redirect:vendorDetails.html?id=" + userForm.getUser().getId());
	}

	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws ServletRequestBindingException {

		UserDepartmentForm userForm = (UserDepartmentForm) command;

		Set<Department> filteredDepts = userForm.getUser().getDepartments();
		//I don't think we need this code...
		//for (Department dept : userForm.getUser().getDepartments()) {
		//	filteredDepts.add(dept);
		//}

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("userDepartments", filteredDepts);
		model.put("departments", this.getCarLookupManager().getAllDepartments());

		return model;
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {

		String userId = request.getParameter("id");
		UserDepartmentForm userForm = new UserDepartmentForm();

		User user = null;
		if (StringUtils.isBlank(userId) && !isAddDepartment(request)) {
			user = getUserManager().getUserByUsername(request.getRemoteUser());
		} else if (StringUtils.isNotBlank(userId)) {
			user = getUserManager().getUser(userId);
		} else {
			user = new User();
		}

		if (user != null) {
			userForm.setUser(user);
		}

		return userForm;

	}

	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.registerCustomEditor(Set.class, "departments", new CustomCollectionEditor(Set.class) {
			protected Object convertElement(Object element) {
				Long departmentId = null;

				if (element instanceof Long)
					departmentId = (Long) element;
				else if (element instanceof String)
					departmentId = Long.valueOf((String) element);

				return getCarLookupManager().getDepartmentById(departmentId);
			}
		});
	}

	protected boolean isAddDepartment(HttpServletRequest request) {
		return "addDepartment".equalsIgnoreCase(request.getParameter("method"));
	}
}
