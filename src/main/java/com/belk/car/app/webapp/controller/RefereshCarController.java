/**
 * 
 */
package com.belk.car.app.webapp.controller;



import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.appfuse.model.User;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.SourceType;
import com.belk.car.app.model.VendorStyleType;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.PIMAttributeManager;

public class RefereshCarController extends BaseFormController {
	
	private PIMAttributeManager pimAttributeManager;
	
	private CarManager carManager;
	
	public void setCarManager(CarManager carManager) {
        this.carManager = carManager;
    }
    public void setPimAttributeManager(PIMAttributeManager pimAttributeManager) {
		this.pimAttributeManager = pimAttributeManager;
	}
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User systemUser = (User) getServletContext().getAttribute(Constants.SYSTEM_USER);
		String carId = request.getParameter(Constants.CAR_ID);
		System.out.println("entered the method handle request in RefereshCarController==>"+carId);
		Calendar startTime = Calendar.getInstance();
		Car car = carManager.getCar(new Long(carId));
		//handling pre cut over scenarios
        try { 
            String type = VendorStyleType.PRODUCT; //default
            if (car!=null) {
                type = car.getVendorStyle().getVendorStyleType().getCode();
            }
            if(!VendorStyleType.PRODUCT.equals(type) && !VendorStyleType.PYG.equals(type)){
                pimAttributeManager.refreshCarPIMAttributesForPattern(carId, systemUser);
            }else{
                if(carManager.isPostCutoverCar(car)){
                    if(!SourceType.OUTFIT.equals(car.getSourceType().getSourceTypeCd()) && !SourceType.PYG.equals(car.getSourceType().getSourceTypeCd())){
                        pimAttributeManager.refreshAdditionalCarPIMAttributes(carId, systemUser);
                    }                        
                }else{
                    pimAttributeManager.refreshCarPIMAttributes(carId,systemUser);
                }
            }
            
        } catch (Exception e) {
            log.error("Caught error when refreshing PIM attributes: " + e);
        }
        Calendar endTime = Calendar.getInstance();
        if (log.isInfoEnabled()) {
            long duration = endTime.getTimeInMillis() - startTime.getTimeInMillis();
            duration = duration / 1000; // convert to seconds
            log.info("Refreshing PIM attributes for car_id "+ carId +" took "+duration+" seconds!");
        }
		return new ModelAndView("redirect:/editCarForm.html?carId="+ carId);
	}
}
