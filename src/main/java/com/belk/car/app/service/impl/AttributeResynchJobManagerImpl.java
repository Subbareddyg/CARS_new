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
import com.belk.car.app.service.AttributeManager;
import com.belk.car.app.service.AttributeResynchJobManager;

/**
 * AttributeResynchJobManager's impl class for Attribute Resynch related batch jobs processing.
 * @author 
 */
public class AttributeResynchJobManagerImpl implements AttributeResynchJobManager, IExceptionProcessor {


	private static String NEW_LINE = "\r\n";
	private static transient final Log log = LogFactory.getLog(AttributeResynchJobManagerImpl.class);
	
	
	
	private EmailManager sendEmailManager;

	public EmailManager getSendEmailManager() {
		return sendEmailManager;
	}

	public void setSendEmailManager(EmailManager sendEmailManager) {
		this.sendEmailManager = sendEmailManager;
	}

	private CarLookupManager lookupManager;

	public CarLookupManager getLookupManager() {
		return lookupManager;
	}
	
	public void setLookupManager(CarLookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}
	
	private CarManager carManager;
	public CarManager getCarManager() {
		return carManager;
	}

	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	
	private SizeConversionRulesUtils sizeConversionRulesUtils;

	public SizeConversionRulesUtils getSizeConversionRulesUtils() {
		return sizeConversionRulesUtils;
	}

	public void setSizeConversionRulesUtils(
			SizeConversionRulesUtils sizeConversionRulesUtils) {
		this.sizeConversionRulesUtils = sizeConversionRulesUtils;
	}

	private QuartzJobCommonUtil quartzJobCommonUtil;

	public QuartzJobCommonUtil getQuartzJobCommonUtil() {
		return quartzJobCommonUtil;
	}

	public void setQuartzJobCommonUtil(QuartzJobCommonUtil quartzJobCommonUtil) {
		this.quartzJobCommonUtil = quartzJobCommonUtil;
	}

	private AttributeManager attributeManager;

	public void setAttributeManager(AttributeManager attributeManager) {
		this.attributeManager = attributeManager;
	}

	public AttributeManager getAttributeManager() {
		return attributeManager;
	}

	/**
	 * 
	 * <B>Description:</B> This method is invoked by quartz scheduler as a batch job process.<br/>
	 * It simply executes the  oracle-store-procedure for the Attribute resysnch
	 */
	@Override
	public void resynchAttributes() throws IOException{
		final String EXTENSION= ".txt";
		if(log.isInfoEnabled()){
			log.info("_____________________###___Started Cron Job for ATTRIBUTE RESYNCH____###_________________");
		}

                boolean lockFileCreated = createLockFile(Config.ATTR_RESYNC_LOCK_PATH);
                if (!lockFileCreated) {
                    if (log.isErrorEnabled()) {
                        log.error("Exiting.  Another process is already running!");
                        log.info("_____________________###____End of Cron Job for Attribute Synch____###_________________");
		
                    }
                    processException("Attribute Synch Job",new Exception("Job execution prevented due to lockfile."));
                    return;
                }
                
		try{
                    
                    
                    
			List<String> ruleChangedFalseList = new ArrayList<String>();
			Map<String,String> failedSkuMap = new Hashtable<String, String>();
			Config exportDirConfig = (Config) lookupManager.getById(Config.class, Config.ATTR_RESYNCH_BMI_EXPORT_DIRECTORY);
			String fileName = System.currentTimeMillis()+EXTENSION;
			BMIFileGenerationUtil.FileSession fileSession = BMIFileGenerationUtil.getFileSession(exportDirConfig.getValue(), fileName);
			File bmiFile = fileSession.getFile();
			try{
				
                            if(log.isInfoEnabled()){
                                    log.info("Direct db-store-procedure based attribute synch approach:");
                            }
                            fileSession = performDirectSynchInDB(exportDirConfig.getValue(),fileName);  //this approach makes a call to store-procedure and handles the synch and then tmp_attribute_resync table is populated get the records for BMI generation manually and writes it into the file.
			
                        } finally {
				fileSession.close();
			}
			if(log.isDebugEnabled()){
					log.debug("No Errors, Ftping file ...");
				}
			Config cfgFtpHost = (Config) lookupManager.getById(Config.class, Config.BMI_EXPORT_FTP_HOST);
			Config cfgFtpUserId = (Config) lookupManager.getById(Config.class, Config.BMI_EXPORT_FTP_USERNAME);
			Config cfgFtpPassword = (Config) lookupManager.getById(Config.class, Config.BMI_EXPORT_FTP_PASSWORD);
			Config cfgFtpRemoteDir = (Config) lookupManager.getById(Config.class, Config.BMI_EXPORT_FTP_ATTRIBUTE_REMOTE_DIRECTORY);
			if(bmiFile.length() > 0){
				log.info("FTPing data file to CMP...");
				FileTransferUtil.transferFiles(cfgFtpHost.getValue(),cfgFtpUserId.getValue() ,cfgFtpPassword.getValue() ,cfgFtpRemoteDir.getValue(),
					bmiFile, true);
                                log.info("FTP Completed.");
			} else {
                            log.warn("No data to send to CMP - not FTPing any file");
                        }
				
			Config completedDirectory = (Config) lookupManager.getById(Config.class, Config.BMI_FILE_COMPLETED_DIR_NAME); //process completed file
			BMIFileGenerationUtil.processCompletedFile(exportDirConfig.getValue(), fileName, completedDirectory.getValue(),true,false);
			
			
		}catch(Exception e){
			processException("Attribute Synch Job",e);
		} finally {
                    deleteLockFile(Config.ATTR_RESYNC_LOCK_PATH);
                }
		if(log.isInfoEnabled()){
			log.info("_____________________###____End of Cron Job for Attribute Synch____###_________________");
		}
		
	}

