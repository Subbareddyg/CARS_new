/**
 * 
 */
package com.belk.car.app.webapp.controller.admin.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationTrustResolver;
import org.acegisecurity.AuthenticationTrustResolverImpl;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserExistsException;
import org.json.JSONObject;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.exceptions.SendEmailException;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.model.Status;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarUserManager;
import com.belk.car.app.service.EmailManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.util.PasswordGenerator;

/**
 * @author antoniog
 * 
 */

public class UserFormController extends BaseFormController {
	private RoleManager roleManager;
	private CarLookupManager carLookupManager;
	private EmailManager emailManager;
	
	private CarUserManager carUserManager;
	
	public CarUserManager getCarUserManager() {
		return carUserManager;
	}

	public void setCarUserManager(CarUserManager carUserManager) {
		this.carUserManager = carUserManager;
	}

	public void setEmailManager(EmailManager emailManager) {
		this.emailManager = emailManager;
	}

	public EmailManager getEmailManager() {
		return emailManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public void setCarLookupManager(CarLookupManager carLookupManager) {
		this.carLookupManager = carLookupManager;
	}

	public CarLookupManager getCarLookupManager() {
		return this.carLookupManager;
	}

	public UserFormController() {
		setCommandName("user");
		setCommandClass(User.class);
	}

	public ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		
		String userId = request.getParameter("id");
		Map model = new HashMap();
		if(StringUtils.isNotBlank(userId)) {
			model.put("id", userId);
		}
		if (request.getParameter("cancel") != null) {
			if (!StringUtils.equals(request.getParameter("from"), "list")) {
				return new ModelAndView(getCancelView());
			} else {
				return new ModelAndView(getSuccessView(),model);
			}
		}
		else if(errors.hasErrors() && isAjax(request)) {			
			return new ModelAndView(doAjaxRequest(request, true, errors));
		}

		return super.processFormSubmission(request, response, command, errors);
	}

	public String doAjaxRequest(HttpServletRequest request, boolean hasErrors, BindException errors) {
		Map model2 = new HashMap();		
		if(hasErrors) {
			model2.put("messages",processErrors(errors));
		}
		
		model2.put("success", new Boolean(!hasErrors));
		request.setAttribute("jsonObj", new JSONObject(model2));
		return getAjaxView();
	}

	private List<String> processErrors(BindException errors) {
		
		List<String> errorsList = new ArrayList<String>();
		for(Object error : errors.getFieldErrors()) {
			 if(error instanceof FieldError) {
			   FieldError field = (FieldError)error;//down cast				
				 errorsList.add(getText(field.getCode(),
		                    field.getRejectedValue().toString(), Locale.ENGLISH));
			 }
		}
		
		return errorsList;
	}

	public String getMainRedirectView() {
		return "redirect:/admin/user/userDetails.html";
	}

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		log.debug("entering userForm 'onSubmit' method...");

		User user = (User) command;
		Locale locale = request.getLocale();

		String action = getCurrentAction(request);
		String nextView = getSuccessView();
		Map model = new HashMap();

		String phoneNumber = user.getPhoneAreaCode() + user.getPhoneNumber1()
				+ user.getPhoneNumber2();
		String altPhoneNumber = user.getAltPhoneAreaCode()
				+ user.getAltPhoneNumber1() + user.getAltPhoneNumber2();
		user.setPhone(phoneNumber);
		user.setAltPhone(altPhoneNumber);

		String userType = user.getUserTypeCd();

		// Set the Username to EmailAddress
		user.setUsername(user.getEmailAddress());

		Integer originalVersion = user.getVersion();
		user.setUserTypeCd(userType);

		boolean isProfileUpdate = (user.getId() != null && user.getId().longValue()> 0);
		
		populateUser(user, isProfileUpdate);
		if (!isProfileUpdate) {
			String generatedPassword = PasswordGenerator.getPassword();
			user.setPassword(generatedPassword);
			model.put("newPassword", generatedPassword);
		}

		// only attempt to change roles if user is admin for other users,
		// formBackingObject() method will handle populating
		if (request.isUserInRole(Constants.ADMIN_ROLE)) {
			Set<Role> selectedRoles = new HashSet();
			if (user.getRoles() != null && user.getRoles().size() > 0) {
				selectedRoles.addAll(user.getRoles());
				if (user.getRoles() != null) {
					user.getRoles().clear();
					for (Role role : selectedRoles) {
						user.addRole(carLookupManager.getRole(role.getId()));
					}
				}
			}
		}
		
