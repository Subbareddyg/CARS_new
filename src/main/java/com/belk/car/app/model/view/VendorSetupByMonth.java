/**
 * Class Name : VendorSetupByMonth.java
 * 
 * Version Information : v1.0
 * 
 * Date : 
 * 
 * Copyright Notice :
 */
package com.belk.car.app.model.view;

import java.io.Serializable;
import java.util.Date;

/**
 * Class VendorSetupByMonth provides the additional attributes required 
 * to generate reports for Vendor Setup By Month.
 * @author AFUSY38
 * 
 */
public class VendorSetupByMonth extends DropshipVendorView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8716378561370291829L;
	
	private Date createdDate;
	
	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate  the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
