/**
 * 
 */

package com.belk.car.app.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.appfuse.model.User;
import org.springframework.dao.DataAccessException;

import com.belk.car.app.model.NotificationType;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.oma.Address;
import com.belk.car.app.model.oma.CompositeKeyVFIRStylesku;
import com.belk.car.app.model.oma.FulfillmentIntegrationType;
import com.belk.car.app.model.oma.FulfillmentMethod;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.ItemRequest;
import com.belk.car.app.model.oma.ItemRequestWorkflow;
import com.belk.car.app.model.oma.ItemSource;
import com.belk.car.app.model.oma.State;
import com.belk.car.app.model.oma.StylePopulationMethod;
import com.belk.car.app.model.oma.VFIRStatus;
import com.belk.car.app.model.oma.VFIRStyle;
import com.belk.car.app.model.oma.VFIRStyleSku;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.to.IdbDropshipDataTO;

/**
 * Method declarations to be used in DaoHibernate layer
 */
public interface FulfillmentServiceDao {

	public FulfillmentService getFulfillmentServiceDetails(Long serviceID);

	public List<FulfillmentService> getFulfillmentServiceList();
	
	public List<FulfillmentService> getFulfillmentServiceList(String status);

	public void updateFulfillmentService(FulfillmentService fulfillmentService,Address addr);

	public List<FulfillmentService> searchFS(String fserviceName, String status);

	public List<FulfillmentIntegrationType> getIntegrationTypes();

	public List<FulfillmentMethod> getFulfillmentMethods();
	
	public List<State> getStates(); 

	public FulfillmentMethod getFulfillmentMethodById(String fmId);

	public FulfillmentIntegrationType getIntTypesById(String intId);

	public FulfillmentService getFulfillmentServiceId(String name);

	public Address getAddrByID(long addrID);

	public void saveUpdateAddress(Address addr);

	/**
	 * This method gets all the available styles population methods for item
	 * request.
	 * 
	 * @return List <StylePopulationMethod>
	 * @throws DataAccessException
	 */
	public List<StylePopulationMethod> getStylePopulationMethods()
			throws Exception;

	/**
	 * Method to fetch all the available item sources
	 * 
	 * @return List <ItemSource>
	 * @throws Exception
	 */
	public List<ItemSource> getItemSource()
			throws Exception;

	/**
	 * Method to save the Item request record
	 * 
	 * @param itemRequest ItemRequest
	 * @throws Exception
	 */
	public void saveItemRequest(ItemRequest itemRequest)
			throws Exception;

	/**
	 * Method to update the Item request record
	 * 
	 * @param itemRequest ItemRequest
	 * @throws Exception
	 */
	public void updateItemRequest(ItemRequest itemRequest)
			throws Exception;

	/**
	 * Method gets the fulfillment service for a given request
	 * 
	 * @param id Long
	 * @return
	 * @throws Exception
	 */
	public FulfillmentService getFulfillmentServiceForItemRequest(Long id)
			throws Exception;

	/**
	 * Method gets the vendor for a given request
	 * 
	 * @param vendorNumber String
	 * @return Vendor
	 * @throws Exception
	 */
	public Vendor getVendorForItemRequest(String vendorNumber)
			throws Exception;

	/**
	 * Method to get style population method based on its description
	 * 
	 * @param desc String
	 * @return StylePopulationMethod
	 * @throws Exception
	 */
	public StylePopulationMethod getStyleForItemRequest(String name)
			throws Exception;

	/**
	 * Method to get source based on its description.
	 * 
	 * @param desc String
	 * @return ItemSource
	 * @throws Exception
	 */
	public ItemSource getSourceForItemRequest(String desc)
			throws Exception;

	/**
	 * Method to fetch details of an item request
	 * 
	 * @param id Long
	 * @return ItemRequest
	 * @throws Exception
	 */
	public ItemRequest getItemRequestDetails(Long id)
			throws Exception;

	/**
	 * Method to get status based on the name.
	 * 
	 * @param name String
	 * @return VFIRStatus
	 * @throws Exception
	 */
	public VFIRStatus getStatusForItemRequest(String name)
			throws Exception;

	/**
	 * Method for validating whether the request id exists
	 * 
	 * @param id Long
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isValidRequestId(Long id, String vendorNo, Long serviceId)
			throws Exception;

	/**
	 * This method gets the directory for uploading the item request styles file
	 * 
	 * @param cls Class
	 * @param id Serializable
	 * @return Object
	 * @throws Exception
	 */
	public Object getById(Class cls, Serializable id)
			throws Exception;

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
			throws SQLException;

	/**
	 * This method inserts the styles into VFIR_Style table.
	 * 
	 * @param list
	 * @throws Exception
	 */
	public void uploadStyles(List<VFIRStyle> list)
			throws Exception;

