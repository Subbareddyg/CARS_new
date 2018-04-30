package com.belk.car.app.service.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;

import com.belk.car.app.exceptions.IExceptionProcessor;
import com.belk.car.app.exceptions.SendEmailException;
import com.belk.car.app.model.Config;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.EmailManager;
import com.belk.car.app.service.QuartzJobManagerForVendorImages;
import com.belk.car.app.service.VendorImageManager;
import com.belk.car.app.service.WorkflowManager;
import com.belk.car.app.util.FtpUtil;
import com.belk.car.app.util.VendorImageXMLUtil;
import com.belk.car.util.DateUtils;

/**
 * 
 * @author AFUSY12
 * this is Quartz job manager for vendor images job 
 */
public class QuartzJobManagerForVendorImagesImpl implements QuartzJobManagerForVendorImages, IExceptionProcessor {

	private transient final Log log = LogFactory.getLog(QuartzJobManagerForVendorImagesImpl.class);
	private CarLookupManager lookupManager;
	private UserManager userManager;
	private CarManager carManager;
	private WorkflowManager workflowManager;
	private EmailManager sendEmailManager;
	private VendorImageManager vendorImageManager;
	
	
	public void setLookupManager(CarLookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	public void setWorkflowManager(WorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}

	public void setSendEmailManager(EmailManager sendEmailManager) {
		this.sendEmailManager = sendEmailManager;
	}
	
	public void setVendorImageManager(VendorImageManager vendorImageManager) {
		this.vendorImageManager = vendorImageManager;
	}

	
	/**
	 * This method will send the images and Image metadata via XML feed to RRD
	 * It will pickup all the images in TRANSIT status and will send it to RRD via XML feed for mechanical quality check
	 * All sent images will be marked as MQ_SENT  
	 * Note: This job should be finished before 9 PM EDT
	 */
	public void sendVendorImagesFeedForMCToRRD() {
		//to-do code
		if (log.isInfoEnabled()){
			log.info("---------------->  Begin exporting VendorImages XML feed for mechnical check <-----------------");
		}
		try{
			
			final String host = ((Config) lookupManager.getById(Config.class, Config.RRD_FTP_HOST)).getValue();
			final String userName = ((Config) lookupManager.getById(Config.class, Config.RRD_FTP_USERNAME)).getValue();
			final String password = ((Config) lookupManager.getById(Config.class, Config.RRD_FTP_PASSWORD)).getValue();
			final String remoteDirName = ((Config) lookupManager.getById(Config.class, Config.RRD_VENDORIMAGE_EXPORT_DIRECTORY)).getValue();
			final String localExportDirName = ((Config) lookupManager.getById(Config.class, Config.RRD_VENDORIMAGE_LOCAL_EXPORT_DIRECTORY)).getValue();
			/*
			final String host = "ftp.belk.com";
			final String userName = "RRDon";
			final String password = "New4you";
			final String remoteDirName = "test";
			final String localExportDirName = "C:\\cars\\data\\carsdata\\export_uploadImage\\";
			*/
			String strFileName = "CARStoRRD_VendorImageUpload_" + DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss") + ".xml";
			String strFilePath =  localExportDirName + strFileName;
			
			//call to util function to create and populate XML file
			VendorImageXMLUtil util = new VendorImageXMLUtil();
			util.setVendorImageManager(vendorImageManager);
			util.setLookupManager(lookupManager);
			util.setCarManager(carManager);
			util.createVendorImageUploadXML(strFilePath);

			File f = new File(strFilePath);
			if (f.exists()) {
				//FTP File to Server
				if (host != null) {
					FTPClient ftp = FtpUtil.openConnection(host, userName, password);
					ftp.setFileType(FTPClient.ASCII_FILE_TYPE);
					FtpUtil.sendDataFiles(ftp, Arrays.asList(f), remoteDirName);
					FtpUtil.closeConnection(ftp);
				}
				//Archive File to archive directory for Support Issues
				final String archiveDirName = ((Config) lookupManager.getById(Config.class, Config.RRD_FILE_COMPLETED_DIRECTORY_NAME)).getValue();
				processCompletedFile(localExportDirName, strFileName, archiveDirName, false);
			}

			if (log.isInfoEnabled()){
				log.info("---------------->  End exporting VendorImages XML feed for mechnical check <-----------------");
			}	
			// Get the BelkMacl delete directory
		    String belkMaclDir= vendorImageManager.getVendorImageUploadDir();
			// Delete RRD sent image from BelkMacl Directory
		    deleteRRDSentImages(belkMaclDir, "SENT");
	  }catch(Exception e){
		 log.error("error while sending uploaded VI XML file to RRD", e) ;
		 //e.printStackTrace();
		 processException("send Vendor Images XML Feed For MC To RRD",e);
	  }
	}

	/**
	 * This job will send todays DELETED images and vendor uploaded APPROVED/REJECTED images to RRD though XML file.
	 *  NOTE: RRD will remove the images from media compass if image is deleted/rejected in CARS
	 * 		  If vendor uploaded image is approved in CARS then that image will be available for creative check in MC.
	 */
	public void sendVendorImagesUpdateFeedToRRD() {
		if (log.isInfoEnabled()){
			log.info("---------------->  Begin exporting VendorImages update XML feed  <-----------------");
		}
		try{
			
			final String host = ((Config) lookupManager.getById(Config.class, Config.RRD_FTP_HOST)).getValue();
			final String userName = ((Config) lookupManager.getById(Config.class, Config.RRD_FTP_USERNAME)).getValue();
			final String password = ((Config) lookupManager.getById(Config.class, Config.RRD_FTP_PASSWORD)).getValue();
			final String remoteDirName = ((Config) lookupManager.getById(Config.class, Config.RRD_VENDORIMAGE_UPDATE_EXPORT_DIRECTORY)).getValue();
			final String localExportDirName = ((Config) lookupManager.getById(Config.class, Config.RRD_VENDORIMAGE_UPDATE_LOCAL_EXPORT_DIRECTORY)).getValue();
			Config lastJobRunTime = ((Config) lookupManager.getById(Config.class, Config.VENDORIMAGE_UPDATEFEED_LAST_RUN_TIME));

			//Uncomment below code when testing on local
			/*final String host = "ftp.belk.com";
			final String userName = "RRDon";
			final String password = "New4you";
			final String remoteDirName = "test";
			final String localExportDirName = "C:\\cars\\data\\carsdata\\export_uploadImage\\";
			*/
			
			String strLastJobRunDate = lastJobRunTime.getValue();
			String strFileName = "CARStoRRD_VendorImageUpdate_" + DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss") + ".xml";
			String strFilePath =  localExportDirName + strFileName;
			
			//call to util function to create and populate XML file
			VendorImageXMLUtil util = new VendorImageXMLUtil();
			util.setVendorImageManager(vendorImageManager);
			util.createVendorImageUpdateXML(strFilePath, strLastJobRunDate);
			
			File f = new File(strFilePath);
			if (f.exists()) {
				//FTP File to Server
				if (host != null) {
					FTPClient ftp = FtpUtil.openConnection(host, userName, password);
					ftp.setFileType(FTPClient.ASCII_FILE_TYPE);
					FtpUtil.sendDataFiles(ftp, Arrays.asList(f), remoteDirName);
					FtpUtil.closeConnection(ftp);
				}
				//Archive File to archive directory for Support Issues
				final String archiveDirName = ((Config) lookupManager.getById(Config.class, Config.RRD_FILE_COMPLETED_DIRECTORY_NAME)).getValue();
				processCompletedFile(localExportDirName, strFileName, archiveDirName, false);
				lastJobRunTime.setValue(DateUtils.formatDate(new Date(), "MM/dd/yyyy HH:mm:ss"));
				this.vendorImageManager.save(lastJobRunTime);
			}
			
			if (log.isInfoEnabled()){
				log.info("---------------->  End exporting VendorImages update XML feed <-----------------");
			}	
			
	  }catch(Exception e){
		 log.error("error while sending Vendor image update XML file to RRD", e) ;
		// e.printStackTrace();
		 processException("send Vendor Images update XML Feed",e);
	  }
	}
	
	
	/**
	 * This job will Read and process RRD Image history XML file.
	 * Each Vendor image received in this XML file will be marked as 'RECEIVED'
	 */
	public void importVendorImageHistoryFeed() {
		if (log.isInfoEnabled()){
			log.info("---------------->  Begin importing VendorImages history XML feed  <-----------------");
		}
		try{
			
			final String host = ((Config) lookupManager.getById(Config.class, Config.RRD_FTP_HOST)).getValue();
			final String userName = ((Config) lookupManager.getById(Config.class, Config.RRD_FTP_USERNAME)).getValue();
			final String password = ((Config) lookupManager.getById(Config.class, Config.RRD_FTP_PASSWORD)).getValue();
			final String remoteDirName = ((Config) lookupManager.getById(Config.class, Config.RRD_VENDORIMAGE_HISTORY_IMPORT_DIRECTORY)).getValue();
			final String localDirName = ((Config) lookupManager.getById(Config.class, Config.RRD_VENDORIMAGE_HISTOY_LOCAL_IMPORT_DIRECTORY)).getValue();
			final boolean deleteAfterGet = true;
			
			/*
			 * to run this job locally please uncomment below line of code
			final String host = "ftp.belk.com";
			final String userName = "RRDon";
			final String password = "New4you";
			final String remoteDirName = "test";
			final String localDirName = "C:\\cars\\data\\carsdata\\import_vendorImageHistory\\";
			*/
			
			//download all XML files from RRD FTP site and delete after getting those files
			try {
				FTPClient ftp = FtpUtil.openConnection(host, userName, password);
				FtpUtil.setTransferModeToAscii(ftp);
				FtpUtil.getDataFiles(ftp, remoteDirName, localDirName, deleteAfterGet);
				FtpUtil.closeConnection(ftp);
			} catch (IOException ioex) {
				log.error("Could not read files from the RRD FTP", ioex);
			}
			
			File dir = new File(localDirName);
			if(dir.isDirectory()){
				
				//filer to load only files from current directory
				FileFilter fileFilter = new FileFilter() {
					public boolean accept(File dir) {
						return (!dir.isDirectory());
					}
				};
				
				//get all the files from the directory to process
				File[] files = dir.listFiles(fileFilter);
				if(null != files && files.length>0){
					VendorImageXMLUtil util = new VendorImageXMLUtil();
					util.setVendorImageManager(vendorImageManager);
					util.setLookupManager(lookupManager);
					
					for(File f: files){
						try{
							//read and Process the image history file
							util.readAndProcessVendorImageHistoryFeed(f);	
							final String archiveDirName = ((Config) lookupManager.getById(Config.class, Config.RRD_FILE_COMPLETED_DIRECTORY_NAME)).getValue();
							
							//move file to archive directory for backup
							processCompletedFile(localDirName, f.getName(), archiveDirName, false);
							
					    }catch(Exception e){
					    	log.error("Error while processing the image history file : "+ f.getAbsolutePath() , e);  
					    	processException("receive Vendor Images history XML Feed",e);
					    }
						
					}
					
				}
			}
			
	  }catch(Exception e){
		 log.error("error while importing VendorImages history XML feed file from RRD", e) ;
		 processException("receive Vendor Images history XML Feed",e);
	  }	
	   if (log.isInfoEnabled()){
			log.info("---------------->  End importing VendorImages history XML feed <-----------------");
		}
		
	}
	
	/**
	 * This job will Read and process RRD Creative check Detail XML file.
	 * Each Vendor image Status with reason will be read and updated to database
	 */
	public void importVendorImageCheckFeedbackFromRRD() {
		if (log.isInfoEnabled()){
			log.info("---------------->  Begin Creative Check XML feed  <-----------------");
		}
		try{
			
			final String host = ((Config) lookupManager.getById(Config.class, Config.RRD_FTP_HOST)).getValue();
			final String userName = ((Config) lookupManager.getById(Config.class, Config.RRD_FTP_USERNAME)).getValue();
			final String password = ((Config) lookupManager.getById(Config.class, Config.RRD_FTP_PASSWORD)).getValue();
			final String remoteDirName = ((Config) lookupManager.getById(Config.class, Config.RRD_VENDORIMAGE_CHECK_FEEDBACK_IMPORT_DIRECTORY)).getValue();
			final String localDirName = ((Config) lookupManager.getById(Config.class, Config.RRD_VENDORIMAGE_CHECK_FEEDBACK_LOCAL_IMPORT_DIRECTORY)).getValue();
			final boolean deleteAfterGet = true;
			
			// * to run this job locally please uncomment below line of code
			/*final String host = "ftp.belk.com";
			final String userName = "RRDon";
			final String password = "New4you";
			final String remoteDirName = "test";*/
				
			//final String localDirName = "C:\\cars\\data\\carsdata\\import_vendorImageCheck\\";
					
			//download all XML files from RRD FTP site and delete after getting those files
			try {
				FTPClient ftp = FtpUtil.openConnection(host, userName, password);
				FtpUtil.setTransferModeToAscii(ftp);
				FtpUtil.getDataFiles(ftp, remoteDirName, localDirName, deleteAfterGet);
				FtpUtil.closeConnection(ftp);
			} catch (IOException ioex) {
				log.error("Could not read files from the RRD FTP", ioex);
			}
			
			File dir = new File(localDirName);
			if(dir.isDirectory()){
				
				//filer to load only files from current directory
				FileFilter fileFilter = new FileFilter() {
					public boolean accept(File dir) {
						return (!dir.isDirectory());
					}
				};
				
				//get all the files from the directory to process
				
				File[] files = dir.listFiles(fileFilter);
				if(null != files && files.length>0){
					VendorImageXMLUtil util = new VendorImageXMLUtil();
					util.setVendorImageManager(vendorImageManager);
					util.setLookupManager(lookupManager);
					util.setCarManager(carManager);
					util.setUserManager(userManager);
					for(File f: files){
						try{
							//read and Process the Creative Check Feedback Details file
							util.readAndProcessVendorImageCheckFeedbackDetails(f);	
							final String archiveDirName = ((Config) lookupManager.getById(Config.class, Config.RRD_FILE_COMPLETED_DIRECTORY_NAME)).getValue();
							
							//move file to archive directory for backup
							processCompletedFile(localDirName, f.getName(), archiveDirName, false);
							
					    }catch(Exception e){
					    	log.error("Error while processing the Creative Check XML feed : "+ f.getAbsolutePath() , e);  
					    	processException("receive Creative Check XML Feed",e);
					    }
						
					}
					
				}
			}
			
	  }catch(Exception e){
		 log.error("error while Reading CreativeCheckXML feed file from RRD", e) ;
		 processException("receive Vendor Images history XML Feed",e);
	  }	
	   if (log.isInfoEnabled()){
			log.info("---------------->  End Creative Check XML feed feed <-----------------");
		}
		
	}
	

	
	
	/**
	 * @param filePath : path of file which need to move
	 * @param fileName : file which needs to move
	 * @param destinationDir : destination dir of file
	 * @param copyFile : boolean asking do you want to copy file or move? 
	 * @return boolean defining success
	 * 
	 */
	private boolean processCompletedFile(String filePath, String fileName, String destinationDir, boolean copyFile) {
		// File to be moved
		File currentFile = new File(filePath + fileName);

		StringBuffer sb = new StringBuffer();
		//File.separator
		String destFileName = sb.append(filePath).append(destinationDir).append("/").append(DateUtils.formatDate(new Date(), "MM_dd_yyyy_HH_mm_ss"))
				.append("_").append(fileName).toString();

		if (log.isDebugEnabled())
			log.debug((copyFile ? "Copying" : "Moving") + " file from: " + currentFile + " to " + destFileName);

		//Create directory if it doesn't exist
		File destDir = new File(filePath + destinationDir);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		if (copyFile) {
			try {
				FileUtils.copyFile(currentFile, new File(destFileName));
			} catch (IOException ioex) {
				//log.debug("");
				ioex.printStackTrace();
			}
		} else {
			File destFile = new File(destFileName);
			boolean ren = currentFile.renameTo(destFile);
			if (!ren)
				log.debug("Could not rename file: " + currentFile.getAbsolutePath() + " to " + destFile.getAbsolutePath());
		}

		return true;
	}
	
	
	@Override
	public void processException(String processName, Exception ex) {
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		sendFailureNotification(processName, sw.toString());
	}
	
	/**
	 * Send email notification if any job fails
	 * @param processName
	 * @param content
	 */
	private void sendFailureNotification(String processName, String content) {
		NotificationType type = lookupManager.getNotificationType(NotificationType.SYSTEM_FAILURE);

		Config userName = (Config) lookupManager.getById(Config.class, Config.SYSTEM_USER);
		Config sendNotifications = (Config) lookupManager.getById(Config.class, Config.SEND_EMAIL_NOTIFICATIONS);
		Config emailNotificationList = (Config) lookupManager.getById(Config.class, Config.SCHEDULED_PROCESS_ERROR_NOTIFICATION_LIST);

		User systemUser = this.userManager.getUserByUsername(userName.getValue());

		Map<String, String> model = new HashMap<String, String>();
		String[] emails = StringUtils.split(emailNotificationList.getValue(), ",;");
		for (String email : emails) {
			model.put("userEmail", email);
			model.put("processName", processName);
			model.put("exceptionContent", StringUtils.abbreviate(content,4000));
			model.put("executionDate", DateUtils.formatDate(new Date(), "MM/dd/yyyy HH:mm:ss"));

			try {
				if ("true".equalsIgnoreCase(sendNotifications.getValue())) {
					sendEmailManager.sendNotificationEmail(type, systemUser, model);
				}
			} catch (SendEmailException se) {
				log.error("Error when sending email to: " + email);
			} catch (Exception ex) {
				log.error("General Exception occured. Cause: " + email);
			}
		}

	}
	
	/**
	 * This method deletes the images from BelkMacl server,
	 * which are sent to RRD
	 * @param directory
	 * @param pattern
	 */
	@SuppressWarnings("unchecked")
	private boolean deleteRRDSentImages(String directory, String pattern) {
		log.info("---------------->  Begin : Delete RRD Sent images from BelkMacl Server  <-----------------");
		File dir = new File(directory);
		String[] extensions = new String[] { pattern };
		try {
			List<File> files = (List<File>) FileUtils.listFiles(dir,
					extensions, true);
			for (File file : files) {
				log.info("image: " + file.getName() + " deleted ");
				file.delete();
			}
		} catch (Exception e) {
			log.error("Exception occured while deleting the images from BelkMaclServer"
					+ e);
			return false;
		}
		log.info("---------------->  END : Delete RRD Sent images from BelkMacl Server <-----------------");
		return true;
	}

}
