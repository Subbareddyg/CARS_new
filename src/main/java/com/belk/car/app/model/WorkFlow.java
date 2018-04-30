package com.belk.car.app.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.model.workflow.WorkflowTransition;
import com.belk.car.app.model.workflow.WorkflowType;

@Entity
@Table(name = "WORKFLOW", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WorkFlow extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4637197551130323288L;

	private long workflowId;
	private String name;
	private String description;
	private String statusCode;
    //private List<String> fieldOrder;   
    private List<WorkflowStatus> supportedStatus;
    private Map<Integer, String> states;
    private WorkflowType workflowType ;
    private Set<WorkflowTransition> workflowTransitions = new HashSet<WorkflowTransition>(0);
    
	public WorkFlow() {
	}

	
	@Id 
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="WORKFLOW_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "WORKFLOW_SEQ_GEN", sequenceName = "WORKFLOW_SEQ", allocationSize = 1)
	@Column(name = "WORKFLOW_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getWorkflowId() {
		return this.workflowId;
	}
	
	public void setWorkflowId(long workflowId) {
		this.workflowId = workflowId;
	}

	@Column(name = "NAME", unique = true, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCR", length = 2000)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "STATUS_CD", nullable = false, length = 1)
	public String getStatusCode() {
		return this.statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
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
    
    //====================================================================
    


    @Transient
    public String getStatusValue(Integer key) {
        if (key == null) {
            return "";
        }
        String s = states.get(key);
        if (s == null) {
            return "";
        }
        return s;
    }
 
    @Transient
    public int getStateCount() {
        return states.size();
    }
    
   
    @Transient
    public Map<Integer, String> getStates() {
        return states;
    }
   /* @Transient
    public List<String> getFieldOrder() {
        return fieldOrder;
    }   */
    
   
   
    public void addWorkStatus(WorkflowStatus status) {
    	getSupportedStatus().add(status);
    }
    

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JoinTable(name = "SUPPORTED_WORKFLOW_STATUS", joinColumns = { @JoinColumn(name = "WORKFLOW_ID") }, inverseJoinColumns = @JoinColumn(name = "WORKFLOW_STATUS_CD"))	
	public List<WorkflowStatus> getSupportedStatus() {
		return supportedStatus;
	}

	public void setSupportedStatus(List<WorkflowStatus> supportedStatus) {
		this.supportedStatus = supportedStatus;
	}


	/**
	 * @return the workflowType
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "WORKFLOW_TYPE_CD", nullable = false)
	public WorkflowType getWorkflowType() {
		return workflowType;
	}


	/**
	 * @param workflowType the workflowType to set
	 */
	public void setWorkflowType(WorkflowType workflowType) {
		this.workflowType = workflowType;
	}

	public void removeWorkflowTransition(WorkflowTransition workflowTransition) {
		if(getWorkflowTransitions()!=null && getWorkflowTransitions().size()>0) {
			getWorkflowTransitions().remove(workflowTransition);
		}
	}
	
	public void addWorkflowTransition(WorkflowTransition workflowTransition) {
		if(getWorkflowTransitions()!= null) {
			getWorkflowTransitions().add(workflowTransition);
		}
	}
	

	/**
	 * @return the workflowTransitions
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "workflow")
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
          org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public Set<WorkflowTransition> getWorkflowTransitions() {
		return workflowTransitions;
	}


	/**
	 * @param workflowTransitions the workflowTransitions to set
	 */
	public void setWorkflowTransitions(Set<WorkflowTransition> workflowTransitions) {
		this.workflowTransitions = workflowTransitions;
	}
}
