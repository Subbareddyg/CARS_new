package com.belk.car.app.dao.hibernate;

import java.util.List;

import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.hibernate.Query;
import org.hibernate.Session;

import com.belk.car.app.dao.PIMAttributeExclusionListDao;
import com.belk.car.app.model.PIMAttributeExclusionList;

public class PIMAttributeExclusionListDaoHibernate extends CachedQueryDaoHibernate implements PIMAttributeExclusionListDao{

	@SuppressWarnings("unchecked")
	@Override
	public boolean isAttributeExcluded(long pimAttrId) {
		boolean isExcluded = false;

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createQuery("FROM PIMAttributeExclusionList where pimAttrId = :attrId");
		query.setLong("attrId", pimAttrId);
        List<PIMAttributeExclusionList> exclusionList = query.list();
		if(exclusionList!=null && exclusionList.size() >0){
			isExcluded = true;
		}
		return isExcluded;
	}

}
