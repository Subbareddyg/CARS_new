package com.belk.car.app.service;


import java.sql.SQLException;
import java.util.List;
import org.appfuse.service.UniversalManager;
import com.belk.car.app.model.ColorMappingMaster;
import com.belk.car.app.model.SuperColorSynchDataHolderView;



public interface SuperColorManager extends UniversalManager{
	
	//add/update
	public void saveSuperColor(ColorMappingMaster colorMapping);
	public void saveOrUpdateSuperColor(ColorMappingMaster colorMapping);
	public void updateSuperColorStatus(long cmmId,String statusCode) ;
	public void setColorMappingRuleAsNotChanged(long cmmId) ;
	//get 
	public List<ColorMappingMaster> getAllSuperColorMapping();
	public String findSuperColorCodeForColorCode(String skuColorCode);
	public String findSuperColorNameForColorCode(String skuColorCode);
	public ColorMappingMaster getSuperColorByCmmId(long cmmId);
	public ColorMappingMaster getSuperColorByColorName(String colorName);
	public ColorMappingMaster getSuperColorByColorCode(String colorName);
	public ColorMappingMaster getSuperColorByBeginCode(String beginCode);
	public ColorMappingMaster getSuperColorByEndCode(String endCode);
	public List<ColorMappingMaster> getChangedSuperColorMappings();
	//search 
	public Long getSuperColorSynchViewRowCount(boolean insertNewSuperColorCase);
	public List<ColorMappingMaster> searchSuperColors(String superColorName,String superColorCode);
	public Boolean isColorBeginCodeOverlaps(String colorBeginCode,long cmmId);
	public Boolean isColorEndCodeOverlaps(String colorEndCode,long cmmId);
	public void deleteSuperColor(long cmmId);
	public void removeDeletedSuperColorsFromSystem();
	public List<SuperColorSynchDataHolderView> getVendorSkuSuperColorUpdationData(int rowsOffset,int batchSize,boolean forInsertSuperColor);
	//batch 
	public int[] executeSuperColorRuleChangedBatch(List<String> scmIdList)throws SQLException;
	
}
