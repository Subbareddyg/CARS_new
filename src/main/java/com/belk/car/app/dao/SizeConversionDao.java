package com.belk.car.app.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import com.belk.car.app.model.SizeConversionMaster;
import com.belk.car.app.model.SizeSynchDataHolderView;
import com.belk.car.app.model.VendorSku;

/**
 * 
 * @author Yogesh.Vedak
 *
 */
public interface SizeConversionDao {

	//size
	public SizeConversionMaster findSizeConversionByScmId(long scmId);
	public SizeConversionMaster findSizeConversionBySizeName(String sizeName);
	public SizeConversionMaster findSizeConversionByConversionSizeName(String conversionSizeName);
	public SizeConversionMaster findConversionName(String sizeName,String conversionSizeName,String vendorNumber,String deptCode,String strClassNum); //EC:1602 CARS Size Name Issue
	public List<SizeConversionMaster> searchSizeConversions(String sizeName,String conversionName, String departmentNumber,String classNumber,String vendorNumber,String facetSize,String facetSubSize);
	public void createSizeConversion(SizeConversionMaster sizeConversionMaster);
	public void updateSizeConversion(SizeConversionMaster sizeConversionMaster);
	public void updateSizeConversionStatus(long scmId,String statusCode) ;
	public void updateSizeConversionListStatus(List<String> scmIdList,String statusCode);
	public void updateRuleChangedFlag(long scmId,String flagValue);
	public List<SizeConversionMaster> getAllSizeConversionMapping();   
	public List<SizeConversionMaster> getSizeConversionsByQuery(final String query);
	public  List<SizeConversionMaster> findSizeConversionByCriteria(String criteriaPropertyName,String criteriaValue) ;
	public void removeSizeConversionFromSystem(SizeConversionMaster sizeConversionMaster);
	public List<SizeConversionMaster> getChangedSizeConversions();	
	public SizeConversionMaster getSizeConversion(String sizeName,String classId, String deptId, String vendorId);
	public List<SizeSynchDataHolderView> getVendorSkuSizeUpdationData(final int offset,final int batchSize);
    public List<SizeSynchDataHolderView> getVendorSkuSizeUpdationDataForSkus(Set<VendorSku> skus);
	public void removeDeletedSizeConversionsFromSystem();
	public Long getSizeSynchViewRowCount();
	public int[] executeSizeRuleChangedFalseBatch(List<String> scmIdList) throws SQLException;
	public int[] executeNewSizeRuleBatchInsert(List<SizeConversionMaster> ruleList) throws SQLException;
	public List<SizeConversionMaster> searchConversionName( //EC:1602 CARS Size Name Issue
			String sizeName,
			String criteriaValue,
			String vendorCriteriaValue,
			String deptCriteriaValue,
			String classCriteriaValue);
}
