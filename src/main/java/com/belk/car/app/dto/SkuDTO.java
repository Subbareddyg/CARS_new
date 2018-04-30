package com.belk.car.app.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SkuDTO implements Serializable {

	private static final long serialVersionUID = 1309841791755333452L;

	String vendorUpc;
	String Description;
	String longSku;
	String belkSku;
	String styleNumber;
	// String vendorNumber;
	// String deptCode;
	// String classNumber;
	boolean isSetUPC = false;
	String parentBelkSKU;
	double retailPrice = 0;
	double unitCost=0;
	long ordQuantity=0;
	String orin;
	String colorCode;
	String colorName;
	String sizeCode;
	String sizeName;
	String setFlag;
	String packId;
	String PackUPC;
	
	
	List<AttributeDTO> attributeDTOList = new ArrayList<AttributeDTO>();

	public SkuDTO() {

	}

	public String getVendorUpc() {
		return vendorUpc;
	}

	public void setVendorUpc(String vendorUpc) {
		this.vendorUpc = vendorUpc;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getLongSku() {
		return longSku;
	}

	public void setLongSku(String longSku) {
		this.longSku = longSku;
	}

	public String getBelkSku() {
		return belkSku;
	}

	public void setBelkSku(String belkSku) {
		this.belkSku = belkSku;
	}

	public String getStyleNumber() {
		return styleNumber;
	}

	public void setStyleNumber(String styleNumber) {
		this.styleNumber = styleNumber;
	}

	public boolean isSetUPC() {
		return isSetUPC;
	}

	public void setSetUPC(boolean isSetUPC) {
		this.isSetUPC = isSetUPC;
	}

	public String getParentBelkSKU() {
		return parentBelkSKU;
	}

	public void setParentBelkSKU(String parentBelkSKU) {
		this.parentBelkSKU = parentBelkSKU;
	}

	public double getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(double retailPrice) {
		this.retailPrice = retailPrice;
	}
	
	public String getOrin() {
		return orin;
	}

	public void setOrin(String orin) {
		this.orin = orin;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getSizeCode() {
		return sizeCode;
	}

	public void setSizeCode(String sizeCode) {
		this.sizeCode = sizeCode;
	}

	public String getSetFlag() {
		return setFlag;
	}

	public void setSetFlag(String setFlag) {
		this.setFlag = setFlag;
	}

	public List<AttributeDTO> getAttributeDTOList() {
		return attributeDTOList;
	}

	public void setAttributeDTOList(List<AttributeDTO> attributeDTOList) {
		this.attributeDTOList = attributeDTOList;
	}
	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}

	public double getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(double unitCost) {
		this.unitCost = unitCost;
	}

	public long getOrdQuantity() {
		return ordQuantity;
	}

	public void setOrdQuantity(long ordQuantity) {
		this.ordQuantity = ordQuantity;
	}

	public String getPackId() {
		return packId;
	}

	public void setPackId(String packId) {
		this.packId = packId;
	}

	public String getPackUPC() {
		return PackUPC;
	}

	public void setPackUPC(String packUPC) {
		PackUPC = packUPC;
	}

}