
package com.belk.car.app.dao;

import java.io.Serializable;
import java.util.List;

import org.appfuse.dao.UniversalDao;
import org.appfuse.model.User;

import com.belk.car.app.dto.DropshipVendorActiveStyleSkuDTO;
import com.belk.car.app.model.oma.Address;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.model.oma.FulfillmentServiceVendorReturn;
import com.belk.car.app.model.oma.InvoiceMethods;
import com.belk.car.app.model.oma.ReturnMethod;
import com.belk.car.app.model.oma.State;

/**
 * @author afusy07 Feb 9, 2010TODO
 */
public interface VendorFulfillmentServiceDao extends UniversalDao {

	public FulfillmentServiceVendor addVendorFulfillmentService(
			FulfillmentServiceVendor vendorFulfillmentServiceModel);

	public List<FulfillmentServiceVendor> getVendorFulfillmentServiceList(
			String name, Long id, String status, long fulfillmentServiceId);

	public List<FulfillmentServiceVendor> getAllVendorFulfillmentServiceList(
			long fulfillmentServiceId);

	public FulfillmentServiceVendor getVendorFulfillmentServicesByID(Long id);

	public FulfillmentServiceVendorReturn getVendorReturns(Long venId, Long fsId);

	public FulfillmentServiceVendorReturn saveVendorReturn(FulfillmentServiceVendorReturn vndrRtrnModel);

	public List<ReturnMethod> getReturnMethods();

	public List<Address> saveAddress(
			List<Address> addrList, Long venId, Long fsId, List<Long> addrIdsFromSession);

	public List<Address> getAddressForVen(Long vndrId, Long fulfillmentServId);

	public List<State> getStates();

	public FulfillmentServiceVendorReturn getFulfillmentServReturns(long fulfillmentServiceID);

	@SuppressWarnings("unchecked")
	public Object getById(Class cls, Serializable id);

	public List<InvoiceMethods> getInvoiceMethods();

	public List<DropshipVendorActiveStyleSkuDTO> getActiveStyleSkusForVendors(Long serviceId);

	public List<State> getStatesForVendorTax();
	
	// adde methods for dropship phase 2
	/*
	 * lock the fulfillment service vendor
	 */
	public void lockFulfillmntServiceVendor(String contactId,User user);
	/*
	 * un lock the fulfillment service vendor
	 */
    public void unlockFulfillmntServiceVendor(String userName);
}
