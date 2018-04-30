package com.belk.car.app.webapp.validators;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.belk.car.app.model.UserType;

/**
 * @author Antonio Gonzalez
 * 
 */
public class VendorValidator implements Validator {

	public boolean supports(Class clazz) {
		return User.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		User user = (User)obj;	    		 
		if (user.getRoles() != null && user.getRoles().size() > 0 && StringUtils.isNotBlank(user.getUserTypeCd())) {
			String userTypeCd = user.getUserTypeCd();
			//Check to see if the user selected any other role other than Car User and that the  is usertype = 'VENDOR'
			for(Role role : user.getRoles()) {
				if(!role.getName().equalsIgnoreCase(Role.ROLE_USER) && userTypeCd.equalsIgnoreCase(UserType.VENDOR)) {
					Object[] args = {userTypeCd};
					errors.rejectValue("userTypeCd", "user.type.and.role.not.allowed", args, "A vendor can only be assigned to a role of type Cars User");
				}
			}			
		}
	}
}
