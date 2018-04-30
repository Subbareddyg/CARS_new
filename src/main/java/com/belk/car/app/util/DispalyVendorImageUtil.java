package com.belk.car.app.util;

import java.net.MalformedURLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;

import com.belk.car.app.webapp.controller.VendorImageLinkJsonController;
import com.rrd.sso.SSOSession;
import com.rrd.sso.SSOSessionKey;
import com.rrd.sso.SSOSessionService;

public class DispalyVendorImageUtil {
	
	static String token;
	static String url;
	private transient final Log log = LogFactory.getLog(DispalyVendorImageUtil.class);
	private static final String WS = "http://mediacompass.rrd.com/SSOWebService/services/SSOSessionService";

	public DispalyVendorImageUtil() {

	}

	public static String myKey(String imageName) throws MalformedURLException {
		
			// THIS IS THE CODE THE SETTING PROGRAM WOULD CALL TO SET VARIABLES
			// IN A SHARED SESSION BEFORE IT TELLS ANOTHER APP ABOUT IT
			Service serviceModel = new ObjectServiceFactory()
					.create(SSOSessionService.class);
			SSOSessionService service = (SSOSessionService) new XFireProxyFactory()
					.create(serviceModel, WS);
			SSOSession session = new SSOSession();
			session.add("customer_name", "Belk");
			session.add("image_name", imageName);
			SSOSessionKey key = service.createSession(session);
			if (key.isEmpty()) {
				throw new RuntimeException("could not create shared session");
			}
			 token = key.getValue();
			 
			System.out.println("created token "
					+ SSOSessionKey.create(token).toString());
			
			 url = "http://mediacompass.rrd.com/MediaCompassImages/servlet/MCImageServlet?ssoid=";
		
		return url+token;
	}
	
	

}
