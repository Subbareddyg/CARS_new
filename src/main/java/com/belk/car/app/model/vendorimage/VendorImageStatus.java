/**
 * 
 */
package com.belk.car.app.model.vendorimage;

import java.util.Date;

import com.belk.car.app.model.BaseAuditableModel;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


/**
 * @author afuszm1
 *
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, include="all")
@Table(name = "VENDOR_IMAGE_STATUS", uniqueConstraints = @UniqueConstraint(columnNames = "VENDOR_IMAGE_STATUS_CD"))
public class VendorImageStatus  extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5096216252035320181L;

	public static final String UPLOADED ="UPLOADED";
	public static final String TRANSIT ="TRANSIT";
	public static final String SENT_TO_MQ ="SENT_TO_MQ";
	public static final String	IMAGE_UPLOAD_FAILED ="IMAGE_UPLOAD_FAILED";
	public static final String MQ_PASSED ="MQ_PASSED";
	public static final String	MQ_FAILED ="MQ_FAILED";
	public static final String	REUPLOADED ="REUPLOADED";
	public static final String	CREATIVE_FAILED ="CREATIVE_FAILED";
	public static final String	CREATIVE_PASS ="CREATIVE_PASSED";
	public static final String RECEIVED ="RECEIVED";
	public static final String DELETED ="DELETED";
	public static final String RETRANSIT ="RETRANSIT";

	
	private String vendorImageStatusCd;
	private String name;
	private String descr;
	
	public VendorImageStatus() {
	}
	
	@Id
	@Column(name = "VENDOR_IMAGE_STATUS_CD", unique = true, nullable = false, length = 20)
	public String getVendorImageStatusCd() {
		return this.vendorImageStatusCd;
	}

	public void setVendorImageStatusCd(String vendorImageStatusCd) {
		this.vendorImageStatusCd = vendorImageStatusCd;
	}
	@Column(name = "NAME", unique=true, nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCR", nullable = false, length = 200)
	public String getDescription() {
		return this.descr;
	}

	public void setDescription(String descr) {
		this.descr = descr;
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
	@Column(name = "CREATE_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}
}
