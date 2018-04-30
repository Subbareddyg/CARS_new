package com.belk.car.app.model.report;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.belk.car.app.model.BaseAuditableModel;

@Entity
@Table(name = "LATE_CARS_REPORT")
public class LateCarsReport extends BaseAuditableModel implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4803559526216800853L;

	private long carId;
	private Long deptId;
	private String userName;
	private Date createdDate;
	private Date generatedDate;
	private Integer skuCount;
	private String vendorName;
	private String carStatus;

	@Id	
	@Column(name = "CAR_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getCarId() {
		return this.carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

	@Column(name = "DEPT_ID")
	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	@Column(name = "USER_NAME")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "GENERATED_DATE")
	public Date getGeneratedDate() {
		return generatedDate;
	}

	public void setGeneratedDate(Date generatedDate) {
		this.generatedDate = generatedDate;
	}

	@Column(name = "SKU_COUNT")
	public Integer getSkuCount() {
		return skuCount;
	}

	public void setSkuCount(Integer skuCount) {
		this.skuCount = skuCount;
	}

	@Column(name = "VENDOR_NAME")
	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	
	@Column(name = "CAR_STATUS")
	public String getCarStatus() {
		return carStatus;
	}

	public void setCarStatus(String carStatus) {
		this.carStatus = carStatus;
	}


}
