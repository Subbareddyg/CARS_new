/**
 * Class Name : OMARestockingFeesView.java
 * 
 * Version Information : v1.0
 * 
 * Date : 12/15/2009
 * 
 * Copyright Notice :
 */
package com.belk.car.app.model.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class contains the fields/list required for view generation of OMARestockingFees Report.
 * @author AFUSY38
 *
 */
public class OMARestockingFeesView implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4834529802150314534L;
	private Long vendorNbr;
	private String vendorName;
	private List<OMARestockingFees> omaRestockingFees =  new ArrayList<OMARestockingFees>();
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
	 * @param omaStockingFees the omaStockingFees to set
	 */
	public void setOmaRetockingFees(OMARestockingFees omaRestockingFees) {
		this.omaRestockingFees.add(omaRestockingFees);
	}
	/**
	 * @return the omaStockingFees
	 */
	public List<OMARestockingFees> getOmaRestockingFees() {
		return omaRestockingFees;
	}
	
}
