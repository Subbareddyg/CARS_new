
package com.belk.car.app.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.appfuse.model.User;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

import com.belk.car.app.dao.VendorFulfillmentServiceDao;
import com.belk.car.app.dto.DropshipVendorActiveStyleSkuDTO;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.oma.Address;
import com.belk.car.app.model.oma.CompoundKeyVndrShip;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.model.oma.FulfillmentServiceVendorLocation;
import com.belk.car.app.model.oma.FulfillmentServiceVendorReturn;
import com.belk.car.app.model.oma.InvoiceMethods;
import com.belk.car.app.model.oma.ReturnMethod;
import com.belk.car.app.model.oma.State;

/**
 * @author afusy07-priyanka_gadia@syntelinc.com
 * @Date 12-dec-09
 */
public class VendorFulfillmentServiceDaoHibernate extends UniversalDaoHibernate
implements
VendorFulfillmentServiceDao {

	private transient final Log log = LogFactory.getLog(VendorFulfillmentServiceDaoHibernate.class);
	/**
	 * @param id
	 * @return Vendor Based on the ID
	 * @TODO Get a record for Vendor by passing the vendor id
	 */
	public FulfillmentServiceVendor getVendorFulfillmentServicesByID(Long id) {
		return (FulfillmentServiceVendor) getHibernateTemplate().get(
				FulfillmentServiceVendor.class, id);
	}

	/**
	 * @param name - Vendor Name
	 * @param id - Vendor Number
	 * @param status - Status of vendor fulfillment service
	 * @param fulfillmentServiceId - Fulfillment Service ID
	 * @return - List of Fulfillment Service Vendors based on search criteria
	 * @TODO Get records for Vendor by search criteria
	 */
	@SuppressWarnings("unchecked")
	public List<FulfillmentServiceVendor> getVendorFulfillmentServiceList(
			String name, Long id, String status, long fulfillmentServiceId) {
		String likeFormat = "%%%1$s%%";
		ArrayList<String> query = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();

		StringBuffer sqlB = new StringBuffer("from FulfillmentServiceVendor vf ");

		if (!StringUtils.isBlank(name) || !(id == null) || !StringUtils.isBlank(status)) {
			sqlB.append(" inner join fetch vf.vndr as v ");
		}
		if (!StringUtils.isBlank(name)) {
			query.add("upper(v.name) LIKE ?");
			values.add(String.format(likeFormat, name.toUpperCase(Locale.US)).toString());
		}
		if (null != id) {
			query.add("v.vendorNumber = ?");
			values.add(id.toString());
		}
		if (!StringUtils.isBlank(status) && !status.toUpperCase().equals("ALL")) {
			query.add("upper(vf.statusCd) LIKE ?");
			values.add(status.toUpperCase(Locale.US).toString());
		}
		query.add("vf.fulfillmentServId = ?");
		values.add(fulfillmentServiceId);
		if (!query.isEmpty()) {
			sqlB.append(" WHERE ");
			int i = 0;
			for (String s : query) {
				if (i > 0) {
					sqlB.append(" AND ");
				}
				sqlB.append(s);
				i++;
			}
		}

		sqlB.append(" order by v.vendorNumber");

		return getHibernateTemplate().find(sqlB.toString(), values.toArray());

	}

	/**
	 * @param fulfillmentServiceId - Fulfillment Service ID
	 * @return - Fulfillment Service Vendor List
	 * @TODO Get all records for Vendor under a fulfillment service
	 */
	@SuppressWarnings("unchecked")
	public List<FulfillmentServiceVendor> getAllVendorFulfillmentServiceList(
			long fulfillmentServiceId) {

		return getHibernateTemplate().find(
				"from FulfillmentServiceVendor " + "where fulfillmentServId=?",
				fulfillmentServiceId);
	}

	/**
	 * @param FulfillmentServiceVendor
	 * @return FulfillmentServiceVendor -Saved FulfillmentServiceVendor object
	 * @TODO Save records for Vendor under a fulfillment service
	 */
	public FulfillmentServiceVendor addVendorFulfillmentService(
			FulfillmentServiceVendor vendorFulfillmentServiceModel) {
		if (log.isDebugEnabled()) {
			log.debug("Saving record for-" + vendorFulfillmentServiceModel.toString());
		}
		getHibernateTemplate().saveOrUpdate(vendorFulfillmentServiceModel);
		getHibernateTemplate().flush();
		return vendorFulfillmentServiceModel;
	}

	/**
	 * @param addrList - List of addresses for vendor
	 * @param vendorId - Vendor ID
	 * @param fulfillmentId -Fulfillment Service ID
	 * @param addrIdsFromSession - Address IDs to be deleted
	 * @return List of saved addresses
	 * @TODO Save addresses for Vendor under a fulfillment service
	 */
	public List<Address> saveAddress(
			List<Address> addrList, Long vendorId, Long fulfillmentId, List<Long> addrIdsFromSession) {

		Iterator<Address> itr = addrList.iterator();
		Address addr;
		Address addrNew = new Address();
		List<Address> addrListNew = new ArrayList<Address>();
		long addrId;
		CompoundKeyVndrShip cmpKey = new CompoundKeyVndrShip();
		FulfillmentServiceVendorLocation shipModel = new FulfillmentServiceVendorLocation();
		if (log.isDebugEnabled()) {
			log.debug("size of address ids to be deleted===============" + addrIdsFromSession);
		}
		/*
		 * 1-Save addresses for Vendor in ADDRESS table
		 */
		while (itr.hasNext()) {
			addr = itr.next();
			addrId = addr.getAddressID();
			if (log.isDebugEnabled()) {
				log.debug("addrId before saving=" + addrId);
			}
			/*
			 * Saving new addresses
			 */
			if (addrId < 0) {
				addrNew = new Address();
				addrNew.setAddr1(addr.getAddr1());
				addrNew.setAddr2(addr.getAddr2());
				addrNew.setCity(addr.getCity());
				addrNew.setState(addr.getState());
				addrNew.setLocName(addr.getLocName());
				addrNew.setZip(addr.getZip());
				addrNew.setStatus("ACTIVE");
				addrNew.setAuditInfo(getLoggedInUser());
				getHibernateTemplate().save(addrNew);
				// Adding saved address in new address list
				addrListNew.add(addrNew);
				if (log.isDebugEnabled()) {
					log.debug("addrId after saving=" + addrNew.getAddressID());
				}
				getHibernateTemplate().flush();
			}
		}

		/*
		 * 2-Saving venid,fsid,addrid in intermediate table
		 */
		itr = addrListNew.iterator();
		while (itr.hasNext()) {
			cmpKey = new CompoundKeyVndrShip();
			shipModel = new FulfillmentServiceVendorLocation();
			addr = itr.next();
			addrId = addr.getAddressID();
			if (log.isDebugEnabled()) {
				log.debug("addrId before saving in intermediate table=" + addrId);
				log.debug("fsId before saving in intermediate table=" + fulfillmentId);
			}
			// Preparing a compound key for vendor_shipment table
			cmpKey.setAddrId(addrId);
			cmpKey.setFulfillmentServId(fulfillmentId);
			cmpKey.setVendorId(vendorId);
			shipModel.setCompoundId(cmpKey);
			// long start = Calendar.getInstance().getTimeInMillis();
			getHibernateTemplate().saveOrUpdate(shipModel);
			getHibernateTemplate().flush();
			if (log.isDebugEnabled()) {
				log.debug("Saved the intermediate data shipModel="
						+ shipModel.getCompoundId().getVendorId());
			}

		}
		/*
		 * 3-Remove the addresses user wished to remove by clicking on remove
		 * link
		 */
		removeAddr(addrIdsFromSession, vendorId, fulfillmentId);

		/*
		 * 4-Get the latest Address list for vendor and fulfillment service and
		 * return
		 */

		List<Address> addrListNewFromDB = getAddressForVen(vendorId, fulfillmentId);

		return (ArrayList<Address>) addrListNewFromDB;
	}

	/**
	 * @param vendorID
	 * @param fulfillmentServiceID
	 * @return Fulfillment Service Vendor Return
	 * @TODO Get return for vendor
	 */
	@SuppressWarnings("unchecked")
	public FulfillmentServiceVendorReturn getVendorReturns(Long vendorID, Long fulfillmentServiceID) {

		StringBuffer sqlB = new StringBuffer("from FulfillmentServiceVendorReturn vr ");
		ArrayList<Object> values = new ArrayList<Object>();
		if (vendorID != null && fulfillmentServiceID != null) {
			sqlB.append("WHERE vr.fulfillmentServId=? AND vr.vendorID=?");
			values.add(fulfillmentServiceID);
			values.add(vendorID);
		}

		// Executing the query generated
		List<FulfillmentServiceVendorReturn> vendorReturnList = getHibernateTemplate().find(
				sqlB.toString(), values.toArray());
		FulfillmentServiceVendorReturn vendordReturn = null;
		if (vendorReturnList != null && !vendorReturnList.isEmpty()) {
			if (log.isDebugEnabled()) {
				log.debug("...vndrReturnList=" + vendorReturnList.size());
			}
			vendordReturn = vendorReturnList.get(0);
		}

		return vendordReturn;
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
	 * @param vendorReturn - Fulfillment service vendor return model
	 * @return - Saved Fulfillment service vendor return model
	 * @TODO Save return for vendor
	 */
	public FulfillmentServiceVendorReturn saveVendorReturn(
			FulfillmentServiceVendorReturn vendorReturn) {

		Address address = vendorReturn.getAddr();
		address.setStatus(Status.ACTIVE);
		if (log.isDebugEnabled()) {
			log.debug("addr from FulfillmentServiceVendorReturn==" + address.toString());
		}
		/*
		 * 1- Save address for vendor return
		 */
		// long start = Calendar.getInstance().getTimeInMillis();
		StringBuffer sqlB = new StringBuffer("from Address ad ");
		ArrayList<Object> values = new ArrayList<Object>();
		sqlB.append("WHERE ad.addressID=?");
		values.add(address.getAddressID());
		List<Address> addressNewList=(List<Address>) getHibernateTemplate().find(sqlB.toString(), values.toArray());
		if(!addressNewList.isEmpty()){

			Address addressNew=addressNewList.get(0);
			log.debug("Got the address="+addressNew.getAddressID());
			if(null!=addressNew){
				addressNew.setAuditInfo(getLoggedInUser());
				addressNew.setAddr1(address.getAddr1());
				addressNew.setAddr2(address.getAddr2());
				addressNew.setCity(address.getCity());
				addressNew.setLocName(address.getLocName());
				addressNew.setState(address.getState());
				addressNew.setStatus(address.getStatus());
				addressNew.setZip(address.getZip());
				getHibernateTemplate().saveOrUpdate(addressNew);
			}
		}
		else{
			address.setAuditInfo(getLoggedInUser());
			getHibernateTemplate().saveOrUpdate(address);
		}

		/*
		 * 2- Get the address Id for saved address for vendor return
		 */
		long id = address.getAddressID();
		if (log.isDebugEnabled()) {
			log.debug("addr ID from Address==" + id);
		}
		/*
		 * 3- Store the address id in vendor returns model
		 */

		vendorReturn.setAddrId(id);
		/*
		 * 4-Save the vendor return object in DB
		 */
		// start = Calendar.getInstance().getTimeInMillis();
		getHibernateTemplate().saveOrUpdate(vendorReturn);
        getHibernateTemplate().flush();
		return vendorReturn;
	}

	/**
	 * @return List of return Methods.
	 * @TODO Get the return methods for return methods dropdown on returns page
	 */
	@SuppressWarnings("unchecked")
	public List<ReturnMethod> getReturnMethods() {
		// time();

		return (ArrayList<ReturnMethod>) getHibernateTemplate().find("from ReturnMethod");
	}

	/**
	 * @param vendorId -Vendor ID
	 * @param fulfillmentServiceId -Fulfillment Service ID
	 * @return - List of address
	 * @TODO Get the address list for given vendor and fulfillment service
	 */
	@SuppressWarnings("unchecked")
	public List<Address> getAddressForVen(Long vendorId, Long fulfillmentServiceId) {
		ArrayList<Address> addressListNew = new ArrayList<Address>();
		long id;
		StringBuffer sqlB = new StringBuffer("from FulfillmentServiceVendorLocation vf ");
		sqlB.append("WHERE vf.compoundId.vendorId=? AND "
				+ "vf.fullfillServ.fulfillmentServiceID=?");
		ArrayList<Object> values = new ArrayList<Object>();
		values.add(vendorId);
		values.add(fulfillmentServiceId);
		/*
		 * 1- Get the vendor shipment list i.e intermediate table's data
		 */
		List<FulfillmentServiceVendorLocation> shipModelList = (ArrayList<FulfillmentServiceVendorLocation>) getHibernateTemplate()
		.find(sqlB.toString(), values.toArray());
		if (log.isDebugEnabled()) {
			log.debug("Got the shipModelList=" + shipModelList.size());
		}

		/*
		 * 2- Get the address for the address id in above model list
		 */
		for (int i = 0; i < shipModelList.size(); i++) {
			id = shipModelList.get(i).getCompoundId().getAddrId();
			// long start = Calendar.getInstance().getTimeInMillis();
			Address address1 = (Address) getHibernateTemplate().get(Address.class, id);
			addressListNew.add(address1);
		}
		/*
		 * 3- Return the address model
		 */
		// time();
		return (ArrayList<Address>) addressListNew;

	}

	/**
	 * @param addressIdsFromSession - List of Address IDs to be deleted
	 * @param vendorId - Vendor ID
	 * @param fulfillmentId -Fulfillment Service ID
	 * @return void
	 * @TODO Remove the address models
	 */
	public void removeAddr(List<Long> addressIdsFromSession, Long vendorId, Long fulfillmentId) {

		if (null != addressIdsFromSession && !addressIdsFromSession.isEmpty()) {
			Iterator<Long> itr = addressIdsFromSession.iterator();
			FulfillmentServiceVendorLocation shipModel = new FulfillmentServiceVendorLocation();
			CompoundKeyVndrShip compoundKey = null;
			List<Address> addrList = new ArrayList<Address>();
			while (itr.hasNext()) {

				Long addrId = itr.next();
				if (log.isDebugEnabled()) {
					log.debug("address id to be deleted=" + addrId);
				}
				compoundKey = new CompoundKeyVndrShip();
				compoundKey.setAddrId(addrId);
				compoundKey.setFulfillmentServId(fulfillmentId);
				compoundKey.setVendorId(vendorId);
				shipModel.setCompoundId(compoundKey);
				getHibernateTemplate().delete(shipModel);
				getHibernateTemplate().flush();
				Address address = (Address) getHibernateTemplate().get(Address.class, addrId);
				address.setStatus(Status.INACTIVE);
				addrList.add(address);
			}
			getHibernateTemplate().saveOrUpdateAll(addrList);
			getHibernateTemplate().flush();
		}
	}

	/**
	 * @return List of states
	 * @TODO Get the states for states dropdown on returns page
	 */
	@SuppressWarnings("unchecked")
	public List<State> getStates() {
		// time();
		return (ArrayList<State>) getHibernateTemplate().find("from State");
	}
	public List<State> getStatesForVendorTax(){
		return (ArrayList<State>) getHibernateTemplate().find("from State s where s.stateId NOT IN " +
		"(Select DISTINCT(d.state.stateId) from " +
		" DropshipTaxState d where d.status = 'ACTIVE' )");
		
	}

	/**
	 * @param fulfillmentServiceID
	 * @return Fulfillment Service Return
	 * @TODO Get return for fulfillment service
	 */
	@SuppressWarnings("unchecked")
	public FulfillmentServiceVendorReturn getFulfillmentServReturns(long fulfillmentServiceID) {
		// time();
		StringBuffer sqlB = new StringBuffer("from FulfillmentServiceVendorReturn vr ");
		ArrayList<Object> values = new ArrayList<Object>();
		sqlB.append("WHERE vr.fulfillmentServId=? AND vr.vendorID IS NULL");
		values.add(fulfillmentServiceID);

		List<FulfillmentServiceVendorReturn> vendorReturnList = getHibernateTemplate().find(
				sqlB.toString(), values.toArray());
		FulfillmentServiceVendorReturn vendorReturn = null;
		if (vendorReturnList != null && !vendorReturnList.isEmpty()) {
			vendorReturn = vendorReturnList.get(0);
		}

		return vendorReturn;
	}

	/**
	 * @param cls -Class
	 * @param id - ID of the class
	 * @return - Object
	 * @TODO
	 */
	@SuppressWarnings("unchecked")
	public Object getById(Class cls, Serializable id) {
		return this.getHibernateTemplate().get(cls, id);
	}

	/**
	 * @return List of invoice methods
	 * @TODO get List of invoice methods
	 */
	@SuppressWarnings("unchecked")
	public List<InvoiceMethods> getInvoiceMethods() {

		return (ArrayList<InvoiceMethods>) getHibernateTemplate().find("from InvoiceMethods");
	}

	/**
	 * @return List of DropshipVendorActiveStyleSkuDTO
	 * @param serviceId- Fulfillment Service IDs
	 * @TODO Get active styles and skus for vendors.
	 */
	@SuppressWarnings("unchecked")
	public List<DropshipVendorActiveStyleSkuDTO> getActiveStyleSkusForVendors(Long serviceId) {

		SessionFactory sessionFactory = getHibernateTemplate().getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		List<DropshipVendorActiveStyleSkuDTO> vendorList = new ArrayList<DropshipVendorActiveStyleSkuDTO>();

		StringBuffer queryBuf = new StringBuffer(
		"SELECT fv.FULFMNT_SERVICE_VENDOR_ID vendorFulfillmentId,Y.totalActiveStyles,Y.totalActiveSKUs FROM ")
		.append(" (SELECT DISTINCT FS.FULFMNT_SERVICE_NAME AS fullfilmentSeviceName, ")
		.append(" V.NAME AS vendorName ,V.VENDOR_NUMBER AS ")
		.append(" vendorNbr, nvl(T3.totalActiveSKUs,0)AS totalActiveSKUs,v.vendor_id,")
		.append("	 nvl(T3.totalActiveStyles,0)AS totalActiveStyles FROM ")
		.append("  (SELECT  T1.VENDOR_ID,totalActiveStyles,totalActiveSKUs ")
		.append("   FROM")
		.append("   (SELECT  vendor_id , Count(*) AS totalActiveStyles FROM (")
		.append(" SELECT  vendor_id,vendor_style_id  ")
		.append(" from VIFR_STYLE V1, VNDR_ITEM_FULFMNT_RQST V2") 
		.append("  WHERE  V1.VNDR_ITEM_FULFMNT_RQST_ID=V2.VNDR_ITEM_FULFMNT_RQST_ID") 
		.append("  AND STATUS_CD='ACTIVE' AND   v1.vndr_id=v2.vendor_id  ")
		.append("  group BY  vendor_id ,vendor_style_id")   
		.append("   ) ")
		.append(" GROUP BY vendor_id  )T1") 
		.append("	  ,  ")
		.append(" (SELECT vendor_id , Count(*) AS totalActiveSKUs   from ")
		.append(" (SELECT VENDOR_ID,belk_upc")
		.append(" FROM  VIFR_STYLE_SKU V1,VNDR_ITEM_FULFMNT_RQST V2")
		.append(" WHERE v1.VNDR_ITEM_FULFMNT_RQST_ID=V2.VNDR_ITEM_FULFMNT_RQST_ID") 
		.append(" AND STATUS_CD ='ACTIVE'  GROUP BY V2.VENDOR_ID , belk_upc)")
		.append(" group BY vendor_id")
		.append(" )T2 ")
		.append("	 WHERE T1.vendor_id = T2.vendor_id(+) ) T3 ,")
		.append(
		"	 VNDR_ITEM_FULFMNT_RQST VIFR, FULFMNT_METHOD FM, FULFMNT_SERVICE FS , VENDOR V   ,FULFMNT_SERVICE_VENDOR FV")
		.append("	WHERE T3.VENDOR_ID = VIFR.VENDOR_ID  ").append(
		"	AND VIFR.VENDOR_ID = V.VENDOR_ID ").append(
		"	 AND FS.FULFMNT_SERVICE_ID=VIFR.FULFMNT_SERVICE_ID ").append(
		"  AND FS.FULFMNT_METHOD_CD =  FM.FULFMNT_METHOD_CD ").append(
		"	 and FS.FULFMNT_SERVICE_ID=:serviceId").append(
		" ORDER BY fullfilmentSeviceName").append(
		"	)Y   inner join fulfmnt_service_vendor fv").append(
		"	ON fv.vendor_id=Y.vendor_id").append(" AND fv. FULFMNT_SERVICE_ID=:serviceId");

		if (log.isInfoEnabled()) {
			log.info("Find active style/skus===>" + queryBuf.toString());
		}

		SQLQuery sQuery = (SQLQuery) session.createSQLQuery(queryBuf.toString());
		sQuery.addScalar("vendorFulfillmentId", new LongType()).addScalar("totalActiveSKUs",
				new LongType()).addScalar("totalActiveStyles", new LongType())
				.setResultTransformer(
						Transformers.aliasToBean(DropshipVendorActiveStyleSkuDTO.class));
		sQuery.setLong("serviceId", serviceId);
		vendorList = sQuery.list();
		return vendorList;

	}

	
	
		
    
    /*
     * 	(non-Javadoc)
     * @see com.belk.car.app.dao.VendorFulfillmentServiceDao#lockFulfillmntServiceVendor(java.lang.String, org.appfuse.model.User)
     * lock the fulfillment service vendor
     */
    
    public void lockFulfillmntServiceVendor(String vndrFulfillmentServId,User user){
	      if(log.isDebugEnabled()) {
	    	  	log.debug("Start 'lockFulfillmntServiceVendor' method vndrFulfillmentServId:"+vndrFulfillmentServId +" Username:"+user.getUsername());
     	}
		 	try {
		 		FulfillmentServiceVendor serviceVendor = (FulfillmentServiceVendor) getHibernateTemplate().get(FulfillmentServiceVendor.class, 
						Long.parseLong(vndrFulfillmentServId));
		 		serviceVendor.setIsLocked("Y");
		 		serviceVendor.setLockedBy(user.getUsername());
		 		serviceVendor.setAuditInfo(user);
				this.getHibernateTemplate().saveOrUpdate(serviceVendor);
				getHibernateTemplate().flush();
			} catch(Exception e) {
			   log.error("Hibdernate Exception in lockFulfillmntServiceVendor -" + e)	;
			}
		     if(log.isDebugEnabled()) {
		        	 log.debug("End 'lockFulfillmntServiceVendor' method");
	        }
    }
	
	/*
	 * 		(non-Javadoc)
	 * @see com.belk.car.app.dao.VendorFulfillmentServiceDao#unlockFulfillmntServiceVendor(java.lang.String)
	 * unlock the fulfillment service vendor
	 */
    public void unlockFulfillmntServiceVendor(String userName){
		 try{
			   SessionFactory sf = getHibernateTemplate().getSessionFactory();
			   Session session = sf.getCurrentSession();
			   String query = "UPDATE FULFMNT_SERVICE_VENDOR SET LOCKED_BY=NULL , IS_LOCKED=NULL WHERE LOCKED_BY= :USER";
			   int updateRows =  session.createSQLQuery(query).setString("USER", userName).executeUpdate();
			   log.info(updateRows+" FulfillmntServiceVendor Unlocked for the user:"+userName);
    }catch(Exception e){
    	log.error("Got exception while unlockFulfillmntServiceVendor all the vendors : " + e.getMessage());
    }
		
	}
}
