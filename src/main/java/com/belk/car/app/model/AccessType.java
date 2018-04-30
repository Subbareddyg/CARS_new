package com.belk.car.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "ACCESS_TYPE")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, include = "all")
public class AccessType extends BaseAuditableModel implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4210330438497437163L;
	private String accessTypeCd;
	private String name;
	private String descr;

	//private Set<RoleCarFeature> roleCarFeatures = new HashSet<RoleCarFeature>(0);

	public AccessType() {
	}


	@Id
	@Column(name = "ACCESS_TYPE_CD", unique = true, nullable = false, length = 20)
	public String getAccessTypeCd() {
		return this.accessTypeCd;
	}

	public void setAccessTypeCd(String accessTypeCd) {
		this.accessTypeCd = accessTypeCd;
	}

	@Column(name = "NAME", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCR", nullable = false, length = 200)
	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "UPDATED_BY", nullable = false, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	/*
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "accessType")
	public Set<RoleCarFeature> getRoleCarFeatures() {
		return this.roleCarFeatures;
	}

	public void setRoleCarFeatures(Set<RoleCarFeature> roleCarFeatures) {
		this.roleCarFeatures = roleCarFeatures;
	}
	*/

}
