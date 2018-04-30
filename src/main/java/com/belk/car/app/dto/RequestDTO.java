/**
 * 
 */
package com.belk.car.app.dto;

import java.io.Serializable;

/**
 * @author afusy01
 *
 */
public class RequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8392145651405527083L;
	private Long requestId;
	private String requestDesc;
	private String source;
	private String action;
	private String effectiveDate;
	private String status;
	private String createDate;
	private String createdBy;
	private String updatedDate;
	private String updatedBy;
	private String override;
	private Double unitCost;
	private Double unitFee;
	
	/**
	 * @return the requestId
	 */
	public Long getRequestId() {
		return requestId;
	}
	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}
	/**
	 * @return the requestDesc
	 */
	public String getRequestDesc() {
		return requestDesc;
	}
	/**
	 * @param requestDesc the requestDesc to set
	 */
	public void setRequestDesc(String requestDesc) {
		this.requestDesc = requestDesc;
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
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @return the effectiveDate
	 */
	public String getEffectiveDate() {
		return effectiveDate;
	}
	/**
	 * @param effectiveDate the effectiveDate to set
	 */
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
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
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the updatedDate
	 */
	public String getUpdatedDate() {
		return updatedDate;
	}
	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	/**
	 * @return the updatedBy
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}
	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	/**
	 * @param override the override to set
	 */
	public void setOverride(String override) {
		this.override = override;
	}
	/**
	 * @return the override
	 */
	public String getOverride() {
		return override;
	}
	/**
	 * @return the unitCost
	 */
	public Double getUnitCost() {
		return unitCost;
	}
	/**
	 * @param unitCost the unitCost to set
	 */
	public void setUnitCost(Double unitCost) {
		this.unitCost = unitCost;
	}
	/**
	 * @return the unitFee
	 */
	public Double getUnitFee() {
		return unitFee;
	}
	/**
	 * @param unitFee the unitFee to set
	 */
	public void setUnitFee(Double unitFee) {
		this.unitFee = unitFee;
	}
	
	
	
}
