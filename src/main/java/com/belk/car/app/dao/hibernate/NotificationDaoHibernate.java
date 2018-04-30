package com.belk.car.app.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.appfuse.dao.hibernate.UniversalDaoHibernate;

import com.belk.car.app.dao.NotificationDao;
import com.belk.car.app.model.Notification;

public class NotificationDaoHibernate extends UniversalDaoHibernate implements
		NotificationDao {

	public Notification save(Notification notification) {
		getHibernateTemplate().saveOrUpdate(notification);
        getHibernateTemplate().flush();
		return notification;
	}

	public Object get(Class clazz, Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List getAll(Class clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	public void remove(Class clazz, Serializable id) {
		// TODO Auto-generated method stub

	}

	public Object save(Object o) {
		// TODO Auto-generated method stub
		return null;
	}

}
