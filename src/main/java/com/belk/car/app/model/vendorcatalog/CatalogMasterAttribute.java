package com.belk.car.app.model.vendorcatalog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

@Entity
@Table(name = "CATALOG_MASTER_ATTRIBUTE", uniqueConstraints = @UniqueConstraint(columnNames = {
		"CATALOG_MASTER_ATTR_ID", "CATALOG_MASTER_ATTR_NAME" }))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class CatalogMasterAttribute extends BaseAuditableModel
implements
java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -887291329276442668L;
	private Long catalogMasterAttrId;
	private String  catalogMasterAttrName;
	private String  catalogMasterAttrDescr;
	private String status;
	
	@Id
	@Column(name = "CATALOG_MASTER_ATTR_ID", precision = 12, scale = 0)
	public Long getCatalogMasterAttrId() {
		return catalogMasterAttrId;
	}
	public void setCatalogMasterAttrId(Long catalogMasterAttrId) {
		this.catalogMasterAttrId = catalogMasterAttrId;
	}
	
	@Column(name="CATALOG_MASTER_ATTR_NAME",nullable=false,length=100)
	public String getCatalogMasterAttrName() {
		return catalogMasterAttrName;
	}
	public void setCatalogMasterAttrName(String catalogMasterAttrName) {
		this.catalogMasterAttrName = catalogMasterAttrName;
	}
	
	@Column(name="CATALOG_MASTER_ATTR_DESCR",nullable=false,length=100)
	public String getCatalogMasterAttrDescr() {
		return catalogMasterAttrDescr;
	}
	public void setCatalogMasterAttrDescr(String catalogMasterAttrDescr) {
		this.catalogMasterAttrDescr = catalogMasterAttrDescr;
	}
	
	@Column(name="STATUS_CD",nullable=false,length=20)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	

}
