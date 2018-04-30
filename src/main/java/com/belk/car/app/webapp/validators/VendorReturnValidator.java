
package com.belk.car.app.webapp.validators;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.belk.car.app.webapp.forms.VendorReturnForm;

public class VendorReturnValidator implements Validator {


	@SuppressWarnings("unchecked")
	public boolean supports(Class arg0) {
		return VendorReturnForm.class.isAssignableFrom(arg0);
	}

	/**
	 * @param arg0
	 * @param errors
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 * @Enclosing_Method  validate
	 * @TODO
	 */
	public void validate(Object arg0, Errors errors) {
		VendorReturnForm form = (VendorReturnForm) arg0;
		if (StringUtils.isNotBlank(form.getReturnMethod()) && form.getReturnMethod().equals("2")
				&& StringUtils.isBlank(form.getReturnMethodType())) {
			errors.rejectValue("returnMethodType", "invalid", "Belk Location cannot be empty!");

		}
		if (StringUtils.isNotBlank(form.getDropShipRma())) {
			try {
				Long longRma=new Long(form.getDropShipRma());
				if(longRma<=0){
					throw new NumberFormatException("RMA number is negative");
				}
			}
			catch (NumberFormatException nfe) {
				nfe.printStackTrace();
				errors.rejectValue("dropShipRma", "invalid", "Dropship RMA #: must be numeric and greater than zero.");

			}

		}
	}

}
