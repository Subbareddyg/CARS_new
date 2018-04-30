package com.belk.car.app.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RoleCarFeatureId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6592447985109645889L;
	private String roleCd;
	private String carFeatureCd;

	public RoleCarFeatureId() {
	}

	public RoleCarFeatureId(String roleCd, String carFeatureCd) {
		this.roleCd = roleCd;
		this.carFeatureCd = carFeatureCd;
	}

	@Column(name = "ROLE_CD", nullable = false, precision = 12, scale = 0)
	public String getRoleCd() {
		return this.roleCd;
	}

	public void setRoleCd(String roleCd) {
		this.roleCd = roleCd;
	}

	@Column(name = "CAR_FEATURE_CD", nullable = false, length = 20)
	public String getCarFeatureCd() {
		return this.carFeatureCd;
	}

	public void setCarFeatureCd(String carFeatureCd) {
		this.carFeatureCd = carFeatureCd;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof RoleCarFeatureId))
			return false;
		RoleCarFeatureId castOther = (RoleCarFeatureId) other;

		return (this.getRoleCd() == castOther.getRoleCd())
				&& ((this.getCarFeatureCd() == castOther.getCarFeatureCd()) || (this
						.getCarFeatureCd() != null
						&& castOther.getCarFeatureCd() != null && this
						.getCarFeatureCd().equals(castOther.getCarFeatureCd())));
	}

	public int hashCode() {
		int result = 17;

		//result = 37 * result + (int) this.getRoleCd();
		result = 37
				* result
				+ (getCarFeatureCd() == null ? 0 : this.getCarFeatureCd()
						.hashCode());
		return result;
	}

}
