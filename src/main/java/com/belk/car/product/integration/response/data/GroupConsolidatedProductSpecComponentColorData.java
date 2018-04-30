package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the group consolidated spec component section.
 * Current Path in xml response: /getGroupResponse/Group_Catalog/pim_entry/entry/Consolidated_Product_Spec/Component/Color.
 */
@XmlRootElement(name = "Color")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GroupConsolidatedProductSpecComponentColorData {
	
	private String code;
	
	private String isDefault;
	
	private String description;
	
	@XmlAttribute(name = "Code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@XmlAttribute(name = "Default")
	public String getDefault() {
		return isDefault;
	}

	public void setDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	
	@XmlAttribute(name = "Description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
