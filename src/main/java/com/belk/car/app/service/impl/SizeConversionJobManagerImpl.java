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
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.service.UserManager;

import com.belk.car.app.Constants;
import com.belk.car.app.exceptions.CarJobDetailException;
import com.belk.car.app.exceptions.IExceptionProcessor;
import com.belk.car.app.model.Config;
import com.belk.car.app.model.ContentStatus;
import com.belk.car.app.model.SizeConversionMaster;
import com.belk.car.app.model.SizeSynchDataHolderView;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.EmailManager;
import com.belk.car.app.service.OutfitManager;
import com.belk.car.app.service.SizeConversionJobManager;
import com.belk.car.app.service.SizeConversionManager;
import com.belk.car.app.util.BMIFileGenerationUtil;
import com.belk.car.app.util.FileTransferUtil;
import com.belk.car.app.util.QuartzJobCommonUtil;
import com.belk.car.app.util.SizeConversionRulesUtils;
import com.belk.car.util.DateUtils;
import com.belk.car.util.GenericUtils;

/**
 * SizeConversionJobManager's impl class for Size Conversion related batch jobs processing.
 * @author Yogesh.Vedak
 */
public class SizeConversionJobManagerImpl implements SizeConversionJobManager, IExceptionProcessor {


	private static String NEW_LINE = "\r\n";
	private static transient final Log log = LogFactory.getLog(SizeConversionJobManagerImpl.class);
	private CarLookupManager lookupManager;
	
	public CarLookupManager getLookupManager() {
		return lookupManager;
	}

	public void setLookupManager(CarLookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}

	private SizeConversionManager sizeConversionManager;
	public SizeConversionManager getSizeConversionManager() {
		return sizeConversionManager;
	}

