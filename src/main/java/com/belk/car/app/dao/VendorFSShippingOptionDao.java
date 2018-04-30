
package com.belk.car.app.dao;

import java.util.List;

import org.appfuse.dao.UniversalDao;

import com.belk.car.app.model.oma.FulfillmentServiceShippingOptions;
import com.belk.car.app.model.oma.ShippingCarrierOption;
import com.belk.car.app.model.oma.VendorFulfillmentShippingOption;

public interface VendorFSShippingOptionDao extends UniversalDao {

	FulfillmentServiceShippingOptions getFulfillmentServiceShippingOptions(
			long fulfillmentServiceID);

	VendorFulfillmentShippingOption getVendorFSShippingOptions(
			long vendorId, long fulfillmentServID);

	List<VendorFulfillmentShippingOption> saveVendorFSShippingOptions(
			List<VendorFulfillmentShippingOption> vendorFSShippingOptionsList);

	List<ShippingCarrierOption> getShippingOptionsModelList();

	List<VendorFulfillmentShippingOption> getShippingOptionsForVendorFS(
			long vendorId, long fulfillmentServID);

	List<FulfillmentServiceShippingOptions> saveFulfillmentShippingOptions(
			List<FulfillmentServiceShippingOptions> fulfillmentFSShippingOptionsList);

	List<FulfillmentServiceShippingOptions> getShippingOptionsForFulfillment(
			long fulfillmentServID);
}
