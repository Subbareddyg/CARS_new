package com.belk.car.app.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "CAR_USER_VENDOR_DEPARTMENT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class CarUserVendorDepartment extends BaseAuditableModel implements
		java.io.Serializable {

	private static final long serialVersionUID = -7372037522429255175L;
	public static final String DELETED = "DELETED";
	private Department dept;	
	private Vendor  vendor;
	private long carUserVendorDepartmentId;
	private String statusCd;
	
	
	
	public CarUserVendorDepartment() {

	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAR_USER_VENDOR_DEPARTMENT_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CAR_USER_VENDOR_DEPARTMENT_SEQ_GEN", sequenceName = "CAR_USER_VENDOR_DEPARTMENT_SEQ", allocationSize = 1)
	@Column(name = "CAR_USER_VENDOR_DEPARTMENT_ID", unique = true, nullable = false, length = 100)
	public Long getCarUserVendorDepartmentId() {
		return this.carUserVendorDepartmentId;
	}
	
	public void setCarUserVendorDepartmentId(Long carUserVendorDepartmentId) {
		this.carUserVendorDepartmentId = carUserVendorDepartmentId;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEPT_CD", nullable = false)
	public Department getDept() {
		return dept;
	}
	
	public void setDept(Department dept) {
		this.dept = dept;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VENDOR_NUMBER", nullable = false)
	public Vendor getVendor() {
		return vendor;
	}
	
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
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
	
	
	
	@Column(name = "STATUS_CD", nullable = false, length = 20)
	public String getStatusCd() {
		return this.statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		// object must be Test at this point
		CarUserVendorDepartment carUserDepartment = (CarUserVendorDepartment) obj;
		return  dept.getDeptId() == (carUserDepartment.getDept().getDeptId())
				&& (vendor.getVendorId() == (carUserDepartment.getVendor().getVendorId()));
	}

	public int hashCode() {
		int hash = 7;
		
		hash = 31 * hash + ((int) (dept.getDeptId() ^ (dept.getDeptId() >>> 32)));
		hash = 31 * hash + ((int) (vendor.getVendorId() ^ (vendor.getVendorId() >>> 32)));
		return hash;
	}

}
