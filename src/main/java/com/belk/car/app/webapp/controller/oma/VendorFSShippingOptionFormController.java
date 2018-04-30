
package com.belk.car.app.webapp.controller.oma;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.acegisecurity.AccessDeniedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.oma.CompositeKeyForFSShippingOptions;
import com.belk.car.app.model.oma.CompositeKeyForShippingOptions;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.FulfillmentServiceShippingOptions;
import com.belk.car.app.model.oma.ShippingCarrierOption;
import com.belk.car.app.model.oma.VendorFulfillmentShippingOption;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.service.VendorFSShippingOptionManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.VendorFSShippingOptionsForm;

/**
 * Controller for Vendor Fulfillment Service operations.
 * @author afusy07 Feb 9, 2010TODO
 */
public class VendorFSShippingOptionFormController extends BaseFormController
		implements
			DropShipConstants {

	public VendorFSShippingOptionFormController() {
		setCommandName("vendorFSShippingOptionsForm");
		setCommandClass(VendorFSShippingOptionsForm.class);
	}

	VendorFSShippingOptionManager vendorFSShippingOptionManager;
	private static final String SHOW_MSG = "showMsg";

	public VendorFSShippingOptionManager getVendorFSShippingOptionManager() {
		return vendorFSShippingOptionManager;
	}

	public void setVendorFSShippingOptionManager(
			VendorFSShippingOptionManager vendorFSShippingOptionManager) {
		this.vendorFSShippingOptionManager = vendorFSShippingOptionManager;
	}

	private transient final Log log = LogFactory.getLog(VendorFSShippingOptionFormController.class);

	/**
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 * @TODO
	 */
	public ModelAndView processFormSubmission(
			HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Entering ................................");
		}
		HttpSession session = request.getSession();
		//Check whether the user session is timed out 
		if( session.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION) == null){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		if (request.getParameter(CANCEL_BTN) != null) {
			if (log.isDebugEnabled()) {
				log.debug(" Cancelling ......");
			}
			session.removeAttribute(SHIPPING_OPTIONS_MODEL_LIST_SESSION);
			session.removeAttribute(SHIPPING_OPTIONS_SIZE_SESSION);
			FulfillmentServiceVendor venFSModel = (FulfillmentServiceVendor) session
					.getAttribute(VEN_INFO_FROM_SESSION);
			FulfillmentService fulfillmentService = (FulfillmentService) session
					.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION);
			if (null != venFSModel) {
				return new ModelAndView("redirect:/oma/AddVendor.html?mode=fromReturn");

			}
			else if (null != fulfillmentService) {
				return new ModelAndView("redirect:/oma/fsPropertiesList.html?mode="
						+ session.getAttribute("mode") + "&fsID="
						+ fulfillmentService.getFulfillmentServiceID());
			}

			return new ModelAndView(getCancelView());
		}

		// Getting the form object
		VendorFSShippingOptionsForm form = (VendorFSShippingOptionsForm) command;

		List<ShippingCarrierOption> shippingOptionsModelList1 = new ArrayList<ShippingCarrierOption>();
		shippingOptionsModelList1 = form.getShippingOptionsModel();
		/*
		 * Setting the shipping model list in session if any validation error
		 * occurs or user submit the form.
		 */
		session.setAttribute(SHIPPING_OPTIONS_MODEL_LIST_SESSION, shippingOptionsModelList1);
		// Setting the size for some java scripting
		session.setAttribute(SHIPPING_OPTIONS_SIZE_SESSION, shippingOptionsModelList1.size());

		log.debug("Returning .......");
		return super.processFormSubmission(request, response, command, errors);
	}

	/**
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 * @TODO
	 */
	public ModelAndView onSubmit(
			HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors)
			throws Exception {

		// Retrieve form object
		VendorFSShippingOptionsForm form = (VendorFSShippingOptionsForm) command;
		List<ShippingCarrierOption> shippingOptionsModelList = new ArrayList<ShippingCarrierOption>();
		if (log.isDebugEnabled()) {
			log.debug("Entering..............");
		}

		String belkDirect = form.getDirectBelk();
		String vendorDirect = form.getDirectVendor();

		shippingOptionsModelList = form.getShippingOptionsModel();
		if (log.isDebugEnabled()) {
			log.debug("shippingOptionsModelList=" + shippingOptionsModelList.size());
		}

		HttpSession session = request.getSession();

		FulfillmentService fulfillmentService = null;
		FulfillmentServiceVendor vndrFulfillmentServiceModel = null;

		/*
		 * Retrieving the models from Session
		 */
		/*
		 * If user has landed through Vendor Tabs
		 */

		vndrFulfillmentServiceModel = (FulfillmentServiceVendor) session
				.getAttribute(VEN_INFO_FROM_SESSION);

		/*
		 * If user has landed through Fulfillment Service Tabs
		 */

		fulfillmentService = (FulfillmentService) session
				.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION);

		/*
		 * If user has landed through vendor Tabs
		 */
		try {
			if (null != vndrFulfillmentServiceModel) {
				if (log.isDebugEnabled()) {
					log.debug("Got Vendor Model ........");
				}
				long vendorId = vndrFulfillmentServiceModel.getVendorID();
				long fulfillmentServID = vndrFulfillmentServiceModel.getFulfillmentServId();

				VendorFulfillmentShippingOption vendorShip = new VendorFulfillmentShippingOption();
				CompositeKeyForShippingOptions compKey = new CompositeKeyForShippingOptions();
				compKey.setFulfillmentServId(fulfillmentServID);
				compKey.setVendorId(vendorId);

				// Create new vendor ship model to be saved in DB
				vendorShip.setCompositeKeyForShippingOptionsId(compKey);
				vendorShip.setAllowDirectBill(belkDirect);
				vendorShip.setAllowVendorBill(vendorDirect);

				// Save the vendor ship model list in Database
				setAuditInfo(request, vendorShip);
				vendorFSShippingOptionManager.saveVendorFSShippingOptions(vendorShip,
						shippingOptionsModelList);
				session.setAttribute(SHOW_MSG, "yes");
				/*
				 * If user has landed through Fulfillment Service Tabs
				 */

			}
			else if (null != fulfillmentService) {
				if (log.isDebugEnabled()) {
					log.debug("Got fulfillmentService Model ........");
				}
				long fulfillmentServID = fulfillmentService.getFulfillmentServiceID();

				FulfillmentServiceShippingOptions fulfillmentShip = new FulfillmentServiceShippingOptions();
				CompositeKeyForFSShippingOptions compKey = new CompositeKeyForFSShippingOptions();
				compKey.setFulfillmentServId(fulfillmentServID);

				// Create new fulfillment ship model to be saved in DB
				fulfillmentShip.setCompositeKeyForShippingOptionsId(compKey);
				fulfillmentShip.setAllowDirectBill(belkDirect);
				fulfillmentShip.setAllowVendorBill(vendorDirect);

				// Save the fulfillment ship model list in Database
				setAuditInfo(request, fulfillmentShip);
				vendorFSShippingOptionManager.saveFulfillmentShippingOptions(fulfillmentShip,
						shippingOptionsModelList);
				session.setAttribute(SHOW_MSG, "yes");
			}
		}
		catch (AccessDeniedException ade) {
			// thrown by UserSecurityAdvice configured in aop:advisor
			// userManagerSecurity
			if (log.isWarnEnabled()) {
				log.warn(ade.getMessage());
			}
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		catch (Exception e) {
			errors.rejectValue("", "errors.shipping.fs", "errors.shipping.fs");
			if (log.isErrorEnabled()) {
				log.error(e.getCause());
			}
			return showForm(request, response, errors);
		}

		return new ModelAndView("redirect:/oma/shippingOptions.html");
	}

	/**
	 * @param request
	 * @return
	 * @throws Exception
	 * @TODO
	 */
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Entering........");
		}
		User currentUser = getLoggedInUser();

		/*
		 * Check if the current user has order management admin role or order
		 * management user role
		 */
		if ((currentUser.isOrderMgmtAdmin())) {
			request.setAttribute(DISPLAY_ROLE, "admin");

		}
		else {
			request.setAttribute(DISPLAY_ROLE, "user");
		}
		VendorFSShippingOptionsForm vendorFSShippingOptionsForm = new VendorFSShippingOptionsForm();

		HttpSession session = request.getSession();
		FulfillmentService fulfillmentServiceFromSession = (FulfillmentService) session
				.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION);

		if (fulfillmentServiceFromSession == null) {
			return vendorFSShippingOptionsForm;
		}
		if (!isFormSubmission(request)) {

			VendorFulfillmentShippingOption vendorFSShippingOptionsModel = new VendorFulfillmentShippingOption();
			List<ShippingCarrierOption> shippingOptionsModelList = new ArrayList<ShippingCarrierOption>();

			FulfillmentService fulfillmentService = null;
			FulfillmentServiceVendor vendorFulfillmentService = null;

			/*
			 * Retrieving the models from Session If user has landed through
			 * Vendor Tabs
			 */

			vendorFulfillmentService = (FulfillmentServiceVendor) session
					.getAttribute(VEN_INFO_FROM_SESSION);

			/*
			 * If user has landed through Fulfillment Service Tabs
			 */

			fulfillmentService = (FulfillmentService) session
					.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION);
			/*
			 * If user has landed through Vendor Tab
			 */
			if (null != vendorFulfillmentService) {
				if (log.isDebugEnabled()) {
					log.debug("Got Vendor Model ........");
				}
				long vendorId = vendorFulfillmentService.getVendorID();
				long fulfillmentServID = vendorFulfillmentService.getFulfillmentServId();

				// Get the Vendor Shipment from Database
				vendorFSShippingOptionsModel = vendorFSShippingOptionManager
						.getVendorFSShippingOptions(vendorId, fulfillmentServID);
				// Get the shipment list from database
				shippingOptionsModelList = vendorFSShippingOptionManager
						.getShippingOptionsModelList(vendorId, fulfillmentServID);

				// If new vendor which has not configured shipping options
				if (vendorFSShippingOptionsModel == null) {

					vendorFSShippingOptionsModel = new VendorFulfillmentShippingOption();
					if (log.isDebugEnabled()) {
						log.debug("vendor has not configured shipping options ............");
					}
					// Setting composite key for vendor shipment model
					CompositeKeyForShippingOptions c = new CompositeKeyForShippingOptions();
					c.setFulfillmentServId(fulfillmentServID);
					c.setVendorId(vendorId);

					vendorFSShippingOptionsModel.setCompositeKeyForShippingOptionsId(c);
					if (log.isDebugEnabled()) {
						log.debug("shippingOptionsModelList=" + shippingOptionsModelList.size());
					}
					// Save the new vendor shipment models(per shipment class)
					// in database
					List<VendorFulfillmentShippingOption> vendorFSShippingOptionsListnew = vendorFSShippingOptionManager
							.saveVendorFSShippingOptions(vendorFSShippingOptionsModel,
									shippingOptionsModelList);
					vendorFSShippingOptionsModel = vendorFSShippingOptionsListnew.get(0);
				}
				if (log.isDebugEnabled()) {
					log.debug("shippingOptionsModelList account="
							+ shippingOptionsModelList.get(0).getAccount());
				}
				// Set the shipment model in form to be displayed in table
				vendorFSShippingOptionsForm.setShippingOptionsModel(shippingOptionsModelList);

				// Set the shipment model in session for the checkbox values
				session.setAttribute(SHIPPING_OPTIONS_MODEL_LIST_SESSION, shippingOptionsModelList);

				// set the shipment model size in session for some java
				// scripting
				session
						.setAttribute(SHIPPING_OPTIONS_SIZE_SESSION, shippingOptionsModelList
								.size());

				// set vendor bil and Belk bill in form
				vendorFSShippingOptionsForm.setDirectBelk(vendorFSShippingOptionsModel
						.getAllowDirectBill());
				vendorFSShippingOptionsForm.setDirectVendor(vendorFSShippingOptionsModel
						.getAllowVendorBill());
				// else user has landed through fulfillment service tab
			}
			else if (null != fulfillmentService) {
				long fulfillmentServiceID = fulfillmentService.getFulfillmentServiceID();
				if (log.isDebugEnabled()) {
					log.debug("Got FS Model ........fulfillmentServiceID =" + fulfillmentServiceID);
				}
				// Get the fulfillment service Shipment from Database
				FulfillmentServiceShippingOptions fulfillmentShippingOptionsModel = vendorFSShippingOptionManager
						.getFulfillmentServiceShippingOptions(fulfillmentServiceID);
				// Get the shipment list from database
				shippingOptionsModelList = vendorFSShippingOptionManager
						.getShippingOptionsModelListForFulfillment(fulfillmentServiceID);
				// If new fulfillment service which has not configured shipping
				// options
				if (fulfillmentShippingOptionsModel == null) {
					if (log.isDebugEnabled()) {
						log
								.debug("fulfillemnt service  has not configured shipping options ............");
					}
					fulfillmentShippingOptionsModel = new FulfillmentServiceShippingOptions();
					CompositeKeyForFSShippingOptions c = new CompositeKeyForFSShippingOptions();
					c.setFulfillmentServId(fulfillmentServiceID);

					fulfillmentShippingOptionsModel.setCompositeKeyForShippingOptionsId(c);
					if (log.isDebugEnabled()) {
						log.debug("vendor is null=" + fulfillmentShippingOptionsModel.toString());
						log.debug("shippingOptionsModelList=" + shippingOptionsModelList.size());
					}
					// Save the new vendor shipment models(per shipment class)
					// in database
					List<FulfillmentServiceShippingOptions> fulfillmentShippingOptionsListnew = vendorFSShippingOptionManager
							.saveFulfillmentShippingOptions(fulfillmentShippingOptionsModel,
									shippingOptionsModelList);
					fulfillmentShippingOptionsModel = fulfillmentShippingOptionsListnew.get(0);

				}
				if (log.isDebugEnabled()) {
					log.debug("shippingOptionsModelList account="
							+ shippingOptionsModelList.get(0).getAccount());
				}
				// Set the shipment model in form to be displayed in table
				vendorFSShippingOptionsForm.setShippingOptionsModel(shippingOptionsModelList);

				// Set the shipment model in session for the checkbox values
				session.setAttribute(SHIPPING_OPTIONS_MODEL_LIST_SESSION, shippingOptionsModelList);

				// set the shipment model size in session for some java
				// scripting
				session
						.setAttribute(SHIPPING_OPTIONS_SIZE_SESSION, shippingOptionsModelList
								.size());

				// set vendor bil and Belk bill in form
				vendorFSShippingOptionsForm.setDirectBelk(fulfillmentShippingOptionsModel
						.getAllowDirectBill());
				vendorFSShippingOptionsForm.setDirectVendor(fulfillmentShippingOptionsModel
						.getAllowVendorBill());
			}
			return vendorFSShippingOptionsForm;

		}

		return super.formBackingObject(request);
	}

}
