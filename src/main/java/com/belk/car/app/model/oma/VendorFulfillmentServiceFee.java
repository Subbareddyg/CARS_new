/**
 * Class Name : VendorFeeReqModel.java 
 * Version Information : v1.0 
 * Date : 12/01/2009 
 * Copyright Notice : All rights are reserved to Syntel.
 */

package com.belk.car.app.model.oma;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;
import com.belk.car.app.model.Vendor;
/**
 * Bean class to contain the Id's of fulfillment service, Vendor, Vendor Fee Request and corresponding vendor fee.
 * @author afusy13
 *
 */
@Entity
@Table(name = "VNDR_FULFMNT_SERV_FEE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class VendorFulfillmentServiceFee extends BaseAuditableModel implements Serializable{

	/**
	 * @param fulfillmentService 
	 * @param vendor 
	 * @param compositeKeyVendorFees
	 */
	public VendorFulfillmentServiceFee(Vendor vendor, FulfillmentService fulfillmentService) {
		super();
		this.compositeKeyVendorFees = new CompositeKeyVendorFees(vendor,fulfillmentService);
	}
	
	public VendorFulfillmentServiceFee(){
		super();
		this.compositeKeyVendorFees = new CompositeKeyVendorFees();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	          
	
	private CompositeKeyVendorFees compositeKeyVendorFees;
	
	@EmbeddedId
	@AttributeOverrides( {
		@AttributeOverride(name = "vndrFeeRequest", column = @Column(name = "VNDR_FEE_REQUEST_ID", nullable = false, precision = 12, scale = 0)),
		@AttributeOverride(name = "vendor", column = @Column(name = "VENDOR_ID", nullable = false, precision = 12, scale = 0)),
		@AttributeOverride(name = "vendorFee", column = @Column(name = "VENDOR_FEE_ID", nullable = false, precision = 12, scale = 0)),
		@AttributeOverride(name = "fulfillmentService", column = @Column(name = "FULFMNT_SERVICE_ID", nullable = false, precision = 12, scale = 0))})
	public CompositeKeyVendorFees getCompositeKeyVendorFees() {
		return compositeKeyVendorFees;
	}
	public void setCompositeKeyVendorFees(CompositeKeyVendorFees compositeKeyVendorFees) {
		this.compositeKeyVendorFees = compositeKeyVendorFees;
	}
	
	
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	@Column(name = "UPDATED_BY", nullable = false, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	

	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	
}
