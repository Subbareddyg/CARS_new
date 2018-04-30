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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;

import com.belk.car.app.model.BaseAuditableModel;

@Entity
@Table(name = "CATALOG_SKU", uniqueConstraints = @UniqueConstraint(columnNames = "CATALOG_SKU_ID"))
public class CatalogSku extends BaseAuditableModel implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4907978024213151237L;

	private long catalogSkuId;
	private CatalogProduct catalogProduct;
	private String vendorSku;

	private Set<CatalogSkuAttribute> attributes = new HashSet<CatalogSkuAttribute>() ;

	public CatalogSku() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATALOG_SKU_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CATALOG_SKU_SEQ_GEN", sequenceName = "CATALOG_SKU_SEQ", allocationSize = 1)
	@Column(name = "CATALOG_SKU_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getCatalogSkuId() {
		return this.catalogSkuId;
	}

	public void setCatalogSkuId(long catalogSkuId) {
		this.catalogSkuId = catalogSkuId;
	}
	
	@Column(name = "VENDOR_SKU", nullable = false, length = 50)
	public String getVendorSku() {
		return vendorSku;
	}

	public void setVendorSku(String vendorSku) {
		this.vendorSku = vendorSku;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATALOG_PRODUCT_ID", nullable = false)
	public CatalogProduct getCatalogProduct() {
		return catalogProduct;
	}

	public void setCatalogProduct(CatalogProduct catalogProduct) {
		this.catalogProduct = catalogProduct;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "catalogSku")
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
          org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public Set<CatalogSkuAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<CatalogSkuAttribute> attributes) {
		this.attributes = attributes;
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
