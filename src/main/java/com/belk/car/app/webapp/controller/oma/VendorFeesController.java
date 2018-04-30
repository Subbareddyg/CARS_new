/**
 * Class Name : VendorFeesController.java
 * 
 * Version Information : v1.0
 * 
 * Date : 12/21/09
 * 
 * Copyright Notice : All rights are reserved to Syntel.
 */
package com.belk.car.app.webapp.controller.oma;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
import com.belk.car.app.model.oma.Fee;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.VendorFee;
import com.belk.car.app.model.oma.VendorFulfillmentServiceFee;
import com.belk.car.app.model.oma.VendorFeeRequest;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.service.VendorFeesManager;
import com.belk.car.app.webapp.forms.VendorFeesForm;


/**
 * A Multi Action Controller to control the different actions from page vendorShippingFee.jsp
 * @author afusy13
 *
 */
public class VendorFeesController extends MultiActionController implements DropShipConstants{
	private VendorFeesManager vendorFeesManager;
	private FulfillmentService fulfillmentService = null;
	private FulfillmentServiceVendor vendorFulfillmentService = null;
	private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
	private transient final Log log = LogFactory.getLog(VendorFeesController.class);
	/**
	 * Sets the instance ContactsManager in the Context at the loading of the application
	 */
	public void setVendorFeesManager(VendorFeesManager vendorFeesManager) {
		this.vendorFeesManager = vendorFeesManager;
	}
	 
	
	/**
	 * Loads the Vendor Fees details for that Vendor and Fulfillment Service
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView load(HttpServletRequest request,HttpServletResponse response) throws Exception{
		if(log.isDebugEnabled()){
			log.debug("Inside load() Method of VendorFeesController..");
		}
		Map<String, Object> model = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		List <VendorFeeRequest> vendorFeeRequestList = new ArrayList<VendorFeeRequest>();
		List<VendorFee> vendorFeeModelList = new ArrayList<VendorFee>();
		//Get the VendorFeeRequestModel from database based on the current vendor.
		getDataFromSession(request, response);
		//Check whether the user session is timed out 
		if(fulfillmentService == null){
			log.debug("User session has expired. Redirecting the user to Dashboard.");
			return new ModelAndView("redirect:/mainMenu.html");
		}
		//Check for user role for edit option.
		canEdit(request);
		VendorFeesForm vendorFeesForm = new VendorFeesForm();
		//Clearing the list to make sure no previous data.
		vendorFeeRequestList.clear();
		vendorFeeModelList.clear();
		
		List<VendorFulfillmentServiceFee>	vendorFeeReqIntModelList = null;
		vendorFeeReqIntModelList =  vendorFeesManager.getVendorFeeRequestIntList(fulfillmentService.getFulfillmentServiceID(),vendorFulfillmentService.getVendorID());
		
		//Get the Vendor Fee Request List from intermediate table list.
		vendorFeeRequestList = vendorFeesManager.populateVendorFeeRequestList(vendorFeeReqIntModelList,vendorFeeRequestList);
		
		//Keep Intermediate table list in session
		if(vendorFeeReqIntModelList != null){
			if(session.getAttribute("vendorFeeReqIntModelList") != null){
				session.removeAttribute("vendorFeeReqIntModelList");
			}
			session.setAttribute("vendorFeeReqIntModelList", vendorFeeReqIntModelList);
		}else{
			session.setAttribute("vendorFeeReqIntModelList", null);
		}
		
		//Get the current Vendor_Fee_Request
		//ListIterator<VendorFeeRequestModel> iteratorVendorFeeRequest = vendorFeeRequestList.listIterator();
		long currentFeeRequestId = populateCurrentFeeRequest(vendorFeesForm,session,vendorFeeRequestList);
		if(log.isDebugEnabled()){
			log.debug("Current Fee Request ID:"+ currentFeeRequestId);
		}
		//Keep the Current Vendor Fee Request Id in session to access it in next page 
		session.setAttribute("currentVendorFeeRequestId", currentFeeRequestId);
		//Logic for retaining the pagination
		String page=request.getParameter("d-4314083-p");
		if(StringUtils.isBlank(page)){
			log.debug("Setting Fees pagination parameter to 1.");
			model.put("pagination", 1);
		}else {
			log.debug("Setting Fess pagination parameter to :"+ page);
			model.put("pagination", page);
		}
		
		//To get the current Vendor_fee object
		vendorFeeModelList = getCurrentVendorFeeModelList(vendorFeeReqIntModelList,currentFeeRequestId,vendorFeeModelList);
		
		//Setting the current fee objects list in form
		vendorFeesForm.setVendorFees(vendorFeeModelList);
		//vendorFeesForm.setFees(fees);

		vendorFeesForm.setFormEffectiveDate(dateFormat.format((vendorFeesForm.getVendorFeeRequestModel()!=null && vendorFeesForm.getVendorFeeRequestModel().getEffectiveDate()!= null )?vendorFeesForm.getVendorFeeRequestModel().getEffectiveDate(): new Date()));
		//Setting the values to be passes to JSP in model. 
		model.put("venderFeeRequestList", vendorFeeRequestList);
		model.put("vendorFeeModel", vendorFeeModelList);
		model.put("vendorFeesForm", vendorFeesForm);
		model.put("isEditMode", false);
		return new ModelAndView("oma/vendorShippingFee",model);
		
	}
	/**
	 * Method to check whether the  logged in user can edit the Vendor Fee or not.
	 * @param request HttpServletRequest 
	 */
	private void canEdit(HttpServletRequest request) {
		User currentUser = getLoggedInUser();
		if(log.isDebugEnabled()){
			log.debug("currentUser in properties controller.."+currentUser);
		}
		/*Check if the current user has order management admin role or order management user role*/
		if(currentUser.isOrderMgmtAdmin()){
			log.debug("User has role order management user..set mode to edit");
			request.setAttribute("mode",EDIT);
		//Commented the following code to enable the view only mode to all users except Admin.
		//}else if((currentUser.isOrderMgmtUser()) || (currentUser.isBuyer()) || (currentUser.isBuyer())){
		}else{
			log.debug("User has role order management admin..set mode to ViewOnly");
			request.setAttribute("mode", VIEW_ONLY_MODE);
		}
		
	}

