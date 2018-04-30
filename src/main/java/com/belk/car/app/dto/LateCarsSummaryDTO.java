package com.belk.car.app.dto;

import java.util.Date;


/*
 * DTO to store reporting for late cars
 */
public class LateCarsSummaryDTO implements java.io.Serializable {
	
	private static final long serialVersionUID = 509429990081639843L;
	
	private long carId;
	private String deptCd;
	private Date createdDate;
	private Date completionDate;
	private Date dueDate;
	private long userId;
	
	private String firstName;
	private String lastName;
	private String email;
	private String userTypeCd;
	private String statusCd;
	private String vendorName;
	private int skuCount;
	private String dmmEmail;
	private String dmmName;
	private String gmmEmail;
	private String gmmName;
	private String vendorNumber;
	private String brandName;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUserTypeCd() {
		return userTypeCd;
	}
	public void setUserTypeCd(String userTypeCd) {
		this.userTypeCd = userTypeCd;
	}
	public int getSkuCount() {
		return skuCount;
	}
	public void setSkuCount(int skuCount) {
		this.skuCount = skuCount;
	}
	public long getCarId() {
		return carId;
	}
	public void setCarId(long carId) {
		this.carId = carId;
	}
	public String getDeptCd() {
		return deptCd;
	}
	public void setDeptCd(String deptCd) {
		this.deptCd = deptCd;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getCompletionDate() {
		return completionDate;
	}
	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getDmmEmail() {
		return dmmEmail;
	}
	public void setDmmEmail(String dmmEmail) {
		this.dmmEmail = dmmEmail;
	}
	public String getDmmName() {
		return dmmName;
	}
	public void setDmmName(String dmmName) {
		this.dmmName = dmmName;
	}
	public String getGmmEmail() {
		return gmmEmail;
	}
	public void setGmmEmail(String gmmEmail) {
		this.gmmEmail = gmmEmail;
	}
	public String getGmmName() {
		return gmmName;
	}
	public void setGmmName(String gmmName) {
		this.gmmName = gmmName;
	}
	public String getStatusCd() {
		return statusCd;
	}
	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}
	public String getVendorNumber() {
		return vendorNumber;
	}
	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = (brandName==null)? "":brandName;
	}

}
