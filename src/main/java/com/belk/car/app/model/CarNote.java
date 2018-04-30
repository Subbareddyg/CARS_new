package com.belk.car.app.model;

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

@Entity
@Table(name = "CAR_NOTE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CarNote extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6001336114648742148L;
	private long carNoteId;
	private NoteType noteType;
	private Car car;
	private String isExternallyDisplayable;
	private String noteText;
	private String statusCd;

	public CarNote() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAR_NOTE_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CAR_NOTE_SEQ_GEN", sequenceName = "CAR_NOTE_SEQ", allocationSize = 1)
	@Column(name = "CAR_NOTE_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getCarNoteId() {
		return this.carNoteId;
	}

	public void setCarNoteId(long carNoteId) {
		this.carNoteId = carNoteId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NOTE_TYPE_CD", nullable = false)
	public NoteType getNoteType() {
		return this.noteType;
	}

	public void setNoteType(NoteType noteType) {
		this.noteType = noteType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CAR_ID", nullable = false)
	public Car getCar() {
		return this.car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	@Column(name = "IS_EXTERNALLY_DISPLAYABLE", nullable = false, length = 1)
	public String getIsExternallyDisplayable() {
		return this.isExternallyDisplayable;
	}

	public void setIsExternallyDisplayable(String isExternallyDisplayable) {
		this.isExternallyDisplayable = isExternallyDisplayable;
	}

	@Column(name = "NOTE_TEXT", nullable = false, length = 4000)
	public String getNoteText() {
		return this.noteText;
	}

	public void setNoteText(String noteText) {
		this.noteText = noteText;
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

}
