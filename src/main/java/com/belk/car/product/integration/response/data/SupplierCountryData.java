package com.belk.car.product.integration.response.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the supplier country information. 
 * Current Path in xml response: /getItemRespone/Item_Catalog/pim_entry/entry/Item_Ctg_Spec/Supplier/Country.
 */
@XmlRootElement(name="Country")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class SupplierCountryData {
	
	private String countryIndicator;
	
	private String originId;
	
	private String isPrimaryFlag;
	
	private String unitCost;
	
	private String leadTime;
	
	private String pickupLeadTime;
	
	private String packSize;
	
	private String innerPackSize;
	
	private Integer minimumOrderQty;
	
	private Integer maximumOrderQty;
	
	private String packingMethod;
	
	private String defaultUOP;
	
	private String supplierTI;
	
	private String supplierHI;
	
	private String costUOM;
	
	private List<DimensionsData> dimensions;

	@XmlAttribute(name="occ")
	public String getCountryIndicator() {
		return countryIndicator;
	}

	public void setCountryIndicator(String countryIndicator) {
		this.countryIndicator = countryIndicator;
	}

	@XmlElement(name="Origin_Id")
	public String getOriginId() {
		return originId;
	}

	public void setOriginId(String originId) {
		this.originId = originId;
	}

	@XmlElement(name="Primary_Flag")
	public String getIsPrimaryFlag() {
		return isPrimaryFlag;
	}

	public void setIsPrimaryFlag(String isPrimaryFlag) {
		this.isPrimaryFlag = isPrimaryFlag;
	}

	@XmlElement(name="Unit_Cost")
	public String getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(String unitCost) {
		this.unitCost = unitCost;
	}

	@XmlElement(name="Lead_Time")
	public String getLeadTime() {
		return leadTime;
	}

	public void setLeadTime(String leadTime) {
		this.leadTime = leadTime;
	}

	@XmlElement(name="Pickup_Lead_Time")
	public String getPickupLeadTime() {
		return pickupLeadTime;
	}

	public void setPickupLeadTime(String pickupLeadTime) {
		this.pickupLeadTime = pickupLeadTime;
	}

	@XmlElement(name="Pack_Size")
	public String getPackSize() {
		return packSize;
	}

	public void setPackSize(String packSize) {
		this.packSize = packSize;
	}

	@XmlElement(name="Inner_Pack_Size")
	public String getInnerPackSize() {
		return innerPackSize;
	}

	public void setInnerPackSize(String innerPackSize) {
		this.innerPackSize = innerPackSize;
	}

	@XmlElement(name="Min_Order_Qty")
	public Integer getMinimumOrderQty() {
		return minimumOrderQty;
	}

	public void setMinimumOrderQty(Integer minimumOrderQty) {
		this.minimumOrderQty = minimumOrderQty;
	}

	@XmlElement(name="Max_Order_Qty")
	public Integer getMaximumOrderQty() {
		return maximumOrderQty;
	}

	public void setMaximumOrderQty(Integer maximumOrderQty) {
		this.maximumOrderQty = maximumOrderQty;
	}

	@XmlElement(name="Packing_Method")
	public String getPackingMethod() {
		return packingMethod;
	}

	public void setPackingMethod(String packingMethod) {
		this.packingMethod = packingMethod;
	}

	@XmlElement(name="Default_UOP")
	public String getDefaultUOP() {
		return defaultUOP;
	}

	public void setDefaultUOP(String defaultUOP) {
		this.defaultUOP = defaultUOP;
	}

	@XmlElement(name="TI")
	public String getSupplierTI() {
		return supplierTI;
	}

	public void setSupplierTI(String supplierTI) {
		this.supplierTI = supplierTI;
	}

	@XmlElement(name="HI")
	public String getSupplierHI() {
		return supplierHI;
	}

	public void setSupplierHI(String supplierHI) {
		this.supplierHI = supplierHI;
	}

	@XmlElement(name="Cost_UOM")
	public String getCostUOM() {
		return costUOM;
	}

	public void setCostUOM(String costUOM) {
		this.costUOM = costUOM;
	}

	@XmlElement(name = "Dimensions")
	public List<DimensionsData> getDimensions() {
		return dimensions;
	}

	public void setDimensions(List<DimensionsData> dimensions) {
		this.dimensions = dimensions;
	}
}
