package com.belk.car.app.dao;

import java.util.List;

import org.appfuse.dao.UniversalDao;

import com.belk.car.app.model.LateCarsGroup;
import com.belk.car.app.model.LateCarsParams;

/**
 * Model interface for Late Cars Group.
 * @author AFUMXB4
 *
 */

public interface LateCarsGroupDao extends UniversalDao {

	List<LateCarsGroup> getAllLateCarsGroups();

	LateCarsGroup getLateCarsGroup(Long LateCarsGroupId);

	LateCarsGroup save(LateCarsGroup lateCarsGroup);

	void remove(Long lateCarsGroupId);
	
	void updateLateCarsGroupRule(LateCarsParams lateCarsParams);
	
	void removeLateCarsGroupRule(Long lateCarsParamsId);
	
	List<LateCarsGroup> getLateCarsGroups(String lateCarsGroup);

	LateCarsParams getLateCarsParams(Long lateCarsParamId);
	
	public boolean CheckExistingLateCarsParams(long lateCarsParamId, String currentUserTypeCd, String workflowStatusCd, long lateCarsGroupId);
	
	public boolean CheckExistingLateCarsParams( String currentUserTypeCd, String workflowStatusCd, long lateCarsGroupId);
	
}
