package com.belk.car.app.webapp.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.MailEngine;
import org.appfuse.service.UserManager;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.belk.car.app.Constants;
import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.BaseAuditableModel;
import com.belk.car.app.model.SearchCriteria;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import java.util.Iterator;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

public class MultiActionFormController extends MultiActionController  {
	protected final transient Log log = LogFactory.getLog(getClass());
	public static final String MESSAGES_KEY = "successMessages";

	protected UserManager userManager = null;
	protected MailEngine mailEngine = null;
	protected SimpleMailMessage message = null;
	protected String templateName = null;
	protected String cancelView;
	protected String listView;
	protected String ajaxView;

       
	protected CarManager carManager;
	protected CarLookupManager carLookupManager;
	protected Map<String, String> searchResultsViewsMap;
    
	protected String successView;

    protected BindingResult errors ;

        
    public void setSuccessView(String successView) {
        this.successView = successView;
    }


         public String getSuccessView() {
        return successView;
        }
	public CarManager getCarManager() {
		return carManager;
	}

	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	public CarLookupManager getCarLookupManager() {
		return carLookupManager;
	}

	public void setCarLookupManager(CarLookupManager carLookupManager) {
		this.carLookupManager = carLookupManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public UserManager getUserManager() {
		return this.userManager;
	}

	@SuppressWarnings("unchecked")
	public void saveError(HttpServletRequest request, String error) {
		List errors = (List) request.getSession().getAttribute("errors");
		if (errors == null) {
			errors = new ArrayList();
		}
		errors.add(error);
		request.getSession().setAttribute("errors", errors);
	}

	@SuppressWarnings("unchecked")
	public void saveMessage(HttpServletRequest request, String msg) {
		List messages = (List) request.getSession().getAttribute(MESSAGES_KEY);

		if (messages == null) {
			messages = new ArrayList();
		}

		messages.add(msg);
		request.getSession().setAttribute(MESSAGES_KEY, messages);
	}

	/**
	 * Convenience method for getting a i18n key's value. Calling
	 * getMessageSourceAccessor() is used because the RequestContext variable is
	 * not set in unit tests b/c there's no DispatchServlet Request.
	 * 
	 * @param msgKey
	 * @param locale
	 *            the current locale
	 * @return
	 */
	public String getText(String msgKey, Locale locale) {
		return getMessageSourceAccessor().getMessage(msgKey, locale);
	}

	/**
	 * Convenient method for getting a i18n key's value with a single string
	 * argument.
	 * 
	 * @param msgKey
	 * @param arg
	 * @param locale
	 *            the current locale
	 * @return
	 */
	public String getText(String msgKey, String arg, Locale locale) {
		return getText(msgKey, new Object[] { arg }, locale);
	}

	/**
	 * Convenience method for getting a i18n key's value with arguments.
	 * 
	 * @param msgKey
	 * @param args
	 * @param locale
	 *            the current locale
	 * @return
	 */
	public String getText(String msgKey, Object[] args, Locale locale) {
		return getMessageSourceAccessor().getMessage(msgKey, args, locale);
	}

	/**
	 * Convenience method to get the Configuration HashMap from the servlet
	 * context.
	 * 
	 * @return the user's populated form from the session
	 */
	public Map getConfiguration() {
		Map config = (HashMap) getServletContext().getAttribute(Constants.CONFIG);

		// so unit tests don't puke when nothing's been set
		if (config == null) {
			return new HashMap();
		}

		return config;
	}

	/**
	 * Default behavior for FormControllers - redirect to the successView when
	 * the cancel button has been pressed.
	 */
	public ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		if (request.getParameter("cancel") != null) {
			return new ModelAndView(getCancelView());
		}
              return  super.handleRequest(request, response);
		//return super.processFormSubmission(request, response, command, errors);
	}

