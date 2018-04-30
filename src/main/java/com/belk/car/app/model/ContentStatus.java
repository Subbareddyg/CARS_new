package com.belk.car.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "CONTENT_STATUS")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, include = "all")
public class ContentStatus extends BaseAuditableModel implements
		java.io.Serializable {
	
	/**
	 * Object serialized identifier
	 */
	private static final long serialVersionUID = 4031808744502776403L;

	public static String APPROVAL_REQUESTED = "APPROVAL_REQUESTED";        
	public static String APPROVED = "APPROVED";                            
	public static String IN_PROGRESS = "IN-PROGRESS";                      
	public static String PUBLISHED = "PUBLISHED";                          
	public static String REJECTED = "REJECTED";
	public static String SENT_TO_CMP = "SENT_TO_CMP";
	public static String RESEND_TO_CMP = "RESEND_TO_CMP";
	public static String RESEND_DATA_TO_CMP = "RESEND_DATA_TO_CMP";
	public static String DISABLE_IN_CMP = "DISABLE_IN_CMP";
	public static String ENABLE_IN_CMP = "ENABLE_IN_CMP";

	private String code;
	private String name;
	private String descr;

	public ContentStatus() {
	}


	@Id
	@Column(name = "CONTENT_STATUS_CD", unique = true, nullable = false, length = 20)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME", nullable = false, length = 50)
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

	@Override
	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}

	@Override
	@Column(name = "UPDATED_BY", nullable = false, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	@Override
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	@Override
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

}
