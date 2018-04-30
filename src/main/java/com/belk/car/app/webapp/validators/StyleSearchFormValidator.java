package com.belk.car.app.webapp.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.belk.car.app.webapp.forms.SearchStyleForm;

/**
 * @author Antonio Gonzalez
 * 
 */
public class StyleSearchFormValidator implements Validator {

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return SearchStyleForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		SearchStyleForm styleSearchForm = (SearchStyleForm) obj;
		String startDate = styleSearchForm.getDateLastUpdatedStart();
		String endDate = styleSearchForm.getDateLastUpdatedEnd();
		String vendorStyle= styleSearchForm.getVendorStyle();
		String department = styleSearchForm.getDepartment();
		
		if(null != department && StringUtils.isNotBlank(department) && !StringUtils.isNumeric(department)) {
			
				errors.rejectValue("department", "Invalid Department.");
			
		}

		if((startDate!=null && !StringUtils.isBlank(startDate)) ||( endDate!=null&& !StringUtils.isBlank(endDate))) {
			if(startDate  !=null && StringUtils.isNotBlank(startDate) &&( endDate == null || StringUtils.isBlank(endDate))) {
				errors.rejectValue("dateLastUpdatedEnd", "Invalid Date Last Update End.");
			} else if(startDate  ==null && StringUtils.isBlank(startDate) &&( endDate != null || StringUtils.isNotBlank(endDate))) {
				errors.rejectValue("dateLastUpdatedStart", "Invalid Date Last Update Start.");
			} else
			{
				boolean flag=true;
				if(! isValidDate(startDate)) {
					errors.rejectValue("dateLastUpdatedStart", "Invalid Date Last Update Start.");
					flag=false;
				}
				if(! isValidDate(endDate.trim())) {
					errors.rejectValue("dateLastUpdatedEnd", "Invalid Date Last Update End.");
					flag=false;
				}
				if(flag )  {
					
                                     int dateDiff = 0;
                                    dateDiff = checkFutureDate(startDate);

                                    if (dateDiff < 0) {
                                        errors.rejectValue("dateLastUpdatedStart", " The Date Last Updated Start field must contain a valid start date with today date or earlier.");
                                        flag = false;
                                    }
                                    dateDiff = checkFutureDate(endDate);
                                    if (dateDiff < 0) {
                                        errors.rejectValue("dateLastUpdatedEnd", "The Date Last Updated End field must contain a valid end date with today date or earlier.");
                                        flag = false;
                                    }
                                    if (flag) {
                                        dateDiff = dateDiff(startDate, endDate);

                                        if (dateDiff > 0) {
                                            errors.rejectValue("dateLastUpdatedStart", "Start Date should be less than or equal to End Date.");
                                        }
                                    }

				}

			}
		}



	}

	public boolean isValidDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");


		@SuppressWarnings("unused")
		Date testDate = null;
		boolean flag=true;
		// we will now try to parse the string into date form
		try {
			testDate = sdf.parse(date);
		} // if the format of the string provided doesn't match the format we
		// declared in SimpleDateFormat() we will get an exception
		catch (ParseException e) {
			flag= false;
		}

		return flag;
	}

	public int dateDiff(String startDate ,String endDate) {
		int diff=0;
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		try {
			Date date1 = sdf.parse(startDate);
			Date date2 = sdf.parse(endDate);
			diff= date1.compareTo(date2);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return diff;
	}

         public int checkFutureDate(String inputDate) {
        int diff = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date date1 = new Date();
            Date date2 = sdf.parse(inputDate);
            diff = date1.compareTo(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff;
    }

}
