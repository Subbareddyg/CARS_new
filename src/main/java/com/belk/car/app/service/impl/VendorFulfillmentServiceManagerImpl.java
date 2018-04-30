package com.belk.car.app.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.appfuse.model.User;
import org.appfuse.service.impl.UniversalManagerImpl;

import com.belk.car.app.dao.VendorFulfillmentServiceDao;
import com.belk.car.app.dto.DropshipVendorActiveStyleSkuDTO;
import com.belk.car.app.model.oma.Address;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.model.oma.FulfillmentServiceVendorReturn;
import com.belk.car.app.model.oma.InvoiceMethods;
import com.belk.car.app.model.oma.ReturnMethod;
import com.belk.car.app.model.oma.State;
import com.belk.car.app.service.VendorFulfillmentServiceManager;

/**
 * @author afusy07-priyanka_gadia@syntelinc.com
 * @Date 12-dec-09
 */
public class VendorFulfillmentServiceManagerImpl extends UniversalManagerImpl
		implements
			VendorFulfillmentServiceManager {

	private VendorFulfillmentServiceDao vendorFulfillmentServiceDao;
	

	public VendorFulfillmentServiceDao getVendorFulfillmentServiceDao() {
		return vendorFulfillmentServiceDao;
	}

	public void setVendorFulfillmentServiceDao(
			VendorFulfillmentServiceDao vendorFulfillmentServiceDao) {
		this.vendorFulfillmentServiceDao = vendorFulfillmentServiceDao;
	}

	/**
	 * @param vendorFulfillmentServiceModel
	 * @return FulfillmentServiceVendor
	 * @TODO Save or update vendor fulfillment service model
	 */
	public FulfillmentServiceVendor addVendorFulfillmentService(
			FulfillmentServiceVendor vendorFulfillmentServiceModel) {

		return vendorFulfillmentServiceDao
				.addVendorFulfillmentService(vendorFulfillmentServiceModel);

	}
	
	/**
	 * @param addressList - List of addresses for vendor
	 * @param vendorId - Vendor ID
	 * @param fulfillmentId -Fulfillment Service ID
	 * @param addrIdsFromSession - Address IDs to be deleted
	 * @return List of saved addresses
	 * @TODO Save addresses for Vendor under a fulfillment service
	 */
	public List<Address> saveAddress(
			List<Address> addressList, Long vendorID, Long fulfillmentServiceID, List<Long> addrIdsFromSession) {

		return vendorFulfillmentServiceDao.saveAddress(addressList, vendorID, fulfillmentServiceID, addrIdsFromSession);
	}

	

	/**
	 * @param fulfillmentServiceId - Fulfillment Service ID
	 * @return - Fulfillment Service Vendor List
	 * @TODO Get all records for Vendor under a fulfillment service
	 */
	public List<FulfillmentServiceVendor> getAllVendorFulfillmentServices(
			long fulfillmentServiceId) {
		
		return vendorFulfillmentServiceDao.getAllVendorFulfillmentServiceList(fulfillmentServiceId);
	}

	/**
	 * @param name - Vendor Name
	 * @param id - Vendor Number
	 * @param status - Status of vendor fulfillment service
	 * @param fulfillmentServiceId - Fulfillment Service ID
	 * @return - List of Fulfillment Service Vendors based on search criteria
	 * @TODO Get records for Vendor by search criteria
	 */
	public List<FulfillmentServiceVendor> getVendorFulfillmentServices(
			String name, Long id, String status, long fulfillmentServiceId) {
		return vendorFulfillmentServiceDao.getVendorFulfillmentServiceList(name, id, status,
				fulfillmentServiceId);
	}

	/**
	 * @param id
	 * @return Vendor Based on the ID
	 * @TODO Get a record for Vendor by passing the vendor id
	 */
	public FulfillmentServiceVendor getVendorFulfillmentServicesByID(Long id) {
		return vendorFulfillmentServiceDao.getVendorFulfillmentServicesByID(id);
	}

	/**
	 * @param vendorID
	 * @param fulfillmentServiceID
	 * @return Fulfillment Service Vendor Return
	 * @TODO Get return for vendor
	 */
	public FulfillmentServiceVendorReturn getVendorReturns(Long vendorID, Long fulfillmentServiceID) {

		return vendorFulfillmentServiceDao.getVendorReturns(vendorID, fulfillmentServiceID);
	}

	/**
	 * @param vendorReturn - Fulfillment service vendor return model
	 * @return - Saved Fulfillment service vendor return model
	 * @TODO Save return for vendor
	 */
	public FulfillmentServiceVendorReturn saveVendorReturn(FulfillmentServiceVendorReturn vendorReturn) {
		return (FulfillmentServiceVendorReturn) vendorFulfillmentServiceDao.saveVendorReturn(vendorReturn);

	}

	/**
	 * @return List of return Methods.
	 * @TODO Get the return methods for return methods dropdown on returns page
	 */
	public List<ReturnMethod> getReturnMethods() {
		return vendorFulfillmentServiceDao.getReturnMethods();

	}

	/**
	 * @param vendorId -Vendor ID
	 * @param fulfillmentServiceId -Fulfillment Service ID
	 * @return - List of address
	 * @TODO Get the address list for given vendor and fulfillment service
	 */
	public List<Address> getAddressForVen(Long vendorID, Long fulfillmentServiceId) {

		return vendorFulfillmentServiceDao.getAddressForVen(vendorID, fulfillmentServiceId);
	}

	/**
	 * @return List of states
	 * @TODO Get the states for states dropdown on returns page
	 */
	public List<State> getStates() {
		
		return vendorFulfillmentServiceDao.getStates();
	}

	/**
	 * @param fulfillmentServiceID
	 * @return Fulfillment Service Return
	 * @TODO Get return for fulfillment service
	 */
	public FulfillmentServiceVendorReturn getFulfillmentServReturns(long fulfillmentServiceID) {

		return vendorFulfillmentServiceDao.getFulfillmentServReturns(fulfillmentServiceID);
	}

	/**
	 * @param cls -Class
	 * @param id - ID of the class
	 * @return - Object
	 * @TODO
	 */
	@SuppressWarnings("unchecked")
	public Object getById(Class cls, Serializable id) {

		return vendorFulfillmentServiceDao.getById(cls, id);
	}

	/**
	 * @return List of invoice methods
	 * @TODO get List of invoice methods
	 */
	public List<InvoiceMethods> getInvoiceMethods() {
		
		return vendorFulfillmentServiceDao.getInvoiceMethods();
	}

	/**
	 * @return List of DropshipVendorActiveStyleSkuDTO
	 * @param serviceId- Fulfillment Service IDs
	 * @TODO Get active styles and skus for vendors.
	 */
	public List<FulfillmentServiceVendor> getActiveStyleSkusForVendors(
			List<FulfillmentServiceVendor> vendorList, Long serviceId) {

	
		List<FulfillmentServiceVendor> vendorListNew = new ArrayList<FulfillmentServiceVendor>();
		List<DropshipVendorActiveStyleSkuDTO> vendorListStyleSku = vendorFulfillmentServiceDao
				.getActiveStyleSkusForVendors(serviceId);

		Map<Long, DropshipVendorActiveStyleSkuDTO> mapStyle = new HashMap<Long, DropshipVendorActiveStyleSkuDTO>();

		// Construct a map for vendor fulfillment id and
		// DropshipVendorActiveStyleSkuView
		for (DropshipVendorActiveStyleSkuDTO style : vendorListStyleSku) {

			mapStyle.put(style.getVendorFulfillmentId(), style);

		}
		// If the list of DropshipVendorActiveStyleSkuView containd the vendor
		// fulfillment id
		// Add the number of styles and skus to the model.
		for (FulfillmentServiceVendor vendor : vendorList) {
			if (mapStyle.containsKey(vendor.getVndrFulfillmentServId())) {
				vendor.setNumStyles(mapStyle.get(vendor.getVndrFulfillmentServId())
						.getTotalActiveStyles());
				vendor.setNumSkus(mapStyle.get(vendor.getVndrFulfillmentServId())
						.getTotalActiveSKUs());
				vendorListNew.add(vendor);
			}
			else {
				vendor.setNumStyles(new Long(0));
				vendor.setNumSkus(new Long(0));
				vendorListNew.add(vendor);
			}

		}

		// Return new model list.
		return vendorListNew;
	}

	/**
	 * @return Tax states list which are inactive or not added in the tax list
	 * @TODO
	 */
	public List<State> getStatesForVendorTax() {
		
		 return vendorFulfillmentServiceDao.getStatesForVendorTax();
	}
	
	public void lockFulfillmntServiceVendor(String contactId,User user){
		vendorFulfillmentServiceDao.lockFulfillmntServiceVendor(contactId,user);
	}
    public void unlockFulfillmntServiceVendor(String userName){
    	vendorFulfillmentServiceDao.unlockFulfillmntServiceVendor(userName);
    }

}
