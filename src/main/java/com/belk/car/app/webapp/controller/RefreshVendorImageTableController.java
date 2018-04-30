package com.belk.car.app.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.appfuse.model.User;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.belk.car.app.model.Car;
import com.belk.car.app.service.CarLookupManager;
/**
 * 
 * @author AFUSY12
 * THis controller will refresh the vendorImageTable on car edit page 
 */
public class RefreshVendorImageTableController extends AbstractController {

	CarLookupManager carLookupManager;
	private String successView;
	
	public String getSuccessView() {
		return successView;
	}

	public void setSuccessView(String successView) {
		this.successView = successView;
	}

	public void setCarLookupManager(CarLookupManager carLookupManager) {
		this.carLookupManager = carLookupManager;
	}

	public ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String carId=request.getParameter("car_id");
		Car car = null;
	    car = (Car)this.carLookupManager.getById(Car.class, Long.parseLong(carId));
		ModelAndView modelnView = new ModelAndView(getSuccessView());
		modelnView.addObject("detailCar", car);
		modelnView.addObject("user", this.getLoggedInUser());
		
		return modelnView;
	}
	
	public User getLoggedInUser() {
    	User user = null;
    	 Authentication auth = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
         if (auth.getPrincipal() instanceof UserDetails) {
             user = (User)  auth.getPrincipal();
         }
         return user;
    }
	

}
