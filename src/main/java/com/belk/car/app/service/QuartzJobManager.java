package com.belk.car.app.service;

import java.io.IOException;

import javax.naming.NamingException;

import com.belk.car.app.exceptions.CarJobDetailException;
import com.belk.car.app.exceptions.SendEmailException;
import com.belk.car.app.exceptions.UserRankException;

public interface QuartzJobManager {

	//Job #1
	public void exportManualCarsToFile() throws IOException, CarJobDetailException;

	//Job #2
	public void importCars() throws IOException, CarJobDetailException, NamingException;

	//Job #3 & #4 -- Set isVendor flag to true if you want to retrieve all of the notifications for users of type 'Vendor'
	public void sendUserCarNotification() throws SendEmailException, CarJobDetailException;

	public void sendVendorCarNotification() throws SendEmailException, CarJobDetailException;

	//Job #5 - Get all of the vendor cars for a defined timeframe
	public void sendVendorCarEscalationList() throws SendEmailException, CarJobDetailException;

	//Job #6 - Get all of the vendor sample for a defined timeframe
	public void sendVendorSampleEscalationList() throws SendEmailException, CarJobDetailException;

	//Job #7 - Import RLR Information
	public void importRLR() throws IOException, CarJobDetailException, NamingException;

	//Job #8 - Export RRD Information
	public void exportRRDFeeds() throws CarJobDetailException;

	//Job #9 - Import RRD Information
	public void importRRDFeeds() throws CarJobDetailException;

	//Job #10 - Export CMP Information
	public void exportCESInfoWithContent() throws IOException, CarJobDetailException ;

	//Job #10 - Export CMP Information
	public void exportCESInfoWithoutContent() throws IOException, CarJobDetailException ;

	//Job #11 - Close Completed CAR Information
	public void closeCars() throws IOException, CarJobDetailException ;

	//Job #12 - Close Completed CAR Information
	public void exportHexValues() throws IOException, CarJobDetailException ;
	
	//Job #13 - Export Trends Data To BMI
	public void exportProductsInTrend() throws IOException, CarJobDetailException ;
	
	//Job #24 - Update Latest Data From BEL_INV_AVAIL to TMP table in CARS Schema
	public void updateTmpWithBel_Inv_Avail() throws IOException, CarJobDetailException ;
	
	//job #25 - unlock all the cars
	public void unlockAllCars()  throws Exception;
	
	//job #26 - updateAutomaticallyApproveImage
	public void updateAutomaticallyApproveImage() throws Exception;
	
	public void exportPWPandGWPAttributes() throws CarJobDetailException,IOException;

	//Job #27
	public void resynchAttributes() throws IOException;
	
	//job #32 - Oracle job to import user rank table
	public void importUserRankData() throws IOException, UserRankException,NamingException;

}
