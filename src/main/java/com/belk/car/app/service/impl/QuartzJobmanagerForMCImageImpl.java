/**
 * 
 */
package com.belk.car.app.service.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;

import com.belk.car.app.Constants;
import com.belk.car.app.exceptions.CarJobDetailException;
import com.belk.car.app.exceptions.IExceptionProcessor;
import com.belk.car.app.exceptions.SendEmailException;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.Config;
import com.belk.car.app.model.Image;
import com.belk.car.app.model.MediaCompassImage;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.model.vendorimage.VendorImageStatus;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.EmailManager;
import com.belk.car.app.service.VendorImageManager;
import com.belk.car.app.util.FtpUtil;
import com.belk.car.app.util.PropertyLoader;
import com.belk.car.integrations.rrd.MediaCompassSamplePhotoReader;
import com.belk.car.integrations.rrd.xml.update.MediaCompassImageReader;
import com.belk.car.util.DateUtils;
import com.rrd.sso.SSOSessionService;

/**
 * @author afusy12
 *
 */
public class QuartzJobmanagerForMCImageImpl implements IExceptionProcessor {

	private CarLookupManager lookupManager;
	private UserManager userManager;
	private CarManager carManager;
	private EmailManager sendEmailManager;
	private VendorImageManager vendorImageManager;
	private transient final Log log = LogFactory.getLog(QuartzJobmanagerForMCImageImpl.class);
	private static final String WS = "http://mediacompass.rrd.com/SSOWebService/services/SSOSessionService";
	//urlPath.tempImagesURL
	
	public void setSendEmailManager(EmailManager sendEmailManager) {
		this.sendEmailManager = sendEmailManager;
	}
	public void setLookupManager(CarLookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}
	
