package com.belk.car.product.integration.response.data;

import java.util.List;

public class SecondaryImageSpecs {
	
	private List<String> images;
	
	private List<String> deletedImages;
	
	private List<String> samples;
	
	private List<String> swatches;

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public List<String> getDeletedImages() {
		return deletedImages;
	}

	public void setDeletedImages(List<String> deletedImages) {
		this.deletedImages = deletedImages;
	}

	public List<String> getSamples() {
		return samples;
	}

	public void setSamples(List<String> samples) {
		this.samples = samples;
	}

	public List<String> getSwatches() {
		return swatches;
	}

	public void setSwatches(List<String> swatches) {
		this.swatches = swatches;
	}

}
