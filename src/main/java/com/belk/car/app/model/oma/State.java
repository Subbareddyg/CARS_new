/**
 * Class Name : State.java 
 * Version Information : v1.0 
 * Date : 12/01/2009 
 * Copyright Notice : All rights are reserved to Syntel.
 */
package com.belk.car.app.model.oma;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;
/**
 * Bean class for States.
 * @author afusy13
 *
 */
@Entity
@Table(name = "STATE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class State extends BaseAuditableModel implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private Long stateId ;
	private String stateCd ;
	private String stateName ;
	
	@Id
	@Column(name="STATE_ID", precision=12, scale=0 )
	public Long getStateId() {
		return stateId;
	}
	public void setStateId(Long stateID) {
		this.stateId = stateID;
	}
	
	@Column(name="STATE_ABBR", nullable=true, length=2)
	public String getStateCd() {
		return stateCd;
	}
	public void setStateCd(String stateCd) {
		this.stateCd = stateCd;
	}
	
	@Column(name="STATE_NM", nullable=false, length=50)
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
}
