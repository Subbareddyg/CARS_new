package com.belk.car.app.webapp.controller.admin.workflow;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.service.WorkflowManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class WorkflowController extends BaseFormController {
	
    private transient final Log log = LogFactory.getLog(WorkflowController.class);
    
    private WorkflowManager workflowManager;


	/**
	 * @param workflowManager the workflowManager to set
	 */
	public void setWorkflowManager(WorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}


	public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering workflow 'handleRequest' method...");
        }
        
        return new ModelAndView(getSuccessView(), Constants.WORKFLOW_LIST, workflowManager.getAllWorkFlows());
    }

}
