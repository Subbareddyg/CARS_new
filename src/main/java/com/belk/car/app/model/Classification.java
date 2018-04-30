package com.belk.car.app.model;

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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "CLASSIFICATION", uniqueConstraints = @UniqueConstraint(columnNames = "BELK_CLASS_NUMBER"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class Classification extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4614656945111006036L;
	private long classId;
	private Department department = new Department();
	private String name;
	private String descr;
	private short belkClassNumber;
	private String isProductTypeRequired;
	private String statusCd;
	private PatternProcessingRule patternProcessingRule;

	private Set<ClassAttribute> classAttributes = new HashSet<ClassAttribute>(0);
	//private Set<CarUserClass> carUserClasses = new HashSet<CarUserClass>(0);
	//private Set<VendorStyle> vendorStyles = new HashSet<VendorStyle>(0);

	public Classification() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLASSIFICATION_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CLASSIFICATION_SEQ_GEN", sequenceName = "CLASSIFICATION_SEQ", allocationSize = 1)
	@Column(name = "CLASS_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getClassId() {
		return this.classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "DEPT_ID", nullable = false)
	public Department getDepartment() {
		return this.department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@Column(name = "NAME", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCR", nullable = false, length = 2000)
	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	@Column(name = "BELK_CLASS_NUMBER", unique = true, nullable = false, precision = 4, scale = 0)
	public short getBelkClassNumber() {
		return this.belkClassNumber;
	}

	public void setBelkClassNumber(short belkClassNumber) {
		this.belkClassNumber = belkClassNumber;
	}

	@Column(name = "IS_PRODUCT_TYPE_REQUIRED", nullable = false, length = 1)
	public String getIsProductTypeRequired() {
		return this.isProductTypeRequired;
	}

	public void setIsProductTypeRequired(String isProductTypeRequired) {
		this.isProductTypeRequired = isProductTypeRequired;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "classification")
	public Set<ClassAttribute> getClassAttributes() {
		return this.classAttributes;
	}

	public void setClassAttributes(Set<ClassAttribute> classAttributes) {
		this.classAttributes = classAttributes;
	}


	@Transient
	public String getClassificationIdAsString() {
		return String.valueOf(classId);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PATTERN_PROCESSING_RULE_CD", nullable = false)
	public PatternProcessingRule getPatternProcessingRule() {
		return patternProcessingRule;
	}

	public void setPatternProcessingRule(PatternProcessingRule patternProcessingRule) {
		this.patternProcessingRule = patternProcessingRule;
	}

}
