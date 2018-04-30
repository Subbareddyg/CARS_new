
package com.belk.car.app.webapp.controller.vendorcatalog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.vendorcatalog.VendorCatalog;
import com.belk.car.app.model.vendorcatalog.VendorCatalogNote;
import com.belk.car.app.service.VendorCatalogManager;

/**
 * Purpose: MultiAction controller for viewAll/search/load vendor catalog notes
 * Project: EC-2009-00092 - FY 10 eCommerce Drop-Ship Phase I Initial
 * Requirements: BR.002 Description: This class is used for loading/searching
 * vendor catalog notes.
 * 
 * @author afusya2
 */
public class VendorCatalogNotesController extends MultiActionController
		implements
			DropShipConstants {

	private transient final Log log = LogFactory.getLog(VendorCatalogNotesController.class);

	private VendorCatalogManager vendorCatalogManager;

	/**
	 * @return the vendorCatalogManager
	 */
	public VendorCatalogManager getVendorCatalogManager() {
		return vendorCatalogManager;
	}

	/**
	 * @param vendorCatalogManager the vendorCatalogManager to set
	 */
	public void setVendorCatalogManager(VendorCatalogManager vendorCatalogManager) {
		this.vendorCatalogManager = vendorCatalogManager;
	}

	/**
	 * This method loads all the available vendor catalog notes
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView load(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if(log.isDebugEnabled()){
		log.debug("inside load method of vendor catalog notes......");
		}
		HttpSession session = request.getSession(false);
		VendorCatalog vendorCatalog = null;
		if(null != session.getAttribute(VENDOR_CATALOG)){
		 vendorCatalog = (VendorCatalog) request.getSession().getAttribute(
				VENDOR_CATALOG);
		}else {
			//User session is timed out. Redirecting to Dashborad after login.
			return new ModelAndView("redirect:/mainMenu.html");
		}
		//No need to check the roles here, as the variables will already present in session.
		//checkUserRoles(request);
		Long vendorCatalogId = vendorCatalog.getVendorCatalogID();
		return new ModelAndView("vendorCatalog/vendorCatalogNoteDetails", VENDOR_CATALOG_NOTE,
				vendorCatalogManager.getVendorCatalogNotes(vendorCatalogId));
	}
	
	//Commenting the following code, because in future this code is required to check for roles.
//	private void checkUserRoles(HttpServletRequest request) {
//		/*
//		 * Check if the current user has order management admin role or other
//		 * role
//		 */
//		HttpSession session = request.getSession(false);
//
//		User currentUser = getLoggedInUser();
//		if (currentUser.isOrderMgmtAdmin()) {
//			if (log.isDebugEnabled()){
//				log
//				.debug("User has role order management admin..set mode to edit");
//			}
//			session.setAttribute("mode", EDIT);
//
//		}
//		else{
//			if (log.isDebugEnabled()){
//				log.debug("User has role not order management admin..set mode to viewonly");
//			}
//			session.setAttribute("mode", VIEW_ONLY_MODE);
//		}
//		
//	}
	/**
	 * Method to get logged in user
	 * @return user
	 */
//	private User getLoggedInUser() {
//		User user = null;
//		Authentication auth = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
//		if (auth.getPrincipal() instanceof UserDetails) {
//			user = (User) auth.getPrincipal();
//		}
//		return user;
//	}

	/**
	 * This method returns view of all available vendor catalog notes
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView viewAll(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if(log.isDebugEnabled()){
		log.debug("inside view method of vendor catalog notes......");
		}
		HttpSession session = request.getSession(false);
		VendorCatalog vendorCatalog = null;
		if(null != session.getAttribute(VENDOR_CATALOG)){
		 vendorCatalog = (VendorCatalog) request.getSession().getAttribute(
				VENDOR_CATALOG);
		}
		long vendorCatalogId = vendorCatalog.getVendorCatalogID();
		return new ModelAndView("vendorCatalog/vendorCatalogNoteDetails", VENDOR_CATALOG_NOTE,
				vendorCatalogManager.getVendorCatalogNotes(vendorCatalogId));
	}

	/**
	 * This searches for a user entered(in textBox) subject and brings back all
	 * the related data
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView searchNotes(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if(log.isDebugEnabled()){
			log.debug("inside searchNotes method of vendor catalog notes......");
		}
		String subject = null;

		if (StringUtils.isNotBlank(request.getParameter(SUBJECT))) {
			subject = (String) request.getParameter(SUBJECT);
		}

		if (log.isDebugEnabled()) {
			log.debug("Queries for: " + subject);
		}
		HttpSession session = request.getSession(false);
		VendorCatalog vendorCatalog = null;
		if(null != session.getAttribute(VENDOR_CATALOG)){
		vendorCatalog = (VendorCatalog) request.getSession().getAttribute(
				VENDOR_CATALOG);
		}
		long vendorCatalogId = vendorCatalog.getVendorCatalogID();
		/*
		 * Method returns List of notes matching the user entered subject under
		 * that vendor catalog Id
		 */
		List<VendorCatalogNote> catalogNotes = vendorCatalogManager.searchNotes(subject,
				vendorCatalogId);

		Map<String, Object> newMap = new HashMap<String, Object>();
		newMap.put(SUBJECT, subject);
		newMap.put(VENDOR_CATALOG_NOTE, catalogNotes);
		return new ModelAndView("/vendorCatalog/vendorCatalogNoteDetails", newMap);

	}

}
