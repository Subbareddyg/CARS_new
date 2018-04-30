package com.belk.car.app.service;

import java.io.IOException;

import javax.naming.NamingException;

import com.belk.car.app.exceptions.CarJobDetailException;

public interface QuartzJobManagerForDropship {

	//Job #14 Imports IDB Feed For Dropship
	public void importIDBFeedForDropship() throws IOException, CarJobDetailException, NamingException, Exception;
	
	
	//Job #15 Imports IDB Cross Reference Feed For Dropship
	public void importIDBCrossReferenceFeedForDropship() throws IOException, CarJobDetailException, NamingException, Exception;
	
	//Job #16 Imports IDB Purge Feed For Dropship
	public void importIDBPurgeFeedForDropship() throws IOException,CarJobDetailException, NamingException, Exception;
	
	public void importIDBCrossReferenceFeedForDropshipForCarCreation() throws Exception;

   public void importDropshipCars() throws IOException, CarJobDetailException, NamingException;

   public void prefIllPatternStyleCar() throws Exception;
   
   // Vendor Expedited shipping to Blue Martini 
   public void exportVendorExpeditedShipping() throws IOException, CarJobDetailException;
}
