package com.belk.car.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "ATTRIBUTE_VALUE_PROCESS_STATUS")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, include="all")
public class AttributeValueProcessStatus extends BaseAuditableModel implements
		java.io.Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6164960042691664101L;

	
	public static final String CHECK_REQUIRED = "CHECK_REQUIRED";
	public static final String NO_CHECK_REQUIRED = "NO_CHECK_REQUIRED";
	public static final String IN_VALUE = "IN_VALUE";
	public static final String ACCEPTED = "ACCEPTED";
	public static final String REJECTED = "REJECTED";
	
	
	private String processStatusCd;
	private String name;
	
	
	
	public AttributeValueProcessStatus() {
	}
	

	/**
	 * @return the attrValueProcessStatusCd
	 */
	@Id
	@Column(name = "ATTR_VALUE_PROCESS_STATUS_CD", unique = true, nullable = false, length = 20)
	public String getProcessStatusCd() {
		return processStatusCd;
	}

	/**
	 * @param attrValueProcessStatusCd the attrValueProcessStatusCd to set
	 */
	public void setProcessStatusCd(String attrValueProcessStatusCd) {
		this.processStatusCd = attrValueProcessStatusCd;
	}

	/**
	 * @return the name
	 */
	@Column(name = "NAME", nullable = false, length = 50)
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
