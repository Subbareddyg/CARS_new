/**
 * 
 */
package com.belk.car.app.webapp.controller.admin.attribute;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.exceptions.AttributeValueExistsException;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeChangeTracking;
import com.belk.car.app.model.ClassAttribute;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.Status;
import com.belk.car.app.service.AttributeManager;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarsPIMAttributeMappingManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.AttributeForm;

/**
 * @author antoniog
 *
 */

public class AttributeClassFormController extends BaseFormController {

	private transient final Log log = LogFactory.getLog(AttributeClassFormController.class);

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

	public AttributeClassFormController() {
		setCommandName("attributeForm");
		setCommandClass(AttributeForm.class);
	}

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		log.debug("Entering Associate Attribute to Classification 'onSubmit' Method...");

		AttributeForm attrForm = (AttributeForm) command;

		Set<Classification> classifications = attrForm.getClassifications();
		System.out.println("classification set="+classifications.toString());
		List<String> classificationList = new ArrayList<String>();
		for (Classification classification : classifications) {
			classificationList.add(String.valueOf(classification.getClassId()));
		}
		System.out.println("classification list="+classificationList.toString());
		List<AttributeChangeTracking> trackingList = new ArrayList<AttributeChangeTracking>();
		if (!classificationList.isEmpty()) {
			//userForm.getUser().getDepartments().clear();
			for (String classId: classificationList) {
				Classification dbClassification = carLookupManager.getClassificationById(Long
						.valueOf(classId));
				ClassAttribute classAttr = new ClassAttribute();
				classAttr.setAttribute(attrForm.getAttribute());
				classAttr.setDefaultAttrValue(Attribute.DEFAULT_VALUE);//"None");
				classAttr.setIsMandatory(Constants.FLAG_NO);
				classAttr.setStatusCd(Status.ACTIVE);
				classAttr.setClassification(dbClassification);
				classAttr.setIsAttrVariable(Constants.FLAG_NO);
				setAuditInfo(request, classAttr);
				attrForm.getAttribute().associateWithClassification(classAttr);
			
		
			//Attribute Change Tracking
				AttributeChangeTracking act = new AttributeChangeTracking();
				act.setAttrId(attrForm.getAttribute().getAttributeId());
				act.setTypeName(Constants.CLASSIFICATION_LIST);
				act.setTypeAction(Constants.ADD_VALUE_DISPLAY);
				//act.setOldValue(oldValue);
				act.setNewValue(classId);
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
				carsPIMAttributeMappingManager.addCarsPimMappingAttribtues(attrForm.getAttribute().getAttributeId(), classificationList, attrForm.getAction(),
						Constants.CLASSIFICATION_LIST);
		} catch (AccessDeniedException ade) {
			// thrown by UserSecurityAdvice configured in aop:advisor userManagerSecurity
			log.warn(ade.getMessage());
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (AttributeValueExistsException ave) {
			errors.rejectValue("attribute", "errors.existing.attribute",
					new Object[] { attrForm.getName() }, "duplicate attribute");

			return showForm(request, response, errors);
		} catch (Exception e) {
			/*errors.rejectValue(null,"errors",
					new Object[] { attrForm.getName() }, "Error.Classification Already Exists");*/
			Map model = new HashMap();
			 model.put("id",attrForm.getAttribute()
						.getAttributeId());
				return new ModelAndView(getSuccessView(),model);		
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

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		
			String attrId = request.getParameter("classificationAttrId");
			AttributeForm attrForm = new AttributeForm();
			
			if(request.getSession().getAttribute(Constants.CLASSIFICATION_LIST) != null ) {
				attrForm.setClassificationList((List<Classification>) request.getSession().getAttribute(Constants.CLASSIFICATION_LIST));
				request.getSession().removeAttribute(Constants.CLASSIFICATION_LIST);
			}
			
			Attribute attr = null;
			if (!StringUtils.isBlank(attrId) && isAddClassification(request)) {
				attr = attributeManager.getAttribute(Long.valueOf(attrId));
			} else {
				attr = new Attribute();
			}
			attrForm.setAttribute(attr);
			return attrForm;
		

	}

	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) {
		binder.registerCustomEditor(Set.class, "classifications",
				new CustomCollectionEditor(Set.class) {
					protected Object convertElement(Object element) {
						Long classificationId = null;
						if (element instanceof Long)
							classificationId = (Long) element;
						else if (element instanceof String)
							classificationId = Long.valueOf((String) element);
						Classification classification = carLookupManager
								.getClassificationById(classificationId);
						return classification;
					}
				});
	}

	protected boolean isAddClassification(HttpServletRequest request) {
		String method = request.getParameter("action");
		return (method != null && method.equalsIgnoreCase("associateWithClass"));
	}
}
