package com.belk.car.app.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "CAR_FEATURE")
public class CarFeature extends BaseAuditableModel implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7261673214154628362L;
	private String carFeatureCd;
	private String name;
	private String descr;

	private Set<RoleCarFeature> roleCarFeatures = new HashSet<RoleCarFeature>(0);

	public CarFeature() {
	}

	@Id
	@Column(name = "CAR_FEATURE_CD", unique = true, nullable = false, length = 20)
	public String getCarFeatureCd() {
		return this.carFeatureCd;
	}

	public void setCarFeatureCd(String carFeatureCd) {
		this.carFeatureCd = carFeatureCd;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "carFeature")
	public Set<RoleCarFeature> getRoleCarFeatures() {
		return this.roleCarFeatures;
	}

	public void setRoleCarFeatures(Set<RoleCarFeature> roleCarFeatures) {
		this.roleCarFeatures = roleCarFeatures;
	}

}
