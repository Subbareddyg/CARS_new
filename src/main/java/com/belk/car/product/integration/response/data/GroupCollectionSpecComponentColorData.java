package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the group collection spec component section.
 * Current Path in xml response: /getGroupResponse/Group_Catalog/pim_entry/entry/Collection_Spec/Component/Color.
 */
@XmlRootElement(name = "Color")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GroupCollectionSpecComponentColorData {
	
	private String id;
	
	private String code;
	
	private String isDefault;
	
	@XmlAttribute(name = "Id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}	
	
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
}
