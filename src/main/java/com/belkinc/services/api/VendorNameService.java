package com.belkinc.services.api;

import java.io.IOException;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.belk.car.app.service.impl.PIMAttributeManagerImpl;

public class VendorNameService {

	public String getVendorName(String url) {
		return this.getVendorNameFromSMART(url);
	}
	
	private transient final Log log = LogFactory.getLog(PIMAttributeManagerImpl.class);
	
	/**
	 * This method returns the VendorName through REST
	 * 
	 * @param webserviceUrl
	 * @return vendorName
	 */
	private String getVendorNameFromSMART(String webserviceUrl) {

		// Create an instance of HttpClient.
		HttpClient client = new HttpClient();
		String vendorName = null;
		// Create a method instance.
		GetMethod method = new GetMethod(webserviceUrl);
		// Provide custom retry handler is necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));
		try {
			// Execute the method.
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				log.error("Method failed: " + method.getStatusLine());
			}
			// Read the response body.
			byte[] responseBody = method.getResponseBody();
			// Get the vendor name from responseBody
			vendorName = splitVendorName(new String(responseBody));
			log.info("Webservice returned  vendorName:"+vendorName);
		} catch (HttpException e) {
			log.error("Fatal protocol violation: " + e.getMessage());
		} catch (IOException e) {
			log.error("Fatal transport error: " + e.getMessage());
		} finally {
			// Release the connection.
			method.releaseConnection();
		}
		return vendorName;
	}

	/**
	 * This method returns the vendorName from message header
	 * @param responseVendorName
	 * @return vendorName
	 */
	public String splitVendorName(String responseVendorName) {
		String[] vendorName = responseVendorName.split(",");
		vendorName = vendorName[1].split(":");
		vendorName = vendorName[2].split("}");
		String vendorNameNew = vendorName[0].substring(1,
				vendorName[0].length() - 2);
		return vendorNameNew;
	}
}
