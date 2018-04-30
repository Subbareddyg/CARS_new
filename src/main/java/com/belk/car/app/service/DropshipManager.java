
package com.belk.car.app.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.appfuse.service.UniversalManager;

import com.belk.car.app.exceptions.SendEmailException;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarImage;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.Image;
import com.belk.car.app.model.ImageSourceType;
import com.belk.car.app.model.ImageTrackingStatus;
import com.belk.car.app.model.ImageType;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.model.oma.ItemRequest;
import com.belk.car.app.model.oma.VFIRStatus;
import com.belk.car.app.model.vendorcatalog.VendorCatalog;
import com.belk.car.app.model.vendorcatalog.VendorCatalogFieldDataMapping;
import com.belk.car.app.model.vendorcatalog.VendorCatalogHeader;
import com.belk.car.app.model.vendorcatalog.VendorCatalogImport;
import com.belk.car.app.model.vendorcatalog.VendorCatalogRecord;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSku;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSkuId;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSkuImage;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSkuMaster;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSkuMasterId;
import com.belk.car.app.to.IdbCarDataTO;
import com.belk.car.app.to.IdbDropshipDataTO;
import java.util.Collection;

/**
 * Business Service Interface to talk to persistence layer
 * 
 * @author AFUSY45
 */
public interface DropshipManager extends UniversalManager {
	/**
	 * Method to get value associated value for ID in DB
	 * @param cls,id
	 * @return Object
	 */
	@SuppressWarnings("unchecked")
	public Object getById(Class cls, Serializable id);

	@SuppressWarnings("unchecked")
	public void saveFeed(List<IdbDropshipDataTO> ob);

	public void saveItemRequestList(List itemRequestObjects);

	public List<IdbDropshipDataTO> getVendorUpcs(Set vendorUPCs);

	public void saveStylesToVFIRStyle(
			String vendorStyle, String vendorStyleDesc,
			Double idbStyleUnitCost, ItemRequest itemRequest, Vendor vendorObj);

	public void saveStylesToVFIRStyleForPurge(
			String vendorStyle, String vendorStyleDesc,
			Double idbStyleUnitCost, ItemRequest itemRequest);

	public void saveStyleSKUToVFIRStyleSKU(
			String VendorStyleID, String getBelkUPC,Double idbUPCUnitCost,String colorDesc, String sizeDesc,ItemRequest itemRequest) throws Exception;

	public void saveStyleSKUToVFIRStyleSKUForPurge(
			String VendorStyleID, String getBelkUPC,Double idbUPCUnitCost,String colorDesc, String sizeDesc,ItemRequest itemRequest) throws Exception;

	public void saveWorkflowHist(ItemRequest itemRequest,VFIRStatus workflowStatus,String action);

	public void saveVendorCatalogNote(VendorCatalog catalog,String noteSubject,List noteText) throws ParseException;

	public void changeStatusOfCatalogFile(VendorCatalog catalog);

	//public List<VendorCatalogDepartment> getDeptForCatalogID(Long catalogID) throws Exception;

	//	public List<Object[]> getCarUserIDs();

	//	public List getDeptIDList(String carUserID);

	public List getBuyerMailIDs(Long catalogID);


	public void createVendor(Vendor vendor);

	public List<FulfillmentServiceVendor> getFulfillmentServiceIdForVenID(
			Long vendorId);
	
	/**
	 * @param status
	 * @return List<VendorCatalog>
	 * @TODO Get catalogs with status-translating
	 */
	public List<VendorCatalog> getCatalogsWithStatus(String status) ;

	/**
	 * @param templateId
	 * @param catalogId
	 * @param attrId1
	 * @param attrId2
	 * @param attrId3
	 * @return List<VendorCatalogRecord>
	 * @TODO To get records ordered by order number , given the attribute IDs
	 *       from MASTER_ATTRIBUTE_MAPPING table
	 */
	public List<VendorCatalogRecord> getRecordsForStandardAttributes(
			long templateId, long catalogId, long styleId, long deptId,
			long vendorUPC);

