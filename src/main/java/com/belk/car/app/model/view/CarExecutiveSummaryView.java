package com.belk.car.app.model.view;

import java.io.Serializable;
import java.util.Date;

public class CarExecutiveSummaryView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6792825894890334362L;

	private Long carId;
	private String deptCd;
	private String deptName;
	private short classNumber;
	private String className;
	private String vendorNumber;
	private String vendorName;
	private String vendorStyleNumber;
	private String vendorStyleName;
	private String workflowStatus;
	private String assignedToUser;
	private Date expectedShipDate;
	private Date turninDate;
	private String imageProvider; //added by Santosh
	private Integer numberOfSkus;
	private String colorName; //added by AFUSYS3
	private String colorCode;

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

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
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

	public String getVendorStyleNumber() {
		return vendorStyleNumber;
	}

	public void setVendorStyleNumber(String vendorStyleNumber) {
		this.vendorStyleNumber = vendorStyleNumber;
	}

	public String getVendorStyleName() {
		return vendorStyleName;
	}

	public void setVendorStyleName(String vendorStyleName) {
		this.vendorStyleName = vendorStyleName;
	}

	public String getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	public String getAssignedToUser() {
		return assignedToUser;
	}

	public void setAssignedToUser(String assignedToUser) {
		this.assignedToUser = assignedToUser;
	}

	public Date getExpectedShipDate() {
		return expectedShipDate;
	}

	public void setExpectedShipDate(Date expectedShipDate) {
		this.expectedShipDate = expectedShipDate;
	}

	public Date getTurninDate() {
		return turninDate;
	}

	public void setTurninDate(Date turninDate) {
		this.turninDate = turninDate;
	}
	
	//added by Santosh
	public String getImageProvider() {
		return imageProvider;
	}
	//added by Santosh
	public void setImageProvider(String imageProvider) {
		this.imageProvider = imageProvider;
	}

	public Integer getNumberOfSkus() {
		return numberOfSkus;
	}

	public void setNumberOfSkus(Integer numberOfSkus) {
		this.numberOfSkus = numberOfSkus;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public short getClassNumber() {
		return this.classNumber;
	}

	public void setClassNumber(short classNumber) {
		this.classNumber = classNumber;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

}
