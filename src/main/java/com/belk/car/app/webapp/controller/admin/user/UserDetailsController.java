/**
 * 
 */
package com.belk.car.app.webapp.controller.admin.user;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.model.Department;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.webapp.controller.BaseFormController;

/**
 * @author antoniog
 *
 */
public class UserDetailsController extends BaseFormController implements Controller {

	private transient final Log log = LogFactory.getLog(UserDetailsController.class);

	private CarManager carManager = null;
	private CarLookupManager carLookupManager;
	
	/**
	 * Spring Injected manager
	 * @param carLookupManager
	 */
	public void setCarLookupManager(CarLookupManager carLookupManager) {
		this.carLookupManager = carLookupManager;
	}
	
	/**
	 * Spring Injected manager
	 * @param carManager
	 */
	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Map model = new HashMap();
        Long userId = ServletRequestUtils.getLongParameter(request, "id");      
        if (userId == null) {
        	log.debug("User id was null. Redirecting...");
        	return new ModelAndView("redirect:/admin/user/users.html");
        }
        
        User user = getUserManager().getUser(String.valueOf(userId));
        model.put("user", user);
        return new ModelAndView("/admin/user/userDetails", model);
	}

}
