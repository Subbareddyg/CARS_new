package com.belk.car.app.webapp.controller;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.belk.car.app.util.DispalyVendorImageUtil;


public class VendorImageLinkJsonController extends AbstractController {
	
	private transient final Log log = LogFactory.getLog(VendorImageLinkJsonController.class);
	String token;
	String strKeyToken = "strMCIImageToken";
	String strKeyValue;
	
	public ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		
		JSONObject json = new JSONObject();
		String imageName=request.getParameter("imageName");
	
		if(imageName!=null){
			
			token = DispalyVendorImageUtil.myKey(imageName);	
			if(log.isDebugEnabled()){
				log.debug("got the MC URL for vendor image: "+ token);
			}
			json.put(strKeyToken, token);
			strKeyValue = (String) json.get(strKeyToken);
			request.setAttribute("finalUrl", strKeyValue);
			//System.out.println("keyValue----> "+keyValue);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(json.get(strKeyToken));
			out.flush();
		}
		ModelAndView model = new ModelAndView(token);
		return model;			
		
		
		
	}
	
	

}
