package com.belk.car.app.webapp.controller.admin.latecars;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.model.LateCarsParams;
import com.belk.car.app.service.LateCarsGroupManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.LateCarsParamsForm;

/*
 * Controller for show edit form and Remove Late Cars Parameters(Rules).
 */

public class LateCarsParamsManageController extends BaseFormController implements
		Controller {

	private transient final Log log = LogFactory
			.getLog(LateCarsParamsManageController.class);
	
	protected LateCarsGroupManager lateCarsGroupManager;
	
	public void setLateCarsGroupManager(LateCarsGroupManager lateCarsGroupManager) {
		this.lateCarsGroupManager = lateCarsGroupManager;
	}
    
	public ModelAndView handleRequest(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		Long lateCarsGroupId = ServletRequestUtils.getLongParameter(request, "lateCarsGroupId");
		log.debug("lateCarsGroupId:"+lateCarsGroupId);
		Long lateCarsParamId = ServletRequestUtils.getLongParameter(request, "lateCarsParamId");
		log.debug("lateCarsParamId:"+lateCarsParamId);
		String method=request.getParameter("method");// passing only for remove
		log.debug("method:"+method);
		if("remove".equals(method))
		{
			lateCarsGroupManager.removeLateCarsGroupRule(lateCarsParamId);
			return new ModelAndView("redirect:/admin/latecarsgroup/lateCarsGroupDetails.html?method=detail&lateCarsGroupId="+lateCarsGroupId);
		}
		else
		{
			if (lateCarsParamId == null || lateCarsGroupId==null ) {
				log.debug("late cars params id or late cars group id was null. Redirecting to dashboard...");
				return new ModelAndView("redirect:dashBoard.html");
			}
		
			LateCarsParams lateCarsParams=lateCarsGroupManager.getLateCarsParams(lateCarsParamId);
			
			LateCarsParamsForm lateCarsParamsform=new LateCarsParamsForm();
			lateCarsParamsform.setCurrentUserType(lateCarsParams.getOwner().getUserTypeCd());
			lateCarsParamsform.setWeeksDue(Long.toString(lateCarsParams.getWeeksdue()));
			lateCarsParamsform.setWorkflowStatus(lateCarsParams.getStatus().getStatusCd());
			lateCarsParamsform.setLateCarsGroupId(lateCarsGroupId);
			lateCarsParamsform.setLateCarsParamId(lateCarsParamId);
			
			return new ModelAndView(getFormView(),"lateCarsParamsForm",lateCarsParamsform);
		}
	}
}