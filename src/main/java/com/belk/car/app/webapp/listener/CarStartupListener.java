package com.belk.car.app.webapp.listener;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.acegisecurity.providers.AuthenticationProvider;
import org.acegisecurity.providers.ProviderManager;
import org.acegisecurity.providers.dao.DaoAuthenticationProvider;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.acegisecurity.providers.rememberme.RememberMeAuthenticationProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.belk.car.app.Constants;
import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.AttributeValueProcessStatus;
import com.belk.car.app.model.Config;
import com.belk.car.app.model.ContentStatus;
import com.belk.car.app.model.NoteType;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.model.SourceType;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.VendorStyleType;
import com.belk.car.app.model.WorkFlow;
import com.belk.car.app.model.car.ManualCarProcessStatus;
import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.model.workflow.WorkflowType;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.WorkflowManager;
import com.belk.car.app.util.PropertyLoader;

public class CarStartupListener implements ServletContextListener {
	private static final Log log = LogFactory.getLog(CarStartupListener.class);

    @SuppressWarnings({"unchecked"})
    public void contextInitialized(ServletContextEvent event) {
        log.debug("Initializing context...");

        ServletContext context = event.getServletContext();

        // Orion starts Servlets before Listeners, so check if the config
        // object already exists
        Map<String, Object> config = (HashMap<String, Object>) context.getAttribute(Constants.CONFIG);

        if (config == null) {
            config = new HashMap<String, Object>();
        }
        
        if (context.getInitParameter(Constants.CSS_THEME) != null) {
            config.put(Constants.CSS_THEME, context.getInitParameter(Constants.CSS_THEME));
        }

        ApplicationContext ctx =
            WebApplicationContextUtils.getRequiredWebApplicationContext(context);

        PasswordEncoder passwordEncoder = null;
        try {
            ProviderManager provider = (ProviderManager) ctx.getBean("authenticationManager");
            for (Object o : provider.getProviders()) {
                AuthenticationProvider p = (AuthenticationProvider) o;
                if (p instanceof RememberMeAuthenticationProvider) {
                    config.put("rememberMeEnabled", Boolean.TRUE);
                } else if (p instanceof DaoAuthenticationProvider) {
                    passwordEncoder = ((DaoAuthenticationProvider) p).getPasswordEncoder();
                }
            }
        } catch (NoSuchBeanDefinitionException n) {
            log.debug("authenticationManager bean not found, assuming test and ignoring...");
            // ignore, should only happen when testing
        }

        context.setAttribute(Constants.CONFIG, config);

        // output the retrieved values for the Init and Context Parameters
        if (log.isDebugEnabled()) {
            log.debug("Remember Me Enabled? " + config.get("rememberMeEnabled"));
            if (passwordEncoder != null) {
                log.debug("Password Encryptor: " + passwordEncoder.getClass().getName());
            }
            log.debug("Populating drop-downs...");
        }

        setupContext(context);
    }

