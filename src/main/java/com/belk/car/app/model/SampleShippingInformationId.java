package com.belk.car.app.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class SampleShippingInformationId implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7968005421457370795L;
	private long shippingInfoId;
	private long sampleId;


	public SampleShippingInformationId()
	{
	}


	public SampleShippingInformationId(long shippingInfoId, long sampleId)
	{
		this.shippingInfoId = shippingInfoId;
		this.sampleId = sampleId;
	}


	@Column(name = "SHIPPING_INFO_ID", nullable = false, precision = 12, scale = 0)
	public long getShippingInfoId()
	{
		return this.shippingInfoId;
	}


	public void setShippingInfoId(long shippingInfoId)
	{
		this.shippingInfoId = shippingInfoId;
	}


	@Column(name = "SAMPLE_ID", nullable = false, precision = 12, scale = 0)
	public long getSampleId()
	{
		return this.sampleId;
	}


	public void setSampleId(long sampleId)
	{
		this.sampleId = sampleId;
	}


	public boolean equals(Object other)
	{
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SampleShippingInformationId))
			return false;
		SampleShippingInformationId castOther = (SampleShippingInformationId) other;

		return (this.getShippingInfoId() == castOther.getShippingInfoId()) && (this.getSampleId() == castOther.getSampleId());
	}


	public int hashCode()
	{
		int result = 17;

		result = 37 * result + (int) this.getShippingInfoId();
		result = 37 * result + (int) this.getSampleId();
		return result;
	}
}


