/**
 * 
 */
package com.belk.car.app.service.impl;

import java.util.Date;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.appfuse.model.User;
import org.appfuse.service.impl.UniversalManagerImpl;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.belk.car.app.dao.NotificationDao;
import com.belk.car.app.exceptions.SendEmailException;
import com.belk.car.app.model.Notification;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.vendorcatalog.VendorCatalog;
import com.belk.car.app.service.EmailManager;

/**
 * @author vsundar
 * 
 */
public class VelocityEmailManagerImpl extends UniversalManagerImpl implements EmailManager {

	private NotificationDao notificationDao;
	private JavaMailSender mailSender;
	private VelocityEngine velocityEngine;
	private String templatePath;

	public void setNotificationDao(NotificationDao notificationDao) {
		this.notificationDao = notificationDao;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public void setTemplatePath(String path) {
		this.templatePath = path;
	}

	public String getTemplatePath() {
		return StringUtils.isBlank(this.templatePath) ? "" : this.templatePath;
	}

	public void sendEmail(final NotificationType type, final User user, final Map model) throws SendEmailException {
		sendEmail(type, user, model, user.getUsername());
	}

	public void sendEmail(final NotificationType type, final User user, final Map model, String initiateByUser) throws SendEmailException {
		if (!model.containsKey("user")) {
			model.put("user", user);
		}

		final Notification notification = new Notification();
		if (StringUtils.isNotBlank(type.getFromEmailAddress())) {
			notification.setFromEmailAddress(type.getFromEmailAddress());
		} else {
			notification.setFromEmailAddress(NotificationType.DEFAULT_EMAIL);
		}
		notification.setNotificationType(type);
		notification.setSentDate(new Date());
		notification.setSubject(type.getSubject());
		notification.setToEmailAddress(user.getEmailAddress());
		notification.setUpdatedBy(initiateByUser);
		notification.setCreatedBy(initiateByUser);
		notification.setUpdatedDate(new Date());
		notification.setCreatedDate(new Date());

		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				message.setTo(user.getEmailAddress());
				message.setFrom(type.getFromEmailAddress(), type.getFromName());
				message.setSubject(type.getSubject());
				if (log.isDebugEnabled()) {
					try {
						log.debug("Using Velocity Template: " + getTemplatePath() + type.getCode().toLowerCase() + ".vm");
						log.debug("Template Exists: " + velocityEngine.templateExists(getTemplatePath() + type.getCode().toLowerCase() + ".vm"));
						log.debug(velocityEngine.getTemplate(getTemplatePath() + type.getCode().toLowerCase() + ".vm"));
					} catch (Exception ex) {
						log.debug("Exception in Velocity Template: " + ex);
					}
				}
				String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, getTemplatePath() + type.getCode().toLowerCase() + ".vm", model);
				message.setText(text, true);
				notification.setContent(text);

			}
		};

		// Save message in the Notification before you try and Send it
		try {
			this.mailSender.send(preparator);
		} catch (Exception ex) {
            if (ex != null && ex.getCause().getMessage() != null) {
                notification.setErrorMessage(ex.getCause().getMessage());
            } else {
                notification.setErrorMessage("Unknown error has occurred in sending an email.");
            }
		}

		if (notification.getContent() != null) {
			notification.setSentDate(new Date());
			notificationDao.save(notification);
		} else {
			if (log.isDebugEnabled())
				log.debug("Content is null.  This will happen because the Velocity Template is not loadable!!!");
		}

		if (StringUtils.isNotBlank(notification.getErrorMessage())) {
			throw new SendEmailException("An error occured when sending the email. error: " + notification.getErrorMessage());
		}
	}

	public void sendNotificationEmail(final NotificationType type, final User user, final Map model) throws SendEmailException{
		sendNotificationEmailReport(type,user,model,true);
	}
	
	public void  sendNotificationEmailReport(final NotificationType type, final User user, final Map model, final boolean saveContent) throws SendEmailException {
		try{
		final String toEmailAddress = (String) model.get("userEmail");
		//Verify that the email was set.
		if (StringUtils.isNotBlank(toEmailAddress)) {
			final Notification notification = new Notification();
			notification.setFromEmailAddress(type.getFromEmailAddress());
			notification.setNotificationType(type);
			notification.setSentDate(new Date());
			notification.setSubject(type.getSubject());
			notification.setToEmailAddress(toEmailAddress);
			notification.setUpdatedBy(user.getUsername());
			notification.setCreatedBy(user.getUsername());
			notification.setUpdatedDate(new Date());
			notification.setCreatedDate(new Date());
		
			MimeMessagePreparator preparator = new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
					MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
					message.setTo(toEmailAddress);
					message.setFrom(type.getFromEmailAddress(), type.getFromName());
					message.setSubject(type.getSubject());
					
					if (log.isDebugEnabled()) {
						try {
							log.debug("Using Velocity Template: " + getTemplatePath() + type.getCode().toLowerCase() + ".vm");
							log.debug("Template Exists: " + velocityEngine.templateExists(getTemplatePath() + type.getCode().toLowerCase() + ".vm"));
							log.debug(velocityEngine.getTemplate(getTemplatePath() + type.getCode().toLowerCase() + ".vm"));
						} catch (Exception ex) {
							log.debug("Exception in Velocity Template: " + ex);
						}
					}
					String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, getTemplatePath() + type.getCode().toLowerCase() + ".vm", model);
					message.setText(text, true);
					if(saveContent){
						notification.setContent(text);
					}else{
						notification.setContent(type.getDescription());
					}
				}
			};
			// Save message in the Notification before you try and Send it
			try {
				this.mailSender.send(preparator);
			} catch (Exception ex) {
                if (ex != null && ex.getCause().getMessage() != null) {
                    notification.setErrorMessage(ex.getCause().getMessage());
                } else {
                    notification.setErrorMessage("Unknown error has occurred in sending an email.");
                }
			}

			if (notification.getContent() != null) {
				notification.setSentDate(new Date());
				notificationDao.save(notification);
			} else {
				if (log.isDebugEnabled())
					log.debug("Content is null.  This will happen because the Velocity Template is not loadable!!!");
			}

			if (StringUtils.isNotBlank(notification.getErrorMessage())) {
				throw new SendEmailException("An error occured when sending the email. error: " + notification.getErrorMessage());
			}
		}
		}catch(Exception e){ e.printStackTrace();}
	}

	/**
	 *Method to send email for new catalog with dynamic vendor name ready for data mapping 
	 */
	 public void sendEmailForCatalogDataMapping(final NotificationType type, final User user, final Map model) throws SendEmailException{

			
			final VendorCatalog catalog = (VendorCatalog) model.get("catalog");
			final String catalogName = catalog.getVendorCatalogName();
			
			//Verify that the email was set.
			final Notification notification = new Notification();
			if (StringUtils.isNotBlank(type.getFromEmailAddress())) {
				notification.setFromEmailAddress(type.getFromEmailAddress());
			} else {
				notification.setFromEmailAddress(NotificationType.DEFAULT_EMAIL);
			}
			
				notification.setNotificationType(type);
				notification.setSentDate(new Date());
				notification.setSubject(catalogName +" - " + type.getSubject());
				notification.setToEmailAddress(UserType.ECOM_OPERATOR);
				notification.setUpdatedBy(user.getUsername());
				notification.setCreatedBy(user.getUsername());
				notification.setUpdatedDate(new Date());
				notification.setCreatedDate(new Date());

				MimeMessagePreparator preparator = new MimeMessagePreparator() {
					public void prepare(MimeMessage mimeMessage) throws Exception {
						MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
						message.setTo((String[]) model.get("userEmail"));
						
						message.setFrom(type.getFromEmailAddress(), type.getFromName());
						message.setSubject(catalogName +" - " + type.getSubject());
						
						if (log.isDebugEnabled()) {
							try {
								log.debug("Using Velocity Template: " + getTemplatePath() + type.getCode().toLowerCase() + ".vm");
								log.debug("Template Exists: " + velocityEngine.templateExists(getTemplatePath() + type.getCode().toLowerCase() + ".vm"));
								log.debug(velocityEngine.getTemplate(getTemplatePath() + type.getCode().toLowerCase() + ".vm"));
							} catch (Exception ex) {
								log.debug("Exception in Velocity Template: " + ex);
							}
						}
						String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, getTemplatePath() + type.getCode().toLowerCase() + ".vm", model);
						message.setText(text, true);
						notification.setContent(text);

					}
				};

				// Save message in the Notification before you try and Send it
				try {
					this.mailSender.send(preparator);
				} catch (Exception ex) {
                    if (ex != null && ex.getCause().getMessage() != null) {
                        notification.setErrorMessage(ex.getCause().getMessage());
                    } else {
                        notification.setErrorMessage("Unknown error has occurred in sending an email.");
                    }
				}

				if (notification.getContent() != null) {
					notification.setSentDate(new Date());
					notificationDao.save(notification);
				} else {
					if (log.isDebugEnabled())
						log.debug("Content is null.  This will happen because the Velocity Template is not loadable!!!");
				}

				if (StringUtils.isNotBlank(notification.getErrorMessage())) {
					throw new SendEmailException("An error occured when sending the email. error: " + notification.getErrorMessage());
				}
			}
		
	
	public void sendEmailForRequest(final NotificationType type, final User user,
			final Map model, final String requestId) throws SendEmailException {
		
		final Notification notification = new Notification();
		if (StringUtils.isNotBlank(type.getFromEmailAddress())) {
			notification.setFromEmailAddress(type.getFromEmailAddress());
		} else {
			notification.setFromEmailAddress(NotificationType.DEFAULT_EMAIL);
		}
		notification.setNotificationType(type);
		notification.setSentDate(new Date());
		notification.setSubject(type.getSubject() + requestId);
		notification.setToEmailAddress(UserType.ECOM_OPERATOR);
		notification.setUpdatedBy(user.getFullName());
		notification.setCreatedBy(user.getFullName());
		notification.setUpdatedDate(new Date());
		notification.setCreatedDate(new Date());

		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				message.setTo((String[]) model.get("user"));
				
				message.setFrom(type.getFromEmailAddress(), type.getFromName());
				message.setSubject(type.getSubject() + requestId);
				if (log.isDebugEnabled()) {
					try {
						log.debug("Using Velocity Template: " + getTemplatePath() + type.getCode().toLowerCase() + ".vm");
						log.debug("Template Exists: " + velocityEngine.templateExists(getTemplatePath() + type.getCode().toLowerCase() + ".vm"));
						log.debug(velocityEngine.getTemplate(getTemplatePath() + type.getCode().toLowerCase() + ".vm"));
					} catch (Exception ex) {
						log.debug("Exception in Velocity Template: " + ex);
					}
				}
				String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, getTemplatePath() + type.getCode().toLowerCase() + ".vm", model);
				message.setText(text, true);
				notification.setContent(text);

			}
		};

		// Save message in the Notification before you try and Send it
		try {
			this.mailSender.send(preparator);
		} catch (Exception ex) {
			 log.error("Error sending email... cause: " +
			 ex.getCause().getMessage() );
		}
	}

	
	@Override
	public void sendVendorImageRejectionEmail(final NotificationType type,
			User user, final Map model, final boolean isMQCheck) {
		log.info("------- Entering Email Manager ------");
		try {
			final String toEmailAddress = (String) model.get("emailAddress");
			if (StringUtils.isNotBlank(toEmailAddress)) {
				final Notification notification = new Notification();
				notification.setFromEmailAddress(type.getFromEmailAddress());
				notification.setNotificationType(type);
				notification.setSentDate(new Date());
				notification.setSubject(type.getSubject());
				notification.setToEmailAddress(toEmailAddress);
				notification.setUpdatedBy(user.getUsername());
				notification.setCreatedBy(user.getUsername());
				notification.setUpdatedDate(new Date());
				notification.setCreatedDate(new Date());

				MimeMessagePreparator preparator = new MimeMessagePreparator() {
					public void prepare(MimeMessage mimeMessage)
							throws Exception {
						MimeMessageHelper message = new MimeMessageHelper(
								mimeMessage);
						message.setTo(toEmailAddress);
						//message.setTo("Srinivas_Gade@Belk.com");
						message.setFrom(type.getFromEmailAddress(),
								type.getFromName());
						message.setSubject(type.getSubject());
						if (log.isDebugEnabled()) {
							try {
								log.debug("Using Velocity Template: "+ getTemplatePath()
																     + type.getCode().toLowerCase() + ".vm");
								log.debug("Template Exists: "+ velocityEngine.templateExists(getTemplatePath()
																	 + type.getCode().toLowerCase()+ ".vm"));
								log.debug(velocityEngine.getTemplate(getTemplatePath()
															    	 + type.getCode().toLowerCase()+ ".vm"));
							} catch (Exception ex) {
								log.debug("Exception in Velocity Template: "+ ex);
							}
						}
						String text = VelocityEngineUtils
								.mergeTemplateIntoString(velocityEngine,getTemplatePath()
															+ type.getCode().toLowerCase()+ ".vm", model);
						message.setText(text, true);
						if (isMQCheck) {
							notification.setContent(text);
						} else {
							notification.setContent(type.getDescription());
						}
					}
				};
				try {
					log.info(" ---- Preparing an email message ----");
					this.mailSender.send(preparator);
					log.info("--- Email sent to user :---");
				} catch (Exception ex) {
					log.error("Unknown error has occurred in sending an email.");
					notification
							.setErrorMessage("Unknown error has occurred in sending an email.");
					notification.setErrorMessage(ex.getMessage());
				}
				if (notification.getContent() != null) {
					notification.setSentDate(new Date());
					notificationDao.save(notification);
				} else {
					if (log.isDebugEnabled())
						log.debug("Content is null.  This will happen because the Velocity Template is not loadable!!!");
				}
				if (StringUtils.isNotBlank(notification.getErrorMessage())) {
					throw new SendEmailException(
							"An error occured when sending the email. error: "
									+ notification.getErrorMessage());
				}
			}
		} catch (Exception e) {
			log.error("Error has occurred in sending an email." + e);

		}
	}
}
