/**
 * 
 */
package com.belk.car.app.service.impl;

import java.io.File;
import java.io.IOException;
import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.belk.car.app.Constants;
import com.belk.car.app.exceptions.CarJobDetailException;
import com.belk.car.app.exceptions.IExceptionProcessor;
import com.belk.car.app.model.Config;
import com.belk.car.app.model.SuperColorSynchDataHolderView;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.OutfitManager;
import com.belk.car.app.service.SuperColorJobManager;
import com.belk.car.app.service.SuperColorManager;
import com.belk.car.app.util.BMIFileGenerationUtil;
import com.belk.car.app.util.FileTransferUtil;
import com.belk.car.app.util.QuartzJobCommonUtil;
import com.belk.car.app.util.SuperColorRulesUtils;
import com.belk.car.util.DateUtils;

/**
 * @author Yogesh.Vedak
 * 
 */
public class SuperColorJobManagerImpl implements SuperColorJobManager, IExceptionProcessor {

	private SuperColorManager superColorManager;
	private SuperColorRulesUtils superColorRulesUtils;
	private static transient final Log log = LogFactory.getLog(SuperColorJobManagerImpl.class);
	public SuperColorRulesUtils getSuperColorRulesUtils() {
		return superColorRulesUtils;
	}

	public void setSuperColorRulesUtils(SuperColorRulesUtils superColorRulesUtils) {
		this.superColorRulesUtils = superColorRulesUtils;
	}

	public SuperColorManager getSuperColorManager() {
		return superColorManager;
	}

	public void setSuperColorManager(SuperColorManager superColorManager) {
		this.superColorManager = superColorManager;
	}

	private OutfitManager outfitManager;

	public OutfitManager getOutfitManager() {
		return outfitManager;
	}


	private CarManager carManager;
	public CarManager getCarManager() {
		return carManager;
	}

	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	public void setOutfitManager(OutfitManager outfitManager) {
		this.outfitManager = outfitManager;
	}

	private CarLookupManager lookupManager;

	public CarLookupManager getLookupManager() {
		return lookupManager;
	}

	public void setLookupManager(CarLookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}

	private QuartzJobCommonUtil quartzJobCommonUtil;

	public QuartzJobCommonUtil getQuartzJobCommonUtil() {
		return quartzJobCommonUtil;
	}

	public void setQuartzJobCommonUtil(QuartzJobCommonUtil quartzJobCommonUtil) {
		this.quartzJobCommonUtil = quartzJobCommonUtil;
	}

