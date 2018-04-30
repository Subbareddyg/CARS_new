package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the group category spec information. 
 * Current Path in xml response: /getGroupResponse/Group_Catalog/pim_entry/entry/Group_Ctg_Spec.
 */
@XmlRootElement(name ="Group_Ctg_Spec")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GroupCatalogSpecData {
	
	private Long primaryKey;
	
	private String name;
	
	private String description;
	
	private String type;
	
	private String imageStatus;
	
	private String contentStatus;
	
	private String overallStatus;
	
	private String carsGroupType;
	
	private String effectiveStartDate;
	
	private String effectiveEndDate;
	
	private String completionDate;
	
	private String omniChannelBrand;
	
	private String carBrand;
	
	private String vendorNumber;
	
	private String productCode;
	
	@XmlElement(name = "Id")
	public Long getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	@XmlElement(name = "Name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name = "Description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@XmlElement(name = "Type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@XmlElement(name = "Image_Status")
	public String getImageStatus() {
		return imageStatus;
	}

	public void setImageStatus(String imageStatus) {
		this.imageStatus = imageStatus;
	}
	
	@XmlElement(name = "Content_Status")
	public String getContentStatus() {
		return contentStatus;
	}

	public void setContentStatus(String contentStatus) {
		this.contentStatus = contentStatus;
	}
	
	@XmlElement(name = "Overall_Status")
	public String getOverallStatus() {
		return overallStatus;
	}

	public void setOverallStatus(String overallStatus) {
		this.overallStatus = overallStatus;
	}
	
	@XmlElement(name = "CARS_Group_Type")
	public String getCARSGroupType() {
		return carsGroupType;
	}

	public void setCARSGroupType(String carsGroupType) {
		this.carsGroupType = carsGroupType;
	}
	
	@XmlElement(name = "Effective_Start_Date")
	public String getEffectiveStartDate() {
		return effectiveStartDate;
	}

	public void setEffectiveStartDate(String effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}
	
	@XmlElement(name = "Effective_End_Date")
	public String getEffectiveEndDate() {
		return effectiveEndDate;
	}

	public void setEffectiveEndDate(String effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}
	
	@XmlElement(name = "Completion_Date")
	public String getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(String completionDate) {
		this.completionDate = completionDate;
	}
	
	@XmlElement(name = "OmniChannelBrand")
	public String getOmniChannelBrand() {
		return omniChannelBrand;
	}

	public void setOmniChannelBrand(String omniChannelBrand) {
		this.omniChannelBrand = omniChannelBrand;
	}
	
	@XmlElement(name = "CarBrand")
	public String getCarBrand() {
		return carBrand;
	}

	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}
	
	@XmlElement(name = "Vendor_Number")
	public String getVendorNumber() {
		return vendorNumber;
	}

	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}
	
	@XmlElement(name = "Product_Code")
	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
}
