/**
 * 
 */
package com.belk.car.app.webapp.controller.admin.pattern;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.VendorStyleSearchCriteria;
import com.belk.car.app.webapp.controller.BaseController;

/**
 * @author antoniog
 *
 */
public class PatternDetailController extends BaseController {

	private transient final Log log = LogFactory.getLog(PatternDetailController.class);


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
        Long patternId = ServletRequestUtils.getLongParameter(request, "patternId");      
        if (patternId == null) {
        	log.debug("VendorStyleId was null. Redirecting...");
            return new ModelAndView("redirect:dashBoard.html");
        }
        
        VendorStyle pattern = getCarManager().getVendorStyle(patternId);
        VendorStyleSearchCriteria criteria = new VendorStyleSearchCriteria();
        criteria.setVendorStyleId(patternId) ;
        criteria.setChildrenOnly(true);
        List<VendorStyle> patternProducts = getCarManager().searchVendorStyle(criteria);
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pattern", pattern);
        map.put("patternProducts", patternProducts);
        
        return new ModelAndView("admin/pattern/detail", map);
	}

}
