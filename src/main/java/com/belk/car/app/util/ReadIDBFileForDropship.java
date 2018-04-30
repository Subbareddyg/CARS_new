
package com.belk.car.app.util;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.dao.DropshipDao;
import com.belk.car.app.dao.FulfillmentServiceDao;
import com.belk.car.app.exceptions.SendEmailException;
import com.belk.car.app.model.Config;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.model.oma.ItemRequest;
import com.belk.car.app.model.oma.ItemSource;
import com.belk.car.app.model.oma.StylePopulationMethod;
import com.belk.car.app.model.oma.VFIRStatus;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.DropshipManager;
import com.belk.car.app.service.EmailManager;
import com.belk.car.app.service.FulfillmentServiceManager;
import com.belk.car.app.to.IdbDropshipDataTO;
import com.belk.car.util.DateUtils;

/**
 * Class with method to read the IDB Feed file
 * 
 * @author afusy45
 */
public class ReadIDBFileForDropship implements DropShipConstants {

	private static final String IDB_FEED_DESC = "IDB Feed";
	private DropshipManager dropshipManager;
	private DropshipDao dropshipDao;
	private FulfillmentServiceDao fulfillmentServiceDao;
	private FulfillmentServiceManager fulfillmentServiceManager;
	//private EmailManager emailManager;
	private User user = new User();
	private CarLookupManager lookupManager;
	private EmailManager sendEmailManager;
	private UserManager userManager;
	
	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
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

	public DropshipDao getDropshipDao() {
		return dropshipDao;
	}

	public void setDropshipDao(DropshipDao dropshipDao) {
		this.dropshipDao = dropshipDao;
	}

	/**
	 * @return the fulfillmentServiceManager
	 */
	public FulfillmentServiceManager getFulfillmentServiceManager() {
		return fulfillmentServiceManager;
	}

	/**
	 * @param fulfillmentServiceManager the fulfillmentServiceManager to set
	 */
	public void setFulfillmentServiceManager(FulfillmentServiceManager fulfillmentServiceManager) {
		this.fulfillmentServiceManager = fulfillmentServiceManager;
	}

	/**
	 * @return the fulfillmentServiceDao
	 */
	public FulfillmentServiceDao getFulfillmentServiceDao() {
		return fulfillmentServiceDao;
	}

