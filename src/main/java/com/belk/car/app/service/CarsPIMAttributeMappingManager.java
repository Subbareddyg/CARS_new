package com.belk.car.app.service;

import java.util.List;

import org.appfuse.service.UniversalManager;

import com.belk.car.app.model.CarsPIMAttributeMapping;

/**
 * Business Service Interface to talk to persistence layer and retrieve
 *  mapping attributes between Cars and PIM.
 *   
 * @author AFUSYS9
 *
 */
public interface CarsPIMAttributeMappingManager extends UniversalManager{
	
	/**
	 * Retrieves all existing mapping attributes for a given dept,class
	 *  and product type.
	 *  
	 * @param classId
	 * @param deptId
	 * @param productTypeId
	 * @return
	 */
	public List<CarsPIMAttributeMapping> getCarsPIMMappingDetails(long classId,long deptId,long productTypeId);
	public String removeCARSPIMMappingAttribtues(long carAttrId,String classOrDeptOrPtId, String type); 
	public String addCarsPimMappingAttribtues(long carAttrId,List<String> classOrDeptOrPtId, String type, String deptOrClassOrPt);
    
	

}
