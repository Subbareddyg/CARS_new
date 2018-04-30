package com.belk.car.app.webapp.controller.admin.pattern;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.model.Status;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.VendorStyleSearchCriteria;
import com.belk.car.app.model.VendorStyleType;
import com.belk.car.app.webapp.controller.BaseFormController;

public class PatternSearchController extends BaseFormController {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String vendorStyleName = null;
		String vendorStyleNumber = null;

		if (StringUtils.isNotBlank(request.getParameter("vendorStyleName"))) {
			vendorStyleName = request.getParameter("vendorStyleName");
		}

		if (StringUtils.isNotBlank(request.getParameter("vendorStyleNumber"))) {
			vendorStyleNumber = request.getParameter("vendorStyleNumber");
		}

		if (logger.isDebugEnabled())
			logger.debug("Queries for Pattern Search: " + vendorStyleNumber + " : "
					+ vendorStyleName);
		
		//Set the value to session of future use for RE-SEARCH
		this.saveSearchCriteria(request, "patternSearchCriteria");

		VendorStyleSearchCriteria criteria = new VendorStyleSearchCriteria() ;
		criteria.setVendorStyleName(vendorStyleName);
		criteria.setVendorStyleNumber(vendorStyleNumber);
		criteria.setPatternsOnly(true) ;
		criteria.setStatusCd(Status.ACTIVE);
		
		if(!this.getLoggedInUser().isSuperAdmin()) {
		    //If user is NOT a superAdmin, display only Group-by-Size patterns
			criteria.setPatternType(VendorStyleType.PATTERN_SGBS_VS);
		}
		criteria.setUser(this.getLoggedInUser());
		criteria.setSearchChildVendorStyle(true);
		
		//criteria.set
		List<VendorStyle> vendorStyles = carManager.searchVendorStyle(criteria);

		Map<String, Object> context = new HashMap<String, Object>();
		context.put("vendorStyleName", vendorStyleName);
		context.put("vendorStyleNumber", vendorStyleNumber);
		context.put("loggedInUser", getLoggedInUser());
		context.put("patternList", vendorStyles);

		return new ModelAndView(this.getSuccessView(), context);

	}

}
