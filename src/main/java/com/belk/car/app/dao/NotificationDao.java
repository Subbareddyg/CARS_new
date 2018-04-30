package com.belk.car.app.dao;

import org.appfuse.dao.UniversalDao;

import com.belk.car.app.model.Notification;

public interface NotificationDao extends UniversalDao {
	  Notification save(Notification notification);
}
