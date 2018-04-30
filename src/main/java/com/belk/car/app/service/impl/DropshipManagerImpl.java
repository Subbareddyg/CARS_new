
package com.belk.car.app.service.impl;

import com.belk.car.app.Constants;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.appfuse.service.impl.UniversalManagerImpl;
import org.springframework.dao.DataAccessException;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.dao.DropshipDao;
import com.belk.car.app.dao.FulfillmentServiceDao;
import com.belk.car.app.exceptions.SendEmailException;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeValueProcessStatus;
import com.belk.car.app.model.BaseAuditableModel;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarImage;
import com.belk.car.app.model.CarImageId;
import com.belk.car.app.model.CarNote;
import com.belk.car.app.model.CarSearchCriteria;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.ClassAttribute;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.Config;
import com.belk.car.app.model.ContentStatus;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.DepartmentAttribute;
import com.belk.car.app.model.Image;
import com.belk.car.app.model.ImageSourceType;
import com.belk.car.app.model.ImageTrackingStatus;
import com.belk.car.app.model.ImageType;
import com.belk.car.app.model.NoteType;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.model.ProductTypeAttribute;
import com.belk.car.app.model.SourceType;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.VendorStyleSearchCriteria;
import com.belk.car.app.model.VendorStyleType;
import com.belk.car.app.model.WorkFlow;
import com.belk.car.app.model.car.ManualCar;
import com.belk.car.app.model.car.ManualCarProcessStatus;
import com.belk.car.app.model.catalog.CatalogProduct;
import com.belk.car.app.model.catalog.CatalogSku;
import com.belk.car.app.model.oma.CompositeKeyVFIRStylesku;
import com.belk.car.app.model.oma.CompositeKeyVIFRStyle;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.model.oma.ItemRequest;
import com.belk.car.app.model.oma.ItemRequestWorkflow;
import com.belk.car.app.model.oma.VFIRStatus;
import com.belk.car.app.model.oma.VFIRStyle;
import com.belk.car.app.model.oma.VFIRStyleSku;
import com.belk.car.app.model.vendorcatalog.VendorCatalog;
import com.belk.car.app.model.vendorcatalog.VendorCatalogFieldDataMapping;
import com.belk.car.app.model.vendorcatalog.VendorCatalogHeader;
import com.belk.car.app.model.vendorcatalog.VendorCatalogImage;
import com.belk.car.app.model.vendorcatalog.VendorCatalogImport;
import com.belk.car.app.model.vendorcatalog.VendorCatalogNote;
import com.belk.car.app.model.vendorcatalog.VendorCatalogRecord;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSku;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSkuId;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSkuImage;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSkuMaster;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSkuMasterId;
import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.model.workflow.WorkflowType;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.CatalogImportManager;
import com.belk.car.app.service.DropshipManager;
import com.belk.car.app.service.EmailManager;
import com.belk.car.app.service.FulfillmentServiceManager;
import com.belk.car.app.service.ProductManager;
import com.belk.car.app.service.ReportManager;
import com.belk.car.app.service.WorkflowManager;
import com.belk.car.app.to.IdbCarDataTO;
import com.belk.car.app.to.IdbCarSkuTO;
import com.belk.car.app.to.IdbDropshipDataTO;
import com.belk.car.app.util.PropertyLoader;
import com.belk.car.util.DateUtils;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import org.appfuse.service.UserManager;

/*
 * Implementation of DropshipManager Interface
 */
/**
 * @author afusy07 Feb 4, 2010TODO
 */
