package com.belk.car.product.integration.response.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * POJO to hold the change information. 
 * Current Path in xml response: /getGroupResponse/Group_Catalog/pim_entry/group_entry_header/change_details.
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GroupChangeDetailsData {
	
	private List<String> attributePath;

	@XmlElement(name="changedAttributes")
	public List<String> getAttributePath() {
		return attributePath;
	}

	public void setAttributePath(List<String> attributePath) {
		this.attributePath = attributePath;
	}

}