	/**
	 * Gets the Current Vendor Fee list for Change Details section on JSP.
	 * @param vendorFeeReqIntModelList
	 * @param currentFeeRequestId
	 * @param vendorFeeModelList
	 * @return vendorFeeModelList 
	 */
	private List<VendorFee> getCurrentVendorFeeModelList(
			List<VendorFulfillmentServiceFee> vendorFeeReqIntModelList,
			long currentFeeRequestId, List<VendorFee> vendorFeeModelList) {
		if(log.isDebugEnabled()){
			log.debug("currentFeeRequestId : " + currentFeeRequestId);
		}
		vendorFeeModelList.clear();
		if(!vendorFeeReqIntModelList.isEmpty()){
			for(VendorFulfillmentServiceFee vendorFeeReqModel : vendorFeeReqIntModelList){
				if(vendorFeeReqModel.getCompositeKeyVendorFees().getVendorFeeRequest().getVendorFeeRequestId() == currentFeeRequestId ){
					vendorFeeModelList.add(vendorFeeReqModel.getCompositeKeyVendorFees().getVendorFee());
					if(log.isDebugEnabled()){
						log.debug("Setting the VendorFee :" + vendorFeeReqModel.getCompositeKeyVendorFees().getVendorFee().getFee().getFeeDesc());
						log.debug("Setting the VendorFee :" + vendorFeeReqModel.getCompositeKeyVendorFees().getVendorFee().getPerItemAmount());
						log.debug("Setting the VendorFee :" + vendorFeeReqModel.getCompositeKeyVendorFees().getVendorFee().getPerOrderAmount());
					}
				}
			}
		}else{
			//Means this is a new Fee Request
			//Get all the 5 Fees from database
			List<Fee> feeList =  vendorFeesManager.getFeeList();
			//Loop the list and create the objects of Vendor Fee setting the fee.
			for(Fee fee : feeList){
				vendorFeeModelList.add(new VendorFee(fee)); 
			}
		}
		return vendorFeeModelList;
		
	}

