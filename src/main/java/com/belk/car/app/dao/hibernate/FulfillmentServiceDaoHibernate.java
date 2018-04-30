/**
 * 
 */

package com.belk.car.app.dao.hibernate;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.appfuse.model.User;
import org.hibernate.FlushMode;
import org.hibernate.LockMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.dao.FulfillmentServiceDao;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.oma.Address;
import com.belk.car.app.model.oma.CompositeKeyVFIRStylesku;
import com.belk.car.app.model.oma.FulfillmentIntegrationType;
import com.belk.car.app.model.oma.FulfillmentMethod;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.model.oma.ItemRequest;
import com.belk.car.app.model.oma.ItemRequestWorkflow;
import com.belk.car.app.model.oma.ItemSource;
import com.belk.car.app.model.oma.State;
import com.belk.car.app.model.oma.StylePopulationMethod;
import com.belk.car.app.model.oma.VFIRStatus;
import com.belk.car.app.model.oma.VFIRStyle;
import com.belk.car.app.model.oma.VFIRStyleSku;
import com.belk.car.app.to.IdbDropshipDataTO;

/**
 * @author afusy45
 * Implementation of FulfillmentServiceDao interface
 */
@SuppressWarnings("unchecked")
public class FulfillmentServiceDaoHibernate extends CachedQueryDaoHibernate
implements
FulfillmentServiceDao,DropShipConstants {

	private static final String ADDRESS_STATUS_ACTIVE = "ACTIVE";

	public void addFulfillmentService() {

	}

	/**
	 * Method to return object of type Fulfillment Method--To be used in
	 * managerImpl class to set values for fulfillment methods from form to
	 * model(non-Javadoc)
	 * @return FulfillmentMethod object
	 * @param fmId
	 * @see com.belk.car.app.dao.FulfillmentServiceDao#getFulfillmentMethodById(java.lang.Long)
	 */
	public FulfillmentMethod getFulfillmentMethodById(String fmId) {
		FulfillmentMethod Fmethod = new FulfillmentMethod();

		Fmethod = (FulfillmentMethod) getHibernateTemplate().get(
				FulfillmentMethod.class, fmId);
		return Fmethod;
	}

	/**
	 * Method to return object of type FulfillmentIntegrationType --To be used
	 * in managerImpl class to set values for integration types from form to
	 * model
	 * @return FulfillmentIntegrationType object
	 * @param intId
	 */
	public FulfillmentIntegrationType getIntTypesById(String intId) {
		FulfillmentIntegrationType intType = new FulfillmentIntegrationType();

		intType = (FulfillmentIntegrationType) getHibernateTemplate().get(
				FulfillmentIntegrationType.class, intId);

		return intType;
	}

	/**
	 * Saves/Updates address
	 * @param addr
	 */
	public void saveUpdateAddress(Address addr) {
		addr.setStatus(ADDRESS_STATUS_ACTIVE);

		if (addr.getAddressID() != 0){
			getHibernateTemplate().saveOrUpdate(addr);
		}else{
			getHibernateTemplate().save(addr);
		}
		getHibernateTemplate().flush();
	}

	/**
	 * Get the address information by ID
	 * @return Address object
	 * @param addrID
	 */
	public Address getAddrByID(long addrID) {
		List<Address> list = getHibernateTemplate().find(
				"FROM Address WHERE addressID = ?", addrID);
		Address addr=null;
		if (null!=list && !list.isEmpty()) {
			addr= list.get(0);
		}

		return addr;
	}

	/**
	 * Get fulfillment service details by passing name
	 * @return FulfillmentService object
	 * @param name
	 */
	public FulfillmentService getFulfillmentServiceId(String name) {
		List<FulfillmentService> list = getHibernateTemplate().find(
				"FROM FulfillmentService WHERE fulfillmentServiceName = ?",
				name);
		FulfillmentService fulfillmentService=null;
		if (null!=list && !list.isEmpty()) {
			fulfillmentService =list.get(0);
		}
		return fulfillmentService;
	}

	/**
	 * Get fulfillment service details by ID
	 * @return FulfillmentService object
	 * @param serviceID
	 */
	public FulfillmentService getFulfillmentServiceDetails(Long serviceID) {
		List<FulfillmentService> list = getHibernateTemplate().find(
				"FROM FulfillmentService WHERE fulfillmentServiceID = ?",
				serviceID);
		FulfillmentService fulfillmentService=null;
		if (null !=list && !list.isEmpty()) {
			fulfillmentService=list.get(0);
		}
		return fulfillmentService;

	}

	/**
	 * Returns list of all the fulfillment services from table
	 * @return List
	 */
	public List<FulfillmentService> getFulfillmentServiceList() {
		List<FulfillmentService> fulfillmentServiceList = new ArrayList<FulfillmentService>();
		fulfillmentServiceList = getHibernateTemplate().find(
		"FROM FulfillmentService");

		return fulfillmentServiceList;
	}

	/**
	 * Saves/Updates Fulfillment Service
	 * @exception DataAccessException
	 */
	public void updateFulfillmentService(FulfillmentService fulfillmentService,Address addr) {
		if(!("1".equals(fulfillmentService.getFulfillmentServiceIntTypeID()))){
			fulfillmentService.setDefaultReturnAddID(null);
		}
		if (!(addr.getLocName().equalsIgnoreCase(EMPTY_STRING))) {
			addr.setCreatedBy(fulfillmentService.getCreatedBy());
			addr.setCreatedDate(fulfillmentService.getCreatedDate());
			addr.setUpdatedBy(fulfillmentService.getUpdatedBy());
			addr.setUpdatedDate(fulfillmentService.getUpdatedDate());
			/* Insert/Update address entered by user in ADDRESS table */
			saveUpdateAddress(addr);
			fulfillmentService.setDefaultReturnAddID(addr.getAddressID());
		}

		getHibernateTemplate().saveOrUpdate(fulfillmentService);
		getHibernateTemplate().flush();
	}
	/**
	 * @author afusy45
	 * @param String
	 * @return List
	 */
	public List<FulfillmentService> getFulfillmentServiceList(String status){
		List<FulfillmentService> list = getHibernateTemplate().find(
				"FROM FulfillmentService WHERE fulfillmentServiceStatusCode = ?",
				status);
		return list;
	}
	/**
	 * Returns list of all fulfillment Methods
	 * @return List
	 */

	public List<FulfillmentMethod> getFulfillmentMethods() {
		List<FulfillmentMethod> list = getHibernateTemplate().find(
		"FROM FulfillmentMethod");
		return list;
	}

	/**
	 * Returns list of all States
	 * @return List
	 */
	public List<State> getStates(){
		List<State> list = getHibernateTemplate().find(
		"FROM State");
		return list;
	}

	/**
	 * Returns details of the details of the service name/status passed as
	 * parameter
	 * @return List
	 * @param fserviceName,status
	 * @see com.belk.car.app.dao.FulfillmentServiceDao#searchFS(java.lang.String)
	 */
	public List<FulfillmentService> searchFS(String fserviceName, String status) {
		log.info("Inside method to search fulfillment service");
		ArrayList<Object> values = new ArrayList<Object>();
		StringBuffer stringBuffer = new StringBuffer("from FulfillmentService fs ");

		if (fserviceName != null && !(fserviceName.equals(""))) {
			String[] serviceName = fserviceName.split(" ");
			stringBuffer.append("where upper(fs.fulfillmentServiceName) like '");
			for(int s=0;s<serviceName.length;s++){
				stringBuffer.append("%");
				stringBuffer.append(serviceName[s].toUpperCase()).append("%");
			}
			stringBuffer.append("'");

		}

		if (!"".equals(status) && !"ALL".equals(status.toUpperCase(Locale.US))) {
			if (fserviceName != null && !(fserviceName.equals(""))) {
				stringBuffer.append("AND ");
				stringBuffer.append(" upper(fs.fulfillmentServiceStatusCode)= '");
			}
			else {
				stringBuffer.append("where upper(fs.fulfillmentServiceStatusCode) = '");
			}
			stringBuffer.append(status.toUpperCase(Locale.US));
			stringBuffer.append("'");

		}


		stringBuffer.append(" order by fs.fulfillmentServiceID");
		return getHibernateTemplate().find(stringBuffer.toString(), values.toArray());

	}

	/**
	 * Returns list of all integration types
	 */
	public List<FulfillmentIntegrationType> getIntegrationTypes() {
		List<FulfillmentIntegrationType> list = getHibernateTemplate().find(
		"FROM FulfillmentIntegrationType");
		return list;
	}

	/**
	 * This method gets all the available styles population methods for item
	 * request.
	 * 
	 * @return List <StylePopulationMethod>
	 * @throws DataAccessException
	 */
	public List<StylePopulationMethod> getStylePopulationMethods()
	throws Exception {
		List<StylePopulationMethod> list = getHibernateTemplate().find(
		"FROM StylePopulationMethod");
		return list;
	}

	/**
	 * Method to fetch all the available item sources
	 * 
	 * @return List <ItemSource>
	 * @throws Exception
	 */
	public List<ItemSource> getItemSource()
	throws Exception {
		return getHibernateTemplate().find("FROM ItemSource");
	}

	/**
	 * Method to save the Item request record
	 * 
	 * @param itemRequest ItemRequest
	 * @throws Exception
	 */
	public void saveItemRequest(ItemRequest itemRequest)
	throws Exception {
		Session session = getHibernateTemplate().getSessionFactory()
		.getCurrentSession();
		session.setFlushMode(FlushMode.COMMIT);
		getHibernateTemplate().save(itemRequest);
        getHibernateTemplate().flush();
	}

	/**
	 * Method gets the fulfillment service for a given request
	 * 
	 * @param id Long
	 * @return
	 * @throws Exception
	 */
	public FulfillmentService getFulfillmentServiceForItemRequest(Long id)
	throws Exception {
		List<FulfillmentService> list = getHibernateTemplate().find(
				"FROM FulfillmentService WHERE fulfillmentServiceID = ?", id);
		FulfillmentService fulfillmentService=null;
		if (null != list && !list.isEmpty()) {
			fulfillmentService = list.get(0);

		}
		return fulfillmentService;
	}

	/**
	 * Method gets the vendor for a given request
	 * 
	 * @param vendorNumber String
	 * @return Vendor
	 * @throws Exception
	 */
	public Vendor getVendorForItemRequest(String vendorNumber)
	throws Exception {

		List<Vendor> list = getHibernateTemplate().find(
				"FROM Vendor WHERE vendorNumber = ?", vendorNumber);
		Vendor vendor=null;
		if (null != list && !list.isEmpty()) {
			vendor=list.get(0);

		}
		return vendor;
	}

	/**
	 * Method to get style population method based on its description
	 * 
	 * @param desc String
	 * @return StylePopulationMethod
	 * @throws Exception
	 */
	public StylePopulationMethod getStyleForItemRequest(String name)
	throws Exception {
		List<StylePopulationMethod> list = getHibernateTemplate().find(
				"FROM StylePopulationMethod WHERE name = ?", name);
		StylePopulationMethod population=null;
		if (null != list && !list.isEmpty()) {
			population=list.get(0);

		}
		return population;	
	}

	/**
	 * Method to get source based on its description.
	 * 
	 * @param desc String
	 * @return ItemSource
	 * @throws Exception
	 */
	public ItemSource getSourceForItemRequest(String desc)
	throws Exception {
		List<ItemSource> list = getHibernateTemplate().find(
				"FROM ItemSource WHERE name = ?", desc);
		ItemSource itemSrc=null;
		if (null != list && !list.isEmpty()) {
			itemSrc=list.get(0);

		}
		return itemSrc;
	}

	/**
	 * Method to fetch details of an item request
	 * 
	 * @param id Long
	 * @return ItemRequest
	 * @throws Exception
	 */
	public ItemRequest getItemRequestDetails(Long id)
	throws Exception {
		List<ItemRequest> list = getHibernateTemplate().find(
				"FROM ItemRequest WHERE itemRequestID = ?", id);
		ItemRequest item=null;
		if (null != list && !list.isEmpty()) {
			item=list.get(0);
		}
		return item;
	}

	/**
	 * Method to get status based on the name.
	 * 
	 * @param name String
	 * @return VFIRStatus
	 * @throws Exception
	 */
	public VFIRStatus getStatusForItemRequest(String name)
	throws Exception {
		List<VFIRStatus> list = getHibernateTemplate().find(
				"FROM VFIRStatus WHERE vfirStatusCode = ?", name);
		VFIRStatus status=null;
		if (null != list && !list.isEmpty()) {
			status=list.get(0);
		}
		return status;
	}

	/**
	 * Method to update the Item request record
	 * 
	 * @param itemRequest ItemRequest
	 * @throws Exception
	 */
	public void updateItemRequest(ItemRequest itemRequest)
	throws Exception {
		getHibernateTemplate().update(itemRequest);
	}

	/**
	 * Method for validating whether the request id exists
	 * 
	 * @param id Long
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isValidRequestId(Long id, String vendorNo, Long serviceId)
	throws Exception {

		Vendor vendor = (Vendor) this.getVendorForItemRequest(vendorNo);
		FulfillmentService fulfillmentService = (FulfillmentService) getById(
				FulfillmentService.class, serviceId);
		List<ItemRequest> list = getHibernateTemplate()
		.find(
				"FROM ItemRequest WHERE itemRequestID = ? AND vendor=? AND fulfillmentService=?",
				new Object[] { id, vendor, fulfillmentService });
		boolean flag=false;
		if (null != list && !list.isEmpty()) {
			flag=true;
		}
		return flag;
	}

	/**
	 * This method gets the directory for uploading the item request styles file
	 * 
	 * @param cls Class
	 * @param id Serializable
	 * @return Object
	 * @throws Exception
	 */
	public Object getById(Class cls, Serializable id)
	throws Exception {
		return this.getHibernateTemplate().get(cls, id, LockMode.UPGRADE);
	}

	/**
	 * Method will get the number of styles which match with the records in
	 * IDB_FEED table.
	 * 
	 * @param vendorStyles String
	 * @param upc String
	 * @param vendorId Long
	 * @return Object
	 * @throws SQLException
	 */
	public Object getNumberOfUploadedStyles(
			String vendorStyles, String upc, Long vendorId)
	throws SQLException {
		Session session = getHibernateTemplate().getSessionFactory()
		.getCurrentSession();
		CallableStatement callableStatement = session.connection().prepareCall(
		"{CALL SEARCH_STYLE_IN_IDB(?,?,?,?)}");
		callableStatement.setString(1, vendorStyles);
		callableStatement.setString(2, upc);
		callableStatement.setLong(3, vendorId);
		callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);
		callableStatement.execute();

		return callableStatement.getString(4);

	}

	/**
	 * Updated the styles in database
	 */
	public void uploadStyles(List<VFIRStyle> list)
	throws Exception {
		this.getHibernateTemplate().saveOrUpdateAll(list);
		getHibernateTemplate().flush();
	}

	/**
	 * Gets the requested styles
	 */
	public List<VFIRStyle> getRequestStyles(Long requestId)
	throws Exception {
		ItemRequest itemRequest = this.getItemRequestDetails(requestId);
		List<VFIRStyle> list = getHibernateTemplate().find(
				"FROM VFIRStyle WHERE compositeKeyVIFRStyle.itemRequest=?", itemRequest);
		return list;
	}

	/**
	 * Gets the styles for user department
	 */
	public Object getStylesForUserDepartment(Long id) throws Exception {
		Session session = getHibernateTemplate().getSessionFactory()
		.getCurrentSession();
		String query = "SELECT DISTINCT vendor_sytle_id FROM IDB_DROP_SHIP_FEED WHERE style_drop_ship_flag='Y' " +
		"AND DEPT_ID IN(SELECT D.DEPT_CD FROM CAR_USER_DEPARTMENT C JOIN DEPARTMENT D " +
		"ON (C.DEPT_ID=D.DEPT_ID) WHERE C.CAR_USER_ID=:ID)";
		
		SQLQuery query2 = session.createSQLQuery(query);
		query2.setLong("ID", id);
		return query2.list();

	}

	/**
	 * 
	 */
	public Object getVFIRStyleById(String vendorStyleId, ItemRequest itemRequest)
	throws Exception {
		List<VFIRStyle> list = getHibernateTemplate()
		.find(
				"FROM VFIRStyle WHERE compositeKeyVIFRStyle.vendorStyleId=? AND compositeKeyVIFRStyle.itemRequest=?",
				new Object[] { vendorStyleId, itemRequest });
		Object object=null;
		if (null != list && !list.isEmpty()) {
			object=list.get(0);
		}
		return object;
	}

	/**
	 * 
	 */
	public void removeStyle(VFIRStyle style)
	throws Exception {
		style.setStatusCode(VFIRStyle.INACTIVE);
		getHibernateTemplate().update(style);

		/**Updating the status of the skus for that style*/
		List<VFIRStyleSku> list = this.getSkusForStyle(style
				.getCompositeKeyVIFRStyle().getVendorStyleId(), style
				.getCompositeKeyVIFRStyle().getItemRequest());
		Iterator<VFIRStyleSku> iterator = list.iterator();
		while (iterator.hasNext()) {
			VFIRStyleSku styleSku2 = iterator.next();
			styleSku2.setStatusCode(VFIRStyleSku.INACTIVE);
		}
		this.saveStyleSkus(list);
	}

	/**
	 * Saves Item Request Workflow
	 */
	public void saveItemRequestWorkflow(ItemRequestWorkflow itemRequestWorkflow)
	throws Exception {
		
		getHibernateTemplate().save(itemRequestWorkflow);
		getHibernateTemplate().flush();
	}

	/**
	 * Gets the rejection reason of the request
	 */
	public ItemRequestWorkflow getRequestRejectReason(ItemRequest itemRequest)
	throws Exception {
		List<ItemRequestWorkflow> list = getHibernateTemplate()
		.find(
				"FROM ItemRequestWorkflow WHERE itemRequest=? AND workflowStatus=?",
				new Object[] { itemRequest,
						itemRequest.getWorkflowStatus() });
		ItemRequestWorkflow workFlow=null;
		if (null != list && !list.isEmpty()) {
			workFlow=list.get(0);
		}
		return workFlow;
	}

	/**
	 * Gets styles for Item Request
	 */
	public List<VFIRStyle> getStylesForItemRequest(ItemRequest itemRequest)
	throws Exception {
		List<VFIRStyle> list = getHibernateTemplate().find(
				"FROM VFIRStyle WHERE statusCode='ACTIVE' AND compositeKeyVIFRStyle.itemRequest=?",
				itemRequest);
		return list;
	}

	/**
	 * Gets Styles SKUs for Item Request
	 */
	public List<VFIRStyleSku> getStyleSkusForItemRequest(ItemRequest itemRequest)
	throws Exception {
		List<VFIRStyleSku> list = getHibernateTemplate()
		.find(
				"FROM VFIRStyleSku WHERE statusCode='ACTIVE' AND compositeKeyVFIRStylesku.itemRequest=?",
				itemRequest);
		return list;
	}

	/**
	 * Saves the styles and skus.
	 */
	public void saveStyleSkus(List<VFIRStyleSku> list)
	throws Exception {
		getHibernateTemplate().saveOrUpdateAll(list);
		getHibernateTemplate().flush();
	}

	/**
	 * Gets styles form Drop ship
	 */
	public Object getStylesFromDropship(String vendorNo)
	throws Exception {
		Session session = getHibernateTemplate().getSessionFactory()
		.getCurrentSession();
		String query = "SELECT DISTINCT vendor_sytle_id FROM IDB_DROP_SHIP_FEED WHERE style_drop_ship_flag='Y' AND vendor_number=:VEND_NUM";
		SQLQuery query2 = session.createSQLQuery(query);
		query2.setString("VEND_NUM", vendorNo);
		
		return query2.list();

	}

	/**
	 * Gets IDB record for style ID.
	 */
	public IdbDropshipDataTO getIDBRecordForStyleId(String styleId)
	throws Exception {
		List<IdbDropshipDataTO> list = getHibernateTemplate().find(
				"FROM IdbDropshipDataTO WHERE vendorStyleID=? AND styleDropshipFlag IN ('Y','V')", styleId);
		IdbDropshipDataTO data=null;
		if (null != list && !list.isEmpty()) {
			data=list.get(0);
		}
		return data;
	}

	/**
	 * Gets IDB Records for Styles.
	 */
	public List<IdbDropshipDataTO> getIDBRecordsForStyles(String styles)
	throws Exception {
		String query = "FROM IdbDropshipDataTO WHERE vendorStyleID IN("
			+ styles + ")";
		return getHibernateTemplate().find(query);

	}

	/**
	 * Gets SKUs for given style
	 */
	public List<VFIRStyleSku> getSkusForStyle(
			String styleId, ItemRequest itemRequest)
			throws Exception {
		return  getHibernateTemplate()
		.find(
				"FROM VFIRStyleSku WHERE statusCode='ACTIVE' AND compositeKeyVFIRStylesku.itemRequest=? " +
				"AND compositeKeyVFIRStylesku.vendorStyleId=?",
				new Object[] { itemRequest, styleId });

	}

	/**
	 * Gets the SKU
	 */
	public List<VFIRStyleSku> getSkuFromId(
			CompositeKeyVFIRStylesku compositeKeyVFIRStylesku) throws Exception {
		return getHibernateTemplate()
		.find(
				"FROM VFIRStyleSku WHERE compositeKeyVFIRStylesku.belkUpc=? " +
				"AND compositeKeyVFIRStylesku.itemRequest=? AND compositeKeyVFIRStylesku.vendorStyleId=?",
				new Object[] { compositeKeyVFIRStylesku.getBelkUpc(), compositeKeyVFIRStylesku.getItemRequest(), compositeKeyVFIRStylesku.getVendorStyleId() });

	}
	/**
	 * Fetches the SKU example
	 */
	public List<VFIRStyleSku> fetchSkuExample(VFIRStyleSku styleSku,
			CompositeKeyVFIRStylesku keyVFIRStylesku) {
		List<VFIRStyleSku> list = null;
		
		Example example = Example.create(styleSku);
		if (null == keyVFIRStylesku.getBelkUpc()) {
			list = getHibernateTemplate().getSessionFactory()
					.getCurrentSession().createCriteria(VFIRStyleSku.class)
					.add(example).add(
							Restrictions.eq(
									"compositeKeyVFIRStylesku.itemRequest",
									keyVFIRStylesku.getItemRequest())).add(
							Restrictions.eq(
									"compositeKeyVFIRStylesku.vendorStyleId",
									keyVFIRStylesku.getVendorStyleId())).list();
		} else {
			list = getHibernateTemplate().getSessionFactory()
			.getCurrentSession().createCriteria(VFIRStyleSku.class)
			.add(example).add(
					Restrictions.eq(
							"compositeKeyVFIRStylesku.itemRequest",
							keyVFIRStylesku.getItemRequest())).add(
					Restrictions.eq(
							"compositeKeyVFIRStylesku.vendorStyleId",
							keyVFIRStylesku.getVendorStyleId())).add(
					Restrictions.eq(
							"compositeKeyVFIRStylesku.belkUpc",
							keyVFIRStylesku.getBelkUpc())).list();
		}
		return list;
	}

	/**
	 * Fetches the details
	 */
	public List<Object[]> fetchDetails(String query) throws Exception {
		Session session = getHibernateTemplate().getSessionFactory()
		.getCurrentSession();
		return session.createSQLQuery(query).list();

	}

	public Object fetchCount(String query) throws Exception {
		Session session = getHibernateTemplate().getSessionFactory()
		.getCurrentSession();
		return session.createSQLQuery(query).list();

	}

	/**
	 * Executes the update query.
	 */
	public void updateQuery(String query) throws Exception {
		Session session = getHibernateTemplate().getSessionFactory()
		.getCurrentSession();
		SQLQuery query2 = session.createSQLQuery(query);
		query2.executeUpdate();
	}

	public Object getNotificationTypeById(String id) throws Exception {
		List<NotificationType> list = getHibernateTemplate().find("FROM NotificationType WHERE code=?", id);
		Object obj=null;
		if(null != list && !list.isEmpty()) {
			obj=list.get(0);
		}
		return obj;
	}

	/**
	 * Gets the Vendor Fulfillment Service Model
	 */
	public FulfillmentServiceVendor getVendorFSModel(
			Long serviceId, Vendor vendor) throws Exception {
		List<FulfillmentServiceVendor> list = getHibernateTemplate()
		.find(
				"FROM FulfillmentServiceVendor WHERE fulfillmentServId=? AND vndr=?",
				new Object[] { serviceId, vendor });
		FulfillmentServiceVendor vendor1=null;
		if(null != list && !list.isEmpty()) {
			vendor1=list.get(0);
		}
		return vendor1;
	}

	/**
	 * Fetches the user by type.
	 */
	public List<User> fetchUsersByType(String userTypeCode) throws Exception {
		UserType userType = (UserType) this.get(UserType.class, userTypeCode);
		return getHibernateTemplate().find("FROM User WHERE userType=?", userType);

	}
	/**
	 * Fetches distinct color SKU list
	 */
	public List<Object[]> fetchDistinctColorSkuList(String vendorStyleId,
			Long requestId) throws Exception {
		StringBuffer query = new StringBuffer();
		query.append("SELECT * ")
			 .append("FROM (")
			 .append("SELECT COLOR,'ALL',BELK_UPC,SKU_EXC_IND,UNIT_COST,UNIT_HANDLING_FEE,")
			 .append("       OVERRIDE_COST,OVERRIDE_HANDLING_FEE,")
			 .append("       RANK() OVER (PARTITION BY COLOR ORDER BY COLOR,BELK_UPC) RANK ")
			 .append("FROM   VIFR_STYLE_SKU VSS ")
			 .append("WHERE  VSS.VNDR_ITEM_FULFMNT_RQST_ID = ").append(requestId)
			 .append("       AND VSS.VENDOR_STYLE_ID='").append(vendorStyleId).append("' ")
			 .append("       AND STATUS_CD ='ACTIVE' ")
			 .append("ORDER BY COLOR ) ")
			 .append("WHERE 1=1 AND RANK=1");
		return fetchDetails(query.toString());
	}

	/**
	 * Fetches distinct size SKU List
	 */
	public List<Object[]> fetchDistinctSizeSkuList(String vendorStyleId,
			Long requestId) throws Exception {
		StringBuffer query = new StringBuffer();
		query.append("SELECT * ")
			 .append("FROM (")
			 .append("SELECT SIZE_DESCR,'ALL',BELK_UPC,SKU_EXC_IND,UNIT_COST,UNIT_HANDLING_FEE,")
			 .append("       OVERRIDE_COST,OVERRIDE_HANDLING_FEE,")
			 .append("       RANK() OVER (PARTITION BY SIZE_DESCR ORDER BY SIZE_DESCR,BELK_UPC) RANK ")
			 .append("FROM   VIFR_STYLE_SKU VSS ")
			 .append("WHERE  VSS.VNDR_ITEM_FULFMNT_RQST_ID = ").append(requestId)
			 .append("       AND VSS.VENDOR_STYLE_ID='").append(vendorStyleId).append("' ")
			 .append("       AND STATUS_CD ='ACTIVE' ")
			 .append("ORDER BY SIZE_DESCR ) ")
			 .append("WHERE 1=1 AND RANK=1");
 		return fetchDetails(query.toString());
	}
}
