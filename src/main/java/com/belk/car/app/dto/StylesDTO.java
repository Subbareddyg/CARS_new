package com.belk.car.app.dto;

import java.io.Serializable;

public class StylesDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5145463266854728192L;
	private String skuexceptionInd;
	private Double unitCost;
	private Double unitHandlingfee;
	private Double overrideCost;
	private Double overrideFee;
	private String color;
	private String sizeDescription;
	private String vendorStyleId;
	private String styleDesc;
	private String belkUpc;
	
	/**
	 * @return the skuexceptionInd
	 */
	public String getSkuexceptionInd() {
		return skuexceptionInd;
	}
	/**
	 * @param skuexceptionInd the skuexceptionInd to set
	 */
	public void setSkuexceptionInd(String skuexceptionInd) {
		this.skuexceptionInd = skuexceptionInd;
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
	 * @return the unitHandlingfee
	 */
	public Double getUnitHandlingfee() {
		return unitHandlingfee;
	}
	/**
	 * @param unitHandlingfee the unitHandlingfee to set
	 */
	public void setUnitHandlingfee(Double unitHandlingfee) {
		this.unitHandlingfee = unitHandlingfee;
	}
	/**
	 * @return the overrideCost
	 */
	public Double getOverrideCost() {
		return overrideCost;
	}
	/**
	 * @param overrideCost the overrideCost to set
	 */
	public void setOverrideCost(Double overrideCost) {
		this.overrideCost = overrideCost;
	}
	/**
	 * @return the overrideFee
	 */
	public Double getOverrideFee() {
		return overrideFee;
	}
	/**
	 * @param overrideFee the overrideFee to set
	 */
	public void setOverrideFee(Double overrideFee) {
		this.overrideFee = overrideFee;
	}
	
	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}
	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}
	/**
	 * @return the sizeDescription
	 */
	public String getSizeDescription() {
		return sizeDescription;
	}
	/**
	 * @param sizeDescription the sizeDescription to set
	 */
	public void setSizeDescription(String sizeDescription) {
		this.sizeDescription = sizeDescription;
	}
	/**
	 * @return the vendorStyleId
	 */
	public String getVendorStyleId() {
		return vendorStyleId;
	}
	/**
	 * @param vendorStyleId the vendorStyleId to set
	 */
	public void setVendorStyleId(String vendorStyleId) {
		this.vendorStyleId = vendorStyleId;
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
	 * @param belkUpc the belkUpc to set
	 */
	public void setBelkUpc(String belkUpc) {
		this.belkUpc = belkUpc;
	}
	/**
	 * @return the belkUpc
	 */
	public String getBelkUpc() {
		return belkUpc;
	}
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("\n---------------------------------------\n");
		stringBuilder.append("\nVendor style #:");
		stringBuilder.append(this.getVendorStyleId());
		stringBuilder.append("\nColor:");
		stringBuilder.append(this.getColor());
		stringBuilder.append("\nSize:");
		stringBuilder.append(this.getSizeDescription());
		stringBuilder.append("\nSku exception indicator:");
		stringBuilder.append(this.getSkuexceptionInd());
		stringBuilder.append("\nOverride cost:");
		stringBuilder.append(this.getOverrideCost());
		stringBuilder.append("\nOverride fee:");
		stringBuilder.append(this.getOverrideFee());
		return stringBuilder.toString();
	}
}
