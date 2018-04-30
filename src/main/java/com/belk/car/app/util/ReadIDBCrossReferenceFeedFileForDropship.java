package com.belk.car.app.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
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
import com.belk.car.app.dao.CarManagementDao;
import com.belk.car.app.dao.DropshipDao;
import com.belk.car.app.dao.FulfillmentServiceDao;
import com.belk.car.app.exceptions.SendEmailException;
import com.belk.car.app.model.Config;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.ItemRequest;
import com.belk.car.app.model.oma.ItemSource;
import com.belk.car.app.model.oma.StylePopulationMethod;
import com.belk.car.app.model.oma.VFIRStatus;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.model.workflow.WorkflowStatus;
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
public class ReadIDBCrossReferenceFeedFileForDropship implements
		DropShipConstants {

	private DropshipManager dropshipManager;
	private FulfillmentServiceDao fulfillmentServiceDao;
	private FulfillmentServiceManager fulfillmentServiceManager;
	private DropshipDao dropshipDao;
	// private EmailManager emailManager;
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
	 * @param fulfillmentServiceManager
	 *            the fulfillmentServiceManager to set
	 */
	public void setFulfillmentServiceManager(
			FulfillmentServiceManager fulfillmentServiceManager) {
		this.fulfillmentServiceManager = fulfillmentServiceManager;
	}

	/**
	 * @return the fulfillmentServiceDao
	 */
	public FulfillmentServiceDao getFulfillmentServiceDao() {
		return fulfillmentServiceDao;
	}

	/**
	 * @param fulfillmentServiceDao
	 *            the fulfillmentServiceDao to set
	 */
	public void setFulfillmentServiceDao(
			FulfillmentServiceDao fulfillmentServiceDao) {
		this.fulfillmentServiceDao = fulfillmentServiceDao;
	}

	/**
	 * @return the dropshipManager
	 */
	public DropshipManager getDropshipManager() {
		return dropshipManager;
	}

	/**
	 * @param dropshipManager
	 *            the dropshipManager to set
	 */
	public void setDropshipManager(DropshipManager dropshipManager) {
		this.dropshipManager = dropshipManager;
	}

	// Set start and end limit for each field
	public static final int STARTNEWBELKUPC = 0;
	public static final int ENDNEWBELKUPC = STARTNEWBELKUPC + 13;

	public static final int STARTOLDBELKUPC = ENDNEWBELKUPC;
	public static final int ENDOLDBELKUPC = STARTOLDBELKUPC + 13;

	public static final int STARTVNDRSTYLENUMFROMNEWBELKUPC = ENDOLDBELKUPC;
	public static final int ENDVNDRSTYLENUMFROMNEWBELKUPC = STARTVNDRSTYLENUMFROMNEWBELKUPC + 20;

	public static final int STARTVNDRNBR = ENDVNDRSTYLENUMFROMNEWBELKUPC;
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

	public static final int STARTSZCHARTCODE = ENDIDBUPCUNITCOST;
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

	private static transient final Log log = LogFactory
			.getLog(ReadIDBCrossReferenceFeedFileForDropship.class);

	/**
	 * Method to process Cross Reference Feed
	 * 
	 * @param fl
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List processReferenceFeed(File fl) throws Exception {
		log.debug("Inside process CrossReference Feed method");

		// List crossReferenceFeedList = new ArrayList();

		ItemRequest itemRequest = new ItemRequest();
		// List to store vendor UPCs
		List<IdbDropshipDataTO> vendorUPCList = new ArrayList<IdbDropshipDataTO>();

		ItemSource crossReferenceFeedID = fulfillmentServiceDao
				.getSourceForItemRequest("Cross Reference Feed");

		// get Style Population Method
		StylePopulationMethod populationMethod = fulfillmentServiceDao
				.getStyleForItemRequest("Drop Ship In IDB Not In ECommerce");

		// get Workflow status code
		VFIRStatus workflowStatus = fulfillmentServiceDao
				.getStatusForItemRequest(VFIRStatus.OPEN);

		if (log.isInfoEnabled()) {
			log.info("Opened Path");
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(MM_DD_YYYY);
		Date newDate = new Date();
		// Reads line by line into List
		List list = FileUtils.readLines(fl, UTF_8);
		List<IdbDropshipDataTO> idbObjectsList = new ArrayList<IdbDropshipDataTO>();
		// HashMap to store the data came from Feed file
		HashMap<String, IdbDropshipDataTO> vendorUPCsFeed = new HashMap<String, IdbDropshipDataTO>();
		HashMap<String, List> vendornumVendorUPCMap = new HashMap<String, List>();
		HashMap<List, ItemRequest> objectsListforIDB = new HashMap<List, ItemRequest>();
		IdbDropshipDataTO idb = null;
		// Iterator to iterate over the list
		java.util.Iterator it = list.iterator();

		List<IdbDropshipDataTO> resultList = new ArrayList();
		try {
			while (it.hasNext()) {
				log.debug("Inside iterator loop of no of lines in the feed");
				String str = (String) it.next();

				// Check if field is empty or has less than 5 characters then
				// skip the current row

				if (str == null || str.length() < 5) {
					log.debug("skip the current row....");
					continue;
				}

				// Read the values of each row into a string
				String newBelkUPC = str.substring(STARTNEWBELKUPC,
						ENDNEWBELKUPC).trim();

				String oldBelkUPC = str.substring(STARTOLDBELKUPC,
						ENDOLDBELKUPC).trim();

				String vndrStyleNumForNewBelkUPC = str.substring(
						STARTVNDRSTYLENUMFROMNEWBELKUPC,
						ENDVNDRSTYLENUMFROMNEWBELKUPC).trim();

				String vendorNumber = str.substring(STARTVNDRNBR, ENDVNDRNBR)
						.trim();

				String vendorName = str.substring(STARTVNDRNAME, ENDVNDRNAME)
						.trim();

				String deptNbr = str.substring(STARTDPTNBR, ENDDPTNBR).trim();

				String deptName = str.substring(STARTDEPTNAME, ENDDEPTNAME)
						.trim();

				String classNumber = str.substring(STARTCLASSNBR, ENDCLASSNBR)
						.trim();

				String className = str.substring(STARTCLASSNAME, ENDCLASSNAME)
						.trim();

				String vendorStyle = str
						.substring(STARTVNDRSTYLE, ENDVNDRSTYLE).trim();

				String vendorStyleDesc = str.substring(STARTSTYLEDESC,
						ENDSTYLEDESC).trim();

				String vendorColor = str
						.substring(STARTVNDRCOLOR, ENDVNDRCOLOR).trim();

				String vendorColorName = str.substring(STARTVNDRCOLORNAME,
						ENDVNDRCOLORNAME).trim();

				String vendorSizeCode = str.substring(STARTVNDRSZCODE,
						ENDVNDRSZCODE).trim();

				String vendorSizeDesc = str.substring(STARTVNDRSZDESC,
						ENDVNDRSZDESC).trim();

				String vendorUPC = str.substring(STARTVENDORUPC, ENDVENDORUPC)
						.trim();

				String upcAddDate = str.substring(STARTUPCADDDATE,
						ENDUPCADDDATE).trim();

				String idbUPCUnitPrice = str.substring(STARTIDBUPCUNITPRICE,
						ENDIDBUPCUNITPRICE).trim();

				String idbUPCUnitCost = str.substring(STARTIDBUPCUNITCOST,
						ENDIDBUPCUNITCOST).trim();

				String sizeChartCode = str.substring(STARTSZCHARTCODE,
						ENDSZCHARTCODE).trim();

				String styleDropshipFlag = str.substring(STARTSTYLEDRPSHIPFLG,
						ENDSTYLEDRPSHIPFLG).trim();

				String skuDropshipFlag = str.substring(STARTSKUDRPSHIPFLG,
						ENDSKUDRPSHIPFLG).trim();

				String dateFlagUpdated = str.substring(STARTDATEFLGUPDATED,
						ENDDATEFLGUPDATED).trim();

				String notOrderableFlag = str.substring(STARTNOTORDERABLEFLG,
						ENDNOTORDERABLEFLG).trim();

				String upcDiscountedFlag = str.substring(STARTUPCDISCFLG,
						ENDUPCDISCFLG).trim();

				String upcDiscountedDate = str.substring(STARTUPCDISCDATE,
						ENDUPCDISCDATE).trim();

				idb = new IdbDropshipDataTO();
				idb.setNewBelkUPC(newBelkUPC);
				idb.setOldBelkUPC(oldBelkUPC);
				idb.setVndrStyleNumForNewBelkUPC(vndrStyleNumForNewBelkUPC);
				idb.setClassName(className);
				idb.setClassNum(Long.valueOf(classNumber));
				idb.setDeptName(deptName);
				idb.setDeptNum(Long.valueOf(deptNbr));
				idb.setVendorName(vendorName);
				idb.setVendorNumber(vendorNumber);
				idb.setVendorStyleID(vendorStyle);
				idb.setBelkUPC(newBelkUPC);
				idb.setStyleDesc(vendorStyleDesc);
				idb.setVendorColorCode(vendorColor);
				idb.setColorDesc(vendorColorName);
				idb.setVndrSizeCode(vendorSizeCode);
				idb.setVndrSize(vendorSizeDesc);
				idb.setVndrUPC(vendorUPC);
				idb.setUPCAddDate(dateFormat.parse(upcAddDate));
				idb.setIDBUPCUnitPrice(Double.valueOf(idbUPCUnitPrice));
				idb.setIDBUPCUnitCost(Double.valueOf(idbUPCUnitCost));
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

				resultList.add(idb);

			}// End of while loop
		} catch (Exception ex) {
			ex.printStackTrace();
			// Send Notification on Exception to People in the List...
			processException("Import Cross Reference Feed", ex);
		}

		for (int i = 0; i < resultList.size(); i++) {

			IdbDropshipDataTO idbForExistingVenUPC = null;
			idbForExistingVenUPC = (resultList.get(i));

			// Check if vendor number got from feed is present in VENDOR table
			Vendor venObj = fulfillmentServiceDao
					.getVendorForItemRequest(idbForExistingVenUPC
							.getVendorNumber());

			// * Check if vendor object is not null-->If not null then
			// * only proceed with entries into other tables

			if (venObj != null) {

				// * logic to get fulfillmentservice IDS for specific
				// * vendor using vendor id from vendor object
				if ((idbForExistingVenUPC.getStyleDropshipFlag())
						.equals(STYLE_DROPSHIP_FLAG_Y)) {

					/*
					 * Need to get vendor associated to the vendorupc and
					 * maintain map of vendor number with list of vendorupcs
					 * under it
					 */
					if (!(vendornumVendorUPCMap.containsKey(venObj
							.getVendorNumber()))) {

						vendorUPCList = new ArrayList<IdbDropshipDataTO>();
						vendorUPCList.add(idbForExistingVenUPC);
						vendornumVendorUPCMap.put(venObj.getVendorNumber(),
								vendorUPCList);

					} else {
						vendorUPCList = vendornumVendorUPCMap.get(venObj
								.getVendorNumber());
						vendorUPCList.add(idbForExistingVenUPC);
						vendornumVendorUPCMap.put(venObj.getVendorNumber(),
								vendorUPCList);
					}
				}// End if style dropship flag is Y
			}// End of if venobj is not null
			else {
				// make entry into vendor table
				log.debug("Since vendor number doesnot exist in VENDOR table make new entry into VENDOR table");
				Vendor vendor = new Vendor();
				vendor.setVendorNumber(idbForExistingVenUPC.getVendorNumber());
				vendor.setName(idbForExistingVenUPC.getVendorName());
				vendor.setDescr(EMPTY_STRING_VENDOR);
				vendor.setIsDisplayable("Y");
				vendor.setContactEmailAddr(EMPTY_STRING_VENDOR);
				vendor.setCreatedBy(CARSDEV_USER);
				vendor.setCreatedDate(dateFormat.parse(getCurrentDate()));
				vendor.setUpdatedBy(CARSDEV_USER);
				vendor.setUpdatedDate(dateFormat.parse(getCurrentDate()));
				vendor.setStatusCd(STATUS_CD_ACTIVE);

				try {
					dropshipManager.createVendor(vendor);
				} catch (Exception e) {
					log.debug("Exception saving new record to VENDOR table"
									+ e);
				}
			}
		}// End of resultList

		Set vendorNumberSet = vendornumVendorUPCMap.keySet();
		java.util.Iterator venNumIterator = vendorNumberSet.iterator();
		List itemRequestObjects = new ArrayList();
		/* Iterator over vendor numbers to create item request objects */
		while (venNumIterator.hasNext()) {
			String vndrNum = (String) venNumIterator.next();

			Vendor vendorObj = fulfillmentServiceDao
					.getVendorForItemRequest(vndrNum);

			List<FulfillmentServiceVendor> fulfillmentServiceID = dropshipManager
					.getFulfillmentServiceIdForVenID(vendorObj.getVendorId());
			FulfillmentServiceVendor vfServiceID = null;
			if (fulfillmentServiceID != null && !fulfillmentServiceID.isEmpty()) {

				for (int f = 0; f < fulfillmentServiceID.size(); f++) {
					// List to store value for vendor number key
					idbObjectsList = vendornumVendorUPCMap.get(vendorObj
							.getVendorNumber());

					/* Add new record in ITEM REQUEST table */
					itemRequest.setAction(ADD_EDIT_STYLES_SKUS_ACTION);
					itemRequest.setItemSource(crossReferenceFeedID);
					itemRequest.setEffectiveDate(dateFormat
							.parse(getCurrentDate()));
					itemRequest
							.setAllowSalesClearancePrice(STYLE_DROPSHIP_FLAG_N);

					// Need to get fulfillment service object for
					// each fulfillment service ID

					vfServiceID = fulfillmentServiceID.get(f);

					Long serviceID = vfServiceID.getFulfillmentServId();
					if (serviceID != null) {
						FulfillmentService fulfillmentService = fulfillmentServiceDao
								.getFulfillmentServiceDetails(serviceID);

						itemRequest.setFulfillmentService(fulfillmentService);
					}
					itemRequest.setWorkflowStatus(workflowStatus);
					itemRequest.setStylePopulationMethod(populationMethod);
					itemRequest.setVendor(vendorObj);
					itemRequest.setCreatedBy(CARSDEV_USER);
					itemRequest.setUpdatedBy(CARSDEV_USER);
					itemRequest.setCreatedDate(dateFormat
							.parse(getCurrentDate()));
					itemRequest.setUpdatedDate(dateFormat
							.parse(getCurrentDate()));

					// Add the object to list
					itemRequestObjects.add(itemRequest);
				}
				// Store in map to be used for saving in vifr style and vifr sku
				// objectsListforIDB.put(idbObjectsList, itemRequestObjects);
				if (itemRequest != null) {
					dropshipManager.saveItemRequestList(itemRequestObjects);
				}

				// For all the item requests
				for (int t = 0; t < itemRequestObjects.size(); t++) {
					ItemRequest itemreq = (ItemRequest) itemRequestObjects
							.get(t);
					// Check if Item Request is created
					if (itemreq.getItemRequestID() != null) {
						// For all the values in list for specific key
						for (int l = 0; l < idbObjectsList.size(); l++) {
							// Get the idbDropShipDataTo object for each record
							// in list
							IdbDropshipDataTO idbObject = idbObjectsList.get(l);

							/* To save styles in VFIR_Style table */
							try {
								dropshipManager.saveStylesToVFIRStyle(idbObject
										.getVndrStyleNumForNewBelkUPC(),
										idbObject.getStyleDesc(), Double
												.valueOf(idbObject
														.getIDBUPCUnitCost()),
										itemreq, vendorObj);
							} catch (Exception e) {
								e.printStackTrace();
							}
							try {

								dropshipManager.saveStyleSKUToVFIRStyleSKU(
										idbObject.getVendorStyleID(), idbObject
												.getBelkUPC(), Double
												.valueOf(idbObject
														.getIDBUPCUnitCost()),
										idbObject.getColorDesc(), idbObject
												.getStyleDesc(), itemreq);
							} catch (Exception e) {
								e.printStackTrace();
							}

							/* Entry into VIFRWorkflowHist table */
							try {
								dropshipManager.saveWorkflowHist(itemRequest,
										workflowStatus, ACTION_REQUEST_CREATED);
							} catch (Exception ex) {
								log.debug("Exception while saving data into VIFRWorkflowHist table"
												+ ex);
							}
						}// End of IDB Objects List
					}// If itemrequest is not null
				}// End of item requests List

			}// End of fulfillment service
		}// End of while iterator

		// End new code for cross reference Feed

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
		return dateFormat.format(date);

	}

	/**
	 * Method to process Exception and send email Notification
	 * 
	 * @param processName
	 * @param ex
	 */
	public void processException(String processName, Exception ex) {
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
		NotificationType type = lookupManager
				.getNotificationType(NotificationType.SYSTEM_FAILURE);

		Config userName = (Config) lookupManager.getById(Config.class,
				Config.SYSTEM_USER);
		Config sendNotifications = (Config) lookupManager.getById(Config.class,
				Config.SEND_EMAIL_NOTIFICATIONS);
		Config emailNotificationList = (Config) lookupManager.getById(
				Config.class, Config.SCHEDULED_PROCESS_ERROR_NOTIFICATION_LIST);

		User systemUser = this.userManager.getUserByUsername(userName
				.getValue());

		Map<String, String> model = new HashMap<String, String>();
		String[] emails = StringUtils.split(emailNotificationList.getValue(),
				",;");
		for (String email : emails) {
			model.put("userEmail", email);
			model.put("processName", processName);
			model
					.put("exceptionContent", StringUtils.abbreviate(content,
							4000));
			model.put("executionDate", DateUtils.formatDate(new Date(),
					"MM/dd/yyyy HH:mm:ss"));

			try {
				if ("true".equalsIgnoreCase(sendNotifications.getValue())) {
					sendEmailManager.sendNotificationEmail(type, systemUser,
							model);
				}
			} catch (SendEmailException se) {
				if (log.isErrorEnabled()) {
					log.error("Error when sending email to: " + email);
				}
			} catch (Exception ex) {
				if (log.isErrorEnabled()) {
					log.error("General Exception occured. Cause: " + email);
				}
			}
		}

	}

}
