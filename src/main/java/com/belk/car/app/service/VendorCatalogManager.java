/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package com.belk.car.app.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.appfuse.model.User;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.belk.car.app.dto.CatalogHeaderDTO;
import com.belk.car.app.dto.CatalogMappedFieldDTO;
import com.belk.car.app.dto.CatalogVendorDTO;
import com.belk.car.app.dto.FieldMappingDataDTO;
import com.belk.car.app.dto.NameValue;
import com.belk.car.app.dto.VendorCatalogDTO;
import com.belk.car.app.dto.VendorCatalogStyleDTO;
import com.belk.car.app.dto.VendorStyleImageDTO;
import com.belk.car.app.dto.VendorStylePropertiesDTO;
import com.belk.car.app.model.AttributeLookupValue;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.vendorcatalog.CatalogGroupTemplate;
import com.belk.car.app.model.vendorcatalog.CatalogMasterAttribute;
import com.belk.car.app.model.vendorcatalog.CompositeKeyForDataFldMapping;
import com.belk.car.app.model.vendorcatalog.MasterAttributeMapping;
import com.belk.car.app.model.vendorcatalog.VendorCatalog;
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
import com.belk.car.app.webapp.forms.VendorCatalogForm;

/**
 * @author AFUSY85
 */
public interface VendorCatalogManager {
        /**
         * Get Catalog Vendors List who has catalogs.
         * @return List<CatalogVendorDTO>
         */
	List<CatalogVendorDTO> getAllVendors();

	List<CatalogVendorDTO> getAllVendors(String vendorNo);
	List<CatalogVendorDTO> getAllVendors(int isDisplayable);
	// Vendor Catalog Form Start
	VendorCatalog saveVendorCatalog(
			VendorCatalogForm vendorCatalogForm, boolean isNewCatalog,
			List<Department> removedDeptList, List<Department> addedDeptList)
			throws Exception;

	VendorCatalogTemplate getVendorCatalogTemplate(Long vendorCatalogTemplateId);

	List<VendorCatalogImageLocation> getVendorCatalogImageLocations()
			throws Exception;

	List<VendorCatalogUpdateAction> getVendorCatalogUpdateActions()
			throws Exception;

	List<VendorCatalogFileFormat> getVendorCatalogFileFormats()
			throws Exception;

	boolean getCatalogNameForVendor(VendorCatalogForm vendorCatalogForm)
			throws Exception;

	Vendor getVendorForCatalog(String vendorNumber);

	boolean methodInvokingImportFTPImages(
			String destinationDirectory, Properties properties,
			VendorCatalogForm vendorCatalogForm)
			throws IOException;

	void saveImage(List imageList, Long vendorCatalogId)
			throws Exception;

	VendorCatalog getVendorCatalog(Long vendorCatalogID);

	List<VendorCatalogImport> getPreviousCatalogsList(Long vendorId);
	
	VendorCatalogImport getVendorCatalogImportDetails(long vendorCatalogID)
			throws Exception;

	public List getVendorCatalogDeptDetails(VendorCatalog vendorCatalog)
			throws Exception;

	long getCatalogIdFromSeq();

	List<VendorCatalogImage> getVendorCatalogImageList(long vendorCatalogID)
			throws Exception;

	// Vendor Catalog Form End
	// Vendor Catalog Note Start
	List<VendorCatalogNote> getVendorCatalogNotes(Long vendorCatalogId);

	VendorCatalogNote getVendorCatalogNoteByID(long vendorCatalogNoteId);

	List<VendorCatalogNote> searchNotes(String noteSubject, long vendorCatalogId);

	VendorCatalogNote addNote(VendorCatalogNote vendorCatalogNote);

	// Vendor Catalog Note End
	//List<VendorCatalog> getVendorCatalogs(VendorStyle vendorStyle);

	// need to add throws clause

	// Data mapping Screen.
	List<VendorCatalog> getVendorCatalogHeader(String catalogID);

	List<CatalogVendorDTO> searchCatalogVendors(
			CatalogVendorDTO catalogVendorDTO);

	String createNewImageDirectory(
			Properties properties, String venNum, Long catalogSeqId)
			throws IOException;
        /**
         * Get Vendor Info to display in Header section of teh screens.
         * @param vendorId
         * @return CatalogVendorDTO
         */
	CatalogVendorDTO getVendorInfo(String vendorId);