	/**
	 * Populates the Current Request Id according to the effective date 
	 * and stores the Current Fee Request in Form.
	 * @param vendorFeesForm	Form in which the values to be set
	 * @param session Session object to store Fees the last modified by and last modified date.  
	 * @param vendorFeeRequestList2 
	 * @return	
	 */
	private long populateCurrentFeeRequest(
			VendorFeesForm vendorFeesForm, HttpSession session, List<VendorFeeRequest> vendorFeeRequestList) {
		Date currentDate = new Date(); //Current Date
		Date tempDate;
		Date lastUpdatedFeeDate =null;
		String lastFeeModifiedBy = null;
		
		long currentFeeRequestId = 0;
		if(!vendorFeeRequestList.isEmpty() && vendorFeeRequestList.size() > 1){
			
			//Get the first record values for the sake of comparison
			Date createdDate =vendorFeeRequestList.get(0).getCreatedDate() ; //Created Date
			tempDate = vendorFeeRequestList.get(0).getEffectiveDate(); //Effective Date
			currentFeeRequestId =vendorFeeRequestList.get(0).getVendorFeeRequestId(); //Current ID
			if(log.isDebugEnabled()){
				log.debug("Before starting the loop, temp date:"+tempDate);
				log.debug("Iterator Size: "+ vendorFeeRequestList.size());
			}
			//Loop  through the VendorFeeRequestModelList to get the current Fee Request
			for(VendorFeeRequest vendorFeesRequestModel : vendorFeeRequestList){
				if(log.isDebugEnabled()){
					log.debug("Inside for loop");
					log.debug("Inside current date check: Condition value: "+ currentDate.compareTo(vendorFeesRequestModel.getEffectiveDate()) );
				}
				if(currentDate.compareTo(vendorFeesRequestModel.getEffectiveDate()) >= 0){
					if(log.isDebugEnabled()){
						log.debug("Inside Inner current date check: Condition Value: "+ tempDate.compareTo(vendorFeesRequestModel.getEffectiveDate()));
					}
					if(vendorFeesRequestModel.getEffectiveDate().compareTo(tempDate) > 0){
						tempDate =  vendorFeesRequestModel.getEffectiveDate();
						createdDate = vendorFeesRequestModel.getCreatedDate();
						currentFeeRequestId =  vendorFeesRequestModel.getVendorFeeRequestId();
						
						if(log.isDebugEnabled()){
							log.debug("Current Request ID:"+currentFeeRequestId );
						}
						//Setting the current fee request object in form
						vendorFeesForm.setVendorFeeRequestModel(vendorFeesRequestModel);
						if(log.isDebugEnabled()){
							log.debug("Setting the VendorFeeRequest :"+ vendorFeesRequestModel.getEffectiveDate());
							log.debug("Setting the VendorFeeRequest :"+ vendorFeesRequestModel.getFeeRequestDesciption());
						}
					} else if(tempDate.compareTo(vendorFeesRequestModel.getEffectiveDate()) == 0){  //If effective Dates are equal then check the created date
						log.debug("Effective Dates are equal..");
						if(vendorFeesRequestModel.getCreatedDate().compareTo(createdDate) >= 0){
							log.debug("Comparing the created Dates(in condition).. temp createdDate: "+ createdDate +" and the current createdDate: "+vendorFeesRequestModel.getCreatedDate());
							createdDate = vendorFeesRequestModel.getCreatedDate();
							currentFeeRequestId =  vendorFeesRequestModel.getVendorFeeRequestId();
							if(log.isDebugEnabled()){
								log.debug("Current Request ID:"+currentFeeRequestId );
							}
							//Setting the current fee request object in form
							vendorFeesForm.setVendorFeeRequestModel(vendorFeesRequestModel);
						}
					}else{
						log.debug("Inside the Else Block");
						if(tempDate.compareTo(currentDate) > 0 && currentDate.compareTo(vendorFeesRequestModel.getEffectiveDate()) >= 0){
							tempDate = vendorFeesRequestModel.getEffectiveDate();
							createdDate = vendorFeesRequestModel.getCreatedDate();
							currentFeeRequestId = vendorFeesRequestModel.getVendorFeeRequestId();
							//Setting the current fee request object in form
							vendorFeesForm.setVendorFeeRequestModel(vendorFeesRequestModel);
						}
					}
				}
				//Get the variables last updated date and last updated by
				if(lastUpdatedFeeDate == null && lastFeeModifiedBy == null){
					lastUpdatedFeeDate = vendorFeesRequestModel.getCreatedDate();
					lastFeeModifiedBy = vendorFeesRequestModel.getUpdatedBy();
				}else{
					//check which one is last
					if(lastUpdatedFeeDate.compareTo(vendorFeesRequestModel.getUpdatedDate()) <= 0){
						lastUpdatedFeeDate = vendorFeesRequestModel.getUpdatedDate();
						lastFeeModifiedBy = vendorFeesRequestModel.getUpdatedBy();
					}
				}
			} 
			}else if(!vendorFeeRequestList.isEmpty() && vendorFeeRequestList.size() == 1){	
				log.debug("Only one Vendor Fee Request is found.");
				currentFeeRequestId =vendorFeeRequestList.get(0).getVendorFeeRequestId(); //Current ID
				vendorFeesForm.setVendorFeeRequestModel(vendorFeeRequestList.get(0));
			}
			else {
			//Means this is a new Fee Request 
			if(log.isDebugEnabled()){
				log.debug("This is a new Fee request..");
			}
			vendorFeesForm.setVendorFeeRequestModel(new VendorFeeRequest()); 
		}
		session.setAttribute(FEES_LAST_UPDATED,(lastUpdatedFeeDate !=null)?dateFormat.format(lastUpdatedFeeDate):null);
		session.setAttribute(FEES_LAST_MODIFIED_BY, lastFeeModifiedBy);
		
		return currentFeeRequestId; 
	}



