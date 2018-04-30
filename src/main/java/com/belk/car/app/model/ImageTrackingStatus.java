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
@Table(name = "IMAGE_TRACKING_STATUS", uniqueConstraints = @UniqueConstraint(columnNames = "IMAGE_TRACKING_STATUS_CD"))
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, include="all")
public class ImageTrackingStatus extends BaseAuditableModel implements
		java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2304260745696333325L;

	public static final String REQUESTED = "REQUESTED";
	public static final String RECEIVED = "RECEIVED";
	public static final String AVAILABLE = "AVAILABLE";
	public static final String APPROVED = "APPROVED";
	public static final String REJECTED = "REJECTED";


	private String imageTrackingStatusCd;
	private String name;
	private String descr;

	public ImageTrackingStatus() {
	}

	@Id
	@Column(name = "IMAGE_TRACKING_STATUS_CD", unique = true, nullable = false, length = 20)
	public String getImageTrackingStatusCd() {
		return this.imageTrackingStatusCd;
	}

	public void setImageTrackingStatusCd(String imageTrackingStatusCd) {
		this.imageTrackingStatusCd = imageTrackingStatusCd;
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

}
