package com.belk.car.app.webapp.validators;

import com.belk.car.app.webapp.forms.SearchCatalogForm;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.apache.commons.lang.StringUtils;

/**
 * @author Antonio Gonzalez
 * 
 */
public class CatalogSearchFormValidator implements Validator {

	public boolean supports(Class clazz) {
		return SearchCatalogForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
               SearchCatalogForm catalogForm = (SearchCatalogForm) obj;
                String startDate = catalogForm.getDateLastUpdatedStart();
                String endDate = catalogForm.getDateLastUpdatedEnd();
                String vendorId= catalogForm.getVendorId();
                if(null != vendorId && StringUtils.isNotBlank(vendorId)) {
                    if(!StringUtils.isNumeric(vendorId)) {
                     errors.rejectValue("vendorId", "The Vendor # must be numeric.");
                    }
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


        Date testDate = null;

        // we will now try to parse the string into date form
        try {
            testDate = sdf.parse(date);
        } // if the format of the string provided doesn't match the format we
        // declared in SimpleDateFormat() we will get an exception
        catch (ParseException e) {
            return false;
        }

        return true;
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
