package com.belk.car.app.model;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "SAMPLE_SHIPPING_INFORMATION")
public class SampleShippingInformation extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6194545321495215536L;
	private SampleShippingInformationId id;
	private Sample sample;
	private ShippingInformation shippingInformation;

	public SampleShippingInformation() {
	}

	@EmbeddedId
	@AttributeOverrides( {
		@AttributeOverride(name = "shippingInfoId", column = @Column(name = "SHIPPING_INFO_ID", nullable = false, precision = 12, scale = 0)),
		@AttributeOverride(name = "sampleId", column = @Column(name = "SAMPLE_ID", nullable = false, precision = 12, scale = 0)) })
	public SampleShippingInformationId getId() {
		return this.id;
	}

	public void setId(SampleShippingInformationId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SAMPLE_ID", nullable = false, insertable = false, updatable = false)
	public Sample getSample() {
		return this.sample;
	}

	public void setSample(Sample sample) {
		this.sample = sample;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SHIPPING_INFO_ID", nullable = false, insertable = false, updatable = false)
	public ShippingInformation getShippingInformation() {
		return this.shippingInformation;
	}

	public void setShippingInformation(ShippingInformation shippingInformation) {
		this.shippingInformation = shippingInformation;
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
		final SampleShippingInformation other = (SampleShippingInformation) obj;
		if (this.id != null && other.id != null) {
			return (this.id != other.id && (this.id == null || !this.id.equals(other.id))) ? false : true;
		}
		else {
			long sampleID = (this.sample != null) ? this.sample.getSampleId() : -1;
			long shippingInformationID = (this.shippingInformation != null) ? this.shippingInformation.getShippingInfoId() : -1;
			long sampleID2 = (other.sample != null) ? other.sample.getSampleId() : -1;
			long shippingInformationID2 = (other.shippingInformation != null) ? other.shippingInformation.getShippingInfoId() : -1;
			
			if (sampleID == -1 || shippingInformationID == -1 || sampleID2 == -1 || shippingInformationID2 == -1) return false;
			else return (sampleID == sampleID2) && (shippingInformationID == shippingInformationID2);
		}
	}


	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
		hash = 79 * hash + (this.sample != null ? this.sample.hashCode() : 0);
		hash = 79 * hash + (this.shippingInformation != null ? this.shippingInformation.hashCode() : 0);
		return hash;
	}
}
