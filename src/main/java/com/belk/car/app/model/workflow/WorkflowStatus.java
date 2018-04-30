package com.belk.car.app.model.workflow;

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

import com.belk.car.app.model.BaseAuditableModel;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, include="all")
@Table(name = "WORKFLOW_STATUS", uniqueConstraints = @UniqueConstraint(columnNames = "WORKFLOW_STATUS_CD"))
public class WorkflowStatus extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5096216252035320175L;

	public static final String INITIATED = "INITIATED";
	public static final String IN_PROCESS = "IN-PROCESS";
	public static final String APPROVED = "APPROVED";
	public static final String PUBLISHED = "PUBLISHED";
	public static final String CLOSED = "CLOSED";
	public static final String READY_FOR_REVIEW = "READY_FOR_REVIEW";
	public static final String WITH_VENDOR = "WITH_VENDOR";

	public static final String IMAGES_REVIEWED = "IMAGES_REVIEWED";                                                                          
	public static final String IMAGE_APPROVAL_REQ = "IMAGE_APPROVAL_REQ";                                                                    
	public static final String IMAGE_SHOT_APPROVED = "IMAGE_SHOT_APPROVED";                                                                  
	public static final String NEED_MORE_INFO = "NEED_MORE_INFO";                                                                            
	public static final String SAMPLE_MANAGEMENT = "SAMPLE_MANAGEMENT";                                                                      
	public static final String WAITING_FOR_SAMPLE = "WAITING_FOR_SAMPLE";
	
	public static final String PENDING = "PENDING";
	public static final String IMAGE_FAILED_IN_CC = "IMAGE_FAILED_IN_CC";
	public static final String IMAGE_FAILED_IN_MC = "IMAGE_FAILED_IN_MC";
	
	
	private String statusCd;
	private String name;
	private String descr;

	public WorkflowStatus() {
	}

	@Id
	@Column(name = "WORKFLOW_STATUS_CD", unique = true, nullable = false, length = 20)
	public String getStatusCd() {
		return this.statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}

	@Column(name = "NAME", unique=true, nullable = false, length = 50)
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
