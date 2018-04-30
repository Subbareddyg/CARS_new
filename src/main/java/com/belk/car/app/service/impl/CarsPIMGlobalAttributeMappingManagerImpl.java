package com.belk.car.app.service.impl;

import java.util.List;

import org.appfuse.service.impl.UniversalManagerImpl;

import com.belk.car.app.dao.CarsPIMGlobalAttributeMappingDao;
import com.belk.car.app.model.CarsPIMGlobalAttributeMapping;
import com.belk.car.app.service.CarsPIMGlobalAttributeMappingManager;

public class CarsPIMGlobalAttributeMappingManagerImpl extends UniversalManagerImpl implements CarsPIMGlobalAttributeMappingManager{
 
	private CarsPIMGlobalAttributeMappingDao carsPIMGlobalAttributeMappingDao;
	
	public CarsPIMGlobalAttributeMappingDao getCarsPIMGlobalAttributeMappingDao() {
		return carsPIMGlobalAttributeMappingDao;
	}

	public void setCarsPIMGlobalAttributeMappingDao(CarsPIMGlobalAttributeMappingDao carsPIMGlobalAttributeMappingDao){
		this.carsPIMGlobalAttributeMappingDao = carsPIMGlobalAttributeMappingDao;
	}
	

	@Override
	public List<Object[]> getPIMGlobalAttrMappingById(long pimAttrId) {

		return carsPIMGlobalAttributeMappingDao.getPIMGlobalAttrMappingById(pimAttrId);
	}
	
	public List<CarsPIMGlobalAttributeMapping> getAllCarsPimGlobalAttributes(){
	    return carsPIMGlobalAttributeMappingDao.getAllCarsPimGlobalAttributes();
	}

}
