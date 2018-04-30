package com.belk.car.app.service;


import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.appfuse.service.UniversalManager;

import com.belk.car.app.model.SizeConversionMaster;
import com.belk.car.app.model.SizeSynchDataHolderView;
import com.belk.car.app.model.VendorSku;


public interface SizeConversionManager extends UniversalManager{
	
	public List<SizeConversionMaster> getAllSizeConversionMapping(); 
	//search 
	public List<SizeConversionMaster> searchSizeConversions(String sizeName,String departmentCode,String conversionName,String classNumber,String vendorNumber,String facetSize,String facetSubSize);
	
	//size starts
	public SizeConversionMaster getSizeConversionByScmId(long scmId);
	public void saveSizeConversion(SizeConversionMaster colorMapping);
	public void saveOrUpdateSizeConversion(SizeConversionMaster colorMapping);
	public void updateSizeConversionStatus(long scmId,String statusCode);
	public void updateSizeConversionListStatus(List<String> sizeList,String statusCode);
	public void updateRuleChangedFlag(long scmId,String flagValue);
	public void setSizeConvRuleToNotChanged(long scmId);
	public void deleteSizeConversion(long scmId);
	public void deleteBatchSizeConversionList(List<String> sizeList);
	public SizeConversionMaster getSizeConversionBySizeName(String sizeName);
	public SizeConversionMaster getSizeConversionByConversionSizeName(String conversionSizeName);
	public SizeConversionMaster getSizeConversionName(String sizeName,String conversionSizeName,String vendorNumber,String deptCode,String strClassNum); //EC:1602 CARS Size Name Issue
	public List<SizeConversionMaster> getSizeConversionsByQuery(String sqlQuery);	
	public SizeConversionMaster getSizeConversion(String sizeName,String classId,String deptId,String vendorId);
	public boolean isSizeConversionExists(long scmId,String sizeName,String classId,String deptId,String vendorId);
	public List<SizeConversionMaster> getSizeChangedConversions();
	public List<SizeSynchDataHolderView> getVendorSkuSizeUpdationData(int offset,int batchSize);
	public void removeDeletedSizeConversionsFromSystem();
	public Long getSizeSynchViewRowCount();
	public int[] executeSizeRuleChangedFalseBatch(List<String> scmIdList) throws SQLException;
	public int[] executeNewSizeRuleBatchInsert(List<SizeConversionMaster> ruleList) throws SQLException;
    public List<SizeSynchDataHolderView> getVendorSkuSizeUpdationDataForSkus(Set<VendorSku> skus);
}
