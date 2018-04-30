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
@Table(name = "NOTE_TYPE", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, include = "all")
public class NoteType extends BaseAuditableModel implements
		java.io.Serializable {

	/**
	 * 
	 */
	public static final String USER_NOTES = "USER_NOTES";
	public static final String DEFAULT_USER_NOTE = "USER_NOTES";
	public static final String SAMPLE_NOTES = "SAMPLE_NOTES" ;
	public static final String CAR_NOTES = "CAR_NOTES" ;
	public static final String ITEM_FAILURE_NOTES = "ITEM_FAILURE_NOTES" ;
	public static final String PIM_IMAGE_FAIL_NOTES = "PIM_IMAGE_FAIL_NOTES";
	private static final long serialVersionUID = -3531402321990160822L;

	private String noteTypeCd;
	private String name;
	private String descr;
	private String statusCd;

	//private Set<CarNote> carNotes = new HashSet<CarNote>(0);
	//private Set<CarUserNote> carUserNotes = new HashSet<CarUserNote>(0);

	public NoteType() {
	}

	@Id
	@Column(name = "NOTE_TYPE_CD", unique = true, nullable = false, length = 20)
	public String getNoteTypeCd() {
		return this.noteTypeCd;
	}

	public void setNoteTypeCd(String noteTypeCd) {
		this.noteTypeCd = noteTypeCd;
	}

	@Column(name = "NAME", unique = true, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCR", nullable = false, length = 200)
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

	/*
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "noteType")
	public Set<CarNote> getCarNotes() {
		return this.carNotes;
	}

	public void setCarNotes(Set<CarNote> carNotes) {
		this.carNotes = carNotes;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "noteType")
	public Set<CarUserNote> getCarUserNotes() {
		return this.carUserNotes;
	}

	public void setCarUserNotes(Set<CarUserNote> carUserNotes) {
		this.carUserNotes = carUserNotes;
	}
	*/

}
