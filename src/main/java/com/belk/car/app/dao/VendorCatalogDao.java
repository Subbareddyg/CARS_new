
package com.belk.car.app.dao;

import java.io.Serializable;
import java.util.List; 
import java.util.Map; 
 
import com.belk.car.app.dto.CatalogHeaderDTO;
import com.belk.car.app.dto.CatalogMappedFieldDTO;
import com.belk.car.app.dto.CatalogVendorDTO;
import com.belk.car.app.dto.FieldMappingDataDTO;
import com.belk.car.app.dto.NameValue;
import com.belk.car.app.dto.VendorCatalogDTO;
import com.belk.car.app.dto.VendorCatalogStyleDTO;
import com.belk.car.app.dto.VendorStyleImageDTO;
import com.belk.car.app.dto.VendorStyleInfo;
import com.belk.car.app.dto.VendorStylePropertiesDTO;
import com.belk.car.app.dto.VendorUpcDTO;
import com.belk.car.app.model.AttributeLookupValue;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.vendorcatalog.CatalogGroupTemplate;
import com.belk.car.app.model.vendorcatalog.CatalogMasterAttribute;
import com.belk.car.app.model.vendorcatalog.CompositeKeyForDataFldMapping;
import com.belk.car.app.model.vendorcatalog.MasterAttributeMapping;
import com.belk.car.app.model.vendorcatalog.VendorCatalog;
import com.belk.car.app.model.vendorcatalog.VendorCatalogDepartment;
import com.belk.car.app.model.vendorcatalog.VendorCatalogFieldDataMapping;
import com.belk.car.app.model.vendorcatalog.VendorCatalogFieldMapping;
import com.belk.car.app.model.vendorcatalog.VendorCatalogFileFormat;
import com.belk.car.app.model.vendorcatalog.VendorCatalogImage;
import com.belk.car.app.model.vendorcatalog.VendorCatalogImageLocation;
import com.belk.car.app.model.vendorcatalog.VendorCatalogImport;
import com.belk.car.app.model.vendorcatalog.VendorCatalogNote;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSkuImage;
import com.belk.car.app.model.vendorcatalog.VendorCatalogTemplate;
import com.belk.car.app.model.vendorcatalog.VendorCatalogUpdateAction;
import org.appfuse.model.User;

public interface VendorCatalogDao extends CachedQueryDao {

	/**
	 * Retrieve a VendorCatalog, based on the given catalog ID
	 * 
	 * @return VendorCatalog
	 */
	VendorCatalog getVendorCatalog(Long vendorCatalogID);

	List<VendorCatalogImport> getPreviousCatalogsList(Long vendorId);
	
	/**
	 * Retrieve a list of VendorCatalog objects for the given vendor and style
	 * number
	 * 
	 * @param vendorStyle
	 * @return list of VendorCatalog objects
	 */
	//List<VendorCatalog> getVendorCatalogs(VendorStyle vendorStyle);

	List<Vendor> getAllVendors();

	/** Vendor Catalog Form Start */
	Vendor getVendor(String vendorNumber);

	VendorCatalogImport getVendorCatalogImpDetails(long vendorCatalogID);

	List getVendorCatalogDeptDetails(VendorCatalog vendorCatalog);

	VendorCatalogFileFormat getFileFormat(String fileFormatName);

	VendorCatalogUpdateAction getUpdateAction(String updateAction);

	VendorCatalogImageLocation getImageLocation(String imageLocation);

	/** This method gets the catalog id from sequence */
	long getCatalogIdFromSeq();

	long getCurrCatalogId(); // gets CURRVAL from dump

	List<VendorCatalog> getCatalogNameForVendor(String vendorNumber, String catalogName);

	VendorCatalog save(VendorCatalog vendorCatalog);

	void save(VendorCatalogImport vendorCatalogImport);

	VendorCatalogDepartment getVendorCatalogDepartment(Long deptId, VendorCatalog vendorCatalog);

	void saveVndrCatlDept(List<VendorCatalogDepartment> vndrCatlDeptList);

	void saveVndrCatlImg(List<VendorCatalogImage> vendorCatalogImageList);

	VendorCatalogTemplate getVendorCatalogTemplate(Long vendorCatalogTemplateId);

	List<VendorCatalogImageLocation> getVendorCatalogImageLocations();

	List<VendorCatalogUpdateAction> getVendorCatalogUpdateActions();

	List<VendorCatalogFileFormat> getVendorCatalogFileFormats();

	List<VendorCatalogImage> getVendorCatalogImageList(long vendorCatalogID);

	/** Vendor Catalog Form End */

	/** Vendor Catalog Note Start */
	List<VendorCatalogNote> getVendorCatalogNotesList(Long vendorCatalogId);

	VendorCatalogNote getVendorCatalogNoteByID(Long id);