	/**
	 * @param styleSkuList
	 * @throws Exception 
	 * @TODO To save the style sku list in database
	 */
	public void saveStyleSkus(List<VendorCatalogStyleSku> styleSkuList) throws Exception;



	public void saveHeader(List<VendorCatalogHeader> headerList);

	public void saveRecord(List<VendorCatalogRecord> recordList);

	public void deleteRecord(Long vendorCatalogID);

	/**
	 * @return Map for car id and corresponding images
	 * @TODO Method to get all active images for CAR with in-progress status
	 *       Used while saving car images from catalog
	 */
	public Map<Long, List<Image>> getCARSInProgress();

	/**
	 * Retrieve all of the Attribute objects associated with the given CAR
	 * 
	 * @param carID
	 * @return a list of Attribute objects
	 */
	public List<Attribute> getAttributesforCar(long carId);

	/**
	 * @param vendorStyleNumber
	 * @return List<VendorCatalogStyleSkuMaster>
	 * @TODO To get the master records from master table for a vendor style
	 */
	public List<VendorCatalogStyleSkuMaster> getMasterRecordsForStyle(
			String vendorStyleNumber);

	/**
	 * @param compositeKey
	 * @return VendorCatalogStyleSku
	 * @TODO To get the style sku based on the composite ID
	 */
	public VendorCatalogStyleSku getStyleSkuForMaster(
			VendorCatalogStyleSkuId compositeKey);

	/**
	 * @param recordNum
	 * @param vendorCatalogId
	 * @return List<VendorCatalogRecord>
	 * @TODO - To get the records from record table with a given record number
	 */
	public List<VendorCatalogRecord> getAllRecordsWithRecordNum(
			long recordNum, long vendorCatalogId);

	/**
	 * @param recordModel
	 * @param templateId
	 * @return Attribute 
	 * @TODO To get the attribute for  a record 
	 */
	public String getAttributeForRecord(VendorCatalogRecord recordModel, long templateId);

	/**
	 * @param vendorCatalogId
	 * @return VendorCatalog
	 * To get the catalog based on catalog ID
	 */
	public VendorCatalog getCatalogForId(Long vendorCatalogId);

	/**
	 * @param carId
	 * @return List<VendorSku>
	 * @TODO - To get the vendor skus for a car from vendor sku table.
	 */
	public List<VendorSku> getVendorSku(
			Long carId);

	/**
	 * @param attributeIdFromMapping
	 * @return Attribute
	 * @TODO To get the attribute based on the attribute ID
	 */
	public Attribute getAttributeByID(String attributeIdFromMapping);

	/**
	 * @param carSkuAttribute
	 * @TODO To save the car sku attribute
	 */
	public void saveOrUpdateCarSkuAttribute(CarSkuAttribute carSkuAttribute);

	
	/*
	 * Method added by Priyanka Gadia
	 *  Retrieve the mapped record value for the given record
	 * @param: tempalte id
	 * @param: record models
	 * @return data field mapping
	 */
	public VendorCatalogFieldDataMapping getVendorCatalogFieldDataMapping(
			long templateIdMaster, VendorCatalogRecord recordModel);

	/**
	 * @param carSkuId
	 * @param attributeIdFromMapping
	 * @return CarSkuAttribute
	 * @throws ClassCastException
	 * @TODO To get the car sku attributes based on the attribute and car sku id 
	 */
	public CarSkuAttribute getCarSkuAttribute(
			Long carSkuId, String attributeIdFromMapping);

	//	public void fillCatalogDataInMasterTable(List<VendorCatalog> catalogs);


	public void sendEmailNotification(VendorCatalog catalog, int totalRecords,Map<String, List> errorMap, int alertMsg) throws Exception;

	public void sendEmailForCatalogDataMapping(VendorCatalog catalog,List emailIDs) throws SendEmailException;

