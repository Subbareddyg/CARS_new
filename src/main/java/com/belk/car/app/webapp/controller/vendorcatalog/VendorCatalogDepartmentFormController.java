/**
 * 
 */
package com.belk.car.app.webapp.controller.vendorcatalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.appfuse.model.User;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.Department;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.VendorCatalogDepartmentForm;

/**
 * Purpose: Form controller for adding departments for vendor catalog
 * Project: EC-2009-00092 - FY 10 eCommerce Drop-Ship Phase I Initial
 * Requirements: BR.002 Description: This is the form controller class for Vendor Catalog 
 * add department
 * 
 * @author afusya2
 */
public class VendorCatalogDepartmentFormController extends BaseFormController
		implements DropShipConstants {

	public VendorCatalogDepartmentFormController() {
		setCommandName("vendorCatalogDepartmentForm");
		setCommandClass(VendorCatalogDepartmentForm.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax
	 * .servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * java.lang.Object, org.springframework.validation.BindException)
	 */
	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		if (log.isDebugEnabled()){
			log.debug("entering 'onSubmit' method...");	
		}
		/** gives the selected departments from Add Department page */
		VendorCatalogDepartmentForm userForm = (VendorCatalogDepartmentForm) command;
		HttpSession session = request.getSession(false);
		List<Department> departments = new ArrayList<Department>(userForm
				.getDepartments());
		if (log.isDebugEnabled()){
			log.debug("department List..." + departments.size());
		}
		List<String> deptList = new ArrayList<String>();
		
		String mode = request.getParameter("mode");
		if(null != session.getAttribute("mode")){
			mode  = session.getAttribute("mode").toString();
			}else{
				session.setAttribute("mode", mode);
			}
		String vndrCatlId = null;
		vndrCatlId = (String)request.getSession(false)
		.getAttribute("vcID");
		
		for (Department dept : departments) {
			deptList.add(String.valueOf(dept.getDeptId()));
		}
		
		//declared for getting added dept list
		List<Department> addedDeptList = new ArrayList<Department>();
		if(null != addedDeptList && null != session.getAttribute("addedDeptList")){
			//get the added dept list from session
			addedDeptList = (List) request.getSession(false)
			.getAttribute("addedDeptList");
		}
		if (!deptList.isEmpty()) {
			// userForm.getUser().getDepartments().clear();
			/** gets department ids for selected departments */
			for (String deptId : deptList) {
				userForm.getUser().addDepartment(
						carLookupManager
								.getDepartmentById(Long.valueOf(deptId)));
				addedDeptList.add(carLookupManager.getDepartmentById(Long.valueOf(deptId)));
			}
		}
		List<Department> newDeptList = new ArrayList<Department>(userForm
				.getUser().getDepartments());
		if (log.isDebugEnabled()){
			log.debug("check for dept::"+newDeptList.size());
		}
		/** sets the selected departments in session variable */
		session.setAttribute(SELECTED_DEPT, newDeptList);
		//save added departments in session for edit/save functionality
		session.setAttribute("addedDeptList", addedDeptList);
		long userId = this.getLoggedInUser().getId();
		Map model = new HashMap();
		model.put("id", userId);
		model.put("mode", mode);
		model.put("vcID", vndrCatlId);
		
		/**Edited values from vendor catalog properties page*/
		session.setAttribute("EDITED_CATALOG_NAME", request.getParameter("cname"));
		return new ModelAndView(getSuccessView(), model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.mvc.SimpleFormController#referenceData
	 * (javax.servlet.http.HttpServletRequest, java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws ServletRequestBindingException {
		if (log.isDebugEnabled()){
			log.debug("entering 'referencedData' method...");
		}
		@SuppressWarnings("unused")
		VendorCatalogDepartmentForm userForm = (VendorCatalogDepartmentForm) command;
		HttpSession session = request.getSession(false);
		/** for showing remaining department list in Add Department page */
		List<Department> filteredDepts = new ArrayList<Department>();
		if(null != session.getAttribute(NEW_DEPT_LIST)){
			filteredDepts = (List) request.getSession(false)
			.getAttribute(NEW_DEPT_LIST);
		}
		if (log.isDebugEnabled()){
			log.debug("filtered list..." + filteredDepts.size());
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(USER_DEPARTMENTS, filteredDepts);
		model.put(DEPARTMENTS, this.getCarLookupManager().getAllDepartments());
		return model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject
	 * (javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		if (log.isDebugEnabled()){
			log.debug("entering 'formaBacking' method...");
		}
		String userId = request.getSession().getAttribute("userId").toString();
		if (log.isDebugEnabled()){
			log.debug("logged in user id..." + userId);
		}
		VendorCatalogDepartmentForm userForm = new VendorCatalogDepartmentForm();
		User user;
		if (userId == null && !isAddDepartment(request)) {
			user = getUserManager().getUserByUsername(request.getRemoteUser());
		} else {
			user = new User();
		}
		if (user != null) {
			userForm.setUser(user);
		}
		request.setAttribute("cname1",  request.getParameter("cname"));
		return userForm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.belk.car.app.webapp.controller.BaseFormController#initBinder(javax
	 * .servlet.http.HttpServletRequest,
	 * org.springframework.web.bind.ServletRequestDataBinder)
	 */
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) {
		binder.registerCustomEditor(List.class, DEPARTMENTS,
				new CustomCollectionEditor(List.class) {
					protected Object convertElement(Object element) {
						Long departmentId = null;

						if (element instanceof Long){
							departmentId = (Long) element;
						}else if (element instanceof String){
							departmentId = Long.valueOf((String) element);}
						return getCarLookupManager().getDepartmentById(
								departmentId);
					}
				});
	}

	/**
	 * checks for add department
	 * 
	 * @param request
	 * @return boolean
	 */
	protected boolean isAddDepartment(HttpServletRequest request) {
		String method = request.getParameter(METHOD);
		return (method != null && method.equalsIgnoreCase(ADD_DEPARTMENT));
	}
}
