package com.belk.car.app.model.oma;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

@Entity
@Table(name = "fulfmnt_service",uniqueConstraints = @UniqueConstraint(columnNames = "fulfmnt_service_id"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class FulfillmentService  extends BaseAuditableModel implements java.io.Serializable {

	private static final long serialVersionUID = 2667713137037878558L;
	private long fulfillmentServiceID;
	private String fulfillmentServiceName;
	private String fulfillmentServiceDesc;
	private String fulfillmentServiceStatusCode;
	private FulfillmentMethod fulfillmentMethodID;
	private FulfillmentIntegrationType fulfillmentServiceIntTypeID;
	private Long defaultReturnAddID;
	private String strUpdatedDate;
	private String strCreatedDate;

	
	
	/*Fulfillment Service Constructor*/
	public FulfillmentService() {
		super();
		fulfillmentMethodID=new FulfillmentMethod();
		fulfillmentServiceIntTypeID=new FulfillmentIntegrationType();
		}

	
	@Id 
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FULFILLMENT_SERV_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "FULFILLMENT_SERV_SEQ_GEN", sequenceName = " SEQ_FULFMNT_SERVICE_ID", allocationSize = 1)
	@Column(name = "fulfmnt_service_id", unique = true, nullable = false, precision = 12, scale = 0)
	public long getFulfillmentServiceID() {
		return fulfillmentServiceID;
	}

	public void setFulfillmentServiceID(long fulfillmentServiceID) {
		this.fulfillmentServiceID = fulfillmentServiceID;
	}
	
	
	@Column(name = "fulfmnt_service_name", nullable = false, length = 50,unique = true)
	public String getFulfillmentServiceName() {	
		return fulfillmentServiceName;
	}

	public void setFulfillmentServiceName(String fulfillmentServiceName) {
		this.fulfillmentServiceName = fulfillmentServiceName;
	}
	
	@Column(name = "fulfmnt_service_descr", length = 200)
	public String getFulfillmentServiceDesc() {
		return fulfillmentServiceDesc;
	}

	public void setFulfillmentServiceDesc(String fulfillmentServiceDesc) {
		this.fulfillmentServiceDesc = fulfillmentServiceDesc;
	}
	
	@ManyToOne
	@JoinColumn(name="fulfmnt_method_cd", nullable = false)
	public FulfillmentMethod getFulfillmentMethodID() {
		return fulfillmentMethodID;
	}

	public void setFulfillmentMethodID(FulfillmentMethod fulfillmentMethodID) {
		this.fulfillmentMethodID = fulfillmentMethodID;
	}
	
	@ManyToOne
	@JoinColumn(name="fulfmnt_intg_type_cd", nullable = false)
	public FulfillmentIntegrationType getFulfillmentServiceIntTypeID() {
		return fulfillmentServiceIntTypeID;
	}

	public void setFulfillmentServiceIntTypeID(FulfillmentIntegrationType fulfillmentServiceIntTypeID) {
		this.fulfillmentServiceIntTypeID = fulfillmentServiceIntTypeID;
	}
	
	
	@Column(name="dflt_fulfmnt_rtn_loc_id",nullable=true)
	public Long getDefaultReturnAddID() {
		return defaultReturnAddID;
	}

	public void setDefaultReturnAddID(Long defaultReturnAddID) {
		this.defaultReturnAddID = defaultReturnAddID;
	}

	
	@Column(name = "status_cd", nullable = false, length=20)
	public String getFulfillmentServiceStatusCode() {
		return fulfillmentServiceStatusCode;
	}

	public void setFulfillmentServiceStatusCode(String fulfillmentServiceStatusCode) {
		this.fulfillmentServiceStatusCode = fulfillmentServiceStatusCode;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name="created_date",nullable=false)
	public Date getCreatedDate() {
		return this.createdDate;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name="updated_date",nullable=false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	@Column(name="created_by",nullable=false)
	public String getCreatedBy() {
		return this.createdBy;
	}

	@Column(name="updated_by",nullable=false)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	@Transient
	public String getStrCreatedDate() {
		return strCreatedDate;
	}

	public void setStrCreatedDate(String strCreatedDate) {
		this.strCreatedDate = strCreatedDate;
	}

	@Transient
	public String getStrUpdatedDate() {
		return strUpdatedDate;
	}

	public void setStrUpdatedDate(String strUpdatedDate) {
		this.strUpdatedDate = strUpdatedDate;
	}
}
