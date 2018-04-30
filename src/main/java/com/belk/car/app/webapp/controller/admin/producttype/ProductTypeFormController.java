package com.belk.car.app.webapp.controller.admin.producttype;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.AccessDeniedException;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.exceptions.ProductTypeExistsException;
import com.belk.car.app.model.ProductGroup;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.service.ProductGroupManager;
import com.belk.car.app.service.ProductManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.ProductTypeForm;

public class ProductTypeFormController extends BaseFormController {

	private ProductManager productManager;
	private ProductGroupManager productGroupManager;

	/**
	 * @param productManager the productManager to set
	 */
	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}
	
	public void setProductGroupManager(ProductGroupManager productGroupManager) {
		this.productGroupManager = productGroupManager;
	}
	
	public ProductTypeFormController() {
		setCommandName("productTypeForm");
		setCommandClass(ProductTypeForm.class);
	}

	public ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		
		String productTypeId = request.getParameter("id");
		ProductTypeForm productForm = (ProductTypeForm) command;
		Map model = new HashMap();
		if(StringUtils.isNotBlank(productTypeId)) {
			model.put("id", productTypeId);
		}
		if (request.getParameter("cancel") != null) {
			if (!StringUtils.equals(request.getParameter("from"), "list")) {
				return new ModelAndView(getCancelView());
			} else {
				return new ModelAndView(getSuccessView(),model);
			}
		}

		return super.processFormSubmission(request, response, command, errors);
	}


	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		log.debug("entering productForm 'onSubmit' method...");

		ProductTypeForm productForm = (ProductTypeForm) command;	
		Locale locale = request.getLocale();
		String productTypeId = request.getParameter("id");		
		String action = null;
		Map model = new HashMap();
		
		//Check type of action
		if(StringUtils.isNotBlank(productForm.getAction())) {
			action = productForm.getAction();
		} else {
			action = Constants.NOT_DEFINED;
		}
		
		ProductType product = null;
		
		try {
		    product = productManager.save(populateProduct(request, productForm));
		} catch (AccessDeniedException ade) {			
			log.warn(ade.getMessage());
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}catch (ProductTypeExistsException e) {
			errors.rejectValue("productType", "errors.existing.product.type",
					new Object[] { product.getName()}, "duplicate product type");
			return showForm(request, response, errors);			
		}catch (Exception e) {
			errors.rejectValue("productType", "errors.product.type",
					new Object[] { productForm.getName() },
					"an error has occured");
			return showForm(request, response, errors);
			
		}

		if (!StringUtils.equals(request.getParameter("from"), "list")) {
			saveMessage(request, getText("producttype.saved",  productForm.getName(), 
			locale));
		}
		model.put("id",product.getProductTypeId());
		return new ModelAndView(getSuccessView(),model);
	}

	private ProductType populateProduct(HttpServletRequest request,
			ProductTypeForm productForm) {
		
		ProductType productType = null;
		if (productForm.getProductType() == null) {// New Product
			productType = new ProductType();
		} else {
			productType = productForm.getProductType(); // Update Product
		}		
	    productType.setName(productForm.getName());
	    productType.setStatusCd(productForm.getStatusCd());
	    productType.setDescription(productForm.getDescription());
	    ProductGroup productGroup = new ProductGroup();
	    productGroup.setProductGroupId(Long.parseLong(productForm.getProductGroupID()));
	    productType.setProductGroup(productGroup);
		setAuditInfo(request, productType);
		return productType;
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

		ProductType product = null;
		ProductTypeForm productTypeForm = new ProductTypeForm();
		if (!isFormSubmission(request)) {
			String attrId = request.getParameter("id");
			if (!StringUtils.isBlank(attrId)
					&& !"".equals(request.getParameter("version"))) {
				product = productManager.getProductType(Long.valueOf(attrId));
				if (product != null) {		
					productTypeForm.setProductGroups(productGroupManager.getAllActiveProductGroups());
					return populateProductTypeForm(productTypeForm,product);
				}
			} else {
				productTypeForm = new ProductTypeForm();
				productTypeForm.setProductGroups(productGroupManager.getAllActiveProductGroups());
				return productTypeForm;
			}

		} else if (request.getParameter("id") != null
				&& !"".equals(request.getParameter("id"))
				&& !request.getParameter("id").equals("0")
				&& request.getParameter("cancel") == null) {
			// populate attribute object from database, so all fields don't need
			// to
			// be hidden fields in form
			product = productManager.getProductType(Long.valueOf(request
					.getParameter("id")));
			productTypeForm.setProductType(product);
			return productTypeForm;
		} else {
			productTypeForm = new ProductTypeForm();
			productTypeForm.setProductGroups(productGroupManager.getAllActiveProductGroups());
			return productTypeForm;
		}
		return super.formBackingObject(request);
	}

	private ProductTypeForm populateProductTypeForm(ProductTypeForm productTypeForm,
			ProductType product) {		
		productTypeForm.setProductType(product);
		productTypeForm.setName(product.getName());
		productTypeForm.setDescription(product.getDescription());
		//productTypeForm.setClassId(String.valueOf(product.getClassification().getClassId()));
		productTypeForm.setStatusCd(product.getStatusCd());
		if (product.getProductGroup() != null) {
			productTypeForm.setProductGroupID(product.getProductGroup().getProductGroupIdAsString());
		}
		return productTypeForm;
	}

	protected boolean isAdd(HttpServletRequest request) {
		String method = request.getParameter("action");
		return (method != null && method.equalsIgnoreCase("addProduct"));
	}

	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws ServletRequestBindingException {

		Map model = new HashMap();		
	
		return model;
	}
	
}
