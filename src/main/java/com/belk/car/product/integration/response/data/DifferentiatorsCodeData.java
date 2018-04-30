package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the item spec differentiators section. Usually this will be a collection object
 * under the entry section in the response. 
 * Current Path in xml response: /getItemRespone/Item_Catalog/pim_entry/entry/{item_spec}/change_details.
 * The {item_spec} wil vary from the input request.
 */
@XmlRootElement(name = "Codes")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DifferentiatorsCodeData {
	
	private String codeIndicator;
	
	private String code;
	
	private String vendorDescription;
	
	private String codeFamily;
	
	private String styleDifferentiatorId;
	
	private String idbId;

	@XmlAttribute(name = "occ")
	public String getCodeIndicator() {
		return codeIndicator;
	}

	public void setCodeIndicator(String codeIndicator) {
		this.codeIndicator = codeIndicator;
	}

	@XmlElement(name = "Code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@XmlElement(name = "Vendor_Description")
	public String getVendorDescription() {
		return vendorDescription;
	}

	public void setVendorDescription(String vendorDescription) {
		this.vendorDescription = vendorDescription;
	}

	@XmlElement(name = "Family")
	public String getCodeFamily() {
		return codeFamily;
	}

	public void setCodeFamily(String codeFamily) {
		this.codeFamily = codeFamily;
	}

	@XmlElement(name = "Style_Diff_Id")
	public String getStyleDifferentiatorId() {
		return styleDifferentiatorId;
	}

	public void setStyleDifferentiatorId(String styleDifferentiatorId) {
		this.styleDifferentiatorId = styleDifferentiatorId;
	}

	@XmlElement(name = "IDB_Id")
	public String getIdbId() {
		return idbId;
	}

	public void setIdbId(String idbId) {
		this.idbId = idbId;
	}
	
	

}
