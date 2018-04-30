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
@Table(name = "VENDOR_STYLE_ATTRIBUTE", uniqueConstraints = @UniqueConstraint(columnNames = {
		"VENDOR_STYLE_ID", "ATTR_ID" }))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class VendorStyleAttribute extends BaseAuditableModel implements
		java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8608449985997642250L;

	private long vendorStyleAttrId;
	private VendorStyle vendorStyle;
	private Attribute attribute;
	private String attrValue;

	public VendorStyleAttribute() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VENDOR_STYLE_ATTRIBUTE_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "VENDOR_STYLE_ATTRIBUTE_SEQ_GEN", sequenceName = "VENDOR_STYLE_ATTRIBUTE_SEQ", allocationSize = 1)
	@Column(name = "VENDOR_STYLE_ATTR_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getVendorStyleAttrId() {
		return this.vendorStyleAttrId;
	}

	public void setVendorStyleAttrId(long vendorStyleAttrId) {
		this.vendorStyleAttrId = vendorStyleAttrId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VENDOR_STYLE_ID", nullable = false)
	public VendorStyle getVendorStyle() {
		return this.vendorStyle;
	}

	public void setVendorStyle(VendorStyle vendorStyle) {
		this.vendorStyle = vendorStyle;
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
