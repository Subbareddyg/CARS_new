package com.belk.car.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
/**
 * 
 * @author Yogesh.Vedak
 *
 */


@NamedNativeQuery(name="size_view",query="Select * from V_SKU_SIZE_RULE" ,resultClass = SizeSynchDataHolderView.class)
@Entity
@Table (name = "V_SKU_SIZE_RULE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class SizeSynchDataHolderView extends BaseAuditableModel implements java.io.Serializable {

	private static final long serialVersionUID = -3982928293999999999L;
	
	long 	carSkuId;
	String  vendorUpc;
	String  belkUpc;
	String 	sizeName;
	String	sizeRuleId;
	String 	colorRuleId;
	Long 	deptId;
	Long  	classId;
	Long  	vendorId;
	String 	matchingSizeName;
	long 	scmId;
	Long 	scmDeptId ;
	Long  	scmClassId;
	Long  	scmVendorId;
	String  scmSizeName;
	String  scmConversionSizeName;
	String  statusCd;
	String  createdBy;
	String  updatedBy;
	Date 	createdDate;
	Date 	updatedDate;
	String  facet1="";
	String  facet2="";
	String  facet3="";
	String  subFacet1="";
	String  subFacet2="";
	String  scmRuleChanged="";
	String  rank ="";
	String contentStatus="";
	
	@Id
	@Column(name = "CAR_SKU_ID", nullable = false, precision = 12 ,scale = 0)
	public long getCarSkuId() {
		return carSkuId;
	}
	public void setCarSkuId(long carSkuId) {
		this.carSkuId = carSkuId;
	}
	@Column(name = "BELK_UPC", nullable = false, length = 20)
	public String getBelkUpc() {
		return belkUpc;
	}
	public void setBelkUpc(String belkUpc) {
		this.belkUpc = belkUpc;
	}
	
	@Column(name = "SIZE_NAME", nullable = true, length = 100)
	public String getSizeName() {
		return sizeName;
	}
	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}
	
	@Column(name = "SIZE_RULE_ID", nullable = true,  length = 12)
	public String getSizeRuleId() {
		return sizeRuleId;
	}
	public void setSizeRuleId(String sizeRuleId) {
		this.sizeRuleId = sizeRuleId;
	}
	
	@Column(name = "COLOR_RULE_ID", nullable = true, precision = 12,scale = 0)
	public String getColorRuleId() {
		return colorRuleId;
	}
	public void setColorRuleId(String colorRuleId) {
		this.colorRuleId = colorRuleId;
	}
	
	@Column(name = "DEPT_ID", nullable = true, precision = 12, scale = 0)
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	
	@Column(name = "CLASS_ID", nullable = true, precision = 12, scale = 0)
	public Long getClassId() {
		return classId;
	}
	public void setClassId(Long classId) {
		this.classId = classId;
	}
	
	@Column(name = "VENDOR_ID", nullable = true, precision = 12, scale = 0)
	public Long getVendorId() {
		return vendorId;
	}
	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
	
	@Column(name = "MATCHING_SIZE_NAME", nullable = true, length = 100)
	public String getMatchingSizeName() {
		return matchingSizeName;
	}
	public void setMatchingSizeName(String matchingSizeName) {
		this.matchingSizeName = matchingSizeName;
	}
	
	@Column(name = "SCM_DEPT_ID", nullable = true, precision = 12, scale = 0)
	public Long getScmDeptId() {
		return scmDeptId;
	}
	public void setScmDeptId(Long scmDeptId) {
		this.scmDeptId = scmDeptId;
	}
	
	@Column(name = "SCM_CLASS_ID", nullable = true, precision = 12, scale = 0)
	public Long getScmClassId() {
		return scmClassId;
	}
	public void setScmClassId(Long scmClassId) {
		this.scmClassId = scmClassId;
	}
	
	@Column(name = "SCM_VENDOR_ID", nullable = true, precision = 12, scale = 0)
	public Long getScmVendorId() {
		return scmVendorId;
	}
	public void setScmVendorId(Long scmVendorId) {
		this.scmVendorId = scmVendorId;
	}
	
	@Column(name = "SCM_SIZE_NAME", nullable = true, length = 100)
	public String getScmSizeName() {
		return scmSizeName;
	}
	public void setScmSizeName(String scmSizeName) {
		this.scmSizeName = scmSizeName;
	}
	
	@Column(name = "SCM_CONVERSION_SIZE_NAME", nullable = true, length = 100)
	public String getScmConversionSizeName() {
		return scmConversionSizeName;
	}
	public void setScmConversionSizeName(String scmConversionSizeName) {
		this.scmConversionSizeName = scmConversionSizeName;
	}
	
	@Column(name = "STATUS_CD", nullable = true, length = 20)
	public String getStatusCd() {
		return statusCd;
	}
	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}
	
	@Column(name = "CREATED_BY", nullable = true, length = 100)
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name = "UPDATED_BY", nullable = true, length = 100)
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	@Column(name = "CREATED_DATE", nullable = true)
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name = "UPDATED_DATE", nullable = true)
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	@Column(name = "FACET_SIZE_1", nullable = true, length = 100)
	public String getFacet1() {
		return facet1;
	}
	public void setFacet1(String facet1) {
		this.facet1 = facet1;
	}
	
	@Column(name = "FACET_SIZE_2", nullable = true, length = 100)
	public String getFacet2() {
		return facet2;
	}
	public void setFacet2(String facet2) {
		this.facet2 = facet2;
	}
	
	@Column(name = "FACET_SIZE_3", nullable = true, length = 100)
	public String getFacet3() {
		return facet3;
	}
	public void setFacet3(String facet3) {
		this.facet3 = facet3;
	}
	
	@Column(name = "FACET_SUB_SIZE_1", nullable = true, length = 100)
	public String getSubFacet1() {
		return subFacet1;
	}
	public void setSubFacet1(String subFacet1) {
		this.subFacet1 = subFacet1;
	}
	
	@Column(name = "FACET_SUB_SIZE_2", nullable = true, length = 100)
	public String getSubFacet2() {
		return subFacet2;
	}
	public void setSubFacet2(String subFacet2) {
		this.subFacet2 = subFacet2;
	}
	
	@Column(name = "SCM_RULE_CHANGED", nullable = true, length = 1)
	public String getScmRuleChanged() {
		return scmRuleChanged;
	}
	public void setScmRuleChanged(String scmRuleChanged) {
		this.scmRuleChanged = scmRuleChanged;
	}
	
	@Column(name = "RN", nullable = true, length = 50)
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Column(name = "SCM_ID", nullable = true, length = 12)
	public long getScmId() {
		return scmId;
	}
	public void setScmId(long scmId) {
		this.scmId = scmId;
	}
	@Column(name = "vendor_upc", nullable = true, length = 12)
	public String getVendorUpc() {
		return vendorUpc;
	}
	public void setVendorUpc(String vendorUpc) {
		this.vendorUpc = vendorUpc;
	}
	
	@Column(name = "CONTENT_STATUS_CD", nullable = true, length = 20)
	public String getContentStatus() {
		return contentStatus;
	}
	public void setContentStatus(String contentStatus) {
		this.contentStatus = contentStatus;
	}
	
	
}


