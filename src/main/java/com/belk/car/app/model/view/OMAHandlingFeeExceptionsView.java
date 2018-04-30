/**
 * Class Name : OMAHandlingFeeExceptionsView.java
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
 * Class contains the attributes required for view generation of OMAHandlingFeeException report.
 * @author AFUSY38
 *
 */
public class OMAHandlingFeeExceptionsView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1071468006139104182L;
	private Long vendorNbr;
	private String vendorName;
	private List<OMAHandlingFeeExceptions> omaHandlingFeeException =  new ArrayList<OMAHandlingFeeExceptions>();
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
	 * @return the omaHandlingFeeException
	 */
	public List<OMAHandlingFeeExceptions> getOmaHandlingFeeException() {
		return omaHandlingFeeException;
	}
	/**
	 * @param omaHandlingFeeException the omaHandlingFeeException to set
	 */
	public void setOmaHandlingFeeException(
			OMAHandlingFeeExceptions omaHandlingFeeException) {
		this.omaHandlingFeeException.add(omaHandlingFeeException);
	}

}