		try {
			if(log.isDebugEnabled()){
				log.debug(" user name:"+user.getUsername());
				log.debug(" checking user exists");
			}
			// Checking User already exists with 'DELETED' status.
			
			User oldUser=carUserManager.getVendorOrUser(user.getUsername());
			if(log.isDebugEnabled()){
				log.debug(" oldUser:"+oldUser);
			}
			// if User already exists in 'DELETED' status, update the status 
			// and with user details.
			if(oldUser!=null && !isProfileUpdate) {
				if(log.isDebugEnabled()){
					log.debug(" old user email:"+oldUser.getEmailAddress());
					log.debug(" old user status:"+oldUser.getStatusCd());
				}
				Long oldId=oldUser.getId();
				if(log.isDebugEnabled()){
					log.debug(" old user ID:"+oldId);
					log.debug("updaing vendor ");
				}
				BeanUtils.copyProperties(oldUser, user);
				oldUser.setId(oldId);// resetting user id after copying user
				oldUser.setStatusCd(Status.ACTIVE);
				log.info("updating user already exist with 'DELETED' status for id : "+oldUser.getId());
				carUserManager.updateVendorOrUser(oldUser);
				user.setId(oldId);
			}else {
				log.info("inserting user : "+user.getUsername());
				getUserManager().saveUser(user);
			}
		} catch (AccessDeniedException ade) {
			// thrown by UserSecurityAdvice configured in aop:advisor
			// userManagerSecurity
			log.warn(ade.getMessage());
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (UserExistsException e) {
			errors
					.rejectValue("username", "errors.existing.user",
							new Object[] { user.getEmailAddress() }, "duplicate e-mail");

			// redisplay the unencrypted passwords
			//user.setPassword(generatedPassword);
			// reset the version # to what was passed in
			user.setVersion(originalVersion);
			user.setId(null);
			return showForm(request, response, errors);
			// return showForm(request, response, errors);
		}

		if (!isProfileUpdate) {
			// Send an account information e-mail
			NotificationType type = getCarLookupManager().getNotificationType(
					NotificationType.NEW_REGISTRATION);
			try {
				emailManager.sendEmail(type, user, model);
			} catch (SendEmailException see) {
				// add failed email warning message
				saveError(request, see.getMessage());
			}
		}
		if (!StringUtils.equals(request.getParameter("from"), "list")) {
			saveMessage(request, getText("user.saved", user.getFullName(),
					locale));
			if (isAjax(request)) {
				nextView = doAjaxRequest(request,false,null);
			} else {
				// return to main Menu
				nextView = getMainRedirectView();
			}
		} else {
			if (StringUtils.isBlank(request.getParameter("version"))) {
				saveMessage(request, getText("user.added", user.getFullName(),
						locale));

				nextView = getSuccessView();
			} else {
				saveMessage(request, getText("user.updated.byAdmin", user
						.getFullName(), locale));
			}
		}

		model.put("id", user.getId());
		return new ModelAndView(nextView, model);
	}

	/**
	 * @param user
	 */
	protected void populateUser(User user, boolean isUpdate) {
		if (user.getAdmin()) {
			user.setAdministrator(Constants.FLAG_YES);
		} else {
			user.setAdministrator(Constants.FLAG_NO);
		}
		user.setIsLocked(Constants.FLAG_NO);
		user.setStatusCd(Status.ACTIVE);
		user.setUserType(carLookupManager.getUserType(user.getUserTypeCd()));

		if (!isUpdate) {
			user.setCreatedBy(user.getUsername());
			user.setCreatedDate(new Date());
		}
		user.setUpdatedBy(user.getUsername());
		user.setUpdatedDate(new Date());
		user.setLastLoginDate(new Date());
	}

	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors)
			throws Exception {

		// If not an adminstrator, make sure user is not trying to add or edit
		// another user
		//
		//02/05/08
		//VASAN: Commented as VBuyer need to add VENDOR
		//
		/*
		if (!request.isUserInRole(Constants.ADMIN_ROLE)
				&& !isFormSubmission(request)) {
			if (isAdd(request) || request.getParameter("id") != null) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				log.warn("User '" + request.getRemoteUser()
						+ "' is trying to edit user with id '"
						+ request.getParameter("id") + "'");

				throw new AccessDeniedException(
						"You do not have permission to modify other users.");
			}
		}
		*/

		/*	if (isAjax(request) && errors.hasErrors()) {
				Map model2 = new HashMap();
				model2.put("failure", new Boolean(true));
				model2.put("errorsList",errors.getAllErrors());
				request.setAttribute("jsonObj", new JSONObject(model2));
				return new ModelAndView(getAjaxView(),model2);
			}*/

		return super.showForm(request, response, errors);
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

		if (!isFormSubmission(request)) {
			String userId = request.getParameter("id");

			// if user logged in with remember me, display a warning that they
			// can't change passwords
			log.debug("checking for remember me login...");

			AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();
			SecurityContext ctx = SecurityContextHolder.getContext();

			if (ctx.getAuthentication() != null) {
				Authentication auth = ctx.getAuthentication();

				if (resolver.isRememberMe(auth)) {
					request.getSession().setAttribute("cookieLogin", "true");

					// add warning message
					saveMessage(request, getText("userProfile.cookieLogin",
							request.getLocale()));
				}
			}

			User user;
			if (userId == null && !isAdd(request)) {
				user = getUserManager().getUserByUsername(
						request.getRemoteUser());
			} else if (!StringUtils.isBlank(userId)
					&& !"".equals(request.getParameter("version"))) {
				user = getUserManager().getUser(userId);
			} else {
				user = new User();
				// user.addRole(new Role(Constants.USER_ROLE));
			}

			user.setConfirmPassword(user.getPassword());

			return user;
		} else if (request.getParameter("id") != null
				&& !"".equals(request.getParameter("id"))
				&& request.getParameter("cancel") == null) {
			// populate user object from database, so all fields don't need to
			// be hidden fields in form
			return getUserManager().getUser(request.getParameter("id"));
		}
		return super.formBackingObject(request);
	}

	protected void onBind(HttpServletRequest request, Object command)
			throws Exception {
		super.setValidateOnBinding(true);
	}

	protected boolean isAdd(HttpServletRequest request) {
		String method = request.getParameter("method");
		return (method != null && method.equalsIgnoreCase("add"));
	}

	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws ServletRequestBindingException {

		Map model = new HashMap();
		model.put("loggedInUser", getLoggedInUser());
		return model;
	}

	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) {
		binder.registerCustomEditor(Set.class, "roles",
				new CustomCollectionEditor(Set.class) {
					protected Object convertElement(Object element) {
						String roleId = null;
						if (element instanceof Long)
							roleId = String.valueOf(element);
						else if (element instanceof String)
							roleId = (String) element;
						return carLookupManager.getRole(String.valueOf(roleId));
					}
				});
	}

}
