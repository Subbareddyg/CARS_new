package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the Catalog information. 
 * Current Path in xml response: /getItemRespone/Item_Catalog/.
 */
@XmlRootElement(name = "Item_Catalog")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ItemCatalogData {
	
	private Integer requestId;
	
	private String responseType;

	private PIMEntryInformation pimEntry;

	@XmlElement(name = "pim_entry")
	public PIMEntryInformation getPimEntry() {
		return pimEntry;
	}

	public void setPimEntry(PIMEntryInformation pimEntry) {
		this.pimEntry = pimEntry;
	}

	@XmlAttribute(name = "type")
	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	@XmlAttribute(name = "id")
	public Integer getRequestId() {
		return requestId;
	}
	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}
	
	
	
	
	
}
