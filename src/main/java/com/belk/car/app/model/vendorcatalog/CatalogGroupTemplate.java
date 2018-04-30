package com.belk.car.app.model.vendorcatalog;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

@Entity
@Table(name = "CATALOG_GROUP_TEMPLATE", uniqueConstraints = @UniqueConstraint(columnNames = {
		"PRODUCT_GROUP_ID", "VENDOR_CATALOG_TMPL_ID" }))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class CatalogGroupTemplate extends BaseAuditableModel implements java.io.Serializable {

	private static final long serialVersionUID = -8828358191924173605L;
	private String statusCD;
	private CompositeKeyForCatalogGroupTemplate compositeKeyForCatalogGroupID;

	public CatalogGroupTemplate() {
	}
	
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "vendorCatalogTmplID", column = @Column(name = "VENDOR_CATALOG_TMPL_ID", nullable = false, precision = 12, scale = 0)),
			@AttributeOverride(name = "productGroupID", column = @Column(name = "PRODUCT_GROUP_ID", nullable = false, precision = 12, scale = 0)) })
	public CompositeKeyForCatalogGroupTemplate getCompositeKeyForCatalogGroupID() {
		return compositeKeyForCatalogGroupID;
	}

	public void setCompositeKeyForCatalogGroupID(
			CompositeKeyForCatalogGroupTemplate compositeKeyForCatalogGroupID) {
		this.compositeKeyForCatalogGroupID = compositeKeyForCatalogGroupID;
	}
	
		
	@Column(name = "STATUS_CD", nullable = false, length = 20)
	public String getStatusCD() {
		return statusCD;
	}

	public void setStatusCD(String statusCD) {
		this.statusCD = statusCD;
	}
}