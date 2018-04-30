package com.belk.car.app.webapp.controller.admin.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.model.Classification;


public class ProductTypeClassificationSearchController extends ClassificationSearchController {

	
	
public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String classificationId = "";
		String classificationName = "";	
		
		//Check that the attribute id is present wo se can redisplay the page
		Long productTypeId = ServletRequestUtils.getLongParameter(request, "productTypeId");
		String action = request.getParameter("action");
	        if (productTypeId == null || StringUtils.isBlank(action)) {
	        	log.error("Attribute id was null. Redirecting...");
	            return new ModelAndView(getFormView());
	        }
		
		if(StringUtils.isNotBlank(request.getParameter("classificationId"))){
			classificationId = (String)request.getParameter("classificationId");
		}
		if(StringUtils.isNotBlank(request.getParameter("classificationName"))){
			classificationName = (String)request.getParameter("classificationName");
		}
		
		if (logger.isDebugEnabled())
			logger.debug("Query for: " + classificationId + " : " + classificationName);

		List<Classification> classifications = new ArrayList<Classification>();
		classifications = getCarLookupManager().searchClassifications(classificationId,classificationName);
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("classProductTypeId", productTypeId);
		model.put("action",action);
		model.put("className", classificationName);
		model.put("classId", classificationId);	
		request.getSession().setAttribute(Constants.CLASSIFICATION_LIST, classifications);
		//model.put(Constants.CLASSIFICATION_LIST, classifications);
		
		return new ModelAndView(getSuccessView(), model);

	}
	
}
