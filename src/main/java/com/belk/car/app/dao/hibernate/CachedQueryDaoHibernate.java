package com.belk.car.app.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.hibernate.Query;
import org.hibernate.Session;

public class CachedQueryDaoHibernate<T> extends UniversalDaoHibernate {
	
	
	public  List<T>  doCachedQueryCall(String classType) {
		Session sess = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = sess.createQuery("from " + classType);
		return query.setCacheable(true).list();
	}

}
