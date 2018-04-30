package com.belk.car.app.model.car;

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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

@Entity
@Table(name = "MANUAL_CAR")
public class ManualCar extends BaseAuditableModel implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6285825293644128743L;

	public static final String ACTIVE = "ACTIVE";
	
	private long manualCarId ;
	private String vendorNumber;
	private String vendorStyleNumber;
	private String colorDescription;
	private String sizeDescription;
	private String statusCd ;
	private ManualCarProcessStatus processStatus;
	private String postProcessInfo;
	private String isPack="N";

	
	public ManualCar() {
	}


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MANUAL_CAR_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "MANUAL_CAR_SEQ_GEN", sequenceName = "MANUAL_CAR_SEQ", allocationSize = 1)
	@Column(name = "MANUAL_CAR_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getManualCarId() {
		return manualCarId;
	}


	public void setManualCarId(long manualCarId) {
		this.manualCarId = manualCarId;
	}


	@Column(name = "VENDOR_NUMBER", nullable = false, length = 50)
	public String getVendorNumber() {
		return vendorNumber;
	}


	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}


	@Column(name = "VENDOR_STYLE_NUMBER", nullable = false, length = 50)
	public String getVendorStyleNumber() {
		return vendorStyleNumber;
	}


	public void setVendorStyleNumber(String vendorStyleNumber) {
		this.vendorStyleNumber = vendorStyleNumber;
	}


	@Column(name = "COLOR_DESCR", nullable = true, length = 200)
	public String getColorDescription() {
		return colorDescription;
	}


	public void setColorDescription(String colorDescription) {
		this.colorDescription = colorDescription;
	}


	@Column(name = "SIZE_DESCR", nullable = true, length = 200)
	public String getSizeDescription() {
		return sizeDescription;
	}


	public void setSizeDescription(String sizeDescription) {
		this.sizeDescription = sizeDescription;
	}


	@Column(name = "STATUS_CD", nullable = false, length = 20)
	public String getStatusCd() {
		return statusCd;
	}


	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROCESS_STATUS_CD", nullable=false)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public ManualCarProcessStatus getProcessStatus() {
		return processStatus;
	}


	public void setProcessStatus(ManualCarProcessStatus processStatus) {
		this.processStatus = processStatus;
	}
		

	@Column(name = "POST_PROCESS_INFO", nullable = true, length = 100)
	public String getPostProcessInfo() {
		return postProcessInfo;
	}


	public void setPostProcessInfo(String postProcessInfo) {
		this.postProcessInfo = postProcessInfo;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
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


	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}

	@Column(name = "IS_PACK", nullable = false, length = 1 )
	public String getIsPack() {
		return isPack;
	}


	public void setIsPack(String isPack) {
		this.isPack = isPack;
	}



}
