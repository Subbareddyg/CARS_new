/**
 * Class Name : DropshipVendorView.java
 * 
 * Version Information : v1.0
 * 
 * Date : 12/11/2009
 * 
 * Copyright Notice :
 */
package com.belk.car.app.model.view;

import java.io.Serializable;

/**
 * Class to get/set the Vendor/Fulfillment Service information.
 * @author AFUSY38
 *
 */
public class DropshipVendorView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1494848418389094728L;
	
	private Long vendorNbr;
	private String vendorName;
	private String fullfilmentMethodName;
	private String fullfilmentSeviceName;
	/**
	 * @return the vendorNbr
	 */
	public Long getVendorNbr() {
		return vendorNbr;
	}
	/**
	 * @param vendorNbr the vendorNbr to set
	 */
	public void setVendorNbr(Long vendorNbr) {
		this.vendorNbr = vendorNbr;
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
	 * @return the fullfilmentMethodName
	 */
	public String getFullfilmentMethodName() {
		return fullfilmentMethodName;
	}
	/**
	 * @param fullfilmentMethodName the fullfilmentMethodName to set
	 */
	public void setFullfilmentMethodName(String fullfilmentMethodName) {
		this.fullfilmentMethodName = fullfilmentMethodName;
	}
	/**
	 * @return the fullfilmentSeviceName
	 */
	public String getFullfilmentSeviceName() {
		return fullfilmentSeviceName;
	}
	/**
	 * @param fullfilmentSeviceName the fullfilmentSeviceName to set
	 */
	public void setFullfilmentSeviceName(String fullfilmentSeviceName) {
		this.fullfilmentSeviceName = fullfilmentSeviceName;
	}

}
