package com.belk.car.app.webapp.listener;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.belk.car.app.service.CarManager;

public class CarSessionListener implements HttpSessionListener{
	private static final Log log = LogFactory.getLog(CarSessionListener.class);
	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		// TODO Auto-generated method stub
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		if(log.isDebugEnabled()){
			log.debug("Destorying the current session");
		}
		try{
			HttpSession session = sessionEvent.getSession();
			String userEmail = (String)session.getAttribute("ACEGI_SECURITY_LAST_USERNAME");
			if(userEmail != null){
				ServletContext context = session.getServletContext();
				ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
				CarManager carMgr = (CarManager) ctx.getBean("carManager");
				carMgr.unlockCar(userEmail);
			}
			session.removeAttribute("carLock");
		
	    }catch (NoSuchBeanDefinitionException n) {
            log.error("CarManager bean not found, assuming test and ignoring...");
        }
		catch(Exception e){
			log.error("Exception occured while unlocking the CAR");
	    }
	}
}
