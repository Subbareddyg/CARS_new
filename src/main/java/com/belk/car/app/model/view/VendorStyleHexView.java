package com.belk.car.app.model.view;

import java.io.Serializable;

public class VendorStyleHexView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -963793663441612957L;

	private String vendorNumber;
	private String styleNumber;
	private String colorCode;
	private String hexValue;

	public String getVendorNumber() {
		return vendorNumber;
	}

	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}

	public String getStyleNumber() {
		return styleNumber;
	}

	public void setStyleNumber(String styleNumber) {
		this.styleNumber = styleNumber;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public String getHexValue() {
		return hexValue;
	}

	public void setHexValue(String hexValue) {
		this.hexValue = hexValue;
	}

}
