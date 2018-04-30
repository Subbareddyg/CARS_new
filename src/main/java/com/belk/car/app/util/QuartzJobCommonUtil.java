package com.belk.car.app.util;

import java.io.File;
import java.io.IOException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;

import com.belk.car.app.Constants;
import com.belk.car.app.exceptions.SendEmailException;
import com.belk.car.app.model.Config;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.model.SizeConversionMaster;
import com.belk.car.app.model.SizeSynchDataHolderView;
import com.belk.car.app.model.SuperColorSynchDataHolderView;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.EmailManager;
import com.belk.car.app.service.impl.QuartzJobManagerImpl;
import com.belk.car.util.DateUtils;
import com.belk.car.util.GenericUtils;

/**
 * 
 * A util Class for maintaining general method to be used in classes implementing batch job functionality
 * @author Yogesh.Vedak
 *
 */
public class QuartzJobCommonUtil {
	private static transient final Log log = LogFactory.getLog(QuartzJobManagerImpl.class);
	private CarLookupManager lookupManager;

	public CarLookupManager getLookupManager() {
		return lookupManager;
	}

	public void setLookupManager(CarLookupManager lookupManager) {
		this.lookupManager = lookupManager;
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


	public List formatedExceptionMessage(Map <String,String> failedSkusForNotification){

		List<Map> skuAndExceptionList = new ArrayList();
		Iterator it1 = failedSkusForNotification.entrySet().iterator();
		while(it1.hasNext()){
			Map.Entry skuAndException = (Map.Entry)it1.next();
			Map<Object,Object> map = new HashMap();
			map.put("carSkuId", skuAndException.getKey());
			map.put("exception", skuAndException.getValue());
			skuAndExceptionList.add(map);
		}

		return skuAndExceptionList;
	}


	public void sendFailedSkusNotification(Map <String,String> failedSkusForNotification,String processName){
		if(failedSkusForNotification != null && !failedSkusForNotification.isEmpty()){
			List skuAndErrorMapList = formatedExceptionMessage(failedSkusForNotification);
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("skuAndErrorList", skuAndErrorMapList);
			sendNotification(processName, model,NotificationType.SKU_SYNCHJOB_FAILURE,Config.SCHEDULED_PROCESS_ERROR_NOTIFICATION_LIST_BMIWRITE);
		}
	} 


	/**
	 * General method to send notifications( based on arguments passed ) to configured email-Ids.
	 * 
	 * @param processName
	 * @param model
	 * @param NOTIFICATION_TYPE
	 * @param RECIEVERS
	 * @author Yogesh.Vedak
	 */
	public void sendNotification(String processName, Map<String,Object> model,final String NOTIFICATION_TYPE,final String RECIEVERS) {
		NotificationType type = lookupManager.getNotificationType(NOTIFICATION_TYPE);
		Config userName = (Config) lookupManager.getById(Config.class, Config.SYSTEM_USER);
		Config sendNotifications = (Config) lookupManager.getById(Config.class, Config.SEND_SIZEJOB_EMAIL_NOTIFICATIONS);
		Config emailNotificationList = (Config) lookupManager.getById(Config.class, RECIEVERS);
		User systemUser = this.userManager.getUserByUsername(userName.getValue());
		String[] emails = StringUtils.split(emailNotificationList.getValue(), ",");
		
		for (String email : emails) {
			model.put("userEmail", email);
			model.put("processName", processName);
			model.put("executionDate", DateUtils.formatDate(new Date(), "MM/dd/yyyy HH:mm:ss"));
			try {
				if ("true".equalsIgnoreCase(sendNotifications.getValue())) {
					sendEmailManager.sendNotificationEmailReport(type, systemUser, model,false);
				}
			} catch (SendEmailException se) {
				if(log.isErrorEnabled()){
					log.error("Error when sending email to: " + email);
				}
			} catch (Exception ex) {
				if(log.isErrorEnabled()){
					log.error("General Exception occured. Cause: " + email);
				}
			}
		}

	}

	public void sendSystemFailureNotification(String processName, Exception ex){
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("exceptionContent", StringUtils.abbreviate(GenericUtils.getExceptionAsString(ex),4000));
		sendNotification(processName,model,NotificationType.SYSTEM_FAILURE,Config.SCHEDULED_PROCESS_ERROR_NOTIFICATION_LIST_BMIWRITE);
	}

	public boolean isStatementExecutedSuccessfully(int rowFlagValue){
		if(rowFlagValue > 0 || rowFlagValue == Statement.SUCCESS_NO_INFO){
			return true;
		}
		return false;
	}

	public void logSizeSynchFailed(long carSkuId,String sizeName,String convValue){
		StringBuffer info = new StringBuffer();
		info.append("Sku updation failed for CarSkuId: ");
		info.append(String.valueOf(carSkuId));
		info.append("while updating size name "+sizeName);
		info.append(" to '");
		info.append(convValue);
		info.append("'");
		if(log.isInfoEnabled()){
			log.info(info.toString());
		}
	}


	/**
	 *  General method to FTP the file.
	 * @param fileToExport
	 * @param ftpHost
	 * @param ftpUserId
	 * @param ftpPassword
	 * @param ftpRemoteDir
	 * @throws IOException
	 * @author Yogesh.Vedak
	 */
	public static void ftpFile(File fileToExport,Config ftpHost,Config ftpUserId,Config ftpPassword,Config ftpRemoteDir) throws IOException{
		if(fileToExport.exists()){
			if(ftpHost != null && StringUtils.isNotBlank(ftpHost.getValue())) {
				FTPClient ftp = FtpUtil.openConnection(ftpHost.getValue(), ftpUserId.getValue(), ftpPassword.getValue());
				FtpUtil.setTransferModeToAscii(ftp);
				FtpUtil.sendDataFiles(ftp, Arrays.asList(fileToExport), ftpRemoteDir.getValue());
				FtpUtil.closeConnection(ftp);
			}
		}

	}

	/**
	 * General method to FTP the file based on parameter values passed. It is expected that all these arguments are present in the Config Table in to the database
	 * @param file
	 * @param CONFIG_PARAM_FTPHOST
	 * @param CONFIG_PARAM_FTPUSERID
	 * @param CONFIG_PARAM_PWD
	 * @param CONFIG_PARAM_REMTDIR
	 * @throws IOException
	 * @author Yogesh.Vedak
	 */
	public void ftpFile(File file,final String CONFIG_PARAM_FTPHOST,final String CONFIG_PARAM_FTPUSERID,final String CONFIG_PARAM_PWD,String CONFIG_PARAM_REMTDIR)throws IOException{
		Config cfgFtpHost = (Config) lookupManager.getById(Config.class, CONFIG_PARAM_FTPHOST);
		Config cfgFtpUserId = (Config) lookupManager.getById(Config.class, CONFIG_PARAM_FTPUSERID);
		Config cfgFtpPassword = (Config) lookupManager.getById(Config.class, CONFIG_PARAM_PWD);
		Config cfgFtpRemoteDir = (Config) lookupManager.getById(Config.class, CONFIG_PARAM_REMTDIR);
		QuartzJobCommonUtil.ftpFile(file,cfgFtpHost, cfgFtpUserId, cfgFtpPassword, cfgFtpRemoteDir);
	}


	public void sendNewSizeNamesAddedNotification(List<String> newSizeListAdded,List<String> failedSizeList){
		if(newSizeListAdded != null && !newSizeListAdded.isEmpty()){
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("newSizeNameList", newSizeListAdded);
			model.put("failedSizeNameList", failedSizeList); //this can be utilized in vm template to send failed entries of adding a new size names. Currently it is not used in vm template so this list object can be set just null.
			sendNotification("Insertion of New Size name in the system",model,NotificationType.NEW_SIZES_ADDED,Config.SCHEDULED_PROCESS_NEW_SIZE_NOTIFICATION_LIST);
		}
	}

	public 	Map<String,String> collectSkuForNotification(Map<String,String> failedSkuMap,SuperColorSynchDataHolderView currentSkuViewRow,Exception e){
		Map<String,String> skuMap = failedSkuMap;
		if(currentSkuViewRow !=null && skuMap !=null){
			skuMap.put(String.valueOf(currentSkuViewRow.getCarSkuId()),GenericUtils.getExceptionAsString(e));
		}
		return skuMap;
	}

	public 	Map<String,String> collectSkuForNotification(Map<String,String> failedSkuMap,SizeSynchDataHolderView currentSkuViewRow,Exception e){
		Map<String,String> skuMap = failedSkuMap;
		if(currentSkuViewRow !=null && skuMap !=null){
			skuMap.put(String.valueOf(currentSkuViewRow.getCarSkuId()),GenericUtils.getExceptionAsString(e));
		}
		return skuMap;
	}
	public 	List<String> collectRuleToChangeStatus(List<String> existingIdList,long ruleId){
		List<String> ruleIdList = existingIdList;
		if (ruleIdList != null && !ruleIdList.contains(String.valueOf(ruleId))){
			ruleIdList.add(String.valueOf(ruleId));
		}
		return ruleIdList;
	}

	public  Map<String,String> getQuartzJobPropertyMapFromFile() {
		return GenericUtils.getPropertyMapFromFile(Constants.BATCHJOB_PROPERTYFILE_BASENAME, null);
	}
	
	public void sendSizeNamesAddedNotification(List<SizeConversionMaster> newSizeListAdded,List<SizeConversionMaster> failedSizeList){
		if(newSizeListAdded != null && !newSizeListAdded.isEmpty()){
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("newSizeNameList", newSizeListAdded);
			model.put("failedSizeNameList", failedSizeList); //this can be utilized in vm template to send failed entries of adding a new size names. Currently it is not used in vm template so this list object can be set just null.
			sendNotification("Insertion of New Size name in the system",model,NotificationType.NEW_SIZES_ADDED,Config.SCHEDULED_PROCESS_NEW_SIZE_NOTIFICATION_LIST);
		}
	}
}
