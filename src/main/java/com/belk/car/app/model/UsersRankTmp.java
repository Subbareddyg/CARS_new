package com.belk.car.app.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the USERS_RANK_TMP database table.
 * 
 */
@Entity
@Table(name="USERS_RANK_TMP")
public class UsersRankTmp implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DEPARTMENT_CODE")
	private long departmentCode;

	@Column(name="DC_DESC")
	private String dcDesc;

	@Column(name="DEMAND_CENTER")
	private String demandCenter;

	@Column(name="DMM_EMAIL")
	private String dmmEmail;

	@Column(name="DMM_NAME")
	private String dmmName;

	@Column(name="GMM_EMAIL")
	private String gmmEmail;

	@Column(name="GMM_NAME")
	private String gmmName;

	public UsersRankTmp() {
	}

	public long getDepartmentCode() {
		return this.departmentCode;
	}

	public void setDepartmentCode(long departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getDcDesc() {
		return this.dcDesc;
	}

	public void setDcDesc(String dcDesc) {
		this.dcDesc = dcDesc;
	}

	public String getDemandCenter() {
		return this.demandCenter;
	}

	public void setDemandCenter(String demandCenter) {
		this.demandCenter = demandCenter;
	}

	public String getDmmEmail() {
		return this.dmmEmail;
	}

	public void setDmmEmail(String dmmEmail) {
		this.dmmEmail = dmmEmail;
	}

	public String getDmmName() {
		return this.dmmName;
	}

	public void setDmmName(String dmmName) {
		this.dmmName = dmmName;
	}

	public String getGmmEmail() {
		return this.gmmEmail;
	}

	public void setGmmEmail(String gmmEmail) {
		this.gmmEmail = gmmEmail;
	}

	public String getGmmName() {
		return this.gmmName;
	}

	public void setGmmName(String gmmName) {
		this.gmmName = gmmName;
	}

}