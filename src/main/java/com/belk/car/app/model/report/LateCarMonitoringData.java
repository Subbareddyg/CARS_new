package com.belk.car.app.model.report;

import java.io.Serializable;
import java.util.Date;

public class LateCarMonitoringData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1357400261352134904L;
	
	
	
	private String deptCd;
	private Long carId;
	private String workflowStatus;
	private String vendorName;
	private String vendorNumber;
	private String brand;
	private String expectedShipDate;
	private String carGeneratedDate;
	private String assignedToUser;
	private String dmmName;
	private String gmmName;
	private Integer daysWithArtDirector;
	private Integer daysWithBuyerIntiated;
	private Integer daysWithBuyerReview;
	private Integer daysWithContentManager;
	private Integer daysWithContentWriter;
	private Integer daysWitheCommOperation;
	private Integer daysWithFinance;
	private Integer daysWithSampleCoordinator;
	private Integer daysWithVendor;
	private Integer daysWithBuyerWaitingSamples;
	private Integer daysWithBuyerNeedMoreInfo;
	private Integer daysWithVendorProvidedImage;

	
	public LateCarMonitoringData(){
		daysWithArtDirector = 0;
		daysWithBuyerIntiated = 0;
		daysWithBuyerReview = 0;
		daysWithContentManager = 0;
		daysWithContentWriter = 0;
		daysWitheCommOperation = 0;
		daysWithBuyerIntiated = 0;
		daysWithBuyerReview = 0;
		daysWithSampleCoordinator =0;
		daysWithVendor = 0;
		daysWithFinance = 0;
		daysWithBuyerWaitingSamples = 0;
		daysWithBuyerNeedMoreInfo = 0;
		daysWithVendorProvidedImage=0;
	}
	/**
	 * @return the carId
	 */
	public Long getCarId() {
		return carId;
	}
	/**
	 * @param carId the carId to set
	 */
	public void setCarId(Long carId) {
		this.carId = carId;
	}
	/**
	 * @return the deptCd
	 */
	public String getDeptCd() {
		return deptCd;
	}
	/**
	 * @param deptCd the deptCd to set
	 */
	public void setDeptCd(String deptCd) {
		this.deptCd = deptCd;
	}
	/**
	 * @return the workflowStatus
	 */
	public String getWorkflowStatus() {
		return workflowStatus;
	}
	/**
	 * @param workflowStatus the workflowStatus to set
	 */
	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}
	/**
	 * @return the vendorName
	 */
	public String getVendorName() {
		return vendorName;
	}
	/**
	 * @param vendorName the vendorName to set
	 */
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	/**
	 * @return the vendorNumber
	 */
	public String getVendorNumber() {
		return vendorNumber;
	}
	/**
	 * @param vendorNumber the vendorNumber to set
	 */
	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}
	/**
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}
	/**
	 * @param brand the brand to set
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}

	
	/**
	 * @return the assignedToUser
	 */
	public String getAssignedToUser() {
		return assignedToUser;
	}
	/**
	 * @param assignedToUser the assignedToUser to set
	 */
	public void setAssignedToUser(String assignedToUser) {
		this.assignedToUser = assignedToUser;
	}
	/**
	 * @return the gmmName
	 */
	public String getGmmName() {
		return gmmName;
	}
	/**
	 * @param gmmName the gmmName to set
	 */
	public void setGmmName(String gmmName) {
		this.gmmName = gmmName;
	}
	/**
	 * @return the dmmName
	 */
	public String getDmmName() {
		return dmmName;
	}
	
	/**
	 * @return the expectedShipDate
	 */
	public String getExpectedShipDate() {
		return expectedShipDate;
	}
	/**
	 * @param expectedShipDate the expectedShipDate to set
	 */
	public void setExpectedShipDate(String expectedShipDate) {
		this.expectedShipDate = expectedShipDate;
	}
	/**
	 * @return the carGeneratedDate
	 */
	public String getCarGeneratedDate() {
		return carGeneratedDate;
	}
	/**
	 * @param carGeneratedDate the carGeneratedDate to set
	 */
	public void setCarGeneratedDate(String carGeneratedDate) {
		this.carGeneratedDate = carGeneratedDate;
	}
	/**
	 * @param dmmName the dmmName to set
	 */
	public void setDmmName(String dmmName) {
		this.dmmName = dmmName;
	}
	/**
	 * @return the daysWithArtDirector
	 */
	public Integer getDaysWithArtDirector() {
		return daysWithArtDirector;
	}
	
	/**
	 * @return the daysWithBuyerIntiated
	 */
	public Integer getDaysWithBuyerIntiated() {
		return daysWithBuyerIntiated;
	}
	/**
	 * @param daysWithBuyerIntiated the daysWithBuyerIntiated to set
	 */
	public void setDaysWithBuyerIntiated(Integer daysWithBuyerIntiated) {
		this.daysWithBuyerIntiated = daysWithBuyerIntiated;
	}
	/**
	 * @return the daysWithBuyerReview
	 */
	public Integer getDaysWithBuyerReview() {
		return daysWithBuyerReview;
	}
	/**
	 * @param daysWithBuyerReview the daysWithBuyerReview to set
	 */
	public void setDaysWithBuyerReview(Integer daysWithBuyerReview) {
		this.daysWithBuyerReview = daysWithBuyerReview;
	}
	/**
	 * @param daysWithArtDirector the daysWithArtDirector to set
	 */
	public void setDaysWithArtDirector(Integer daysWithArtDirector) {
		this.daysWithArtDirector = daysWithArtDirector;
	}
	/**
	 * @return the daysWithContentManager
	 */
	public Integer getDaysWithContentManager() {
		return daysWithContentManager;
	}
	/**
	 * @param daysWithContentManager the daysWithContentManager to set
	 */
	public void setDaysWithContentManager(Integer daysWithContentManager) {
		this.daysWithContentManager = daysWithContentManager;
	}
	/**
	 * @return the daysWithContentWriter
	 */
	public Integer getDaysWithContentWriter() {
		return daysWithContentWriter;
	}
	/**
	 * @param daysWithContentWriter the daysWithContentWriter to set
	 */
	public void setDaysWithContentWriter(Integer daysWithContentWriter) {
		this.daysWithContentWriter = daysWithContentWriter;
	}
	/**
	 * @return the daysWitheCommOperation
	 */
	public Integer getDaysWitheCommOperation() {
		return daysWitheCommOperation;
	}
	/**
	 * @param daysWitheCommOperation the daysWitheCommOperation to set
	 */
	public void setDaysWitheCommOperation(Integer daysWitheCommOperation) {
		this.daysWitheCommOperation = daysWitheCommOperation;
	}
	/**
	 * @return the daysWithFinance
	 */
	public Integer getDaysWithFinance() {
		return daysWithFinance;
	}
	/**
	 * @param daysWithFinance the daysWithFinance to set
	 */

	public void setDaysWithFinance(Integer daysWithFinance) {
		this.daysWithFinance = daysWithFinance;
	}
	/**
	 * @return the daysWithSampleCoordinator
	 */
	public Integer getDaysWithSampleCoordinator() {
		return daysWithSampleCoordinator;
	}
	/**
	 * @param daysWithSampleCoordinator the daysWithSampleCoordinator to set
	 */
	public void setDaysWithSampleCoordinator(Integer daysWithSampleCoordinator) {
		this.daysWithSampleCoordinator = daysWithSampleCoordinator;
	}
	/**
	 * @return the daysWithVendor
	 */
	public Integer getDaysWithVendor() {
		return daysWithVendor;
	}
	/**
	 * @param daysWithVendor the daysWithVendor to set
	 */
	public void setDaysWithVendor(Integer daysWithVendor) {
		this.daysWithVendor = daysWithVendor;
	}
	
	/**
	 * @return the daysWithBuyerWaitingSamples
	 */
	public final Integer getDaysWithBuyerWaitingSamples() {
		return daysWithBuyerWaitingSamples;
	}
	/**
	 * @param daysWithBuyerWaitingSamples the daysWithBuyerWaitingSamples to set
	 */
	public final void setDaysWithBuyerWaitingSamples(
			Integer daysWithBuyerWaitingSamples) {
		this.daysWithBuyerWaitingSamples = daysWithBuyerWaitingSamples;
	}
	/**
	 * @return the daysWithBuyerNeedMoreInfo
	 */
	public Integer getDaysWithBuyerNeedMoreInfo() {
		return daysWithBuyerNeedMoreInfo;
	}
	/**
	 * @param daysWithBuyerNeedMoreInfo the daysWithBuyerNeedMoreInfo to set
	 */
	public void setDaysWithBuyerNeedMoreInfo(Integer daysWithBuyerNeedMoreInfo) {
		this.daysWithBuyerNeedMoreInfo = daysWithBuyerNeedMoreInfo;
	}
	/**
	 * @return the daysWithVendorProvidedImage
	 */
	public final Integer getDaysWithVendorProvidedImage(){
		return daysWithVendorProvidedImage;
	}
	/**
	 * @param daysWithVendorProvidedImage the daysWithVendorProvidedImage to set
	 */
	public void setDaysWithVendorProvidedImage(Integer daysWithVendorProvidedImage){
		this.daysWithVendorProvidedImage = daysWithVendorProvidedImage;
	}
	
}
