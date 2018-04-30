
package com.belk.car.app.webapp.controller.oma;

import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.acegisecurity.AccessDeniedException;
import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.oma.Address;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.model.oma.FulfillmentServiceVendorReturn;
import com.belk.car.app.model.oma.ReturnMethod;
import com.belk.car.app.model.oma.State;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.VendorFulfillmentServiceManager;
import com.belk.car.app.util.PropertyLoader;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.VendorReturnForm;

/**
 * Controller for Vendor Return operations
 * @author afusy07-priyanka_gadia@syntelinc.com
 * @Date 12-dec-09
 */
public class VendorReturnsFormController extends BaseFormController implements DropShipConstants {

	private static final String BELK_ADDRESS = "belkAddress";
	private static final String SHOW_MSG = "showMsg";
	private VendorFulfillmentServiceManager vendorFulfillmentServiceManager;

	public VendorFulfillmentServiceManager getVendorFulfillmentServiceManager() {
		return vendorFulfillmentServiceManager;
	}

	public void setVendorFulfillmentServiceManager(
			VendorFulfillmentServiceManager vendorFulfillmentServiceManager) {
		this.vendorFulfillmentServiceManager = vendorFulfillmentServiceManager;
	}

	private CarManager carManager;

	public CarManager getCarManager() {
		return carManager;
	}

	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	/**
	 * @param productManager the productManager to set
	 */

	public VendorReturnsFormController() {
		setCommandName("vendorReturnForm");
		setCommandClass(VendorReturnForm.class);
	}

