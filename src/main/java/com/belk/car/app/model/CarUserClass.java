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

@Entity
@Table(name = "CAR_USER_CLASS")
public class CarUserClass extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5655554825070107546L;

	private long carUserClassId;
	private Classification classification;
	private User carUser;

	public CarUserClass() {
	}


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAR_USER_CLASS_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CAR_USER_CLASS_SEQ_GEN", sequenceName = "CAR_USER_CLASS_SEQ", allocationSize = 1)
	@Column(name = "CAR_USER_CLASS_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getCarUserClassId() {
		return this.carUserClassId;
	}

	public void setCarUserClassId(long carUserClassId) {
		this.carUserClassId = carUserClassId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASS_ID", nullable = false)
	public Classification getClassification() {
		return this.classification;
	}

	public void setClassification(Classification classification) {
		this.classification = classification;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CAR_USER_ID", nullable = false)
	public User getCarUser() {
		return this.carUser;
	}

	public void setCarUser(User carUser) {
		this.carUser = carUser;
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
