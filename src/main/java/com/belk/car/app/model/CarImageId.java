package com.belk.car.app.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CarImageId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 166230878679123530L;
	private long carId;
	private long imageId;

	public CarImageId() {
	}

	public CarImageId(long carId, long imageId) {
		this.carId = carId;
		this.imageId = imageId;
	}

	@Column(name = "CAR_ID", nullable = false, precision = 12, scale = 0)
	public long getCarId() {
		return this.carId;
	}

	public void setCarId(long carId) {
		this.carId = carId;
	}

	@Column(name = "IMAGE_ID", nullable = false, precision = 12, scale = 0)
	public long getImageId() {
		return this.imageId;
	}

	public void setImageId(long imageId) {
		this.imageId = imageId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CarImageId))
			return false;
		CarImageId castOther = (CarImageId) other;

		return (this.getCarId() == castOther.getCarId())
				&& (this.getImageId() == castOther.getImageId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getCarId();
		result = 37 * result + (int) this.getImageId();
		return result;
	}

}