	/**
	 * Set up a custom property editor for converting form inputs to real
	 * objects
	 */
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.registerCustomEditor(Integer.class, null, new CustomNumberEditor(Integer.class, null, true));
		binder.registerCustomEditor(Long.class, null, new CustomNumberEditor(Long.class, null, true));
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		SimpleDateFormat dateFormat = new SimpleDateFormat(getText("date.format", request.getLocale()));
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
	}

	/**
	 * Convenience message to send messages to users, includes app URL as
	 * footer.
	 * 
	 * @param user
	 * @param msg
	 * @param url
	 */
	protected void sendUserMessage(User user, String msg, String url) {
		if (log.isDebugEnabled()) {
			log.debug("sending e-mail to user [" + user.getUsername() + "]...");
		}

		message.setTo(user.getFirstName() + " " + user.getLastName() + "<" + user.getUsername() + ">");

		Map<String, Serializable> model = new HashMap<String, Serializable>();
		model.put("user", user);

		// TODO: once you figure out how to get the global resource bundle in
		// WebWork, then figure it out here too. In the meantime, the Username
		// and Password labels are hard-coded into the template.
		// model.put("bundle", getTexts());
		model.put("message", msg);
		model.put("applicationURL", url);
		mailEngine.sendMessage(message, templateName, model);
	}

	public void setMailEngine(MailEngine mailEngine) {
		this.mailEngine = mailEngine;
	}

	public void setMessage(SimpleMailMessage message) {
		this.message = message;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * Indicates what view to use when the cancel button has been pressed.
	 */
	public final void setCancelView(String cancelView) {
		this.cancelView = cancelView;
	}

	/**
	 * Indicates what view to use when the cancel button has been pressed.
	 */
	public final void setAjaxView(String ajaxView) {
		this.ajaxView = ajaxView;
	}

	public final String getCancelView() {
		// Default to successView if cancelView is invalid
		if (this.cancelView == null || this.cancelView.length() == 0) {
			//return getSuccessView();
		}
		return this.cancelView;
	}
	
	/**
	 * @return the listView
	 */
	public String getListView() {
		return listView;
	}

	/**
	 * @param listView the listView to set
	 */
	public void setListView(String listView) {
		this.listView = listView;
	}

	public final boolean isAjax(HttpServletRequest request) {
		return "true".equals(request.getParameter("ajax"));
	}

	public String getAjaxView() {
		return this.ajaxView;
	}

	public String getCurrentAction(HttpServletRequest request) {
		String action = request.getParameter("action");
		if (StringUtils.isBlank(action)) {
			action = Constants.NONE;
		}
		return action;
	}

	public User getLoggedInUser() {
		User user = null;
		Authentication auth = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (auth.getPrincipal() instanceof UserDetails) {
			user = (User) auth.getPrincipal();
		}
		return user;
	}

	public void setAuditInfo(HttpServletRequest request, BaseAuditableModel model) {
		String user = getLoggedInUser().getUsername();
		if (user == null) {
			user = (String) request.getAttribute("username");
			if (user == null) {
				user = "Unknown";
			}
		}
		if (StringUtils.isBlank(model.getCreatedBy())) {
			model.setCreatedBy(user);
			model.setCreatedDate(new Date());
		}
		model.setUpdatedBy(user);
		model.setUpdatedDate(new Date());
	}

	/**
	 * @return the searchResultsViewsMap
	 */
	public Map<String, String> getSearchResultsViewsMap() {
		return searchResultsViewsMap;
	}

	/**
	 * @param searchResultsViewsMap
	 *            the searchResultsViewsMap to set
	 */
	public void setSearchResultsViewsMap(Map<String, String> searchResultsViewsMap) {
		this.searchResultsViewsMap = searchResultsViewsMap;
	}
	
	protected void saveSearchCriteria(HttpServletRequest request, String formName) {
		SearchCriteria attrSearchCriteria = new SearchCriteria() ;
		if (!request.getParameterMap().isEmpty()) {
			Enumeration params = request.getParameterNames() ;
			while( params.hasMoreElements()) {
				String paramName= (String) params.nextElement();
				String paramValue = request.getParameter(paramName);
				if (StringUtils.isNotEmpty(paramValue)) {
					attrSearchCriteria.getCriteria().put(paramName, StringUtils.defaultString(paramValue,"")) ;
				}
			}
		}
		request.getSession(true).setAttribute(formName,attrSearchCriteria);
	}
	
   protected void bind(HttpServletRequest request, Object command ) throws Exception {
            ServletRequestDataBinder  binder = createBinder(request,command);
            binder.bind(request);
            errors = binder.getBindingResult();
            List errList = errors.getAllErrors();

        Iterator itr = errList.iterator();
        int cnt = 0;
        while (cnt < errList.size()) {
            ObjectError err = (ObjectError) errList.get(cnt);
            //allErrors.add(err.getCode());
            log.debug("Error Message->" + err.getCode());
            cnt++;
        }
    }
	 
     public BindingResult getErrors() {
         return errors;
     }

     public void setErrors(BindingResult errors) {
         this.errors = errors;
     }

     public void saveErrors(HttpServletRequest request, Errors errors) {
        List allErrors = new ArrayList();

        if (allErrors == null) {
            allErrors = new ArrayList();
        }
        List errList = errors.getAllErrors();

        Iterator itr = errList.iterator();
        int cnt = 0;
        while (cnt < errList.size()) {
            ObjectError err = (ObjectError) errList.get(cnt);
            allErrors.add(err.getCode());
            log.debug("Error Message->" + err.getCode());
            cnt++;
        }

        request.setAttribute(DropShipConstants.ERRORS, allErrors);
    }


}
