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
@Table(name = "SAMPLE_TRACKING_STATUS", uniqueConstraints = @UniqueConstraint(columnNames = "SAMPLE_TRACKING_STATUS_CD"))
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, include="all")
public class SampleTrackingStatus extends BaseAuditableModel implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4485783622294900335L;

	public static final String REQUESTED = "REQUESTED";
	public static final String RECEIVED = "RECEIVED";
	public static final String SHIPPED = "SHIPPED";
	public static final String RETURNED = "RETURNED";
	public static final String CLOSED = "CLOSED";

	private String sampleTrackingStatusCd;
	private String name;
	private String descr;

	public SampleTrackingStatus() {
	}

	@Id
	@Column(name = "SAMPLE_TRACKING_STATUS_CD", unique = true, nullable = false, length = 20)
	public String getSampleTrackingStatusCd() {
		return this.sampleTrackingStatusCd;
	}

	public void setSampleTrackingStatusCd(String sampleTrackingStatusCd) {
		this.sampleTrackingStatusCd = sampleTrackingStatusCd;
	}

	@Column(name = "NAME", nullable = false, length = 50)
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

		final SampleTrackingStatus other = (SampleTrackingStatus) obj;
		return this.sampleTrackingStatusCd != null 
				&& this.sampleTrackingStatusCd.equals(other.sampleTrackingStatusCd) ; 
	}
}
