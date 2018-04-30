package com.belk.car.app.service.impl;


import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.appfuse.service.impl.UniversalManagerImpl;

import com.belk.car.app.Constants;
import com.belk.car.app.model.SizeConversionMaster;
import com.belk.car.app.model.SizeSynchDataHolderView;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.service.SizeConversionManager;
import com.belk.car.app.dao.SizeConversionDao;

/**
 * 
 * @author Yogesh.Vedak
 *
 */
public class SizeConversionManagerImpl extends UniversalManagerImpl implements SizeConversionManager{
	

	private SizeConversionDao sizeConversionDao;
	
	public SizeConversionDao getSizeConversionDao() {
		return sizeConversionDao;
	}

	public void setSizeConversionDao(SizeConversionDao sizeConversionDao) {
		this.sizeConversionDao = sizeConversionDao;
	}

	@Override
	public List<SizeConversionMaster> getAllSizeConversionMapping() {
		return getSizeConversionDao().getAllSizeConversionMapping();
	}

	@Override
	public void saveSizeConversion(SizeConversionMaster sizeConversion) {
		getSizeConversionDao().createSizeConversion(sizeConversion);
	}

	@Override
	public void saveOrUpdateSizeConversion(SizeConversionMaster sizeConversion) {
		sizeConversion.setRuleChanged(Constants.RULECHANGED_TRUE);
		getSizeConversionDao().updateSizeConversion(sizeConversion);
	}

	@Override
	public List<SizeConversionMaster> searchSizeConversions(String sizeName,String conversionName,String departmentCode,String classNumber,String vendorNumber,String facetSize,String facetSubSize) {
		 return getSizeConversionDao().searchSizeConversions(sizeName,conversionName, departmentCode, classNumber,vendorNumber,facetSize,facetSubSize);
	}

	@Override
	public SizeConversionMaster getSizeConversionByScmId(long scmId) {
		return getSizeConversionDao().findSizeConversionByScmId(scmId);
	}


	@Override
	public void updateSizeConversionStatus(long scmId,String statusCode) {
		getSizeConversionDao().updateSizeConversionStatus(scmId,statusCode);
	}

	@Override
	public void updateSizeConversionListStatus(List<String> sizeList,String statusCode) {
		getSizeConversionDao().updateSizeConversionListStatus(sizeList,statusCode);
	}

	@Override
	public void updateRuleChangedFlag(long scmId,String flagValue){
		getSizeConversionDao().updateRuleChangedFlag(scmId, flagValue);
	}
	
	public void setSizeConvRuleToNotChanged(long scmId){
		updateRuleChangedFlag(scmId,Constants.RULECHANGED_FALSE);
	}
	
	@Override
	public void deleteSizeConversion(long scmId) {
		updateSizeConversionStatus(scmId,Constants.SIZECONV_STATUS_DELETED);
	}

	@Override
	public void deleteBatchSizeConversionList(List sizeList) {
		updateSizeConversionListStatus(sizeList,Constants.SIZECONV_STATUS_DELETED);
	}

	@Override
	public SizeConversionMaster getSizeConversionBySizeName(String sizeName) {
		return getSizeConversionDao().findSizeConversionBySizeName(sizeName);
	}

	@Override
	public SizeConversionMaster getSizeConversionByConversionSizeName(
			String conversionSizeName) {
		return getSizeConversionDao().findSizeConversionByConversionSizeName(conversionSizeName);		
	}
	
	//EC:1602 CARS Size Name Issue
	@Override
	public SizeConversionMaster getSizeConversionName(String sizeName,
				String conversionSizeName,String vendorNumber,String deptCode,String strClassNum) {
		return getSizeConversionDao().findConversionName(sizeName,conversionSizeName,vendorNumber,deptCode,strClassNum);
	}

	@Override
	public List<SizeConversionMaster> getSizeConversionsByQuery(String sqlQuery) {
		return getSizeConversionDao().getSizeConversionsByQuery(sqlQuery);
	}

	@Override
	public SizeConversionMaster getSizeConversion(String sizeName,String classId, String deptId, String vendorId) {
		return  getSizeConversionDao().getSizeConversion(sizeName, classId, deptId, vendorId);
	}

	@Override
	public boolean isSizeConversionExists(long scmId,String sizeName, String classId,String deptId,String vendorId) {

		SizeConversionMaster sizeConv = getSizeConversion(sizeName, classId, deptId, vendorId);
		if(sizeConv != null){
			if(log.isDebugEnabled()){
				log.debug("in Manager :-Found ruleScmId is"+sizeConv.getScmId()+" object being validted Id is"+scmId);
			}
			if(sizeConv.getScmId() != scmId ){
				
				return true;
			}
		}
		return false;
	}

	@Override
	public List<SizeConversionMaster> getSizeChangedConversions() {
		return getSizeConversionDao().getChangedSizeConversions();
	}

	@Override
	public List<SizeSynchDataHolderView> getVendorSkuSizeUpdationData(int offset,int batchSize){
		return getSizeConversionDao().getVendorSkuSizeUpdationData(offset,batchSize);
	}

	@Override
	public void removeDeletedSizeConversionsFromSystem(){
		getSizeConversionDao().removeDeletedSizeConversionsFromSystem();
	}

	@Override
	public Long getSizeSynchViewRowCount() {
		return getSizeConversionDao().getSizeSynchViewRowCount();
	}

	@Override
	public int[] executeSizeRuleChangedFalseBatch(List<String> scmIdList)
			throws SQLException {
		return getSizeConversionDao().executeSizeRuleChangedFalseBatch(scmIdList);
	}

	@Override
	public int[] executeNewSizeRuleBatchInsert(List<SizeConversionMaster> ruleList) throws SQLException {
		return getSizeConversionDao().executeNewSizeRuleBatchInsert(ruleList);
	}
	
    @Override
    public List<SizeSynchDataHolderView> getVendorSkuSizeUpdationDataForSkus(Set<VendorSku> skus) {
        return getSizeConversionDao().getVendorSkuSizeUpdationDataForSkus(skus);
    }

}
