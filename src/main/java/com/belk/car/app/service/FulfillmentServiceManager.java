/**
 * 
 */
package com.belk.car.app.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.appfuse.service.UniversalManager;

import com.belk.car.app.model.oma.Address;
import com.belk.car.app.model.oma.FulfillmentIntegrationType;
import com.belk.car.app.model.oma.FulfillmentMethod;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.ItemSource;
import com.belk.car.app.model.oma.State;
import com.belk.car.app.model.oma.StylePopulationMethod;
import com.belk.car.app.model.oma.VFIRStyle;
import com.belk.car.app.webapp.forms.FulfillmentServiceForm;
import com.belk.car.app.webapp.forms.ItemRequestForm;
import com.belk.car.app.webapp.forms.RequestHistoryForm;
import com.belk.car.app.webapp.forms.StylesSkuForm;

/**
 * @author 
 * Service Layer with method declarations
 *
 */
public interface FulfillmentServiceManager extends UniversalManager {

	public List<FulfillmentService> getFulfillmentServices(); 
	
	public List<FulfillmentService> getFulfillmentServices(String status); 
	
	public FulfillmentService getFulfillmentServiceDetails (Long serviceID);
	
	public FulfillmentService updateFulfillmentService(FulfillmentServiceForm fulfillmentServiceForm, String mode);
	
	public Address getAddrByID(long addrID);
	
	public List<FulfillmentService> searchFS(String fserviceName, String status);
	
	public List <FulfillmentIntegrationType> getIntegrationTypes();
	
	public FulfillmentService getFulfillmentServiceId(String name);
	
	public List <FulfillmentMethod> getFulfillmentMethods();
	
	public List<State> getStates();
	
	public Double setVendorHandlingFee(Long vendorId, Long serviceId) throws Exception;
	
	/**
	 * This method gets all the availble styles population methods for item
	 * request.
	 * 
	 * @return List <StylePopulationMethod>
	 */
	public List<StylePopulationMethod> getStylePopulationMethods()
			throws Exception;

	/**
	 * Method to fetch all the available item sources
	 * 
	 * @return List <ItemSource>
	 * @throws Exception
	 */
	public List<ItemSource> getItemSource() throws Exception;

	/**
	 * Method to save the Item request record
	 * 
	 * @param itemRequestForm
	 *            ItemRequestForm
	 * @param mode
	 *            String
	 * @throws Exception
	 */
	public Long saveItemRequest(ItemRequestForm itemRequestForm, String mode)
			throws Exception;

	/**
	 * Method to fetch details of an item request
	 * 
	 * @param id
	 *            Long
	 * @return Object
	 * @throws Exception
	 */
	public Object getItemRequestDetails(Long id) throws Exception;

	/**
	 * Method for validating whether the request id exists
	 * 
	 * @param id
	 *            Long
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isValidRequestId(Long id, String vendorNo, Long serviceId)
			throws Exception;

	/**
	 * This method gets the directory for uploading the item request styles file
	 * 
	 * @param cls
	 *            Class
	 * @param id
	 *            Serializable
	 * @return Object
	 * @throws Exception
	 */
	public Object getById(Class cls, Serializable id) throws Exception;

	/**
	 * This method uploads the item request styles file in a given directory
	 * 
	 * @param file
	 *            Object
	 * @return boolean
	 * @throws IOException
	 */
	public boolean uploadStylesFile(Object file) throws Exception;

	/**
	 * This method validates styles file and returns number of styles uploaded
	 * successfully.
	 * 
	 * @param file
	 * @return Object
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public Object validateStyleFile(Object file, Long vendorId)
			throws Exception;


	/**
	 * This method fetches all the styles associated with a request.
	 * 
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public List<VFIRStyle> getRequestStyles(Long requestId) throws Exception;

	/**
	 * This method updates the styles.
	 * 
	 * @param styles
	 * @throws Exception
	 */
	public void updateRequestStyles(ItemRequestForm itemRequestForm)
			throws Exception;

	/**
	 * This method changes the status of a style to 'INACTIVE'
	 * 
	 * @param vendorStyleId
	 * @param requestId
	 * @throws Exception
	 */
	public void removeStyle(String vendorStyleId, Long requestId)
			throws Exception;

	/**
	 * This method adds styles to a request from request style screen.
	 * 
	 * @param form
	 * @return
	 * @throws Exception
	 */
	public String addStyles(Object form) throws Exception;

	/**
	 * This method changes the status of sku to 'INACTIVE'
	 * 
	 * @param request
	 * @param style
	 * @param color
	 * @param size
	 * @throws Exception
	 */
	public void removeStyleSku(Object request, Object style, Object color,
			Object size, Object upc) throws Exception;

	/**
	 * This methods updates the styles and sku information associated with a
	 * request
	 * 
	 * @param itemRequestForm
	 * @throws Exception
	 */
	public void updateStyleSkus(ItemRequestForm itemRequestForm)
			throws Exception;

	/**
	 * This method updates the sku details for a request.
	 * 
	 * @param itemRequestForm
	 * @throws Exception
	 */
	public void updateSkusFromStyles(ItemRequestForm itemRequestForm)
			throws Exception;


	/**
	 * This method fetches request details for given vendor & fulfillment
	 * service.
	 * 
	 * @param requestHistoryForm
	 * @param vendorId
	 * @param serviceId
	 * @throws Exception
	 */
	public void getRequestHistoryDetails(RequestHistoryForm requestHistoryForm,
			Long vendorId, Long serviceId) throws Exception;

	/**
	 * This method fetches all the styles for associated vendor and fulfillment
	 * service
	 * 
	 * @param stylesSkuForm
	 * @param vendorId
	 * @param serviceId
	 * @throws Exception
	 */
	public void getStyleSkuDetails(StylesSkuForm stylesSkuForm, Long vendorId,
			Long serviceId) throws Exception;

	/**
	 * This method gets sku level details for a given style.
	 * 
	 * @param stylesSkuForm
	 * @param vendorId
	 * @param serviceId
	 * @param vendorUpc
	 * @throws Exception
	 */
	public void getSkuDetails(StylesSkuForm stylesSkuForm, Long vendorId,
			Long serviceId, String vendorUpc) throws Exception;

	public List<Object> getFulfillmentServiceVendorInfo(Long requestId) throws Exception;
	
}
