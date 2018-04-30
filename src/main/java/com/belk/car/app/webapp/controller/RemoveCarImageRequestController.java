package com.belk.car.app.webapp.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarImage;
import com.belk.car.app.model.Image;
import com.belk.car.app.model.ImageSourceType;

public class RemoveCarImageRequestController extends BaseFormController {
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String carId = request.getParameter("car") ;
		String imageId = request.getParameter("image") ;
		
		Map<String, Object> model = new HashMap<String, Object>();		
		if (StringUtils.isNotBlank(carId) && StringUtils.isNotBlank(imageId)) {
			Car car = this.getCarManager().getCar(Long.parseLong(carId)) ;
			Image image = (Image) this.getCarManager().getFromId(Image.class, Long.parseLong(imageId));
			
			if (image.getImageSourceType().getImageSourceTypeCd().equals(
					ImageSourceType.ON_HAND)) {
				model.put("action", "AddOnHand");
			}
	
			if (car != null && image != null) {
				CarImage cImg = null;
				for(CarImage cImage : car.getCarImages()) {
					if(cImage.getId().getImageId() == image.getImageId()) {
						cImg = cImage;
						break;
					}
				}
				car.getCarImages().remove(cImg);
				this.getCarManager().save(car) ;
				this.getCarManager().remove(image);

				model.put("detailCar", car);
			}
		}

		return new ModelAndView(this.getSuccessView(), model);

	}
}
