package com.belk.car.app.webapp.controller.admin.workflow;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.AccessDeniedException;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.model.WorkFlow;
import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.service.WorkflowManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.WorkflowForm;

public class WorkflowFormController extends BaseFormController {

	private WorkflowManager workflowManager;

	/**
	 * @param workflowManager the workflowManager to set
	 */
	public void setWorkflowManager(WorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}

	public WorkflowFormController() {
		setCommandName("workflowForm");
		setCommandClass(WorkflowForm.class);
	}

	public ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		if (request.getParameter("cancel") != null) {
			if (!StringUtils.equals(request.getParameter("from"), "list")) {
				return new ModelAndView(getCancelView());
			} else {
				return new ModelAndView(getSuccessView());
			}
		}

		return super.processFormSubmission(request, response, command, errors);
	}

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		log.debug("entering WorkflowForm 'onSubmit' method...");

		WorkflowForm workflowForm = (WorkflowForm) command;
		WorkFlow workflow = null;
		Locale locale = request.getLocale();
		String workflowId = request.getParameter("id");
		Map model = new HashMap();

		try {
			workflow = workflowManager.save(populateWorkFlow(request, workflowForm));

		} catch (AccessDeniedException ade) {
			log.warn(ade.getMessage());
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (Exception e) {
			errors.rejectValue("workflow", "errors.existing.product.type",
					new Object[] { workflowForm.getName() },
					"duplicate workflow type");
			return showForm(request, response, errors);
			// return showForm(request, response, errors);
		}

		if (!StringUtils.equals(request.getParameter("from"), "list")) {
			saveMessage(request, getText("workflow.saved", workflowForm
					.getName(), locale));
		}
		model.put("id",workflow.getWorkflowId());
		return new ModelAndView(getSuccessView(),model);
	}

	private WorkFlow populateWorkFlow(HttpServletRequest request,
			WorkflowForm workflowForm) {

		//Clean and populate
		if (workflowForm.getWorkflow().getSupportedStatus() != null) {
			workflowForm.getWorkflow().getSupportedStatus().clear();
			for (String workflowStatus : workflowForm.getStatuses()) {
				workflowForm.getWorkflow().addWorkStatus(
						workflowManager.getWorkFlowStatus(workflowStatus));
			}
		}
		if(StringUtils.isNotBlank(workflowForm.getName())) {
			workflowForm.getWorkflow().setName(workflowForm.getName());
		}
		if(StringUtils.isNotBlank(workflowForm.getWorkflowType())) {
			workflowForm.getWorkflow().setWorkflowType(workflowManager.getWorkflowType(workflowForm.getWorkflowType()));
		}
		if(StringUtils.isNotBlank(workflowForm.getDescription())) {
			workflowForm.getWorkflow().setDescription(workflowForm.getDescription());
		}
		setAuditInfo(request, workflowForm.getWorkflow());
		return workflowForm.getWorkflow();
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

		WorkFlow workflow = null;
		WorkflowForm workflowForm = new WorkflowForm();
		String action = getCurrentAction(request);
		if (!isFormSubmission(request)) {
			String attrId = request.getParameter("id");
			if (!StringUtils.isBlank(attrId)
					&& !"".equals(request.getParameter("version"))) {
				workflow = workflowManager.getWorkFlow(Long.valueOf(attrId));
				if (workflow != null) {
					return populateWorkflowForm(workflowForm, workflow);
				}
			}

		} else if (request.getParameter("id") != null
				&& !"".equals(request.getParameter("id"))
				&& !request.getParameter("id").equals("0")
				&& request.getParameter("cancel") == null) {					
			workflow = workflowManager.getWorkFlow(Long.valueOf(request
					.getParameter("id")));
			workflowForm.setWorkflow(workflow);
			return workflowForm;
		} else {
			return new WorkflowForm
			();
		}
		return super.formBackingObject(request);
	}

	private WorkflowForm populateWorkflowForm(WorkflowForm workflowForm,
			WorkFlow workFlow) {
		workflowForm.setName(workFlow.getName());
		workflowForm.setDescription(workFlow.getDescription());
		workflowForm
				.setStatuses(convertStatuses(workFlow.getSupportedStatus()));
		workflowForm.setStatusCd(workFlow.getStatusCode());
		workflowForm.setWorkflowId(workFlow.getWorkflowId());
		workflowForm.setWorkflowType(workFlow.getWorkflowType().getTypeCd());
		return workflowForm;
	}

	private String[] convertStatuses(List<WorkflowStatus> supportedStatus) {
		String[] convertedValues = new String[supportedStatus.size()];
		if (supportedStatus != null && supportedStatus.size() > 0) {
			Iterator it = supportedStatus.iterator();
			for (int i = 0; it.hasNext(); i++) {
				convertedValues[i] = String
						.valueOf(((WorkflowStatus) it.next()).getStatusCd());
			}
		}
		return convertedValues;
	}

	protected boolean isAdd(HttpServletRequest request) {
		String method = request.getParameter("action");
		return (method != null && method.equalsIgnoreCase("editWorkFlow"));
	}

	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws ServletRequestBindingException {

		Map model = new HashMap();
		model.put("statuses", workflowManager.getAllWorkFlowStatuses());
		model.put("types", workflowManager.getAllWorkflowTypes());
		return model;
	}

}
