package com.belk.car.product.integration.response.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the Image_Sec_Spec.
 * Current Path in xml response: /getGroupResponse/Group_Catalog/pim_entry/entry/Image_Sec_Spec.
 */
@XmlRootElement(name = "Image_Sec_Spec")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GroupImageSecSpecData {
	
	private List<GroupImageSecSpecImagesData> images;
	
	private List<GroupImageSecSpecDeletedImagesData> deletedImages;
	
	private List<GroupImageSecSpecScene7ImagesData> scene7Images;
	
	@XmlElement(name = "Images")
	public List<GroupImageSecSpecImagesData> getImages() {
		return images;
	}

	public void setImages(List<GroupImageSecSpecImagesData> images) {
		this.images = images;
	}
	
	@XmlElement(name = "Deleted_Images")
	public List<GroupImageSecSpecDeletedImagesData> getDeletedImages() {
		return deletedImages;
	}

	public void setDeletedImages(List<GroupImageSecSpecDeletedImagesData> deletedImages) {
		this.deletedImages = deletedImages;
	}
	
	@XmlElement(name = "Scene7_Images")
	public List<GroupImageSecSpecScene7ImagesData> getScene7Images() {
		return scene7Images;
	}

	public void setScene7Images(List<GroupImageSecSpecScene7ImagesData> scene7Images) {
		this.scene7Images = scene7Images;
	}
}
