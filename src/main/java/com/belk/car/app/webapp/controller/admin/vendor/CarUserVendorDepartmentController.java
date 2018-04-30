package com.belk.car.app.webapp.controller.admin.vendor;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.appfuse.model.User;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.model.CarUserVendorDepartment;
import com.belk.car.app.model.Status;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.CarUserVendorDepartmentForm;



public class CarUserVendorDepartmentController extends BaseFormController {

	
	private CarManager carManager;
	
	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}


	public CarUserVendorDepartmentController() {
		setCommandName("carUserVendorDeparmentForm");
		setCommandClass(CarUserVendorDepartmentForm.class);
	}
	
	public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		log.debug("entering 'onSubmit' method...");
		CarUserVendorDepartmentForm userVendorDept = (CarUserVendorDepartmentForm) command;
		String deptCd=userVendorDept.getDepartmentCid();
		log.debug("deparment cd:"+deptCd);
		String VendorNumber=userVendorDept.getVendorNumber();
		log.debug("vendor number:"+VendorNumber);
		long vendorId=Long.parseLong(request.getParameter("id"));
		log.debug("Vendor id :"+vendorId);
		ModelAndView mav= null;
		CarUserVendorDepartment carVendor=null;
		User user = getLoggedInUser();
		Date dt = new Date();
 	    if(deptCd!=null && VendorNumber!=null){			
			carVendor=new CarUserVendorDepartment();
			carVendor.setDept(carManager.getDepartment(deptCd));
			carVendor.setVendor(carManager.getVendor(VendorNumber));
			carVendor.setCreatedBy(user.getUsername());
			carVendor.setUpdatedBy(user.getUsername());
			carVendor.setCreatedDate(dt);
			carVendor.setUpdatedDate(dt);
			carVendor.setStatusCd(Status.ACTIVE);
			try{
				carManager.saveVendorDepartment(carVendor);
				mav=new ModelAndView("redirect:vendorDetails.html?id="+vendorId);
			}catch(Exception ex)
			{
				log.debug(ex.getMessage());
			}
 	    }else {
 	    	log.info("no departments and vendors found");
 	    	mav=new ModelAndView("redirect:/vendor/vendorDeptForm.html?id="+vendorId);
 	    }
		return mav;
	}
	
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws ServletRequestBindingException {
		
		log.debug("referenceData method...");
		Long userId = ServletRequestUtils.getLongParameter(request, "id");
		User user = getUserManager().getUser(String.valueOf(userId));
		Map model = new HashMap();
		model.put("user", user);
		return model;
	}
	
	protected Object formBackingObject(HttpServletRequest request)
	throws Exception {
		log.debug("formBackingObject method...");
		CarUserVendorDepartmentForm carUserVendorDeparmentForm = new CarUserVendorDepartmentForm();
		return carUserVendorDeparmentForm;

   }

}