	/**
	 * @param fulfillmentServiceDao the fulfillmentServiceDao to set
	 */
	public void setFulfillmentServiceDao(FulfillmentServiceDao fulfillmentServiceDao) {
		this.fulfillmentServiceDao = fulfillmentServiceDao;
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

	/* Set start and end limit for each field */
	public static final int STARTVNDRNBR = 0;
	public static final int ENDVNDRNBR = STARTVNDRNBR + 7;

	public static final int STARTVNDRNAME = ENDVNDRNBR;
	public static final int ENDVNDRNAME = STARTVNDRNAME + 30;

	public static final int STARTDPTNBR = ENDVNDRNAME;
	public static final int ENDDPTNBR = STARTDPTNBR + 3;

	public static final int STARTDEPTNAME = ENDDPTNBR;
	public static final int ENDDEPTNAME = STARTDEPTNAME + 25;

	public static final int STARTCLASSNBR = ENDDEPTNAME;
	public static final int ENDCLASSNBR = STARTCLASSNBR + 4;

	public static final int STARTCLASSNAME = ENDCLASSNBR;
	public static final int ENDCLASSNAME = STARTCLASSNAME + 30;

	public static final int STARTVNDRSTYLE = ENDCLASSNAME;
	public static final int ENDVNDRSTYLE = STARTVNDRSTYLE + 20;

	public static final int STARTSTYLEDESC = ENDVNDRSTYLE;
	public static final int ENDSTYLEDESC = STARTSTYLEDESC + 20;

	public static final int STARTVNDRCOLOR = ENDSTYLEDESC;
	public static final int ENDVNDRCOLOR = STARTVNDRCOLOR + 3;

	public static final int STARTVNDRCOLORNAME = ENDVNDRCOLOR;
	public static final int ENDVNDRCOLORNAME = STARTVNDRCOLORNAME + 10;

	public static final int STARTVNDRSZCODE = ENDVNDRCOLORNAME;
	public static final int ENDVNDRSZCODE = STARTVNDRSZCODE + 5;

	public static final int STARTVNDRSZDESC = ENDVNDRSZCODE;
	public static final int ENDVNDRSZDESC = STARTVNDRSZDESC + 20;

	public static final int STARTVENDORUPC = ENDVNDRSZDESC;
	public static final int ENDVENDORUPC = STARTVENDORUPC + 13;

	public static final int STARTUPCADDDATE = ENDVENDORUPC;
	public static final int ENDUPCADDDATE = STARTUPCADDDATE + 10;

	public static final int STARTIDBUPCUNITPRICE = ENDUPCADDDATE;
	public static final int ENDIDBUPCUNITPRICE = STARTIDBUPCUNITPRICE + 11;

	public static final int STARTIDBUPCUNITCOST = ENDIDBUPCUNITPRICE;
	public static final int ENDIDBUPCUNITCOST = STARTIDBUPCUNITCOST + 11;

	public static final int STARTIDBSTYLEUNITPRICE = ENDIDBUPCUNITCOST;
	public static final int ENDIDBSTYLEUNITPRICE = STARTIDBSTYLEUNITPRICE + 11;

	public static final int STARTIDBSTYLEUNITCOST = ENDIDBSTYLEUNITPRICE;
	public static final int ENDIDBSTYLEUNITCOST = STARTIDBSTYLEUNITCOST + 11;

	public static final int STARTSZCHARTCODE = ENDIDBSTYLEUNITCOST;
	public static final int ENDSZCHARTCODE = STARTSZCHARTCODE + 3;

	public static final int STARTBELKUPC = ENDSZCHARTCODE;
	public static final int ENDBELKUPC = STARTBELKUPC + 13;

	public static final int STARTSTYLEDRPSHIPFLG = ENDBELKUPC;
	public static final int ENDSTYLEDRPSHIPFLG = STARTSTYLEDRPSHIPFLG + 1;

	public static final int STARTSKUDRPSHIPFLG = ENDSTYLEDRPSHIPFLG;
	public static final int ENDSKUDRPSHIPFLG = STARTSKUDRPSHIPFLG + 1;

	public static final int STARTDATEFLGUPDATED = ENDSKUDRPSHIPFLG;
	public static final int ENDDATEFLGUPDATED = STARTDATEFLGUPDATED + 26;

	public static final int STARTNOTORDERABLEFLG = ENDDATEFLGUPDATED;
	public static final int ENDNOTORDERABLEFLG = STARTNOTORDERABLEFLG + 1;

	public static final int STARTUPCDISCFLG = ENDNOTORDERABLEFLG;
	public static final int ENDUPCDISCFLG = STARTUPCDISCFLG + 1;

	public static final int STARTUPCDISCDATE = ENDUPCDISCFLG;
	public static final int ENDUPCDISCDATE = STARTUPCDISCDATE + 10;

	public static final int STARTSOURCEOFCODE = ENDUPCDISCDATE;
	public static final int ENDSOURCEOFCODE = STARTSOURCEOFCODE + 8;

	public static final int START_FILLER = ENDSOURCEOFCODE;
	public static final int END_FILLER = START_FILLER + 16;

	private static transient final Log log = LogFactory.getLog(ReadIDBFileForDropship.class);

	/**
	 * Method to process the IDB Feed file row by row and set the values in
	 * IdbDropshipDataTO model
	 * 
	 * @param fl
	 * @return List
	 * @throws Exception
	 */

	@SuppressWarnings("unchecked")
	public List<IdbDropshipDataTO> process(File fl)
	throws Exception {
		log.debug("inside process method for IDB Feed..");

		ItemRequest itemRequest = new ItemRequest();

		/* get source IDs */
		ItemSource IDBFeedsourceID = fulfillmentServiceDao.getSourceForItemRequest(IDB_FEED_SOURCE);
		ItemSource IDBCostChangeID = fulfillmentServiceDao
		.getSourceForItemRequest(IDB_COST_CHANGE_SOURCE);

		/* get Style Population Method */
		StylePopulationMethod populationMethod = fulfillmentServiceDao
		.getStyleForItemRequest(DROP_SHIP_IN_IDB_NOT_IN_ECOMMERCE);

		/* get Workflow status code */
		VFIRStatus workflowStatus = fulfillmentServiceDao.getStatusForItemRequest(VFIRStatus.OPEN);

		if (log.isInfoEnabled()) {
			log.info("Opened Path");
		}

		/* Reads line by line into List */

		List list = FileUtils.readLines(fl, UTF_8);

		/* HashMap to store the data came from Feed file */
		HashMap<String, IdbDropshipDataTO> vendorUPCsFeed = new HashMap<String, IdbDropshipDataTO>();

		

		IdbDropshipDataTO idb = null;
		/* Iterator to iterate over the list */
		java.util.Iterator it = list.iterator();

		List resultList = new ArrayList();
		SimpleDateFormat dateFormat = new SimpleDateFormat(MM_DD_YYYY);
		Date newDate = new Date();

		try {
			while (it.hasNext()) {

				String str = (String) it.next();

				/*
				 * Check if field is empty or has less than 5 characters then
				 * jump to next row
				 */
				if (str == null || str.length() < 5) {
					log.debug("jump to next row....");
					continue;
				}

				/* Read the values of each row into a string */
				String vendorNumber = str.substring(STARTVNDRNBR, ENDVNDRNBR).trim();
				String vendorName = str.substring(STARTVNDRNAME, ENDVNDRNAME).trim();

				String deptNbr = str.substring(STARTDPTNBR, ENDDPTNBR).trim();
				String deptName = str.substring(STARTDEPTNAME, ENDDEPTNAME).trim();
				String classNumber = str.substring(STARTCLASSNBR, ENDCLASSNBR).trim();
				String className = str.substring(STARTCLASSNAME, ENDCLASSNAME).trim();
				String vendorStyle = str.substring(STARTVNDRSTYLE, ENDVNDRSTYLE).trim();
				String vendorStyleDesc = str.substring(STARTSTYLEDESC, ENDSTYLEDESC).trim();
				String vendorColor = str.substring(STARTVNDRCOLOR, ENDVNDRCOLOR).trim();

				String vendorColorName = str.substring(STARTVNDRCOLORNAME, ENDVNDRCOLORNAME).trim();

				String vendorSizeCode = str.substring(STARTVNDRSZCODE, ENDVNDRSZCODE).trim();

				String vendorSizeDesc = str.substring(STARTVNDRSZDESC, ENDVNDRSZDESC).trim();

				String vendorUPC = str.substring(STARTVENDORUPC, ENDVENDORUPC).trim();

				String upcAddDate = str.substring(STARTUPCADDDATE, ENDUPCADDDATE).trim();

				String idbUPCUnitPrice = str.substring(STARTIDBUPCUNITPRICE, ENDIDBUPCUNITPRICE)
				.trim();

				String idbUPCUnitCost = str.substring(STARTIDBUPCUNITCOST, ENDIDBUPCUNITCOST)
				.trim();

				String idbStyleUnitPrice = str.substring(STARTIDBSTYLEUNITPRICE,
						ENDIDBSTYLEUNITPRICE).trim();

				String idbStyleUnitCost = str.substring(STARTIDBSTYLEUNITCOST, ENDIDBSTYLEUNITCOST)
				.trim();

				String sizeChartCode = str.substring(STARTSZCHARTCODE, ENDSZCHARTCODE).trim();

				String belkUPC = str.substring(STARTBELKUPC, ENDBELKUPC).trim();

				String styleDropshipFlag = str.substring(STARTSTYLEDRPSHIPFLG, ENDSTYLEDRPSHIPFLG)
				.trim();
				String skuDropshipFlag = str.substring(STARTSKUDRPSHIPFLG, ENDSKUDRPSHIPFLG).trim();

				String dateFlagUpdated = str.substring(STARTDATEFLGUPDATED, ENDDATEFLGUPDATED)
				.trim();

				String notOrderableFlag = str.substring(STARTNOTORDERABLEFLG, ENDNOTORDERABLEFLG)
				.trim();

				String upcDiscountedFlag = str.substring(STARTUPCDISCFLG, ENDUPCDISCFLG).trim();

				String upcDiscountedDate = str.substring(STARTUPCDISCDATE, ENDUPCDISCDATE).trim();

				

				/* Set the string values in IDBDropshipDataTo object */
				idb = new IdbDropshipDataTO();
				idb.setClassName(className);
				idb.setClassNum(Long.valueOf(classNumber));
				idb.setDeptName(deptName);
				idb.setDeptNum(Long.valueOf(deptNbr));
				idb.setVendorName(vendorName);
				idb.setVendorNumber(vendorNumber);
				idb.setVendorStyleID(vendorStyle);
				idb.setBelkUPC(belkUPC);
				idb.setStyleDesc(vendorStyleDesc);
				idb.setVendorColorCode(vendorColor);
				idb.setColorDesc(vendorColorName);
				idb.setVndrSizeCode(vendorSizeCode);
				idb.setVndrSize(vendorSizeDesc);
				idb.setVndrUPC(vendorUPC);
				idb.setUPCAddDate(dateFormat.parse(upcAddDate));
				idb.setIDBUPCUnitPrice(Double.valueOf(idbUPCUnitPrice));
				idb.setIDBUPCUnitCost(Double.valueOf(idbUPCUnitCost));
				idb.setIDBStyleUnitPrice(Double.valueOf(idbStyleUnitPrice));
				idb.setIDBStyleUnitCost(Double.valueOf(idbStyleUnitCost));
				idb.setSizeChartCode(sizeChartCode);
				idb.setStyleDropshipFlag(styleDropshipFlag);
				idb.setSKUDropshipFlag(skuDropshipFlag);

				// Need to read the date in the format it is sent
				DateFormat formatter = new SimpleDateFormat(YYYY_MM_DD_FORMAT);
				newDate = formatter.parse(dateFlagUpdated);
				String currentDate1 = dateFormat.format(newDate);
				idb.setDropshipFlagUpdated(dateFormat.parse(currentDate1));
				idb.setNotOrderableFlag(notOrderableFlag);
				idb.setUPCDiscountedFlag(upcDiscountedFlag);
				idb.setUPCDiscountedDate(dateFormat.parse(upcDiscountedDate));

				vendorUPCsFeed.put(vendorUPC, idb);
				resultList.add(idb);

			}// End of while loop
		}
		catch (Exception ex) {
			ex.printStackTrace();
			// Send Notification on Exception to People in the List...
			processException("Import IDB Feed", ex);
		}
		Set set = vendorUPCsFeed.keySet();

		/* Method to convert set values to comma seperated strind buffer */
		log.debug("Set values before sending to method" + set);

		// String venUPCs = readValuesFromSet(set);

		/* Get IDBDropShipDataTo object mapping to vendorUPCs got from feed */
		List<IdbDropshipDataTO> idb_existing_vendorUPCs = new ArrayList<IdbDropshipDataTO>();
		if (set != null && !set.isEmpty()) {
			idb_existing_vendorUPCs = dropshipManager.getVendorUpcs(set);
		}
		
		/*
		 * Check if records found in idb_feed table matching to New feed
		 * compared by vendorUPC
		 */
		if (idb_existing_vendorUPCs != null && !idb_existing_vendorUPCs.isEmpty()) {
			log.debug("Records found in idb_feed table matching the vendorUPCs got from feed");

			IdbDropshipDataTO idbForExistingVenUPC = null;
			String vendorUPC = "";
			for (int i = 0; i < idb_existing_vendorUPCs.size(); i++) {
				log.info("inside for loop over the matching vendorUPCs..class.."
						+ (idb_existing_vendorUPCs.get(0)).getClass());
				idbForExistingVenUPC = (idb_existing_vendorUPCs.get(i));

				vendorUPC = idbForExistingVenUPC.getVndrUPC();
				if (vendorUPCsFeed.containsKey(vendorUPC)) {
					log
					.debug("Inside if feed contains matching UPCs in idb_feed table-->Just for cross check");

					/*
					 * Check if vendor number got from feed is present in VENDOR
					 * table
					 */

					Vendor venObj = fulfillmentServiceDao
					.getVendorForItemRequest(idbForExistingVenUPC.getVendorNumber());
					log.info("Got vendor Object");
					/*
					 * Check if vendor object is not null-->If not null then
					 * only proceed with entries into other tables
					 */
					if (venObj != null) {

						/*
						 * logic to get fulfillmentservice IDS for specific
						 * vendor using vendor id from vendor object
						 */
						log.debug("If Vendor Object is not null");
						List<FulfillmentServiceVendor> fulfillmentServiceID = dropshipManager
						.getFulfillmentServiceIdForVenID(venObj.getVendorId());

						FulfillmentServiceVendor vfServiceID = null;
						if (fulfillmentServiceID != null && !fulfillmentServiceID.isEmpty()) {
							for (int f = 0; f < fulfillmentServiceID.size(); f++) {
								log.debug("inside for loop for fulfillment service IDs....");
								
								// check if cost change
								if ((vendorUPCsFeed.get(vendorUPC).getIDBUPCUnitCost()).equals(idbForExistingVenUPC
										.getIDBUPCUnitCost())) {
									log.debug("if no cost change....");
									itemRequest.setItemSource(IDBFeedsourceID);
									// /do nothing

								}
								else {
									// idb.setIDBStyleUnitCost(Double.valueOf(idbStyleUnitCost));
									log.debug("there is cost change....");
									itemRequest.setItemSource(IDBCostChangeID);
								}
								// check if styleDropship flag is Yes when
								// vendorUPC is already existing(old)
								log.info("idbForExistingVenUPC.getStyleDropshipFlag()"+idbForExistingVenUPC.getStyleDropshipFlag()+"for style number"+idbForExistingVenUPC.getVendorStyleID()+"And vendor UPC"+idbForExistingVenUPC.getVndrUPC());
							if(idbForExistingVenUPC.getStyleDropshipFlag() == null){
								log.debug("Since style dropship flag is null feed is invalid");
							}
							else if ((idbForExistingVenUPC.getStyleDropshipFlag())
										.equals(STYLE_DROPSHIP_FLAG_Y)) {
									// Do he following actions if flag is Yes
									itemRequest.setAction(ADD_EDIT_STYLES_SKUS_ACTION);
								}
								else if ((idbForExistingVenUPC.getStyleDropshipFlag())
										.equals(STYLE_DROPSHIP_FLAG_V)) {
									if ((idbForExistingVenUPC.getSKUDropshipFlag())
											.equals(STYLE_DROPSHIP_FLAG_Y)) {
										itemRequest.setAction(ADD_EDIT_STYLES_SKUS_ACTION);
									}
									else {
										itemRequest.setAction(REMOVE_STYLES_SKUS_ACTION);
									}

								}
								else if ((idbForExistingVenUPC.getStyleDropshipFlag())
										.equals(STYLE_DROPSHIP_FLAG_N)) {
									itemRequest.setAction(REMOVE_STYLES_SKUS_ACTION);

								}
								// setting other values in itemRequest Model to
								// be saved in DB
								itemRequest.setEffectiveDate(dateFormat.parse(getCurrentDate()));
								itemRequest.setAllowSalesClearancePrice(STYLE_DROPSHIP_FLAG_N);

								/*
								 * Need to get fulfillment service object for
								 * each fulfillment service ID
								 */

								vfServiceID = fulfillmentServiceID.get(f);

								Long serviceID = vfServiceID.getFulfillmentServId();
								if (serviceID != null) {
									FulfillmentService fulfillmentService = fulfillmentServiceDao
									.getFulfillmentServiceDetails(serviceID);
									itemRequest.setFulfillmentService(fulfillmentService);
								}

								itemRequest.setWorkflowStatus(workflowStatus);
								itemRequest.setStylePopulationMethod(populationMethod);
								itemRequest.setDesc(IDB_FEED_DESC);
								itemRequest.setVendor(venObj);
								itemRequest.setCreatedBy(CARSDEV_USER);
								itemRequest.setUpdatedBy(CARSDEV_USER);
								itemRequest.setCreatedDate(dateFormat.parse(getCurrentDate()));
								itemRequest.setUpdatedDate(dateFormat.parse(getCurrentDate()));
								// save Item request
								fulfillmentServiceDao.saveItemRequest(itemRequest);

								/* Check if Item Request is created */
								if (itemRequest.getItemRequestID() != null) {
									log.debug("inside if item request created...>");
									/* To save styles in VFIR_Style table */
									try {
										dropshipManager.saveStylesToVFIRStyle(idbForExistingVenUPC
												.getVendorStyleID(), idbForExistingVenUPC
												.getStyleDesc(),
												Double.valueOf(idbForExistingVenUPC
														.getIDBStyleUnitCost()), itemRequest,venObj );
									}
									catch (Exception e) {
										log.debug("Exception while saving data to VFIRStyle table"
												+ e);
									}

									/*
									 * Before making new entry in VIFRStyle
									 * table check if the itemrequest action for
									 * new record in feed id Remove Styles and
									 * Skus and vendor style is already present
									 * in the table make it INACTIVE instead of
									 * new entry
									 */

									try {
										dropshipManager.saveStyleSKUToVFIRStyleSKU(
												idbForExistingVenUPC.getVendorStyleID(),
												idbForExistingVenUPC.getBelkUPC(), Double
												.valueOf(idbForExistingVenUPC
														.getIDBUPCUnitCost()),idbForExistingVenUPC.getColorDesc(),idbForExistingVenUPC.getStyleDesc(), itemRequest);
									}
									catch (Exception e) {
										log
										.debug("Exception while saving data to VFIRStyleSku Table "
												+ e);
									}

									/* Entry into VIFRWorkflowHist table */
									try {
										dropshipManager.saveWorkflowHist(itemRequest,
												workflowStatus, ACTION_REQUEST_CREATED);
									}
									catch (Exception ex) {
										log
										.debug("Exception while saving data into VIFRWorkflowHist table"
												+ ex);
									}
								}

							}// End fsID for loop
						}// End of FSID if loop
					}// End of if vendor object is not null loop
					else {
						// make entry into vendor table
						log
						.debug("Since vendor number doesnot exist in VENDOR table make new entry into VENDOR table");
						Vendor vendor = new Vendor();
						vendor.setVendorNumber(idbForExistingVenUPC.getVendorNumber());
						vendor.setName(idbForExistingVenUPC.getVendorName());
						vendor.setDescr(EMPTY_STRING_VENDOR);
						vendor.setContactEmailAddr(EMPTY_STRING_VENDOR);
						vendor.setCreatedBy(CARSDEV_USER);
						vendor.setCreatedDate(dateFormat.parse(getCurrentDate()));
						vendor.setUpdatedBy(CARSDEV_USER);
						vendor.setUpdatedDate(dateFormat.parse(getCurrentDate()));
						vendor.setStatusCd(STATUS_CD_ACTIVE);
						vendor.setIsDisplayable("Y");
						try {
							dropshipManager.createVendor(vendor);
						}
						catch (Exception e) {
							log.debug("Exception saving new record to VENDOR table" + e);
						}
					}
				}// End of if vendorFeed contains matching key loop
			}// end for loop
		}

		// ends here.

		/* Validate the vendor Number */
		validateVendorNumber(vendorUPCsFeed);

		return resultList;
	}

	/**
	 * This method gets the current date and formats it in MM/DD/YYYY format
	 * 
	 * @return String
	 */
	private String getCurrentDate() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(MM_DD_YYYY);
		String currentDate = dateFormat.format(date);
		log.debug("Current date is=" + currentDate);
		return currentDate;
	}

