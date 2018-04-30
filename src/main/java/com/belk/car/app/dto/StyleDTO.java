package com.belk.car.app.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.belk.car.app.model.Car;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.car.ManualCar;


public class StyleDTO implements Serializable {

	private static final long serialVersionUID = 2306241559145552982L;
	String vendorNumber;
	String vendorName;
	String departmentName;
	String departmentNumber;
	String classNumber;
	String className;
	String vendorStyle;
	String vendorStyleDescription;
	String parentVendorStyle;
	String orin;
	String poNumber;
	String expectedShipDate;
	Car existingCar ;
	VendorStyle pattern;
	VendorStyle vendorStyleObj;
	Classification classification;
	Department department;
	Vendor vendor;
	ManualCar manualCar;
	String errorMsg;
	String packId;
	String PackUPC;
	boolean sellable;
	boolean vendorStyleExixtsInDB;
	
	List<vendorStylePIMAttributeDTO> pimAttributeDTOList = new ArrayList<vendorStylePIMAttributeDTO>();
	List<SkuDTO> skuInfo = new ArrayList<SkuDTO>();
	

	public StyleDTO() {

	}

	public String getVendorNumber() {
		return vendorNumber;
	}

	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDepartmentNumber() {
		return departmentNumber;
	}

	public void setDepartmentNumber(String departmentNumber) {
		this.departmentNumber = departmentNumber;
	}

	public String getClassNumber() {
		return classNumber;
	}

	public void setClassNumber(String classNumber) {
		this.classNumber = classNumber;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getVendorStyle() {
		return vendorStyle;
	}

	public void setVendorStyle(String vendorStyle) {
		this.vendorStyle = vendorStyle;
	}

	public String getVendorStyleDescription() {
		return vendorStyleDescription;
	}

	public void setVendorStyleDescription(String vendorStyleDescription) {
		this.vendorStyleDescription = vendorStyleDescription;
	}

	public String getParentVendorStyle() {
		return parentVendorStyle;
	}

	public void setParentVendorStyle(String parentVendorStyle) {
		this.parentVendorStyle = parentVendorStyle;
	}

	public String getOrin() {
		return orin;
	}

	public void setOrin(String orin) {
		this.orin = orin;
	}

	public VendorStyle getVendorStyleObj() {
		return vendorStyleObj;
	}

	public void setVendorStyleObj(VendorStyle vendorStyleObj) {
		this.vendorStyleObj = vendorStyleObj;
	}

	public Classification getClassification() {
		return classification;
	}

	public void setClassification(Classification classification) {
		this.classification = classification;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}


	public List<vendorStylePIMAttributeDTO> getPimAttributeDTOList() {
		return pimAttributeDTOList;
	}

	public void setPimAttributeDTOList(List<vendorStylePIMAttributeDTO> pimAttributeDTOList) {
		this.pimAttributeDTOList = pimAttributeDTOList;
	}

	public List<SkuDTO> getSkuInfo() {
		return skuInfo;
	}

	public void setSkuInfo(List<SkuDTO> skuInfo) {
		this.skuInfo = skuInfo;
	}

	public ManualCar getManualCar() {
		return manualCar;
	}

	public void setManualCar(ManualCar manualCar) {
		this.manualCar = manualCar;
	}
	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public VendorStyle getPattern() {
		return pattern;
	}

	public void setPattern(VendorStyle pattern) {
		this.pattern = pattern;
	}

	public Car getExistingCar() {
		return existingCar;
	}

	public void setExistingCar(Car existingCar) {
		this.existingCar = existingCar;
	}

	public String getExpectedShipDate() {
		return expectedShipDate;
	}

	public void setExpectedShipDate(String expectedShipDate) {
		this.expectedShipDate = expectedShipDate;
	}

	public String getPackId() {
		return packId;
	}

	public void setPackId(String packId) {
		this.packId = packId;
	}

	public String getPackUPC() {
		return PackUPC;
	}

	public void setPackUPC(String packUPC) {
		PackUPC = packUPC;
	}

	public boolean isSellable() {
		return sellable;
	}

	public void setSellable(boolean sellable) {
		this.sellable = sellable;
	}

	public boolean isVendorStyleExixtsInDB() {
		return vendorStyleExixtsInDB;
	}

	public void setVendorStyleExixtsInDB(boolean vendorStyleExixtsInDB) {
		this.vendorStyleExixtsInDB = vendorStyleExixtsInDB;
	}

	
	

		
	
}
