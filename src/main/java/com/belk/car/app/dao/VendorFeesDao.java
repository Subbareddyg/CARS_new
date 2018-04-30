/**
 * Class Name : VendorFeesDao.java 
 * Version Information : v1.0 
 * Date : 12/01/2009 
 * Copyright Notice : All rights are reserved to Syntel.
 */
package com.belk.car.app.dao;

import java.util.List;

import org.appfuse.dao.UniversalDao;

import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.oma.Fee;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.VendorFee;
import com.belk.car.app.model.oma.VendorFulfillmentServiceFee;
import com.belk.car.app.model.oma.VendorFeeRequest;
/**
 * Interface to declare the methods to be implemented by Dao Hibernate.
 * @author afusy13
 *
 */
public interface VendorFeesDao extends UniversalDao {
	
	//public VendorFeeModel getVendorFeeModel(Long venderFeeId);
	
	public List<VendorFulfillmentServiceFee> getVendorFeeRequestIntList(long fulfillmentServiceId,long vendorId);
	
	//public List<VendorFeeModel> getVendorFeeList(Long venderId);

	public FulfillmentService getFulfillmentService(Long fulfillmentServId); 
	
	public Vendor getVendor(Long vendorId);

	public VendorFeeRequest saveOrUpdateVendorFeeRequestModel(
			VendorFeeRequest vendorFeeRequestModel);

	public List<VendorFee> saveVendorFeeModelList(
			List<VendorFee> vendorFeeModelList);

	public List<VendorFulfillmentServiceFee> saveVendorFeeReqModelList(
			List<VendorFulfillmentServiceFee> vendorFeeReqIntModelList);

	public List<Fee> getFeeList();
	
	public Fee getFeeModel(String feeId);
}
