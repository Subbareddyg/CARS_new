/**
 * Class Name : OMARestockingFees.java
 * 
 * Version Information : v1.0
 * 
 * Date : 12/15/2009
 * 
 * Copyright Notice :
 */
package com.belk.car.app.model.view;

import java.io.Serializable;
import java.util.Date;

/**
 * Class required to store the criteria required to generate the OMARestockingFees Report
 * @author AFUSY38
 *
 */
public class OMARestockingFees extends DropshipVendorView   implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1764355557231111753L;

	private Date effectiveDate;
	private Double restockingFees;
	private Double perOrderAmount;
	private Double perItemAmount;
	/**
	 * @return the perOrderAmount
	 */
	public Double getPerOrderAmount() {
		return perOrderAmount;
	}
	/**
	 * @param perOrderAmount the perOrderAmount to set
	 */
	public void setPerOrderAmount(Double perOrderAmount) {
		this.perOrderAmount = perOrderAmount;
	}
	/**
	 * @return the perItemAmount
	 */
	public Double getPerItemAmount() {
		return perItemAmount;
	}
	/**
	 * @param perItemAmount the perItemAmount to set
	 */
	public void setPerItemAmount(Double perItemAmount) {
		this.perItemAmount = perItemAmount;
	}
	
	/**
	 * @return the effectiveDate
	 */
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	/**
	 * @param effectiveDate the effectiveDate to set
	 */
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	/**
	 * @param restockingFees the restockingFees to set
	 */
	public void setRestockingFees(Double restockingFees) {
		this.restockingFees = restockingFees;
	}
	/**
	 * @return the restockingFees
	 */
	public Double getRestockingFees() {
		return restockingFees;
	}
	
}