	/**
	 * This method fetches all the styles associated with a request.
	 * 
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public List<VFIRStyle> getRequestStyles(Long requestId)
			throws Exception;

	/**
	 * This method fetches all the styles belonging to the departments of a given user.
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Object getStylesForUserDepartment(Long id)
			throws Exception;

	/**
	 * This method fetches all the styles configured for vendor dropship for a given vendor & fulfillment service pair.
	 * @param vendorNo
	 * @return
	 * @throws Exception
	 */
	public Object getStylesFromDropship(String vendorNo)
			throws Exception;

	/**
	 * This method fetches a style object by its style id & request id
	 * @param vendorStyleId
	 * @param itemRequest
	 * @return
	 * @throws Exception
	 */
	public Object getVFIRStyleById(String vendorStyleId, ItemRequest itemRequest)
			throws Exception;

	/**
	 * This method changes the status of a given style to 'INACTIVE'
	 * @param style
	 * @throws Exception
	 */
	public void removeStyle(VFIRStyle style)
			throws Exception;

	/**
	 * This method is to save a record in vifr_workflow_hist table
	 * @param itemRequestWorkflow
	 * @throws Exception
	 */
	public void saveItemRequestWorkflow(ItemRequestWorkflow itemRequestWorkflow)
			throws Exception;

	/**
	 * This method fetches "Reject reason" for a given request.
	 * @param itemRequest
	 * @return
	 * @throws Exception
	 */
	public ItemRequestWorkflow getRequestRejectReason(ItemRequest itemRequest)
			throws Exception;


	/**
	 * This method fecthes all the skus of a given request.
	 * @param itemRequest
	 * @return
	 * @throws Exception
	 */
	public List<VFIRStyleSku> getStyleSkusForItemRequest(ItemRequest itemRequest)
			throws Exception;

	/**
	 * This method fecthes all the styles in a given request.
	 * @param itemRequest
	 * @return
	 * @throws Exception
	 */
	public List<VFIRStyle> getStylesForItemRequest(ItemRequest itemRequest)
			throws Exception;

	/**
	 * This method saves/updates set of skus
	 * @param list
	 * @throws Exception
	 */
	public void saveStyleSkus(List<VFIRStyleSku> list)
			throws Exception;

	/**
	 * This method fetches a style by style id
	 * @param styleId
	 * @return
	 * @throws Exception
	 */
	public IdbDropshipDataTO getIDBRecordForStyleId(String styleId)
			throws Exception;

	/**
	 * This method fetches set of style objects for a given set of style ids
	 * @param styles
	 * @return
	 * @throws Exception
	 */
	public List<IdbDropshipDataTO> getIDBRecordsForStyles(String styles)
			throws Exception;

	/**
	 * This method fetches a set of skus for a given style in a given request.
	 * @param styleId
	 * @param itemRequest
	 * @return
	 * @throws Exception
	 */
	public List<VFIRStyleSku> getSkusForStyle(
			String styleId, ItemRequest itemRequest)
			throws Exception;

	/**
	 * This method fetches sku object
	 * @param styleSku
	 * @param keyVFIRStylesku
	 * @return
	 */
	public List<VFIRStyleSku> fetchSkuExample(VFIRStyleSku styleSku, CompositeKeyVFIRStylesku keyVFIRStylesku);

	/**
	 * This method executes a sql query which will return more than one row and columns.
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> fetchDetails(String query)
			throws Exception;
	
	/**
	 * This method executes a sql query which will return one column.
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public Object fetchCount(String query) throws Exception;
	
	/**
	 * This method returns a sku object by its primary key
	 * @param compositeKeyVFIRStylesku
	 * @return
	 * @throws Exception
	 */
	public List<VFIRStyleSku> getSkuFromId(CompositeKeyVFIRStylesku compositeKeyVFIRStylesku) throws Exception;
	
	/**
	 * This method executes a update sql query.
	 * @param query
	 * @throws Exception
	 */
	public void updateQuery(String query) throws Exception;
	
	/**
	 * This method fecthes notification type of object by its ID
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Object getNotificationTypeById(String id) throws Exception;
	
	/**
	 * This method fetches a vendor fulfillment service object
	 * @param serviceId
	 * @param vendor
	 * @return
	 * @throws Exception
	 */
	public FulfillmentServiceVendor getVendorFSModel(Long serviceId, Vendor vendor) throws Exception;
	
	public List<User> fetchUsersByType(String userTypeCode) throws Exception;
	
	public List<Object[]> fetchDistinctColorSkuList(String vendorStyleId, Long requestId) throws Exception;
	
	public List<Object[]> fetchDistinctSizeSkuList(String vendorStyleId, Long requestId) throws Exception;
}
