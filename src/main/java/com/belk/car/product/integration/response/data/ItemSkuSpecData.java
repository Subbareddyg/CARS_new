package com.belk.car.product.integration.response.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the specific item_sku_spec in the SKU request.
 * Current Path in xml response: /getItemRespone/Item_Catalog/pim_entry/entry/Item_SKU_Spec.
 */
@XmlRootElement(name = "Item_SKU_Spec")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ItemSkuSpecData {
	
	private String styleId;
	
	private String containerId;
	
	private List<ItemDifferentiatorsData> differentiators;

	@XmlElement(name = "Differentiators")
	public List<ItemDifferentiatorsData> getDifferentiators() {
		return differentiators;
	}

	public void setDifferentiators(List<ItemDifferentiatorsData> differentiators) {
		this.differentiators = differentiators;
	}

	@XmlElement(name = "Style_Id")
	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	@XmlAttribute(name = "Container")
	public String getContainerId() {
		return containerId;
	}

	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}
	
	

}
