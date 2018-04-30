package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Deleted_Images")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ItemDeletedImagesData {
	private long pimImageId;
	private String imgFileName;
	@XmlElement(name = "Image_ID")
	public long getPimImageId() {
		return pimImageId;
	}
	public void setPimImageId(long pimImageId) {
		this.pimImageId = pimImageId;
	}
	@XmlElement(name = "Image_File_Name")
	public String getImgFileName() {
		return imgFileName;
	}
	public void setImgFileName(String imgFileName) {
		this.imgFileName = imgFileName;
	}

}
