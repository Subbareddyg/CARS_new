package com.belk.car.app.webapp.controller.admin.producttype;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.service.ProductManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class RemoveProductTypeController extends BaseFormController implements
		Controller {

	private transient final Log log = LogFactory
			.getLog(RemoveProductTypeController.class);

	private ProductManager productManager;
	
	/**
	 * @param productManager the productManager to set
	 */
	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}


	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Long productId = ServletRequestUtils.getLongParameter(request,
				"id");
		if (productId == null) {
			log
					.debug("Product id was null. Redirecting...");
			// TODO - Change to a property view
			return new ModelAndView("redirect:dashBoard.html");
		}

		String action = getCurrentAction(request);

		if ("remove".equals(action)) {
			ProductType product = productManager.getProductType(productId);
			if (product != null) {	
				product.setStatusCd(DropShipConstants.INACTIVE);
				productManager.remove(product);
				saveMessage(request, getText("product.deleted",
						productId+"", request.getLocale()));			
			}
			else {
				saveMessage(request, getText("product.deletion.problem",
						productId+"", request.getLocale()));	
			}
		}
		return new ModelAndView(getSuccessView());

	}

}