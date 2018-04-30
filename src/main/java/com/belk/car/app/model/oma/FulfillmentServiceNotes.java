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

@Entity
@Table(name = "fulfmnt_service_note",uniqueConstraints = @UniqueConstraint(columnNames = "fulfmnt_service_note_id"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class FulfillmentServiceNotes  extends BaseAuditableModel implements java.io.Serializable {

	private static final long serialVersionUID = 2667713137037878558L;
	
	private long fulfillmentServiceNoteID;
	private FulfillmentService fulfillmentServiceID1;
	private String fulfillmentServiceNotesSubject;
	private String fulfillmentServiceNotesDesc;
	private String fulfillmentServiceNotesStatusCode;
	
	
	
	/*Fulfillment Service Notes Constructor*/
	public FulfillmentServiceNotes() {
		super();
	}

	
	@Id 
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FULFILLMENT_SERV_NOTES_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "FULFILLMENT_SERV_NOTES_SEQ_GEN", sequenceName = " SEQ_FULFMNT_SERVICE_NOTE_ID", allocationSize = 1)
	@Column(name = "fulfmnt_service_note_id", unique = true, nullable = false, precision = 12, scale = 0)
	public long getFulfillmentServiceNoteID() {
		return fulfillmentServiceNoteID;
	}

	public void setFulfillmentServiceNoteID(long fulfillmentServiceNoteID) {
		this.fulfillmentServiceNoteID = fulfillmentServiceNoteID;
	}
	
	
	@Column(name = "subject", nullable = false, length = 100)
	public String getFulfillmentServiceNotesSubject() {	
		return fulfillmentServiceNotesSubject;
	}

	public void setFulfillmentServiceNotesSubject(String fulfillmentServiceNotesSubject) {
		this.fulfillmentServiceNotesSubject = fulfillmentServiceNotesSubject;
	}
	
	@Column(name = "note_text", length = 200,nullable=false)
	public String getFulfillmentServiceNotesDesc() {
		return fulfillmentServiceNotesDesc;
	}

	public void setFulfillmentServiceNotesDesc(String fulfillmentServiceNotesDesc) {
		this.fulfillmentServiceNotesDesc = fulfillmentServiceNotesDesc;
	}
	
	@ManyToOne
	@JoinColumn(name="fulfmnt_service_id", nullable = false)
	public FulfillmentService getFulfillmentServiceID1() {
		return fulfillmentServiceID1;
	}

	public void setFulfillmentServiceID1(FulfillmentService fulfillmentServiceID1) {
		this.fulfillmentServiceID1 = fulfillmentServiceID1;
	}
	
	
	@Column(name = "status_cd", nullable = false, length=20)
	public String getFulfillmentServiceNotesStatusCode() {
		return fulfillmentServiceNotesStatusCode;
	}

	public void setFulfillmentServiceNotesStatusCode(String fulfillmentServiceNotesStatusCode) {
		this.fulfillmentServiceNotesStatusCode = fulfillmentServiceNotesStatusCode;
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
