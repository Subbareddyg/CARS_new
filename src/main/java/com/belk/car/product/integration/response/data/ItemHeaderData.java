package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * POJO to hold the item header information. 
 * Current Path in xml response: /getItemRespone/Item_Catalog/pim_entry/item_header.
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ItemHeaderData {
	
	private Long primaryKey;
	
	private String parentInformation;
	
	private String objectType;
	
	private ItemCategoryHeaderData categoryPaths;
	
	private ChangeDetailsData changeInformation;

	@XmlElement(name="pk")
	public Long getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

	@XmlElement(name="parent")
	public String getParentInformation() {
		return parentInformation;
	}

	public void setParentInformation(String parentInformation) {
		this.parentInformation = parentInformation;
	}

	@XmlElement(name="object_type")
	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	@XmlElement(name = "category_paths")
	public ItemCategoryHeaderData getCategoryPaths() {
		return categoryPaths;
	}

	public void setCategoryPaths(ItemCategoryHeaderData categoryPaths) {
		this.categoryPaths = categoryPaths;
	}

	@XmlElement(name = "change_details")
	public ChangeDetailsData getChangeInformation() {
		return changeInformation;
	}

	public void setChangeInformation(
			ChangeDetailsData changeInformation) {
		this.changeInformation = changeInformation;
	}
	
	

}
