package com.belk.car.app.model.vendorcatalog;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;

import com.belk.car.app.model.BaseAuditableModel;

@Entity
@Table(name = "VENDOR_CATALOG_TEMPLATE", uniqueConstraints = @UniqueConstraint(columnNames = "VENDOR_CATALOG_TMPL_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class VendorCatalogTemplate extends BaseAuditableModel implements java.io.Serializable {

	private static final long serialVersionUID = 782010222538974795L;
	private long vendorCatalogTmplID;
	private String vendorCatalogName;
	private boolean shared;
	private boolean global;
	private String isLocked;
	private List<CatalogGroupTemplate> catalogGroupTemplate=new ArrayList<CatalogGroupTemplate>(0);

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATL_TMPL_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CATL_TMPL_SEQ_GEN", sequenceName = "SEQ_VENDOR_CATALOG_TMPL_ID", allocationSize = 1)
	@Column(name = "VENDOR_CATALOG_TMPL_ID", unique = false, length = 12)
	public long getVendorCatalogTmplID() {
		return this.vendorCatalogTmplID;
	}

	public void setVendorCatalogTmplID(long vendorCatalogTmplID) {
		this.vendorCatalogTmplID = vendorCatalogTmplID;
	}

	@Column(name = "VENDOR_CATALOG_TMPL_NAME", unique = false, length = 50)
	public String getVendorCatalogName() {
		return this.vendorCatalogName;
	}

	public void setVendorCatalogName(String vendorCatalogName) {
		this.vendorCatalogName = vendorCatalogName;
	}

	@Column(name = "IS_SHARED", unique = false, length = 1)
	@Type(type="yes_no")
	public boolean getShared() {
		return this.shared;
	}

	public void setShared(boolean shared) {
		this.shared = shared;
	}

	@Column(name = "IS_GLOBAL", unique = false, length = 1)
	@Type(type="yes_no")
	public boolean getGlobal() {
		return this.global;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	@Column(name = "LOCKED_BY", unique = false, length = 1)
	public String getIsLocked() {
		return this.isLocked;
	}

	public void setIsLocked(String isLocked) {
		this.isLocked = isLocked;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "compositeKeyForCatalogGroupID.vendorCatalogTmplID")
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public List<CatalogGroupTemplate> getCatalogGroupTemplate() {
		return catalogGroupTemplate;
	}

	public void setCatalogGroupTemplate(List<CatalogGroupTemplate> catalogGroupTemplate) {
		this.catalogGroupTemplate = catalogGroupTemplate;
	}

}
