package com.belk.car.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.model.workflow.WorkflowTransition;

/**
 * 
 */

/**
 * @author antoniog
 *
 * Wrapper class to obtain information about a current user type and a current transition
 */
public class WorkflowTransitionInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5850091411756415909L;

	List<WorkflowTransition> workflowTransitionList = new ArrayList<WorkflowTransition>();

	UserType userType;

	WorkflowStatus workflowStatus;

	List<WorkflowStatus> workflowStatusList = new ArrayList<WorkflowStatus>();

	List<UserType> userTypeList = new ArrayList<UserType>();

	/**
	 * @return the userType
	 */
	public UserType getUserType() {
		return userType;
	}

	/**
	 * @param userType the userType to set
	 */
	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	/**
	 * @return the workflowStatus
	 */
	public WorkflowStatus getWorkflowStatus() {
		return workflowStatus;
	}

	/**
	 * @param workflowStatus the workflowStatus to set
	 */
	public void setWorkflowStatus(WorkflowStatus workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	/**
	 * @return the workflowStatusList
	 */
	public List<WorkflowStatus> getWorkflowStatusList() {
		return workflowStatusList;
	}

	/**
	 * @param workflowStatusList the workflowStatusList to set
	 */
	public void setWorkflowStatusList(List<WorkflowStatus> workflowStatusList) {
		this.workflowStatusList = workflowStatusList;
	}

	/**
	 * @return the userTypeList
	 */
	public List<UserType> getUserTypeList() {
		return userTypeList;
	}

	/**
	 * @param userTypeList the userTypeList to set
	 */
	public void setUserTypeList(List<UserType> userTypeList) {
		this.userTypeList = userTypeList;
	}

	/**
	 * @return the workflowTransitionList
	 */
	public List<WorkflowTransition> getWorkflowTransitionList() {
		return workflowTransitionList;
	}

	/**
	 * @param workflowTransitionList the workflowTransitionList to set
	 */
	public void setWorkflowTransitionList(
			List<WorkflowTransition> workflowTransitionList) {
		this.workflowTransitionList = workflowTransitionList;

		if (workflowTransitionList != null) {
			for (WorkflowTransition wft : workflowTransitionList) {
				if (!this.userTypeList.contains(wft.getTransitionToUserType())) {
					userTypeList.add(wft.getTransitionToUserType());
				}
				if (!this.workflowStatusList
						.contains(wft.getTransitionStatus())) {
					workflowStatusList.add(wft.getTransitionStatus());
				}

			}
		}
	}
	
	public void removeWorkflowTransitionToUserType(String UserTypeCd){
		for(WorkflowTransition wt: workflowTransitionList){
			if(UserTypeCd.equals(wt.getTransitionToUserType().getUserTypeCd())){
				workflowTransitionList.remove(wt);
				userTypeList.remove(wt.getTransitionToUserType());
				workflowStatusList.remove(wt.getTransitionStatus());
				break;
			}
		}
	}


}
