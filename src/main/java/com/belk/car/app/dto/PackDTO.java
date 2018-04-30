package com.belk.car.app.dto;

import java.io.Serializable;

public class PackDTO implements Serializable{
	
	private static final long serialVersionUID = 2306241374545762982L;

	private String vendorNumber;
	private String vendorStyleNumber;
	private String colorDescription;
	private String sizeDescription;
	public String getVendorNumber() {
		return vendorNumber;
	}
	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}
	public String getVendorStyleNumber() {
		return vendorStyleNumber;
	}
	public void setVendorStyleNumber(String vendorStyleNumber) {
		this.vendorStyleNumber = vendorStyleNumber;
	}
	public String getColorDescription() {
		return colorDescription;
	}
	public void setColorDescription(String colorDescription) {
		this.colorDescription = colorDescription;
	}
	public String getSizeDescription() {
		return sizeDescription;
	}
	public void setSizeDescription(String sizeDescription) {
		this.sizeDescription = sizeDescription;
	}

}
