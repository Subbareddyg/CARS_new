/**
 * 
 */
package com.belk.car.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

/**
 * @author afusy01
 *
 */
public class GenericUtils {

	/**
	 * This method escapes the quote character in a string to make it compatible
	 * with oracle sql.
	 * 
	 * @param aText
	 * @return 
	 * @return
	 */
	public static String escapeSpecialCharacters(String aText) {
		final StringBuilder result = new StringBuilder();
		final StringCharacterIterator iterator = new StringCharacterIterator(
				aText);
		char character = iterator.current();
		while (character != CharacterIterator.DONE) {
			if (character == '\'') {
				result.append("''");
			} else {
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}
	
	/**
	 * This method takes an exception in an argument and return its stack trace as a string. If object passed was null then returns simply empty string. 
	 * @param ex
	 * @return {@link String}
	 * @author Yogesh.Vedak
	 */
	public static String getExceptionAsString(Exception ex){
		if(ex != null){
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			return sw.toString();
		}
		return "";
	}
	
	public  static ResourceBundle getResourceBundle(String propertyFileBaseName,Locale locale) {
			if(locale != null){
			return ResourceBundle.getBundle(propertyFileBaseName,locale);
			} 
		return ResourceBundle.getBundle(propertyFileBaseName);
	}
	
	public static  Map<String,String> getPropertyMapFromFile(String propertyFileBaseName,Locale locale) {
		Map<String,String> configMap = new HashMap<String,String>();
		ResourceBundle propertyBundle = getResourceBundle(propertyFileBaseName,locale);
		Enumeration<String> enumrationKeys = propertyBundle.getKeys();
		while (enumrationKeys.hasMoreElements()) {
			String key = enumrationKeys.nextElement();
			configMap.put(key,propertyBundle.getString(key));
		}
	return configMap;
}
}
