
package com.belk.car.app.service;

import java.io.Serializable;
import java.util.List;

import org.appfuse.model.User;
import org.appfuse.service.UniversalManager;

import com.belk.car.app.model.oma.Address;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.model.oma.FulfillmentServiceVendorReturn;
import com.belk.car.app.model.oma.InvoiceMethods;
import com.belk.car.app.model.oma.ReturnMethod;
import com.belk.car.app.model.oma.State;

/**
 * @author afusy07-priyanka_gadia@syntelinc.com
 * @Date 12-dec-09
 */
public interface VendorFulfillmentServiceManager extends UniversalManager {

	public FulfillmentServiceVendor addVendorFulfillmentService(
			FulfillmentServiceVendor vendorFulfillmentServiceModel);

	public List<FulfillmentServiceVendor> getVendorFulfillmentServices(
			String name, Long id, String status, long fulfillmentServiceId);

	public List<FulfillmentServiceVendor> getAllVendorFulfillmentServices(
			long fulfillmentServiceId);

	public FulfillmentServiceVendor getVendorFulfillmentServicesByID(Long id);

	public List<Address> saveAddress(
			List<Address> addrList, Long long1, Long long2, List<Long> addrIdsFromSession);

	public FulfillmentServiceVendorReturn getVendorReturns(Long venId, Long fsId);

	public FulfillmentServiceVendorReturn saveVendorReturn(FulfillmentServiceVendorReturn vndrRtrnModel);

	public List<ReturnMethod> getReturnMethods();

	public List<Address> getAddressForVen(Long vndrId, Long fulfillmentServId);

	public List<State> getStates();

	public FulfillmentServiceVendorReturn getFulfillmentServReturns(long fulfillmentServiceID);

	@SuppressWarnings("unchecked")
	public Object getById(Class cls, Serializable id);

	public List<InvoiceMethods> getInvoiceMethods();

	public List<FulfillmentServiceVendor> getActiveStyleSkusForVendors(
			List<FulfillmentServiceVendor> vendorList, Long serviceId);
	
	public List<State> getStatesForVendorTax();
	
	// added methods for dropship phase 2
	public void lockFulfillmntServiceVendor(String contactId,User user);
    public void unlockFulfillmntServiceVendor(String userName);
}
