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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "SKU_ATTRIBUTE_DELETE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class SkuAttributeDelete extends BaseAuditableModel implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -959483822316466591L;

	private long skuAtrDelId;
	private long carSkuId;
	private String attrValue;
	private Attribute attribute;
	

	public SkuAttributeDelete() {
	}

	

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SKU_ATTRIBUTE_DELETE_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "SKU_ATTRIBUTE_DELETE_SEQ_GEN", sequenceName = "SKU_ATTRIBUTE_DELETE_SEQ", allocationSize = 1)
	@Column(name = "SKU_ATR_DEL_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getSkuAtrDelId() {
		return skuAtrDelId;
	}

	public void setSkuAtrDelId(long skuAtrDelId) {
		this.skuAtrDelId = skuAtrDelId;
	}

	@Column(name = "CAR_SKU_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getCarSkuId() {
		return this.carSkuId;
	}

	public void setCarSkuId(long carSkuId) {
		this.carSkuId = carSkuId;
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
