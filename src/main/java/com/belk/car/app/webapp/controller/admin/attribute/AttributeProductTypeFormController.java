/**
 * 
 */
package com.belk.car.app.webapp.controller.admin.attribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.AccessDeniedException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeChangeTracking;
import com.belk.car.app.model.DepartmentAttribute;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.model.ProductTypeAttribute;
import com.belk.car.app.model.Status;
import com.belk.car.app.service.AttributeManager;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarsPIMAttributeMappingManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.AttributeProductTypeForm;
import com.belk.car.util.GenericComparator;

/**
 * @author antoniog
 *
 */

public class AttributeProductTypeFormController extends BaseFormController {

	private transient final Log log = LogFactory.getLog(AttributeProductTypeFormController.class);

	private AttributeManager attributeManager;
	private CarLookupManager carLookupManager;
	private CarsPIMAttributeMappingManager carsPIMAttributeMappingManager;

	public void setCarsPIMAttributeMappingManager(CarsPIMAttributeMappingManager carsPIMAttributeMappingManager) {
		this.carsPIMAttributeMappingManager = carsPIMAttributeMappingManager;
	}

	/**
	 * @param attributeManager the attributeManager to set
	 */
	public void setAttributeManager(AttributeManager attributeManager) {
		this.attributeManager = attributeManager;
	}

	/**
	 * @param carLookupManager the carLookupManager to set
	 */
	public void setCarLookupManager(CarLookupManager carLookupManager) {
		this.carLookupManager = carLookupManager;
	}

	public AttributeProductTypeFormController() {
		setCommandName("attributeProductTypeForm");
		setCommandClass(AttributeProductTypeForm.class);
	}

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		log.debug("entering associate Attribute to Product Type 'onSubmit' method...");

		AttributeProductTypeForm attrForm = (AttributeProductTypeForm) command;

		Set<ProductType> products = attrForm.getProducts();
		List<String> productTypeList = new ArrayList<String>();
		for (ProductType pType : products) {
			productTypeList.add(String.valueOf(pType.getProductTypeId()));
		}
		System.out.println("productTypeList list="+productTypeList.toString());
		List<AttributeChangeTracking> trackingList = new ArrayList<AttributeChangeTracking>();
		if (!productTypeList.isEmpty()) {
			for (String deptId : productTypeList) {
				ProductType dbProductType = carLookupManager.getProductTypeById(Long
						.valueOf(deptId));
				ProductTypeAttribute productTypeAttr = new ProductTypeAttribute();
				productTypeAttr.setAttribute(attrForm.getAttribute());
				productTypeAttr.setDefaultAttrValue(Attribute.DEFAULT_VALUE);//"None");
				productTypeAttr.setIsMandatory(ProductTypeAttribute.FLAG_YES);
				productTypeAttr.setStatusCd(Status.ACTIVE);
				productTypeAttr.setProductType(dbProductType);
				productTypeAttr.setIsAttrVariable(ProductTypeAttribute.FLAG_NO);
				setAuditInfo(request, productTypeAttr);
				attrForm.getAttribute().associateWithProductType(productTypeAttr);
			    
			
			//Attribute Change Tracking
				AttributeChangeTracking act = new AttributeChangeTracking();
				act.setAttrId(attrForm.getAttribute().getAttributeId());
				act.setTypeName(Constants.PRODUCT_TYPE_LIST);
				act.setTypeAction(Constants.ADD_VALUE_DISPLAY);
				//act.setOldValue(oldValue);
				act.setNewValue(deptId);
				act.setResynchCars(Constants.FLAG_YES);
				act.setSendCmpBm(Constants.FLAG_NO);
				act.setProcessed(Constants.FLAG_NO);
				act.setCreatedDate(new Date());
				act.setUpdatedDate(new Date());
				trackingList.add(act);
				
			}
		}

		try {
				attributeManager.save(attrForm.getAttribute());
				attributeManager.saveAttributeTrackingList(trackingList);
				carsPIMAttributeMappingManager.addCarsPimMappingAttribtues(attrForm.getAttribute().getAttributeId(), productTypeList, attrForm.getAction(),
						Constants.PRODUCT_TYPE_LIST);
		} catch (AccessDeniedException ade) {
			// thrown by UserSecurityAdvice configured in aop:advisor userManagerSecurity
			log.warn(ade.getMessage());
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}	
		catch (Throwable e) {
        	log
			.error("Error:"+e.getMessage());
		}
		 Map model = new HashMap();
		 model.put("id",attrForm.getAttribute()
					.getAttributeId());
		return new ModelAndView(getSuccessView(),model);		
	}

	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws ServletRequestBindingException {

		AttributeProductTypeForm attrForm = (AttributeProductTypeForm) command;
		//Convenience Method
		Set<ProductType> productTypeList = new HashSet<ProductType>();
		for (ProductTypeAttribute productTypeAttr : attrForm.getAttribute().getProductTypeAttributes()) {
			productTypeList.add(productTypeAttr.getProductType());
		}
		Map model = new HashMap();
		model.put("attributeProductTypes",productTypeList);		
		model.put("productTypes", carLookupManager.getAllProductTypes());
		return model;
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {

		String attrId = request.getParameter("productTypeAttrId");
		AttributeProductTypeForm attrForm = new AttributeProductTypeForm();
		Attribute attr = null;

		if (!StringUtils.isBlank(attrId) && isAddProductType(request)) {
			attr = attributeManager.getAttribute(Long.valueOf(attrId));
		} else {
			attr = new Attribute();
		}
		attrForm.setAttribute(attr);
		return attrForm;

	}

	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.registerCustomEditor(Set.class, "products", new CustomCollectionEditor(Set.class) {
			protected Object convertElement(Object element) {
				Long productTypeId = null;
				if (element instanceof Long)
					productTypeId = (Long) element;
				else if (element instanceof String)
					productTypeId = Long.valueOf((String) element);
				ProductType pType = carLookupManager.getProductTypeById(productTypeId);
				return pType;
			}
		});
	}

	protected boolean isAddProductType(HttpServletRequest request) {
		String method = request.getParameter("action");
		return (method != null && method.equalsIgnoreCase("associateWithProductType"));
	}
}
