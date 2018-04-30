package com.belk.car.app.webapp.controller.admin.producttype;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.model.Classification;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.ProductManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class RemoveProductTypeClassificationController extends BaseFormController
		implements Controller {

	private transient final Log log = LogFactory
			.getLog(RemoveProductTypeClassificationController.class);

	private ProductManager productManager;
	private CarLookupManager carLookupManager;

	

	/**
	 * @param productManager the productManager to set
	 */
	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}

	/**
	 * @param carLookupManager
	 *            the carLookupManager to set
	 */
	public void setCarLookupManager(CarLookupManager carLookupManager) {
		this.carLookupManager = carLookupManager;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Long classProductTypeId = ServletRequestUtils.getLongParameter(request,
				"classProductTypeId");
		Long productTypeId = ServletRequestUtils.getLongParameter(request, "id");
		if (classProductTypeId == null || productTypeId == null) {
			log.info("Classification Id or product type id was null. Redirecting...");
			return new ModelAndView("redirect:dashBoard.html");
		}
		String action = getCurrentAction(request);
		if ("remove".equals(action)) {
			ProductType ptrType = productManager.getProductType(productTypeId);
			removeProductTypeClassification(ptrType, classProductTypeId);
			productManager.save(ptrType);
			saveMessage(request, getText("classification.producttype.deleted", ptrType.getName(),
					request.getLocale()));
		}
		//TODO - Change to a form view property
		return new ModelAndView("redirect:/admin/producttype/details.html?id="
				+ productTypeId);
	}

	private void removeProductTypeClassification(ProductType pType, Long classId) {
		if (pType != null) {
			Classification classification = carLookupManager.getClassificationById(classId);
			if (classification != null) {
				pType.getClassifications().remove(classification);
			} else {
				log
						.error("Classification with id " + classId
								+ "could not be found");
			}
		}
	}
}