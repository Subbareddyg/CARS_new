package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the item category spec information. 
 * Current Path in xml response: /getItemRespone/Item_Catalog/pim_entry/entry/Item_Ctg_Spec.
 */
@XmlRootElement(name ="Item_Ctg_Spec")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ItemCatalogSpecData {
	
	private Long primaryKey;
	
	private String display;
	
	private Long idbId;
	
	private String status;
	
	private String retailZoneGroupId;
	
	private String costZoneGroupId;
	
	private String standardUOM;
	
	private String uomConversionFactor;
	
	private Boolean isMerchandiseFlag;
	
	private String storeOrder;
	
	private Boolean isForecastFlag;
	
	private String originalRetailPrice;
	
	private String itemComments;
	
	private Boolean isInventoryFlag;
	
	private Boolean isContainsInnerFlag;
	
	private String handlingSensitivity;
	
	private String idbSubClass;
	
	private ItemSupplierData supplierData;
	
	private ItemDescriptionData descriptionData;
	
	private String purchaseType;

	@XmlElement(name = "Id")
	public Long getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

	@XmlElement(name = "display")
	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	@XmlElement(name = "IDB_Id")
	public Long getIdbId() {
		return idbId;
	}

	public void setIdbId(Long idbId) {
		this.idbId = idbId;
	}

	@XmlElement(name = "Status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@XmlElement(name = "Retail_Zone_Group_Id")
	public String getRetailZoneGroupId() {
		return retailZoneGroupId;
	}

	public void setRetailZoneGroupId(String retailZoneGroupId) {
		this.retailZoneGroupId = retailZoneGroupId;
	}

	@XmlElement(name = "Cost_Zone_Group_Id")
	public String getCostZoneGroupId() {
		return costZoneGroupId;
	}

	public void setCostZoneGroupId(String costZoneGroupId) {
		this.costZoneGroupId = costZoneGroupId;
	}

	@XmlElement(name = "Standard_UOM")
	public String getStandardUOM() {
		return standardUOM;
	}

	public void setStandardUOM(String standardUOM) {
		this.standardUOM = standardUOM;
	}

	@XmlElement(name = "UOM_Conv_Factor")
	public String getUomConversionFactor() {
		return uomConversionFactor;
	}

	public void setUomConversionFactor(String uomConversionFactor) {
		this.uomConversionFactor = uomConversionFactor;
	}

	@XmlElement(name = "Merchandise_Flag")
	public Boolean getIsMerchandiseFlag() {
		return isMerchandiseFlag;
	}

	public void setIsMerchandiseFlag(Boolean isMerchandiseFlag) {
		this.isMerchandiseFlag = isMerchandiseFlag;
	}

	@XmlElement(name = "Store_Order")
	public String getStoreOrder() {
		return storeOrder;
	}

	public void setStoreOrder(String storeOrder) {
		this.storeOrder = storeOrder;
	}

	@XmlElement(name = "Forecast_Flag")
	public Boolean getIsForecastFlag() {
		return isForecastFlag;
	}

	public void setIsForecastFlag(Boolean isForecastFlag) {
		this.isForecastFlag = isForecastFlag;
	}

	@XmlElement(name = "Original_Retail")
	public String getOriginalRetailPrice() {
		return originalRetailPrice;
	}

	public void setOriginalRetailPrice(String originalRetailPrice) {
		this.originalRetailPrice = originalRetailPrice;
	}

	@XmlElement(name = "Comments")
	public String getItemComments() {
		return itemComments;
	}

	public void setItemComments(String itemComments) {
		this.itemComments = itemComments;
	}

	@XmlElement(name = "Inventory_Flag")
	public Boolean getIsInventoryFlag() {
		return isInventoryFlag;
	}

	public void setIsInventoryFlag(Boolean isInventoryFlag) {
		this.isInventoryFlag = isInventoryFlag;
	}

	@XmlElement(name = "Contains_Inner_Flag")
	public Boolean getIsContainsInnerFlag() {
		return isContainsInnerFlag;
	}

	public void setIsContainsInnerFlag(Boolean isContainsInnerFlag) {
		this.isContainsInnerFlag = isContainsInnerFlag;
	}

	@XmlElement(name = "Handling_Sensitivity")
	public String getHandlingSensitivity() {
		return handlingSensitivity;
	}

	public void setHandlingSensitivity(String handlingSensitivity) {
		this.handlingSensitivity = handlingSensitivity;
	}

	@XmlElement(name = "IDB_Subclass")
	public String getIdbSubClass() {
		return idbSubClass;
	}

	public void setIdbSubClass(String idbSubClass) {
		this.idbSubClass = idbSubClass;
	}

	@XmlElement(name = "Supplier")
	public ItemSupplierData getSupplierData() {
		return supplierData;
	}

	public void setSupplierData(ItemSupplierData supplierData) {
		this.supplierData = supplierData;
	}
	
	@XmlElement(name = "Purchase_Type")
	public String getPurchaseType() {
		return purchaseType;
	}

	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
	}

	@XmlElement(name = "Description")
	public ItemDescriptionData getDescriptionData() {
		return descriptionData;
	}

	public void setDescriptionData(ItemDescriptionData descriptionData) {
		this.descriptionData = descriptionData;
	}
	

}
