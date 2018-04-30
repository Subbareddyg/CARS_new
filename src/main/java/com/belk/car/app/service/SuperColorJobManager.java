package com.belk.car.app.service;

import java.io.IOException;

import javax.naming.NamingException;

import com.belk.car.app.exceptions.CarJobDetailException;
import com.belk.car.app.exceptions.SendEmailException;

/**
 * 
 * @author Yogesh.Vedak
 *
 */
public interface SuperColorJobManager {

	//Job #1
	public void resynchSuperColor1OfVendorSkus() throws IOException, CarJobDetailException;

	
	
}
