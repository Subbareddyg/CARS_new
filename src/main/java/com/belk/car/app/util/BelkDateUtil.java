/**
 * 
 */
package com.belk.car.app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author AFUSYS9
 *
 */
public class BelkDateUtil {

    /**
     * Method to convert the date from a given format to mm/dd/yyyy.
     * 
     * @param format
     * @param dateValue
     * @return
     * @throws ParseException
     */
    public static String formatDate(String format,String dateValue) throws ParseException{
        String formatedDate = null;
        SimpleDateFormat sdf  = new SimpleDateFormat(format);
        Date skuActiveStartDate = new Date();
        skuActiveStartDate = sdf.parse(dateValue);
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(skuActiveStartDate);
        formatedDate = (cal.get(Calendar.MONTH) + 1)+"/"+cal.get(Calendar.DATE) + "/" + cal.get(Calendar.YEAR);
        
        return formatedDate;
    }
}
