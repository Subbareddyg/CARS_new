/**
 * @author afusy45 Siddhi Shrishrimal
 * This controller is used to load the Initial Fulfillment Service Notes Page with load,search,ViewAll functionalities
 */
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

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.FulfillmentServiceNotes;
import com.belk.car.app.model.oma.VendorFulfillmentNotes;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import com.belk.car.app.service.FulfillmentServiceNotesManager;

/**
 * Controller for Fulfillment Service Notes
 * @author afusy13
 *
 */
public class FulfillmentServiceNotesController extends MultiActionController implements DropShipConstants  { 


	private FulfillmentServiceNotesManager fulfillmentServiceNotesManager;

	public void setFulfillmentServiceNotesManager(FulfillmentServiceNotesManager fulfillmentServiceNotesManager) {
		this.fulfillmentServiceNotesManager = fulfillmentServiceNotesManager;
	}
	private transient final Log log = LogFactory.getLog(FulfillmentServiceNotesController.class);

	/**
	 * Method to get logged in user
	 * @return user
	 */
	public User getLoggedInUser() {
		User user = null;
		Authentication auth = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (auth.getPrincipal() instanceof UserDetails) {
			user = (User) auth.getPrincipal();
		}
		return user;
	}
	/**
	 * Method loads all the notes for the Fulfillment Service in session
	 * @param request
	 * @param response
	 * @return ModelAndView object
	 * @throws Exception
	 */
	public ModelAndView load(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.debug("inside load method of fulfillment service notes......");

		HttpSession session = request.getSession(false);

		User currentUser = getLoggedInUser();
		if (log.isDebugEnabled()){
			log.debug("currentUser in properties controller.." + currentUser);
		}

		/*
		 * Check if the current user has order management admin role or other
		 * role
		 */
		if (currentUser.isOrderMgmtAdmin()) {
			if (log.isDebugEnabled()){
				log
				.debug("User has role order management admin..set mode to edit");
			}
			session.setAttribute("mode", EDIT);

		}
		else{
			if (log.isDebugEnabled()){
				log.debug("User has role not order management admin..set mode to viewonly");
			}
			session.setAttribute("mode", VIEW_ONLY_MODE);
		}
		FulfillmentService fulfillmentService=(FulfillmentService)session.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION);
		if(null == fulfillmentService){
			return new ModelAndView("redirect:/mainMenu.html");
		}

