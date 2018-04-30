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
@Table(name = "ATTRIBUTE_LOOKUP_VALUE", uniqueConstraints = @UniqueConstraint(columnNames = {
		"ATTR_ID", "VALUE" }))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")		
public class AttributeLookupValue extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5043243808928963172L;
	private long attrLookupValueId;
	private Attribute attribute;
	private String value;
	private short displaySeq;
	private String attributeTag;
	private String statusCd;
	private long attrId;
	
	public AttributeLookupValue() {
	}

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATTRIBUTE_LOOKUP_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "ATTRIBUTE_LOOKUP_SEQ_GEN", sequenceName = "ATTRIBUTE_LOOKUP_VALUE_SEQ", allocationSize = 1)
	@Column(name = "ATTR_LOOKUP_VALUE_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getAttrLookupValueId() {
		return this.attrLookupValueId;
	}

	public void setAttrLookupValueId(long attrLookupValueId) {
		this.attrLookupValueId = attrLookupValueId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ATTR_ID", nullable = false)
	public Attribute getAttribute() {
		return this.attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	@Column(name = "VALUE", length = 250)
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "DISPLAY_SEQ", nullable = false, precision = 4, scale = 0)
	public short getDisplaySequence() {
		return this.displaySeq;
	}

	public void setDisplaySequence(short displaySeq) {
		this.displaySeq = displaySeq;
	}

	@Column(name = "ATTRIBUTE_TAG", length = 250)
	public String getTag() {
		return this.attributeTag;
	}

	public void setTag(String attributeTag) {
		this.attributeTag = attributeTag;
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
	@Column(name = "ATTR_ID", nullable = false, length = 20, insertable=false, updatable=false)
	public long getAttrId() {
		return attrId;
	}


	public void setAttrId(long attrId) {
		this.attrId = attrId;
	}

}
