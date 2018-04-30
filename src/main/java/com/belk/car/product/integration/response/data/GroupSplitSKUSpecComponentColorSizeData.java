package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the group split SKU spec component section.
 * Current Path in xml response: /getGroupResponse/Group_Catalog/pim_entry/entry/Split_SKU_Spec/Component/Color/Size.
 */
@XmlRootElement(name = "Size")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GroupSplitSKUSpecComponentColorSizeData {
	
	private String id;
	
	private String isDefault;
	
	private String code;	
	
	private String description;

	@XmlAttribute(name = "Id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}	
	
	@XmlAttribute(name = "Default")
	public String getDefault() {
		return isDefault;
	}

	public void setDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	
	@XmlAttribute(name = "Code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@XmlAttribute(name = "Description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
