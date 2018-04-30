package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlElement;

/**
 * POJO to hold the item header category information. 
 * Current Path in xml response: /getItemRespone/Item_Catalog/pim_entry/item_header/category_paths
 *
 */
public class CategoryHeaderData {
	
    private String primaryKey;
	
	private String categoryPath;

	@XmlElement(name="pk")
	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	@XmlElement(name="path")
	public String getCategoryPath() {
		return categoryPath;
	}

	public void setCategoryPath(String categoryPath) {
		this.categoryPath = categoryPath;
	}

}
