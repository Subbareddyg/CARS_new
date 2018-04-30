
package com.belk.car.app.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.appfuse.model.User;
import org.springframework.dao.DataAccessException;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarImage;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.Image;
import com.belk.car.app.model.ImageType;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.model.oma.ItemRequestWorkflow;
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
import com.belk.car.app.to.IdbDropshipDataTO;

/**
 * Interface with methods used by DropshipManager
 * 
 * @author afusy45
 */
public interface DropshipDao {

	/**
	 * Method to get value associated value for ID in DB
	 * @param cls,id
	 * @return Object
	 */
	public Object getById(Class cls, Serializable id);

	public void saveFeed(List<IdbDropshipDataTO> ob);

	public void saveItemRequestList(List itemRequestObjects);
	
	public List<IdbDropshipDataTO> getVendorUpcs(Set vendorUPCs);

	public List<FulfillmentServiceVendor> getFulfillmentServiceIdForVenID(
			Long vendorId);

	public void uploadSKUs(VFIRStyleSku styleSku)
	throws DataAccessException;

	public void uploadStyles(VFIRStyle style) throws DataAccessException ; 
	//public List<VendorCatalogDepartment> getDeptForCatalogID(Long catalogID);
	
//	public List<Object[]> getCarUserIDs();
	
	//public List getDeptIDList(String carUserID);
	
	public void changeStatusOfCatalogFile(VendorCatalog catalog);
	
	public List getBuyerMailIDs(Long catalogID);
	
	public void createVendor(Vendor vendor);

	/**
	 * @param status
	 * @return List<VendorCatalog>
	 * @TODO Get catalogs with status-translating
	 */
	public List<VendorCatalog> getCatalogsWithStatus(String status);

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

	public void saveWorkflowHist(ItemRequestWorkflow itemRequestWorkflow);
	
	/**
	 * Method to save errors to vendor catalog note table
	 */
	public void saveVendorCatalogNote(List<VendorCatalogNote> catalogNoteList);
	
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
	public String getAttributeForRecord(VendorCatalogRecord recordModel,long templateId);

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

	/**
	 * @param vendorCatalogID
	 * @return VendorCatalogImport
	 * @TODO To get import information for vendor catalog
	 */
	public VendorCatalogImport getVendorCatalogImportForCatalog(
			Long vendorCatalogID);

	/**
	 * @param vendorCatalogID
	 * @return List of VendorCatalogStyleSku
	 * @TODO Method to fetch records form VENDOR_CATALOG_STY_SKU Used while
	 *       filling VNDR_CATL_STY_SKU_MAST table
	 */
	public List<VendorCatalogStyleSku> getStyelSkusWithCatalogId(
			Long vendorCatalogID);

	/**
	 * @param VendorCatalogStyleSkuMaster List
	 * @TODO Method to save MASTER records from VNDR_CATL_STY_SKU_MAST table
	 *       Used while filling VNDR_CATL_STY_SKU_MAST table .
	 */
	public void saveOrUpdateMasterList(
			List<VendorCatalogStyleSkuMaster> tempMasterList);

	/**
	 * @return List<VendorCatalogStyleSkuMaster>
	 * @TODO Method to fetch MASTER records from VNDR_CATL_STY_SKU_MAST table
	 *       Used while filling VNDR_CATL_STY_SKU_MAST table using overwrite
	 *       action
	 */
	public List<VendorCatalogStyleSkuMaster> getAllMasterRecords(long vendorId);

	/**
	 * @param styleSkuImageModel
	 * @TODO Method to SAVE images from VNDR_CATL_STY_SKU_IMAGE table Used while
	 *       filling VNDR_CATL_STY_SKU_IMAGE table
	 */
	public void saveImageForStyleSku(
			VendorCatalogStyleSkuImage styleSkuImageModel);

	/**
	 * @param master
	 * @return List<VendorCatalogStyleSkuImage>
	 * @TODO Method to fetch images from VNDR_CATL_STY_SKU_IMAGE table for given
	 *       catalog, style and sku
	 */
	public List<VendorCatalogStyleSkuImage> getImagesForStyleSku(
			VendorCatalogStyleSkuMaster master);

