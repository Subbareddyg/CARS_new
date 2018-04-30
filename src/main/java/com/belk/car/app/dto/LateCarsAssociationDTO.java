package com.belk.car.app.dto;

import java.io.Serializable;

public class LateCarsAssociationDTO implements Serializable  
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -900268879400266044L;
	
	public LateCarsAssociationDTO()
	{

	}
	
	
	private Long lateCarsAssociationID;
	private String deptIds;
	private String vendorIds;
	private String deptNO;	
	private String vendorNo;
	private String deptName;
	private String vendorName;

	public Long getLateCarsAssociationID() {
		return lateCarsAssociationID;
	}
	public void setLateCarsAssociationID(Long lateCarsAssociationID) {
		this.lateCarsAssociationID = lateCarsAssociationID;
	}
	public String getDeptIds() {
		return deptIds;
	}
	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}
	public String getVendorIds() {
		return vendorIds;
	}
	public void setVendorIds(String vendorIds) {
		this.vendorIds = vendorIds;
	}
	public String getDeptNO() {
		return deptNO;
	}
	public void setDeptNO(String deptNO) {
		this.deptNO = deptNO;
	}
	public String getVendorNo() {
		return vendorNo;
	}
	public void setVendorNo(String vendorNo) {
		this.vendorNo = vendorNo;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
}