	public List<VendorCatalogImport> getVendorCatalogImportInfo(Long catalogId);

	public void saveImageData(List fileName,Long catalogId) throws ParseException;

	/**
	 * @param catalogs
	 * @see com.belk.car.app.service.DropshipManager#setImageForCatalogInCatalogImageTable(java.util.List)
	 * @Enclosing_Method setImageForCatalogInCatalogImageTable
	 * @TODO To process images and insert into image table along with copying it
	 *       to defined location.
	 */
	public void setImageForCatalogInCatalogImageTable(
			List<VendorCatalog> catalogs);


	/**
	 * @param master
	 * @return List<VendorCatalogStyleSkuImage>
	 * @TODO Method to fetch images from VNDR_CATL_STY_SKU_IMAGE table for given
	 *       catalog, style and sku
	 */
	public List<VendorCatalogStyleSkuImage> getImagesForStyleSku(
			VendorCatalogStyleSkuMaster master);

	/**
	 * @param attrId
	 * @param templateId
	 * @param catalogId
	 * @return VendorCatalogHeader
	 * @TODO Get the header for attribute in MASTER_ATTRIBUTE_MAPPING table
	 */
	public VendorCatalogHeader getMappingForMasterAttribute(
			long styleIdAttrId, long templateId, long catalogId);

	/**
	 * @param catalogId
	 * @param headerList
	 * @return List<VendorCatalogRecord>
	 * @TODO Method to get records for the header numbers Used while filling
	 *       VENDOR_CATALOG_STY_SKU table
	 */
	public List<VendorCatalogRecord> getRecordsForHeaderNums(
			long catalogId, List<Long> headerList);

	//public List<Image> getAllActiveCarImages(Long carId);
	/**
	 * @return Map<Car, Set<VendorSku>>
	 * @TODO Method to get skus for CAR with in-progress status Used while
	 *       saving car images from catalog
	 */
	public Map<Car, Set<VendorSku>> getCarAndSkus();

	public Map<String, ImageType> getImageTypes();

	public ImageSourceType getImageSourceType(String string);

	/**
	 * @param string
	 * @return ImageTrackingStatus
	 * Get the tracking status of image 
	 */
	public ImageTrackingStatus getImageTrackingStatus(String string);

	/**
	 * @param carImageListNew
	 * @TODO Save or update car images in CAR_IMAGE table
	 */
	public void saveOrUpdateCarImages(List<CarImage> carImageListNew);

	/**
	 * @param imageListNew
	 * @return List<Image>
	 * @TODO Method to save images in IMAGE table Used while saving car images
	 *       from catalog
	 */
	public List<Image> saveOrUpdateImages(List<Image> imageListNew);

	/**
	 * @return List<VendorCatalogStyleSkuImage>
	 * @TODO Method to get all ACTIVE images for each record in master table.
	 *       Used while saving car images from catalog
	 */
	public Map<VendorCatalogStyleSkuMasterId, List<VendorCatalogStyleSkuImage>> getAllImagesforMaster();

	/**
	 * @param vendorCatalogID
	 * @return VendorCatalogImport
	 * @TODO To get import information for vendor catalog
	 */
	public VendorCatalogImport getVendorCatalogImportForCatalog(
			Long vendorCatalogID);

	/**
	 * @return List<VendorCatalogStyleSkuMaster>
	 * @TODO Method to fetch MASTER records from VNDR_CATL_STY_SKU_MAST table
	 *       Used while filling VNDR_CATL_STY_SKU_MAST table using overwrite
	 *       action
	 */
	public List<VendorCatalogStyleSkuMaster> getAllMasterRecords(long vendorId);

	/**
	 * @param VendorCatalogStyleSkuMaster List
	 * @TODO Method to save MASTER records from VNDR_CATL_STY_SKU_MAST table
	 *       Used while filling VNDR_CATL_STY_SKU_MAST table .
	 */
	public void saveOrUpdateMasterList(
			List<VendorCatalogStyleSkuMaster> masterListNew);

