/**
 * 
 */

package com.belk.car.app.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.NamingException;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.dao.DataAccessException;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.exceptions.CarJobDetailException;
import com.belk.car.app.model.BaseAuditableModel;
import com.belk.car.app.model.Config;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.model.view.CarAttributeView;
import com.belk.car.app.model.view.VendorStyleHexView;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.CatalogImportManager;
import com.belk.car.app.service.DropshipManager;
import com.belk.car.app.service.EmailManager;
import com.belk.car.app.service.PatternPrefillManager;
import com.belk.car.app.service.ProductManager;
import com.belk.car.app.service.QuartzJobManagerForDropship;
import com.belk.car.app.service.ReportManager;
import com.belk.car.app.service.WorkflowManager;
import com.belk.car.app.to.IdbCarDataTO;
import com.belk.car.app.to.IdbDropshipDataTO;
import com.belk.car.app.util.FileTransferUtil;
import com.belk.car.app.util.FtpUtil;
import com.belk.car.app.util.PropertyLoader;
import com.belk.car.app.util.ReadIDBCrossReferenceFeedFileForCarCreation;
import com.belk.car.app.util.ReadIDBCrossReferenceFeedFileForDropship;
import com.belk.car.app.util.ReadIDBFeedForDropship;
import com.belk.car.app.util.ReadIDBFileForDropship;
import com.belk.car.app.util.ReadIDBPurgeFeedForDropship;
import com.belk.car.app.util.SFtpUtil;
import com.belk.car.util.DateUtils;
import com.belk.car.util.GenericUtils;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

/**
 * @author afusy45 and afusy07
 */
