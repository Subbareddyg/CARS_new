package com.belk.car.app.to;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.belk.car.app.model.Car;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.car.ManualCar;

public class IdbCarDataTO {
	String validItemFlag;
	String vendorNumber;
	String vendorName;
	String departmentName;
	String departmentNumber;
	String classNumber;
	String className;
	String vendorStyle;
	String vendorStyleDescription;
	String poNumber ;
	String dueDate ;
	String manualCarId;
	String expectedShipDate;
	VendorStyle vendorStyleObj ;
	Classification classification ;
	Department department ;
	Vendor vendor ;
	VendorStyle pattern ;
	ManualCar manualCar;
	Car existingCar ;
	
	List<IdbCarSkuTO> skuInfo = new ArrayList<IdbCarSkuTO>();
	Set<String> skus = new HashSet<String>();

	public IdbCarDataTO() {

	}

	public String getValidItemFlag() {
		return validItemFlag;
	}

	public void setValidItemFlag(String validItemFlag) {
		this.validItemFlag = validItemFlag;
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

	public List<IdbCarSkuTO> getSkuInfo() {
		return skuInfo;
	}

	public void setSkuInfo(List<IdbCarSkuTO> skuInfo) {
		this.skuInfo = skuInfo;
	}

	public void addSku(IdbCarSkuTO idbSku) {
		if (idbSku != null) {
			if (!skus.contains(idbSku.getBelkUPC())) {
				skuInfo.add(idbSku);
				skus.add(idbSku.getBelkUPC());
			}
		}
	}

	public boolean containsSku(String belkUpc) {
		return skus.contains(belkUpc);
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getManualCarId() {
		return manualCarId;
	}

	public void setManualCarId(String manualCarId) {
		this.manualCarId = manualCarId;
	}

	public String getExpectedShipDate() {
		return expectedShipDate;
	}

	public void setExpectedShipDate(String expectedShipDate) {
		this.expectedShipDate = expectedShipDate;
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

	public VendorStyle getPattern() {
		return pattern;
	}

	public void setPattern(VendorStyle pattern) {
		this.pattern = pattern;
	}

	public ManualCar getManualCar() {
		return manualCar;
	}

	public void setManualCar(ManualCar manualCar) {
		this.manualCar = manualCar;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public Car getExistingCar() {
		return existingCar;
	}

	public void setExistingCar(Car existingCar) {
		this.existingCar = existingCar;
	}
}