	@Override
	public void processException(String name, Exception ex) {
		getQuartzJobCommonUtil().sendSystemFailureNotification(name,ex);
	}




	/**
	 * This approach makes a call to store-procedure named resync_attributes() and handles the synch and then tmp_attribute_resync table is populated get the records for BMI generation manually and writes it into the file.
	 * @param dir
	 * @param fileName
	 * @return
	 * @throws IOException
	 * @author 
	 */
	public BMIFileGenerationUtil.FileSession performDirectSynchInDB(String dir,String fileName) throws IOException{
		BMIFileGenerationUtil.FileSession fileSession = BMIFileGenerationUtil.getFileSession(dir, fileName);
		getAttributeManager().executeAttributeSynchronizationUsingStoreProcedure();
		int batchIteration = getSizeConversionRulesUtils().getIterationCount(getAttributeManager().getTempAttributeSynchCount().intValue(),getSizeSynchBatchSize());
		log.info("batchIteration=="+batchIteration);
		int offset = 0;
		for(int i=1;i<=batchIteration;i++)
		{
			List<String> recordList =  getAttributeManager().getAttributeSynchRecordForBMIGeneration(offset,getSizeSynchBatchSize());
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
     * This method creates the UpdateItem LockFile if it doesn't exist.
     * 
     * @return true if LockFile was successfully created.
     * @throws IOException 
     */
    private boolean createLockFile(String configName) throws IOException {
        if (log.isInfoEnabled()) {
            log.info("Creating LockFile: " + configName);
        }
        Config lockfilePathConfig = (Config) lookupManager.getById(Config.class, configName);
        if (lockfilePathConfig == null) {
            if (log.isErrorEnabled()) {
                log.error("Unable to create LockFile due to undefined param "+configName+" in CONFIG table!");
            }
            return false; // no path to create lockfile
        }
        String lockfilePath = lockfilePathConfig.getValue();
        File lockfile = new File(lockfilePath);
        return lockfile.createNewFile();
    }
    
    /**
     * This method deletes the UpdateItem LockFile if it exists.  
     * 
     * @return
     */
    private boolean deleteLockFile(String configName) {
        if (log.isInfoEnabled()) {
            log.info("Deleting LockFile: " + configName);
        }
        Config lockfilePathConfig = (Config) lookupManager.getById(Config.class, configName);
        if (lockfilePathConfig == null) {
            if (log.isErrorEnabled()) {
                log.error("Unable to delete LockFile due to undefined param "+configName+" in CONFIG table!");
            }
            return false; // no path to delete lockfile
        }
        String lockfilePath = lockfilePathConfig.getValue();
        File lockfile = new File(lockfilePath);
        return lockfile.delete();
    }


}

