package com.belk.car.app.service.impl;

import java.util.List;

import org.appfuse.service.impl.UniversalManagerImpl;

import com.belk.car.app.dao.CarsPIMAttributeMappingDao;
import com.belk.car.app.model.CarsPIMAttributeMapping;
import com.belk.car.app.service.CarsPIMAttributeMappingManager;

/**
 * Implementation of methods defined in CarsPIMAttributeMappingManager interface.
 * 
 * @author AFUSYS9
 *
 */
public class CarsPIMAttributeMappingManagerImpl extends UniversalManagerImpl implements CarsPIMAttributeMappingManager{

	private CarsPIMAttributeMappingDao carsPIMAttributeMappingDao;
	
	public void setCarsPIMAttributeMappingDao(CarsPIMAttributeMappingDao carsPIMAttributeMappingDao) {
		this.carsPIMAttributeMappingDao = carsPIMAttributeMappingDao;
	}
	
	public CarsPIMAttributeMappingManagerImpl(){
		
	}
	
	@Override
	public List<CarsPIMAttributeMapping> getCarsPIMMappingDetails(long classId,
			long deptId, long productTypeId) {
		return carsPIMAttributeMappingDao.getCarsPIMMappingDetails(classId, deptId, productTypeId);
	}

	@Override
	public String removeCARSPIMMappingAttribtues(long carAttrId, String classOrDeptOrPtId,
			String type) {
		// TODO Auto-generated method stub
		return carsPIMAttributeMappingDao.removeCARSPIMMappingAttribtues(carAttrId, classOrDeptOrPtId, type);
	}

	@Override
	public String addCarsPimMappingAttribtues(long carAttrId, List<String> classOrDeptOrPtId, String type,String deptOrClassOrPt) {
		// TODO Auto-generated method stub
		return carsPIMAttributeMappingDao.addCarsPimMappingAttribtues(carAttrId, classOrDeptOrPtId, type, deptOrClassOrPt);
	}

}
