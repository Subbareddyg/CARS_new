package com.belk.car.app.dto;

import java.io.Serializable;

public class DropshipVendorActiveStyleSkuDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -302480775901729560L;
	
	private Long vendorFulfillmentId;
	private Long totalActiveSKUs;
	private Long totalActiveStyles;
	
	public Long getVendorFulfillmentId() {
		return vendorFulfillmentId;
	}
	public void setVendorFulfillmentId(Long vendorFulfillmentId) {
		this.vendorFulfillmentId = vendorFulfillmentId;
	}
	public Long getTotalActiveSKUs() {
		return totalActiveSKUs;
	}
	public void setTotalActiveSKUs(Long totalActiveSKUs) {
		this.totalActiveSKUs = totalActiveSKUs;
	}
	public Long getTotalActiveStyles() {
		return totalActiveStyles;
	}
	public void setTotalActiveStyles(Long totalActiveStyles) {
		this.totalActiveStyles = totalActiveStyles;
	}
	
	
	

}
