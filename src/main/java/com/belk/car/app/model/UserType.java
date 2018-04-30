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
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "USER_TYPE", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, include="all")
public class UserType extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1131152819149825422L;

	public static final String BUYER = "BUYER";
	public static final String CONTENT_MANAGER = "CONTENT_MANAGER";
	public static final String CONTENT_WRITER = "CONTENT_WRITER";
	public static final String ART_DIRECTOR = "ART_DIRECTOR";
	public static final String SAMPLE_COORDINATOR = "SAMPLE_COORDINATOR"; 
	public static final String VENDOR = "VENDOR";
	//public static final String WEB_MERCHANT = "WEB_MERCHANT";
	public static final String ECOM_OPERATOR = "ECOM_OPERATION";
	
	public static final String VENDOR_PROVIDED_IMAGE = "VENDOR_PROVIDED_IMAGE";
	
	private String userTypeCd;
	private String name;
	private String descr;
	private String statusCd;

	public UserType() {
	}

	@Id
	@Column(name = "USER_TYPE_CD", unique = true, nullable = false, length = 20)
	public String getUserTypeCd() {
		return this.userTypeCd;
	}

	public void setUserTypeCd(String userTypeCd) {
		this.userTypeCd = userTypeCd;
	}

	@Column(name = "NAME", unique = true, length = 50)
	@NotFound(action = NotFoundAction.IGNORE)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCR", nullable = false, length = 200)
	public String getDescription() {
		return this.descr;
	}

	public void setDescription(String descr) {
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
	
}
