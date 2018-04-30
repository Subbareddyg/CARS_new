/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.integrations.rrd.dao;


import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author amuaxg1
 */

public class ProductInfoDAO 
{
	long	carID;
	String	vendorName;
	String	vendorNumber;
	String	styleNumber;
	String	productName;
	String	productType;
	String	brand;
	long	departmentID;
	String 	departmentCode;  //added by santosh
	String	departmentName;
	long	classID;
	String	className;
	String	photoInstructions;
	

	transient List<SampleInfoDAO> sampleDAOs = new ArrayList<SampleInfoDAO>();
	transient ArrayList<String> childStyleNumbers = new ArrayList<String>();
	
	public ArrayList<String> getChildStyleNumbers() { return childStyleNumbers;	}
	public void setChildStyleNumbers(ArrayList<String> childStyleNumbers) {	this.childStyleNumbers = childStyleNumbers;	}


	transient List<ReturnInstructionsDAO> returnInstructionsDAOs = new ArrayList<ReturnInstructionsDAO>();

	public String getBrand() { return brand; }
	public void setBrand(String brand) { this.brand = brand; }
	public long getCarID() { return carID; }
	public void setCarID(long carID) { this.carID = carID; }
	public long getClassID() { return classID; }
	public void setClassID(long classID) { this.classID = classID; }
	public String getClassName() { return className; }
	public void setClassName(String className) { this.className = className; }
	public long getDepartmentID() { return departmentID; }
	public void setDepartmentID(long departmentID) { this.departmentID = departmentID; }
	public String getDepartmentCode() { return departmentCode; }  //added by santosh
	public void setDepartmentCode(String departmentCode) { this.departmentCode = departmentCode; }  //added by santosh
	public String getDepartmentName() { return departmentName; }
	public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
	public String getProductName() { return productName; }
	public void setProductName(String productName) { this.productName = productName; }
	public String getProductType() { return productType; }
	public void setProductType(String productType) { this.productType = productType; }
	public List<SampleInfoDAO> getSampleInfoDAOs() { return sampleDAOs; }
	public void setSampleInfoDAOs(List<SampleInfoDAO> samples) { this.sampleDAOs = samples; }
	public List<ReturnInstructionsDAO> getReturnInstructionsDAOs() { return this.returnInstructionsDAOs; }
	public void setReturnInstructionsDAOs(List<ReturnInstructionsDAO> returnInstructionsDAOs) { this.returnInstructionsDAOs = returnInstructionsDAOs; }
	public String getStyleNumber() { return styleNumber; }
	public void setStyleNumber(String styleNumber) { this.styleNumber = styleNumber; }
	
	public String getVendorName() { return vendorName; }
	public void setVendorName(String vendorName) { this.vendorName = vendorName; }
	public String getVendorNumber() { return vendorNumber; }
	public void setVendorNumber(String vendorNumber) { this.vendorNumber = vendorNumber; }
	public String getPhotoInstructions() { return this.photoInstructions; }
	public void setPhotoInstructions(String photoInstructions) { this.photoInstructions = photoInstructions; }
	
	
	
	
	
	public String toString() {
		return new StringBuffer("ProductInfo[")
			.append(" carID:").append(carID)
			.append(", vendorName:").append(vendorName)
			.append(", vendorNumber:").append(vendorNumber)
			.append(", styleNumber:").append(styleNumber)
			.append(", productName:").append(productName)
			.append(", productType:").append(productType)
			.append(", brand:").append(brand)
			.append(", departmentID:").append(departmentID)
			.append(", departmentCode:").append(departmentCode)    //added by santosh
			.append(", departmentName:").append(departmentName)
			.append(", classID:").append(classID)
			.append(", className:").append(className)
			.append(", photoInstructions:").append(photoInstructions)
			.append(" ]")
			.toString();

	}
	@Override
	public boolean equals( Object o ) {
		if (this == o) return true;
		if (o == null || (!(o instanceof ProductInfoDAO))) return false;
		ProductInfoDAO pi = (ProductInfoDAO)o;
		return (
			this.vendorNumber == null ? pi.vendorNumber == null : this.vendorNumber.equals(pi.vendorNumber)
			&& 
			this.styleNumber == null ? pi.styleNumber == null : this.styleNumber.equals(pi.styleNumber)
		);
	}


	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 83 * hash + (this.vendorNumber != null ? this.vendorNumber.hashCode() : 0);
		hash = 83 * hash + (this.styleNumber != null ? this.styleNumber.hashCode() : 0);
		return hash;
	}

}

