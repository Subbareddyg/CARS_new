package com.belk.car.app.webapp.controller.vendorcatalog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.service.VendorCatalogManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class VendorCatalogForVendorController extends BaseFormController {
	
    private transient final Log log = LogFactory.getLog(VendorCatalogController.class);
    
     private  VendorCatalogManager catalogManager;
    /**
     *
     * @param catalogManager
     */
    public void setCatalogManager(VendorCatalogManager catalogManager) {
        this.catalogManager = catalogManager;
    }
	public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering workflow 'handleRequest' method...");
        }
        
        return new ModelAndView(getSuccessView(), Constants.WORKFLOW_LIST, null);
    }

}
