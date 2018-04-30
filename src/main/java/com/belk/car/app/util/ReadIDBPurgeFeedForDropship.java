
package com.belk.car.app.util;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.belk.car.app.model.oma.ItemRequest;
import com.belk.car.app.model.oma.ItemSource;
import com.belk.car.app.model.oma.StylePopulationMethod;
import com.belk.car.app.model.oma.VFIRStatus;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
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
public class ReadIDBPurgeFeedForDropship implements DropShipConstants {

	private DropshipManager dropshipManager;
	private FulfillmentServiceDao fulfillmentServiceDao;
	private FulfillmentServiceManager fulfillmentServiceManager;
	private DropshipDao dropshipDao;
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

	// Set start and end limit for each field
	/** Whether Vendor ID or Vendor Number */
	public static final int STARTBELKUPC = 0;
	public static final int ENDBELKUPC = 13;

	public static final int STARTVENDORNUM = ENDBELKUPC;
	public static final int ENDVENDORNUM = STARTVENDORNUM + 7;

	public static final int STARTVNDRSTYLE = ENDVENDORNUM;
	public static final int ENDVNDRSTYLE = STARTVNDRSTYLE + 20;

	public static final int STARTVNDRCOLOR = ENDVNDRSTYLE;
	public static final int ENDVNDRCOLOR = STARTVNDRCOLOR + 3;

	public static final int STARTVNDRSZCODE = ENDVNDRCOLOR;
	public static final int ENDVNDRSZCODE = STARTVNDRSZCODE + 5;

	public static final int STARTFILLER = ENDVNDRSZCODE;
	public static final int ENDFILLER = STARTFILLER + 32;

	private static transient final Log log = LogFactory.getLog(ReadIDBPurgeFeedForDropship.class);

	/**
	 * Method to process IDB Purge Feed
	 * 
	 * @param fl
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List processIDBPurgeFeed(File fl)
	throws Exception {
		log.debug("Inside process IDB Purge Feed method");

		// List crossReferenceFeedList = new ArrayList();

		ItemRequest itemRequest = new ItemRequest();
		/* No IDB Purge Feed in DB */
		ItemSource IDBPurgeFeedID = fulfillmentServiceDao.getSourceForItemRequest("IDB Purge Feed");

		// get Style Population Method
		StylePopulationMethod populationMethod = fulfillmentServiceDao
		.getStyleForItemRequest("Drop Ship In IDB Not In ECommerce");

		// get Workflow status code
		VFIRStatus workflowStatus = fulfillmentServiceDao
		.getStatusForItemRequest(VFIRStatus.APPROVED);

