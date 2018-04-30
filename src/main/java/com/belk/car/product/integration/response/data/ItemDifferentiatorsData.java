package com.belk.car.product.integration.response.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the item specific spec differentiators information. Usually this will be a collection object
 * under the specific item section spec in the response. 
 * Current Path in xml response: /getItemRespone/Item_Catalog/pim_entry/entry/{Item_Spec}/Differentiators.
 */
@XmlRootElement(name = "Differentiators")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ItemDifferentiatorsData {
	
	private String differentiatorsCode;
	
	private String differentiatorsType;
	
	private List<DifferentiatorsCodeData> codes;
	
	private String differentiatorsIndicator;
	
	private String vendorDescription;

	@XmlAttribute(name = "occ")
	public String getDifferentiatorsCode() {
		return differentiatorsCode;
	}

	public void setDifferentiatorsCode(String differentiatorsCode) {
		this.differentiatorsCode = differentiatorsCode;
	}

	@XmlElement(name = "Type")
	public String getDifferentiatorsType() {
		return differentiatorsType;
	}

	public void setDifferentiatorsType(String differentiatorsType) {
		this.differentiatorsType = differentiatorsType;
	}

	@XmlElement(name = "Codes")
	public List<DifferentiatorsCodeData> getCodes() {
		return codes;
	}

	public void setCodes(List<DifferentiatorsCodeData> codes) {
		this.codes = codes;
	}

	@XmlElement(name = "Code")
	public String getDifferentiatorsIndicator() {
		return differentiatorsIndicator;
	}

	public void setDifferentiatorsIndicator(String differentiatorsIndicator) {
		this.differentiatorsIndicator = differentiatorsIndicator;
	}

	@XmlElement(name = "Vendor_Description")
	public String getVendorDescription() {
		return vendorDescription;
	}

	public void setVendorDescription(String vendorDescription) {
		this.vendorDescription = vendorDescription;
	}
	
	

}
