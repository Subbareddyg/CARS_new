package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the item category description information. 
 * Current Path in xml response: /getItemRespone/Item_Catalog/pim_entry/entry/Item_Ctg_Spec/Description/.
 */
@XmlRootElement(name = "Description")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ItemDescriptionData {
	
	private String descriptionCode;
	
	private String longDescription;
	
	private String secondayDescription;
	
	private String shortDescription;

	@XmlAttribute(name = "occ")
	public String getDescriptionCode() {
		return descriptionCode;
	}

	public void setDescriptionCode(String descriptionCode) {
		this.descriptionCode = descriptionCode;
	}

	@XmlElement(name = "Long")
	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	@XmlElement(name = "Secondary")
	public String getSecondayDescription() {
		return secondayDescription;
	}

	public void setSecondayDescription(String secondayDescription) {
		this.secondayDescription = secondayDescription;
	}

	@XmlElement(name = "Short")
	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
	

}