	List<VendorCatalogNote> searchNotes(String noteSubject, long vendorCatalogId);

	VendorCatalogNote addNote(VendorCatalogNote vendorCatalogNote);
        /** Get the List of the Vendors who has a catalog setup in CARS
         * getCatalogVendors
         * @param searchCriteria
         * @return
         */
	List<CatalogVendorDTO> getCatalogVendors(CatalogVendorDTO searchCriteria);

	CatalogVendorDTO getVednorInfo(String vednorId);

	List<VendorCatalogDTO> searchVendorCatalogs(CatalogVendorDTO searchCriteria);
        /**
         * Get The List of All Open Catalogs.
         * @return List<VendorCatalog>
         */
	List<VendorCatalog> getOpenCatalogs();
        /**
         * Get The List of  Open Catalogs using Search Criteria.
         * @return List<VendorCatalog>
         */
	List<VendorCatalog> searchOpenCatalogs(CatalogVendorDTO searchCriteria);
        /**
         * Lock/Unlock a Vendor Catalog.
         * @param catalogVendorDTO CatalogVendorDTO
         * @param vendorCatalogId String
         * @param user User
         * @param mode String
         * @return List<VendorCatalogDTO>
         */
	/*List<VendorCatalogDTO> lockUnlockCatalog(
			CatalogVendorDTO catalogVendorDTO, String vendorCatalogId, User user, String mode);*/
        /**
         * Lock/Unlock a Open Catalog.
         * @param catalogVendorDTO CatalogVendorDTO
         * @param vendorCatalogId String
         * @param user User
         * @param mode String
         * @return List<VendorCatalog>
         */
	/*List<VendorCatalog> lockUnlockOpenCatalog(
			CatalogVendorDTO catalogVendorDTO, String vendorCatalogId, User user, String mode);*/

	// Vendor Catalog Note End
	/** Vendor Catalog Note End */

	VendorCatalog getCatalogInfo(long catalogId);
	/**
	 * This Method returns the list of BM attribute.
	 * @param String
	 * @param Long
	 * @return List
	 * */
	List<String> getBlueMartiniAttribute(String productGroupIds, Long catalogID);
        /**
         *Get all the Vendor Catalog Styles.
         * @param catalogStyleDTO VendorCatalogStyleDTO
         * @return List<VendorCatalogStyleDTO>
         */
	List<VendorCatalogStyleDTO> getVendorStyles(VendorCatalogStyleDTO catalogStyleDTO);

	List<VendorCatalogStyleDTO> getVendorCatalogStyles(VendorCatalogStyleDTO styleDTO);

	List<CatalogMasterAttribute> getCatalogMasterAttr(String filter);

	VendorCatalogImport getVendorCatalogImport(Long vendorCatalogID);
        /**
         * 
         * @param styleDTO VendorCatalogStyleDTO
         * @param vendorStyleId String
         * @param vendorCatalogId String
         * @param userId String
         * @param mode String
         * @return
         */
	/*List<VendorCatalogStyleDTO> lockUnlockVendorStyles(
			VendorCatalogStyleDTO styleDTO, String vendorStyleId, String vendorCatalogId,
			String userId, String mode);*/

	int getCountForPreviousUploads(Map<String, Object> param);
        /**
         * Get The master Attribute Mapping for a catalog.
         * @param catalogTemaplateId
         * @param vendorCatalogId
         * @return
         */
	List<NameValue> getMasterAttributeMapping(String catalogTemaplateId, String vendorCatalogId);
        /**
         * Get a record from a Vendor Catalog.
         * @param vendorCatalogId
         * @param recordNumber
         * @return
         */
	List<String> getCatalogRecord(String vendorCatalogId, String recordNumber);
        /**
         * Get
         * @param catalogTemaplateId
         * @return
         */
	List<NameValue> getCatalogAttributeMapping(String catalogTemaplateId);
        /**
         * Get Images associated with 
         * @param vendorCatalogId
         * @param vendorStyleId
         * @param vendorUPC
         * @return
         */
	List<VendorStyleImageDTO> getStyleSKUImages(
			String vendorCatalogId, String vendorStyleId, String vendorUPC);
        /**
         * Get The List of UPC for a particular Style.
         * @param vendorId String
         * @param vendorStyleId String
         * @return List<VendorUpcDTO>
         */
	List<VendorUpcDTO> getUPCList(String vendorId, String vendorStyleId);
        /**
         * Save Vendor Catalog Style Sku Image
         * @param vendorCatalogStyleSkuImage
         * @return
         */
	VendorCatalogStyleSkuImage saveVendorCatalogStyleSkuImageObject(
			VendorCatalogStyleSkuImage vendorCatalogStyleSkuImage);

	VendorCatalogTemplate saveTemplate(VendorCatalogTemplate vendorCatalogTemplate);

