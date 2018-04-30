/**
 * 
 */

package com.belk.car.app.util;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to read the properties file.
 * 
 * @author afusya2
 */
public abstract class PropertyLoader {
	/**
	 * This method loads properties file
	 * 
	 * @param propertyName
	 * @return
	 */
	public static Properties loadProperties(String propertyName) {
		Properties properties = new Properties();
		InputStream in = null;
		try {
			in = ClassLoader.getSystemResourceAsStream(propertyName);
			if (in == null) {
				Logger.getLogger(PropertyLoader.class.getName()).log(Level.INFO,
						"Class resource not found, finding class Thread resource!");
				in = Thread.currentThread().getContextClassLoader().getResourceAsStream(
						propertyName);
			}
			if (in != null) {
				properties.load(in);
			}
			else {
				Logger.getLogger(PropertyLoader.class.getName())
				.log(Level.SEVERE,
						"Thread resource not found, don't know what to do now!  Could not load ftp.properties file.");
				properties= null;
			}
		}
		catch (Exception ex) {
			Logger.getLogger(PropertyLoader.class.getName()).log(Level.SEVERE, null, ex);
		}
		finally {
			if (in != null) {
				try {
					in.close();
				}
				catch (Throwable ignore) {
					Logger.getLogger(PropertyLoader.class.getName())
					.log(Level.SEVERE,
							"Could not load ftp.properties file.");
				}
			}

		}
		return properties;
	}

}
