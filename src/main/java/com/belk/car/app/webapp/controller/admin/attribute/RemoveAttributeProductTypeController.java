package com.belk.car.app.webapp.controller.admin.attribute;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.Constants;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeChangeTracking;
import com.belk.car.app.model.ProductTypeAttribute;
import com.belk.car.app.service.AttributeManager;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarsPIMAttributeMappingManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class RemoveAttributeProductTypeController extends BaseFormController
		implements Controller {

	private transient final Log log = LogFactory
			.getLog(RemoveAttributeProductTypeController.class);

	private AttributeManager attributeManager;
	private CarLookupManager carLookupManager;
    private CarsPIMAttributeMappingManager carsPIMAttributeMappingManager;
	
	public void setCarsPIMAttributeMappingManager(CarsPIMAttributeMappingManager carsPIMAttributeMappingManager) {
		this.carsPIMAttributeMappingManager = carsPIMAttributeMappingManager;
	}
	/**
	 * @param attributeManager
	 *            the attributeManager to set
	 */
	public void setAttributeManager(AttributeManager attributeManager) {
		this.attributeManager = attributeManager;
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
		Map<String,Long> model = new HashMap<String,Long>();
		Long attrProductTypeId = ServletRequestUtils.getLongParameter(request,
				"productTypeAttrId");
		Long attrId = ServletRequestUtils.getLongParameter(request, "id");
        try {
		if (attrProductTypeId == null || attrId == null) {
			log
					.info("Product Type Id or attribute id was null. Redirecting...");
			return new ModelAndView("redirect:dashBoard.html");
		}
		String action = getCurrentAction(request);
		if ("remove".equals(action)) {
			Attribute attr = attributeManager.getAttribute(attrId);
			AttributeChangeTracking act = populateAttributeChangeTracking(attrId,attrProductTypeId);
			removeAttrProductType(attr, attrProductTypeId);
			attributeManager.save(attr);
			attributeManager.saveAttributeTracking(act);

			saveMessage(request, getText("product.type.attribute.deleted", attr.getName(),
					request.getLocale()));
		}
		
        } catch (Throwable e) {
        	log
			.error("Error:"+e.getMessage());
		}
		model.put("id",attrId);
		return new ModelAndView(getSuccessView(),model);
	}

	private void removeAttrProductType(Attribute attr, Long attrProductTypeId) {
		if (attr != null) {
			ProductTypeAttribute pTypeAttr = attributeManager
					.getProductTypeAttribute(attrProductTypeId);
			if (pTypeAttr != null) {
				attr.getProductTypeAttributes().remove(pTypeAttr);
				carsPIMAttributeMappingManager.removeCARSPIMMappingAttribtues(attr.getAttributeId(), String.valueOf(pTypeAttr.getProductType().getProductTypeId()), Constants.REMOVE_ATTR_PRODCUT_TYPE);

			} else {
				log.error("Product Type with id " + attrProductTypeId
						+ " could not be found");
			}
		}
	}

	private AttributeChangeTracking populateAttributeChangeTracking(Long attrId, Long attrProductTypeId) {
			AttributeChangeTracking act = new AttributeChangeTracking();
			act.setAttrId(attrId);
			act.setTypeName(Constants.PRODUCT_TYPE_LIST);
			act.setTypeAction(Constants.DELETE_VALUE_DISPLAY);
			//act.setOldValue(oldValue);
			act.setNewValue(Long.toString(attributeManager
					.getProductTypeAttribute(attrProductTypeId).getProductType().getProductTypeId()));
			act.setResynchCars(Constants.FLAG_YES);
			act.setSendCmpBm(Constants.FLAG_YES);
			act.setProcessed(Constants.FLAG_NO);
			act.setCreatedDate(new Date());
			act.setUpdatedDate(new Date());
			return act;
	}
}