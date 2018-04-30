package com.belk.car.app.dto;

import java.io.Serializable;

public class ChildCarDTO implements Serializable {

	private static final long serialVersionUID = 499129990081639843L;
	
	private long carId;
	private String order;
	private String skuCode;
	
	public long getCarId() {
		return carId;
	}
	public void setCarId(long carId) {
		this.carId = carId;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

}
