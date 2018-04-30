package com.belk.car.product.integration.response.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * POJO to hold the change information. Usually this will be a collection object
 * under the item_header section in the response. 
 * Current Path in xml response: /getItemRespone/Item_Catalog/pim_entry/item_header/change_details.
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ChangeDetailsData {
	
	private List<String> attributePath;

	@XmlElement(name="changedAttributes")
	public List<String> getAttributePath() {
		return attributePath;
	}

	public void setAttributePath(List<String> attributePath) {
		this.attributePath = attributePath;
	}
	
	

}
