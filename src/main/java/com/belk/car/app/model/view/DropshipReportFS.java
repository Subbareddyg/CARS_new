/**
 * Class Name : DropshipReportFS.java
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

import com.belk.car.app.DropShipConstants;

/**
 * Class to List of Reports.
 * @author AFUSY38
 *
 */
public class DropshipReportFS implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3597207510696411031L;

	private String fullfilmentServiceName;
	private List<VendorSetupByMonth> vendorSetupByMonth ;
	private List<OMAStylesSKUs> omaStylesSKUs;

	private List<OMAHandlingFees> omaHandlingFees;
	private List<OMAHandlingFeesView> omaHandlingFeesView;
	private List<OMARestockingFees> omaRestockingFees;
	private List<OMARestockingFeesView> omaRestockingFeesView;

	private List<OMACostExceptionsView> omaCostExceptionsView;
	private List<OMACostExceptions> omaCostExceptions;
	private List<OMAHandlingFeeExceptionsView> omaHandlingFeeExceView;
	private List<OMAHandlingFeeExceptions> omaHandlingFeeExceptions;

	/**
	 * Default Constructor 
	 */
	public DropshipReportFS() {

	}
	/**
	 * This Constructor is used to initialize List.
	 */
	public DropshipReportFS(String childClassName) {
		initializeDropshipReportFS(childClassName);
	}

	/**
	 * @return the fullfilmentServiceName
	 */
	public String getFullfilmentServiceName() {
		return fullfilmentServiceName;
	}
	/**
	 * @param fullfilmentServiceName the fullfilmentServiceName to set
	 */
	public void setFullfilmentServiceName(String fullfilmentServiceName) {
		this.fullfilmentServiceName = fullfilmentServiceName;
	}
	/**
	 * @return the vendorSetupByMonth
	 */
	public List<VendorSetupByMonth> getVendorSetupByMonth() {
		return vendorSetupByMonth;
	}
	/**
	 * @param vendorSetupByMonth the vendorSetupByMonth to set
	 */
	public void setVendorSetupByMonth(VendorSetupByMonth vendorSetupByMonth) {
		this.vendorSetupByMonth.add( vendorSetupByMonth);
	}
	/**
	 * @return the omaHandlingFees
	 */
	public List<OMAHandlingFees> getOmaHandlingFees() {
		return omaHandlingFees;
	}
	/**
	 * @param omaHandlingFees the omaHandlingFees to set
	 */
	public void setOmaHandlingFees(OMAHandlingFees omaHandlingFees) {
		this.omaHandlingFees.add(omaHandlingFees);
	}
	/**
	 * @return the omaStylesSkus
	 */
	public List<OMAStylesSKUs> getOmaStylesSKUs() {
		return omaStylesSKUs;
	}
	/**
	 * @param omaStylesSkus the omaStylesSkus to set
	 */
	public void setOmaStylesSKUs(OMAStylesSKUs omaStylesSkus) {
		this.omaStylesSKUs.add(omaStylesSkus);
	}

	/**
	 * @param omaHandlingFeesView the omaHandlingFeesView to set
	 */
	public void setOmaHandlingFeesView(OMAHandlingFeesView omaHandlingFeesView) {
		this.omaHandlingFeesView.add(omaHandlingFeesView);
	}
	/**
	 * @return the omaHandlingFeesView
	 */
	public List<OMAHandlingFeesView> getOmaHandlingFeesView() {
		return omaHandlingFeesView;
	}
	/**
	 * @param omaRestockingFees the omaRestockingFees to set
	 */
	public void setOmaRestockingFees(OMARestockingFees omaRestockingFees) {
		this.omaRestockingFees.add(omaRestockingFees);
	}
	/**
	 * @return the omaRestockingFees
	 */
	public List<OMARestockingFees> getOmaRestockingFees() {
		return omaRestockingFees;
	}
	/**
	 * @param omaRestockingFeesView the omaRestockingFeesView to set
	 */
	public void setOmaRestockingFeesView(OMARestockingFeesView omaRestockingFeesView) {
		this.omaRestockingFeesView.add(omaRestockingFeesView);
	}
	/**
	 * @return the omaRestockingFeesView
	 */
	public List<OMARestockingFeesView> getOmaRestockingFeesView() {
		return omaRestockingFeesView;
	}
	/**
	 * @return the omaCostExceptionsView
	 */
	public List<OMACostExceptionsView> getOmaCostExceptionsView() {
		return omaCostExceptionsView;
	}
	/**
	 * @param omaCostExceptionsView the omaCostExceptionsView to set
	 */
	public void setOmaCostExceptionsView(
			OMACostExceptionsView omaCostExceptionsView) {
		this.omaCostExceptionsView.add(omaCostExceptionsView);
	}
	/**
	 * @return the omaCostExceptions
	 */
	public List<OMACostExceptions> getOmaCostExceptions() {
		return omaCostExceptions;
	}
	/**
	 * @param omaCostExceptions the omaCostExceptions to set
	 */
	public void setOmaCostExceptions(OMACostExceptions omaCostExceptions) {
		this.omaCostExceptions.add(omaCostExceptions);
	}
	/**
	 * @return the omaHandlingFeeExceView
	 */
	public List<OMAHandlingFeeExceptionsView> getOmaHandlingFeeExceView() {
		return omaHandlingFeeExceView;
	}
	/**
	 * @param omaHandlingFeeExceView the omaHandlingFeeExceView to set
	 */
	public void setOmaHandlingFeeExceView(
			OMAHandlingFeeExceptionsView omaHandlingFeeExceView) {
		this.omaHandlingFeeExceView.add(omaHandlingFeeExceView);
	}
	/**
	 * @return the omaHandlingFeeExceptions
	 */
	public List<OMAHandlingFeeExceptions> getOmaHandlingFeeExceptions() {
		return omaHandlingFeeExceptions;
	}
	/**
	 * @param omaHandlingFeeExceptions the omaHandlingFeeExceptions to set
	 */
	public void setOmaHandlingFeeExceptions(
			OMAHandlingFeeExceptions omaHandlingFeeExceptions) {
		this.omaHandlingFeeExceptions.add(omaHandlingFeeExceptions);
	}

	/**
	 * @param childClassName
	 * @return void
	 * This method is used to initialize ArrayList for respective Report
	 * and is called from constructor  
	 */
	private void initializeDropshipReportFS(String childClassName){
		if(childClassName.equals(DropShipConstants.VENDOR_SETUP_BY_MONTH)){
			vendorSetupByMonth = new ArrayList<VendorSetupByMonth>();
		}
		else if(childClassName.equals(DropShipConstants.OMA_STYLES_SKUS)){
			omaStylesSKUs = new ArrayList<OMAStylesSKUs>();
		}
		else if(childClassName.equals(DropShipConstants.OMA_HANDLING_FEES)){
			omaHandlingFees = new ArrayList<OMAHandlingFees>();
			omaHandlingFeesView = new ArrayList<OMAHandlingFeesView>();
		}
		else if(childClassName.equals(DropShipConstants.OMA_RESTOCKING_FEES)){
			omaRestockingFeesView = new ArrayList<OMARestockingFeesView>();
			omaRestockingFees = new ArrayList<OMARestockingFees>();
		}
		else if(childClassName.equals(DropShipConstants.OMA_HANDLING_FEE_EXCEPTION)){
			omaHandlingFeeExceView = new ArrayList<OMAHandlingFeeExceptionsView>();
			omaHandlingFeeExceptions = new ArrayList<OMAHandlingFeeExceptions>();
		}
		else if(childClassName.equals(DropShipConstants.OMA_COST_EXCEPTION)){
			omaCostExceptionsView = new ArrayList<OMACostExceptionsView>();
			omaCostExceptions = new ArrayList<OMACostExceptions>();
		}


	}

}
