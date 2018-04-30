package com.belk.car.app.model.report;

import java.util.List;

import com.belk.car.app.model.view.CarPendingImagesView;

public class CarPendingImagesReport extends BaseReport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6359293176224620370L;

	private List<CarPendingImagesView> pendingImages ;

	public List<CarPendingImagesView> getPendingImages() {
		return pendingImages;
	}

	public void setPendingImages(List<CarPendingImagesView> pendingImages) {
		this.pendingImages = pendingImages;
	}

}