		if (log.isInfoEnabled()) {
			log.info("Opened Path");
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(MM_DD_YYYY);
		// Reads line by line into List
		List list = FileUtils.readLines(fl, UTF_8);

		IdbDropshipDataTO idb = null;
		// Iterator to iterate over the list
		java.util.Iterator it = list.iterator();

		List<IdbDropshipDataTO> resultList = new ArrayList();
		try {
			while (it.hasNext()) {
				log.debug("Inside iterator loop of no of lines in the feed");
				String str = (String) it.next();

				// Check if field is empty or has less than 5 characters then
				// skip
				// * the current row

				if (str == null || str.length() < 5) {
					log.debug("skip the current row....");
					continue;
				}

				// Read the values of each row into a string
				String belkUPC = str.substring(STARTBELKUPC, ENDBELKUPC).trim();

				String vendorNum = str.substring(STARTVENDORNUM, ENDVENDORNUM).trim();

				String vendorStyle = str.substring(STARTVNDRSTYLE, ENDVNDRSTYLE).trim();

				String vendorColor = str.substring(STARTVNDRCOLOR, ENDVNDRCOLOR).trim();

				String vendorSizeCode = str.substring(STARTVNDRSZCODE, ENDVNDRSZCODE).trim();

				idb = new IdbDropshipDataTO();
				idb.setVendorNumber(vendorNum);
				idb.setVendorStyleID(vendorStyle);

				idb.setVendorColorCode(vendorColor);

				idb.setVndrSizeCode(vendorSizeCode);
				idb.setBelkUPC(belkUPC);

				resultList.add(idb);

			}// End of while loop
		}
		catch (Exception ex) {
			ex.printStackTrace();
			// Send Notification on Exception to People in the List...
			processException("Import IDB Purge Feed", ex);
		}

		// * For each record in the resultList got from feed

		for (int i = 0; i < resultList.size(); i++) {
			IdbDropshipDataTO idbForExistingVenUPC = null;
			idbForExistingVenUPC = (resultList.get(i));

			// Check if vendor number got from feed is present in VENDOR
			// * table

			Vendor venObj = fulfillmentServiceDao.getVendorForItemRequest(idbForExistingVenUPC
					.getVendorNumber());
			log.debug("Got vendor Object" + venObj);

			// * Check if vendor object is not null-->If not null then
			// * only proceed with entries into other tables

			if (venObj != null) {

				// * logic to get fulfillmentservice IDS for specific
				// * vendor using vendor id from vendor object

				List<FulfillmentServiceVendor> fulfillmentServiceID = dropshipManager
				.getFulfillmentServiceIdForVenID(venObj.getVendorId());

				FulfillmentServiceVendor vfServiceID = null;
				if (fulfillmentServiceID != null && !fulfillmentServiceID.isEmpty()) {
					for (int f = 0; f < fulfillmentServiceID.size(); f++) {
						log.debug("inside for loop for fulfillment service IDs....");

						/* Add new record in ITEM REQUEST table */
						itemRequest.setAction(REMOVE_STYLES_SKUS_ACTION);
						// Source should be set to IDB Purge Feed
						itemRequest.setItemSource(IDBPurgeFeedID);
						itemRequest.setEffectiveDate(dateFormat.parse(getCurrentDate()));
						itemRequest.setAllowSalesClearancePrice(STYLE_DROPSHIP_FLAG_N);

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
						itemRequest.setDesc("Vendor Style Number-"+idbForExistingVenUPC.getVendorStyleID()+"is purged");
						itemRequest.setVendor(venObj);
						itemRequest.setCreatedBy(CARSDEV_USER);
						itemRequest.setUpdatedBy(CARSDEV_USER);
						itemRequest.setCreatedDate(dateFormat.parse(getCurrentDate()));
						itemRequest.setUpdatedDate(dateFormat.parse(getCurrentDate()));
						// save Item request
						if (itemRequest != null) {
							fulfillmentServiceDao.saveItemRequest(itemRequest);
						}
						/* Check if Item Request is created */
						if (itemRequest.getItemRequestID() != null) {
							log.debug("inside if item request created...>");
							/* To save styles in VFIR_Style table */
							try {
								dropshipManager.saveStylesToVFIRStyleForPurge(idbForExistingVenUPC
										.getVendorStyleID(), null, null, itemRequest);
							}
							catch (Exception e) {
								e.printStackTrace();
							}
							try {
								dropshipManager.saveStyleSKUToVFIRStyleSKUForPurge(
										idbForExistingVenUPC.getVendorStyleID(), idb.getBelkUPC(),
										null,null,null, itemRequest);
							}
							catch (Exception e) {
								e.printStackTrace();
							}

							/* Entry into VIFRWorkflowHist table */
							try {
								dropshipManager.saveWorkflowHist(itemRequest, workflowStatus,
										ACTION_REQUEST_CREATED);
							}
							catch (Exception ex) {
								log.debug("Exception while saving data into VIFRWorkflowHist table"
										+ ex);
							}

						}

					}
				}
				else {
					if (log.isDebugEnabled()) {
						log.debug("No fulfillment Service Associated with the vendor");
					}
				}
			}// End if vendor object not null loop

		}
		// End new code for IDB Purge Feed

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
	 
	
	
}
