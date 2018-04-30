package com.belk.car.app.model.oma;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;
import com.belk.car.app.model.Vendor;

@Entity
@Table(name = "fulfmnt_serv_vndr_note",uniqueConstraints = @UniqueConstraint(columnNames = "fulflmnt_serv_vndr_note_id"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class VendorFulfillmentNotes  extends BaseAuditableModel implements java.io.Serializable {

	private static final long serialVersionUID = 2667713137037878558L;
	private long vendorFulfillmentNoteID;
	private Vendor vendorID;
	private FulfillmentService fulfillmentServiceID;
	private String vendorFulfillmentNotesSubject;
	private String vendorFulfillmentNotesText;
	private String vendorFulfillmentNotesStatusCode;
	
	
	
	/*Fulfillment Service Notes Constructor*/
	public VendorFulfillmentNotes() {
		super();
	}

	
	@Id 
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FULFILLMENT_SERV_NOTES_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "FULFILLMENT_SERV_NOTES_SEQ_GEN", sequenceName = " SEQ_FULFMNT_SERVICE_NOTE_ID", allocationSize = 1)
	@Column(name = "fulflmnt_serv_vndr_note_id", unique = true, nullable = false, precision = 12, scale = 0)
	public long getVendorFulfillmentNoteID() {
		return vendorFulfillmentNoteID;
	}

	public void setVendorFulfillmentNoteID(long vendorFulfillmentNoteID) {
		this.vendorFulfillmentNoteID = vendorFulfillmentNoteID;
	}
	
	
	@Column(name = "subject", nullable = false, length = 100)
	public String getVendorFulfillmentNotesSubject() {	
		return vendorFulfillmentNotesSubject;
	}

	public void setVendorFulfillmentNotesSubject(String vendorFulfillmentNotesSubject) {
		this.vendorFulfillmentNotesSubject = vendorFulfillmentNotesSubject;
	}
	
	@Column(name = "note_text", length = 200,nullable=false)
	public String getVendorFulfillmentNotesText() {
		return vendorFulfillmentNotesText;
	}

	public void setVendorFulfillmentNotesText(String vendorFulfillmentNotesText) {
		this.vendorFulfillmentNotesText = vendorFulfillmentNotesText;
	}
	
	@ManyToOne
	@JoinColumn(name="fulflmnt_service_id", nullable = false)
	public FulfillmentService getFulfillmentServiceID() {
		return fulfillmentServiceID;
	}

	public void setFulfillmentServiceID(FulfillmentService fulfillmentServiceID) {
		this.fulfillmentServiceID = fulfillmentServiceID;
	}
	
	@ManyToOne
	@JoinColumn(name="vendor_id", nullable = false)
	public Vendor getVendorID() {
		return vendorID;
	}

	public void setVendorID(Vendor vendorID) {
		this.vendorID = vendorID;
	}
	
	
	@Column(name = "status_cd", nullable = false, length=20)
	public String getVendorFulfillmentNotesStatusCode() {
		return vendorFulfillmentNotesStatusCode;
	}

	public void setVendorFulfillmentNotesStatusCode(String vendorFulfillmentNotesStatusCode) {
		this.vendorFulfillmentNotesStatusCode = vendorFulfillmentNotesStatusCode;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_date",nullable=false)
	public Date getCreatedDate() {
		return this.createdDate;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_date",nullable=false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	@Column(name="created_by",nullable=false)
	public String getCreatedBy() {
		return this.createdBy;
	}

	@Column(name="updated_by",nullable=false)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

//	public Long getFulfillmentServiceIDForNotes(){
//		FulfillmentService fulfillmentService=new FulfillmentService();
//		fulfillmentService.setFulfillmentServiceID(new Long(fulfillmentServiceNoteID));
//		return this.fulfillmentServiceID1.getFulfillmentServiceID();
//	}
}
