package com.belk.car.app.model.vendorcatalog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.belk.car.app.model.BaseAuditableModel;

/**
 * This is the model class for table name file_format
 * @version 1.0 10 December 2009
 * @author afusya2
 */
@Entity
@Table(name = "file_format")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class VendorCatalogFileFormat extends BaseAuditableModel implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6934250484232380205L;
	private String fileFormatID;
	private String statusCD;	
	private String fileFormatName;
	private String descr;
	
	   
	/**
	 * @return the fileFormatID
	 */
	@Id
	@Column(name = "file_format_cd", unique = false, length = 12)	
	public String getFileFormatID() {
		return fileFormatID;
	}
	/**
	 * @param fileFormatID the fileFormatID to set
	 */
	public void setFileFormatID(String fileFormatID) {
		this.fileFormatID = fileFormatID;
	}

	/**
	 * @return the fileFormatName
	 */
	@Column(name = "NAME", unique = false, length = 50)
	public String getFileFormatName() {
		return fileFormatName;
	}
	/**
	 * @param fileFormatName the fileFormatName to set
	 */
	public void setFileFormatName(String fileFormatName) {
		this.fileFormatName = fileFormatName;
	}

	/**
	 * @return the descr
	 */
	@Column(name = "descr", unique = false, length = 200)
	public String getDescr() {
		return descr;
	}
	/**
	 * @param descr the descr to set
	 */
	public void setDescr(String descr) {
		this.descr = descr;
	}
	
	/**
	 * 
	 * @return the status
	 */
	@Column(name = "status_cd", unique = false, length = 20)
	public String getStatusCD() {
		return statusCD;
	}
	/**
	 * @param statusCD the statusCD to set
	 */
	public void setStatusCD(String statusCD) {
		this.statusCD = statusCD;
	}
}
