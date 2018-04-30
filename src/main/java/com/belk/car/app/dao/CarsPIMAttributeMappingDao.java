package com.belk.car.app.dao;

import java.util.List;

import org.appfuse.dao.UniversalDao;

import com.belk.car.app.model.CarsPIMAttributeMapping;

public interface CarsPIMAttributeMappingDao extends UniversalDao{

	/**
	 * Method to get the mapping attributes from cars to PIM for a 
	 * given department,class and product_type combination.
	 * 
	 * @param classId
	 * @param deptId
	 * @param productTypeId
	 * @return
	 */
	public List<CarsPIMAttributeMapping> getCarsPIMMappingDetails(long classId,long deptId,long productTypeId);

	public String removeCARSPIMMappingAttribtues(long carAttrId, String classOrDeptOrPtId, String type);

	public String addCarsPimMappingAttribtues(long carAttrId, List<String> classOrDeptOrPtId, String type, String deptOrClassOrPt);  

}
