package com.belk.car.product.integration.response.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the group consolidated spec component information.
 * Current Path in xml response: /getGroupResponse/Group_Catalog/pim_entry/entry/Consolidated_Product_Spec/Component.
 */
@XmlRootElement(name = "Component")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GroupConsolidatedProductSpecComponent {
	
	private String id;
	
	private String displayOrder;
	
	private String isDefault;
	
	private String type;
	
	private List<GroupConsolidatedProductSpecComponentColorData> color;
	
	@XmlElement(name = "Id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@XmlElement(name = "Display_Order")
	public String getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	@XmlElement(name = "Default")
	public String getDefault() {
		return isDefault;
	}

	public void setDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	
	@XmlElement(name = "Type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@XmlElement(name = "Color")
	public List<GroupConsolidatedProductSpecComponentColorData> getColor() {
		return color;
	}

	public void setColor(List<GroupConsolidatedProductSpecComponentColorData> color) {
		this.color = color;
	}
}
