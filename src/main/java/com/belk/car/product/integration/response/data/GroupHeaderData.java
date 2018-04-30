package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the group header information. 
 * Current Path in xml response: /getGroupResponse/Group_Catalog/pim_entry/group_entry_header.
 */
@XmlRootElement(name = "group_entry_header")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GroupHeaderData {
	
	private Long primaryKey;
	
	private String parentInformation;
	
	private String objectType;
	
	private GroupCategoryHeaderData categoryPaths;
	
	private GroupChangeDetailsData changeInformation;

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
	public GroupCategoryHeaderData getCategoryPaths() {
		return categoryPaths;
	}

	public void setCategoryPaths(GroupCategoryHeaderData categoryPaths) {
		this.categoryPaths = categoryPaths;
	}

	@XmlElement(name = "change_details")
	public GroupChangeDetailsData getChangeInformation() {
		return changeInformation;
	}

	public void setChangeInformation(
			GroupChangeDetailsData changeInformation) {
		this.changeInformation = changeInformation;
	}

}
