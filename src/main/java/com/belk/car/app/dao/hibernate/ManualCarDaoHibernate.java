package com.belk.car.app.dao.hibernate;

import java.util.List;

import org.appfuse.dao.hibernate.UniversalDaoHibernate;

import com.belk.car.app.dao.ManualCarDao;
import com.belk.car.app.model.car.ManualCar;
import com.belk.car.app.model.car.ManualCarProcessStatus;

public class ManualCarDaoHibernate extends UniversalDaoHibernate implements ManualCarDao {

	public List<ManualCar> getAllManualCars(){
		return getHibernateTemplate().find("from ManualCar");
	}

	public List<ManualCar> getManualCars(String processStatusCd){
		Object arr[] = { processStatusCd };
		return getHibernateTemplate().find("from ManualCar where processStatus.statusCd=?", arr);
	}

	public ManualCar getManualCar(Long manualCarId){
		return (ManualCar) getHibernateTemplate().get(ManualCar.class, manualCarId);
	}
	
	public ManualCar save(ManualCar manualCar){
		getHibernateTemplate().saveOrUpdate(manualCar);
        getHibernateTemplate().flush();
		return manualCar;
	}
	
	public void remove(ManualCar manualCar){
		getHibernateTemplate().delete(manualCar);
	}
	
	public ManualCarProcessStatus getMCProcessStatus(String processCode){
		return (ManualCarProcessStatus)getHibernateTemplate().get(ManualCarProcessStatus.class, processCode);
	}
	
	public List<ManualCarProcessStatus> getAllMCProcessStatus(){
		return getHibernateTemplate().find("from ManualCarProcessStatus");
	}
	
	public List<ManualCar> searchManualCars(String query, Object[] queryValues){
		return getHibernateTemplate().find(query, queryValues);
	}

}