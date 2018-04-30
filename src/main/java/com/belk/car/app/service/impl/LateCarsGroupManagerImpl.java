/**
 * 
 */
package com.belk.car.app.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.service.impl.UniversalManagerImpl;

import com.belk.car.app.dao.LateCarsGroupDao;
import com.belk.car.app.exceptions.LateCarsGroupExistsException;
import com.belk.car.app.model.LateCarsGroup;
import com.belk.car.app.model.LateCarsParams;
import com.belk.car.app.service.LateCarsGroupManager;

/**
 * Implementation of the product group Manager.
 * 
 * @author AFUMXB4
 * 
 */
public class LateCarsGroupManagerImpl extends UniversalManagerImpl implements
		LateCarsGroupManager {

	private transient final Log log = LogFactory
			.getLog(LateCarsGroupManagerImpl.class);

	private LateCarsGroupDao lateCarsGroupDao;

	public void setLateCarsGroupDao(LateCarsGroupDao lateCarsGroupDao) {
		this.lateCarsGroupDao = lateCarsGroupDao;
	}

	public List<LateCarsGroup> getAllLateCarsGroups() {
		return lateCarsGroupDao.getAllLateCarsGroups();
	}

	/**
	 * Saves the product group. If the group already exists the Product group
	 * exists exception is thrown.
	 */
	public LateCarsGroup save(LateCarsGroup LateCarsGroup)
			throws LateCarsGroupExistsException {
		try {
			return lateCarsGroupDao.save(LateCarsGroup);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new LateCarsGroupExistsException(
					"Cannot Save the late car group -" + LateCarsGroup.getName());
		}
	}

	@Override
	public LateCarsGroup getLateCarsGroup(Long LateCarsGroupId) {

		return lateCarsGroupDao.getLateCarsGroup(LateCarsGroupId);
	}

	@Override
	public void updateLateCarsGroupRule(LateCarsParams lateCarsParams) {

		lateCarsGroupDao.updateLateCarsGroupRule(lateCarsParams);
	}

	@Override
	public LateCarsParams getLateCarsParams(Long lateCarsParamId) {

		return lateCarsGroupDao.getLateCarsParams(lateCarsParamId);
		
	}

	
	@Override
	public void remove(Long LateCarsGroupId) {

		lateCarsGroupDao.remove(LateCarsGroupId);
	}

	
	@Override
	public void removeLateCarsGroupRule(Long lateCarsParamId) {
		lateCarsGroupDao.removeLateCarsGroupRule(lateCarsParamId);

	}

	public List<LateCarsGroup> getLateCarsGroups(String lateCarsGroup) {
		return lateCarsGroupDao.getLateCarsGroups(lateCarsGroup);
	}
	
	public boolean CheckExistingLateCarsParams(long lateCarsParamId, String currentUserTypeCd, String workflowStatusCd, long lateCarsGroupId){
		return lateCarsGroupDao.CheckExistingLateCarsParams(lateCarsParamId, currentUserTypeCd, workflowStatusCd, lateCarsGroupId);
	}
	
	public boolean CheckExistingLateCarsParams(String currentUserTypeCd, String workflowStatusCd, long lateCarsGroupId){
		return lateCarsGroupDao.CheckExistingLateCarsParams(currentUserTypeCd, workflowStatusCd,  lateCarsGroupId);
	}
}