	/**
	 * <B>Description:</B> This method is invoked by quartz scheduler as a Super_Color_1 synch batch job process.<br/>
	 * It simply does the following:<br/>
	 * 1.First identify the deleted-status color rules from COLOR_MAPPING_MASTER table and nullify their references from Vendor_Sku table 
	 *    that might have been set as foreign keys.<br/>
	 *	 (Here setting null on color_rule_id column for these SKUs says  that these must be the new SKUs came in to the system or particular
	 * color rule has been removed from the system by admin so these SKUs need to be eventually considered for new color-synch to remove
	 *  their SUPER_COLOR_1 attribute value).<br/>
	 * 2.Actually perform the deletion of SUPER_COLOR_1 ATTTRIBUTE and then remove completely (hard delete) all those color rules from rule table 
	 * which were of deleted-status.(Just cleaning activity)<br/>
	 * 3.Also write BMI record for each of these SKUs whose super color has now been removed. (So this BMI file would be sent to 
	 * CMP system for these SKUs)
	 * 4.Then get the actual synch-data as view objects to synchronize color name values of SKUs for edited color rules.<br/>
	 *   (This view i.e. named V_SKU_SIZE_RULE actually decides which rule to be applied on which SKU and simply returns the rows of their combination.)<br/>
	 * 4.1 Here we iterate over rows and at each row get the color-rule-id from the row and apply its value on SKU for car_sku_id returned in that row .<br/>
	 * 4.2 While doing Step 4 at each iteration this method also writes the same entry in to the generated BMI file for these SKUs' attributes 
	 * along with their values.<br/>
     * 5.Once all iterations are processed, make cmm_rule_changed column/property to false of those color rules. (So next day when job runs these
     *  rules will not be picked up to match SKUs).<br/>
     * 6.FTP the File to the server.<br/>
     * 7.And file is passed for archiving for which it is copied /moved to the specified archiving directory by appending time stamp to the name.<br/>
 	 * Note:1.If an error occurs at STEP-4.2 or 4.3 i.e. while updating or writing to the file; then it logs it and sends the notification via
 	 *  email to the configured address. So the record is written to the file (i.e. STEP-4) only if updating of SKU's attribute was successful 
 	 *  (i.e. STEP 3 was successful). <br/> 
	 *     :2.The step 4 is performed in two steps: 1st By identifying each that SKU(ie.sku whose color_rule_id is not null) whose super color
	 *      has to be updated to new value and update there,then 2nd is by identifying SKUs(ie.skus whose color_rule_id=null) where super color
	 *       is to be 'inserted'.  
	 * @author Yogesh.Vedak
	 */
	@Override
	public void resynchSuperColor1OfVendorSkus() throws IOException,CarJobDetailException {
		boolean hasErrors = false;
		boolean recordWasFound=false; 
		try{
			if(log.isInfoEnabled()){
				log.info("_____________________###___Start of Cron Job for SUPER COLOR___###_________________");
			}
			Map<String,String> failedSkuMap = new Hashtable<String, String>();
			List<String> ruleChangedFalseList = new ArrayList<String>();
			Config exportDirConfig = (Config) lookupManager.getById(Config.class, Config.COLORBMI_EXPORT_DIRECTORY);
			Config exportFileConfig = (Config) lookupManager.getById(Config.class, Config.COLORBMI_EXPORT_FILENAME);
			String fileName =  DateUtils.formatDate(new Date(), "MM_dd_yyyy_HH_mm_ss")+"_"+exportFileConfig.getValue();
			BMIFileGenerationUtil.FileSession fileSession = BMIFileGenerationUtil.getFileSession(exportDirConfig.getValue(), fileName);
			File bmiFile = fileSession.getFile();
			try{
				getCarManager().bulkRemoveSuperColor1FromSkus(); // REMOVE FROM THE CAR_SKU_ATTRIBUTE table
				fileSession = writeRecordForDeletedColorRules(fileSession);
				getCarManager().clearColorRuleReferencesFromSku(Constants.SUPERCOLOR_STATUS_DELETED);  //set color_rule_id NULL in VENDOR_SKU for all the DELETED colors
				getSuperColorManager().removeDeletedSuperColorsFromSystem(); // REMOVE ALL DELETED STATUS COLOR CODES FROM THE COLOR_MAPPING_MASTER 	
				boolean synchInsertingSuperColor = false;
				for(int insertUpdate=0;insertUpdate<=1;insertUpdate++){ 
					String activity = synchInsertingSuperColor?"Inserting Super Color 1":"Updating Super Color 1";//debug purpose
					if(log.isDebugEnabled()){
						log.debug(activity+"Phase started:");
					}
					int batchForIterations = getSuperColorRulesUtils().getIterationCount(getSuperColorManager().getSuperColorSynchViewRowCount(synchInsertingSuperColor).intValue(), getColorSynchBatchSize());
					int rowsOffset = 0;	
					boolean writeRecordToFile = false;
					int cnt=1;//counter for just debug & testing purpose
					forLoopBatchIteration:
					for(int i=1;i<=batchForIterations;i++){
						List <SuperColorSynchDataHolderView> setColorRuleOnSkuList = new ArrayList<SuperColorSynchDataHolderView>();  
						List <SuperColorSynchDataHolderView> synchDataList = getSuperColorManager().getVendorSkuSuperColorUpdationData(rowsOffset,getColorSynchBatchSize(),synchInsertingSuperColor); 
						int[] rowResultFlags = new int[getColorSynchBatchSize()] ;
						try{
							if(synchDataList !=null && !synchDataList.isEmpty()){
								rowResultFlags = executeSkuSynchBatchForSuperColor(synchDataList,synchInsertingSuperColor);
							}else{
								break;
							}
						}catch (BatchUpdateException bue) {
							try{
								rowResultFlags = executeSkuSynchBatchForSuperColor(synchDataList,synchInsertingSuperColor); //what to do..should we save in a list and process here again cause all will be rollback as exception has come
							}catch (BatchUpdateException e) {
								hasErrors=true;
								for(SuperColorSynchDataHolderView failedViewRow: synchDataList){
									if(log.isDebugEnabled()){
										log.info("Super Color 1 synch failed for sku with CarSkuId:"+failedViewRow.getCarSkuId());
									}
									failedSkuMap = getQuartzJobCommonUtil().collectSkuForNotification(failedSkuMap, failedViewRow, e);
								}
								rowsOffset = rowsOffset + getColorSynchBatchSize(); 
								continue forLoopBatchIteration ; //directly go to next batch-iteration as this batch failed 2nd time too and has been considered for emailNotification 
							}
						}catch (Exception e) {
							hasErrors=true;
							processException("Super Color Synch Job-Batch failed--",e);
						}
						int rowFlagsSize = rowResultFlags.length;
						SuperColorSynchDataHolderView currentSkuViewRow = null;
						for (int j=0;j<rowFlagsSize;j++){
							try{
								int currentRowFlagVal= rowResultFlags[j];
								currentSkuViewRow = synchDataList.get(j);
								recordWasFound=true;   			//record is/are found ; now error may come or may not while updating or writing based on which hasError flag will be set but recordWasFound=T can decide if to enter into ftping .
								if(getQuartzJobCommonUtil().isStatementExecutedSuccessfully(currentRowFlagVal)){
									writeRecordToFile=true;
									ruleChangedFalseList = getQuartzJobCommonUtil().collectRuleToChangeStatus(ruleChangedFalseList, currentSkuViewRow.getCmmId());
									setColorRuleOnSkuList.add(currentSkuViewRow);
									if(writeRecordToFile){
										if(log.isDebugEnabled()){
											log.debug(cnt+"th Record writing for "+activity+".SKU is:"+currentSkuViewRow.getCarSkuId());
										}
										fileSession.write(getRecordAsString(currentSkuViewRow));
										writeRecordToFile=false;
										cnt = cnt+1;
									}
								}else{ 
									if(!synchInsertingSuperColor){
										if(log.isDebugEnabled()){
											log.info("Update Action:Super Color 1 synch failed for sku with CarSkuId:"+currentSkuViewRow.getCarSkuId());
										}
									}else{
										if(log.isDebugEnabled()){
											log.info("Insert Action:Super Color 1 synch failed for sku with CarSkuId:"+currentSkuViewRow.getCarSkuId());
										}
									}
									failedSkuMap = getQuartzJobCommonUtil().collectSkuForNotification(failedSkuMap, currentSkuViewRow, new BatchUpdateException());
								}
								if(log.isDebugEnabled()){
									log.debug(j+"th row is over--|");
								}
							}catch(IOException e){ //exception is caught here if writing to file was failed. So as to still continue for next for-loop's iteration.
								hasErrors=true;
								failedSkuMap = getQuartzJobCommonUtil().collectSkuForNotification(failedSkuMap, currentSkuViewRow, e);
							}catch(Exception e){ //exception is caught here so synch process would  continue for next for-loop's iteration
								hasErrors=true;
								failedSkuMap = getQuartzJobCommonUtil().collectSkuForNotification(failedSkuMap, currentSkuViewRow, e);
							}
						} //synch-data VIEW-rows for-loop ends
						if(log.isDebugEnabled()){
							log.debug(i+"th BATCH iteration completed!");
						}
						rowsOffset = rowsOffset + getColorSynchBatchSize();
						if(log.isDebugEnabled()){
							log.debug("Offeset incremented to "+rowsOffset);
						}
						updateColorRulesOnSkus(setColorRuleOnSkuList);
					}//batch-for-loop ends
					synchInsertingSuperColor = true;
				}//-insert-update phase ends
			}finally {
				fileSession.close();
			}
			updateSizeRulesChangeStatus(ruleChangedFalseList);
			if (bmiFile.exists()) {
				if (!hasErrors && recordWasFound) {
					//getQuartzJobCommonUtil().ftpFile(bmiFile, Config.BMI_EXPORT_FTP_HOST, Config.BMI_EXPORT_FTP_USERNAME, Config.BMI_EXPORT_FTP_PASSWORD, Config.BMI_EXPORT_FTP_REMOTE_DIRECTORY);
					//FileTransferUtil.transferFiles(hostName, userName, passwd, remoteDir, fileName, ignoreHostKey)
					Config cfgFtpHost = (Config) lookupManager.getById(Config.class, Config.BMI_EXPORT_FTP_HOST);
					Config cfgFtpUserId = (Config) lookupManager.getById(Config.class, Config.BMI_EXPORT_FTP_USERNAME);
					Config cfgFtpPassword = (Config) lookupManager.getById(Config.class, Config.BMI_EXPORT_FTP_PASSWORD);
					Config cfgFtpRemoteDir = (Config) lookupManager.getById(Config.class, Config.BMI_EXPORT_FTP_REMOTE_DIRECTORY);
					FileTransferUtil.transferFiles(cfgFtpHost.getValue(),cfgFtpUserId.getValue() ,cfgFtpPassword.getValue() ,cfgFtpRemoteDir.getValue(),
							bmiFile, true);
					Config completedDirectory = (Config) lookupManager.getById(Config.class, Config.BMI_FILE_COMPLETED_DIR_NAME);//Process File to archive directory ie BMI_FILE_COMPLETED_DIR_NAME 
					BMIFileGenerationUtil.processCompletedFile(exportDirConfig.getValue(), fileName, completedDirectory.getValue(),true,false);
				}else if(hasErrors){
					getQuartzJobCommonUtil().sendFailedSkusNotification(failedSkuMap,"SKU Super-Color-Synch Batch Process");  
					Config completedDirectory = (Config) lookupManager.getById(Config.class, Config.BMI_FILE_ERR_DIR_NAME);
					BMIFileGenerationUtil.processCompletedFile(exportDirConfig.getValue(), fileName, completedDirectory.getValue(),true,false);
				}
			}
		}catch (Exception e) {
			if(log.isInfoEnabled()){
			log.info("_____Exception in an execution of Super Color Synch Job"); //log exception saying synch job failed
			}
			processException("Super Color Synch Job",e);
		}
		if(log.isInfoEnabled()){
			log.info("_____________________###___End of Cron Job for SUPER COLOR___###_________________");
		}
	}

