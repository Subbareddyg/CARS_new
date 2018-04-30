package com.belk.car.app.model.report;

import java.io.Serializable;

public class CarSummaryData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4803559526216800853L;

	private String deptCd;
	private String deptName;
	private String workflowStatusName;
	private String workflowStatusCd;
	private Integer count;

	public String getDeptCd() {
		return deptCd;
	}

	public void setDeptCd(String deptCd) {
		this.deptCd = deptCd;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getWorkflowStatusName() {
		return workflowStatusName;
	}

	public void setWorkflowStatusName(String workflowStatusName) {
		this.workflowStatusName = workflowStatusName;
	}

	public String getWorkflowStatusCd() {
		return workflowStatusCd;
	}

	public void setWorkflowStatusCd(String workflowStatusCd) {
		this.workflowStatusCd = workflowStatusCd;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}
