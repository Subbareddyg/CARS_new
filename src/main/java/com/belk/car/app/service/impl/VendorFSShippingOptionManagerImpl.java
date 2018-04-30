
package com.belk.car.app.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.service.impl.UniversalManagerImpl;

import com.belk.car.app.dao.VendorFSShippingOptionDao;
import com.belk.car.app.model.oma.CompositeKeyForFSShippingOptions;
import com.belk.car.app.model.oma.CompositeKeyForShippingOptions;
import com.belk.car.app.model.oma.FulfillmentServiceShippingOptions;
import com.belk.car.app.model.oma.ShippingCarrierOption;
import com.belk.car.app.model.oma.VendorFulfillmentShippingOption;
import com.belk.car.app.service.VendorFSShippingOptionManager;

/**
 * @author afusy07 Feb 9, 2010 TODO Manager Implementation class for vendor
 *         fulfillment shipping options.
 */
public class VendorFSShippingOptionManagerImpl extends UniversalManagerImpl
		implements
			VendorFSShippingOptionManager {

	private transient final Log log = LogFactory.getLog(VendorFSShippingOptionManagerImpl.class);
	private VendorFSShippingOptionDao vendorFSShippingOptionDao;

	public void setVendorFSShippingOptionDao(VendorFSShippingOptionDao vendorFSShippingOptionDao) {
		this.vendorFSShippingOptionDao = vendorFSShippingOptionDao;
	}

	public VendorFSShippingOptionDao getVendorFSShippingOptionDao() {
		return vendorFSShippingOptionDao;
	}

	public FulfillmentServiceShippingOptions getFulfillmentServiceShippingOptions(
			long fulfillmentServiceID) {
		return vendorFSShippingOptionDao.getFulfillmentServiceShippingOptions(fulfillmentServiceID);

	}

	/**
	 * @param vendorId
	 * @param fulfillmentServID
	 * @return
	 * @TODO Get the Shipment Model list for Vendor
	 */
	public List<ShippingCarrierOption> getShippingOptionsModelList(
			long vendorId, long fulfillmentServID) {

		// Get the vendor shipment list from database with the specified vendor
		// id and fulfillment service id
		List<VendorFulfillmentShippingOption> vendorFSShippingOptionsModelList = vendorFSShippingOptionDao
				.getShippingOptionsForVendorFS(vendorId, fulfillmentServID);
		// Get the shipment list from database table SHIP_OPT
		List<ShippingCarrierOption> shippingOptionsModelList = vendorFSShippingOptionDao
				.getShippingOptionsModelList();
		List<ShippingCarrierOption> sList = new ArrayList<ShippingCarrierOption>();
		if (null != vendorFSShippingOptionsModelList && !vendorFSShippingOptionsModelList.isEmpty()) {
			if (log.isDebugEnabled()) {
				log.debug("Got the vendorFSShippingOptionsModelList ");
			}
			Iterator<VendorFulfillmentShippingOption> itr = vendorFSShippingOptionsModelList
					.iterator();
			Iterator<ShippingCarrierOption> itrShip = shippingOptionsModelList.iterator();
			VendorFulfillmentShippingOption vendorFSShippingOptionsModel = null;

			ShippingCarrierOption shippingCarrierOption = null;
			while (itrShip.hasNext()) {
				if (itr.hasNext()) {
					vendorFSShippingOptionsModel = itr.next();
				}
				shippingCarrierOption = itrShip.next();
				if (log.isDebugEnabled()) {
					log.debug("outside if s.getShippingOptionId()="
							+ shippingCarrierOption.getShippingOptionId());
					log
							.debug("vendorFSShippingOptionsModel.getCompositeKeyForShippingOptionsId().getShippingOptionId()="
									+ vendorFSShippingOptionsModel
											.getCompositeKeyForShippingOptionsId()
											.getShippingOptionId());
				}
				/*
				 * If shipping carrier already added in vendor shipment table
				 * then set the direct bill account numb and is allowed value to
				 * the shipment model else add the new shipment carrier to the
				 * list with account and is allowed as default value
				 */

				if (vendorFSShippingOptionsModel != null
						&& vendorFSShippingOptionsModel.getCompositeKeyForShippingOptionsId()
								.getShippingOptionId().equals(
										shippingCarrierOption.getShippingOptionId())) {
					if (log.isDebugEnabled()) {
						log.debug("inside if s.getShippingOptionId()="
								+ shippingCarrierOption.getShippingOptionId());
					}
					shippingCarrierOption.setAccount(vendorFSShippingOptionsModel
							.getDirectBillAccountNumber());
					shippingCarrierOption.setIsAllowed(vendorFSShippingOptionsModel.getIsAllowed());
					sList.add(shippingCarrierOption);
				}
				else {
					if (log.isDebugEnabled()) {
						log.debug("inside else s.getShippingOptionId()="
								+ shippingCarrierOption.getShippingOptionId());
					}
					shippingCarrierOption.setIsAllowed("N");
					shippingCarrierOption.setAccount("");
					sList.add(shippingCarrierOption);
				}

			}

		}
		if (sList.isEmpty()) {
			if (log.isDebugEnabled()) {
				log.debug("returning shipping list from db");
			}
			sList = shippingOptionsModelList;
		}
		return sList;
	}

	/**
	 * @param fulfillmentServID
	 * @return
	 * @TODO Get the Shipment Model list for Fulfillment Service
	 */
	public List<ShippingCarrierOption> getShippingOptionsModelListForFulfillment(
			long fulfillmentServID) {
		List<FulfillmentServiceShippingOptions> fulfillmentShippingOptionsModelList = vendorFSShippingOptionDao
				.getShippingOptionsForFulfillment(fulfillmentServID);
		List<ShippingCarrierOption> shippingOptionsModelList = vendorFSShippingOptionDao
				.getShippingOptionsModelList();
		List<ShippingCarrierOption> sList = new ArrayList<ShippingCarrierOption>();

		if (null != fulfillmentShippingOptionsModelList
				&& !fulfillmentShippingOptionsModelList.isEmpty()) {
			if (log.isDebugEnabled()) {
				log.debug("Got the fulfillmentShippingOptionsModelList ="
						+ fulfillmentShippingOptionsModelList.size());
			}
			Iterator<FulfillmentServiceShippingOptions> itr = fulfillmentShippingOptionsModelList
					.iterator();
			Iterator<ShippingCarrierOption> itrShip = shippingOptionsModelList.iterator();
			FulfillmentServiceShippingOptions vendorFSShippingOptionsModel = null;

			ShippingCarrierOption shippingCarrierOption = null;
			while (itrShip.hasNext()) {
				if (itr.hasNext()) {
					vendorFSShippingOptionsModel = itr.next();
				}

				shippingCarrierOption = itrShip.next();
				if (log.isDebugEnabled()) {
					log.debug("outside if s.getShippingOptionId()="
							+ shippingCarrierOption.getShippingOptionId());
				}
				/*
				 * If shipping carrier already added in fulfillment shipment
				 * table then set the direct bill account numb and is allowed
				 * value to the shipment model else add the new shipment carrier
				 * to the list with account and is allowed as default value
				 */
				if (vendorFSShippingOptionsModel != null
						&& vendorFSShippingOptionsModel.getCompositeKeyForShippingOptionsId()
								.getShippingOptionId().equals(
										shippingCarrierOption.getShippingOptionId())) {
					if (log.isDebugEnabled()) {
						log.debug("inside if s.getShippingOptionId()="
								+ shippingCarrierOption.getShippingOptionId());
					}
					shippingCarrierOption.setAccount(vendorFSShippingOptionsModel
							.getDirectBillAccountNumber());
					shippingCarrierOption.setIsAllowed(vendorFSShippingOptionsModel.getIsAllowed());
					sList.add(shippingCarrierOption);
				}
				else {
					if (log.isDebugEnabled()) {
						log.debug("inside else s.getShippingOptionId()="
								+ shippingCarrierOption.getShippingOptionId());
					}
					shippingCarrierOption.setIsAllowed("N");
					shippingCarrierOption.setAccount("");
					sList.add(shippingCarrierOption);
				}

			}

		}
		if (sList.isEmpty()) {
			if (log.isDebugEnabled()) {
				log.debug("returning shipping list from db");
			}
			sList = shippingOptionsModelList;
		}
		return sList;
	}

	/**
	 * @param vendorId
	 * @param fulfillmentServID
	 * @return
	 * @TODO Get the VendorFulfillmentShippingOption from database with given
	 *       vendor id and fulfillment id
	 */
	public VendorFulfillmentShippingOption getVendorFSShippingOptions(
			long vendorId, long fulfillmentServID) {
		return vendorFSShippingOptionDao.getVendorFSShippingOptions(vendorId, fulfillmentServID);
	}

	/**
	 * @param vendorFSShippingOptionsModel
	 * @param ShippingOptionsModel
	 * @return List<VendorFulfillmentShippingOption>
	 * @TODO Save vendor fulfillment shipping options.
	 */
	public List<VendorFulfillmentShippingOption> saveVendorFSShippingOptions(
			VendorFulfillmentShippingOption vendorFSShippingOptionsModel,
			List<ShippingCarrierOption> ShippingOptionsModel) {
		VendorFulfillmentShippingOption vendorFulfillmentShipOption = null;
		List<VendorFulfillmentShippingOption> vendorFSShippingOptionsList = new ArrayList<VendorFulfillmentShippingOption>();
		Iterator<ShippingCarrierOption> itr = ShippingOptionsModel.iterator();
		if (log.isDebugEnabled()) {
			log.debug("ShippingOptionsModel size=" + ShippingOptionsModel.size());
		}
		CompositeKeyForShippingOptions compKey = new CompositeKeyForShippingOptions();
		ShippingCarrierOption shippingOptionsModelTemp = null;

		/*
		 * Setting the values from shipment model list to vendor shipment model
		 * list. values like- account number, is allowed Also creating the
		 * vendor shipment model list to save in databse
		 */
		try {
			while (itr.hasNext()) {
				shippingOptionsModelTemp = itr.next();
				if (log.isDebugEnabled()) {
					log.debug("shippingOptionsModelTemp="
							+ shippingOptionsModelTemp.getShippingOptionId());
				}
				vendorFulfillmentShipOption = new VendorFulfillmentShippingOption();
				compKey = new CompositeKeyForShippingOptions();
				if (StringUtils.isBlank(vendorFSShippingOptionsModel.getAllowDirectBill())) {
					vendorFulfillmentShipOption.setAllowDirectBill("N");
				}
				else {
					vendorFulfillmentShipOption.setAllowDirectBill(vendorFSShippingOptionsModel
							.getAllowDirectBill());
				}
				if (StringUtils.isBlank(vendorFSShippingOptionsModel.getAllowVendorBill())) {
					vendorFulfillmentShipOption.setAllowVendorBill("N");
				}
				else {
					vendorFulfillmentShipOption.setAllowVendorBill(vendorFSShippingOptionsModel
							.getAllowVendorBill());
				}
				if (log.isDebugEnabled()) {
					log.debug("shippingOptionsModelTemp.getIsAllowed()="
							+ shippingOptionsModelTemp.getIsAllowed());
				}
				if (null != vendorFSShippingOptionsModel.getAllowDirectBill()
						&& vendorFSShippingOptionsModel.getAllowDirectBill().equals("Y")
						&& StringUtils.isNotBlank(shippingOptionsModelTemp.getIsAllowed())) {
					if (log.isDebugEnabled()) {
						log.debug("Setting is alloewd" + shippingOptionsModelTemp.getIsAllowed());
					}
					vendorFulfillmentShipOption.setIsAllowed(shippingOptionsModelTemp
							.getIsAllowed());
				}
				else {
					vendorFulfillmentShipOption.setIsAllowed("N");
				}
				if (null != vendorFSShippingOptionsModel.getAllowDirectBill()
						&& vendorFSShippingOptionsModel.getAllowDirectBill().equals("Y")
						&& StringUtils.isNotBlank(shippingOptionsModelTemp.getAccount())) {
					if (log.isDebugEnabled()) {
						log.debug("Setting is account" + shippingOptionsModelTemp.getAccount());
					}
					vendorFulfillmentShipOption.setDirectBillAccountNumber(shippingOptionsModelTemp
							.getAccount());
				}
				else {
					vendorFulfillmentShipOption.setDirectBillAccountNumber("");
				}
				if (log.isDebugEnabled()) {
					log.debug("shippingOptionsModelTemp.getShippingOptionId()="
							+ shippingOptionsModelTemp.getShippingOptionId());
				}
				compKey.setFulfillmentServId(vendorFSShippingOptionsModel
						.getCompositeKeyForShippingOptionsId().getFulfillmentServId());
				compKey.setVendorId(vendorFSShippingOptionsModel
						.getCompositeKeyForShippingOptionsId().getVendorId());
				compKey.setShippingOptionId(shippingOptionsModelTemp.getShippingOptionId());
				vendorFulfillmentShipOption.setCompositeKeyForShippingOptionsId(compKey);
				if (log.isDebugEnabled()) {
					log.debug("temp="
							+ vendorFulfillmentShipOption.getCompositeKeyForShippingOptionsId()
									.getShippingOptionId());
				}
				vendorFSShippingOptionsList.add(vendorFulfillmentShipOption);

			}
			if (log.isDebugEnabled()) {
				log.debug("vendorFSShippingOptionsList=" + vendorFSShippingOptionsList.size());
			}
			vendorFSShippingOptionsList = vendorFSShippingOptionDao
					.saveVendorFSShippingOptions(vendorFSShippingOptionsList);
		}
		catch (Exception e) {
			e.printStackTrace();

		}
		return vendorFSShippingOptionsList;
	}

	/**
	 * @param fulfillmentShippingOptionsModel
	 * @param ShippingOptionsModel
	 * @return
	 * @TODO save fulfillment shipping options
	 */
	public List<FulfillmentServiceShippingOptions> saveFulfillmentShippingOptions(
			FulfillmentServiceShippingOptions fulfillmentShippingOptionsModel,
			List<ShippingCarrierOption> ShippingOptionsModel) {
		FulfillmentServiceShippingOptions fulfillmentShippingOptions = null;
		List<FulfillmentServiceShippingOptions> fulfillmentShippingOptionsList = new ArrayList<FulfillmentServiceShippingOptions>();
		Iterator<ShippingCarrierOption> itr = ShippingOptionsModel.iterator();
		if (log.isDebugEnabled()) {
			log.debug("ShippingOptionsModel size=" + ShippingOptionsModel.size());
		}
		CompositeKeyForFSShippingOptions compKey = new CompositeKeyForFSShippingOptions();
		ShippingCarrierOption shippingOptionsModelTemp = null;

		/*
		 * Setting the values from shipment model list to fulfillment shipment
		 * model list. values like- account number, is allowed .Also creating
		 * the fulfillment shipment model list to save in database.
		 */
		try {
			while (itr.hasNext()) {
				shippingOptionsModelTemp = itr.next();
				if (log.isDebugEnabled()) {
					log.debug("shippingOptionsModelTemp="
							+ shippingOptionsModelTemp.getShippingOptionId());
				}
				fulfillmentShippingOptions = new FulfillmentServiceShippingOptions();
				compKey = new CompositeKeyForFSShippingOptions();
				if (StringUtils.isBlank(fulfillmentShippingOptionsModel.getAllowDirectBill())) {
					fulfillmentShippingOptions.setAllowDirectBill("N");
				}
				else {
					fulfillmentShippingOptions.setAllowDirectBill(fulfillmentShippingOptionsModel
							.getAllowDirectBill());
				}
				if (StringUtils.isBlank(fulfillmentShippingOptionsModel.getAllowVendorBill())) {
					fulfillmentShippingOptions.setAllowVendorBill("N");
				}
				else {
					if (log.isDebugEnabled()) {
						log.debug("fulfillmentShippingOptionsModel.getAllowVendorBill()="
								+ fulfillmentShippingOptionsModel.getAllowVendorBill());
					}
					fulfillmentShippingOptions.setAllowVendorBill(fulfillmentShippingOptionsModel
							.getAllowVendorBill());
				}
				if (log.isDebugEnabled()) {
					log.debug("shippingOptionsModelTemp.getIsAllowed()="
							+ shippingOptionsModelTemp.getIsAllowed());
				}
				if (null != fulfillmentShippingOptionsModel.getAllowDirectBill()
						&& fulfillmentShippingOptionsModel.getAllowDirectBill().equals("Y")
						&& StringUtils.isNotBlank(shippingOptionsModelTemp.getIsAllowed())) {
					if (log.isDebugEnabled()) {
						log.debug("Setting is allowed=" + shippingOptionsModelTemp.getIsAllowed());
					}
					fulfillmentShippingOptions
							.setIsAllowed(shippingOptionsModelTemp.getIsAllowed());
				}
				else {
					fulfillmentShippingOptions.setIsAllowed("N");
				}
				if (null != fulfillmentShippingOptionsModel.getAllowDirectBill()
						&& fulfillmentShippingOptionsModel.getAllowDirectBill().equals("Y")
						&& StringUtils.isNotBlank(shippingOptionsModelTemp.getAccount())) {
					if (log.isDebugEnabled()) {
						log.debug("Setting is account=" + shippingOptionsModelTemp.getAccount());
					}
					fulfillmentShippingOptions.setDirectBillAccountNumber(shippingOptionsModelTemp
							.getAccount());
				}
				else {
					fulfillmentShippingOptions.setDirectBillAccountNumber("");
				}
				if (log.isDebugEnabled()) {
					log.debug("shippingOptionsModelTemp.getShippingOptionId()="
							+ shippingOptionsModelTemp.getShippingOptionId());
				}
				compKey.setFulfillmentServId(fulfillmentShippingOptionsModel
						.getCompositeKeyForShippingOptionsId().getFulfillmentServId());
				compKey.setShippingOptionId(shippingOptionsModelTemp.getShippingOptionId());
				fulfillmentShippingOptions.setCompositeKeyForShippingOptionsId(compKey);
				if (log.isDebugEnabled()) {
					log.debug("temp="
							+ fulfillmentShippingOptions.getCompositeKeyForShippingOptionsId()
									.getShippingOptionId());
				}
				fulfillmentShippingOptionsList.add(fulfillmentShippingOptions);

			}
			if (log.isDebugEnabled()) {
				log
						.debug("fulfillmentShippingOptionsList="
								+ fulfillmentShippingOptionsList.size());
			}

			fulfillmentShippingOptionsList = vendorFSShippingOptionDao
					.saveFulfillmentShippingOptions(fulfillmentShippingOptionsList);
		}
		catch (Exception e) {
			e.printStackTrace();

		}
		return fulfillmentShippingOptionsList;
	}

}