	public UserManager getUserManager() {
		return userManager;
	}
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}
	public void setVendorImageManager(VendorImageManager vendorImageManager) {
		this.vendorImageManager = vendorImageManager;
	}
	
	

	/**
	 *  this Job reads the temporary media compass image XML file and loads the car id, sample id and image name to CARS.MEDIA_COMPASS_IMAGE table 
	 * @throws IOException
	 * @throws CarJobDetailException
	 */
	public void importMCTempImageFeed()  throws IOException, CarJobDetailException {
		
		
		log.info("---------------->  Begin importing MC Temporary Sample Images Feeds <-----------------");
		try{
			final String host = ((Config) lookupManager.getById(Config.class, Config.RRD_FTP_HOST)).getValue();
			final String userName = ((Config) lookupManager.getById(Config.class, Config.RRD_FTP_USERNAME)).getValue();
			final String password = ((Config) lookupManager.getById(Config.class, Config.RRD_FTP_PASSWORD)).getValue();
			final String remoteDirName = ((Config) lookupManager.getById(Config.class, Config.RRD_MC_TEMP_IMAGE_DOWNLOAD_DIR)).getValue();
			final String localDirName = ((Config) lookupManager.getById(Config.class, Config.LOCAL_MC_TEMP_IMAGE_DOWNLOAD_DIR)).getValue();
			final boolean deleteAfterGet = true;
	
			try {
				FTPClient ftp = FtpUtil.openConnection(host, userName, password);
				FtpUtil.setTransferModeToAscii(ftp);
				FtpUtil.getDataFiles(ftp, remoteDirName, localDirName, deleteAfterGet);
				FtpUtil.closeConnection(ftp);
			} catch (IOException ioex) {
				log.error("Could not read files from the RRD FTP", ioex);
			}
			log.info("processing the media compass temp image XML file(s)");
			FileProcessor readRRDFileToDB = new FileProcessor(){
				public void process(File f) throws Exception {
					if (!f.exists() || f.isDirectory() || (f.length() == 0))
						return;
					log.info("calling read and process method for: "+ f.getName());
					MediaCompassImageReader reader = new MediaCompassImageReader();
					//reader.setStandalone(false);
					reader.readAndProcessXML(f);
				}
	
				public String getProcessName() {
					return "reading XML from RRD";
				}
			};
	
			String importFileName = ((Config) lookupManager.getById(Config.class, Config.RRD_IMPORT_FILENAME)).getValue();
			String completedDirName = ((Config) lookupManager.getById(Config.class, Config.RRD_FILE_COMPLETED_DIRECTORY_NAME)).getValue();
	
			FileProcessorChain fileProcessorChain = new FileProcessorChain("importing media compass temp image RRD feeds");
			fileProcessorChain.setExceptionProcessor(this) ;
			fileProcessorChain.setFiles(findFiles(localDirName, importFileName));
			fileProcessorChain.addFileProcessor(readRRDFileToDB, FileProcessorChain.ActionOnException.ContinueWithNextFile);
			fileProcessorChain.addFileProcessor(new FileArchiver(completedDirName), FileProcessorChain.ActionOnException.ContinueChain);
			fileProcessorChain.processFiles();

			//Added in VIP project:  get media compass temp image link for vendor images
			log.info("processing the media compass temps for VENDOR IMAGES");
			getMediaCompassImageForVendorImages();
			
		}catch(Exception e){
			log.error("error while updating the media_comapss temporary image link",e);
			processException("Import Media Compass Images ", e);
		}
		
		log.info("---------------->  End importing  MC Temporary Sample Images Feeds  <-----------------");

	}
		
	/**
	 * This method insert an row for MQ passed vendor images in MEDIA_COMPASS_IMAGE table with status RECEIVED.
	 *  
	 */
	private void getMediaCompassImageForVendorImages() throws Exception{

		Date currDate = new Date();
		 Calendar cal = Calendar.getInstance();
		Car carDetail=null;
		MediaCompassImage mediaCompassImage =null;
		List<Image> imageList=null;
		try{
			
		    cal.add(Calendar.DATE, -1);    
		    //Get all yesterdays MQ passed vendor images
			imageList = this.vendorImageManager.getImagesByVendorImageStatus(new String[]{VendorImageStatus.MQ_PASSED}, cal.getTime());
			if(imageList==null){
				log.info("No MQ_PASSED vendor images found ");
				return;
			}
			if(log.isInfoEnabled()){
				log.info("Number of  Vendorimage found from the Query : "+imageList.size());
			}
			for(Image img :imageList)
			{				
				try{
					mediaCompassImage = null;//added for temp image production issue 
					carDetail = this.vendorImageManager.getCarByVendorImageId(img.getVendorImage().getVendorImageId());
					//code start: Production issue fixed
					mediaCompassImage=this.vendorImageManager.getMediaComapssByCarId(carDetail.getCarId());	
					//code ends : Production issue fixed
					if(mediaCompassImage == null){  //if current car doesnot exists in media compass then create new row for current car
						log.info("adding record for CAR ID:     " + carDetail);
						mediaCompassImage =new MediaCompassImage();
						mediaCompassImage.setCar(carDetail);
						//set image location
						mediaCompassImage.setImageName(img.getImageLocation());
						mediaCompassImage.setImageStatus(MediaCompassImage.MC_IMAGE_RECEIVED);
						mediaCompassImage.setCreatedDate(currDate);
						mediaCompassImage.setUpdatedDate(currDate);
						carManager.save(mediaCompassImage);	
					}else{
						log.error("Specified car already exists in the Table:"+ carDetail.getCarId());
					}
				}catch(Exception e){
					log.info("Error while adding record to MEDIA_COMASS_IMAGE table:  " + e);
				}
			}

		}catch(Exception e){
			log.error("Error while adding record to MEDIA_COMASS_IMAGE table for vendor images:  " + e);
			throw e;
		}
	}
	
	/**
	 * This jobs reads the received status images from CARS.MEDIA_COMPASS_IMAGE table  and downloads the image from Media compass through RRD web-service
	 * after downloading, Image status will be DOWNLOADED.
	 * @throws IOException
	 * @throws CarJobDetailException
	 */
	public void loadTempImagesFromMC() throws IOException, CarJobDetailException{
		
		log.info("------------------>Begin loading temporary images from Media Compass to Belk server <----------------------");
		Config ImageDownloadDirConfig = (Config) lookupManager.getById(Config.class, Config.BELK_TEMP_IMAGE_DOWNLOAD_DIR);
		
		final String receivedStatus = Constants.MC_IMAGE_RECEIVED;
		final String downloadedStatus = Constants.MC_IMAGE_DOWNLOADED;
		List<MediaCompassImage> receivedImageList = null;
		String tempImagesURL = "";
		int downloadedImageCt = 0;
		int recievdImageCt = 0;
		
		try{
			String strImageDownloadLocation = ImageDownloadDirConfig.getValue();
			Properties properties = PropertyLoader.loadProperties("ftp.properties");
			tempImagesURL= properties.getProperty("tempImagesURL");
			
			//get SSOServiceObject for Single signOn webservice
			Service serviceModel = new ObjectServiceFactory().create(SSOSessionService.class);
			SSOSessionService service = (SSOSessionService)new XFireProxyFactory().create(serviceModel, WS);
			
			//Find all images of received status from media_compass_image table
			receivedImageList = lookupManager.getAllMCImages(receivedStatus);
			recievdImageCt = receivedImageList.size();
			if(log.isInfoEnabled()){
				log.info("Found " + recievdImageCt + " received media compass image names to download");
			}
			
			for(MediaCompassImage mcImage: receivedImageList){
				String strImageName = mcImage.getImageName();
				MediaCompassSamplePhotoReader reader = new MediaCompassSamplePhotoReader();

				//Call the RRD SSOWebservice and get the Media Compass image URL
				String strMCImageURL = reader.getMCImageURLbyName(service, strImageName);
				
				//Download the image from Media Compass URL to destination dir
				boolean isImageDownloaded = reader.downloadImage(strMCImageURL, strImageDownloadLocation, strImageName);
				
				if(isImageDownloaded){
					//TODO update the status and URL of MediaCompassImage to downloaded
					if(log.isDebugEnabled()){
						log.debug("image downloaded successfully "+ strImageName);
					}
					mcImage.setImageStatus(downloadedStatus);
					mcImage.setImageLocation(tempImagesURL + strImageName);
					mcImage.setUpdatedDate(new Date());
					carManager.save(mcImage);
					downloadedImageCt++;
				}else{
					log.error("Not able to download image from media compass: "+ strImageName);
				}
			}
			if(log.isInfoEnabled()){
				log.info("number of images downloaded" + downloadedImageCt);
			}
			if(recievdImageCt > downloadedImageCt){
				log.error("Could not download  " + (recievdImageCt-downloadedImageCt) + "images from media compass !!");
				sendFailureNotification("Load Media Compass Images","Not able to download" + (recievdImageCt-downloadedImageCt)+ " images from Media compass");
			}
		}
		catch(Exception e){
			log.error("Error while downloading the image from MediaCompass Webservice");
			processException("Load Media Compass Images ", e);
		}
		log.info("------------------>End loading temporary images from Media Compass to Belk server <----------------------");
		
	}
	
	private File[] findFiles(String dirName, String fileName) {
		return findFiles(dirName, fileName, false);
	}

	private File[] findFiles(String dirName, String fileName, boolean includeDirectories) {
		// We need all of the config values
		if (dirName == null) {
			throw new IllegalArgumentException("Cannot process files. The directory name is missing");
		}

		boolean useAllFilesInDirectory = StringUtils.isNotBlank(dirName) && StringUtils.isBlank(fileName);
		File[] files = null;
		if (useAllFilesInDirectory) { //User selected to process all files under the directory			
			File dir = new File(dirName);
			FileFilter fileFilter = (includeDirectories) ? null : new FileFilter() {
				public boolean accept(File pathname) {
					return (!pathname.isDirectory());
				}
			};
			files = dir.listFiles(fileFilter);
		} else { //User selected to scan a single file
			String filePath = dirName + fileName;
			File fl = new File(filePath);
			files = new File[] { fl };
		}
		return files;
	}
	
	static class FileProcessorChain {
		String name;

		public FileProcessorChain(String name) {
			this.name = name;
		}

		IExceptionProcessor exceptionProcessor ;
		public void setExceptionProcessor(IExceptionProcessor exceptionProcessor) {
			this.exceptionProcessor = exceptionProcessor ;
		}
		
		File[] files = new File[0];

		private void setFiles(File[] files) {
			this.files = files;
		}

		public void setFiles(Collection<File> files) {
			this.files = files.toArray(new File[0]);
		}

		public void addFiles(Collection<File> additionalFiles) {
			List<File> fileList = Arrays.asList(this.files);
			fileList.addAll(additionalFiles);
			this.files = fileList.toArray(new File[0]);
		}

		List<FileProcessor> fileProcessors = new ArrayList<FileProcessor>();
		Map<FileProcessor, ActionOnException> fileProcessor2ActionOnError = new HashMap<FileProcessor, ActionOnException>();

		public static enum ActionOnException {
			ContinueChain, ContinueWithNextFile, DoNotContinue
		}

		public void addFileProcessor(FileProcessor fileProcessor) {
			fileProcessors.add(fileProcessor);
		}

		public void addFileProcessor(FileProcessor fileProcessor, ActionOnException actionOnException) {
			addFileProcessor(fileProcessor);
			fileProcessor2ActionOnError.put(fileProcessor, actionOnException);
		}

		public void processFiles() {
			if (files == null || files.length < 1 || fileProcessors == null || fileProcessors.size() < 1)
				return;
			Log log = LogFactory.getLog(QuartzJobmanagerForMCImageImpl.class);

			processingFiles: 
			for (File file : files) {

				processingTheCurrentFile: 
				for (FileProcessor fileProcessor : fileProcessors) {
					try {
						if (log.isInfoEnabled())
							log.info("running process (" + this.name + ") on file " + file.getName());
						//if (file.length() > 0)
						fileProcessor.process(file);
						if (log.isInfoEnabled())
							log.info("- " + fileProcessor.getProcessName() + ": success");
					} catch (Exception ex) {
						if (log.isErrorEnabled()) {
							log.error("- " + fileProcessor.getProcessName() + ": failure", ex);
						}
						
						if (this.exceptionProcessor != null) {
							exceptionProcessor.processException(fileProcessor.getProcessName() + ":(" +  file.getName() + ")", ex);
						}

						ActionOnException actionOnEx = fileProcessor2ActionOnError.get(fileProcessor);
						String actionStr;
						switch (actionOnEx) {
						case ContinueChain:
							actionStr = "Continuing with next process on the current file.";
							break;
						case DoNotContinue:
							actionStr = "Processing on all files has stopped.";
							break;
						default:
						case ContinueWithNextFile:
							actionStr = "Continuing with next file.";
							break;
						}
						log.error(actionStr);

						switch (actionOnEx) {
						case ContinueChain:
							continue processingTheCurrentFile;
						case DoNotContinue:
							return;
						default:
						case ContinueWithNextFile:
							continue processingFiles;
						}
					}
				}
			}
		}

	}
	
	
	public void processException(String processName, Exception ex) {
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		sendFailureNotification(processName, sw.toString());
	}
	
	
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
	
	interface FileProcessor {
		public String getProcessName();

		public void process(File file) throws Exception;
	}	
	
	private class FileArchiver implements FileProcessor {
		String archiveDirName;

		FileArchiver(String archiveDirName) {
			this.archiveDirName = archiveDirName;
		}

		public void process(File f) throws Exception {
			if (f.isDirectory() || !f.exists())
				return;

			File destDir = new File(f.getParentFile(), archiveDirName);
			String destFileName = new StringBuffer().append(DateUtils.formatDate(new Date(), "MM_dd_yyyy_HH_mm_ss")).append("_").append(f.getName()).toString();
			File destFile = new File(destDir, destFileName);

			if (!destDir.exists())
				destDir.mkdirs();

			boolean isMoveSuccessful = f.renameTo(destFile);

			if (isMoveSuccessful) {
					log.info(f.getName() + " file was archived successfully.");
			} else {
					log.error("Error moving file " + f.getName() + " to directory: " + destDir);
			}
		}

		public String getProcessName() {
			return "archiving";
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
	

}
