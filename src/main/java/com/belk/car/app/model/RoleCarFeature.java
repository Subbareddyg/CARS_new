package com.belk.car.app.model;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.appfuse.model.Role;

@Entity
@Table(name = "ROLE_CAR_FEATURE")
public class RoleCarFeature extends BaseAuditableModel implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8982752193276120183L;
	private RoleCarFeatureId id;
	private Role role;
	private CarFeature carFeature;
	private AccessType accessType;

	public RoleCarFeature() {
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "roleCd", column = @Column(name = "ROLE_CD", nullable = false, precision = 12, scale = 0)),
			@AttributeOverride(name = "carFeatureCd", column = @Column(name = "CAR_FEATURE_CD", nullable = false, length = 20)) })
	public RoleCarFeatureId getId() {
		return this.id;
	}

	public void setId(RoleCarFeatureId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLE_CD", nullable = false, insertable = false, updatable = false)
	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CAR_FEATURE_CD", nullable = false, insertable = false, updatable = false)
	public CarFeature getCarFeature() {
		return this.carFeature;
	}

	public void setCarFeature(CarFeature carFeature) {
		this.carFeature = carFeature;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACCESS_TYPE_CD", nullable = false)
	public AccessType getAccessType() {
		return this.accessType;
	}

	public void setAccessType(AccessType accessType) {
		this.accessType = accessType;
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
