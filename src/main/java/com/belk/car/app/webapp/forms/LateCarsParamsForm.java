package com.belk.car.app.webapp.forms;

public class LateCarsParamsForm {

	private String workflowStatus;
	private String currentUserType;
	private String weeksDue;
	private Long lateCarsGroupId;
	private Long lateCarsParamId;
	
	public Long getLateCarsParamId() {
		return lateCarsParamId;
	}
	public void setLateCarsParamId(Long lateCarsParamId) {
		this.lateCarsParamId = lateCarsParamId;
	}
	public Long getLateCarsGroupId() {
		return lateCarsGroupId;
	}
	public void setLateCarsGroupId(Long lateCarsGroupId) {
		this.lateCarsGroupId = lateCarsGroupId;
	}
	public String getWorkflowStatus() {
		return workflowStatus;
	}
	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}
	public String getCurrentUserType() {
		return currentUserType;
	}
	public void setCurrentUserType(String currentUserType) {
		this.currentUserType = currentUserType;
	}
	public String getWeeksDue() {
		return weeksDue;
	}
	public void setWeeksDue(String weeksDue) {
		this.weeksDue = weeksDue;
	}
}
