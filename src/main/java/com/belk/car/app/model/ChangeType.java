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
@Table(name = "CHANGE_TYPE", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, include = "all")
public class ChangeType extends BaseAuditableModel implements
		java.io.Serializable {
	
	public static String CAR_CREATED = "CAR_CREATED";
	public static String ATTRIBUTE_UPDATED = "ATTRIBUTE_UPDATED";
	public static String CAR_APPROVED = "CAR_APPROVED";
	public static String NOTES_CREATED = "NOTES_CREATED";

	/**
	 * 
	 */
	private static final long serialVersionUID = 718919694656843029L;
	private String changeTypeCd;
	private String name;
	private String descr;
	private String statusCd;

	@Id
	@Column(name = "CHANGE_TYPE_CD", unique = true, nullable = false, length = 20)
	public String getChangeTypeCd() {
		return this.changeTypeCd;
	}

	public void setChangeTypeCd(String changeTypeCd) {
		this.changeTypeCd = changeTypeCd;
	}

	@Column(name = "NAME", unique = true, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCR", length = 100)
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
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "changeType")
	public Set<CarHistory> getCarHistories() {
		return this.carHistories;
	}

	public void setCarHistories(Set<CarHistory> carHistories) {
		this.carHistories = carHistories;
	}
*/
}
