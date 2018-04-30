package com.belk.car.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.workflow.WorkflowStatus;

@Entity
@Table(name = "Late_Cars_Parameters")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class LateCarsParams extends BaseAuditableModel implements java.io.Serializable {

	private static final long serialVersionUID = -7540238555995568939L;
	private long lateCarsParamId;
	private UserType owner;
	private long weeksdue;
	private WorkflowStatus status;
	private LateCarsGroup lateCarsGroup;
	

	public LateCarsParams() {
		
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAR_USER_VENDOR_DEPARTMENT_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CAR_USER_VENDOR_DEPARTMENT_SEQ_GEN", sequenceName = "CAR_USER_VENDOR_DEPARTMENT_SEQ", allocationSize = 1)
	@Column(name = "LATE_CARS_PARAMS_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getLateCarsParamId() {
		return lateCarsParamId;
	}
	
	public void setLateCarsParamId(long lateCarsParamId) {
		this.lateCarsParamId = lateCarsParamId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OWNER", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public UserType getOwner() {
		return owner;
	}
	
	public void setOwner(UserType owner) {
		this.owner = owner;
	}
		
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)	
	public WorkflowStatus getStatus() {
		return status;
	}
	
	public void setStatus(WorkflowStatus status) {
		this.status = status;
	}
	
	@Column(name = "WEEKSDUE", unique = true, nullable = false, length = 50)
	public long getWeeksdue() {
		return weeksdue;
	}
	
	public void setWeeksdue(long weeksdue) {
		this.weeksdue = weeksdue;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LATE_CARS_GROUP_ID", nullable = false)
	public LateCarsGroup getLateCarsGroup() {
		return lateCarsGroup;
	}
	
	public void setLateCarsGroup(LateCarsGroup lateCarsGroup) {
		this.lateCarsGroup = lateCarsGroup;
	}
	
	public String toString() {
		StringBuffer sbObjectValue = new StringBuffer();
		sbObjectValue.append("\nowner --" + owner);
		sbObjectValue.append("\nweeksdue --" + weeksdue);
		sbObjectValue.append("\nstatus --" + status);
		return sbObjectValue.toString();
	}
}
