package com.belk.car.app.dao.hibernate;

import java.util.List;

import org.appfuse.dao.hibernate.UniversalDaoHibernate;

import com.belk.car.app.dao.LateCarsGroupDao;
import com.belk.car.app.model.LateCarsGroup;
import com.belk.car.app.model.LateCarsParams;

/**
 * Implementation Model class for the late cars group
 * 
 * @author AFUMXB4 : Mallikarjun Bhaigond
 * 
 */

public class LateCarsGroupDaoHibernate extends UniversalDaoHibernate implements LateCarsGroupDao {
	
	/**
	 * This method is used to retrieve the all late cars groups.
	 * @return list.
	 * */
	@SuppressWarnings("unchecked")
	public List<LateCarsGroup> getAllLateCarsGroups() {
		return getHibernateTemplate().find("from LateCarsGroup");
	}
	
	
	
	/**
	 * This method returns the Product Group Object from the product group ID.
	 * @param Long lateCarsGroupId.
	 * @return LateCarsGroup lateCarsGroup.
	 * */
	public LateCarsGroup getLateCarsGroup(Long lateCarsGroupId) {
		return (LateCarsGroup) getHibernateTemplate().get(LateCarsGroup.class, lateCarsGroupId);
	}
	
	/**
	 * This method save the product Group.
	 * @param LateCarsGroup
	 * @return LateCarsGroup
	 * */
	public LateCarsGroup save(LateCarsGroup lateCarsGroup) {
		getHibernateTemplate().saveOrUpdate(lateCarsGroup);
		getHibernateTemplate().flush();
		return lateCarsGroup;
	}
	/**
	 * This method delete the Late Cars Group record
	 * @param LateCarsGroup.	 
	 */
	public void remove(Long lateCarsGroupId) {
		
		getHibernateTemplate().delete(getLateCarsGroup(lateCarsGroupId));
		getHibernateTemplate().flush();
	}
		
	public void updateLateCarsGroupRule(LateCarsParams lateCarsParams)
	{
		getHibernateTemplate().saveOrUpdate(lateCarsParams);
		getHibernateTemplate().flush();
	}

	/**
	 * This method returns the Product Group Object from the product group ID.
	 * @param Long lateCarsGroupId.
	 * @return LateCarsGroup lateCarsGroup.
	 * */
	public LateCarsParams getLateCarsGroupRule(Long lateCarsParamsId) {
		return (LateCarsParams) getHibernateTemplate().get(LateCarsParams.class, lateCarsParamsId);
	}
	
	/**
	 * 
	 * Removes the LateCarsGroupRule for a given LateCarsGroup
	 * @param lateCarsGroupRule
	 */
	public void removeLateCarsGroupRule(Long lateCarsParamsId)
	{
		getHibernateTemplate().delete(getLateCarsGroupRule(lateCarsParamsId));
		getHibernateTemplate().flush();
	}	
		
	
	/**
	 * This Method returns the list of lateCarsGroup .
	 * @param String.
	 * @return List.
	 */
	@SuppressWarnings("unchecked")
	public List<LateCarsGroup> getLateCarsGroups(String lateCarsGroup) {
		return getHibernateTemplate().find("from LateCarsGroup where lower(description) like lower('" + lateCarsGroup +"%'))");
	}



	@Override
	public LateCarsParams getLateCarsParams(Long lateCarsParamId) {
	
		List list = getHibernateTemplate().find("from LateCarsParams where lateCarsParamId in (" + lateCarsParamId +")");
		LateCarsParams lateCarsParams = null;
		if(list != null && list.size() >0) {
			lateCarsParams = (LateCarsParams) list.get(0);
			
		}
		return lateCarsParams;
	}



	@Override
	public boolean CheckExistingLateCarsParams(long lateCarsParamId,
			String currentUserTypeCd, String workflowStatusCd, long lateCarsGroupId) {
		// TODO Auto-generated method stub
		List list = getHibernateTemplate().find("from LateCarsParams where lateCarsParamId != "+ lateCarsParamId + 
				" and owner.userTypeCd = '"+currentUserTypeCd+ "' and status.statusCd = '"+ workflowStatusCd +"'" + " and lateCarsGroup.lateCarsGroupId = "+lateCarsGroupId);
		if(list != null && list.size()>0){
			return true;
		}else{
			return false;
		}
	}	
	
	public boolean CheckExistingLateCarsParams(String currentUserTypeCd, String workflowStatusCd, long lateCarsGroupId) {
		// TODO Auto-generated method stub
		List list = getHibernateTemplate().find("from LateCarsParams where " + 
				"owner.userTypeCd = '"+currentUserTypeCd+ "' and status.statusCd = '"+ workflowStatusCd +"'" + " and lateCarsGroup.lateCarsGroupId = "+lateCarsGroupId);
		if(list != null && list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
}
