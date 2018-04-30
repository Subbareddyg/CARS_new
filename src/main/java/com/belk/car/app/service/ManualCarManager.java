package com.belk.car.app.service;

import java.util.List;

import org.appfuse.service.UniversalManager;

import com.belk.car.app.model.car.ManualCar;
import com.belk.car.app.model.car.ManualCarProcessStatus;

/**
 * @author anilm
 *
 */
public interface ManualCarManager extends UniversalManager{
	
	List<ManualCar> getAllManualCars();
	
	List<ManualCar> getManualCars(String processStatusCd);

	ManualCar getManualCar(Long manualCarId);

	ManualCar save(ManualCar manualCar);
	
	void remove(ManualCar manualCar);
	
	ManualCarProcessStatus getMCProcessStatus(String processCode);
	
	List<ManualCarProcessStatus> getAllMCProcessStatus();
	
	List<ManualCar> searchManualCars(String query, Object[] queryValues);
	
}


