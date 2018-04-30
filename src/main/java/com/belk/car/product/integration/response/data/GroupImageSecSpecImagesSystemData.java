package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the group image spec component section.
 * Current Path in xml response: /getGroupResponse/Group_Catalog/pim_entry/entry/Image_Sec_Spec/Images/System.
 */
@XmlRootElement(name = "System")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GroupImageSecSpecImagesSystemData {
	
	private String ownerType;
	
	private String ownerID;
	
	@XmlElement(name = "Owner_Type")
	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	
	@XmlElement(name = "Owner_ID")
	public String getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}
}
