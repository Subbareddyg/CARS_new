package com.belk.car.product.integration.response.data;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * POJO to hold the pim entry information. 
 * Current Path in xml response: /getItemRespone/Item_Catalog/pim_entry.
 */
@XmlRootElement(name = "pim_entry")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class PIMResponseData {

	private ItemHeaderData itemHeaderInformation;
	
	private EntryDetailsData entries;
	
	private Map<String, List<AttributeData>> attributesInformation;
	
	@XmlElement(name="item_header")
	public ItemHeaderData getItemHeaderInformation() {
		return itemHeaderInformation;
	}

	public void setItemHeaderInformation(ItemHeaderData itemHeaderInformation) {
		this.itemHeaderInformation = itemHeaderInformation;
	}

	@XmlElement(name="entry")
	public EntryDetailsData getEntries() {
		return entries;
	}

	public void setEntries(EntryDetailsData entries) {
		this.entries = entries;
	}

	@XmlTransient
	public Map<String, List<AttributeData>> getAttributesInformation() {
		return attributesInformation;
	}

	public void setAttributesInformation(
			Map<String, List<AttributeData>> attributesInformation) {
		this.attributesInformation = attributesInformation;
	}
}
