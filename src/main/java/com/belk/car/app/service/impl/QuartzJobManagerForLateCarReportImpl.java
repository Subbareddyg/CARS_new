package com.belk.car.app.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;

import com.belk.car.app.dto.LateCarsSummaryDTO;
import com.belk.car.app.exceptions.SendEmailException;
import com.belk.car.app.model.Config;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.EmailManager;
import com.belk.car.app.service.QuartzJobManagerForLateCarReport;
import com.belk.car.app.service.WorkflowManager;


public class QuartzJobManagerForLateCarReportImpl  implements QuartzJobManagerForLateCarReport {
	
	private transient final Log log = LogFactory.getLog(QuartzJobManagerForLateCarReportImpl.class);
	private UserManager userManager;
	private CarManager carManager;
	private EmailManager sendEmailManager;
	private CarLookupManager lookupManager;
	private WorkflowManager workflowManager;
	
	public void setWorkflowManager(WorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}

	public void setUserCarMap(Map<Long, List<Map<String, String>>> userCarMap) {
		this.userCarMap = userCarMap;
	}

	public void setUserMap(Map<Long, User> userMap) {
		this.userMap = userMap;
	}

	public void setLookupManager(CarLookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}

	public void setSendEmailManager(EmailManager sendEmailManager) {
		this.sendEmailManager = sendEmailManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	
	
	Map<Long, List<Map<String, String>>> userCarMap = null;
	Map<Long, User> userMap = new HashMap<Long, User>();
	Map<String, String> gmmEmailToNameMap = new HashMap<String, String>();
	Map<String, String> dmmEmailToNameMap = new HashMap<String, String>();
	
	
	/**
	 * This method sends email reports 
	 */
	public void sendLateCarReports(){
		log.info("------ Beging send Late CARS email reports -----------");	
		List<LateCarsSummaryDTO> lateCarsSummaryDTOList = null;
		List<LateCarsSummaryDTO> lateCarsSummaryDTOListForOtherUser = null;
		try{
			
			lateCarsSummaryDTOList = carManager.getAllLateCarsForBuyer();
			if(lateCarsSummaryDTOList != null && lateCarsSummaryDTOList.size()>0){
				if(log.isInfoEnabled()){
					log.info("Found "+lateCarsSummaryDTOList.size() + " buyer status late cars today");
				}
				generateUserDtoMap(lateCarsSummaryDTOList);
				sendLateCarsReportforBuyer(lateCarsSummaryDTOList);
				sendLateCarsReportforDMMandGMM(lateCarsSummaryDTOList);
				//sendLateCarsReportforCEO(lateCarsSummaryDTOList);
			}else{
				if(log.isInfoEnabled()){
					log.info("No buyer status Late cars found for email reportss.. Exiting job");
				}
			}
			
			lateCarsSummaryDTOListForOtherUser = carManager.getAllLateCarsForOtherUser();
			if(lateCarsSummaryDTOListForOtherUser != null && lateCarsSummaryDTOListForOtherUser.size()>0){ 
				if(log.isInfoEnabled()){
					log.info("Found "+lateCarsSummaryDTOListForOtherUser.size() + " non-buyer status late cars today");
				}
			sendLateCarsReportforHead(lateCarsSummaryDTOListForOtherUser);
			}else{
				if(log.isInfoEnabled()){
					log.info("No non-buyer status Late cars found for email reportss.. Exiting job");
				}
			}
			
		}catch(Exception e){
			log.error("Exception occured while sending Email Reports :"+ e.getStackTrace());
			e.printStackTrace();
		}
		log.info("------ End send Late CARS email reports -----------");
	}
	
	/**
	 * This method sends email reports to buyer
	 */
	public void sendLateCarsReportforBuyer(List<LateCarsSummaryDTO> lateCarsSummaryDTOList){
		log.info("---------------------Begin sendLateCarsReportforBuyer-------------------");
		try{
		Config userName = (Config) lookupManager.getById(Config.class, Config.SYSTEM_USER);
		Config sendNotifications = (Config) lookupManager.getById(Config.class, Config.SEND_EMAIL_NOTIFICATIONS);
		User systemUser = this.userManager.getUserByUsername(userName.getValue());
		NotificationType type = lookupManager.getNotificationType(NotificationType.LATE_CAR_BUYER);
		userCarMap = generateUserCarMap(lateCarsSummaryDTOList);
		Set<Long> users = userCarMap.keySet();
		
		log.info("Number of buyer to send late car email report  : "+users.size());
		
			for(Long userId : users){
				List<Map<String, String>> userCars = userCarMap.get(userId);
				User user = userMap.get(userId);
				if(userCars != null && user != null){
					int totalNoOfSkus = 0;
					Map<String,Object> model  = new HashMap<String,Object>();
					model.put("userEmail",user.getEmailAddress() );
					model.put("firstName", user.getFirstName());
					model.put("lastName", user.getLastName());
					model.put("noOfCar", userCars.size());
					model.put("date",formatDate(new Date()));
					model.put("carList", userCars);
					for(Map<String, String> car : userCars){
						String noOfSkus = car.get("noOfSkus");
						Integer skuCount = new Integer(noOfSkus);
						totalNoOfSkus = totalNoOfSkus + skuCount.intValue();
					}
					model.put("totalNoOfSkus", totalNoOfSkus);
					if(log.isDebugEnabled()){
						log.debug("Sending BUYER late cars email report to  : "+user.getEmailAddress());
					}
					try {
						if ("true".equalsIgnoreCase(sendNotifications.getValue()) && userCars.size()>0) {
							sendEmailManager.sendNotificationEmailReport(type, systemUser, model, false);
						}
					} catch (SendEmailException se) {
						log.error("Error when sending emails");
					} catch (Exception ex) {
						log.error("General Exception occurred. Cause: " + ex.getMessage() + ex);
					}
				}
			}
		}catch(Exception e){
			log.error("exception occured while sending email to buyer : "+ e.getStackTrace());
		}
		log.info("---------------------End sendLateCarsReportforBuyer-------------------");
	}
	
	/**
	 * This method sends email reports to DMM and GMM and CEO group
	 */
	public void sendLateCarsReportforDMMandGMM(List<LateCarsSummaryDTO> lateCarsSummaryDTOList){
		log.info("---------------------Begin sendLateCarsReport for DMM GMM and CEO group-------------------");
		
		Config userName = (Config) lookupManager.getById(Config.class, Config.SYSTEM_USER);
		Config sendNotifications = (Config) lookupManager.getById(Config.class, Config.SEND_EMAIL_NOTIFICATIONS);
		Config CEOnotificationUserList = (Config) lookupManager.getById(Config.class, Config.CEO_REPORT_EMAIL_LIST);
		User systemUser = this.userManager.getUserByUsername(userName.getValue());
		NotificationType dmmNotification = lookupManager.getNotificationType(NotificationType.LATE_CAR_DMM);
		NotificationType gmmNotification = lookupManager.getNotificationType(NotificationType.LATE_CAR_GMM);
		NotificationType CEOnotification = lookupManager.getNotificationType(NotificationType.LATE_CAR_CEO);
		
		
		Map<String, Object> CEOmodel = new HashMap<String, Object>();
		List<Object> gmmModelList = new ArrayList<Object>();
		
		/* 
		 * Create a Map of GMM's and receptive DMM's ,Buyers and their late cars
		 * This map has hierarchy of GMM - DMM - Buyer and its late car details
		 * Map<Gmm_email, Map<DMM's>>
		 *	 Map<DMM's> = Map<dmm_email, List<Buyers>>
		 *		List<Buyers> = List<Cars>
		 *			List<Cars> = List<Map<car_entity, value>> 
		 * Map<Gmm_email, Map<Dmm_email , List<buyer_user_id, List<Map<Car_entity, value>>>>>
		 */
		
		Map<String, Map<String, Map<Long, List<Map<String,String>>>>> gmmMap = createDMMMap(lateCarsSummaryDTOList );
		
		Set<String> gmmEmailList = gmmMap.keySet();
		long CEOCarCount = 0;
		long CEOSkuCount = 0;
		//Loop through GMM's
		for(String gmmEmail: gmmEmailList){
			long gmmCarCount = 0;
			long gmmSkuCount = 0;
			Map<String, Map<Long, List<Map<String,String>>>> dmmMap = gmmMap.get(gmmEmail);
			Set<String> dmmEmailList = dmmMap.keySet();
			List<Map<String,Object>> dmmModelList = new ArrayList<Map<String,Object>>();
			
			// loop through DMM's of current GMM
			for(String dmmEmail: dmmEmailList){
				long dmmCarCount = 0;
				long dmmSkuCount =0; 
				Map<Long, List<Map<String,String>>> buyerMap = dmmMap.get(dmmEmail);
				Set<Long> buyerList = buyerMap.keySet();
				List<Map<String,Object>> buyerModelList = new ArrayList<Map<String,Object>>();  

				//Loop through buyer's of current DMM
				for(Long buyerId: buyerList){
					int buyerSkuCount = 0;
					Map<String,Object> buyerModel = new HashMap<String,Object>();
					List<Map<String,String>> carList = buyerMap.get(buyerId);
					User user = userMap.get(buyerId);
					for(Map<String,String> car : carList){
						String noOfSkus = car.get("noOfSkus") ;
						if(noOfSkus!=null && noOfSkus.length()>0){
							buyerSkuCount = buyerSkuCount + Integer.parseInt(noOfSkus);	
						}
					}
					buyerModel.put("userId",String.valueOf(buyerId));
					buyerModel.put("firstName", user.getFirstName());
					buyerModel.put("lastName", user.getLastName());
					buyerModel.put("carList", carList);
					buyerModel.put("buyerCarCount", carList.size());
					buyerModel.put("buyerSkuCount", buyerSkuCount);
					buyerModelList.add(buyerModel);
					dmmCarCount = dmmCarCount + carList.size();
					dmmSkuCount = dmmSkuCount + buyerSkuCount;
				}
				Map<String,Object> dmmModel = new HashMap<String,Object>();
				String dmmName = dmmEmailToNameMap.get(dmmEmail);
				dmmModel.put("userEmail", dmmEmail);
				dmmModel.put("dmmName", dmmName);
				dmmModel.put("buyerList", buyerModelList);
				dmmModel.put("dmmCarCount", dmmCarCount);
				dmmModel.put("dmmSkuCount", dmmSkuCount);
				dmmModel.put("date", formatDate(new Date()));
				dmmModelList.add(dmmModel);
				if(log.isInfoEnabled()){
					log.info("Found " + buyerModelList.size() + " buyers with late cars for DMM "+ dmmEmail);
				}
				try {
					if ("true".equalsIgnoreCase(sendNotifications.getValue()) && dmmCarCount>0) {
						//Send email to DMM
						sendEmailManager.sendNotificationEmailReport(dmmNotification, systemUser, dmmModel, false);
					}
				} catch (SendEmailException se) {
					log.error("Error when sending emails");
				} catch (Exception ex) {
					log.error("General Exception occurred. Cause: " + ex.getMessage() + ex);
				}
				gmmCarCount = gmmCarCount+ dmmCarCount;
				gmmSkuCount = gmmSkuCount + dmmSkuCount;
			}
			
			Map<String,Object> gmmModel = new HashMap<String,Object>();
			String gmmName = gmmEmailToNameMap.get(gmmEmail);
			gmmModel.put("userEmail", gmmEmail);
			gmmModel.put("gmmName", gmmName);
			gmmModel.put("gmmCarCount", gmmCarCount);
			gmmModel.put("gmmSkuCount", gmmSkuCount);
			gmmModel.put("date", formatDate(new Date()));
			gmmModel.put("dmmList", dmmModelList);
			if(log.isInfoEnabled()){
				log.info(" Found " + dmmModelList.size() + " DMM with late cars for GMM "+ gmmEmail);
			}
			try {
				if ("true".equalsIgnoreCase(sendNotifications.getValue()) && gmmCarCount>0) {
					//Send email to GMM
					sendEmailManager.sendNotificationEmailReport(gmmNotification, systemUser, gmmModel, false);
				}
			} catch (SendEmailException se) {
				log.error("Error when sending emails");
			} catch (Exception ex) {
				log.error("General Exception occurred. Cause: " + ex.getMessage() + ex);
			}
			CEOCarCount = CEOCarCount + gmmCarCount;
			CEOSkuCount = CEOSkuCount + gmmSkuCount;
		    gmmModelList.add(gmmModel);
		}
		
		CEOmodel.put("CEOSkuCount", CEOSkuCount);
		CEOmodel.put("CEOCarCount", CEOCarCount);
		CEOmodel.put("gmmList", gmmModelList);
		CEOmodel.put("date", formatDate(new Date()));
		String[] emails = StringUtils.split(CEOnotificationUserList.getValue(), ",;");
		
		if(log.isInfoEnabled()){
			log.info("Number of late  cars found for CEO late car reporting   "+ CEOCarCount);
		}
		
		for(String userEmail: emails){
			CEOmodel.put("userEmail", userEmail);	
			try {
				if(log.isInfoEnabled()){
					log.info("Sending CEO late car reporting email to  "+userEmail);
				}
				if ("true".equalsIgnoreCase(sendNotifications.getValue()) && gmmModelList.size() >0) {
					//Send email to CEO group
					sendEmailManager.sendNotificationEmailReport(CEOnotification, systemUser, CEOmodel, false);
				}
			} catch (SendEmailException se) {
				log.error("Error when sending emails");
			} catch (Exception ex) {
				log.error("General Exception occurred. Cause: " + ex.getMessage() + ex);
			}
		}
		
		log.info("---------------------End sendLateCarsReportfor DMM GMM and CEO-------------------");
	}

	
	/**
	 * This method sends late cars email report to cars head 
	 * This email report contain all late cars of Sample Coordinator / art director and content manager status
	 */
	public void sendLateCarsReportforHead(List<LateCarsSummaryDTO> lateCarsSummaryDTOListForOtherUsers){
		log.info("---------------------Begin sendLateCarsReportforHead-------------------");
		Config userName = (Config) lookupManager.getById(Config.class, Config.SYSTEM_USER);
		Config sendNotifications = (Config) lookupManager.getById(Config.class, Config.SEND_EMAIL_NOTIFICATIONS);
		User systemUser = this.userManager.getUserByUsername(userName.getValue());
		NotificationType notificationType = lookupManager.getNotificationType(NotificationType.LATE_CAR_HEAD);
		Config carsHead = (Config) lookupManager.getById(Config.class, Config.CARS_HEAD_EMAIL_REPORT_LIST);
		String sendTo = carsHead.getValue();
		
		Map<String, Object> model = new HashMap<String, Object>();
		List<Map<String,String>>  artDirectors = new ArrayList<Map<String,String>>();
		List<Map<String,String>>  sampleCoordinators = new ArrayList<Map<String,String>>();
		List<Map<String,String>>  contentMgnrs = new ArrayList<Map<String,String>>();
		Map<String,String> carMap = null;
		long totalCarCount = 0;
		for(LateCarsSummaryDTO lateCarsSummary: lateCarsSummaryDTOListForOtherUsers){
			carMap = new HashMap<String,String>();
			carMap.put("userName", lateCarsSummary.getFirstName() + " " +lateCarsSummary.getLastName() );
			carMap.put("carId", String.valueOf(lateCarsSummary.getCarId()));
			carMap.put("vendorName", lateCarsSummary.getVendorName());
			carMap.put("vendorNumber", lateCarsSummary.getVendorNumber());
			carMap.put("brandName", lateCarsSummary.getBrandName());
			carMap.put("dept", lateCarsSummary.getDeptCd());
			carMap.put("completionDate",formatDate(lateCarsSummary.getCompletionDate()));
			carMap.put("createdDate",formatDate(lateCarsSummary.getCreatedDate()));
			carMap.put("noOfSkus", String.valueOf(lateCarsSummary.getSkuCount()));
		
			if(lateCarsSummary.getUserTypeCd().equalsIgnoreCase(UserType.ART_DIRECTOR)){
				artDirectors.add(carMap);
			}
			if(lateCarsSummary.getUserTypeCd().equalsIgnoreCase(UserType.SAMPLE_COORDINATOR)){
				sampleCoordinators.add(carMap);
			}
			if(lateCarsSummary.getUserTypeCd().equalsIgnoreCase(UserType.CONTENT_MANAGER) || lateCarsSummary.getUserTypeCd().equalsIgnoreCase(UserType.CONTENT_WRITER)){
				contentMgnrs.add(carMap);
			}
		}
		totalCarCount = artDirectors.size() + sampleCoordinators.size() + contentMgnrs.size();
		model.put("directorList",artDirectors);
		model.put("sampleCoList",sampleCoordinators);
		model.put("contentMgnrList",contentMgnrs);
		model.put("directorCarCount",artDirectors.size());
		model.put("sampleCoCarCount",sampleCoordinators.size());
		model.put("contentMgnrCarCount",contentMgnrs.size());
		model.put("totalCarCount", totalCarCount);
		model.put("date", formatDate(new Date()));
		if(log.isDebugEnabled()){
			log.debug("Total number of late cars found in Art-Director's status : " + artDirectors.size());
			log.debug("Total number of late cars found in Sample-Coordinator's status : " + sampleCoordinators.size());
			log.debug("Total number of late cars found in Content Manager's status : " + contentMgnrs.size());
		}
		
		if(log.isInfoEnabled()){
			log.info("Total number of late cars found for CARS HEAD email report: " + totalCarCount);
		}
		
		String[] emails = StringUtils.split(sendTo, ",;");
		for(String userEmail :emails){
			model.put("userEmail", userEmail);
			try {
				if ("true".equalsIgnoreCase(sendNotifications.getValue()) && totalCarCount > 0) {
					sendEmailManager.sendNotificationEmailReport(notificationType, systemUser, model, false);
				}else{
					if(log.isInfoEnabled()){
						log.info("no late cars found for CARS HEAD email report " );
					}
				}
			} catch (SendEmailException se) {
				log.error("Error when sending emails");
			} catch (Exception ex) {
				log.error("General Exception occurred. Cause: " + ex.getMessage() + ex);
			}
		}
		log.info("---------------------End sendLateCarsReportforHead-------------------");
	}
	
	
	/**
	 * This method creates the map of cars with DMM and GMM
	 * @param lateCarsSummaryList
	 * @return Map of DMM/GMM to cars
	 */
	public Map<String, Map<String, Map<Long, List<Map<String,String>>>>> createDMMMap(List<LateCarsSummaryDTO> lateCarsSummaryList){
		Map<String, Map<String, Map<Long, List<Map<String,String>>>>> gmmMap = new HashMap<String, Map<String, Map<Long, List<Map<String,String>>>>>();
		Map<String, Map<Long, List<Map<String,String>>>>  dmmMap = null;
		Map<Long, List<Map<String,String>>>  buyerMap = null;
		List<Map<String,String>> carList = null;
		for(LateCarsSummaryDTO lateCarsSummaryDTO : lateCarsSummaryList){
			String gmmEmail = lateCarsSummaryDTO.getGmmEmail();
			if((dmmMap = gmmMap.get(gmmEmail))== null){
				dmmMap = new HashMap<String, Map<Long, List<Map<String,String>>>> ();
			}
			String dmmEmail = lateCarsSummaryDTO.getDmmEmail();
			if((buyerMap = dmmMap.get(dmmEmail))== null){
				buyerMap = new HashMap<Long, List<Map<String,String>>>();
			}
			Long buyerId = lateCarsSummaryDTO.getUserId(); 
			if((carList = buyerMap.get(buyerId))== null){
				carList = new ArrayList<Map<String,String>>();
			}
			if(lateCarsSummaryDTO.getUserTypeCd().equals(UserType.BUYER) || lateCarsSummaryDTO.getUserTypeCd().equals(UserType.VENDOR)){
				Map<String, String> carMap = new HashMap<String, String>();
				carMap.put("carid", String.valueOf(lateCarsSummaryDTO.getCarId()));
				carMap.put("vendorName", lateCarsSummaryDTO.getVendorName());
				carMap.put("dept", lateCarsSummaryDTO.getDeptCd());
				carMap.put("completionDate",formatDate(lateCarsSummaryDTO.getCompletionDate()));
				carMap.put("createdDate",formatDate(lateCarsSummaryDTO.getCreatedDate()));
				carMap.put("noOfSkus", String.valueOf(lateCarsSummaryDTO.getSkuCount()));
				carMap.put("vendorNumber", lateCarsSummaryDTO.getVendorNumber());
				carMap.put("brandName", lateCarsSummaryDTO.getBrandName());
				carList.add(carMap);
				buyerMap.put(buyerId, carList);
				dmmMap.put(dmmEmail, buyerMap);
				gmmMap.put(gmmEmail, dmmMap);
			}
		}
		return gmmMap;
	}
	
	/**
	 * 
	 * This method create the GMM/DMM to cars map for CEO report
	 * @param lateCarsSummaryList
	 * @return map of CARS 
	 */
	public Map <String, Map<String, Map<Long, Long>>> createGMMMapforCEO(List<LateCarsSummaryDTO> lateCarsSummaryList){
		Map <String, Map<String, Map<Long, Long>>> gmmMap = new HashMap<String, Map<String, Map<Long, Long>>>();
		Map <String, Map<Long, Long>> dmmMap = new HashMap<String, Map<Long, Long>>();
		Map<Long, Long> buyerMap = null;
		long carCount = 0;
		for(LateCarsSummaryDTO lateCarsSummaryDTO : lateCarsSummaryList){
			String gmmEmail = lateCarsSummaryDTO.getGmmEmail();
			if((dmmMap = gmmMap.get(gmmEmail))== null){
				dmmMap = new HashMap<String, Map<Long, Long>>();
			}
			String dmmEmail = lateCarsSummaryDTO.getDmmEmail();
			if((buyerMap = dmmMap.get(dmmEmail))== null){
				buyerMap = new HashMap<Long, Long>();
			}
			Long buyerId = lateCarsSummaryDTO.getUserId(); 
			
			if((lateCarsSummaryDTO.getUserTypeCd().equals(UserType.BUYER) &&  lateCarsSummaryDTO.getStatusCd().equalsIgnoreCase(WorkflowStatus.INITIATED))
			   || (lateCarsSummaryDTO.getUserTypeCd().equals(UserType.VENDOR) && lateCarsSummaryDTO.getStatusCd().equalsIgnoreCase(WorkflowStatus.READY_FOR_REVIEW))){
				Long prevCarCount = (buyerMap.get(buyerId)==null?  0: buyerMap.get(buyerId));
				carCount = prevCarCount.longValue()+1;
				buyerMap.put(buyerId, new Long(carCount));
				dmmMap.put(dmmEmail, buyerMap);
				gmmMap.put(gmmEmail, dmmMap);
				
			}
		}
		log.info("number of GMM :"+gmmMap.size());
		return gmmMap;
	}
	
	/**
	 * This method creates the Map of car with it's details
	 * @param userList
	 * @return Map of cardetail
	 */
	private Map<Long, List<Map<String, String>>> generateUserCarMap(List<LateCarsSummaryDTO> userList){
		Map<Long, List<Map<String, String>>> userCarMap = new HashMap<Long, List<Map<String, String>>>();
		
		for(LateCarsSummaryDTO dto: userList){
			if(dto.getUserTypeCd().equals(UserType.BUYER) || dto.getUserTypeCd().equals(UserType.VENDOR)){
				Long UserId = new Long(dto.getUserId());
				List<Map<String, String>> carList = null;
				if((carList=userCarMap.get(UserId)) == null){
					carList = new ArrayList<Map<String, String>>();
				}
				Map<String, String> carMap = new HashMap<String, String>();
				carMap.put("carid", String.valueOf(dto.getCarId()));
				carMap.put("vendorName", dto.getVendorName());
				carMap.put("dept", dto.getDeptCd());
				carMap.put("completionDate",formatDate(dto.getCompletionDate()));
				carMap.put("createdDate",formatDate(dto.getCreatedDate()));
				carMap.put("noOfSkus", String.valueOf(dto.getSkuCount()));
				carMap.put("brandName", dto.getBrandName());
				carMap.put("vendorNumber", dto.getVendorNumber());
				carList.add(carMap);
				userCarMap.put(UserId, carList);
			}
		}
		return userCarMap;
	}
	
	/**
	 * This method creates the map of user with its detail
	 * @param lateCarsSummaryDTOList
	 */
	private void generateUserDtoMap(List<LateCarsSummaryDTO> lateCarsSummaryDTOList){
		User user = null;
		for(LateCarsSummaryDTO dto: lateCarsSummaryDTOList){
			if((user = userMap.get(new Long(dto.getUserId()))) == null){
				user = new User();
				user.setId(dto.getUserId());
				user.setFirstName(dto.getFirstName());
				user.setLastName(dto.getLastName());
				user.setUserTypeCd(dto.getUserTypeCd());
				user.setEmailAddress(dto.getEmail());
				userMap.put(new Long(dto.getUserId()), user);
			}
			if(gmmEmailToNameMap.get(dto.getGmmEmail()) == null){
				gmmEmailToNameMap.put(dto.getGmmEmail(), dto.getGmmName());
			}
			if(dmmEmailToNameMap.get(dto.getDmmEmail()) == null){
				dmmEmailToNameMap.put(dto.getDmmEmail(), dto.getDmmName());
			}
		}
	}
	
	/**
	 * This method format the DATE to MMM-DD-YYYY formats
	 * @param date
	 * @return String date
	 */
	public String formatDate(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat("MMM-dd-yyyy");
		String formattedDate =  formatter.format(date);
		if(formattedDate == null){
			return "";
		}
		else{
			return formattedDate;
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			new QuartzJobManagerForLateCarReportImpl();
	}
	

}
