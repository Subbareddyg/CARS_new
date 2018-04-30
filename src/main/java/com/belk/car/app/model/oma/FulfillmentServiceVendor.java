/**
 * @author afusy07-priyanka_gadia@syntelinc.com
 * @Date 12-dec-09
 *
 */

package com.belk.car.app.model.oma;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;
import com.belk.car.app.model.Vendor;

@Entity
@Table(name = "FULFMNT_SERVICE_VENDOR", uniqueConstraints = @UniqueConstraint(columnNames = {
		"VENDOR_ID", "FULFMNT_SERVICE_ID" }))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class FulfillmentServiceVendor extends BaseAuditableModel
		implements
			java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long vndrFulfillmentServId;
	private Long safetyInvAmt;
	private String safetyInvAmtTyp;
	private String vndrServLvl;
	private String isInvToRcpt;
	private int overrideDays;
	private String isLocked;
	private Vendor vndr;
	private Long vendorID;
	private Long fulfillmentServId;
	private String statusCd;
	private String lockedBy;
	private String invoiceMethodCode;
	public Long numSkus;
	public Long numStyles;
	private String strCreatedDate;
	private String strUpdatedDate;
	// added for dropship phase 2
	private String isExpeditedShipping;

	

	public String toString() {
		StringBuilder vendorForm = new StringBuilder("vndrFulfillmentServId="
				+ getVndrFulfillmentServId());

		vendorForm.append("\n" + "safetyInvAmt=" + getSafetyInvAmt());
		vendorForm.append("\n" + "vndrId=" + getVendorID());
		vendorForm.append("\n" + "safetyInvAmtTyp=" + getSafetyInvAmtTyp());
		vendorForm.append("\n" + "vndrServLvl=" + getVndrServLvl());
		vendorForm.append("\n" + "isInvToRcpt=" + getIsInvToRcpt());
		vendorForm.append("\n" + "isExpeditedShipping=" + getIsExpeditedShipping());
		vendorForm.append("\n" + "overrideDays=" + getOverrideDays());
		vendorForm.append("\n" + "isLocked=" + getIsLocked());
		vendorForm.append("\n" + "fulfillmentServId=" + getFulfillmentServId());
		vendorForm.append("\n" + "statusCd=" + getStatusCd());
		vendorForm.append("\n" + "numSkus=" + getNumSkus());
		vendorForm.append("\n" + "numStyles=" + getNumStyles());
		vendorForm.append("\n" + "lockedBy=" + getLockedBy());

		return vendorForm.toString();
	}

	public void setNumSkus(Long numSkus) {
		this.numSkus = numSkus;
	}

	public void setNumStyles(Long numStyles) {
		this.numStyles = numStyles;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FULFMNT_SERVICE_VENDOR_ID")
	@javax.persistence.SequenceGenerator(name = "SEQ_FULFMNT_SERVICE_VENDOR_ID", sequenceName = "SEQ_FULFMNT_SERVICE_VENDOR_ID", allocationSize = 1)
	@Column(name = "FULFMNT_SERVICE_VENDOR_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public Long getVndrFulfillmentServId() {
		return vndrFulfillmentServId;
	}

	public void setVndrFulfillmentServId(Long vndrFulfillmentServId) {
		this.vndrFulfillmentServId = vndrFulfillmentServId;
	}

	@Column(name = "DFLT_SAFETY_INV_AMT", nullable = true, precision = 12, scale = 0)
	public Long getSafetyInvAmt() {
		return safetyInvAmt;
	}

	public void setSafetyInvAmt(Long safety_inv_amt) {
		this.safetyInvAmt = safety_inv_amt;
	}

	@Column(name = "DFLT_SAFETY_INV_AMT_TYPE_CD", nullable = true, length = 50)
	public String getSafetyInvAmtTyp() {
		return safetyInvAmtTyp;
	}

	public void setSafetyInvAmtTyp(String safetyInvAmtTyp) {
		this.safetyInvAmtTyp = safetyInvAmtTyp;
	}

	@Column(name = "VENDOR_SERVICE_LVL_DAYS", nullable = false, length = 50)
	public String getVndrServLvl() {
		return vndrServLvl;
	}

	public void setVndrServLvl(String vndrServLvl) {
		this.vndrServLvl = vndrServLvl;
	}

	@Column(name = "IS_INV_TO_RCPT", nullable = true, length = 1)
	public String getIsInvToRcpt() {
		return isInvToRcpt;
	}

	public void setIsInvToRcpt(String isInvToRcpt) {
		this.isInvToRcpt = isInvToRcpt;
	}
	
	@Column(name = "EXPEDITED_SHIPPING", nullable = true, length = 1)
	public String getIsExpeditedShipping() {
		return isExpeditedShipping;
	}

	public void setIsExpeditedShipping(String isExpeditedShipping) {
		this.isExpeditedShipping = isExpeditedShipping;
	}

	@Column(name = "OVERRIDE_DAYS", nullable = true, precision = 3, scale = 0)
	public int getOverrideDays() {
		return overrideDays;
	}

	public void setOverrideDays(int override_days) {
		this.overrideDays = override_days;
	}

	@Column(name = "IS_LOCKED", nullable = false, length = 50)
	public String getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(String is_locked) {
		this.isLocked = is_locked;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "VENDOR_ID", nullable = false, updatable = false, insertable = false)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public Vendor getVndr() {
		return vndr;
	}

	public void setVndr(Vendor vndr) {
		this.vndr = vndr;
	}

	@Column(name = "STATUS_CD", nullable = false, length = 50)
	public String getStatusCd() {
		return statusCd;
	}

	public void setStatusCd(String status_cd) {
		this.statusCd = status_cd;
	}

	@Transient
	public Long getNumSkus() {
		// return this.getVndr().getVendorSkuSize();
		return this.numSkus;
	}

	@Transient
	public Long getNumStyles() {
		// return this.getVndr().getVendorStyles().size();
		return this.numStyles;
	}

	@Column(name = "VENDOR_ID", nullable = false, precision = 12, scale = 0)
	public Long getVendorID() {
		return vendorID;
	}

	public void setVendorID(Long vendorID) {
		this.vendorID = vendorID;
	}

	public void setFulfillmentServId(Long fulfillmentServId) {
		this.fulfillmentServId = fulfillmentServId;
	}

	@Column(name = "FULFMNT_SERVICE_ID", nullable = false, precision = 12, scale = 0)
	public Long getFulfillmentServId() {
		return fulfillmentServId;
	}

	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}

	@Column(name = "LOCKED_BY", nullable = true, length = 50)
	public String getLockedBy() {
		return lockedBy;
	}

	@Transient
	public String getVenName() {
		return this.getVndr().getName();
	}

	@Transient
	public String getVenNum() {
		return this.getVndr().getVendorNumber();
	}

	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}

	@Column(name = "UPDATED_BY", nullable = false, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}
	@Column(name = "INVOICE_METHOD_CD", nullable = true, length = 20)
	public String getInvoiceMethodCode() {
		return invoiceMethodCode;
	}

	public void setInvoiceMethodCode(String invoiceMethodCode) {
		this.invoiceMethodCode = invoiceMethodCode;
	}

	
	@Transient
	public String getStrCreatedDate() {
		return strCreatedDate;
	}

	public void setStrCreatedDate(String strCreatedDate) {
		this.strCreatedDate = strCreatedDate;
	}

	@Transient
	public String getStrUpdatedDate() {
		return strUpdatedDate;
	}

	public void setStrUpdatedDate(String strUpdatedDate) {
		this.strUpdatedDate = strUpdatedDate;
	}

}
