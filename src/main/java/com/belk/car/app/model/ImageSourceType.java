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
@Table(name = "IMAGE_SOURCE_TYPE", uniqueConstraints = @UniqueConstraint(columnNames = "IMAGE_SOURCE_TYPE_CD"))
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, include="all")
public class ImageSourceType extends BaseAuditableModel implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5386403075512304090L;

	public static final String ON_HAND = "ON_HAND";
	public static final String VENDOR = "VENDOR";
	public static final String FROM_SAMPLE = "FROM_SAMPLE";
	public static final String VENDOR_UPLOADED = "VENDOR_UPLOADED";
	public static final String BUYER_UPLOADED = "BUYER_UPLOADED";

	private String imageSourceTypeCd;
	private String name;
	private String descr;

	public ImageSourceType() {
	}

	@Id
	@Column(name = "IMAGE_SOURCE_TYPE_CD", unique = true, nullable = false, length = 20)
	public String getImageSourceTypeCd() {
		return this.imageSourceTypeCd;
	}

	public void setImageSourceTypeCd(String imageSourceTypeCd) {
		this.imageSourceTypeCd = imageSourceTypeCd;
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

}
