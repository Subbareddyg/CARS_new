package com.belk.car.integrations.rrd;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.belk.car.app.Constants;
import com.rrd.sso.SSOSession;
import com.rrd.sso.SSOSessionKey;
import com.rrd.sso.SSOSessionService;

public class MediaCompassSamplePhotoReader {

	private static final String SSOURL = "http://mediacompass.rrd.com/MediaCompassImages/servlet/MCImageServlet?ssoid=";
	private transient final Log log = LogFactory.getLog(MediaCompassSamplePhotoReader.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * This method will call the SSOWebservice to generate the single time hit Image URL
	 * @param service
	 * @param strImageName
	 * @return
	 */
	public String getMCImageURLbyName(SSOSessionService service, String strImageName){
		String strSessionID = "";
		try{
			SSOSession session = new SSOSession();
			session.add("customer_name", Constants.BELK);
			session.add("image_name", strImageName);
			SSOSessionKey key = service.createSession(session);
			if(key.isEmpty()) {
				throw new RuntimeException("could not create shared session");
			}
			String token = key.getValue();
			strSessionID =SSOSessionKey.create(token).toString();
		}catch(Exception e){
			log.error("error while generating token id from media compass for image" + strImageName);
		}
		return SSOURL + strSessionID ;
	}
	
	/**
	 * This Method is to download the image from Media compass U
	 * @param strFrom
	 * @param strTo
	 * @param strImageName
	 * @return
	 */
	public boolean downloadImage(String strFrom, String strTo, String strImageName){
		
		if(log.isDebugEnabled()){
			log.debug("downloding image from "+ strFrom + " to " + strTo+"/"+strImageName);
		}
		try{
			
			URL imageUrl = new URL(strFrom);
			File destinationfile = new File(strTo + strImageName);
			FileUtils.copyURLToFile(imageUrl, destinationfile);
			if(destinationfile.exists()){
				return true;
			}
		}catch(IOException ioe){
			log.error("IOException: error while downloading the image from media compass to "+ strTo + ":" + strImageName);
			log.error("may be issue with file permissions or disk space on belkmacl");
		}catch(Exception e){
			log.error("error while downloading the image from media compass to " +strTo  +"  : " + strImageName);
			e.printStackTrace();
		}
		return false;
	}
	

}