public class QuartzJobManagerForDropshipImpl
		implements
			QuartzJobManagerForDropship, DropShipConstants {

	private DropshipManager dropshipManager;
	private ReadIDBFileForDropship readIDBFileForDropship;
	private ReadIDBCrossReferenceFeedFileForDropship readIDBCrossReferenceFeedFileForDropship;
	private ReadIDBCrossReferenceFeedFileForCarCreation carCreation;
	private ReadIDBPurgeFeedForDropship readIDBPurgeFeedForDropship;
        private CarLookupManager lookupManager;
        private CarManager carManager;
        private WorkflowManager workflowManager;
        private UserManager userManager;
        private ProductManager productManager;
	private EmailManager sendEmailManager;
        private CatalogImportManager catalogImportManager;
	private ReportManager reportManager;
        private PatternPrefillManager patternPrefillManager;



	

	/**
	 * @return the readIDBPurgeFeedForDropship
	 */
	public ReadIDBPurgeFeedForDropship getReadIDBPurgeFeedForDropship() {
		return readIDBPurgeFeedForDropship;
	}

	/**
	 * @param readIDBPurgeFeedForDropship the readIDBPurgeFeedForDropship to set
	 */
	public void setReadIDBPurgeFeedForDropship(
			ReadIDBPurgeFeedForDropship readIDBPurgeFeedForDropship) {
		this.readIDBPurgeFeedForDropship = readIDBPurgeFeedForDropship;
	}

	/**
	 * @return the crossReferenceFeedFileForDropship
	 */
	public ReadIDBCrossReferenceFeedFileForDropship getReadIDBCrossReferenceFeedFileForDropship() {
		return readIDBCrossReferenceFeedFileForDropship;
	}

	/**
	 * @param crossReferenceFeedFileForDropship the
	 *            crossReferenceFeedFileForDropship to set
	 */
	public void setReadIDBCrossReferenceFeedFileForDropship(
			ReadIDBCrossReferenceFeedFileForDropship readIDBCrossReferenceFeedFileForDropship) {
		this.readIDBCrossReferenceFeedFileForDropship = readIDBCrossReferenceFeedFileForDropship;
	}

	/**
	 * @return the dropshipManager
	 */
	public DropshipManager getDropshipManager() {
		return dropshipManager;
	}

	/**
	 * @param dropshipManager the dropshipManager to set
	 */
	public void setDropshipManager(DropshipManager dropshipManager) {
		this.dropshipManager = dropshipManager;
	}

	/**
	 * @return the readIDBFileForDropship
	 */
	public ReadIDBFileForDropship getReadIDBFileForDropship() {
		return readIDBFileForDropship;
	}

	/**
	 * @param readIDBFileForDropship the readIDBFileForDropship to set
	 */
	public void setReadIDBFileForDropship(
			ReadIDBFileForDropship readIDBFileForDropship) {
		this.readIDBFileForDropship = readIDBFileForDropship;
	}

        public CarManager getCarManager() {
            return carManager;
        }

        public void setCarManager(CarManager carManager) {
            this.carManager = carManager;
        }

        public CatalogImportManager getCatalogImportManager() {
            return catalogImportManager;
        }

        public void setCatalogImportManager(CatalogImportManager catalogImportManager) {
            this.catalogImportManager = catalogImportManager;
        }

        public CarLookupManager getLookupManager() {
            return lookupManager;
        }

        public void setLookupManager(CarLookupManager lookupManager) {
            this.lookupManager = lookupManager;
        }

        public ProductManager getProductManager() {
            return productManager;
        }

        public void setProductManager(ProductManager productManager) {
            this.productManager = productManager;
        }

        public ReportManager getReportManager() {
            return reportManager;
        }

        public void setReportManager(ReportManager reportManager) {
            this.reportManager = reportManager;
        }

        public EmailManager getSendEmailManager() {
            return sendEmailManager;
        }

        public void setSendEmailManager(EmailManager sendEmailManager) {
            this.sendEmailManager = sendEmailManager;
        }

        public UserManager getUserManager() {
            return userManager;
        }

        public void setUserManager(UserManager userManager) {
            this.userManager = userManager;
        }

        public WorkflowManager getWorkflowManager() {
            return workflowManager;
        }

        public void setWorkflowManager(WorkflowManager workflowManager) {
            this.workflowManager = workflowManager;
        }

        public PatternPrefillManager getPatternPrefillManager() {
            return patternPrefillManager;
        }

        public void setPatternPrefillManager(PatternPrefillManager patternPrefillManager) {
            this.patternPrefillManager = patternPrefillManager;
        }


	private transient final Log log = LogFactory
			.getLog(QuartzJobManagerForDropshipImpl.class);

	/**
	 * Method to read file from IDB into CARS and process it (non-Javadoc)
	 * @throws Exception 
	 * 
	 * @see com.belk.car.app.service.QuartzJobManagerForDropship#importCarsForDropship()
	 */
	public void importIDBFeedForDropship()
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Inside IDBFeed method in quartzImpl....");
			log.info("---------------->  Begin importing IDB Feed Data For Dropship <-----------------");
		}
		// Initialize Global Variables
		List<IdbDropshipDataTO> col = null;
		Properties properties = PropertyLoader
		.loadProperties("ftp.properties");
		
		String filePath = properties.getProperty("idbFeedFilePath");
		log.debug("filePath...." + filePath);
		String newDirName = properties.getProperty("idbFeedArchive");
		String fileName = properties.getProperty("idbFeedFileName");
		
		if (filePath == null) {
			throw new CarJobDetailException(
					"Cannot process import job. A configuration value is missing");
		}

		
		File[] files = null;
		
		File dir = new File(filePath);
		files = dir.listFiles();

		// }

		// Directory doesn't exist or there aren't any files to process
		if (files == null) {
			if (log.isInfoEnabled()) {
				log.info("There are no files to process in directory: "
						+ filePath + " Exiting job...");
			}
		}
		else {
			log.info("inside else file length" + files.length);
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().equals(fileName)) {
					log.debug("if file name is wic40000.txt...");
					// Process Files
					try {
						col = readIDBFileForDropship.process(files[i]);

					}
					catch (ParseException e) {
						log.error("Exception processing file", e);
					}

					if (col == null || col.isEmpty()) {
						if (log.isInfoEnabled()) {
							log.info("There was an error processing. "
									+ files[i].getName()
									+ " Skipping to next file...");
						}
					}
					else {
						try{
						dropshipManager.saveFeed(col);
						
						/*Move the file to archive folder*/
						moveFileToArchive(filePath , newDirName, fileName);
						

						} catch(DataAccessException dae){
							log.error("Exception while saving IDB Feed", dae);
						}
						
					}
				}// End of if filename is dropship.txt loop
			}// End of no of files for loop

		}

		log.info("---------------->  End importing IDB Feed For Dropship <-----------------");
	}

	/**
	 * Method to read file from IDB into CARS and process it (non-Javadoc)
	 * @throws Exception 
	 * @author afusy45-Siddhi Shrishrimal
	 * @see com.belk.car.app.service.QuartzJobManagerForDropship#importCarsForDropship()
	 */
	public void importIDBCrossReferenceFeedForDropship()
			throws Exception {
		log.debug("Inside IDBFeed method in quartzImpl....");
		log.info("---------------->  Begin importing IDB Cross Reference Feed Data For Dropship <-----------------");

		// Initialize Global Variables
		List<IdbDropshipDataTO> col = null;

		
		Properties properties = PropertyLoader
		.loadProperties("ftp.properties");
		
		String filePath = properties.getProperty("idbCrossReferenceFeedPath");
		log.info("filePath...." + filePath);
		String newDirName = properties.getProperty("idbCrossReferenceFeedArchive");
		String fileName = properties.getProperty("idbCrossRefernceFile");
		
		// We need all of the config values
		if (filePath == null) {
			throw new CarJobDetailException(
					"Cannot process import job. A configuration value is missing");
		}

		
		File[] files = null;
		
		File dir = new File(filePath);
		files = dir.listFiles();

		// Directory doesn't exist or there aren't any files to process
		if (files == null) {
			
				log.info("There are no files to process in directory: "
						+ filePath + " Exiting job...");
			
		}
		else {
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().equals(fileName)) {
					log.debug("if file name is crossfeed.txt...");
						log.info("filename"+files[i].getName());
					//Process Files
					try {
						col = readIDBCrossReferenceFeedFileForDropship
								.processReferenceFeed(files[i]);

					}
					catch (ParseException e) {
						log.error("Exception processing file", e);
					}

					if (col == null || col.isEmpty()) {
						if (log.isInfoEnabled()) {
							log.info("There was an error processing. "
									+ files[i].getName()
									+ " Skipping to next file...");
						}
					}
					else {
						log.info("File processed successfully....");
						
						try{
							
							dropshipManager.saveFeed(col);
						}catch(DataAccessException dae){
							log.error("Exception while saving data in IDB Feed table for Cross Reference Feed");
						}
						
						/*Move the file to archive folder*/
						moveFileToArchive(filePath , newDirName, fileName);
						
					}
				}// End of if filename is dropship.txt loop
			}// End of no of files for loop

		}

		if (log.isInfoEnabled()) {
			log
					.info("---------------->  End importing IDB Cross Reference Feed For Dropship <-----------------");
		}
	}
	
	/**
	 * Method to read file from IDB into CARS and process it (non-Javadoc)
	 * @throws Exception 
	 * @author Bharati Bhat
	 */
	public void importIDBCrossReferenceFeedForDropshipForCarCreation()
			throws Exception {
		log.info("---------------->  Begin importing IDB Cross Reference Feed Data For Dropship <-----------------");

		// Initialize Global Variables
		List<IdbDropshipDataTO> col = null;

		
		Properties properties = PropertyLoader
		.loadProperties("ftp.properties");
		
		String filePath = properties.getProperty("idbCrossReferenceFeedPath");
		String fileName = "IDB-XREFF.txt";
		
		// We need all of the config values
		if (filePath == null) {
			throw new CarJobDetailException(
					"Cannot process import job. A configuration value is missing");
		}

		
		File[] files = null;
		
		File dir = new File(filePath);
		files = dir.listFiles();

		// Directory doesn't exist or there aren't any files to process
		if (files == null) {
			
				log.info("There are no files to process in directory: "
						+ filePath + " Exiting job...");
			
		}
		else {
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().equals(fileName)) {
					log.debug("if file name is crossfeed.txt...");
					//Process Files
					try {
						carCreation.processReferenceFeed(files[i]);
					}
					catch (Exception e) {
						log.error("Exception processing file", e);
					}

					if (col == null || col.isEmpty()) {
						if (log.isInfoEnabled()) {
							log.info("There was an error processing. "
									+ files[0].getName()
									+ " Skipping to next file...");
						}
					}
					else {
						log.info("File processed successfully....");
					}	
				}// End of if filename is dropship.txt loop
			}// End of no of files for loop

		}

		if (log.isInfoEnabled()) {
			log
					.info("---------------->  End importing IDB Cross Reference Feed For Dropship <-----------------");
		}
	}

	/**
	 * Method to import IDB Purge Feed
	 * @throws Exception 
	 * @author afusy45-Siddhi Shrishrimal
	 */
	public void importIDBPurgeFeedForDropship()
			throws Exception {

		log.debug("Inside IDBPurgeFeed method in quartzImpl....");
		
			log
					.info("---------------->  Begin importing IDB Purge Feed Data For Dropship <-----------------");

		// Initialize Global Variables
		List<IdbDropshipDataTO> col = null;

		Properties properties = PropertyLoader
		.loadProperties("ftp.properties");
		
		String filePath = properties.getProperty("idbPurgeFeed");
		log.info("filePath...." + filePath);
		String newDirName = properties.getProperty("idbPurgeFeedArchive");
		String fileName = properties.getProperty("idbPurgeFeedFile");
		
		// We need all of the config values
		if (filePath == null) {
			throw new CarJobDetailException(
					"Cannot process import job. A configuration value is missing");
		}

		
		File[] files = null;
		
		File dir = new File(filePath);
		files = dir.listFiles();

	
		// Directory doesn't exist or there aren't any files to process
		if (files == null) {
			
				log.info("There are no files to process in directory: "
						+ filePath + " Exiting job...");
			
		}
		else {
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().equals(fileName)) {
					log.debug("if file name is crossfeed.txt...");
					// Process Files
					try {
						col = readIDBPurgeFeedForDropship
								.processIDBPurgeFeed(files[i]);

					}
					catch (ParseException e) {
						log.error("Exception processing file", e);
					}

					if (col == null || col.isEmpty()) {
						if (log.isInfoEnabled()) {
							log.info("There was an error processing. "
									+ files[i].getName()
									+ " Skipping to next file...");
						}
					}
					else {
						log.info("File processed successfully....");
						/*Move the file to archive folder*/
						moveFileToArchive(filePath , newDirName, fileName);
						
					}
				}// End of if filename is dropship.txt loop
			}// End of no of files for loop

		}

		log.info("---------------->  End importing IDB Purge Feed For Dropship <-----------------");

	}

	/**
	 * @param carCreation the carCreation to set
	 */
	public void setCarCreation(ReadIDBCrossReferenceFeedFileForCarCreation carCreation) {
		this.carCreation = carCreation;
	}

	/**
	 * @return the carCreation
	 */
	public ReadIDBCrossReferenceFeedFileForCarCreation getCarCreation() {
		return carCreation;
	}

	/**
	 * importDropship Cars
	 */
	public void importDropshipCars() throws IOException, CarJobDetailException, NamingException {
		if (log.isInfoEnabled())
			log.info("---------------->  Begin importingDropship Car Data <-----------------");

		// Initialize Global Variables
		Collection<IdbCarDataTO> col = null;
		Properties properties = PropertyLoader.loadProperties("ftp.properties");

        String importFileName = properties.getProperty("importDropShipCARSFileName");//="wic40000.txt"//
        String importDirectory =properties.getProperty("dropShipCARSFilePath");//"c:/idbFeed/";"
        String completedDirectory =properties.getProperty("dropShipCARSArchiveDirectoryPath");//"archive";//
        //importFileName="wic40000.txt";
        // importDirectory="C:\\cars\\data\\carsdata\\import_idb\\";
       //  completedDirectory="C:\\cars\\data\\carsdata\\import_idb\\archive\\";
		// We need all of the config values
		if (importDirectory == null || importFileName == null || completedDirectory == null) {
			throw new CarJobDetailException("Cannot process import job. A configuration value is missing");
		}

		boolean scanDirectory = StringUtils.isNotBlank(importDirectory) && StringUtils.isBlank(importFileName);
		File[] files = null;
		if (scanDirectory) { //User selected to process all files under the directory
			File dir = new File(importDirectory);
			files = dir.listFiles();
		} else { //User selected to scan a single file
			String filePath = importDirectory +"/"+ importFileName;
			File fl = new File(filePath);
			if (fl.exists()) {
				files = new File[] { fl };
			}
		}

                if (log.isInfoEnabled())
			log.info("---------------->  File path is sucessfull-----------------");

		// Directory doesn't exist or there aren't any files to process
		if (files == null) {
			if (log.isInfoEnabled()) {
				if (scanDirectory) {
					log.info("There are no files to process in directory: " + importDirectory + " Exiting job...");
				} else {
					log.info("File " + importDirectory + importFileName + " not found!!! Nothing to process. Exiting job...");
				}
			}
		} else {
			//Process Files
			for (File file : files) {
				if (!file.isDirectory()) {
                                    //File exsits call process method"
					col = ReadIDBFeedForDropship.process(file);
					if (col == null || col.isEmpty()) {
						if (log.isInfoEnabled())
							log.info("There was an error processing. " + file.getName() + " Skipping to next file...");
					} else {
                                            //File exsits call processCars method
						try{
							dropshipManager.processCars(col);
						}catch(Exception e)	{
							if (log.isInfoEnabled())
								log.info("Exception in processCars : "+e.toString());
							}
						if (log.isInfoEnabled())
							log.info("Process Cars done...");
					} //else

					if (processCompletedFile(importDirectory, file.getName(), completedDirectory)) {
						if (log.isInfoEnabled())
							log.info(importFileName + " file was archived successfully.");
					} else {
						if (log.isErrorEnabled())
							log.error("Error moving file " + importFileName + " to directory: " + completedDirectory);
					}
				}
			}
		}

		if (log.isInfoEnabled())
			log.info("---------------->  End importing Dropship Car Data <-----------------");
	}


        private boolean processCompletedFile(String filePath, String fileName, String destinationDir) {
		return processCompletedFile(filePath, fileName, destinationDir, false);
	}

        private boolean processCompletedFile(String filePath, String fileName, String destinationDir, boolean copyFile) {
        	if (log.isDebugEnabled())
    			log.debug("inside processCompletedFile");
    		// File to be moved
    		File currentFile = new File(filePath + "/" + fileName);  //07252013 - Modified to fix archiving issue
    

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
	    			try{
			    			File destFile = new File(destFileName);
			    			boolean ren = currentFile.renameTo(destFile);
			    			if (!ren)
			    				log.info("Could not rename file: " + currentFile.getAbsolutePath() + " to " + destFile.getAbsolutePath());
	    			}catch(Exception ex){
	    				ex.printStackTrace();
	    			}
    		}

    		return true;
    	}


        /**
         *  Set Audit Info
         * @param user User
         * @param model BaseAuditableModel
         */
        public void setAuditInfo(User user, BaseAuditableModel model) {

		if (StringUtils.isBlank(model.getCreatedBy())) {
			model.setCreatedBy(user.getUsername());
			model.setCreatedDate(new Date());
		}
		model.setUpdatedBy(user.getUsername());
		model.setUpdatedDate(new Date());
	}
	/**
	 * Method to move the successfully read files to archive
	 * @param filePath
	 * @param newDirName
	 */
	public void moveFileToArchive(String filePath, String newDirName, String fileName){
		/*Move read file to archive folder*/
		log
		.debug("If success reading file move the file to archive folder");
		File sourceDir = new File(filePath);
		File newDirectory = new File(newDirName);

		newDirectory.mkdir();
		File[] filesToMove = null;

		filesToMove = sourceDir.listFiles();

		String DATE_FORMAT = YYYY_M_MDD_H_HMMSS_FORCATALOG;

		SimpleDateFormat sdf = new SimpleDateFormat(
				DATE_FORMAT);

		Calendar c1 = Calendar.getInstance();

		sdf.format(c1.getTime());
		boolean newFile = true;
		String newFileName = null;
		for (int k = 0; k < filesToMove.length; k++) {

				if (filesToMove[k].getName().equals(fileName)) {

					filesToMove[k].renameTo(new File(
							newDirName, fileName));
					File[] f = newDirectory.listFiles();
					newFileName = filesToMove[k].getName()
							.substring(
									0,
									filesToMove[k].getName()
											.indexOf("."))
							.concat(sdf.format(c1.getTime()));
					for (int s = 0; s < f.length; s++) {
						newFile = f[s].renameTo(new File(
								newDirName, newFileName
										.concat(".txt")));
					}

				}

			}
	}

        public void prefIllPatternStyleCar() {
            patternPrefillManager.prefIllPatternStyle();
        }
        
        
        /*
         * Export the vendor expedited shipping 
         */
        public void exportVendorExpeditedShipping() throws IOException, CarJobDetailException {
    		
        	if (log.isInfoEnabled())
    			log.info("---------------->  Begin Export Vendor Expedited Shipping Values Process <-----------------");

    		// Pre SDF_Phase2 :Get all the Vendor Number, and expedited shipping data
        	
        	//SDF_PHASE2 Changes :To GET ONLY NEWLY INSERTED/UPDATED DATA SINCE THE LAST JOB RUN(Hence a delta feed) ...SDF_Phase2 :Yogesh V
        	Config expJobLastRun = (Config) lookupManager.getById(Config.class, Config.VNDR_EXPEDSHIP_JOB_LASTRUN); //SDF_Phase2 :Yogesh V
        	String defaultDate =DateUtil.formatDate(new Date(),DropShipConstants.EXPORT_EXPDSHIPPING_JOB_LASTDATE_FORMATE);
        	String strDateLastRun ="";
        	if(expJobLastRun == null || expJobLastRun.getValue() ==null){
        		strDateLastRun =defaultDate;
        	}else{
        		strDateLastRun = expJobLastRun.getValue();
        	}
    		List<FulfillmentServiceVendor> exportData = dropshipManager.getLatestVendorExpditedShippingValues(strDateLastRun);// SDF_Phase2 :Yogesh V
    		if(exportData!=null){
    			if(log.isDebugEnabled()){
    				log.debug(" size of vendor fulfillment service vendor:"+exportData.size());
    			}
    		}
    		Config exportDirConfig = (Config) lookupManager.getById(Config.class, Config.VNDR_EXPEDITED_SHIPPING_DIRECTORY);
    		Config exportFileConfig = (Config) lookupManager.getById(Config.class, Config.VNDR_EXPEDITED_SHIPPING_FILENAME);
    		File dir = new File(exportDirConfig.getValue());
    		if (!dir.exists())
    			dir.mkdirs();
    		
    		// Create a File object
    		String fileName = DateUtils.formatDate(new Date(), "MM_dd_yyyy_HH_mm_ss")+"_"+exportFileConfig.getValue();
    		if(log.isDebugEnabled()){
    			log.debug("file name:"+fileName);
    		}
    		String filePath = exportDirConfig.getValue() + fileName;
    		if(log.isDebugEnabled()){
    			log.debug(" file path:"+filePath);
    		}
			File file = new File(filePath);

			if (!file.exists()) {
				// Create file on disk (if it doesn't exist)
				file.createNewFile();
			}

			FileWriter out = null;
			boolean hasErrors = false ;
			String NEW_LINE = "\r\n";
			try {
				out = new FileWriter(file, false);
				for (FulfillmentServiceVendor fsv : exportData) {
					Date createdDate = fsv.getCreatedDate();
					Date lastRunDate = DateUtils.parseDate(strDateLastRun,DropShipConstants.EXPORT_EXPDSHIPPING_JOB_LASTDATE_FORMATE);//SDF_Phase2:yogesh v
					boolean isCreatedBeforeLastRun = DateUtils.getAsCalendar(createdDate).before(DateUtils.getAsCalendar(lastRunDate)); //SDF_Phase2:yogesh v
					String actFlg = (isCreatedBeforeLastRun)?DropShipConstants.VENDORINFO_UPDATE:DropShipConstants.VENDORINFO_INSERT;//SDF_Phase2:yogesh v
					StringBuffer sb = new StringBuffer();
					sb.append(fsv.getVndr().getVendorNumber())
					.append("|")
					.append(fsv.getIsExpeditedShipping())
					.append("|")
					.append(actFlg) //To add I/U SDF_Phase2 :Yogesh V
					.append(NEW_LINE);
					if(log.isDebugEnabled()){
						log.debug("vendor expedited shipping data:"+sb.toString());
					}
					out.write(sb.toString());
				}
			} catch (Exception ex) {
    			ex.printStackTrace();
    			hasErrors = true ;
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (Exception ex) {
					}
				}
			}
			Properties ftpProperties = PropertyLoader.loadProperties("ftp.properties");
    		Config ftpHost = (Config) lookupManager.getById(Config.class, Config.VNDR_EXPEDITED_SHIPPING_FTP_HOST);
    		String ftpUserId = ftpProperties.getProperty(DropShipConstants.EXPDTD_SHIPPING_FTP_UNAME); //(Config) lookupManager.getById(Config.class, Config.VNDR_EXPEDITED_SHIPPING_FTP_USERNAME);
    		String ftpPassword = ftpProperties.getProperty(DropShipConstants.EXPDTD_SHIPPING_FTP_PWORD);//(Config) lookupManager.getById(Config.class, Config.VNDR_EXPEDITED_SHIPPING_FTP_PASSWORD);
    		Config ftpRemoteDir = (Config) lookupManager.getById(Config.class, Config.VNDR_EXPEDITED_SHIPPING_REMOTE_DIRECTORY);
    		try {
    		if (file.exists()) {
				if (!hasErrors) {
					//SFTP File to Server  Yogesh V
					if(ftpHost != null && StringUtils.isNotBlank(ftpHost.getValue())) {
    						/*
    						 * Changes done by Anitha Rosary
    						 * Commenting below line and transferring files using SFTPUtil for below error
    						 * The Transport Protocol thread failed java.io.IOException: The socket is EOF
    						 * */
    						//FileTransferUtil.transferFiles(ftpHost.getValue(),ftpUserId ,ftpPassword,ftpRemoteDir.getValue(),file, true);
    						Session session =SFtpUtil.openSftpSession(ftpHost.getValue(),ftpUserId ,ftpPassword, 22, "no");
    						Channel channel = session.openChannel("sftp");
    				        channel.connect();
    				        ChannelSftp  sftpChannel = (ChannelSftp) channel;
    				        SFtpUtil.sendDataFiles(sftpChannel, Arrays.asList(file), ftpRemoteDir.getValue());
    				        SFtpUtil.closeSftpConnection(session,sftpChannel);
					}
					//as file is written successfully date of lastSuccessful execution is being changed to current date in Config Table: Yogesh V
					if(expJobLastRun !=null){
					Date newRunDate = new Date();
					String newLastRunDate = DateUtil.formatDate(newRunDate,DropShipConstants.EXPORT_EXPDSHIPPING_JOB_LASTDATE_FORMATE);
					expJobLastRun.setValue(newLastRunDate);
					expJobLastRun.setUpdatedDate(newRunDate);
					expJobLastRun.setUpdatedBy(DropShipConstants.EXPORT_EXPDSHIPPING_JOB);
					carManager.save(expJobLastRun);
					}
				}
    		}
			} catch (Exception ex) {
				ex.printStackTrace();
				log.error("Export Vendor Expedited Shipping Failed due to :"+ex.getMessage(),ex);
			}
    		
    		if (log.isInfoEnabled())
    			log.info("---------------->  End Export Vendor Expedited Shipping Values Process <-----------------");

    	}        

	
}