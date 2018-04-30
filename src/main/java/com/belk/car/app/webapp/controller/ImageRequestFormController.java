package com.belk.car.app.webapp.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarImage;
import com.belk.car.app.model.CarImageId;
import com.belk.car.app.model.Image;
import com.belk.car.app.model.ImageLocationType;
import com.belk.car.app.model.ImageSourceType;
import com.belk.car.app.model.ImageTrackingStatus;
import com.belk.car.app.model.ImageType;
import com.belk.car.app.model.Status;


public class ImageRequestFormController extends BaseFormController {
	private static final String PARAMCARID = "carId";
	private static final String IMAGEID = "imgId";
	private static final String ACTION = "action";
	
	private static final String ADDONHAND = "AddOnHand";
	private static final String VENDOR = "VENDOR";
	private static final String ISVENDOR = "isVendor";
	private static final String IMAGETYPES = "imageTypes";
	private static final String IMAGELOCATIONTYPES =  "imageLocationTypes";
	private static final String USER = "user";
	private static final String DETAILCAR = "detailCar";
	
	public ImageRequestFormController() {
		setCommandName("image");
		setCommandClass(Image.class);
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering ImageRequestFormController 'formBackingObject'");
		}
		Object returnVal=null;
		if (!isFormSubmission(request)) {
			String imgId=request.getParameter(IMAGEID);
			Image img = null;
			if(StringUtils.isNotEmpty(imgId)){
				Long imageIdNum = new Long(imgId);
				img = (Image) this.getCarManager().getFromId(Image.class, imageIdNum);
			}
			else{
				img=new Image();
				img.setImageTrackingStatus((ImageTrackingStatus)this.getCarManager().getFromId(ImageTrackingStatus.class, ImageTrackingStatus.REQUESTED));
				img.setImageProcessingStatusCd(Image.PROCESSING_STATUS_PENDING) ;
				img.setStatusCd(Status.ACTIVE);
			}
			if(ADDONHAND.equals(request.getParameter(ACTION))){
				request.setAttribute(ACTION, ADDONHAND);
			}
			request.setAttribute(IMAGETYPES, this.getCarManager().getAllImageTypes());
			request.setAttribute(IMAGELOCATIONTYPES, this.getCarManager().getAllImageLocationTypes());
			request.setAttribute(PARAMCARID,request.getParameter(PARAMCARID));
			request.setAttribute(USER,getLoggedInUser());
			returnVal=img;
		}
		else{
			request.setAttribute(USER,getLoggedInUser());
			returnVal=super.formBackingObject(request);
		}
		return returnVal;
	}
	
	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command,BindException errors)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering ImageRequestFormController 'onSubmit'");
		}
		// persist the image and return the updated images table html
		
		Car car = this.getCarManager().getCarFromId(new Long(request.getParameter(PARAMCARID)));
		User user = getLoggedInUser();

		Image img = (Image) command;

		request.setAttribute(DETAILCAR, car);
		
		if(VENDOR.equals(user.getUserTypeCd()) || !ImageTrackingStatus.REQUESTED.equals(request.getParameter("imageTrackingStatusCd")) || ADDONHAND.equals(request.getParameter(ACTION))){
			this.saveOnHandImage(request, img, car);
			if(!VENDOR.equals(user.getUserTypeCd())){
				request.setAttribute(ACTION,ADDONHAND);
			}
		}
		else {
			this.saveImageRequest(request, img, car);
		}
		
		if(VENDOR.equals(user.getUserTypeCd())){
			request.setAttribute(ISVENDOR,true);
		}
		
		return new ModelAndView(getSuccessView());
	}
	
	private void saveImageRequest(HttpServletRequest request, Image img, Car car) {
		
		if (StringUtils.isBlank(img.getImageSourceTypeCd())) {
			img.setImageSourceTypeCd(ImageSourceType.VENDOR);
		}
		if (StringUtils.isBlank(img.getImageTrackingStatusCd())) {
			img.setImageTrackingStatusCd(ImageTrackingStatus.REQUESTED);
		}
		img = this.saveImage(request, car, img);
	}
	
	public void saveOnHandImage(HttpServletRequest request, Image img, Car car) {

		if (StringUtils.isBlank(img.getImageSourceTypeCd())) {
			img.setImageSourceTypeCd(ImageSourceType.ON_HAND);
		}
		if (StringUtils.isBlank(img.getImageTrackingStatusCd())) {
			img.setImageTrackingStatusCd(ImageTrackingStatus.AVAILABLE);
		}
		this.saveImage(request, car, img);
	}
	
	private Image saveImage(HttpServletRequest request, Car car, Image img) {
		this.setAuditInfo(request, img);
		
		Date dt = new Date();
		
		img.setRequestDate(dt);
		img.setReceivedDate(dt);
		img.setImageType((ImageType)this.getCarManager().getFromId(ImageType.class, img.getImageTypeCd()));
		if (StringUtils.isNotBlank(img.getImageLocationTypeCd()))
			img.setImageLocationType((ImageLocationType)this.getCarManager().getFromId(ImageLocationType.class, img.getImageLocationTypeCd()));
		img.setImageSourceType((ImageSourceType)this.getCarManager().getFromId(ImageSourceType.class, img.getImageSourceTypeCd()));
		img.setImageTrackingStatus(((ImageTrackingStatus)this.getCarManager().getFromId(ImageTrackingStatus.class, img.getImageTrackingStatusCd())));
		
		if (StringUtils.isBlank(img.getDescription())) {
			img.setDescription(" ");
		}
		
		if (StringUtils.isBlank(img.getStatusCd())) {
			img.setStatusCd(Status.ACTIVE);
		}
		img.setReceivedDate(dt);

		this.getCarManager().save(img);

		CarImage ci = new CarImage();
		CarImageId cId = new CarImageId();
		cId.setCarId(car.getCarId());
		cId.setImageId(img.getImageId());
		ci.setId(cId);
		ci.setCar(car);
		
		this.setAuditInfo(request, ci);
		
		ci.setImage(img);

		this.getCarManager().save(ci);

		return ci.getImage();
	}
	
}
