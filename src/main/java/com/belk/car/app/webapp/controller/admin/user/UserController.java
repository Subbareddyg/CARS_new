package com.belk.car.app.webapp.controller.admin.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.Constants;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class UserController extends BaseFormController implements Controller {
	private transient final Log log = LogFactory.getLog(UserController.class);
	private UserManager mgr = null;
	private static final String DISPLAY_TABLE_PAGE_NUMBER = "d-1335586-p";
	private static final String SELECTED_PAGE_NUMBER = "pageno"; 

	private CarLookupManager carLookupManager = null;

	/**
	 * @param carLookupManager the carLookupManager to set
	 */
	public void setCarLookupManager(CarLookupManager carLookupManager) {
		this.carLookupManager = carLookupManager;
	}

	public void setUserManager(UserManager userManager) {
		this.mgr = userManager;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }
        //Below code modified as part of Performance tuning project.
        Integer pageNo=0;
        Long totalUser=0L;
        if(null != request.getParameter(DISPLAY_TABLE_PAGE_NUMBER)) {
			pageNo = new Integer(request.getParameter(DISPLAY_TABLE_PAGE_NUMBER)); 			
		}
        HttpSession session = request.getSession(false);
		session.setAttribute(SELECTED_PAGE_NUMBER, pageNo);	

		request.getSession(true).removeAttribute("userSearchCriteria");
		totalUser=carLookupManager.getUsersCount(null,null,null);
	    Map model = new HashMap();
        model.put("loggedInUser", getLoggedInUser());
        model.put(SELECTED_PAGE_NUMBER, pageNo);
        model.put("totalResultSize", new Integer(totalUser.intValue()));
        // model.put( Constants.USER_LIST, mgr.getUsers(new User()));
        // Rewrite the code of appfuse method call to get 'ACTIVE' users.
        model.put( Constants.USER_LIST, carLookupManager.getUsers());
        return new ModelAndView(getSuccessView(),model);
    }


}