		String fsID=String.valueOf(fulfillmentService.getFulfillmentServiceID());
		return new ModelAndView("/oma/fulfillmentServiceNotes","FulfillmentServiceNotes",fulfillmentServiceNotesManager.getFulfillmentServicesNotes(fsID));
	}

	/**
	 * Method loads all the notes for the Vendor in session
	 * @param request
	 * @param response
	 * @return ModelAndView object
	 * @throws Exception
	 */
	public ModelAndView loadVendorNote(HttpServletRequest request, HttpServletResponse response) throws Exception{
		if (log.isDebugEnabled()){
			log.debug("inside load method for vendor fulfillment notes......");
		}
		HttpSession session=request.getSession(false);
		FulfillmentServiceVendor vendorFulfillmentServiceModel=(FulfillmentServiceVendor)session.getAttribute(VEN_INFO_FROM_SESSION);
		if(null == vendorFulfillmentServiceModel){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		User currentUser = getLoggedInUser();
		if (log.isDebugEnabled()){
			log.debug("currentUser in notes controller.." + currentUser);
		}

		/*
		 * Check if the current user has order management admin role or order
		 * management user role
		 */
		/*if ((currentUser.isOrderMgmtAdmin()) || (currentUser.isOrderMgmtUser()) || (currentUser.isBuyer())
				|| (currentUser.isWebmerchant())) {*/
		// setting the Edit access only for the Admin role , Buyer and Web Merchant
		// as part of CARS Dropship 2012
		if ((currentUser.isOrderMgmtAdmin())|| (currentUser.isBuyer())) {
			if (log.isDebugEnabled()){
				log.debug("User has role not order management admin..set mode to edit");
			}
			session.setAttribute("mode", EDIT);
		}
		String venID= String.valueOf(vendorFulfillmentServiceModel.getVendorID());
		if (log.isDebugEnabled()){
			log.debug("venID in notes controller--load method--"+venID);
		}
		return new ModelAndView("/oma/fulfillmentServiceNotes","VendorFulfillmentNotes",fulfillmentServiceNotesManager.getVendorFulfillmentNotes(venID));
	}



	/**
	 * Method returns view with all the notes for the Vendor/Fulfillment Service in session
	 * @param request
	 * @param response
	 * @return ModelAndView object
	 * @throws Exception
	 */
	public ModelAndView viewAll(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HttpSession session=request.getSession(false);
		FulfillmentService fulfillmentService=(FulfillmentService)session.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION);
		if(null!= session.getAttribute(VEN_INFO_FROM_SESSION)){
			FulfillmentServiceVendor vendorfsModel=(FulfillmentServiceVendor)session.getAttribute(VEN_INFO_FROM_SESSION);
			String venID= String.valueOf(vendorfsModel.getVendorID());
			return new ModelAndView("/oma/fulfillmentServiceNotes","VendorFulfillmentNotes",fulfillmentServiceNotesManager.getVendorFulfillmentNotes(venID));
		}

		String fsID=String.valueOf(fulfillmentService.getFulfillmentServiceID());
		return new ModelAndView("/oma/fulfillmentServiceNotes","FulfillmentServiceNotes",fulfillmentServiceNotesManager.getFulfillmentServicesNotes(fsID));
	}

	/**
	 * Method searches for note under the fulfillment Service
	 * @param request
	 * @param response
	 * @return ModelAndView object
	 * @throws Exception
	 */
	public ModelAndView searchFS(HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session=request.getSession(false);

		FulfillmentService fulfillmentService=(FulfillmentService)session.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION);
		String fsID= null;
		if(null == fulfillmentService){
			return new ModelAndView("redirect:/mainMenu.html");
		}

		fsID=String.valueOf(fulfillmentService.getFulfillmentServiceID());
		String subject = null;


		if (StringUtils.isNotBlank(request.getParameter("fulfillmentServiceNotesSubject"))) {
			subject = (String) request.getParameter("fulfillmentServiceNotesSubject");
		}

		/*Check if Vendor info exists in session*/
		if(null!= session.getAttribute(VEN_INFO_FROM_SESSION)){
			FulfillmentServiceVendor vendorfsModel=(FulfillmentServiceVendor)session.getAttribute(VEN_INFO_FROM_SESSION);
			String venID= String.valueOf(vendorfsModel.getVendorID());

			/*Method returns List of notes matching the user entered subject under that vendor */
			List<VendorFulfillmentNotes> vendorFulfillmentNotes = fulfillmentServiceNotesManager.searchNoteByVendorID(subject,venID);

			Map<String, Object> newMap = new HashMap<String, Object>();
			newMap.put("fulfillmentServiceNotesSubject", subject);
			newMap.put( "VendorFulfillmentNotes", vendorFulfillmentNotes);

			return new ModelAndView("/oma/fulfillmentServiceNotes",newMap);
		}else if(null== session.getAttribute(VEN_INFO_FROM_SESSION) && null == fulfillmentService ){
			return new ModelAndView("redirect:/mainMenu.html");
		}


		if (log.isDebugEnabled()){
			log.debug("Queries for: " + subject);
		}

		/*Method returns List of notes matching the user entered subject under that fulfillment service */
		List<FulfillmentServiceNotes> fulfillmentServiceInfo = fulfillmentServiceNotesManager.searchNoteBySubject(subject,fsID);
		Map<String, Object> fsMap = new HashMap<String, Object>();
		fsMap.put("fulfillmentServiceNotesSubject", subject);
		fsMap.put( "FulfillmentServiceNotes", fulfillmentServiceInfo);

		return new ModelAndView("/oma/fulfillmentServiceNotes",fsMap);


	}


}
