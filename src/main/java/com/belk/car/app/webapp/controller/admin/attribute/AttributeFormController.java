package com.belk.car.app.webapp.controller.admin.attribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Date;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.AccessDeniedException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.exceptions.AttributeValueExistsException;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeChangeTracking;
import com.belk.car.app.model.AttributeConfig;
import com.belk.car.app.model.AttributeDatatype;
import com.belk.car.app.model.AttributeLookupValue;
import com.belk.car.app.model.AttributeType;
import com.belk.car.app.model.HtmlDisplayType;
import com.belk.car.app.model.Status;
import com.belk.car.app.service.AttributeManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.AttributeForm;

public class AttributeFormController extends BaseFormController {

	private AttributeManager attributeManager;

	public void setAttributeManager(AttributeManager attributeManager) {
		this.attributeManager = attributeManager;
	}

	public AttributeFormController() {
		setCommandName("attributeForm");
		setCommandClass(AttributeForm.class);
	}

	public ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		if (request.getParameter("cancel") != null) {
			if (StringUtils.isBlank(request.getParameter("id"))) {
				return new ModelAndView(getListView());
			} else {
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("id", request.getParameter("id"));
				return new ModelAndView(getCancelView(), model);
			}
		}

		return super.processFormSubmission(request, response, command, errors);
	}

	public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		log.debug("entering attributeForm 'onSubmit' method...");
		AttributeForm attrForm = (AttributeForm) command;
		Attribute attribute = null;
		//String attrId = request.getParameter("id");
		Locale locale = request.getLocale();
		String nextView = getSuccessView();
		Map<String, Object> model = new HashMap<String, Object>();
		boolean isCopy = false;

		String action = null;
		//Check type of action
		if (StringUtils.isNotBlank(attrForm.getAction())) {
			action = attrForm.getAction();
		} else {
			action = Constants.NOT_DEFINED;
		}

		if (action.equals(Constants.COPY)) {
			isCopy = true;
		}

		try {
			attribute = populateAndSaveAttribute(request, attrForm, isCopy);

		} catch (AccessDeniedException ade) {
			log.warn(ade.getMessage());
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (Exception e) {
			errors.rejectValue("attribute", "errors.existing.attribute", new Object[] { attrForm.getName() }, "duplicate attribute");

			return showForm(request, response, errors);
			// return showForm(request, response, errors);
		}

		if (!StringUtils.equals(request.getParameter("from"), "list")) {
			saveMessage(request, getText("attribute.saved", /* user.getFullName(), */
			locale));

		}

		model.put("id", attribute.getAttributeId());
		return new ModelAndView(nextView, model);
	}

	private Attribute populateAndSaveAttribute(HttpServletRequest request, AttributeForm attrForm, boolean isCopy) throws AttributeValueExistsException,
			DataIntegrityViolationException {

		Attribute attribute = null;
		boolean isUpdate = false;
		boolean isAdd = false;
		if (attrForm.getAttribute() == null || isCopy) {// New or Copy Attribute
			attribute = new Attribute();
		} else {
			attribute = attrForm.getAttribute();
			isUpdate = true;
		}
		
		if (attrForm.getAttribute() == null)
			isAdd = true;

		attribute.setName(attrForm.getName());

		AttributeType attributeType = attributeManager.getAttributeType(attrForm.getAttrTypeCd());
		AttributeDatatype attributeDataType = attributeManager.getAttributeDataType(attrForm.getDataTypeCd());
		//1. Save config reference
		AttributeConfig attrConfig = doSaveAttributeConfig(request, attrForm, isUpdate);
		attribute.setAttributeConfig(attrConfig);
		attribute.setAttributeType(attributeType);
		attribute.setDatatype(attributeDataType);
		attribute.setBlueMartiniAttribute(StringUtils.trim(attrForm.getBlueMartiniAttribute()));
		attribute.setDescription(attrForm.getDescription());
		attribute.setIsDisplayable(attrForm.getIsDisplayable() ? Attribute.FLAG_YES : Attribute.FLAG_NO);
		attribute.setIsSearchable(attrForm.getIsSearchable() ? Attribute.FLAG_YES : Attribute.FLAG_NO);
		/* -- Start of code added for Outfit Management -- */
		attribute.setIsOutfit(attrForm.getIsOutfit() ? Attribute.FLAG_YES : Attribute.FLAG_NO);
	    /* -- End of code added for Outfit Management -- */
		//System.out.println("AttributeFormController ---- IsOutfit"+ attrForm.getIsOutfit());
		/* -- Start of code added for Deal Based Management -- */
		attribute.setIsPYG(attrForm.getIsPYG() ? Attribute.FLAG_YES : Attribute.FLAG_NO);
		/* -- End of code added for Deal Based Management -- */
		
		/* -- Start of code added for Required Field -- */
		attribute.setIsRequired(attrForm.getIsRequired() ? Attribute.FLAG_YES : Attribute.FLAG_NO);
		/* -- End of code added for Required Field -- */
		attribute.setStatusCd(Status.ACTIVE);
		setAuditInfo(request, attribute);
		//2. Save attribute
		attribute = doSaveAttribute(attribute);
		//3. Save attribute values		
		attribute = doSaveAttributeValues(request, attrForm, attribute);
		//4. Save attribute change tracking
		doSaveAttributeChangeTracking(request, attribute, isAdd);

		return attribute;
	}

	private Attribute doSaveAttributeValues(HttpServletRequest request, AttributeForm attrForm, Attribute attribute) throws AttributeValueExistsException,
			DataIntegrityViolationException {
		Attribute attr = null;
		if (StringUtils.isNotBlank(attrForm.getAttributeValues())) {
			List<String> lookupValues = new ArrayList<String>();
			
			//De-Dupe all Attribute Values
			StringTokenizer st = new StringTokenizer(attrForm.getAttributeValues(), attrForm.getDelimiter());
			while (st.hasMoreTokens()) {
				String strValue = st.nextToken().trim();
				if (StringUtils.isNotBlank(strValue) && !lookupValues.contains(strValue)) {
					lookupValues.add(strValue) ;
				}
			}
			
			if (!lookupValues.isEmpty()) {
				//Sort Current List of Values
				Collections.sort(lookupValues);

				//Clear Existing Values
				attribute.getAttributeLookupValues().clear();//Remove all and re-populate
				attribute = attributeManager.save(attribute);
				
				//Create New Attribute Values and Save
				for(String strValue : lookupValues) {
					AttributeLookupValue value = new AttributeLookupValue();
					value.setValue(strValue);
					value.setAttribute(attribute);
					value.setStatusCd(Status.ACTIVE);
					value.setTag("");
					value.setDisplaySequence((short) 0);
					setAuditInfo(request, value);
					attribute.getAttributeLookupValues().add(value);
				}
			}
		} else if (attribute.getAttributeLookupValues() != null && !attribute.getAttributeLookupValues().isEmpty()) {
			attribute.getAttributeLookupValues().clear();//Remove all
		}

		try {
			attr = attributeManager.save(attribute);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
			throw new DataIntegrityViolationException("Data Integrity Violation Exception occured. Cause: " + e.getCause().getMessage());
		} catch (EntityExistsException e) { // needed for JPA
			e.printStackTrace();
			log.warn(e.getMessage());
			throw new AttributeValueExistsException("Attribute name '" + attrForm.getName() + "' already exists!");
		}
		return attr;
	}

	/**
	 * @param request
	 * @param attrForm
	 * @return
	 */
	private AttributeConfig doSaveAttributeConfig(HttpServletRequest request, AttributeForm attrForm, boolean isUpdate) {

		AttributeConfig attrConfig = null;
		if (isUpdate) {
			attrConfig = attributeManager.getAttributeConfig(attrForm.getAttribute().getAttributeConfig().getAttrConfigId());
		} else {
			attrConfig = new AttributeConfig();
		}
		populateAttributeConfig(request, attrForm, attrConfig);
		attributeManager.save(attrConfig);

		return attrConfig;
	}

	private Attribute doSaveAttribute(Attribute attribute) throws AttributeValueExistsException {
		return attributeManager.save(attribute);
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {

		Attribute attr = null;
		AttributeForm attrForm = new AttributeForm();

		String action = getCurrentAction(request);
		if (StringUtils.isNotBlank(action)) {
			attrForm.setAction(action);
		}
		if (!isFormSubmission(request)) {
			String attrId = request.getParameter("id");
			if (!StringUtils.isBlank(attrId) && !"".equals(request.getParameter("version"))) {
				attr = attributeManager.getAttribute(Long.valueOf(attrId));
				if (attr != null) {
					attrForm = populateAttributeForm(attrForm, attr);
				}
			} else {
				return attrForm;
			}

			return attrForm;
		} else if (request.getParameter("id") != null && !"".equals(request.getParameter("id")) && request.getParameter("cancel") == null) {
			// populate attribute object from database, so all fields don't need
			// to
			// be hidden fields in form
			attr = attributeManager.getAttribute(Long.valueOf(request.getParameter("id")));
			attrForm.setAttribute(attr);
			return attrForm;
		}

		return super.formBackingObject(request);
	}


	/**
	 * @param request
	 * @param attrForm
	 * @return
	 */
	private void doSaveAttributeChangeTracking(HttpServletRequest request, Attribute attr, boolean isAdd) {

		//added for Attribute Resynch
		String addValueList = request.getParameter("addValueList");
		String removeValueList = request.getParameter("removeValueList");
		String editValueList = request.getParameter("editValueList");
		List<AttributeChangeTracking> trackingList = new ArrayList<AttributeChangeTracking>();
		String trackingAction;

		if(isAdd){
			trackingList = populateAttributeTracking(attr,trackingList);
		}
		if (StringUtils.isNotBlank(addValueList)){ 
			trackingAction=Constants.ADD_VALUE_DISPLAY;
			trackingList = populateAttributeTrackingValues(addValueList,trackingAction,attr,trackingList);
			
		}
		if (StringUtils.isNotBlank(removeValueList)){ 
			trackingAction=Constants.DELETE_VALUE_DISPLAY;
			trackingList = populateAttributeTrackingValues(removeValueList,trackingAction,attr,trackingList);
			
		}
		if (StringUtils.isNotBlank(editValueList)){ 
			trackingAction=Constants.EDIT_VALUE_DISPLAY;
			trackingList = populateAttributeTrackingValues(editValueList,trackingAction,attr,trackingList);
			
		}
		if(trackingList.size() >0){
				attributeManager.saveAttributeTrackingList(trackingList);
		}

		return;
	}

	/**
	 * @param valueList
	 * @param action
	 * @param attribute
	 * @param trackingList
	 */
	private List<AttributeChangeTracking> populateAttributeTrackingValues(String valueList, String action, Attribute attribute, List<AttributeChangeTracking> trackingList) {
		String[] values = valueList.split(",");
		String resynchCars = Constants.FLAG_NO;
		String sendCmpBm = Constants.FLAG_NO;
		String process = Constants.FLAG_NO;
		String oldValue="";
		String newValue ="";
		try{
		//Create New Attribute Change Tracking and add it to the list
				for(String strValue : values) {
					if(action.equals(Constants.EDIT_VALUE_DISPLAY)){//This if loop to replace existing tracking row of attribute value if the current value is changed multiple times before processed. It replces same row's new value instead of creating new row.
						String[] editVal = strValue.split(Constants.ATTRVAL_DELIMITER);
						newValue = editVal[1];
						oldValue = editVal[0];
						AttributeChangeTracking existingAct = attributeManager.getUnprocessedEditedAttrValueByOldValue(oldValue);
						if(existingAct !=null){
							existingAct.setNewValue(newValue);
							existingAct.setUpdatedDate(new Date());
							trackingList.add(existingAct);
							continue;
						}
					}
					
					AttributeChangeTracking act = new AttributeChangeTracking();
					newValue = strValue;
					if(action.equals(Constants.EDIT_VALUE_DISPLAY)){
						String[] editVal = strValue.split(Constants.ATTRVAL_DELIMITER);
						newValue = editVal[1];
						oldValue = editVal[0];
						resynchCars =Constants.FLAG_YES;
						sendCmpBm = Constants.FLAG_YES;
					}else if(action.equals(Constants.DELETE_VALUE_DISPLAY)){
						resynchCars =Constants.FLAG_YES;
						sendCmpBm = Constants.FLAG_YES;
					}
					act.setAttrId(attribute.getAttributeId());
					act.setTypeName(Constants.ATTRIBUTE_VALUE_DISPLAY);
					act.setTypeAction(action);
					act.setOldValue(oldValue);
					act.setNewValue(newValue);
					act.setResynchCars(resynchCars);
					act.setSendCmpBm(sendCmpBm);
					act.setProcessed(process);
					act.setCreatedDate(new Date());
					act.setUpdatedDate(new Date());
					trackingList.add(act);
				}
	}catch(Exception ex){
		ex.printStackTrace();
	}
		return trackingList;
	}
	
	private List<AttributeChangeTracking> populateAttributeTracking(Attribute attribute, List<AttributeChangeTracking> trackingList) {
			AttributeChangeTracking act = new AttributeChangeTracking();
			act.setAttrId(attribute.getAttributeId());
			act.setTypeName(Constants.ATTRIBUTE_LIST);
			act.setTypeAction(Constants.ADD_VALUE_DISPLAY);
			act.setNewValue(attribute.getName());
			act.setResynchCars(Constants.FLAG_NO);
			act.setSendCmpBm(Constants.FLAG_NO);
			act.setProcessed(Constants.FLAG_NO);
			act.setCreatedDate(new Date());
			act.setUpdatedDate(new Date());
			trackingList.add(act);
		
		return trackingList;
	}

	/**
	 * @param request
	 * @param attrForm
	 * @param attrConfig
	 */
	private AttributeConfig populateAttributeConfig(HttpServletRequest request, AttributeForm attrForm, AttributeConfig attrConfig) {
		HtmlDisplayType htmlDisplayType = getCarLookupManager().getHtmlDisplayType(attrForm.getHtmlDisplayTypeCd());
		attrConfig.setHtmlDisplayType(htmlDisplayType);
		attrConfig.setDisplayName(attrForm.getDisplayName());
		attrConfig.setName(attrForm.getName());
		attrConfig.setStatusCd(Status.ACTIVE);
		attrConfig.setValidationExpression(this.getCarLookupManager().getValidationExpression(attrForm.getValidationExpression()));
		setAuditInfo(request, attrConfig);
		return attrConfig;
	}

	/**
	 * 
	 * @param attrForm
	 * @param attr
	 * @return
	 */
	private AttributeForm populateAttributeForm(AttributeForm attrForm, Attribute attr) {

		attrForm.setHtmlDisplayTypeCd(attr.getAttributeConfig().getHtmlDisplayType().getHtmlDisplayTypeCd());
		attrForm.setValidationExpression(attr.getAttributeConfig().getValidationExpression().getValidationExpressionCd());
		attrForm.setAttrTypeCd(attr.getAttributeType().getAttrTypeCd());
		attrForm.setDataTypeCd(attr.getDatatype().getAttrDatatypeCd());
		attrForm.setBlueMartiniAttribute(attr.getBlueMartiniAttribute());
		attrForm.setDescription(attr.getDescription());
		attrForm.setDisplayName(attr.getAttributeConfig().getDisplayName());
		attrForm.setIsDisplayable(Attribute.FLAG_YES.equalsIgnoreCase(attr.getIsDisplayable()));
		attrForm.setIsSearchable(Attribute.FLAG_YES.equalsIgnoreCase(attr.getIsSearchable()));
	    /* -- Start of code added for Outfit Management -- */
		attrForm.setIsOutfit(Attribute.FLAG_YES.equalsIgnoreCase(attr.getIsOutfit()));
		/* -- End of code added for Outfit Management -- */
		  /* -- Start of code added for Deal Based Management -- */
				attrForm.setIsPYG(Attribute.FLAG_YES.equalsIgnoreCase(attr.getIsPYG()));
				/* -- End of code added for Deal Based Management -- */
		
		/* -- Start of code added for Required Field -- */
		attrForm.setIsRequired(Attribute.FLAG_YES.equalsIgnoreCase(attr.getIsRequired()));
		/* -- End of code added for Required Field -- */
		attrForm.setName(attr.getName());
		attrForm.setAttribute(attr);
		attrForm.setAttributeValues(parseAttributeValues(attr, attrForm.getDelimiter()));
		String validationExpression = attr.getAttributeConfig().getValidationExpression().getValidationExpressionCd();
		String vExpressionResult = StringUtils.isNotBlank(validationExpression) ? validationExpression : AttributeConfig.DEFAULT_VALIDATION_EXPRESSION;
		attrForm.setValidationExpression(vExpressionResult);
		return attrForm;
	}

	private String parseAttributeValues(Attribute attribute, String delimiter) {
		List<AttributeLookupValue> lookupList = attribute.getValues() ;
		StringBuffer sb = new StringBuffer();
		if ( lookupList != null && !lookupList.isEmpty()) {
			for (AttributeLookupValue lookUpValue : lookupList) {
				if (StringUtils.isNotBlank(lookUpValue.getValue())) {
					sb.append(lookUpValue.getValue()).append(delimiter);
				}
			}
		} 
		return sb.toString();
	}

	protected boolean isAdd(HttpServletRequest request) {
		String method = request.getParameter("method");
		return (method != null && method.equalsIgnoreCase("add"));
	}

	@Override
	protected Map<String, Object> referenceData(HttpServletRequest request, Object command, Errors errors) throws ServletRequestBindingException {

		Map<String, Object> model = new HashMap<String, Object>();
		String attrId = request.getParameter("id");
		if (!StringUtils.isBlank(attrId))
				model.put("attribute", attributeManager.getAttribute(Long.valueOf(attrId)));
		model.put("datatTypes", attributeManager.getAttributeDataTypes());
		model.put("attributeGroup", attributeManager.getAttributeTypes());
		model.put("displayType", getCarLookupManager().getAllHtmlDisplayTypes());
		model.put("validationRules", getCarLookupManager().getAllValidationExpressions());
		return model;
	}

	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.registerCustomEditor(List.class, "attributeValues", new CustomCollectionEditor(List.class) {
			protected Object convertElement(Object element) {
				String value = null;
				if (element instanceof String)
					value = (String) element;
				return value;
			}
		});
	}

}
