package com.belk.car.app.model.vendorcatalog;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

/**
 * This is the model class for table name vendor_catalog_dept
 * @version 1.0 10 December 2009
 * @author afusya2
 */
@Entity
@Table(name = "vendor_catalog_dept")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class VendorCatalogDepartment extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -753143086304504610L;
	private CompositeKeyForVndrCatlDept compositeKeyForVndrCatlDept;
	private String statusCD;

	/**
	 * 
	 */
	public VendorCatalogDepartment() {
		super();
	}
	
	@EmbeddedId
	public CompositeKeyForVndrCatlDept getCompositeKeyForVndrCatlDept() {
		return compositeKeyForVndrCatlDept;
	}



	public void setCompositeKeyForVndrCatlDept(
			CompositeKeyForVndrCatlDept compositeKeyForVndrCatlDept) {
		this.compositeKeyForVndrCatlDept = compositeKeyForVndrCatlDept;
	}



	@Column(name = "status_cd", unique = false, length = 20)
	public String getStatusCD() {
		return statusCD;
	}
	
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

	@Column(name = "UPDATED_BY", nullable = false, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}
	

}
