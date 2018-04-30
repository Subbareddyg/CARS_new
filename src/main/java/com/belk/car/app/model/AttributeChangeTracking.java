package com.belk.car.app.model;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * @author Prabhakaran Karuppiah
 *
 */


@Entity
@Table(name = "ATTRIBUTE_CHANGE_TRACKING")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class AttributeChangeTracking extends BaseAuditableModel implements java.io.Serializable {

	private static final long serialVersionUID = -3982928293839292979L;
	
	private long actId;
	private long attrId;
	private String typeName = "";
	private String typeAction = "";
	private String oldValue = "";
	private String newValue="" ;
	private String resynchCars="" ;
	private String sendCmpBm="" ;
	private String processed="" ;
	
	
	

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATTRIBUTE_TRACKING_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "ATTRIBUTE_TRACKING_SEQ_GEN", sequenceName = "ATTRIBUTE_TRACKING_SEQ", allocationSize = 1)
	@Column(name = "ACT_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getActId() {
		return actId;
	}
	
	public void setActId(long actId) {
		this.actId= actId;
	}
	
	
	@Column(name = "ACT_ATTR_ID", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public long getAttrId() {
		return attrId;
	}

	public void setAttrId(long attrId) {
		this.attrId = attrId;
	}

	
	@Column(name = "ACT_TYPE", nullable = false, length = 50)
	public String getTypeName() {
		return typeName;
	}


	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Column(name = "ACT_ACTION", nullable = false, length = 50)
	public String getTypeAction() {
		return typeAction;
	}

	public void setTypeAction(String typeAction) {
		this.typeAction = typeAction;
	}

	@Column(name = "ACT_OLD_VALUE", nullable = true, length = 100)
	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	@Column(name = "ACT_NEW_VALUE", nullable = true, length = 100)
	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	@Column(name = "ACT_RESYNCH_CARS", nullable = true, length = 1)
	public String getResynchCars() {
		return resynchCars;
	}

	public void setResynchCars(String resynchCars) {
		this.resynchCars = resynchCars;
	}

	@Column(name = "ACT_SEND_CMP_BM", nullable = true, length = 1)
	public String getSendCmpBm() {
		return sendCmpBm;
	}

	public void setSendCmpBm(String sendCmpBm) {
		this.sendCmpBm = sendCmpBm;
	}

	@Column(name = "ACT_PROCESSED", nullable = true, length = 1)
	public String getProcessed() {
		return processed;
	}

	public void setProcessed(String processed) {
		this.processed = processed;
	}

	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ACT_CREATED_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ACT_UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	public String toString(){
    	return new StringBuffer().append("ACT_ATTR_ID :").append(this.attrId).append("ACT_TPYE :").append(this.typeName).append("ACT_ACTION :").append(this.typeAction).append("ACT_OLD_VALU :").append(this.oldValue)
			.append("newValue :").append(this.newValue).append("resynchCars :").append(this.resynchCars).append("sendCmpBm :").append(this.sendCmpBm).append("processed :").append(this.processed).toString();
    }
	
}
