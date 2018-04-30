package com.belk.car.app.model;


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;




/**
 * The persistent class for the USERS_RANK database table.
 * 
 */
@Entity
@Table(name="USERS_RANK",uniqueConstraints = @UniqueConstraint(columnNames = "DEPARTMENT_CODE"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,include="all")
public class UsersRank extends BaseAuditableModel implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private long departmentCode;
	private String dcDesc;
	private String demandCenter;
	private String dmmEmail;
	private String dmmName;
	private String gmmEmail;
	private String gmmName;
	
	public UsersRank() {
	}



	@Id
	@Column(name="DEPARTMENT_CODE", unique = true, nullable = false, precision = 12, scale = 0)
	public long getDepartmentCode() {
		return this.departmentCode;
	}

	public void setDepartmentCode(long departmentCode) {
		this.departmentCode = departmentCode;
	}

	@Column(name="DC_DESC", nullable = true, length = 25)
	public String getDcDesc() {
		return this.dcDesc;
	}

	public void setDcDesc(String dcDesc) {
		this.dcDesc = dcDesc;
	}
	@Column(name="DEMAND_CENTER", nullable = true, length = 3)
	public String getDemandCenter() {
		return this.demandCenter;
	}

	public void setDemandCenter(String demandCenter) {
		this.demandCenter = demandCenter;
	}
	
	@Column(name="DMM_EMAIL",nullable = false, length = 200)
	public String getDmmEmail() {
		return this.dmmEmail;
	}

	public void setDmmEmail(String dmmEmail) {
		this.dmmEmail = dmmEmail;
	}

	@Column(name="DMM_NAME",nullable = false, length = 100)
	public String getDmmName() {
		return this.dmmName;
	}

	public void setDmmName(String dmmName) {
		this.dmmName = dmmName;
	}

	@Column(name="GMM_EMAIL",nullable = false, length = 200)
	public String getGmmEmail() {
		return this.gmmEmail;
	}

	public void setGmmEmail(String gmmEmail) {
		this.gmmEmail = gmmEmail;
	}
	
	@Column(name="GMM_NAME",nullable = false, length = 100)
	public String getGmmName() {
		return this.gmmName;
	}

	public void setGmmName(String gmmName) {
		this.gmmName = gmmName;
	}	



  @Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
	return this.createdBy;
	}

	@Column(name = "UPDATED_BY", nullable = false, length = 100)
	public String getUpdatedBy() {
	return this.updatedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	public Date getCreatedDate() {
	return this.createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
	return this.updatedDate;	
	}

}