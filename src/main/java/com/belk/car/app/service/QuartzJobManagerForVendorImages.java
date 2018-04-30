package com.belk.car.app.service;

/**
 * @author AFUSY12
 * Quartz job manager for vendor images
 */
public interface QuartzJobManagerForVendorImages {
	
	public void sendVendorImagesFeedForMCToRRD();
	public void sendVendorImagesUpdateFeedToRRD();
	public void importVendorImageHistoryFeed();
	public void importVendorImageCheckFeedbackFromRRD();

}
