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
@Table(name = "CLASS_ATTRIBUTE", uniqueConstraints = @UniqueConstraint(columnNames = {
		"ATTR_ID", "CLASS_ID" }))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class ClassAttribute extends BaseAuditableModel implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8093659399709227593L;
	private long classAttrId;
	private Classification classification;
	private Attribute attribute;
	private String isMandatory;
	private short displaySeq;
	private String defaultAttrValue;
	private String isAttrVariable;
	private String statusCd;

	public ClassAttribute() {
	}


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLASS_ATTRIBUTE_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CLASS_ATTRIBUTE_SEQ_GEN", sequenceName = "CLASS_ATTRIBUTE_SEQ", allocationSize = 1)
	@Column(name = "CLASS_ATTR_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getClassAttrId() {
		return this.classAttrId;
	}

	public void setClassAttrId(long classAttrId) {
		this.classAttrId = classAttrId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASS_ID", nullable = false)
	public Classification getClassification() {
		return this.classification;
	}

	public void setClassification(Classification classification) {
		this.classification = classification;
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

	@Column(name = "DEFAULT_ATTR_VALUE", nullable = false, length = 2000)
	public String getDefaultAttrValue() {
		return this.defaultAttrValue;
	}

	public void setDefaultAttrValue(String defaultAttrValue) {
		this.defaultAttrValue = defaultAttrValue;
	}

	@Column(name = "IS_ATTR_VARIABLE", nullable = false, length = 1)
	public String getIsAttrVariable() {
		return this.isAttrVariable;
	}

	public void setIsAttrVariable(String isAttrVariable) {
		this.isAttrVariable = isAttrVariable;
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
