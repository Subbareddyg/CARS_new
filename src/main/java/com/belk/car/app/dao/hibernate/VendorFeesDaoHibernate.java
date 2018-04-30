/**
 * Class Name : VendorFeesDaoHibernate.java 
 * Version Information : v1.0 
 * Date : 12/01/2009 
 * Copyright Notice : All rights are reserved to Syntel.
 */
package com.belk.car.app.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.appfuse.model.User;

import com.belk.car.app.dao.VendorFeesDao;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.oma.Fee;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.VendorFee;
import com.belk.car.app.model.oma.VendorFulfillmentServiceFee;
import com.belk.car.app.model.oma.VendorFeeRequest;
/**
 * 
 * @author afusy13
 *
 */
public class VendorFeesDaoHibernate  extends UniversalDaoHibernate implements VendorFeesDao{
	/**
	 * Gets all Fee list.
	 * @return	feeList List<Fee> List of all available Fees.
	 */
	public List<Fee> getFeeList(){
		if (log.isDebugEnabled()) {
			log.debug("Inside getFeeList() Method.");
		}
		List<Fee> feeList = null;
		feeList = getHibernateTemplate().find("From Fee");
		return feeList;
	}

	/**
	 * Gets the VendorFee Request List according to Fulfillment Service ID and Vendor ID
	 * @param fulfillmentServiceId	long Fulfillment Service Id 
	 * @param vendorId	long	Vendor Id to get the required Vendor Fees.
	 * @return venderFeeReqList	List<VendorFulfillmentServiceFee>	List of all Vendor fulfillment Service Fees.
	 */
	public List<VendorFulfillmentServiceFee> getVendorFeeRequestIntList(long fulfillmentServiceId,long vendorId) {
		if (log.isDebugEnabled()) {
			log.debug("Inside getVendorFeeRequestIntList() Method..");
		}
		 List<VendorFulfillmentServiceFee> venderFeeReqList = new ArrayList<VendorFulfillmentServiceFee>();
		 Object arr[] = {fulfillmentServiceId, vendorId};
		 venderFeeReqList = (List<VendorFulfillmentServiceFee>)getHibernateTemplate().find("FROM VendorFulfillmentServiceFee where FULFMNT_SERVICE_ID =? AND VENDOR_ID =? ORDER BY VNDR_FEE_REQUEST_ID DESC", arr);
		 /*for(VendorFulfillmentServiceFee vendorFeeReqModel : venderFeeReqList){
			venderFeeRequestList.add(vendorFeeReqModel.getCompositeKeyVendorFees().getVendorFeeRequest());
		}
		 
		*/
		return venderFeeReqList;
	}
	/** 
	 * Returns the currently logged in user
	 * @return user User User bean, currently logged in.
	 */
	public User getLoggedInUser() {
		if (log.isDebugEnabled()) {
			log.debug("Inside getLoggedIntUser() Method..");
		}
    	User user = null;
    	 Authentication auth = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
         if (auth.getPrincipal() instanceof UserDetails) {
             user = (User)  auth.getPrincipal();
         }
         return user;
    }

	
	/**
	 * Gets the Fulfillment Service by ID. 
	 * @param	fulfillmentServId	Long	Fulfillment Service Id
	 * @return	fulfillmentService	@Link(FulfillmentService)	fulfillment Service model	
	 */
	public FulfillmentService getFulfillmentService(Long fulfillmentServId) {
		if (log.isDebugEnabled()) {
			log.debug("Inside getFulfillmentService() Method..");
		}
		FulfillmentService fulfillmentService =(FulfillmentService) getHibernateTemplate().get(FulfillmentService.class, fulfillmentServId.longValue());
		return fulfillmentService;
	}
	
	/**
	 * Gets the vendor using vendorId
	 * @param vendorId	Long	Vendor Id
	 * @return Vendor	Vendor Object for the provided vendor Id	
	 */
	public Vendor getVendor(Long vendorId) {
		if (log.isDebugEnabled()) {
			log.debug("Inside getVendor() Method..");
		}
		Vendor vendor =  (Vendor) getHibernateTemplate().get(Vendor.class, vendorId);
		return vendor;
	}
	/**
	 * Saves the Vendor Fee Request Model.
	 */
	public VendorFeeRequest saveOrUpdateVendorFeeRequestModel(
			VendorFeeRequest vendorFeeRequestModel) {
		if (log.isDebugEnabled()) {
			log.debug("Inside saveOrUpdateVendorFeeRequestModel() Method..");
		}
		getHibernateTemplate().save(vendorFeeRequestModel);
		getHibernateTemplate().flush();
		return vendorFeeRequestModel;
	}
	/**
	 * Saves/Updates the Vendor Fee Model List.
	 */
	public List<VendorFee> saveVendorFeeModelList(
			List<VendorFee> vendorFeeModelList) {
		if (log.isDebugEnabled()) {
			log.debug("Inside saveVendorFeeModelList() Method..");
		}
		getHibernateTemplate().saveOrUpdateAll(vendorFeeModelList);
		getHibernateTemplate().flush();
		return vendorFeeModelList;
	}
	/**
	 * Saves the Vendor Fee Req Model (Intermediate table) List.
	 * @param vendorFeeReqIntModelList	List<{@link VendorFulfillmentServiceFee}>	List to be inserted/updated.
	 * @return vendorFeeReqIntModelList	List<{@link VendorFulfillmentServiceFee}>	Inserted/Updated list.
	 */
	public List<VendorFulfillmentServiceFee> saveVendorFeeReqModelList(
			List<VendorFulfillmentServiceFee> vendorFeeReqIntModelList) {
		if (log.isDebugEnabled()) {
			log.debug("Inside saveVendorFeeReqModelList() Method..");
		}
		getHibernateTemplate().saveOrUpdateAll(vendorFeeReqIntModelList);
		getHibernateTemplate().flush();
		return vendorFeeReqIntModelList; 
	}
	/**
	 * Gets the Fee Model using the Id passed
	 * @param feeId String Id of the Fee to be retrieved.
	 * @return fee	Fee	Fee model object retrieved from database.
	 */
	public Fee getFeeModel(String feeId) {
		if (log.isDebugEnabled()) {
			log.debug("Inside getFeeModel() Method..");
		}
		Fee fee =  (Fee) getHibernateTemplate().get(Fee.class, feeId);
		return fee;
	}

}
