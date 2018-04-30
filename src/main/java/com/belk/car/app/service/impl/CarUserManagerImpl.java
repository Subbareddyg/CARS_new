package com.belk.car.app.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.appfuse.model.User;
import org.appfuse.service.impl.UniversalManagerImpl;

import com.belk.car.app.dao.CarUserDao;
import com.belk.car.app.dto.CarUserVendorDepartmentDTO;
import com.belk.car.app.model.CarUserVendorDepartment;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.CarUserManager;

public class CarUserManagerImpl extends UniversalManagerImpl implements
		CarUserManager {

	private CarUserDao carUserDao;
	
	protected CarManager carManager;

	/**
	 * Method that allows setting the DAO to talk to the data store with.
	 * 
	 * @param dao
	 *            the dao implementation
	 */
	public void setCarUserDao(CarUserDao carUserDao) {
		this.carUserDao = carUserDao;
	}

	public CarManager getCarManager() {
		return carManager;
	}

	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}
	/*
	 * (non-Javadoc)
	 * @see com.belk.car.app.service.CarUserManager#searchVendors(java.lang.String, java.lang.String, java.lang.String)
	 *Modified searchUsers Method sign so passing 3 new argument as null.
	 */
	public List<User> searchVendors(String firstName, String lastName,
			String emailId) {
		return carUserDao.searchUsers(firstName, lastName, emailId,
				UserType.VENDOR,null,null,null);
	}
	/*
	 * (non-Javadoc)
	 * @see com.belk.car.app.service.CarUserManager#searchUsers(java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String)
	 * Page ,sortedOn and sortedOrder are added to implement external pagination logic.
	 */
	
	public List<User> searchUsers(String firstName, String lastName,
			String emailId,Integer page,String sortedOn,String sortedOrder) {
		return carUserDao.searchUsers(firstName, lastName, emailId, "",page,sortedOn,sortedOrder);
	}

	public void removeVendor(User vendor) {
		log.info("Get all the vendors and their car user vendor department details if exits and remove them");
		Set<Vendor> vendors=vendor.getVendors();
		Iterator<Vendor> it = vendors.iterator();
		while (it.hasNext()) {
			Vendor v =(Vendor) it.next();
			List<CarUserVendorDepartment> vendorDeptsList= getCarManager().getVendorDepartments(Long.valueOf(v.getVendorId()));
			if(vendorDeptsList!=null){
				for(Iterator<CarUserVendorDepartment> i = vendorDeptsList.iterator(); i.hasNext(); ) {
					 	CarUserVendorDepartment cuvd = i.next();
					 	carManager.deleteVendorDepartment(cuvd.getCarUserVendorDepartmentId());
					}
			}
		}
		carUserDao.updateVendorOrUser(vendor);
	}
	
	/*
	 * This method is used for updating/removing(Status update to 'DELETE') vendor or user.
	 */
	
	public void updateVendorOrUser(User vendor){
		 carUserDao.updateVendorOrUser(vendor);
	 }
	
	 /*
	 * This method is used for get vendor or user.
	 */
	
	 public User getVendorOrUser(String userName){
		 return carUserDao.getVendorOrUser(userName);
	 }
	 
	
}
