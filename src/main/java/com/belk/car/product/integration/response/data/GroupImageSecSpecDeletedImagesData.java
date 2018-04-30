package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the group image spec component information.
 * Current Path in xml response: /getGroupResponse/Group_Catalog/pim_entry/entry/Image_Sec_Spec/Deleted_Images.
 */
@XmlRootElement(name = "Deleted_Images")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GroupImageSecSpecDeletedImagesData {
	
	private long imageID;
	
	private String imageFileName;
	
	@XmlElement(name = "Image_ID")
	public long getPimImageId() {
		return imageID;
	}

	public void setPimImageId(long imageID) {
		this.imageID = imageID;
	}
	
	@XmlElement(name = "Image_File_Name")
	public String getImgFileName() {
		return imageFileName;
	}

	public void setImgFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}
}
