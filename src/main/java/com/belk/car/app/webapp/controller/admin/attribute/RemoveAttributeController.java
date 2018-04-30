package com.belk.car.app.webapp.controller.admin.attribute;

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
import com.belk.car.app.model.Status;
import com.belk.car.app.service.AttributeManager;
import com.belk.car.app.service.CarsPIMAttributeMappingManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class RemoveAttributeController extends BaseFormController implements Controller {

	private transient final Log log = LogFactory.getLog(RemoveAttributeController.class);

	private AttributeManager attributeManager;
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

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Long attrId = ServletRequestUtils.getLongParameter(request, "id");
		if (attrId == null) {
			log.debug("Attribute id was null. Redirecting...");
			// TODO - Change to a property view
			return new ModelAndView("redirect:dashBoard.html");
		}

		String action = getCurrentAction(request);
		Attribute attribute = attributeManager.getAttribute(attrId);
		if (attribute == null) {
			saveMessage(request, getText("attribute.deletion.problem", attrId + "", request.getLocale()));
		} else {
			if ("remove".equals(action)) {
				attribute.setStatusCd(Status.INACTIVE);
				AttributeChangeTracking act = populateAttributeChangeTracking(attrId);
				attributeManager.save(attribute) ;
				attributeManager.saveAttributeTracking(act);
				//attributeManager.remove(attribute);
				carsPIMAttributeMappingManager.removeCARSPIMMappingAttribtues(attribute.getAttributeId(), null, null);
				saveMessage(request, getText("attribute.deleted", attrId + "", request.getLocale()));
			} else if ("activate".equals(action) && attribute.getStatusCd().equals(Status.INACTIVE)){
				attribute.setStatusCd(Status.ACTIVE);
				attributeManager.save(attribute) ;
				saveMessage(request, getText("attribute.deleted", attrId + "", request.getLocale()));
			}
		}
		return new ModelAndView(getSuccessView());

	}

	private AttributeChangeTracking populateAttributeChangeTracking(Long attrId) {
			AttributeChangeTracking act = new AttributeChangeTracking();
			act.setAttrId(attrId);
			act.setTypeName(Constants.ATTRIBUTE_LIST);
			act.setTypeAction(Constants.DELETE_VALUE_DISPLAY);
			act.setNewValue(attributeManager.getAttribute(Long.valueOf(attrId)).getName());
			act.setResynchCars(Constants.FLAG_YES);
			act.setSendCmpBm(Constants.FLAG_YES);
			act.setProcessed(Constants.FLAG_NO);
			act.setCreatedDate(new Date());
			act.setUpdatedDate(new Date());
			return act;
	}

}