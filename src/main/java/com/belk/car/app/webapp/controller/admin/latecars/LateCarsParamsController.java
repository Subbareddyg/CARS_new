package com.belk.car.app.webapp.controller.admin.latecars;




import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.LateCarsGroupManager;
import com.belk.car.app.service.WorkflowManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.LateCarsParamsForm;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import com.belk.car.app.model.LateCarsGroup;
import com.belk.car.app.model.LateCarsParams;

/*
 * Controller for Adding and Update Late CARS Params(Rules)
 */

public class LateCarsParamsController  extends BaseFormController {
	
	private LateCarsGroupManager lateCarsGroupManager;
	private WorkflowManager  workflowManager;
	private CarLookupManager carLookupManager;
	
	public void setWorkflowManager(WorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}

	public void setCarLookupManager(CarLookupManager carLookupManager) {
		this.carLookupManager = carLookupManager;
	}

	public void setLateCarsGroupManager(LateCarsGroupManager lateCarsGroupManager) {
		this.lateCarsGroupManager = lateCarsGroupManager;
	}

	LateCarsParamsController()
	{
		setCommandName("lateCarsParamsForm");
		setCommandClass(LateCarsParamsForm.class);
	}
	
	protected Object formBackingObject(HttpServletRequest request) 	throws Exception {
		log.debug("Inside fomrBackingObject method");
		Long lateCarsGroupId = ServletRequestUtils.getLongParameter(request, "lateCarsGroupId");
		log.debug("lateCarsGroupId on bakcing object:"+lateCarsGroupId);
		LateCarsParamsForm lateCarsParamsform=new LateCarsParamsForm();
		lateCarsParamsform.setLateCarsGroupId(lateCarsGroupId);
		return lateCarsParamsform;
	}
	
	public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		log.debug("entering 'onSubmit' method...");
		LateCarsGroup lateCarsGroup =null;
		LateCarsParams lateCarsParams = null;
		boolean paramExists=false;
		LateCarsParamsForm lateCarsParamsform=(LateCarsParamsForm)command;
		long lateCarsGroupId=lateCarsParamsform.getLateCarsGroupId();
		String workflowStatusCd=lateCarsParamsform.getWorkflowStatus();
		String currentUserTypeCd=lateCarsParamsform.getCurrentUserType();
		String weeksDue=lateCarsParamsform.getWeeksDue();
		String lateCarsParamId = request.getParameter("lateCarsParamId");
		if(log.isDebugEnabled()){
			log.debug("lateCarsGroupId on submit:"+lateCarsGroupId);
			log.debug("lateCarsParamId on submit from request:"+lateCarsParamId);
			log.debug("workflowStatus on submit:"+workflowStatusCd);
			log.debug("currentUserType on submit:"+currentUserTypeCd);
			log.debug("weeksDue on submit:"+weeksDue);
		}
		
		//update
		if(lateCarsParamId!=null && !"".equals(lateCarsParamId)){
			log.info("Edit late car parameter ID" + lateCarsParamId);
			paramExists = lateCarsGroupManager.CheckExistingLateCarsParams(Long.parseLong(lateCarsParamId), currentUserTypeCd, workflowStatusCd, lateCarsGroupId);
			if(paramExists==true){
				errors.rejectValue("currentUserType","Late CAR Group Rule Already Exists");
				saveError(request, errors.getFieldError().getCode());
				return new ModelAndView("redirect:/admin/latecarsgroup/editLateCarsParams.html?lateCarsParamId="+lateCarsParamId+"&lateCarsGroupId="+lateCarsGroupId);
			}
			if(log.isDebugEnabled()){
				log.debug("paramExists on submit:" +paramExists);
			}
			lateCarsParams=lateCarsGroupManager.getLateCarsParams(Long.parseLong(lateCarsParamId));
		} else{ // add new late cars params
			log.info("Add late car parameter");
			paramExists = lateCarsGroupManager.CheckExistingLateCarsParams(currentUserTypeCd, workflowStatusCd, lateCarsGroupId);
			if(paramExists==true){
				errors.rejectValue("currentUserType","Late CAR Group Rule Already Exists");
				saveError(request, errors.getFieldError().getCode());
				return new ModelAndView("redirect:/admin/latecarsgroup/addLateCarsParams.html?lateCarsGroupId="+lateCarsGroupId);
			}
			lateCarsParams = new LateCarsParams();			
		}
		lateCarsGroup=lateCarsGroupManager.getLateCarsGroup(lateCarsGroupId);
		lateCarsParams.setWeeksdue(Long.parseLong(weeksDue));
		lateCarsParams.setOwner(carLookupManager.getUserType(currentUserTypeCd));
		lateCarsParams.setStatus(workflowManager.getWorkFlowStatus(workflowStatusCd));
		lateCarsParams.setLateCarsGroup(lateCarsGroup);
		
		try{
		      lateCarsGroupManager.updateLateCarsGroupRule(lateCarsParams);
		}catch(Exception e)
		{
			log.error("Error in add or update late cars group rule" + e.getMessage());
		}
		return new ModelAndView("redirect:/admin/latecarsgroup/lateCarsGroupDetails.html?method=detail&lateCarsGroupId="+lateCarsGroupId);
	}
}
