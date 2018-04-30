package com.belk.car.app.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.appfuse.service.impl.UniversalManagerImpl;

import com.belk.car.app.Constants;
import com.belk.car.app.dao.SuperColorDao;
import com.belk.car.app.model.ColorMappingMaster;
import com.belk.car.app.model.SuperColorSynchDataHolderView;
import com.belk.car.app.service.SuperColorManager;


public class SuperColorManagerImpl extends UniversalManagerImpl implements SuperColorManager{

	private SuperColorDao superColorDao;
	
	public SuperColorDao getSuperColorDao() {
		return superColorDao;
	}

	public void setSuperColorDao(SuperColorDao superColorDao) {
		this.superColorDao = superColorDao;
	}

	/**
	 * This method Returns all the "Active/Pending" super colors ,and  not the ones marked deleted.
	 */
	@Override
	public List<ColorMappingMaster> getAllSuperColorMapping() {
		return  getSuperColorDao().getAllSuperColorMapping();
	}

	@Override
	public void saveSuperColor(ColorMappingMaster colorMapping){
		 getSuperColorDao().saveSuperColor(colorMapping);
	}
	
	/**
	 * Saves the object if is a newly created. Else updates the object . 
	 * Also Sets value of RULE_CHANGED = T  
	 * @author Yogesh.Vedak
	 */
	public void saveOrUpdateSuperColor(ColorMappingMaster colorMapping){
		colorMapping.setRuleChanged(Constants.RULECHANGED_TRUE);
		 getSuperColorDao().saveOrUpdateSuperColor(colorMapping);
	}
	 
	 public List<ColorMappingMaster> searchSuperColors(String superColorName,String superColorCode){
		 return  getSuperColorDao().searchSuperColors(superColorName,superColorCode);
		}

	@Override
	public ColorMappingMaster getSuperColorByCmmId(long cmmId) {
		return 	 getSuperColorDao().findSuperColorByCmmId(cmmId);
	}

