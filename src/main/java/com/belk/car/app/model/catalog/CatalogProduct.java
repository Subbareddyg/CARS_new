package com.belk.car.app.model.catalog;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;

import com.belk.car.app.model.BaseAuditableModel;

@Entity
@Table(name = "CATALOG_PRODUCT", uniqueConstraints = @UniqueConstraint(columnNames = "CATALOG_PRODUCT_ID"))
public class CatalogProduct extends BaseAuditableModel implements
		java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8342814694162139582L;
	
	private long catalogProductId;
	private String styleNumber;
	private String vendorNumber ;
	private String primaryName;
	private String secondaryName;
	private String copyText;
	
	private Set<CatalogProductAttribute> attributes = new HashSet<CatalogProductAttribute>() ;
	private Set<CatalogSku> skus = new HashSet<CatalogSku>() ;

	public CatalogProduct() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATALOG_PRODUCT_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CATALOG_PRODUCT_SEQ_GEN", sequenceName = "CATALOG_PRODUCT_SEQ", allocationSize = 1)
	@Column(name = "CATALOG_PRODUCT_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getCatalogProductId() {
		return this.catalogProductId;
	}

	public void setCatalogProductId(long catalogProductId) {
		this.catalogProductId = catalogProductId;
	}
	
	@Column(name = "VENDOR_NUMBER", nullable = false, length = 50)
	public String getVendorNumber() {
		return vendorNumber;
	}

	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}

	@Column(name = "STYLE_NUMBER", nullable = false, length = 50)
	public String getStyleNumber() {
		return styleNumber;
	}

	public void setStyleNumber(String styleNumber) {
		this.styleNumber = styleNumber;
	}

	@Column(name = "PRIMARY_NAME", nullable = false, length = 200)
	public String getPrimaryName() {
		return primaryName;
	}

	public void setPrimaryName(String primaryName) {
		this.primaryName = primaryName;
	}

	@Column(name = "SECONDARY_NAME", nullable = false, length = 200)
	public String getSecondaryName() {
		return secondaryName;
	}

	public void setSecondaryName(String secondaryName) {
		this.secondaryName = secondaryName;
	}

	@Column(name = "COPY_TEXT", nullable = false, length = 4000)
	public String getCopyText() {
		return copyText;
	}

	public void setCopyText(String copyText) {
		this.copyText = copyText;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "catalogProduct")
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
          org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public Set<CatalogProductAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<CatalogProductAttribute> attributes) {
		this.attributes = attributes;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "catalogProduct")
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
          org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public Set<CatalogSku> getSkus() {
		return skus;
	}

	public void setSkus(Set<CatalogSku> skus) {
		this.skus = skus;
	}

	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}

	@Column(name = "UPDATED_BY", nullable = false, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}
}
