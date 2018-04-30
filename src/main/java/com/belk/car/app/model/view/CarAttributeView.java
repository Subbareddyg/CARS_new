package com.belk.car.app.model.view;

import java.io.Serializable;

public class CarAttributeView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8652114710048919092L;

	private Long carId;
	private String deptCd;
	private Short classNumber;
	private String productType ;
	private String vendorNumber;
	private String vendorName;
	private String styleNumber;
	private String styleName;
	private String attrName;
	private String attrValue;
	private Long attrId ;
	private String bmAttrName;

	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

	public String getDeptCd() {
		return deptCd;
	}

	public void setDeptCd(String deptCd) {
		this.deptCd = deptCd;
	}

	public String getVendorNumber() {
		return vendorNumber;
	}

	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getStyleNumber() {
		return styleNumber;
	}

	public void setStyleNumber(String styleNumber) {
		this.styleNumber = styleNumber;
	}

	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	public Long getAttrId() {
		return attrId;
	}

	public void setAttrId(Long attrId) {
		this.attrId = attrId;
	}

	public String getBmAttrName() {
		return bmAttrName;
	}

	public void setBmAttrName(String bmAttrName) {
		this.bmAttrName = bmAttrName;
	}

	public Short getClassNumber() {
		return classNumber;
	}

	public void setClassNumber(Short classNumber) {
		this.classNumber = classNumber;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

}
