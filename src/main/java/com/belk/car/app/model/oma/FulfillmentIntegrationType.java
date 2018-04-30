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
@Table(name = "fulfmnt_intg_type",uniqueConstraints = @UniqueConstraint(columnNames = "fulfmnt_intg_type_cd"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class FulfillmentIntegrationType  extends BaseAuditableModel implements java.io.Serializable {

	private static final long serialVersionUID = 2667713137037878558L;
	
	private String integrationTypeID;
	private String integrationTypeDesc;
	private String integrationTypeName;
	private String integrationTypeStatusCd;
	
	@Id
	@Column(name = "fulfmnt_intg_type_cd", unique = true, nullable = false, precision = 12, scale = 0)
	public String getIntegrationTypeID() {
		return integrationTypeID;
	}
	public void setIntegrationTypeID(String integrationTypeID) {
		this.integrationTypeID = integrationTypeID;
	}
	
	@Column(name="descr",nullable = false)
	public String getIntegrationTypeDesc() {
		return integrationTypeDesc;
	}
	public void setIntegrationTypeDesc(String integrationTypeDesc) {
		this.integrationTypeDesc = integrationTypeDesc;
	}
	
	@Column(name="NAME",unique = true, nullable = false)
	public String getIntegrationTypeName() {
		return integrationTypeName;
	}
	public void setIntegrationTypeName(String integrationTypeName) {
		this.integrationTypeName = integrationTypeName;
	}
	
	@Column(name="status_cd",nullable = false)
	public String getIntegrationTypeStatusCd() {
		return integrationTypeStatusCd;
	}
	public void setIntegrationTypeStatusCd(String integrationTypeStatusCd) {
		this.integrationTypeStatusCd = integrationTypeStatusCd;
	}
	
}
