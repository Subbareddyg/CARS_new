package com.belk.car.product.integration.response.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the Split_SKU_Spec.
 * Current Path in xml response: /getGroupResponse/Group_Catalog/pim_entry/entry/Split_SKU_Spec.
 */
@XmlRootElement(name = "Split_SKU_Spec")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GroupSplitSKUSpecData {
	
	private String parentStyleId;
	
	private String type;
	
	private List<GroupSplitSKUSpecComponent> component;
	
	private String defaultSKUCode;
	
	@XmlElement(name = "Parent_Style_Id")
	public String getParentStyleId() {
		return parentStyleId;
	}

	public void setParentStyleId(String parentStyleId) {
		this.parentStyleId = parentStyleId;
	}
	
	@XmlElement(name = "Type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlElement(name = "Component")
	public List<GroupSplitSKUSpecComponent> getComponent() {
		return component;
	}

	public void setComponent(List<GroupSplitSKUSpecComponent> component) {
		this.component = component;
	}
	
	@XmlElement(name = "Default_SKU_Code")
	public String getDefaultSKUCode() {
		return defaultSKUCode;
	}

	public void setDefaultSKUCode(String defaultSKUCode) {
		this.defaultSKUCode = defaultSKUCode;
	}
}
