package com.belk.car.app.model.workflow;

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
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.BaseAuditableModel;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.WorkFlow;

@Entity
@Table(name = "WORKFLOW_TRANSITION", uniqueConstraints = @UniqueConstraint(columnNames = "WORKFLOW_TRANSITION_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class WorkflowTransition extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7858808165554601261L;

	private long workflowTransitionId;
	private int numberOfDaysToCompleteTask;
	private String transitionName;
	private String description;
	private WorkFlow workflow = new WorkFlow();
	private UserType currentUserType = new UserType();
	private UserType transitionToUserType = new UserType();
	private WorkflowStatus currentStatus = new WorkflowStatus();
	private WorkflowStatus transitionStatus = new WorkflowStatus();
	private WorkflowTask workflowTask = new WorkflowTask();

	public WorkflowTransition() {
	}


	@Id 
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="WORKFLOW_TRANSITION_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name="WORKFLOW_TRANSITION_SEQ_GEN", sequenceName="WORKFLOW_TRANSITION_SEQ", allocationSize=1)
	@Column(name = "WORKFLOW_TRANSITION_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getWorkflowTransitionId() {
		return workflowTransitionId;
	}


	public void setWorkflowTransitionId(long workflowTransitionId) {
		this.workflowTransitionId = workflowTransitionId;
	}


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "WORKFLOW_ID", nullable = false)
	public WorkFlow getWorkflow() {
		return workflow;
	}


	public void setWorkflow(WorkFlow workflow) {
		this.workflow = workflow;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CURR_USER_TYPE_CD", nullable = false)
	public UserType getCurrentUserType() {
		return currentUserType;
	}


	public void setCurrentUserType(UserType currentUserType) {
		this.currentUserType = currentUserType;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TRAN_USER_TYPE_CD", nullable = false)
	public UserType getTransitionToUserType() {
		return transitionToUserType;
	}


	public void setTransitionToUserType(UserType transitionToUserType) {
		this.transitionToUserType = transitionToUserType;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CURR_WORKFLOW_STATUS_CD", nullable = false)
	public WorkflowStatus getCurrentStatus() {
		return currentStatus;
	}


	public void setCurrentStatus(WorkflowStatus currentStatus) {
		this.currentStatus = currentStatus;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TRAN_WORKFLOW_STATUS_CD", nullable = false)
	public WorkflowStatus getTransitionStatus() {
		return transitionStatus;
	}


	public void setTransitionStatus(WorkflowStatus transitionStatus) {
		this.transitionStatus = transitionStatus;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WORKFLOW_TASK_CD", nullable = false)
	public WorkflowTask getWorkflowTask() {
		return workflowTask;
	}


	public void setWorkflowTask(WorkflowTask workflowTask) {
		this.workflowTask = workflowTask;
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

	/**
	 * @return the numberOfDaysToCompleteTask
	 */
	@Column(name="NUMBER_DAYS_TO_COMPLETE_TASK", nullable=false, precision=2, scale=0)
	public int getNumberOfDaysToCompleteTask() {
		return numberOfDaysToCompleteTask;
	}


	/**
	 * @return the transitionName
	 */
	@Column(name = "TRANSITION_NAME", nullable = false, length = 100)
	public String getTransitionName() {
		return transitionName;
	}


	/**
	 * @param transitionName the transitionName to set
	 */
	public void setTransitionName(String transitionName) {
		this.transitionName = transitionName;
	}


	/**
	 * @param numberOfDaysToCompleteTask the numberOfDaysToCompleteTask to set
	 */
	public void setNumberOfDaysToCompleteTask(int numberOfDaysToCompleteTask) {
		this.numberOfDaysToCompleteTask = numberOfDaysToCompleteTask;
	}


	/**
	 * @return the description
	 */
	@Column(name = "DESCR", nullable = false, length = 200)
	public String getDescription() {
		return description;
	}


	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
    public String toString(){
    	return transitionName;
    }
}
