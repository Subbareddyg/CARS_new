package com.belk.car.app.dto;

import java.io.Serializable;

public class DepartmentClassUpload implements Serializable {

	private String demandCenterCode;
	private String deptCode;
	private String deptName;
	private String classCode;
	private String className;

	public String getDemandCenterCode() {
		return demandCenterCode;
	}

	public void setDemandCenterCode(String demandCenterCode) {
		this.demandCenterCode = demandCenterCode;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
