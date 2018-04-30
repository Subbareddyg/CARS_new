package com.belk.car.product.integration.response.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the supploer information. 
 * Current Path in xml response: /getItemRespone/Item_Catalog/pim_entry/entry/Item_Ctg_Spec/.
 */
@XmlRootElement(name = "Supplier")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ItemSupplierData {
	
	private String itemSupplierIndicator;
	
	private Long id;
	
	private Boolean isPrimaryFlag;
	
	private List<SupplierUPCData> upcodes;
	
	private String supplierVpn;
	
	private String label;
	
	private String consignmentRate;
	
	private String palletName;
	
	private String caseName;
	
	private String innerName;
	
	private String discontinuedDate;
	
	private Boolean isDirectShipFlag;
	
	private SupplierCountryData supplierCountryData;
	
	private SupplierManufacturerData supplierManufacturerData;
	
	@XmlAttribute(name = "occ")
	public String getItemSupplierIndicator() {
		return itemSupplierIndicator;
	}

	public void setItemSupplierIndicator(String itemSupplierIndicator) {
		this.itemSupplierIndicator = itemSupplierIndicator;
	}

	@XmlElement(name = "Id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlElement(name = "Primary_Flag")
	public Boolean getIsPrimaryFlag() {
		return isPrimaryFlag;
	}

	public void setIsPrimaryFlag(Boolean isPrimaryFlag) {
		this.isPrimaryFlag = isPrimaryFlag;
	}

	@XmlElement(name = "UPCs")
	public List<SupplierUPCData> getUpcodes() {
		return upcodes;
	}

	public void setUpcodes(List<SupplierUPCData> upcodes) {
		this.upcodes = upcodes;
	}

	@XmlElement(name = "VPN")
	public String getSupplierVpn() {
		return supplierVpn;
	}

	public void setSupplierVpn(String supplierVpn) {
		this.supplierVpn = supplierVpn;
	}

	@XmlElement(name = "Label")
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@XmlElement(name = "Consignment_Rate")
	public String getConsignmentRate() {
		return consignmentRate;
	}

	public void setConsignmentRate(String consignmentRate) {
		this.consignmentRate = consignmentRate;
	}

	@XmlElement(name = "Pallet_Name")
	public String getPalletName() {
		return palletName;
	}

	public void setPalletName(String palletName) {
		this.palletName = palletName;
	}

	@XmlElement(name = "Case_Name")
	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}

	@XmlElement(name = "Inner_Name")
	public String getInnerName() {
		return innerName;
	}

	public void setInnerName(String innerName) {
		this.innerName = innerName;
	}

	@XmlElement(name = "Discontinue_Date")
	public String getDiscontinuedDate() {
		return discontinuedDate;
	}

	public void setDiscontinuedDate(String discontinuedDate) {
		this.discontinuedDate = discontinuedDate;
	}

	@XmlElement(name = "Direct_Ship_Flag")
	public Boolean getIsDirectShipFlag() {
		return isDirectShipFlag;
	}

	public void setIsDirectShipFlag(Boolean isDirectShipFlag) {
		this.isDirectShipFlag = isDirectShipFlag;
	}

	@XmlElement(name = "Country")
	public SupplierCountryData getSupplierCountryData() {
		return supplierCountryData;
	}

	public void setSupplierCountryData(SupplierCountryData supplierCountryData) {
		this.supplierCountryData = supplierCountryData;
	}

	@XmlElement(name = "Manufacturer")
	public SupplierManufacturerData getSupplierManufacturerData() {
		return supplierManufacturerData;
	}

	public void setSupplierManufacturerData(
			SupplierManufacturerData supplierManufacturerData) {
		this.supplierManufacturerData = supplierManufacturerData;
	}
}
