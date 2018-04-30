/**
 * Class Name : OMACostExceptions.java
 * 
 * Version Information : v1.0
 * 
 * Date : 12/15/2009
 * 
 * Copyright Notice :
 */
package com.belk.car.app.model.view;

import java.io.Serializable;

/**
 * CLass to store the information required for OMACostException Report.
 * @author AFUSY38
 *
 */
public class OMACostExceptions extends DropshipVendorView implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7089860478059965933L;
	private String styleNbr;
	private String styleDesc;
	private String color;
	private String itemSize;
	private String vendorUPC;
	private String belkUPC;
	private Double cost;
	private Double idbCost;
	/**
	 * @return the styleNbr
	 */
	public String getStyleNbr() {
		return styleNbr;
	}
	/**
	 * @param styleNbr the styleNbr to set
	 */
	public void setStyleNbr(String styleNbr) {
		this.styleNbr = styleNbr;
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
	 * @return the size
	 */
	public String getItemSize() {
		return itemSize;
	}
	/**
	 * @param size the size to set
	 */
	public void setItemSize(String itemSize) {
		this.itemSize = itemSize;
	}
	/**
	 * @return the vendorUPC
	 */
	public String getVendorUPC() {
		return vendorUPC;
	}
	/**
	 * @param vendorUPC the vendorUPC to set
	 */
	public void setVendorUPC(String vendorUPC) {
		this.vendorUPC = vendorUPC;
	}
	/**
	 * @return the belkUPC
	 */
	public String getBelkUPC() {
		return belkUPC;
	}
	/**
	 * @param belkUPC the belkUPC to set
	 */
	public void setBelkUPC(String belkUPC) {
		this.belkUPC = belkUPC;
	}
	/**
	 * @return the cost
	 */
	public Double getCost() {
		return cost;
	}
	/**
	 * @param cost the cost to set
	 */
	public void setCost(Double cost) {
		this.cost = cost;
	}
	/**
	 * @return the idbCost
	 */
	public Double getIdbCost() {
		return idbCost;
	}
	/**
	 * @param idbCost the idbCost to set
	 */
	public void setIdbCost(Double idbCost) {
		this.idbCost = idbCost;
	}
	

}
