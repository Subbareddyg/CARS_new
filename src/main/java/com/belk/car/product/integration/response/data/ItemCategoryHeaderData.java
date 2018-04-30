package com.belk.car.product.integration.response.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * POJO to hold the item header category information. 
 * Current Path in xml response: /getItemRespone/Item_Catalog/pim_entry/item_header/category_paths.
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ItemCategoryHeaderData {
	
	private List<CategoryHeaderData> categoryInformation;

	@XmlElement(name="category")
	public List<CategoryHeaderData> getCategoryInformation() {
		return categoryInformation;
	}

	public void setCategoryInformation(List<CategoryHeaderData> categoryInformation) {
		this.categoryInformation = categoryInformation;
	}
	
	
	
	

}
