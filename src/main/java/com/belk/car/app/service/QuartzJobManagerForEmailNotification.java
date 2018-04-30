package com.belk.car.app.service;

import com.belk.car.app.exceptions.CarJobDetailException;
import com.belk.car.app.exceptions.SendEmailException;

/**
 * @author afurxd2
 *
 */
public interface QuartzJobManagerForEmailNotification {
	
	public void sendVendorImageRejectionEmailNotifitcation() throws SendEmailException, CarJobDetailException;
}