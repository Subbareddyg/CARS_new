package com.belk.car.app.model.vendorcatalog;

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

/**
 * @author afusy07-Priyanka Gadia
 * @Model class for VNDR_CATL_STY_SKU_MAST table
 *
 */
@Entity
@Table(name = "VNDR_CATL_STY_SKU_MAST")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class VendorCatalogStyleSkuMaster //extends BaseAuditableModel
		implements
			java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9222008096438050586L;
	private VendorCatalogStyleSkuMasterId compositeKey;
	private String status;
	private Long vendorCatalogId;
	private String color;
	private Date updatedDate;
	
	
	
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "vendorStyleId", column = @Column(name = "VENDOR_STYLE_ID", nullable = false, precision = 12, scale = 0)),
			@AttributeOverride(name = "vendorUPC", column = @Column(name = "VENDOR_UPC",nullable = false,length=20)),
			@AttributeOverride(name = "vendorId", column = @Column(name = "VENDOR_ID", nullable = false, precision = 12, scale = 0)) })
	public VendorCatalogStyleSkuMasterId getCompositeKey() {
		return compositeKey;
	}
	public void setCompositeKey(VendorCatalogStyleSkuMasterId compositeKey) {
		this.compositeKey = compositeKey;
	}
	
	
	@Column(name = "VENDOR_CATALOG_ID", nullable = false, precision = 12, scale = 0)
	public Long getVendorCatalogId() {
		return vendorCatalogId;
	}
	public void setVendorCatalogId(Long vendorCatalogId) {
		this.vendorCatalogId = vendorCatalogId;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name = "STATUS_CD", nullable = false,length=20)
	public String getStatus() {
		return status;
	}
	
	@Column(name = "COLOR", nullable = true,length=20)
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	
}
