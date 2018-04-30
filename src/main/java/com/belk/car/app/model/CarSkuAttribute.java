package com.belk.car.app.model;

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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "CAR_SKU_ATTRIBUTE", uniqueConstraints = @UniqueConstraint(columnNames = {
		"CAR_SKU_ID", "ATTR_ID" }))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class CarSkuAttribute extends BaseAuditableModel implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -959483582916466591L;

	
	private long carSkuAttrId;
	private VendorSku vendorSku;
	private Attribute attribute;
	private String attrValue;

	public CarSkuAttribute() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAR_SKU_ATTRIBUTE_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CAR_SKU_ATTRIBUTE_SEQ_GEN", sequenceName = "CAR_SKU_ATTRIBUTE_SEQ", allocationSize = 1)
	@Column(name = "CAR_SKU_ATTR_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getCarSkuAttrId() {
		return this.carSkuAttrId;
	}

	public void setCarSkuAttrId(long carSkuAttrId) {
		this.carSkuAttrId = carSkuAttrId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CAR_SKU_ID", nullable = false)
	public VendorSku getVendorSku() {
		return this.vendorSku;
	}

	public void setVendorSku(VendorSku vendorSku) {
		this.vendorSku = vendorSku;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ATTR_ID", nullable = false)
	public Attribute getAttribute() {
		return this.attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	@Column(name = "ATTR_VALUE", length = 2000)
	public String getAttrValue() {
		return this.attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
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
