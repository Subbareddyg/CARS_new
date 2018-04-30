package com.belk.car.app.webapp.forms;

import com.belk.car.app.model.WorkFlow;
import com.belk.car.app.model.workflow.WorkflowTransition;

public class WorkflowForm {

	private WorkFlow workflow;
	private WorkflowTransition workflowTransition;
	private Long workflowId;
	private Long workflowTransitionId;
	private String name;
	private String description;
	private String classId;
	private String statusCd;
	private String action;
	private String[] statuses;
	private String currentUserType;
	private String transitionUserType;
	private String currentStatusCd;
	private String transitionStatusCd;
	private String task;
	private String workflowType;
	private String transitionName;
	private String transitionDescription;
	private int numberOfDaysToCompleteTask;
	
	
	//Default Constructor
	public WorkflowForm() { }
	
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * @return the productType
	 */
	public WorkFlow getWorkflow() {
		return workflow;
	}


	/**
	 * @param productType the productType to set
	 */
	public void setWorkflow(WorkFlow workflow) {
		this.workflow = workflow;
	}


	/**
	 * @return the classId
	 */
	public String getClassId() {
		return classId;
	}


	/**
	 * @param classId the classId to set
	 */
	public void setClassId(String classId) {
		this.classId = classId;
	}


	/**
	 * @return the statusCd
	 */
	public String getStatusCd() {
		return statusCd;
	}


	/**
	 * @param statusCd the statusCd to set
	 */
	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}


	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}


	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}


	/**
	 * @return the statuses
	 */
	public String[] getStatuses() {
		return statuses;
	}


	/**
	 * @param statuses the statuses to set
	 */
	public void setStatuses(String[] statuses) {
		this.statuses = statuses;
	}


	/**
	 * @return the currentUserType
	 */
	public String getCurrentUserType() {
		return currentUserType;
	}


	/**
	 * @param currentUserType the currentUserType to set
	 */
	public void setCurrentUserType(String currentUserType) {
		this.currentUserType = currentUserType;
	}


	/**
	 * @return the transitionUserType
	 */
	public String getTransitionUserType() {
		return transitionUserType;
	}


	/**
	 * @param transitionUserType the transitionUserType to set
	 */
	public void setTransitionUserType(String transitionUserType) {
		this.transitionUserType = transitionUserType;
	}


	/**
	 * @return the currentStatusCd
	 */
	public String getCurrentStatusCd() {
		return currentStatusCd;
	}


	/**
	 * @param currentStatusCd the currentStatusCd to set
	 */
	public void setCurrentStatusCd(String currentStatusCd) {
		this.currentStatusCd = currentStatusCd;
	}


	/**
	 * @return the transitionStatusCd
	 */
	public String getTransitionStatusCd() {
		return transitionStatusCd;
	}


	/**
	 * @param transitionStatusCd the transitionStatusCd to set
	 */
	public void setTransitionStatusCd(String transitionStatusCd) {
		this.transitionStatusCd = transitionStatusCd;
	}


	/**
	 * @return the task
	 */
	public String getTask() {
		return task;
	}


	/**
	 * @param task the task to set
	 */
	public void setTask(String task) {
		this.task = task;
	}


	/**
	 * @return the workflowTransitionId
	 */
	public Long getWorkflowTransitionId() {
		return workflowTransitionId;
	}


	/**
	 * @param workflowTransitionId the workflowTransitionId to set
	 */
	public void setWorkflowTransitionId(Long workflowTransitionId) {
		this.workflowTransitionId = workflowTransitionId;
	}

	/**
	 * @return the workflowId
	 */
	public Long getWorkflowId() {
		return workflowId;
	}

	/**
	 * @param workflowId the workflowId to set
	 */
	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}

	/**
	 * @return the workflowTransition
	 */
	public WorkflowTransition getWorkflowTransition() {
		return workflowTransition;
	}

	/**
	 * @param workflowTransition the workflowTransition to set
	 */
	public void setWorkflowTransition(WorkflowTransition workflowTransition) {
		this.workflowTransition = workflowTransition;
	}


	/**
	 * @return the workflowType
	 */
	public String getWorkflowType() {
		return workflowType;
	}


	/**
	 * @param workflowType the workflowType to set
	 */
	public void setWorkflowType(String workflowType) {
		this.workflowType = workflowType;
	}


	/**
	 * @return the transitionName
	 */
	public String getTransitionName() {
		return transitionName;
	}


	/**
	 * @param transitionName the transitionName to set
	 */
	public void setTransitionName(String transitionName) {
		this.transitionName = transitionName;
	}


	/**
	 * @return the numberOfDaysToCompleteTask
	 */
	public int getNumberOfDaysToCompleteTask() {
		return numberOfDaysToCompleteTask;
	}


	/**
	 * @param numberOfDaysToCompleteTask the numberOfDaysToCompleteTask to set
	 */
	public void setNumberOfDaysToCompleteTask(int numberOfDaysToCompleteTask) {
		this.numberOfDaysToCompleteTask = numberOfDaysToCompleteTask;
	}


	/**
	 * @return the transitionDescription
	 */
	public String getTransitionDescription() {
		return transitionDescription;
	}


	/**
	 * @param transitionDescription the transitionDescription to set
	 */
	public void setTransitionDescription(String transitionDescription) {
		this.transitionDescription = transitionDescription;
	}

}
