/**
 * 
 */

package com.belk.car.app.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.naming.NamingException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;
import org.xml.sax.SAXException;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.exceptions.CarJobDetailException;
import com.belk.car.app.exceptions.SendEmailException;
import com.belk.car.app.model.BaseAuditableModel;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarImage;
import com.belk.car.app.model.CarImageId;
import com.belk.car.app.model.Config;
import com.belk.car.app.model.Image;
import com.belk.car.app.model.ImageSourceType;
import com.belk.car.app.model.ImageTrackingStatus;
import com.belk.car.app.model.ImageType;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.vendorcatalog.CompositeKeyForVndrCatRecord;
import com.belk.car.app.model.vendorcatalog.VendorCatalog;
import com.belk.car.app.model.vendorcatalog.VendorCatalogHeader;
import com.belk.car.app.model.vendorcatalog.VendorCatalogImageLocation;
import com.belk.car.app.model.vendorcatalog.VendorCatalogImport;
import com.belk.car.app.model.vendorcatalog.VendorCatalogRecord;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSku;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSkuId;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSkuImage;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSkuMaster;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSkuMasterId;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.DropshipManager;
import com.belk.car.app.service.EmailManager;
import com.belk.car.app.service.QuartzJobManagerForVendorCatalog;
import com.belk.car.app.util.ImportVendorCatalog;
import com.belk.car.app.util.PropertyLoader;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.util.DateUtils;

/**
 * @author afusy45
 */
/**
 * @author afusy07
 * @Date Feb 6, 2010
 */
