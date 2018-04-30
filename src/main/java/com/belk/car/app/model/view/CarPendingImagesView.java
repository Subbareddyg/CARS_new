package com.belk.car.app.model.view;

import java.io.Serializable;
import java.util.Date;

public class CarPendingImagesView implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7134095472520600165L;
	
	
	private Long carId;
	private String deptCd;
	private String vendorNumber;
	private String styleNumber;
	private String workflowStatus;
	private Date expectedShipDate;
	private String contentStatus;
	private String imageProvider;
	private Integer numSampleRequested;
	private Integer numSampleWithoutImage;
	private Integer numSampleFromStudio;
	private Integer numPickupRequested;
	private Integer numPickupReceived;
	private Integer numSkus;

	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId ;//Long.parseLong(carId.toString());
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

	public String getStyleNumber() {
		return styleNumber;
	}

	public void setStyleNumber(String styleNumber) {
		this.styleNumber = styleNumber;
	}

	public String getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	public Date getExpectedShipDate() {
		return expectedShipDate;
	}

	public void setExpectedShipDate(Date expectedShipDate) {
		this.expectedShipDate = expectedShipDate;
	}

	public String getContentStatus() {
		return contentStatus;
	}

	public void setContentStatus(String contentStatus) {
		this.contentStatus = contentStatus;
	}

	public String getImageProvider() {
		return imageProvider;
	}

	public void setImageProvider(String imageProvider) {
		this.imageProvider = imageProvider;
	}

	public Integer getNumSampleRequested() {
		return numSampleRequested;
	}

	public void setNumSampleRequested(Integer numSampleRequested) {
		this.numSampleRequested = numSampleRequested;
	}

	public Integer getNumSampleWithoutImage() {
		return numSampleWithoutImage;
	}

	public void setNumSampleWithoutImage(Integer numSampleWithoutImage) {
		this.numSampleWithoutImage = numSampleWithoutImage;
	}

	public Integer getNumSampleFromStudio() {
		return numSampleFromStudio;
	}

	public void setNumSampleFromStudio(Integer numSampleFromStudio) {
		this.numSampleFromStudio = numSampleFromStudio;
	}

	public Integer getNumPickupRequested() {
		return numPickupRequested;
	}

	public void setNumPickupRequested(Integer numPickupRequested) {
		this.numPickupRequested = numPickupRequested;
	}

	public Integer getNumPickupReceived() {
		return numPickupReceived;
	}

	public void setNumPickupReceived(Integer numPickupReceived) {
		this.numPickupReceived = numPickupReceived;
	}

	public Integer getNumSkus() {
		return numSkus;
	}

	public void setNumSkus(Integer numSkus) {
		this.numSkus = numSkus;
	}

}
