package com.belk.car.app.model;

import java.util.Date;

import javax.persistence.CascadeType;
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
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.belk.car.app.model.workflow.WorkflowStatus;

@Entity
@Table(name = "CAR_HISTORY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CarHistory implements java.io.Serializable, Comparable<CarHistory>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4555236784072846659L;
	private long carHistId;
	private Car car;
	private UserType assignedToUserType;
	private WorkflowStatus workflowStatus;
	protected String createdBy;
	protected Date createdDate;
	protected int daysWithUser;
	

	public CarHistory() {
		daysWithUser = -1;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAR_HIST_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CAR_HIST_SEQ_GEN", sequenceName = "car_history_seq", allocationSize = 1)
	@Column(name = "CAR_HIST_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getCarHistId() {
		return this.carHistId;
	}

	public void setCarHistId(long carHistId) {
		this.carHistId = carHistId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CAR_ID", nullable = false)
	public Car getCar() {
		return this.car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	@ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinColumn(name = "ASSIGNED_TO_USER_TYPE_CD", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@NotFound(action = NotFoundAction.IGNORE)

	public UserType getAssignedToUserType() {
		

		if(assignedToUserType ==null) {
			assignedToUserType=new UserType();
		    assignedToUserType.setName("WEB_MERCHANT");
		}else if(assignedToUserType.getName() ==null) {
			assignedToUserType.setName("WEB_MERCHANT");
		}
			
		return this.assignedToUserType;
	}

	public void setAssignedToUserType(UserType assignedToUserType) {
		this.assignedToUserType = assignedToUserType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WORKFLOW_STATUS", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public WorkflowStatus getWorkflowStatus() {
		return this.workflowStatus;
	}

	public void setWorkflowStatus(WorkflowStatus workflowStatus) {
		this.workflowStatus = workflowStatus;
	}


	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name = "DAYS_WITH_USER", nullable = false, length = 4)
	public int getDaysWithUser() {
		return daysWithUser;
	}

	public void setDaysWithUser(int daysWithUser) {
		this.daysWithUser = daysWithUser;
	}
	
    public String toString(){
    	StringBuffer strBuffCarHistory = new StringBuffer();
    	
    	if(assignedToUserType !=null && assignedToUserType.getName() !=null) {
    		strBuffCarHistory.append("History ").append(carHistId)
    					 .append(" for CAR :").append(car.getCarId()).append(" Assigned to: ").append(assignedToUserType.getName())
    					 .append(" workflow status: ").append(workflowStatus.getName());
    	} else {
    		strBuffCarHistory.append("History ").append(carHistId)
			 .append(" for CAR :").append(car.getCarId()).append(" Assigned to: ").append("WEB_MERCHANT")
			 .append(" workflow status: ").append(workflowStatus.getName());
    	}
    	 
    	return strBuffCarHistory.toString();
    }


	@Override
	public int compareTo(CarHistory hist) {
		// TODO Auto-generated method stub
		return this.createdDate.compareTo(hist.getCreatedDate()); 
	}
	


	
}
