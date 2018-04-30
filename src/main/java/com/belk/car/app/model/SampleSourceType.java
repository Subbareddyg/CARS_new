package com.belk.car.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "SAMPLE_SOURCE_TYPE", uniqueConstraints = @UniqueConstraint(columnNames = "SAMPLE_SOURCE_TYPE_CD"))
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, include="all")
public class SampleSourceType extends BaseAuditableModel implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4838160993578793051L;

	public static final String ON_HAND = "ON_HAND";
	public static final String VENDOR = "VENDOR";
	public static final String NEITHER = "NEITHER";

	private String sampleSourceTypeCd;
	private String name;
	private String descr;

	public SampleSourceType() {
	}

	@Id
	@Column(name = "SAMPLE_SOURCE_TYPE_CD", unique = true, nullable = false, length = 20)
	public String getSampleSourceTypeCd() {
		return this.sampleSourceTypeCd;
	}

	public void setSampleSourceTypeCd(String sampleSourceTypeCd) {
		this.sampleSourceTypeCd = sampleSourceTypeCd;
	}

	@Column(name = "NAME", unique = true, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCR", nullable = false, length = 200)
	public String getDescription() {
		return this.descr;
	}

	public void setDescription(String descr) {
		this.descr = descr;
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

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		if (obj == this)
			return true ;

		final SampleSourceType other = (SampleSourceType) obj;
		return this.sampleSourceTypeCd != null 
				&& this.sampleSourceTypeCd.equals(other.sampleSourceTypeCd) ; 
	}
}
