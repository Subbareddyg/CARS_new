/**
 * 
 */
package com.belk.car.app.webapp.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.AccessDeniedException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.springframework.mail.MailException;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.service.EmailManager;
import com.belk.car.app.webapp.forms.VendorCreationForm;
import com.belk.car.util.PasswordGenerator;

/**
 * @author antoniog
 * 
 */
public class ActivateVendorFormController extends BaseFormController {

	private transient final Log log = LogFactory
			.getLog(ActivateVendorFormController.class);

	private static final String ROLE_USER = "ROLE_USER";	
	private static final String VENDOR = "VENDOR";
	
	private EmailManager emailManager;

	public void setEmailManager(EmailManager emailManager) {
		this.emailManager = emailManager;
	}

	public ActivateVendorFormController() {
		setCommandName("vendorCreationForm");
		setCommandClass(VendorCreationForm.class);
	}

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		
		if (log.isDebugEnabled()) {
			log.debug("entering new vendor account 'onSubmit' method...");
		}

		VendorCreationForm vendorForm = (VendorCreationForm) command;
		Locale locale = request.getLocale();

		String generatedPassword = PasswordGenerator.getPassword();

		User dbVendor = getUserManager().getUser(vendorForm.getVendorId());

		if (dbVendor != null) {
				User newUser = createUserFromVendor(request,dbVendor,vendorForm);
				newUser.setPassword(generatedPassword);
			try {
				this.getUserManager().saveUser(newUser);
			} catch (AccessDeniedException ade) {				
				log.warn(ade.getMessage());
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return null;
			} catch (Exception e) {
				errors.rejectValue("emailAddress", "errors.existing.email",
						new Object[] { newUser.getEmailAddress() }, "duplicate email");	
				saveMessage(request, getText("errors.existing.email", newUser.getEmailAddress(),
						locale));
				//return showForm(request, response, errors);
				return new ModelAndView("redirect:/signin.html?vendorUserId="+vendorForm.getVendorId());
			}

			saveMessage(request, getText("user.registered", newUser.getUsername(),
					locale));
			request.getSession().setAttribute(Constants.REGISTERED,
					Boolean.TRUE);

			// Send user an e-mail
			if (log.isDebugEnabled()) {
				log.debug("Sending user '" + newUser.getUsername()
						+ "' an account information e-mail");
			}

			// Send a new account information e-mail
			// Send Email with the new generated password
			NotificationType type = getCarLookupManager().getNotificationType(
					NotificationType.NEW_REGISTRATION);

			HashMap model = new HashMap();
			model.put("userName", dbVendor.getUsername());
			model.put("password", generatedPassword);

			try {
				emailManager.sendEmail(type, newUser, model);
			} catch (MailException me) {
				saveError(request, me.getCause().getLocalizedMessage());
			}
			return new ModelAndView(getFormView());

		} else {

			saveMessage(request, getText("vendor.activate.vendor.failure",
					vendorForm.getVendorId(), locale));
			request.getSession().setAttribute(Constants.NOT_ACIVATED,
					Boolean.TRUE);
			return new ModelAndView(getFormView());
		}

	}

	private User createUserFromVendor(HttpServletRequest request, User dbVendor, VendorCreationForm vendorForm) {
		User user = new User();
		user.setLastName(vendorForm.getLastName());
		user.setFirstName(vendorForm.getFirstName());
		user.setEmailAddress(vendorForm.getEmailAddress());
		user.setCreatedBy(dbVendor.getUsername());
		user.setUpdatedBy(dbVendor.getUsername());
		user.setCreatedDate(new Date());
		user.setUpdatedDate(new Date());
		user.setIsLocked("N");
		user.setAdministrator("N");
		user.setStatusCd(Status.ACTIVE);
		user.setLastLoginDate(new Date());
		user.setUsername(vendorForm.getEmailAddress());
		
		for(Department dept : dbVendor.getDepartments()) {
			user.addDepartment(dept);
		}
		for(Vendor vendor : dbVendor.getVendors()) {
			user.addVendor(vendor);
		}		
		user.addRole(getCarLookupManager().getRole(ROLE_USER));
		user.setUserType(getCarLookupManager().getUserType(VENDOR));
		return user;
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		String vendorId = request.getParameter("vendorUserId");

		VendorCreationForm vendorCreationForm = new VendorCreationForm();
		
		if(StringUtils.isNotBlank(vendorId)) 
			vendorCreationForm.setVendorId(vendorId);
		
		User user = null;
		if (!StringUtils.isBlank(vendorId)) {
			user = getUserManager().getUser(vendorId);
			return populateVendorForm(user, vendorCreationForm);
		} else {
			return vendorCreationForm;
		}

	}

	private Object populateVendorForm(User user,
			VendorCreationForm vendorCreationForm) {

		for (Vendor vendor : user.getVendors()) {
			vendorCreationForm.addVendor(vendor.getName());
		}
		for (Department department : user.getDepartments()) {
			vendorCreationForm.addDepartment(department.getName());
		}
		return vendorCreationForm;
	}

}
