/**
 * 
 */
package com.belk.car.app.webapp.controller.admin.attribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.model.Attribute;
import com.belk.car.app.service.AttributeManager;
import com.belk.car.app.webapp.controller.BaseFormController;

/**
 * @author antoniog
 *
 */
public class AttributeDetailsController extends BaseFormController implements Controller {

	private transient final Log log = LogFactory.getLog(AttributeDetailsController.class);

	private AttributeManager attributeManager;
	

	/**
	 * @return the attributeManager
	 */
	protected AttributeManager getAttributeManager() {
		return attributeManager;
	}

	/**
	 * @param attributeManager the attributeManager to set
	 */
	public void setAttributeManager(AttributeManager attributeManager) {
		this.attributeManager = attributeManager;
	}


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
        Long attributeId = ServletRequestUtils.getLongParameter(request, "id");      
        if (attributeId == null) {
        	log.debug("Attribute id was null. Redirecting...");
            return new ModelAndView("redirect:dashBoard.html");
        }
        
        Attribute attribute = getAttributeManager().getAttribute(Long.valueOf(attributeId));     
        return new ModelAndView("admin/attributes/details", "attribute", attribute);
	}

}
