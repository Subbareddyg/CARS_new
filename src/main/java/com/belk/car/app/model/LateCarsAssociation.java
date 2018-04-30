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
@Table(name = "LATE_CARS_ASSOCIATION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LateCarsAssociation extends BaseAuditableModel implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2513713886697403226L;

	private LateCarsGroup lateCarsGroup;
	private Department dept;
	private Vendor vendor;
	private Long lateCarsAssociationId;
	private String statusCd;

	public LateCarsAssociation() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LATE_CARS_ASSOCIATION_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "LATE_CARS_ASSOCIATION_SEQ_GEN", sequenceName = "LATE_CARS_ASSOCIATION_SEQ", allocationSize = 1)
	@Column(name = "LATE_CARS_ASSOCIATION_ID", unique = true, nullable = false, length = 100)
	public Long getLateCarsAssociationId() {
		return lateCarsAssociationId;
	}

	public void setLateCarsAssociationId(Long lateCarsAssociationId) {
		this.lateCarsAssociationId = lateCarsAssociationId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LATE_CARS_GROUP_ID", nullable = false)
	public LateCarsGroup getLateCarsGroup() {
		return lateCarsGroup;
	}

	public void setLateCarsGroup(LateCarsGroup lateCarsGroup) {
		this.lateCarsGroup = lateCarsGroup;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEPT_ID", nullable = true)
	public Department getDept() {
		return dept;
	}

	public void setDept(Department dept) {
		this.dept = dept;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VENDOR_ID", nullable = true)
	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
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
