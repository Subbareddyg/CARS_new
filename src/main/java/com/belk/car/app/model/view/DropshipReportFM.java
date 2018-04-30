/**
 * Class Name : DropshipReportFM.java
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
 * Class to set/get the fulfillment service method and reports
 * @author AFUSY38
 *
 */
public class DropshipReportFM implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4421729174881316428L;
	private String fullfilmentMethodName;
	private List<DropshipReportFS> dropshipReportFS = new ArrayList<DropshipReportFS>();
	
	
	/**
	 * @return the fullfilmentServiceName
	 */
	public String getFullfilmentMethodName() {
		return fullfilmentMethodName;
	}
	/**
	 * @param fullfilmentServiceName the fullfilmentServiceName to set
	 */
	public void setFullfilmentMethodName(String fullfilmentMethodName) {
		this.fullfilmentMethodName = fullfilmentMethodName;
	}
	/**
	 * @return the dropshipReportFS
	 */
	public List<DropshipReportFS> getDropshipReportFS() {
		return dropshipReportFS;
	}
	/**
	 * @param dropshipReportFS the dropshipReportFS to set
	 */
	public void setDropshipReportFS(DropshipReportFS dropshipReportFS) {
		this.dropshipReportFS.add(dropshipReportFS);
	}
	
}
