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
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * @author Yogesh.Vedak
 *
 */


@Entity
@Table(name = "SIZE_CONVERSION_MASTER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class SizeConversionMaster extends BaseAuditableModel implements java.io.Serializable {

	private static final long serialVersionUID = -3982928293839292999L;
	
	private long scmId;
	private Department department;
	private Classification classification;
	private Vendor vendor;
	private String sizeName = "";
	private String coversionSizeName = "";
	private String statusCode = "";
	private String facetSize1="" ;
	private String facetSize2="" ;
	private String facetSize3="" ;
	private String facetSubSize1="" ;
	private String facetSubSize2="" ;
	private String ruleChanged="" ;
	
	private String carId;
	

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CMM_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CMM_SEQ_GEN", sequenceName = "CMM_SEQ", allocationSize = 1)
	@Column(name = "SCM_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getScmId() {
		return scmId;
	}
	
	public void setScmId(long scmId) {
		this.scmId= scmId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCM_DEPT_ID", nullable = true)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCM_CLASS_ID", nullable = true)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public Classification getClassification() {
		return classification;
	}

	public void setClassification(Classification classification) {
		this.classification = classification;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCM_VENDOR_ID", nullable = true)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public Vendor getVendor() {
		return vendor;
	}

	
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	@Column(name = "SCM_SIZE_NAME", nullable = false, length = 100)
	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}

	@Column(name = "SCM_CONVERSION_SIZE_NAME", nullable = true, length = 100)
	public String getCoversionSizeName() {
		return coversionSizeName;
	}

	public void setCoversionSizeName(String coversionSizeName) {
		this.coversionSizeName = coversionSizeName;
	}

	@Column(name = "STATUS_CD", nullable = false, length = 20)
	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	@Column(name = "FACET_SIZE_1", nullable = true, length = 100)
	public String getFacetSize1() {
		return facetSize1;
	}

	public void setFacetSize1(String facetSize1) {
		this.facetSize1 = facetSize1;
	}

	@Column(name = "FACET_SIZE_2", nullable = true, length = 100)
	public String getFacetSize2() {
		return facetSize2;
	}

	public void setFacetSize2(String facetSize2) {
		this.facetSize2 = facetSize2;
	}

	@Column(name = "FACET_SIZE_3", nullable = true, length = 100)
	public String getFacetSize3() {
		return facetSize3;
	}

	public void setFacetSize3(String facetSize3) {
		this.facetSize3 = facetSize3;
	}

	@Column(name = "FACET_SUB_SIZE_1", nullable = true, length = 100)
	public String getFacetSubSize1() {
		return facetSubSize1;
	}

	public void setFacetSubSize1(String facetSubSize1) {
		this.facetSubSize1 = facetSubSize1;
	}

	@Column(name = "FACET_SUB_SIZE_2", nullable = true, length = 100)
	public String getFacetSubSize2() {
		return facetSubSize2;
	}

	public void setFacetSubSize2(String facetSubSize2) {
		this.facetSubSize2 = facetSubSize2;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Column(name = "SCM_RULE_CHANGED", nullable = true, length = 1)
	public String getRuleChanged() {
		return ruleChanged;
	}

	public void setRuleChanged(String ruleChanged) {
		this.ruleChanged = ruleChanged;
	}
	
	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Column(name = "UPDATED_BY", nullable = false, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	@Transient
	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	

	
}


