/**
 * Class Name : CompositeKeyVendorFees.java 
 * Version Information : v1.0 
 * Date : 12/01/2009 
 * Copyright Notice : All rights are reserved to Syntel.
 */
package com.belk.car.app.model.oma;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.belk.car.app.model.Vendor;
/**
 * Contains the composite key required for Vendor Fees. 
 * @author afusy13
 *
 */
@Embeddable
public class CompositeKeyVendorFees implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private VendorFeeRequest vendorFeeRequest; 
	private FulfillmentService fulfillmentService;
	private Vendor vendor; 
	private VendorFee vendorFee;
	
	public CompositeKeyVendorFees(){
		super();
	}
	public CompositeKeyVendorFees(VendorFeeRequest vndrFeeRequest,FulfillmentService fulfillmentService,Vendor vendor,VendorFee vendorFee){
		this.vendorFeeRequest = vndrFeeRequest;
		this.fulfillmentService = fulfillmentService;
		this.vendor = vendor;
		this.vendorFee = vendorFee;
	}
	
	public CompositeKeyVendorFees(Vendor vendor,
			FulfillmentService fulfillmentService) {
		this.vendor=vendor;
		this.fulfillmentService = fulfillmentService;
	}
	@ManyToOne
	@JoinColumn(name="VNDR_FEE_REQUEST_ID",nullable=false)
	public VendorFeeRequest getVendorFeeRequest() {
		return vendorFeeRequest;
	}
	public void setVendorFeeRequest(VendorFeeRequest vndrFeeRequestId) {
		this.vendorFeeRequest = vndrFeeRequestId;
	}
	
	@ManyToOne
	@JoinColumn(name="FULFMNT_SERVICE_ID", nullable=false)
	public FulfillmentService getFulfillmentService() {
		return fulfillmentService;
	}
	public void setFulfillmentService(FulfillmentService fulfillmentService) {
		this.fulfillmentService = fulfillmentService;
	}
	
	@ManyToOne
	@JoinColumn(name="VENDOR_ID")
	public Vendor getVendor() {
		return vendor;
	}
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	@ManyToOne
	@JoinColumn(name="VENDOR_FEE_ID", nullable=false)
	public VendorFee getVendorFee() {
		return vendorFee;
	}
	public void setVendorFee(VendorFee vendorFee) {
		this.vendorFee = vendorFee;
	}
}
