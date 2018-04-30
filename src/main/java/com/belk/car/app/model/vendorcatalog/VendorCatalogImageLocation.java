package com.belk.car.app.model.vendorcatalog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

/**
 * This is the model class for table name IMAGE_SOURCE
 * @version 1.0 10 December 2009
 * @author afusya2
 */
@Entity
@Table(name = "IMAGE_SOURCE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class VendorCatalogImageLocation extends BaseAuditableModel implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8934060185413175046L;
	private String imageLocationID;
	private String statusCD;	
	private String imageLocationDesc;
	public String imageSourceName;
	

	@Id
	@Column(name = "image_source_cd", unique = false, length = 20)
	public String getImageLocationID() {
		return imageLocationID;
	}

	public void setImageLocationID(String imageLocationID) {
		this.imageLocationID = imageLocationID;
	}
	

	@Column(name = "DESCR", unique = false, length = 200)
	public String getImageLocationDesc() {
		return imageLocationDesc;
	}

	public void setImageLocationDesc(String imageLocationDesc) {
		this.imageLocationDesc = imageLocationDesc;
	}

	/**
	 * @return the imageSourceName
	 */
	@Column(name = "NAME", unique = false, length = 50)
	public String getImageSourceName() {
		return imageSourceName;
	}

	/**
	 * @param imageSourceName the imageSourceName to set
	 */
	public void setImageSourceName(String imageSourceName) {
		this.imageSourceName = imageSourceName;
	}

	/**
	 * 
	 * @return the status
	 */
	@Column(name = "STATUS_CD", unique = false, length = 20)
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
