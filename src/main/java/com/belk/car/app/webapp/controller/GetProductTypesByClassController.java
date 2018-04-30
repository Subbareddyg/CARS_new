package com.belk.car.app.webapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.model.ProductType;
import com.belk.car.app.util.JSONUtil;

public class GetProductTypesByClassController extends BaseController  {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		boolean success = false;
		JSONObject json = new JSONObject();
		String classId = request.getParameter("class_id");
		if(StringUtils.isNotBlank(classId)){
			try{
				List<ProductType> prodtypes = this.getCarManager().getProductTypesByClass(Short.parseShort(classId));
				json.put("product_types", JSONUtil.convertProductTypesToJSON(prodtypes));
				success=true;
			}
			catch(Exception ex){}
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		json.putOpt("success", success);
		model.put("jsonObj",json.toString());
		return new ModelAndView(this.getAjaxView(),model);
	}
}