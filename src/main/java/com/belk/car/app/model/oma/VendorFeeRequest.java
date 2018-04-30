/**
 * Class Name : VendorFeeRequestModel.java 
 * Version Information : v1.0 
 * Date : 12/01/2009 
 * Copyright Notice : All rights are reserved to Syntel.
 */
package com.belk.car.app.model.oma;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;
/**
 * Bean class for Vendor Fee Requests.
 * @author afusy13
 *
 */
@Entity
@Table(name = "VENDOR_FEE_RQST", uniqueConstraints = @UniqueConstraint(columnNames = "VENDOR_FEE_RQST_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class VendorFeeRequest extends BaseAuditableModel implements Serializable{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long vendorFeeRequestId; 
	   
	
	private String feeRequestDesciption;  
	private Date effectiveDate;
	private Date formattedEffDate;
	private char rowReadyUpdate;
	
	public VendorFeeRequest(){
		super();
	}
	
	public VendorFeeRequest(Date effectiveDate) {
		super();
		this.effectiveDate = effectiveDate;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_VNDR_FEE_RQST_ID_GEN")
	@javax.persistence.SequenceGenerator(name = "SEQ_VNDR_FEE_RQST_ID_GEN", sequenceName = "SEQ_VNDR_FEE_RQST_ID", allocationSize = 1)
	@Column(name = "VENDOR_FEE_RQST_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getVendorFeeRequestId() {
		return vendorFeeRequestId;
	}
	public void setVendorFeeRequestId(long vendorFeeRequestId) {
		this.vendorFeeRequestId = vendorFeeRequestId;
	}
	
	
	@Column(name="VENDOR_FEE_RQST_DESCR")
	public String getFeeRequestDesciption() {
		return feeRequestDesciption;
	}
	public void setFeeRequestDesciption(String feeRequestDesciption) {
		this.feeRequestDesciption = feeRequestDesciption;
	}
	
	@Column(name="ROW_READY_UPDATE")
	public char getRowReadyUpdate() {
		return rowReadyUpdate;
	}

	public void setRowReadyUpdate(char rowReadyUpdate) {
		this.rowReadyUpdate = rowReadyUpdate;
	}

	@SuppressWarnings("deprecation")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="EFFECTIVE_DATE")
	public Date getEffectiveDate() {
//		Date  effDate = new Date();
//		effDate.setDate(effectiveDate.getDate());
//		effDate.setMonth(effectiveDate.getMonth());
//		effDate.setYear(effectiveDate.getYear());
		//effDate.set
		//SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		//String fmdate = dateFormat.format(this.effectiveDate);
		//this.formattedEffDate 
		//setFormattedDate();
//		this.effectiveDate.setHours(0);
//		this.effectiveDate.setMinutes(0);
//		this.effectiveDate.setSeconds(0);
		return this.effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
		//setFormattedDate();
		
	}         
	@SuppressWarnings("deprecation")
	@Transient
	public void setFormattedDate(){
		this.formattedEffDate.setMonth(this.effectiveDate.getMonth());
		this.formattedEffDate.setDate(this.effectiveDate.getDate());
		this.formattedEffDate.setYear(this.effectiveDate.getYear());
	}
	@SuppressWarnings("deprecation")
	@Transient
	public Date getFormattedEffDate() {
		if(this.effectiveDate != null){
		formattedEffDate = this.effectiveDate;
		//setFormattedDate();
		this.formattedEffDate.setHours(0);
		this.formattedEffDate.setMinutes(0);
		this.formattedEffDate.setSeconds(0);
		}
		return formattedEffDate;
	}
	@SuppressWarnings("deprecation")
	@Transient
	public void setFormattedEffDate(Date formattedEffDate) {
		this.formattedEffDate = formattedEffDate;
//		Integer month = new Integer(formattedEffDate.substring(0, 2));
//		Integer date  = new Integer(formattedEffDate.substring(3, 5));
//		Integer year  = new Integer(formattedEffDate.substring(6, 10));
		
//		effectiveDate.setDate(date.intValue());
//		effectiveDate.setMonth(month.intValue());
//		effectiveDate.setYear(year.intValue());
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
