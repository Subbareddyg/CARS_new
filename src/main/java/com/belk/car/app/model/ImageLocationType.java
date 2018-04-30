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
@Table(name = "IMAGE_LOCATION_TYPE", uniqueConstraints = @UniqueConstraint(columnNames = "IMAGE_LOCATION_TYPE_CD"))
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, include="all")
public class ImageLocationType extends BaseAuditableModel implements
		java.io.Serializable {
	public static String BELK_FTP_LOCATION = "BELK_FTP" ;
	public static String FTP = "FTP";
	public static String CD = "CD";

	/**
	 * 
	 */
	private static final long serialVersionUID = -6229533991071354506L;

	private String imageLocationTypeCd;
	private String name;
	private String descr;

	public ImageLocationType() {
	}

	@Id
	@Column(name = "IMAGE_LOCATION_TYPE_CD", unique = true, nullable = false, length = 20)
	public String getImageLocationTypeCd() {
		return this.imageLocationTypeCd;
	}

	public void setImageLocationTypeCd(String imageLocationTypeCd) {
		this.imageLocationTypeCd = imageLocationTypeCd;
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
