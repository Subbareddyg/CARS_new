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
@Table(name = "SAMPLE_TYPE", uniqueConstraints = @UniqueConstraint(columnNames = "SAMPLE_TYPE_CD"))
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, include="all")
public class SampleType extends BaseAuditableModel implements
		java.io.Serializable {

	public static final String PRODUCT = "PRODUCT";
	public static final String SWATCH = "SWATCH";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1555031805184032485L;
	private String sampleTypeCd;
	private String name;
	private String descr;
	private String statusCd;
	//private Set<Sample> samples = new HashSet<Sample>(0);

	public SampleType() {
	}

	@Id
	@Column(name = "SAMPLE_TYPE_CD", unique = true, nullable = false, length = 20)
	public String getSampleTypeCd() {
		return this.sampleTypeCd;
	}

	public void setSampleTypeCd(String sampleTypeCd) {
		this.sampleTypeCd = sampleTypeCd;
	}

	@Column(name = "NAME", unique = true, nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCR", nullable = false, length = 100)
	public String getDescription() {
		return this.descr;
	}

	public void setDescription(String descr) {
		this.descr = descr;
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


	/*
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sampleType")
	public Set<Sample> getSamples() {
		return this.samples;
	}

	public void setSamples(Set<Sample> samples) {
		this.samples = samples;
	}
	*/

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		if (obj == this)
			return true ;

		final SampleType other = (SampleType) obj;
		return this.sampleTypeCd != null 
				&& this.sampleTypeCd.equals(other.sampleTypeCd) ; 
	}
}
