/**
 * 
 */
package com.belk.car.app.webapp.controller.admin.producttype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.AccessDeniedException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.ProductManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.ProductTypeForm;

/**
 * @author antoniog
 *
 */

public class ProductTypeClassFormController extends BaseFormController {

	private transient final Log log = LogFactory.getLog(ProductTypeClassFormController.class);

	private ProductManager productManager;
	private CarLookupManager carLookupManager;

	/**
	 * @param productManager the productManager to set
	 */
	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}

	/**
	 * @param carLookupManager the carLookupManager to set
	 */
	public void setCarLookupManager(CarLookupManager carLookupManager) {
		this.carLookupManager = carLookupManager;
	}

	public ProductTypeClassFormController() {
		setCommandName("productTypeForm");
		setCommandClass(ProductTypeForm.class);
	}

	public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		log.debug("Entering Associate Product Type to Classification 'onSubmit' Method...");

		ProductTypeForm productTypeForm = (ProductTypeForm) command;

		Set<Classification> classifications = productTypeForm.getClassifications();
		List<String> classificationList = new ArrayList<String>();
		for (Classification classification : classifications) {
			classificationList.add(String.valueOf(classification.getClassId()));
		}

		if (!classificationList.isEmpty()) {
			//userForm.getUser().getDepartments().clear();
			for (String classId : classificationList) {
				Classification dbClassification = carLookupManager.getClassificationById(Long.valueOf(classId));
				productTypeForm.getProductType().associateWithClassification(dbClassification);
			}
		}

		try {
			productManager.save(productTypeForm.getProductType());
		} catch (AccessDeniedException ade) {
			// thrown by UserSecurityAdvice configured in aop:advisor userManagerSecurity
			log.warn(ade.getMessage());
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (Exception e) {
			errors.rejectValue(null, "errors", new Object[] { productTypeForm.getName() }, "Error.Classification Already Exists");
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("product", productTypeForm.getProductType());
			return new ModelAndView(getFormView(), model);
		}

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("id", productTypeForm.getProductType().getProductTypeId());
		return new ModelAndView(getSuccessView(), model);
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {

		String productTypeId = request.getParameter("classProductTypeId");
		ProductTypeForm prdTypeForm = new ProductTypeForm();

		if (request.getSession().getAttribute(Constants.CLASSIFICATION_LIST) != null) {
			prdTypeForm.setClassificationList((List<Classification>) request.getSession().getAttribute(Constants.CLASSIFICATION_LIST));
			request.getSession().removeAttribute(Constants.CLASSIFICATION_LIST);
		}

		ProductType productType = null;
		if (!StringUtils.isBlank(productTypeId) && isAddClassification(request)) {
			productType = productManager.getProductType(Long.valueOf(productTypeId));
		} else {
			productType = new ProductType();
		}
		prdTypeForm.setProductType(productType);
		return prdTypeForm;

	}

	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.registerCustomEditor(Set.class, "classifications", new CustomCollectionEditor(Set.class) {
			protected Object convertElement(Object element) {
				Long classificationId = null;
				if (element instanceof Long)
					classificationId = (Long) element;
				else if (element instanceof String)
					classificationId = Long.valueOf((String) element);
				Classification classification = carLookupManager.getClassificationById(classificationId);
				return classification;
			}
		});
	}

	protected boolean isAddClassification(HttpServletRequest request) {
		String method = request.getParameter("action");
		return (method != null && method.equalsIgnoreCase("associateWithClass"));
	}
}
