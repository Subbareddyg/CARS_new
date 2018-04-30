/**
 * Class Name : OMACostExceptionsView.java
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
 * Class to store the required view fields in OMACostException Report. 
 * @author AFUSY38
 *
 */
public class OMACostExceptionsView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3162429909204774755L;
	private Long vendorNbr;
	private String vendorName;
	private List<OMACostExceptions> omaCostException =  new ArrayList<OMACostExceptions>();
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
	 * @return the omaCostException
	 */
	public List<OMACostExceptions> getOmaCostException() {
		return omaCostException;
	}
	/**
	 * @param omaCostException the omaCostException to set
	 */
	public void setOmaCostException(OMACostExceptions omaCostException) {
		this.omaCostException.add(omaCostException);
	}
}
