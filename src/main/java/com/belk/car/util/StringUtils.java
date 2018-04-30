package com.belk.car.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StringUtils {
	private static String DATE_FORMAT = "MM/dd/yyyy"; 
	private static SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, new Locale("en-US"));
	public static String formatDate(Date dt) {
		return formatter.format(dt);
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
}
