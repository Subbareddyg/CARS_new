/**
 * 
 */
package com.belk.car.app.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author afusy01
 *
 */
public class CarsDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 509429990081639843L;
	
	private Long carId;
	private String source;
	private String sourceName;
	private String deptNo;
	private String vendor;
	private String vendorNo;
	private String style;
	private String styleNo;
	private String requestType;
	private String status;
	private String assignedTo;
	private String dueDate;
	private String dueFlag;
	private String completionDate;
	private Date dueDatte;
	private Date completionDatte;
	private String lastUpdatedBy;
	private String classi;
	private String contentStatus;
	private String workFlowStatus;
	private String styleDesc;
	private String productType;
	private String patternType;
	private String userTypeCd;
	private String styleTypeCd;
	private String canDoUrgent;
	private String lock;
	private String setEdit;
	private String archived;
	private String readyToSendToCMPFlag;
	private String statusDueFlag;
	private String buyerApprovalFlag;//added for VIP
	private String strImmediate;//added for VIP
	private String strImmediateFlag;//added for VIP
	
	public CarsDTO(){
		buyerApprovalFlag ="";
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
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the deptNo
	 */
	public String getDeptNo() {
		return deptNo;
	}
	/**
	 * @param deptNo the deptNo to set
	 */
	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}
	/**
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}
	/**
	 * @param vendor the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}
	/**
	 * @param style the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}
	/**
	 * @return the requestType
	 */
	public String getRequestType() {
		return requestType;
	}
	/**
	 * @param requestType the requestType to set
	 */
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the assignedTo
	 */
	public String getAssignedTo() {
		return assignedTo;
	}
	/**
	 * @param assignedTo the assignedTo to set
	 */
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	/**
	 * @return the dueDate
	 */
	public String getDueDate() {
		return dueDate;
	}
	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	/**
	 * @return the completionDate
	 */
	public String getCompletionDate() {
		return completionDate;
	}
	/**
	 * @param completionDate the completionDate to set
	 */
	public void setCompletionDate(String completionDate) {
		this.completionDate = completionDate;
	}
	public Date getDueDatte() {
		return dueDatte;
	}
	public void setDueDatte(Date dueDatte) {
		this.dueDatte = dueDatte;
	}
	public Date getCompletionDatte() {
		return completionDatte;
	}
	public void setCompletionDatte(Date completionDatte) {
		this.completionDatte = completionDatte;
	}
	/**
	 * @return the lastUpdatedBy
	 */
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	/**
	 * @param lastUpdatedBy the lastUpdatedBy to set
	 */
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	/**
	 * @return the vendorNo
	 */
	public String getVendorNo() {
		return vendorNo;
	}
	/**
	 * @param vendorNo the vendorNo to set
	 */
	public void setVendorNo(String vendorNo) {
		this.vendorNo = vendorNo;
	}
	/**
	 * @return the styleNo
	 */
	public String getStyleNo() {
		return styleNo;
	}
	/**
	 * @param styleNo the styleNo to set
	 */
	public void setStyleNo(String styleNo) {
		this.styleNo = styleNo;
	}
	/**
	 * @return the classi
	 */
	public String getClassi() {
		return classi;
	}
	/**
	 * @param classi the classi to set
	 */
	public void setClassi(String classi) {
		this.classi = classi;
	}
	/**
	 * @return the contentStatus
	 */
	public String getContentStatus() {
		return contentStatus;
	}
	/**
	 * @param contentStatus the contentStatus to set
	 */
	public void setContentStatus(String contentStatus) {
		this.contentStatus = contentStatus;
	}
	/**
	 * @return the workFlowStatus
	 */
	public String getWorkFlowStatus() {
		return workFlowStatus;
	}
	/**
	 * @param workFlowStatus the workFlowStatus to set
	 */
	public void setWorkFlowStatus(String workFlowStatus) {
		this.workFlowStatus = workFlowStatus;
	}
	/**
	 * @return the styleDesc
	 */
	public String getStyleDesc() {
		return styleDesc;
	}
	/**
	 * @param styleDesc the styleDesc to set
	 */
	public void setStyleDesc(String styleDesc) {
		this.styleDesc = styleDesc;
	}
	/**
	 * @return the productType
	 */
	public String getProductType() {
		return productType;
	}
	/**
	 * @param productType the productType to set
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}
	/**
	 * @return the patternType
	 */
	public String getPatternType() {
		return patternType;
	}
	/**
	 * @param patternType the patternType to set
	 */
	public void setPatternType(String patternType) {
		this.patternType = patternType;
	}
	/**
	 * @return the userTypeCd
	 */
	public String getUserTypeCd() {
		return userTypeCd;
	}
	/**
	 * @param userTypeCd the userTypeCd to set
	 */
	public void setUserTypeCd(String userTypeCd) {
		this.userTypeCd = userTypeCd;
	}
	/**
	 * @param styleTypeCd the styleTypeCd to set
	 */
	public void setStyleTypeCd(String styleTypeCd) {
		this.styleTypeCd = styleTypeCd;
	}
	/**
	 * @return the styleTypeCd
	 */
	public String getStyleTypeCd() {
		return styleTypeCd;
	}
	/**
	 * @param canDoUrgent the canDoUrgent to set
	 */
	public void setCanDoUrgent(String canDoUrgent) {
		this.canDoUrgent = canDoUrgent;
	}
	/**
	 * @return the canDoUrgent
	 */
	public String getCanDoUrgent() {
		return canDoUrgent;
	}
	/**
	 * @param sourceName the sourceName to set
	 */
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	/**
	 * @return the sourceName
	 */
	public String getSourceName() {
		return sourceName;
	}
	/**
	 * @param lockUnlock the lockUnlock to set
	 */
	public void setLock(String lock) {
		this.lock = lock;
	}
	/**
	 * @return the lockUnlock
	 */
	public String getLock() {
		return lock;
	}
	/**
	 * @param setEdit the setEdit to set
	 */
	public void setSetEdit(String setEdit) {
		this.setEdit = setEdit;
	}
	/**
	 * @return the setEdit
	 */
	public String getSetEdit() {
		return setEdit;
	}
	/**
	 * @param archived the archived to set
	 */
	public void setArchived(String archived) {
		this.archived = archived;
	}
	/**
	 * @return the archived
	 */
	public String getArchived() {
		return archived;
	}
	
	public String getDueFlag() {
		return dueFlag;
	}
	public void setDueFlag(String dueFlag) {
		this.dueFlag = dueFlag;
	}
	
	/**
	 * Flag to check if outfit car is ready to send to CMP(based on child cars status)
	 * @return
	 */
	public String getReadyToSendToCMPFlag() {
		return readyToSendToCMPFlag;
	}
	public void setReadyToSendToCMPFlag(String readyToSendToCMPFlag) {
		this.readyToSendToCMPFlag = readyToSendToCMPFlag;
	}
	
	//Added a flag to show yellow color on dashboard if car is due within next 2 days
	public String getStatusDueFlag() {
		return statusDueFlag;
	}
	public void setStatusDueFlag(String statusDueFlag) {
		this.statusDueFlag = statusDueFlag;
	}
	public String getBuyerApprovalFlag() {
		return buyerApprovalFlag;
	}
	public void setBuyerApprovalFlag(String buyerApprovalFlag) {
		this.buyerApprovalFlag = buyerApprovalFlag;
	}
	public String getStrImmediate() {
		return strImmediate;
	}
	public void setStrImmediate(String strImmediate) {
		this.strImmediate = strImmediate;
	}
	public String getStrImmediateFlag() {
		return strImmediateFlag;
	}
	public void setStrImmediateFlag(String strImmediateFlag) {
		this.strImmediateFlag = strImmediateFlag;
	}

}