	/**
	 * This method checks whether colorBeginCode present in the argument belongs to any existing super colors' begin and end code range.
	 * If found then checks whether that objects id is same as that of passed in param if yes then returns 'false' ie it does not overlaps.Becuase it is same object being compared against itself.
	 * If id is different then it returns 'true'.
	 * Because then cmmId passed in param must be of either a new object or some other existing being edited which is not supposed to overlap in range for that other existing object which was retrieved for comparison.  
	 * @author Yogesh.Vedak
	 */
	@Override
	public Boolean isColorBeginCodeOverlaps(String colorBeginCode,long cmmId) {
		List<ColorMappingMaster> mappingList =   getSuperColorDao().getAvailableColorMappingsContainingRangeCode(colorBeginCode);
		if( (mappingList!= null) && (!mappingList.isEmpty()))
		{
			ColorMappingMaster master = mappingList.get(0);
			//check if object found was not same with the passed parameter 'cmmId' to test. 
			if(log.isDebugEnabled()){
				log.debug("Service Test begin Range "+cmmId+" and master's is:"+master.getCmmId());
			}
			if(master.getCmmId() != cmmId ){
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	/**
	 * This method checks whether colorEndCode present in the argument belongs to any existing super colors' begin and end code range.
	 * If found then checks whether that objects id is same as that of passed in param if yes then returns 'false' ie it does not overlaps.
	 * Because it is same object being compared against itself.
	 * If id is different then it returns 'true'.
	 * Because then cmmId passed in param must be of either a new object or some other existing being edited which is not supposed to overlap in range for that other existing object which was retrieved for comparison.  
	 * @author Yogesh.Vedak
	 */
	@Override
	public Boolean isColorEndCodeOverlaps(String colorEndCode,long cmmId) {
		List<ColorMappingMaster> mappingList =   getSuperColorDao().getAvailableColorMappingsContainingRangeCode(colorEndCode);
		if( (mappingList!= null) && (!mappingList.isEmpty())){
			ColorMappingMaster master = mappingList.get(0);
			if(master.getCmmId() != cmmId ){
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	
	/**
	 * Returns the super color based on the super color name provided in the argument . It will be only single object in return as Super color names are unique.
	 * @param colorName
	 * @author Yogesh.Vedak
	 */
	@Override
	public ColorMappingMaster getSuperColorByColorName(String colorName) {
		return  getSuperColorDao().findSuperColorByColorName(colorName);
	}

	/**
	 * Returns the super color based on the super color code provided in the argument . It will be only single object in return as Super color names are unique.
	 * @param colorCode
	 * @author Yogesh.Vedak
	 */
	
	@Override
	public ColorMappingMaster getSuperColorByColorCode(String colorCode) {
		return  getSuperColorDao().findSuperColorByColorCode(colorCode);
	}



	@Override
	public void updateSuperColorStatus(long cmmId,String statusCode) {
		 getSuperColorDao().updateSuperColorStatus(cmmId,statusCode);
	}
	
	/**
	 * 
	 * This method Soft-deletes the super color ie just marks the status of the same as 'D'. It doesn't not remove that particular object's row from the table.
	 * @param cmmId
	 * @author Yogesh.Vedak
	 */
	@Override
	public void deleteSuperColor(long cmmId) {
		updateSuperColorStatus(cmmId,Constants.SUPERCOLOR_STATUS_DELETED);
	}

	public String findSuperColorCodeForColorCode(String skuColorCode){
		return  getSuperColorDao().findSuperColorCodeForColorCode(skuColorCode);
	}

	@Override
	public String findSuperColorNameForColorCode(String skuColorCode){
		return  getSuperColorDao().findSuperColorNameForColorCode(skuColorCode);
	}
	
	@Override
	public ColorMappingMaster getSuperColorByBeginCode(String beginCode) {
		return  getSuperColorDao().getSuperColorByBeginCode(beginCode);
	}

	@Override
	public ColorMappingMaster getSuperColorByEndCode(String endCode) {
		return  getSuperColorDao().getSuperColorByEndCode(endCode);
	}

	
	/**
	 * This Method returns the list of those active super color objects which have been changed currently. i.e. the colors marked as RULE_CHANGED=T in the database.   
	 * @return  List of {@link ColorMappingMaster}
	 */
	@Override
	public List<ColorMappingMaster> getChangedSuperColorMappings() {
		return getSuperColorDao().getChangedSuperColorMappings();
	}
	 
	/**
	 * Gets the cmmId and updates the objects Rule_Changed vale to False(F)
	 * @param cmmId
	 */
	public void setColorMappingRuleAsNotChanged(long cmmId) {
		 getSuperColorDao().updateRuleChangedFlag(cmmId, Constants.RULECHANGED_FALSE);
	}
	
	/**
	 * This methods removes super colors from the system permanently which were soft-deleted i.e. the ones which were marked 'Deleted' as their status .
	 * 
	 */
	public void removeDeletedSuperColorsFromSystem(){
		getSuperColorDao().removeDeletedSuperColorsFromSystem();
	}
	
	/**
	 * This method returns the specified list of VIEW objects which holds the data to be applied on sku ,for updating vendor sku's SUPER_COLOR_1 attribute value . 
	 * @return List of {@link SuperColorSynchDataHolderView}
	 */
	@Override
	public List<SuperColorSynchDataHolderView> getVendorSkuSuperColorUpdationData(int rowsOffset,int batchSize,boolean inserNewSuperColor){
		return getSuperColorDao().getVendorSkuSuperColorUpdationData(rowsOffset,batchSize,inserNewSuperColor);
	}
	
	public Long getSuperColorSynchViewRowCount(boolean insertNewSuperColorCase){
		return getSuperColorDao().getSuperColorSynchViewRowCount(insertNewSuperColorCase);
	}
	@Override
	public int[] executeSuperColorRuleChangedBatch(List<String> scmIdList)
			throws SQLException {
		return getSuperColorDao().executeSuperColorRuleChangedBatch(scmIdList);
	}
}
