/**
 * 
 */
package com.belk.car.app.service;

import java.io.IOException;

import com.belk.car.app.exceptions.CarJobDetailException;


/**
 * @author afusy12
 *
 */
public interface QuartzJobmanagerForMCImage {
	
	/**
	 *  this Job reads the temporary media compass image XML file and loads the car id, sample id and image name to CARS.MEDIA_COMPASS_IMAGE table 
	 * @throws IOException
	 * @throws CarJobDetailException
	 */
	public void importMCTempImageNames() throws IOException, CarJobDetailException;
	
	/**
	 * This jobs reads the received status images from CARS.MEDIA_COMPASS_IMAGE table  and downloads the image from Media compass through RRD web-service
	 * @throws IOException
	 * @throws CarJobDetailException
	 */
	public void loadTempImagesFromMC() throws IOException, CarJobDetailException;
	
}
