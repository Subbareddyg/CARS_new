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
import com.belk.car.app.model.ClassAttribute;
import com.belk.car.app.service.AttributeManager;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarsPIMAttributeMappingManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class RemoveAttributeClassificationController extends BaseFormController
		implements Controller {

	private transient final Log log = LogFactory
			.getLog(RemoveAttributeClassificationController.class);

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
		Long attrClassId = ServletRequestUtils.getLongParameter(request,
				"classAttrId");
		Long attrId = ServletRequestUtils.getLongParameter(request, "id");
		try {
	
		if (attrClassId == null || attrId == null) {
			log.info("Classification Id or attribute id was null. Redirecting...");
			return new ModelAndView("redirect:dashBoard.html");
		}
		String action = getCurrentAction(request);
		if ("remove".equals(action)) {
			Attribute attr = attributeManager.getAttribute(attrId);
			AttributeChangeTracking act = populateAttributeChangeTracking(attrId,attrClassId);
			removeAttrClassification(attr, attrClassId);
			attributeManager.save(attr);
			attributeManager.saveAttributeTracking(act);
			saveMessage(request, getText("classification.attribute.deleted", attr.getName(),
					request.getLocale()));
		}
		
		model.put("id",attrId);
		}catch (Throwable e) {
			model.put("id",attrId);
        	log
			.error("Error:"+e.getMessage());
		}
		return new ModelAndView(getSuccessView(),model);
		
	}

	private void removeAttrClassification(Attribute attr, Long attrClassificationId) {
		if (attr != null) {
			ClassAttribute classAttr = attributeManager
					.getClassificationAttribute(attrClassificationId);
			if (classAttr != null) {
				attr.getClassAttributes().remove(classAttr);
				carsPIMAttributeMappingManager.removeCARSPIMMappingAttribtues(attr.getAttributeId(), String.valueOf(classAttr.getClassification().getClassId()), Constants.REMOVE_ATTR_CLASS);

			} else {
				log.error("Classification attribute with id " + attrClassificationId
						+ " could not be found");
			}
		}
	}
	
	private AttributeChangeTracking populateAttributeChangeTracking(Long attrId, Long attrClassificationId) {
			AttributeChangeTracking act = new AttributeChangeTracking();
			act.setAttrId(attrId);
			act.setTypeName(Constants.CLASSIFICATION_LIST);
			act.setTypeAction(Constants.DELETE_VALUE_DISPLAY);
			//act.setOldValue(oldValue);
			act.setNewValue(Long.toString(attributeManager
					.getClassificationAttribute(attrClassificationId).getClassification().getClassId()));
			act.setResynchCars(Constants.FLAG_YES);
			act.setSendCmpBm(Constants.FLAG_YES);
			act.setProcessed(Constants.FLAG_NO);
			act.setCreatedDate(new Date());
			act.setUpdatedDate(new Date());
			return act;
	}
	
}