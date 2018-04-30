package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the supplier manufacturer information. 
 * Current Path in xml response: /getItemRespone/Item_Catalog/pim_entry/entry/Item_Ctg_Spec/Supplier/Manufacturer.
 */
@XmlRootElement(name = "Manufacturer")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class SupplierManufacturerData {

	private String manufucturerIndicator;
	
	private String countryCode;
	
	private Boolean isPrimaryCountryFlag;

	@XmlAttribute(name = "occ")
	public String getManufucturerIndicator() {
		return manufucturerIndicator;
	}

	public void setManufucturerIndicator(String manufucturerIndicator) {
		this.manufucturerIndicator = manufucturerIndicator;
	}

	@XmlElement(name = "Country_Id")
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@XmlElement(name="Primary_Country_Flag")
	public Boolean getIsPrimaryCountryFlag() {
		return isPrimaryCountryFlag;
	}

	public void setIsPrimaryCountryFlag(Boolean isPrimaryCountryFlag) {
		this.isPrimaryCountryFlag = isPrimaryCountryFlag;
	}
	
	
}
