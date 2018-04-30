package com.belk.car.product.integration.response.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the specific Item_Style_Spec in the Style request.
 * Current Path in xml response: /getItemRespone/Item_Catalog/pim_entry/entry/Item_Style_Spec.
 */
@XmlRootElement(name = "Item_Style_Spec")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ItemStyleSpecData {
	
	private List<ItemDifferentiatorsData> differentiators;

	@XmlElement(name = "Differentiators")
	public List<ItemDifferentiatorsData> getDifferentiators() {
		return differentiators;
	}

	public void setDifferentiators(List<ItemDifferentiatorsData> differentiators) {
		this.differentiators = differentiators;
	}
	
	

}
