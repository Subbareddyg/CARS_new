/**
 * 
 */
package com.belk.car.app.webapp.controller.admin.vendor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;


import com.belk.car.app.model.CarUserVendorDepartment;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.webapp.controller.BaseFormController;

/**
 * @author antoniog
 * 
 */
public class VendorsDetailsController extends BaseFormController implements Controller {

	private transient final Log log = LogFactory.getLog(VendorsDetailsController.class);

	
	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Long userId = ServletRequestUtils.getLongParameter(request, "id");
		if (userId == null) {
			if (log.isDebugEnabled()){
				log.debug("User id was null. Redirecting...");
			}
			return new ModelAndView("redirect:dashBoard.html");
		} 
		User user = getUserManager().getUser(String.valueOf(userId));
	
		Set<Vendor> vendors=user.getVendors();
		Set<Department> departments=user.getDepartments();
		List<CarUserVendorDepartment> finalList=new ArrayList<CarUserVendorDepartment>();
		Iterator<Vendor> itVendor = vendors.iterator();
		while (itVendor.hasNext()) {
		    Vendor v =(Vendor) itVendor.next();
			List<CarUserVendorDepartment> vendorDeptsList= getCarManager().getVendorDepartments(Long.valueOf(v.getVendorId()));
			if(log.isDebugEnabled()){
				log.debug("vendor number: "+v.getVendorNumber()+" and CarUserVendorDeptsList :"+vendorDeptsList);
			}
			if(vendorDeptsList!=null){
				log.info("have : "+vendorDeptsList.size()+ " departments for vendor "+ v.getVendorNumber());
				for(Iterator<CarUserVendorDepartment> i = vendorDeptsList.iterator(); i.hasNext(); ) {
					 CarUserVendorDepartment item = i.next();
					 //if(departments.contains(item.getDept())){
					 if(containsDepartment(departments,item)){
						 if(log.isDebugEnabled()){
							 log.info(" Mapping Already Exists for DeptId: "+item.getDept().getDeptId());
						 }
						 finalList.add(item);
					 }
				}
			}
		}
		Map model = new HashMap();
		model.put("user", user);
	    model.put("vendorDepts",finalList);
		return new ModelAndView("admin/vendor/vendorDetails", model);
	}
	
	/*
	 * Method to check Department already exists for CarUserVendorDepartment
	 */
	
	private boolean containsDepartment(Set<Department> depts,CarUserVendorDepartment cuv)
	{
		if(log.isDebugEnabled()){
			log.debug(" CarUserVendorDepartment: DeptCd: "+cuv.getDept().getDeptCd());
		}
		Iterator<Department> dept = depts.iterator();
		while(dept.hasNext()) 
		{
			Department d=(Department)dept.next();
			if(d.getDeptId()==cuv.getDept().getDeptId()){
				 log.info(" Department DeptId: "+d.getDeptId()+" CarUserVendorDepartment: DeptId: "+cuv.getDept().getDeptId()+ " are equal");
				return true;
			}
		}
		return false;
	}
}
