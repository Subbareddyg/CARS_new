/**
 * 
 */

package com.belk.car.app.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.appfuse.model.User;
import org.appfuse.service.impl.UniversalManagerImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.dao.FulfillmentServiceDao;
import com.belk.car.app.dto.RequestDTO;
import com.belk.car.app.dto.StyleSkuDTO;
import com.belk.car.app.dto.StylesDTO;
import com.belk.car.app.model.BaseAuditableModel;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.oma.Address;
import com.belk.car.app.model.oma.CompositeKeyVFIRStylesku;
import com.belk.car.app.model.oma.CompositeKeyVIFRStyle;
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
import com.belk.car.app.service.EmailManager;
import com.belk.car.app.service.FulfillmentServiceManager;
import com.belk.car.app.to.IdbDropshipDataTO;
import com.belk.car.app.util.PropertyLoader;
import com.belk.car.app.webapp.forms.FulfillmentServiceForm;
import com.belk.car.app.webapp.forms.ItemRequestForm;
import com.belk.car.app.webapp.forms.ItemRequestStylesForm;
import com.belk.car.app.webapp.forms.RequestHistoryForm;
import com.belk.car.app.webapp.forms.StylesSkuForm;
import com.belk.car.util.DateUtils;

/**
 * @author afusy45 Implementation of FulfillmentServiceManager interface
 */
