package com.belk.car.app.webapp.controller.admin.producttype;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.service.ProductManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class ProductTypeController extends BaseFormController {
	
    private transient final Log log = LogFactory.getLog(ProductTypeController.class);
    
    private ProductManager productManager;
    
    /**
	 * @param productManager the productManager to set
	 */
	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}


	public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering products 'handleRequest' method...");
        }
        
        return new ModelAndView(getSuccessView(), Constants.PRODUCT_LIST, productManager.getAllProductTypes());
    }

}
