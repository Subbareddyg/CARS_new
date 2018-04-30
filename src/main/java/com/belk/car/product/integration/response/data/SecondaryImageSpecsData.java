package com.belk.car.product.integration.response.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "Image_Sec_Spec")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class SecondaryImageSpecsData {
	
	private List<ItemImagesData> images;
	
	private List<ItemDeletedImagesData> deletedImages;
		
	private List<ItemSwatchImagesData> swatches;
	@XmlElement(name = "Images")
	public List<ItemImagesData> getImages() {
		return images;
	}

	public void setImages(List<ItemImagesData> images) {
		this.images = images;
	}
	@XmlElement(name = "Deleted_Images")
	public List<ItemDeletedImagesData> getDeletedImages() {
		return deletedImages;
	}

	public void setDeletedImages(List<ItemDeletedImagesData> deletedImages) {
		this.deletedImages = deletedImages;
	}

	@XmlElement(name = "Swatch")
	public List<ItemSwatchImagesData> getSwatches() {
		return swatches;
	}

	public void setSwatches(List<ItemSwatchImagesData> swatches) {
		this.swatches = swatches;
	}

}
