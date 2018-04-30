package com.belk.car.product.integration.response.data;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * POJO to hold the group pim entry information. 
 * Current Path in xml response: /getGroupResponse/Group_Catalog/pim_entry.
 */
@XmlRootElement(name = "pim_entry")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GroupPIMResponseData {

	private GroupHeaderData groupHeaderInformation;
	
	private GroupEntryDetailsData entries;
	
	private Map<String, List<AttributeData>> attributesInformation;
	
	@XmlElement(name="group_entry_header")
	public GroupHeaderData getGroupHeaderInformation() {
		return groupHeaderInformation;
	}

	public void setGroupHeaderInformation(GroupHeaderData groupHeaderInformation) {
		this.groupHeaderInformation = groupHeaderInformation;
	}

	@XmlElement(name="entry")
	public GroupEntryDetailsData getEntries() {
		return entries;
	}

	public void setEntries(GroupEntryDetailsData entries) {
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