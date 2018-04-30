/**
 * Class Name : OMAHandlingFeeExceptions.java
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
 * Class contains the fields required for OMAHandlingFeeExceptions Report. 
 * @author AFUSY38
 *
 */
public class OMAHandlingFeeExceptions extends DropshipVendorView implements
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -467832456797314624L;
	private String styleNbr;
	private String styleDesc;
	private String color;
	private String itemSize;
	private String vendorUPC;
	private Double vendorOrderFee;
	private Double vendorItemFee;
	private Double exceptionOrderFee;
	private Double exceptionItemFee;
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
	 * @return the vendorOrderFee
	 */
	public Double getVendorOrderFee() {
		return vendorOrderFee;
	}
	/**
	 * @param vendorOrderFee the vendorOrderFee to set
	 */
	public void setVendorOrderFee(Double vendorOrderFee) {
		this.vendorOrderFee = vendorOrderFee;
	}
	/**
	 * @return the vendorItemFee
	 */
	public Double getVendorItemFee() {
		return vendorItemFee;
	}
	/**
	 * @param vendorItemFee the vendorItemFee to set
	 */
	public void setVendorItemFee(Double vendorItemFee) {
		this.vendorItemFee = vendorItemFee;
	}
	/**
	 * @return the exceptionOrderFee
	 */
	public Double getExceptionOrderFee() {
		return exceptionOrderFee;
	}
	/**
	 * @param exceptionOrderFee the exceptionOrderFee to set
	 */
	public void setExceptionOrderFee(Double exceptionOrderFee) {
		this.exceptionOrderFee = exceptionOrderFee;
	}
	/**
	 * @return the exceptionItemFee
	 */
	public Double getExceptionItemFee() {
		return exceptionItemFee;
	}
	/**
	 * @param exceptionItemFee the exceptionItemFee to set
	 */
	public void setExceptionItemFee(Double exceptionItemFee) {
		this.exceptionItemFee = exceptionItemFee;
	}
	
}