	@Override
	public void processException(String processName, Exception ex) {
		if(log.isDebugEnabled()){
			log.debug("Exception occured in Running QuartsJOb for SuperColor1 Synch");
		}
		getQuartzJobCommonUtil().sendSystemFailureNotification(processName,ex);
	}

	private String getRecordAsString(SuperColorSynchDataHolderView synchData){
		StringBuffer recordString = new StringBuffer();
		recordString.append(BMIFileGenerationUtil.getObjectAttributeImportRecord(Constants.ELEMENT_SKU,synchData.getBelkUpc(),Constants.ATTR_SUPERCOLOR1,synchData.getSuperColorName()));
		return recordString.toString();
	}
	private void updateSizeRulesChangeStatus(List<String> ruleChangedFalseList) throws SQLException{
		int size = ruleChangedFalseList.size();
		int[] colorRulesExecutionFlags = new int[size];
		try{
			colorRulesExecutionFlags =  getSuperColorManager().executeSuperColorRuleChangedBatch(ruleChangedFalseList);
		}catch (BatchUpdateException e) {
			for(int i=0;i<size;i++){
				int curRow = colorRulesExecutionFlags[i];
				if(!getQuartzJobCommonUtil().isStatementExecutedSuccessfully(curRow)){
					if(log.isDebugEnabled()){
						log.debug("\n \n ------------The size rule with id "+ruleChangedFalseList.get(i)+"was not updated to false for its 'rule_changed' status ");
					}
				}
			}
		}
	}

