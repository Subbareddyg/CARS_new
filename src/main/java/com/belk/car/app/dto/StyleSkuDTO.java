/**
 * 
 */
package com.belk.car.app.dto;

import java.io.Serializable;

/**
 * @author afusy01
 *
 */
public class StyleSkuDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -953379399636660972L;
	private String styleId;
	private String styleDesc;
	private String dropShipStatus;
	private Integer noOfDropshipSkus;
	private String idbAllowDropship;
	private Integer noOfIdbDropshipSkus;
	private String styleSelected;
	
	/**Sku details*/
	private String vendorUpc;
	private String belkUpc;
	private String lastRequest;
	private String updatedDate;
	private String updatedBy;
	private String source;
	private String approvedRequest;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\nStyleID :" + this.getStyleId());
		builder.append("\nDesc :" + this.getStyleDesc());
		builder.append("\nDStatus :" + this.getDropShipStatus());
		builder.append("\nNOD :" + this.getNoOfDropshipSkus());
		builder.append("\nIStatus :" + this.getIdbAllowDropship());
		builder.append("\nNOI :" + this.getNoOfIdbDropshipSkus());
		builder.append("\nSelected :" + this.getStyleSelected());
		return builder.toString();
	}
	/**
	 * @return the styleId
	 */
	public String getStyleId() {
		return styleId;
	}
	/**
	 * @param styleId the styleId to set
	 */
	public void setStyleId(String styleId) {
		this.styleId = styleId;
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
	 * @return the dropShipStatus
	 */
	public String getDropShipStatus() {
		return dropShipStatus;
	}
	/**
	 * @param dropShipStatus the dropShipStatus to set
	 */
	public void setDropShipStatus(String dropShipStatus) {
		this.dropShipStatus = dropShipStatus;
	}
	/**
	 * @return the noOfDropshipSkus
	 */
	public Integer getNoOfDropshipSkus() {
		return noOfDropshipSkus;
	}
	/**
	 * @param noOfDropshipSkus the noOfDropshipSkus to set
	 */
	public void setNoOfDropshipSkus(Integer noOfDropshipSkus) {
		this.noOfDropshipSkus = noOfDropshipSkus;
	}
	/**
	 * @return the idbAllowDropship
	 */
	public String getIdbAllowDropship() {
		return idbAllowDropship;
	}
	/**
	 * @param idbAllowDropship the idbAllowDropship to set
	 */
	public void setIdbAllowDropship(String idbAllowDropship) {
		this.idbAllowDropship = idbAllowDropship;
	}
	/**
	 * @return the noOfIdbDropshipSkus
	 */
	public Integer getNoOfIdbDropshipSkus() {
		return noOfIdbDropshipSkus;
	}
	/**
	 * @param noOfIdbDropshipSkus the noOfIdbDropshipSkus to set
	 */
	public void setNoOfIdbDropshipSkus(Integer noOfIdbDropshipSkus) {
		this.noOfIdbDropshipSkus = noOfIdbDropshipSkus;
	}
	/**
	 * @param styleSelected the styleSelected to set
	 */
	public void setStyleSelected(String styleSelected) {
		this.styleSelected = styleSelected;
	}
	/**
	 * @return the styleSelected
	 */
	public String getStyleSelected() {
		return styleSelected;
	}
	/**
	 * @return the vendorUpc
	 */
	public String getVendorUpc() {
		return vendorUpc;
	}
	/**
	 * @param vendorUpc the vendorUpc to set
	 */
	public void setVendorUpc(String vendorUpc) {
		this.vendorUpc = vendorUpc;
	}
	/**
	 * @return the belkUpc
	 */
	public String getBelkUpc() {
		return belkUpc;
	}
	/**
	 * @param belkUpc the belkUpc to set
	 */
	public void setBelkUpc(String belkUpc) {
		this.belkUpc = belkUpc;
	}
	/**
	 * @return the lastRequest
	 */
	public String getLastRequest() {
		return lastRequest;
	}
	/**
	 * @param lastRequest the lastRequest to set
	 */
	public void setLastRequest(String lastRequest) {
		this.lastRequest = lastRequest;
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
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param approvedRequest the approvedRequest to set
	 */
	public void setApprovedRequest(String approvedRequest) {
		this.approvedRequest = approvedRequest;
	}
	/**
	 * @return the approvedRequest
	 */
	public String getApprovedRequest() {
		return approvedRequest;
	}
	
}
