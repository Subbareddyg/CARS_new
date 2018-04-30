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

import org.appfuse.model.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "CAR_USER_NOTE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class CarUserNote extends BaseAuditableModel implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7951906802239333990L;
	public static final String YES = "Y";
	public static final String ACTIVE = "ACTIVE";

	private Long carUserNoteId;
	private NoteType noteType;
	private User carUser;
	private String isExternallyDisplayable;
	private String noteText;
	private String statusCd;

	public CarUserNote() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAR_USER_NOTE_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CAR_USER_NOTE_SEQ_GEN", sequenceName = "CAR_USER_NOTE_SEQ", allocationSize = 1)
	@Column(name = "CAR_USER_NOTE_ID", unique = true, nullable = false, length = 100)
	public Long getCarUserNoteId() {
		return this.carUserNoteId;
	}

	public void setCarUserNoteId(Long carUserNoteId) {
		this.carUserNoteId = carUserNoteId;
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
	@JoinColumn(name = "CAR_USER_ID", nullable = false)
	public User getCarUser() {
		return this.carUser;
	}

	public void setCarUser(User carUser) {
		this.carUser = carUser;
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

	@Column(name = "STATUS_CD", nullable = false, length = 20)
	public String getStatusCd() {
		return this.statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}

}