    /**
     * This method uses the LookupManager to lookup available roles from the data layer.
     * @param context The servlet context
     */
    public static void setupContext(ServletContext context) {
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
        CarLookupManager lookupMgr = (CarLookupManager) ctx.getBean("carLookupManager");
        CarManager carMgr = (CarManager) ctx.getBean("carManager");
        WorkflowManager workflowMgr = (WorkflowManager) ctx.getBean("workflowManager");
        UserManager userMgr = (UserManager) ctx.getBean("userManager");
      
        /*
         * Added by syntel
         * to read the FTP config properties file
         * using PropertyLoader class
         */
        context.setAttribute(DropShipConstants.FTPDETAILS, PropertyLoader.loadProperties(DropShipConstants.FTP_PROPERTIES));

        // get list of possible roles
        context.setAttribute(Constants.AVAILABLE_ROLES, lookupMgr.getAllRoles());        
        context.setAttribute(Constants.AVAILABLE_USER_TYPES, lookupMgr.getActiveUserTypes());
        /*
         * Added by  syntel.
         * get list of available source type
         */
        context.setAttribute(DropShipConstants.AVAILABLE_SOURCE_TYPES, lookupMgr.getAllSourceType());

        //get list of available features
        //context.setAttribute(Constants.AVAILABLE_FEATURES, mgr.getAllFeatures());
       // log.debug("Drop-down initialization complete [OK]");
        context.setAttribute("imageLocationTypes", carMgr.getAllImageLocationTypes());        
        context.setAttribute("imageTypes", carMgr.getAllImageTypes());        
        context.setAttribute("imageProviders",carMgr.getAllImageProviders());        
        context.setAttribute("shippingTypes", carMgr.getAllShippingTypes());        
        context.setAttribute("imageSourceTypes", lookupMgr.getAllImageSourceTypes());        
        context.setAttribute("sampleSourceTypes", lookupMgr.getAllSampleSourceTypes());        
        context.setAttribute("workflowStatuses", workflowMgr.getAllWorkFlowStatuses());
        context.setAttribute("contentStatuses", lookupMgr.getAllContentStatuses());        
        context.setAttribute("patternProcessingRules", lookupMgr.getAllPatternProcessingRules());        

        context.setAttribute("vendorStyleTypes", lookupMgr.getAllVendorStyleTypes());
        context.setAttribute("buildDate", new Date());

        
        //added for sustem user detials for MQ jobs.
        Config importCarUserName = (Config) lookupMgr.getById(Config.class, Config.CAR_IMPORT_USER);
		User systemUser = userMgr.getUserByUsername(importCarUserName.getValue());
		log.info("System User in Carstartup Listner >>>>>>>>>>"+systemUser);
		SourceType sourcePOCar = carMgr.getSourceTypeForCode(SourceType.PO);
		SourceType sourceManualCar =carMgr.getSourceTypeForCode(SourceType.MANUAL);
		SourceType sourceDropshipCar =carMgr.getSourceTypeForCode(SourceType.DROPSHIP);
		
		WorkFlow defaultWorkflow = workflowMgr.getWorkFlow(workflowMgr.getWorkflowType(WorkflowType.CAR));
		WorkflowStatus initiated = (WorkflowStatus)lookupMgr.getById(WorkflowStatus.class, WorkflowStatus.INITIATED);
		WorkflowStatus withVendor = (WorkflowStatus) lookupMgr.getById(WorkflowStatus.class, WorkflowStatus.WITH_VENDOR);
				
		UserType buyer = (UserType)lookupMgr.getById(UserType.class, UserType.BUYER);
		UserType vendorType = (UserType)lookupMgr.getById(UserType.class, UserType.VENDOR);
		
		AttributeValueProcessStatus checkRequired = lookupMgr.getAttributeValueProcessStatus(AttributeValueProcessStatus.CHECK_REQUIRED);
		AttributeValueProcessStatus noCheckRequired = lookupMgr.getAttributeValueProcessStatus(AttributeValueProcessStatus.NO_CHECK_REQUIRED);
						
		NoteType carNoteType = (NoteType) lookupMgr.getById(NoteType.class, NoteType.CAR_NOTES);
		ContentStatus contentInProgress = (ContentStatus) lookupMgr.getById(ContentStatus.class, ContentStatus.IN_PROGRESS);
		VendorStyleType vendorStyleTypeProduct =(VendorStyleType)lookupMgr.getById(VendorStyleType.class, VendorStyleType.PRODUCT);
		
		ManualCarProcessStatus completed = (ManualCarProcessStatus) carMgr.getFromId(ManualCarProcessStatus.class, ManualCarProcessStatus.COMPLETED);
		ManualCarProcessStatus completeWithError = (ManualCarProcessStatus) carMgr.getFromId(ManualCarProcessStatus.class, ManualCarProcessStatus.COMPLETED_WITH_ERROR);
		
		NotificationType type = lookupMgr.getNotificationType(NotificationType.SYSTEM_FAILURE);
		Config sendNotifications = (Config) lookupMgr.getById(Config.class, Config.SEND_EMAIL_NOTIFICATIONS);
		Config emailNotificationList = (Config) lookupMgr.getById(Config.class, Config.SCHEDULED_PROCESS_ERROR_NOTIFICATION_LIST);
		
				
		context.setAttribute(Constants.SYSTEM_USER, systemUser);
		context.setAttribute(Constants.SOURCE_PO_CAR, sourcePOCar);
		context.setAttribute(Constants.SOURCE_DROPSHIP_CAR, sourceDropshipCar);
		context.setAttribute(Constants.DEFAULT_WORKFLOW, defaultWorkflow);
		context.setAttribute(Constants.INITIATED, initiated);
		context.setAttribute(Constants.WITH_VENDOR, withVendor);
		context.setAttribute(Constants.BUYER, buyer);
		context.setAttribute(Constants.VENDOR_TYPE, vendorType);
		context.setAttribute(Constants.CHECK_REQUIRED, checkRequired);
		context.setAttribute(Constants.NO_CHECK_REQUIRED, noCheckRequired);
		context.setAttribute(Constants.CAR_NOTE_TYPE, carNoteType);
		context.setAttribute(Constants.CONTENT_IN_PROGRESS, contentInProgress);
		context.setAttribute(Constants.VENDOR_STYLE_TYPE_PRODUCT, vendorStyleTypeProduct);
		context.setAttribute(Constants.SOURCE_MANUAL_CAR, sourceManualCar);
		context.setAttribute(Constants.MANUAL_CAR_STATUS_COMPLETED, completed);
		context.setAttribute(Constants.MANUAL_CAR_STATUS_COMPLETE_WITH_ERROR, completeWithError);
		context.setAttribute(Constants.NOTIFICATION_TYPE_SYSTEM_FAILURE, type);
		context.setAttribute(Constants.SEND_EMAIL_NOTIFICATIONS, sendNotifications);
		context.setAttribute(Constants.SCHEDULED_PROCESS_ERROR_NOTIFICATION_LIST, emailNotificationList);
		
		
		

        if (log.isDebugEnabled())
        	log.debug("Drop-down initialization complete [OK]");

    }

    /**
     * Shutdown servlet context (currently a no-op method).
     *
     * @param servletContextEvent The servlet context event
     */
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //LogFactory.release(Thread.currentThread().getContextClassLoader());
        //Commented out the above call to avoid warning when SLF4J in classpath.
        //WARN: The method class org.apache.commons.logging.impl.SLF4JLogFactory#release() was invoked.
        //WARN: Please see http://www.slf4j.org/codes.html for an explanation.
    }
}
