
package com.belk.car.app.model.oma;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

@Entity
@Table(name = "location", uniqueConstraints = @UniqueConstraint(columnNames = "location_id"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class Address extends BaseAuditableModel implements java.io.Serializable {

	private static final long serialVersionUID = 2667713137037878558L;

	private long addressID;
	private String locName;
	private String addr1;
	private String addr2;
	private String city;
	private String zip;
	private String state;
	private String status;
	private String createdBy;
	private String updatedBy;
	private Date createdDate;
	private Date updatedDate;

	public String toString() {
		StringBuilder address = new StringBuilder();
		address.append("location=" + getLocName());
		address.append("\n addr1=" + getAddr1());
		address.append("\n addr2=" + getAddr2());
		address.append("\n city=" + getCity());
		address.append("\n state=" + getState());
		address.append("\n zip=" + getZip());
		address.append("\n Address Id=" + getAddressID());
		return address.toString();
	}

	public Address() {
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_LOCATION_ID")
	@javax.persistence.SequenceGenerator(name = "SEQ_LOCATION_ID", sequenceName = "SEQ_LOCATION_ID", allocationSize = 1)
	@Column(name = "location_id", unique = true, nullable = false, precision = 12, scale = 0)
	public long getAddressID() {
		return addressID;
	}

	public void setAddressID(long addressID) {
		this.addressID = addressID;
	}

	@Column(name = "location_name", nullable = false)
	public String getLocName() {
		return locName;
	}

	public void setLocName(String locName) {
		this.locName = locName;
	}

	@Column(name = "addr1")
	public String getAddr1() {
		return addr1;
	}

	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}

	@Column(name = "addr2")
	public String getAddr2() {
		return addr2;
	}

	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}

	@Column(name = "city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "zipcode")
	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@Column(name = "state_cd")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "status_cd", nullable = false)
	public String getStatus() {
		return status;
	}

	@Transient
	public String getCompleteAddr() {
		return (this.getAddr1() + this.getAddr2());
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Column(name = "UPDATED_BY", nullable = false, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}


	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}
