package com.belk.car.app.service;

import java.io.IOException;
import java.util.Set;

import com.belk.car.app.exceptions.CarJobDetailException;
import com.belk.car.app.model.VendorSku;


/**
 * 
 * @author Yogesh.Vedak
 *
 */
public interface SizeConversionJobManager {

	//Job #1
	public void resynchSizeValuesOfVendorSkus() throws IOException, CarJobDetailException;

	public void resynchSizeValuesOfVendorSkus(Set<VendorSku> skus);
	
}
