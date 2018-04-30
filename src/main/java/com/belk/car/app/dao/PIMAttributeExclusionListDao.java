package com.belk.car.app.dao;

public interface PIMAttributeExclusionListDao extends CachedQueryDao{

	/**
	 * Method to check if the attribute is excluded.
	 * 
	 * @param pimAttrId
	 * @return
	 */
	public boolean isAttributeExcluded(long pimAttrId);
}
