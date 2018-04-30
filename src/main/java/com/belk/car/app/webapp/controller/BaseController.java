/**
 * 
 */
package com.belk.car.app.webapp.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.Constants;
import com.belk.car.app.model.BaseAuditableModel;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;

/**
 * @author antoniog
 *
 */
public abstract class BaseController implements Controller {
	
	protected CarLookupManager carLookupManager = null;
	protected CarManager carManager = null;
	protected UserManager userManager = null;
	private String successView;
	private String errorView;
	protected String ajaxView;
	 
	/**
	 * @return the errorView
	 */
	public String getErrorView() {
		return errorView;
	}
	/**
	 * @param errorView the errorView to set
	 */
	public void setErrorView(String errorView) {
		this.errorView = errorView;
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

	public String getSuccessView() {
		return successView;
	}

	public void setSuccessView(String successView) {
		this.successView = successView;
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
             user = (User)  auth.getPrincipal();
         }
         return user;
    }
    
    public void setAuditInfo(HttpServletRequest request, BaseAuditableModel model) {
    	String user = getLoggedInUser().getUsername();
    	if (user == null) {
    		user = (String) request.getAttribute("username") ;
    		if(user == null) {
    			user = "Unknown" ;
    		}
    	}
    	if (StringUtils.isBlank(model.getCreatedBy())) {
    		model.setCreatedBy(user);
        	model.setCreatedDate(new Date());
    	}
		model.setUpdatedBy(user);
    	model.setUpdatedDate(new Date());
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
    
    public final boolean isAjax(HttpServletRequest request) {
		return "true".equals(request.getParameter("ajax"));
	}

    public final void setAjaxView(String ajaxView) {
		this.ajaxView = ajaxView;
	}
    
	public String getAjaxView() {
		return this.ajaxView;
	}
}
