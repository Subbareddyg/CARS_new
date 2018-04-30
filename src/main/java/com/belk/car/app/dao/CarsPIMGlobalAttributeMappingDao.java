package com.belk.car.app.dao;

import java.util.List;

import com.belk.car.app.model.CarsPIMGlobalAttributeMapping;

public interface CarsPIMGlobalAttributeMappingDao extends CachedQueryDao{

	/**
	 * Method to find the global attribute mapping.
	 * 
	 * @param pimAttrId
	 * @return
	 */
	public List<Object[]> getPIMGlobalAttrMappingById(long pimAttrId);
	
	public List<CarsPIMGlobalAttributeMapping> getAllCarsPimGlobalAttributes();
}
