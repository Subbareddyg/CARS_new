/**
 * Class Name : VendorFeesManagerImpl.java 
 * Version Information : v1.0 
 * Date : 12/01/2009 
 * Copyright Notice : All rights are reserved to Syntel.
 */
package com.belk.car.app.service.impl;

import java.util.List;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.appfuse.model.User;
import org.appfuse.service.impl.UniversalManagerImpl;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.dao.VendorFeesDao;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.oma.Fee;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.VendorFee;
import com.belk.car.app.model.oma.VendorFulfillmentServiceFee;
import com.belk.car.app.model.oma.VendorFeeRequest;
/**
 * Implements the interface VendorFeesManager
 * @author afusy13
 *
 */
public class VendorFeesManagerImpl extends UniversalManagerImpl implements com.belk.car.app.service.VendorFeesManager, DropShipConstants{

	/**
	 *   
	 */
	private static final long serialVersionUID = 1L;
	private VendorFeesDao vendorFeesDao; 


	public List<Fee> getFeeList() {
		return vendorFeesDao.getFeeList();
	}

	/**
	 * @param vendorFeesDao the vendorFeesDao to set
	 */
	public void setVendorFeesDao(VendorFeesDao vendorFeesDao) {
		this.vendorFeesDao = vendorFeesDao;
	}
 
	/**
	 * @return the vendorFeesDao
	 */
	public VendorFeesDao getVendorFeesDao() {
		return vendorFeesDao;
	}

	public List <VendorFulfillmentServiceFee> getVendorFeeRequestIntList(long fulfillmentServiceId,long vendorId){
		
		return vendorFeesDao.getVendorFeeRequestIntList(fulfillmentServiceId,vendorId);
	} 


	public FulfillmentService getFulfillmentService(Long fulfillmentServId) {
		
		return vendorFeesDao.getFulfillmentService(fulfillmentServId);
	}

	public Vendor getVendor(Long vendorId) {
		return vendorFeesDao.getVendor(vendorId);
	}

	public VendorFeeRequest saveOrUpdateVendorFeeRequestModel(
			VendorFeeRequest vendorFeeRequestModel) {
		log.debug("Set the time in object."+ vendorFeeRequestModel.getEffectiveDate());
		//Set the id as 0, to avoid any garbage value.
		vendorFeeRequestModel.setVendorFeeRequestId(0);
		//Setting the hardcpded value for Row Ready Update as char Y.
		vendorFeeRequestModel.setRowReadyUpdate('Y');
		//Setting the User information (Created/Updated by, Created/Updated Date)
		vendorFeeRequestModel.setAuditInfo(getLoggedInUser());
		
		return vendorFeesDao.saveOrUpdateVendorFeeRequestModel(vendorFeeRequestModel);
	}
	private  User getLoggedInUser() {
		User user = null;
		Authentication auth = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (auth.getPrincipal() instanceof UserDetails) {
			user = (User) auth.getPrincipal();
		}
		return user;
	}

	public List<VendorFee> saveVendorFeeModelList(
			List<VendorFee> vendorFeeModelList) {
		return vendorFeesDao.saveVendorFeeModelList(vendorFeeModelList);
	}

	public List<VendorFulfillmentServiceFee> saveVendorFeeReqModelList(
			List<VendorFulfillmentServiceFee> vendorFeeReqIntModelList) {
		return vendorFeesDao.saveVendorFeeReqModelList(vendorFeeReqIntModelList);
	}

	public Fee getFeeModel(String feeId) {
		return vendorFeesDao.getFeeModel(feeId);
	}


	/**
	 * Populate the VendorFeeRequestList from the intermediate table(VendorFeeReqModel) list.
	 * @param vendorFeeReqIntModelList
	 * @param vendorFeeRequestList
	 * @return
	 */
	public List<VendorFeeRequest> populateVendorFeeRequestList(List<VendorFulfillmentServiceFee> vendorFeeReqIntModelList, List<VendorFeeRequest> vendorFeeRequestList) {
		if(log.isDebugEnabled()){
			log.debug("Inside populateVendorFeeRequestList() method of VendorFeesManageImpl.");
		}
		VendorFeeRequest vendorFeeRequestModel = null;
		long vendorFeeRequestId = 0;
		
		//Retrieve the VendorFeeRequest List
		if(null != vendorFeeReqIntModelList && !vendorFeeReqIntModelList.isEmpty()){
			
			for(VendorFulfillmentServiceFee vendorFeeRequestIntModel : vendorFeeReqIntModelList){
				vendorFeeRequestModel = (VendorFeeRequest)vendorFeeRequestIntModel.getCompositeKeyVendorFees().getVendorFeeRequest();
				//Logic to avoid the duplicate values in venderFeeRequestList
				if(vendorFeeRequestId != vendorFeeRequestModel.getVendorFeeRequestId()){
					vendorFeeRequestId = vendorFeeRequestModel.getVendorFeeRequestId();
					vendorFeeRequestList.add(vendorFeeRequestModel);
				}
			}
			
		}
		return vendorFeeRequestList; 
		
	}

}
