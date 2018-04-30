package com.belk.car.app.service;


/**
 * Purpose: Batch job to import IDB Feed Vendor Drop Ship data and create CARS
 * 
 * Initial Project: EC-2009-00092 - FY 10 eCommerce Drop-Ship Phase I
 * 
 * Initial Requirements: BR.012.002 (see also BR.007)
 *
 * @author afudxm2
 *
 */

public interface IDBDropShipToCarsJobManager {

	public void importIDBDropshipForCars() throws Exception;

}
