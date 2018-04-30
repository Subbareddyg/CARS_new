package com.belk.car.app.to;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;


@Entity
@Table(name = "idb_drop_ship_feed", uniqueConstraints = @UniqueConstraint(columnNames = "vendor_upc"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class IdbDropshipDataTO extends BaseAuditableModel implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String vendorNumber;
	private String vendorStyleID;
	private String belkUPC;
	private String vendorName;
	private Long deptNum;
	private String deptName;
	private Long classNum;
	private String className;
	private String styleDesc;
	private String vendorColorCode;
	private String colorDesc;
	private String vndrSizeCode;
	private String vndrSize;
	private String vndrUPC;
	private Date uPCAddDate;
	private Double iDBUPCUnitPrice;
	private Double iDBUPCUnitCost;
	private Double iDBStyleUnitPrice;
	private Double iDBStyleUnitCost;
	private String sizeChartCode;
	private String styleDropshipFlag;
	private String sKUDropshipFlag;
	private Date dropshipFlagUpdated;
	private String notOrderableFlag;
	private String uPCDiscountedFlag;
	private Date uPCDiscountedDate;
	
	//Transient variables
	private String newBelkUPC;
	private String oldBelkUPC;
	private String vndrStyleNumForNewBelkUPC;
	
	
	/*
	 * Constructor
	 */
	public IdbDropshipDataTO(){
		super();
	}
	
	@Column(name="vendor_number",nullable=true)
	public String getVendorNumber() {
		return vendorNumber;
	}
	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}
	
	@Column(name="vendor_sytle_id",nullable=false)
	public String getVendorStyleID() {
		return vendorStyleID;
	}
	public void setVendorStyleID(String vendorStyleID) {
		this.vendorStyleID = vendorStyleID;
	}
	
	@Column(name="belk_upc",nullable=true)
	public String getBelkUPC() {
		return belkUPC;
	}
	public void setBelkUPC(String belkUPC) {
		this.belkUPC = belkUPC;
	}
	@Column(name="vendor_name",nullable=true)
	public String getVendorName() {
		return vendorName;
	}
	
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	
	@Column(name="dept_id",nullable=true)
	public Long getDeptNum() {
		return deptNum;
	}
	public void setDeptNum(Long deptNum) {
		this.deptNum = deptNum;
	}
	
	@Column(name="dept_name",nullable=true)
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	@Column(name="class_id",nullable=true)
	public Long getClassNum() {
		return classNum;
	}
	public void setClassNum(Long classNum) {
		this.classNum = classNum;
	}
	
	@Column(name="class_name",nullable=true)
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	@Column(name="style_descr",nullable=true)
	public String getStyleDesc() {
		return styleDesc;
	}
	public void setStyleDesc(String styleDesc) {
		this.styleDesc = styleDesc;
	}
	
	@Column(name="color_cd",nullable=true)
	public String getVendorColorCode() {
		return vendorColorCode;
	}
	public void setVendorColorCode(String vendorColorCode) {
		this.vendorColorCode = vendorColorCode;
	}
	
	@Column(name="color",nullable=true)
	public String getColorDesc() {
		return colorDesc;
	}
	public void setColorDesc(String colorDesc) {
		this.colorDesc = colorDesc;
	}
	
	@Column(name="size_cd",nullable=true)
	public String getVndrSizeCode() {
		return vndrSizeCode;
	}
	public void setVndrSizeCode(String vndrSizeCode) {
		this.vndrSizeCode = vndrSizeCode;
	}
	
	@Column(name="size_descr",nullable=true)
	public String getVndrSize() {
		return vndrSize;
	}
	public void setVndrSize(String vndrSize) {
		this.vndrSize = vndrSize;
	}
	
	@Id
	@Column(name = "vendor_upc", unique = true, nullable = false, precision = 12, scale = 0)
	public String getVndrUPC() {
		return vndrUPC;
	}
	public void setVndrUPC(String vndrUPC) {
		this.vndrUPC = vndrUPC;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name="update_add_date",nullable=true)
	public Date getUPCAddDate() {
		return uPCAddDate;
	}
	public void setUPCAddDate(Date addDate) {
		uPCAddDate = addDate;
	}
	
	@Column(name="idb_upc_unit_price",nullable=true)
	public Double getIDBUPCUnitPrice() {
		return iDBUPCUnitPrice;
	}
	public void setIDBUPCUnitPrice(Double unitPrice) {
		iDBUPCUnitPrice = unitPrice;
	}
	
	@Column(name="idb_upc_unit_cost",nullable=true)
	public Double getIDBUPCUnitCost() {
		return iDBUPCUnitCost;
	}
	public void setIDBUPCUnitCost(Double unitCost) {
		iDBUPCUnitCost = unitCost;
	}
	
	@Column(name="idb_style_lvl_unit_price",nullable=true)
	public Double getIDBStyleUnitPrice() {
		return iDBStyleUnitPrice;
	}
	public void setIDBStyleUnitPrice(Double styleUnitPrice) {
		iDBStyleUnitPrice = styleUnitPrice;
	}
	
	@Column(name="idb_style_lvl_unit_cost",nullable=true)
	public Double getIDBStyleUnitCost() {
		return iDBStyleUnitCost;
	}
	public void setIDBStyleUnitCost(Double styleUnitCost) {
		iDBStyleUnitCost = styleUnitCost;
	}
	
	@Column(name="size_chart_cd",nullable=true)
	public String getSizeChartCode() {
		return sizeChartCode;
	}
	public void setSizeChartCode(String sizeChartCode) {
		this.sizeChartCode = sizeChartCode;
	}
	
	@Column(name="style_drop_ship_flag",nullable=false)
	public String getStyleDropshipFlag() {
		return styleDropshipFlag;
	}
	public void setStyleDropshipFlag(String styleDropshipFlag) {
		this.styleDropshipFlag = styleDropshipFlag;
	}
	
	@Column(name="sku_drop_ship_flag",nullable=false)
	public String getSKUDropshipFlag() {
		return sKUDropshipFlag;
	}
	public void setSKUDropshipFlag(String dropshipFlag) {
		sKUDropshipFlag = dropshipFlag;
	}
	
	@Column(name="drop_ship_flag_update",nullable=true)
	public Date getDropshipFlagUpdated() {
		return dropshipFlagUpdated;
	}
	public void setDropshipFlagUpdated(Date dropshipFlagUpdated) {
		this.dropshipFlagUpdated = dropshipFlagUpdated;
	}
	
	@Column(name="not_orderable_flag",nullable=true)
	public String getNotOrderableFlag() {
		return notOrderableFlag;
	}
	public void setNotOrderableFlag(String notOrderableFlag) {
		this.notOrderableFlag = notOrderableFlag;
	}
	
	@Column(name="upc_discontinue_flag",nullable=true)
	public String getUPCDiscountedFlag() {
		return uPCDiscountedFlag;
	}
	public void setUPCDiscountedFlag(String discountedFlag) {
		uPCDiscountedFlag = discountedFlag;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name="upc_discontinue_date",nullable=true)
	public Date getUPCDiscountedDate() {
		return uPCDiscountedDate;
	}
	public void setUPCDiscountedDate(Date discountedDate) {
		uPCDiscountedDate = discountedDate;
	}
	
	@Column(name = "UPDATED_BY", nullable = false, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}
	
	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}
	
	/**
	 * @return the newBelkUPC
	 */
	@Transient
	public String getNewBelkUPC() {
		return newBelkUPC;
	}

	/**
	 * @param newBelkUPC the newBelkUPC to set
	 */
	public void setNewBelkUPC(String newBelkUPC) {
		this.newBelkUPC = newBelkUPC;
	}

	/**
	 * @return the oldBelkUPC
	 */
	@Transient
	public String getOldBelkUPC() {
		return oldBelkUPC;
	}

	/**
	 * @param oldBelkUPC the oldBelkUPC to set
	 */
	
	public void setOldBelkUPC(String oldBelkUPC) {
		this.oldBelkUPC = oldBelkUPC;
	}
	
	/**
	 * @return the vndrStyleNumForNewBelkUPC
	 */
	@Transient
	public String getVndrStyleNumForNewBelkUPC() {
		return vndrStyleNumForNewBelkUPC;
	}

	/**
	 * @param vndrStyleNumForNewBelkUPC the vndrStyleNumForNewBelkUPC to set
	 */
	public void setVndrStyleNumForNewBelkUPC(String vndrStyleNumForNewBelkUPC) {
		this.vndrStyleNumForNewBelkUPC = vndrStyleNumForNewBelkUPC;
	}


}

	