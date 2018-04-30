package com.belk.car.app.webapp.controller.admin.workflow;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.model.WorkFlow;
import com.belk.car.app.model.workflow.WorkflowTransition;
import com.belk.car.app.service.WorkflowManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class RemoveTransitionController extends BaseFormController implements
		Controller {

	private transient final Log log = LogFactory
			.getLog(RemoveTransitionController.class);

	private WorkflowManager workflowManager;
	

	/**
	 * @param workflowManager the workflowManager to set
	 */
	public void setWorkflowManager(WorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Long workFlowId = ServletRequestUtils.getLongParameter(request,
				"id");
		Long transitionId = ServletRequestUtils.getLongParameter(request,"workflowTransitionId");
		if (workFlowId == null || transitionId == null) {
			log.debug("Workflow id was null. Redirecting...");
			// TODO - Change to a property view
			return new ModelAndView("redirect:dashBoard.html");
		}

		String action = getCurrentAction(request);

		if ("remove".equals(action)) {
			 WorkFlow workflow = workflowManager.getWorkFlow(workFlowId);
			 WorkflowTransition transition = workflowManager.getWorkflowTransition(transitionId);
		    			 
			if (workflow != null && transition !=null) {				
				workflowManager.deleteWorkflowTransition(workflow, transition);
				saveMessage(request, getText("transition.deleted",
						transitionId+"", request.getLocale()));
				java.util.Map model = new HashMap();
				model.put("id", workFlowId);
				return new ModelAndView(getSuccessView(),model);
			}
			else {
				saveMessage(request, getText("transition.deletion.problem",
						transitionId+"", request.getLocale()));				
			
			}					
		}
		return new ModelAndView(getFormView());

	}
}
