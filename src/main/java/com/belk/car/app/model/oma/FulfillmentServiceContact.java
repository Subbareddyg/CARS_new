/**
 * Class Name : FulfillmentServiceContact.java 
 * Version Information : v1.0 
 * Date : 12/01/2009 
 * Copyright Notice : All rights are reserved to Syntel.
 */
package com.belk.car.app.model.oma;

import java.io.Serializable;
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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

/**
 * Bean Class for Fulfillment Service Contacts.
 * @author afusy13
 *
 */
@Entity
@Table(name = "FULFMNT_SERVICE_CONTACT", uniqueConstraints = @UniqueConstraint(columnNames = {
		"FIRST_NAME", "LAST_NAME", "FULFMNT_SERVICE_ID"}))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class FulfillmentServiceContact extends BaseAuditableModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long contactId;

	private FulfillmentService fulfillmentService;
	private ContactType contactType;
	private Address address;

	private String emailAddress;
	private String firstName;
	private String lastName;

	private String contactName;

	private String otherTypeDesc;
	private String jobTitle;

	private String phoneNbr;
	private String altPhoneNbr;
	private String notes;
	private String status;
	private String lockedBy;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FULFLMNT_SERVICE_CONTACT_ID_GEN")
	@javax.persistence.SequenceGenerator(name = "SEQ_FULFLMNT_SERVICE_CONTACT_ID_GEN", sequenceName = "SEQ_FULFMNT_SERVICE_CONTACT_ID", allocationSize = 1)
	@Column(name = "FULFMNT_SERVICE_CONTACT_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getContactId() {
		return contactId;
	}
	public void setContactId(long contactId) {
		this.contactId = contactId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FULFMNT_SERVICE_ID", nullable = false)
	public FulfillmentService getFulfillmentService() {
		return fulfillmentService;
	}
	public void setFulfillmentService(FulfillmentService fulfillmentService) {
		this.fulfillmentService = fulfillmentService;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTACT_TYPE_CD", nullable = false)
	public ContactType getContactType() {
		return contactType;
	}
	public void setContactType(ContactType contactType) {
		this.contactType = contactType;
	}
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOCATION_ID")
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
	@Column(name = "EMAIL_ADDR", nullable = false, length = 50)
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	@Column(name = "FIRST_NAME", nullable = false, length = 50)
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Column(name = "LAST_NAME", nullable = true, length = 50)
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Transient
	public String getContactName() {
		contactName = this.firstName.concat(" ").concat(this.lastName);
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	
	@Column(name = "CONTACT_OTHER_TYPE_DESCR", nullable = true, length = 100)
	public String getOtherTypeDesc() {
		return otherTypeDesc;
	}
	public void setOtherTypeDesc(String otherTypeDesc) {
		this.otherTypeDesc = otherTypeDesc;
	}
	
	@Column(name = "JOB_TITLE", nullable = true, length = 50)
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	
	@Column(name = "PRIMARY_PHONE", nullable = false, length = 50)
	public String getPhoneNbr() {
		return phoneNbr;
	}
	public void setPhoneNbr(String phoneNbr) {
		this.phoneNbr = phoneNbr;
	}
	
	@Column(name = "ALT_PHONE", nullable = true, length = 50)
	public String getAltPhoneNbr() {
		return altPhoneNbr;
	}
	public void setAltPhoneNbr(String altPhoneNbr) {
		this.altPhoneNbr = altPhoneNbr;
	}
	
	@Column(name = "NOTE_TEXT", nullable = true, length = 50)
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@Column(name = "STATUS_CD", nullable = false, length = 20)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "LOCKED_BY", nullable = true, length = 100)
	public String getLockedBy() {
		return lockedBy;
	}
	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	@Column(name = "UPDATED_BY", nullable = false, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}
}
