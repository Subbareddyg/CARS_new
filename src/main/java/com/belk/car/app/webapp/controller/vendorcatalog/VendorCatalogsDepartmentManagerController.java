package com.belk.car.app.webapp.controller.vendorcatalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.Department;
import com.belk.car.app.service.VendorCatalogManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.VendorCatalogForm;

/**
 * Purpose: Form controller for removing departments for vendor catalog
 * Project: EC-2009-00092 - FY 10 eCommerce Drop-Ship Phase I Initial
 * Requirements: BR.002 Description: This is the form controller class for Vendor Catalog 
 * remove department
 * 
 * @author afusya2
 */
public class VendorCatalogsDepartmentManagerController extends
		BaseFormController implements Controller, DropShipConstants {

	private transient final Log log = LogFactory
			.getLog(VendorCatalogsDepartmentManagerController.class);
	private VendorCatalogManager vendorCatalogManager;
	/**
	 * @return the vendorCatalogManager
	 */
	public VendorCatalogManager getVendorCatalogManager() {
		return vendorCatalogManager;
	}
	/**
	 * @param vendorCatalogManager the vendorCatalogManager to set
	 */
	public void setVendorCatalogManager(VendorCatalogManager vendorCatalogManager) {
		this.vendorCatalogManager = vendorCatalogManager;
	}
	
	public VendorCatalogsDepartmentManagerController() {
		setCommandName(VENDOR_CATALOG_FORM);
		setCommandClass(VendorCatalogForm.class);
	}
	@SuppressWarnings("unchecked")
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Long deptId = ServletRequestUtils.getLongParameter(request, "deptId");
		Long userId = ServletRequestUtils.getLongParameter(request, "id");
		if (deptId == null || userId == null) {
			if(log.isDebugEnabled()){
				log.debug("Deparment or user id was null. Redirecting...");	
			}
			return new ModelAndView("redirect:/mainMenu.html");
		}

		String action = getCurrentAction(request);
		HttpSession session = request.getSession(false);
		String mode = request.getParameter("mode");
		if(null != session.getAttribute("mode")){
			mode  = session.getAttribute("mode").toString();
			}else{
				session.setAttribute("mode", mode);
			}
		String vndrCatlId = null;
		vndrCatlId = (String)request.getSession(false)
		.getAttribute("vcID");
		/** for removing departments from vendor catalog set up form */
		if (deptId != null && "remove".equals(action)) {
			User user = getUserManager().getUser(String.valueOf(userId));
			if (user != null) {
				Department dept = getCarLookupManager().getDepartmentById(
						deptId);
				if (dept != null) {
					List<Department> deptUpdatedList = new ArrayList<Department>();
					//declared for getting removed dept list
					List<Department> removedDeptList = new ArrayList<Department>();
					deptUpdatedList = (List) request.getSession(false)
							.getAttribute(DEPARTMENTS);
					
					if(null != removedDeptList && null != session.getAttribute("removedDeptList")){
						//get the removed dept list from session
						removedDeptList = (List) request.getSession(false)
						.getAttribute("removedDeptList");
					}
					if (log.isDebugEnabled()){
						log.debug("dept id = " + dept.getDeptId());
					}
					removedDeptList.add(dept);
					deptUpdatedList.remove(dept);

					session.setAttribute(DEPARTMENTS, deptUpdatedList);
					//save removed departments in session for edit/save functionality
					session.setAttribute("removedDeptList", removedDeptList);
				} else {
					log.error("Department with id " + deptId
							+ "could not be found");
				}
			}
			saveMessage(request, getText("department.deleted", user
					.getFullName(), request.getLocale()));
		}

		Map model = new HashMap();
		model.put("id", userId);
		model.put("mode", mode);
		model.put("vcID", vndrCatlId);
		return new ModelAndView(getSuccessView(), model);
	}

}
