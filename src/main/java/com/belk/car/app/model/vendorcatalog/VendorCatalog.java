package com.belk.car.app.model.vendorcatalog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;
import com.belk.car.app.model.Vendor;

/**
 * This is the model class for table name vndr_catl
 * 
 * @version 1.0 10 December 2009
 * @author afusya2
 */
@Entity
@Table(name = "vendor_catalog")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class VendorCatalog extends BaseAuditableModel implements java.io.Serializable {

	private static final long serialVersionUID = 2667713137037878558L;
	private Long vendorCatalogID;
	private String vendorCatalogName;
	private Vendor vendor;
	private String statusCD;
	private String source;
	private String lockedBy;
	private VendorCatalogTemplate vendorCatalogTemplate;
	private VendorCatalogImport vendorCatalogImport;
	private List<VendorCatalogHeader> vendorCatalogHeaderList=new ArrayList<VendorCatalogHeader>(0); 
	private Set<VendorCatalogImport> catalogImport;

	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy="vendorCatalogID")
	public Set<VendorCatalogImport> getCatalogImport() {
		return catalogImport;
	}

	
	public void setCatalogImport(Set<VendorCatalogImport> catalogImport) {
		this.catalogImport = catalogImport;
	}

	@Id
	@Column(name = "vendor_catalog_id", nullable = false, length = 12)
	public Long getVendorCatalogID() {
		return vendorCatalogID;
	}

	public void setVendorCatalogID(long vendorCatalogID) {
		this.vendorCatalogID = vendorCatalogID;
	}

	@Column(name = "vendor_catalog_name", nullable = false, unique= true ,length = 100)
	public String getVendorCatalogName() {
		return vendorCatalogName;
	}

	public void setVendorCatalogName(String vendorCatalogName) {
		this.vendorCatalogName = vendorCatalogName;
	}

	/**
	 * @return the vendor
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "vendor_id", nullable = false, unique=true)
	public Vendor getVendor() {
		return vendor;
	}

	/**
	 * @param vendor
	 *            the vendor to set
	 */
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	@Column(name = "status_cd", unique = false, length = 20)
	public String getStatusCD() {
		return statusCD;
	}

	public void setStatusCD(String statusCD) {
		this.statusCD = statusCD;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_catalog_tmpl_id", nullable = true)
	// temp changed as null
	public VendorCatalogTemplate getVendorCatalogTemplate() {
		return vendorCatalogTemplate;
	}

	public void setVendorCatalogTemplate(VendorCatalogTemplate vendorCatalogTemplate) {
		this.vendorCatalogTemplate = vendorCatalogTemplate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_date", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	@Column(name = "UPDATED_BY", nullable = false, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}

	@Column(name = "source", nullable = false, length = 10)
	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Column(name = "locked_by", nullable = true, length = 25)
	public String getLockedBy() {
		return lockedBy;
	}

	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}

	public void setVendorCatalogImport(VendorCatalogImport vendorCatalogImport) {
		this.vendorCatalogImport = vendorCatalogImport;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy="vendorCatalogID")
	public VendorCatalogImport getVendorCatalogImport() {
		return vendorCatalogImport;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy="vendorCatalogID")
	public List<VendorCatalogHeader> getVendorCatalogHeaderList() {
		return vendorCatalogHeaderList;
	}

	public void setVendorCatalogHeaderList(List<VendorCatalogHeader> vendorCatalogHeaderList) {
		this.vendorCatalogHeaderList = vendorCatalogHeaderList;
	}

	
}
