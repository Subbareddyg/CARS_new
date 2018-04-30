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
@Table(name = "WORKFLOW_TASK", uniqueConstraints = @UniqueConstraint(columnNames = "WORKFLOW_TASK_CD"))
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, include="all")
public class WorkflowTask extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7494072894380894604L;

	private String taskCd;
	private String name;
	private String className;

	public WorkflowTask() {
	}

	@Id
	@Column(name = "WORKFLOW_TASK_CD", unique = true, nullable = false, length = 20)
	public String getTaskCd() {
		return this.taskCd;
	}

	public void setTaskCd(String taskCd) {
		this.taskCd = taskCd;
	}

	@Column(name = "NAME", unique=true, nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "TASK_CLASS_NAME", nullable = false, length = 200)
	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
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
