
package com.belk.car.app.webapp.controller.oma;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.acegisecurity.AccessDeniedException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.oma.Address;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.service.FulfillmentServiceManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.FulfillmentServiceForm;

/**
 * This form controller handles all the operations related to Fulfillment
 * Service Form
 */
public class FulfillmentServiceFormController extends BaseFormController
		implements
			DropShipConstants,
			Controller {

	private FulfillmentServiceManager fulfillmentServiceManager;

	public void setFulfillmentServiceManager(
			FulfillmentServiceManager fulfillmentServiceManager) {
		this.fulfillmentServiceManager = fulfillmentServiceManager;
	}

	public FulfillmentServiceFormController() {
		setCommandName(FULFILLMENT_SERVICE_FORM);
		setCommandClass(FulfillmentServiceForm.class);
		setSessionForm(true);
	}

	/**
	 * This method is called after initial call to formBackingObject() when a
	 * user submits the form
	 * @return ModelAndView
	 * @param HttpServletRequest request
	 * @param HttpServletResponse response
	 * @param Object command
	 * @param BindException errors
	 */
	public ModelAndView processFormSubmission(
			HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors)
			throws Exception {
		HttpSession session = request.getSession(false);
		FulfillmentServiceForm fulfillmentServiceForm=  new FulfillmentServiceForm();
		if (log.isDebugEnabled())
		log
				.debug("In processFormSubmission ");

		if (request.getParameter(CANCEL_BTN) != null) {

			return new ModelAndView(getCancelView());

		}
		
		
		return super.processFormSubmission(request, response, command, errors);
	}

	/**
	 * This method is called when user submits the form The user entered
	 * information is saved/updated in DB and also stored in seesion to be used
	 * in other scenarios(requirements)
	 * @return ModelAndView
	 * @param HttpServletRequest request
	 * @param HttpServletResponse response
	 * @param Object command
	 * @param BindException errors
	 */
	public ModelAndView onSubmit(
			HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors)
			throws Exception {
		if (log.isDebugEnabled()){
			log.debug("Entering fulfillment service form 'onSubmit' Method...");
		}
		FulfillmentServiceForm fulfillmentServiceForm = (FulfillmentServiceForm) command;
		boolean notUniqueName =true;
		
		HttpSession session = request.getSession(false);
		String mode=(session.getAttribute("mode") !=null)?session.getAttribute("mode").toString():""; 
		try {

			setAuditInfo(request, fulfillmentServiceForm
					.getFulfillmentService());
			
			/*Check for unique fulfillment service name*/
			
			List<FulfillmentService> fulfillmentServiceList = fulfillmentServiceManager.getFulfillmentServices();
			for(int i=0;i<fulfillmentServiceList.size();i++){
				if (log.isDebugEnabled()){
					log.debug("Inside for loop of fulfillment services");
				}
				FulfillmentService fulfillmentService =fulfillmentServiceList.get(i);
				String fulfillmentServiceName =fulfillmentService.getFulfillmentServiceName();
				Long fulfillmentServiceID = fulfillmentService.getFulfillmentServiceID();
				
				if(fulfillmentServiceName.equalsIgnoreCase(fulfillmentServiceForm.getFulfillmentService().getFulfillmentServiceName())
						&& fulfillmentServiceID !=fulfillmentServiceForm.getFulfillmentService().getFulfillmentServiceID()){
					if (log.isDebugEnabled()){
						log.debug("If service name already exists");
					}
					session.setAttribute("UniqueServiceName", "yes");
					errors.rejectValue("fulfillmentService.servicename", "VALID", "Service Name must be Unique");
					notUniqueName =false;
					
											
				}else{
					session.setAttribute("UniqueServiceName", "no");
									
				}
			}
			
			
			if(notUniqueName){
				/*
				 * Method to sava/update the user entered information in form to DB
				 * and return it in fulfillment service object
				 */
				log.debug("Save the service");
				FulfillmentService fulfillmentService = fulfillmentServiceManager
					.updateFulfillmentService(fulfillmentServiceForm,mode);
			
			/* Set fulfillment service object in session */
			session.setAttribute(FULFILLMENT_SERVICE_IN_SESSION,
					fulfillmentService);
			/*
			 * Keeping a value for save message in session to be checked on jsp
			 * while displaying
			 */
			session.setAttribute(SAVE_MSG_FULFILLMENTSERVICE, "yes");
			errors.rejectValue("", "VALID", SAVED_SUCCESSFULLY_MESSAGE);
			
			String fsId = (String.valueOf(fulfillmentService.getFulfillmentServiceID()));
			
			return showForm(request, errors,
					"redirect:/oma/fsPropertiesList.html?mode=edit&fsID="
							+ fsId);
			
			}
						

		}catch (DataIntegrityViolationException e) {
			errors.rejectValue(null,
					"errors.fulfillmentService.exist", new Object[] { fulfillmentServiceForm
					.getFulfillmentService().getFulfillmentServiceName() }, "already exisits!");
			e.printStackTrace();
			return showForm(request, response, errors);
		}
		catch (AccessDeniedException ade) {
			if (log.isDebugEnabled()){
				log.warn(ade.getMessage());
			}
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		catch (Exception e) {
			errors.rejectValue(null, "errors",
					new Object[] { fulfillmentServiceForm
							.getFulfillmentService()
							.getFulfillmentServiceName() },
					"Error.Classification Already Exists");
		}
		if (log.isDebugEnabled())
			log.debug("end of onsubmit........");
		
		session.setAttribute("serviceForm",fulfillmentServiceForm);
		
		return showForm(request, errors,
		"redirect:/oma/fsPropertiesList.html?mode="+session.getAttribute("mode"));

	}

	/**
	 * FormBackingObject method is called first whenever this controller is
	 * called. Consists of logic to be implemented before the form is displayed.
	 * Contains condition for add,edit and respective actions Method is called
	 * again when the form is submitted
	 * @return Object
	 * @param HttpServletRequest request
	 */
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		if (log.isDebugEnabled()){
			log.debug("!isFormSubmission(request)" + isFormSubmission(request));
		}
		FulfillmentService fulfillmentService = null;
		HttpSession session = request.getSession(false);

		/* Check if vendor fulfillment service info exists in session */
		if (session.getAttribute(VEN_INFO_FROM_SESSION) != null) {
			session.setAttribute(VEN_INFO_FROM_SESSION, null);
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
		
		FulfillmentServiceForm fulfillmentServiceForm = new FulfillmentServiceForm();

		/*
		 * Need to check if Integration Types and Fulfillment Methods already
		 * exist in the session
		 */
		if (null == session.getAttribute(INTEGRATION_TYPE_FS)) {
			session.setAttribute(INTEGRATION_TYPE_FS,
					this.fulfillmentServiceManager.getIntegrationTypes());
			
			session.setAttribute(FULFILLMENT_METHOD_FS,
					this.fulfillmentServiceManager.getFulfillmentMethods());
			session.setAttribute(STATES,
					this.fulfillmentServiceManager.getStates());
		}

		/* Need to perform check for mode */
		String mode=request.getParameter("mode");
		
		/*Set mode in session to be used for further pages(since change of mode depends on the user role logged in)*/
		if (!isFormSubmission(request)) {
			session.setAttribute("mode", mode);
			log.info("mode in controller"+mode);
			if (mode.equals(EDIT) || (mode.equals("viewOnly"))) {

				/* Check if fulfillment service object exists in the session */
				if (session.getAttribute(FULFILLMENT_SERVICE_IN_SESSION) != null) {
					log.debug("Fulfillment Service object is present in session");
					fulfillmentServiceForm = new FulfillmentServiceForm();
					
					fulfillmentServiceForm
							.setFulfillmentService((FulfillmentService) session
									.getAttribute(FULFILLMENT_SERVICE_IN_SESSION));
					if (fulfillmentServiceForm.getFulfillmentService()
							.getDefaultReturnAddID() != null) {
						log.debug("If address id is not null");
						long addrID = fulfillmentServiceForm
								.getFulfillmentService()
								.getDefaultReturnAddID();
						Address address = fulfillmentServiceManager
								.getAddrByID(addrID);
						fulfillmentServiceForm.setAddress(address);
					}

				}
				/*if (session.getAttribute(FULFILLMENT_SERVICE_IN_SESSION) != null){
					session.removeAttribute(FULFILLMENT_SERVICE_IN_SESSION);
				}else{
					log.debug("not in session");
				}*/
				else {

					if (request.getParameter(FULFILLMENT_SERVICE_ID) != null
							|| (request.getParameter(FULFILLMENT_SERVICE_NAME) != null)) {
						String fServiceId = request
								.getParameter(FULFILLMENT_SERVICE_ID);
						String name = request
								.getParameter(FULFILLMENT_SERVICE_NAME);

						/* If the user has navigated through service name link */
						if (name != null) {
							/*
							 * Returns fulfillment service object with desired
							 * service name and corresponding details
							 */
							fulfillmentService = fulfillmentServiceManager
									.getFulfillmentServiceId(name);

						}
						else {
							/*
							 * Returns fulfillment service object with desired
							 * service ID and corresponding details
							 */
							fulfillmentService = fulfillmentServiceManager
									.getFulfillmentServiceDetails(Long
											.valueOf(fServiceId)); 
						}
					}
					else {
						if (log.isDebugEnabled()){
							log.debug("Oops!!nothing to display in the session!!!");
						}
						return new ModelAndView("redirect:/mainMenu.html");
						
					}
					fulfillmentServiceForm = new FulfillmentServiceForm();

					/* Set fulfillment service object object to form */
					if(fulfillmentService !=null){
					fulfillmentServiceForm
							.setFulfillmentService(fulfillmentService);
					}
					/* Set address object object to form */
					if(fulfillmentServiceForm.getFulfillmentService() !=null){
					if (fulfillmentServiceForm.getFulfillmentService()
							.getDefaultReturnAddID() != null) {
						long addrID = fulfillmentServiceForm
								.getFulfillmentService()
								.getDefaultReturnAddID();
						try{
						Address address = fulfillmentServiceManager
								.getAddrByID(addrID);
						fulfillmentServiceForm.setAddress(address);
						}catch(DataIntegrityViolationException di){
							log.debug("Data integrity violation exception"+di);
						}
						catch(DataAccessException da){
						log.debug("Access Exception"+da);
						}
					}
					session.setAttribute(FULFILLMENT_SERVICE_IN_SESSION,fulfillmentService);
					}
				}
				return fulfillmentServiceForm;

			}
			else if (mode.equals(MODE_ADD)) {
				if (session.getAttribute(FULFILLMENT_SERVICE_IN_SESSION) != null){
				session.removeAttribute(FULFILLMENT_SERVICE_IN_SESSION);
			}
				/*Check if form is in session--This is done for check of unique constraint on fulfillment service name*/	
				if(session.getAttribute("serviceForm")!=null){
					log.debug("Service form exists in session");
					fulfillmentServiceForm = (FulfillmentServiceForm)session.getAttribute("serviceForm");
					session.removeAttribute("serviceForm");
					return fulfillmentServiceForm;
					
					}
				return super.formBackingObject(request);

			}

		}else{
			if(mode != null){
				session.setAttribute("mode",mode);
			}
		
		}

		return super.formBackingObject(request);

	}

}
