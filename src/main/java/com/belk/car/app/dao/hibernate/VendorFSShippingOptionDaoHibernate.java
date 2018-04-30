
package com.belk.car.app.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.appfuse.model.User;

import com.belk.car.app.dao.VendorFSShippingOptionDao;
import com.belk.car.app.model.oma.FulfillmentServiceShippingOptions;
import com.belk.car.app.model.oma.ShippingCarrierOption;
import com.belk.car.app.model.oma.VendorFulfillmentShippingOption;

/**
 * @author afusy07 priyanka_gadia@syntelinc.com
 * @Date 18-Dec-09
 */
public class VendorFSShippingOptionDaoHibernate extends UniversalDaoHibernate
implements
VendorFSShippingOptionDao {

	private transient final Log log = LogFactory.getLog(VendorFSShippingOptionDaoHibernate.class);
	//Following code might be required for manual query performance testing. So havn't deleted.
	/*
	 * public void time() { SessionFactory sessionFactory = getSessionFactory();
	 * Statistics stats = sessionFactory.getStatistics();log.debug(
	 * "inside time----------------------------------------------------------------"
	 * ); stats.setStatisticsEnabled(true); String[] s = stats.getQueries();
	 * log.debug("s.length=" + s.length); int i = 0; for (i = 0; i < s.length;
	 * i++) { QueryStatistics queryStats = stats.getQueryStatistics(s[i]);
	 * log.debug("s" + i + "=" + s[i]); long time =
	 * queryStats.getExecutionMaxTime(); log.debug("time for| " + s[i] + "|" +
	 * time); i++; } }
	 */

	/**
	 * @param fulfillmentServiceID
	 * @return FulfillmentServiceShippingOptions
	 * @TODO get FulfillmentServiceShippingOptions based on fulfillment service
	 *       ID
	 */
	@SuppressWarnings("unchecked")
	public FulfillmentServiceShippingOptions getFulfillmentServiceShippingOptions(
			long fulfillmentServiceID) {
		StringBuffer sqlB = new StringBuffer("from FulfillmentServiceShippingOptions f ");
		sqlB.append("WHERE " + "f.compositeKeyForShippingOptionsId.fulfillmentServId=?"
				+ " order by " + "f.compositeKeyForShippingOptionsId.shippingOptionId ");

		ArrayList<FulfillmentServiceShippingOptions> fulfillmentShippingOptionsList = (ArrayList<FulfillmentServiceShippingOptions>) getHibernateTemplate()
		.find(sqlB.toString(), fulfillmentServiceID);
		getHibernateTemplate().flush();
		FulfillmentServiceShippingOptions shipOption = null;
		if (null != fulfillmentShippingOptionsList && !fulfillmentShippingOptionsList.isEmpty()) {
			shipOption = fulfillmentShippingOptionsList.get(0);
		}
		return shipOption;
	}

	/**
	 * @param vendorId
	 * @param fulfillmentServID
	 * @return List<VendorFulfillmentShippingOption>
	 * @TODO get List<VendorFulfillmentShippingOption> for vendor
	 */
	@SuppressWarnings("unchecked")
	public List<VendorFulfillmentShippingOption> getShippingOptionsForVendorFS(
			long vendorId, long fulfillmentServID) {
		// time();
		ArrayList<VendorFulfillmentShippingOption> vendorFSShippingOptionsList = null;
		if (vendorId != 0) {
			StringBuffer sqlB = new StringBuffer("from VendorFulfillmentShippingOption vf ");
			sqlB.append("WHERE ").append("vf.compositeKeyForShippingOptionsId.vendorId=?").append(
			" AND ").append("vf.compositeKeyForShippingOptionsId.fulfillmentServId=?")
			.append(" order by ").append(
			"vf.compositeKeyForShippingOptionsId.shippingOptionId ");
			ArrayList<Object> values = new ArrayList<Object>();
			values.add(vendorId);
			values.add(fulfillmentServID);
			vendorFSShippingOptionsList = (ArrayList<VendorFulfillmentShippingOption>) getHibernateTemplate()
			.find(sqlB.toString(), values.toArray());
			// time();
			getHibernateTemplate().flush();
		}
		else {
			// time();
			StringBuffer sqlB = new StringBuffer("from VendorFulfillmentShippingOption vf ");
			sqlB.append("WHERE").append(" vf.compositeKeyForShippingOptionsId.vendorId IS NULL")
			.append(" AND ").append(
			"vf.compositeKeyForShippingOptionsId.fulfillmentServId=?").append(
			" order by ").append(
			"vf.compositeKeyForShippingOptionsId.shippingOptionId");

			vendorFSShippingOptionsList = (ArrayList<VendorFulfillmentShippingOption>) getHibernateTemplate()
			.find(sqlB.toString(), fulfillmentServID);
			getHibernateTemplate().flush();
		}
		// time();
		return vendorFSShippingOptionsList;
	}

	/**
	 * @param fulfillmentServID
	 * @return List<FulfillmentServiceShippingOptions> Get
	 * @TODO Get shipping Options for Fulfillment service
	 */
	@SuppressWarnings("unchecked")
	public List<FulfillmentServiceShippingOptions> getShippingOptionsForFulfillment(
			long fulfillmentServID) {
		// time();
		ArrayList<FulfillmentServiceShippingOptions> vendorFSShippingOptionsList = null;

		StringBuffer sqlB = new StringBuffer("from FulfillmentServiceShippingOptions vf ");
		sqlB.append("WHERE  ").append("vf.compositeKeyForShippingOptionsId.fulfillmentServId=?")
		.append(" order by")
		.append(" vf.compositeKeyForShippingOptionsId.shippingOptionId");

		vendorFSShippingOptionsList = (ArrayList<FulfillmentServiceShippingOptions>) getHibernateTemplate()
		.find(sqlB.toString(), fulfillmentServID);
		// time();
		getHibernateTemplate().flush();

		return vendorFSShippingOptionsList;
	}

	/**
	 * @return
	 * @TODO Get shipping option list from SHIPPING_CARRIER_OPTION table
	 */
	@SuppressWarnings("unchecked")
	public List<ShippingCarrierOption> getShippingOptionsModelList() {
		// time();
		return getHibernateTemplate().find(
				"from ShippingCarrierOption s " + "order by s.shippingOptionId");
	}

	@SuppressWarnings("unchecked")
	public VendorFulfillmentShippingOption getVendorFSShippingOptions(
			long vendorId, long fulfillmentServID) {
		// time();

		StringBuffer sqlB = new StringBuffer("from VendorFulfillmentShippingOption vf ");
		sqlB.append("WHERE " + "vf.compositeKeyForShippingOptionsId.vendorId=?" + " AND "
				+ "vf.compositeKeyForShippingOptionsId.fulfillmentServId=? " + "order by"
				+ " vf.compositeKeyForShippingOptionsId.shippingOptionId");
		ArrayList<Object> values = new ArrayList<Object>();
		values.add(vendorId);
		values.add(fulfillmentServID);
		ArrayList<VendorFulfillmentShippingOption> vendorFSShippingOptionsList = (ArrayList<VendorFulfillmentShippingOption>) getHibernateTemplate()
		.find(sqlB.toString(), values.toArray());
		if (null != vendorFSShippingOptionsList && !vendorFSShippingOptionsList.isEmpty()) {
			return vendorFSShippingOptionsList.get(0);
		}
		// time();
		return null;
	}

	/**
	 * Returns the currently logged in user
	 * 
	 * @return user User User bean, currently logged in.
	 */
	public User getLoggedInUser() {
		if (log.isDebugEnabled()) {
			log.debug("Inside getLoggedIntUser() Method..");
		}
		User user = null;
		Authentication auth = ((SecurityContext) SecurityContextHolder.getContext())
		.getAuthentication();
		if (auth.getPrincipal() instanceof UserDetails) {
			user = (User) auth.getPrincipal();
		}
		return user;
	}

	/**
	 * @param vendorFSShippingOptionsList
	 * @return List<VendorFulfillmentShippingOption>
	 * @TODO Save vendor shipping options
	 */
	public List<VendorFulfillmentShippingOption> saveVendorFSShippingOptions(
			List<VendorFulfillmentShippingOption> vendorFSShippingOptionsList) {
		if (log.isDebugEnabled()) {
			log.debug("vendorFSShippingOptionsList=" + vendorFSShippingOptionsList.size());
		}
		// time();
		// Set auditable info before saving
		for (VendorFulfillmentShippingOption shipOption : vendorFSShippingOptionsList) {
			StringBuffer sqlB = new StringBuffer("from VendorFulfillmentShippingOption vf ");
			sqlB.append("WHERE vf.compositeKeyForShippingOptionsId.shippingOptionId= ? AND ")
			.append(" vf.compositeKeyForShippingOptionsId.fulfillmentServId= ? AND ")
			.append(" vf.compositeKeyForShippingOptionsId.vendorId=?");
			ArrayList<Object> values = new ArrayList<Object>();
			values.add(shipOption.getCompositeKeyForShippingOptionsId().getShippingOptionId());
			values.add(shipOption.getCompositeKeyForShippingOptionsId().getFulfillmentServId());
			values.add(shipOption.getCompositeKeyForShippingOptionsId().getVendorId());

			List<VendorFulfillmentShippingOption> shipOptionFromDBList = (List<VendorFulfillmentShippingOption>) getHibernateTemplate()
			.find(sqlB.toString(), values.toArray());
			if(!shipOptionFromDBList.isEmpty()){
				VendorFulfillmentShippingOption shipOptionFromDB=shipOptionFromDBList.get(0);
				if (null != shipOptionFromDB) {
					log.debug("Got shipping options ="
							+ shipOptionFromDB.getCreatedBy()
							+ " :"
							+ shipOptionFromDB.getCreatedDate()
							+ shipOptionFromDB.getCompositeKeyForShippingOptionsId()
							.getShippingOptionId());

					shipOptionFromDB.setAuditInfo(getLoggedInUser());
					shipOptionFromDB.setAllowDirectBill(shipOption.getAllowDirectBill());
					shipOptionFromDB.setAllowVendorBill(shipOption.getAllowVendorBill());
					shipOptionFromDB
					.setDirectBillAccountNumber(shipOption.getDirectBillAccountNumber());
					shipOptionFromDB.setIsAllowed(shipOption.getIsAllowed());

					getHibernateTemplate().saveOrUpdate(shipOptionFromDB);

				}
			}
			else {
				shipOption.setAuditInfo(getLoggedInUser());
				getHibernateTemplate().saveOrUpdate(shipOption);
			}
		}
        getHibernateTemplate().flush();
		return vendorFSShippingOptionsList;
	}

	/**
	 * @param fulfillmentFSShippingOptionsList
	 * @return List<FulfillmentServiceShippingOptions>
	 * @TODO Save fulfillment shipping options
	 */
	public List<FulfillmentServiceShippingOptions> saveFulfillmentShippingOptions(
			List<FulfillmentServiceShippingOptions> fulfillmentFSShippingOptionsList) {
		if (log.isDebugEnabled()) {
			log.debug("vendorFSShippingOptionsList=" + fulfillmentFSShippingOptionsList.size());
		}
		for (FulfillmentServiceShippingOptions shipOption : fulfillmentFSShippingOptionsList) {
			StringBuffer sqlB = new StringBuffer("from FulfillmentServiceShippingOptions vf ");
			sqlB.append("WHERE vf.compositeKeyForShippingOptionsId.shippingOptionId= ? AND ")
			.append(" vf.compositeKeyForShippingOptionsId.fulfillmentServId= ?  ");
			
			ArrayList<Object> values = new ArrayList<Object>();
			values.add(shipOption.getCompositeKeyForShippingOptionsId().getShippingOptionId());
			values.add(shipOption.getCompositeKeyForShippingOptionsId().getFulfillmentServId());
			

			List<FulfillmentServiceShippingOptions> shipOptionFromDBList = (List<FulfillmentServiceShippingOptions>) getHibernateTemplate()
			.find(sqlB.toString(), values.toArray());
			if(!shipOptionFromDBList.isEmpty()){
				FulfillmentServiceShippingOptions shipOptionFromDB=shipOptionFromDBList.get(0);
				if (null != shipOptionFromDB) {
					log.debug("Got shipping options ="
							+ shipOptionFromDB.getCreatedBy()
							+ " :"
							+ shipOptionFromDB.getCreatedDate()
							+ shipOptionFromDB.getCompositeKeyForShippingOptionsId()
							.getShippingOptionId());

					shipOptionFromDB.setAuditInfo(getLoggedInUser());
					shipOptionFromDB.setAllowDirectBill(shipOption.getAllowDirectBill());
					shipOptionFromDB.setAllowVendorBill(shipOption.getAllowVendorBill());
					shipOptionFromDB
					.setDirectBillAccountNumber(shipOption.getDirectBillAccountNumber());
					shipOptionFromDB.setIsAllowed(shipOption.getIsAllowed());

					getHibernateTemplate().saveOrUpdate(shipOptionFromDB);

				}
			}
			else {
				shipOption.setAuditInfo(getLoggedInUser());
				getHibernateTemplate().saveOrUpdate(shipOption);
			}
		}
        getHibernateTemplate().flush();
		// time();
		return fulfillmentFSShippingOptionsList;
	}

}
