package com.belk.car.app.model.catalog;

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
import javax.persistence.UniqueConstraint;

import com.belk.car.app.model.BaseAuditableModel;

@Entity
@Table(name = "CATALOG_SKU_ATTRIBUTE", uniqueConstraints = @UniqueConstraint(columnNames = "CATALOG_SKU_ATTR_ID"))
public class CatalogSkuAttribute extends BaseAuditableModel implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8041783631989169118L;

	private long catalogSkuAttributeId;
	private CatalogSku catalogSku;
	private String attributeName;
	private String attributeValue;

	public CatalogSkuAttribute() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATALOG_SKU_ATTRIBUTE_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CATALOG_SKU_ATTRIBUTE_SEQ_GEN", sequenceName = "CATALOG_SKU_ATTRIBUTE_SEQ", allocationSize = 1)
	@Column(name = "CATALOG_SKU_ATTR_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getCatalogSkuAttributeId() {
		return this.catalogSkuAttributeId;
	}

	public void setCatalogSkuAttributeId(long catalogSkuAttributeId) {
		this.catalogSkuAttributeId = catalogSkuAttributeId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATALOG_SKU_ID", nullable = false)
	public CatalogSku getCatalogSku() {
		return catalogSku;
	}

	public void setCatalogSku(CatalogSku catalogSku) {
		this.catalogSku = catalogSku;
	}

	@Column(name = "ATTR_NAME", nullable = false, length = 50)
	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	@Column(name = "ATTR_VALUE", nullable = false, length = 200)
	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
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
