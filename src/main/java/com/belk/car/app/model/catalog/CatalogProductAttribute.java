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
@Table(name = "CATALOG_PRODUCT_ATTRIBUTE", uniqueConstraints = @UniqueConstraint(columnNames = "CATALOG_PRODUCT_ATTR_ID"))
public class CatalogProductAttribute extends BaseAuditableModel implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4380695276966546553L;

	private long catalogProductAttributeId;
	private CatalogProduct catalogProduct;
	private String attributeName;
	private String attributeValue;

	public CatalogProductAttribute() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATALOG_PRODUCT_ATTRIBUTE_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CATALOG_PRODUCT_ATTRIBUTE_SEQ_GEN", sequenceName = "CATALOG_PRODUCT_ATTRIBUTE_SEQ", allocationSize = 1)
	@Column(name = "CATALOG_PRODUCT_ATTR_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getCatalogProductAttributeId() {
		return this.catalogProductAttributeId;
	}

	public void setCatalogProductAttributeId(long catalogProductAttributeId) {
		this.catalogProductAttributeId = catalogProductAttributeId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATALOG_PRODUCT_ID", nullable = false)
	public CatalogProduct getCatalogProduct() {
		return catalogProduct;
	}

	public void setCatalogProduct(CatalogProduct catalogProduct) {
		this.catalogProduct = catalogProduct;
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
