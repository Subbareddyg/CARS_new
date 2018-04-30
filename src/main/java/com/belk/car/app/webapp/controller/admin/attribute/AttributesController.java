package com.belk.car.app.webapp.controller.admin.attribute;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.service.AttributeManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class AttributesController extends BaseFormController {
	
    private transient final Log log = LogFactory.getLog(AttributesController.class);
    
    private AttributeManager attributeManager;
    
    
    
    public void setAttributeManager(AttributeManager attributeManager) {
		this.attributeManager = attributeManager;
	}

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering attributes 'handleRequest' method...");
        }
        
        //return new ModelAndView(getSuccessView(), Constants.ATTR_LIST, attributeManager.getAllAttributes());
        Map<String, Object> model = new HashMap<String, Object>();
        User loggedUser = getLoggedInUser();
        boolean isDataGovernanceAdmin = loggedUser.isDataGovernanceAdmin();			
        System.out.println("isDataGovernanceAdmin===>"+isDataGovernanceAdmin);
        model.put("isDataGovernanceAdmin", isDataGovernanceAdmin);
		model.put(Constants.ATTR_LIST,attributeManager.getAllAttributes());
		return new ModelAndView(this.getSuccessView(), model);
        
    }

}
