package com.belk.car.app.dto;

import java.io.Serializable;

public class UserRankDTO implements Serializable {

	private long deptNo;
	private String dmmName;
	private String dmmEmail;
	private String gmmName;
	private String gmmEmail;
	private String demandCenter;

	public long getDeptNo() {
		return deptNo;
	}

	public void setDeptNo(long deptNo) {
		this.deptNo = deptNo;
	}

	public String getDmmName() {
		return dmmName;
	}

	public void setDmmName(String dmmName) {
		this.dmmName = dmmName;
	}

	public String getDmmEmail() {
		return dmmEmail;
	}

	public void setDmmEmail(String dmmEmail) {
		this.dmmEmail = dmmEmail;
	}

	public String getGmmName() {
		return gmmName;
	}

	public void setGmmName(String gmmName) {
		this.gmmName = gmmName;
	}

	public String getGmmEmail() {
		return gmmEmail;
	}

	public void setGmmEmail(String gmmEmail) {
		this.gmmEmail = gmmEmail;
	}

	public String getDemandCenter() {
		return demandCenter;
	}

	public void setDemandCenter(String demandCenter) {
		this.demandCenter = demandCenter;
	}

	public String getDcDesc() {
		return dcDesc;
	}

	public void setDcDesc(String dcDesc) {
		this.dcDesc = dcDesc;
	}

	private String dcDesc;
	/*
	 * private static transient final Log log = LogFactory
	 * .getLog(UserRankDTO.class);
	 */
}
