/**
 * Class Name : OMAStylesSKUs.java
 * 
 * Version Information : v1.0
 * 
 * Date : 
 * 
 * Copyright Notice :
 */
package com.belk.car.app.model.view;

import java.io.Serializable;

/**
 * Class required to store the criteria required to generate the OMAStylesSKUs Report
 * @author AFUSY38
 *
 */
public class OMAStylesSKUs extends DropshipVendorView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5883603354703703970L;

	private Long totalActiveSKUs;
	private Long totalActiveStyles;
	/**
	 * @return the totalActiveSku
	 */
	public Long getTotalActiveSku() {
		return totalActiveSKUs;
	}
	/**
	 * @param totalActiveSku the totalActiveSku to set
	 */
	public void setTotalActiveSKUs(Long totalActiveSKUs) {
		this.totalActiveSKUs = totalActiveSKUs;
	}
	/**
	 * @return the totalActiveStyles
	 */
	public Long getTotalActiveStyles() {
		return totalActiveStyles;
	}
	/**
	 * @param totalActiveStyles the totalActiveStyles to set
	 */
	public void setTotalActiveStyles(Long totalActiveStyles) {
		this.totalActiveStyles = totalActiveStyles;
	}
		
}
