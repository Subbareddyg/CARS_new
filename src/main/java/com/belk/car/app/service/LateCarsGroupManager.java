package com.belk.car.app.service;

import java.util.List;

import org.appfuse.service.UniversalManager;

import com.belk.car.app.exceptions.LateCarsGroupExistsException;
import com.belk.car.app.model.LateCarsGroup;
import com.belk.car.app.model.LateCarsParams;

/**
 * Service interface for Late Cars Group.
 * @author AFUMXB4
 *
 */
public interface LateCarsGroupManager extends UniversalManager{
	
	List<LateCarsGroup> getAllLateCarsGroups();

	LateCarsGroup getLateCarsGroup(Long LateCarsGroupId);

	LateCarsGroup save(LateCarsGroup LateCarsGroup) throws LateCarsGroupExistsException;
	
	void remove(Long LateCarsGroupId);
	
	void updateLateCarsGroupRule(LateCarsParams lateCarsParams);
	
	void removeLateCarsGroupRule(Long lateCarsParamId);
	
	LateCarsParams getLateCarsParams(Long lateCarsParamId);
	
	List<LateCarsGroup> getLateCarsGroups(String lateCarsGroup);
	
	public boolean CheckExistingLateCarsParams(long lateCarsParamId, String currentUserTypeCd, String workflowStatusCd, long lateCarsGroupId);
	public boolean CheckExistingLateCarsParams(String currentUserTypeCd, String workflowStatusCd, long lateCarsGroupId);
	
}
