package com.belk.car.app.model.oma;

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

import com.belk.car.app.model.BaseAuditableModel;
import com.belk.car.app.model.workflow.WorkflowStatus;

@Entity
@Table(name = "vifr_workflow_hist")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class ItemRequestWorkflow extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3425122067554872589L;
	private Long itmrqstWorkflowId;
	private ItemRequest itemRequest;
	private VFIRStatus workflowStatus;
	private String action;
	private String rejectReason;
	
	/**
	 * Default constructor.
	 */
	public ItemRequestWorkflow() {
		super();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VFIR_WORKFLOW_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "VFIR_WORKFLOW_SEQ_GEN", sequenceName = "SEQ_VIFR_WORKFLOW_HIST_ID", allocationSize = 1)
	@Column(name = "vifr_workflow_hist_id", nullable = false, precision = 12, scale = 0)
	public Long getItmrqstWorkflowId() {
		return itmrqstWorkflowId;
	}
	public void setItmrqstWorkflowId(Long itmrqstWorkflowId) {
		this.itmrqstWorkflowId = itmrqstWorkflowId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vfir_id", nullable = false)
	public ItemRequest getItemRequest() {
		return itemRequest;
	}
	public void setItemRequest(ItemRequest itemRequest) {
		this.itemRequest = itemRequest;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VIFR_WORKFLOW_STATUS_CD", nullable = false)
	public VFIRStatus getWorkflowStatus() {
		return workflowStatus;
	}
	public void setWorkflowStatus(VFIRStatus workflowStatus) {
		this.workflowStatus = workflowStatus;
	}
	
	@Column(name = "action_text", nullable = false, length = 50)
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	@Column(name = "reject_reason_text", nullable = true, length = 2000)
	public String getRejectReason() {
		return rejectReason;
	}
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
	
	@Column(name = "UPDATED_BY", nullable = false, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}
	
	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}
}
