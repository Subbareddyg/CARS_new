package com.belk.car.app.service;

import java.util.List;

import org.appfuse.service.UniversalManager;

import com.belk.car.app.model.Car;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.WorkFlow;
import com.belk.car.app.model.WorkflowTransitionInfo;
import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.model.workflow.WorkflowTask;
import com.belk.car.app.model.workflow.WorkflowTransition;
import com.belk.car.app.model.workflow.WorkflowType;

public interface WorkflowManager extends UniversalManager{
	
	List<WorkFlow> getAllWorkFlows();
	
	WorkflowTask getWorkflowTask(String workflowTaskId);
	
	List<WorkflowStatus> getAllWorkFlowStatuses();

	WorkFlow getWorkFlow(Long workflowId);

	WorkFlow getWorkFlow(WorkflowType type);

	WorkFlow save(WorkFlow workFlow);
	
	WorkflowTransition saveWorkflowTransition(WorkflowTransition workflowTransition);
	
	void remove(WorkFlow workflow);
	
	WorkflowStatus getWorkFlowStatus(String workflowStatusCd);
	
	List<WorkflowTransition> getAllowedTransitionsForWorkflow(Long workflowTransitionId);
	
	List<WorkflowTask> getAllWorkFlowTasks();
	
	WorkFlow deleteWorkflowTransition(WorkFlow workflow, WorkflowTransition workflowTransition);
	
	WorkflowTransition getWorkflowTransition(Long workflowTransitionId);
	
	WorkflowType getWorkflowType(String workflowTypeCd);
	
	List<WorkflowType> getAllWorkflowTypes();
	
	void removeWorkflowType(WorkflowType workflowType);
	
	WorkflowType saveWorkflowType(WorkflowType workflowType);
	
	WorkflowTransitionInfo getNextWorkFlowTransitionInformation(UserType currentUserType, WorkflowStatus currentStatus);
	
	WorkflowTransitionInfo getNextWorkFlowTransitionInformation(String currentUserType, String currentStatus);
	
	WorkflowTransitionInfo getNextWorkFlowTransitionInformation(String currentUserType, String currentStatus, Car car);
}
