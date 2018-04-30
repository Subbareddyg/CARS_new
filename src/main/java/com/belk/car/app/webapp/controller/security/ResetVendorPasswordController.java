package com.belk.car.app.webapp.controller.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.exceptions.SendEmailException;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.model.Status;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.EmailManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.util.PasswordGenerator;

public class ResetVendorPasswordController extends BaseFormController {

	private UserManager userManager;

	private EmailManager emailManager ;
	
	private CarLookupManager carLookupManager;

	private String mainView ;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

	public void setEmailManager(EmailManager emailManager) {
		this.emailManager = emailManager;
	}

	public void setCarLookupManager(CarLookupManager carLookupManager) {
		this.carLookupManager = carLookupManager;
	}

	public String getMainView() {
		return mainView;
	}

	public void setMainView(String mainView) {
		this.mainView = mainView;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String username = request.getParameter("userName");
		if (StringUtils.isNotBlank(username)) {
			User user = userManager.getUserByUsername(username);
			if (user != null) {
				if (user.isVendor() && user.getStatusCd().equalsIgnoreCase(Status.INACTIVE)) {
					// don't reset password for inactive vendor users
					return new ModelAndView(this.getCancelView(), "id", user.getId().toString());
				}
				//Reset the Users Password
				String newPassword = PasswordGenerator.getPassword();
				user.setPassword(newPassword);
				
				//Update User with the new password 
				userManager.saveUser(user);
				
				// Send Email with the new password
				NotificationType type = carLookupManager.getNotificationType(NotificationType.FORGOT_PASSWORD);
				
				HashMap model = new HashMap() ;
				model.put("newPassword", newPassword);
				try {
					emailManager.sendEmail(type, user, model);
				} catch (SendEmailException see) {
					log.debug("Error in sending password!!!");
				}
			}			
			this.saveMessage(request, "message.resetpassword.success");
			return new ModelAndView(this.getSuccessView(), "id", user.getId().toString());			
		}
		
		return null;
	}

    public static final String MESSAGES_KEY = "successMessages";
    public void saveMessage(HttpServletRequest request, String msg) {
        List messages = (List) request.getSession().getAttribute(MESSAGES_KEY);

        if (messages == null) {
            messages = new ArrayList();
        }

        messages.add(msg);
        request.getSession().setAttribute(MESSAGES_KEY, messages);
    }

}
