package com.belk.car.app.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "DEPARTMENT", uniqueConstraints = @UniqueConstraint(columnNames = "DEPT_CD"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class Department extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7372037522429255175L;
	private long deptId;
	private String name;
	private String descr;
	private String deptCd;
	private String statusCd;

	//private Set<Classification> classifications = new HashSet<Classification>(0);
	private Set<DepartmentAttribute> departmentAttributes = new HashSet<DepartmentAttribute>(
			0);

	public Department() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEPARTMENT_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "DEPARTMENT_SEQ_GEN", sequenceName = "DEPARTMENT_SEQ", allocationSize = 1)
	@Column(name = "DEPT_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getDeptId() {
		return this.deptId;
	}

	public void setDeptId(long deptId) {
		this.deptId = deptId;
	}

	@Column(name = "NAME", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCR", length = 2000)
	public String getDescription() {
		return this.descr;
	}

	public void setDescription(String descr) {
		this.descr = descr;
	}

	@Column(name = "DEPT_CD", unique = true, nullable = false, length = 100)
	public String getDeptCd() {
		return this.deptCd;
	}

	public void setDeptCd(String deptCd) {
		this.deptCd = deptCd;
	}

	@Column(name = "STATUS_CD", nullable = false, length = 20)
	public String getStatusCd() {
		return this.statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
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

/*
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "department")
	public Set<Car> getCars() {
		return this.cars;
	}

	public void setCars(Set<Car> cars) {
		this.cars = cars;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "department")
	public Set<Classification> getClassifications() {
		return this.classifications;
	}

	public void setClassifications(Set<Classification> classifications) {
		this.classifications = classifications;
	}

*/

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "department")
	public Set<DepartmentAttribute> getDepartmentAttributes() {
		return this.departmentAttributes;
	}

	public void setDepartmentAttributes(
			Set<DepartmentAttribute> departmentAttributes) {
		this.departmentAttributes = departmentAttributes;
	}

	@Transient
	public String getDeptIdAsString() {
		return String.valueOf(deptId);
	}
	
	@Transient
	public String getDescriptiveName() {		
		return deptCd + " - " + name ;
	}
	


	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		// object must be Test at this point
		Department dept = (Department) obj;
		return deptId == dept.deptId && (deptCd == dept.deptCd || (deptCd != null && deptCd.equals(dept.deptCd)));
	}

	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + ((int)(deptId ^ (deptId >>> 32)));
		hash = 31 * hash + (null == deptCd ? 0 : deptCd.hashCode());
		return hash;
	}

}
