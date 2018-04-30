package com.belk.car.app.service;

import java.io.IOException;
import java.text.ParseException;

import javax.naming.NamingException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.belk.car.app.exceptions.CarJobDetailException;

public interface QuartzJobManagerForVendorCatalog {

	//Job #18 Imports Vendor Catalog Data
	public void importVendorCatalogData() throws IOException, CarJobDetailException, NamingException, ParseException, SAXException, ParserConfigurationException;
	
	//Job #17 Load Catalog Data into existing CARS
	public void loadVendorCatalogDataIntoCARS() throws IOException, CarJobDetailException, NamingException, Exception;

	
	
	
}
