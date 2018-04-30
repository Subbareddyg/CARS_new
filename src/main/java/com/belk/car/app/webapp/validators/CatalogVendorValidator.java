package com.belk.car.app.webapp.validators;

import com.belk.car.app.webapp.forms.CatalogVendorsForm;
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
public class CatalogVendorValidator implements Validator {

    public boolean supports(Class clazz) {
        return CatalogVendorsForm.class.isAssignableFrom(clazz);
    }

    public void validate(Object obj, Errors errors) {
        CatalogVendorsForm catalogVendorsForm = (CatalogVendorsForm) obj;
        String vendorNumber = catalogVendorsForm.getVendorNumber();
        String vendorName = catalogVendorsForm.getVendorName();
        String startDate = catalogVendorsForm.getDateLastImportStart();
        String endDate = catalogVendorsForm.getDateLastImportEnd();
        if (null != vendorNumber && StringUtils.isNotBlank(vendorNumber) && !StringUtils.isNumeric(vendorNumber)) {

            errors.rejectValue("vendorNumber", "The Vendor # must be numeric.");

        }
        if ((startDate != null && !StringUtils.isBlank(startDate)) || (endDate != null && !StringUtils.isBlank(endDate))) {

            if (startDate != null && StringUtils.isNotBlank(startDate) && (endDate == null || StringUtils.isBlank(endDate))) {
                errors.rejectValue("dateLastImportEnd", "Invalid Date Last Import End.");
            } else if (startDate == null && StringUtils.isBlank(startDate) && (endDate != null || StringUtils.isNotBlank(endDate))) {
                errors.rejectValue("dateLastImportStart", "Invalid Date Last Import Start.");
            } else {
                boolean flag = true;
                if (!isValidDate(startDate)) {
                    errors.rejectValue("dateLastImportStart", "Invalid Date Last Import Start.");
                    flag = false;
                }
                if (null != endDate && StringUtils.isNotBlank(endDate) && !isValidDate(endDate.trim())) {
                    errors.rejectValue("dateLastImportEnd", "Invalid Date Last Import End.");
                    flag = false;
                }
                if (flag) {
                    int dateDiff = 0;
                    dateDiff = checkFutureDate(startDate);

                    if (dateDiff < 0) {
                        errors.rejectValue("dateLastImportStart", " The Date Last Imported Start field must be Less than or Equal to current Date.");
                        flag = false;
                    }
                    dateDiff = checkFutureDate(endDate);
                    if (dateDiff < 0) {
                        errors.rejectValue("dateLastImportEnd", "The Date Last Imported End field must be less than or equal to current date.");
                        flag = false;
                    }
                    if (flag) {
                        dateDiff = dateDiff(startDate, endDate);

                        if (dateDiff > 0) {
                            errors.rejectValue("dateLastImportStart", "Start Date should be less than or equal to End Date.");
                        }
                    }


                }

            }

        }



    }

    public boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");



        boolean flag;

        // we will now try to parse the string into date form
        try {
            sdf.parse(date);
            flag = true;
        } // if the format of the string provided doesn't match the format we
        // declared in SimpleDateFormat() we will get an exception
        catch (ParseException e) {
            flag = false;
        }

        return flag;
    }

    public int dateDiff(String startDate, String endDate) {
        int diff = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date date1 = sdf.parse(startDate);
            Date date2 = sdf.parse(endDate);
            diff = date1.compareTo(date2);
        } catch (ParseException e) {
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
