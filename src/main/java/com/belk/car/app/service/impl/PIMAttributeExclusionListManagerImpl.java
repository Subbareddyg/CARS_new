/**
 * 
 */
package com.belk.car.app.service.impl;

import org.appfuse.service.impl.UniversalManagerImpl;

import com.belk.car.app.dao.PIMAttributeExclusionListDao;
import com.belk.car.app.service.PIMAttributeExclusionListManager;

/**
 * Implementation of methods defined in PIMAttributeExclusionListManager interface.
 * 
 * @author AFUSYS9
 *
 */
public class PIMAttributeExclusionListManagerImpl extends UniversalManagerImpl implements PIMAttributeExclusionListManager{

	private PIMAttributeExclusionListDao pimAttributeExclusionListDao;
	
	public PIMAttributeExclusionListDao getPimAttributeExclusionListDao() {
		return pimAttributeExclusionListDao;
	}

	public void setPimAttributeExclusionListDao(
			PIMAttributeExclusionListDao pimAttributeExclusionListDao) {
		this.pimAttributeExclusionListDao = pimAttributeExclusionListDao;
	}

	@Override
	public boolean isAttributeExcluded(long pimAttrId) {
		return pimAttributeExclusionListDao.isAttributeExcluded(pimAttrId);
	}

}