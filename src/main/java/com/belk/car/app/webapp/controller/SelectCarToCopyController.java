package com.belk.car.app.webapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.model.Car;
import com.belk.car.app.util.JSONUtil;
import javax.servlet.http.HttpSession;
public class SelectCarToCopyController extends BaseController {

	private static final String CARID = "carId";
	private static final String TOCAR = "toCar";
	private static final String JSONOBJ = "jsonObj";

	private transient final Log log = LogFactory.getLog(SelectCarToCopyController.class);
	
	public ModelAndView handleRequest(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Long carId = ServletRequestUtils.getLongParameter(request, CARID);
		if (carId == null) {
        	log.debug("CAR id was null. Redirecting...");
            return new ModelAndView("redirect:dashBoard.html");
        }
	
		String scrollPos = request.getParameter("scrollPos");
		log.debug("*****************Scroll Position :"+ scrollPos);
		HttpSession session = request.getSession();
		session.setAttribute("scrollPos", scrollPos);
		Map model = new HashMap();
		// car to copy to
		model.put(TOCAR, this.getCarManager().getCarFromId(carId));
	
		// cars to copy from
		User user = this.getLoggedInUser();
		//Display empty list. This will enforce the search functionality of the page
		model.put(JSONOBJ, JSONUtil.convertCarsToJSON(new ArrayList<Car>(),user,this.getCarLookupManager()));
		//model.put(JSONOBJ, JSONUtil.convertCarsToJSON(this.getCarManager().getCarsForUser(user),user,this.getCarLookupManager()));
		
		return new ModelAndView(this.getSuccessView(), model);
	}

}
