package com.belk.car.app.service;

import java.util.Map;

import com.belk.car.app.model.Car;
import com.belk.car.product.integration.response.data.GroupIntegrationResponseData;
import com.belk.car.product.integration.response.data.IntegrationResponseData; 

/**
 * @author AFUTXD3
 * 
 */
public interface PIMFtpImageManager {

	public Map<String, IntegrationResponseData> uploadOrDeletePimImagesByCar(Car car);
	public Map<String, GroupIntegrationResponseData> uploadOrDeletePimImagesByCarForGroups(Car car);

}
