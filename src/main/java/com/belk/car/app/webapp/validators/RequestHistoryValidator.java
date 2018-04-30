/**
 * 
 */
package com.belk.car.app.webapp.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.webapp.forms.RequestHistoryForm;

/**
 * @author afusy01
 *
 */
public class RequestHistoryValidator implements Validator,DropShipConstants  {

	
	public boolean supports(Class arg0) {
		return RequestHistoryForm.class.isAssignableFrom(arg0);
	}

	public void validate(Object arg0, Errors errors) {
		RequestHistoryForm form = (RequestHistoryForm) arg0;
		String startEffDate = form.getEffectiveStartDate();
		String endEffDate = form.getEffectiveEndDate();
		
		if(!startEffDate.equals(BLANK) && !validateDate(startEffDate)) {
			errors.rejectValue("effectiveStartDate", INVALID, "Effective start date is not a valid date.");
		}
		if(!endEffDate.equals(BLANK) && !validateDate(endEffDate)) {
			errors.rejectValue("effectiveEndDate", INVALID, "Effective end date is not a valid date.");
		}
		if(!startEffDate.equals(BLANK) && validateDate(startEffDate) && endEffDate.equals(BLANK)) {
			errors.rejectValue("effectiveEndDate", INVALID, "Please enter effective end date.");
		}
		if(startEffDate.equals(BLANK) && !endEffDate.equals(BLANK) && validateDate(endEffDate)) {
			errors.rejectValue("effectiveStartDate", INVALID, "Please enter effective start date.");
		}
	}
	
	private boolean validateDate(String strDate) {
		boolean flag=true;
        try
        {
        	SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        	dateFormat.parse(strDate);
        }
        catch (ParseException e)
        {
              flag= Boolean.FALSE;
        }
		return flag;
	}
}
