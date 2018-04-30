package com.belk.car.app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;

import com.belk.car.app.dao.VendorImageEmailNotificationDao;
import com.belk.car.app.dto.FailedImagesDeptDTO;
import com.belk.car.app.dto.RRDCheckEmailNotificationDTO;
import com.belk.car.app.exceptions.CarJobDetailException;
import com.belk.car.app.exceptions.IExceptionProcessor;
import com.belk.car.app.exceptions.SendEmailException;
import com.belk.car.app.model.Config;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.EmailManager;
import com.belk.car.app.service.QuartzJobManagerForEmailNotification;

/**
 * @author afurxd2
 *
 */
public class QuartzJobManagerForEmailNotificationImpl implements QuartzJobManagerForEmailNotification, IExceptionProcessor {
	
	private CarLookupManager lookupManager;
	private UserManager userManager;
	private CarManager carManager;
	private EmailManager sendEmailManager;
	protected VendorImageEmailNotificationDao vendorImageEmailNotificationDao;
	protected QuartzJobManagerForEmailNotification quartzJobManagerForEmailNotification;

	private transient final Log log = LogFactory.getLog(QuartzJobManagerForEmailNotificationImpl.class);

	/**
	 * @param sendEmailManager the sendEmailManager to set
	 */
	public void setSendEmailManager(EmailManager sendEmailManager) {
		this.sendEmailManager = sendEmailManager;
	}

	/**
	 * @param carManager the carManager to set
	 */
	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	/**
	 * @param userManager the userManager to set
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 * @param lookupManager the lookupManager to set
	 */
	public void setLookupManager(CarLookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}

	public void setQuartzJobManagerForEmailNotification(
			    QuartzJobManagerForEmailNotification quartzJobManagerForEmailNotification) {
		this.quartzJobManagerForEmailNotification = quartzJobManagerForEmailNotification;
	}
	
	
	/**
	 * @return the vendorImageEmailNotificationDao
	 */
	public VendorImageEmailNotificationDao getVendorImageEmailNotificationDao() {
		return vendorImageEmailNotificationDao;
	}

	/**
	 * @param vendorImageEmailNotificationDao the vendorImageEmailNotificationDao to set
	 */
	public void setVendorImageEmailNotificationDao(
			VendorImageEmailNotificationDao vendorImageEmailNotificationDao) {
		this.vendorImageEmailNotificationDao = vendorImageEmailNotificationDao;
	}
	
	@Override
	public void processException(String name, Exception ex) {
		
	}

	@Override
	public void sendVendorImageRejectionEmailNotifitcation()
					throws SendEmailException, CarJobDetailException {
		if (log.isInfoEnabled()) {
			log.info("---------------->  Begin Send Vendor Image Rejection Email Notification Process <-----------------");
		}	
		NotificationType type = null;
		Config userName = (Config) lookupManager.getById(Config.class, Config.SYSTEM_USER);		
		Config sendNotifications = (Config) lookupManager.getById(Config.class, Config.SEND_EMAIL_NOTIFICATIONS);
		User systemUser = this.userManager.getUserByUsername(userName.getValue());
		// finalMap will be used to send an email using velocity framework
		Map<String, RRDCheckEmailNotificationDTO> failedImagesMapForAllCARS=null;
		// populate the map for All CARS including Regular,Outfit and Drop ship
		failedImagesMapForAllCARS = carManager.populateEmailListForAllCARS();
		log.info("Final Map Size : " + failedImagesMapForAllCARS.size());
		// loop through map and send an email notification to user
		Map<String, Object> model = new HashMap<String, Object>();
		for (Map.Entry<String, RRDCheckEmailNotificationDTO> entry : failedImagesMapForAllCARS.entrySet()) {
			RRDCheckEmailNotificationDTO rrdFinalCheckEmailNotificationDTO = new RRDCheckEmailNotificationDTO();
			FailedImagesDeptDTO failedImagesDeptDTO = new FailedImagesDeptDTO();
			rrdFinalCheckEmailNotificationDTO = (RRDCheckEmailNotificationDTO) entry.getValue();
			User user = rrdFinalCheckEmailNotificationDTO.getUserDetailObject();
			// do not send emails to Vendor, modified for PI-103.
			if("BUYER".equals(user.getUserTypeCd())){
				type = lookupManager.getNotificationType(NotificationType.ACTION_REQUIRED_CARS);
				log.info("------------------------User Type Buyer----------------------------- : " + user.getUserTypeCd());
				
				model.put("emailAddress", user.getEmailAddress());
				model.put("userId",user.getEmailAddress());
				Map<String, FailedImagesDeptDTO > failedImageDeptMap = rrdFinalCheckEmailNotificationDTO.getMapFailedImagesDeptDTO();
				Map<String, FailedImagesDeptDTO> sortedFailedImageDeptMap = new TreeMap<String, FailedImagesDeptDTO>(failedImageDeptMap);
				//Sort Map with deptCode
				List<FailedImagesDeptDTO> failedImagesDeptList=new ArrayList<FailedImagesDeptDTO>();
				for(Map.Entry<String, FailedImagesDeptDTO> entryFailed : sortedFailedImageDeptMap.entrySet()){
					 failedImagesDeptDTO = (FailedImagesDeptDTO)entryFailed.getValue(); 
					 failedImagesDeptList.add(failedImagesDeptDTO);
				}
				// send model object data to template
				model.put("failedImagesDeptList", failedImagesDeptList);
				try {
						if ("true".equalsIgnoreCase(sendNotifications.getValue())) {
							sendEmailManager.sendVendorImageRejectionEmail(type, systemUser, model, false);
							log.info("<------------------------------Sending Email-------------------------------------->");
						}
				} catch (Exception ex) {
					log.error("General Exception occured. Cause: " + ex.getMessage());
				}
			} 
		}
		if (log.isInfoEnabled()) {
			log.info("---------------->  End of Send Vendor Image Rejection Email Notification Process <-----------------");
		}
	 }

}