	/**
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView processFormSubmission(
			HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors)
			throws Exception {
		HttpSession session = request.getSession();
		if (log.isDebugEnabled()) {
			log.debug("Entering........");
		}
		//Check whether the user session is timed out. 
		if( session.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION) == null){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		if (request.getParameter("cancel") != null) {
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
		}

		return super.processFormSubmission(request, response, command, errors);
	}

	/**
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Entering........");
		}
		HttpSession session = request.getSession();
		VendorReturnForm returnForm = new VendorReturnForm();

		User currentUser = getLoggedInUser();
		FulfillmentService fulfillmentServiceFromSession = (FulfillmentService) session
				.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION);

		if (fulfillmentServiceFromSession == null) {
			return returnForm;
		}

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

		Properties properties = PropertyLoader.loadProperties("ftp.properties");

		String location = properties.getProperty("location");
		String addr1 = properties.getProperty("addr1");
		String city = properties.getProperty("city");
		String state = properties.getProperty("state");
		String zip = properties.getProperty("zip");

		Address addrBelk = new Address();
		addrBelk.setLocName(location);
		addrBelk.setAddr1(addr1);
		addrBelk.setCity(city);
		addrBelk.setState(state);
		addrBelk.setZip(zip);

		session.setAttribute(BELK_ADDRESS, addrBelk);
		FulfillmentServiceVendorReturn vendorReturnModel = null;
		if (!isFormSubmission(request)) {

			Enumeration params = session.getAttributeNames();

			FulfillmentService fulfillmentService = null;
			FulfillmentServiceVendor vndrFulfillmentServiceModel = null;
			/*
			 * Retrieving the models from Session
			 */
			while (params.hasMoreElements()) {
				String paramName = (String) params.nextElement();
				/*
				 * If user has landed through Vendor Tabs
				 */
				if (paramName.equals(VEN_INFO_FROM_SESSION)) {
					vndrFulfillmentServiceModel = (FulfillmentServiceVendor) session
							.getAttribute(VEN_INFO_FROM_SESSION);
					log.debug("Got Vendor Model from session=........"
							+ vndrFulfillmentServiceModel.toString());
					break;
				}
				/*
				 * If user has landed through Fulfillment Service Tabs
				 */
				if (paramName.equals(FULFILLMENT_SERVICE_FROM_SESSION)) {
					fulfillmentService = (FulfillmentService) session
							.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION);
					log.debug("Got Fulfillment Service Model from session=........"
							+ fulfillmentService.toString());
					break;
				}
			}
			/*
			 * Retrieving the values for Return Method Dropdown
			 */

			List<ReturnMethod> returnMethModel = vendorFulfillmentServiceManager.getReturnMethods();
			/*
			 * Setting Return Methods in session
			 */
			if (returnMethModel != null) {
				session.setAttribute(RETURN_METHODS, returnMethModel);
			}

			/*
			 * Retrieving the values for States Dropdown
			 */

			List<State> states = vendorFulfillmentServiceManager.getStates();
			/*
			 * Setting states in session
			 */
			if (states != null) {
				session.setAttribute(STATES, states);
			}

			// For Vendor Returns
			if (null != vndrFulfillmentServiceModel) {
				if (log.isDebugEnabled()) {
					log.debug("Got Vendor Model ........");
				}
				long vendorId = vndrFulfillmentServiceModel.getVendorID();
				long fulfillmentServID = vndrFulfillmentServiceModel.getFulfillmentServId();
				// Get Vendor Return corresponding to Vendor ID and Fulfillment
				// ID
				vendorReturnModel = vendorFulfillmentServiceManager.getVendorReturns(vendorId,
						fulfillmentServID);

				// Return Model exist for the given vendor
				if (vendorReturnModel != (null)) {
					if (log.isDebugEnabled()) {
						log.debug("Got Vendor Return Model from DB ........vndrRtrnModel"
								+ vendorReturnModel.toString());
					}

					// populating the form with model
					returnForm = populateForm(returnForm, vendorReturnModel);
					if (log.isDebugEnabled()) {
						log.debug("Returning with  Vendor Return  Model ........");
					}
					return returnForm;
				}// Return Model does not exist for the given vendor
				else {
					returnForm.setVenId((new Long(vendorId)).toString());
					returnForm.setFsId((new Long(fulfillmentServID)).toString());
					if (log.isDebugEnabled()) {
						log.debug("Returning with  Vendor Return  Model ........with vendorId="
								+ vendorId + " and fulfillmentServID=" + fulfillmentServID);
					}
					return returnForm;
				}
			}// For FS Returns
			else if (null != fulfillmentService) {
				long fulfillmentServiceID = fulfillmentService.getFulfillmentServiceID();
				if (log.isDebugEnabled()) {
					log.debug("Got FS Model ........fulfillmentServiceID =" + fulfillmentServiceID);
				}
				// Get FS Return corresponding to Fulfillment ID
				vendorReturnModel = vendorFulfillmentServiceManager
						.getFulfillmentServReturns(fulfillmentServiceID);
				if (vendorReturnModel != (null)) {
					if (log.isDebugEnabled()) {
						log.debug("Got Vendor Return Model from DB ........vndrRtrnModel"
								+ vendorReturnModel.toString());
					}
					// populating the form with model
					returnForm = populateForm(returnForm, vendorReturnModel);
					return returnForm;
				}
				else {// Return Model does not exist for the given FS

					returnForm.setFsId((new Long(fulfillmentServiceID)).toString());
					if (log.isDebugEnabled()) {
						log.debug("Returning with  Vendor Return  Model ........with fsid="
								+ fulfillmentServiceID);
					}
					return returnForm;
				}
			}
		}
		else if (request.getParameter("returnId") != null
				&& !"".equals(request.getParameter("returnId"))) {
			vendorReturnModel = (FulfillmentServiceVendorReturn) vendorFulfillmentServiceManager
					.getById(FulfillmentServiceVendorReturn.class, Long.valueOf(request
							.getParameter("returnId")));
			returnForm.setReturnModel(vendorReturnModel);
			if(request.getParameter("stateCdHidden") !=null && !"".equals(request.getParameter("stateCdHidden"))){
				returnForm.setState(request.getParameter("stateCdHidden"));
				log.debug("return form="+returnForm.getState());
			}
			return returnForm;
		}
		else {
			//If in not submit reuquest and a new reuturn for which return id has not been geenrated set the state code which is hidden i.e. NC if it is not null
			
			returnForm=new VendorReturnForm();
			if(request.getParameter("stateCdHidden") !=null && !"".equals(request.getParameter("stateCdHidden"))){
				returnForm.setState(request.getParameter("stateCdHidden"));
				log.debug("return form="+returnForm.getState());
			}
			return  returnForm;
		}

		return super.formBackingObject(request);

	}

	/**
	 * @param returnForm
	 * @param vendorReturn
	 * @return VendorReturnForm Method to populate form with the model object
	 */
	private VendorReturnForm populateForm(
			VendorReturnForm returnForm, FulfillmentServiceVendorReturn vendorReturn) {
		returnForm.setAddr1(vendorReturn.getAddr().getAddr1());
		returnForm.setAddr2(vendorReturn.getAddr().getAddr2());
		returnForm.setCity(vendorReturn.getAddr().getCity());
		returnForm.setState(vendorReturn.getAddr().getState());
		returnForm.setZip(vendorReturn.getAddr().getZip());
		returnForm.setLocName(vendorReturn.getAddr().getLocName());
		returnForm.setReturnModel(vendorReturn);

		if (vendorReturn.getFulfillmentServId() != null) {
			returnForm.setFsId(vendorReturn.getFulfillmentServId().toString());
		}
		if (vendorReturn.getVendorID() != null) {
			returnForm.setVenId(vendorReturn.getVendorID().toString());
		}
		if (vendorReturn.getVendorReturnId() != null) {
			returnForm.setReturnId(vendorReturn.getVendorReturnId().toString());
		}
		if (vendorReturn.getRmaNum() != null) {
			returnForm.setDropShipRma(vendorReturn.getRmaNum().toString());
		}
		returnForm.setReturnMethod(vendorReturn.getRtrnMethod().getRtnMethCode());
		if (vendorReturn.getRtrnMethod().getRtnMethCode().equals("4")) {
			returnForm.setReturnMethod("2");
			returnForm.setReturnMethodType("4");
		}
		else if (vendorReturn.getRtrnMethod().getRtnMethCode().equals("2")) {
			returnForm.setReturnMethodType("2");
		}

		if (vendorReturn.getAddrId() != null) {
			returnForm.setAddrId(vendorReturn.getAddrId().toString());
		}

		return returnForm;
	}

	/**
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return ModelAndView
	 */
	public ModelAndView onSubmit(
			HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Entering........");
		}
		HttpSession session = request.getSession();

		VendorReturnForm vendorReturnForm = (VendorReturnForm) command;
		FulfillmentServiceVendorReturn vndrRtrnModel = null;

		// Populate vendor Model with the form values
		vndrRtrnModel = populateModel(vndrRtrnModel, vendorReturnForm);

		setAuditInfo(request, vndrRtrnModel);

		try {
			vndrRtrnModel = vendorFulfillmentServiceManager.saveVendorReturn(vndrRtrnModel);
			if (null != vndrRtrnModel.getVendorReturnId()) {
				session.setAttribute(SHOW_MSG, "yes");
			}
		}
		catch (AccessDeniedException ade) {
			if (log.isWarnEnabled()) {
				log.warn(ade.getMessage());
			}
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		catch (Exception e) {
			errors.rejectValue("", "", "Return Could Not be Saved.Please Try Again!!");
			if (log.isWarnEnabled()) {
				log.warn(e.getMessage());
			}
			return showForm(request, response, errors);

		}
		if (log.isDebugEnabled()) {
			log.debug("Befor saving........ vendor return model=" + vndrRtrnModel.toString());
		}

		return new ModelAndView("redirect:/oma/fulfillmentVendorReturns.html");
	}

	/**
	 * @param vendorReturn
	 * @param vendorReturnForm
	 * @return FulfillmentServiceVendorReturn
	 * @TODO Populate Model From Form
	 */
	private FulfillmentServiceVendorReturn populateModel(
			FulfillmentServiceVendorReturn vendorReturn, VendorReturnForm vendorReturnForm) {
		Address address = new Address();

		if (vendorReturnForm.getReturnModel() == null) {// New Return
			vendorReturn = new FulfillmentServiceVendorReturn();
		}
		else {
			vendorReturn = vendorReturnForm.getReturnModel(); // Update Return
		}

		if (!StringUtils.isBlank(vendorReturnForm.getFsId())) {
			vendorReturn.setFulfillmentServId(Long.valueOf(vendorReturnForm.getFsId()));
		}

		if (!StringUtils.isBlank(vendorReturnForm.getVenId())) {
			vendorReturn.setVendorID(Long.valueOf(vendorReturnForm.getVenId()));
		}
		if (!StringUtils.isBlank(vendorReturnForm.getReturnMethod())) {
			ReturnMethod rtnMethod = (ReturnMethod) vendorFulfillmentServiceManager.getById(
					ReturnMethod.class, vendorReturnForm.getReturnMethod());
			vendorReturn.setRtrnMethod(rtnMethod);
			if (!StringUtils.isBlank(vendorReturnForm.getReturnMethodType())) {
				ReturnMethod rtnMethodType = (ReturnMethod) vendorFulfillmentServiceManager
						.getById(ReturnMethod.class, vendorReturnForm.getReturnMethodType());
				vendorReturn.setRtrnMethod(rtnMethodType);
			}
		}
		if (!StringUtils.isBlank(vendorReturnForm.getAddrId())) {
			vendorReturn.setAddrId(Long.valueOf(vendorReturnForm.getAddrId()));
			address.setAddressID(Long.valueOf(vendorReturnForm.getAddrId()));
		}
		if (!StringUtils.isBlank(vendorReturnForm.getDropShipRma())) {
			vendorReturn.setRmaNum(Long.valueOf(vendorReturnForm.getDropShipRma()));
		}
		else vendorReturn.setRmaNum(null);
		if (!StringUtils.isBlank(vendorReturnForm.getReturnId())) {
			vendorReturn.setVendorReturnId(Long.valueOf(vendorReturnForm.getReturnId()));
		}
		address.setAddr1(vendorReturnForm.getAddr1());
		address.setAddr2(vendorReturnForm.getAddr2());
		address.setState(vendorReturnForm.getState());
		address.setCity(vendorReturnForm.getCity());
		address.setZip(vendorReturnForm.getZip());
		address.setLocName(vendorReturnForm.getLocName());

		vendorReturn.setAddr(address);
		vendorReturn.setStatusCd(Status.ACTIVE);
		return vendorReturn;
	}
}
