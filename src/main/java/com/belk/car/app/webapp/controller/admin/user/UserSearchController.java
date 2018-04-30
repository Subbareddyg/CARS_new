package com.belk.car.app.webapp.controller.admin.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarUserManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class UserSearchController extends BaseFormController {

	private CarUserManager carUserManager ;
	
	private CarLookupManager carLookupManager ;
	
	private static final String DISPLAY_TABLE_SORT_ORDER = "d-149542-o";
	private static final String DASHBOARD_DISPLAY_TABLE_ID = "d-149542-s";
	private static final String PAGE_NO = "d-149542-p";
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String userFName = "";
		String userLName = "";
		String emailId = "";
		if(StringUtils.isNotBlank(request.getParameter("userFName"))){
			userFName = (String)request.getParameter("userFName");
		}
		if(StringUtils.isNotBlank(request.getParameter("userLName"))){
			userLName = (String)request.getParameter("userLName");
		}
		if(StringUtils.isNotBlank(request.getParameter("emailId"))){
			emailId = (String)request.getParameter("emailId");
		}
		//Below code added as a part of Performance improvement.
		Integer pageNo=0;
		Long countUser=0L;
		String dashboardSortedOn="";
		String dashboardSortedOrder="";
		
        if(null != request.getParameter(PAGE_NO)) {
			pageNo = new Integer(request.getParameter(PAGE_NO)); 			
		}
        if(null != request.getParameter(DASHBOARD_DISPLAY_TABLE_ID)) {
			Integer colno = new Integer(request.getParameter(DASHBOARD_DISPLAY_TABLE_ID));
			colno++;
			dashboardSortedOn = colno.toString();
			
			if(request.getParameter(DISPLAY_TABLE_SORT_ORDER).equals("2")) {
				dashboardSortedOrder = "ascending";
			}else{
				dashboardSortedOrder = "descending";
			} 
		}		
        if (logger.isDebugEnabled())
			logger.debug("Queries for: " + userFName + " : " + userLName + " : " + emailId);
        
        if(pageNo == 0)
     	   pageNo=1;
        
		//Set the value to session of future use for RE-SEARCH
		this.saveSearchCriteria(request, "userSearchCriteria");
		List<User> users = new ArrayList<User>();
		users = carUserManager.searchUsers(userFName, userLName, emailId,pageNo,dashboardSortedOn,dashboardSortedOrder);
		countUser=carLookupManager.getUsersCount(userFName, userLName, emailId);
		
		Map<String, Object> context = new HashMap<String, Object>();
        context.put("userfname", userFName);
        context.put("userlname", userLName);
        context.put("emailid", emailId);
        context.put("loggedInUser", getLoggedInUser());
        context.put("totalResultSize", new Integer(countUser.intValue()));
        context.put("pageno", pageNo);
        context.put( Constants.USER_LIST, users);
		
       return new ModelAndView(this.getSuccessView(), context);

	}

	public void setCarUserManager(CarUserManager carUserManager) {
		this.carUserManager = carUserManager;
	}
	
	public void setCarLookupManager(CarLookupManager carLookupManager) {
		this.carLookupManager = carLookupManager;
	}
}
