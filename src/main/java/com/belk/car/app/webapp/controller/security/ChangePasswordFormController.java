package com.belk.car.app.webapp.controller.security;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.json.JSONObject;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.exceptions.SendEmailException;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.service.EmailManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.ChangePasswordForm;

public class ChangePasswordFormController extends BaseFormController {

	private EmailManager emailManager ;
	
	public void setEmailManager(EmailManager emailManager) {
		this.emailManager = emailManager;
	}
	
	public ChangePasswordFormController() {
		setCommandName("changePasswordForm");
		setCommandClass(ChangePasswordForm.class);
	}
	
	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors) throws Exception {
		
		super.onBindAndValidate(request, command, errors);
		if (isFormSubmission(request)) {
			if (!errors.hasErrors()) {
				ChangePasswordForm form = (ChangePasswordForm) command;				
				User user = this.getLoggedInUser();
				if (!user.getEmailAddress().equals(form.getEmailAddress())) {
					user = this.getUserManager().getUserByUsername(form.getEmailAddress());
				}
				form.setUser(user);
			}
		}
	}

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		if (log.isDebugEnabled())
			log.debug("entering  forgotPasswordForm 'onSubmit' method...");

		ChangePasswordForm form = (ChangePasswordForm) command;	

		
		User user = form.getUser();
		if (user != null) {
			//Reset the Users Password
			user.setPassword(form.getPassword());
			
			//Update User with the new password 
			this.getUserManager().saveUser(user);
			
			// Send Email with the new password
			NotificationType type = getCarLookupManager().getNotificationType(NotificationType.CHANGE_PASSWORD);
			
			HashMap model = new HashMap() ;
			model.put("newPassword", form.getPassword());

			try {
				emailManager.sendEmail(type, user, model);
			} catch (SendEmailException see) {
				log.debug("Error in sending password!!!");
			}
		}
		
		String nextView = this.getSuccessView();
		Map<String, Object> model = new HashMap<String, Object>();
		if (isAjax(request)) {
			model.put("success",true); //Fixed the PMD issue. 
			request.setAttribute("jsonObj", new JSONObject(model));
			nextView = getAjaxView() ;
		}
		return new ModelAndView(nextView, model);
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

		String userId = request.getParameter("id");
		ChangePasswordForm form = new ChangePasswordForm() ;
		if (form == null) {
			form = new ChangePasswordForm() ;
		}
		User user = null;
		if (StringUtils.isNotBlank(userId)){
			user = this.getUserManager().getUser(userId);
		} else {
			user = this.getLoggedInUser();
		}
		if (user != null) {
			form.setEmailAddress(user.getUsername());
		}
		return form;
	}
}
