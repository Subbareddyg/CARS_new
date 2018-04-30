package com.belk.car.app.model.car;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

@Entity
@Table(name = "MANUAL_CAR_PROCESS_STATUS")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, include = "all")
public class ManualCarProcessStatus extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8577238242247941511L;

	public static final String INITIATED = "INITIATED";
	public static final String EXPORTED = "EXPORTED";
	public static final String PROCESSING = "PROCESSING";
	public static final String COMPLETED = "COMPLETED";
	public static final String COMPLETED_WITH_ERROR = "COMPLETED_WITH_ERR";

	
	private String statusCd;
	private String name;
	private String descr;

	public ManualCarProcessStatus() {
	}

	@Id
	@Column(name = "PROCESS_STATUS_CD", unique = true, nullable = false, length = 20)
	public String getStatusCd() {
		return this.statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}

	@Column(name = "NAME", nullable = false, length = 20)
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
