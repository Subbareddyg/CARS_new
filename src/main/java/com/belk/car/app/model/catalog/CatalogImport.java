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
@Table(name = "CATALOG_IMPORT", uniqueConstraints = @UniqueConstraint(columnNames = "CATALOG_IMPORT_ID"))
public class CatalogImport extends BaseAuditableModel implements
		java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3920978979736799098L;

	private long importId;
	private CatalogTemplate template;
	private String importFileName;
	private String vendorNumber ;
	private long recordsProcessed ;

	public CatalogImport() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATALOG_IMPORT_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CATALOG_IMPORT_SEQ_GEN", sequenceName = "CATALOG_IMPORT_SEQ", allocationSize = 1)
	@Column(name = "CATALOG_IMPORT_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getImportId() {
		return this.importId;
	}

	public void setImportId(long importId) {
		this.importId = importId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEMPLATE_ID", nullable = false)
	public CatalogTemplate getTemplate() {
		return template;
	}

	public void setTemplate(CatalogTemplate template) {
		this.template = template;
	}

	@Column(name = "IMPORT_FILENAME", nullable = false, length = 255)
	public String getImportFileName() {
		return importFileName;
	}

	public void setImportFileName(String importFileName) {
		this.importFileName = importFileName;
	}

	@Column(name = "VENDOR_NUMBER", nullable = false, length = 50)
	public String getVendorNumber() {
		return vendorNumber;
	}

	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}

	@Column(name = "RECORDS_PROCESSED", nullable = false, precision = 12, scale = 0)
	public long getRecordsProcessed() {
		return recordsProcessed;
	}

	public void setRecordsProcessed(long recordsProcessed) {
		this.recordsProcessed = recordsProcessed;
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
