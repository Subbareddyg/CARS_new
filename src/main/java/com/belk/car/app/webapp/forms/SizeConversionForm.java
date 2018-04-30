package com.belk.car.app.webapp.forms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


public class SizeConversionForm {

	private long scmId;
	private String deptCode ;
	/*private short classNumber;*/
	private String classNumber;
	
	private String vendorNumber ;
	private String sizeName="";
	private String coversionSizeName="";
	private String statusCode= "ACTIVE" ;
	private Boolean isEditForm = Boolean.FALSE;
	private String facetSize1="" ;
	private String facetSize2="" ;
	private String facetSize3="" ;
	private String facetSubSize1="" ;
	private String facetSubSize2="" ;
	protected String createdBy;
	protected String updatedBy;
	protected Date createdDate;
	protected Date updatedDate;
	//private Boolean disableSizeField = Boolean.FALSE;
	
	public long getScmId() {
		return scmId;
	}
	public void setScmId(long scmId) {
		this.scmId = scmId;
	}
//	public String getDeptCode() {
//		return deptCode;
//	}
//	public void setDeptCode(String deptCode) {
//		this.deptCode = deptCode;
//	}
	/*public short getClassNumber() {
		return classNumber;
	}
	public void setClassNumber(short classNumber) {
		this.classNumber = classNumber;
	}*/
//	public String getVendor() {
//		return vendorId;
//	}
//	public void setVendorId(String vendorId) {
//		this.vendorId = vendorId;
//	}
	public String getSizeName() {
		return sizeName;
	}
	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}
	public String getCoversionSizeName() {
		return coversionSizeName;
	}
	public void setCoversionSizeName(String coversionSizeName) {
		this.coversionSizeName = coversionSizeName;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getVendorNumber() {
		return vendorNumber;
	}
	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}
	public Boolean getIsEditForm() {
		return isEditForm;
	}
	public void setIsEditForm(Boolean isEditForm) {
		this.isEditForm = isEditForm;
	}
	public String getFacetSize1() {
		return facetSize1;
	}
	public void setFacetSize1(String facetSize1) {
		this.facetSize1 = facetSize1;
	}
	public String getFacetSize2() {
		return facetSize2;
	}
	public void setFacetSize2(String facetSize2) {
		this.facetSize2 = facetSize2;
	}
	public String getFacetSize3() {
		return facetSize3;
	}
	public void setFacetSize3(String facetSize3) {
		this.facetSize3 = facetSize3;
	}
	public String getFacetSubSize1() {
		return facetSubSize1;
	}
	public void setFacetSubSize1(String facetSubSize1) {
		this.facetSubSize1 = facetSubSize1;
	}
	public String getFacetSubSize2() {
		return facetSubSize2;
	}
	public void setFacetSubSize2(String facetSubSize2) {
		this.facetSubSize2 = facetSubSize2;
	}
	public String getClassNumber() {
		return classNumber;
	}
	public void setClassNumber(String classNumber) {
		this.classNumber = classNumber;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}
	/*public Boolean getDisableSizeField() {
		return disableSizeField;
	}
	public void setDisableSizeField(Boolean disableSizeField) {
		this.disableSizeField = disableSizeField;
	}*/
}
