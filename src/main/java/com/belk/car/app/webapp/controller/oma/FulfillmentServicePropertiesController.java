
package com.belk.car.app.webapp.controller.oma;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.service.FulfillmentServiceManager;

/**
 * This controller is used to load the Initial Fulfillment Service Page with
 * load,search,ViewAll functionalities
 */
public class FulfillmentServicePropertiesController extends MultiActionController
		implements
			DropShipConstants {

	private static transient final Log log = LogFactory
			.getLog(FulfillmentServicePropertiesController.class);
	private FulfillmentServiceManager fulfillmentServiceManager;

	public void setFulfillmentServiceManager(FulfillmentServiceManager fulfillmentServiceManager) {
		this.fulfillmentServiceManager = fulfillmentServiceManager;
	}

	/**
	 * Method to get logged in user
	 * @return user
	 */
	public User getLoggedInUser() {
		User user = null;
		Authentication auth = ((SecurityContext) SecurityContextHolder.getContext())
				.getAuthentication();
		if (auth.getPrincipal() instanceof UserDetails) {
			user = (User) auth.getPrincipal();
		}
		return user;
	}

	/**
	 * This method loads all the available fulfillment Services
	 * @return ModelAndView
	 * @param HttpServletRequest request
	 * @param HttpServletResponse response
	 */
	public ModelAndView load(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = request.getSession();

		User currentUser = getLoggedInUser();
		if (log.isDebugEnabled()) {
			log.debug("currentUser in properties controller.." + currentUser);
		}

		/*
		 * Check if the current user has order management admin role or order
		 * management user role
		 */
		if (currentUser.isOrderMgmtAdmin()) {
			if (log.isDebugEnabled()) {
				log.debug("User has role order management admin..set mode to edit");
			}
			session.setAttribute("mode", EDIT);

		} else {
			if (log.isDebugEnabled()) {
				log.debug("User has role order management user/buyer..set mode to viewonly");
			}
			session.setAttribute("mode", VIEW_ONLY_MODE);
		}

		/*
		 * Check required attributes in session
		 */
		if (session.getAttribute(FULFILLMENT_SERVICE_IN_SESSION) != null) {
			log.debug("fulfillment service in session");
			session.removeAttribute(FULFILLMENT_SERVICE_IN_SESSION);
			session.setAttribute(FULFILLMENT_SERVICE_IN_SESSION, null);
		}
		if (session.getAttribute(VEN_INFO_FROM_SESSION) != null) {
			session.removeAttribute(VEN_INFO_FROM_SESSION);
		}
		if (session.getAttribute(ADDED_ADDRESS) != null) {
			session.removeAttribute(ADDED_ADDRESS);
		}

		if (session.getAttribute(FS_VENDOR_FORM) != null) {
			session.removeAttribute(FS_VENDOR_FORM);
		}
		if (session.getAttribute(VEN_INFO_FROM_SESSION) != null) {
			session.removeAttribute(VEN_INFO_FROM_SESSION);
		}
		if (session.getAttribute(ADDR_LIST) != null) {
			session.removeAttribute(ADDR_LIST);
		}

		if (session.getAttribute(ADDR_IDS_FROM_SESSION) != null) {
			session.removeAttribute(ADDR_IDS_FROM_SESSION);
		}
		return new ModelAndView("oma/orderManagementList", "FulfillmentService",
				fulfillmentServiceManager.getFulfillmentServices());
	}

	/**
	 * This method returns view of all available Fulfillment Services
	 * @return ModelAndView
	 * @param HttpServletRequest request
	 * @param HttpServletResponse response
	 */
	public ModelAndView viewAll(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String status= "Active";
		return new ModelAndView("oma/orderManagementList", "FulfillmentService",
				fulfillmentServiceManager.getFulfillmentServices(status));
	}

	/**
	 * This searches for a user entered(in textBox) fulfillment service and
	 * brings back all the related data
	 * @return ModelAndView
	 * @param HttpServletRequest request
	 * @param HttpServletResponse response
	 */
	public ModelAndView searchFS(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String fsName = null;
		String status = null;
		if (StringUtils.isNotBlank(request.getParameter(FULFILLMENT_SERVICE_NAME_SEARCH))) {
			fsName = (String) request.getParameter(FULFILLMENT_SERVICE_NAME_SEARCH);
		}
		if (StringUtils.isNotBlank(request.getParameter(FULFILLMENT_SERVICE_STATUS_CODE))) {
			status = (String) request.getParameter(FULFILLMENT_SERVICE_STATUS_CODE);
			log.debug("status for search" + status);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Queries for: " + fsName);
		}

		/* Method returns List of specified fulfillment service information */

		List<FulfillmentService> fulfillmentServiceInfo = fulfillmentServiceManager.searchFS(
				fsName, status);

		Map<String, Object> newMap = new HashMap<String, Object>();
		newMap.put(FULFILLMENT_SERVICE_NAME_SEARCH, fsName);
		newMap.put(FULFILLMENT_SERVICE_STATUS_CODE, status);
		newMap.put("FulfillmentService", fulfillmentServiceInfo);

		return new ModelAndView("/oma/orderManagementList", newMap);


	}

}
