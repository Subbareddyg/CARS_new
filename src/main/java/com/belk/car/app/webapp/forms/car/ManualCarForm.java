package com.belk.car.app.webapp.forms.car;

import com.belk.car.app.model.car.ManualCar;


public class ManualCarForm {

	private long manualCarId ;
	private String vendorNumber;
	private String vendorStyleNumber;
	private String colorDescription;
	private String sizeDescription;
	private String statusCd ;
	private String processStatus;
	private String postProcessInfo;
	private String action;
	private ManualCar manualCar;
	private String isPack;
	
	public long getManualCarId() {
		return manualCarId;
	}
	public void setManualCarId(long manualCarId) {
		this.manualCarId = manualCarId;
	}
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
	public String getStatusCd() {
		return statusCd;
	}
	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}
	public String getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}
	public String getPostProcessInfo() {
		return postProcessInfo;
	}
	public void setPostProcessInfo(String postProcessInfo) {
		this.postProcessInfo = postProcessInfo;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public ManualCar getManualCar() {
		return manualCar;
	}
	public void setManualCar(ManualCar manualCar) {
		this.manualCar = manualCar;
	}
	public String getIsPack() {
		return isPack;
	}
	public void setIsPack(String isPack) {
		this.isPack = isPack;
	}
	
	
	
}
