package com.belk.car.app.model.vendorcatalog;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

/**
 * This is the model class for table name vndr_catl_note
 * @version 1.0 10 December 2009
 * @author afusya2
 */
@Entity
@Table(name = "vendor_catalog_note")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class VendorCatalogNote extends BaseAuditableModel implements java.io.Serializable{
	  
	/**
	 * 
	 */
	private static final long serialVersionUID = -6001336114648742148L;
	
	private long catalogNoteID;
	private VendorCatalog vendorCatalog;
	private String subject;
	private String noteText;
	private String statusCD;
	

	/**
	 * @return the catalogNoteID
	 */
	@Id 
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATL_NOTE_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CATL_NOTE_SEQ_GEN", sequenceName = "SEQ_VENDOR_CATALOG_NOTE_ID", allocationSize = 1)
	@Column(name = "vendor_catalog_note_id", unique = false, length = 12)	
	public long getCatalogNoteID() {
		return catalogNoteID;
	}

	/**
	 * @param catalogNoteID the catalogNoteID to set
	 */
	public void setCatalogNoteID(long catalogNoteID) {
		this.catalogNoteID = catalogNoteID;
	}

	/**
	 * @return the vendorCatalog
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_catalog_id", nullable = false)
	public VendorCatalog getVendorCatalog() {
		return vendorCatalog;
	}

	/**
	 * @param vendorCatalog the vendorCatalog to set
	 */
	public void setVendorCatalog(VendorCatalog vendorCatalog) {
		this.vendorCatalog = vendorCatalog;
	}

	/**
	 * @return the subject
	 */
	@Column(name = "subject", unique = false, length = 50)
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the noteText
	 */
	@Column(name = "note_text", unique = false, length = 4000)
	public String getNoteText() {
		return noteText;
	}

	/**
	 * @param noteText the noteText to set
	 */
	public void setNoteText(String noteText) {
		this.noteText = noteText;
	}

	/**
	 * @return the statusCD
	 */
	@Column(name = "status_cd", unique = false, length = 20)
	public String getStatusCD() {
		return statusCD;
	}

	/**
	 * @param statusCD the statusCD to set
	 */
	public void setStatusCD(String statusCD) {
		this.statusCD = statusCD;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_date", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	@Column(name = "updated_by", nullable = false, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	@Column(name = "created_by", nullable = false, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}
}
