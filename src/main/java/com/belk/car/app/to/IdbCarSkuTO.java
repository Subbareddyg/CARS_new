package com.belk.car.app.to;

public class IdbCarSkuTO {
	String vendorColor;
	String vendorColorName;
	String vendorSizeCode;
	String vendorSizeDesc;
	String vendorSizeTop;
	String vendorSizeSide;
	String upcAddDate;
	String belkUPC;
	String startDeliveryDate;
	String endDeliveryDate;
	String carId;
	String longSku;
	String parentUPC;
	String setFlag;
	double retailPrice=0;

	
	public String getVendorColor() {
		return vendorColor;
	}

	public void setVendorColor(String vendorColor) {
		this.vendorColor = vendorColor;
	}

	public String getVendorColorName() {
		return vendorColorName;
	}

	public void setVendorColorName(String vendorColorName) {
		this.vendorColorName = vendorColorName;
	}

	public String getVendorSizeCode() {
		return vendorSizeCode;
	}

	public void setVendorSizeCode(String vendorSizeCode) {
		this.vendorSizeCode = vendorSizeCode;
	}

	public String getVendorSizeDesc() {
		return vendorSizeDesc;
	}

	public void setVendorSizeDesc(String vendorSizeDesc) {
		this.vendorSizeDesc = vendorSizeDesc;
	}

	public String getVendorSizeTop() {
		return vendorSizeTop;
	}

	public void setVendorSizeTop(String vendorSizeTop) {
		this.vendorSizeTop = vendorSizeTop;
	}

	public String getVendorSizeSide() {
		return vendorSizeSide;
	}

	public void setVendorSizeSide(String vendorSizeSide) {
		this.vendorSizeSide = vendorSizeSide;
	}

	public String getUpcAddDate() {
		return upcAddDate;
	}

	public void setUpcAddDate(String upcAddDate) {
		this.upcAddDate = upcAddDate;
	}

	public String getBelkUPC() {
		return belkUPC;
	}

	public void setBelkUPC(String belkUPC) {
		this.belkUPC = belkUPC;
	}

	public String getStartDeliveryDate() {
		return startDeliveryDate;
	}

	public void setStartDeliveryDate(String startDeliveryDate) {
		this.startDeliveryDate = startDeliveryDate;
	}

	public String getEndDeliveryDate() {
		return endDeliveryDate;
	}

	public void setEndDeliveryDate(String endDeliveryDate) {
		this.endDeliveryDate = endDeliveryDate;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getLongSku() {
		return longSku;
	}

	public void setLongSku(String longSku) {
		this.longSku = longSku;
	}

	public String getParentUPC() {
		return parentUPC;
	}

	public void setParentUPC(String parentUPC) {
		this.parentUPC = parentUPC;
	}

	public String getSetFlag() {
		return setFlag;
	}

	public void setSetFlag(String setFlag) {
		this.setFlag = setFlag;
	}
	
	public double getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(double retailPrice) {
		this.retailPrice = retailPrice;
	}

	public String toString()
    {
        return getClass().getName() + ".belkUpc[" + this.belkUPC + "]";
    }
	
}
