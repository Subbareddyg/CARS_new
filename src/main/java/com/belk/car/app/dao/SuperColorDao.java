package com.belk.car.app.dao;

import java.sql.SQLException;
import java.util.List;

import com.belk.car.app.model.ColorMappingMaster;
import com.belk.car.app.model.SuperColorSynchDataHolderView;

/**
 * 
 * @author Yogesh.Vedak
 *
 */
public interface SuperColorDao {

	//get
	List<ColorMappingMaster> getAllSuperColorMapping();
	public ColorMappingMaster findSuperColorByCmmId(long cmmId);
	public ColorMappingMaster findSuperColorByColorName(String superColorName);
	public ColorMappingMaster findSuperColorByColorCode(String superColorCode);
	public ColorMappingMaster getSuperColorByBeginCode(String beginCode);
	public ColorMappingMaster getSuperColorByEndCode(String endCode);
	public String findSuperColorCodeForColorCode(String skuColorCode);
	public String findSuperColorNameForColorCode(String skuColorCode);
	//save/update/set
	public void saveSuperColor(ColorMappingMaster colorMapping);
	public void saveOrUpdateSuperColor(ColorMappingMaster colorMapping);
	public void updateSuperColorStatus(long cmmId,String statusCode) ;
	public void updateRuleChangedFlag(long cmmId,String flagCode) ;
	public List<ColorMappingMaster> searchSuperColors(String superColorName,String superColorCode);
	public List<ColorMappingMaster>getSuperColorsByQuery(final String query);	
	public List<ColorMappingMaster>  getAvailableColorMappingsContainingRangeCode(String beginOrEndRangeCode);
	public Long getSuperColorSynchViewRowCount(boolean insertNewSuperColorCase);
	public List<ColorMappingMaster>  getChangedSuperColorMappings();
	
	public List<ColorMappingMaster> findSuperColorsByCriteria(String criteriaPropertyName , String criteriaValue);
	public void removeSuperColor(ColorMappingMaster superColor);
	public void removeSuperColor(long cmmId);
	public void removeDeletedSuperColorsFromSystem();
	//public List<SuperColorSynchDataHolderView> getVendorSkuSuperColorUpdationData(int rowsOffset,int bathSize);
	public List<SuperColorSynchDataHolderView> getVendorSkuSuperColorUpdationData(int rowsOffset,int bathSize,boolean insertSuperColor1);
	//batch
	public int[] executeSuperColorRuleChangedBatch(List<String> cmmIdList) throws SQLException; 
}