public class QuartzJobManagerForVendorCatalogImpl extends BaseFormController
implements
QuartzJobManagerForVendorCatalog,
DropShipConstants {


	/**
	 * 
	 */
	private static final int STYLE_NAME = 19;
	private DropshipManager dropshipManager;
	private ImportVendorCatalog importVendorCatalog;
	private CarLookupManager lookupManager;
	private EmailManager sendEmailManager;
	private List<VendorCatalog> catalogTranslated = new ArrayList<VendorCatalog>();
	private Map<Long, String> catalogAndErrors = new HashMap<Long, String>();
	private Map<Long, String> catalogAndName = new HashMap<Long, String>();
	private Map<Long, String> catalogAndVendorId = new HashMap<Long, String>();
	private Map<Long, String> catalogAndVendorName = new HashMap<Long, String>();
	private Map<Long, SortedSet<String>> catalogAndErrorRecord = new HashMap<Long, SortedSet<String>>();
	private Map<CompositeKeyForVndrCatRecord, List<String>> recordAndError = new HashMap<CompositeKeyForVndrCatRecord, List<String>>();
	private Map<Long, String> catalogAndNumberOfRecordsImported = new HashMap<Long, String>();

	public EmailManager getSendEmailManager() {
		return sendEmailManager;
	}

	public void setSendEmailManager(EmailManager sendEmailManager) {
		this.sendEmailManager = sendEmailManager;
	}

	public CarLookupManager getLookupManager() {
		return lookupManager;
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

	private UserManager userManager;

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
	 * @return the importVendorCatalog
	 */
	public ImportVendorCatalog getImportVendorCatalog() {
		return importVendorCatalog;
	}

	/**
	 * @param importVendorCatalog the importVendorCatalog to set
	 */
	public void setImportVendorCatalog(ImportVendorCatalog importVendorCatalog) {
		this.importVendorCatalog = importVendorCatalog;
	}

	private transient final Log log = LogFactory.getLog(QuartzJobManagerForVendorCatalogImpl.class);

	private List<String> noteTextList = new ArrayList<String>();

	/**
	 * Method to read file from IDB into CARS and process it (non-Javadoc)
	 * 
	 * @throws IOException,NamingException,CarJobDetailException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @author afusy45-Siddhi Shrishrimal
	 */
	public void importVendorCatalogData()

	throws IOException, CarJobDetailException, NamingException, ParseException,
	SAXException, ParserConfigurationException {
		log.info("---------------->  Begin importing Vendor Catalog Data <-----------------");


		boolean success = true;
		/* List to store image names read from catalog file */
		List list = new ArrayList();
		String imageLocationId = "";
		VendorCatalogImageLocation imageCode = new VendorCatalogImageLocation();
		List<VendorCatalog> catalogs = null;
		/*
		 * Getting all the catalogs with Status Importing from Vendor_Catalog
		 * table
		 */
		try {
			// Get All catalogs with importing status
			catalogs = dropshipManager.getCatalogsWithStatus("Importing");

		}
		catch (HibernateObjectRetrievalFailureException he) {
			he.printStackTrace();
			log.error("StatkTrace=" + he);
			log.error("Cause=" + he.getCause());
			log.error("" + he.getMostSpecificCause());
		}
		catch (DataRetrievalFailureException d) {
			d.printStackTrace();
			log.error("StatkTrace=" + d);
			log.error("Cause=" + d.getCause());
		}
		catch (Exception e) {
			e.printStackTrace();
			log.error("StatkTrace=" + e);
			log.error("Cause=" + e.getCause());
		}

		/********************** IMPORT VENDOR IMAGES ************************************/

		boolean isUploadImages = false;
		String ftpURL = "";
		String subStringURL = "";
		int startURL;
		int endURL;
		String vendorCatalogName = null;
		List<VendorCatalogImport> vendorCatalogImport = new ArrayList<VendorCatalogImport>();
		VendorCatalogImport catalogImport = new VendorCatalogImport();

		//Get FTP details
		Properties servletProperties = (Properties) getServletContext().getAttribute(FTPDETAILS);
		String destinationFtpHost = servletProperties.getProperty(FTPHOST);
		String destinationFtpUname = servletProperties.getProperty(FTPUSER);
		String destinationFtpPasswd = servletProperties.getProperty(FTPPASSWORD);
		Properties properties = PropertyLoader.loadProperties("ftp.properties");
		String destinationPath = properties.getProperty("imagePath");
		if(log.isDebugEnabled())
			log.debug("image path=" + destinationPath);
		String newDirName = properties.getProperty("catalogFilePathArchive");
		//String newDirName = "C:\\cars\\data\\catalogs\\dev\\archive";
		String filePath =properties.getProperty("catalogFilePath");
		//String filePath="C:\\cars\\data\\catalogs\\dev";
		if(log.isDebugEnabled()) {
			log.debug("filePath...." + filePath);
		}
		// Check if the catalogs inside the folder have required name
		if (filePath == null) {
			throw new CarJobDetailException("Cannot process import job. A configuration value is missing");
		}



		if (catalogs != null) {

			log.info("catalogs =" + catalogs.size());

			for (VendorCatalog catalog : catalogs) {

				// try {
				if(log.isDebugEnabled()) {
					log.debug("catalog_id="+catalog.getVendorCatalogID());
				}
				Long catalogId = catalog.getVendorCatalogID();
				String vendorNum = catalog.getVendor().getVendorNumber();

				// Construct folderName
				String folderName = "" + vendorNum + "_" + "" + catalogId;
				if(log.isInfoEnabled()) {
					log.info("folder name:-----" + folderName);
				}
				String destDir = destinationPath + "/" + folderName
				+ "/" + UNMAPPED+"/";

				/*
				 * Method to get Vendor Catalog Import Info from
				 * VENDOR_CATALOG_IMPORT table for vendorCatalogName and
				 * CatalogId
				 */

				vendorCatalogImport = dropshipManager.getVendorCatalogImportInfo(catalogId);
				if (vendorCatalogImport.size() > 0 && !vendorCatalogImport.isEmpty()) {

					vendorCatalogImport = dropshipManager.getVendorCatalogImportInfo(catalogId);
					if (vendorCatalogImport.size() > 0) {
						catalogImport = vendorCatalogImport.get(0);
					}
				}
				if (catalogImport != null) {
					log.debug("If catalogImport info is not null");

					vendorCatalogName = catalogImport.getVendorCatalogFileName();
					imageCode = catalogImport.getImageLocationID();
					imageCode = catalogImport.getImageLocationID();
					if(log.isDebugEnabled()) {
						log.debug("vendorCatalogName......" + vendorCatalogName);
						log.debug("imageCode...." + imageCode);
					}
					imageLocationId = imageCode.getImageLocationID();

				}
				/* Get image location ID */
				imageLocationId = imageCode.getImageLocationID();

				log
				.debug("/************************************Import VENDOR CATALOG***********************************/");

				//Check whether the file in uploaded on specified location  or not.

				String catalogFilePath = filePath +"/" + vendorNum +"/" + catalogId + "/" +  vendorCatalogName;
				File catalogFile = new File(catalogFilePath);
				if(log.isDebugEnabled()) {
					log.debug("Catalog FIle Path:"+catalogFilePath);
					log.debug("catalogFile-" + catalogFile + "-" + catalogFile.isFile());
				}
				if(null!=catalogFile && catalogFile.isFile()) {
					log.debug("catalog file present at the location");
					if (catalogImport.getFtpUrl() != null) {
						log.debug("File has ftp location specified");
						ftpURL = catalogImport.getFtpUrl();
						startURL = 0;
						endURL = startURL + 4;
						subStringURL = ftpURL.substring(startURL, endURL);
					}
					if(log.isDebugEnabled()) {
						log.info("ftpURL...." + ftpURL);
					}

					/* Check if text */
					if ((vendorCatalogName.toUpperCase().endsWith(".TXT"))
							|| (vendorCatalogName.toUpperCase().endsWith(".CSV"))) {
						if(log.isDebugEnabled()) {
							log.debug("File is text file....");
						}
						String delimiter = catalogImport.getDataFileDelimeter();

						try {
							if(log.isDebugEnabled()) {
								log.debug("Reading text file....");
							}
							if(!delimiter.equals("") && !delimiter.equals(null)){
								list = importVendorCatalog.readTextFile(catalogFile, catalog,
										delimiter);
							}else{
								log.info("No delimiter present for the file");
							}
							if(list==null || list.isEmpty()) {
								success =false;
							}
						}
						catch (ParseException e) {
							e.printStackTrace();
							success =false;
						}

					} else if ((vendorCatalogName.toUpperCase().endsWith(".XLS"))) {
						if(log.isDebugEnabled()) {
							log.debug("File is excel file....");
							log.debug("Reading excel file....");
						}
						try {
							list = importVendorCatalog.readExcelFile(catalogFile, catalog);
							if(list==null || list.isEmpty()) {
								success =false;
							}
						}
						catch(Exception e) {
							e.printStackTrace();
							success =false;
						}


					} else if ((vendorCatalogName.toUpperCase().endsWith(".XML"))) {
						if(log.isDebugEnabled()) {
							log.debug("File is xml file....");
							log.debug("Reading xml file....");
						}
						try {
							list = importVendorCatalog.readXMLFile(catalogFile, catalog);
							if(list==null || list.isEmpty()) {
								success =false;
							}
						}
						catch(Exception e) {
							e.printStackTrace();
							success =false;
						}

					}
					// Process the iumages associated with Catalog.
					/*
					 * If location id is 2 then upload from
					 * vendor FTP location
					 */

					if ("2".equals(imageLocationId)) {
						log.debug("Upload images location provided");

						/*
						 * Check whether the image location does
						 * not starts with http
						 */
						if (!"http".equals(subStringURL)) {
							log.debug("Transfer images from ftpsource to destination");
							isUploadImages = importVendorCatalog
							.methodInvokingImportFTPImages(catalogId,
									destDir,catalogImport
									.getFtpUrl(), catalogImport
									.getFtpUname(), catalogImport
									.getFtpPassword());
						}
						else {
							log.debug("If image location path doesnt start with ftp");
							log.info("List of images in file" + list);

							isUploadImages = importVendorCatalog
							.methodReadingImagesFromSite(list, catalogId,
									destDir, destinationFtpHost,
									destinationFtpUname,
									destinationFtpPasswd, catalogImport
									.getFtpUrl(), catalogImport
									.getFtpUname(), catalogImport
									.getFtpPassword());
						}
						if (isUploadImages) {
							log.debug("/*************END IMPORT VENDOR IMAGES******************************/");
						}
					}else if("4".equals(imageLocationId)){
						//No images option is selected so do nothing
						log.debug("Image location ID is 4 so no Images uploaded");
						isUploadImages = false;

					}
					else{
						//Read the image names from folder(image) location and save in vendorcatalog image table
						isUploadImages = importVendorCatalog.methodSavingImagesToImageTable(destDir,catalogId);
					}


					//If Catalog File rerading and Image Processing is successfull then update database and move the file to archive
					if (success) {
						log.debug("If success reading file move the file to archive folder");

						File sourceDir = new File(filePath);
						// fix code by changing from mkdir to mkdirs
						boolean isNewDirectory = new File(newDirName+"/"+vendorNum + "/" + catalogId).mkdirs();
						log.debug("archive directory created? :"+isNewDirectory);
						boolean archived=catalogFile.renameTo(new File(newDirName+"/"+vendorNum+"/" + catalogId, catalogFile.getName()));
						log.debug("archive success? :"+archived);
						// make new directory for Arch. directory
					}
				} else {
					log.debug("Error Processing Catalog");
					catalogFile =null;
				}

			}

			log.info("<<---------------------------------End of Import Vendor Catalog Record-------------------------------------->>");
		}// End of file length


	}// End of import vendor catalog method

	/**
	 * @throws IOException
	 * @throws CarJobDetailException
	 * @throws NamingException
	 * @TODO Cron 17. Load vendor catalog data in
	 *       VENDOR_CATALOG_STY_SKU,VNDR_CATL_STY_SKU_MAST
	 *       ,VNDR_CATL_STY_SKU_IMAGE TABELS Load images for existing cars in
	 *       progress.
	 */
	public void loadVendorCatalogDataIntoCARS()
	throws IOException, CarJobDetailException, NamingException {
		if (log.isInfoEnabled()) {
			log
			.info("---------------->  Begin Importing Catalog Data to CARS For Dropship <-----------------");
		}

		List<VendorCatalog> catalogs = new ArrayList<VendorCatalog>();
		List<VendorCatalogStyleSku> styleSkuList = new ArrayList<VendorCatalogStyleSku>();
		List<VendorCatalogRecord> records = null;

		VendorCatalogHeader headerStyle = new VendorCatalogHeader();
		VendorCatalogHeader headerUpc = new VendorCatalogHeader();
		VendorCatalogHeader headerColor = new VendorCatalogHeader();
		VendorCatalogHeader headerDescription = new VendorCatalogHeader();
		VendorCatalogHeader headerStyleName =  new VendorCatalogHeader();
		Long catalogId = null;
		long templateId = 0;

		try {
			// Get All catalogs with translating status
			catalogs = dropshipManager.getCatalogsWithStatus(TRANSLATING);

		}
		catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("StatkTrace=" + e.getStackTrace());
				log.error("Cause=" + e.getCause());
			}
		}
		if (catalogs != null) {
			if (log.isDebugEnabled()) {
				log.debug("catalogs =" + catalogs.size());
			}
			if (catalogs.isEmpty()) {
				return;
			}
		}

		// For each catalog get records and save to VENDOR_CATALOG_STY_SKU table
		for (VendorCatalog catalog : catalogs) {
			noteTextList = new ArrayList<String>();
			try{
				catalogId = catalog.getVendorCatalogID();
				templateId = catalog.getVendorCatalogTemplate().getVendorCatalogTmplID();
			}
			catch(Exception e){
				if(log.isErrorEnabled()){
					log.error("Template ID not found , returning");
				}
				continue;
			}
			if (log.isDebugEnabled()) {
				log.debug("catalogId=" + catalogId);
				log.debug("templateId=" + templateId);
				log.debug("catalog added for sending email");
			}
			// Add catalog id for sending email
			catalogTranslated.add(catalog);

			// Initially no errors for catalog
			catalogAndErrors.put(catalogId, "NO");

			// Catalog and its name
			catalogAndName.put(catalogId, catalog.getVendorCatalogName());
			Long vendorID = catalog.getVendor().getVendorId();
			catalogAndVendorId.put(catalogId, vendorID.toString());
			catalogAndVendorName.put(catalogId, catalog.getVendor().getName());

			// Get the headers from header table for style,vendorUpc,deptid From
			// HEADER table

			headerStyle = dropshipManager.getMappingForMasterAttribute(STYLE_ATTRIBUTE_ID,
					templateId, catalogId);
			headerUpc = dropshipManager.getMappingForMasterAttribute(VENDOR_UPC_ATTRIBUTE_ID,
					templateId, catalogId);
			headerColor = dropshipManager.getMappingForMasterAttribute(COLOR_ATTRIBUTE_ID,
					templateId, catalogId);
			headerDescription = dropshipManager.getMappingForMasterAttribute(
					STYLE_DESC_ATTRIBUTE_ID, templateId, catalogId);
			headerStyleName = dropshipManager.getMappingForMasterAttribute(
					STYLE_NAME, templateId, catalogId);

			long headerNumUPC = (headerUpc == null) ? 0 : headerUpc.getVendorCatalogFieldNum();
			long headerNumStyle = (headerStyle == null) ? 0 : headerStyle
					.getVendorCatalogFieldNum();
			long headerNumColor = (headerColor == null) ? 0 : headerColor
					.getVendorCatalogFieldNum();
			long headerNumDesc = (headerDescription == null) ? 0 : headerDescription
					.getVendorCatalogFieldNum();
			long headerNumStyleName= (headerStyleName == null) ? 0 : headerStyleName
					.getVendorCatalogFieldNum();
			if (log.isDebugEnabled()) {
				log.debug("headerNumStyle=" + headerNumStyle);
				log.debug("headerNumUPC=" + headerNumUPC);
				log.debug("headerNumColor=" + headerNumColor);
				log.debug("headerNumDesc=" + headerNumDesc);
				log.debug("headerNumStyleName=" + headerNumStyleName);
			}
			// Get records for the three attributes order by record num from
			// RECORD table
			List<Long> headerList = new ArrayList<Long>();
			headerList.add(headerNumStyle);
			headerList.add(headerNumUPC);
			headerList.add(headerNumColor);
			headerList.add(headerNumDesc);
			headerList.add(headerNumStyleName);
			try {
				records = dropshipManager.getRecordsForHeaderNums(catalogId, headerList);
			}
			catch (Exception e) {
				if (log.isErrorEnabled()) {
					log.error(e.getStackTrace());
				}
				noteTextList.add("Error while getting records for standard attributes.");
			}
			if (log.isDebugEnabled() && !records.isEmpty()) {
				log.debug("records=" + records.size());
			}

			// Construct Style SKU model based on records received, to be saved
			// in VENDOR_CATLOG_STYLE_SKU table
			if (null != records && !records.isEmpty()) {
				styleSkuList = getStyleSkuModel(records, headerNumStyle, headerNumUPC,
						headerNumColor, headerNumDesc, headerNumStyleName,catalogId, templateId);
				if (log.isDebugEnabled() && !styleSkuList.isEmpty() ) {
					log.debug("styleSkuList=" + styleSkuList.size());
				}
			}

			if (styleSkuList != null) {
				Iterator<VendorCatalogStyleSku> styleSkuItr = styleSkuList.iterator();
				while (styleSkuItr.hasNext()) {
					VendorCatalogStyleSku vendorCatalogStyleSku = styleSkuItr.next();
					if (log.isDebugEnabled()) {
						log.debug("composite key ="
								+ vendorCatalogStyleSku.getCompositeKey().getVendorUPC());
						log.debug("composite key ="
								+ vendorCatalogStyleSku.getCompositeKey().getVendorCatalogId());
						log.debug("composite key ="
								+ vendorCatalogStyleSku.getCompositeKey().getVendorStyleId());
						log.debug("record Num-=" + vendorCatalogStyleSku.getRecordNum());
					}
					List<VendorCatalogStyleSku> list = new ArrayList<VendorCatalogStyleSku>();
					//Added status to style sku 
					vendorCatalogStyleSku.setStatus(Status.ACTIVE);
					list.add(vendorCatalogStyleSku);
					try {
						dropshipManager.saveStyleSkus(list);
					}
					catch (Exception ex) {
						StringWriter sw = new StringWriter();
						// Change the status of catalog error to yes //i.e.
						// catalog has error
						if (catalogAndErrors.containsKey(catalogId)) {
							catalogAndErrors.remove(catalogId);
						}
						catalogAndErrors.put(catalogId, "YES");
						// Get erroneous record list for catalog
						if (catalogAndErrorRecord.containsKey(catalogId)) {
							catalogAndErrorRecord.get(catalogId).add(
									vendorCatalogStyleSku.getRecordNum().toString());
						}
						else {
							SortedSet<String> errorRecordNums =  new TreeSet<String>();
							errorRecordNums.add(vendorCatalogStyleSku.getRecordNum().toString());
							catalogAndErrorRecord.put(catalogId, errorRecordNums);
						}

						ex.printStackTrace(new PrintWriter(sw));
						// Put the error related to record in catalog to map
						CompositeKeyForVndrCatRecord compKey = new CompositeKeyForVndrCatRecord(
								catalogId, new Long(0), vendorCatalogStyleSku.getRecordNum());
						if (recordAndError.containsKey(compKey)) {
							recordAndError.get(compKey).add(sw.toString());
						}
						else {
							List<String> errorRecordNums = new ArrayList<String>();
							errorRecordNums.add(sw.toString());
							recordAndError.put(compKey, errorRecordNums);
						}

						if (log.isErrorEnabled()) {
							log.error("Error----" + sw);
						}
						noteTextList.add("Error while inserting  record in style sku table-"
								+ vendorCatalogStyleSku.getRecordNum() + " \nError is-  "
								+ sw.toString().substring(0,50));
						continue;
					}

				}

			}
			try {
				dropshipManager.saveVendorCatalogNote(catalog, noteSubject, noteTextList);
			}
			catch (ParseException e) {

				e.printStackTrace();
			}
		}
		fillCatalogDataInMasterTable(catalogs);
		dropshipManager.setImageForCatalogInCatalogImageTable(catalogs);
		Config userName = (Config) lookupManager.getById(Config.class, Config.CAR_IMPORT_USER);
		User systemUser = this.userManager.getUserByUsername(userName.getValue());
		// Get images corresponding to car
		Map<Long, List<Image>> carsImagesMap = dropshipManager.getCARSInProgress();
		// Get images for all records in master table
		Map<VendorCatalogStyleSkuMasterId, List<VendorCatalogStyleSkuImage>> masterImageMapping = dropshipManager
		.getAllImagesforMaster();
		// Get car skus
		Map<Car, Set<VendorSku>> carsSkuMap = dropshipManager.getCarAndSkus();
		// Get image types
		Map<String, ImageType> imageTypeMap = dropshipManager.getImageTypes();
		ImageSourceType imageSourceType = dropshipManager.getImageSourceType("ON_HAND");
		ImageTrackingStatus imageTrackingStatus = dropshipManager
		.getImageTrackingStatus("RECEIVED");

		Set<Car> carKeySet = carsSkuMap.keySet();
		if (log.isDebugEnabled()) {
			log.debug("carsInProgress=" + carKeySet.size());
		}
		// For each Car get the skus
		// For each sku-
		// Check whether the sku style exists in master table
		// If sku exists in master table-
		// Get the images for that sku
		// If images already exist with same name in Image table then make them
		// as inactive
		// and make an entry into car_image and image table
		for (Car car : carKeySet) {

			// Sku list for car to match with sku in master table
			Set<VendorSku> skuList = carsSkuMap.get(car);
			List<Image> imagesForCar = carsImagesMap.get(new Long(car.getCarId()));

			List<Image> imageListNew = new ArrayList<Image>();
			List<CarImage> carImageListNew = new ArrayList<CarImage>();
			List<Image> imageListExisting = new ArrayList<Image>();
			Map<String, Image> imageToBeSaved = new HashMap<String, Image>();
			Map<Long, Image> existingImageToBeSaved = new HashMap<Long, Image>();
			Long longVendorUpc = null;
			VendorStyle style=null;
			String styleNum=null;
			if (null != skuList && !skuList.isEmpty()) {


				// For each sku get the record in master table and find images
				for (VendorSku vendorSku : skuList) {

					if (StringUtils.isNumeric(vendorSku.getVendorUpc())) {
						longVendorUpc = new Long(vendorSku.getVendorUpc());
					}
					else {
						longVendorUpc = new Long("0");
					}

					style=(VendorStyle) dropshipManager.getById(VendorStyle.class, vendorSku.getVendorStyle().getVendorStyleId());
					styleNum=style.getVendorStyleNumber();
					VendorCatalogStyleSkuMasterId masterId = new VendorCatalogStyleSkuMasterId(
							styleNum,
							longVendorUpc.toString(), new Long(0));

					// If Images are present for that master key
					if (masterImageMapping.containsKey(masterId)) {
						if (log.isDebugEnabled()) {
							log.debug("Master not null");
						}
						// Get image list for the master key.
						List<VendorCatalogStyleSkuImage> imageList = masterImageMapping
						.get(masterId);
						
						// For each image for one style sku do-
						for (VendorCatalogStyleSkuImage imageFromCatalog : imageList) {
							String imageName = imageFromCatalog.getImageFileName();

							if (null != imagesForCar && !imagesForCar.isEmpty()) {
								for (Image imageFromCar : imagesForCar) {
									if (log.isDebugEnabled()) {
										log.debug("imageFromCar.getImageFinalUrl()="
												+ imageFromCar.getImageFinalUrl());
									}
									// Check whether the image already exist in
									// Car
									if (imageFromCar.getImageFinalUrl().equals(imageName)) {
										// Check whether that image is already
										// inactivated.
										// If yes then don't add it in the list
										// of images which is to be inactivated
										// this is done so because image can
										// come again for one vendor style and
										// upc from another catalog
										if (!existingImageToBeSaved.containsKey(imageFromCar
												.getImageId())) {
											if (log.isDebugEnabled()) {
												log.debug("Adding image ="
														+ imageFromCar.getImageId());
											}
											imageFromCar.setStatusCd("INACTIVE");
											imageListExisting.add(imageFromCar);

											existingImageToBeSaved.put(imageFromCar.getImageId(),
													imageFromCar);
										}
										if (log.isDebugEnabled()) {
											log.debug("Images matched");
										}

									}
								}
							}
							// Construct a new Image model to be saved.
							if(!(imageFromCatalog.getImageFileName().indexOf("dummy")>-1)){
								Image imageNew = new Image();
								imageNew.setDescription(imageFromCatalog.getImageFileName());
								imageNew
								.setImageType(imageTypeMap.get(imageFromCatalog.getImageType()));
								imageNew.setImageFinalUrl(imageFromCatalog.getImageFileName());
								imageNew.setImageSourceType(imageSourceType);
								imageNew.setImageTrackingStatus(imageTrackingStatus);
								imageNew.setRequestDate(new Date());
								imageNew.setStatusCd("ACTIVE");
								imageNew.setCreatedBy(systemUser.getEmailAddress());
								imageNew.setCreatedDate(new Date());
								imageNew.setUpdatedBy(systemUser.getEmailAddress());
								imageNew.setUpdatedDate(new Date());

								// Check whether that image is already added to
								// list.
								// If yes then don't add it in the list of images
								// which is to be saved
								if (!imageToBeSaved.containsKey(imageNew.getImageFinalUrl())) {
									if (log.isDebugEnabled()) {
										log.debug("Adding image =" + imageNew.getImageFinalUrl());
									}
									imageListNew.add(imageNew);
									imageToBeSaved.put(imageNew.getImageFinalUrl(), imageNew);
								}
							}
						}
					}
				}

				// Code for saving style images in CAR.
				VendorCatalogStyleSkuMasterId masterId = new VendorCatalogStyleSkuMasterId(
						styleNum,"0", new Long(0));

				// If Images are present for that master key
				if (masterImageMapping.containsKey(masterId)) {
					log.debug("====================>Long UPC is null and got the image for style");
					// Get image list for the master key.
					List<VendorCatalogStyleSkuImage> imageList = masterImageMapping
					.get(masterId);

					for(VendorCatalogStyleSkuImage imageFromCtlg :imageList){
						// Construct a new Image model to be saved.
						if(!(imageFromCtlg.getImageFileName().indexOf("dummy")>-1)){
							Image imageNew = new Image();
							imageNew.setDescription(imageFromCtlg.getImageFileName());
							imageNew
							.setImageType(imageTypeMap.get(imageFromCtlg.getImageType()));
							imageNew.setImageFinalUrl(imageFromCtlg.getImageFileName());
							imageNew.setImageSourceType(imageSourceType);
							imageNew.setImageTrackingStatus(imageTrackingStatus);
							imageNew.setRequestDate(new Date());
							imageNew.setStatusCd("ACTIVE");
							imageNew.setCreatedBy(systemUser.getEmailAddress());
							imageNew.setCreatedDate(new Date());
							imageNew.setUpdatedBy(systemUser.getEmailAddress());
							imageNew.setUpdatedDate(new Date());

							// Check whether that image is already added to
							// list.
							// If yes then don't add it in the list of images
							// which is to be saved
							if (!imageToBeSaved.containsKey(imageNew.getImageFinalUrl())) {
								if (log.isDebugEnabled()) {
									log.debug("Adding image for style =" + imageNew.getImageFinalUrl());
								}
								imageListNew.add(imageNew);
								imageToBeSaved.put(imageNew.getImageFinalUrl(), imageNew);
							}
						}

					}

				}


				try{

					// Save new images
					imageListNew = dropshipManager.saveOrUpdateImages(imageListNew);
					// Save existing images
					imageListExisting = dropshipManager.saveOrUpdateImages(imageListExisting);


					// For each new image saved make an entry in car image table
					for (Image image : imageListNew) {
						CarImageId carImageId = new CarImageId(car.getCarId(), image.getImageId());
						//log.debug("Image id in car image="+image.getImageId());
						CarImage carImage = new CarImage();
						carImage.setId(carImageId);
						carImage.setCreatedBy(systemUser.getEmailAddress());
						carImage.setCreatedDate(new Date());
						carImage.setUpdatedBy(systemUser.getEmailAddress());
						carImage.setUpdatedDate(new Date());
						carImageListNew.add(carImage);
					}

					// Save car image objects
					dropshipManager.saveOrUpdateCarImages(carImageListNew);
				}
				catch(Exception e){
					log.error(e);
				}
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("end impoting images in CARS");
		}

		this.checkNumberOfRecordsImported(catalogs);
		this.sendEmailForCatalogTranslation(catalogTranslated, catalogAndErrors, recordAndError,
				catalogAndErrorRecord, catalogAndName, catalogAndVendorId, catalogAndVendorName,
				catalogAndNumberOfRecordsImported);

		if (log.isInfoEnabled()) {
			log
			.info("---------------->  End Ipmorting Catalog Data to CARS For Dropship <-----------------");
		}
	}

	/**
	 * @param catalogs
	 * @return void
	 * @Enclosing_Method checkNumberOfRecordsImported
	 * @TODO
	 */
	private void checkNumberOfRecordsImported(List<VendorCatalog> catalogs) {

		Map<BigDecimal, Long> numOfRecordsInStyleSkuMap = dropshipManager
		.getNumberOfRecordsImportedInStyleSku();
		for (VendorCatalog catalog : catalogs) {
			BigDecimal catalogId = new BigDecimal(catalog.getVendorCatalogID());
			Long numberOfRecordsInStyelSku = numOfRecordsInStyleSkuMap.get(catalogId) != null ? numOfRecordsInStyleSkuMap
					.get(catalogId)
					: new Long(0);
					log.debug("number of records imported is less for catalog=" + catalogId);
					StringBuffer statement = new StringBuffer("# of Successful Records Imported:")
					.append(numberOfRecordsInStyelSku);
					catalogAndNumberOfRecordsImported.put(catalog.getVendorCatalogID(), statement
							.toString());
		}

	}

	/**
	 * @param catalogTranslated -List of catalog translated
	 * @param catalogAndErrors - map with key as catalog Id and value as String
	 *            telling whether catalog has error or not
	 * @param recordAndError - Map with key as CompositeKeyForVndrCatRecord and
	 *            value as List of errors.
	 * @param catalogAndErrorRecord2 - Map with key as Catalog ID and values as
	 *            List of record numbers having error
	 * @param catalogAndName - Map with key as catalog Id and value as catalog
	 *            name.
	 * @param catalogAndVendorId -Map with key as catalog Id and value as vendor
	 *            ID
	 * @param catalogAndVendorName - Map with key as catalog Id and vendor name
	 * @param catalogAndNumberOfRecordsImported2 - Map with catalog Id and
	 *            number of catalogs imported
	 * @throws CarJobDetailException
	 * @return void
	 * @TODO
	 */
	@SuppressWarnings("unchecked")
	public void sendEmailForCatalogTranslation(
			List<VendorCatalog> catalogTranslated, Map<Long, String> catalogAndErrors,
			Map<CompositeKeyForVndrCatRecord, List<String>> recordAndError,
			Map<Long, SortedSet<String>> catalogAndErrorRecord2, Map<Long, String> catalogAndName,
			Map<Long, String> catalogAndVendorId, Map<Long, String> catalogAndVendorName,
			Map<Long, String> catalogAndNumberOfRecordsImported2)
	throws CarJobDetailException {
		Map model = new HashMap();
		Config userName = (Config) lookupManager.getById(Config.class, Config.CAR_IMPORT_USER);
		User systemUser = this.userManager.getUserByUsername(userName.getValue());

		Config sendNotifications = (Config) lookupManager.getById(Config.class,
				Config.SEND_EMAIL_NOTIFICATIONS);
		Properties properties = PropertyLoader.loadProperties("ftp.properties");
		String email = properties.getProperty("emailAddress");
		List<String> emails = new ArrayList<String>();

		Iterator<VendorCatalog> itrCatl = catalogTranslated.iterator();
		String showMsg = "0";
		while (itrCatl.hasNext()) {
			emails = new ArrayList<String>();

			// Add email from config file
			emails.add(email);

			// Get created by for catalog and add to email list
			VendorCatalog catl = (VendorCatalog) itrCatl.next();
			String userCatalog = catl.getCreatedBy();
			emails.add(userCatalog);

			Long catlId = catl.getVendorCatalogID();
			log.debug("Sending email for catalog Id:" + catlId);
			model.put("catalogId", catlId.toString());

			model.put("catalogName", catalogAndName.get(catlId));
			model.put("vendorId", catalogAndVendorId.get(catlId));
			model.put("vendorName", catalogAndVendorName.get(catlId));
			if (catalogAndErrors.containsKey(catlId) && catalogAndErrors.get(catlId).equals("NO")) {
				NotificationType type = lookupManager
				.getNotificationType(VENDOR_CATALOG_TRANSALTION_COMPLETE);
				if (systemUser == null || type == null) {
					throw new CarJobDetailException(
					"Cannot process sendVendorOpenCarNotification job. A configuration value is missing");
				}
				try {
					if ("true".equalsIgnoreCase(sendNotifications.getValue())) {
						if (log.isDebugEnabled()) {
							log.debug("Sending email for catalog Id without error:" + catlId);
						}
						// For each user in email list send email
						for (String emailNew : emails) {

							User emailUser = this.userManager.getUserByUsername(emailNew);
							
							model.put("userEmail", emailNew);
							model.put("userFirstName", emailUser.getFirstName());
							model.put("userLastName", emailUser.getLastName());
							sendEmailManager.sendNotificationEmail(type, systemUser, model);
						}
					}
				}
				catch (SendEmailException se) {
					processException("sendCatalogCompleteNotification:" + catlId, se);
					if (log.isErrorEnabled()) {
						log.error("Error when sending email to: "
								+ " catalog id=" + catlId);
					}
				}
				catch (Exception ex) {
					processException("sendCatalogCompleteNotification:" + catlId, ex);
					if (log.isErrorEnabled()) {
						log.error("General Exception occured. Cause: " + ex.getMessage());
					}
				}
			}

			else if (catalogAndErrors.containsKey(catlId)
					&& catalogAndErrors.get(catlId).equals("YES")) {
				if (log.isDebugEnabled()) {
					log.debug("Catalog contains error........" + catlId);
				}
				SortedSet<String> recNum = (SortedSet<String>) catalogAndErrorRecord2.get(catlId);

				List<String> errors = new ArrayList<String>();

				Map map = new HashMap();

				List list = new ArrayList();

				if (recNum != null) {
					if (log.isDebugEnabled() && !recNum.isEmpty()) {
						log.debug("Got the records having errors-" + recNum.size());
					}
					Iterator<String> itrRec = recNum.iterator();

					while (itrRec.hasNext()) {
						String rec = itrRec.next();
						if (log.isDebugEnabled()) {
							log.debug("Got the erreneous record-" + rec);
						}
						CompositeKeyForVndrCatRecord compKey = new CompositeKeyForVndrCatRecord(
								catlId, new Long(0), new Long(rec));
						List<String> errorList = recordAndError.get(compKey);

						for (String error : errorList) {
							//As per requirement if more than five errors exists in a catalog then show a message in email
							//that first five errors are shown in this email.
							if (list.size() < 5) {

								if (log.isDebugEnabled()) {
									log.debug("Got the error-" + error);
								}
								errors.add(error.substring(0, 50));
								map = new HashMap();
								map.put("rno", rec);
								map.put("error", error.substring(0, 700));
								list.add(map);

							}
							else {
								showMsg = "1";
								break;
							}
						}

					}

				}
				model.put("records", list);
				model.put("showMsg", showMsg);
				if (catalogAndNumberOfRecordsImported.containsKey(catlId)) {
					model.put("importedRecord", catalogAndNumberOfRecordsImported.get(catlId));
				}
				else {
					model.put("importedRecord", "");
				}

				try {
					NotificationType type1 = lookupManager
					.getNotificationType(VENDOR_CATALOG_TRANSALTION_ERROR);
					if ("true".equalsIgnoreCase(sendNotifications.getValue())) {
						if (log.isDebugEnabled()) {
							log.debug("Sending failure email for catalog Id:" + catlId);
						}
						// For each user in email list send email
						for (String emailNew : emails) {

							User emailUser = this.userManager.getUserByUsername(emailNew);
							
							model.put("userEmail", emailNew);
							model.put("userFirstName", emailUser.getFirstName());
							model.put("userLastName", emailUser.getLastName());
							sendEmailManager.sendNotificationEmail(type1, systemUser, model);
						}
					}
				}
				catch (SendEmailException se) {
					processException("sendCatalogTransaltionFailureNotification:" + catlId, se);

					if (log.isErrorEnabled()) {
						log.error("Error when sending email to:"
								+ " catalog id=" + catlId);
					}
				}
				catch (Exception ex) {

					processException("sendCatalogTransaltionFailureNotification:" + catlId, ex);

					if (log.isErrorEnabled()) {
						log.error("General Exception occured. Cause: " + ex.getMessage());
					}
				}
			}
		}
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
		Config sendNotifications = (Config) lookupManager.getById(Config.class,
				Config.SEND_EMAIL_NOTIFICATIONS);
		Config emailNotificationList = (Config) lookupManager.getById(Config.class,
				Config.SCHEDULED_PROCESS_ERROR_NOTIFICATION_LIST);

		User systemUser = this.userManager.getUserByUsername(userName.getValue());

		Map<String, String> model = new HashMap<String, String>();
		String[] emails = StringUtils.split(emailNotificationList.getValue(), ",;");
		for (String email : emails) {
			model.put("userEmail", email);
			model.put("processName", processName);
			model.put("exceptionContent", StringUtils.abbreviate(content, 4000));
			model.put("executionDate", DateUtils.formatDate(new Date(), "MM/dd/yyyy HH:mm:ss"));

			try {
				if ("true".equalsIgnoreCase(sendNotifications.getValue())) {
					sendEmailManager.sendNotificationEmail(type, systemUser, model);
				}
			}
			catch (SendEmailException se) {
				if (log.isErrorEnabled()) {
					log.error("Error when sending email to: " + email);
				}
			}
			catch (Exception ex) {
				if (log.isErrorEnabled()) {
					log.error("General Exception occured. Cause: " + email);
				}
			}
		}

	}

	/**
	 * @param records - List of VendorCatalogStyleSku
	 * @param headerNumStyle - Header number of style id field
	 * @param headerNumUPC - Header number of UPC field in catalog
	 * @param headerNumColor - Header number of Color field in catalog
	 * @param catalogId - Catalog ID
	 * @param templateId - Template ID
	 * @param templateId2 
	 * @return List<VendorCatalogStyleSku>
	 * @TODO Construct style sku model based on records,header numbers
	 */
	public List<VendorCatalogStyleSku> getStyleSkuModel(
			List<VendorCatalogRecord> records, long headerNumStyle, long headerNumUPC,
			long headerNumColor, long headerNumDesc,long headerNumStyleName, long catalogId, long templateId) {

		VendorCatalogStyleSku styleSku;
		VendorCatalogStyleSkuId compKeyStyleSku;
		List<VendorCatalogStyleSku> styleSkuList = new ArrayList<VendorCatalogStyleSku>();

		long recordNum = records.get(0).getCompositeKey().getRecordNumber();
		if (log.isDebugEnabled()) {
			log.debug("recordNum=" + recordNum);
		}
		// iterate through records to fill the model style sku
		Map<Long, List<VendorCatalogRecord>> recordMap = new HashMap<Long, List<VendorCatalogRecord>>();

		// Construct a map for records
		for (VendorCatalogRecord rec : records) {

			Long recNum = rec.getCompositeKey().getRecordNumber();
			if (recordMap.containsKey(recNum)) {
				recordMap.get(recNum).add(rec);
			}
			else {
				List<VendorCatalogRecord> recList = new ArrayList<VendorCatalogRecord>();
				recList.add(rec);
				recordMap.put(recNum, recList);
			}

		}
		Set<Long> recordNums = recordMap.keySet();
		if (log.isDebugEnabled() && !recordNums.isEmpty()) {
			log.debug("Got keyset=" + recordNums.size());
		}
		noteTextList = new ArrayList<String>();
		for (Long recNum : recordNums) {
			List<VendorCatalogRecord> recordList = recordMap.get(recNum);
			VendorStyle vendorStyleForComparison=null;
			compKeyStyleSku = new VendorCatalogStyleSkuId();
			styleSku = new VendorCatalogStyleSku();
			String styleName=null;;
			// Process the entire row for one record in catalog .
			for (VendorCatalogRecord recordTemp : recordList) {
				try {
					log.debug("recordTemp.getVendorCatalogFieldValue()============"+recordTemp.getVendorCatalogFieldValue());
					if(null!=recordTemp.getVendorCatalogFieldValue()){
						if (recordTemp.getCompositeKey().getHeaderNum() == headerNumStyle
								&& StringUtils.isNotBlank(recordTemp.getVendorCatalogFieldValue())) {
							

							compKeyStyleSku.setVendorStyleId(recordTemp
									.getVendorCatalogFieldValue().trim());
							if (log.isDebugEnabled()) {
								log.debug("In style id=" + recordTemp.getVendorCatalogFieldValue());
							}
						}
						else if (recordTemp.getCompositeKey().getHeaderNum() == headerNumUPC ) {
							compKeyStyleSku.setVendorUPC(recordTemp.getVendorCatalogFieldValue().trim());
							if (log.isDebugEnabled()) {
								log.debug("In UPC=" + recordTemp.getVendorCatalogFieldValue());
							}

						}
						else if (recordTemp.getCompositeKey().getHeaderNum() == headerNumColor ) {
							styleSku.setColor(recordTemp.getVendorCatalogFieldValue().trim());
							if (log.isDebugEnabled()) {
								log.debug("In color id=" + recordTemp.getVendorCatalogFieldValue());
							}

						}
						else if (recordTemp.getCompositeKey().getHeaderNum() == headerNumDesc) {
							styleSku.setDescription(recordTemp.getVendorCatalogFieldValue().trim());
							if (log.isDebugEnabled()  ) {
								log.debug("In desc =" + recordTemp.getVendorCatalogFieldValue());
							}

						}
						else if (recordTemp.getCompositeKey().getHeaderNum() == headerNumStyleName) {
							styleName=recordTemp.getVendorCatalogFieldValue().trim();
							if (log.isDebugEnabled()  ) {
								log.debug("In style name =" + recordTemp.getVendorCatalogFieldValue());
							}

						}
					}
				}
				catch (Exception ex) {
					StringWriter sw = new StringWriter();

					// Change the status of catalog error to yes //i.e. catalog
					// has
					// error
					if (catalogAndErrors.containsKey(catalogId)) {
						catalogAndErrors.remove(catalogId);
					}
					catalogAndErrors.put(catalogId, "YES");
					// Get erroneous record list for catalog
					if (catalogAndErrorRecord.containsKey(catalogId)) {
						
						catalogAndErrorRecord.get(catalogId).add(recNum.toString());
						log.debug("Catalog exists----" + catalogAndErrorRecord.keySet().size());
					}
					else {
						SortedSet<String> errorRecordNums = new TreeSet<String>();
						errorRecordNums.add(recNum.toString());
						catalogAndErrorRecord.put(catalogId, errorRecordNums);
						log.debug("Catalog not exists----" + catalogAndErrorRecord.keySet().size());
					}

					ex.printStackTrace(new PrintWriter(sw));
					// Put the error related to record in catalog to map
					CompositeKeyForVndrCatRecord compKey = new CompositeKeyForVndrCatRecord(
							catalogId, new Long(0), recNum);
					if (recordAndError.containsKey(compKey)) {
						recordAndError.get(compKey).add(sw.toString());
					}
					else {
						List<String> errorRecordNums = new ArrayList<String>();
						errorRecordNums.add(sw.toString());
						recordAndError.put(compKey, errorRecordNums);
					}
					if (log.isErrorEnabled()) {
						log.error("Error----" + sw.toString());
					}
					noteTextList
					.add("Error while inserting  record in style sku table ,record number -"
							+ recNum + " \nError is-  " + sw.toString().substring(0,40));
					continue;
				}
			}

			try {
				compKeyStyleSku.setVendorCatalogId(catalogId);

				//Validate vendor UPC.
				//1- Vendor UPC must be numeric
				//3-Vendor UPC should be made 0 if it is null in record
				//4-If vendor UPC is less than 12 digits in length then append 0 in front
				if (compKeyStyleSku.getVendorUPC() == null) {
					if (log.isDebugEnabled()) {
						log.debug("vendor UPC is null  .. Settitng zero");
					}
					compKeyStyleSku.setVendorUPC("0");
				}
				else if(!StringUtils.isNumeric(compKeyStyleSku.getVendorUPC())){
					if (log.isDebugEnabled()) {
						log.debug("Vendor upc is not numeric..............");
					}
					throw new NullPointerException(
							"Required data not supplied.\n"
							+ "Vendor UPC is not numeric. Vendor UPC must be a numeric field.");
				}
				else if(StringUtils.isNumeric(compKeyStyleSku.getVendorUPC())){

					String stringUpc = String.format("%013d", Long.parseLong(compKeyStyleSku.getVendorUPC()));
					compKeyStyleSku.setVendorUPC(stringUpc);
				}


				Long departmentId =0L;
				styleSku.setCompositeKey(compKeyStyleSku);
				VendorCatalog catlog=(VendorCatalog) dropshipManager.getById(VendorCatalog.class, catalogId);
				VendorStyle vendorStyle = (VendorStyle) dropshipManager.getStyleByNumber(catlog.getVendor(),
						styleSku.getCompositeKey().getVendorStyleId());

				if (null == vendorStyle) {
					log.debug("Vendor style number does not exixts in databse, vendor style number="+styleSku.getCompositeKey().getVendorStyleId());
					throw new NullPointerException(
							"Required data not supplied.\n"
							+ "The row did not contain a value for the vendor style # or Vendor Style Description field");
				}
				else{

					if (null == styleSku.getDescription()
							|| StringUtils.isBlank(styleSku.getDescription())) {
						log.debug("Description is not provided");
						throw new NullPointerException(
								"Required data not supplied.\n"
								+ "The row did not contain a value for the vendor style # or Vendor Style Description field");
					}

					// Added code By Priyanka
					// Code for overriding style name and description from catalog.
					vendorStyle=(VendorStyle) dropshipManager.getById(VendorStyle.class, vendorStyle.getVendorStyleId());

					if( null!=styleName || !StringUtils.isBlank(styleName) ){
						vendorStyle.setVendorStyleName(styleName);
					}
					vendorStyle.setDescr(styleSku.getDescription());


					departmentId = dropshipManager.getDepartmentIdForVendorStyle(vendorStyle);
					if (log.isDebugEnabled()) {
						log.debug("department id=" + departmentId);
					}
					styleSku.setDepartmentId(departmentId);
					styleSku.setRecordNum(recNum);
					styleSku.setUpdatedBy(catlog.getUpdatedBy());
					styleSku.setLockedBy(catlog.getUpdatedBy());
					styleSku.setCreatedBy(catlog.getCreatedBy());
					styleSkuList.add(styleSku);
					dropshipManager.saveStyle(vendorStyle);
				}
			}
			catch (Exception ex) {
				StringWriter sw = new StringWriter();
				// Change the status of catalog error to yes //i.e. catalog has
				// error
				if (catalogAndErrors.containsKey(catalogId)) {
					catalogAndErrors.remove(catalogId);
				}
				catalogAndErrors.put(catalogId, "YES");
				// Get erroneous record list for catalog
				if (catalogAndErrorRecord.containsKey(catalogId)) {
					log.debug("Catalog exists----" + catalogAndErrorRecord.keySet().size());
					catalogAndErrorRecord.get(catalogId).add(recNum.toString());
				}
				else {
					SortedSet<String> errorRecordNums = new TreeSet<String>();

					errorRecordNums.add(recNum.toString());
					catalogAndErrorRecord.put(catalogId, errorRecordNums);
					log.debug("Catalog not exists----" + catalogAndErrorRecord.keySet().size());
				}

				ex.getMessage();
				ex.printStackTrace(new PrintWriter(sw));
				// Put the error related to record in catalog to map
				CompositeKeyForVndrCatRecord compKey = new CompositeKeyForVndrCatRecord(catalogId,
						new Long(0), recNum);
				if (recordAndError.containsKey(compKey)) {
					recordAndError.get(compKey).add(sw.toString());
				}
				else {
					List<String> errorRecordNums = new ArrayList<String>();
					errorRecordNums.add(sw.toString());
					recordAndError.put(compKey, errorRecordNums);
				}
				if (log.isErrorEnabled()) {
					log.error("Error----" + sw.toString());
				}
				noteTextList
				.add("Error while inserting  record in style sku table , record number -"
						+ recNum + " \nError is-  " + sw.toString().substring(0,40));
				continue;
			}

		}
		if (null == styleSkuList) {
			styleSkuList = new ArrayList<VendorCatalogStyleSku>();
		}
		return styleSkuList;
	}

	/**
	 * @param catalogs
	 * @return void
	 * @TODO Fill catalog data in VNDR_CATL_STY_SKU_MAST
	 */
	public void fillCatalogDataInMasterTable(List<VendorCatalog> catalogs) {
		List<VendorCatalog> catalogListNew = new ArrayList<VendorCatalog>();

		// For each catalog in translating status
		for (VendorCatalog catalog : catalogs) {
			noteTextList = new ArrayList<String>();

			// get import information for vendor catalog
			VendorCatalogImport catlImport = dropshipManager
			.getVendorCatalogImportForCatalog(catalog.getVendorCatalogID());
			if (catlImport == null) {
				noteTextList.add("Could not find catalog import info for catalog");
				catalog.setStatusCD("Complete");
				catalogListNew.add(catalog);
			}
			Long updateActionId = null;
			try {
				updateActionId = catlImport.getUpdateActionID().getUpdateActionID();
				if (log.isDebugEnabled()) {
					log.debug("Upadte action ID==" + updateActionId);
					log.debug("Catalog ID=" + catalog.getVendorCatalogID());
				}
				if (updateActionId.equals(new Long(UPDATE_ACTION_ID))) {
					if (log.isDebugEnabled()) {
						log.debug("Update Action Called");
					}
					getMasterRecordsForUpdateAction(catalog);

				}
				else if (updateActionId.equals(new Long(APPEND_ACTION_ID))) {
					if (log.isDebugEnabled()) {
						log.debug("Append action called");
					}
					getMasterRecordsForAppendAction(catalog);
				}
				else if (updateActionId.equals(new Long(OVERWRITE_ACTION_ID))) {
					if (log.isDebugEnabled()) {
						log.debug("Overwrite action called");
					}
					getMasterRecordsForOverwriteAction(catalog);
				}
				catalog.setStatusCD("Complete");
				catalogListNew.add(catalog);
			}
			catch (Exception ex) {
				ex.printStackTrace();

				noteTextList
				.add("Error while translating records in master table." + ex.toString().substring(0,40));
			}

			try {
				dropshipManager.saveVendorCatalogNote(catalog, noteSubject, noteTextList);
			}
			catch (ParseException e) {

				e.printStackTrace();
			}
		}
		try {
			dropshipManager.saveCatalogsToComplete(catalogListNew);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @param catalog
	 * @return List<VendorCatalogStyleSkuMaster>
	 * @Enclosing_Method getMasterRecordsForOverwriteAction
	 * @TODO Save records for catalog in master table for overwrite action
	 */
	private List<VendorCatalogStyleSkuMaster> getMasterRecordsForOverwriteAction(
			VendorCatalog catalog) {

		// Get all master records from master table

		//Shailesh -Modfied the method -
		List<VendorCatalogStyleSkuMaster> masterList = dropshipManager.getAllMasterRecords(catalog.getVendor().getVendorId());
		List<VendorCatalogStyleSkuMaster> masterListNew = new ArrayList<VendorCatalogStyleSkuMaster>();
		Iterator<VendorCatalogStyleSkuMaster> itr = masterList.iterator();
		List<VendorCatalogStyleSku> tempStyleSkuList=null;
		// Make each record as inactive
		try {
			while (itr.hasNext()) {
				VendorCatalogStyleSkuMaster master = (VendorCatalogStyleSkuMaster) itr.next();
				master.setStatus("INACTIVE");
				masterListNew.add(master);

				//Shailesh Added following lines for updeting status field in VendorCatalogStyleSku Table.
				VendorCatalogStyleSkuId catalogStyleSkuId = new VendorCatalogStyleSkuId(master.getVendorCatalogId(),
						master.getCompositeKey().getVendorStyleId(),master.getCompositeKey().getVendorUPC());

				VendorCatalogStyleSku catalogStyleSku = (VendorCatalogStyleSku) dropshipManager.getById(VendorCatalogStyleSku.class, catalogStyleSkuId);
				catalogStyleSku.setStatus("INACTIVE");
				tempStyleSkuList =  new ArrayList<VendorCatalogStyleSku>();
				tempStyleSkuList.add(catalogStyleSku);
				//End code of updating status field in VendorCatalogStyleSku Table.
			}

			// Update the inactivated records in master table again.
			dropshipManager.saveOrUpdateMasterList(masterListNew);

			//Inactivate the vendor styles from VendorCatalogStyleSku table.
			dropshipManager.saveStyleSkus(tempStyleSkuList);
		} catch(Exception e) {

		}
		List<VendorCatalogStyleSku> styleSkuList = dropshipManager
		.getStyelSkusWithCatalogId(catalog.getVendorCatalogID());
		VendorCatalogStyleSkuMasterId compKeyMaster;
		List<VendorCatalogStyleSkuMaster> tempMasterList = new ArrayList<VendorCatalogStyleSkuMaster>();

		// For each style sku for catalog
		// Construct the master model and save to databse.
		for (VendorCatalogStyleSku styelSku : styleSkuList) {
			try {
				VendorCatalogStyleSkuMaster newMaster = new VendorCatalogStyleSkuMaster();
				compKeyMaster = new VendorCatalogStyleSkuMasterId(styelSku.getCompositeKey()
						.getVendorStyleId(), styelSku.getCompositeKey().getVendorUPC(), catalog
						.getVendor().getVendorId());

				newMaster.setColor(styelSku.getColor());
				newMaster.setCompositeKey(compKeyMaster);
				newMaster.setStatus("ACTIVE");
				newMaster.setVendorCatalogId(catalog.getVendorCatalogID());
				newMaster.setUpdatedDate(new Date());
				//newMaster.setCreatedBy(catalog.getCreatedBy());
				//newMaster.setUpdatedBy(catalog.getUpdatedBy());
				tempMasterList.add(newMaster);
			}
			catch (Exception ex) {
				StringWriter sw = new StringWriter();
				// Change the status of catalog error to yes //i.e. catalog has
				// error
				if (catalogAndErrors.containsKey(styelSku.getCompositeKey().getVendorCatalogId())) {
					catalogAndErrors.remove(styelSku.getCompositeKey().getVendorCatalogId());
				}
				catalogAndErrors.put(styelSku.getCompositeKey().getVendorCatalogId(), "YES");
				// Get erroneous record list for catalog
				if (catalogAndErrorRecord.containsKey(styelSku.getCompositeKey()
						.getVendorCatalogId())) {
					catalogAndErrorRecord.get(styelSku.getCompositeKey().getVendorCatalogId()).add(
							styelSku.getRecordNum().toString());
				}
				else {
					SortedSet<String> errorRecordNums = new TreeSet<String>();
					errorRecordNums.add(styelSku.getRecordNum().toString());
					catalogAndErrorRecord.put(styelSku.getCompositeKey().getVendorCatalogId(),
							errorRecordNums);
				}

				ex.printStackTrace(new PrintWriter(sw));
				// Put the error related to record in catalog to map
				CompositeKeyForVndrCatRecord compKey = new CompositeKeyForVndrCatRecord(styelSku
						.getCompositeKey().getVendorCatalogId(), new Long(0), styelSku
						.getRecordNum());
				if (recordAndError.containsKey(compKey)) {
					recordAndError.get(compKey).add(sw.toString());
				}
				else {
					List<String> errorRecordNums = new ArrayList<String>();
					errorRecordNums.add(sw.toString());
					recordAndError.put(compKey, errorRecordNums);
				}
				if (log.isErrorEnabled()) {
					log.error("Error----" + sw);
				}
				noteTextList.add("Error while inserting record in master table-"
						+ styelSku.getRecordNum() + " \nError is-  " + sw.toString().substring(0,40));
				continue;
			}
		}
		dropshipManager.saveOrUpdateMasterList(tempMasterList);
		return null;
	}

	/**
	 * @param catalog
	 * @return List<VendorCatalogStyleSkuMaster>
	 * @Enclosing_Method getMasterRecordsForUpdateAction
	 * @TODO Save records for catalog in master table for update action
	 */
	private List<VendorCatalogStyleSkuMaster> getMasterRecordsForUpdateAction(VendorCatalog catalog) {

		/**
		 * Get styles skus for catalog in VENDOR_CATALOG_STY_SKU table
		 */
		List<VendorCatalogStyleSku> styleSkuList = dropshipManager
		.getStyelSkusWithCatalogId(catalog.getVendorCatalogID());
		VendorCatalogStyleSkuMasterId compKeyMaster;

		/**
		 * For each style SKU do-
		 */
		for (VendorCatalogStyleSku styelSku : styleSkuList) {

			VendorCatalogStyleSkuMaster master = new VendorCatalogStyleSkuMaster();

			// Construct Id for Master table
			VendorCatalogStyleSkuMasterId masterId = new VendorCatalogStyleSkuMasterId(styelSku
					.getCompositeKey().getVendorStyleId(), styelSku.getCompositeKey()
					.getVendorUPC(), catalog.getVendor().getVendorId());

			master = (VendorCatalogStyleSkuMaster) dropshipManager.getById(
					VendorCatalogStyleSkuMaster.class, masterId);
			List<VendorCatalogStyleSkuMaster> tempMasterList = null;
			List<VendorCatalogStyleSku> tempStyleSkuList=null;
			
			// If new style UPC
			// Save to database
			// else update the existing record in master table
			try {
				if (master != null) {
					// Make original master record as inactive
					if (log.isDebugEnabled()) {
						log.debug("Not a new style id and vendor upc .....master key"
								+ master.getVendorCatalogId());
						log.debug("catalog.getVendorCatalogID()=" + catalog.getVendorCatalogID());
					}

					//Shailesh Added following lines for updeting status field in VendorCatalogStyleSku Table.
					VendorCatalogStyleSkuId catalogStyleSkuId = new VendorCatalogStyleSkuId(master.getVendorCatalogId(),
							master.getCompositeKey().getVendorStyleId(),master.getCompositeKey().getVendorUPC());

					VendorCatalogStyleSku catalogStyleSku = (VendorCatalogStyleSku) dropshipManager.getById(VendorCatalogStyleSku.class, catalogStyleSkuId);
					catalogStyleSku.setStatus("INACTIVE");
					tempStyleSkuList =  new ArrayList<VendorCatalogStyleSku>();
					tempStyleSkuList.add(catalogStyleSku);
					dropshipManager.saveStyleSkus(tempStyleSkuList);

					//End code of updating status field in VendorCatalogStyleSku Table.
					master.setColor(styelSku.getColor());
					master.setStatus("ACTIVE");
					master.setVendorCatalogId(catalog.getVendorCatalogID());
					//master.setCreatedBy(catalog.getCreatedBy());
					//master.setUpdatedBy(catalog.getUpdatedBy());
					master.setUpdatedDate(new Date());
					tempMasterList = new ArrayList<VendorCatalogStyleSkuMaster>();
					tempMasterList.add(master);
					dropshipManager.saveOrUpdateMasterList(tempMasterList);

					continue;
				}

				else {
					if (log.isDebugEnabled()) {
						log.debug("New catalog entry...............");
					}
					tempMasterList = new ArrayList<VendorCatalogStyleSkuMaster>();
					VendorCatalogStyleSkuMaster newMaster = new VendorCatalogStyleSkuMaster();
					compKeyMaster = new VendorCatalogStyleSkuMasterId(styelSku.getCompositeKey()
							.getVendorStyleId(), styelSku.getCompositeKey().getVendorUPC(), catalog
							.getVendor().getVendorId());

					newMaster.setColor(styelSku.getColor());
					newMaster.setCompositeKey(compKeyMaster);
					newMaster.setStatus("ACTIVE");
					newMaster.setUpdatedDate(new Date());
					//newMaster.setCreatedBy(catalog.getCreatedBy());
					//newMaster.setUpdatedBy(catalog.getUpdatedBy());
					if (log.isDebugEnabled()) {
						log.debug("catalog.getVendorCatalogID()=" + catalog.getVendorCatalogID());
					}
					newMaster.setVendorCatalogId(catalog.getVendorCatalogID());
					tempMasterList.add(newMaster);
					
					dropshipManager.saveOrUpdateMasterList(tempMasterList);
				}
			}
			catch (Exception ex) {
				StringWriter sw = new StringWriter();
				// Change the status of catalog error to yes //i.e. catalog has
				// error
				if (catalogAndErrors.containsKey(styelSku.getCompositeKey().getVendorCatalogId())) {
					catalogAndErrors.remove(styelSku.getCompositeKey().getVendorCatalogId());
				}
				catalogAndErrors.put(styelSku.getCompositeKey().getVendorCatalogId(), "YES");
				// Get erroneous record list for catalog
				if (catalogAndErrorRecord.containsKey(styelSku.getCompositeKey()
						.getVendorCatalogId())) {
					catalogAndErrorRecord.get(styelSku.getCompositeKey().getVendorCatalogId()).add(
							styelSku.getRecordNum().toString());
				}
				else {
					SortedSet<String> errorRecordNums = new TreeSet<String>();
					errorRecordNums.add(styelSku.getRecordNum().toString());
					catalogAndErrorRecord.put(styelSku.getCompositeKey().getVendorCatalogId(),
							errorRecordNums);
				}

				ex.printStackTrace(new PrintWriter(sw));
				// Put the error related to record in catalog to map
				CompositeKeyForVndrCatRecord compKey = new CompositeKeyForVndrCatRecord(styelSku
						.getCompositeKey().getVendorCatalogId(), new Long(0), styelSku
						.getRecordNum());
				if (recordAndError.containsKey(compKey)) {
					recordAndError.get(compKey).add(sw.toString());
				}
				else {
					List<String> errorRecordNums = new ArrayList<String>();
					errorRecordNums.add(sw.toString());
					recordAndError.put(compKey, errorRecordNums);
				}
				if (log.isErrorEnabled()) {
					log.error("Error----" + sw.toString());
				}
				noteTextList.add("Error while inserting record in master table-"
						+ styelSku.getRecordNum() + " \nError is-  " + sw.toString().substring(0,40));
				continue;
			}

		}
		return null;
	}

	/**
	 * @param catalog
	 * @return
	 * @return List<VendorCatalogStyleSkuMaster>
	 * @Enclosing_Method getMasterRecordsForAppendAction
	 * @TODO Save records for catalog in master table for append action
	 */
	private List<VendorCatalogStyleSkuMaster> getMasterRecordsForAppendAction(VendorCatalog catalog) {

		List<VendorCatalogStyleSku> styleSkuList = dropshipManager
		.getStyelSkusWithCatalogId(catalog.getVendorCatalogID());
		VendorCatalogStyleSkuMasterId compKeyMaster;
		for (VendorCatalogStyleSku styelSku : styleSkuList) {

			VendorCatalogStyleSkuMasterId masterId = new VendorCatalogStyleSkuMasterId(styelSku
					.getCompositeKey().getVendorStyleId(), styelSku.getCompositeKey()
					.getVendorUPC(), catalog.getVendor().getVendorId());
			VendorCatalogStyleSkuMaster master = (VendorCatalogStyleSkuMaster) dropshipManager
			.getById(VendorCatalogStyleSkuMaster.class, masterId);
			try {
				if (master != null) {
					continue;
				}
				else {
					List<VendorCatalogStyleSkuMaster> tempMasterList = new ArrayList<VendorCatalogStyleSkuMaster>();
					VendorCatalogStyleSkuMaster newMaster = new VendorCatalogStyleSkuMaster();
					compKeyMaster = new VendorCatalogStyleSkuMasterId(styelSku.getCompositeKey()
							.getVendorStyleId(), styelSku.getCompositeKey().getVendorUPC(), catalog
							.getVendor().getVendorId());

					newMaster.setColor(styelSku.getColor());
					newMaster.setCompositeKey(compKeyMaster);
					newMaster.setStatus("ACTIVE");
					newMaster.setVendorCatalogId(catalog.getVendorCatalogID());
					newMaster.setUpdatedDate(new Date());
					//newMaster.setCreatedBy(catalog.getCreatedBy());
					//newMaster.setUpdatedBy(catalog.getUpdatedBy());
					tempMasterList.add(newMaster);

					dropshipManager.saveOrUpdateMasterList(tempMasterList);
				}
			}
			catch (Exception ex) {
				StringWriter sw = new StringWriter();
				// Change the status of catalog error to yes //i.e. catalog has
				// error
				if (catalogAndErrors.containsKey(styelSku.getCompositeKey().getVendorCatalogId())) {
					catalogAndErrors.remove(styelSku.getCompositeKey().getVendorCatalogId());
				}
				catalogAndErrors.put(styelSku.getCompositeKey().getVendorCatalogId(), "YES");
				// Get erroneous record list for catalog
				if (catalogAndErrorRecord.containsKey(styelSku.getCompositeKey()
						.getVendorCatalogId())) {
					catalogAndErrorRecord.get(styelSku.getCompositeKey().getVendorCatalogId()).add(
							styelSku.getRecordNum().toString());
				}
				else {
					SortedSet<String> errorRecordNums = new TreeSet<String>();
					errorRecordNums.add(styelSku.getRecordNum().toString());
					catalogAndErrorRecord.put(styelSku.getCompositeKey().getVendorCatalogId(),
							errorRecordNums);
				}
				ex.printStackTrace(new PrintWriter(sw));
				// Put the error related to record in catalog to map
				CompositeKeyForVndrCatRecord compKey = new CompositeKeyForVndrCatRecord(styelSku
						.getCompositeKey().getVendorCatalogId(), new Long(0), styelSku
						.getRecordNum());
				if (recordAndError.containsKey(compKey)) {
					recordAndError.get(compKey).add(sw.toString());
				}
				else {
					List<String> errorRecordNums = new ArrayList<String>();
					errorRecordNums.add(sw.toString());
					recordAndError.put(compKey, errorRecordNums);
				}
				if (log.isErrorEnabled()) {
					log.error("Error----" + sw.toString());
				}
				noteTextList.add("Error whilde inserting record in master table-"
						+ styelSku.getRecordNum() + " \nError is-  " + sw.toString().substring(0,40));
				continue;
			}
		}

		return null;
	}

}