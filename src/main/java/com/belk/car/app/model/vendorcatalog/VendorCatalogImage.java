package com.belk.car.app.model.vendorcatalog;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

/**
 * This is the model class for table name vendor_catalog_image
 * @version 1.0 10 December 2009
 * @author afusya2
 */
@Entity
@Table(name = "vendor_catalog_image")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class VendorCatalogImage extends BaseAuditableModel implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1235805857511877666L;
	private long vndrcatlImageId;
	private long vendorCatalogId;
	private String imgName;
	/**
	 * @return the vndrcatlImageId
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATL_IMG_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CATL_IMG_SEQ_GEN", sequenceName = "SEQ_VENDOR_CATALOG_IMAGE_ID", allocationSize = 1)
	@Column(name = "vendor_catalog_image_id", unique = false, length = 12)
	public long getVndrcatlImageId() {
		return vndrcatlImageId;
	}
	/**
	 * @param vndrcatlImageId the vndrcatlImageId to set
	 */
	public void setVndrcatlImageId(long vndrcatlImageId) {
		this.vndrcatlImageId = vndrcatlImageId;
	}
	/**
	 * @return the vendorCatalogId
	 */
	@Column(name = "vendor_catalog_id", unique = false, length = 12)
	public long getVendorCatalogId() {
		return vendorCatalogId;
	}
	/**
	 * @param vendorCatalogId the vendorCatalogId to set
	 */
	public void setVendorCatalogId(long vendorCatalogId) {
		this.vendorCatalogId = vendorCatalogId;
	}
	/**
	 * @return the imgName
	 */
	@Column(name = "image_name", unique = false, length = 20)
	public String getImgName() {
		return imgName;
	}
	/**
	 * @param imgName the imgName to set
	 */
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_date", nullable = false)
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
	
}
