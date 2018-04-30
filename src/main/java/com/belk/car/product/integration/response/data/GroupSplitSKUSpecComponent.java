package com.belk.car.product.integration.response.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the group split SKU spec component information.
 * Current Path in xml response: /getGroupResponse/Group_Catalog/pim_entry/entry/Split_SKU_Spec/Component.
 */
@XmlRootElement(name = "Component")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GroupSplitSKUSpecComponent {
	
	private List<GroupSplitSKUSpecComponentColorData> color;
	
	@XmlElement(name = "Color")
	public List<GroupSplitSKUSpecComponentColorData> getColor() {
		return color;
	}

	public void setColor(List<GroupSplitSKUSpecComponentColorData> color) {
		this.color = color;
	}


}
