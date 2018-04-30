package com.belk.car.app.model.oma;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.belk.car.app.model.BaseAuditableModel;

@Entity
@Table(name = "fulfmnt_method",uniqueConstraints = @UniqueConstraint(columnNames = "fulfmnt_method_cd"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class FulfillmentMethod  extends BaseAuditableModel implements java.io.Serializable {

	private static final long serialVersionUID = 2667713137037878558L;
	private String fulfillmentMethodID;
	private String fulfillmentMethodDesc;
	private String fulfillmentMethodStatusCd;
	private String fulfillmentMethodName;
	@Id 
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FULFILLMENT_SERV_SEQ_GEN")
	
	@Column(name = "fulfmnt_method_cd", unique = true, nullable = false, precision = 12, scale = 0)
	public String getFulfillmentMethodID() {
		return fulfillmentMethodID;
	}
	public void setFulfillmentMethodID(String fulfillmentMethodID) {
		this.fulfillmentMethodID = fulfillmentMethodID;
	}
	
	@Column(name = "descr",nullable = false)
	public String getFulfillmentMethodDesc() {
		return fulfillmentMethodDesc;
	}
	public void setFulfillmentMethodDesc(String fulfillmentMethodDesc) {
		this.fulfillmentMethodDesc = fulfillmentMethodDesc;
	}
	
	@Column(name = "NAME",nullable = false)
	public String getFulfillmentMethodName() {
		return fulfillmentMethodName;
	}
	public void setFulfillmentMethodName(String fulfillmentMethodName) {
		this.fulfillmentMethodName = fulfillmentMethodName;
	}
	
	@Column(name = "status_cd",nullable = false)
	public String getFulfillmentMethodStatusCd() {
		return fulfillmentMethodStatusCd;
	}
	public void setFulfillmentMethodStatusCd(String fulfillmentMethodStatusCd) {
		this.fulfillmentMethodStatusCd = fulfillmentMethodStatusCd;
	}
}
