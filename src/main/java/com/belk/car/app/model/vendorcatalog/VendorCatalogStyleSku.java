package com.belk.car.app.model.vendorcatalog;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

/**
 * This is the model class for table name VENDOR_CATALOG_STYLE_SKU
 * 
 * @version 1.0 10 December 2009
 * @author afusy07 : Priyanka Gadia
 */
@Entity
@Table(name = "VENDOR_CATALOG_STY_SKU")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class VendorCatalogStyleSku extends BaseAuditableModel implements java.io.Serializable {

	private static final long serialVersionUID = -7945669803872939591L;
	
	private Long departmentId;
	private Long recordNum;
	private String lockedBy;
	private VendorCatalogStyleSkuId compositeKey;
	private String color;  
	private String description;
	protected String createdBy;
	protected String updatedBy;
        private String status;
	
	
	

	public VendorCatalogStyleSku() {
		super();
	}
	
	
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "vendorCatalogId", column = @Column(name = "VENDOR_CATALOG_ID", nullable = false, precision = 12, scale = 0)),
			@AttributeOverride(name = "vendorStyleId", column = @Column(name = "VENDOR_STYLE_ID", nullable = false, precision = 12, scale = 0)),
			@AttributeOverride(name = "vendorUPC", column = @Column(name = "VENDOR_UPC", nullable = false, precision = 12, scale = 0)) })
	public VendorCatalogStyleSkuId getCompositeKey() {
		return compositeKey;
	}


	public void setCompositeKey(VendorCatalogStyleSkuId compositeKey) {
		this.compositeKey = compositeKey;
	}

	@Column(name = "DEPT_ID", nullable = false)
	public long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}

	@Column(name = "LOCKED_BY", nullable = true)
	public String getLockedBy() {
		return lockedBy;
	}

	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}

	public void setRecordNum(Long recordNum) {
		this.recordNum = recordNum;
	}
	
	@Column(name = "RECORD_NUMBER", nullable = true)
	public Long getRecordNum() {
		return recordNum;
	}
	@Column(name = "COLOR", nullable = true,length=20)
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "DESCRIPTION", nullable = true , length=100)
	public String getDescription() {
		return description;
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
        public void setStatus(String status) {
		this.status = status;
	}
	@Column(name = "STATUS_CD", nullable = true,length=20)
	public String getStatus() {
		return status;
	}
}
