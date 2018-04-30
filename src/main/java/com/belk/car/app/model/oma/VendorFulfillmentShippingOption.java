
package com.belk.car.app.model.oma;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

@Entity
@Table(name = "VNDR_FULFMNT_SHIP_OPT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class VendorFulfillmentShippingOption extends BaseAuditableModel
		implements
			java.io.Serializable {

	
	private static final long serialVersionUID = 6407077305047137977L;
	private CompositeKeyForShippingOptions compositeKeyForShippingOptionsId;
	private String allowDirectBill;
	private String allowVendorBill;
	private String isAllowed;
	private String directBillAccountNumber;
	private String createdBy;
	private String updatedBy;
	private Date createdDate;
	private Date updatedDate;

	@Transient
	public String toString() {
		StringBuilder model = new StringBuilder("allowDirectBill="
				+ this.getAllowDirectBill());
		model.append("\nallowVendorBill=" + this.getAllowVendorBill());
		model.append("\nisAllowed=" + this.getIsAllowed());
		model.append("\ndirectBillAccountNumber="
				+ this.getDirectBillAccountNumber());
		model.append("\nfulfillment Service Id="
				+ this.getCompositeKeyForShippingOptionsId()
						.getFulfillmentServId());
		model.append("\nshipping option id="
				+ this.getCompositeKeyForShippingOptionsId()
						.getShippingOptionId());
		return model.toString();

	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "shippingOptionId", column = @Column(name = "SHIPPING_CARRIER_OPTION_ID", nullable = false, precision = 12, scale = 0)),
			@AttributeOverride(name = "fulfillmentServId", column = @Column(name = "FULFMNT_SERVICE_ID", nullable = false, precision = 12, scale = 0)),
			@AttributeOverride(name = "vendorId", column = @Column(name = "VENDOR_ID", nullable = false, precision = 12, scale = 0)) })
	public CompositeKeyForShippingOptions getCompositeKeyForShippingOptionsId() {
		return compositeKeyForShippingOptionsId;
	}

	public void setCompositeKeyForShippingOptionsId(
			CompositeKeyForShippingOptions compositeKeyForShippingOptionsId) {
		this.compositeKeyForShippingOptionsId = compositeKeyForShippingOptionsId;
	}

	@Column(name = "ALLOW_DIRECT_BILL", nullable = false, length = 1)
	public String getAllowDirectBill() {
		return allowDirectBill;
	}

	public void setAllowDirectBill(String allowDirectBill) {
		this.allowDirectBill = allowDirectBill;
	}

	@Column(name = "ALLOW_VENDOR_BILL", nullable = false, length = 1)
	public String getAllowVendorBill() {
		return allowVendorBill;
	}

	public void setAllowVendorBill(String allowVendorBill) {
		this.allowVendorBill = allowVendorBill;
	}

	@Column(name = "ALLOW_SHIPPING_CARRIER_OPTION", nullable = false, length = 1)
	public String getIsAllowed() {
		return isAllowed;
	}

	public void setIsAllowed(String isAllowed) {
		this.isAllowed = isAllowed;
	}

	@Column(name = "DIRECT_BILL_ACCT_NUM", nullable = true, length = 20)
	public String getDirectBillAccountNumber() {
		return directBillAccountNumber;
	}

	public void setDirectBillAccountNumber(String directBillAccountNumber) {
		this.directBillAccountNumber = directBillAccountNumber;
	}
	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Column(name = "UPDATED_BY", nullable = false, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}


	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}