public class FulfillmentServiceManagerImpl extends UniversalManagerImpl
		implements FulfillmentServiceManager, DropShipConstants {

	private FulfillmentServiceDao fulfillmentServiceDao;
	private EmailManager emailManager;

	/**
	 * @param set fulfillmentServiceDao
	 */
	public void setFulfillmentServiceDao(
			FulfillmentServiceDao fulfillmentServiceDao) {
		this.fulfillmentServiceDao = fulfillmentServiceDao;
	}

	/**
	 * @return the fulfillmentServiceDAO
	 */
	public FulfillmentServiceDao getFulfillmentServiceDao() {
		return fulfillmentServiceDao;
	}

	/**
	 * @param emailManager
	 *            the emailManager to set
	 */
	public void setEmailManager(EmailManager emailManager) {
		this.emailManager = emailManager;
	}

	/**
	 * @return the emailManager
	 */
	public EmailManager getEmailManager() {
		return emailManager;
	}

	/**
	 * Get fulfillment service details by passing service ID
	 * @return Object FulfillmentService
	 * @param Long serviceID
	 */
	public FulfillmentService getFulfillmentServiceDetails(Long serviceID) {
		return fulfillmentServiceDao.getFulfillmentServiceDetails(serviceID);
	}

	/**
	 * Returns all the fulfillment services list
	 * @param 
	 * @return List<FulfillmentService>
	 */
	public List<FulfillmentService> getFulfillmentServices() {
		return fulfillmentServiceDao.getFulfillmentServiceList();
	}

	/**
	 * Add/Update Fulfillment Service
	 * @param FulfillmentServiceForm fulfillmentServiceForm
	 * @param String mode
	 * @return Object FulfillmentService
	 */
	public FulfillmentService updateFulfillmentService(
			FulfillmentServiceForm fulfillmentServiceForm, String mode) {

		FulfillmentMethod FMethod = null;
		FulfillmentIntegrationType intType = null;
		FulfillmentService fulfillmentService = null;

		if ((mode.equalsIgnoreCase("edit"))) {
			System.out.println("if mode is edit");
			fulfillmentService = fulfillmentServiceDao
					.getFulfillmentServiceDetails(fulfillmentServiceForm
							.getFulfillmentService().getFulfillmentServiceID());
		} else {
			fulfillmentService = new FulfillmentService();
		}
		fulfillmentService.setFulfillmentMethodID(fulfillmentServiceForm
				.getFulfillmentService().getFulfillmentMethodID());
		fulfillmentService
				.setFulfillmentServiceIntTypeID(fulfillmentServiceForm
						.getFulfillmentService()
						.getFulfillmentServiceIntTypeID());
		/*
		 * Made object of fulfillment method to pass to the fulfillment service
		 * set method
		 */
		try {
			FMethod = fulfillmentServiceDao
					.getFulfillmentMethodById(fulfillmentService
							.getFulfillmentMethodID().getFulfillmentMethodID());
		} catch (DataAccessException de) {
			log.debug("Exception while getting fulfillment method By ID" + de);
		}
		/*
		 * Made object of fulfillment integration type to pass to the
		 * fulfillment service set method
		 */
		try {
			intType = fulfillmentServiceDao.getIntTypesById(fulfillmentService
					.getFulfillmentServiceIntTypeID().getIntegrationTypeID());
		} catch (DataAccessException de) {
			log.debug("Exception while getting Integration Types By Id" + de);
		}
		Address addr = fulfillmentServiceForm.getAddress();

		/* Set every field explicitly */
		fulfillmentService.setCreatedBy(fulfillmentServiceForm
				.getFulfillmentService().getCreatedBy());

		
		fulfillmentService.setCreatedDate(fulfillmentServiceForm
				.getFulfillmentService().getCreatedDate());

		fulfillmentService.setUpdatedBy(fulfillmentServiceForm
				.getFulfillmentService().getUpdatedBy());
		fulfillmentService.setUpdatedDate(fulfillmentServiceForm
				.getFulfillmentService().getUpdatedDate());
		fulfillmentService.setFulfillmentServiceDesc(fulfillmentServiceForm
				.getFulfillmentService().getFulfillmentServiceDesc());
		fulfillmentService.setFulfillmentServiceName(fulfillmentServiceForm
				.getFulfillmentService().getFulfillmentServiceName());
		fulfillmentService
				.setFulfillmentServiceStatusCode(fulfillmentServiceForm
						.getFulfillmentService()
						.getFulfillmentServiceStatusCode());
		fulfillmentService.setFulfillmentServiceIntTypeID(intType);
		fulfillmentService.setFulfillmentMethodID(FMethod);
		try {
			fulfillmentServiceDao.updateFulfillmentService(fulfillmentService,
					addr);
		} catch (DataAccessException de) {
			log.debug("Exception while saving/updating fulfillment service"
					+ de);
		}
		return fulfillmentService;
	}

	/**
	 * Method to get all the address details by passing ID
	 * 
	 * @return Address Object
	 * @param addrID
	 */
	public Address getAddrByID(long addrID) {
		return fulfillmentServiceDao.getAddrByID(addrID);
	}

	/**
	 * Search Fulfillment Service by name
	 * 
	 * @return List<FulfillmentService>
	 * @param fserviceName
	 *            ,status
	 */
	public List<FulfillmentService> searchFS(String fserviceName, String status) {
		return fulfillmentServiceDao.searchFS(fserviceName, status);
	}
	
	/**
	 * ViewAll link clicked
	 * Method lists only ACTIVE fulfillment services
	 * @author afusy45
	 * @param
	 */
	public List<FulfillmentService> getFulfillmentServices(String status){
		return fulfillmentServiceDao.getFulfillmentServiceList(status);
	}
	/**
	 * Returns all the Integration Types
	 * 
	 * @return List
	 */
	public List<FulfillmentIntegrationType> getIntegrationTypes() {
		return fulfillmentServiceDao.getIntegrationTypes();
	}

	/**
	 * Returns all the fulfillment Methods
	 * 
	 * @return List
	 */
	public List<FulfillmentMethod> getFulfillmentMethods() {
		return fulfillmentServiceDao.getFulfillmentMethods();
	}

	/**
	 * Returns all the States
	 * 
	 * @return List
	 */
	public List<State> getStates() {
		return fulfillmentServiceDao.getStates();
	}

	/**
	 * Gets all the fulfillment service details by passing name
	 * 
	 * @return FulfillmentService Object
	 * @param name
	 */
	public FulfillmentService getFulfillmentServiceId(String name) {
		return fulfillmentServiceDao.getFulfillmentServiceId(name);
	}

	/**
	 * This method gets all the availble styles population methods for item
	 * request.
	 * 
	 * @return List <StylePopulationMethod>
	 */
	public List<StylePopulationMethod> getStylePopulationMethods()
			throws Exception {
		return fulfillmentServiceDao.getStylePopulationMethods();
	}

	/**
	 * Method to fetch all the available item sources
	 * 
	 * @return List <ItemSource>
	 * @throws Exception
	 */
	public List<ItemSource> getItemSource() throws Exception {
		return fulfillmentServiceDao.getItemSource();
	}

	/**
	 * Method to save the Item request record
	 * 
	 * @param itemRequestForm
	 *            ItemRequestForm
	 * @param mode
	 *            String
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public Long saveItemRequest(ItemRequestForm itemRequestForm, String mode)
			throws Exception {
		ItemRequest itemRequest = null;

		if (mode.equals(EDIT_ITEMREQUEST)) {
			itemRequest = fulfillmentServiceDao.getItemRequestDetails(new Long(
					itemRequestForm.getRequestId()));
		} else {
			itemRequest = new ItemRequest();
			itemRequest.setCreatedDate(new Date());
		}
		itemRequest.setEffectiveDate(new Date(itemRequestForm.getCreateDate()));
		itemRequest.setDesc(itemRequestForm.getDescription());
		itemRequest.setAction(itemRequestForm.getItemAction());
		itemRequest.setAllowSalesClearancePrice(itemRequestForm
				.getMerchandise());
		itemRequest.setCreatedBy(itemRequestForm.getCreatedBy());
		itemRequest.setUpdatedBy((itemRequestForm.getUpdatedBy()));
		itemRequest.setUpdatedDate(new Date());

		if (itemRequestForm.getUserdepts()) {
			itemRequest.setIsUserDept(USER_DEPT_SELECTED);
		} else {
			itemRequest.setIsUserDept(USER_DEPT_NOT_SELECTED);
		}
		itemRequest.setFulfillmentService(fulfillmentServiceDao
				.getFulfillmentServiceForItemRequest(new Long(itemRequestForm
						.getServiceId())));
		Vendor vendor = fulfillmentServiceDao.getVendorForItemRequest(itemRequestForm.getVendorNumber());
		itemRequest.setVendor(vendor);
		itemRequest.setItemSource(fulfillmentServiceDao
				.getSourceForItemRequest(itemRequestForm.getSourceId()));
		itemRequest.setStylePopulationMethod(fulfillmentServiceDao
				.getStyleForItemRequest(itemRequestForm.getStylePopMethodId()));
		itemRequest.setWorkflowStatus(fulfillmentServiceDao
				.getStatusForItemRequest(itemRequestForm.getRequestStatus()));

		/** Setting minimum markup percent if entered */
		if (null != itemRequestForm.getMinimumMarkup()) {
			itemRequest.setMinMarkupPct(new Double(itemRequestForm
					.getMinimumMarkup()));
		}

		/** Setting the file path if the styles file has been uploaded */
		if (null != itemRequestForm.getFilePath()
				&& !itemRequestForm.getFilePath().equals(BLANK)) {
			String uploadDir = getStylesDirectory();
			if (!uploadDir.endsWith(String.valueOf(File.separatorChar))) {
				uploadDir = uploadDir + File.separatorChar;
			}
			itemRequest.setFileName(uploadDir + itemRequestForm.getFilePath());
		}

		/**
		 * Setting the previous request id if style population method selected
		 * id 'PREVIOUS REQUEST'
		 */
		if (null != itemRequestForm.getLocationName()) {
			itemRequest.setPreviousReqID(new Long(itemRequestForm
					.getLocationName()));
		}

		/** Setting the exception reasons if entered */
		if (null != itemRequestForm.getExceptionReason()) {
			itemRequest.setExceptionReasons(itemRequestForm
					.getExceptionReason());
		}

		/** COndition if the request is posted */
		if (null != itemRequestForm.getPostedBy()) {
			itemRequest.setPostedBy(itemRequestForm.getPostedBy());
			itemRequest
					.setPostedDate((new Date(itemRequestForm.getPostedDate())));
		} else {
			itemRequest.setPostedBy(null);
			itemRequest.setPostedDate(null);
		}

		/** Saving/Updating the request */
		if (mode.equals(EDIT_ITEMREQUEST)) {
			itemRequest.setItemRequestID(new Long(itemRequestForm
					.getRequestId()));
			fulfillmentServiceDao.updateItemRequest(itemRequest);
		} else {
			fulfillmentServiceDao.saveItemRequest(itemRequest);
		}

		/** Saving record in styles & sku's table */
		if (itemRequestForm.getRequestStatus().equalsIgnoreCase(
				VFIRStatus.OPEN)
				|| mode.equals(ADD_ITEMREQUEST)) {
			this.saveStylesForRequest(itemRequestForm, itemRequest);
		}

		/**
		 * If the request is approved then make the styles and skus in current
		 * request 'ACTIVE' and all others as 'INACTIVE'
		 */
		if (itemRequestForm.getRequestStatus().equals(VFIRStatus.APPROVED)
				&& itemRequestForm.getItemAction().equals(
						ADD_EDIT_STYLES_SKUS_ACTION)) {
			this.updateStylesSkus(itemRequest.getItemRequestID());
		} else if(itemRequestForm.getRequestStatus().equals(VFIRStatus.APPROVED)
				&& itemRequestForm.getItemAction().equals(
						REMOVE_STYLES_SKUS_ACTION)) {
			this.updateStyleSkus(vendor.getVendorId(), new Long(itemRequestForm.getServiceId()));
		}

		/** Saving record in item request workflow */
		saveItemRequestWorkflow(itemRequest, itemRequestForm.getRejectReason(),
				mode);
		return itemRequest.getItemRequestID();
	}

	/**
	 * This method saves styles for the newly created request.
	 * 
	 * @param itemRequestForm
	 * @param itemRequest
	 * @throws Exception
	 */
	private void saveStylesForRequest(ItemRequestForm itemRequestForm,
			ItemRequest itemRequest) throws Exception {
		/** Saving record in styles & sku's table */
		List<VFIRStyle> list = null;
		if (null != itemRequestForm.getValidStyles()
				&& !itemRequestForm.getValidStyles().equals(BLANK)) {
			list = saveStylesFromFile(itemRequestForm.getValidStyles(),
					itemRequest);
		} else if (null != itemRequestForm.getLocationName()
				&& !itemRequestForm.getLocationName().equals(BLANK)) {
			list = saveRequestStylesFromPreviousRequest(new Long(
					itemRequestForm.getLocationName()), itemRequest);
		} else if (null != itemRequestForm.getUserdepts()
				&& itemRequestForm.getUserdepts()) {
			list = saveStylesForUserDepartment(itemRequestForm.getUser(),
					itemRequest);
		} else if (itemRequestForm.getStylePopMethodId().equalsIgnoreCase(
				StylePopulationMethod.DROPSHIP_NOT_IN_ECOMMERCE)) {
			list = saveStylesForDropship(itemRequest.getVendor()
					.getVendorNumber(), itemRequest);
		} else if (null != itemRequestForm.getSelectedStyles()) {
			list = saveStylesSelectedFromStyleSku(itemRequestForm
					.getSelectedStyles(), itemRequest);
		}

		/** Save sku */
		if (null != list && !list.isEmpty()) {
			itemRequestForm.setStyleDetails(list);
			itemRequestForm.setRequestId(itemRequest.getItemRequestID()
					.toString());
			this.updateSkusFromStyles(itemRequestForm);
		}
	}

	/**
	 * This method saves the record of updation made to a request.
	 * 
	 * @param itemRequest
	 * @param rejectReason
	 * @throws Exception
	 */
	private void saveItemRequestWorkflow(ItemRequest itemRequest,
			String rejectReason, String mode) throws Exception {
		System.out.println("Is request updated...?=>" + itemRequest.getUpdatedBy());
		/** Saving record in item request work flow status table */
		ItemRequestWorkflow itemRequestWorkflow = new ItemRequestWorkflow();
		itemRequestWorkflow.setItemRequest(itemRequest);
		itemRequestWorkflow.setCreatedBy(itemRequest.getUpdatedBy());
		itemRequestWorkflow.setCreatedDate(new Date());
		itemRequestWorkflow.setUpdatedBy((itemRequest.getUpdatedBy()));
		itemRequestWorkflow.setUpdatedDate(new Date());
		itemRequestWorkflow.setRejectReason(rejectReason);
		itemRequestWorkflow.setWorkflowStatus(itemRequest.getWorkflowStatus());

		if (null != itemRequest.getPostedBy()) {
			itemRequestWorkflow.setAction(ACTION_POSTED);
		} else if (itemRequest.getWorkflowStatus().getVfirStatusCode().equals(
				VFIRStatus.OPEN)) {
			if (mode.equals(ADD_ITEMREQUEST)) {
				itemRequestWorkflow.setAction(ACTION_REQUEST_CREATED);
			} else {
				itemRequestWorkflow.setAction(ACTION_REQUEST_SAVED);
			}
		} else {
			itemRequestWorkflow.setAction(itemRequest.getWorkflowStatus()
					.getName());
		}
		this.fulfillmentServiceDao.saveItemRequestWorkflow(itemRequestWorkflow);
	}

	/**
	 * Method to fetch details of an item request
	 * 
	 * @param id
	 *            Long
	 * @return Object
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object getItemRequestDetails(Long id) throws Exception {
		ItemRequest itemRequest = fulfillmentServiceDao
				.getItemRequestDetails(id);
		ItemRequestForm itemRequestForm = new ItemRequestForm();
		itemRequestForm.setRequestId(itemRequest.getItemRequestID().toString());
		itemRequestForm.setDescription(itemRequest.getDesc());
		itemRequestForm.setCreatedBy(itemRequest.getCreatedBy());
		itemRequestForm.setVendorNumber(itemRequest.getVendor()
				.getVendorNumber());
		itemRequestForm.setServiceId(new Long(itemRequest
				.getFulfillmentService().getFulfillmentServiceID()).toString());
		itemRequestForm.setCreatedDate(getFormattedDate(itemRequest
				.getCreatedDate()));
		itemRequestForm.setVendorName(itemRequest.getVendor().getName());
		itemRequestForm.setServiceName(itemRequest.getFulfillmentService()
				.getFulfillmentServiceName());
		itemRequestForm.setUpdatedDate(getFormattedDate(itemRequest
				.getUpdatedDate()));
		itemRequestForm.setUpdatedBy(itemRequest.getUpdatedBy());
		itemRequestForm.setExceptionReason(itemRequest.getExceptionReasons());
		itemRequestForm.setStatus(itemRequest.getWorkflowStatus().getName());
		itemRequestForm.setRequestStatus(itemRequest.getWorkflowStatus()
				.getVfirStatusCode());

		itemRequestForm.setCreateDate(getFormattedDate(itemRequest
				.getEffectiveDate()));
		itemRequestForm.setSourceId(itemRequest.getItemSource().getName());
		itemRequestForm.setItemAction((itemRequest.getAction()));
		itemRequestForm.setMerchandise(itemRequest
				.getAllowSalesClearancePrice());
		itemRequestForm.setPostedBy(itemRequest.getPostedBy());
		itemRequestForm.setPostedDate(getFormattedDate(itemRequest
				.getPostedDate()));

		if (null == itemRequest.getIsUserDept()
				|| itemRequest.getIsUserDept().equalsIgnoreCase(
						USER_DEPT_NOT_SELECTED)) {
			itemRequestForm.setUserdepts(Boolean.FALSE);
		} else {
			itemRequestForm.setUserdepts(Boolean.TRUE);
		}
		itemRequestForm.setRejectReason(this.fulfillmentServiceDao
				.getRequestRejectReason(itemRequest).getRejectReason());
		if (itemRequest.getAllowSalesClearancePrice().equalsIgnoreCase(
				ALLOW_SALE_CLEARANCE)) {
			itemRequestForm.setMinimumMarkup(itemRequest.getMinMarkupPct()
					.toString());
		}
		itemRequestForm.setStylePopMethodId(itemRequest
				.getStylePopulationMethod().getName());
		itemRequestForm.setFilePath(itemRequest.getFileName());
		if (null != itemRequest.getPreviousReqID()) {
			itemRequestForm.setLocationName((itemRequest.getPreviousReqID()
					.toString()));
		}

		/** Fetching the details of styles associated with a request */
		List<VFIRStyle> styleDetails = this.fulfillmentServiceDao
				.getStylesForItemRequest(itemRequest);

		itemRequestForm.setStyleDetails(styleDetails);

		/**Fetching the details of sku associated with a request*/
		List<StylesDTO> skuDetails = this
				.fetchStyleSkusForSkuException(styleDetails);
		itemRequestForm.setSkuDetails(skuDetails);
		itemRequestForm.setTotalResultSize(skuDetails.size());

		/** Fetching the details of the updation of the request */
		List historyDetails = new ArrayList(itemRequest.getWorkFlow());
		itemRequestForm.setUpdateDetails(historyDetails);
		
		this.disableSkuException(itemRequest.getItemRequestID(), itemRequestForm);
		return itemRequestForm;
	}

	/**
	 * This method returns the formatted date.
	 * 
	 * @param date
	 * @return String
	 */
	private String getFormattedDate(Date date) {
		String formattedDate = null;
		if (null != date) {
			formattedDate = DateUtils.formatDate(date);
		}
		return formattedDate;
	}

	/**
	 * Method for validating whether the request id exists
	 * 
	 * @param id
	 *            Long
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isValidRequestId(Long id, String vendorNo, Long serviceId)
			throws Exception {
		return fulfillmentServiceDao.isValidRequestId(id, vendorNo, serviceId);
	}

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
	@SuppressWarnings("unchecked")
	public Object getById(Class cls, Serializable id) throws Exception {
		return this.fulfillmentServiceDao.getById(cls, id);
	}

	/**
	 * This method uploads the item request styles file in a given directory
	 * 
	 * @param file
	 *            Object
	 * @return boolean
	 * @throws IOException
	 */
	public boolean uploadStylesFile(Object file) throws Exception {
		CommonsMultipartFile multipartFile = (CommonsMultipartFile) file;

		String uploadDir = getStylesDirectory();
		if (!uploadDir.endsWith(String.valueOf(File.separatorChar))) {
			uploadDir = uploadDir + File.separatorChar;
		}

		/** Create the directory if it doesn't exist */
		File dirPath = new File(uploadDir);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		/** retrieve the file data */

		InputStream stream = multipartFile.getInputStream();

		String uploadFile = uploadDir + multipartFile.getOriginalFilename();

		OutputStream bos = new FileOutputStream(uploadFile);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];

		while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
			bos.write(buffer, 0, bytesRead);
		}
		bos.close();
		/** close the stream */
		stream.close();

		return true;
	}

	/**
	 * This method validates styles file and returns number of styles uploaded
	 * successfully.
	 * 
	 * @param file
	 * @return Object
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	public Object validateStyleFile(Object file, Long vendorId)
			throws Exception {
		CommonsMultipartFile multipartFile = (CommonsMultipartFile) file;
		
		StringBuffer vendorStyles = new StringBuffer();
		StringBuffer upc = new StringBuffer();
		List list = new ArrayList();
		Integer noOfStyles = 0;
		Integer noOfUploadedStyles = 0;
		String validStyles = null;

		POIFSFileSystem fs = new POIFSFileSystem(multipartFile.getInputStream());
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFSheet sheet = wb.getSheetAt(0);
		short styleColumnValue = 0;
		short upcColumnValue = 0;
		Iterator rowIterator = sheet.rowIterator();

		while (rowIterator.hasNext()) {
			HSSFRow row = (HSSFRow) rowIterator.next();
			String label = null;
			String value = null;
			Double doubleValue = null;

			if (noOfStyles == 0) {
				Iterator cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					HSSFCell cell = (HSSFCell) cellIterator.next();

					if (null != cell
							&& null != (cell.getRichStringCellValue().toString()).trim()
							&& !((cell.getRichStringCellValue().toString()).trim()).equals(BLANK)) {
						label = cell.getRichStringCellValue().toString().trim();
						if (!label.equalsIgnoreCase(LABEL_VENDOR_STYLE)
								&& !(label.equalsIgnoreCase(LABEL_BELK_UPC)
										|| label
												.equalsIgnoreCase(LABEL_VENDOR_UPC) || label
										.equalsIgnoreCase(LABEL_UPC))) {
							log.info("Label is not proper...." + label);
							list.add("Label(s) in the file is not proper:"
									+ label);
							return list;
						} else if (label.equalsIgnoreCase(LABEL_VENDOR_STYLE)) {
							styleColumnValue = cell.getCellNum();
						} else {
							upcColumnValue = cell.getCellNum();
						}
					}
				}
			}

			HSSFCell cell = row.getCell((short) styleColumnValue);

			/** COndition checks the label of the first column */
			if (noOfStyles > 0 && null != cell) {
				try {
					doubleValue = cell.getNumericCellValue();
					vendorStyles.append(doubleValue.longValue() + COMMA);
				} catch (NumberFormatException formatException) {
					value = cell.getRichStringCellValue().toString();
					vendorStyles.append(value + COMMA);
				}
			} else if (noOfStyles > 0) {
				log.info("style is not available");
				continue;
			}

			/** Condition checks the label of the second column */
			cell = row.getCell((short) upcColumnValue);
			if (noOfStyles > 0 && null != cell) {
				try {
					doubleValue = cell.getNumericCellValue();
					upc.append(doubleValue.longValue() + COMMA);
				} catch (NumberFormatException formatException) {
					value = cell.getRichStringCellValue().toString();
					upc.append(value + COMMA);
				}
			} else if (noOfStyles > 0) {
				log.info("UPC not available");
				continue;
			}
			noOfStyles++;
		}

		if (vendorStyles.length() > 0) {
			vendorStyles.deleteCharAt(vendorStyles.length() - 1);
		} else {
			log.info("No style available");
			list.add("No styles are present in the file to be uploaded");
			return list;
		}

		if (upc.length() > 0) {
			upc.deleteCharAt(upc.length() - 1);
		} else {
			log.info("No upc available");
			list.add("No upc's are available in the file to be uploaded");
			return list;
		}
		// ++Add procedure call
		validStyles = (String) this.fulfillmentServiceDao
				.getNumberOfUploadedStyles(vendorStyles.toString(), upc
						.toString(), vendorId);
		noOfUploadedStyles = (Integer) getNumberOfValidStyles(validStyles);
		list.add(noOfUploadedStyles.toString());
		list.add(new Integer((noOfStyles - 1) - noOfUploadedStyles).toString());
		list.add(validStyles);
		return list;
	}

	/**
	 * This method tokenizes the comma separated valid style id's and returns
	 * the number of tokens. This number represents the no. of valid styles that
	 * are upload to a request.
	 * 
	 * @param styles
	 * @return
	 */
	private Object getNumberOfValidStyles(String styles) {
		Integer noOfValidStyles = 0;
		if (null != styles && !styles.equals(BLANK)) {
			StringTokenizer stringTokenizer = new StringTokenizer(styles, COMMA);
			noOfValidStyles = stringTokenizer.countTokens();
		}
		return noOfValidStyles;
	}

	/**
	 * This methods gets the directory to upload the styles file.
	 * 
	 * @return String
	 */
	private String getStylesDirectory() throws Exception {
		Properties properties = PropertyLoader.loadProperties("ftp.properties");
		return properties.getProperty("stylesUploadDirectory");
	}

	/**
	 * This method sets the primary key for a style.
	 * 
	 * @param style
	 * @param itemRequest
	 * @param vendorStyleId
	 */
	private VFIRStyle setKeyForStyle(ItemRequest itemRequest, String vendorStyleId) throws Exception {
		VFIRStyle style = (VFIRStyle) this.fulfillmentServiceDao.getVFIRStyleById(vendorStyleId, itemRequest);
		if (null == style) {
			style = new VFIRStyle();
			CompositeKeyVIFRStyle compositeKeyVIFRStyle = new CompositeKeyVIFRStyle();
			compositeKeyVIFRStyle.setItemRequest(itemRequest);
			compositeKeyVIFRStyle.setVendorStyleId(vendorStyleId);
			style.setCompositeKeyVIFRStyle(compositeKeyVIFRStyle);
		}
		return style;
	}

	/**
	 * This method saves the styles that has been selected in style sku screen
	 * prior to creating a request. It is called if the style population method
	 * of request is set to 'MANUAL' and styles have been selected in style sku
	 * screen.
	 * 
	 * @param list
	 * @param itemRequest
	 * @return
	 * @throws Exception
	 */
	private List<VFIRStyle> saveStylesSelectedFromStyleSku(List<String> list,
			ItemRequest itemRequest) throws Exception {
		List<VFIRStyle> list2 = new ArrayList<VFIRStyle>();
		VFIRStyle style = null;
		IdbDropshipDataTO idbDropshipDataTO = null;
		String styleId = null;

		/** Fetching unit handling fee for a vendor &fulfillment service pair */
		Double fee = this.setVendorHandlingFee(itemRequest.getVendor()
				.getVendorId(), itemRequest.getFulfillmentService()
				.getFulfillmentServiceID());

		Iterator<String> iterator = list.iterator();
		while (iterator.hasNext()) {
			styleId = iterator.next();
			idbDropshipDataTO = this.fulfillmentServiceDao
					.getIDBRecordForStyleId(styleId);
			style = this.setKeyForStyle(itemRequest, styleId);
			style.setStyleDescription(idbDropshipDataTO.getStyleDesc());
			style.setAllColorIndicator(N);
			style.setAllSizeIndicator(N);
			style.setAllSkuIndicator(N);
			style.setStatusCode(VFIRStyle.ACTIVE);
			style.setUnitCost(idbDropshipDataTO.getIDBStyleUnitCost());
			style.setUnitHandlingFee(fee);
			style.setVendor(itemRequest.getVendor());
			this.setAuditInfo(style);
			list2.add(style);
		}
		if (!list2.isEmpty()) {
			this.fulfillmentServiceDao.uploadStyles(list2);
		}
		return list2;
	}

	/**
	 * This method adds new request styles obtained from the file to a request
	 */
	private List<VFIRStyle> saveStylesFromFile(Object styles,
			ItemRequest itemRequest) throws Exception {
		List<VFIRStyle> list = new ArrayList<VFIRStyle>();
		VFIRStyle style = null;
		String styleId = null;
		IdbDropshipDataTO idbDropshipDataTO = null;
		
		StringTokenizer stringTokenizer = new StringTokenizer((String) styles,
				COMMA);

		Double fee = this.setVendorHandlingFee(itemRequest.getVendor()
				.getVendorId(), itemRequest.getFulfillmentService()
				.getFulfillmentServiceID());
		while (stringTokenizer.hasMoreElements()) {
			styleId = (String) stringTokenizer.nextElement();
			idbDropshipDataTO = this.fulfillmentServiceDao
					.getIDBRecordForStyleId(styleId);
			style = this.setKeyForStyle(itemRequest, styleId);
			style.setStyleDescription(idbDropshipDataTO.getStyleDesc());
			style.setAllColorIndicator(N);
			style.setAllSizeIndicator(N);
			style.setAllSkuIndicator(N);
			style.setStatusCode(VFIRStyle.ACTIVE);
			style.setUnitCost(idbDropshipDataTO.getIDBStyleUnitCost());
			style.setUnitHandlingFee(fee);
			style.setVendor(itemRequest.getVendor());
			this.setAuditInfo(style);
			list.add(style);
		}
		if (!list.isEmpty()) {
			this.fulfillmentServiceDao.uploadStyles(list);
		}
		return list;
	}

	/**
	 * 
	 */
	public List<VFIRStyle> getRequestStyles(Long requestId) throws Exception {
		List<VFIRStyle> list = this.fulfillmentServiceDao
				.getRequestStyles(requestId);
		return list;
	}

	/**
	 * This method adds request styles obtained from previous request.
	 */
	@SuppressWarnings("unchecked")
	private List<VFIRStyle> saveRequestStylesFromPreviousRequest(
			Long previousRequestId, ItemRequest itemRequest) throws Exception {
		List<VFIRStyle> list = this.fulfillmentServiceDao
				.getStylesForItemRequest(this.fulfillmentServiceDao
						.getItemRequestDetails(previousRequestId));
		List<VFIRStyle> stylesList = new ArrayList<VFIRStyle>();
		VFIRStyle style = null;
		VFIRStyle style2 = null;

		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			style = (VFIRStyle) iterator.next();

			style2 = this.setKeyForStyle(itemRequest, style
					.getCompositeKeyVIFRStyle().getVendorStyleId());
			style2.setVendor(style.getVendor());
			style2.setStyleDescription(style.getStyleDescription());
			style2.setAllColorIndicator(style.getAllColorIndicator());
			style2.setAllSizeIndicator(style.getAllSizeIndicator());
			style2.setAllSkuIndicator(style.getAllSkuIndicator());
			style2.setUnitCost(style.getUnitCost());
			style2.setUnitHandlingFee(style.getUnitHandlingFee());
			style2.setOverrideFee(style.getOverrideFee());
			style2.setOverrideUnitCost(style.getOverrideUnitCost());
			style2.setStatusCode(VFIRStyle.ACTIVE);
			this.setAuditInfo(style2);
			stylesList.add(style2);
		}
		this.fulfillmentServiceDao.uploadStyles(stylesList);
		return stylesList;
	}

	/**
	 * 
	 */
	public void updateRequestStyles(ItemRequestForm itemRequestForm)
			throws Exception {
		System.out.println("status in manager====>" + itemRequestForm.getRequestStatus());
		/** List contains styles to be saved in DB */
		List<VFIRStyle> list = itemRequestForm.getStyleDetails();

		ItemRequest itemRequest = this.fulfillmentServiceDao
				.getItemRequestDetails(new Long(itemRequestForm.getRequestId()));

		/** saveStatus indicates if the status of the request has been changed */
		boolean saveStatus = false;

		/**
		 * isSendEmail checks if there is any cost override for the styles and
		 * the request has been posted
		 */
		boolean isSendEmail = false;

		/**
		 * Fetching all the styles whose cost has been overridden. These styles
		 * are mailed to the admin for approval
		 */
		List<VFIRStyle> mailList = fetchStylesWithOveriddenCost(list);

		/** Saving the styles in DB */
		this.fulfillmentServiceDao.uploadStyles(list);

		/** Updating the status of the request if any changes in the status */
		if (itemRequestForm.getRequestStatus().equalsIgnoreCase(APPROVE)) {
			itemRequestForm.setRequestStatus(VFIRStatus.APPROVED);
			saveStatus = true;
		} else if (itemRequestForm.getRequestStatus().equalsIgnoreCase(REJECT)) {
			itemRequestForm.setRequestStatus(VFIRStatus.REJECT);
			saveStatus = true;
		} else if (itemRequestForm.getRequestStatus().equalsIgnoreCase(
				SAVE_POST)) {
			/**
			 * Condition : No cost overrides for the styles and the request is
			 * posted, change the status of request to 'APPROVED'
			 */
			if (mailList.isEmpty()) {
				itemRequestForm.setRequestStatus(VFIRStatus.APPROVED);
			} else {
				itemRequestForm
						.setRequestStatus(VFIRStatus.AWAITING_APPROVAL);
			}
			saveStatus = true;
			isSendEmail = true;
		}
		if (saveStatus) {
			/** Setting the status of the request */
			itemRequest
					.setWorkflowStatus(fulfillmentServiceDao
							.getStatusForItemRequest(itemRequestForm
									.getRequestStatus()));

			/**
			 * Setting the posted by and posted date fields if the request has
			 * been posted
			 */
			if (isSendEmail) {
				itemRequest.setPostedBy(itemRequestForm.getPostedBy());
				itemRequest.setPostedDate(new Date(itemRequestForm
						.getPostedDate()));
			} else {
				itemRequest.setPostedBy(null);
				itemRequest.setPostedDate(null);
			}
		}

		itemRequest.setUpdatedBy(itemRequestForm.getUpdatedBy());
		itemRequest.setUpdatedDate(new Date(itemRequestForm
				.getUpdatedDate()));

		/** Updating the request */
		this.fulfillmentServiceDao.updateItemRequest(itemRequest);
		/** Updating the request workflow */
		this.saveItemRequestWorkflow(itemRequest, BLANK, EDIT_ITEMREQUEST);

		/**
		 * If the request is approved then change the status of the other styles
		 * & skus included in this request to 'INACTIVE'
		 */
		if (itemRequestForm.getRequestStatus().equals(VFIRStatus.APPROVED)) {
			this.updateStylesSkus(itemRequest.getItemRequestID());
		}

		/** Update the override cost of sku's for all the styles */
		if (!mailList.isEmpty()) {
			this.updateOverrideCostOfAllSkus(mailList, itemRequest);
		}
		/** Send email to admin for approval if any cost overrides */
		if (isSendEmail && !mailList.isEmpty()) {
			log.info("Sending email.....");
			sendEmail(itemRequestForm, mailList, OVERRIDE_IN_STYLE);
		}
	}

	/**
	 * This method updates the override cost (if any) of all the skus in a style
	 * 
	 * @param list
	 * @param itemRequest
	 * @throws Exception
	 */
	private void updateOverrideCostOfAllSkus(List<VFIRStyle> list,
			ItemRequest itemRequest) throws Exception {
		Iterator<VFIRStyle> iterator = list.iterator();

		while (iterator.hasNext()) {
			VFIRStyle style = iterator.next();

			List<VFIRStyleSku> list2 = this.fulfillmentServiceDao
					.getSkusForStyle(style.getCompositeKeyVIFRStyle()
							.getVendorStyleId(), itemRequest);
			if (null != list2 && !list2.isEmpty()) {
				Iterator<VFIRStyleSku> iterator2 = list2.iterator();
				while (iterator2.hasNext()) {
					VFIRStyleSku styleSku = iterator2.next();
					styleSku.setOverrideCost(style.getOverrideUnitCost());
					styleSku.setOverrideFee(style.getOverrideFee());
				}
				this.fulfillmentServiceDao.saveStyleSkus(list2);
			}
		}
	}

	/**
	 * This method checks which styles cost is overidden. The styles returned
	 * from this method are sent in email to the Ecommerce operations for approving/rejecting the
	 * overidden cost.
	 * 
	 * @param list
	 * @return
	 */
	private List<VFIRStyle> fetchStylesWithOveriddenCost(List<VFIRStyle> list) {
		Iterator<VFIRStyle> iterator = list.iterator();
		List<VFIRStyle> list2 = new ArrayList<VFIRStyle>();
		while (iterator.hasNext()) {
			VFIRStyle style = iterator.next();
			/**If all size & all color are checked then all size must be set to "Y"*/
			if(style.getAllSizeIndicator().equals(ALL_SIZE_Y) && style.getAllColorIndicator().equals(ALL_COLOR_Y)) {
				style.setAllSkuIndicator(ALL_SIZE_Y);
			}
			if ((null != style.getUnitCost()
					&& null != style.getOverrideUnitCost() && style
					.getUnitCost() != style.getOverrideUnitCost())
					|| (null != style.getUnitHandlingFee()
							&& null != style.getOverrideFee() && style
							.getUnitHandlingFee() != style.getOverrideFee())) {
				list2.add(style);
			}
		}
		return list2;
	}

	/**
	 * This method fetches and saves styles which are allowed for dropship but
	 * are not in ECommerce for a request. It gets called if the style
	 * population method of the request is 'Dropship In IDB Not In Ecommerce'
	 * 
	 * @param vendorNumber
	 * @param itemRequest
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private List<VFIRStyle> saveStylesForDropship(String vendorNumber,
			ItemRequest itemRequest) throws Exception {

		/** The list contains the styles that need to be saved for the request */
		List requestStyles = new ArrayList();

		/** Fetching the required style id's from the DB */
		List list = (List) this.fulfillmentServiceDao
				.getStylesFromDropship(vendorNumber);

		Map<String, String> map = this.fetchDropShipStatusOfStyles(itemRequest
				.getVendor().getVendorId(), itemRequest.getFulfillmentService()
				.getFulfillmentServiceID());
		list = this.fetchStylesDropshipInIDBNotInEcom(list, map);

		/**
		 * Fetching the vendor handling fee for vendor and fulfillment service
		 * pair
		 */
		Double fee = this.setVendorHandlingFee(itemRequest.getVendor()
				.getVendorId(), itemRequest.getFulfillmentService()
				.getFulfillmentServiceID());

		IdbDropshipDataTO idbDropshipDataTO = null;
		String vendorStyleId = null;
		VFIRStyle style = null;
		
		Iterator iterator2 = list.iterator();
		while (iterator2.hasNext()) {
			vendorStyleId = (String) iterator2.next();
			idbDropshipDataTO = this.fulfillmentServiceDao
					.getIDBRecordForStyleId(vendorStyleId);
			style = this.setKeyForStyle(itemRequest, vendorStyleId);
			style.setStyleDescription(idbDropshipDataTO.getStyleDesc());
			style.setAllColorIndicator(N);
			style.setAllSizeIndicator(N);
			style.setAllSkuIndicator(N);
			style.setStatusCode(VFIRStyle.ACTIVE);
			style.setUnitCost(idbDropshipDataTO.getIDBStyleUnitCost());
			style.setUnitHandlingFee(fee);
			style.setVendor(itemRequest.getVendor());
			this.setAuditInfo(style);
			requestStyles.add(style);
		}
		this.fulfillmentServiceDao.uploadStyles(requestStyles);
		return requestStyles;
	}

	/**
	 * This method fetches and saves styles included for the logged in user's
	 * department and associated with the seleted vendor & fulfillment service.
	 * 
	 * @param user
	 * @param itemRequest
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private List<VFIRStyle> saveStylesForUserDepartment(User user,
			ItemRequest itemRequest) throws Exception {

		/** The list contains the styles that need to be saved for the request */
		List requestStyles = new ArrayList();

		/** Fetching the required style id's from the DB */
		StringBuilder query = new StringBuilder();
		Long serviceId = itemRequest.getFulfillmentService()
				.getFulfillmentServiceID();
		Long vendorId = itemRequest.getVendor().getVendorId();

		query.append("SELECT VENDOR_SYTLE_ID ");
		query.append("FROM FULFMNT_SERVICE_VENDOR VF JOIN VENDOR V ON (VF.VENDOR_ID=V.VENDOR_ID)");
		query.append("     JOIN IDB_DROP_SHIP_FEED I ON (I.VENDOR_NUMBER=V.VENDOR_NUMBER) ");
		query.append("WHERE VF.vendor_id = ");
		query.append(vendorId);
		query.append(" AND VF.fulfmnt_service_id = ");
		query.append(serviceId);
		query.append(" AND I.style_drop_ship_flag='Y'");
		query.append(" AND I.dept_id IN (SELECT D.DEPT_CD FROM CAR_USER_DEPARTMENT C JOIN DEPARTMENT D ");
		query.append("                   ON (C.DEPT_ID=D.DEPT_ID) WHERE C.CAR_USER_ID=");
		query.append(user.getId());
		query.append(                   ")");
		
		List list = (List) this.fulfillmentServiceDao.fetchCount(query
				.toString());
		Map<String, String> map = this.fetchDropShipStatusOfStyles(vendorId,
				serviceId);
		list = this.fetchStylesDropshipInIDBNotInEcom(list, map);

		/**
		 * Fetching the vendor handling fee for vendor and fulfillment service
		 * pair
		 */
		Double fee = this.setVendorHandlingFee(itemRequest.getVendor()
				.getVendorId(), itemRequest.getFulfillmentService()
				.getFulfillmentServiceID());
		
		String vendorStyleId = null;
		IdbDropshipDataTO idbDropshipDataTO = null;
		VFIRStyle style = null;

		Iterator iterator2 = list.iterator();
		while (iterator2.hasNext()) {
			vendorStyleId = (String) iterator2.next();
			idbDropshipDataTO = this.fulfillmentServiceDao
					.getIDBRecordForStyleId(vendorStyleId);

			style = this.setKeyForStyle(itemRequest, vendorStyleId);
			style.setStyleDescription(idbDropshipDataTO.getStyleDesc());
			style.setAllColorIndicator(N);
			style.setAllSizeIndicator(N);
			style.setAllSkuIndicator(N);
			style.setStatusCode(VFIRStyle.ACTIVE);
			style.setUnitCost(idbDropshipDataTO.getIDBStyleUnitCost());
			style.setUnitHandlingFee(fee);
			style.setVendor(itemRequest.getVendor());
			this.setAuditInfo(style);
			requestStyles.add(style);
		}
		if (!requestStyles.isEmpty()) {
			this.fulfillmentServiceDao.uploadStyles(requestStyles);
		}
		return requestStyles;
	}

	/**
	 * This method checks and returns all the styles which is drop ship allowed
	 * in IDB but not in Order management administration.
	 * 
	 * @param list
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private List fetchStylesDropshipInIDBNotInEcom(List list,
			Map<String, String> map) throws Exception {
		Iterator iterator = list.iterator();
		List list2 = new ArrayList();

		while (iterator.hasNext()) {
			String styleId = (String) iterator.next();
			Integer activeSkuCount = null;

			if (null != map.get(styleId)) {
				activeSkuCount = new Integer(map.get(styleId));
			}
			if (null == activeSkuCount || activeSkuCount == 0) {
				list2.add(styleId);
			}
		}
		return list2;
	}

	/**
	 * 
	 */
	public void removeStyle(String vendorStyleId, Long requestId)
			throws Exception {
		ItemRequest itemRequest = this.fulfillmentServiceDao
				.getItemRequestDetails(requestId);
		VFIRStyle style = (VFIRStyle) this.fulfillmentServiceDao
				.getVFIRStyleById(vendorStyleId, itemRequest);
		this.fulfillmentServiceDao.removeStyle(style);
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String addStyles(Object form) throws Exception {
		ItemRequestStylesForm form2 = (ItemRequestStylesForm) form;
		List<String> list = new ArrayList();
		List<VFIRStyle> list2 = new ArrayList<VFIRStyle>();
		String isValidStyles = null;
		
		int counter = 0;

		if (null != form2.getStyle1() && !form2.getStyle1().equals(BLANK)) {
			list.add(form2.getStyle1());
			counter++;
		}
		if (null != form2.getStyle2() && !form2.getStyle2().equals(BLANK)) {
			list.add(form2.getStyle2());
			counter++;
		}
		if (null != form2.getStyle3() && !form2.getStyle3().equals(BLANK)) {
			list.add(form2.getStyle3());
			counter++;
		}
		if (null != form2.getStyle4() && !form2.getStyle4().equals(BLANK)) {
			list.add(form2.getStyle4());
			counter++;
		}
		if (null != form2.getStyle5() && !form2.getStyle5().equals(BLANK)) {
			list.add(form2.getStyle5());
			counter++;
		}
		if (null != form2.getStyle6() && !form2.getStyle6().equals(BLANK)) {
			list.add(form2.getStyle6());
			counter++;
		}
		if (null != form2.getStyle7() && !form2.getStyle7().equals(BLANK)) {
			list.add(form2.getStyle7());
			counter++;
		}
		if (null != form2.getStyle8() && !form2.getStyle8().equals(BLANK)) {
			list.add(form2.getStyle8());
			counter++;
		}
		if (null != form2.getStyle9() && !form2.getStyle9().equals(BLANK)) {
			list.add(form2.getStyle9());
			counter++;
		}
		if (null != form2.getStyle0() && !form2.getStyle0().equals(BLANK)) {
			list.add(form2.getStyle0());
			counter++;
		}

		/** Fetching the style records to be saved */
		List<String> invalidStyles = validateAndFetchStyleObjects(list, list2,
				new Long(form2.getRequestId()));

		if (!list2.isEmpty() && list2.size() == counter) {
			this.fulfillmentServiceDao.uploadStyles(list2);

			ItemRequest itemRequest = this.fulfillmentServiceDao
					.getItemRequestDetails(new Long(form2.getRequestId()));

			/** Saving sku's for the styles added */
			List<VFIRStyleSku> list3 = this.fetchSkusForStyles(itemRequest,
					list2);
			if (null != list3 && !list3.isEmpty()) {
				this.fulfillmentServiceDao.saveStyleSkus(list3);
			}
		} else {
			isValidStyles = constructErrorMessage(invalidStyles);
		}
		return isValidStyles;
	}

	/**
	 * This method constructs an error message for add style pop up which gets opened up on request styles screen.
	 * @param invalidStyles
	 * @return
	 */
	private String constructErrorMessage(List<String> invalidStyles) {
		StringBuilder builder = new StringBuilder();
		
		if(!invalidStyles.isEmpty()) {
			builder.append("Invalid style numbers are:");
			Iterator<String> iterator = invalidStyles.iterator();
			while(iterator.hasNext()) {
				builder.append(iterator.next());
				builder.append(COMMA);
			}
			builder.deleteCharAt(builder.length() - 1);
		}
		return builder.toString();
	}
	/**
	 * This method validates if the style entered in the request style screen
	 * exists for that vendor and fulfillment service pair
	 * 
	 * @param list
	 * @param list2
	 * @param requestId
	 * @throws Exception
	 */
	private List<String> validateAndFetchStyleObjects(List<String> list,
			List<VFIRStyle> list2, Long requestId) throws Exception {

		List<String> invalidStyles = new ArrayList<String>();
		ItemRequest itemRequest = this.fulfillmentServiceDao
				.getItemRequestDetails(requestId);

		String styleId = null;
		VFIRStyle style = null;
		IdbDropshipDataTO idbDropshipDataTO = null;
		String vendorNumber = null;
		
		Iterator<String> iterator = list.iterator();
		while (iterator.hasNext()) {
			styleId = iterator.next();
			idbDropshipDataTO = this.fulfillmentServiceDao
					.getIDBRecordForStyleId(styleId);
			if (null != idbDropshipDataTO) {
				style = this.setKeyForStyle(itemRequest, styleId);
				style.setStyleDescription(idbDropshipDataTO.getStyleDesc());
				style.setAllColorIndicator(N);
				style.setAllSizeIndicator(N);
				style.setAllSkuIndicator(N);
				style.setUnitCost(idbDropshipDataTO.getIDBStyleUnitCost());
				style.setStatusCode(VFIRStyle.ACTIVE);
				style.setVendor(itemRequest.getVendor());
				
				vendorNumber = itemRequest.getVendor().getVendorNumber();
				if (vendorNumber.equals(idbDropshipDataTO.getVendorNumber())) {
					this.setAuditInfo(style);
					list2.add(style);
				}
			} else {
				invalidStyles.add(styleId);
			}
		}
		return invalidStyles;
	}

	/**
	 * This method sends email to the admin if there is any cost override for a
	 * style or sku.
	 * 
	 * @param itemRequestForm
	 * @param mailList
	 * @param styleOrSku
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void sendEmail(ItemRequestForm itemRequestForm, List mailList,
			String styleOrSku) throws Exception {
		NotificationType notification = new NotificationType();

		if (styleOrSku.equalsIgnoreCase(OVERRIDE_IN_STYLE)) {
			notification = (NotificationType) this.fulfillmentServiceDao
					.getNotificationTypeById(NotificationType.ITMRQST_PST_APPROVAL);
		} else if (styleOrSku.equalsIgnoreCase(OVERRIDE_IN_SKU)) {
			notification = (NotificationType) this.fulfillmentServiceDao
					.getNotificationTypeById(NotificationType.RQST_SKUPST_APPROVAL);
		}

		List<User> list = this.fulfillmentServiceDao
				.fetchUsersByType(UserType.ECOM_OPERATOR);
		String[] mailIdsOfUsers = new String[list.size()];
		int counter = 0;
		Iterator<User> iterator = list.iterator();
		while (iterator.hasNext()) {
			mailIdsOfUsers[counter] = iterator.next().getEmailAddress();
			counter++;
		}

		Properties properties = PropertyLoader.loadProperties(FTP_PROPERTIES);
		String urlPath = properties.getProperty(PROPERTY_URL_PATH);
		
		Map model = new HashMap();
		model.put(USER_RECIEVING_EMAIL, mailIdsOfUsers);
		model.put(ITEM_REQUEST_FORM, itemRequestForm);
		model.put(STYLE_SKU_DETAILS_IN_MAIL, mailList);
		model.put("URLPATH", urlPath);
		this.emailManager.sendEmailForRequest(notification, itemRequestForm
				.getUser(), model, itemRequestForm.getRequestId());
	}

	/**
	 * 
	 */
	public void removeStyleSku(Object request, Object style, Object color,
			Object size, Object upc) throws Exception {

		/**
		 * This list contains the skus that needs to be set to 'INACTIVE' status
		 */
		List<VFIRStyleSku> list = new ArrayList<VFIRStyleSku>();

		ItemRequest itemRequest = this.fulfillmentServiceDao
				.getItemRequestDetails(new Long((String) request));

		String styleId = (String) style;
		String colorId = (String) color;
		String sizeId = (String) size;
		String value = "all";

		/** Setting the primary key of VFIRStyleSku */
		VFIRStyleSku styleSku = new VFIRStyleSku();
		CompositeKeyVFIRStylesku compositeKeyVFIRStylesku = new CompositeKeyVFIRStylesku();
		compositeKeyVFIRStylesku.setItemRequest(itemRequest);
		compositeKeyVFIRStylesku.setVendorStyleId(styleId);
		// styleSku.setCompositeKeyVFIRStylesku(compositeKeyVFIRStylesku);

		if (value.equalsIgnoreCase(colorId)) {
			styleSku.setSizeDescription(sizeId);
		} else if (value.equalsIgnoreCase(sizeId)) {
			styleSku.setColor(colorId);
		} else {
			styleSku.setSizeDescription(sizeId);
			styleSku.setColor(colorId);
			compositeKeyVFIRStylesku.setBelkUpc((String) upc);
		}

		/** Fetching the skus that needs to be removed */
		list = this.fulfillmentServiceDao.fetchSkuExample(styleSku,
				compositeKeyVFIRStylesku);
		Iterator<VFIRStyleSku> iterator = list.iterator();
		while (iterator.hasNext()) {
			VFIRStyleSku styleSku2 = iterator.next();
			styleSku2.setStatusCode(VFIRStyleSku.INACTIVE);
		}

		/** Updating the status of sku to 'INACTIVE' */
		this.fulfillmentServiceDao.saveStyleSkus(list);
		
		this.removeStyleForAllInactiveSkus(itemRequest, styleId);
	}

	private void removeStyleForAllInactiveSkus(ItemRequest itemRequest, String styleId) throws Exception {
		StringBuilder query = new StringBuilder();
		query
		.append("SELECT * ")
		.append("FROM VIFR_STYLE_SKU ")
		.append("WHERE VNDR_ITEM_FULFMNT_RQST_ID=").append(itemRequest.getItemRequestID())
		.append(" AND VENDOR_STYLE_ID='").append(styleId).append("' AND STATUS_CD='").append(VFIRStyleSku.ACTIVE).append(QUOTE);
		
		log.info("Active skus for a vendor and request id===>" + query.toString());
		List<Object[]> list = this.fulfillmentServiceDao.fetchDetails(query.toString());
		if(null == list || list.isEmpty()) {
			query = new StringBuilder();
			query
			.append("UPDATE VIFR_STYLE ")
			.append("SET STATUS_CD='").append(VFIRStyle.INACTIVE)
			.append("' WHERE VNDR_ITEM_FULFMNT_RQST_ID=").append(itemRequest.getItemRequestID())
			.append(" AND VENDOR_STYLE_ID='").append(styleId).append("'");
			log.info("Removing style query===>" + query.toString());
			this.fulfillmentServiceDao.updateQuery(query.toString());
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void updateStyleSkus(ItemRequestForm itemRequestForm)
			throws Exception {

		/** This list contains the sku's that needs to be saved */
		List<StylesDTO> list = itemRequestForm.getSkuDetails();

		/**
		 * This list contains the skus whose cost has been overriden. This list
		 * is mailed to the admin for approval of the cost change
		 */
		List<StylesDTO> mailList = new ArrayList<StylesDTO>();

		ItemRequest itemRequest = this.fulfillmentServiceDao
				.getItemRequestDetails(new Long(itemRequestForm.getRequestId()));

		/**
		 * saveStatus represents if there is any change in the status of the
		 * request
		 */
		boolean saveStatus = false;

		/** isSendEmail represents if the request is posted */
		boolean isSendEmail = false;

		/** Fetching the skus which needs to be saved */
		List<VFIRStyleSku> list2 = this.fetchSkusBeforeSave(list, itemRequest);

		/** Saving the sku's in DB */
		this.fulfillmentServiceDao.saveStyleSkus(list2);

		/** Setting the status of the request if there is any change */
		if (itemRequestForm.getRequestStatus().equalsIgnoreCase(APPROVE)) {
			itemRequestForm.setRequestStatus(VFIRStatus.APPROVED);
			saveStatus = true;
		} else if (itemRequestForm.getRequestStatus().equalsIgnoreCase(REJECT)) {
			itemRequestForm.setRequestStatus(VFIRStatus.REJECT);
			saveStatus = true;
		} else if (itemRequestForm.getRequestStatus().equalsIgnoreCase(
				SAVE_POST)) {
			/**
			 * Fetching the sku's whose cost has been overridden and the request
			 * is posted
			 */
			mailList = fetchSkusWithOverridenCost(list);

			/**
			 * Condition: If no overrides in cost and the request is posted then
			 * change the status of request to 'APPROVED'
			 */
			if (mailList.isEmpty()) {
				itemRequestForm.setRequestStatus(VFIRStatus.APPROVED);
			} else {
				itemRequestForm.setRequestStatus(VFIRStatus.AWAITING_APPROVAL);
			}
			saveStatus = true;
			isSendEmail = true;
		}
		if (saveStatus) {
			itemRequest
					.setWorkflowStatus(fulfillmentServiceDao
							.getStatusForItemRequest(itemRequestForm
									.getRequestStatus()));
			if (isSendEmail) {
				itemRequest.setPostedBy(itemRequestForm.getPostedBy());
				itemRequest.setPostedDate(new Date());
			} else {
				itemRequest.setPostedBy(null);
				itemRequest.setPostedDate(null);
			}
		}

		itemRequest.setUpdatedBy(itemRequestForm.getUpdatedBy());
		itemRequest.setUpdatedDate(new Date(itemRequestForm
				.getUpdatedDate()));

		/** Updating the status of the request */
		this.fulfillmentServiceDao.updateItemRequest(itemRequest);
		
		/**
		 * Updating in the request workflow table if there is any change in the
		 * status of the request
		 */
		this.saveItemRequestWorkflow(itemRequest, BLANK, EDIT_ITEMREQUEST);

		if (itemRequestForm.getRequestStatus().equals(VFIRStatus.APPROVED)) {
			this.updateStylesSkus(itemRequest.getItemRequestID());
		}

		/** Send mail to admin if there is any cost override for sku's */
		if (isSendEmail && !mailList.isEmpty()) {
			sendEmail(itemRequestForm, mailList, OVERRIDE_IN_SKU);
		}
	}

	/**
	 * This method gets called before the changes made to the sku's from Sku
	 * exception screen is saved.
	 * 
	 * @param list
	 * @param itemRequest
	 * @return
	 */
	private List<VFIRStyleSku> fetchSkusBeforeSave(List<StylesDTO> list,
			ItemRequest itemRequest) {

		/** This list contains the sku's that needs to be saved in DB */
		List<VFIRStyleSku> list2 = new ArrayList<VFIRStyleSku>();
		
		StylesDTO stylesDTO = null;
		String styleId = null;
		String colorId = null;
		String sizeId = null;
		String value = null;
		VFIRStyleSku styleSku = null;
		CompositeKeyVFIRStylesku compositeKeyVFIRStylesku = null;
		List<VFIRStyleSku> list3 = null;
		VFIRStyleSku styleSku2 = null;

		Iterator<StylesDTO> iterator = list.iterator();
		while (iterator.hasNext()) {
			stylesDTO = iterator.next();
			styleId = stylesDTO.getVendorStyleId();
			colorId = stylesDTO.getColor();
			sizeId = stylesDTO.getSizeDescription();
			value = "all";

			/** Setting the primary key of sku */
			styleSku = new VFIRStyleSku();
			compositeKeyVFIRStylesku = new CompositeKeyVFIRStylesku();
			compositeKeyVFIRStylesku.setItemRequest(itemRequest);
			compositeKeyVFIRStylesku.setVendorStyleId(styleId);
			

			if (value.equalsIgnoreCase(colorId)) {
				styleSku.setSizeDescription(sizeId);
			} else if (value.equalsIgnoreCase(sizeId)) {
				styleSku.setColor(colorId);
			} else {
				styleSku.setSizeDescription(sizeId);
				styleSku.setColor(colorId);
				compositeKeyVFIRStylesku.setBelkUpc(stylesDTO.getBelkUpc());
			}

			/** Fetching the required sku's from DB */
			list3 = this.fulfillmentServiceDao.fetchSkuExample(styleSku,
					compositeKeyVFIRStylesku);

			Iterator<VFIRStyleSku> iterator2 = list3.iterator();
			while (iterator2.hasNext()) {
				styleSku2 = iterator2.next();
				styleSku2.setSkuexceptionInd(stylesDTO.getSkuexceptionInd());
				styleSku2.setOverrideCost(stylesDTO.getOverrideCost());
				styleSku2.setOverrideFee(stylesDTO.getOverrideFee());
				list2.add(styleSku2);
			}
		}
		return list2;
	}

	/**
	 * This method fetches all the sku's whose cost/fee has been overridden.
	 * 
	 * @param list
	 * @return
	 */
	private List<StylesDTO> fetchSkusWithOverridenCost(List<StylesDTO> list) {
		Iterator<StylesDTO> iterator = list.iterator();
		List<StylesDTO> list2 = new ArrayList<StylesDTO>();
		while (iterator.hasNext()) {
			StylesDTO styleSku = iterator.next();
			if ((null != styleSku.getOverrideCost()
					&& null != styleSku.getUnitCost() && styleSku.getUnitCost() != styleSku
					.getOverrideCost())
					|| (null != styleSku.getOverrideFee()
							&& null != styleSku.getUnitHandlingfee() && styleSku
							.getOverrideFee() != styleSku.getUnitHandlingfee())) {
				list2.add(styleSku);
			}
		}
		return list2;
	}

	/**
	 * 
	 */
	public void updateSkusFromStyles(ItemRequestForm itemRequestForm)
			throws Exception {
		ItemRequest itemRequest = this.fulfillmentServiceDao
				.getItemRequestDetails(new Long(itemRequestForm.getRequestId()));

		if (null != itemRequestForm.getStyleDetails()
				&& !itemRequestForm.getStyleDetails().isEmpty()) {
			List<VFIRStyleSku> list2 = this.fetchSkusForStyles(itemRequest,
					itemRequestForm.getStyleDetails());
			if (null != list2 && !list2.isEmpty()) {
				this.fulfillmentServiceDao.saveStyleSkus(list2);
			}
		}
	}

	/**
	 * This method fetches skus for given styles and saves it in DB for a given
	 * request.
	 * 
	 * @param itemRequest
	 * @param styles
	 * @return
	 * @throws Exception
	 */
	private List<VFIRStyleSku> fetchSkusForStyles(ItemRequest itemRequest,
			List<VFIRStyle> styles) throws Exception {

		StringBuffer stringBuffer = new StringBuffer();

		/** Constructing comma separated style id's */
		Iterator<VFIRStyle> iterator = styles.iterator();
		while (iterator.hasNext()) {
			VFIRStyle style = iterator.next();
			stringBuffer.append(QUOTE);
			stringBuffer.append(style.getCompositeKeyVIFRStyle()
					.getVendorStyleId());
			stringBuffer.append(QUOTE);
			stringBuffer.append(COMMA);
		}
		stringBuffer.deleteCharAt(stringBuffer.length() - 1);

		/** Fetching the Sku's from IDB table */
		List<IdbDropshipDataTO> list = this.fulfillmentServiceDao
				.getIDBRecordsForStyles(stringBuffer.toString());

		/** This list contains the actual Sku's */
		List<VFIRStyleSku> list2 = new ArrayList<VFIRStyleSku>();

		/**
		 * Fetching the vendor handling fee for a vendor & fulfillment service
		 * pair
		 */
		Double fee = this.setVendorHandlingFee(itemRequest.getVendor()
				.getVendorId(), itemRequest.getFulfillmentService()
				.getFulfillmentServiceID());

		Iterator<IdbDropshipDataTO> iterator2 = list.iterator();
		while (iterator2.hasNext()) {
			VFIRStyleSku styleSku = new VFIRStyleSku();
			CompositeKeyVFIRStylesku compositeKeyVFIRStylesku = new CompositeKeyVFIRStylesku();
			IdbDropshipDataTO idbDropshipDataTO = iterator2.next();
			compositeKeyVFIRStylesku.setItemRequest(itemRequest);
			compositeKeyVFIRStylesku.setVendorStyleId(idbDropshipDataTO
					.getVendorStyleID());
			compositeKeyVFIRStylesku.setBelkUpc(idbDropshipDataTO.getBelkUPC());
			styleSku.setCompositeKeyVFIRStylesku(compositeKeyVFIRStylesku);

			/** Check if the sku exists */
			List<VFIRStyleSku> list3 = this.fulfillmentServiceDao
					.getSkuFromId(compositeKeyVFIRStylesku);
			if (null != list3 && !list3.isEmpty()) {
				styleSku = list3.get(0);
			}

			if (null != idbDropshipDataTO.getVndrSize()) {
				styleSku.setSizeDescription(idbDropshipDataTO.getVndrSize());
			} else {
				styleSku.setSizeDescription(DEFAULT_SIZE_DESC);
			}
			if (null != idbDropshipDataTO.getColorDesc()) {
				styleSku.setColor(idbDropshipDataTO.getColorDesc());
			} else {
				styleSku.setColor(DEFAULT_COLOR_DESC);
			}
			styleSku.setSkuexceptionInd(DEFAULT_SKUEXCEPTION_IND_VALUE);
			styleSku.setUnitCost(idbDropshipDataTO.getIDBUPCUnitCost());
			styleSku.setUnitHandlingfee(fee);
			styleSku.setStatusCode(VFIRStyleSku.ACTIVE);
			this.setAuditInfo(styleSku);
			list2.add(styleSku);
		}
		return list2;
	}

	/**
	 * This method fetches sku's that needs to be displayed on Sku exception
	 * screen
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public List<StylesDTO> fetchStyleSkusForSkuException(List<VFIRStyle> list)
			throws Exception {
		List<StylesDTO> displaySkuList = new ArrayList<StylesDTO>();
		VFIRStyle style = null;

		Iterator<VFIRStyle> iterator = list.iterator();

		/**
		 * The sku's that are displayed on sku exception screen depends on the
		 * value of 'ALL SKU IND','ALL SIZE IND','ALL COLOR IND' for the style.
		 * If 'ALL SKU IND = Y OR ('ALL SIZE IND = Y' AND 'ALL COLOR IND = Y')
		 * then don't display any sku's related to that style. If 'ALL SKU IND =
		 * N AND ALL SIZE IND = Y AND ALL COLOR IND = N' then display the value
		 * of size as 'ALL'. If 'ALL SKU IND = N AND ALL SIZE IND = N AND ALL
		 * COLOR IND = Y' then display the value of color as 'ALL'.
		 */
		while (iterator.hasNext()) {
			style= iterator.next();
			String allSku = style.getAllSkuIndicator();
			String allSize = style.getAllSizeIndicator();
			String allColor = style.getAllColorIndicator();

			if (allSku.equalsIgnoreCase(SKU_IND_Y)
					|| (allSize.equalsIgnoreCase(ALL_SIZE_Y) && allColor
							.equalsIgnoreCase(ALL_COLOR_Y))) {
				log.info("Cost applies to all the sku's!");
			} else if (allSize.equalsIgnoreCase(ALL_SIZE_Y)) {
				processSizeExceptionSkuList(style.getCompositeKeyVIFRStyle()
						.getVendorStyleId(), style
						.getCompositeKeyVIFRStyle().getItemRequest().getItemRequestID(), displaySkuList, style
						.getStyleDescription());
			} else if (allColor.equalsIgnoreCase(ALL_COLOR_Y)) {
				processColorExceptionSkuList(style.getCompositeKeyVIFRStyle()
						.getVendorStyleId(), style
						.getCompositeKeyVIFRStyle().getItemRequest().getItemRequestID(), displaySkuList, style
						.getStyleDescription());
			} else if (allSize.equalsIgnoreCase(ALL_SIZE_N)
					&& allColor.equalsIgnoreCase(ALL_COLOR_N)) {
				processSkuExceptionList(style.getCompositeKeyVIFRStyle()
						.getVendorStyleId(), style
						.getCompositeKeyVIFRStyle().getItemRequest(), displaySkuList, style
						.getStyleDescription());
			}
		}
		System.out.println("No.of skus to be displayed====>" + displaySkuList.size());
		return displaySkuList;
	}

	private void processColorExceptionSkuList(String vendorStyleId,
			Long itemRequestID, List<StylesDTO> displaySkuList,
			String styleDescription) throws Exception{
		List<Object[]> list = this.fulfillmentServiceDao.fetchDistinctSizeSkuList(vendorStyleId, itemRequestID);
		Iterator<Object[]> it = list.iterator();
		while (it.hasNext()) {
			StylesDTO stylesDTO = new StylesDTO();
			Object[] skuDetail = it.next();
			stylesDTO.setSizeDescription((String)skuDetail[0]);
			stylesDTO.setColor(VALUE_AS_ALL.toUpperCase());
			stylesDTO.setBelkUpc((String)skuDetail[2]);
			stylesDTO.setSkuexceptionInd((String)skuDetail[3]);
			stylesDTO.setUnitCost(skuDetail[4]== null ? null:((BigDecimal) skuDetail[4]).doubleValue());
			stylesDTO.setUnitHandlingfee(skuDetail[5]== null ? null:((BigDecimal)skuDetail[5]).doubleValue());
			stylesDTO.setOverrideCost(skuDetail[6]== null ? null:((BigDecimal)skuDetail[6]).doubleValue());
			stylesDTO.setOverrideFee(skuDetail[7]== null ? null:((BigDecimal)skuDetail[7]).doubleValue());
			stylesDTO.setStyleDesc(styleDescription);
			stylesDTO.setVendorStyleId(vendorStyleId);
			displaySkuList.add(stylesDTO);
		}
	}
	
	private void processSizeExceptionSkuList(String vendorStyleId,
			Long itemRequestID, List<StylesDTO> displaySkuList,
			String styleDescription) throws Exception{
		List<Object[]> list = this.fulfillmentServiceDao.fetchDistinctColorSkuList(vendorStyleId, itemRequestID);
		Iterator<Object[]> it = list.iterator();
		while (it.hasNext()) {
			StylesDTO stylesDTO = new StylesDTO();
			Object[] skuDetail = it.next();
			stylesDTO.setColor((String)skuDetail[0]);
			stylesDTO.setSizeDescription(VALUE_AS_ALL.toUpperCase());
			stylesDTO.setBelkUpc((String)skuDetail[2]);
			stylesDTO.setSkuexceptionInd((String)skuDetail[3]);
			stylesDTO.setUnitCost(skuDetail[4]== null ? null:((BigDecimal) skuDetail[4]).doubleValue());
			stylesDTO.setUnitHandlingfee(skuDetail[5]== null ? null:((BigDecimal)skuDetail[5]).doubleValue());
			stylesDTO.setOverrideCost(skuDetail[6]== null ? null:((BigDecimal)skuDetail[6]).doubleValue());
			stylesDTO.setOverrideFee(skuDetail[7]== null ? null:((BigDecimal)skuDetail[7]).doubleValue());
			stylesDTO.setStyleDesc(styleDescription);
			stylesDTO.setVendorStyleId(vendorStyleId);
			displaySkuList.add(stylesDTO);
		}
	}


	/**
	 * This method is called to populate the skus that need to be displayed on
	 * sku exception screen depending on value of size,sku and color indicators
	 * for that style.
	 * 
	 * @param skus
	 * @param stylesDto
	 * @param desc
	 * @param sizeColor
	 */
	private void processSkuExceptionList(String vendorStyleId,
			ItemRequest itemRequest, List<StylesDTO> displaySkuList,
			String styleDescription) throws Exception{
		List<VFIRStyleSku> skuList = this.fulfillmentServiceDao.getSkusForStyle(vendorStyleId, itemRequest);
		Iterator<VFIRStyleSku> iterator = skuList.iterator();
		while (iterator.hasNext()) {
			VFIRStyleSku styleSku = (VFIRStyleSku) iterator.next();
			StylesDTO stylesDTO= new StylesDTO();
			stylesDTO.setColor(styleSku.getColor());
			stylesDTO.setSizeDescription(styleSku.getSizeDescription());
			stylesDTO.setBelkUpc(styleSku.getCompositeKeyVFIRStylesku().getBelkUpc());
			stylesDTO.setSkuexceptionInd(styleSku.getSkuexceptionInd());
			stylesDTO.setUnitCost(styleSku.getUnitCost());
			stylesDTO.setUnitHandlingfee(styleSku.getUnitHandlingfee());
			stylesDTO.setOverrideCost(styleSku.getOverrideCost());
			stylesDTO.setOverrideFee(styleSku.getOverrideFee());
			stylesDTO.setStyleDesc(styleDescription);
			stylesDTO.setVendorStyleId(vendorStyleId);
			displaySkuList.add(stylesDTO);
		}
	}
	

	/**
	 * 
	 */
	public void getRequestHistoryDetails(RequestHistoryForm requestHistoryForm,
			Long vendorId, Long serviceId) throws Exception {

		List<RequestDTO> list = new ArrayList<RequestDTO>();
		StringBuffer buffer = new StringBuffer();
		buffer
				.append("SELECT R.vndr_item_fulfmnt_rqst_id,R.item_rqst_descr,R.effective_date,");
		buffer
				.append("       R.VIFR_WORKFLOW_ACTION_CD,SRC.\"NAME\" SOURCE,W.\"NAME\" STATUS,R.created_by,R.created_date,");
		buffer.append("       R.updated_by,R.updated_date ");
		buffer
				.append("FROM   vndr_item_fulfmnt_rqst R JOIN item_source SRC ON (R.item_source_cd=SRC.item_source_cd)");
		buffer
				.append("       JOIN vifr_workflow_status W ON (R.VIFR_WORKFLOW_STATUS_CD = W.VIFR_WORKFLOW_STATUS_CD) ");

		if (!requestHistoryForm.getStyleId().equals(BLANK)
				|| !requestHistoryForm.getStyleDesc().equals(BLANK)) {
			buffer
					.append("   JOIN vifr_style VS ON (VS.vndr_item_fulfmnt_rqst_id = R.vndr_item_fulfmnt_rqst_id) ");
		}
		if (!requestHistoryForm.getVendorUpc().equals(BLANK)) {
			buffer.append("   JOIN vendor V ON (R.vendor_id = V.vendor_id)");
			buffer
					.append("   JOIN vifr_style_sku SKU ON (R.vndr_item_fulfmnt_rqst_id=SKU.vndr_item_fulfmnt_rqst_id)");
			buffer
					.append("   JOIN IDB_DROP_SHIP_FEED I ON (I.vendor_number = V.vendor_number AND I.belk_upc = SKU.belk_upc) ");
		}
		if (requestHistoryForm.getVendorUpc().equals(BLANK)
				&& !requestHistoryForm.getBelkUpc().equals(BLANK)) {
			buffer
					.append("   JOIN vifr_style_sku SKU ON (R.vndr_item_fulfmnt_rqst_id=SKU.vndr_item_fulfmnt_rqst_id) ");
		}
		buffer.append("WHERE R.vendor_id =" + vendorId
				+ "AND R.fulfmnt_service_id=" + serviceId);

		if (!requestHistoryForm.getRequestId().equals(BLANK)) {
			buffer.append("  AND R.vndr_item_fulfmnt_rqst_id="
					+ requestHistoryForm.getRequestId());
		}
		if (!requestHistoryForm.getRequestDesc().equals(BLANK)) {
			buffer.append("  AND LOWER(R.item_rqst_descr)='"
					+ this.escapeSpecialCharacters(
							requestHistoryForm.getRequestDesc()).toLowerCase()
							.trim() + QUOTE);
		}
		if (!requestHistoryForm.getStyleId().equals(BLANK)) {
			buffer.append("  AND VS.vendor_style_id='"
					+ this.escapeSpecialCharacters(requestHistoryForm
							.getStyleId()) + "' AND VS.status_cd='"
					+ VFIRStyle.ACTIVE + QUOTE);
		}
		if (!requestHistoryForm.getStyleDesc().equals(BLANK)) {
			buffer.append("  AND LOWER(VS.style_descr)='"
					+ this.escapeSpecialCharacters(
							requestHistoryForm.getStyleDesc()).toLowerCase()
							.trim() + "' AND VS.status_cd='" + VFIRStyle.ACTIVE
					+ QUOTE);
		}
		if (!requestHistoryForm.getVendorUpc().equals(BLANK)) {
			buffer.append("  AND I.vendor_upc='"
					+ this.escapeSpecialCharacters(requestHistoryForm
							.getVendorUpc()) + QUOTE);
		}
		if (!requestHistoryForm.getBelkUpc().equals(BLANK)) {
			buffer.append("  AND SKU.belk_upc='"
					+ this.escapeSpecialCharacters(requestHistoryForm
							.getBelkUpc()) + "' AND SKU.status_cd='"
					+ VFIRStyleSku.ACTIVE + QUOTE);
		}
		if (!requestHistoryForm.getEffectiveStartDate().equals(BLANK)
				&& !requestHistoryForm.getEffectiveEndDate().equals(BLANK)) {
			buffer.append("  AND TRUNC(R.effective_date) BETWEEN TO_DATE('"
					+ requestHistoryForm.getEffectiveStartDate()
					+ "','MM/DD/YYYY')");
			buffer.append("  AND TO_DATE('"
					+ requestHistoryForm.getEffectiveEndDate()
					+ "','MM/DD/YYYY')");
		} else if (!requestHistoryForm.getEffectiveStartDate().equals(BLANK)
				&& requestHistoryForm.getEffectiveEndDate().equals(BLANK)) {
			buffer.append("  AND TRUNC(R.effective_date) >= TO_DATE('"
					+ requestHistoryForm.getEffectiveStartDate()
					+ "','MM/DD/YYYY')");
		} else if (requestHistoryForm.getEffectiveStartDate().equals(BLANK)
				&& !requestHistoryForm.getEffectiveEndDate().equals(BLANK)) {
			buffer.append("  AND TRUNC(R.effective_date) <= TO_DATE('"
					+ requestHistoryForm.getEffectiveEndDate()
					+ "','MM/DD/YYYY')");
		}

		if (!requestHistoryForm.getStatusFilter().equals(BLANK)
				&& !requestHistoryForm.getStatusFilter().equals("ALL")) {
			if (requestHistoryForm.getStatusFilter().equals("APPROVED")) {
				buffer.append("  AND W.VIFR_WORKFLOW_STATUS_CD='").append(
						VFIRStatus.APPROVED).append("'");
			} else if (requestHistoryForm.getStatusFilter().equals("REJECTED")) {
				buffer.append("  AND W.VIFR_WORKFLOW_STATUS_CD='").append(
						VFIRStatus.REJECT).append("'");
			} else if (requestHistoryForm.getStatusFilter()
					.equals("UNAPPROVED")) {

				buffer.append("  AND W.VIFR_WORKFLOW_STATUS_CD IN('").append(
						VFIRStatus.AWAITING_APPROVAL).append("','").append(
								VFIRStatus.OPEN).append("')");
			}

		}
		log.info("Request details query===>" + buffer.toString());
		List<Object[]> list2 = this.fulfillmentServiceDao.fetchDetails(buffer
				.toString());
		Iterator<Object[]> iterator = list2.iterator();
		while (iterator.hasNext()) {
			Object[] objects = iterator.next();
			RequestDTO requestDTO = new RequestDTO();
			BigDecimal bigDecimal = (BigDecimal) objects[0];
			requestDTO.setRequestId(bigDecimal.longValue());
			requestDTO.setRequestDesc((String) objects[1]);
			requestDTO.setEffectiveDate(getFormattedDate((Date) objects[2]));
			requestDTO.setAction((String) objects[3]);
			requestDTO.setSource((String) objects[4]);
			requestDTO.setStatus((String) objects[5]);
			requestDTO.setCreatedBy((String) objects[6]);
			requestDTO.setCreateDate(getFormattedDate((Date) objects[7]));
			requestDTO.setUpdatedBy((String) objects[8]);
			requestDTO.setUpdatedDate(getFormattedDate((Date) objects[9]));
			list.add(requestDTO);
		}
		requestHistoryForm.setRequestDetails(list);
	}

	/**
	 * This method fetches all the styles for associated vendor and fulfillment
	 * service
	 */
	public void getStyleSkuDetails(StylesSkuForm stylesSkuForm, Long vendorId,
			Long serviceId) throws Exception {

		List<StyleSkuDTO> skus = new ArrayList<StyleSkuDTO>();

		/** Fetch the number of active styles for a vendor & fulfillment service */
		this.fetchNumberOfActiveStyles(stylesSkuForm, vendorId, serviceId);

		StringBuffer query = new StringBuffer();
		query.append("SELECT DISTINCT VENDOR_SYTLE_ID,STYLE_DESCR ");
		query
				.append("FROM FULFMNT_SERVICE_VENDOR VF JOIN VENDOR V ON (VF.VENDOR_ID=V.VENDOR_ID)");
		query
				.append("     JOIN IDB_DROP_SHIP_FEED I ON (I.VENDOR_NUMBER=V.VENDOR_NUMBER) ");
		query.append("WHERE VF.vendor_id = " + vendorId
				+ " AND VF.fulfmnt_service_id = " + serviceId);

		if (!stylesSkuForm.getStyleId().equals(BLANK)) {
			query.append("  AND I.vendor_sytle_id='"
					+ this.escapeSpecialCharacters(stylesSkuForm.getStyleId())
					+ QUOTE);
		}
		if (!stylesSkuForm.getStyleDesc().equals(BLANK)) {
			query.append("  AND LOWER(I.style_descr)='"
					+ this
							.escapeSpecialCharacters(
									stylesSkuForm.getStyleDesc()).toLowerCase()
							.trim() + QUOTE);
		}
		if (!stylesSkuForm.getBelkUpc().equals(BLANK)) {
			query.append("  AND I.belk_upc='"
					+ this.escapeSpecialCharacters(stylesSkuForm.getBelkUpc())
					+ QUOTE);
		}
		if (!stylesSkuForm.getVendorUpc().equals(BLANK)) {
			query.append("  AND I.vendor_upc='"
					+ this
							.escapeSpecialCharacters(stylesSkuForm
									.getVendorUpc()) + QUOTE);
		}
		if (null != stylesSkuForm.getUserDeptsOnly()
				&& !stylesSkuForm.getUserDeptsOnly().equals(BLANK)
				&& stylesSkuForm.getUserDeptsOnly()) {
			query
					.append("  AND I.dept_id IN (SELECT D.DEPT_CD FROM CAR_USER_DEPARTMENT C JOIN DEPARTMENT D ON (C.DEPT_ID=D.DEPT_ID) WHERE C.CAR_USER_ID="
							+ stylesSkuForm.getUser().getId() + ")");
		}
		log.info("Sku query===>" + query.toString());
		List<Object[]> list = this.fulfillmentServiceDao.fetchDetails(query
				.toString());

		Map<String, String> dropshipStatus = this.fetchDropShipStatusOfStyles(
				vendorId, serviceId);
		Integer noOfActiveSkus = 0;
		if (null != dropshipStatus.get(null)) {
			noOfActiveSkus = new Integer(dropshipStatus.get(null));
		}
		stylesSkuForm.setNoOfActiveSkus(noOfActiveSkus.toString());

		Map<String, Object[]> idbStatus = this.fetchIDBStausOfStyles(vendorId,
				serviceId);
		Iterator<Object[]> iterator = list.iterator();

		while (iterator.hasNext()) {
			Object[] objects = iterator.next();
			String styleId = (String) objects[0];
			String styleDesc = (String) objects[1];

			Integer noOfDropshipSkus = 0;

			if (null != dropshipStatus.get(styleId)) {
				noOfDropshipSkus = new Integer(dropshipStatus.get(styleId));
			}

			Object[] objects2 = idbStatus.get(styleId);

			Integer noOfIDBYStatus = ((BigDecimal) objects2[1]).intValue();
			Integer noOfIDBNStatus = ((BigDecimal) objects2[2]).intValue();

			StyleSkuDTO styleSkuDTO = new StyleSkuDTO();
			styleSkuDTO.setStyleId(styleId);
			styleSkuDTO.setStyleDesc(styleDesc);

			if (null != noOfDropshipSkus && noOfDropshipSkus > 0) {
				styleSkuDTO.setDropShipStatus(DROP_SHIP_STATUS_ACTIVE);
			} else {
				styleSkuDTO.setDropShipStatus(DROP_SHIP_STATUS_INACTIVE);
			}
			styleSkuDTO.setNoOfDropshipSkus(noOfDropshipSkus);

			if (noOfIDBYStatus > 0 && noOfIDBNStatus > 0) {
				styleSkuDTO.setIdbAllowDropship(IDB_ALLOW_DROPSHIP_V);
			} else if (noOfIDBYStatus > 0) {
				styleSkuDTO.setIdbAllowDropship(IDB_ALLOW_DROPSHIP_Y);
			} else {
				styleSkuDTO.setIdbAllowDropship(IDB_ALLOW_DROPSHIP_N);
			}
			styleSkuDTO.setNoOfIdbDropshipSkus(noOfIDBYStatus);

			if (isValidDTO(styleSkuDTO, stylesSkuForm)) {
				skus.add(styleSkuDTO);
			}
		}
		stylesSkuForm.setList(skus);
	}

	/**
	 * This method filters the styles according to the status of dropship/ IDB
	 * as selected by the user.
	 * 
	 * @param styleSkuDTO
	 * @param stylesSkuForm
	 * @return
	 */
	private boolean isValidDTO(StyleSkuDTO styleSkuDTO,
			StylesSkuForm stylesSkuForm) {
		Boolean isValidDTO = Boolean.FALSE;

		if (stylesSkuForm.getDropShipStatus().equals(BLANK)
				&& stylesSkuForm.getIdbStatus().equals(BLANK)) {
			isValidDTO = Boolean.TRUE;
		} else if (!stylesSkuForm.getDropShipStatus().equals(BLANK)
				&& !stylesSkuForm.getIdbStatus().equals(BLANK)) {
			String styleDropshipStatus = styleSkuDTO.getDropShipStatus();
			String styleIdbStatus = styleSkuDTO.getIdbAllowDropship();
			if (styleDropshipStatus.equalsIgnoreCase(stylesSkuForm
					.getDropShipStatus())
					&& styleIdbStatus.equalsIgnoreCase(stylesSkuForm
							.getIdbStatus())) {
				isValidDTO = Boolean.TRUE;
			}
		} else if (!stylesSkuForm.getDropShipStatus().equals(BLANK)) {
			String styleDropshipStatus = styleSkuDTO.getDropShipStatus();
			if (styleDropshipStatus.equalsIgnoreCase(stylesSkuForm
					.getDropShipStatus())) {
				isValidDTO = Boolean.TRUE;
			}
		} else {
			String styleIdbStatus = styleSkuDTO.getIdbAllowDropship();
			if (styleIdbStatus.equalsIgnoreCase(stylesSkuForm.getIdbStatus())) {
				isValidDTO = Boolean.TRUE;
			}
		}
		return isValidDTO;
	}

	/**
	 * This method fetches the number of active styles for a vendor &
	 * fulfillment service
	 * 
	 * @param stylesSkuForm
	 * @param vendorId
	 * @param serviceId
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void fetchNumberOfActiveStyles(StylesSkuForm stylesSkuForm,
			Long vendorId, Long serviceId) throws Exception {
		StringBuffer query = new StringBuffer();
		query.append("SELECT Count(DISTINCT(VENDOR_STYLE_ID)) ");
		query.append("FROM VIFR_STYLE VSS JOIN vndr_item_fulfmnt_rqst R ");
		query.append("     ON (R.vndr_item_fulfmnt_rqst_id=VSS.vndr_item_fulfmnt_rqst_id) ")
			 .append("     JOIN VENDOR V ON (R.vendor_id=V.vendor_id) ")
			 .append("     JOIN IDB_DROP_SHIP_FEED I ON (I.vendor_number=V.vendor_number and VSS.vendor_style_id=I.vendor_sytle_id) ");
		query.append("WHERE R.vendor_id = " + vendorId
				+ " AND R.fulfmnt_service_id=" + serviceId
				+ " AND  VSS.STATUS_CD='" + VFIRStyle.ACTIVE + QUOTE);

		log.info("No. of active styles====>" + query.toString());
		List<Object> list = (List<Object>) this.fulfillmentServiceDao
				.fetchCount(query.toString());
		if (null != list && !list.isEmpty()) {
			BigDecimal bigDecimal = (BigDecimal) list.get(0);
			stylesSkuForm.setNoOfActiveStyles(bigDecimal.toString());
		} else {
			stylesSkuForm.setNoOfActiveStyles(NO_OF_ACTIVE_STYLES_IS_ZERO);
		}
	}

	/**
	 * This method fetches the number of dropship active sku's in OMA for all
	 * the styles associated with a vendor & fulfillment service pair.
	 * 
	 * @param vendorId
	 * @param serviceId
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> fetchDropShipStatusOfStyles(Long vendorId,
			Long serviceId) throws Exception {
		StringBuffer query = new StringBuffer();
		query.append("SELECT VENDOR_STYLE_ID,Count(VENDOR_STYLE_ID) ");
		query.append("FROM (");
		query.append("SELECT DISTINCT VENDOR_STYLE_ID,BELK_UPC ");
		query
				.append("FROM VIFR_STYLE_SKU VSS JOIN VNDR_ITEM_FULFMNT_RQST R ON (R.vndr_item_fulfmnt_rqst_id=VSS.vndr_item_fulfmnt_rqst_id) ");
		query.append("WHERE R.vendor_id =" + vendorId
				+ " AND R.fulfmnt_service_id=" + serviceId
				+ " AND VSS.STATUS_CD='" + VFIRStyleSku.ACTIVE + QUOTE);
		query.append(") GROUP BY ROLLUP(vendor_style_id)");

		log.info("Drop ship status of styles===>" + query.toString());
		List<Object[]> list = this.fulfillmentServiceDao.fetchDetails(query
				.toString());
		Map<String, String> map = new HashMap<String, String>();

		Iterator<Object[]> iterator = list.iterator();
		while (iterator.hasNext()) {
			Object[] objects = iterator.next();
			String styleId = (String) objects[0];
			String statusCount = ((BigDecimal) objects[1]).toString();
			map.put(styleId, statusCount);
		}
		return map;
	}

	/**
	 * This method fetches the IDB status of all styles associated with a vendor
	 * & fulfillment service pair.
	 * 
	 * @param vendorId
	 * @param serviceId
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object[]> fetchIDBStausOfStyles(Long vendorId,
			Long serviceId) throws Exception {
		StringBuffer query = new StringBuffer();
		query.append("SELECT vendor_sytle_id,");
		query
				.append("       Count(Decode(sku_drop_ship_flag,'Y','Y')) IDB_DROP_SHIP_STATUS_Y,");
		query
				.append("       Count(Decode(sku_drop_ship_flag,'N','N')) IDB_DROP_SHIP_STATUS_N ");
		query
				.append("FROM fulfmnt_service_vendor VF JOIN VENDOR V ON (VF.vendor_id=V.vendor_id)");
		query
				.append("	   JOIN IDB_DROP_SHIP_FEED I ON (V.vendor_number = I.vendor_number) ");
		query.append("WHERE VF.vendor_id =" + vendorId
				+ " AND VF.fulfmnt_service_id = " + serviceId);
		query.append(" GROUP BY vendor_sytle_id");
		List<Object[]> list = this.fulfillmentServiceDao.fetchDetails(query
				.toString());
		Map<String, Object[]> map = new HashMap<String, Object[]>();

		Iterator<Object[]> iterator = list.iterator();
		while (iterator.hasNext()) {
			Object[] objects = iterator.next();
			String styleId = (String) objects[0];
			map.put(styleId, objects);
		}
		return map;
	}

	/**
	 * 
	 */
	public void getSkuDetails(StylesSkuForm stylesSkuForm, Long vendorId,
			Long serviceId, String vendorUpc) throws Exception {
		/** Step1 : Fetch Date last request and Indicator details */
		this.fetchStylelevelDetails(stylesSkuForm, vendorId, serviceId);

		/** Step2 : Fetch date last IDB update */
		this.fetchLastIDBUpdateDate(stylesSkuForm, vendorId, serviceId);

		/** Step3 : Fetch sku details */
		this.fetchSkuLevelDetails(stylesSkuForm, vendorId, serviceId);

		/** Step4 : Fetch request details */
		if (null != vendorUpc) {
			this.fetchRequestlevelDetails(stylesSkuForm, vendorId, serviceId,
					vendorUpc);
		}
	}

	/**
	 * This method fetches the style details for a given style.
	 * 
	 * @param stylesSkuForm
	 * @param vendorId
	 * @param serviceId
	 * @throws Exception
	 */
	private void fetchStylelevelDetails(StylesSkuForm stylesSkuForm,
			Long vendorId, Long serviceId) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM ");
		query
				.append("(SELECT R.updated_date,VS.applies_to_all_sku_ind,VS.applies_to_all_color_ind,VS.applies_to_all_size_ind ");
		query
				.append("FROM VIFR_STYLE VS JOIN vndr_item_fulfmnt_rqst R ON (VS.vndr_item_fulfmnt_rqst_id = R.vndr_item_fulfmnt_rqst_id) ");
		query
				.append("     JOIN IDB_DROP_SHIP_FEED I ON (VS.vendor_style_id = I.vendor_sytle_id) ");
		query.append("WHERE VS.vendor_style_id = '"
				+ this.escapeSpecialCharacters(stylesSkuForm.getStyleId())
				+ "' AND R.VENDOR_ID=" + vendorId
				+ " AND R.fulfmnt_service_id=" + serviceId);
		query.append(" ORDER BY R.updated_date DESC) WHERE ROWNUM = 1");

		List<Object[]> list = this.fulfillmentServiceDao.fetchDetails(query
				.toString());

		Iterator<Object[]> iterator = list.iterator();
		while (iterator.hasNext()) {
			Object[] objects = iterator.next();
			stylesSkuForm
					.setDateLastRequest(getFormattedDate((Date) objects[0]));
			stylesSkuForm.setAllSku((String) objects[1]);
			stylesSkuForm.setAllColor((String) objects[2]);
			stylesSkuForm.setAllSize((String) objects[3]);
		}
	}

	/**
	 * This method fetches the last IDB update date for a given style.
	 * 
	 * @param stylesSkuForm
	 * @param vendorId
	 * @param serviceId
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void fetchLastIDBUpdateDate(StylesSkuForm stylesSkuForm,
			Long vendorId, Long serviceId) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM ");
		query.append("(SELECT MAX(I.updated_date) ");
		query.append("FROM IDB_DROP_SHIP_FEED I JOIN VENDOR V ON (I.vendor_number = V.vendor_number)");
		query.append("     JOIN fulfmnt_service_vendor VF ON (V.vendor_id = VF.vendor_id) ");
		query.append("WHERE VF.vendor_id =" + vendorId
				+ " AND VF.fulfmnt_service_id =" + serviceId + " AND I.vendor_sytle_id='")
		.append(stylesSkuForm.getStyleId()).append(QUOTE).append(")");

		log.info("Last idb update date for a style==>" + query.toString());
		List<Object> list = (List<Object>) this.fulfillmentServiceDao
				.fetchCount(query.toString());
		Date date = (Date) list.get(0);
		stylesSkuForm.setDateLastIDBUpdate(getFormattedDate(date));
	}

	/**
	 * This method fetches sku details of a given style.
	 * 
	 * @param stylesSkuForm
	 * @param vendorId
	 * @param serviceId
	 * @throws Exception
	 */
	private void fetchSkuLevelDetails(StylesSkuForm stylesSkuForm,
			Long vendorId, Long serviceId) throws Exception {
		StringBuilder query = new StringBuilder();
		query
		.append("SELECT A.VENDOR_UPC,A.BELK_UPC,A.STYLE_DESCR,A.sku_drop_ship_flag, ")
		.append("       A.UPDATED_DATE,A.UPDATED_BY ")
		.append("FROM fulfmnt_service_vendor VF JOIN VENDOR V ON (VF.vendor_id=V.vendor_id)")
		.append("     JOIN IDB_DROP_SHIP_FEED A ON (V.vendor_number = A.vendor_number) ")
		.append("WHERE VF.vendor_id =").append(vendorId).append(" AND VF.fulfmnt_service_id=").append(serviceId)
		.append("       AND A.VENDOR_SYTLE_ID='").append(stylesSkuForm.getStyleId()).append("'");

		log.info("Sku details query in style property===>" + query.toString());
		List<Object[]> list = this.fulfillmentServiceDao.fetchDetails(query
				.toString());
		Map<String, String> statusMap = this.fetchSkuStatus(vendorId, serviceId, stylesSkuForm.getStyleId());
		Map<String, String> sourceMap = this.fetchSourceOfLastRequestForSku(vendorId, serviceId, stylesSkuForm.getStyleId());
		
		String approvedRequest = this.isApprovedRequestsForStyle(stylesSkuForm.getStyleId(), vendorId, serviceId);
		List<StyleSkuDTO> list2 = new ArrayList<StyleSkuDTO>();

		Iterator<Object[]> iterator = list.iterator();
		while (iterator.hasNext()) {
			Object[] objects = iterator.next();
			StyleSkuDTO styleSkuDTO = new StyleSkuDTO();
			styleSkuDTO.setVendorUpc((String) objects[0]);
			styleSkuDTO.setBelkUpc((String) objects[1]);
			styleSkuDTO.setStyleDesc((String) objects[2]);
			styleSkuDTO.setIdbAllowDropship((String) objects[3]);
			styleSkuDTO.setUpdatedDate(getFormattedDate((Date) objects[4]));
			styleSkuDTO.setUpdatedBy((String) objects[5]);
			
			String status = statusMap.get((String) objects[1]);
			if(null == status) {
				styleSkuDTO.setDropShipStatus(INACTIVE);
			} else {
				styleSkuDTO.setDropShipStatus(ACTIVE);
			}

			styleSkuDTO.setSource(sourceMap.get((String) objects[1]));
			
			styleSkuDTO.setApprovedRequest(approvedRequest);
			list2.add(styleSkuDTO);
		}
		stylesSkuForm.setList(list2);
	}

	private Map<String,String> fetchSkuStatus(Long vendorId, Long serviceId, String styleId) throws Exception {
		StringBuilder query = new StringBuilder();
		query
		.append("SELECT DISTINCT BELK_UPC,VSS.STATUS_CD ")
		.append("FROM   VIFR_STYLE_SKU VSS JOIN VNDR_ITEM_FULFMNT_RQST R ")
		.append("       ON (R.vndr_item_fulfmnt_rqst_id=VSS.vndr_item_fulfmnt_rqst_id) ")
		.append("WHERE R.vendor_id =").append(vendorId).append(" AND R.fulfmnt_service_id=").append(serviceId)
		.append("      AND VSS.VENDOR_STYLE_ID='").append(styleId).append("' AND VSS.STATUS_CD='").append(VFIRStyleSku.ACTIVE).append(QUOTE);
		
		log.info("Status of all skus for a given style===>" + query.toString());
		List<Object[]> list = this.fulfillmentServiceDao.fetchDetails(query
				.toString());
		
		Map<String, String> map = new HashMap<String, String>();
		if(null != list && !list.isEmpty()) {
			Iterator<Object[]> iterator = list.iterator();
			while(iterator.hasNext()) {
				Object[] objects = iterator.next();
				map.put((String) objects[0], (String)objects[1]);
			}
		}
		return map;
	}
	
	private Map<String, String> fetchSourceOfLastRequestForSku(Long vendorId, Long serviceId, String styleId) throws Exception {
		StringBuilder query = new StringBuilder();
		query
		.append("SELECT DISTINCT A.BELK_UPC,B.\"NAME\" ")
		.append("FROM ")
		.append("(SELECT BELK_UPC,Max(R.UPDATED_DATE) AS MAX_UPDATED_DATE")
		.append(" FROM   VIFR_STYLE_SKU VSS JOIN VNDR_ITEM_FULFMNT_RQST R")
		.append("        ON (R.vndr_item_fulfmnt_rqst_id=VSS.vndr_item_fulfmnt_rqst_id)")
		.append(" WHERE  R.vendor_id =").append(vendorId).append(" AND R.fulfmnt_service_id=").append(serviceId)
		.append("        AND VSS.VENDOR_STYLE_ID='").append(styleId).append("'")
		.append(" GROUP BY BELK_UPC")
		.append(") A ")
		.append("JOIN ")
		.append("(")
		.append("SELECT BELK_UPC,R.UPDATED_DATE,R.vndr_item_fulfmnt_rqst_id,SRC.\"NAME\" ")
		.append("FROM   VIFR_STYLE_SKU VSS JOIN VNDR_ITEM_FULFMNT_RQST R")
		.append("       ON (R.vndr_item_fulfmnt_rqst_id=VSS.vndr_item_fulfmnt_rqst_id)")
		.append("       JOIN ITEM_SOURCE SRC ON (R.ITEM_SOURCE_CD=SRC.ITEM_SOURCE_CD) ")
		.append("WHERE  R.vendor_id =").append(vendorId).append(" AND R.fulfmnt_service_id=").append(serviceId)
		.append("       AND VSS.VENDOR_STYLE_ID='").append(styleId).append("'")
		.append(") B ON (A.BELK_UPC=B.BELK_UPC AND A.MAX_UPDATED_DATE=B.UPDATED_DATE)");
		
		log.info("Last source of request for skus in a given style===>" + query.toString());
		List<Object[]> list = this.fulfillmentServiceDao.fetchDetails(query
				.toString());
		
		Map<String, String> map = new HashMap<String, String>();
		if(null != list && !list.isEmpty()) {
			Iterator<Object[]> iterator = list.iterator();
			while(iterator.hasNext()) {
				Object[] objects = iterator.next();
				map.put((String) objects[0], (String)objects[1]);
			}
		}
		return map;
	}
	/**
	 * This method checks if any request for a given UPC is approved.
	 * 
	 * @param vendorUpc
	 * @param vendorId
	 * @param serviceId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean isApprovedRequestsForSku(String vendorUpc, Long vendorId,
			Long serviceId) throws Exception {
		Boolean isApprovedRequestExists = Boolean.FALSE;
		StringBuilder query = new StringBuilder();
		
		query.append("SELECT I.VENDOR_UPC ");
		query.append("FROM VIFR_STYLE_SKU VSS JOIN IDB_DROP_SHIP_FEED I ON (VSS.VENDOR_STYLE_ID=I.VENDOR_SYTLE_ID)");
		query.append("     JOIN vndr_item_fulfmnt_rqst R ON (VSS.vndr_item_fulfmnt_rqst_id=R.vndr_item_fulfmnt_rqst_id) ");
		query.append("WHERE I.VENDOR_UPC='" + vendorUpc
				+ "' AND R.VIFR_WORKFLOW_STATUS_CD='" + VFIRStatus.APPROVED
				+ QUOTE);
		query.append("      AND R.VENDOR_ID=" + vendorId
				+ " AND R.fulfmnt_service_id=" + serviceId);

		List<Object> list = (List<Object>) this.fulfillmentServiceDao
				.fetchCount(query.toString());

		if (null != list && !list.isEmpty()) {
			isApprovedRequestExists = Boolean.TRUE;
		}
		return isApprovedRequestExists;
	}
	
	/**
	 * This method checks if any request for a given UPC is approved.
	 * 
	 * @param vendorUpc
	 * @param vendorId
	 * @param serviceId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private String isApprovedRequestsForStyle(String styleId, Long vendorId,
			Long serviceId) throws Exception {
		StringBuilder query = new StringBuilder();
		
		query.append("SELECT I.VENDOR_UPC ");
		query.append("FROM VIFR_STYLE VSS JOIN IDB_DROP_SHIP_FEED I ON (VSS.VENDOR_STYLE_ID=I.VENDOR_SYTLE_ID)");
		query.append("     JOIN vndr_item_fulfmnt_rqst R ON (VSS.vndr_item_fulfmnt_rqst_id=R.vndr_item_fulfmnt_rqst_id) ");
		query.append("WHERE I.VENDOR_SYTLE_ID='" + styleId
				+ "' AND R.VIFR_WORKFLOW_STATUS_CD='" + VFIRStatus.APPROVED
				+ QUOTE);
		query.append("      AND R.VENDOR_ID=" + vendorId
				+ " AND R.fulfmnt_service_id=" + serviceId);

		List<Object> list = (List<Object>) this.fulfillmentServiceDao
				.fetchCount(query.toString());

		if (null != list && !list.isEmpty()) {
			return APPROVED_REQUEST;
		}
		return REQUEST_NOT_APPROVED;
	}

	/**
	 * This method fetches request details for a given upc.
	 * 
	 * @param stylesSkuForm
	 * @param vendorId
	 * @param serviceId
	 * @param vendorUpc
	 * @throws Exception
	 */
	private void fetchRequestlevelDetails(StylesSkuForm stylesSkuForm,
			Long vendorId, Long serviceId, String vendorUpc) throws Exception {
		StringBuilder query = new StringBuilder();
		query
		.append("SELECT DISTINCT vndr_item_fulfmnt_rqst_id,item_rqst_descr,effective_date,itm_src_desc,")
		.append("       \"NAME\",NVL(unit_cost,0),NVL(unit_handling_fee,0),")
		.append("       ( CASE WHEN OVERRIDE_COST = 'N' AND OVERRIDE_FEE = 'N' THEN 'N'")
		.append("              ELSE 'Y'")
		.append("        END ) OVERRIDE,UPDATED_BY APPROVED_BY,VIFR_WORKFLOW_ACTION_CD ")
		.append("FROM (")
		.append("SELECT R.vndr_item_fulfmnt_rqst_id,R.item_rqst_descr,R.effective_date,R.VIFR_WORKFLOW_ACTION_CD,SRC.\"NAME\" itm_src_desc,")
		.append("       W.\"NAME\",VSS.unit_cost,VSS.unit_handling_fee,")
		.append("       Nvl2(VSS.override_cost,'Y','N') OVERRIDE_COST,")
		.append("       Nvl2(VSS.override_handling_fee,'Y','N') OVERRIDE_FEE,R.UPDATED_BY ")
		.append("FROM   VIFR_STYLE_SKU VSS JOIN IDB_DROP_SHIP_FEED I ON (I.belk_upc = VSS.belk_upc)")
		.append("       JOIN vndr_item_fulfmnt_rqst R ON (VSS.vndr_item_fulfmnt_rqst_id = R.vndr_item_fulfmnt_rqst_id)")
		.append("       JOIN ITEM_SOURCE SRC ON (R.item_source_cd = SRC.item_source_cd)")
		.append("       JOIN vifr_workflow_status W ON (R.VIFR_WORKFLOW_STATUS_CD = W.VIFR_WORKFLOW_STATUS_CD)")
		.append("WHERE R.vendor_id =").append(vendorId).append(" AND R.fulfmnt_service_id=").append(serviceId)
		.append("      AND I.VENDOR_UPC = '").append(this.escapeSpecialCharacters(vendorUpc))
		.append("' AND R.VIFR_WORKFLOW_STATUS_CD='").append(VFIRStatus.APPROVED).append("')");

		log.info("Request level details query===>" + query.toString());
		List<Object[]> list = this.fulfillmentServiceDao.fetchDetails(query
				.toString());
		List<RequestDTO> list2 = new ArrayList<RequestDTO>();

		Iterator<Object[]> iterator = list.iterator();
		while (iterator.hasNext()) {
			Object[] objects = iterator.next();
			RequestDTO requestDTO = new RequestDTO();
			requestDTO.setRequestId(((BigDecimal) objects[0]).longValue());
			requestDTO.setRequestDesc((String) objects[1]);
			requestDTO.setEffectiveDate(getFormattedDate((Date) objects[2]));
			requestDTO.setSource((String) objects[3]);
			requestDTO.setStatus((String) objects[4]);
			requestDTO.setUnitCost(((BigDecimal) objects[5]).doubleValue());
			requestDTO.setUnitFee(((BigDecimal) objects[6]).doubleValue());
			requestDTO.setOverride(((Character) objects[7]).toString());
			requestDTO.setUpdatedBy((String) objects[8]);
			requestDTO.setAction((String) objects[9]);
			list2.add(requestDTO);
		}
		stylesSkuForm.setRequestDetails(list2);
	}

	/**
	 * This method removes all styles and sku's for a given vendor & fulfillment service pair
	 * @param vendorId
	 * @param serviceId
	 * @throws Exception
	 */
	private void updateStyleSkus(Long vendorId, Long serviceId) throws Exception {
		StringBuilder query = new StringBuilder();
		query
		.append("UPDATE VIFR_STYLE ")
		.append("SET STATUS_CD='").append(VFIRStyle.INACTIVE).append("' ")
		.append("WHERE vndr_item_fulfmnt_rqst_id IN (")
		.append("SELECT vndr_item_fulfmnt_rqst_id ")
		.append("FROM vndr_item_fulfmnt_rqst ")
		.append("WHERE VENDOR_ID=").append(vendorId).append("AND FULFMNT_SERVICE_ID=").append(serviceId)
		.append(")");
		this.fulfillmentServiceDao.updateQuery(query.toString());
		
		query = new StringBuilder();
		query
		.append("UPDATE VIFR_STYLE_SKU ")
		.append("SET STATUS_CD='").append(VFIRStyleSku.INACTIVE).append("' ")
		.append("WHERE vndr_item_fulfmnt_rqst_id IN (")
		.append("SELECT vndr_item_fulfmnt_rqst_id ")
		.append("FROM vndr_item_fulfmnt_rqst ")
		.append("WHERE VENDOR_ID=").append(vendorId).append("AND FULFMNT_SERVICE_ID=").append(serviceId)
		.append(")");
		this.fulfillmentServiceDao.updateQuery(query.toString());
	}
	/**
	 * This method updates the status of all styles and sku's not included in a
	 * given request to 'INACTIVE'
	 * 
	 * @param requestId
	 * @throws Exception
	 */
	private void updateStylesSkus(Long requestId) throws Exception {
		StringBuilder styleIds = new StringBuilder();
		List<VFIRStyle> list = this.fulfillmentServiceDao
				.getRequestStyles(requestId);
		if (null != list && !list.isEmpty()) {
			Iterator<VFIRStyle> iterator = list.iterator();
			while (iterator.hasNext()) {
				String styleId = iterator.next().getCompositeKeyVIFRStyle()
						.getVendorStyleId();
				styleIds.append(QUOTE);
				styleIds.append(styleId);
				styleIds.append(QUOTE);
				styleIds.append(COMMA);
			}
			if (styleIds.length() > 0) {
				styleIds.deleteCharAt(styleIds.length() - 1);
				StringBuilder query = new StringBuilder();
				query.append("UPDATE VIFR_STYLE SET STATUS_CD='"
						+ VFIRStyle.INACTIVE + "' ");
				query.append("WHERE vndr_item_fulfmnt_rqst_id !=" + requestId);
				query.append("      AND VENDOR_STYLE_ID IN ("
						+ styleIds.toString() + ")");
				this.fulfillmentServiceDao.updateQuery(query.toString());

				query = new StringBuilder();
				query.append("UPDATE VIFR_STYLE_SKU SET STATUS_CD='"
						+ VFIRStyleSku.INACTIVE + "' ");
				query.append("WHERE vndr_item_fulfmnt_rqst_id !=" + requestId);
				query.append("      AND VENDOR_STYLE_ID IN ("
						+ styleIds.toString() + ")");
				this.fulfillmentServiceDao.updateQuery(query.toString());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Double setVendorHandlingFee(Long vendorId, Long serviceId)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT VFEE.PER_ITEM_AMOUNT ");
		query.append("FROM(");
		query.append("      SELECT *");
		query.append("      FROM(");
		query.append("            SELECT VF.VENDOR_FEE_ID");
		query
				.append("            FROM VNDR_FULFMNT_SERV_FEE VF JOIN VENDOR_FEE_RQST VR");
		query
				.append("                 ON (VF.VNDR_FEE_REQUEST_ID=VR.VENDOR_FEE_RQST_ID)");
		query.append("            WHERE VENDOR_ID=" + vendorId
				+ " AND FULFMNT_SERVICE_ID=" + serviceId);
		query.append("                  AND VR.EFFECTIVE_DATE <= SYSDATE");
		query.append("            ORDER BY VR.EFFECTIVE_DATE DESC");
		query.append("           ) WHERE ROWNUM = 1");
		query.append("     ) VFFEE ");
		query
				.append("JOIN VNDR_FEE VFEE ON (VFFEE.VENDOR_FEE_ID=VFEE .VNDR_FEE_ID) ");
		query
				.append("JOIN FEE F ON (VFEE.FEE_CD = F.FEE_CD) WHERE Lower(F.NAME)='handling fee'");

		List<Object> list = (List<Object>) this.fulfillmentServiceDao
				.fetchCount(query.toString());
		Double handlingFee = 0.0;

		if (null != list && !list.isEmpty()) {
			Object objects = list.get(0);
			handlingFee = ((BigDecimal) objects).doubleValue();
		}
		return handlingFee;
	}

	/**
	 * This method escapes the quote character in a string to make it compatible
	 * with oracle sql.
	 * 
	 * @param aText
	 * @return
	 */
	private String escapeSpecialCharacters(String aText) {
		final StringBuilder result = new StringBuilder();
		final StringCharacterIterator iterator = new StringCharacterIterator(
				aText);
		char character = iterator.current();
		while (character != CharacterIterator.DONE) {
			if (character == '\'') {
				result.append("''");
			} else {
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}

	public List<Object> getFulfillmentServiceVendorInfo(Long requestId)
			throws Exception {
		List<Object> list = new ArrayList<Object>();
		ItemRequest itemRequest = this.fulfillmentServiceDao
				.getItemRequestDetails(requestId);
		FulfillmentService fulfillmentService = itemRequest
				.getFulfillmentService();
		Vendor vendor = itemRequest.getVendor();

		FulfillmentServiceVendor fulfillmentServiceModel = this.fulfillmentServiceDao
				.getVendorFSModel(fulfillmentService.getFulfillmentServiceID(),
						vendor);

		list.add(fulfillmentService);
		list.add(fulfillmentServiceModel);
		return list;
	}
	
	private User getLoggedInUser() {
		User user = null;
		Authentication auth = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (auth.getPrincipal() instanceof UserDetails) {
			user = (User) auth.getPrincipal();
		}
		return user;
	}

	private void setAuditInfo(BaseAuditableModel model) {
		String user = getLoggedInUser().getUsername();
		if (user == null) {
			user = "Unknown";
		}
		if (StringUtils.isBlank(model.getCreatedBy())) {
			model.setCreatedBy(user);
			model.setCreatedDate(new Date());
			model.setUpdatedBy(user);
			model.setUpdatedDate(new Date());
		} else {
			model.setUpdatedBy(user);
			model.setUpdatedDate(new Date());
		}
	}

	private void disableSkuException(Long requestId, ItemRequestForm form) throws Exception {
		
		if(null == requestId) {
			form.setEnableDisableSkuException(Boolean.TRUE);
			return;
		}
		Boolean disableSkuException = Boolean.TRUE;
		ItemRequest itemRequest = this.fulfillmentServiceDao.getItemRequestDetails(requestId);
		List<VFIRStyle> list = this.fulfillmentServiceDao.getStylesForItemRequest(itemRequest);
		
		if(null != list && !list.isEmpty()) {
			Iterator<VFIRStyle> iterator = list.iterator();
			while(iterator.hasNext()) {
				VFIRStyle style = iterator.next();
				if(style.getAllSkuIndicator().equals(ALL_SIZE_N)) {
					disableSkuException = Boolean.FALSE;
					break;
				}
			}
		}
		form.setEnableDisableSkuException(disableSkuException);
	}
}
