/**
 * 
 */
package com.belk.car.app.webapp.controller.admin.producttype;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.model.ProductType;
import com.belk.car.app.service.ProductManager;
import com.belk.car.app.webapp.controller.BaseFormController;

/**
 * @author antoniog
 *
 */
public class ProductTypeDetailsController extends BaseFormController implements Controller {

	private transient final Log log = LogFactory.getLog(ProductTypeDetailsController.class);

	private ProductManager productManager;	

	/**
	 * @param productManager the productManager to set
	 */
	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}



	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
        Long productId = ServletRequestUtils.getLongParameter(request, "id");      
        if (productId == null) {
        	log.debug("Product id was null. Redirecting...");
            return new ModelAndView("redirect:dashBoard.html");
        }
        
        ProductType product = productManager.getProductType(Long.valueOf(productId));
        return new ModelAndView(getSuccessView(), "product", product);
	}

}
