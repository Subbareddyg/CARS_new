package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the group split color spec component information.
 * Current Path in xml response: /getGroupResponse/Group_Catalog/pim_entry/entry/Split_Color_Spec/Component.
 */
@XmlRootElement(name = "Component")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GroupSplitColorSpecComponent {
	
	private String id;
	
	private String colorCode;
	
	private String isDefault;
	
	private String description;
	
	@XmlElement(name = "Id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@XmlElement(name = "Color_Code")
	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	
	@XmlElement(name = "Default")
	public String getDefault() {
		return isDefault;
	}

	public void setDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	
	@XmlElement(name = "Description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
