package com.belk.car.app.service;

import java.util.Map;

import org.appfuse.model.User;
import org.appfuse.service.UniversalManager;

import com.belk.car.app.exceptions.SendEmailException;
import com.belk.car.app.model.NotificationType;

public interface EmailManager extends UniversalManager{
	
	  void sendEmail(NotificationType type, User user, Map model) throws SendEmailException;

	  void sendEmail(NotificationType type, User user, Map model, String initiatedByUser) throws SendEmailException ;
	  
	  public void sendNotificationEmail(final NotificationType type, final User user, final Map model) throws SendEmailException;
	  
	  void sendEmailForRequest(NotificationType type, User user, Map model, String requestId) throws SendEmailException ;

	  public void sendEmailForCatalogDataMapping(NotificationType type, User user, Map model) throws SendEmailException;
	  
	  public void sendNotificationEmailReport(final NotificationType type, final User user, final Map model,  final boolean saveContent) throws SendEmailException;
	  
	  public void sendVendorImageRejectionEmail(final NotificationType type, User user, final Map model, final boolean isMQCheck);
}
