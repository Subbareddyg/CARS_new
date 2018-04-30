package com.belk.car.app.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.service.CarManager;

public class UnlockCarController extends BaseFormController{
	CarManager carManager = null;
	private transient final Log log = LogFactory.getLog(UnlockCarController.class);
	
	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if(log.isDebugEnabled()){
			log.debug("unlocking the CAR");
		}
		String car_id = request.getParameter("car_id");
		try{
			if(car_id!=null && car_id.length() > 0){
				long carId = Long.parseLong(car_id);
				carManager.unlockCar(carId);
				log.info("unlocked the CAR # "+ carId);
				HttpSession session = request.getSession();
				session.removeAttribute("carLock");
			}
		}catch(Exception e){
			log.error("Unable to unlock the car+ "+ e.getMessage());
		}
		
	    return null;
	}
	
}
