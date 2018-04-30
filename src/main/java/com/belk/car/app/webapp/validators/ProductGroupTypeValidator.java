package com.belk.car.app.webapp.validators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.belk.car.app.webapp.forms.ProductTypeGroupForm;

/**
 * Validator for associating the types for the group.
 * 
 * @author AFUSXS7
 * 
 */
public class ProductGroupTypeValidator implements Validator {

	private transient final Log log = LogFactory.getLog(ProductGroupTypeValidator.class);
	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return ProductTypeGroupForm.class.isAssignableFrom(clazz);
	}

	/**
	 * Validates the input values for the form and throws appropriate error
	 * messages when the rules are not met.
	 */
	public void validate(Object obj, Errors errors) {
		ProductTypeGroupForm productTypeGroupForm = (ProductTypeGroupForm) obj;
		
		String[] productTypeIds = productTypeGroupForm.getNewProductTypeArr();
		if (productTypeIds== null || (productTypeIds != null && productTypeIds.length <1)) {
			log.debug("In ProductGroupTypeValidator : Error in Adding products to the group");
			errors.rejectValue("newProductTypeArr", "Please select atleast one Product Type");
		} 
	}
	
	

}