public class DropshipManagerImpl extends UniversalManagerImpl
implements
DropshipManager,
DropShipConstants {

	
	
	private DropshipDao dropshipDao;
	private FulfillmentServiceDao fulfillmentServiceDao;
	private FulfillmentServiceManager fulfillmentServiceManager;
	private EmailManager emailManager;
        private CarLookupManager lookupManager;
        private CarManager carManager;
        private WorkflowManager workflowManager;
        private UserManager userManager;
        private ProductManager productManager;
        private CatalogImportManager catalogImportManager;
	private ReportManager reportManager;
	
	private User user = new User();
	private SimpleDateFormat dateFormat = new SimpleDateFormat(MM_DD_YYYY);

	public FulfillmentServiceManager getFulfillmentServiceManager() {
		return fulfillmentServiceManager;
	}

	public void setFulfillmentServiceManager(FulfillmentServiceManager fulfillmentServiceManager) {
		this.fulfillmentServiceManager = fulfillmentServiceManager;
	}

	/**
	 * @return the emailManager
	 */
	public EmailManager getEmailManager() {
		return emailManager;
	}

	/**
	 * @param emailManager the emailManager to set
	 */
	public void setEmailManager(EmailManager emailManager) {
		this.emailManager = emailManager;
	}

	/**
	 * @return the fulfillmentServiceDao
	 */
	public FulfillmentServiceDao getFulfillmentServiceDao() {
		return fulfillmentServiceDao;
	}

	/**
	 * @param fulfillmentServiceDao the fulfillmentServiceDao to set
	 */
	public void setFulfillmentServiceDao(FulfillmentServiceDao fulfillmentServiceDao) {
		this.fulfillmentServiceDao = fulfillmentServiceDao;
	}

	/**
	 * @return the dropshipDao
	 */
	public DropshipDao getDropshipDao() {
		return dropshipDao;
	}

    public CarManager getCarManager() {
        return carManager;
    }

    public void setCarManager(CarManager carManager) {
        this.carManager = carManager;
    }

    public CatalogImportManager getCatalogImportManager() {
        return catalogImportManager;
    }

    public void setCatalogImportManager(CatalogImportManager catalogImportManager) {
        this.catalogImportManager = catalogImportManager;
    }

    public CarLookupManager getLookupManager() {
        return lookupManager;
    }

    public void setLookupManager(CarLookupManager lookupManager) {
        this.lookupManager = lookupManager;
    }

    public ProductManager getProductManager() {
        return productManager;
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public ReportManager getReportManager() {
        return reportManager;
    }

    public void setReportManager(ReportManager reportManager) {
        this.reportManager = reportManager;
    }
    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public WorkflowManager getWorkflowManager() {
        return workflowManager;
    }

    public void setWorkflowManager(WorkflowManager workflowManager) {
        this.workflowManager = workflowManager;
    }

	/**
	 * @param dropshipDao the dropshipDao to set
	 */
	public void setDropshipDao(DropshipDao dropshipDao) {
		this.dropshipDao = dropshipDao;
	}



	/**
	 * This method gets the current date and formats it in MM/DD/YYYY format
	 * 
	 * @return String currentDate
	 */
	private String getCurrentDate() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(MM_DD_YYYY);
		return dateFormat.format(date);
	}
	
	

	/**
	 * Method to save or update IDBfeed into idb_feed table
	 * @author afusy45
	 * @param List object
	 *
	 */
	public void saveFeed(List<IdbDropshipDataTO> ob) {
		dropshipDao.saveFeed(ob);
	}

	/**
	 * Method to save list of item requests to Item Request table
	 * @author afusy45
	 * @param List
	 */
	public void saveItemRequestList(List itemRequestObjects){
		dropshipDao.saveItemRequestList(itemRequestObjects);
	}

	/**
	 * Method to list of IdbDropshipDataTo object with matching vendorUPCs from
	 * feed
	 * @author afusy45
	 * @param vendorUPC
	 * @return List of type IdbDropshipDataTo
	 * 
	 */
	public List<IdbDropshipDataTO> getVendorUpcs(Set vendorUPCs) {
		return dropshipDao.getVendorUpcs(vendorUPCs);
	}

	/**
	 * Get value by serializable Id
	 * 
	 * @param cls,serializable id
	 * @return object
	 */
	@SuppressWarnings("unchecked")
	public Object getById(Class cls, Serializable id) {
		return dropshipDao.getById(cls, id);
	}

	private void setAuditInfo(BaseAuditableModel model) {
		if (StringUtils.isBlank(model.getCreatedBy())) {
			model.setCreatedBy(CARSDEV_USER);
			model.setCreatedDate(new Date());
			model.setUpdatedBy(CARSDEV_USER);
			model.setUpdatedDate(new Date());
		} else {
			model.setUpdatedBy(CARSDEV_USER);
			model.setUpdatedDate(new Date());
		}
	}
	/**
	 * Method to save styles and corresponding data to VFIR_STYLE table
	 * @author afusy45
	 * @param vendorStyleID,vendorStyleDesc,idbStyleUnitCost,itemRequest,vendorObj
	 *
	 */
	
	public void saveStylesToVFIRStyle(
			String vendorStyleID, String vendorStyleDesc, Double idbStyleUnitCost,
			ItemRequest itemRequest, Vendor vendorObj) {

		VFIRStyle style = new VFIRStyle();
		this.setKeyForStyle(style, itemRequest, vendorStyleID);
		style.setStyleDescription(vendorStyleDesc);
		style.setAllColorIndicator(N);
		style.setAllSizeIndicator(N);
		style.setAllSkuIndicator(N);
		style.setVendor(vendorObj);
		if((itemRequest.getAction()).equals(REMOVE_STYLES_SKUS_ACTION)){
			style.setStatusCode(STATUS_CODE_INACTIVE);
		}else{
			style.setStatusCode(STATUS_CD_ACTIVE);
		}
		if((itemRequest.getItemSource().getName()).equals(IDB_COST_CHANGE_SOURCE)){
			log.info("idbStyleUnitCost.......cost change."+idbStyleUnitCost);

			style.setOverrideUnitCost(idbStyleUnitCost);
		}else{
			log.info("idbStyleUnitCost......."+idbStyleUnitCost);
			style.setUnitCost(idbStyleUnitCost);
		}
		style.setVendor(itemRequest.getVendor());
		this.setAuditInfo(style);

		try {
			this.dropshipDao.uploadStyles(style);
		}
		catch (Exception e) {
			log.error("Error uploading styles to VIFR Style table"+e);
		}
	}

	/**
	 * Method to save styles and corresponding data to VFIR_STYLE table for
	 * Purge Feed
	 * @author afusy45
	 * @param vendorStyleID,vendorStyleDesc,idbStyleUnitCost,itemRequest
	 */
	@SuppressWarnings("unchecked")
	public void saveStylesToVFIRStyleForPurge(
			String vendorStyleID, String vendorStyleDesc, Double idbStyleUnitCost,
			ItemRequest itemRequest) {
		List requestStyles = new ArrayList();

		VFIRStyle style = new VFIRStyle();
		this.setKeyForStyle(style, itemRequest, vendorStyleID);
		style.setStyleDescription(vendorStyleDesc);
		style.setAllColorIndicator(N);
		style.setAllSizeIndicator(N);
		style.setAllSkuIndicator(N);
		style.setStatusCode(STATUS_CODE_INACTIVE);
		if((itemRequest.getItemSource().getName()).equals(IDB_COST_CHANGE_SOURCE)){
			log.info("idbStyleUnitCost.......cost change."+idbStyleUnitCost);

			style.setOverrideUnitCost(idbStyleUnitCost);
		}else{
			log.info("idbStyleUnitCost......."+idbStyleUnitCost);
			style.setUnitCost(idbStyleUnitCost);
		}
		style.setVendor(itemRequest.getVendor());
		this.setAuditInfo(style);
		requestStyles.add(style);

		try {
			this.fulfillmentServiceDao.uploadStyles(requestStyles);
		}
		catch (Exception e) {
			log.error("Error uploading styles to VIFR Style table"+e);
		}
	}

	private void setKeyForStyle(VFIRStyle style, ItemRequest itemRequest, String vendorStyleId) {
		CompositeKeyVIFRStyle compositeKeyVIFRStyle = new CompositeKeyVIFRStyle();
		if(itemRequest !=null){
		compositeKeyVIFRStyle.setItemRequest(itemRequest);
		}
		if(!vendorStyleId.equals("") || !vendorStyleId.equals(null)){
		compositeKeyVIFRStyle.setVendorStyleId(vendorStyleId);
		}
		style.setCompositeKeyVIFRStyle(compositeKeyVIFRStyle);
	}

	/**
	 * Method to save styles and SKUs got from feed to VFIRSTYLESKU table
	 * @author afusy45
	 * @param vendorStyleID,belkUPC,itemRequest
	 * @throws Exception
	 * 
	 */

	public void saveStyleSKUToVFIRStyleSKU(
			String vendorStyleID, String belkUPC, Double idbUPCUnitCost,String colorDesc, String sizeDesc, ItemRequest itemRequest)
	throws Exception {
		VFIRStyleSku styleSku = new VFIRStyleSku();
		CompositeKeyVFIRStylesku compositeKeyVFIRStylesku = new CompositeKeyVFIRStylesku();
		compositeKeyVFIRStylesku.setVendorStyleId(vendorStyleID);
		compositeKeyVFIRStylesku.setItemRequest(itemRequest);
		compositeKeyVFIRStylesku.setBelkUpc(belkUPC);
		Double fee = fulfillmentServiceManager.setVendorHandlingFee(itemRequest.getVendor()
				.getVendorId(), itemRequest.getFulfillmentService().getFulfillmentServiceID());

		styleSku.setUnitHandlingfee(fee);
		styleSku.setColor(colorDesc);
		styleSku.setSizeDescription(sizeDesc);
		styleSku.setSkuexceptionInd("N");
		if((itemRequest.getAction()).equals(REMOVE_STYLES_SKUS_ACTION)){
			styleSku.setStatusCode(STATUS_CODE_INACTIVE);
		}else{
			styleSku.setStatusCode("ACTIVE");
		}
		if((itemRequest.getItemSource().getName()).equals(IDB_COST_CHANGE_SOURCE)){
			log.info("idbStyleUnitCost.......cost change."+idbUPCUnitCost);

			styleSku.setOverrideCost(idbUPCUnitCost);
		}else{
			log.info("idbStyleUnitCost......."+idbUPCUnitCost);
			styleSku.setUnitCost(idbUPCUnitCost);
		}

		styleSku.setCompositeKeyVFIRStylesku(compositeKeyVFIRStylesku);
		this.setAuditInfo(styleSku);
		this.dropshipDao.uploadSKUs(styleSku);
	}

	/**
	 * Method to save styles and SKUs got from Purge feed to VFIRSTYLESKU table
	 * @author afusy45
	 * @param vendorStyleID,belkUPC,idbUPCUnitCost,colorDesc,sizeDesc,itemRequest
	 * @throws Exception
	 */

	public void saveStyleSKUToVFIRStyleSKUForPurge(
			String vendorStyleID, String belkUPC, Double idbUPCUnitCost,String colorDesc, String sizeDesc, ItemRequest itemRequest)
	throws Exception {
		VFIRStyleSku styleSku = new VFIRStyleSku();
		CompositeKeyVFIRStylesku compositeKeyVFIRStylesku = new CompositeKeyVFIRStylesku();
		compositeKeyVFIRStylesku.setVendorStyleId(vendorStyleID);
		compositeKeyVFIRStylesku.setItemRequest(itemRequest);
		compositeKeyVFIRStylesku.setBelkUpc(belkUPC);
		Double fee = fulfillmentServiceManager.setVendorHandlingFee(itemRequest.getVendor()
				.getVendorId(), itemRequest.getFulfillmentService().getFulfillmentServiceID());

		styleSku.setUnitHandlingfee(fee);
		styleSku.setColor(colorDesc);
		styleSku.setSizeDescription(sizeDesc);
		styleSku.setSkuexceptionInd("N");
		styleSku.setStatusCode(STATUS_CODE_INACTIVE);
		if((itemRequest.getItemSource().getName()).equals(IDB_COST_CHANGE_SOURCE)){
			log.info("idbStyleUnitCost.......cost change."+idbUPCUnitCost);

			styleSku.setOverrideCost(idbUPCUnitCost);
		}else{
			log.info("idbStyleUnitCost......."+idbUPCUnitCost);
			styleSku.setUnitCost(idbUPCUnitCost);
		}

		styleSku.setCompositeKeyVFIRStylesku(compositeKeyVFIRStylesku);
		this.setAuditInfo(styleSku);
		this.dropshipDao.uploadSKUs(styleSku);
	}

	/**
	 * Method to save data to VIFRWorkFlowHist table
	 * @author afusy45
	 * @param itemRequest, workflowStatus,action
	 */
	public void saveWorkflowHist(
			ItemRequest itemRequest, VFIRStatus workflowStatus, String action) {
		ItemRequestWorkflow itemRequestWorkflow = new ItemRequestWorkflow();
		itemRequestWorkflow.setItemRequest(itemRequest);
		itemRequestWorkflow.setWorkflowStatus(workflowStatus);
		itemRequestWorkflow.setAction(action);
		Properties properties = PropertyLoader.loadProperties("ftp.properties");
		String carsadminEmail = properties.getProperty("carsAdminEmailAddress");
		itemRequestWorkflow.setCreatedBy(carsadminEmail);
		itemRequestWorkflow.setUpdatedBy(carsadminEmail);
		itemRequestWorkflow.setCreatedDate(itemRequest.getCreatedDate());
		itemRequestWorkflow.setUpdatedDate(itemRequest.getUpdatedDate());
		dropshipDao.saveWorkflowHist(itemRequestWorkflow);
	}

	/**
	 * Method to get associated Fulfillment Service ID passing vendorID
	 * @author afusy45
	 * @return List
	 * @param vendorId
	 * @see com.belk.car.app.service.DropshipManager#getFulfillmentServiceIdForVenID(java.lang.Long)
	 */
	public List<FulfillmentServiceVendor> getFulfillmentServiceIdForVenID(Long vendorId) {
		return dropshipDao.getFulfillmentServiceIdForVenID(vendorId);
	}

	

	/**
	 * Method to get Buyers email IDs responsible for the departments associated to catalog ID
	 * @author afusy45
	 * @param Long
	 * @return List
	 */
	public List getBuyerMailIDs(Long catalogID){
		return dropshipDao.getBuyerMailIDs(catalogID);
	}

	/**
	 * Method to change status of catalog file to data Mapping
	 * @author afusy45
	 * @param VendorCatalog object
	 */
	public void changeStatusOfCatalogFile(VendorCatalog catalog){
		VendorCatalog vendorCatalog = new VendorCatalog();
		vendorCatalog.setVendorCatalogID(catalog.getVendorCatalogID());
		vendorCatalog.setVendorCatalogName(catalog.getVendorCatalogName());
		vendorCatalog.setVendor(catalog.getVendor());
		vendorCatalog.setStatusCD("Data Mapping");
		vendorCatalog.setCreatedBy(catalog.getCreatedBy());
		vendorCatalog.setCreatedDate(catalog.getCreatedDate());
		vendorCatalog.setUpdatedBy(catalog.getUpdatedBy());
		vendorCatalog.setUpdatedDate(catalog.getUpdatedDate());
		vendorCatalog.setSource(catalog.getSource());

		dropshipDao.changeStatusOfCatalogFile(vendorCatalog);
	}

	/**
	 * Method to create new Vendor in VENDOR table
	 * 
	 * @param vendor Object
	 */
	public void createVendor(Vendor vendor) {
		dropshipDao.createVendor(vendor);
	}

	/**
	 * @param status
	 * @return List<VendorCatalog>
	 * @see com.belk.car.app.service.DropshipManager#getCatalogsWithStatus(java.lang.String)
	 * @Enclosing_Method  getCatalogsWithStatus
	 * @TODO get catalogs with translating status
	 */
	public List<VendorCatalog> getCatalogsWithStatus(String status) {
		return dropshipDao.getCatalogsWithStatus(status);
	}

	

	/**
	 * @param templateId
	 * @param catalogId
	 * @param styleId
	 * @param deptId
	 * @param vendorUPC
	 * @return List<VendorCatalogRecord>
	 * @TODO Get records for standard attributes from MASTER_ATTRIBTE_MAPPING
	 *       TABLE
	 */
	public List<VendorCatalogRecord> getRecordsForStandardAttributes(
			long templateId, long catalogId, long styleId, long deptId, long vendorUPC) {

		return dropshipDao.getRecordsForStandardAttributes(templateId, catalogId, styleId, deptId,
				vendorUPC);
	}



	public void saveStyleSkus(List<VendorCatalogStyleSku> styleSkuList) throws Exception {
		dropshipDao.saveStyleSkus(styleSkuList);

	}



	/**
	 * Save header record to Vendor Catalog Header table
	 */
	public void saveHeader(List<VendorCatalogHeader> headerList) {
		dropshipDao.saveHeader(headerList);
	}

	/**
	 * Save record to table vendor catalog record
	 */
	public void saveRecord(List<VendorCatalogRecord> recordList) {
		dropshipDao.saveRecord(recordList);
	}

	/**
	 * Delete record for the vendor catalog id from reocrd table
	 */
	public void deleteRecord(Long vendorCatalogID) {
		this.dropshipDao.deleteRecord(vendorCatalogID);
	}

	/**
	 * @return Map<Long, List<Image>>
	 * @see com.belk.car.app.service.DropshipManager#getCARSInProgress()
	 * @Enclosing_Method  getCARSInProgress
	 * @TODO
	 */
	public Map<Long, List<Image>> getCARSInProgress() {

		return dropshipDao.getCARSInProgress();
	}

	public List<Attribute> getAttributesforCar(long carId) {
		return this.dropshipDao.getAttributesforCar(carId);
	}

	public List<VendorCatalogStyleSkuMaster> getMasterRecordsForStyle(
			String vendorStyleNumber) {
		// TODO Auto-generated method stub
		return dropshipDao.getMasterRecordsForStyle(vendorStyleNumber);
	}

	public VendorCatalogStyleSku getStyleSkuForMaster(VendorCatalogStyleSkuId compositeKey) {

		return dropshipDao.getStyleSkuForMaster(compositeKey);
	}

	public List<VendorCatalogRecord> getAllRecordsWithRecordNum(long recordNum, long vendorCatalogId) {

		return dropshipDao.getAllRecordsWithRecordNum(recordNum, vendorCatalogId);
	}

	public String getAttributeForRecord(VendorCatalogRecord recordModel, long templateId) {

		return dropshipDao.getAttributeForRecord(recordModel, templateId);
	}

	public VendorCatalog getCatalogForId(Long vendorCatalogId) {
		// TODO Auto-generated method stub
		return dropshipDao.getCatalogForId(vendorCatalogId);
	}

	public List<VendorSku> getVendorSku(Long carId) {

		return dropshipDao.getVendorSku(carId);
	}

	public Attribute getAttributeByID(String attributeIdFromMapping) {

		return dropshipDao.getAttributeByID(attributeIdFromMapping);
	}

	public void saveOrUpdateCarSkuAttribute(CarSkuAttribute carSkuAttribute) {
		dropshipDao.saveOrUpdateCarSkuAttribute(carSkuAttribute);
	}

	public VendorCatalogFieldDataMapping getVendorCatalogFieldDataMapping(
			long templateIdMaster, VendorCatalogRecord recordModel) {

		return dropshipDao.getVendorCatalogFieldDataMapping(templateIdMaster, recordModel);
	}

	public CarSkuAttribute getCarSkuAttribute(Long carSkuId, String attributeIdFromMapping) {

		return dropshipDao.getCarSkuAttribute(carSkuId, attributeIdFromMapping);
	}




	/**
	 * @param catalogs
	 * @see com.belk.car.app.service.DropshipManager#setImageForCatalogInCatalogImageTable(java.util.List)
	 * @Enclosing_Method setImageForCatalogInCatalogImageTable
	 * @TODO To process images and insert into image table along with copying it
	 *       to defined location.
	 */
	public void setImageForCatalogInCatalogImageTable(List<VendorCatalog> catalogs) {

		long templateId;
		long catalogId ;
		// For each catalog in translating state
		// Process image
		for (VendorCatalog catalog : catalogs) {
			catalogId = catalog.getVendorCatalogID();
			templateId = catalog.getVendorCatalogTemplate().getVendorCatalogTmplID();
			try{
				catalogId = catalog.getVendorCatalogID();
				templateId = catalog.getVendorCatalogTemplate().getVendorCatalogTmplID();
			}
			catch(Exception e){
				if(log.isErrorEnabled()){
					log.error("Template ID not found , returning");
				}
				continue;
			}
			Vendor vendor = catalog.getVendor();
			if(log.isDebugEnabled()){
				log.debug("catalogId=" + catalogId);
				log.debug("templateId=" + templateId);
			}

			// Get style skus for catalog from Vendor_Catalog_style_Sku table
			List<VendorCatalogStyleSku> styleSkuList = dropshipDao
			.getStyelSkusWithCatalogId(catalogId);
			processImageForCatalog(catalogId, templateId, styleSkuList, vendor);
		}

	}

	/**
	 * @param catalogId
	 * @param templateId
	 * @param styleSkuList
	 * @param vendor
	 * @return void
	 * @Enclosing_Method processImageForCatalog
	 * @TODO To process image and save it to vendor_catalog_style_sku_image
	 *       table.
	 */
	private void processImageForCatalog(
			long catalogId, long templateId, List<VendorCatalogStyleSku> styleSkuList, Vendor vendor) {
		// Get all record for each image types from record table
		Map<Long, VendorCatalogRecord> recordToRecordModelMainImageMap = getRecordsForImageType(
				templateId, catalogId, MAIN_IMAGE_ATTRIBUTE);
		Map<Long, VendorCatalogRecord> recordToRecordModelSwatchImageMap = getRecordsForImageType(
				templateId, catalogId, SWATCH_IMAGE_ATTRIBUTE);
		Map<Long, VendorCatalogRecord> recordToRecordModelAlternate1ImageMap = getRecordsForImageType(
				templateId, catalogId, ALTERNATE_IMAGE_1_ATTRIBUTE);
		Map<Long, VendorCatalogRecord> recordToRecordModelAlternate2ImageMap = getRecordsForImageType(
				templateId, catalogId, ALTERNATE_IMAGE_2_ATTRIBUTE);
		Map<Long, VendorCatalogRecord> recordToRecordModelAlternate3ImageMap = getRecordsForImageType(
				templateId, catalogId, ALTERNATE_IMAGE_3_ATTRIBUTE);
		Map<Long, VendorCatalogRecord> recordToRecordModelAlternate4ImageMap = getRecordsForImageType(
				templateId, catalogId, ALTERNATE_IMAGE_4_ATTRIBUTE);
		Map<Long, VendorCatalogRecord> recordToRecordModelAlternate5ImageMap = getRecordsForImageType(
				templateId, catalogId, ALTERNATE_IMAGE_5_ATTRIBUTE);

		// For each style sku for the catalog
		// Process images
		for (VendorCatalogStyleSku styleSku : styleSkuList) {
			if(log.isDebugEnabled()){
				log.debug("Inside stylesku in image");
			}
			Long recordNum = styleSku.getRecordNum();
                        String styleId = styleSku.getCompositeKey().getVendorStyleId();

			VendorStyle vendorStyle = (VendorStyle) dropshipDao.getVendorStyleByNumber(styleId, vendor);
			String vendorStyleNum = vendorStyle.getVendorStyleNumber();
			String vendorUpc = styleSku.getCompositeKey().getVendorUPC();
			String vendorNumber = vendor.getVendorNumber();
			String color = styleSku.getColor();
			if(log.isDebugEnabled()){
				log.debug("recordNum=" + recordNum);
				log.debug("styleId=" + styleId);
				log.debug("vendorUpc=" + vendorUpc);
				log.debug("vendorNumber=" + vendorNumber);
				log.debug("color=" + color);
			}

			// Get the cell from catalog containing the main image value
			VendorCatalogRecord recordMainImage = recordToRecordModelMainImageMap.get(recordNum);
			String mainImageName = recordMainImage != null ? recordMainImage
					.getVendorCatalogFieldValue() : null;
					if(log.isDebugEnabled()){
						log.debug("mainImageName=" + mainImageName);
					}
					VendorCatalogStyleSkuMaster master = new VendorCatalogStyleSkuMaster();
					VendorCatalogStyleSkuMasterId masterId = new VendorCatalogStyleSkuMasterId(styleId,
							vendorUpc, vendor.getVendorId());
					master.setCompositeKey(masterId);
					master.setVendorCatalogId(catalogId);

					// Get any image for the same catalog with same style sku to disable
					// it.
					List<VendorCatalogStyleSkuImage> imagesExisting = dropshipDao
					.getImagesForStyleSku(master);

					if (mainImageName != null) {
						if(log.isDebugEnabled()){
							log.debug("catalog_id=" + catalogId);
							log.debug("vendor number=" + vendor.getVendorNumber());
							log.debug("vendor style id=" + styleId);
							log.debug("record number=" + recordMainImage.getCompositeKey().getRecordNumber());
							log.debug("record header number=="
									+ recordMainImage.getCompositeKey().getHeaderNum());
							log.debug("record value=" + recordMainImage.getVendorCatalogFieldValue());
						}
						processImage(catalogId, vendorNumber, styleId, vendorStyleNum, vendorUpc, color,
								'M', mainImageName);
					}
					else{
						processImage(catalogId, vendorNumber, styleId, vendorStyleNum, vendorUpc, color,
								'M', "dummy.jpg");
					}

					VendorCatalogRecord recordSwatchImage = recordToRecordModelSwatchImageMap
					.get(recordNum);
					String swatchImageName = recordSwatchImage != null ? recordSwatchImage
							.getVendorCatalogFieldValue() : null;
							if (swatchImageName != null) {
								processImage(catalogId, vendorNumber, styleId, vendorStyleNum, vendorUpc, color,
										'S', swatchImageName);
							}

							VendorCatalogRecord recordAltImage = recordToRecordModelAlternate1ImageMap
							.get(recordNum);
							String alt1ImageName = recordAltImage != null ? recordAltImage
									.getVendorCatalogFieldValue() : null;
									if (alt1ImageName != null) {
										processImage(catalogId, vendorNumber, styleId, vendorStyleNum, vendorUpc, color,
												'1', alt1ImageName);
									}

									recordAltImage = recordToRecordModelAlternate2ImageMap.get(recordNum);
									String alt2ImageName = recordAltImage != null ? recordAltImage
											.getVendorCatalogFieldValue() : null;
											if (alt2ImageName != null) {
												processImage(catalogId, vendorNumber, styleId, vendorStyleNum, vendorUpc, color,
														'2', alt2ImageName);
											}

											recordAltImage = recordToRecordModelAlternate3ImageMap.get(recordNum);
											String alt3ImageName = recordAltImage != null ? recordAltImage
													.getVendorCatalogFieldValue() : null;
													if (alt3ImageName != null) {
														processImage(catalogId, vendorNumber, styleId, vendorStyleNum, vendorUpc, color,
																'3', alt3ImageName);
													}

													recordAltImage = recordToRecordModelAlternate4ImageMap.get(recordNum);
													String alt4ImageName = recordAltImage != null ? recordAltImage
															.getVendorCatalogFieldValue() : null;
															if (alt4ImageName != null) {
																processImage(catalogId, vendorNumber, styleId, vendorStyleNum, vendorUpc, color,
																		'4', alt4ImageName);
															}

															recordAltImage = recordToRecordModelAlternate5ImageMap.get(recordNum);
															String alt5ImageName = recordAltImage != null ? recordAltImage
																	.getVendorCatalogFieldValue() : null;
																	if (alt5ImageName != null) {
																		processImage(catalogId, vendorNumber, styleId, vendorStyleNum, vendorUpc, color,
																				'5', alt5ImageName);
																	}

																	List<VendorCatalogStyleSkuImage> imageExistingInactive = new ArrayList<VendorCatalogStyleSkuImage>();
																	for (VendorCatalogStyleSkuImage image : imagesExisting) {
																		image.setStatus(Status.INACTIVE);
																		imageExistingInactive.add(image);
																	}
																	dropshipDao.deleteImages(imageExistingInactive);
		}

	}

	/**
	 * @param catalogId
	 * @param vendorNumber
	 * @param styleId
	 * @param vendorStyleNum
	 * @param vendorUpc
	 * @param color
	 * @param imageType
	 * @param imageName
	 * @return void
	 * @Enclosing_Method processImage
	 * @TODO Copy image from unmapped folder to mapped folder with proper naming
	 *       convention. Save image record in VNDR_CATL_STY_SKU_IMAGE table
	 */
	public void processImage(
			long catalogId, String vendorNumber, String styleNum, String vendorStyleNum,
			String vendorUpc, String color, char imageType, String imageName) {
		// Read image from the generated path for vendor number,catalog id
		Properties properties = PropertyLoader.loadProperties("ftp.properties");
		String destinationPath = properties.getProperty("imagePath");
		StringBuffer sourceDir =new StringBuffer(destinationPath).append(vendorNumber);
		VendorCatalog catalog=(VendorCatalog) getDropshipDao().getById(VendorCatalog.class, catalogId);
		sourceDir.append("_");
		sourceDir.append(catalogId);
		sourceDir.append("/");
		sourceDir.append(UNMAPPED_IMAGE_INITIAL_FOLDER);
		sourceDir.append("/");
		sourceDir.append(imageName);

		if("dummy".equals(imageName) && imageType=='M'){

			sourceDir =new StringBuffer(destinationPath);
			sourceDir.append(imageName);
			sourceDir.append(".jpg");
		}
		String extention = getExtention(imageName);
		if(log.isDebugEnabled()){
			log.debug("Extention===" + extention);
		}
		File srcFile = new File(sourceDir.toString());
		if(imageType=='M' && (srcFile.isDirectory() || !srcFile.exists())){
			log.debug("main image not found");
			sourceDir =new StringBuffer(destinationPath);
			sourceDir.append("dummy.jpg");

			srcFile = new File(sourceDir.toString());
		}
		if (srcFile.isDirectory() || !srcFile.exists()) {

			if(log.isDebugEnabled()){
				log.debug("File not found returning......." + sourceDir.toString());
			}
			return;
		}

		// Generate the new file name for the image recieved
		// Image name - mapped/vendorNum_VendorStyleNum_ImageType_ColorCode.ext

		StringBuffer destinationDir =new StringBuffer(destinationPath).append(vendorNumber);

		destinationDir.append("_");
		destinationDir.append(catalogId);
		destinationDir.append("/");
		destinationDir.append(MAPPED);
		File destdir = new File(destinationDir.toString());
		if (!destdir.exists()) {
			destdir.mkdirs();
		}
		StringBuffer fileName = new StringBuffer(vendorNumber);
		fileName.append("_");
		fileName.append(vendorStyleNum);
		fileName.append("_");

		switch (imageType) {
			case 'M':
				fileName.append("A");
				fileName.append("_");
				break;
			case 'S':
				fileName.append("SW");
				fileName.append("_");
				break;
			case '1':
				fileName.append("B");
				fileName.append("_");
				break;
			case '2':
				fileName.append("C");
				fileName.append("_");
				break;
			case '3':
				fileName.append("D");
				fileName.append("_");
				break;
			case '4':
				fileName.append("E");
				fileName.append("_");
				break;
			case '5':
				fileName.append("F");
				fileName.append("_");
				break;
			default:
				if(log.isDebugEnabled()){
					log.debug("Invalid image type for vendor style." + styleNum + " and vendorUpc="
							+ vendorUpc);
				}
			break;

		}
		if (!StringUtils.isBlank(color)) {
			fileName.append(color);

		}
		else {
			fileName.append("000");

		}
		fileName.append(extention);
		if(("dummy".equals(imageName) && imageType=='M') || (sourceDir.indexOf("dummy")>-1) ){
			fileName = new StringBuffer("dummy").append(".jpg");
			log.debug("filename dummy="+fileName);
		}

		File destFile = new File(destdir, fileName.toString());
		FileInputStream fis = null;
		FileChannel srcChannel = null;
		FileOutputStream fos = null;
		FileChannel destChannel = null;

		// Move the file from source to destination.

		try {
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(destFile);
			srcChannel = fis.getChannel();
			destChannel = fos.getChannel();
			srcChannel.transferTo(0, srcFile.length(), destChannel);
		}
		catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		catch (IOException e) {

			e.printStackTrace();
		}
		finally {
			try {
				srcChannel.close();
				destChannel.close();
				fis.close();
				fos.close();
			}
			catch (IOException e) {

				e.printStackTrace();
			}
		}

		// Save Record to image table

		VendorCatalogStyleSkuImage styleSkuImageModel = new VendorCatalogStyleSkuImage();
		styleSkuImageModel.setVendorCatalogId(catalogId);
		styleSkuImageModel.setVendorStyleId(styleNum);
		styleSkuImageModel.setVendorUpc(vendorUpc);
		int i = destFile.getAbsolutePath().indexOf(vendorNumber);
		

		// Get the image path excluding root path
		String path = (i > -1) ? destFile.getAbsolutePath().substring(i ,
				destFile.getAbsolutePath().length()) : destFile.getAbsolutePath();
		if("dummy".equals(imageName) && imageType=='M'){
			path=destinationDir.toString()+"/"+imageName;
			log.debug("path="+path);
		}
		//Replace all the backward slashes to forward to show the images on screen
	
		path=path.replace('\\', '/');
	
		styleSkuImageModel.setImageFileName(path);
		styleSkuImageModel.setStatus(Status.ACTIVE);
		styleSkuImageModel.setCreatedBy(catalog.getCreatedBy());
		styleSkuImageModel.setUpdatedBy(catalog.getUpdatedBy());

		switch (imageType) {
			case 'M':
				styleSkuImageModel.setImageType("MAIN");
				break;
			case 'S':
				styleSkuImageModel.setImageType("SWATCH");
				break;
			case '1':
				styleSkuImageModel.setImageType("ALT");
				break;
			case '2':
				styleSkuImageModel.setImageType("ALT");
				break;
			case '3':
				styleSkuImageModel.setImageType("ALT");
				break;
			case '4':
				styleSkuImageModel.setImageType("ALT");
				break;
			case '5':
				styleSkuImageModel.setImageType("ALT");
				break;
			default:
				if(log.isDebugEnabled()){
					log.debug("Invalid image type for vendor style." + styleNum + " and vendorUpc="
							+ vendorUpc);
				}
			break;

		}

		dropshipDao.saveImageForStyleSku(styleSkuImageModel);

	}

	/**
	 * @param imageType
	 * @return
	 * @return String
	 * @Enclosing_Method getExtention
	 * @TODO To get extention of the image files.
	 */
	private String getExtention(String fileName) {
		
		int i = fileName.lastIndexOf('.');
		return (i > -1) ? fileName.substring(i, fileName.length()) : "";

	}

	/**
	 * @param templateId
	 * @param catalogId
	 * @param imageId
	 * @return
	 * @return Map<Long,VendorCatalogRecord>
	 * @Enclosing_Method getRecordsForImageType
	 * @TODO To get images for one particular image type
	 */
	private Map<Long, VendorCatalogRecord> getRecordsForImageType(
			long templateId, long catalogId, long imageId) {
		List<VendorCatalogRecord> records = getRecordsForStandardAttributes(templateId, catalogId,
				imageId, 0, 0);
		Iterator<VendorCatalogRecord> itr = records.iterator();
		Map<Long, VendorCatalogRecord> recordToRecordModelMap = new HashMap<Long, VendorCatalogRecord>();
		while (itr.hasNext()) {
			VendorCatalogRecord record = (VendorCatalogRecord) itr.next();
			recordToRecordModelMap.put(record.getCompositeKey().getRecordNumber(), record);
		}
		return recordToRecordModelMap;

	}

	/**
	 * Send Email for new catalog ready for data mapping
	 * @author afusy45
	 * @param catalog
	 * @param emailIDs
	 * @throws SendEmailException 
	 */
	public void sendEmailForCatalogDataMapping(VendorCatalog catalog,List emailIDs) throws SendEmailException{
		NotificationType notification = new NotificationType();
		Properties properties = PropertyLoader
		.loadProperties("ftp.properties");

		String urlPath = properties.getProperty("urlPath");
		log.info("urlPath"+urlPath);
		Config emailNotificationList = (Config) lookupManager.getById(Config.class, Config.CATALOG_PROCESS_ERROR_NOTIFICATION_LIST);
		String[] emails = StringUtils.split(emailNotificationList.getValue(), ",;");
		log.debug("Data Mapping Email Notification List:"+emails);
		
		String[] newEmails=new String[emails.length];
		for(int i=0;i<emails.length;i++){
			log.debug("Email list for Catalog Data Mapping notification:"+emails[i]);
			newEmails[i]=emails[i];
		}
		newEmails[emails.length]=catalog.getCreatedBy();
		String dataMappingLink = urlPath+VENDOR_CATALOG_DATAMAPPING_LINK+catalog.getVendorCatalogID();

		Map model = new HashMap();
		model.put("userEmail",newEmails );
		model.put("catalog", catalog);
		model.put("dataMappingLink",dataMappingLink);
		
		String carsadminEmail = properties.getProperty("carsAdminEmailAddress");
		user.setCreatedBy(carsadminEmail);
		user.setUpdatedBy(carsadminEmail);
		user.setUsername(carsadminEmail);

		notification = (NotificationType) this.dropshipDao.getById(NotificationType.class,
				NotificationType.NEW_VENDOR_CATALOG_READY_FOR_DATA_MAPPING);
		try{
			this.emailManager.sendEmailForCatalogDataMapping(notification, user, model);
		}catch(SendEmailException se){
			log.error("Ecxeption while sendiong emials from email manager"+se);
		}
	}


	/**
	 * Send Email on validation error
	 * @author afusy45
	 * @param catalog, totalRecords , errorMap , alertMsg
	 */
	public void sendEmailNotification(

			VendorCatalog catalog, int totalRecords, Map<String, List> errorMap,int alertMsg)
	throws Exception {
		NotificationType notification = new NotificationType();
		int recordsParsed =0;

		log.info("total no fo records" + totalRecords);
		recordsParsed = totalRecords - errorMap.size();
		log.info("totalRecords parsed"+totalRecords);
		
		Config emailNotificationList = (Config) lookupManager.getById(Config.class, Config.CATALOG_PROCESS_ERROR_NOTIFICATION_LIST);
		String[] emails = StringUtils.split(emailNotificationList.getValue(), ",;");
		
		String[] newEmails=new String[emails.length+1];
		for(int i=0;i<emails.length;i++){
			log.debug("Email list for catalog process error notification:"+emails[i]);
			newEmails[i]=emails[i];
		}
		newEmails[emails.length]=catalog.getCreatedBy();
		
		Map model = new HashMap();
		model.put("userEmail", newEmails);
		model.put("catalog", catalog);
		model.put("alertMsg", alertMsg);
		model.put("totalRecords", totalRecords);
		model.put("recordsParsed", recordsParsed);
		model.put("errorMap", errorMap);
		user.setCreatedBy(CARS_ADMIN_USER);
		user.setUpdatedBy(CARS_ADMIN_USER);
		user.setUsername(CARS_ADMIN_USER);
		if (errorMap.isEmpty()) {

			log.debug("Error map is empty--success email");
			notification = (NotificationType) this.dropshipDao.getById(NotificationType.class,
					NotificationType.VENDOR_CATALOG_IMPORT_NOTIFICATION);

			this.emailManager.sendEmailForCatalogDataMapping(notification, user, model);

		}

		if (errorMap.containsKey("V")) {

			log.debug("Validation errors exist");
			notification = (NotificationType) this.dropshipDao.getById(NotificationType.class,
					NotificationType.VENDOR_CATALOG_IMPORT_VALIDATION_ERR);

			this.emailManager.sendEmailForCatalogDataMapping(notification, user, model);
		}

		if (errorMap.containsKey("P")) {
			log.debug("Processing errors exist");
			notification = (NotificationType) this.dropshipDao.getById(NotificationType.class,
					NotificationType.VENDOR_CATALOG_IMPORT_PROCESSING_ERR);

			this.emailManager.sendEmailForCatalogDataMapping(notification, user, model);
		}

		
	}

	/* Method to get Vendor Catalog Info from VNDR_CATALOG_IMP table */
	public List<VendorCatalogImport> getVendorCatalogImportInfo(Long catalogId) {

		return dropshipDao.getVendorCatalogImportInfo(catalogId);
	}

	/**
	 * Method to save errors to vendor catalog note table
	 * 
	 * @throws ParseException
	 */
	public void saveVendorCatalogNote(VendorCatalog catalog, String noteSubject, List noteText)
	throws ParseException {


		List<VendorCatalogNote> vendorCatalogNoteList = new ArrayList<VendorCatalogNote>();
                String temp ="";
		for (int i = 0; i < noteText.size(); i++) {
			VendorCatalogNote vendorCatalogNote = new VendorCatalogNote();
			vendorCatalogNote.setSubject(noteSubject);
                        temp = noteText.get(i).toString();

                        if(temp!=null) {
                            if(temp.length() > 4000)
                            temp = temp.substring(0,3990);
                        }
			vendorCatalogNote.setNoteText(temp);

			vendorCatalogNote.setVendorCatalog(catalog);
			vendorCatalogNote.setCreatedBy(CARS_ADMIN_USER);
			vendorCatalogNote.setStatusCD("ACTIVE");
			vendorCatalogNote.setUpdatedBy(CARS_ADMIN_USER);
			vendorCatalogNote.setCreatedDate(new Date());
			vendorCatalogNote.setUpdatedDate(new Date());
			vendorCatalogNoteList.add(vendorCatalogNote);
		}
		if(!vendorCatalogNoteList.isEmpty()){
			dropshipDao.saveVendorCatalogNote(vendorCatalogNoteList);
		}
	}

	/**
	 * Method to save images in Image table
	 * 
	 * @throws ParseException
	 */
	public void saveImageData(List fileName, Long catalogId)
	throws ParseException {
		
		List<VendorCatalogImage> vendorCatalogImageList = new ArrayList<VendorCatalogImage>(); 
		for(int i=0;i<fileName.size();i++){
		
		VendorCatalogImage vendorCatalogImage = new VendorCatalogImage();
		vendorCatalogImage.setImgName(fileName.get(i).toString());
		vendorCatalogImage.setVendorCatalogId(catalogId);
		vendorCatalogImage.setCreatedBy(CARS_ADMIN_USER);
		vendorCatalogImage.setUpdatedBy(CARS_ADMIN_USER);
		vendorCatalogImage.setCreatedDate(dateFormat.parse(getCurrentDate()));
		vendorCatalogImage.setUpdatedDate(dateFormat.parse(getCurrentDate()));
		vendorCatalogImageList.add(vendorCatalogImage);
		}
		try{
		dropshipDao.saveImageData(vendorCatalogImageList);
		}catch(DataAccessException dae){
			log.error("Exception while saving images to vendor catalog image table"+dae);
		}
	}


	public List<VendorCatalogStyleSkuImage> getImagesForStyleSku(VendorCatalogStyleSkuMaster master) {

		return dropshipDao.getImagesForStyleSku(master);
	}

	/**
	 * @param styleIdAttrId
	 * @param templateId
	 * @param catalogId
	 * @return VendorCatalogHeader
	 * @see com.belk.car.app.service.DropshipManager#getMappingForMasterAttribute(long, long, long)
	 * @Enclosing_Method  getMappingForMasterAttribute
	 * @TODO
	 */
	public VendorCatalogHeader getMappingForMasterAttribute(
			long styleIdAttrId, long templateId, long catalogId) {
		return dropshipDao.getMappingForMasterAttribute(styleIdAttrId, templateId, catalogId);
	}

	/**
	 * @param catalogId
	 * @param headerList
	 * @return
	 * @see com.belk.car.app.service.DropshipManager#getRecordsForHeaderNums(long, java.util.List)
	 * @Enclosing_Method  getRecordsForHeaderNums
	 * @TODO
	 */
	public List<VendorCatalogRecord> getRecordsForHeaderNums(long catalogId, List<Long> headerList) {
		return dropshipDao.getRecordsForHeaderNums(catalogId, headerList);
	}



	/**
	 * @return Map<Car, Set<VendorSku>>
	 * @see com.belk.car.app.service.DropshipManager#getCarAndSkus()
	 * @Enclosing_Method  getCarAndSkus
	 * @TODO
	 */
	public Map<Car, Set<VendorSku>> getCarAndSkus() {
		return dropshipDao.getCarAndSkus();
	}

	/**
	 * @param soureTypeId
	 * @return ImageSourceType
	 * @see com.belk.car.app.service.DropshipManager#getImageSourceType(java.lang.String)
	 * @Enclosing_Method  getImageSourceType
	 * @TODO Get image source type
	 */
	public ImageSourceType getImageSourceType(String soureTypeId) {
		return (ImageSourceType) dropshipDao.getById(ImageSourceType.class, soureTypeId);
	}

	/**
	 * @param trackingId
	 * @return ImageTrackingStatus
	 * @see com.belk.car.app.service.DropshipManager#getImageTrackingStatus(java.lang.String)
	 * @Enclosing_Method  getImageTrackingStatus
	 * @TODO
	 */
	public ImageTrackingStatus getImageTrackingStatus(String trackingId) {
		return (ImageTrackingStatus) dropshipDao.getById(ImageTrackingStatus.class, trackingId);
	}


	/**
	 * @return Map<String, ImageType>
	 * @see com.belk.car.app.service.DropshipManager#getImageTypes()
	 * @Enclosing_Method  getImageTypes
	 * @TODO Get image types and construct a map for iamge code and image type model
	 */
	public Map<String, ImageType> getImageTypes() {

		List<ImageType> imageTypes = dropshipDao.getImageTypes();
		Map<String, ImageType> imageTypeMap = new HashMap<String, ImageType>();
		for (ImageType imageType : imageTypes) {
			imageTypeMap.put(imageType.getImageTypeCd(), imageType);
		}
		return imageTypeMap;
	}

	/**
	 * @param carImageListNew
	 * @see com.belk.car.app.service.DropshipManager#saveOrUpdateCarImages(java.util.List)
	 * @Enclosing_Method  saveOrUpdateCarImages
	 * @TODO
	 */
	public void saveOrUpdateCarImages(List<CarImage> carImageListNew) {
		dropshipDao.saveOrUpdateCarImages(carImageListNew);

	}

	/**
	 * @param imageListNew
	 * @return  List<Image> 
	 * @see com.belk.car.app.service.DropshipManager#saveOrUpdateImages(java.util.List)
	 * @Enclosing_Method  saveOrUpdateImages
	 * @TODO 
	 */
	public List<Image> saveOrUpdateImages(List<Image> imageListNew) {
		return dropshipDao.saveOrUpdateImages(imageListNew);
	}

	/**
	 * @return Map<VendorCatalogStyleSkuMasterId,
	 *         List<VendorCatalogStyleSkuImage>>
	 * @see com.belk.car.app.service.DropshipManager#getAllImagesforMaster()
	 * @Enclosing_Method getAllImagesforMaster
	 * @TODO
	 */
	public Map<VendorCatalogStyleSkuMasterId, List<VendorCatalogStyleSkuImage>> getAllImagesforMaster() {
		List<VendorCatalogStyleSkuImage> catalogImages = dropshipDao.getAllImagesforMaster();
		Map<VendorCatalogStyleSkuMasterId, List<VendorCatalogStyleSkuImage>> map = 
			new HashMap<VendorCatalogStyleSkuMasterId, List<VendorCatalogStyleSkuImage>>();

		//Create a map for Images and their master record.
		for (VendorCatalogStyleSkuImage catlImage : catalogImages) {
			Long longVendorUpc;
			if(StringUtils.isNumeric(catlImage.getVendorUpc())){
				longVendorUpc=new Long(catlImage.getVendorUpc());
			}
			else{
				longVendorUpc=new Long("0");
			}
			VendorCatalogStyleSkuMasterId masterId = new VendorCatalogStyleSkuMasterId(catlImage
					.getVendorStyleId(), longVendorUpc.toString(), new Long(0));
			if (map.containsKey(masterId)) {
				map.get(masterId).add(catlImage);
			}
			else {
				List<VendorCatalogStyleSkuImage> imageList = new ArrayList<VendorCatalogStyleSkuImage>();
				imageList.add(catlImage);
				map.put(masterId, imageList);
			}
		}
		return map;
	}

	public VendorCatalogImport getVendorCatalogImportForCatalog(Long vendorCatalogID) {

		return dropshipDao.getVendorCatalogImportForCatalog(vendorCatalogID);
	}

	/**
	 * @return
	 * @see com.belk.car.app.service.DropshipManager#getAllMasterRecords()
	 * @Enclosing_Method  getAllMasterRecords
	 * @TODO
	 */
	public List<VendorCatalogStyleSkuMaster> getAllMasterRecords(long vendorId) {
		return dropshipDao.getAllMasterRecords(vendorId);
	}

	/**
	 * @param vendorCatalogID
	 * @return
	 * @see com.belk.car.app.service.DropshipManager#getStyelSkusWithCatalogId(java.lang.Long)
	 * @Enclosing_Method  getStyelSkusWithCatalogId
	 * @TODO
	 */
	public List<VendorCatalogStyleSku> getStyelSkusWithCatalogId(Long vendorCatalogID) {
		return dropshipDao.getStyelSkusWithCatalogId(vendorCatalogID);
	}

	/**
	 * @param masterListNew
	 * @see com.belk.car.app.service.DropshipManager#saveOrUpdateMasterList(java.util.List)
	 * @Enclosing_Method  saveOrUpdateMasterList
	 * @TODO
	 */
	public void saveOrUpdateMasterList(List<VendorCatalogStyleSkuMaster> masterListNew) {
		dropshipDao.saveOrUpdateMasterList(masterListNew);
	}

	/**
	 * @param vendorStyleId
	 * @return
	 * @see com.belk.car.app.service.DropshipManager#getDepartmentIdForVendorStyle(long)
	 * @Enclosing_Method  getDepartmentIdForVendorStyle
	 * @TODO
	 */
	public Long getDepartmentIdForVendorStyle(VendorStyle vendorStyle) {
		return dropshipDao.getDepartmentIdForVendorStyle(vendorStyle);
	}

	public void saveCatalogsToComplete(List<VendorCatalog> catalogListNew) {
		dropshipDao.saveCatalogsToComplete(catalogListNew);

	}

	public Object checkUpcExists(String upc)
	throws Exception {
		return this.dropshipDao.checkUpcExists(upc);
	}

	public void createCarForCrossReferenceFeed(Object vendorSku, Map<String, Object> map)
	throws Exception {
		VendorSku vendorSku2 = (VendorSku) vendorSku;

		/** Step 1 : Create vendor style if it does not exist */
		log.info("1.Creating vendorstyles if it doesn't exist");
		this.createVendorStyle(map);
		log.info("2.Vendor styles created.");

		/** Step 2 : Create Car */
		log.info("3.Creating car.");
		Car previousCar = (Car) this.fulfillmentServiceDao.getById(Car.class, vendorSku2.getCar().getCarId());
		Car car = this.createCar(previousCar, map);
		log.info("4.Cars created.");

		/** Step 3 : Create vendor sku entry */
		log.info("5.Creating vendor sku entry");
		this.createVendorSku(vendorSku2, map, car);
		log.info("6.Vendor sku entry created.");

		/** Step 4 : Create car attribute entry */
		log.info("7.Copying previous car attributes.");
		this.assignCarAttributes(previousCar, car);
		log.info("8.Attributes copied for new car.");

		/** Step 5 : Assign car images */
		log.info("9.Assigning images of previous car");
		this.assignCarImages(previousCar, car);
		log.info("10.Images assigned of previous car");

		/** Step 6 : Create notes for car */
		log.info("11.Creating notes");
		this.createNotesForCar(car, map);
		log.info("12.Process completed successfully!!!!");

	}

	/**
	 * This method creates a record in CAR_NOTES table.
	 * @param car
	 * @param map
	 * @throws Exception
	 */
	private void createNotesForCar(Car car, Map<String, Object> map)
	throws Exception {
		CarNote carNote = new CarNote();
		carNote.setCar(car);
		carNote.setIsExternallyDisplayable("N");
		carNote
		.setNoteType((NoteType) this.dropshipDao
				.getById(NoteType.class, NoteType.CAR_NOTES));
		carNote
		.setNoteText("initiated due to Cross Reference from Belk UPC :"
				+ map.get("fromupc"));
		carNote.setStatusCd(NOTES_STATUS_ACTIVE);
		carNote.setCreatedDate(new Date());
		carNote.setUpdatedDate(new Date());
		carNote.setCreatedBy(USER_CREATING_CAR);
		carNote.setUpdatedBy(USER_CREATING_CAR);
		this.dropshipDao.saveEntityObject(carNote);
	}

	/**
	 * This method assigns images to the newly created car.
	 * @param previousCar
	 * @param car
	 * @throws Exception
	 */
	private void assignCarImages(Car previousCar, Car car)
	throws Exception {
		List<Object> list = new ArrayList<Object>();
		Set<CarImage> set = previousCar.getCarImages();
		Iterator<CarImage> iterator = set.iterator();
		while (iterator.hasNext()) {
			CarImage image = iterator.next();
			CarImage carImage = new CarImage();
			CarImageId carImageId = new CarImageId();
			carImageId.setCarId(car.getCarId());
			carImageId.setImageId(image.getImage().getImageId());
			carImage.setId(carImageId);
			carImage.setCar(car);
			carImage.setImage(image.getImage());
			carImage.setCreatedDate(new Date());
			carImage.setUpdatedDate(new Date());
			carImage.setCreatedBy(USER_CREATING_CAR);
			carImage.setUpdatedBy(USER_CREATING_CAR);
			list.add(carImage);
		}
		if (!list.isEmpty()) {
			this.dropshipDao.saveList(list);
		}
	}

	/**
	 * This method assigns attributes to newly created car.
	 * @param previousCar
	 * @param car
	 * @throws Exception
	 */
	private void assignCarAttributes(Car previousCar, Car car)
	throws Exception {
		List<Object> list = new ArrayList<Object>();
		Set<CarAttribute> set = previousCar.getCarAttributes();
		Iterator<CarAttribute> iterator = set.iterator();
		while (iterator.hasNext()) {
			CarAttribute attribute = iterator.next();
			CarAttribute carAttribute = new CarAttribute();
			carAttribute.setAttribute(attribute.getAttribute());
			carAttribute.setAttributeValueProcessStatus(attribute.getAttributeValueProcessStatus());
			carAttribute.setAttrValue(attribute.getAttrValue());
			carAttribute.setCar(car);
			carAttribute.setHasChanged(attribute.getHasChanged());
			carAttribute.setIsChangeRequired(attribute.getIsChangeRequired());
			carAttribute.setStatusCd(ATTRIBUTE_STATUS_ACTIVE);
			carAttribute.setDisplaySeq(attribute.getDisplaySeq());
			carAttribute.setCreatedDate(new Date());
			carAttribute.setUpdatedDate(new Date());
			carAttribute.setCreatedBy(USER_CREATING_CAR);
			carAttribute.setUpdatedBy(USER_CREATING_CAR);
			list.add(carAttribute);
		}
		if (!list.isEmpty()) {
			this.dropshipDao.saveList(list);
		}
	}

	/**
	 * This method creates a record for vendor sku.
	 * @param vendorSku2
	 * @param map
	 * @param car
	 * @throws Exception
	 */
	private void createVendorSku(VendorSku vendorSku2, Map<String, Object> map, Car car)
	throws Exception {
		VendorSku vendorSku3 = new VendorSku();
		vendorSku3.setCar(car);
		vendorSku3.setDescr((String) map.get("styledesc"));
		vendorSku3.setLongSku((String) map.get("vendorupc"));
		vendorSku3.setVendorUpc((String) map.get("vendorupc"));
		vendorSku3.setBelkUpc((String) map.get("toupc"));
		vendorSku3.setBelkSku((String) map.get("toupc"));
		vendorSku3.setStatusCd("ACTIVE");
		vendorSku3.setColorCode((String) map.get("color"));
		vendorSku3.setSizeCode((String) map.get("sizecode"));
		vendorSku3.setColorName((String) map.get("colorname"));
		vendorSku3.setSizeName((String) map.get("sizedesc"));
		//CARS Size Conversion Issue - Size name overwritten by resync size job-->
		vendorSku3.setIdbSizeName((String) map.get("sizedesc")); //Adding size name of the SKU in conversion_name column to show the incoming size name in cars edit page
		vendorSku3.setVendorStyle(car.getVendorStyle());
		vendorSku3.setSetFlag(vendorSku2.getSetFlag());
		vendorSku3.setCreatedDate(new Date());
		vendorSku3.setUpdatedDate(new Date());
		vendorSku3.setCreatedBy(USER_CREATING_CAR);
		vendorSku3.setUpdatedBy(USER_CREATING_CAR);
		this.dropshipDao.saveEntityObject(vendorSku3);
	}

	/**
	 * This method creates a car for to_upc whose from_upc already exists.
	 * @param previousCar
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private Car createCar(Car previousCar, Map<String, Object> map)
	throws Exception {
		Car car = new Car();
		car.setDepartment(this.dropshipDao.getDepartmentFromDeptCode((String) map.get("deptno")));
		car.setSourceType((SourceType) this.dropshipDao.getById(SourceType.class,
				SourceType.CROSS_REFERENCE));
		car.setWorkFlow(previousCar.getWorkFlow());
		car.setSourceId(previousCar.getSourceId());
		car.setDueDate(previousCar.getDueDate());
		car.setVendorStyle(this.dropshipDao
				.getVendorStyleFromNumber((String) map.get("newstyleid")));
		car.setCarUserByLoggedByUserId((User) this.dropshipDao.getById(User.class, 1L));
		car.setCarUserByAssignedToUserId(this.dropshipDao.getUserBuyer());
		car.setExpectedShipDate(previousCar.getExpectedShipDate());
		car.setEscalationDate(previousCar.getEscalationDate());
		car.setCurrentWorkFlowStatus((WorkflowStatus) this.dropshipDao.getById(
				WorkflowStatus.class, WorkflowStatus.INITIATED));
		car.setAssignedToUserType((UserType) this.dropshipDao.getById(UserType.class,
				UserType.BUYER));
		car.setStatusCd(CAR_STATUS_ACTIVE);
		car.setIsProductTypeRequired(previousCar.getIsProductTypeRequired());
		car.setIsUrgent(previousCar.getIsUrgent());
		car.setCreatedBy(USER_CREATING_CAR);
		car.setCreatedDate(new Date());
		car.setUpdatedBy(USER_CREATING_CAR);
		car.setUpdatedDate(new Date());
		car.setContentStatus(previousCar.getContentStatus());
		car.setLastWorkflowStatusUpdateDate(new Date());
		car.setTurninDate(previousCar.getTurninDate());
		this.dropshipDao.saveEntityObject(car);
		return car;
	}

	/**
	 * This method creates an entry in vendor style table
	 * @param map
	 * @throws Exception
	 */
	private void createVendorStyle(Map<String, Object> map)
	throws Exception {
		boolean isVendorStyleExists = this.dropshipDao.checkVendorStyleExists((String) map
				.get("newstyleid"));

		if (!isVendorStyleExists) {
			VendorStyle vendorStyle = new VendorStyle();
			vendorStyle.setDescr((String) map.get("styledesc"));
			vendorStyle.setVendorStyleNumber((String) map.get("newstyleid"));
			vendorStyle.setVendorStyleName(BLANK);
			vendorStyle.setClassification(this.dropshipDao.getClassificationFromNumber((Short) map
					.get("classno")));
			vendorStyle.setVendorNumber((String) map.get("vendorno"));
			vendorStyle.setStatusCd(STYLE_STATUS_ACTIVE);
			vendorStyle.setCreatedDate(new Date());
			vendorStyle.setCreatedBy(USER_CREATING_CAR);
			vendorStyle.setUpdatedDate(new Date());
			vendorStyle.setUpdatedBy(USER_CREATING_CAR);
			vendorStyle.setVendorStyleType((VendorStyleType) this.dropshipDao.getById(
					VendorStyleType.class, VendorStyleType.PRODUCT));
			this.dropshipDao.saveEntityObject(vendorStyle);
		}
	}


	/**
	 * @param catalog
	 * @return
	 * @see com.belk.car.app.service.DropshipManager#getNumberOfRecordsImported(com.belk.car.app.model.vendorcatalog.VendorCatalog)
	 * @Enclosing_Method  getNumberOfRecordsImported
	 * @TODO
	 */
	public Map<BigDecimal, Long> getNumberOfRecordsImported() {

		Map<BigDecimal,List<BigDecimal>> map=new HashMap<BigDecimal, List<BigDecimal>>();
		Map<BigDecimal,Long> mapRecordNums=new HashMap<BigDecimal, Long>();
		List<BigDecimal> recordList=dropshipDao.getNumberOfRecordsImported();
		for(BigDecimal record:recordList){
			if(map.containsKey(record)){
				map.get(record).add(record);

			}
			else{
				List<BigDecimal> catalogIds=new ArrayList<BigDecimal>();
				catalogIds.add(record);
				map.put(record, catalogIds);
			}
		}
		Set<BigDecimal> keySet=map.keySet();
		for(BigDecimal key:keySet){
			mapRecordNums.put(key,new Long( map.get(key).size()));
		}
		return mapRecordNums;
	}


	/**
	 * @param catalog
	 * @return
	 * @see com.belk.car.app.service.DropshipManager#getNumberOfRecordsImportedInStyleSku(com.belk.car.app.model.vendorcatalog.VendorCatalog)
	 * @Enclosing_Method  getNumberOfRecordsImportedInStyleSku
	 * @TODO
	 */
	public Map<BigDecimal, Long>  getNumberOfRecordsImportedInStyleSku() {
		Map<BigDecimal,List<BigDecimal>> map=new HashMap<BigDecimal, List<BigDecimal>>();
		Map<BigDecimal,Long> mapRecordNums=new HashMap<BigDecimal, Long>();
		List<BigDecimal> recordList=dropshipDao.getNumberOfRecordsImportedInStyleSku();
		for(BigDecimal record:recordList){
			if(map.containsKey(record)){
				map.get(record).add(record);

			}
			else{
				List<BigDecimal> catalogIds=new ArrayList<BigDecimal>();
				catalogIds.add(record);
				map.put(record, catalogIds);
			}
		}
		Set<BigDecimal> keySet=map.keySet();
		for(BigDecimal key:keySet){
			mapRecordNums.put(key,new Long( map.get(key).size()));
		}
		return mapRecordNums;

	}

	/**
	 * @param vendorCatalogID - Latest catalog ID for vensor style
	 * @param vendorStyleId-Vendor Style ID
	 * @see com.belk.car.app.service.DropshipManager#getRecordsForLatestStyleUPC(java.lang.Long, long)
	 * @Enclosing_Method  getRecordsForLatestStyleUPC
	 * @TODO Get Latest record from catalog with latest UPC 
	 */
	public List<VendorCatalogRecord> getRecordsForLatestStyleUPC(Long vendorCatalogID, String vendorStyleNumber) {

		return dropshipDao.getRecordsForLatestStyleUPC( vendorCatalogID,  vendorStyleNumber) ;
	}

	/**
	 * @param carId
	 * @param attributeId
	 * @return
	 * @see com.belk.car.app.service.DropshipManager#getCarAttribute(long, long)
	 * @Enclosing_Method  getCarAttribute
	 * @TODO
	 */
	public CarAttribute getCarAttribute(long carId, long attributeId) {
		return dropshipDao.getCarAttribute(carId, attributeId);
	}

	/**
	 * @param carAttribute
	 * @see com.belk.car.app.service.DropshipManager#saveOrUpdateCarAttribute(com.belk.car.app.model.CarAttribute)
	 * @Enclosing_Method  saveOrUpdateCarAttribute
	 * @TODO
	 */
	public void saveOrUpdateCarAttribute(CarAttribute carAttribute) {
		dropshipDao.saveOrUpdateCarAttribute(carAttribute);

	}

	/**
	 * @param vendorCatalogID
	 * @param templateId
	 * @return
	 * @see com.belk.car.app.service.DropshipManager#getUnmappedAttribute(java.lang.Long, long)
	 * @Enclosing_Method  getUnmappedAttribute
	 * @TODO
	 */
	public List<VendorCatalogHeader> getUnmappedAttribute(Long vendorCatalogID, long templateId) {
		
		return dropshipDao.getUnmappedAttribute( vendorCatalogID,  templateId);
	}

	
	/**
	 * @param vendor
	 * @param vendorStyleId
	 * @return VendorStyle
	 * @TODO
	 */
	public VendorStyle getStyleByNumber(Vendor vendor, String vendorStyleNum) {
		
		return dropshipDao.getVendorStyleByNumber(vendorStyleNum,vendor);
	}



    public void processCars(Collection<IdbCarDataTO> col) {
                 //start processCars method called");
		if (col != null) {
			if (log.isDebugEnabled())
				log.debug("Processing Cars: There are " + col.size() + " vendor styles in the file.");
		}
                // get userName
                Config userName =null;
                try {
                    if(lookupManager==null) {
                    }
                   userName = (Config) lookupManager.getById(Config.class, Config.CAR_IMPORT_USER);
                } catch(Exception e) {
                    e.printStackTrace();
                }


		WorkflowStatus initiated = (WorkflowStatus) this.lookupManager.getById(WorkflowStatus.class, WorkflowStatus.INITIATED);
		UserType buyer = (UserType) this.lookupManager.getById(UserType.class, UserType.BUYER);

        SourceType sourceDropshipCar = carManager.getSourceTypeForCode(SourceType.DROPSHIP);
		AttributeValueProcessStatus checkRequired = lookupManager.getAttributeValueProcessStatus(AttributeValueProcessStatus.CHECK_REQUIRED);
		AttributeValueProcessStatus noCheckRequired = lookupManager.getAttributeValueProcessStatus(AttributeValueProcessStatus.NO_CHECK_REQUIRED);
		NoteType carNoteType = (NoteType) lookupManager.getById(NoteType.class, NoteType.CAR_NOTES);
		WorkFlow defaultWorkflow = workflowManager.getWorkFlow(workflowManager.getWorkflowType(WorkflowType.CAR));

		ContentStatus contentInProgress = (ContentStatus) lookupManager.getById(ContentStatus.class, ContentStatus.IN_PROGRESS);
		VendorStyleType vendorStyleTypeProduct = (VendorStyleType) lookupManager.getById(VendorStyleType.class, VendorStyleType.PRODUCT);

		User systemUser = this.userManager.getUserByUsername(userName.getValue());

		String vendorContactEmail = "vendor@belk.com";
		Date escalationDate = this.getEscalationDate();
		// Benchmark
		long executionTime = System.currentTimeMillis();
		int totalRecordSize = col.size();
		int rec = 0, numSkusInFile = 0, numElimSkus = 0;
		Map<VendorStyle, List<IdbCarDataTO>> productMap = new HashMap<VendorStyle, List<IdbCarDataTO>>();
		nextCarFromIdb:
		for (IdbCarDataTO idbCarTO : col) {
			if (log.isDebugEnabled())
				log.debug("Processing Vendor Style: " + idbCarTO.getVendorStyle() + " which has " + idbCarTO.getSkuInfo().size() + " skus...");
			numSkusInFile = numSkusInFile + idbCarTO.getSkuInfo().size();
			try {
				Department dept = carManager.getDepartment(idbCarTO.getDepartmentNumber());
				List<String> errorList = new ArrayList<String>();
				if (dept == null) {
					String errorText = "Department: " + idbCarTO.getDepartmentNumber() + " not setup in the database";
					errorList.add(errorText);
					if (log.isDebugEnabled()) {
						log.debug(errorText);
					}
				}
				idbCarTO.setDepartment(dept);
				Classification classification = null;
				if (StringUtils.isBlank(idbCarTO.getClassNumber())) {
					String errorText = "Classification Is Null For: " + idbCarTO.getVendorStyle() + ", invalid data!!";
					errorList.add(errorText);
					if (log.isDebugEnabled()) {
						log.debug(errorText);
					}
				} else {
					classification = carManager.getClassification(Short.parseShort(idbCarTO.getClassNumber()));
				}
				if (classification == null) {
					String errorText = "Classification: " + idbCarTO.getClassNumber() + " not setup in the database";
					errorList.add(errorText);
					if (log.isDebugEnabled()) {
						log.debug(errorText);
					}
				}

				idbCarTO.setClassification(classification);
              
				VendorStyle vendorStyle = carManager.getVendorStyle(idbCarTO.getVendorNumber(), idbCarTO.getVendorStyle());
				VendorStyle pattern = null;
				List<IdbCarSkuTO> existingSkus = new ArrayList<IdbCarSkuTO>();                               
				if (vendorStyle != null) {
					if (log.isDebugEnabled()) {
						log.debug("Vendor Style already exists in CARS");
					}
					idbCarTO.setVendorStyleObj(vendorStyle);
					idbCarTO.setVendor(vendorStyle.getVendor());

					if (StringUtils.isEmpty(StringUtils.trimToNull(vendorStyle.getVendorStyleName()))) {
						if (log.isDebugEnabled()) {
							log.debug("Update Vendor Style Information");
						}
						vendorStyle.setVendorStyleName(idbCarTO.getVendorStyleDescription());
						vendorStyle.setDescr(idbCarTO.getVendorStyleDescription());
						carManager.createVendorStyle(vendorStyle);
					}

					Vendor vendor = vendorStyle.getVendor();
					if (!StringUtils.equals(idbCarTO.getVendorName(), vendor.getName())) {
						if (log.isDebugEnabled()) {
							log.debug("Update Vendor Information");
						}
						vendor.setName(idbCarTO.getVendorName());
						vendor.setDescr(idbCarTO.getVendorName());
                        vendor.setIsDisplayable("Y");
						carManager.createVendor(vendor);
					}
					
					pattern = vendorStyle.getParentVendorStyle();
					List<VendorSku> skusOfStyle = carManager.getSkusForStyle(vendorStyle);
					Map<String, ArrayList<Car>> skumap = new HashMap<String, ArrayList<Car>>();
					java.util.Iterator<VendorSku> it = skusOfStyle.iterator();
					while(it.hasNext()){
						VendorSku vs = it.next();
						ArrayList<Car> carList;
						if((carList=skumap.get(vs.getBelkUpc()))== null)
							 carList = new ArrayList<Car>();
						carList.add(vs.getCar());
						skumap.put(vs.getBelkUpc(), carList);
					}
					
					if(!(skumap == null ||skumap.isEmpty())){				
						for(IdbCarSkuTO sku : idbCarTO.getSkuInfo()){
							List<Car> skuTocarList= null;
							if((skuTocarList=skumap.get(sku.getBelkUPC()))!= null){
								java.util.Iterator<Car> cariteraor = skuTocarList.iterator();
								while(cariteraor.hasNext()){
								Car skuCar = cariteraor.next();	
								
								/*if(skuCar.getStatusCd().equals(Status.DELETED)){
									if(skuCar.getSourceId().equals(idbCarTO.getPoNumber())){
										existingSkus.add(sku);
										if (log.isDebugEnabled()) 
											log.debug("sku "+ sku.getBelkUPC()+ " found in existing deleted car #"+ skuCar.getCarId() +" with same PO number ");
										break;
									}
								}
								else{
									 existingSkus.add(sku);
									 if (log.isDebugEnabled()) 
										log.debug("sku "+ sku.getBelkUPC() +" found in existing Active car #"+ skuCar.getCarId());
									 break;
								 }*/
								if(skuCar.getStatusCd().equals(Status.ACTIVE) ){
									existingSkus.add(sku);
									if (log.isDebugEnabled()) 
										log.debug("sku "+ sku.getBelkUPC() +" found in existing Active car #"+ skuCar.getCarId() +" elminating this sku ");
										break;
									}
								}
						   }
						}
							//Remove all Existing SKU's
						if (!existingSkus.isEmpty()) {
								if (log.isDebugEnabled()) {
									log.debug("Number of existing skus for elmination:"+existingSkus.size());
								}
									numElimSkus = numElimSkus + existingSkus.size();
									idbCarTO.getSkuInfo().removeAll(existingSkus);
							}
						
						if (idbCarTO.getSkuInfo().isEmpty()) {
								String errorText = "All Skus in found in other ACTIVE Cars (Skus):  " + existingSkus;
								errorList.add(errorText);
								if (log.isDebugEnabled())
									log.debug(errorText);
								}
						}
				}

				//Create CAR only if there are no CAR for the and or SKU does not exist in another CAR...
				// VENDOR_STYLE
				if (!errorList.isEmpty()) {
						numElimSkus = numElimSkus + idbCarTO.getSkuInfo().size();
						if (log.isDebugEnabled()){
							log.debug("All skus removed, go to next Car from IDB");
						}
						continue nextCarFromIdb;
				}

				//Go the next Row if there are no Skus that in the data"
				if (idbCarTO != null && idbCarTO.getSkuInfo().isEmpty()) {
					if (log.isDebugEnabled())
						log.debug("There is not SKU information available for Product: " + idbCarTO.getVendorStyle()+" go to next Car from IDB");
					continue nextCarFromIdb;
				}

				if (vendorStyle == null) {
					if (log.isDebugEnabled()){
						log.debug("Vendor Style not exists in CARS");
					}
					Vendor vendor = carManager.getVendor(idbCarTO.getVendorNumber());
					if (vendor == null) {
						vendor = new Vendor();
						vendor.setName(idbCarTO.getVendorName());
						vendor.setVendorNumber(idbCarTO.getVendorNumber());
						vendor.setDescr(idbCarTO.getVendorName());
						vendor.setStatusCd(Status.ACTIVE);
						vendor.setContactEmailAddr(vendorContactEmail);
                        vendor.setIsDisplayable("Y");
						this.setAuditInfo(systemUser, vendor);
						vendor = carManager.createVendor(vendor);
						if (log.isDebugEnabled())
							log.debug("New Vendor Created, vendor Id: " + vendor.getVendorId());

					} else if (!StringUtils.equals(idbCarTO.getVendorName(), vendor.getName())) {
						vendor.setName(idbCarTO.getVendorName());
						vendor.setDescr(idbCarTO.getVendorName());
						vendor.setStatusCd(Status.ACTIVE);
						this.setAuditInfo(systemUser, vendor);
                        vendor.setIsDisplayable("Y");
						vendor = (Vendor) carManager.createVendor(vendor);
						if (log.isDebugEnabled())
							log.debug("Vendor ID: " + vendor.getVendorId() +" Updated ");

					}

					//Removed the code which creates pattern based on pattern processing rule of classifications
					if (log.isDebugEnabled())
						log.debug("Creating new Vendor Style ");
					vendorStyle = new VendorStyle();
					vendorStyle.setVendorNumber(idbCarTO.getVendorNumber());
					vendorStyle.setVendorStyleName(idbCarTO.getVendorStyleDescription());
					vendorStyle.setVendorStyleNumber(idbCarTO.getVendorStyle());
					vendorStyle.setDescr(idbCarTO.getVendorStyleDescription()); 
					vendorStyle.setStatusCd(Status.ACTIVE);
					this.setAuditInfo(systemUser, vendorStyle);
					vendorStyle.setVendorStyleType(vendorStyleTypeProduct);
					vendorStyle.setVendor(vendor);
					vendorStyle.setClassification(classification);
					vendorStyle.setParentVendorStyle(pattern);
					vendorStyle = carManager.createVendorStyle(vendorStyle);
					idbCarTO.setVendor(vendor);
					idbCarTO.setVendorStyleObj(vendorStyle);
					if (log.isDebugEnabled())
						log.debug("Vendor Style Created with ID: " + vendorStyle.getVendorStyleId());
				}

				List<IdbCarDataTO> vendorStyleList = null;
				if (pattern != null) {
					if (productMap.containsKey(pattern)) {
						vendorStyleList = productMap.get(pattern);
					} else {
						vendorStyleList = new ArrayList<IdbCarDataTO>();
						productMap.put(pattern, vendorStyleList);
					}
					vendorStyleList.add(idbCarTO);
				} else {
					vendorStyleList = new ArrayList<IdbCarDataTO>();
					vendorStyleList.add(idbCarTO);
					productMap.put(vendorStyle, vendorStyleList);
				}
			} catch (Exception ex) {
				if (log.isErrorEnabled()) {
					ex.printStackTrace();
					log.error("Transaction Exception. Cause= " + ex.getMessage());
				}
			}
		}

		if (log.isDebugEnabled()) {
			log.debug("Going to Process Cars: # of Vendor Styles to process : " + productMap.size());
			log.debug("<!------ # SKUs - Num Of Sku In File: " + numSkusInFile + "  ------->");
			log.debug("<!------ # SKUs - Num Of Skuz Eliminated: " + numElimSkus + " -------->");
		}

		for (VendorStyle vs : productMap.keySet()) {
			if (log.isDebugEnabled())
				log.debug("Process Cars for Vendor Style: " + vs.getVendorStyleNumber());

			long startime = System.currentTimeMillis();
			List<IdbCarDataTO> lCarData = productMap.get(vs);
			Car car = null;
			CarNote note = null;
			for (IdbCarDataTO idbCarTO : lCarData) {
				log.debug("Processing Record: " + (++rec) + " of " + totalRecordSize);
				if (car == null) {
					car = new Car();
					car.setDepartment(idbCarTO.getDepartment());
					car.setSourceType(sourceDropshipCar);
					car.setSourceId(idbCarTO.getVendorNumber() + "-" + idbCarTO.getVendorStyle());
					car.setVendorStyle(vs);
					car.setWorkFlow(defaultWorkflow);
					car.setCarUserByLoggedByUserId(systemUser);
					car.setAssignedToUserType(buyer);
					car.setCurrentWorkFlowStatus(initiated);

					car.setEscalationDate(escalationDate);

					car.setIsUrgent(Constants.FLAG_NO);

					if (StringUtils.isNotBlank(idbCarTO.getExpectedShipDate())) {
						car.setExpectedShipDate(DateUtils.parseDate(idbCarTO.getExpectedShipDate(), "yyyy-MM-dd"));
						car.setDueDate(this.getDueDate());
					} else {
						car.setExpectedShipDate(this.getCompletionDate());
						car.setDueDate(this.getDueDate());
					}

					String isProductTypeReq = Constants.FLAG_NO;
					List<ProductType> l = productManager.getProductTypes(vs.getClassification().getClassId());
					ProductType productType = null;
					if (l != null && !l.isEmpty()) {
						if (l.size() > 1) {
							isProductTypeReq = Constants.FLAG_YES;
						} else {
							productType = l.get(0);
						}
					}
					car.setIsProductTypeRequired(isProductTypeReq);
					car.setStatusCd(Status.ACTIVE);

					car.setContentStatus(contentInProgress);
					car.setLastWorkflowStatusUpdateDate(Calendar.getInstance().getTime());

					this.setAuditInfo(systemUser, car);

					if (productType != null) {
						List<DepartmentAttribute> departmentAttributes = carManager.getAllDepartmentAttributes(car.getDepartment().getDeptId());
						List<ClassAttribute> classificationAttributes = carManager.getAllClassificationAttributes(car.getVendorStyle().getClassification()
								.getClassId());

						Map<String, Attribute> attributeMap = new HashMap<String, Attribute>();
						Map<String, String> attributeDefaultValueMap = new HashMap<String, String>();

						for (DepartmentAttribute deptAttr : departmentAttributes) {
							if (deptAttr.getAttribute().isActive()) {
								attributeMap.put(deptAttr.getAttribute().getBlueMartiniAttribute(), deptAttr.getAttribute());
								attributeDefaultValueMap.put(deptAttr.getAttribute().getBlueMartiniAttribute(), StringUtils.defaultString(deptAttr
										.getDefaultAttrValue()));
							}
						}

						//Setup Product Type Attributes"
						if (productType != null) {
							vs.setProductType(productType);
							for (ProductTypeAttribute ptAttr : productType.getProductTypeAttributes()) {
								if (ptAttr.getAttribute().isActive()) {
									if (attributeMap.containsKey(ptAttr.getAttribute().getBlueMartiniAttribute())) {
										attributeMap.remove(ptAttr.getAttribute().getBlueMartiniAttribute());
										attributeDefaultValueMap.remove(ptAttr.getAttribute().getBlueMartiniAttribute());
									}
									attributeMap.put(ptAttr.getAttribute().getBlueMartiniAttribute(), ptAttr.getAttribute());
									attributeDefaultValueMap.put(ptAttr.getAttribute().getBlueMartiniAttribute(), StringUtils.defaultString(ptAttr
											.getDefaultAttrValue()));
								}
							}
						}

						for (ClassAttribute classAttr : classificationAttributes) {
							if (classAttr.getAttribute().isActive()) {
								if (attributeMap.containsKey(classAttr.getAttribute().getBlueMartiniAttribute())) {
									attributeMap.remove(classAttr.getAttribute().getBlueMartiniAttribute());
									attributeDefaultValueMap.remove(classAttr.getAttribute().getBlueMartiniAttribute());
								}
								attributeMap.put(classAttr.getAttribute().getBlueMartiniAttribute(), classAttr.getAttribute());
								attributeDefaultValueMap.put(classAttr.getAttribute().getBlueMartiniAttribute(), StringUtils.defaultString(classAttr
										.getDefaultAttrValue()));
							}
						}
						
						
						for (Attribute attribute : attributeMap.values()) {
							CarAttribute carAttribute = new CarAttribute();
							carAttribute.setAttribute(attribute);
							carAttribute.setCar(car);
							if (attribute.getAttributeConfig().getHtmlDisplayType().isAutocomplete()) {
								carAttribute.setAttributeValueProcessStatus(checkRequired);
							} else {
								carAttribute.setAttributeValueProcessStatus(noCheckRequired);
							}
							carAttribute.setAttrValue(attributeDefaultValueMap.get(attribute.getBlueMartiniAttribute()));
							// set the online only attribute value as Yes for the dropship CAR
							// either at product level or sku level
							if(carAttribute.getAttribute().getBlueMartiniAttribute().indexOf("On Line") != -1){
								if(log.isDebugEnabled()){
									log.debug("Online Only Attribute at Product Level set as Yes");
								}
								carAttribute.setAttrValue("Yes");
							}
							carAttribute.setDisplaySeq((short) 0);
							carAttribute.setHasChanged(Constants.FLAG_NO);
							carAttribute.setIsChangeRequired(Constants.FLAG_YES);
							carAttribute.setStatusCd(Status.ACTIVE);
							this.setAuditInfo(systemUser, carAttribute);
							car.getCarAttributes().add(carAttribute);
						}
					}

				}
				
				// Check whether online only attribute at product level 
				String setOnlineOnly=Constants.FLAG_NO;
				Set<CarAttribute> carAttrs= car.getCarAttributes();
				for(CarAttribute ca:carAttrs){
					if(ca.getAttribute().getBlueMartiniAttribute().indexOf("On Line") != -1){
							if(log.isDebugEnabled()){
								log.debug("Online Only Attribute at Product Level");
							}
							setOnlineOnly=Constants.FLAG_YES;
							break;
						}
				}

				if (StringUtils.isNotBlank(idbCarTO.getExpectedShipDate()) && StringUtils.isNotBlank(idbCarTO.getPoNumber())) {
					Date expectedShipDate = DateUtils.parseDate(idbCarTO.getExpectedShipDate(), "yyyy-MM-dd");
					if (expectedShipDate.before(car.getExpectedShipDate())) {
						car.setExpectedShipDate(expectedShipDate);
						car.setSourceId(idbCarTO.getPoNumber());
					}
				}

				//Add Note to Existing Car...
				if (idbCarTO.getExistingCar() != null && note == null) {
					note = new CarNote();
					note.setNoteType(carNoteType);
					note.setIsExternallyDisplayable(CarNote.FLAG_NO);
					note.setStatusCd(Status.ACTIVE);
					note.setCar(car);
					note.setNoteText("Car ID: " + idbCarTO.getExistingCar().getCarId()
							+ " exists for the same Vendor Style.  You can use the copy functionality to update the attributes from this Car.");
					this.setAuditInfo(systemUser, note);
					car.getCarNotes().add(note);
				}

				// Create CAR/CAR SKU Attributes"
				Attribute onlineOnlyAtr=dropshipDao.getAttributeByName("SDF_Online Only");
				Attribute dropShipAtr=dropshipDao.getAttributeByName("IS_DROPSHIP");
				for (IdbCarSkuTO idbSku : idbCarTO.getSkuInfo()) {

					VendorSku sku = new VendorSku();
					sku.setCar(car);
					sku.setBelkSku(idbSku.getBelkUPC());
					sku.setLongSku(idbSku.getLongSku());
					sku.setBelkUpc(idbSku.getBelkUPC());
					sku.setVendorUpc(idbSku.getLongSku());
					sku.setStatusCd(Status.ACTIVE);
					sku.setColorCode(idbSku.getVendorColor());
					sku.setColorName(idbSku.getVendorColorName());
					sku.setSizeCode(idbSku.getVendorSizeCode());
					sku.setSizeName(idbSku.getVendorSizeDesc());
					//CARS Size Conversion Issue - Size name overwritten by resync size job-->
					sku.setIdbSizeName(idbSku.getVendorSizeDesc()); //Adding size name of the SKU in conversion_name column to show the incoming size name in cars edit page
					sku.setParentUpc(idbSku.getParentUPC());
					sku.setSetFlag(idbSku.getSetFlag());
					sku.setVendorStyle(idbCarTO.getVendorStyleObj());
					
					// if online only attribute is not set at product level
					// set at sku level
					if(Constants.FLAG_NO.equals(setOnlineOnly)){
						if(log.isDebugEnabled()){
							log.debug("Online Only Attribute at Sku Level");
						}
						CarSkuAttribute onlineSkuAttr=new CarSkuAttribute();
						onlineSkuAttr.setAttribute(onlineOnlyAtr);
						onlineSkuAttr.setAttrValue("Yes");
						this.setAuditInfo(systemUser, onlineSkuAttr);
						onlineSkuAttr.setVendorSku(sku);
						sku.getCarSkuAttributes().add(onlineSkuAttr);
					}
					// creating the dropship  attribute at sku level 
					CarSkuAttribute dropshipSkuAttr=new CarSkuAttribute();
					dropshipSkuAttr.setAttribute(dropShipAtr);
					dropshipSkuAttr.setAttrValue("Yes");
					this.setAuditInfo(systemUser, dropshipSkuAttr);
					dropshipSkuAttr.setVendorSku(sku);
					sku.getCarSkuAttributes().add(dropshipSkuAttr);
					
					this.setAuditInfo(systemUser, sku);
					
					car.getVendorSkus().add(sku);

				}
			}

			CatalogProduct product = null;
			if (car.getVendorStyle().isPattern()) {
				VendorStyleSearchCriteria criteria = new VendorStyleSearchCriteria();
				criteria.setVendorStyleId(car.getVendorStyle().getVendorStyleId());
				criteria.setChildrenOnly(true);
				if (log.isDebugEnabled())
					log.debug("Pattern - Search for child product information ");
				List<VendorStyle> childProducts = this.carManager.searchVendorStyle(criteria);
				if (childProducts != null) {
					for (VendorStyle childProduct : childProducts) {
						if (log.isDebugEnabled())
							log.debug("Pattern - Searching for product information in catalog " + childProduct.getVendorStyleNumber());
						product = catalogImportManager.getProduct(childProduct.getVendorNumber(), childProduct.getVendorStyleNumber());
						if (product != null) {
							break;
						}
					}
				}

			} else {
				if (log.isDebugEnabled())
					log.debug("Product - Searching for product information in catalog " + car.getVendorStyle().getVendorStyleNumber());
				product = catalogImportManager.getProduct(car.getVendorStyle().getVendorNumber(), car.getVendorStyle().getVendorStyleNumber());

			}

			if (product == null) {
				//check based on sku
				CatalogSku sku = null;
				if (car != null && car.getVendorSkus() != null && !car.getVendorSkus().isEmpty()) {
					catalogImportManager.getSku(car.getVendorSkus().iterator().next().getVendorUpc());
				}

				if (sku != null) {
					product = sku.getCatalogProduct();
				}
			}

			if (product != null && product.getCatalogProductId() != 0 && car.getVendorStyle().getProductType() != null) {
				catalogImportManager.copyToCar(product, car);
				if (car.getCarId() == 0) {
					car.setAssignedToUserType(buyer);
				}
			}

			try {
				if (log.isDebugEnabled())
					log.debug("Creating Car for Vendor Style with ID: " + car.getVendorStyle().getVendorStyleId());
				car = carManager.createCar(car);
				if (log.isDebugEnabled())
					log.debug("Car with ID: " + car.getCarId()+ " created successfully");
			} catch (Exception ex) {
				if (log.isErrorEnabled())
					log.error("Save Exception. Cause: " + ex.getMessage());
			}

			if (log.isInfoEnabled())
				log.info("End time for current Car = " + (System.currentTimeMillis() - startime));
		}

		if (log.isInfoEnabled()) {
			log.info("----------------> Overall execution time in minutes  = " + (System.currentTimeMillis() - executionTime) / 60000);
			log.info("---------------->  End Process Dropship Car Data   <-----------------");
			}
        }

        /**
         * Returns Escalation Date
         * @return Date
         */
        private Date getEscalationDate() {
		Config numberOfEscalationDays = (Config) lookupManager.getById(Config.class, Config.INIT_NUMBER_OF_ESCALATION_DAYS);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, Integer.parseInt(numberOfEscalationDays.getValue()));
		return cal.getTime();
	}

	 /**
         * Returns Due Date
         * @return Date
         */
        private Date getDueDate() {
		Config numberOfDueDays = (Config) lookupManager.getById(Config.class, Config.INIT_NUMBER_OF_DUE_DAYS);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, Integer.parseInt(numberOfDueDays.getValue()));
		return cal.getTime();
	}
        /**
         *  Set Audit Info
         * @param user User
         * @param model BaseAuditableModel
         */
        public void setAuditInfo(User user, BaseAuditableModel model) {

		if (StringUtils.isBlank(model.getCreatedBy())) {
			model.setCreatedBy(user.getUsername());
			model.setCreatedDate(new Date());
		}
		model.setUpdatedBy(user.getUsername());
		model.setUpdatedDate(new Date());
	}

         /**
         * Returns Completoion Date
         * @return Date
         */
	private Date getCompletionDate() {
		//Config numberOfEscalationDays = (Config) lookupManager.getById(Config.class, Config.INIT_NUMBER_OF_ESCALATION_DAYS);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, Car.NUM_DAYS_TO_COMPLETION);
		return cal.getTime();
	}

		/**
		 * @param vendorStyleForComparison
		 */
		public void saveStyle(VendorStyle vendorStyleForComparison) {
			dropshipDao.saveStyle( vendorStyleForComparison);
			
		}
		
		// added methods for dropship phase 2
		public Attribute getAttributeByName(String getAttributeByName){
			return dropshipDao.getAttributeByName(getAttributeByName);
		}
		
		public List<Car> getCarsByVendorStyle(String vendorStyle){
			return dropshipDao.getCarsByVendorStyle(vendorStyle);
		}
		
		public List<VendorCatalogStyleSkuMaster> getLatestCatalogStyles(){
			return dropshipDao.getLatestCatalogStyles();
		}
		
		public List<FulfillmentServiceVendor> getVendorExpditedShippingValues(){
			return dropshipDao.getVendorExpditedShippingValues();
		}
		public List<FulfillmentServiceVendor> getLatestVendorExpditedShippingValues(String strDateLastRun){
			return dropshipDao.getLatestVendorExpditedShippingValues(strDateLastRun);
		}
		public VendorStyle getVendorStyleFromNumber(String styleNumber) throws Exception {
			return dropshipDao.getVendorStyleFromNumber(styleNumber);
		}
}
