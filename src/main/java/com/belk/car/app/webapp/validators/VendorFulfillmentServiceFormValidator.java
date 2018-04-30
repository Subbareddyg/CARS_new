
package com.belk.car.app.webapp.validators;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.belk.car.app.webapp.forms.FSVendorForm;

public class VendorFulfillmentServiceFormValidator implements Validator {

	public boolean supports(Class arg0) {
		return FSVendorForm.class.isAssignableFrom(arg0);
	}

	

	public void validate(Object arg0, Errors arg1) {
		FSVendorForm form = (FSVendorForm) arg0;

		
		if (StringUtils.isNotBlank(form.getSafetyInvAmt())
				&& StringUtils.isNotBlank(form.getSafetyInvAmtTyp())
				&& form.getSafetyInvAmtTyp().equals("num_units")) {

			String safetyInventoryAmt = form.getSafetyInvAmt();

			if (StringUtils.isNotBlank(safetyInventoryAmt)
					&& StringUtils.isNumeric(safetyInventoryAmt)) {
				long longSafetyInventoryAmt = Long
						.parseLong(safetyInventoryAmt);

				if (longSafetyInventoryAmt < 0) {
					arg1.rejectValue("safetyInvAmt", "invalid",
							"# of units should be a positive number!!");
				}
			}
			else {
				arg1.rejectValue("safetyInvAmt", "invalid",
						"# of units should be a positive number!!");
			}

		}
		else if (StringUtils.isNotBlank(form.getSafetyInvAmt())
				&& StringUtils.isNotBlank(form.getSafetyInvAmtTyp())
				&& form.getSafetyInvAmtTyp().equals("perc_inventory")) {
			String safetyInventoryAmt = form.getSafetyInvAmt();

			if (StringUtils.isNotBlank(safetyInventoryAmt)
					&& StringUtils.isNumeric(safetyInventoryAmt)) {

				long longSafetyInventoryAmt = Long
						.parseLong(safetyInventoryAmt);
				if (longSafetyInventoryAmt < 0 || longSafetyInventoryAmt > 50) {

					arg1
							.rejectValue("safetyInvAmt", "invalid",
									"% of Inventory should be a number between 0 to 50 %");
				}
			}
			else if (StringUtils.isNotBlank(safetyInventoryAmt)) {
				arg1.rejectValue("safetyInvAmt", "invalid",
						"% of Inventory should be a number between 0 to 50 %");
			}
		}

	}
	// }
}
