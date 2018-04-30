package com.belk.car.app.model.oma;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;



@Entity
@Table(name = "CONTACT_TYPE",uniqueConstraints = @UniqueConstraint(columnNames = "CONTACT_TYPE_CD"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class ContactType extends BaseAuditableModel implements java.io.Serializable {
	
	private static final long serialVersionUID = 2667713137037878558L;
	
	private String contactTypeId;
	private String contactTypeName;
	private String contactTypeDesc;
	private String statusCd;
	
	@Id
	@Column(name = "CONTACT_TYPE_CD", unique = true, nullable = false, precision = 12, scale = 0)
	public String getContactTypeId() {
		return contactTypeId;
	}

	public void setContactTypeId(String contactTypeId) {
		this.contactTypeId = contactTypeId;
	}

	@Column(name = "NAME", nullable = false, length = 50)
	public String getContactTypeName() {
		return contactTypeName;
	}

	public void setContactTypeName(String contactTypeName) {
		this.contactTypeName = contactTypeName;
	}
	
	@Column(name = "DESCR", nullable = false, length = 200)
	public String getContactTypeDesc() {
		return contactTypeDesc;
	}

	public void setContactTypeDesc(String contactTypeDesc) {
		this.contactTypeDesc = contactTypeDesc;
	}

	@Column(name = "STATUS_CD", nullable = false, length = 20 )
	public String getStatusCd() {
		return statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	@Transient
	public String toString(){
		StringBuilder model=new StringBuilder("contactTypeId="+this.getContactTypeId());
		
		model.append("\ncontactTypeName="+this.getContactTypeName());
		
		model.append("\ncontactTypeDesc="+this.getContactTypeDesc());
		
		return model.toString();
		
	}
}
