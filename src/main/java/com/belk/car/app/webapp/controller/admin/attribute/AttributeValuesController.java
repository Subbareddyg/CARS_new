package com.belk.car.app.webapp.controller.admin.attribute;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.AccessDeniedException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.dto.MissingAttributeValueList;
import com.belk.car.app.model.AttributeLookupValue;
import com.belk.car.app.model.AttributeValueProcessStatus;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.Status;
import com.belk.car.app.service.AttributeManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.AttributeForm;

public class AttributeValuesController extends BaseFormController {

	private AttributeManager attributeManager;

	public void setAttributeManager(AttributeManager attributeManager) {
		this.attributeManager = attributeManager;
	}

	public AttributeValuesController() {
		setCommandName("attributeForm");
		setCommandClass(AttributeForm.class);
	}

	public ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		if (request.getParameter("cancel") != null) {
			if (StringUtils.isBlank(request.getParameter("id"))) {
				return new ModelAndView(getSuccessView());
			}
		}

		return super.processFormSubmission(request, response, command, errors);
	}

	public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		log.debug("entering attributeForm 'onSubmit' method...");

		AttributeForm attrForm = (AttributeForm) command;
		Locale locale = request.getLocale();
		String actionName = null;
		String successView = "";

		Map model = new HashMap();

		String action = attrForm.getAction();

		if (StringUtils.isBlank(action)) {
			action = "";
		}

		try {
			if (action.equalsIgnoreCase(getText("button.accept.attribute.values", locale))) {
				successView = processAttributeValuesStatus(request, attrForm, true);
			} else if (action.equalsIgnoreCase(getText("button.reject.attribute.values", locale))) {
				successView = processAttributeValuesStatus(request, attrForm, false);
			} else { // This is never supposed to happen
				log.error("Error. AttributeValuesController expects an action");
				successView = getFormView();
			}

		} catch (AccessDeniedException ade) {
			log.warn(ade.getMessage());
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (Exception e) {
			errors.rejectValue("attribute", "errors.existing.attribute", new Object[] { attrForm.getName() }, "duplicate attribute");

			return showForm(request, response, errors);
		}

		return new ModelAndView(successView, model);
	}

	private String processAttributeValuesStatus(HttpServletRequest request, AttributeForm attrForm, boolean isApproved) {

		if (attrForm.getAttributeIds() != null) {
			for (String attributeId : attrForm.getAttributeIds()) { //Iterate through all of the selected attribute values		
				try {
					//Update car attribute values
					getCarIdsFromRequest(request, attributeId, isApproved);
				} catch (NumberFormatException e) {
					if (log.isDebugEnabled()) {
						log.debug("Entering NumerFormat Exception");
					}
				}
			}
		}
		return getSuccessView();
	}

	private CarAttribute getCarIdsFromRequest(HttpServletRequest request, String attributeId, boolean isApproved) {
		// Get car Attribute id (unique identifier)
		String cAttrId = request.getParameter("carAttributeId" + attributeId);
		String carIds = request.getParameter("carAttributeIds" + cAttrId);
		String oldValue = request.getParameter("oldValue" + cAttrId);
		
		// Get updated value
		String valueCarAttr = request.getParameter("v_" + cAttrId); 

		AttributeValueProcessStatus accepted = (AttributeValueProcessStatus) getCarLookupManager().getAttributeValueProcessStatus(
				AttributeValueProcessStatus.ACCEPTED);
		AttributeValueProcessStatus rejected = (AttributeValueProcessStatus) getCarLookupManager().getAttributeValueProcessStatus(
				AttributeValueProcessStatus.REJECTED);

		CarAttribute dbCarAttribute = null;
		if (StringUtils.isNotBlank(carIds)) {
			StringTokenizer st = new StringTokenizer(carIds, ",");
			while (st.hasMoreTokens()) {
				String carAttributeId = st.nextToken();
				if (StringUtils.isNotBlank(carAttributeId)) {
					dbCarAttribute = carManager.getCarAttributeFromId(Long.parseLong(carAttributeId));
					if (isApproved) {
						dbCarAttribute.setAttributeValueProcessStatus(accepted);

						dbCarAttribute.setAttrValue(valueCarAttr);

						//Make sure the value is not already contained in the set
						if (!isInLookUpValues(dbCarAttribute.getAttribute().getAttributeLookupValues(), valueCarAttr)) {
							// Create new Attribute Value and store it
							AttributeLookupValue attrValue = new AttributeLookupValue();
							attrValue.setValue(valueCarAttr);
							attrValue.setAttribute(dbCarAttribute.getAttribute());
							attrValue.setStatusCd(Status.ACTIVE);
							attrValue.setTag(valueCarAttr);
							attrValue.setDisplaySequence((short) 0);
							setAuditInfo(request, attrValue);

							dbCarAttribute.getAttribute().getAttributeLookupValues().add(attrValue);
						}
					} else {
						dbCarAttribute.setAttributeValueProcessStatus(rejected);
					}
					// Update car attribute value
					dbCarAttribute = carManager.updateCarAttributeValue(dbCarAttribute);
				}
			}
		}
		return dbCarAttribute;
	}

	private boolean isInLookUpValues(Set<AttributeLookupValue> attributeLookupValues, String attrValue) {
		boolean isInLookUpValues = false;
		for (AttributeLookupValue lValue : attributeLookupValues) {
			if (lValue.getValue().equals(attrValue)) {
				isInLookUpValues = true;
				break;
			}
		}
		return isInLookUpValues;
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		return populateAttributeForm();
	}

	private Object populateAttributeForm() {
		AttributeForm attrForm = new AttributeForm();
		MissingAttributeValueList missingValueList = new MissingAttributeValueList();
		missingValueList.addAll(attributeManager.getAllCarAttributeValuesForStatus(AttributeValueProcessStatus.CHECK_REQUIRED));
		attrForm.setMissingAttributeValues(missingValueList.getMissingAttributeValues());
		return attrForm;
	}

	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.registerCustomEditor(List.class, "carAttributeValues", new CustomCollectionEditor(List.class) {
			protected Object convertElement(Object element) {
				String value = null;
				if (element instanceof String)
					value = (String) element;
				return value;
			}
		});
	}

}
