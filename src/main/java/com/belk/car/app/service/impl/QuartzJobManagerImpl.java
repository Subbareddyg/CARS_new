/**
 * 
 */
package com.belk.car.app.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.dao.DataAccessException;

import com.belk.car.app.Constants;
import com.belk.car.app.dao.ReportDao;
import com.belk.car.app.dto.DepartmentClassUpload;
import com.belk.car.app.dto.DetailNotificationUserDTO;
import com.belk.car.app.dto.NotificationUserDTO;
import com.belk.car.app.dto.UserRankDTO;
import com.belk.car.app.exceptions.CarJobDetailException;
import com.belk.car.app.exceptions.IExceptionProcessor;
import com.belk.car.app.exceptions.SendEmailException;
import com.belk.car.app.exceptions.UserRankException;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeValueProcessStatus;
import com.belk.car.app.model.BaseAuditableModel;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarNote;
import com.belk.car.app.model.CarSearchCriteria;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.CarUserVendorDepartment;
import com.belk.car.app.model.ClassAttribute;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.Config;
import com.belk.car.app.model.ContentStatus;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.DepartmentAttribute;
import com.belk.car.app.model.NoteType;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.model.PatternProcessingRule;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.model.ProductTypeAttribute;
import com.belk.car.app.model.SourceType;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.UsersRankTmp;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.VendorStyleSearchCriteria;
import com.belk.car.app.model.VendorStyleType;
import com.belk.car.app.model.WorkFlow;
import com.belk.car.app.model.car.ManualCar;
import com.belk.car.app.model.car.ManualCarProcessStatus;
import com.belk.car.app.model.catalog.CatalogProduct;
import com.belk.car.app.model.catalog.CatalogSku;
import com.belk.car.app.model.report.CarAttributeReport;
import com.belk.car.app.model.report.ReportType;
import com.belk.car.app.model.vendorimage.VendorImage;
import com.belk.car.app.model.vendorimage.VendorImageStatus;
import com.belk.car.app.model.view.CarAttributeView;
import com.belk.car.app.model.view.VendorStyleHexView;
import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.model.workflow.WorkflowType;
import com.belk.car.app.service.AttributeManager;
import com.belk.car.app.service.AttributeResynchJobManager;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.CatalogImportManager;
import com.belk.car.app.service.EmailManager;
import com.belk.car.app.service.ManualCarManager;
import com.belk.car.app.service.ProductManager;
import com.belk.car.app.service.QuartzJobManager;
import com.belk.car.app.service.ReportManager;
import com.belk.car.app.service.SizeConversionJobManager;
import com.belk.car.app.service.UserRankManager;
import com.belk.car.app.service.SuperColorManager;
import com.belk.car.app.service.WorkflowManager;
import com.belk.car.app.service.impl.QuartzJobManagerImpl.FileProcessorChain.ActionOnException;
import com.belk.car.app.to.IdbCarDataTO;
import com.belk.car.app.to.IdbCarSkuTO;
import com.belk.car.app.util.ExportToCESFile;
import com.belk.car.app.util.FtpUtil;
import com.belk.car.app.util.ReadIDBFile;
import com.belk.car.app.util.ReadRLRFile;
import com.belk.car.app.util.ReadUserRankFile;
import com.belk.car.app.util.SFtpUtil;
import com.belk.car.integrations.rrd.ProductPhotoRequestMessageWriter;
import com.belk.car.integrations.rrd.RRDUpdateMessageReader;
import com.belk.car.util.DateUtils;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.thoughtworks.xstream.converters.basic.StringBufferConverter;
/**
 * @author antoniog
 * 
 */
public class QuartzJobManagerImpl implements QuartzJobManager, IExceptionProcessor {

	private ManualCarManager manualCarManager;
	private CarLookupManager lookupManager;
	private UserManager userManager;
	private CarManager carManager;
	private WorkflowManager workflowManager;
	private ProductManager productManager;
	private EmailManager sendEmailManager;
	private CatalogImportManager catalogImportManager;
	private ReportManager reportManager;
    private SuperColorManager superColorManager;
    private SizeConversionJobManager sizeConversionJobManager;
	private AttributeResynchJobManager attributeResynchJobManager;
	private UserRankManager userRankManager;


	//  private CarManager carManager;
	public CarManager getCarManager() {
		return carManager;
	}
	String NEW_LINE = "\r\n";
	String TAB = "\t";

	/**
	 * @param sendEmailManager the sendEmailManager to set
	 */
	public void setSendEmailManager(EmailManager sendEmailManager) {
		this.sendEmailManager = sendEmailManager;
	}

	/**
	 * @param userManager
	 *            the userManager to set
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 * @param carManager
	 *            the carManager to set
	 */
	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	
	
	/**
	 * @param workflowManager
	 *            the workflowManager to set
	 */
	public void setWorkflowManager(WorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}

	/**
	 * @param attributeManager
	 *            the attributeManager to set
	 */
	public void setAttributeManager(AttributeManager attributeManager) {
		//this.attributeManager = attributeManager;
	}

