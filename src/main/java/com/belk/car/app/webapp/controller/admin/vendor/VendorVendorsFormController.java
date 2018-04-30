package com.belk.car.app.webapp.controller.admin.vendor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
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

import com.belk.car.app.model.Vendor;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.UserForm;

public class VendorVendorsFormController extends BaseFormController {

	private CarLookupManager carLookupManager;

	private CarManager carManager;

	public void setCarLookupManager(CarLookupManager carLookupManager) {
		this.carLookupManager = carLookupManager;
	}

	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	public VendorVendorsFormController() {
		setCommandName("userForm");
		setCommandClass(UserForm.class);
	}

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		log.debug("entering 'onSubmit' method...");

		UserForm userForm = (UserForm) command;
		Locale locale = request.getLocale();

		Set<Vendor> vendors = userForm.getVendors();
		List vendorList = new ArrayList();
		for (Vendor vend : vendors) {
			vendorList.add(String.valueOf(vend.getVendorId()));
		}

		if (vendorList != null && vendorList.size() > 0) {
			// userForm.getUser().getVendors().clear();
			for (Iterator iter = vendorList.iterator(); iter.hasNext();) {
				String deptId = (String) iter.next();
				Vendor dbVendor = carLookupManager.getVendorById(Long
						.valueOf(deptId));
				userForm.getUser().addVendor(dbVendor);
			}
		}

		try {
			carManager.saveUser(userForm.getUser());
		} catch (AccessDeniedException ade) {
			// thrown by UserSecurityAdvice configured in aop:advisor
			// userManagerSecurity
			log.warn(ade.getMessage());
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}

		return new ModelAndView("redirect:/vendor/vendorDetails.html?id="
				+ userForm.getUser().getId());
	}

	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws ServletRequestBindingException {

		UserForm userForm = (UserForm) command;
		// Convenience Method
		Set<Vendor> filteredVendors = new HashSet<Vendor>();
		for (Vendor vend : userForm.getUser().getVendors()) {
			filteredVendors.add(vend);
		}
		Map model = new HashMap();
		model.put("userVendors", filteredVendors);
		model.put("vendors", carLookupManager.getAllVendors());
		return model;
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

		String userId = request.getParameter("id");
		UserForm userForm = new UserForm();
		User user;
		if (userId == null && !isAssociateVendor(request)) {
			user = getUserManager().getUserByUsername(request.getRemoteUser());
		} else if (!StringUtils.isBlank(userId)) {
			user = getUserManager().getUser(userId);
		} else {
			user = new User();
		}
		userForm.setUser(user);

		return userForm;

	}

	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) {
		binder.registerCustomEditor(Set.class, "vendors",
				new CustomCollectionEditor(Set.class) {
					protected Object convertElement(Object element) {
						Long vendorId = null;
						if (element instanceof Long)
							vendorId = (Long) element;
						else if (element instanceof String)
							vendorId = Long.valueOf((String) element);
						return carLookupManager.getVendorById(vendorId);
					}
				});
	}

	protected boolean isAssociateVendor(HttpServletRequest request) {
		String method = request.getParameter("method");
		return (method != null && method.equalsIgnoreCase("associateVendor"));
	}
}