package com.belk.car.app.model.catalog;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.belk.car.app.model.BaseAuditableModel;

@Entity
@Table(name = "CATALOG_IMPORT_AUDIT", uniqueConstraints = @UniqueConstraint(columnNames = "CATALOG_IMPORT_AUDIT_ID"))
public class CatalogImportAudit extends BaseAuditableModel implements
		java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2239556121025203156L;

	private long catalogImportAuditId;
	private CatalogImport catalogImport;
	private String vendorSku;

	public CatalogImportAudit() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATALOG_IMPORT_AUDIT_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CATALOG_IMPORT_AUDIT_SEQ_GEN", sequenceName = "CATALOG_IMPORT_AUDIT_SEQ", allocationSize = 1)
	@Column(name = "CATALOG_IMPORT_AUDIT_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getCatalogImportAuditId() {
		return this.catalogImportAuditId;
	}

	public void setCatalogImportAuditId(long catalogImportAuditId) {
		this.catalogImportAuditId = catalogImportAuditId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATALOG_IMPORT_ID", nullable = false)
	public CatalogImport getCatalogImport() {
		return catalogImport;
	}

	public void setCatalogImport(CatalogImport catalogImport) {
		this.catalogImport = catalogImport;
	}

	@Column(name = "VENDOR_SKU", nullable = false, length = 50)
	public String getVendorSku() {
		return vendorSku;
	}

	public void setVendorSku(String vendorSku) {
		this.vendorSku = vendorSku;
	}

	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}

	@Column(name = "UPDATED_BY", nullable = false, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}
}
