package com.belk.car.app.dao;

import java.util.List;

import org.appfuse.dao.UniversalDao;

import com.belk.car.app.model.car.ManualCar;
import com.belk.car.app.model.car.ManualCarProcessStatus;

public interface ManualCarDao  extends UniversalDao  {
	
	List<ManualCar> getAllManualCars();
	
	List<ManualCar> getManualCars(String processingStatusCd);

	ManualCar getManualCar(Long manualCarId);

	ManualCar save(ManualCar manualCar);
	
	void remove(ManualCar manualCar);
	
	ManualCarProcessStatus getMCProcessStatus(String processCode);
	
	List<ManualCarProcessStatus> getAllMCProcessStatus();
	
	List<ManualCar> searchManualCars(String query, Object[] queryValues);
}
