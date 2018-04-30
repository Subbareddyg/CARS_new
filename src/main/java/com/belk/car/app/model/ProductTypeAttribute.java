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
@Table(name = "PRODUCT_TYPE_ATTRIBUTE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class ProductTypeAttribute extends BaseAuditableModel implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5248901465162850526L;
	private long productTypeAttrId;
	private ProductType productType;
	private Attribute attribute;
	private String isMandatory;
	private short displaySeq;
	private String isAttrVariable;
	private String defaultAttrValue;
	private String statusCd;

	public ProductTypeAttribute() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_TYPE_ATTRIBUTE_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "PRODUCT_TYPE_ATTRIBUTE_SEQ_GEN", sequenceName = "PRODUCT_TYPE_ATTRIBUTE_SEQ", allocationSize = 1)
	@Column(name = "PRODUCT_TYPE_ATTR_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getProductTypeAttrId() {
		return this.productTypeAttrId;
	}

	public void setProductTypeAttrId(long productTypeAttrId) {
		this.productTypeAttrId = productTypeAttrId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_TYPE_ID", nullable = false)
	public ProductType getProductType() {
		return this.productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ATTR_ID", nullable = false)
	public Attribute getAttribute() {
		return this.attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	@Column(name = "IS_MANDATORY", nullable = false, length = 1)
	public String getIsMandatory() {
		return this.isMandatory;
	}

	public void setIsMandatory(String isMandatory) {
		this.isMandatory = isMandatory;
	}

	@Column(name = "DISPLAY_SEQ", nullable = false, precision = 4, scale = 0)
	public short getDisplaySeq() {
		return this.displaySeq;
	}

	public void setDisplaySeq(short displaySeq) {
		this.displaySeq = displaySeq;
	}

	@Column(name = "IS_ATTR_VARIABLE", nullable = false, length = 1)
	public String getIsAttrVariable() {
		return this.isAttrVariable;
	}

	public void setIsAttrVariable(String isAttrVariable) {
		this.isAttrVariable = isAttrVariable;
	}

	@Column(name = "DEFAULT_ATTR_VALUE", nullable = false, length = 2000)
	public String getDefaultAttrValue() {
		return this.defaultAttrValue;
	}

	public void setDefaultAttrValue(String defaultAttrValue) {
		this.defaultAttrValue = defaultAttrValue;
	}

	@Column(name = "STATUS_CD", nullable = false, length = 20)
	public String getStatusCd() {
		return this.statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
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
