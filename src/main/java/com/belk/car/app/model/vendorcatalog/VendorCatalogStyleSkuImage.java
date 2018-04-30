
package com.belk.car.app.model.vendorcatalog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

/**
 * @author afusy07
 *Feb 8, 2010
 */
@Entity
@Table(name = "VNDR_CATL_STY_SKU_IMAGE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class VendorCatalogStyleSkuImage extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5833064321133609744L;
	private Long vendorCatalogStyleSkuImageId;
	private Long vendorCatalogId;
	private String vendorStyleId;
	private String vendorUpc;
	private String imageType;
	private String imageFileName;
	private String status;
	protected String createdBy;
	protected String updatedBy;
	

	@Column(name = "VENDOR_CATALOG_ID", precision = 12, scale = 0)
	public Long getVendorCatalogId() {
		return vendorCatalogId;
	}

	public void setVendorCatalogId(Long vendorCatalogId) {
		this.vendorCatalogId = vendorCatalogId;
	}

	@Column(name = "VENDOR_STYLE_ID" )
	public String getVendorStyleId() {
		return vendorStyleId;
	}

	public void setVendorStyleId(String vendorStyleId) {
		this.vendorStyleId = vendorStyleId;
	}

	@Column(name = "VENDOR_UPC")
	public String getVendorUpc() {
		return vendorUpc;
	}

	public void setVendorUpc(String vendorUpc) {
		this.vendorUpc = vendorUpc;
	}

	@Column(name = "IMAGE_TYPE")
	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	@Column(name = "IMAGE_FILE_NAME")
	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public void setVendorCatalogStyleSkuImageId(Long vendorCatalogStyleSkuImageId) {
		this.vendorCatalogStyleSkuImageId = vendorCatalogStyleSkuImageId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_VNDR_CATL_STY_SKU_IMAGE_ID")
	@javax.persistence.SequenceGenerator(name = "SEQ_VNDR_CATL_STY_SKU_IMAGE_ID", sequenceName = "SEQ_VNDR_CATL_STY_SKU_IMAGE_ID", allocationSize = 1)
	@Column(name = "VNDR_CATL_STY_SKU_IMAGE_ID", precision = 12, scale = 0)
	public Long getVendorCatalogStyleSkuImageId() {
		return vendorCatalogStyleSkuImageId;
	}

	@Column(name = "STATUS_CD", length = 20, nullable = false)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