	/**
	 * Gets the Vendor associated Fees when ID is clicked from Change History.
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView show(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		//Get the VendorFeeRequestModel from database based on the current vendor.
		if(log.isDebugEnabled()){
			log.debug("Inside Show() Method of VendorFeesController..");
		}
		VendorFeesForm vendorFeesForm = new VendorFeesForm();
		List <VendorFeeRequest> vendorFeeRequestList = new ArrayList<VendorFeeRequest>();
		List<VendorFulfillmentServiceFee> vendorFeeReqIntModelList = null;
		VendorFeeRequest vendorFeeRequestModel =null;
		Map<String, Object> model = new HashMap<String, Object>();
		List <VendorFee> vendorFeeModelList =  new ArrayList<VendorFee>();
		Long id = null;
		if(StringUtils.isNotBlank(request.getParameter("id"))){
			id = Long.parseLong(request.getParameter("id"));
		}
		HttpSession session = request.getSession();
		//Check whether the user session is timed out 
		if(null == session.getAttribute("fulfillmentService")){
			log.debug("User session has expired. Redirecting the user to Dashboard.");
			return new ModelAndView("redirect:/mainMenu.html");
		}
		//Logic for retaining the pagination
		String page=request.getParameter("d-4314083-p");
		if(StringUtils.isBlank(page)){
			log.debug("Setting Fees pagination parameter to 1.");
			model.put("pagination", 1);
		}else {
			model.put("pagination", page);
		}
		
		if(session.getAttribute("vendorFeeReqIntModelList") != null){
			vendorFeeReqIntModelList = (List<VendorFulfillmentServiceFee>) session.getAttribute("vendorFeeReqIntModelList");
		}else{
			vendorFeeReqIntModelList =  vendorFeesManager.getVendorFeeRequestIntList(fulfillmentService.getFulfillmentServiceID(),vendorFulfillmentService.getVendorID());
		}
		
		if(vendorFeeReqIntModelList != null){
			if(log.isDebugEnabled()){
				log.debug(" Session attribute vendorFeeReqIntModelList is not null.");
			}
			vendorFeeModelList.clear();
			//Display the Change Details. 
			for(VendorFulfillmentServiceFee venderFeeReqIntModel : vendorFeeReqIntModelList){
				if(venderFeeReqIntModel.getCompositeKeyVendorFees().getVendorFeeRequest().getVendorFeeRequestId() == id.longValue()){
					log.debug("Inside Show method(), Id are equal:" + venderFeeReqIntModel.getCompositeKeyVendorFees().getVendorFeeRequest().getVendorFeeRequestId());
					vendorFeeModelList.add(venderFeeReqIntModel.getCompositeKeyVendorFees().getVendorFee());
					if(log.isDebugEnabled()){
						log.debug("Vendor Fee ID:"+venderFeeReqIntModel.getCompositeKeyVendorFees().getVendorFee().getVendorFeeId());
						log.debug("Vendor Fee Description:"+venderFeeReqIntModel.getCompositeKeyVendorFees().getVendorFee().getFee().getFeeDesc());
					}
					vendorFeeRequestModel = venderFeeReqIntModel.getCompositeKeyVendorFees().getVendorFeeRequest();
				}
			}
		}
		//Require to set the form values
		vendorFeesForm.setVendorFees(vendorFeeModelList);
		vendorFeesForm.setVendorFeeRequestModel(vendorFeeRequestModel);
		//Set the Effective Date
		vendorFeesForm.setFormEffectiveDate(dateFormat.format(vendorFeesForm.getVendorFeeRequestModel().getEffectiveDate()));
		if(log.isDebugEnabled()){
			log.debug("effective Date:"+ vendorFeesForm.getFormEffectiveDate());
		}
		//Commented the code which was used on JSP, so commenting this one as well.
		//session.setAttribute("vendorFeeListSize", vendorFeeModelList.size());
		Properties handlingFeeName = (Properties) getServletContext().getAttribute("vendorFee.handlingFee");
		Properties restockingFeeName = (Properties) getServletContext().getAttribute("vendorFee.restockingFee");
		vendorFeeRequestList.clear();
		vendorFeeRequestList = vendorFeesManager.populateVendorFeeRequestList(vendorFeeReqIntModelList,vendorFeeRequestList);
		model.put("vendorFeeModel", vendorFeeModelList);
		model.put("venderFeeRequestList", vendorFeeRequestList);
		model.put("vendorFeeRequestId", vendorFeeRequestModel.getVendorFeeRequestId()); //Require to highlight the currently clicked record.
		request.setAttribute("vendorFeeRequestId", vendorFeeRequestModel.getVendorFeeRequestId());
		model.put("vendorFeesForm", vendorFeesForm);
		//Required to display the checkboxes only for Handling Fees and Restocking Fees
		model.put("handlingFee", handlingFeeName);
		model.put("restockingFee", restockingFeeName);
		return new ModelAndView("oma/vendorShippingFee",model);
		
	}
	
	/**
	 * Returns the currently logged in user
	 * @return user User User bean, currently logged in.
	 */
	public User getLoggedInUser() {
    	User user = null;
    	 Authentication auth = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
         if (auth.getPrincipal() instanceof UserDetails) {
             user = (User)  auth.getPrincipal();
         }
         return user;
    }
	
	/**
	 * Checks the session and retrieves the data from session.
	 * @param request	the request object containing the request attributes 
	 * @param response	the response object that needs to be passed to view for the request
	 */
	public void getDataFromSession(HttpServletRequest request, HttpServletResponse response){
		if (log.isDebugEnabled()) {
            log.debug("Inside getDataFromSession() method..");
        }
		HttpSession session = request.getSession();
		if(null != session.getAttribute("fulfillmentService")){
			fulfillmentService = (FulfillmentService) session.getAttribute("fulfillmentService"); 
			if(log.isDebugEnabled()){
				log.debug("Got the fulfillmentService Object");
			}
		}else{
			fulfillmentService = null;
		}
		if(null != session.getAttribute("vndrFulfillmentService") ){
			vendorFulfillmentService = (FulfillmentServiceVendor) session.getAttribute("vndrFulfillmentService"); 
			if(fulfillmentService == null){
				fulfillmentService =  vendorFeesManager.getFulfillmentService(vendorFulfillmentService.getFulfillmentServId());
				if(log.isDebugEnabled()){
					log.debug("Got the fulfillmentService Object from database.");
				}
			}
		}else{
			vendorFulfillmentService = null;
		}
		
	}
}
