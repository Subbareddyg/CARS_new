package com.belk.car.app.webapp.controller.oma;

import java.util.ArrayList;
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
import com.belk.car.app.model.Status;
import com.belk.car.app.model.oma.DropshipTaxState;
import com.belk.car.app.model.oma.State;
import com.belk.car.app.service.VendorFulfillmentServiceManager;
import com.belk.car.app.service.VendorTaxStateManager;

/**
 * Controller for vendor tax states.
 * @author afusy07 - Priyanka Gadia Feb 9, 2010
 */
public class VendorTaxStateMultiActionController extends MultiActionController
		implements
			DropShipConstants {

	private transient final Log log = LogFactory.getLog(VendorTaxStateMultiActionController.class);

	private VendorTaxStateManager vendorTaxStateManager;
	private VendorFulfillmentServiceManager vendorFulfillmentServiceManager;

	public User getLoggedInUser() {
		User user = null;
		Authentication auth = ((SecurityContext) SecurityContextHolder.getContext())
				.getAuthentication();
		if (auth.getPrincipal() instanceof UserDetails) {
			user = (User) auth.getPrincipal();
		}
		return user;
	}

	public VendorFulfillmentServiceManager getVendorFulfillmentServiceManager() {
		return vendorFulfillmentServiceManager;
	}

	public void setVendorFulfillmentServiceManager(
			VendorFulfillmentServiceManager vendorFulfillmentServiceManager) {
		this.vendorFulfillmentServiceManager = vendorFulfillmentServiceManager;
	}

	public void setVendorTaxStateManager(VendorTaxStateManager vendorTaxStateManager) {
		this.vendorTaxStateManager = vendorTaxStateManager;
	}

	public VendorTaxStateManager getVendorTaxStateManager() {
		return vendorTaxStateManager;
	}
	
	/**
	 * @param request
	 * @param response
	 * @return void
	 * @Enclosing_Method  setRole
	 * @TODO
	 */
	public void setRole(HttpServletRequest request, HttpServletResponse response){
		User currentUser = getLoggedInUser();
		HttpSession session = request.getSession();
		/*
		 * Check if the current user has order management admin role or order
		 * management user role
		 */
		if ((currentUser.isOrderMgmtAdmin())) {
			session.setAttribute(DISPLAY_ROLE, "admin");

		}
		else  {
			session.setAttribute(DISPLAY_ROLE, "user");
		}
	}

	/**
	 * @param request
	 * @param response
	 * @return void
	 */
	public void getStates(HttpServletRequest request, HttpServletResponse response) {
		/*
		 * Retrieving the values for States Dropdown If present in session get
		 * it else take from DB
		 */
		HttpSession session = request.getSession();
		
			List<State> states = vendorFulfillmentServiceManager.getStatesForVendorTax();
			if (states != null) {
				session.setAttribute(STATES, states);
			}
		
	}

	/**
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @return ModelAndView
	 * @TODO Remove the vendor tax state
	 */
	public ModelAndView remove(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		this.setRole(request, response);
		long vendorTaxId;
		if (StringUtils.isNotBlank(request.getParameter(TAX_ID_FROM_REQUEST))
				&& StringUtils.isNumeric(request.getParameter(TAX_ID_FROM_REQUEST))) {
			vendorTaxId = Long.parseLong(request.getParameter(TAX_ID_FROM_REQUEST));
			vendorTaxStateManager.removeVendorTaxState(vendorTaxId);
		}
		this.getStates(request, response);
		return new ModelAndView(OMA_VENDOR_TAX_STATES_JSP, VENDOR_TAX_STATES_MODEL,
				vendorTaxStateManager.getAllVendorTaxStates());

	}

	/**
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @return ModelAndView
	 * @TODO View All Vendor Tax state
	 */
	public ModelAndView viewAll(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if(log.isDebugEnabled()){
		log.debug("Enetring..................");
		}

		this.setRole(request, response);
		this.getStates(request, response);
		return new ModelAndView(OMA_VENDOR_TAX_STATES_JSP, VENDOR_TAX_STATES_MODEL,
				vendorTaxStateManager.getAllVendorTaxStates());

	}

	/**
	 * @param request
	 * @param response
	 * @throws Exception
	 * @return ModelAndView
	 * @TODO View All active Vendor Tax state
	 */
	public ModelAndView viewAllActive(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		this.setRole(request, response);
		String status = Status.ACTIVE;
		this.getStates(request, response);
		List<DropshipTaxState> vendorTaxStates = new ArrayList<DropshipTaxState>();
		String stateName = "";
		vendorTaxStates = vendorTaxStateManager.getVendorTaxStatesSearch(stateName, status);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("status", "active");
		model.put(VENDOR_TAX_STATES_MODEL, vendorTaxStates);
		return new ModelAndView(OMA_VENDOR_TAX_STATES_JSP, model);

	}

	/**
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @return ModelAndView
	 * @Enclosing_Method search
	 * @TODO Search Vendor Tax state
	 */
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String stateName = "";
		String status = "";

		if (StringUtils.isNotBlank(request.getParameter(STATE_NAME))) {
			stateName = (String) request.getParameter(STATE_NAME);
		}
		// Retrieving status from request
		if (StringUtils.isNotBlank(request.getParameter(STATUS))) {
			status = (String) request.getParameter(STATUS);
		}
		else {
			status = "ALL";
		}
		this.getStates(request, response);

		this.setRole(request, response);
		
		List<DropshipTaxState> vendorTaxStates = new ArrayList<DropshipTaxState>();
		vendorTaxStates = vendorTaxStateManager.getVendorTaxStatesSearch(stateName, status);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(STATE_NAME, stateName);
		model.put("status", status);
		model.put(VENDOR_TAX_STATES_MODEL, vendorTaxStates);
		return new ModelAndView(OMA_VENDOR_TAX_STATES_JSP, model);

	}

}
