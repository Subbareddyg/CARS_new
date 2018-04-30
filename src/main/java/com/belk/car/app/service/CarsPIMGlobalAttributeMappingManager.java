package com.belk.car.app.service;

import java.util.List;

import org.appfuse.service.UniversalManager;

import com.belk.car.app.model.CarsPIMGlobalAttributeMapping;

/**
 * Business Service Interface to talk to persistence layer and retrieve
 * global mapping attribute.
 * 
 * @author AFUSYS9
 *
 */
public interface CarsPIMGlobalAttributeMappingManager extends UniversalManager{

	/**
	 * Retrieve global attribute mapping for a given pim_attr_id.
	 * 
	 * @param pimAttrId
	 * @return
	 */
	public List<Object[]> getPIMGlobalAttrMappingById(long pimAttrId);
	
	/**
	 * Method to retrieve all global cars pim attributes.
	 * 
	 * @return
	 */
    public List<CarsPIMGlobalAttributeMapping> getAllCarsPimGlobalAttributes();
}
