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



@NamedNativeQuery(name="supercolor_view",query="select * from v_sku_color_rule" ,resultClass = SuperColorSynchDataHolderView.class)
@Entity
@Table (name = "v_sku_color_rule")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class SuperColorSynchDataHolderView extends BaseAuditableModel implements java.io.Serializable {

	private static final long serialVersionUID = -3982928293839999999L;
	
	String 	carSkuId;
	String  carId;
	String  belkUpc;
	String  vendorUpc;
	String colorRuleId;
	long  	cmmId;
	String  colorCodeBegin;
	String  colorCodeEnd;
	String  superColorCode;
	String  superColorName;
	String  ruleChanged;
	String  ruleStatus;
	String  createdBy;
	String  updatedBy;
	Date createdDate;
	Date updatedDate;
	
	
	@Id
	@Column(name = "CAR_SKU_ID", nullable = true, length = 12)
	public String getCarSkuId() {
		return carSkuId;
	}
	public void setCarSkuId(String carSkuId) {
		this.carSkuId = carSkuId;
	}
	
	@Column(name = "CAR_ID", nullable = true, length = 12)
	public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}
	
	@Column(name = "BELK_UPC", nullable = true, length = 20)
	public String getBelkUpc() {
		return belkUpc;
	}
	public void setBelkUpc(String belkUpc) {
		this.belkUpc = belkUpc;
	}
	
	@Column(name = "VENDOR_UPC", nullable = true, length = 20)
	public String getVendorUpc() {
		return vendorUpc;
	}
	public void setVendorUpc(String vendorUpc) {
		this.vendorUpc = vendorUpc;
	}
	
	@Column(name = "CMM_ID", nullable = true, length = 12)
	public long getCmmId() {
		return cmmId;
	}
	public void setCmmId(long cmmId) {
		this.cmmId = cmmId;
	}
	
	@Column(name = "COLOR_CODE_BEGIN", nullable = true, length = 3)
	public String getColorCodeBegin() {
		return colorCodeBegin;
	}
	public void setColorCodeBegin(String colorCodeBegin) {
		this.colorCodeBegin = colorCodeBegin;
	}
	
	@Column(name = "COLOR_CODE_END", nullable = true, length = 3)
	public String getColorCodeEnd() {
		return colorCodeEnd;
	}
	public void setColorCodeEnd(String colorCodeEnd) {
		this.colorCodeEnd = colorCodeEnd;
	}
	
	@Column(name = "SUPER_COLOR_CODE", nullable = true, length = 3)
	public String getSuperColorCode() {
		return superColorCode;
	}
	public void setSuperColorCode(String superColorCode) {
		this.superColorCode = superColorCode;
	}
	
	@Column(name = "SUPER_COLOR_NAME", nullable = true, length = 3)
	public String getSuperColorName() {
		return superColorName;
	}
	public void setSuperColorName(String superColorName) {
		this.superColorName = superColorName;
	}
	
	@Column(name = "RULE_CHANGED", nullable = true, length = 1)
	public String getRuleChanged() {
		return ruleChanged;
	}
	public void setRuleChanged(String ruleChanged) {
		this.ruleChanged = ruleChanged;
	}
	
	@Column(name = "RULE_STATUS", nullable = true, length = 1)
	public String getRuleStatus() {
		return ruleStatus;
	}
	public void setRuleStatus(String ruleStatus) {
		this.ruleStatus = ruleStatus;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Column(name = "COLOR_RULE_ID", nullable = true,  length = 12)
	public String getColorRuleId() {
		return colorRuleId;
	}
	public void setColorRuleId(String colorRuleId) {
		this.colorRuleId = colorRuleId;
	}
	
	
}


