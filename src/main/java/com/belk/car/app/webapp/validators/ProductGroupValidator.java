package com.belk.car.app.webapp.validators;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.belk.car.app.webapp.forms.ProductTypeGroupForm;

/**
 * Validator for adding or editing the group
 * 
 * @author AFUSXS7
 * 
 */
public class ProductGroupValidator implements Validator {

	private transient final Log log = LogFactory.getLog(ProductGroupValidator.class);
	public boolean supports(Class clazz) {
		return ProductTypeGroupForm.class.isAssignableFrom(clazz);
	}

	/**
	 * Validates the input values for the form and throws appropriate error
	 * messages when the rules are not met.
	 */
	public void validate(Object obj, Errors errors) {
		ProductTypeGroupForm productTypeGroupForm = (ProductTypeGroupForm) obj;
		String groupName = productTypeGroupForm.getName();
		if (groupName == null
				|| (groupName != null && groupName.trim().length() < 1)) {
			log.debug("In ProductGroupValidator : Group Name is null = "
					+ groupName);
			errors.rejectValue("name", "Product Group Name cannot be empty");
		}
		else if (groupName != null) {
			if (!StringUtils.isAlphaSpace(groupName)
					&& !StringUtils.isAlphanumericSpace(groupName)
					&& !StringUtils.isAlphanumeric(groupName)) {
				log.debug("In ProductGroupValidator : Error in Group Name"
						+ groupName);
				errors.rejectValue("name",
						"Please enter an Alpha Numeric string");
			}
			else if (StringUtils.isNumeric(groupName)) {
				log.debug("In ProductGroupValidator : Error in Group Name"
						+ groupName);
				errors.rejectValue("name",
						"Please enter an Alpha Numeric string");
			}
		}

		String description = productTypeGroupForm.getDescription();
		if (description == null
				|| (description != null && description.length() < 1)) {
			log.debug("In ProductGroupValidator : Group Description is null = "
					+ description);
			errors.rejectValue("description",
					"Product Group Description cannot be empty");
		}
	}
	
	

}