	public void setSizeConversionManager(SizeConversionManager sizeConversionManager) {
		this.sizeConversionManager = sizeConversionManager;
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

	private SizeConversionRulesUtils sizeConversionRulesUtils;

	public SizeConversionRulesUtils getSizeConversionRulesUtils() {
		return sizeConversionRulesUtils;
	}

	public void setSizeConversionRulesUtils(
			SizeConversionRulesUtils sizeConversionRulesUtils) {
		this.sizeConversionRulesUtils = sizeConversionRulesUtils;
	}

	private EmailManager sendEmailManager;

	public EmailManager getSendEmailManager() {
		return sendEmailManager;
	}

	public void setSendEmailManager(EmailManager sendEmailManager) {
		this.sendEmailManager = sendEmailManager;
	}

	private UserManager userManager;


	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	private QuartzJobCommonUtil quartzJobCommonUtil;

	public QuartzJobCommonUtil getQuartzJobCommonUtil() {
		return quartzJobCommonUtil;
	}

	public void setQuartzJobCommonUtil(QuartzJobCommonUtil quartzJobCommonUtil) {
		this.quartzJobCommonUtil = quartzJobCommonUtil;
	}

	/**
	 * 
	 * <B>Description:</B> This method is invoked by quartz scheduler as a batch job process.<br/>
	 * Algorithm : It simply performs the following steps(either using direct oracle-store-procedure based approach or by processing manually from java-side):<br/>
	 * 1.First identify the deleted-status size rules from SIZE_CONVERSION_MASTER table and nullify their references from Vendor_Sku table that might have been set as foreign keys.<br/>
	 *	 (Here having null on size_rule_id column for these SKUs says  that either the size names are not available in size rule table as they must be new SKUs came in or particular size rule has been removed from the system by admin so these SKUs need to be eventually considered for new size-synch value.Also when size-name is manually edited on EDIT CAR Screen its sizeRule_id is set to null ).<br/>
	 * 2.Then remove completely (hard delete) all those size rules from rule table which were of deleted-status.(Just cleaning activity)<br/>
	 * 3.Then get the actual synch-data as view objects to synchronize size name values of SKUs .<br/>
	 *   (This view i.e. named V_SKU_SIZE_RULE actually decides which rule to be applied on which SKU and simply returns the rows of their combination.)<br/>
	 * 3.1 Here we iterate over rows and at each row get the size rule id from the row and apply its value on SKU for car_sku_id returned in that row .<br/>
	 * 4.While doing Step 3 at each iteration this method also writes the same entry in to the generated BMI file for these SKUs' attributes along with their values.<br/>
	 * 5.Once all iterations are processed, make scm_rule_changed column/property to false of those size rules. (So next day when job runs these will not be picked up to mach SKUs).<br/>
	 * 6.Now identify SKUs for which its size_rule_id column value is null. Get their size names and add them in size rule table i.e. in SIZE_CONVERSION_MASTER as new rules . Inform about the same by sending the notification via email for the same.<br/>
	 * 7.FTP the File to the server.<br/>
	 * 8.And file is passed for archiving for which it is copied /moved to the specified archiving directory by appending time stamp to the name.<br/>
	 * Note: If an error occurs at STEP-3 or 4 i.e. while updating or writing to the file; then it logs it and sends the notification via email to the configured address. So the record is written to the file (i.e. STEP-4) only if updating of SKU's attribute was successful (i.e. STEP 3 was successful). <br/> 
	 * @author Yogesh.Vedak
	 */
	//@Transactional(noRollbackFor = RuntimeException.class)
	@Override
	public void resynchSizeValuesOfVendorSkus() throws CarJobDetailException {
		if(log.isInfoEnabled()){
			log.info("_____________________###___Started Cron Job for SIZE____###_________________");
		}
		boolean  hasErrors=false;
		boolean recordWasFound=false;

		try{
			List<String> ruleChangedFalseList = new ArrayList<String>();
			Map<String,String> failedSkuMap = new Hashtable<String, String>();
			Config exportDirConfig = (Config) lookupManager.getById(Config.class, Config.SIZEBMI_EXPORT_DIRECTORY);
			Config exportFileConfig = (Config) lookupManager.getById(Config.class, Config.SIZEBMI_EXPORT_FILENAME);
			String fileName = DateUtils.formatDate(new Date(), "MM_dd_yyyy_HH_mm_ss") +"_"+exportFileConfig.getValue();
			BMIFileGenerationUtil.FileSession fileSession = BMIFileGenerationUtil.getFileSession(exportDirConfig.getValue(), fileName);
			File bmiFile = fileSession.getFile();
			try{
				if(getSizeConversionManager().getSizeSynchViewRowCount()>getSizeThreshold()){//store-procedure based synch processing if records are too large in size to be processed
					if(log.isInfoEnabled()){
						log.info("Direct db-store-procedure based synch approach Selected:");
					}
					fileSession = performDirectSynchInDB(exportDirConfig.getValue(),fileName);  //this approach makes a call to store-procedure and handles the synch and then tmp_size_rules table is populated get the records for BMI generation manually and writes it into the file.
					recordWasFound = true;
				}else{//manual processing from java side to synch SKU size values step by step
					if(log.isDebugEnabled()){
						log.info("Manual synch approach selected:");
					}
					getCarManager().clearSizeRuleReferencesFromSku(Constants.SIZECONV_STATUS_DELETED); 		//this is HQL to clear the references  
					getSizeConversionManager().removeDeletedSizeConversionsFromSystem();					//remove size rule permanently which are in status "D"
					int batchIterations = getSizeConversionRulesUtils().getIterationCount(getSizeConversionManager().getSizeSynchViewRowCount().intValue(), getSizeSynchBatchSize());
					int rowsOffset = 0;	
					boolean writeRecordToFile = false;
					forLoopBatchIteration:
						for(int i=1;i<=batchIterations;i++){
							List<SizeSynchDataHolderView> synchDataList = getSizeConversionManager().getVendorSkuSizeUpdationData(rowsOffset,getSizeSynchBatchSize());    //get size synch data from view 
							//for(SizeSynchDataHolderView view :synchDataList){
							//}
							int[] rowResultFlags = new int[synchDataList!=null?synchDataList.size():0] ;
							try{
								rowResultFlags = getCarManager().executeSkuSynchBatchUpdateForSize(synchDataList);
							}catch (BatchUpdateException bue) {
								try{
									rowResultFlags = getCarManager().executeSkuSynchBatchUpdateForSize(synchDataList); //what to do..should we save in a list and process here again cause all will be rollback as exception has come
								}catch (BatchUpdateException e) {
									hasErrors=true;
									for(SizeSynchDataHolderView failedViewRow: synchDataList){
										getQuartzJobCommonUtil().logSizeSynchFailed(failedViewRow.getCarSkuId(),failedViewRow.getSizeName(),failedViewRow.getScmConversionSizeName());
										failedSkuMap.put(String.valueOf(failedViewRow.getCarSkuId()),GenericUtils.getExceptionAsString(e));
									}
									rowsOffset = rowsOffset + getSizeSynchBatchSize(); 
									continue forLoopBatchIteration; //directly go to next batch-iteration as this batch failed 2nd time too and has been considered for emailNotification
								}
							}catch (Exception e) {
								processException("Size Synch Job-Batch failed--",e);
							}
							int rowFlagsSize = rowResultFlags.length;
							SizeSynchDataHolderView currentSkuViewRow = null;
							for (int j=0;j<rowFlagsSize;j++){
								try{
									int currentRowFlagVal= rowResultFlags[j];
									currentSkuViewRow = synchDataList.get(j);
									recordWasFound=true;   			//record is/are found ; now error may come or may not while updating or writing based on which hasError flag will be set but recordWasFound=T can decide if to enter into ftping .  
									if(getQuartzJobCommonUtil().isStatementExecutedSuccessfully(currentRowFlagVal)){
										if(log.isDebugEnabled()){
											log.debug("Record was updated for carSkuId :"+currentSkuViewRow.getCarSkuId() +" sucessfully ");
										}
										writeRecordToFile=true;
										ruleChangedFalseList = getQuartzJobCommonUtil().collectRuleToChangeStatus(ruleChangedFalseList, currentSkuViewRow.getScmId());
										// only send the skus which are in CMP
										if(log.isDebugEnabled()){
											log.info("Skus CAR Contenet Stauts:"+currentSkuViewRow.getContentStatus());
										}
										if(writeRecordToFile && (currentSkuViewRow.getContentStatus().equals(ContentStatus.SENT_TO_CMP))){
											fileSession.write(getRecordAsString(currentSkuViewRow));
											writeRecordToFile=false;
										}
									}else{ 
										// comes here  if value is '-3'ie Statement.EXECUTE_FAILED OR value other than 0 or -2 
										getQuartzJobCommonUtil().logSizeSynchFailed(currentSkuViewRow.getCarSkuId(),currentSkuViewRow.getSizeName(),currentSkuViewRow.getScmConversionSizeName()); // doing log.info
										failedSkuMap.put(String.valueOf(currentSkuViewRow.getCarSkuId()),GenericUtils.getExceptionAsString(new BatchUpdateException()));
									}
									if(log.isDebugEnabled()){
										log.debug(i+"th row is over--|");
									}
								}catch(IOException e){ //exception is caught here if writing to file was failed. So as to still continue for next for-loop's iteration.
									hasErrors=true;
									failedSkuMap.put(String.valueOf(currentSkuViewRow.getCarSkuId()), GenericUtils.getExceptionAsString(e));
								}catch(Exception e){ //exception is caught to let synch process continue for next for-loop's iteration
									hasErrors=true;
									failedSkuMap.put(String.valueOf(currentSkuViewRow.getCarSkuId()),GenericUtils.getExceptionAsString(e));
								}
							} //synch-data VIEW for-loop ends
							if(log.isDebugEnabled()){
								log.debug(i+"th Batch iteration completed");
							}
							rowsOffset = rowsOffset + getSizeSynchBatchSize();
							if(log.isDebugEnabled()){
								log.debug("Offeset incremented to:"+rowsOffset);
							}
						} // batch-size iteration for loop ends 
					if(log.isWarnEnabled()){
						log.warn("If file exists??"+bmiFile.exists());
					}
					updateSizeRulesChangeStatus(ruleChangedFalseList);
				}
			}finally {
				fileSession.close();
			}
			if (!hasErrors && recordWasFound) {
				if(log.isDebugEnabled()){
					log.debug("No Errors, Ftping file ...");
				}
				//getQuartzJobCommonUtil().ftpFile(bmiFile, Config.BMI_EXPORT_FTP_HOST, Config.BMI_EXPORT_FTP_USERNAME, Config.BMI_EXPORT_FTP_PASSWORD, Config.BMI_EXPORT_FTP_REMOTE_DIRECTORY);
				
				Config cfgFtpHost = (Config) lookupManager.getById(Config.class, Config.BMI_EXPORT_FTP_HOST);
				Config cfgFtpUserId = (Config) lookupManager.getById(Config.class, Config.BMI_EXPORT_FTP_USERNAME);
				Config cfgFtpPassword = (Config) lookupManager.getById(Config.class, Config.BMI_EXPORT_FTP_PASSWORD);
				Config cfgFtpRemoteDir = (Config) lookupManager.getById(Config.class, Config.BMI_EXPORT_FTP_REMOTE_DIRECTORY);
				FileTransferUtil.transferFiles(cfgFtpHost.getValue(),cfgFtpUserId.getValue() ,cfgFtpPassword.getValue() ,cfgFtpRemoteDir.getValue(),
						bmiFile, true);
				
				Config completedDirectory = (Config) lookupManager.getById(Config.class, Config.BMI_FILE_COMPLETED_DIR_NAME); //process completed file
				BMIFileGenerationUtil.processCompletedFile(exportDirConfig.getValue(), fileName, completedDirectory.getValue(),true,false);
			}else if(hasErrors){
				if(log.isInfoEnabled()){
					log.info("Got errors,so archieving file to transfer to error dir");
				}
				//Archive File to archive directory for Support Issues
				getQuartzJobCommonUtil().sendFailedSkusNotification(failedSkuMap,"SKU Size-Facet-Synch Batch Process");
				Config completedDirectory = (Config) lookupManager.getById(Config.class, Config.BMI_FILE_ERR_DIR_NAME);
				BMIFileGenerationUtil.processCompletedFile(exportDirConfig.getValue(),fileName, completedDirectory.getValue(),true,false);
			}
			if(log.isInfoEnabled()){
			log.info("----SizeConversionJobManagerImpl.bulkInsertNewSizeRules()----");
			}
			bulkInsertNewSizeRules(); 			 // we can try using direct 'SQL' query present in getCarManager().bulkInsertInSizeRuleForNewSkuSizeNames(); but which size names were inserted will not be available for sending their email-notification
		}catch(Exception e){
			log.error("resynchSizeValuesOfVendorSkus error",e);
			processException("Size Synch Job",e);
		}
		if(log.isInfoEnabled()){
			log.info("_____________________###____End of Cron Job for Size____###_________________");
		}
	}

	@Override
	public void processException(String name, Exception ex) {
		getQuartzJobCommonUtil().sendSystemFailureNotification(name,ex);
	}


	/**
	 * This method generates a record for current object as a String for all attributes present in the VIEW data
	 * @param synchData
	 * @return String of current record
	 */
	private String getRecordAsString(SizeSynchDataHolderView synchData){
		StringBuffer recordString = new StringBuffer();
		String belkUPC = synchData.getBelkUpc();
		recordString.append(BMIFileGenerationUtil.getObjectAttributeImportRecord(Constants.ELEMENT_SKU, belkUPC,Constants.CONVERTED_SIZE_NAME,synchData.getScmConversionSizeName()==null?Constants.ATTR_NULL_VAL:synchData.getScmConversionSizeName()));
		recordString.append(NEW_LINE);
		recordString.append(BMIFileGenerationUtil.getObjectAttributeImportRecord(Constants.ELEMENT_SKU, belkUPC,Constants.FACET_SIZE_1,synchData.getFacet1()==null?Constants.ATTR_NULL_VAL:synchData.getFacet1()));
		recordString.append(NEW_LINE);
		recordString.append(BMIFileGenerationUtil.getObjectAttributeImportRecord(Constants.ELEMENT_SKU, belkUPC,Constants.FACET_SIZE_2,synchData.getFacet2()==null?Constants.ATTR_NULL_VAL:synchData.getFacet2()));
		recordString.append(NEW_LINE);
		recordString.append(BMIFileGenerationUtil.getObjectAttributeImportRecord(Constants.ELEMENT_SKU,belkUPC,Constants.FACET_SIZE_3,synchData.getFacet3()==null?Constants.ATTR_NULL_VAL:synchData.getFacet3()));
		recordString.append(NEW_LINE);
		recordString.append(BMIFileGenerationUtil.getObjectAttributeImportRecord(Constants.ELEMENT_SKU, belkUPC,Constants.FACET_SUB_SIZE_1,synchData.getSubFacet1()==null?Constants.ATTR_NULL_VAL:synchData.getSubFacet1()));
		recordString.append(NEW_LINE);
		recordString.append(BMIFileGenerationUtil.getObjectAttributeImportRecord(Constants.ELEMENT_SKU, belkUPC,Constants.FACET_SUB_SIZE_2,synchData.getSubFacet2()==null?Constants.ATTR_NULL_VAL:synchData.getSubFacet2()));
		return recordString.toString(); 
	}

	private void updateSizeRulesChangeStatus(List<String> ruleChangedFalseIds) throws SQLException{
		int size = ruleChangedFalseIds.size();
		int[] sizeRulesExecutionFlags = new int[size];
		try{
			sizeRulesExecutionFlags =  getSizeConversionManager().executeSizeRuleChangedFalseBatch(ruleChangedFalseIds);
		}catch (BatchUpdateException e) {
			for(int i=0;i<size;i++){
				int curRow = sizeRulesExecutionFlags[i];
				if(!getQuartzJobCommonUtil().isStatementExecutedSuccessfully(curRow)){
					if(log.isDebugEnabled()){
						log.debug("The size rule with id "+ruleChangedFalseIds.get(i)+"was not updated to false for its 'rule_changed' status ");
					}
				}
			}
		}
	}
	public List<SizeConversionMaster> prepareNewSizeRulesToInsert(){
		List<VendorSku> skus = getCarManager().getVendorSkusHavingNoSizeRule();
		List<SizeConversionMaster> newSizeNameList = new ArrayList<SizeConversionMaster>();
		for(VendorSku sku : skus ){
			String sizeName= sku.getSizeName();
			if(sizeName!=null && !"".equals(sizeName)){ // ignoring the null sizes or empty size names
			    	SizeConversionMaster sizeRule = new SizeConversionMaster();
					sizeRule.setSizeName(sizeName);
					sizeRule.setCoversionSizeName("");
					sizeRule.setStatusCode(Constants.SIZECONV_STATUS_PENDING); //setting status as P ie Pending. User would manually go and change conv-value after which status would be 'A'
					sizeRule.setCreatedBy(Constants.SCHEDULER_CREATEDBY);
					sizeRule.setCreatedDate(new Date());
					sizeRule.setUpdatedBy(Constants.SCHEDULER_UPDATEDBY);
					sizeRule.setUpdatedDate((new Date()));
					sizeRule.setDepartment(sku.getCar().getDepartment());
					sizeRule.setVendor(sku.getVendorStyle().getVendor());
					sizeRule.setClassification(sku.getVendorStyle().getClassification());
					sizeRule.setCarId(Long.toString(sku.getCar().getCarId()));
					newSizeNameList.add(sizeRule);
			}
		}
		return newSizeNameList;
	}


	public void bulkInsertNewSizeRules() throws SQLException {
		if(log.isInfoEnabled()){
		log.info("----SizeConversionJobManagerImpl.bulkInsertNewSizeRules() called-----");
		}
		List<SizeConversionMaster> preparedList =  prepareNewSizeRulesToInsert();
		int[] flagArray = new int[preparedList.size()];
		try{
			flagArray = getSizeConversionManager().executeNewSizeRuleBatchInsert(preparedList);
		}catch (BatchUpdateException bue) {
			if(log.isInfoEnabled()){
				log.info("BatchUpdateException: Inserting of new size rules in to the system failed for following Names:");
			}
			for(SizeConversionMaster rule :preparedList){
				if(log.isInfoEnabled()){
					log.info("Failed Size Name:"+rule.getSizeName());
				}
			}
			getQuartzJobCommonUtil().sendSystemFailureNotification("Batch-Insert of new size rules in size-synch job!", bue);
		}
		processNewSizeRulesExecution(flagArray,preparedList);
	}

	public void processNewSizeRulesExecution(int[] flagArray,List<SizeConversionMaster> newRules){
		int size = flagArray.length;
		List<String> newSizeNames = new ArrayList<String>();
		List<String> failedSizeNames = new ArrayList<String>();
		List<SizeConversionMaster> sucessSizes=new ArrayList<SizeConversionMaster>();
		List<SizeConversionMaster> failedSizes=new ArrayList<SizeConversionMaster>();
		for(int i=0;i<size;i++){
			SizeConversionMaster rule = newRules.get(i);
			if(getQuartzJobCommonUtil().isStatementExecutedSuccessfully(flagArray[i])){
				newSizeNames.add(rule.getSizeName());
				sucessSizes.add(rule);
			}else{
				failedSizeNames.add(rule.getSizeName());
				failedSizes.add(rule);
			}
		}
		//getQuartzJobCommonUtil().sendNewSizeNamesAddedNotification(newSizeNames,failedSizeNames);
		// passing newRules(instead of newSizeNames) to the email, since the Size rules not having any size conversion names
		getQuartzJobCommonUtil().sendSizeNamesAddedNotification(newRules,failedSizes);
	}

	/**
	 * This approach makes a call to store-procedure named process_size_rules() and handles the synch and then tmp_size_rules table is populated get the records for BMI generation manually and writes it into the file.
	 * @param dir
	 * @param fileName
	 * @return
	 * @throws IOException
	 * @author Yogesh.Vedak
	 */
	public BMIFileGenerationUtil.FileSession performDirectSynchInDB(String dir,String fileName) throws IOException{
		BMIFileGenerationUtil.FileSession fileSession = BMIFileGenerationUtil.getFileSession(dir, fileName);
		getCarManager().executeSizeSynchrnizationUsingStoreProedure();
		int batchIteration = getSizeConversionRulesUtils().getIterationCount(getCarManager().getTempSizeSynchCount().intValue(),getSizeSynchBatchSize());
		int offset = 0;
		for(int i=1;i<=batchIteration;i++)
		{
			List<String> recordList =  getCarManager().getSkuSizeSynchRecordForBMIGeneration(offset,getSizeSynchBatchSize());
			for(String record : recordList){
				fileSession.write(record);
			}
			offset = offset + getSizeSynchBatchSize();
		}
		return fileSession;
	}


	int getSizeThreshold(){
		String sizeThreshold =  getQuartzJobCommonUtil().getQuartzJobPropertyMapFromFile().get(Constants.MAX_SIZERECORDS_THRESHOLD);
		return new Integer(sizeThreshold).intValue();
	}
	
	int getSizeSynchBatchSize(){
		String sizeSynchBatch =  getQuartzJobCommonUtil().getQuartzJobPropertyMapFromFile().get(Constants.SIZE_SYNCH_BATCHSIZE);
		return new Integer(sizeSynchBatch).intValue();
	}
	
	/**
	 * This method invoked by ExportToCESFile for pre-cutover cars.  It will only
	 * trigger update for the set of skus provided.
	 */
    @Override
    public void resynchSizeValuesOfVendorSkus(Set<VendorSku> skus) {
        getCarManager().clearSizeRuleReferencesFromSku(Constants.SIZECONV_STATUS_DELETED);      //this is HQL to clear the references  
        getSizeConversionManager().removeDeletedSizeConversionsFromSystem();                    //remove size rule permanently which are in status "D"
        List<SizeSynchDataHolderView> synchDataList = sizeConversionManager
                .getVendorSkuSizeUpdationDataForSkus(skus);
        try {
            int[] rowResultFlags = new int[synchDataList!=null?synchDataList.size():0] ;
            rowResultFlags = getCarManager().executeSkuSynchBatchUpdateForSize(synchDataList);
            List<String> ruleChangedFalseList = new ArrayList<String>();
            SizeSynchDataHolderView currentSkuViewRow = null;
            for (int j=0;j<rowResultFlags.length;j++){
                int currentRowFlagVal= rowResultFlags[j];
                currentSkuViewRow = synchDataList.get(j);
                if(getQuartzJobCommonUtil().isStatementExecutedSuccessfully(currentRowFlagVal)){
                    ruleChangedFalseList = getQuartzJobCommonUtil().collectRuleToChangeStatus(ruleChangedFalseList, currentSkuViewRow.getScmId());
                }
            }
            updateSizeRulesChangeStatus(ruleChangedFalseList);
            bulkInsertNewSizeRules();
        } catch (SQLException e) {
            log.error("SQLException while executing SizeSync for Pre-cutover Car!!!");
            processException("SQLException while executing SizeSync for Pre-cutover Car!!!", e);
        } catch (IOException e) {
            log.error("IOException while executing SizeSync for Pre-cutover Car!!!");
            processException("SQLException while executing SizeSync for Pre-cutover Car!!!", e);
        }
    }

}

