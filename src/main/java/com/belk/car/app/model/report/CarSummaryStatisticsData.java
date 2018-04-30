package com.belk.car.app.model.report;

import java.io.Serializable;
import java.util.Date;

public class CarSummaryStatisticsData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3058879075322137441L;

	private Long carId;
	private Date expectedShipDate;
	private String deptCd;
	private String vendorStyleTypeCd;
	private String contentInProgress;
	private String initiated;
	private String finalized;
	private Integer numProductsInCar;
	private Integer numSkusInCar;
	private Integer numProductsInPattern;
	private Integer numProductColorsInCar;
	private Integer numSamplesRequested;
	private Integer numSamplesReceived;
	private Integer numImagesRequested;
	private Integer numImagesApproved;
	private Integer numPickupImagesReceived;
	private Integer numRRDImagesReceived;

	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

	public Date getExpectedShipDate() {
		return expectedShipDate;
	}

	public void setExpectedShipDate(Date expectedShipDate) {
		this.expectedShipDate = expectedShipDate;
	}

	public String getDeptCd() {
		return deptCd;
	}

	public void setDeptCd(String deptCd) {
		this.deptCd = deptCd;
	}

	public String getVendorStyleTypeCd() {
		return vendorStyleTypeCd;
	}

	public void setVendorStyleTypeCd(String vendorStyleTypeCd) {
		this.vendorStyleTypeCd = vendorStyleTypeCd;
	}

	public String getContentInProgress() {
		return contentInProgress;
	}

	public void setContentInProgress(String contentInProgress) {
		this.contentInProgress = contentInProgress;
	}

	public String getInitiated() {
		return initiated;
	}

	public void setInitiated(String initiated) {
		this.initiated = initiated;
	}

	public String getFinalized() {
		return finalized;
	}

	public void setFinalized(String finalized) {
		this.finalized = finalized;
	}

	public Integer getNumProductsInCar() {
		return numProductsInCar;
	}

	public void setNumProductsInCar(Integer numProductsInCar) {
		this.numProductsInCar = numProductsInCar;
	}

	public Integer getNumSkusInCar() {
		return numSkusInCar;
	}

	public void setNumSkusInCar(Integer numSkusInCar) {
		this.numSkusInCar = numSkusInCar;
	}

	public Integer getNumProductsInPattern() {
		return numProductsInPattern;
	}

	public void setNumProductsInPattern(Integer numProductsInPattern) {
		this.numProductsInPattern = numProductsInPattern;
	}

	public Integer getNumProductColorsInCar() {
		return numProductColorsInCar;
	}

	public void setNumProductColorsInCar(Integer numProductColorsInCar) {
		this.numProductColorsInCar = numProductColorsInCar;
	}

	public Integer getNumSamplesRequested() {
		return numSamplesRequested;
	}

	public void setNumSamplesRequested(Integer numSamplesRequested) {
		this.numSamplesRequested = numSamplesRequested;
	}

	public Integer getNumSamplesReceived() {
		return numSamplesReceived;
	}

	public void setNumSamplesReceived(Integer numSamplesReceived) {
		this.numSamplesReceived = numSamplesReceived;
	}

	public Integer getNumImagesRequested() {
		return numImagesRequested;
	}

	public void setNumImagesRequested(Integer numImagesRequested) {
		this.numImagesRequested = numImagesRequested;
	}

	public Integer getNumImagesApproved() {
		return numImagesApproved;
	}

	public void setNumImagesApproved(Integer numImagesApproved) {
		this.numImagesApproved = numImagesApproved;
	}


	public Integer getNumPickupImagesReceived() {
		return numPickupImagesReceived;
	}

	public void setNumPickupImagesReceived(Integer numPickupImagesReceived) {
		this.numPickupImagesReceived = numPickupImagesReceived;
	}

	public Integer getNumRRDImagesReceived() {
		return numRRDImagesReceived;
	}

	public void setNumRRDImagesReceived(Integer numRRDImagesReceived) {
		this.numRRDImagesReceived = numRRDImagesReceived;
	}
}
