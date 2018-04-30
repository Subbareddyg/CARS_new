
package com.belk.car.app.service;

import java.util.List;

import org.appfuse.service.UniversalManager;

import com.belk.car.app.model.oma.FulfillmentServiceShippingOptions;
import com.belk.car.app.model.oma.ShippingCarrierOption;
import com.belk.car.app.model.oma.VendorFulfillmentShippingOption;

public interface VendorFSShippingOptionManager extends UniversalManager {

	FulfillmentServiceShippingOptions getFulfillmentServiceShippingOptions(
			long fulfillmentServiceID);

	VendorFulfillmentShippingOption getVendorFSShippingOptions(
			long vendorId, long fulfillmentServID);

	List<VendorFulfillmentShippingOption> saveVendorFSShippingOptions(
			VendorFulfillmentShippingOption vendorFSShippingOptionsModelList,
			List<ShippingCarrierOption> shippingOptionsModelList);

	List<ShippingCarrierOption> getShippingOptionsModelList(
			long vendorId, long fulfillmentServID);

	List<FulfillmentServiceShippingOptions> saveFulfillmentShippingOptions(
			FulfillmentServiceShippingOptions fulfillmentShippingOptionsModel,
			List<ShippingCarrierOption> ShippingOptionsModel);

	List<ShippingCarrierOption> getShippingOptionsModelListForFulfillment(
			long fulfillmentServID);
}
