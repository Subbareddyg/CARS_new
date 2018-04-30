package com.belk.car.app.webapp.controller.oma;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.service.VendorFulfillmentServiceManager;

/**
 * @author afusy07- priyanka_gadia@syntelinc.com
 * @Date- 10-dec-09
 */
public class VendorFulfillmentServiceController extends MultiActionController
implements
DropShipConstants {

	private transient final Log log = LogFactory.getLog(VendorFulfillmentServiceController.class);

	private VendorFulfillmentServiceManager vendorFulfillmentServiceManager;

	public VendorFulfillmentServiceManager getVendorFulfillmentServiceManager() {
		return vendorFulfillmentServiceManager;
	}

	/**
	 * @param vendorFulfillmentServiceManager
	 * @return void
	 * @Enclosing_Method setVendorFulfillmentServiceManager
	 * @TODO
	 */
	public void setVendorFulfillmentServiceManager(
			VendorFulfillmentServiceManager vendorFulfillmentServiceManager) {
		this.vendorFulfillmentServiceManager = vendorFulfillmentServiceManager;
	}

	/**
	 * @return User
	 * @Enclosing_Method getLoggedInUser - Returns the logged in user.
	 * @TODO
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
	 * @param request - HttpServletRequest
	 * @param response - HttpServletResponse
	 * @return void
	 * @Enclosing_Method setRole - Set the role of the logged in user in session to provide access control
	 * @TODO
	 */
	public void setRole(HttpServletRequest request, HttpServletResponse response) {
		User currentUser = getLoggedInUser();

		HttpSession session = request.getSession();
		/*
		 * Check if the current user has order management admin role or order
		 * management user role
		 */
		if ((currentUser.isOrderMgmtAdmin())) {

			session.setAttribute(DISPLAY_ROLE, "admin");
			// added mode to session as part of CARS dropship 2012
			session.setAttribute("mode", EDIT);

		}
		else {

			session.setAttribute(DISPLAY_ROLE, "user");
			// added mode to session as part of CARS dropship 2012
			session.setAttribute("mode", VIEW_ONLY_MODE);
		}
	}

	/**
	 * @Method View All Vendors under the fulfillment service in session
	 * @param request -HttpServletRequest
	 * @param response -HttpServletResponse
	 * @return ModelAndView - Return all the vendors under one fulfillment service
	 * @throws Exception
	 */
	public ModelAndView viewAll(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		setRole(request, response);
		HttpSession session = request.getSession();
		FulfillmentService fulfillmentService = (FulfillmentService) session
		.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION);
		long fulfillmentServiceId = 0;

		// Retrieving fulfillment service ID from session to get the vendors
		// under that FS.
		if (null != fulfillmentService) {
			if (log.isDebugEnabled()) {
				log.debug("Got the fulfillment service from session="
						+ fulfillmentService.getFulfillmentServiceID());
			}
			fulfillmentServiceId = fulfillmentService.getFulfillmentServiceID();
		}else{
			return new ModelAndView("redirect:/mainMenu.html");
		}
		session.setAttribute(LOGGEN_IN_USER, this.getLoggedInUser().getUsername());
		session.setAttribute("pagination", 1);

		// Removing the vendor data from session which was set on vendor
		// properties page.
		// This would be useful when user comes on vendor listing page through
		// breadcrumb.
		session.removeAttribute(FS_VENDOR_FORM);
		session.removeAttribute(VEN_INFO_FROM_SESSION);
		session.removeAttribute(ADDR_LIST);
		session.removeAttribute(ADDR_LIST);
		session.removeAttribute(ADDR_IDS_FROM_SESSION);
		session.removeAttribute(ADDED_ADDRESS);
		session.removeAttribute("searchVendor");

		List<FulfillmentServiceVendor> vendorList = vendorFulfillmentServiceManager
		.getAllVendorFulfillmentServices(fulfillmentServiceId);
		List<FulfillmentServiceVendor> vendorListNew = null;
		if (null != vendorList && !vendorList.isEmpty()) {
			vendorListNew = vendorFulfillmentServiceManager.getActiveStyleSkusForVendors(
					vendorList, fulfillmentServiceId);
		}
		request.setAttribute("scrollPos", 0);
		
		vendorFulfillmentServiceManager.unlockFulfillmntServiceVendor(getLoggedInUser().getUsername());
		return new ModelAndView(PATH_TO_VENDOR_LIST_JSP, VENDOR_LIST_MODEL_NAME, vendorListNew);
	}

	/**
	 * @Method View All Vendors under the fulfillment service in session
	 * @param request -Servlet request
	 * @param response - Servlet response
	 * @return ModelAndView -List of all the active vendors.
	 * @throws Exception
	 */
	public ModelAndView viewAllActive(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		setRole(request, response);
		HttpSession session = request.getSession();
		String vendorName = "";
		Map<String, Object> model = new HashMap<String, Object>();

		FulfillmentService fulfillmentService = (FulfillmentService) session
		.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION);
		long fulfillmentServiceId = 0;

		// Retrieving fulfillment service ID from session to get the vendors
		// under that FS.
		if (null != fulfillmentService) {
			if (log.isDebugEnabled()) {
				log.debug("Got the fulfillment service from session="
						+ fulfillmentService.getFulfillmentServiceID());
			}
			fulfillmentServiceId = fulfillmentService.getFulfillmentServiceID();
		}else{
			return new ModelAndView("redirect:/mainMenu.html");
		}
		session.setAttribute(LOGGEN_IN_USER, this.getLoggedInUser().getUsername());
		session.setAttribute("pagination", 1);

		// Removing the vendor data from session which was set on vendor
		// properties page.
		// This would be useful when user comes on vendor listing page through
		// breadcrumb.
		session.removeAttribute(FS_VENDOR_FORM);
		session.removeAttribute(VEN_INFO_FROM_SESSION);
		session.removeAttribute(ADDR_LIST);
		session.removeAttribute(ADDR_LIST);
		session.removeAttribute(ADDR_IDS_FROM_SESSION);
		session.removeAttribute(ADDED_ADDRESS);
		session.removeAttribute("searchVendor");

		List<FulfillmentServiceVendor> vendorList = vendorFulfillmentServiceManager
		.getVendorFulfillmentServices(vendorName, null, "ACTIVE", fulfillmentServiceId);
		List<FulfillmentServiceVendor> vendorListNew = null;
		if (null != vendorList && !vendorList.isEmpty()) {
			vendorListNew = vendorFulfillmentServiceManager.getActiveStyleSkusForVendors(
					vendorList, fulfillmentServiceId);
		}
		model.put("status", "active");
		request.setAttribute("scrollPos", 0);
		model.put(VENDOR_LIST_MODEL_NAME, vendorListNew);
		return new ModelAndView(PATH_TO_VENDOR_LIST_JSP, model);
	}

	/**
	 * @Method Lock a Vendor under the fulfillment service in session
	 * @param request
	 * @param response
	 * @return - Return the vendor list after locking the vendor
	 * @throws Exception
	 */
	/*public ModelAndView unlock(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		HttpSession session = request.getSession();
		setRole(request, response);
		FulfillmentService fulfillmentService = (FulfillmentService) session
		.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION);

		long fulfillmentServiceId = 0;

		// Retrieving fulfillment service ID from session to get the vendors
		// under that FS.
		if (null != fulfillmentService) {
			fulfillmentServiceId = fulfillmentService.getFulfillmentServiceID();
			if (log.isDebugEnabled()) {
				log.debug("Got the fulfillment service from session="
						+ fulfillmentService.getFulfillmentServiceID());
			}
		}else{
			return new ModelAndView("redirect:/mainMenu.html");
		}
		String vendorFulfillId = "";
		if (StringUtils.isNotBlank(request.getParameter(VENDOR_FS_ID_FROM_REQUEST))) {
			vendorFulfillId = (String) request.getParameter(VENDOR_FS_ID_FROM_REQUEST);
		}
		FulfillmentServiceVendor vndrModel = vendorFulfillmentServiceManager
		.getVendorFulfillmentServicesByID(Long.valueOf(vendorFulfillId));

		// Locking the user

		vndrModel.setIsLocked(YES);

		vndrModel.setLockedBy(this.getLoggedInUser().getUsername());
		if (log.isDebugEnabled()) {
			log.debug("Locked by  user=" + vndrModel.getLockedBy());
			log.debug("User in session=" + this.getLoggedInUser().getUsername());
		}
		vendorFulfillmentServiceManager.addVendorFulfillmentService(vndrModel);
		Map<String, Object> model = new HashMap<String, Object>();
		model = (Map<String, Object>) session.getAttribute("searchVendor");
		List<FulfillmentServiceVendor> vendorList = null;
		request.setAttribute("scrollPos", request.getParameter("scrollPos"));
		log.debug("scrollPos="+request.getParameter("scrollPos"));
		setDisplayTableParameters(request);
		if (model != null) {

			vendorList = vendorFulfillmentServiceManager.getVendorFulfillmentServices(model.get(
			"vendorName").toString(), StringUtils
			.isNotBlank((String) model.get("vendorID"))
			&& StringUtils.isNumeric((String) model.get("vendorID")) ? new Long(model.get(
			"vendorID").toString()) : null, model.get("status").toString(),
			fulfillmentServiceId);
			List<FulfillmentServiceVendor> vendorListNew = null;
			if (null != vendorList && !vendorList.isEmpty()) {
				vendorListNew = vendorFulfillmentServiceManager.getActiveStyleSkusForVendors(
						vendorList, fulfillmentServiceId);
			}
			model.put(VENDOR_LIST_MODEL_NAME, vendorListNew);
			session.setAttribute("searchVendor", model);
			return new ModelAndView(PATH_TO_VENDOR_LIST_JSP, model);
		}
		else {
			vendorList = vendorFulfillmentServiceManager
			.getAllVendorFulfillmentServices(fulfillmentServiceId);
			List<FulfillmentServiceVendor> vendorListNew = null;
			if (null != vendorList && !vendorList.isEmpty()) {
				vendorListNew = vendorFulfillmentServiceManager.getActiveStyleSkusForVendors(
						vendorList, fulfillmentServiceId);
			}

			return new ModelAndView(PATH_TO_VENDOR_LIST_JSP, VENDOR_LIST_MODEL_NAME, vendorListNew);
		}
	}*/

	/**
	 * @Method Unlock a Vendor under the fulfillment service in session
	 * @param request -HttpServletRequest
	 * @param response - HttpServletResponse
	 * @return Return the vendor list after unlocking the vendor
	 * @throws Exception
	 */
	/*public ModelAndView lock(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		String venFsId = "";
		HttpSession session = request.getSession();
		setRole(request, response);
		FulfillmentService fulfillmentService = (FulfillmentService) session
		.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION);

		long fulfillmentServiceId = 0;
		if (null != fulfillmentService) {
			fulfillmentServiceId = fulfillmentService.getFulfillmentServiceID();
			if (log.isDebugEnabled()) {
				log.debug("Got the fulfillment service from session="
						+ fulfillmentService.getFulfillmentServiceID());
			}
		} else{
			return new ModelAndView("redirect:/mainMenu.html");
		}
		if (StringUtils.isNotBlank(request.getParameter(VENDOR_FS_ID_FROM_REQUEST))) {
			venFsId = (String) request.getParameter(VENDOR_FS_ID_FROM_REQUEST);
		}
		FulfillmentServiceVendor vndrModel = vendorFulfillmentServiceManager
		.getVendorFulfillmentServicesByID(Long.valueOf(venFsId));
		vndrModel.setIsLocked(NO);
		if (log.isDebugEnabled()) {
			log.debug("Locked by  user=" + vndrModel.getLockedBy());
			log.debug("User in session=" + this.getLoggedInUser().getUsername());
		}
		vndrModel.setLockedBy("");

		vendorFulfillmentServiceManager.addVendorFulfillmentService(vndrModel);
		Map<String, Object> model = new HashMap<String, Object>();
		model = (Map<String, Object>) session.getAttribute("searchVendor");
		List<FulfillmentServiceVendor> vendorList = null;
		request.setAttribute("scrollPos", request.getParameter("scrollPos"));
		log.debug("scrollPos="+request.getParameter("scrollPos"));
		setDisplayTableParameters(request);
		if (model != null) {

			vendorList = vendorFulfillmentServiceManager.getVendorFulfillmentServices(model.get(
			"vendorName").toString(), StringUtils
			.isNotBlank((String) model.get("vendorID"))
			&& StringUtils.isNumeric((String) model.get("vendorID")) ? new Long(model.get(
			"vendorID").toString()) : null, model.get("status").toString(),
			fulfillmentServiceId);
			List<FulfillmentServiceVendor> vendorListNew = null;
			if (null != vendorList && !vendorList.isEmpty()) {
				vendorListNew = vendorFulfillmentServiceManager.getActiveStyleSkusForVendors(
						vendorList, fulfillmentServiceId);
			}
			model.put(VENDOR_LIST_MODEL_NAME, vendorListNew);
			session.setAttribute("searchVendor", model);
			return new ModelAndView(PATH_TO_VENDOR_LIST_JSP, model);
		}
		else {
			vendorList = vendorFulfillmentServiceManager
			.getAllVendorFulfillmentServices(fulfillmentServiceId);
			List<FulfillmentServiceVendor> vendorListNew = null;
			if (null != vendorList && !vendorList.isEmpty()) {
				vendorListNew = vendorFulfillmentServiceManager.getActiveStyleSkusForVendors(
						vendorList, fulfillmentServiceId);
			}

			return new ModelAndView(PATH_TO_VENDOR_LIST_JSP, VENDOR_LIST_MODEL_NAME, vendorListNew);
		}

	}*/

	/** 
	 * @Method Search vendors under the fulfillment service in session
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return Returns the vendor list based on the search criteria
	 * @throws Exception
	 */
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		String vendorName = "";
		String vendorId = "";

		String status = "";
		HttpSession session = request.getSession();
		setRole(request, response);
		session.setAttribute(LOGGEN_IN_USER, this.getLoggedInUser().getUsername());
		FulfillmentService fulfillmentService = (FulfillmentService) session
		.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION);

		long fulfillmentServiceId = 0;
		if (null != fulfillmentService) {
			fulfillmentServiceId = fulfillmentService.getFulfillmentServiceID();
			if (log.isDebugEnabled()) {
				log.debug("Got the fulfillment service from session="
						+ fulfillmentService.getFulfillmentServiceID());
			}
		} else {
			return new ModelAndView("redirect:/mainMenu.html");
		}

		// Retrieving vendor names from request
		if (StringUtils.isNotBlank(request.getParameter(VENDOR_NAME_FROM_REQUEST))) {
			vendorName = (String) request.getParameter(VENDOR_NAME_FROM_REQUEST);
		}
		// Retrieving status from request
		if (StringUtils.isNotBlank(request.getParameter(STATUS))) {
			status = (String) request.getParameter(STATUS);

		}
		else {
			status = "ALL";
		}

		Long vendorID = null;

		Map<String, Object> model = new HashMap<String, Object>();

		// Retrieving vendor id from request
		if (StringUtils.isNotBlank(request.getParameter(VENDOR_ID))) {
			vendorId = (String) request.getParameter(VENDOR_ID);

			try {
				vendorID = new Long(vendorId);
			}
			catch (NumberFormatException nfe) {
				if (log.isDebugEnabled()) {
					log.debug(" VendorSearch ------- > Number format exception");
				}
				nfe.printStackTrace();
				model.put("vendorName", vendorName);
				model.put("status", status);
				model.put("vendorID", vendorId);
				model.put("searchFormError", getText("productType.search.number.format.error", request.getLocale()));
				return new ModelAndView(PATH_TO_VENDOR_LIST_JSP,model );

			}
		}
		if (log.isDebugEnabled()) {
			log.debug("Queries for:vendorName= " + vendorName + " :vendorId= " + vendorId
					+ ":status=" + status + ":fulfillmentServiceId=" + fulfillmentServiceId);
		}
		// Removing session data which came from Vendor properties page
		session.removeAttribute(FS_VENDOR_FORM);
		session.removeAttribute(VEN_INFO_FROM_SESSION);
		session.removeAttribute(ADDR_LIST);
		session.removeAttribute(ADDR_LIST);
		session.removeAttribute(ADDR_IDS_FROM_SESSION);

		List<FulfillmentServiceVendor> vendorList = vendorFulfillmentServiceManager
		.getVendorFulfillmentServices(vendorName, vendorID, status, fulfillmentServiceId);
		List<FulfillmentServiceVendor> vendorListNew = null;
		if (null != vendorList && !vendorList.isEmpty()) {
			vendorListNew = vendorFulfillmentServiceManager.getActiveStyleSkusForVendors(
					vendorList, fulfillmentServiceId);
		}
		model.put("vendorName", vendorName);
		model.put("status", status);
		model.put("vendorID", vendorId);
		model.put(VENDOR_LIST_MODEL_NAME, vendorListNew);
		session.setAttribute("searchVendor", model);
		request.setAttribute("scrollPos", 0);
		session.setAttribute("pagination", 1);
		return new ModelAndView(PATH_TO_VENDOR_LIST_JSP, model);
	}

	public String getText(String msgKey, Locale locale) {
		return getMessageSourceAccessor().getMessage(msgKey, locale);
	}

	/**
	 * Sets  the Display table parameters like d-<Table_Id>-p, to retain the previous actions of the user.
	 * This is required when user selects any page and clicks on Lock/Unlock button.
	 * This method will retain those page numbers and user could view the same page if clicked on any Lock/Unlock button.
	 * @param request	{@link HttpServletRequest} Request Object 
	 */
	private void setDisplayTableParameters(HttpServletRequest request) {
		if(log.isDebugEnabled()){
			log.debug("Inside setDisplayTableParameters() method.");
		}
		HttpSession session = request.getSession();
		String page=request.getParameter("d-49788-p");
		log.debug("Pagination Parameter:"+ page);
		if(StringUtils.isBlank(page)){
			log.debug("Setting pagination parameter to 1.");
			page="1";
		}
		log.debug("Setting pagination parameter to :"+ page);
		//Set the parameter in request.
		session.setAttribute("pagination", page);
	}
}