        /**
         * Search Vendor Catlogs according to Search Criteria.
         * @param catalogVendorDTO
         * @return List<VendorCatalogDTO>
         */
	List<VendorCatalogDTO> searchVendorCatalogs(CatalogVendorDTO catalogVendorDTO);
        /**
         * Get The List of All Open Catalogs.
         * @return List<VendorCatalog>
         */

	List<VendorCatalog> getOpenCatalogs();
        /**
         * Get The List of  Open Catalogs using search criteria.
         * @return List<VendorCatalog>
         */
	List<VendorCatalog> searchOpenCatalogs(CatalogVendorDTO catalogVendorDTO);
        /**
         * Lock/Unlock a Vendor Catalog.
         * @param catalogVendorDTO CatalogVendorDTO
         * @param vendorCatalogId String
         * @param user User
         * @param mode String
         * @return List<VendorCatalogDTO>
         */
	/*List<VendorCatalogDTO> lockUnlockCatalog(
			CatalogVendorDTO catalogVendorDTO, String vendorCatalogId,
			User user, String mode);*/
        /**
         * Lock/Unlock a Open Catalog.
         * @param catalogVendorDTO CatalogVendorDTO
         * @param vendorCatalogId String
         * @param user User
         * @param mode String
         * @return List<VendorCatalog>
         */
	/*List<VendorCatalog> lockUnlockOpenCatalog(
			CatalogVendorDTO catalogVendorDTO, String vendorCatalogId,
			User user, String mode);*/
	/**
	 * This Method returns the catalog information.
	 * @param long
	 * @return VendorCatalog
	 * */
	VendorCatalog getCatalogInfo(long catalogId);
	/**
	 * This Method returns the list of BM attributes.
	 * @param String
	 * @param Long
	 * @returns List
	 * */
	List<String> getBlueMartiniAttribute(String productGroupIds, Long catalogID);
        /**
         *Get all the Vendor Catalog Styles.
         * @param catalogStyleDTO VendorCatalogStyleDTO
         * @return List<VendorCatalogStyleDTO>
         */
	List<VendorCatalogStyleDTO> getVendorStyles(
			VendorCatalogStyleDTO catalogStyleDTO);
	/**
	 * This Method returns the list of Catalog Master Attribute
	 * @return List. 
	 * */
	List<CatalogMasterAttribute> getCatalogMasterAttr(String filter);
        /**
         *
         * @param catalogStyleDTO VendorCatalogStyleDTO
         * @param vendorStyleId String
         * @param vendorCatalogId String
         * @param user User
         * @param mode String
         * @return List<VendorCatalogStyleDTO>
         */
	/*List<VendorCatalogStyleDTO> lockUnlockVendorStyles(
			VendorCatalogStyleDTO catalogStyleDTO, String vendorStyleId,
			String vendorCatalogId, User user, String mode);*/

	int getCountForPreviousUploads(Map<String, Object> param);

	boolean uploadCatalogStyleSkuImage(Object file,String uploadDir)
			throws Exception;

	VendorCatalogTemplate saveTemplate(
			VendorCatalogTemplate vendorCatalogTemplate);

	VendorCatalog save(VendorCatalog vendorCatalog);

	VendorStylePropertiesDTO getVendorStyleProperties(
			String vendorStyleId, String vendorCatalogId, String recordNumber,
			String catalogTemplateId, String vendorId, String vendorUpc);
	
	Object getById(Class cls, Serializable id);
	List<VendorStyleImageDTO> getStyleSKUImages(
			String vendorCatalogId, String vendorStyleId, String vendorUPC);

	VendorCatalogStyleSkuImage saveVendorCatalogStyleSkuImageObject(
			VendorCatalogStyleSkuImage vendorCatalogStyleSkuImage);

	List<VendorCatalogFieldMapping> saveVendorCatalogFieldMapping(
			List<VendorCatalogFieldMapping> lstVendorCatalogFieldMapping);

	CatalogGroupTemplate saveGroupTemplate(
			CatalogGroupTemplate catalogGroupTemplate);

	List<MasterAttributeMapping> saveMasterMapping(
			List<MasterAttributeMapping> lstMasterAttributeMapping, Long templateId);

