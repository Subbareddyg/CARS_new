package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the supplier's UPC information. 
 * Current Path in xml response: /getItemRespone/Item_Catalog/pim_entry/entry/Item_Ctg_Spec/Supplier/UPCs.
 */
@XmlRootElement(name = "UPCs")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class SupplierUPCData {
	
	private String supplierCode;
	
	private String upc;
	
	private String type;
	
	private Boolean isPrimaryFlag;
	
	private String discontinuedDate;

	@XmlAttribute(name = "occ")
	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	@XmlElement(name = "UPC")
	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	@XmlElement(name = "Type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlElement(name = "Primary_Flag")
	public Boolean getIsPrimaryFlag() {
		return isPrimaryFlag;
	}

	public void setIsPrimaryFlag(Boolean isPrimaryFlag) {
		this.isPrimaryFlag = isPrimaryFlag;
	}

	@XmlElement(name = "Discontinue_Date")
	public String getDiscontinuedDate() {
		return discontinuedDate;
	}

	public void setDiscontinuedDate(String discontinuedDate) {
		this.discontinuedDate = discontinuedDate;
	}

}
