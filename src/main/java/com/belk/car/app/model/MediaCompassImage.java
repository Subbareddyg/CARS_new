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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "MEDIA_COMPASS_IMAGE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class MediaCompassImage extends BaseAuditableModel implements java.io.Serializable{
	
	private static final long serialVersionUID = 9122113839144942157L;
	private long mcImageId;
	private Car car;
	private Sample sample;
	private String imageLocation;
	private String imageName;
	private String imageStatus;
	public static final String MC_IMAGE_RECEIVED = "RECEIVED";
	
	
	//TODO have to add entries in SMAPLE.java and CAR.java
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEDIA_COMPASS_IMAGE_GEN")
	@javax.persistence.SequenceGenerator(name = "MEDIA_COMPASS_IMAGE_GEN", sequenceName = "MEDIA_COMPASS_IMAGE_SEQ", allocationSize = 1)
	@Column(name = "MC_IMAGE_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getMcImageId() {
		return mcImageId;
	}
	public void setMcImageId(long mcImageId) {
		this.mcImageId = mcImageId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CAR_ID", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public Car getCar() {
		return car;
	}
	public void setCar(Car car) {
		this.car = car;
	}
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SAMPLE_ID", nullable = true)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public Sample getSample() {
		return sample;
	}
	public void setSample(Sample sample) {
		this.sample = sample;
	}
	
	@Column(name = "IMAGE_STATUS", nullable = true, length = 20)
	public String getImageStatus() {
		return imageStatus;
	}
	public void setImageStatus(String imageStatus) {
		this.imageStatus = imageStatus;
	}
	
	@Column(name = "IMAGE_LOCATION", nullable = true, length = 250)
	public String getImageLocation() {
		return imageLocation;
	}
	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}
	
	@Column(name = "IMAGE_NAME", nullable = false, length = 250)
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
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
