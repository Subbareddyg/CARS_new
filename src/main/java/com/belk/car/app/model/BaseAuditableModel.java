package com.belk.car.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;

public class BaseAuditableModel extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5465884302921889746L;

	public static final String FLAG_NO = "N" ;
	public static final String FLAG_YES = "Y" ;
	
	
	protected String createdBy;
	protected String updatedBy;
	protected Date createdDate;
	protected Date updatedDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Column(name = "UPDATED_BY", nullable = false, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}


	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public void setAuditInfo(User user) {
		if (StringUtils.isBlank(this.getCreatedBy())) {
			this.setCreatedBy(user.getUsername());
			this.setCreatedDate(new Date());
		}
		this.setUpdatedBy(user.getUsername());
		this.setUpdatedDate(new Date());
	}
}
