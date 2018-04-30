/**
 * 
 */
package com.belk.car.app.webapp.validators;

import org.appfuse.model.User;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author antoniog
 *
 */
public class ProductSearchValidator implements Validator {

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class clazz) {		
		return User.class.isAssignableFrom(clazz);
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	public void validate(Object target, Errors errors) {
		

	}

}