	List<VendorCatalogFieldMapping> saveVendorCatalogFieldMapping(
			List<VendorCatalogFieldMapping> lstVendorCatalogFieldMapping);

	CatalogGroupTemplate saveGroupTemplate(CatalogGroupTemplate catalogGroupTemplate);

	List<MasterAttributeMapping> saveMasterMapping(
			List<MasterAttributeMapping> lstMasterAttributeMapping, Long templateId);
        /**
         * Get the Header Number of the coulms having vales of Vendor UPC and Color.
         * @param catalogTemplateId String
         * @return
         */
	List<NameValue> getUPCHeaderNumber(String catalogTemplateId);
        /**
         *  Save the Vendor Style properties.
         * @param stylePropertiesDTO VendorStylePropertiesDTO
         * @param user User
         * @return boolean
         */
	boolean saveStyleProperties(VendorStylePropertiesDTO stylePropertiesDTO, User user);
         /**
         * Remove a Image associated with a Vendor Catalog Style/ SKU
         * @param imageId String
         * @param user User
         * @return boolean
         */
	boolean removeImage(String imageId, User user);

	// List<NameValue> getUPCHeaderNumber(String catalogTemplateId);
	List<CatalogMasterAttribute> getTemplateCatalogMasterAttr(Long vendorCatalogTemplateId, String filter);

	List<String> getTemplateBlueMartiniAttribute(String productGroupIds, Long vendorCatalogID);

	List<MasterAttributeMapping> getTemplateMasterAttributeMapping(Long vendorCatalogID);
	/**
	 * Gets the Vendor Catalog header values that have not yet been mapped.
	 * @param vendorCatalogID
	 * @param vendorCatalogTemplateID.
	 * @return List<CatalogHeaderDTO>    
	 */
	List<CatalogHeaderDTO> getTemplateVendorCatalogHeaderList(Long vendorCatalogID, Long vendorCatalogTemplateI);
	/**
	 * This Method returns the Mapped values from the DB.
	 * @param vendorCatalogID Long.
	 * @param vendorCatalogTemplateID Long.
	 * @return List.
	 * */
	List<CatalogMappedFieldDTO> getTemplateMappedValueList(Long vendorCatalogID, Long vendorCatalogTemplateID);

	List<String> getCarFldMappingDatavalue(String blueMartiniAttribute, String[] selectedProductGroups, Long catalogId);

	List<String> getVendorFldMappingDataValue(Long vendorCataloID, Long catalogHeaderFldNum);

	List<String> getCarFldMappingDatavalue(String blueMartiniAttribute);

	List<AttributeLookupValue> getAttributeLookUpValues(String attributeValue);

	VendorCatalogFieldDataMapping saveDataFieldMapping(
			VendorCatalogFieldDataMapping vendorCatalogFldDataMapping);

	boolean saveVendorProperties(String vendorId, String display);

	List<FieldMappingDataDTO> getSavedFieldMappingData(
			Long vendorCatalogID, String vendorFieldHeader);

	List<VendorCatalogTemplate> getCatlogTemplateForVendor(Long vendorID);

	List<VendorCatalogTemplate> getGlobalTemplates();

	String getVendorCatalogTemplateId(String vendorCatalogId);

	List<Vendor> getOtherVendors(Long vendorID);

	VendorStyleInfo getStyleInfo(String vendorStyleId, String vendorCatalogId, String vendorUpc);

	int getCountOfTemplateName(String templateName);
	/**
	 * This Method return the object depending upon the catalogTemplate ID and
	 * Master ID.
	 * @param Long
	 * @param Long
	 * @return List.
	 * */
	public List<MasterAttributeMapping> getMasterMappingAttribute(
			Long catalogTemplateID, String fieldName);
	/**
	 *
	 * This method gets the vendor catalog template  from VendorCatalogTemplate table
	 * @param  String vendorCatalogTmplName
	 * @return VendorCatalogTemplate
	 */
	public VendorCatalogTemplate getVendorCatalogTemplate(String vendorCatalogTmplName);
	/**
	 * This method removes the mapping records from the database.
	 * @param catalogTemplate VendorCatalogTemplate.
	 * */
	public void removeMappingAttribute(VendorCatalogTemplate catalogTemplate) ;
	
	public Object getById(Class cls, Serializable id);
	
	
	public VendorCatalogFieldDataMapping getMappingObjectById(CompositeKeyForDataFldMapping key);

    // public List<String> getSpreadsheetNamesForVendor(String vendorNumber);
	
	// added methods for dropship phase 2
	
	/*
	 * lock catalog for the user
	 */
	public void lockCatalog(String vendorCatalogId,User user);
	
	/*
	 * unlock catalog 
	 */
	
	public void unlockCatalogs(String userName);
	
	/*
	 * remove vendor catalog which is in import status
	 */
	
	public void removeVendorCatalog(String vendorCatalogId);
}
