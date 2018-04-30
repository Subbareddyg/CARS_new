package com.belk.car.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "IMAGE_PROVIDER", uniqueConstraints = @UniqueConstraint(columnNames = "IMAGE_PROVIDER_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class ImageProvider extends BaseAuditableModel implements
		java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4868001885956249359L;

	private long imageProviderId;
	private String name;
	private String contactInfo;

	public ImageProvider() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMAGE_PROVIDER_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "IMAGE_PROVIDER_SEQ_GEN", sequenceName = "IMAGE_PROVIDER_SEQ", allocationSize = 1)
	@Column(name = "IMAGE_PROVIDER_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getImageProviderId() {
		return this.imageProviderId;
	}

	public void setImageProviderId(long imageProviderId) {
		this.imageProviderId = imageProviderId;
	}

	@Column(name = "IMAGE_PROVIDER_NAME", unique = true, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "IMAGE_PROVIDER_CONTACT", nullable = false, length = 200)
	public String getContactInfo() {
		return this.contactInfo;
	}

	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
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
