package com.belk.car.app.model.vendorcatalog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.Vendor;

/**
 * This is the model class for table name vndr_catl_imp
 * 
 * @version 1.0 10 December 2009
 * @author afusya2
 */
@Entity
@Table(name = "VENDOR_CATALOG_IMPORT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class VendorCatalogImport  implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2758789441389554262L;
	private long vendorCatalogImpID;
	private String vendorCatalogFileName;
	private VendorCatalogFileFormat fileFormatID;
	private VendorCatalogImageLocation imageLocationID;
	private String dataFileDelimeter;
	private Date importedDate;
	private String ftpUrl;
	private String ftpUname;
	private String ftpPassword;
	private VendorCatalogUpdateAction updateActionID;
	private VendorCatalog vendorCatalogID;
	private List<Vendor> vendor = new ArrayList<Vendor>(0);
	private Long prevCatalogId;
	private String dataFileSourceFeedCode;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATL_IMP_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CATL_IMP_SEQ_GEN", sequenceName = "SEQ_VENDOR_CATALOG_IMPORT_ID", allocationSize = 1)
	@Column(name = "VENDOR_CATALOG_IMPORT_ID", unique = false, length = 12)
	public long getVendorCatalogImpID() {
		return vendorCatalogImpID;
	}

	public void setVendorCatalogImpID(long vendorCatalogImpID) {
		this.vendorCatalogImpID = vendorCatalogImpID;
	}

	@Column(name = "DATA_FILE_NAME", nullable = false, length = 250)
	public String getVendorCatalogFileName() {
		return vendorCatalogFileName;
	}

	public void setVendorCatalogFileName(String vendorCatalogFileName) {
		this.vendorCatalogFileName = vendorCatalogFileName;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DATA_FILE_FORMAT_CD", nullable = false )
	public VendorCatalogFileFormat getFileFormatID() {
		return fileFormatID;
	}

	public void setFileFormatID(VendorCatalogFileFormat fileFormatID) {
		this.fileFormatID = fileFormatID;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "IMAGE_FILE_SOURCE_CD", nullable = true)
	// temp changed as true
	public VendorCatalogImageLocation getImageLocationID() {
		return imageLocationID;
	}

	public void setImageLocationID(VendorCatalogImageLocation imageLocationID) {
		this.imageLocationID = imageLocationID;
	}

	

	@Column(name = "IMAGE_FILE_FTP_URL", nullable = true, unique = false, length = 250)
	// temp changed as true
	public String getFtpUrl() {
		return ftpUrl;
	}

	public void setFtpUrl(String ftpUrl) {
		this.ftpUrl = ftpUrl;
	}

	@Column(name = "IMAGE_FILE_FTP_USER_NAME", unique = false, length = 50)
	public String getFtpUname() {
		return ftpUname;
	}

	public void setFtpUname(String ftpUname) {
		this.ftpUname = ftpUname;
	}

	@Column(name = "IMAGE_FILE_FTP_PASSWORD", unique = false, length = 50)
	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VNDR_CATL_UPDATE_ACTION_CD", nullable = true)
	public VendorCatalogUpdateAction getUpdateActionID() {
		return updateActionID;
	}

	public void setUpdateActionID(VendorCatalogUpdateAction updateActionID) {
		this.updateActionID = updateActionID;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "vendor_catalog_id", nullable = false)
	public VendorCatalog getVendorCatalogID() {
		return vendorCatalogID;
	}

	public void setVendorCatalogID(VendorCatalog vendorCatalogID) {
		this.vendorCatalogID = vendorCatalogID;
	}

	/**
	 * @return the dataFileDelimeter
	 */
	@Column(name = "DATA_FILE_COL_DELIMITER_CD", nullable = true, unique = false, length = 20)
	// temp changed as true
	public String getDataFileDelimeter() {
		return dataFileDelimeter;
	}

	/**
	 * @param dataFileDelimeter
	 *            the dataFileDelimeter to set
	 */
	public void setDataFileDelimeter(String dataFileDelimeter) {
		this.dataFileDelimeter = dataFileDelimeter;
	}

	@Column(name = "PREVIOUS_RQST_ID", nullable = true, unique = false, length = 12)
	public Long getPrevCatalogId() {
		return prevCatalogId;
	}

	public void setPrevCatalogId(Long prevCatalogId) {
		this.prevCatalogId = prevCatalogId;
	}

	
	public void setImportedDate(Date importedDate) {
		this.importedDate = importedDate;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "IMPORTED_DATE", nullable = true)
	public Date getImportedDate() {
		return importedDate;
	}

	
	public void setDataFileSourceFeedCode(String dataFileSourceFeedCode) {
		this.dataFileSourceFeedCode = dataFileSourceFeedCode;
	}
	
	@Column(name = "DATA_FILE_SOURCE_FEED_CD", nullable = true, unique = false, length = 20)
	public String getDataFileSourceFeedCode() {
		return dataFileSourceFeedCode;
	}
}
