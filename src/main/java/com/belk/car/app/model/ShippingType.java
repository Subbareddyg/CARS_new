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
@Table(name = "SHIPPING_TYPE", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, include = "all")
public class ShippingType extends BaseAuditableModel implements java.io.Serializable {

	public static final String UPS = "UPS";
	public static final String USPS = "USPS";

	/**
	 * 
	 */
	private static final long serialVersionUID = -3396952375131430133L;
	private String shippingTypeCd;
	private String name;
	private String descr;
	private String statusCd;

	//private Set<ShippingInformation> shippingInformations = new HashSet<ShippingInformation>(
	//		0);

	public ShippingType() {
	}

	@Id
	@Column(name = "SHIPPING_TYPE_CD", unique = true, nullable = false, length = 20)
	public String getShippingTypeCd() {
		return this.shippingTypeCd;
	}

	public void setShippingTypeCd(String shippingTypeCd) {
		this.shippingTypeCd = shippingTypeCd;
	}

	@Column(name = "NAME", unique = true, nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCR", nullable = false, length = 20)
	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
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


	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ShippingType other = (ShippingType) obj;
		return this.shippingTypeCd != null 
				&& this.shippingTypeCd.equals(other.shippingTypeCd) ; 
		/*if (this.shippingTypeCd != other.shippingTypeCd && (this.shippingTypeCd == null || !this.shippingTypeCd.equals(other.shippingTypeCd)))
			return false;
		return true;*/
	}


	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 53 * hash + (this.name != null ? this.name.hashCode() : 0);
		return hash;
	}

	/*
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "shippingType")
	public Set<ShippingInformation> getShippingInformations() {
		return this.shippingInformations;
	}

	public void setShippingInformations(
			Set<ShippingInformation> shippingInformations) {
		this.shippingInformations = shippingInformations;
	}
	*/
	
	
}