	List<NameValue> getUPCHeaderNumber(String catalogTemplateId);

	boolean saveVendorStyleProperties(
			VendorStylePropertiesDTO stylePropertiesDTO, User user);

	boolean removeImage(String imageId, User user);

	List<CatalogMasterAttribute> getTemplateCatalogMasterAttr(
			Long vendorCatalogTemplateId, String filter);

	List<String> getTemplateBlueMartiniAttribute(
			String productGroupIds, Long vendorCatalogID);

	List<MasterAttributeMapping> getTemplateMasterAttributeMapping(
			Long vendorCatalogID);
	/**
	 * Gets the Vendor Catalog header values that have not yet been mapped.
	 * @param vendorCatalogID
	 * @param vendorCatalogTemplateID.
	 * @return List<CatalogHeaderDTO>
	 */
	List<CatalogHeaderDTO> getTemplateVendorCatalogHeaderList(
			Long vendorCatalogID, Long vendorCatalogTemplateID);
	/**
	 * This method returns the Mapped Values from the DB.
	 * @param vendorCatalogID Long.
	 * @param vendorCatalogTemplateID Long.
	 * @return List.
	 * */
	List<CatalogMappedFieldDTO> getTemplateMappedValueList(Long vendorCatalogID, Long vendorCatalogTemplateID);

	List<String> getCarFldMappingDatavalue(
			String blueMartiniAttribute, String[] selectedProductGroups, Long catalogId);

	List<String> getVendorFldMappingDataValue(
			Long vendorCataloID, Long catalogHeaderFldNum);

	List<String> getCarFldMappingDatavalue(String blueMartiniAttribute);

	List<AttributeLookupValue> getAttributeLookUpValues(String attributeValue);

	VendorCatalogFieldDataMapping saveDataFieldMapping(
			VendorCatalogFieldDataMapping vendorCatalogFldDataMapping);

	boolean saveVendorProperties(String vendorId, String display);

	List<FieldMappingDataDTO> getSavedFieldMappingData(
			Long vendorCatalogID, String vendorFieldHeader);

	List<VendorCatalogTemplate> getCatlogTemplateForVendor(Long vendorID);

	List<VendorCatalogTemplate> getGlobalTemplates();

	List<VendorCatalogStyleDTO> getVendorCatalogStyles(
			VendorCatalogStyleDTO catalogStyleDTO);

	String getVendorCatalogTemplateId(String vendorCatalogId);

	List<Vendor> getOtherVendors(Long vendorID);

	int getCountOfTemplateName(String templateName);
	/**
	 * @param string 
	 * @param Properties
	 * @param CommonsMultipartFile
	 * @return boolean.
	 */
	boolean upLoadFile(Properties properties, CommonsMultipartFile file, String vendorNumber, String catalogId) throws IOException;
	/**
	 * This Method returns the master attribute mapping.
	 * @param Long
	 * @param Long
	 * @return List
	 * */
	public List<MasterAttributeMapping> getMasterMappingAttribute(
			Long catalogTemplateID, String fieldName);
	/**
	 * This method gets the vendor catalog template  from
	 * VendorCatalogTemplate table
	 * 
	 * @param String
	 *            vendorCatalogTmplName
	 * @return VendorCatalogTemplate
	 */
	public VendorCatalogTemplate getVendorCatalogTemplate(String vendorCatalogTemplateName);
	/**
	 * This method removes the mapping records from the database.
	 * @param catalogTemplate VendorCatalogTemplate.
	 * */
	public void removeMappingAttribute(VendorCatalogTemplate catalogTemplate) ;

         public void addImageToStyleSku(String vendorStyleId,String vendorCatalogId,String vendorUpc,char imageType,String fileName,String vendorNumber,String user);
         
    public VendorCatalogFieldDataMapping getMappingObjectById(CompositeKeyForDataFldMapping key);
    
    public void methodToMovePreviousCatalogImages(String vcID,String vendorNumber, VendorCatalog oldVendorCatalog);

     //public boolean getSpreadsheetNamesForVendor(String vendorNumber,String spreadsheetName);

    // added methods for dropship phase 2
    public void lockCatalog(String vendorCatalogId,User user);
    public void unlockCatalogs(String userName);
	public void removeVendorCatalog(String vendorCatalogId);    
   
}