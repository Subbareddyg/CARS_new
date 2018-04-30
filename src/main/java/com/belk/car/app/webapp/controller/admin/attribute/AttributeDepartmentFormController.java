/**
 * 
 */
package com.belk.car.app.webapp.controller.admin.attribute;

import java.util.ArrayList;
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
import com.belk.car.app.model.Department;
import com.belk.car.app.model.DepartmentAttribute;
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

public class AttributeDepartmentFormController extends BaseFormController {

	private transient final Log log = LogFactory.getLog(AttributeDepartmentFormController.class);

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

	public AttributeDepartmentFormController() {
		setCommandName("attributeForm");
		setCommandClass(AttributeForm.class);
	}

	public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		log.debug("entering associate Attribute to Department 'onSubmit' method...");

		AttributeForm attrForm = (AttributeForm) command;

		Set<Department> departments = attrForm.getDepartments();
		List<String> deptList = new ArrayList<String>();
		for (Department dept : departments) {
			deptList.add(String.valueOf(dept.getDeptId()));
		}
		System.out.println("dept list="+deptList.toString());
		List<AttributeChangeTracking> trackingList = new ArrayList<AttributeChangeTracking>();
		if (!deptList.isEmpty()) {
			//userForm.getUser().getDepartments().clear();
			for (String deptId : deptList) {
				Department dbDept = carLookupManager.getDepartmentById(Long.valueOf(deptId));
				DepartmentAttribute deptAttr = new DepartmentAttribute();
				deptAttr.setAttribute(attrForm.getAttribute());
				//deptAttr.setDefaultAttrValue("None");
				deptAttr.setDefaultAttrValue(Attribute.DEFAULT_VALUE);
				deptAttr.setIsMandatory(Constants.FLAG_NO);
				deptAttr.setStatusCd(Status.ACTIVE);
				deptAttr.setDepartment(dbDept);
				deptAttr.setIsAttrVariable(Constants.FLAG_NO);
				setAuditInfo(request, deptAttr);
				attrForm.getAttribute().associateWithDepartment(deptAttr);
			

				//Attribute Change Tracking
				AttributeChangeTracking act = new AttributeChangeTracking();
				act.setAttrId(attrForm.getAttribute().getAttributeId());
				act.setTypeName(Constants.DEPARTMENT_LIST);
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
			carsPIMAttributeMappingManager.addCarsPimMappingAttribtues(attrForm.getAttribute().getAttributeId(), deptList, attrForm.getAction(),
					Constants.DEPARTMENT_LIST);
		} catch (AccessDeniedException ade) {
			// thrown by UserSecurityAdvice configured in aop:advisor userManagerSecurity
			log.warn(ade.getMessage());
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (Throwable e) {
        	log
			.error("Error:"+e.getMessage());
		}
		

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("id", attrForm.getAttribute().getAttributeId());
		return new ModelAndView(getSuccessView(), model);
	}

	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws ServletRequestBindingException {

		AttributeForm attrForm = (AttributeForm) command;
		//Convenience Method
		Set<Department> filteredDepts = new HashSet<Department>();
		for (DepartmentAttribute deptAttr : attrForm.getAttribute().getDepartmentAttributes()) {
			filteredDepts.add(deptAttr.getDepartment());
		}
		Map model = new HashMap();
		model.put("attributeDepartments", filteredDepts);
		model.put("departments", carLookupManager.getAllDepartments());
		return model;
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {

		String attrId = request.getParameter("deptAttrId");
		AttributeForm attrForm = new AttributeForm();
		Attribute attr = null;

		if (!StringUtils.isBlank(attrId) && isAddDepartment(request)) {
			attr = attributeManager.getAttribute(Long.valueOf(attrId));
		} else {
			attr = new Attribute();
		}
		attrForm.setAttribute(attr);
		return attrForm;

	}

	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.registerCustomEditor(Set.class, "departments", new CustomCollectionEditor(Set.class) {
			protected Object convertElement(Object element) {
				Long departmentId = null;
				if (element instanceof Long)
					departmentId = (Long) element;
				else if (element instanceof String)
					departmentId = Long.valueOf((String) element);
				Department dept = carLookupManager.getDepartmentById(departmentId);
				return dept;
			}
		});
	}

	protected boolean isAddDepartment(HttpServletRequest request) {
		String method = request.getParameter("action");
		return (method != null && method.equalsIgnoreCase("associateWithDepartment"));
	}
}
