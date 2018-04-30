package com.belk.car.app.webapp.controller.admin.producttype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.belk.car.app.Constants;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class ProductTypeSearchController extends BaseFormController {

	private CarLookupManager carLookupManager ;
	
	/**
	 * @param carLookupManager the carLookupManager to set
	 */
	public void setCarLookupManager(CarLookupManager carLookupManager) {
		this.carLookupManager = carLookupManager;
	}
	

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String productTypeName = "";
		String classificationId = "";
		Long classId = null;
	
		if(StringUtils.isNotBlank(request.getParameter("productTypeName"))){
			productTypeName = (String)request.getParameter("productTypeName");
		}
		if(StringUtils.isNotBlank(request.getParameter("classificationId"))){
			classificationId = (String)request.getParameter("classificationId");
			try {
				classId = new Long(classificationId);
			} catch(NumberFormatException nfe) { 
				log.debug(" ProductTypeSearchController ------- > Number format exception");
				return new ModelAndView(getFormView(),"searchFormError", getText("productType.search.number.format.error", request.getLocale()));
				
			}
		}
		
		if (logger.isDebugEnabled())
			logger.debug("Queries for: " + productTypeName + " : " + classificationId);

		//Set the value to session of future use for RE-SEARCH
		this.saveSearchCriteria(request, "productTypeSearchCriteria");

		List<ProductType> pTypes = new ArrayList<ProductType>();
		pTypes = carLookupManager.searchProductTypes(productTypeName, classId);
		
		Map<String, Object> model = new HashMap<String, Object>();
        model.put("productTypeName", productTypeName);
        model.put("classificationId", classificationId);       
        model.put( "productList", pTypes);
		
		return new ModelAndView(this.getSuccessView(), model);

	}

	
}
