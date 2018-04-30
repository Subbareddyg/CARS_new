package com.belk.car.app.webapp.controller.admin.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.AccessDeniedException;
import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.model.ColorMappingMaster;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.model.SizeConversionMaster;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.UserForm;

public class VendorFormController extends BaseFormController {

	private CarLookupManager carLookupManager;

	private CarManager carManager;

	public void setCarLookupManager(CarLookupManager carLookupManager) {
		this.carLookupManager = carLookupManager;
	}

	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	public VendorFormController() {
		setCommandName("userForm");
		setCommandClass(UserForm.class);
	}

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		if(log.isDebugEnabled())
			log.debug("entering 'onSubmit' method...");

		UserForm userForm = (UserForm) command;

		Set<Vendor> vendors = userForm.getVendors();
		List vendorList = new ArrayList();
		for (Vendor vend : vendors) {
			vendorList.add(String.valueOf(vend.getVendorId()));
		}

		if (vendorList != null && vendorList.size() > 0) {
			// userForm.getUser().getVendors().clear();
			for (Iterator iter = vendorList.iterator(); iter.hasNext();) {
				String deptId = (String) iter.next();
				Vendor dbVendor = carLookupManager.getVendorById(Long
						.valueOf(deptId));
				userForm.getUser().addVendor(dbVendor);
			}
		}

		try {
			carManager.saveUser(userForm.getUser());
		} catch (AccessDeniedException ade) {
			// thrown by UserSecurityAdvice configured in aop:advisor
			// userManagerSecurity
			log.warn(ade.getMessage());
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}

		return new ModelAndView("redirect:/admin/user/userDetails.html?id="
				+ userForm.getUser().getId());
	}
/*
 * (non-Javadoc)
 * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)
 * This method is modified as a part of performance improvement project and search vendor by name or Number
 */
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws ServletRequestBindingException {

	  String isSearch=request.getParameter("firstload");
      String search=request.getParameter("searchUser");
      
      String vendorName1=request.getParameter("vendorName");
      if(null == vendorName1)
    	  vendorName1="";
      String vendorNumber1=request.getParameter("vendorNumber");
      if(null == vendorNumber1)
    	  vendorNumber1="";
      
      UserForm userForm = (UserForm) command;
      if("true".equalsIgnoreCase(search))
    	  userForm.setSearchUser(true);
      else
    	  userForm.setSearchUser(false);
      Set<Vendor> filteredVendors = new HashSet<Vendor>();
      List<Vendor> sampleVendor=new ArrayList<Vendor>();	
	  if(isSearch !=null){
    	  sampleVendor=carLookupManager.getAllVendorsByVendorName(vendorName1, vendorNumber1);
    	  for (Vendor vend : userForm.getUser().getVendors()) {
  		      if(!(vendorName1.length() > 0 ||  vendorNumber1.length() > 0)){
  				    filteredVendors.add(vend);
  		      }else{
  		    	  if(vendorName1.length() > 0 && vendorNumber1.length() > 0){
  		    		  if(vend.getName().contains(vendorName1.toUpperCase()) && vend.getVendorNumber().contains(vendorNumber1)){
  		    			filteredVendors.add(vend);
  		    		  }
  		    	  }else if((vendorName1.length() > 0 && vend.getName().contains(vendorName1.toUpperCase())) 
  		    			  || (vendorNumber1.length() > 0 && vend.getVendorNumber().contains(vendorNumber1))){
  		    		  filteredVendors.add(vend);
  		    	  }
  		      }
  		  }
      }
	  
	  Map model = new HashMap();
	  model.put("userVendors", filteredVendors);      
      model.put("vendors",sampleVendor);
      model.put("vendorName",vendorName1);
      model.put("vendorNumber",vendorNumber1);
      return model;
}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

		String userId = request.getParameter("id");
		UserForm userForm = new UserForm();
		User user;
		if (userId == null && !isAssociateVendor(request)) {
			user = getUserManager().getUserByUsername(request.getRemoteUser());
		} else if (!StringUtils.isBlank(userId)) {
			user = getUserManager().getUser(userId);
		} else {
			user = new User();
		}
		userForm.setUser(user);

		return userForm;

	}
	
	 /**
		 * Sets the error in to the session and returns back to the search form with given error Message.
		 * @param request
		 * @param errorMsg
		 * @param model
		 * @return
		 */
		
	
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) {
		binder.registerCustomEditor(Set.class, "vendors",
				new CustomCollectionEditor(Set.class) {
					protected Object convertElement(Object element) {
						Long vendorId = null;
						if (element instanceof Long)
							vendorId = (Long) element;
						else if (element instanceof String)
							vendorId = Long.valueOf((String) element);
						return carLookupManager.getVendorById(vendorId);
					}
				});
	}

	protected boolean isAssociateVendor(HttpServletRequest request) {
		String method = request.getParameter("method");
		return (method != null && method.equalsIgnoreCase("associateVendor"));
	}
}