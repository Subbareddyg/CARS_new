package com.belk.car.app.model;

import java.math.BigDecimal;
import java.util.Date;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;


@Entity
@Table(name = "SHIPPING_INFORMATION", uniqueConstraints = @UniqueConstraint(columnNames = "SHIP_TRACKING_NUMBER"))
public class ShippingInformation extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 108569582445547577L;
	private long shippingInfoId;
	private ShippingType shippingType;
	private String isReturnLabelEnclosed;
	private String shipTrackingNumber;
	private Date shippingDate;
	private BigDecimal shippingCost;
	private String statusCd;

	private Set<SampleShippingInformation> sampleShippingInformations = new HashSet<SampleShippingInformation>(0);

	public ShippingInformation() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SHIPPING_INFO_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "SHIPPING_INFO_SEQ_GEN", sequenceName = "SHIPPING_INFORMATION_SEQ", allocationSize = 1)
	@Column(name = "SHIPPING_INFO_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getShippingInfoId() {
		return this.shippingInfoId;
	}

	public void setShippingInfoId(long shippingInfoId) {
		this.shippingInfoId = shippingInfoId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SHIPPING_TYPE_CD", nullable = false)
	public ShippingType getShippingType() {
		return this.shippingType;
	}

	public void setShippingType(ShippingType shippingType) {
		this.shippingType = shippingType;
	}

	@Column(name = "IS_RETURN_LABEL_ENCLOSED", nullable=false, length = 1)
	public String getIsReturnLabelEnclosed() {
		return this.isReturnLabelEnclosed;
	}

	public void setIsReturnLabelEnclosed(String isReturnLabelEnclosed) {
		this.isReturnLabelEnclosed = isReturnLabelEnclosed;
	}

	@Column(name = "SHIP_TRACKING_NUMBER", length = 20)
	public String getShipTrackingNumber() {
		return this.shipTrackingNumber;
	}

	public void setShipTrackingNumber(String shipTrackingNumber) {
		this.shipTrackingNumber = shipTrackingNumber;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "SHIPPING_DATE", nullable = false, length = 7)
	public Date getShippingDate() {
		return this.shippingDate;
	}

	public void setShippingDate(Date shippingDate) {
		this.shippingDate = shippingDate;
	}

	@Column(name = "SHIPPING_COST", nullable = false, precision = 8)
	public BigDecimal getShippingCost() {
		return this.shippingCost;
	}

	public void setShippingCost(BigDecimal shippingCost) {
		this.shippingCost = shippingCost;
	}

	@Column(name = "STATUS_CD", length = 20)
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
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "shippingInformation")
	public Set<SampleShippingInformation> getSampleShippingInformations() {
		return this.sampleShippingInformations;
	}

	public void setSampleShippingInformations(
			Set<SampleShippingInformation> sampleShippingInformations) {
		this.sampleShippingInformations = sampleShippingInformations;
	}
	


	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ShippingInformation other = (ShippingInformation) obj;
		if (this.shipTrackingNumber != other.shipTrackingNumber && (this.shipTrackingNumber == null || !this.shipTrackingNumber.equals(other.shipTrackingNumber)))
			return false;
		return true;
	}


	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 13 * hash + (this.shipTrackingNumber != null ? this.shipTrackingNumber.hashCode() : 0);
		return hash;
	}


	
	
}
