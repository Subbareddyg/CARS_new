/**
 * 
 */
package com.belk.car.app.webapp.controller.admin.workflow;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.model.WorkFlow;
import com.belk.car.app.service.WorkflowManager;
import com.belk.car.app.webapp.controller.BaseFormController;

/**
 * @author antoniog
 *
 */
public class WorkflowDetailsController extends BaseFormController implements
		Controller {

	private transient final Log log = LogFactory
			.getLog(WorkflowDetailsController.class);

	private WorkflowManager workflowManager;

	/**
	 * @param workflowManager the workflowManager to set
	 */
	public void setWorkflowManager(WorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Long workflowId = ServletRequestUtils.getLongParameter(request, "id");
		if (workflowId == null) {
			log.debug("Workflow id was null. Redirecting...");
			return new ModelAndView("redirect:dashBoard.html");
		}

		Map model = new HashMap();
		WorkFlow workflow = workflowManager.getWorkFlow(Long.valueOf(workflowId));	
		model.put("transitions", workflowManager.getAllowedTransitionsForWorkflow(workflowId));
		model.put("workflow",workflow);
		
		return new ModelAndView(getSuccessView(), model);
	}
	
}
