/**
 * 
 */
package com.belk.car.app.webapp.controller.admin.workflow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.AccessDeniedException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.WorkFlow;
import com.belk.car.app.model.workflow.WorkflowTransition;
import com.belk.car.app.service.WorkflowManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.WorkflowForm;

/**
 * @author antoniog
 * 
 */

public class TransitionFormController extends BaseFormController {

	private transient final Log log = LogFactory
			.getLog(TransitionFormController.class);

	private WorkflowManager workflowManager = null;

	/**
	 * @param workflowManager
	 *            the workflowManager to set
	 */
	public void setWorkflowManager(WorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}

	public TransitionFormController() {
		setCommandName("workflowForm");
		setCommandClass(WorkflowForm.class);
	}

	public ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		if (request.getParameter("cancel") != null) {
			if (!StringUtils.equals(request.getParameter("from"), "list")) {
				String workflowId = ServletRequestUtils.getStringParameter(request,
				"id");
				Map model = new HashMap();
				model.put("id",workflowId);
				return new ModelAndView(getCancelView(),model);
			}
		}

		return super.processFormSubmission(request, response, command, errors);
	}

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		log.debug("entering add transition to workflow 'onSubmit' method...");

		WorkflowForm workflowForm = (WorkflowForm) command;

		try {
			workflowManager.saveWorkflowTransition(populateWorkflowTask(
					request, workflowForm));
		} catch (AccessDeniedException ade) {
			log.warn(ade.getMessage());
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}

		Map model = new HashMap();
		model.put("id", workflowForm.getWorkflowId());
		return new ModelAndView(getSuccessView(), model);
	}

	private WorkflowTransition populateWorkflowTask(HttpServletRequest request,
			WorkflowForm workflowForm) {

		WorkflowTransition transition = null;
		if (workflowForm.getWorkflowTransition() == null) {
			transition = new WorkflowTransition();
		} else {
			transition = workflowForm.getWorkflowTransition();
		}
		if (StringUtils.isNotBlank(workflowForm.getCurrentStatusCd())) {
			transition.setCurrentStatus(workflowManager
					.getWorkFlowStatus(workflowForm.getCurrentStatusCd()));
		}
		if (StringUtils.isNotBlank(workflowForm.getTransitionStatusCd())) {
			transition.setTransitionStatus(workflowManager
					.getWorkFlowStatus(workflowForm.getTransitionStatusCd()));
		}
		if (StringUtils.isNotBlank(workflowForm.getCurrentUserType())) {
			transition.setCurrentUserType(getCarLookupManager().getUserType(
					workflowForm.getCurrentUserType()));
		}
		if (StringUtils.isNotBlank(workflowForm.getTransitionUserType())) {
			transition.setTransitionToUserType(getCarLookupManager()
					.getUserType(workflowForm.getTransitionUserType()));
		}
		if (StringUtils.isNotBlank(workflowForm.getTransitionName())) {
			transition.setTransitionName(workflowForm.getTransitionName());
		}
		if (StringUtils.isNotBlank(workflowForm.getTransitionDescription())) {
			transition.setDescription(workflowForm.getTransitionDescription());
		}
		if (StringUtils.isNotBlank(workflowForm.getTask())) {
			transition.setWorkflowTask(workflowManager
					.getWorkflowTask(workflowForm.getTask()));
		}
		if (workflowForm.getWorkflowId() != null) {
			transition.setWorkflow(workflowManager.getWorkFlow(workflowForm
					.getWorkflowId()));
		}
		transition.setNumberOfDaysToCompleteTask(workflowForm
				.getNumberOfDaysToCompleteTask());
		setAuditInfo(request, transition);

		return transition;
	}

	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws ServletRequestBindingException {

		WorkflowForm workflowForm = (WorkflowForm) command;
		Map model = new HashMap();
		List<UserType> workflowUserList=getCarLookupManager().getActiveUserTypes();
		if(workflowUserList !=null) {
			UserType userType=	getCarLookupManager().getUserType(Constants.WEB_MERCHANT);
			if(userType !=null) {
			    workflowUserList.remove(userType);
			}
		}
		model.put("users", workflowUserList);
		model.put("statuses", workflowManager.getAllWorkFlowStatuses());
		model.put("tasks", workflowManager.getAllWorkFlowTasks());
		return model;
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

		WorkflowForm workflowForm = new WorkflowForm();
		WorkFlow workflow = new WorkFlow();
		WorkflowTransition transition = new WorkflowTransition();
		String workflowId = ServletRequestUtils.getStringParameter(request,
				"id");
		String workflowTransitionId = ServletRequestUtils.getStringParameter(
				request, "workflowTransitionId");

		if (StringUtils.isNotBlank(workflowId)) {
			Long lWorkflowId = Long.parseLong(workflowId);
			workflow = workflowManager.getWorkFlow(lWorkflowId);

			if (!isFormSubmission(request)) { // Regular request
				if (workflow != null) {
					workflowForm.setWorkflow(workflow);
					workflowForm.setWorkflowId(workflow.getWorkflowId());
					workflowForm.setName(workflow.getName());
					workflowForm.setDescription(workflow.getDescription());
				}
				if (StringUtils.isNotBlank(workflowTransitionId)) { // Edit
					//Transition
					Long iWorkflowTransitonid = Long
							.parseLong(workflowTransitionId);
					transition = workflowManager
							.getWorkflowTransition(iWorkflowTransitonid);
					if (transition != null) {
						return populateWorkflowFormWithTransition(workflowForm,
								transition);
					}
				} else { // New Transition
					return workflowForm;
				}
			} else {
				workflow = workflowManager.getWorkFlow(lWorkflowId);
				workflowForm.setWorkflow(workflow);
				if (StringUtils.isNotBlank(workflowTransitionId)) {
					Long iWorkflowTransitonid = Long
							.parseLong(workflowTransitionId);
					transition = workflowManager
							.getWorkflowTransition(iWorkflowTransitonid);
					if (transition != null) {
						workflowForm.setWorkflowTransition(transition);
					}
				}
				return workflowForm;
			}
		} else {
			return workflowForm;
		}

		return super.formBackingObject(request);
	}

	private WorkflowForm populateWorkflowFormWithTransition(
			WorkflowForm workflowForm, WorkflowTransition transition) {

		workflowForm.setWorkflowTransition(transition);
		workflowForm.setCurrentStatusCd(transition.getCurrentStatus()
				.getStatusCd());
		workflowForm.setTransitionStatusCd(transition.getTransitionStatus()
				.getStatusCd());
		workflowForm.setCurrentUserType(transition.getCurrentUserType()
				.getUserTypeCd());
		workflowForm.setTransitionUserType(transition.getTransitionToUserType()
				.getUserTypeCd());
		workflowForm.setTask(transition.getWorkflowTask().getTaskCd());
		workflowForm.setWorkflowTransitionId(transition
				.getWorkflowTransitionId());
		workflowForm.setTransitionName(transition.getTransitionName());
		workflowForm.setTransitionDescription(transition.getDescription());
		workflowForm.setNumberOfDaysToCompleteTask(transition
				.getNumberOfDaysToCompleteTask());

		return workflowForm;
	}

	protected boolean isAddTransition(HttpServletRequest request) {
		String method = request.getParameter("action");
		return (method != null && method.equalsIgnoreCase("addTransition"));
	}

	private boolean isEditTransition(HttpServletRequest request) {
		String method = request.getParameter("action");
		return (method != null && method.equalsIgnoreCase("editTransition"));
	}

}
