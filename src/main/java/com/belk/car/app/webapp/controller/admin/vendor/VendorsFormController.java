/**
 * 
 */
package com.belk.car.app.webapp.controller.admin.vendor;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
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
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.exceptions.SendEmailException;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.UserType;
import com.belk.car.app.service.CarUserManager;
import com.belk.car.app.service.EmailManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.util.PasswordGenerator;

/**
 * @author antoniog
 * 
 */

public class VendorsFormController extends BaseFormController {
	private RoleManager roleManager;
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

	public VendorsFormController() {
		setCommandName("user");
		setCommandClass(User.class);
	}

	public ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		if (request.getParameter("cancel") != null) {
			if (!StringUtils.equals(request.getParameter("from"), "list")) {
				return new ModelAndView(getCancelView());
			} else {
				return new ModelAndView(getSuccessView());
			}
		}

		return super.processFormSubmission(request, response, command, errors);
	}

	/*
	private List<String> processErrors(BindException errors) {

		List<String> errorsList = new ArrayList<String>();
		for (Object error : errors.getFieldErrors()) {
			if (error instanceof FieldError) {
				FieldError field = (FieldError) error;//down cast				
				errorsList.add(getText(field.getCode(), field.getRejectedValue().toString(), Locale.ENGLISH));
			}
		}

		return errorsList;
	}
	*/

	public String getMainRedirectView() {
		return "redirect:mainMenu.html";
	}

	public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		log.debug("entering userForm 'onSubmit' method...");

		User user = (User) command;
		Locale locale = request.getLocale();

		//String action = getCurrentAction(request);
		String nextView = getSuccessView();
		Map<String, Object> model = new HashMap<String, Object>();

		String phoneNumber = user.getPhoneAreaCode() + user.getPhoneNumber1() + user.getPhoneNumber2();
		String altPhoneNumber = user.getAltPhoneAreaCode() + user.getAltPhoneNumber1() + user.getAltPhoneNumber2();
		user.setPhone(phoneNumber);
		user.setAltPhone(altPhoneNumber);

		String userType = user.getUserTypeCd();

		// Set the Username to EmailAddress
		user.setUsername(user.getEmailAddress());
		Integer originalVersion = user.getVersion();
		user.setUserTypeCd(userType);

		boolean isProfileUpdate = (user.getId() != null && user.getId().longValue() > 0);

		populateUser(request, user);
		if (!isProfileUpdate) {
			String generatedPassword = PasswordGenerator.getPassword();
			user.setPassword(generatedPassword);
			model.put("newPassword", generatedPassword);
		}

		try {
			if(log.isDebugEnabled()){
				log.debug(" user name:"+user.getUsername());
				log.debug(" checking user exists");
			}
			// Checking Vendor already exists with 'DELETED' status.
			User oldVendor=carUserManager.getVendorOrUser(user.getUsername());
			if(log.isDebugEnabled()){
				log.debug(" oldVendor:"+oldVendor);
			}
			//if Vendor already exists in 'DELETED' status, update the status 
			// and with vendor details.
			if(oldVendor!=null && !isProfileUpdate) {
				if(log.isDebugEnabled()){
					log.debug(" old vendor email:"+oldVendor.getEmailAddress());
					log.debug(" old vendor status:"+oldVendor.getStatusCd());
				}
				Long oldVendorId=oldVendor.getId();
				if(log.isDebugEnabled()){
					log.debug(" old vendor ID:"+oldVendorId);
					log.debug("updaing vendor ");
				}
				
				BeanUtils.copyProperties(oldVendor, user);
				oldVendor.setId(oldVendorId);
				oldVendor.setStatusCd(Status.ACTIVE);
				log.info("updating vendor already exist with 'DELETED' status for id : "+oldVendor.getId());
				carUserManager.updateVendorOrUser(oldVendor);
			}else {
				log.info("inserting vendor : "+user.getUsername());
				getUserManager().saveUser(user);
			}
		} catch (AccessDeniedException ade) {
			// thrown by UserSecurityAdvice configured in aop:advisor
			// userManagerSecurity
			if (log.isWarnEnabled())
				log.warn(ade.getMessage());

			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (UserExistsException e) {
			errors.rejectValue("username", "errors.existing.user", new Object[] { user.getEmailAddress() }, "duplicate e-mail");

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
			NotificationType type = this.getCarLookupManager().getNotificationType(NotificationType.NEW_REGISTRATION);
			try {
				emailManager.sendEmail(type, user, model);
			} catch (SendEmailException see) {
				// add failed email warning message
				saveError(request, see.getMessage());
			}
		}
		
		if (!StringUtils.equals(request.getParameter("from"), "list")) {
			saveMessage(request, getText("user.saved", user.getFullName(), locale));
			// return to main Menu
			nextView = getMainRedirectView();

		} else {
			if (StringUtils.isBlank(request.getParameter("version"))) {
				saveMessage(request, getText("user.added", user.getFullName(), locale));

				nextView = getSuccessView();
			} else {
				saveMessage(request, getText("user.updated.byAdmin", user.getFullName(), locale));
			}
		}

		return new ModelAndView(nextView, model);
	}

	/**
	 * @param user
	 */
	protected void populateUser(HttpServletRequest request, User user) {

		if (user.getAdmin()) {
			user.setAdministrator(Constants.FLAG_YES);
		} else {
			user.setAdministrator(Constants.FLAG_NO);
		}
		
		user.setIsLocked(Constants.FLAG_NO);
		user.setStatusCd(Status.ACTIVE);
		//TODO check with Vasan to make sure that this is the default role for vendors
		Role role = this.getCarLookupManager().getRole(Role.ROLE_USER);

		Set<Role> roles = new HashSet<Role>();
		roles.add(role);
		user.setRoles(roles);
		user.setUserType(this.getCarLookupManager().getUserType(UserType.VENDOR));
		user.setLastLoginDate(Calendar.getInstance().getTime());
		this.setAuditInfo(request, user) ;
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {

		if (!isFormSubmission(request)) {
			String userId = request.getParameter("id");

			// if user logged in with remember me, display a warning that they
			// can't change passwords
			if (log.isDebugEnabled())
				log.debug("checking for remember me login...");

			AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();
			SecurityContext ctx = SecurityContextHolder.getContext();

			if (ctx.getAuthentication() != null) {
				Authentication auth = ctx.getAuthentication();
				if (resolver.isRememberMe(auth)) {
					request.getSession().setAttribute("cookieLogin", "true");

					// add warning message
					saveMessage(request, getText("userProfile.cookieLogin", request.getLocale()));
				}
			}

			User user;
			if (userId == null && !isAdd(request)) {
				user = getUserManager().getUserByUsername(request.getRemoteUser());
			} else if (!StringUtils.isBlank(userId) && !"".equals(request.getParameter("version"))) {
				user = getUserManager().getUser(userId);
			} else {
				user = new User();
				// user.addRole(new Role());
			}

			user.setConfirmPassword(user.getPassword());

			return user;
		} else if (StringUtils.isNotBlank(request.getParameter("id")) && request.getParameter("cancel") == null) {
			// populate user object from database, so all fields don't need to
			// be hidden fields in form
			return getUserManager().getUser(request.getParameter("id"));
		}
		return super.formBackingObject(request);
	}

	protected void onBind(HttpServletRequest request, Object command) throws Exception {
		super.setValidateOnBinding(true);
	}

	protected boolean isAdd(HttpServletRequest request) {
		String method = request.getParameter("method");
		return (method != null && method.equalsIgnoreCase("add"));
	}

	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws ServletRequestBindingException {

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("loggedInUser", getLoggedInUser());
		return model;
	}

}
