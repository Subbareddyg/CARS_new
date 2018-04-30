/**
 * Class Name : OMAHandlingFeesView.java
 * 
 * Version Information : v1.0
 * 
 * Date : 12/11/2009
 * 
 * Copyright Notice :
 */
package com.belk.car.app.model.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class contains the fields/list required for view generation of OMAHandlingFees Report.
 * @author AFUSY38
 *
 */
public class OMAHandlingFeesView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4834529802150314534L;
	private Long vendorNbr;
	private String vendorName;
	private List<OMAHandlingFees> omaHandlingFees =  new ArrayList<OMAHandlingFees>();
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
	 * @return the omaHandlingFeesList
	 */
	public List<OMAHandlingFees> getOmaHandlingFees() {
		return omaHandlingFees;
	}
	/**
	 * @param omaHandlingFeesList the omaHandlingFeesList to set
	 */
	public void setOmaHandlingFees(OMAHandlingFees omaHandlingFees) {
		this.omaHandlingFees.add(omaHandlingFees);
	}
}