	/**
	 * @param productManager
	 *            the productManager to set
	 */
	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}

	/**
	 * @param lookupManager
	 *            the lookupManager to set
	 */
	public void setLookupManager(CarLookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}

	/**
	 * @param manualCarManager
	 *            the manualCarManager to set
	 */
	public void setManualCarManager(ManualCarManager manualCarManager) {
		this.manualCarManager = manualCarManager;
	}

	public void setCatalogImportManager(CatalogImportManager catalogImportManager) {
		this.catalogImportManager = catalogImportManager;
	}

	public void setReportManager(ReportManager reportManager) {
		this.reportManager = reportManager;
	}

	/*
	 * Added for CARS Faceted Navigation
	 * 	 */
	public SuperColorManager getSuperColorManager() {
		return superColorManager;
	}

	public void setSuperColorManager(SuperColorManager superColorManager) {
		this.superColorManager = superColorManager;
	}
	
	
	public SizeConversionJobManager getSizeConversionJobManager() {
		return sizeConversionJobManager;
	}
	public void setSizeConversionJobManager(
			SizeConversionJobManager sizeConversionJobManager) {
		this.sizeConversionJobManager = sizeConversionJobManager;
	}
    
	public void setUserRankManager(UserRankManager userRankManager) {
		this.userRankManager = userRankManager;
	}

	public AttributeResynchJobManager getAttributeResynchJobManager() {
		return attributeResynchJobManager;
	}
	public void setAttributeResynchJobManager(
			AttributeResynchJobManager attributeResynchJobManager) {
		this.attributeResynchJobManager = attributeResynchJobManager;
	}
	public void exportProductsInTrend() throws IOException, CarJobDetailException {
		if (log.isInfoEnabled())
			log.info("---------------->  Begin Export Products In Trend Process <-----------------");
		
		ReportType reportType = reportManager.getReportType(ReportType.CAR_ATTRIBUTE_VALUE);

		// Get all the Hex Value, sorted by Vendor Number, Style Number
        CarAttributeReport attrReport = new CarAttributeReport() ;
        attrReport.setReportDate(Calendar.getInstance().getTime()) ;
        attrReport.setName(reportType.getReportName()) ;
        attrReport.getCriteria().put("AttributeName", "Trend") ;
        attrReport.getCriteria().put("EndExpectedShipDate", DateUtils.formatDate(Calendar.getInstance().getTime())) ;
        attrReport.setCarAttributeList(this.reportManager.getCarAttributeValues(attrReport)) ;	
        if (attrReport.getCarAttributeList() != null && !attrReport.getCarAttributeList().isEmpty()) {
    		Config exportDirConfig = (Config) lookupManager.getById(Config.class, Config.TREND_EXPORT_DIRECTORY);
    		Config exportFileConfig = (Config) lookupManager.getById(Config.class, Config.TREND_EXPORT_FILENAME);
    		Config ftpHost = (Config) lookupManager.getById(Config.class, Config.TREND_EXPORT_FTP_HOST);
    		Config ftpUserId = (Config) lookupManager.getById(Config.class, Config.TREND_EXPORT_FTP_USERNAME);
    		Config ftpPassword = (Config) lookupManager.getById(Config.class, Config.TREND_EXPORT_FTP_PASSWORD);
    		Config ftpRemoteDir = (Config) lookupManager.getById(Config.class, Config.TREND_EXPORT_FTP_REMOTE_DIRECTORY);
    		Config trendExclusionVal = (Config) lookupManager.getById(Config.class, Config.TREND_EXCLUSION_VALUE);

    		File dir = new File(exportDirConfig.getValue());
    		if (!dir.exists())
    			dir.mkdirs();

    		// Create a File object
    		String fileName = exportFileConfig.getValue();
    		String filePath = exportDirConfig.getValue() + fileName;
			File file = new File(filePath);

			if (!file.exists()) {
				// Create file on disk (if it doesn't exist)
				file.createNewFile();
			}
			
			String strTrendExclusionVal = trendExclusionVal.getValue();

			FileWriter out = null;
			boolean hasErrors = false ;
			try {
				out = new FileWriter(file, false);
				for (CarAttributeView view : attrReport.getCarAttributeList()) {
					StringBuffer sb = new StringBuffer();
					int startPos = view.getAttrValue().indexOf("-") ;
					if (startPos < 0)
						startPos = -1 ;
					String strTrendAttrVal = view.getAttrValue().substring(startPos+1).trim() ;
					if(strTrendAttrVal.equalsIgnoreCase(strTrendExclusionVal)) {	//Do not write CAR IDs (records) with trend value "N/A" in BMI file - to fix coremetrics job slowness issue 
						continue;
					}
					sb.append("I")
					.append("|")
					.append("ASSORTMENT_CONTENTS")
					.append("|")
					.append("/Assortments/The_Fashion_Report/The_Fashion_Report")
					.append("|")
					.append("PRODUCT")
					.append("|")
					.append(view.getVendorNumber()).append(view.getStyleNumber())
					.append("|")
					.append(view.getAttrId().longValue()==2910?"Most_Wanted_Women/":(view.getAttrId().longValue()==2953?"Most_Wanted_Men/":"Most_Wanted_Junior/"))
					.append(strTrendAttrVal.replaceAll("&", "").replaceAll("  "," ").replaceAll(" ","_").replaceAll("-", "_"))
					.append(NEW_LINE);
					out.write(sb.toString());
				}
			} catch (Exception ex) {
    			ex.printStackTrace();
    			hasErrors = true ;
    			//Send Notification on Exception to People in the List...
    			processException("Export Trends", ex);
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (Exception ex) {
					}
				}
			}

			if (file.exists()) {
				if (!hasErrors) {
					//FTP File to Server
					if(ftpHost != null && StringUtils.isNotBlank(ftpHost.getValue())) {
						try  {
							Session session =SFtpUtil.openSftpSession(ftpHost.getValue(), ftpUserId.getValue(), ftpPassword.getValue(), 22, "no");
							Channel channel = session.openChannel("sftp");
				            channel.connect();
				            ChannelSftp  sftpChannel = (ChannelSftp) channel;
				            SFtpUtil.sendDataFiles(sftpChannel, Arrays.asList(file), ftpRemoteDir.getValue());
				            SFtpUtil.closeSftpConnection(session,sftpChannel);
						}catch(Exception e) {
							log.error(e.getMessage(),e);
						}
						
						/*FTPClient ftp = FtpUtil.openConnection(ftpHost.getValue(), ftpUserId.getValue(), ftpPassword.getValue());
						FtpUtil.setTransferModeToAscii(ftp);
						FtpUtil.sendDataFiles(ftp, Arrays.asList(file), ftpRemoteDir.getValue());
						FtpUtil.closeConnection(ftp);*/
					}
				}

				//Archive File to archive directory for Support Issues
				Config completedDirectory = (Config) lookupManager.getById(Config.class, Config.CAR_FILE_COMPLETED_DIRECTORY_NAME);
				this.processCompletedFile(exportDirConfig.getValue(), fileName, completedDirectory.getValue());
			}
        }
		if (log.isInfoEnabled())
			log.info("---------------->  End Export Products In Trend Process <-----------------");
	}
	
	
	
	public void exportHexValues() throws IOException, CarJobDetailException {
		if (log.isInfoEnabled())
			log.info("---------------->  Begin Export Hex Values Process <-----------------");

		// Get all the Hex Value, sorted by Vendor Number, Style Number
		List<VendorStyleHexView> data = reportManager.getVendorStyleHexValues();

		// Create a Map of Vendor Number/Style Number with List of Hex Values
		Map<String, List<VendorStyleHexView>> vsMap = new HashMap<String, List<VendorStyleHexView>>();
		for (VendorStyleHexView view : data) {
			String prodCode = view.getVendorNumber() + "_" + view.getStyleNumber();
			List<VendorStyleHexView> prodList = vsMap.get(prodCode);
			if (prodList == null) {
				prodList = new ArrayList<VendorStyleHexView>();
				prodList.add(view);
				vsMap.put(prodCode, prodList);
			} else {
				prodList.add(view);
			}
		}

		Config exportDirConfig = (Config) lookupManager.getById(Config.class, Config.HEXVALUE_EXPORT_DIRECTORY);
		Config exportFileConfig = (Config) lookupManager.getById(Config.class, Config.HEXVALUE_EXPORT_FILENAME);

		// Loop thru the Map
		// For each Vendor Style Number, Create File with vendornumber_stylenumber_color.txt
		// Write all Hex Values to the File
		// Close the File
		List<File> files = new ArrayList<File>();
		for (String vsName : vsMap.keySet()) {
			List<VendorStyleHexView> vsList = vsMap.get(vsName);
			if (vsList != null) {
				File file = new File(exportDirConfig.getValue() + vsName + exportFileConfig.getValue());

				if (!file.exists()) {
					// Create file on disk (if it doesn't exist)
					file.createNewFile();
				}

				FileWriter out = null;
				try {
					out = new FileWriter(file, false);
					for (VendorStyleHexView view : vsList) {
						StringBuffer sb = new StringBuffer();
						sb.append(view.getVendorNumber()).append(TAB).append(view.getStyleNumber()).append(TAB).append(view.getColorCode()).append(TAB).append(
								view.getHexValue()).append(NEW_LINE);
						out.write(sb.toString());
					}
					files.add(file);
				} catch (IOException ioex) {
					// DO NOTHING FOR NOW
				} finally {
					if (out != null) {
						try {
							out.close();
						} catch (Exception ex) {
						}
					}
				}
			}
		}

		Config ftpHost = (Config) lookupManager.getById(Config.class, Config.HEXVALUE_EXPORT_FTP_HOST);
		Config ftpUserId = (Config) lookupManager.getById(Config.class, Config.HEXVALUE_EXPORT_FTP_USERNAME);
		Config ftpPassword = (Config) lookupManager.getById(Config.class, Config.HEXVALUE_EXPORT_FTP_PASSWORD);
		Config ftpRemoteDir = (Config) lookupManager.getById(Config.class, Config.HEXVALUE_EXPORT_FTP_REMOTE_DIRECTORY);

		if (!files.isEmpty()) {
			try  {
				Session session =SFtpUtil.openSftpSession(ftpHost.getValue(), ftpUserId.getValue(), ftpPassword.getValue(), 22, "no");
				Channel channel = session.openChannel("sftp");
	            channel.connect();
	            ChannelSftp  sftpChannel = (ChannelSftp) channel;
	            SFtpUtil.sendDataFiles(sftpChannel, files, ftpRemoteDir.getValue());
	            SFtpUtil.closeSftpConnection(session,sftpChannel);
			}catch(Exception e) {
				log.error(e.getMessage(),e);
			}
			/*FTPClient ftp = FtpUtil.openConnection(ftpHost.getValue(), ftpUserId.getValue(), ftpPassword.getValue());
			FtpUtil.setTransferModeToAscii(ftp);
			FtpUtil.sendDataFiles(ftp, files, ftpRemoteDir.getValue());
			FtpUtil.closeConnection(ftp);
*/		}
		if (log.isInfoEnabled())
			log.info("---------------->  End Export Hex Values Process <-----------------");

	}

	private transient final Log log = LogFactory.getLog(QuartzJobManagerImpl.class);

	/**
	 * Retrieve all Active --> Open Cars --> Where Content is Not IN-PROGRESS
	 * Check if Pickup Images have marked as RECEIVED
	 * Check if All Requested Samples are marked RECEIVED
	 * Check if there are Atleast 1 Image per Sample RECEIVED
	 * If all the above criteria are Met, the marked the CAR Workflow Status to CLOSED
	 * Run the process after IMPORT of RRD Feed
	 */
	public void closeCars() throws IOException, CarJobDetailException {
		if (log.isInfoEnabled())
			log.info("---------------->  Begin Close Cars Process <-----------------");
                
                boolean lockFileCreated = createLockFile(Config.CLOSE_CARS_LOCK_PATH);
                if (!lockFileCreated) {
                    if (log.isErrorEnabled()) {
                        log.error("Exiting.  Another process is already running!");
                        log.info("---------------->  End Closing Cars Process  <-----------------");
                    }
                    sendFailureNotification("Close CARS","Job was not executed on schedule due to the presence of a lockfile.");
            
                    return;
                }
                
		CarSearchCriteria carSearchCriteria = new CarSearchCriteria();
		carSearchCriteria.setStatusCd(Status.ACTIVE);
		carSearchCriteria.setIgnoreUser(true);
		carSearchCriteria.setIgnoreClosedCars(true);
		//Will retrieve only cars where ContentStatus is SENT_TO_CMP and all Images have been RECEIVED
		carSearchCriteria.setReadyToClose(true);
		List<Car> cars = carManager.searchCars(carSearchCriteria);
		if (cars == null || cars.isEmpty()) {
			log.info("No cars to close. This job will resume execution at its next scheduled time.");
                        deleteLockFile(Config.CLOSE_CARS_LOCK_PATH);
                        log.info("---------------->  End Closing Cars Process  <-----------------");
			
                        return;
		}

		WorkflowStatus closedStatus = (WorkflowStatus) this.lookupManager.getById(WorkflowStatus.class, WorkflowStatus.CLOSED);
		for (Car car : cars) {
			if (ExportToCESFile.haveAllImagesBeenReceived(car)) {
				if (log.isDebugEnabled())
					log.debug("Closing Car: " + car.getCarId());
				car.setCurrentWorkFlowStatus(closedStatus);
				car.setLastWorkflowStatusUpdateDate(new Date());
				carManager.save(car);
			}
		}
                
                deleteLockFile(Config.CLOSE_CARS_LOCK_PATH);
                
		if (log.isInfoEnabled())
			log.info("---------------->  End Closing Cars Process  <-----------------");
	}

	public void exportCESInfoWithContent() throws IOException, CarJobDetailException {
                boolean lockFileCreated = createLockFile(Config.CMP_EXPORT_LOCK_PATH);
                if (!lockFileCreated) {
                    if (log.isErrorEnabled()) {
                        log.error("Exiting.  Another process is already running!");
                    }
                    sendFailureNotification("Export to CMP","Job was not executed on schedule due to the presence of a lockfile.");
            
                    return;
                }
                try {
                    processExportCESInfoToFile(true);
                } catch (Exception e) {
                    log.error("Caught exception in export to CMP: " + e.getMessage());
                    processException("Export to CMP", e);
                } finally {
                    deleteLockFile(Config.CMP_EXPORT_LOCK_PATH);
                }
	}

	public void exportCESInfoWithoutContent() throws IOException, CarJobDetailException {
            
                boolean lockFileCreated = createLockFile(Config.CMP_EXPORT_LOCK_PATH);
                if (!lockFileCreated) {
                    if (log.isErrorEnabled()) {
                        log.error("Exiting.  Another process is already running!");
                    }
                    sendFailureNotification("Export to CMP","Job was not executed on schedule due to the presence of a lockfile.");
            
                    return;
                }
                try {
                    processExportCESInfoToFile(false);
                } catch (Exception e) {
                    log.error("Caught exception in export to CMP: " + e.getMessage());
                    processException("Export to CMP", e);
                } finally {
                    deleteLockFile(Config.CMP_EXPORT_LOCK_PATH);
                }
	}

	private void processExportCESInfoToFile(boolean exportContentAttributes) throws IOException, CarJobDetailException {
		if (log.isInfoEnabled())
			log.info("---------------->  Begin Exporting CES Data <-----------------");

		Config exportDirConfig = (Config) lookupManager.getById(Config.class, Config.CES_EXPORT_DIRECTORY);
		Config exportFileConfig = (Config) lookupManager.getById(Config.class, Config.CES_EXPORT_FILENAME);
		Config sftpHost = (Config) lookupManager.getById(Config.class, Config.CES_EXPORT_FTP_HOST);
		Config sftpUserId = (Config) lookupManager.getById(Config.class, Config.CES_EXPORT_FTP_USERNAME);
		Config sftpPassword = (Config) lookupManager.getById(Config.class, Config.CES_EXPORT_FTP_PASSWORD);
		Config sftpRemoteDir = (Config) lookupManager.getById(Config.class, Config.CES_EXPORT_FTP_REMOTE_DIRECTORY);
		//Get all active Cars that are not Closed
		CarSearchCriteria carSearchCriteria = new CarSearchCriteria();
		carSearchCriteria.setStatusCd(Status.ACTIVE);
		carSearchCriteria.setIgnoreUser(true);
		carSearchCriteria.setIgnoreClosedCars(false);
		carSearchCriteria.setReadyToSendToCMP(true);
		List<Car> cars = carManager.searchCars(carSearchCriteria);
		if (cars == null || cars.isEmpty()) {
			log.info("No data found to process job. This job will resume execution at its next scheduled time.");
			log.info("---------------->  End Exporting CES Data   <-----------------");
			return;
		}
                
                // now that this job runs during the day, dont export locked cars to avoid data discrepancy.
                List <Car> noLockedCars = new ArrayList<Car>();
                for (Car c : cars) {
                    if (!c.isLocked()) {
                        noLockedCars.add(c);
                    } else {
                        log.info("Not processing locked car: " + c.getCarId());
                    }
                }  
                
                cars = noLockedCars;
                
		File dir = new File(exportDirConfig.getValue());
		if (!dir.exists())
			dir.mkdirs();

		// Create a File object
		String fileName = DateUtils.formatDate(new Date(), "MM_dd_yyyy_HH_mm_ss") + "_" + exportFileConfig.getValue();
		String file = exportDirConfig.getValue() + fileName;

		ExportToCESFile cesExtract = new ExportToCESFile(cars);
		cesExtract.setCarManager(this.carManager);
		cesExtract.setLookupManager(this.lookupManager);
		cesExtract.setExportContentAttributes(exportContentAttributes);
		cesExtract.setSuperColorManager(this.superColorManager);
		cesExtract.setSizeConversionJobManager(sizeConversionJobManager);
		try {
			cesExtract.export(file);

			File f = new File(file);
			if (f.exists()) {
				log.info("export directory file name"+file);
				//FTP File to Server
				if (sftpHost != null && StringUtils.isNotBlank(sftpHost.getValue())) {
                    if (log.isInfoEnabled()) {
                        log.info("-------------------------->Transferring the generated xml to CMP<---------------------");
                    }
					Session session =SFtpUtil.openSftpSession(sftpHost.getValue(), sftpUserId.getValue(), sftpPassword.getValue(), 22, "no");
					Channel channel = session.openChannel("sftp");
			        channel.connect();
			        ChannelSftp  sftpChannel = (ChannelSftp) channel;
			        SFtpUtil.sendDataFiles(sftpChannel, Arrays.asList(f), sftpRemoteDir.getValue());
			        SFtpUtil.closeSftpConnection(session,sftpChannel);
			        //FTPClient ftp = FtpUtil.openConnection(ftpHost.getValue(), ftpUserId.getValue(), ftpPassword.getValue());
					//FtpUtil.setTransferModeToAscii(ftp);
					//FtpUtil.sendDataFiles(ftp, Arrays.asList(f), ftpRemoteDir.getValue());
					//FtpUtil.closeConnection(ftp);
				}
				//Archive File to archive directory for Support Issues
				Config completedDirectory = (Config) lookupManager.getById(Config.class, Config.CAR_FILE_COMPLETED_DIRECTORY_NAME);
				log.debug("Completed file directory"+completedDirectory.getValue()+": file name:"+fileName);
				this.processCompletedFile(exportDirConfig.getValue(), fileName, completedDirectory.getValue());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("export CES Failed due to :"+ex.getMessage(),ex);
			log.debug("---------------->Send CES Failure notification to people in the List...");
			 processException("Export CES", ex);
		}

		if (log.isInfoEnabled())
			log.info("---------------->  End Exporting CES Data   <-----------------");

	}

	public void exportManualCarsToFile() throws IOException, CarJobDetailException {
		if (log.isInfoEnabled())
			log.info("---------------->  Begin Exporting Car Data <-----------------");

		Config exportDirConfig = (Config) lookupManager.getById(Config.class, Config.CAR_EXPORT_DIRECTORY);
		Config exportFileConfig = (Config) lookupManager.getById(Config.class, Config.CAR_EXPORT_FILENAME);
		Config userName = (Config) lookupManager.getById(Config.class, Config.CAR_IMPORT_USER);

		User systemUser = this.userManager.getUserByUsername(userName.getValue());

		List<ManualCar> manualCars = this.manualCarManager.getManualCars(ManualCarProcessStatus.INITIATED);
		if (manualCars == null || manualCars.isEmpty()) {
			log.info("No data found to process job. This job will resume execution at its next scheduled time.");
			return;
		}

		ManualCarProcessStatus exported = this.lookupManager.getManualCarProcessStatus(ManualCarProcessStatus.EXPORTED);

		File dir = new File(exportDirConfig.getValue());
		if (!dir.exists())
			dir.mkdirs();

		// Create a File object
		File file = new File(exportDirConfig.getValue() + exportFileConfig.getValue());

		if (!file.exists()) {
			// Create file on disk (if it doesn't exist)
			file.createNewFile();
		}

		FileWriter out = null;
		try {
			out = new FileWriter(file, true);
			for (ManualCar mc : manualCars) {
				StringBuffer sb = new StringBuffer();
				if (mc.getStatusCd().equals(Status.ACTIVE)) {
					// Pad 44 Space as we are not send the PO and Other
					// Information
					// Assumes that we are getting all Size and Color for Vendor
					// Style "S"
					//Vendor + Style
					String type = "S";
					if (StringUtils.isNotBlank(mc.getColorDescription())) {
						//Vendor + Style + Color
						type = "C";
						if (StringUtils.isNotBlank(mc.getSizeDescription())) {
							//Vendor + Style + Color + Size
							type = "Z";
						}
					}

					sb.append(StringUtils.leftPad("", 44)).append(StringUtils.rightPad(mc.getVendorNumber().toUpperCase(), 7)).append(
							StringUtils.rightPad(mc.getVendorStyleNumber().toUpperCase(), 20)).append(
							StringUtils.rightPad(mc.getColorDescription() == null ? StringUtils.EMPTY : mc.getColorDescription().toUpperCase(), 3)).append(
							StringUtils.rightPad(mc.getSizeDescription() == null ? StringUtils.EMPTY : mc.getSizeDescription().toUpperCase(), 5)).append(type)
							.append(StringUtils.rightPad(String.valueOf(mc.getManualCarId()), 12)).append(NEW_LINE);

					mc.setProcessStatus(exported);
					this.setAuditInfo(systemUser, mc);

					this.manualCarManager.save(mc);
				}
				out.write(sb.toString());
			}
		} catch (IOException ioex) {
			// DO NOTHING FOR NOW
			processException("Export Manual Cars", ioex);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception ex) {
				}
			}
		}

		if (file.exists() && file.canRead() && file.length() > 0) {
			//Archive File to archive directory for Support Issues
			Config completedDirectory = (Config) lookupManager.getById(Config.class, Config.CAR_FILE_COMPLETED_DIRECTORY_NAME);
			//Copy file to archive directory
			processCompletedFile(exportDirConfig.getValue(), exportFileConfig.getValue(), completedDirectory.getValue(), true);
		}

		if (log.isInfoEnabled())
			log.info("---------------->  End Exporting Car Data   <-----------------");

	}

	public void importRLR() throws IOException, CarJobDetailException, NamingException {
		if (log.isInfoEnabled())
			log.info("---------------->  Begin importing RLR data <-----------------");

		// Initialize Global Variables
		Config importDirectory = (Config) lookupManager.getById(Config.class, Config.RLR_IMPORT_DIRECTORY);
		Config completedDirectory = (Config) lookupManager.getById(Config.class, Config.CAR_FILE_COMPLETED_DIRECTORY_NAME);
		Config userName = (Config) lookupManager.getById(Config.class, Config.CAR_IMPORT_USER);

		User systemUser = this.userManager.getUserByUsername(userName.getValue());

		// We need all of the config values
		if (importDirectory == null || userName == null || completedDirectory == null) {
			throw new CarJobDetailException("Cannot process import job. A configuration value is missing");
		}

		File dir = new File(importDirectory.getValue());
		File[] files = dir.listFiles();
		if (files == null) {
			// Directory doesn't exist or there aren't any files to process
			if (log.isInfoEnabled())
				log.info("There are no files to process in directory " + importDirectory.getValue()
						+ ". This job will resume execution at its next scheduled time.");
		} else {
			PatternProcessingRule defPatternProcessingRule = (PatternProcessingRule) lookupManager.getById(PatternProcessingRule.class,
					PatternProcessingRule.NONE);
			for (File file : files) {
				//Only process files in the directory
				if (!file.isDirectory()) {
					Collection<DepartmentClassUpload> col = ReadRLRFile.process(file);
					//Empty file or file error
					if (col == null || col.isEmpty()) {
						if (log.isInfoEnabled())
							log.info("There was an error processing. " + file.getName() + " Skipping to next file...");
					} else {
						//Process Data...
						for (DepartmentClassUpload data : col) {
							if (StringUtils.isNotBlank(data.getClassCode())) {
								Classification cls = carManager.getClassification(Short.parseShort(data.getClassCode()));
								Department dept = carManager.getDepartment(data.getDeptCode());

								// Check if Department exists and create if does not exists
								if (dept == null) {
									dept = new Department();
									dept.setDeptCd(data.getDeptCode());
									dept.setStatusCd(Status.ACTIVE);
									dept.setName(data.getDeptName());
									dept.setDescription(data.getDeptName());
									this.setAuditInfo(systemUser, dept);
									dept = (Department) carManager.save(dept);
									//assign new department to Art Directors and Sample coordinators
									assignNewDepartment(dept);
								} else {
									if (!StringUtils.equals(data.getDeptName(), dept.getName())) {
										dept.setName(data.getDeptName());
										dept.setDescription(data.getDeptName());
										this.setAuditInfo(systemUser, dept);
										dept = (Department) carManager.save(dept);
									}
								}

								//Check if Classification exists and create if does not exists
								if (cls == null) {
									//create class
									cls = new Classification();
									cls.setBelkClassNumber(Short.parseShort(data.getClassCode()));
									cls.setIsProductTypeRequired(Classification.FLAG_NO);
									cls.setStatusCd(Status.ACTIVE);
									cls.setPatternProcessingRule(defPatternProcessingRule);
								}

								cls.setName(data.getClassName());
								cls.setDescr(data.getClassName());

								//Department has changed
								//If Department Association changes we need to update the relationship
								if (cls.getDepartment() == null || !data.getDeptCode().equals(cls.getDepartment().getDeptCd())) {
									cls.setDepartment(dept);
								}
								this.setAuditInfo(systemUser, cls);
								carManager.save(cls);
							}
						}
					}
					processCompletedFile(importDirectory.getValue(), file.getName(), completedDirectory.getValue());
				}
			}
		}

		if (log.isInfoEnabled())
			log.info("---------------->  End importing RLR Data <-----------------");
	}

	/**
	 * importCars
	 */
	public void importCars() throws IOException, CarJobDetailException, NamingException {
		if (log.isInfoEnabled())
			log.info("---------------->  Begin importing Car Data <-----------------");

		// Initialize Global Variables
		Collection<IdbCarDataTO> col = null;

		Config importDirectory = (Config) lookupManager.getById(Config.class, Config.CAR_IMPORT_DIRECTORY);
		Config importFileName = (Config) lookupManager.getById(Config.class, Config.CAR_IMPORT_FILENAME);
		Config completedDirectory = (Config) lookupManager.getById(Config.class, Config.CAR_FILE_COMPLETED_DIRECTORY_NAME);
		Config inprocessDirName = (Config) lookupManager.getById(Config.class, Config.CAR_FILE_INPROCESS_DIRECTORY_NAME);
		Config errorDirName = (Config) lookupManager.getById(Config.class, Config.CAR_FILE_ERROR_DIRECTORY_NAME);
				// We need all of the config values
		if (importDirectory == null || importFileName == null || completedDirectory == null ||inprocessDirName == null || errorDirName ==null ) {
			throw new CarJobDetailException("Cannot process import job. A configuration value is missing");
		}
		String strInprocessDir=importDirectory.getValue() + inprocessDirName.getValue();
		String strArchiveDir=importDirectory.getValue() + completedDirectory.getValue();
		String strErrorDir=importDirectory.getValue() + errorDirName.getValue();
		
		File inprocessDir= new File(strInprocessDir);
		if(inprocessDir.isDirectory()){
			File[] fileList= inprocessDir.listFiles();
			if(fileList == null || fileList.length > 0){
				log.error("Files are still in inprocess, Could not process the file");
				if(log.isInfoEnabled()){
					log.info("---------------->  End importing Car Data <-----------------");
				}
				return;
			}
		}
		boolean scanDirectory = StringUtils.isNotBlank(importDirectory.getValue()) && StringUtils.isBlank(importFileName.getValue());
		File[] files = null;
		if (scanDirectory) { //User selected to process all files under the directory			
			File dir = new File(importDirectory.getValue());
			files = dir.listFiles();
		} else { //User selected to scan a single file
			String filePath = importDirectory.getValue() + importFileName.getValue();
			File fl = new File(filePath);
			if (fl.exists()) {
				files = new File[] { fl };
			}
		}
		// Directory doesn't exist or there aren't any files to process
		if (files == null) {
			if (log.isInfoEnabled()) {
				if (scanDirectory) {
					log.info("There are no files to process in directory: " + importDirectory.getValue() + " Exiting job...");
				} else {
					log.info("File " + importDirectory.getValue() + importFileName.getValue() + " not found!!! Nothing to process. Exiting job...");
				}
			}
		} else{
				List<File> destFiles = moveFilesToDirectory(files, inprocessDir, false);
				if (destFiles == null || destFiles.size() < 1){
				if(log.isInfoEnabled()){
					log.info("There are No files to process");
					log.info("---------------->  End importing Car Data <-----------------");
				}
				return;
			  }
			 try{ 
				//Process Files
				  for (File file : destFiles) {
					 try{	
						 if(!file.isDirectory()) {
							col = ReadIDBFile.process(file);
							if (col == null || col.isEmpty()) {
								if (log.isInfoEnabled())
									log.info("No Sku data in  file " + file.getName() + " Skipping to next file...");
							} else {
								processCars(col);
							}
							List<File> archiveFiles = moveFilesToDirectory(new File[] {file}, new File(strArchiveDir) , true); 
							if (archiveFiles != null || archiveFiles.size() > 0) {
								if (log.isInfoEnabled())
									log.info(file.getAbsolutePath() + " file is archived successfully.");
							} else {
								log.error("Error moving file " + file.getAbsolutePath() + " to directory: " + strArchiveDir);
								List<File> errorFiles = moveFilesToDirectory(new File[]{file}, new File(strErrorDir), true);
								if (errorFiles != null || errorFiles.size() > 0) {
									if (log.isInfoEnabled())
										log.info(file.getAbsolutePath() + " file moved to error directory");
								}
							}
						 }
					  }catch(Exception ex){
							log.error("Exception while Reading/Processing PO file", ex);
							List<File> errorFiles = moveFilesToDirectory(new File[]{file}, new File(strErrorDir), true);
							if (errorFiles != null || errorFiles.size() > 0) {
							  if (log.isInfoEnabled())
									log.info(file.getAbsolutePath() + " file moved to error directory");
							}
					   }
					}
			 }finally{
				 	File[] fileList = inprocessDir.listFiles();
				 	if(fileList != null && fileList.length > 0){
						log.error(fileList.length  + " files are still inprocess directory, Moving it to error directory");
				 		moveFilesToDirectory(fileList, new File(strErrorDir), true);
				 	}
				 }
			} 
		/*try{
			
			 File file = new File("C://cars//data//carsdata//import_cars//cars.txt");
			 col = ReadIDBFile.process(file);
				if (col == null || col.isEmpty()) {
					if (log.isInfoEnabled())
						log.info("No Sku data in  file " + file.getName() + " Skipping to next file...");
				} else {
					processCars(col);
				}
		}catch(Exception e){
			e.printStackTrace();
		}*/
		if (log.isInfoEnabled())
			log.info("---------------->  End importing Car Data <-----------------");
		
		if (log.isInfoEnabled())
			log.info("---------------->  Begin resynch Size Data Process <-----------------");
		try{
			sizeConversionJobManager.resynchSizeValuesOfVendorSkus();
		}catch(Exception ex){
			log.error("Exception in resynch Size Data Process:"+ex.toString());
			ex.printStackTrace();
		}
		if (log.isInfoEnabled())
			log.info("---------------->  End resynch Size Data Process <-----------------");
	}

	/**
	 * Resynch Attributes
	 */
	public void resynchAttributes() throws IOException {
		if (log.isInfoEnabled())
			log.info("---------------->  Begin Resynching Attribute Data <-----------------");
		try {
                    
                    attributeResynchJobManager.resynchAttributes();
		} catch(Exception ex) {
			log.error("Exception in resynch Attribute Data Process:"+ex.toString());
			ex.printStackTrace();
		}
		if (log.isInfoEnabled())
			log.info("---------------->  End Attribute resynch Data Process <-----------------");
	}

	
	/**
	 * moveFilesToDirectory
	 * @param files : List of files to be moved
	 * @param destinationDir : destination directory for files
	 * @param isDateAppend : date should append to destination file name?
	 * @return List<File> : List of new files 
	 */
	public List<File> moveFilesToDirectory(File[] files, File destinationDir, boolean isDateAppend){
		String strDestFileName=null;
		List<File> destFiles = new ArrayList<File>();
		if(files == null || files.length < 1 || destinationDir == null || !destinationDir.isDirectory()){
			log.error("moveFilesToDirectory : Error moving files");
			return null;
		}
		for (File f: files) {
			if(f.exists() && f.isFile()){
				if (isDateAppend){
					strDestFileName= destinationDir.getAbsolutePath() + "//" + DateUtils.formatDate(new Date(), "MM_dd_yyyy_HH_mm_ss" ) + "_" +f.getName();
				}else{
					strDestFileName= destinationDir.getAbsolutePath() + "//" + f.getName();
				}
				File destFile = new File (strDestFileName);
				boolean isRenamed = f.renameTo(destFile);
				if (!isRenamed){
					if (log.isInfoEnabled())
						log.info("Could not move files : " + f.getAbsolutePath() + " to " + destFile.getAbsolutePath());
				}else{
					destFiles.add(destFile);
					if (log.isInfoEnabled())
						log.info("File Moved from  : " + f.getAbsolutePath() + " to " + destFile.getAbsolutePath());
				}
			} 
		}
		return destFiles;
	}
	
	/**
	 * processCars
	 * @param col
	 */
	protected void processCars(Collection<IdbCarDataTO> col) {

		if (col != null) {
			if (log.isInfoEnabled())
				log.info("Processing Cars: There are " + col.size() + " vendor styles in the file.");
		}
		Config userName = (Config) lookupManager.getById(Config.class, Config.CAR_IMPORT_USER);

		WorkflowStatus initiated = (WorkflowStatus) this.lookupManager.getById(WorkflowStatus.class, WorkflowStatus.INITIATED);
		WorkflowStatus withVendor = (WorkflowStatus) this.lookupManager.getById(WorkflowStatus.class, WorkflowStatus.WITH_VENDOR);
		UserType buyer = (UserType) this.lookupManager.getById(UserType.class, UserType.BUYER);
		UserType vendorType = (UserType) this.lookupManager.getById(UserType.class, UserType.VENDOR);

		ManualCarProcessStatus completeWithError = (ManualCarProcessStatus) carManager.getFromId(ManualCarProcessStatus.class,
				ManualCarProcessStatus.COMPLETED_WITH_ERROR);
		ManualCarProcessStatus completed = (ManualCarProcessStatus) carManager.getFromId(ManualCarProcessStatus.class, ManualCarProcessStatus.COMPLETED);

		SourceType sourceManualCar = carManager.getSourceTypeForCode(SourceType.MANUAL);
		SourceType sourcePOCar = carManager.getSourceTypeForCode(SourceType.PO);
		SourceType sourceFJCar = carManager.getSourceTypeForCode(SourceType.FINE_JEWELRY);
		AttributeValueProcessStatus checkRequired = lookupManager.getAttributeValueProcessStatus(AttributeValueProcessStatus.CHECK_REQUIRED);
		AttributeValueProcessStatus noCheckRequired = lookupManager.getAttributeValueProcessStatus(AttributeValueProcessStatus.NO_CHECK_REQUIRED);

		NoteType carNoteType = (NoteType) lookupManager.getById(NoteType.class, NoteType.CAR_NOTES);
		WorkFlow defaultWorkflow = workflowManager.getWorkFlow(workflowManager.getWorkflowType(WorkflowType.CAR));

		ContentStatus contentInProgress = (ContentStatus) lookupManager.getById(ContentStatus.class, ContentStatus.IN_PROGRESS);
		//VendorStyleType vendorStyleTypePattern = (VendorStyleType) lookupManager.getById(VendorStyleType.class, VendorStyleType.PATTERN_SPLIT_VS);
		VendorStyleType vendorStyleTypeProduct = (VendorStyleType) lookupManager.getById(VendorStyleType.class, VendorStyleType.PRODUCT);

		User systemUser = this.userManager.getUserByUsername(userName.getValue());

		String vendorContactEmail = "vendor@belk.com";
		Date escalationDate = this.getEscalationDate();

	    //boolean isAssignedToVendor = false;
	    		
		
		// Benchmark
		long executionTime = System.currentTimeMillis();
		int totalRecordSize = col.size();
		int rec = 0, numSkusInFile = 0, numElimSkus = 0;
		Map<VendorStyle, List<IdbCarDataTO>> productMap = new HashMap<VendorStyle, List<IdbCarDataTO>>();

		nextCarFromIdb: 
		for (IdbCarDataTO idbCarTO : col) {
			if (log.isDebugEnabled())
				log.debug("Processing Vendor Style: " + idbCarTO.getVendorStyle() + " which has " + idbCarTO.getSkuInfo().size() + " skus...");
			numSkusInFile = numSkusInFile + idbCarTO.getSkuInfo().size();
			try {
				if (VendorStyle.FLAG_NO.equals(idbCarTO.getValidItemFlag())) {
					if (log.isErrorEnabled())
						log.error("Error in retrieving Item Information from IDB");
					// Invalid Data
					if (StringUtils.isNotBlank(idbCarTO.getManualCarId())) {
						try {
							ManualCar mCar = (ManualCar) carManager.getFromId(ManualCar.class, Long.parseLong(idbCarTO.getManualCarId()));
							if (mCar != null) {
								mCar.setProcessStatus(completeWithError);
								this.setAuditInfo(systemUser, mCar);
								carManager.save(mCar);
							}
						} catch (Exception ex) {
							if (log.isErrorEnabled())
								log.error("Error in processing manual car: " + ex);
						}
					}
					numElimSkus = numElimSkus + idbCarTO.getSkuInfo().size();
					continue nextCarFromIdb;
				}

				
				// Check if Department Exists
				Department dept = carManager.getDepartment(idbCarTO.getDepartmentNumber());
				List<String> errorList = new ArrayList<String>();
				if (dept == null) {
					// Missing Department Do Not Process Data
					String errorText = "Department: " + idbCarTO.getDepartmentNumber() + " not setup in the database";
					errorList.add(errorText);
					if (log.isDebugEnabled()) {
						log.debug(errorText);
					}

				}
				idbCarTO.setDepartment(dept);

				Classification classification = null;
				if (StringUtils.isBlank(idbCarTO.getClassNumber())) {
					String errorText = "Classification Is Null For: " + idbCarTO.getVendorStyle() + ", invalid data!!";
					errorList.add(errorText);
					if (log.isDebugEnabled()) {
						log.debug(errorText);
					}
				} else {
					classification = carManager.getClassification(Short.parseShort(idbCarTO.getClassNumber()));
				}

				if (classification == null) {
					// Missing Classification Do Not Process Data
					String errorText = "Classification: " + idbCarTO.getClassNumber() + " not setup in the database";
					errorList.add(errorText);
					if (log.isDebugEnabled()) {
						log.debug(errorText);
					}
				}

				idbCarTO.setClassification(classification);

				if (StringUtils.isNotBlank(idbCarTO.getManualCarId())) {
					idbCarTO.setManualCar((ManualCar) carManager.getFromId(ManualCar.class, Long.parseLong(idbCarTO.getManualCarId())));
				}

				
				//Check if Vendor Style Exists
				//If VendorStyle Exists then Check if SKU Exists in CAR
				//If VendorStyle itself does not Exist, there is no point is check for CARS
				VendorStyle vendorStyle = carManager.getVendorStyle(idbCarTO.getVendorNumber(), idbCarTO.getVendorStyle());
				VendorStyle pattern = null;
				List<IdbCarSkuTO> existingSkus = new ArrayList<IdbCarSkuTO>();
				if (vendorStyle != null) {
					idbCarTO.setVendorStyleObj(vendorStyle);
					idbCarTO.setVendor(vendorStyle.getVendor());
					Car existingCar = carManager.getCarForStyle(vendorStyle.getVendorStyleId());
					if(idbCarTO.getExistingCar() == null){
						idbCarTO.setExistingCar(existingCar);
					}

					//Update the VendorStyle Information 
					if (StringUtils.isEmpty(StringUtils.trimToNull(vendorStyle.getVendorStyleName()))) {
						vendorStyle.setVendorStyleName(idbCarTO.getVendorStyleDescription());
						vendorStyle.setDescr(idbCarTO.getVendorStyleDescription());

						//create or update vendor style
						carManager.createVendorStyle(vendorStyle);
					}

					//Update the Vendor Information 
					Vendor vendor = vendorStyle.getVendor();
					if (!StringUtils.equals(idbCarTO.getVendorName(), vendor.getName())) {
						vendor.setName(idbCarTO.getVendorName());
						vendor.setDescr(idbCarTO.getVendorName());
                                                vendor.setIsDisplayable("Y");
						//create or update vendor
						carManager.createVendor(vendor);
					}
					pattern = vendorStyle.getParentVendorStyle();
					
					List<VendorSku> skusOfStyle = carManager.getSkusForStyle(vendorStyle);
					Map<String, ArrayList<Car>> skumap = new HashMap<String, ArrayList<Car>>();
					java.util.Iterator<VendorSku> it = skusOfStyle.iterator();
					while(it.hasNext()){
						VendorSku vs = it.next();
						ArrayList<Car> carList;
						if((carList=skumap.get(vs.getBelkUpc()))== null)
							 carList = new ArrayList<Car>();
						carList.add(vs.getCar());
						skumap.put(vs.getBelkUpc(), carList);
					}
				
				if(log.isDebugEnabled()){
					log.debug("Sku Map with exiting CARS List:"+skumap);
				}
				
				if(!(skumap == null ||skumap.isEmpty())){				
					for(IdbCarSkuTO sku : idbCarTO.getSkuInfo()){
						List<Car> skuTocarList= null;
						if((skuTocarList=skumap.get(sku.getBelkUPC()))!= null){
							java.util.Iterator<Car> cariteraor = skuTocarList.iterator();
							while(cariteraor.hasNext()){
							Car skuCar = cariteraor.next();	
							if(skuCar.getStatusCd().equals(Status.DELETED)){
								if(log.isDebugEnabled()){
									log.debug("Carid with DELETED Status:"+skuCar.getCarId());
								}
								if(skuCar.getSourceId().equals(idbCarTO.getPoNumber())){
									existingSkus.add(sku);
									if (log.isDebugEnabled()) 
										log.debug("sku "+ sku.getBelkUPC()+ " found in existing deleted car #"+ skuCar.getCarId() +" with same PO number ");
									break;
								}
							}
							else{
								 existingSkus.add(sku);
								 if (log.isDebugEnabled()) 
									log.debug("sku "+ sku.getBelkUPC() +" found in existing Active car #"+ skuCar.getCarId());
								 break;
							 }
						 }
					   }
					}
						//Remove all Existing SKU's
						if (!existingSkus.isEmpty()) {
							numElimSkus = numElimSkus + existingSkus.size();
							idbCarTO.getSkuInfo().removeAll(existingSkus);
						}
						if (idbCarTO.getSkuInfo().isEmpty()) {
							String errorText = "All Skus in found in other ACTIVE/DLETED Cars (Skus):  " + existingSkus;
							errorList.add(errorText);
							if (log.isDebugEnabled())
								log.debug(errorText);
							}
						}
					}

				// Create CAR only if there are no CAR for the and or SKU does not exist in another CAR...
				// VENDOR_STYLE
				if (!errorList.isEmpty()) {
					if (idbCarTO.getManualCar() != null && idbCarTO.getManualCar().getProcessStatus().getStatusCd().equals(ManualCarProcessStatus.EXPORTED)) {
						try {
							idbCarTO.getManualCar().setProcessStatus(completeWithError);
							idbCarTO.getManualCar().setPostProcessInfo(StringUtils.left(errorList.toString(), 300));
							this.setAuditInfo(systemUser, idbCarTO.getManualCar());
							carManager.save(idbCarTO.getManualCar());
						} catch (Exception ex) {
							if (log.isErrorEnabled())
								log.error("Error in processing manual car: " + ex);
						}

					}
				//	
				    if(existingSkus.isEmpty())
					 numElimSkus = numElimSkus + idbCarTO.getSkuInfo().size();
					continue nextCarFromIdb;
				}

				//Go the next Row if there are no Skus that in the data
				if (idbCarTO != null && idbCarTO.getSkuInfo().isEmpty()) {
					if (log.isDebugEnabled())
						log.debug("There is not SKU information available for Product: " + idbCarTO.getVendorStyle());
					continue nextCarFromIdb;
				}

				if (vendorStyle == null) {
					Vendor vendor = carManager.getVendor(idbCarTO.getVendorNumber());
					if (vendor == null) {
						// Create a Vendor
						vendor = new Vendor();
						vendor.setName(idbCarTO.getVendorName());
						vendor.setVendorNumber(idbCarTO.getVendorNumber());
						vendor.setDescr(idbCarTO.getVendorName());
						vendor.setStatusCd(Status.ACTIVE);
						vendor.setContactEmailAddr(vendorContactEmail);
						this.setAuditInfo(systemUser, vendor);
                                                vendor.setIsDisplayable("Y");

						vendor = carManager.createVendor(vendor);

						if (log.isInfoEnabled()){
							log.info("Vendor Created with ID: " + vendor.getVendorId());
						}
					} else if (!StringUtils.equals(idbCarTO.getVendorName(), vendor.getName())) {
						vendor.setName(idbCarTO.getVendorName());
						//vendor.setVendorNumber(idbCarTO.getVendorNumber());
						vendor.setDescr(idbCarTO.getVendorName());
						vendor.setStatusCd(Status.ACTIVE);
						this.setAuditInfo(systemUser, vendor);
                                                vendor.setIsDisplayable("Y");
						vendor = (Vendor) carManager.createVendor(vendor);

					}

					//Removed the code which creates pattern based on pattern processing rule of classifications 

					vendorStyle = new VendorStyle();
					vendorStyle.setVendorNumber(idbCarTO.getVendorNumber());
					vendorStyle.setVendorStyleName(idbCarTO.getVendorStyleDescription());
					vendorStyle.setVendorStyleNumber(idbCarTO.getVendorStyle());
					vendorStyle.setDescr(idbCarTO.getVendorStyleDescription()); // change to <blank>
					// based on the bug
					// idbCarTO.getVendorStyleDescription()
					vendorStyle.setStatusCd(Status.ACTIVE);
					this.setAuditInfo(systemUser, vendorStyle);
					vendorStyle.setVendorStyleType(vendorStyleTypeProduct);
					vendorStyle.setVendor(vendor);
					vendorStyle.setClassification(classification);
					vendorStyle.setParentVendorStyle(pattern);

					vendorStyle = carManager.createVendorStyle(vendorStyle);

					idbCarTO.setVendor(vendor);
					idbCarTO.setVendorStyleObj(vendorStyle);

					if (log.isInfoEnabled())
						log.info("Vendor Style Created with ID: " + vendorStyle.getVendorStyleId());
				}

				List<IdbCarDataTO> vendorStyleList = null;
				if (pattern != null) {
					if (productMap.containsKey(pattern)) {
						vendorStyleList = productMap.get(pattern);
					} else {
						vendorStyleList = new ArrayList<IdbCarDataTO>();
						productMap.put(pattern, vendorStyleList);
					}
					vendorStyleList.add(idbCarTO);
				} else {
					vendorStyleList = new ArrayList<IdbCarDataTO>();
					vendorStyleList.add(idbCarTO);
					productMap.put(vendorStyle, vendorStyleList);
				}
			} catch (Exception ex) {
				if (log.isErrorEnabled()) {
					ex.printStackTrace();
					log.error("Transaction Exception. Cause= " + ex.getMessage());
				}
			}
		}

		if (log.isInfoEnabled()) {
			log.info("Going to Process Cars: # of Vendor Styles to process - " + productMap.size());
			log.info("<!------ # SKUs - Num Of Sku In File: " + numSkusInFile + "---------->");
			log.info("<!------ # SKUs - Num Of Skuz Eliminated: " + numElimSkus + "---------->");
		}

		for (VendorStyle vs : productMap.keySet()) {
			if (log.isDebugEnabled())
				log.debug("Process Cars for Vendor Style: " + vs.getVendorStyleNumber());

			long startime = System.currentTimeMillis();
			List<IdbCarDataTO> lCarData = productMap.get(vs);
			Car car = null;
			CarNote note = null;
			for (IdbCarDataTO idbCarTO : lCarData) {
				if (log.isDebugEnabled()){
					log.debug("Processing Record: " + (++rec) + " of " + totalRecordSize + " Vendor: " + idbCarTO.getPoNumber() + " Manual Car Id: "
						+ idbCarTO.getManualCarId());
				}
				if (car == null) {
					car = new Car();
					car.setDepartment(idbCarTO.getDepartment());

					if (idbCarTO.getManualCar() != null) {
						car.setSourceType(sourceManualCar);
						car.setSourceId(idbCarTO.getManualCar().getCreatedBy());
					} else if (StringUtils.isNotBlank(idbCarTO.getManualCarId()) && idbCarTO.getManualCar() == null) {
						car.setSourceType(sourceManualCar);
						car.setSourceId("Manual Car: " + idbCarTO.getManualCarId());
					} else if (StringUtils.isNotBlank(idbCarTO.getPoNumber())) {
						car.setSourceType(sourcePOCar);
						car.setSourceId(idbCarTO.getPoNumber());
					} else {
						car.setSourceType(sourceFJCar);
						// //Currently PO Number is Blank for Fine
						// Jewelry
						car.setSourceId(idbCarTO.getVendorNumber() + "-" + idbCarTO.getVendorStyle());
					}
					car.setVendorStyle(vs);
					car.setWorkFlow(defaultWorkflow);
					
					/* CR assign to vendor - REMOVING FOR PI-103 */										
//					isAssignedToVendor = sendDirectToVendor(idbCarTO.getDepartment().getDeptId(),idbCarTO.getVendor().getVendorId());
//										
//					if (isAssignedToVendor){
//						log.info("send car directly to vendor "+ idbCarTO.getVendor().getVendorId());
//						car.setCarUserByLoggedByUserId(systemUser);
//						car.setAssignedToUserType(vendorType);
//						car.setCurrentWorkFlowStatus(withVendor);
//						car.setRejectionCount(0);
//					}					
//					else {
						car.setCarUserByLoggedByUserId(systemUser);
						car.setAssignedToUserType(buyer);
						car.setCurrentWorkFlowStatus(initiated);
//					}
					//TODO remove escalation date , add due date . 

					// Set the Initial Escalation Date to 7 Days...
					//  Need to figure out where we should
					// default the 7 Days from i.e Transition OR
					// Config
					car.setEscalationDate(escalationDate);

					car.setIsUrgent(Constants.FLAG_NO);

					if (StringUtils.isNotBlank(idbCarTO.getExpectedShipDate())) {
						car.setExpectedShipDate(DateUtils.parseDate(idbCarTO.getExpectedShipDate(), "yyyy-MM-dd"));
						car.setDueDate(this.getDueDate());
					} else {
						car.setExpectedShipDate(this.getCompletionDate());
						car.setDueDate(this.getDueDate());
					}

					String isProductTypeReq = Constants.FLAG_NO;
					List<ProductType> l = productManager.getProductTypes(vs.getClassification().getClassId());
					ProductType productType = null;
					if (l != null && !l.isEmpty()) {
						if (l.size() > 1) {
							isProductTypeReq = Constants.FLAG_YES;
						} else {
							productType = l.get(0);
						}
					}
					car.setIsProductTypeRequired(isProductTypeReq);
					car.setStatusCd(Status.ACTIVE);

					car.setContentStatus(contentInProgress);
					car.setLastWorkflowStatusUpdateDate(Calendar.getInstance().getTime());

					this.setAuditInfo(systemUser, car);

					if (productType != null) {
						List<DepartmentAttribute> departmentAttributes = carManager.getAllDepartmentAttributes(car.getDepartment().getDeptId());
						List<ClassAttribute> classificationAttributes = carManager.getAllClassificationAttributes(car.getVendorStyle().getClassification()
								.getClassId());

						Map<String, Attribute> attributeMap = new HashMap<String, Attribute>();
						Map<String, String> attributeDefaultValueMap = new HashMap<String, String>();

						for (DepartmentAttribute deptAttr : departmentAttributes) {
							if (deptAttr.getAttribute().isActive()) {
								attributeMap.put(deptAttr.getAttribute().getBlueMartiniAttribute(), deptAttr.getAttribute());
								attributeDefaultValueMap.put(deptAttr.getAttribute().getBlueMartiniAttribute(), StringUtils.defaultString(deptAttr
										.getDefaultAttrValue()));
							}
						}

						//Setup Product Type Attributes
						if (productType != null) {
							//Set the VendorStyle Product
							vs.setProductType(productType);
							for (ProductTypeAttribute ptAttr : productType.getProductTypeAttributes()) {
								if (ptAttr.getAttribute().isActive()) {
									if (attributeMap.containsKey(ptAttr.getAttribute().getBlueMartiniAttribute())) {
										attributeMap.remove(ptAttr.getAttribute().getBlueMartiniAttribute());
										attributeDefaultValueMap.remove(ptAttr.getAttribute().getBlueMartiniAttribute());
									}
									attributeMap.put(ptAttr.getAttribute().getBlueMartiniAttribute(), ptAttr.getAttribute());
									attributeDefaultValueMap.put(ptAttr.getAttribute().getBlueMartiniAttribute(), StringUtils.defaultString(ptAttr
											.getDefaultAttrValue()));
								}
							}
						}

						for (ClassAttribute classAttr : classificationAttributes) {
							if (classAttr.getAttribute().isActive()) {
								if (attributeMap.containsKey(classAttr.getAttribute().getBlueMartiniAttribute())) {
									attributeMap.remove(classAttr.getAttribute().getBlueMartiniAttribute());
									attributeDefaultValueMap.remove(classAttr.getAttribute().getBlueMartiniAttribute());
								}
								attributeMap.put(classAttr.getAttribute().getBlueMartiniAttribute(), classAttr.getAttribute());
								attributeDefaultValueMap.put(classAttr.getAttribute().getBlueMartiniAttribute(), StringUtils.defaultString(classAttr
										.getDefaultAttrValue()));
							}
						}

						for (Attribute attribute : attributeMap.values()) {
							CarAttribute carAttribute = new CarAttribute();
							carAttribute.setAttribute(attribute);
							carAttribute.setCar(car);

							if (attribute.getAttributeConfig().getHtmlDisplayType().isAutocomplete()) {
								carAttribute.setAttributeValueProcessStatus(checkRequired);
							} else {
								carAttribute.setAttributeValueProcessStatus(noCheckRequired);
							}
							// Setting to blank for now. Need to get it from the association
							carAttribute.setAttrValue(attributeDefaultValueMap.get(attribute.getBlueMartiniAttribute()));
							carAttribute.setDisplaySeq((short) 0);
							carAttribute.setHasChanged(Constants.FLAG_NO);
							carAttribute.setIsChangeRequired(Constants.FLAG_YES);
							carAttribute.setStatusCd(Status.ACTIVE);
							this.setAuditInfo(systemUser, carAttribute);
							car.getCarAttributes().add(carAttribute);
						}
					}

				}

				if (StringUtils.isNotBlank(idbCarTO.getExpectedShipDate()) && StringUtils.isNotBlank(idbCarTO.getPoNumber())) {
					Date expectedShipDate = DateUtils.parseDate(idbCarTO.getExpectedShipDate(), "yyyy-MM-dd");
					if (expectedShipDate.before(car.getExpectedShipDate())) {
						car.setExpectedShipDate(expectedShipDate);
						car.setSourceId(idbCarTO.getPoNumber());
					}
				}

				//Add Note to Existing Car...
				if (idbCarTO.getExistingCar() != null && note == null) {
					note = new CarNote();
					note.setNoteType(carNoteType);
					note.setIsExternallyDisplayable(CarNote.FLAG_NO);
					note.setStatusCd(Status.ACTIVE);
					note.setCar(car);
					note.setNoteText("Car ID: " + idbCarTO.getExistingCar().getCarId()
							+ " exists for the same Vendor Style.  You can use the copy functionality to update the attributes from this Car.");
					this.setAuditInfo(systemUser, note);
					car.getCarNotes().add(note);
				}

				// Create CAR/CAR SKU Attributes for non dropship CAR
				// this attribute is flase
				Attribute dropShipAtr=carManager.getAttributeByName("IS_DROPSHIP");
				
				
				for (IdbCarSkuTO idbSku : idbCarTO.getSkuInfo()) {
					if (log.isInfoEnabled()) {
						log.info("Creating Vendor Skus for Car: Belk Upc:"+idbSku.getBelkUPC());
					}
					/*
					//Don't need to do the check for existing sku, becuase we are already doing that in the check above SKU's exisitance
					//in other active cars above
					//if we have reached this point it means that we need to create the Vendor SKU
					//VendorSku sku = carManager.getSku(idbSku.getBelkUPC());
					//if (sku == null) {
					 * 
					 */
					VendorSku sku = new VendorSku();
					sku.setCar(car);
					sku.setBelkSku(idbSku.getBelkUPC());
					sku.setLongSku(idbSku.getLongSku());
					sku.setBelkUpc(idbSku.getBelkUPC());
					sku.setVendorUpc(idbSku.getLongSku());
					sku.setStatusCd(Status.ACTIVE);
					sku.setColorCode(idbSku.getVendorColor());
					sku.setColorName(idbSku.getVendorColorName());
					sku.setSizeCode(idbSku.getVendorSizeCode());
					sku.setSizeName(idbSku.getVendorSizeDesc());
					//CARS Size Conversion Issue - Size name overwritten by resync size job
					sku.setIdbSizeName(idbSku.getVendorSizeDesc()); //Adding size name of the SKU in conversion_name column to show the incoming size name in cars edit page
					sku.setParentUpc(idbSku.getParentUPC());
					sku.setSetFlag(idbSku.getSetFlag());
					//Added the retail price to sku
					sku.setRetailPrice(idbSku.getRetailPrice());
					sku.setVendorStyle(idbCarTO.getVendorStyleObj());
					this.setAuditInfo(systemUser, sku);
					
					// Setting is_dropship attribute as N for all the PO skus
					if(log.isDebugEnabled()){
						log.debug("Setting IS_DROPSHIP flag No at sku level");
					}
					CarSkuAttribute dropshipSkuAttr=new CarSkuAttribute();
					dropshipSkuAttr.setAttribute(dropShipAtr);
					dropshipSkuAttr.setAttrValue("No");
					this.setAuditInfo(systemUser, dropshipSkuAttr);
					dropshipSkuAttr.setVendorSku(sku);
					sku.getCarSkuAttributes().add(dropshipSkuAttr);

					car.getVendorSkus().add(sku);
				}
			}

			CatalogProduct product = null;
			//Checking for product information in Catalog
			if (car.getVendorStyle().isPattern()) {
				//Find choice based on the first Product with Content within a Pattern
				VendorStyleSearchCriteria criteria = new VendorStyleSearchCriteria();
				criteria.setVendorStyleId(car.getVendorStyle().getVendorStyleId());
				criteria.setChildrenOnly(true);
				if (log.isDebugEnabled())
					log.debug("Pattern - Search for child product information ");
				List<VendorStyle> childProducts = this.carManager.searchVendorStyle(criteria);
				if (childProducts != null) {
					for (VendorStyle childProduct : childProducts) {
						if (log.isDebugEnabled())
							log.debug("Pattern - Searching for product information in catalog " + childProduct.getVendorStyleNumber());
						product = catalogImportManager.getProduct(childProduct.getVendorNumber(), childProduct.getVendorStyleNumber());
						if (product != null) {
							break;
						}
					}
				}

			} else {
				if (log.isDebugEnabled())
					log.debug("Product - Searching for product information in catalog " + car.getVendorStyle().getVendorStyleNumber());
				product = catalogImportManager.getProduct(car.getVendorStyle().getVendorNumber(), car.getVendorStyle().getVendorStyleNumber());

			}

			if (product == null) {
				//check based on sku
				CatalogSku sku = null;
				if (car != null && car.getVendorSkus() != null && !car.getVendorSkus().isEmpty()) {
					catalogImportManager.getSku(car.getVendorSkus().iterator().next().getVendorUpc());
				}

				if (sku != null) {
					product = sku.getCatalogProduct();
				}
			}

			if (product != null && product.getCatalogProductId() != 0 && car.getVendorStyle().getProductType() != null) {
				catalogImportManager.copyToCar(product, car);

				//Item found in catalog.. then assing the CAR to Buyer
				if (car.getCarId() == 0) {
					car.setAssignedToUserType(buyer);
				}
			}

			try {
				if (log.isInfoEnabled())
					log.info("Creating Car for Vendor Style with ID: " + car.getVendorStyle().getVendorStyleId());
				car = carManager.createCar(car);
				if (log.isInfoEnabled())
					log.info("new CAR created with with CarID: " + car.getCarId());
			} catch (Exception ex) {
				if (log.isErrorEnabled())
					log.error("Save Exception. Cause: " + ex.getMessage());
			}

			// Update the Manual CAR
			if (lCarData != null) {
				for (IdbCarDataTO idbCarTO : lCarData) {
					if (idbCarTO.getManualCar() != null && car != null) {
						try {
							idbCarTO.getManualCar().setProcessStatus(completed);
							idbCarTO.getManualCar().setPostProcessInfo("Car Created With ID: " + car.getCarId());
							this.setAuditInfo(systemUser, idbCarTO.getManualCar());
							ManualCar mCarTemp=idbCarTO.getManualCar();
							carManager.saveOrUpdate(mCarTemp);
						} catch (Exception ex) {
							if (log.isErrorEnabled())
								log.error("Error in processing manual car: " + ex);
						}
					}
				}
			}// End of My Car Update

			if (log.isInfoEnabled())
				log.info("End time for current Car = " + (System.currentTimeMillis() - startime));
		}

		if (log.isInfoEnabled()) {
			log.info("----------------> Overall execution time in minutes  = " + (System.currentTimeMillis() - executionTime) / 60000);
			log.info("---------------->  End Importing Car Data   <-----------------");
		}
	
	}

	
	
	private boolean sendDirectToVendor(Long deptId,Long vendorId) {
		List <CarUserVendorDepartment> vendors = carManager.getVendorsByDeptId(deptId);
		
		for (CarUserVendorDepartment carUserVendorDepartment : vendors) {
			if(carUserVendorDepartment.getVendor().getVendorId() == (vendorId)){
				log.info("returning vendor user");	
				return true;
					
			}
				
		}
		return false;
	}

	public void sendUserCarNotification() throws SendEmailException, CarJobDetailException {
		if (log.isInfoEnabled())
			log.info("---------------->  Begin sendUserCarNotification <-----------------");
			
		this.sendCarAssignmentNotification(false);
		this.sendCarGenerationNotification(false);
		log.info("---------------->  End sendUserCarNotification <-----------------");
	}

	public void sendVendorCarNotification() throws SendEmailException, CarJobDetailException {
		if (log.isInfoEnabled())
			log.info("---------------->  Begin send Vendor Car Notification <-----------------");
		
		this.sendCarAssignmentNotification(true);
		this.sendCarGenerationNotification(true);
		
		if (log.isInfoEnabled())
			log.info("---------------->  End send Vendor Car Notification <-----------------");
	}
	
	/*
	 * sendCarAssignmentNotification()
	 * @param isVendor 
	 * This method sends email on car workflow status change
	 */
	private void sendCarAssignmentNotification(boolean isVendor)throws SendEmailException, CarJobDetailException {
		this.sendCarNotification(isVendor);
	}
	
	/*
	 * sendCarGenerationNotification()
	 * @param isVendor
	 * This method sends email on car generation
	 */
	private void  sendCarGenerationNotification(boolean isVendor)  throws SendEmailException, CarJobDetailException{
		
		if (log.isInfoEnabled())
			log.info("---------------->  Begin send CarGeneration Notification <-----------------");
		
		//logic to send car generation email to buyer and vendor
		Config userName = (Config) lookupManager.getById(Config.class, Config.SYSTEM_USER);
		Config sendNotifications = (Config) lookupManager.getById(Config.class, Config.SEND_EMAIL_NOTIFICATIONS);
		User systemUser = this.userManager.getUserByUsername(userName.getValue());
		NotificationType type = lookupManager.getNotificationType(NotificationType.CAR_GENERATION);
		List<DetailNotificationUserDTO> userList = carManager.getGeneratedCarList(isVendor);
		if (log.isInfoEnabled())
			log.info("found " + userList.size() + " users to send email.");
		
		Map<String,Object> model  = new HashMap<String,Object>();
		
		for(DetailNotificationUserDTO userDTO : userList){
		    if(userDTO != null && userDTO.getUserId() != null && userDTO.getCars() != null && userDTO.getCars().size() > 0){
		    	List<Object> skuDetailList = new ArrayList<Object>();
				List<Car> carList = userDTO.getCars();
				model.put("userEmail", userDTO.getEmailAddress());
				model.put("firstName", userDTO.getFirstName());
				model.put("lastName", userDTO.getLastName());
				model.put("userType", userDTO.getUserType());
				model.put("userNumber", userDTO.getUserId());
				model.put("date",new Date());
				for(Car car: carList){
					Set<VendorSku> skus = car.getVendorSkus();
					if(skus == null){
						skus = new HashSet<VendorSku>();
					}
					Long carid = car.getCarId();
					String deptNumber = car.getDepartment().getDeptCd();
					String vendorNumber = car.getVendorStyle().getVendorNumber();
					String vendorName = car.getVendorStyle().getVendor().getName();
					String styleName = car.getVendorStyle().getVendorStyleName();
					String styleNumber = car.getVendorStyle().getVendorStyleNumber();
					String assignedTo = car.getAssignedToUserType().getUserTypeCd();
					for(VendorSku sku: skus){
						Map<String,Object> skuDetail = new HashMap<String,Object>();
						skuDetail.put("carid",carid);
						skuDetail.put("deptNumber",deptNumber);
						skuDetail.put("vendorName",vendorName);
						skuDetail.put("vendorNumber",vendorNumber);
						skuDetail.put("styleNumber",styleNumber);
						skuDetail.put("styleName",styleName);
						skuDetail.put("sku",sku.getBelkSku());
						skuDetail.put("assignedTo",assignedTo);
						skuDetailList.add(skuDetail);
					}
				}
				model.put("skuList", skuDetailList);
				try {
					if (log.isDebugEnabled())
						log.debug("Sending email to  -  " + userDTO.getEmailAddress() );
					
					if ("true".equalsIgnoreCase(sendNotifications.getValue())) {
						sendEmailManager.sendNotificationEmailReport(type, systemUser, model, false);
					}
				} catch (SendEmailException se) {
					log.error("Error when sending emails");
					processException("sendCarGenerationNotification", se);
				} catch (Exception ex) {
					log.error("General Exception occured. Cause: " + ex.getMessage());
					processException("sendCarNotification", ex);
				}
			}
		}
		if (log.isInfoEnabled())
			log.info("---------------->  End send CarGeneration Notification <-----------------");
	}

	private void sendCarNotification(boolean isVendor) throws SendEmailException, CarJobDetailException {

		if (log.isInfoEnabled())
			log.info("---------------->  Begin sendCarNotification <-----------------");

		Map<String, String> model = new HashMap<String, String>();
		NotificationType type = null;
		List<NotificationUserDTO> notifyUserList = null;

		if (isVendor) {
			type = lookupManager.getNotificationType(NotificationType.VENDOR_CAR_SUMMARY);
			notifyUserList = carManager.getCarNotificationList(true); //Get vendors list
		} else {
			type = lookupManager.getNotificationType(NotificationType.CAR_SUMMARY);
			notifyUserList = carManager.getCarNotificationList(false); //Get list
		}

		Config userName = (Config) lookupManager.getById(Config.class, Config.SYSTEM_USER);
		Config sendNotifications = (Config) lookupManager.getById(Config.class, Config.SEND_EMAIL_NOTIFICATIONS);
		User systemUser = this.userManager.getUserByUsername(userName.getValue());

		if (systemUser == null || type == null || sendNotifications == null) {
			throw new CarJobDetailException("Cannot process sendCarNotification job. A configuration value is missing");
		}

		if (!notifyUserList.isEmpty()) {
			for (NotificationUserDTO user : notifyUserList) {
				model.put("userEmail", user.getEmailAddress());
				model.put("userFirstName", user.getFirstName());
				model.put("userLastName", user.getLastName());
				model.put("carCount", user.getLastName());

				try {
					if ("true".equalsIgnoreCase(sendNotifications.getValue())) {
						sendEmailManager.sendNotificationEmail(type, systemUser, model);
					}
				} catch (SendEmailException se) {
					log.error("Error when sending email to: " + user.getEmailAddress());
					processException("sendCarNotification", se);
				} catch (Exception ex) {
					log.error("General Exception occured. Cause: " + ex.getMessage());
					processException("sendCarNotification", ex);
				}
			}
		}

		if (log.isInfoEnabled())
			log.info("---------------->  End sendCarNotification <-----------------");
	}

	public void sendVendorCarEscalationList() throws SendEmailException, CarJobDetailException {
		if (log.isInfoEnabled())
			log.info("---------------->  Begin sendVendorCarEscalationList <-----------------");

		Map<String, String> model = new HashMap<String, String>();
		List<NotificationUserDTO> notifyUserList = null;
		NotificationType type = lookupManager.getNotificationType(NotificationType.VENDOR_CAR_ESCALATION);

		Config userName = (Config) lookupManager.getById(Config.class, Config.SYSTEM_USER);
		Config sendNotifications = (Config) lookupManager.getById(Config.class, Config.SEND_EMAIL_NOTIFICATIONS);
		User systemUser = this.userManager.getUserByUsername(userName.getValue());

		if (systemUser == null || type == null) {
			throw new CarJobDetailException("Cannot process sendVendorOpenCarNotification job. A configuration value is missing");
		}

		notifyUserList = carManager.getVendorCarEscalationList(); //Get list

		if (!notifyUserList.isEmpty()) {
			for (NotificationUserDTO user : notifyUserList) {
				model.put("userEmail", user.getEmailAddress());
				model.put("userFirstName", user.getFirstName());
				model.put("userLastName", user.getLastName());
				model.put("carCount", String.valueOf(user.getCarCount()));

				try {
					if ("true".equalsIgnoreCase(sendNotifications.getValue())) {
						sendEmailManager.sendNotificationEmail(type, systemUser, model);
					}
				} catch (SendEmailException se) {
					processException("sendVendorNotification", se);
					log.error("Error when sending email to: " + user.getEmailAddress());
				} catch (Exception ex) {
					processException("sendVendorNotification", ex);
					log.error("General Exception occured. Cause: " + ex.getMessage());
				}
			}
		}

		if (log.isInfoEnabled())
			log.info("---------------->  End sendVendorCarEscalationList <-----------------");
	}

	public void sendVendorSampleEscalationList() throws SendEmailException, CarJobDetailException {

		if (log.isInfoEnabled())
			log.info("---------------->  Begin sendVendorSampleEscalationList <-----------------");

		Map<String, String> model = new HashMap<String, String>();
		List<NotificationUserDTO> notifyUserList = null;
		NotificationType type = lookupManager.getNotificationType(NotificationType.VENDOR_SAMPLE_ESCALATION);

		Config userName = (Config) lookupManager.getById(Config.class, Config.SYSTEM_USER);
		Config sendNotifications = (Config) lookupManager.getById(Config.class, Config.SEND_EMAIL_NOTIFICATIONS);
		User systemUser = this.userManager.getUserByUsername(userName.getValue());

		if (systemUser == null || type == null) {
			throw new CarJobDetailException("Cannot process sendVendorSampleEscalationList job. A configuration value is missing");
		}

		notifyUserList = carManager.getVendorSampleEscalationList(); //Get list

		if (!notifyUserList.isEmpty()) {
			for (NotificationUserDTO user : notifyUserList) {
				model.put("userEmail", user.getEmailAddress());
				model.put("userFirstName", user.getFirstName());
				model.put("userLastName", user.getLastName());
				model.put("carNumber", String.valueOf(user.getCarNumber()));
				model.put("dueDate", DateUtils.formatDate(user.getDueDate()));
				try {
					if ("true".equalsIgnoreCase(sendNotifications.getValue())) {
						sendEmailManager.sendNotificationEmail(type, systemUser, model);
					}
				} catch (SendEmailException se) {
					processException("sendVendorSampleEscalation", se);
					log.error("Error when sending email to: " + user.getEmailAddress());
				} catch (Exception ex) {
					processException("sendVendorSampleEscalation", ex);
					log.error("General Exception occured. Cause: " + ex.getMessage());
				}
			}
		}

		if (log.isInfoEnabled())
			log.info("---------------->  End sendVendorSampleEscalationList <-----------------");
	}

	private boolean processCompletedFile(String filePath, String fileName, String destinationDir) {
		return processCompletedFile(filePath, fileName, destinationDir, false);
	}

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

	private Date getEscalationDate() {
		Config numberOfEscalationDays = (Config) lookupManager.getById(Config.class, Config.INIT_NUMBER_OF_ESCALATION_DAYS);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, Integer.parseInt(numberOfEscalationDays.getValue()));
		return cal.getTime();
	}

	private Date getDueDate() {
		Config numberOfDueDays = (Config) lookupManager.getById(Config.class, Config.INIT_NUMBER_OF_DUE_DAYS);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, Integer.parseInt(numberOfDueDays.getValue()));
		return cal.getTime();
	}

	private Date getCompletionDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, Car.NUM_DAYS_TO_COMPLETION);
		return cal.getTime();
	}

	public void setAuditInfo(User user, BaseAuditableModel model) {

		if (StringUtils.isBlank(model.getCreatedBy())) {
			model.setCreatedBy(user.getUsername());
			model.setCreatedDate(new Date());
		}
		model.setUpdatedBy(user.getUsername());
		model.setUpdatedDate(new Date());
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

	public void importRRDFeeds() throws CarJobDetailException {

		if (log.isInfoEnabled())
			log.info("---------------->  Begin importing RRD Feeds <-----------------");

		final String host = ((Config) lookupManager.getById(Config.class, Config.RRD_FTP_HOST)).getValue();
		final String userName = ((Config) lookupManager.getById(Config.class, Config.RRD_FTP_USERNAME)).getValue();
		final String password = ((Config) lookupManager.getById(Config.class, Config.RRD_FTP_PASSWORD)).getValue();
		final String remoteDirName = ((Config) lookupManager.getById(Config.class, Config.RRD_IMPORT_DIRECTORY)).getValue();
		final String localDirName = ((Config) lookupManager.getById(Config.class, Config.RRD_LOCAL_IMPORT_DIRECTORY)).getValue();
		final boolean deleteAfterGet = true;

		try {
			FTPClient ftp = FtpUtil.openConnection(host, userName, password);
			FtpUtil.setTransferModeToAscii(ftp);
			FtpUtil.getDataFiles(ftp, remoteDirName, localDirName, deleteAfterGet);
			FtpUtil.closeConnection(ftp);
		} catch (IOException ioex) {
			log.error("Could not read files from the RRD FTP", ioex);
		}

		FileProcessor readRRDFileToDB = new FileProcessor() {
			public void process(File f) throws Exception {
				if (!f.exists() || f.isDirectory() || (f.length() == 0))
					return;
				String xml = FileUtils.readFileToString(f, "UTF-8");
				RRDUpdateMessageReader reader = new RRDUpdateMessageReader();
				reader.setStandalone(false);
				reader.readAndProcessXML(xml);
			}

			public String getProcessName() {
				return "reading XML from RRD";
			}
		};

		String importFileName = ((Config) lookupManager.getById(Config.class, Config.RRD_IMPORT_FILENAME)).getValue();
		String completedDirName = ((Config) lookupManager.getById(Config.class, Config.RRD_FILE_COMPLETED_DIRECTORY_NAME)).getValue();

		FileProcessorChain fileProcessorChain = new FileProcessorChain("importing RRD feeds");
		fileProcessorChain.setExceptionProcessor(this) ;

		fileProcessorChain.setFiles(findFiles(localDirName, importFileName));
		fileProcessorChain.addFileProcessor(readRRDFileToDB, FileProcessorChain.ActionOnException.ContinueWithNextFile);
		fileProcessorChain.addFileProcessor(new FileArchiver(completedDirName), FileProcessorChain.ActionOnException.ContinueChain);
		fileProcessorChain.processFiles();

		if (log.isInfoEnabled())
			log.info("---------------->  End importing RRD Feeds <-----------------");

	}
	
	public void exportRRDFeeds() throws CarJobDetailException {
		if (log.isInfoEnabled())
			log.info("---------------->  Begin exporting RRD Feeds <-----------------");

		final String host = ((Config) lookupManager.getById(Config.class, Config.RRD_FTP_HOST)).getValue();
		final String userName = ((Config) lookupManager.getById(Config.class, Config.RRD_FTP_USERNAME)).getValue();
		final String password = ((Config) lookupManager.getById(Config.class, Config.RRD_FTP_PASSWORD)).getValue();
		final String remoteDirName = ((Config) lookupManager.getById(Config.class, Config.RRD_EXPORT_DIRECTORY)).getValue();
		final String localExportDirName = ((Config) lookupManager.getById(Config.class, Config.RRD_LOCAL_EXPORT_DIRECTORY)).getValue();
		final String archiveDirName = ((Config) lookupManager.getById(Config.class, Config.RRD_FILE_COMPLETED_DIRECTORY_NAME)).getValue();

		final String fileName = "photoRequests_" + DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss") + ".xml";

		File f = new File(localExportDirName, fileName);
		FileProcessor rrdFTPPutter = new FileProcessor() {
			public void process(File f) throws Exception {
				if (!f.exists() || f.isDirectory())
					return;
				FTPClient ftp = FtpUtil.openConnection(host, userName, password);
				FtpUtil.setTransferModeToAscii(ftp);
				FtpUtil.sendDataFiles(ftp, Arrays.asList(f), remoteDirName);
				FtpUtil.closeConnection(ftp);
			}

			public String getProcessName() {
				return "FTPing to RRD";
			}
		};

		FileProcessor rrdPhotoRequestMessageWriter = new FileProcessor() {
			public void process(File f) throws Exception {
				OutputStream out = null;
				try {
					if (!f.exists())
						f.createNewFile();
					out = new BufferedOutputStream(new FileOutputStream(f));
					//new ProductPhotoRequestMessageWriter().write(out);
					/** START  CARS-193/SUP-776**/	
					ProductPhotoRequestMessageWriter productPhotoWriter=new ProductPhotoRequestMessageWriter();
					productPhotoWriter.setLookupManager(lookupManager);
					productPhotoWriter.setSendEmailManager(sendEmailManager);
					productPhotoWriter.setUserManager(userManager);
					productPhotoWriter.write(out);
					/** END CARS-193/SUP-776**/
				} catch (Exception ex) {
					throw ex;
				} finally {
					try {
						if (out != null)
							out.close();
					} catch (IOException ioex) {
					}
				}
			}

			public String getProcessName() {
				return "wrting XML to RRD";
			}
		};

		FileProcessorChain fileProcessorChain = new FileProcessorChain("writing RRD feeds");
		fileProcessorChain.setExceptionProcessor(this) ;

		fileProcessorChain.setFiles(Arrays.asList(f));
		fileProcessorChain.addFileProcessor(rrdPhotoRequestMessageWriter, ActionOnException.DoNotContinue);
		fileProcessorChain.processFiles();
		fileProcessorChain = new FileProcessorChain("exporting RRD feeds");
		fileProcessorChain.setFiles(findFiles(localExportDirName, null));
		fileProcessorChain.addFileProcessor(rrdFTPPutter, ActionOnException.DoNotContinue);
		fileProcessorChain.addFileProcessor(new FileArchiver(archiveDirName), ActionOnException.ContinueWithNextFile);
		fileProcessorChain.processFiles();

		updateLastTransferTime();

		if (log.isInfoEnabled())
			log.info("---------------->  End exporting RRD Feeds <-----------------");

	}

	private static final Format lastTransferTimeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	private void updateLastTransferTime() {
		Config rrdLastTransferTimeConfig = (Config) lookupManager.getById(Config.class, Config.RRD_LAST_TRANSFER_TIME);
		String dateStr = lastTransferTimeFormat.format(new Date());
		rrdLastTransferTimeConfig.setValue(dateStr);
		carManager.save(rrdLastTransferTimeConfig);
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

	private void processFiles(String dirName, String fileName, FileProcessor... fileProcessors) {
		if (fileProcessors == null)
			return;
		File[] files = findFiles(dirName, fileName);

		// Directory doesn't exist or there aren't any files to process
		if (files == null) {
			log.info("File " + dirName + fileName + " not found!!! Nothing to process. Exiting job...");
		} else if (files.length < 1) {
			log.info("There are no files to process in directory: " + dirName + " Exiting job...");
		} else {
			processFiles(Arrays.asList(files), fileProcessors);
		}

	}

	private void processFiles(Collection<File> files, FileProcessor... fileProcessors) {
		if (files == null || fileProcessors == null)
			return;
		for (File file : files) {
			for (FileProcessor fileProcessor : fileProcessors) {
				try {
					fileProcessor.process(file);
				} catch (Exception ex) {
					if (log.isErrorEnabled())
						log.error("Error running process (" + fileProcessor.getProcessName() + ") on file " + file.getName());
				}
			}
		}
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
			Log log = LogFactory.getLog(QuartzJobManagerImpl.class);

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
				if (log.isInfoEnabled())
					log.info(f.getName() + " file was archived successfully.");
			} else {
				if (log.isErrorEnabled())
					log.error("Error moving file " + f.getName() + " to directory: " + destDir);
			}
		}

		public String getProcessName() {
			return "archiving";
		}
	}

	/**
	 * unlockAllCars
	 * Method to unlock all the locked cars
	 */
	public void unlockAllCars()  throws Exception{
		if(log.isInfoEnabled()){
			log.info("-------------- Begin unlcoking the todays locked cars -----------");
			try{
				this.carManager.unlockAllCars();
				}catch(Exception e){
					log.error("Exception while updating the ASSIGNED_TO_USER falg to null : ", e);
				}
			
			log.info("-------------- End unlcoking the todays locked cars -----------");	
		}		
	}
	
	public static void main(String[] args) {
		try {
			new QuartzJobManagerImpl().exportRRDFeeds();
		} catch (CarJobDetailException cjdex) {
		}
	}
	
	/*
	 * Method to copy latest data of bel-inv avail table to Tmp table in CARS Schema.
	 */
	public void updateTmpWithBel_Inv_Avail() throws IOException, CarJobDetailException {
		log.info("-------------->Start Dumping Data From BEL_INV_AVAIL to TEMP Table <-----------");
		try{
		reportManager.deleteTmpSKUAvailTable();
		}catch(DataAccessException dae){
			log.error("Exception while dropping tmp table"+dae);
		}
		String avail_query = "CREATE TABLE tmp_Sku_Avail AS (SELECT SKU,ON_HAND_QTY FROM BM_EXTERNAL.bel_inv_avail@BM_EXTERNAL)";
		try{
		reportManager.createTmpSkuAvail(avail_query);
		}catch(Exception ex){
			log.error("Exception while creating tmp table"+ex);
		}
	}
	
	/**
	 * Method for adding new department to Art directors and Sample Coordinators
	 * @param dept
	 * @return
	 */
	private void assignNewDepartment(Department dept) {
		//Get the list of Art director and Sample Coordinator car_userid's from CAR_USER table 
		List<User> users=this.carManager.getAllArtAndSampleUsers();
			for(User user:users ){
				user.addDepartment(dept);
				this.carManager.save(user);
			}
	}

	/**
	 * AutomaticallyApproveImage
	 * 
	 * This method runs only during weekdays for approving the 
	 * images pending for buyer approval
	 */
	public void updateAutomaticallyApproveImage() throws Exception {
		if (log.isInfoEnabled()) {
			log.info("---------------->  Begin Automatically Approve Vendor Images  <-----------------");
		}
		long vendorImageId;
		Date mcUploadedDate;
		long carID;
		boolean updateFlag = false;
		// Get the current user object
		Config userName = (Config) lookupManager.getById(Config.class, Config.SYSTEM_USER);	
		// Getting All Cars from CAR Table pending for buyer approval
		List<Car> allCars = this.carManager.getBuyerApprovalPendingCars();
		if (!allCars.isEmpty()) {
			for (Car carDetails : allCars) {
				carID = carDetails.getCarId();
				Date lastWorkflowStatusDate = carDetails
						.getLastWorkflowStatusUpdateDate();
				List<VendorImage> vendorImages = this.carManager
						.getAllVendorImages(carID);
				updateFlag = true;
				// loop through the vendor images to mark as approved
				for (VendorImage vendorImg : vendorImages) {
					vendorImageId = vendorImg.getImage().getImageId();
					mcUploadedDate = vendorImg.getMcUploadDate();
					// checking maximum date for lastWrokflowDate and
					// mcUploadDate
					Date maxDate = max(lastWorkflowStatusDate, mcUploadedDate);
					// get the date object for last 24 hrs
					Date twentyFourHrsAgo = new Date(
							new Date().getTime() - 1440 * 60 * 1000);
					// check whether the image is pending from last 24 hrs
					if (maxDate.before(twentyFourHrsAgo)) {
						// mark image as approved
						vendorImg.setBuyerApproved("APPROVED");
						vendorImg.setApprovedDate(new Date());
						vendorImg.setUpdatedBy(userName.getValue());
						//Modified for fixing the defect send update feed
						vendorImg.setUpdatedDate(new Date());
						getCarManager().save(vendorImg);
						log.info(" updated BuyerApproved as APPROVED in Vendor Image Table for vendor image id "
								+ vendorImg.getVendorImageId());
					} else {
						updateFlag = false;
					}
				}
				if (updateFlag) {
					carDetails.setBuyerApprovalPending("N");
					log.info("no vendor images pending for buyer approval, updating the CAR.BUYER_APPROVAL_PENDING to N :"+carID);
				}
				getCarManager().save(carDetails);
			}
		} else {
				log.info("---------------->  No CARS found for buyer approval <-----------------");
		}
		if (log.isInfoEnabled()) {
			log.info("---------------->  End Automatically Approve Vendor Images  <-----------------");
		}
	 }
			
	/** 
     * Returns the maximum of two dates. A null date is treated as being less
     * than any non-null date. 
     */
    public static Date max(Date d1, Date d2) {
        if (d1 == null && d2 == null) return null;
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return (d1.after(d2)) ? d1 : d2;
    }

	@Override
	public void importUserRankData() throws IOException, UserRankException,
			NamingException {
		if (log.isInfoEnabled())
			log.info("---------------->  Begin importing UserRank Data <-----------------");

		// Initialize Global Variables
		Collection<UserRankDTO> col = null;

		Config importDirectory = (Config) lookupManager.getById(Config.class, Config.USERRANK_IMPORT_DIRECTORY);
		Config importFileName = (Config) lookupManager.getById(Config.class, Config.USERRANK_IMPORT_FILENAME);
		Config completedDirectory = (Config) lookupManager.getById(Config.class, Config.USERRANK_FILE_COMPLETED_DIRECTORY_NAME);
		Config inprocessDirName = (Config) lookupManager.getById(Config.class, Config.USERRANK_FILE_INPROCESS_DIRECTORY_NAME);
		Config errorDirName = (Config) lookupManager.getById(Config.class, Config.USERRANK_FILE_ERROR_DIRECTORY_NAME);
		if (importDirectory == null || importFileName == null || completedDirectory == null ||inprocessDirName == null || errorDirName ==null ) {
			throw new UserRankException("1001","Cannot process import job. A configuration value is missing");
		}
		String strInprocessDir=importDirectory.getValue() + inprocessDirName.getValue();
		String strArchiveDir=importDirectory.getValue() + completedDirectory.getValue();
		String strErrorDir=importDirectory.getValue() + errorDirName.getValue();

		File inprocessDir= new File(strInprocessDir);
		if(inprocessDir.isDirectory()){
			File[] fileList= inprocessDir.listFiles();
			if(fileList == null || fileList.length > 0){
				log.error("Files are still in inprocess, Could not process the file");
				if(log.isInfoEnabled()){
					log.info("---------------->  End importing UserRank Data <-----------------");
				}
				return;
			}
		}
		boolean scanDirectory = StringUtils.isNotBlank(importDirectory.getValue()) && StringUtils.isBlank(importFileName.getValue());
		File[] files = null;
		if (scanDirectory) { //User selected to process all files under the directory			
			File dir = new File(importDirectory.getValue());
			files = dir.listFiles();
		} else { //User selected to scan a single file
			String filePath = importDirectory.getValue() + importFileName.getValue();
			File fl = new File(filePath);
			if (fl.exists()) {
				files = new File[] { fl };
			}
		}
		// Directory doesn't exist or there aren't any files to process
		if (files == null) {
			if (log.isInfoEnabled()) {
				if (scanDirectory) {
					log.info("There are no files to process in directory: " + importDirectory.getValue() + " Exiting job...");
				} else {
					log.info("File " + importDirectory.getValue() + importFileName.getValue() + " not found!!! Nothing to process. Exiting job...");
				}
			}
		} else{
			List<File> destFiles = moveFilesToDirectory(files, inprocessDir, false);
			if (destFiles == null || destFiles.size() < 1){
				if(log.isInfoEnabled()){
					log.info("There are No files to process");
					log.info("---------------->  End importing UserRank Data <-----------------");
				}
				return;
			}
			try{ 
				//Process Files
				for (File file : destFiles) {
					try{	
						if(!file.isDirectory()) {
							col = ReadUserRankFile.process(file);
							if (col == null || col.isEmpty()) {
								if (log.isInfoEnabled())
									log.info("No UserRank data in  file " + file.getName());
							} else {
								processUserRankData(col);
							}
							List<File> archiveFiles = moveFilesToDirectory(new File[] {file}, new File(strArchiveDir) , true); 
							if (archiveFiles != null || archiveFiles.size() > 0) {
								if (log.isInfoEnabled()) {
									log.info(file.getAbsolutePath() + " file is archived successfully.");
								}	
							} else {
								log.error("Error moving file " + file.getAbsolutePath() + " to directory: " + strArchiveDir);
								List<File> errorFiles = moveFilesToDirectory(new File[]{file}, new File(strErrorDir), true);
								if (errorFiles != null || errorFiles.size() > 0) {
									if (log.isInfoEnabled())
										log.info(file.getAbsolutePath() + " file moved to error directory");
								}
							}
						}
					}catch(Exception ex){
						ex.printStackTrace();
						log.error("Exception while Reading/Processing UserRank file", ex);
						List<File> errorFiles = moveFilesToDirectory(new File[]{file}, new File(strErrorDir), true);
						if (errorFiles != null || errorFiles.size() > 0) {
							if (log.isInfoEnabled())
								log.info(file.getAbsolutePath() + " file moved to error directory");
						}
						processException("USER RANK data job", ex);

					}
				}
			}finally{
				File[] fileList = inprocessDir.listFiles();
				if(fileList != null && fileList.length > 0){
					log.error(fileList.length  + " files are still inprocess directory, Moving it to error directory");
					moveFilesToDirectory(fileList, new File(strErrorDir), true);
				}
			}
		}

		if (log.isInfoEnabled())
			log.info("---------------->  End importing UserRank Data <-----------------");
		
	}

	private void processUserRankData(Collection<UserRankDTO> col) { 
		try {
			if (col != null) {
				if (log.isInfoEnabled())
					log.info("Processing UserRank Data: There are " + col.size() + " UserRank data in the file.");
				userRankManager.cleanUsersRankTmpTable();
			}
			Config userName = (Config) lookupManager.getById(Config.class, Config.CAR_IMPORT_USER);
			User systemUser = this.userManager.getUserByUsername(userName.getValue());
			for(UserRankDTO userRankDTO:col){
				UsersRankTmp usersRankTmp=new UsersRankTmp();
				usersRankTmp.setDepartmentCode(userRankDTO.getDeptNo());
				usersRankTmp.setDmmName(userRankDTO.getDmmName());
				usersRankTmp.setDmmEmail(userRankDTO.getDmmEmail());
				usersRankTmp.setGmmName(userRankDTO.getGmmName());
				usersRankTmp.setGmmEmail(userRankDTO.getGmmEmail());
				usersRankTmp.setDemandCenter(userRankDTO.getDemandCenter());
				usersRankTmp.setDcDesc(userRankDTO.getDcDesc());
				userRankManager.save(usersRankTmp);
				//log.info("Processed successfully:::"+userRankManager);
			}
			log.info("Started call build_userrank procedure");
			userRankManager.callBuildUsersRankTmp();
			log.info("Completed executing build_userrank procedure"); 
		} catch (Exception e) {
			log.error("Processing UserRank Data has error"+e.getMessage(),e);
			processException("USER RANK data job failed!!!!!!!!!!!!!!!", e);
		} 
	}

	@Override
	public void exportPWPandGWPAttributes() throws CarJobDetailException, IOException {
		
		log.info("---------------->  Begin Export Attributes Values to BM <-----------------");
		List<CarAttribute> upadtedAttribute =null;
									
			upadtedAttribute = carManager.getupadtedAttributeforActiveCARS();
			
			if (upadtedAttribute!=null){
    			if(log.isDebugEnabled()){
    				log.debug(" size of update Attribute:"+upadtedAttribute.size());
    			}
    		Config exportDirConfig = (Config) lookupManager.getById(Config.class, Config.PWP_GWP_EXPORT_DIR);
    		String fileName = "data_file"+"_"
    				+ DateUtils.formatDate(new Date(),
    						"MM_dd_yyyy_HH_mm_ss_SS")+".bmi";
		
    		File dir = new File(exportDirConfig.getValue());
    		if (!dir.exists())
    			dir.mkdirs();
    		
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
			out = new FileWriter(file, true);
			try {
			for(CarAttribute carAttribute:upadtedAttribute)
			{
				StringBuffer attrValues =new StringBuffer();
				attrValues.append("S");
				attrValues.append("|");
				attrValues.append("OBJECT_ATTRIBUTE");
				attrValues.append("|");
				attrValues.append("PRODUCT");
				attrValues.append("|");
				attrValues.append(carAttribute.getCar().getVendorStyle().getVendorNumber());
				attrValues.append(carAttribute.getCar().getVendorStyle().getVendorStyleNumber());
				attrValues.append("|");
				attrValues.append(carAttribute.getAttribute().getName());
				attrValues.append("|");
					if (!carAttribute.getAttrValue().trim().equals("")
							&& carAttribute.getAttrValue().trim() != null) {
						if (carAttribute.getAttrValue().equals("Yes")) {
							attrValues.append("True");
						} else {
							attrValues.append("False");
						}
					} else {
						attrValues.append("<NULL>");
					}
					attrValues.append(NEW_LINE);
					out.write(attrValues.toString());
				}
		} catch (IOException ioex) {
			hasErrors = true ;
			log.error("----------------> Error ouccured while processing the PWP and GWP <-----------------"+ioex.getMessage());
		}
			finally {
				if (out != null) {
					try {
						out.close();
					} catch (Exception ex) {
						log.error("----------------> Error ouccured while processing the PWP and GWP <-----------------"+ex.getMessage());
						
					}
				}
			}
			Config ftpHost = (Config) lookupManager.getById(Config.class, Config.PWP_GWP_EXPORT_FTP_HOST);
			Config ftpUserId = (Config) lookupManager.getById(Config.class, Config.PWP_GWP_EXPORT_FTP_USER_ID);
			Config ftpPassword = (Config) lookupManager.getById(Config.class, Config.PWP_GWP_EXPORT_FTP_PASSWORD);
			Config ftpRemoteDir = (Config) lookupManager.getById(Config.class, Config.PWP_GWP_EXPORT_REMOTE_DIR);

			if (file.exists()) {
				if (!hasErrors) {
				try  {
					Session session =SFtpUtil.openSftpSession(ftpHost.getValue(), ftpUserId.getValue(), ftpPassword.getValue(), 22, "no");
					Channel channel = session.openChannel("sftp");
		            channel.connect();
		            ChannelSftp  sftpChannel = (ChannelSftp) channel;
		            SFtpUtil.sendDataFiles(sftpChannel,  Arrays.asList(file), ftpRemoteDir.getValue());
		            SFtpUtil.closeSftpConnection(session,sftpChannel);
				}
		            
				catch(Exception e) {
					log.error("----------------> Problem occured while ftping the file <-----------------"+e.getMessage());
				}
			}
	}
  }		
	else {
			log.info("----------------> No PWP and GWP Attribute changes found to export <-----------------");
		}
		log.info("---------------->  End Export Attributes Values to BM <-----------------");
	
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