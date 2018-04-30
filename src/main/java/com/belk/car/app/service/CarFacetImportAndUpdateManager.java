package com.belk.car.app.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.appfuse.model.User;
import org.appfuse.service.UniversalManager;

import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.ClassAttribute;
import com.belk.car.app.model.DepartmentAttribute;

public interface CarFacetImportAndUpdateManager extends UniversalManager {
	
	List<DepartmentAttribute> getAllDepartmentAttributes(long deptId);
	
	List<ClassAttribute> getAllClassificationAttributes(long classId);
	
	List<CarAttribute> getAllCarAttribute(long carId);

	void resyncAttributes(Car car, User loggedInUser,Map attributeXMLMap,Set<String> attrNotProcessed) ;
	
	public Car getCarFromId(Long id);

}