	/**
	 * validateVendorNumber - validates that vendorNumber is Integer not
	 * alphanumeric
	 * 
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	private static void validateVendorNumber(Map<String, IdbDropshipDataTO> data) {
		String vendorNumber = null;
		int i, len;
		Set<String> prd_cd_set = new HashSet<String>(data.keySet());
		String productCode;
		String regExp = "";
		String[] prdVen;
		java.util.Iterator it2 = prd_cd_set.iterator();
		log.debug("Total number of Imported product codes are : " + data.size());
		while (it2.hasNext()) {

			regExp = "";
			productCode = (String) it2.next();
			prdVen = productCode.split("-");

			if (prdVen.length == 2) {
				vendorNumber = prdVen[0];
				len = vendorNumber.length();
				if (len != 0) {
					for (i = 0; i < len; i++) {
						regExp = regExp.concat("[0-9]");
					}
					Pattern p = Pattern.compile(regExp);
					Matcher m = p.matcher(vendorNumber);
					if (m.matches() == false) {
						log.debug("Correct vendor number:" + vendorNumber
								+ " :vendor number is Alphanumeric ");
						data.remove(productCode);
					}
				}
				else {
					log.debug("Incorrect vendor number:" + vendorNumber + " :Length is wrong");
					data.remove(productCode);
				}
			}
			else {
				log.debug("Vendor Number if proper");
			}
		}
		log.debug("Total number of valid product codes are : " + data.size());
	}

	/*
	 * Method to convert values in set to array of comma seperated values
	 */
	@SuppressWarnings("unchecked")
	public String readValuesFromSet(Set set) {
		String value=null;
		if (set == null || set.isEmpty()) {
			return null; // Depending on how you want to deal with this case...
		}
		StringBuilder result = new StringBuilder();

		java.util.Iterator i = set.iterator();

		result.append("'");
		result.append(i.next());
		result.append("'");
		while (i.hasNext()) {
			result.append(",").append("'").append(i.next().toString()).append("'");
		}
		return result.toString();
	}

	/**
	 * Method to process Exception and send email Notification
	 * 
	 * @param processName
	 * @param ex
	 */
	public void processException(String processName, Exception ex) {
		log.debug("Error processing file");
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		sendFailureNotification(processName, sw.toString());
	}

	/**
	 * Method to send failure email notification
	 * 
	 * @param processName
	 * @param content
	 */
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
	 
	
	

}// End of main Class