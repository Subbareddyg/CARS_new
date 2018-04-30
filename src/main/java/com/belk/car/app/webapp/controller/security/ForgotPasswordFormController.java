package com.belk.car.app.webapp.controller.security;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.appfuse.model.User;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.exceptions.SendEmailException;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.service.EmailManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.ForgotPasswordForm;
import com.belk.car.util.PasswordGenerator;

public class ForgotPasswordFormController extends BaseFormController {

	private EmailManager emailManager ;
	
	public void setEmailManager(EmailManager emailManager) {
		this.emailManager = emailManager;
	}
	
	public ForgotPasswordFormController() {
		setCommandName("forgotPasswordForm");
		setCommandClass(ForgotPasswordForm.class);
	}

	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors) throws Exception {
		
		super.onBindAndValidate(request, command, errors);
		if (isFormSubmission(request)) {
			if (!errors.hasErrors()) {
				ForgotPasswordForm form = (ForgotPasswordForm) command;
				String email = form.getEmailAddress();
				
				User user = null;
				try {
					user = getUserManager().getUserByUsername(email);
				} catch (UsernameNotFoundException uex) {
					if (log.isDebugEnabled())
						log.debug("User " + email + " not found!!!");
				}
				if (user == null || user.isVendor()) {
					errors.reject("errors.invalid", new String[]{"Username"}, "");	
				} else {
					form.setUser(user);
				}
			}
		}
	}

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		if (log.isDebugEnabled())
			log.debug("entering  forgotPasswordForm 'onSubmit' method...");

		ForgotPasswordForm form = (ForgotPasswordForm) command;	

		//User user = getUserManager().getUserByUsername(email);
		
		User user = form.getUser();
		if (user != null) {
			//Reset the Users Password
			String newPassword = PasswordGenerator.getPassword();
			user.setPassword(newPassword);
			
			//Update User with the new password 
			this.getUserManager().saveUser(user);
			
			// Send Email with the new password
			NotificationType type = getCarLookupManager().getNotificationType(NotificationType.FORGOT_PASSWORD);
			
			HashMap model = new HashMap() ;
			model.put("newPassword", newPassword);

			try {
				emailManager.sendEmail(type, user, model);
			} catch (SendEmailException see) {
				log.debug("Error in sending password!!!");
			}
		}
		return new ModelAndView(getSuccessView());
	}

//	protected Object formBackingObject(HttpServletRequest request)
//			throws Exception {
//
//		String userId = request.getParameter("id");
//		UserForm userForm = new UserForm();
//		User user;
//		if (userId == null && !isAddUserNote(request)) {
//			user = getUserManager().getUserByUsername(request.getRemoteUser());
//		} else if (!StringUtils.isBlank(userId)) {
//			user = getUserManager().getUser(userId);
//		} else {
//			user = new User();
//			user.addRole(new Role(Constants.USER_ROLE));
//		}
//		userForm.setUser(user);
//		return userForm;
//	}
//
//
//	protected boolean isAddUserNote(HttpServletRequest request) {
//		String method = request.getParameter("method");
//		return (method != null && method.equalsIgnoreCase("addUserNote"));
//	}
//	
//	protected Map referenceData(HttpServletRequest request, Object command,
//			Errors errors) throws ServletRequestBindingException {
//
//		Map model = new HashMap();
//		List<NoteType> test =  carLookupManager.getAllNoteTypes();
//		model.put("noteTypes", test);
//		return model;
//	}
}
