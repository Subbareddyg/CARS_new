package com.belk.car.app.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.service.impl.UniversalManagerImpl;

import com.belk.car.app.dao.CarLookupDao;
import com.belk.car.app.dao.ManualCarDao;
import com.belk.car.app.model.car.ManualCar;
import com.belk.car.app.model.car.ManualCarProcessStatus;
import com.belk.car.app.service.AttributeManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.ManualCarManager;
import com.belk.car.app.service.ProductManager;

/**
 * @author anilm
 * 
 */
public class ManualCarManagerImpl extends UniversalManagerImpl implements
		ManualCarManager {

	private transient final Log log = LogFactory
			.getLog(ManualCarManagerImpl.class);
	private ManualCarDao manualCarDao;
	private CarLookupDao carLookupDao;
	private CarManager carManager;
	private AttributeManager attributeManager;
	private ProductManager productManager;

	/**
	 * @param productManager
	 *            the productManager to set
	 */
	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}

	/**
	 * @param attributeManager
	 *            the attributeManager to set
	 */
	public void setAttributeManager(AttributeManager attributeManager) {
		this.attributeManager = attributeManager;
	}

	/**
	 * @param carManager
	 *            the carManager to set
	 */
	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	public void setManualCarDao(ManualCarDao manualCarDao) {
		this.manualCarDao = manualCarDao;
	}

	public void setCarLookupDao(CarLookupDao carLookupDao) {
		this.carLookupDao = carLookupDao;
	}

	public List<ManualCar> getAllManualCars() {
		return manualCarDao.getAllManualCars();
	}

	public List<ManualCar> getManualCars(String processStatusCd) {
		return manualCarDao.getManualCars(processStatusCd);
	}

	public ManualCar getManualCar(Long manualCarId) {
		return manualCarDao.getManualCar(manualCarId);
	}

	public ManualCar save(ManualCar manualCar) {
		return manualCarDao.save(manualCar);
	}

	public void remove(ManualCar manualCar) {
		manualCarDao.remove(manualCar);
	}

	public ManualCarProcessStatus getMCProcessStatus(String processCode) {
		return manualCarDao.getMCProcessStatus(processCode);
	}

	public List<ManualCarProcessStatus> getAllMCProcessStatus() {
		return manualCarDao.getAllMCProcessStatus();
	}

	public List<ManualCar> searchManualCars(String query, Object[] queryValues) {
		return manualCarDao.searchManualCars(query, queryValues);
	}
	
}