	/**
	 * @param vendorCatalogID
	 * @return List of VendorCatalogStyleSku
	 * @TODO Method to fetch records form VENDOR_CATALOG_STY_SKU Used while
	 *       filling VNDR_CATL_STY_SKU_MAST table
	 */
	public List<VendorCatalogStyleSku> getStyelSkusWithCatalogId(
			Long vendorCatalogID);

	/**
	 * @param vendorStyleId
	 * @return Long department id
	 * @TODO To get the department id for vendor style
	 */
	public Long getDepartmentIdForVendorStyle(VendorStyle vendorStyle);

	/**
	 * @param catalogListNew
	 * @TODO To save the catalog to complete
	 */
	public void saveCatalogsToComplete(List<VendorCatalog> catalogListNew);

	/**
	 * This method checks if a given belk upc exists and if so then it returns the object of that upc.
	 * @param upc
	 * @return
	 * @throws Exception
	 */
	public Object checkUpcExists(String upc) throws Exception;

	/**
	 * This method creates a CAR.
	 * @param vendorSku
	 * @param map
	 * @throws Exception
	 */
	public void createCarForCrossReferenceFeed(Object vendorSku, Map<String, Object> map) throws Exception;

	/**
	 * @param catalog
	 * @return List of records imported.
	 * @TODO To get the number of records imported.
	 */
	public Map<BigDecimal, Long> getNumberOfRecordsImported();

	/**
	 * @param catalog
	 * @return List of records imported.
	 * @TODO To get the number of records imported in style sku table.
	 */
	public Map<BigDecimal, Long> getNumberOfRecordsImportedInStyleSku();

	/**
	 * @param vendorCatalogID
	 * @param vendorStyleNumber
	 * @return void
	 */
	public List<VendorCatalogRecord> getRecordsForLatestStyleUPC(Long vendorCatalogID, String vendorStyleNumber);

	/**
	 * @param carId
	 * @param attributeId
	 * @return
	 * @return CarAttribute
	 * @Enclosing_Method  getCarAttribute
	 * @TODO
	 */
	public CarAttribute getCarAttribute(long carId, long attributeId);

	/**
	 * @param carAttribute
	 * @return void
	 * @Enclosing_Method  saveOrUpdateCarAttribute
	 * @TODO
	 */
	public void saveOrUpdateCarAttribute(CarAttribute carAttribute);

	/**
	 * @param vendorCatalogID
	 * @param templateId
	 * @return
	 * @return Set<VendorCatalogHeader>
	 * @Enclosing_Method  getUnmappedAttribute
	 * @TODO
	 */
	public List<VendorCatalogHeader> getUnmappedAttribute(Long vendorCatalogID, long templateId);

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
	 * Process the images for catalogs.
	 */
	public void processImage(
			long catalogId, String vendorNumber, String styleId, String vendorStyleNum,
			String vendorUpc, String color, char imageType, String imageName);

	/**
	 * @param vendor
	 * @param vendorStyleId
	 * @return
	 * @return VendorStyle
	 * @TODO
	 */
	public VendorStyle getStyleByNumber(Vendor vendor, String vendorStyleId);


        public void processCars(Collection<IdbCarDataTO> col);

		/**
		 * @param vendorStyleForComparison
		 * @return void
		 */
		public void saveStyle(VendorStyle vendorStyleForComparison);
		
		// added methods for dropship phase 2
		public Attribute getAttributeByName(String getAttributeByName);
		public List<Car> getCarsByVendorStyle(String vendorStyle);
		public List<VendorCatalogStyleSkuMaster> getLatestCatalogStyles();
		public List<FulfillmentServiceVendor> getVendorExpditedShippingValues();
		public List<FulfillmentServiceVendor> getLatestVendorExpditedShippingValues(String strDateLastRun);
		
		public VendorStyle getVendorStyleFromNumber(String styleNumber) throws Exception;
		
}

