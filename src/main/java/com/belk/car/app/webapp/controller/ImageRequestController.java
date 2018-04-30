package com.belk.car.app.webapp.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarImage;
import com.belk.car.app.model.ImageTrackingStatus;

public class ImageRequestController extends BaseController  {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String carId = request.getParameter("car_id");
		String[] imageIds = request.getParameterValues("receivedImageId");
		Car car=null;
		if(StringUtils.isNotBlank(carId)){
			car = this.getCarManager().getCarFromId(new Long(carId));
			if(imageIds!=null){
				ImageTrackingStatus recievedTrackingStatus = (ImageTrackingStatus)this.getCarLookupManager().getById(ImageTrackingStatus.class, ImageTrackingStatus.RECEIVED);
				Set<CarImage> carImages = car.getCarImages();
				Set<String> carImageIds = new HashSet<String>();
				for(String imageId : imageIds){
					carImageIds.add(imageId);
				}
				for(CarImage carImage : carImages){
					if (carImageIds.contains(String.valueOf(carImage.getImage().getImageId()))) {
						carImage.getImage().setImageTrackingStatus(recievedTrackingStatus);
						//Set the Audit Information for Tracking Purposes
						this.setAuditInfo(request, carImage.getImage()) ;
						this.getCarManager().save(carImage);
					}
				}
			}
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("detailCar", car);
		model.put("user",this.getLoggedInUser());

		return new ModelAndView(this.getSuccessView(),model);
	}
}
