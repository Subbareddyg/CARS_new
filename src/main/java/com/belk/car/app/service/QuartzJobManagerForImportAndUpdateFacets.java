/**
 * 
 */
package com.belk.car.app.service;

import java.io.IOException;

import com.belk.car.app.exceptions.CarJobDetailException;


/**
 * @author afuszm1
 *
 */
public interface QuartzJobManagerForImportAndUpdateFacets {
	
	/**
	 *  This job is used to update the cars attributes values with the values send by 3rd Party.
	 * @throws IOException
	 * @throws CarJobDetailException
	 */
	public void importAndUpdateCarFacetAttibutes() throws IOException, CarJobDetailException;
}
