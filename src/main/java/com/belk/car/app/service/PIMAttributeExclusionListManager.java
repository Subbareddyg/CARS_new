package com.belk.car.app.service;

import org.appfuse.service.UniversalManager;
/**
 * Business interface to check if the PIM attribute id is excluded.
 * 
 * @author AFUSYS9
 *
 */
public interface PIMAttributeExclusionListManager extends UniversalManager{
	
	/**
	 * Checks if the attribute ID is excluded or not.
	 * 
	 * @param pimAttrId
	 * @return
	 */
	public boolean isAttributeExcluded(long pimAttrId);

}