	private void updateColorRulesOnSkus(List<SuperColorSynchDataHolderView> colorRuleOnSku) throws SQLException{
		
		if(colorRuleOnSku !=null && !colorRuleOnSku.isEmpty()){
		int size = colorRuleOnSku.size();
		int[] colorRulesExecutionFlags = new int[size];
		try{
			colorRulesExecutionFlags =  getCarManager().executeBatchSettingColorRuleOnSkus(colorRuleOnSku);
		}catch (BatchUpdateException e) {
			for(int i=0;i<size;i++){
				SuperColorSynchDataHolderView viewRow = colorRuleOnSku.get(i);
				int curRow = colorRulesExecutionFlags[i];
				if(getQuartzJobCommonUtil().isStatementExecutedSuccessfully(curRow)){
					//log.info("The vendor sku was  updated for its color rule id that has been applied on it");  no need to log for every updation
				}
				else{
					if(log.isDebugEnabled()){
						log.debug("The following vendor sku was not updated for its color rule id that has been applied on it"); //logging for failed ones
						log.debug("CarSkuId:"+viewRow.getCarSkuId()+"For ColorRuleId:"+viewRow.getCmmId());
					}
				}
			}
		}
		}
	}

	public int[] executeSkuSynchBatchForSuperColor(List<SuperColorSynchDataHolderView> colorViewRows, boolean batchInsertSuperColorCase) throws SQLException, IOException{
		int[] flagArray = new int[colorViewRows.size()];
		if(batchInsertSuperColorCase){
			flagArray = getCarManager().executeSkuSynchBatchInsertForSuperColor(colorViewRows);
		}else{
			flagArray = getCarManager().executeSkuSynchBatchUpdateForSuperColor(colorViewRows);
		}
		return flagArray;
	}
	private BMIFileGenerationUtil.FileSession writeRecordForDeletedColorRules(BMIFileGenerationUtil.FileSession fileSessionTowrite) throws IOException{
		int count = getCarManager().getSkuCountRefrencingDeletedColorRules().intValue();
		int batchIterations = getSuperColorRulesUtils().getIterationCount(count, getDeletedColorSynchBatchSize());
		int offset = 0 ;
		int counter=0;
		for(long i=1;i<=batchIterations;i++){
			List<String> upcList = getCarManager().getBelkUpcOFSkuReferencingDeletedColorRules(offset,getDeletedColorSynchBatchSize());
			for(String sku : upcList){
				counter= counter +1;
				if(log.isDebugEnabled()){
					log.debug("\n \n"+counter+"th record writing for deleted SuperColor1:"+BMIFileGenerationUtil.getObjectAttributeImportRecord(Constants.ELEMENT_SKU, sku, Constants.ATTR_SUPERCOLOR1, Constants.ATTR_NULL_VAL));
				}
				fileSessionTowrite.write(BMIFileGenerationUtil.getObjectAttributeImportRecord(Constants.ELEMENT_SKU, sku, Constants.ATTR_SUPERCOLOR1, Constants.ATTR_NULL_VAL));
			}
			offset = offset + getDeletedColorSynchBatchSize();
		}
		return fileSessionTowrite;
	}
	
	int getColorSynchBatchSize(){
		String colorSynchBatch =  getQuartzJobCommonUtil().getQuartzJobPropertyMapFromFile().get(Constants.SCOLOR1_SYNCH_BATCHSIZE);
		return new Integer(colorSynchBatch).intValue();
	}
	int getDeletedColorSynchBatchSize(){
		String delSColorSynchBatch =  getQuartzJobCommonUtil().getQuartzJobPropertyMapFromFile().get(Constants.BATCH_SIZE_DELETED_COLORS_SYNCH);
		return new Integer(delSColorSynchBatch).intValue();
	}
}

