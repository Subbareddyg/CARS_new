package com.belk.car.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
	private static String DATE_FORMAT = "MM/dd/yyyy"; 
	private static SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, new Locale("en-US"));
	
	public static String formatDate(Date dt) {
		return formatter.format(dt);
	}
	
	public static String formatDate(Date dt, String format) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(format, new Locale("en-US"));
		return dateFormatter.format(dt);
	}
	
	public static Date parseDate(String strDate, String strDateFormat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(strDateFormat);
		Date date;
		try {
			date = dateFormat.parse(strDate);
		} catch (ParseException e) {
			// TODO
			date = new Date();
		}

		return date ;
	}
	
	public static Date parseSampleDate(String strDate, String strDateFormat) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(strDateFormat);
		Date date;
		try {
			date = dateFormat.parse(strDate);
		} catch (ParseException e) {
			date = dateFormat.parse("01/01/1900");
		}

		return date ;
	}
	
	
	
	public static Calendar getAsCalendar(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
		}
}