	/**
	 * @param imagesExisting
	 * @TODO Method to disable existing images from VNDR_CATL_STY_SKU_IMAGE
	 *       table Used while filling VNDR_CATL_STY_SKU_IMAGE table
	 */
	public void deleteImages(List<VendorCatalogStyleSkuImage> imagesExisting);

	
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

	/* Method to get Vendor Catalog Info for Catlog Id and Catalog Name */
	public List<VendorCatalogImport> getVendorCatalogImportInfo(Long catalogId);

	public void saveImageData(List<VendorCatalogImage> vendorCatalogImageList);

	//public List<Image> getAllActiveCarImages(Long carId);

	/**
	 * @return Map<Car, Set<VendorSku>>
	 * @TODO Method to get skus for CAR with in-progress status Used while
	 *       saving car images from catalog
	 */
	public Map<Car, Set<VendorSku>> getCarAndSkus();

	/**
	 * @return List<ImageType>
	 * @TODO Get all image types
	 */
	public List<ImageType> getImageTypes();

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
	public List<VendorCatalogStyleSkuImage> getAllImagesforMaster();

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
	
	public VendorSku checkUpcExists(String upc) throws Exception;
	
	public Classification getClassificationFromNumber(Short classNumber) throws Exception;
	
	public boolean checkVendorStyleExists(String vendorStyleNumber) throws Exception;
	
	public void saveEntityObject(Object entity) throws Exception;
	
	public Department getDepartmentFromDeptCode(String deptCd) throws Exception;
	
	public VendorStyle getVendorStyleFromNumber(String styleNumber) throws Exception;
	
	public User getUserBuyer() throws Exception;
	
	public void saveList(List<Object> list) throws Exception;

	/**
	 * @param catalog
	 * @return List of records imported.
	 * @TODO To get the number of records imported.
	 */
	public  List<BigDecimal> getNumberOfRecordsImported();

	/**
	 * @param catalog
	 * @return List of records imported.
	 * @TODO To get the number of records imported in style sku table.
	 */
	public  List<BigDecimal> getNumberOfRecordsImportedInStyleSku();

	/**
	 * @param carSkuId
	 * @param attrId
	 * @return void
	 * @Enclosing_Method  getCarAttribute
	 * @TODO
	 */
	public CarAttribute getCarAttribute(Long carId, Long attrId) ;

	/**
	 * @param vendorCatalogID - Latest catalog ID for vensor style
	 * @param vendorStyleId-Vendor Style ID
	 * @return void
	 * @Enclosing_Method  getRecordsForLatestStyleUPC
	 * @TODO Get Latest record from catalog with latest UPC 
	 */
	public List<VendorCatalogRecord> getRecordsForLatestStyleUPC(Long vendorCatalogID, String vendorStyleNumber);

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
	 */
	public List<VendorCatalogHeader> getUnmappedAttribute(Long vendorCatalogID, long templateId);
	
	/**
	 * @param vendorStyleNumber
	 * @param vendor
	 * @return VendorStyle
	 * @TODO To return vendor style based on vendor style number
	 */
	public VendorStyle getVendorStyleByNumber(String vendorStyleNumber, Vendor vendor);
        public List<Car> getPatternCars();
         public Car getParentStyleCar(long parentStyleId);

		/**
		 * @param vendorStyleForComparison
		 * @return void
		 */
		public void saveStyle(VendorStyle vendorStyleForComparison);
		
		// added for the Dropship Phase2 
		/*
		 *  Method to get the Attribute by attribute name.
		 */
		public Attribute getAttributeByName(String getAttributeByName);
		
		/*
		 * Method to get the CARS by vendor style id
		 * 
		 */
		public List<Car> getCarsByVendorStyle(String vendorStyle);
		
		/*
		 *  Method to get the latest catalog details
		 */
        public List<VendorCatalogStyleSkuMaster> getLatestCatalogStyles();
        
        /*
         * Method to get the fulfillment service vendors
         */
        public List<FulfillmentServiceVendor> getVendorExpditedShippingValues();
        
        public  List<FulfillmentServiceVendor> getLatestVendorExpditedShippingValues(String strDateLastRun);
}
