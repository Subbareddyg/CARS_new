package com.belk.car.product.integration.response.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the Consolidated_Product_Spec.
 * Current Path in xml response: /getGroupResponse/Group_Catalog/pim_entry/entry/Consolidated_Product_Spec.
 */
@XmlRootElement(name = "Consolidated_Product_Spec")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GroupConsolidatedProductSpecData {
	
	private List<GroupConsolidatedProductSpecComponent> component;
	
	private String defaultSKUCode;

	@XmlElement(name = "Component")
	public List<GroupConsolidatedProductSpecComponent> getComponent() {
		return component;
	}

	public void setComponent(List<GroupConsolidatedProductSpecComponent> component) {
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
