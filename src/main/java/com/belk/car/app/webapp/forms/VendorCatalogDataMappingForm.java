package com.belk.car.app.webapp.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.list.LazyList;

import com.belk.car.app.dto.CarAttributesDTO;
import com.belk.car.app.dto.CatalogHeaderDTO;
import com.belk.car.app.dto.CatalogMappedFieldDTO;
import com.belk.car.app.dto.FieldMappingDataDTO;
import com.belk.car.app.model.ProductGroup;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.vendorcatalog.CatalogMasterAttribute;
import com.belk.car.app.model.vendorcatalog.MasterAttributeMapping;
import com.belk.car.app.model.vendorcatalog.VendorCatalog;
import com.belk.car.app.model.vendorcatalog.VendorCatalogFieldMapping;
import com.belk.car.app.model.vendorcatalog.VendorCatalogTemplate;

/**
 * This class contains form fields mapped with vendor catalog data mapping form
 * 
 * @author afusxs7 : Subbu
 */
public class VendorCatalogDataMappingForm {

	private VendorCatalog vendorCatalog;
	private List<VendorCatalogTemplate> existingTemplateForVendor = new ArrayList<VendorCatalogTemplate>(
			0);
	private List<VendorCatalogTemplate> globalTemplate = new ArrayList<VendorCatalogTemplate>(0);
	private List<VendorCatalogTemplate> anotherVendorTemplate = new ArrayList<VendorCatalogTemplate>(
			0);
	private List<Vendor> vendors = new ArrayList<Vendor>(0);
	private List<VendorCatalogTemplate> templatesForOtherVendor = new ArrayList<VendorCatalogTemplate>(0);

	private String sExistTemplateForVendor;
	private String sGlobalTemplate;
	private String sAnotherVendorTemplate;
	private String selectedVendor;
	private String templateFrmAnotherVendor;

	private List<ProductGroup> productGroupList = new ArrayList<ProductGroup>(0);
	private String[] productGroupSelected;
	private VendorCatalogTemplate vendorCatalogTemplate;
	private Long catalogId;
	private Long vendorCatalogTmplID;
	private Long selectedTemplateID;
	private String vendorCatalogTemplateName;

	// List for the field Mapping rules
	private List<String> blueMartiniAttributes = new ArrayList<String>(0);

	private List<CatalogHeaderDTO> vendorCatalogHeaders = new ArrayList<CatalogHeaderDTO>(0);
	private List<CatalogMasterAttribute> catalogMasterAttribute = new ArrayList<CatalogMasterAttribute>(
			0);
	// Catalog Mapped DTO fields
	private List<CatalogMappedFieldDTO> catalogFieldMappingList = new ArrayList<CatalogMappedFieldDTO>(
			0);

	// Map for the setting the field Mapping rules in session
	private Map<Long, CatalogHeaderDTO> sessionVendorCatalogHeader = new HashMap<Long, CatalogHeaderDTO>(
			0);
	private Map<Long, CatalogMasterAttribute> sessionCatalogMasterAttribute = new HashMap<Long, CatalogMasterAttribute>(
			0);
	private Map<Integer, String> sesssionBlueMartiniAttributes = new HashMap<Integer, String>(0);
	
	private List<CarAttributesDTO> sessionCarAttributes = new ArrayList<CarAttributesDTO>();
	// This is form values for mapping the data mapping field
	private int mapRowNumber;
	private Long mapVendorSuppliedFieldID;
	private String mapVendorSuppliedField;
	private boolean mapIsMasterRecord;
	private Long mapMasterMappingId;
	private String mapMasterAttribute;
	private String mapBlueMartiniAttribute;
	private Long mapBlueMartiniKey;
	private String mapVenHeaderData;

	private boolean glbTemplate;
	private String errorString;

	// Objects for saving the value for saving the values in database
	private List<VendorCatalogFieldMapping> lstVendorCatalogFieldMapping = new ArrayList<VendorCatalogFieldMapping>(
			0);
	private List<MasterAttributeMapping> lstMasterAttributeMapping = new ArrayList<MasterAttributeMapping>(
			0);

	// Gets the productgroup values already selected
	private String selectedProductGroups;

	// Setting the vendor values for fld mapping in the form
	private Map<String, FieldMappingDataDTO> sessionVendorFldDataMapping = new HashMap<String, FieldMappingDataDTO>(
			0);

	// Setting the car values for fld mapping in the form
	private List<String> sessionCarFldDataMapping = new ArrayList<String>(0);

	// Setting the mapped list back in the form for displaying and setting it in
	// the session
	private List<FieldMappingDataDTO> fldMappingDataDTOs = new ArrayList<FieldMappingDataDTO>(0);

	// Holds the vendor value for vendor field mapping
	private String mapVenFldDataMapping;

	// Holds the cars value for field mapping
	private List<String> mapCarFldDataMapping = LazyList.decorate(new ArrayList(), FactoryUtils.instantiateFactory(String.class));

	public Long getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(Long catalogId) {
		this.catalogId = catalogId;
	}

	public VendorCatalog getVendorCatalog() {
		return vendorCatalog;
	}

	public void setVendorCatalog(VendorCatalog vendorCatalog) {
		this.vendorCatalog = vendorCatalog;
	}

	public List<VendorCatalogTemplate> getExistingTemplateForVendor() {
		return existingTemplateForVendor;
	}

	public void setExistingTemplateForVendor(List<VendorCatalogTemplate> existingTemplateForVendor) {
		this.existingTemplateForVendor = existingTemplateForVendor;
	}

	public List<VendorCatalogTemplate> getGlobalTemplate() {
		return globalTemplate;
	}

	public void setGlobalTemplate(List<VendorCatalogTemplate> globalTemplate) {
		this.globalTemplate = globalTemplate;
	}

	public List<VendorCatalogTemplate> getAnotherVendorTemplate() {
		return anotherVendorTemplate;
	}

	public void setAnotherVendorTemplate(List<VendorCatalogTemplate> anotherVendorTemplate) {
		this.anotherVendorTemplate = anotherVendorTemplate;
	}

	public String getsExistTemplateForVendor() {
		return sExistTemplateForVendor;
	}

	public void setsExistTemplateForVendor(String sExistTemplateForVendor) {
		this.sExistTemplateForVendor = sExistTemplateForVendor;
	}

	public String getsGlobalTemplate() {
		return sGlobalTemplate;
	}

	public void setsGlobalTemplate(String sGlobalTemplate) {
		this.sGlobalTemplate = sGlobalTemplate;
	}

	public String getsAnotherVendorTemplate() {
		return sAnotherVendorTemplate;
	}

	public void setsAnotherVendorTemplate(String sAnotherVendorTemplate) {
		this.sAnotherVendorTemplate = sAnotherVendorTemplate;
	}

	public List<ProductGroup> getProductGroupList() {
		return productGroupList;
	}

	public void setProductGroupList(List<ProductGroup> productGroupList) {
		this.productGroupList = productGroupList;
	}

	public String[] getProductGroupSelected() {
		return productGroupSelected;
	}

	public void setProductGroupSelected(String[] productGroupSelected) {
		this.productGroupSelected = productGroupSelected;
	}

	public List<String> getBlueMartiniAttributes() {
		return blueMartiniAttributes;
	}

	public void setBlueMartiniAttributes(List<String> blueMartiniAttributes) {
		this.blueMartiniAttributes = blueMartiniAttributes;
	}

	public VendorCatalogTemplate getVendorCatalogTemplate() {
		return vendorCatalogTemplate;
	}

	public void setVendorCatalogTemplate(VendorCatalogTemplate vendorCatalogTemplate) {
		this.vendorCatalogTemplate = vendorCatalogTemplate;
	}

	public List<CatalogMasterAttribute> getCatalogMasterAttribute() {
		return catalogMasterAttribute;
	}

	public void setCatalogMasterAttribute(List<CatalogMasterAttribute> catalogMasterAttribute) {
		this.catalogMasterAttribute = catalogMasterAttribute;
	}

	public Long getVendorCatalogTmplID() {
		return vendorCatalogTmplID;
	}

	public void setVendorCatalogTmplID(Long vendorCatalogTmplID) {
		this.vendorCatalogTmplID = vendorCatalogTmplID;
	}

	public String getVendorCatalogTemplateName() {
		return vendorCatalogTemplateName;
	}

	public void setVendorCatalogTemplateName(String vendorCatalogTemplateName) {
		this.vendorCatalogTemplateName = vendorCatalogTemplateName;
	}

	public List<CatalogMappedFieldDTO> getCatalogFieldMappingList() {
		return catalogFieldMappingList;
	}

	public void setCatalogFieldMappingList(List<CatalogMappedFieldDTO> catalogFieldMappingList) {
		this.catalogFieldMappingList = catalogFieldMappingList;
	}

	public int getMapRowNumber() {
		return mapRowNumber;
	}

	public void setMapRowNumber(int mapRowNumber) {
		this.mapRowNumber = mapRowNumber;
	}

	public Long getMapVendorSuppliedFieldID() {
		return mapVendorSuppliedFieldID;
	}

	public void setMapVendorSuppliedFieldID(Long mapVendorSuppliedFieldID) {
		this.mapVendorSuppliedFieldID = mapVendorSuppliedFieldID;
	}

	public String getMapVendorSuppliedField() {
		return mapVendorSuppliedField;
	}

	public void setMapVendorSuppliedField(String mapVendorSuppliedField) {
		this.mapVendorSuppliedField = mapVendorSuppliedField;
	}

	public boolean isMapIsMasterRecord() {
		return mapIsMasterRecord;
	}

	public void setMapIsMasterRecord(boolean mapIsMasterRecord) {
		this.mapIsMasterRecord = mapIsMasterRecord;
	}

	public Long getMapMasterMappingId() {
		return mapMasterMappingId;
	}

	public void setMapMasterMappingId(Long mapMasterMappingId) {
		this.mapMasterMappingId = mapMasterMappingId;
	}

	public String getMapBlueMartiniAttribute() {
		return mapBlueMartiniAttribute;
	}

	public void setMapBlueMartiniAttribute(String mapBlueMartiniAttribute) {
		this.mapBlueMartiniAttribute = mapBlueMartiniAttribute;
	}

	public Long getMapBlueMartiniKey() {
		return mapBlueMartiniKey;
	}

	public void setMapBlueMartiniKey(Long mapBlueMartiniIndex) {
		this.mapBlueMartiniKey = mapBlueMartiniIndex;
	}

	public String getMapMasterAttribute() {
		return mapMasterAttribute;
	}

	public void setMapMasterAttribute(String mapMasterAttribute) {
		this.mapMasterAttribute = mapMasterAttribute;
	}

	public Map<Long, CatalogMasterAttribute> getSessionCatalogMasterAttribute() {
		return sessionCatalogMasterAttribute;
	}

	public void setSessionCatalogMasterAttribute(
			Map<Long, CatalogMasterAttribute> sessionCatalogMasterAttribute) {
		this.sessionCatalogMasterAttribute = sessionCatalogMasterAttribute;
	}

	public Map<Integer, String> getSesssionBlueMartiniAttributes() {
		return sesssionBlueMartiniAttributes;
	}

	public void setSesssionBlueMartiniAttributes(Map<Integer, String> sesssionBlueMartiniAttributes) {
		this.sesssionBlueMartiniAttributes = sesssionBlueMartiniAttributes;
	}

	public Map<Long, CatalogHeaderDTO> getSessionVendorCatalogHeader() {
		return sessionVendorCatalogHeader;
	}

	public void setSessionVendorCatalogHeader(Map<Long, CatalogHeaderDTO> sessionVendorCatalogHeader) {
		this.sessionVendorCatalogHeader = sessionVendorCatalogHeader;
	}

	public boolean isGlbTemplate() {
		return glbTemplate;
	}

	public void setGlbTemplate(boolean glbTemplate) {
		this.glbTemplate = glbTemplate;
	}

	public List<VendorCatalogFieldMapping> getLstVendorCatalogFieldMapping() {
		return lstVendorCatalogFieldMapping;
	}

	public void setLstVendorCatalogFieldMapping(
			List<VendorCatalogFieldMapping> lstVendorCatalogFieldMapping) {
		this.lstVendorCatalogFieldMapping = lstVendorCatalogFieldMapping;
	}

	public List<MasterAttributeMapping> getLstMasterAttributeMapping() {
		return lstMasterAttributeMapping;
	}

	public void setLstMasterAttributeMapping(List<MasterAttributeMapping> lstMasterAttributeMapping) {
		this.lstMasterAttributeMapping = lstMasterAttributeMapping;
	}

	public String getSelectedProductGroups() {
		return selectedProductGroups;
	}

	public void setSelectedProductGroups(String selectedProductGroups) {
		this.selectedProductGroups = selectedProductGroups;
	}

	public List<CatalogHeaderDTO> getVendorCatalogHeaders() {
		return vendorCatalogHeaders;
	}

	public void setVendorCatalogHeaders(List<CatalogHeaderDTO> vendorCatalogHeaders) {
		this.vendorCatalogHeaders = vendorCatalogHeaders;
	}

	public List<String> getSessionCarFldDataMapping() {
		return sessionCarFldDataMapping;
	}

	public void setSessionCarFldDataMapping(List<String> sessionCarFldDataMapping) {
		this.sessionCarFldDataMapping = sessionCarFldDataMapping;
	}

	public Map<String, FieldMappingDataDTO> getSessionVendorFldDataMapping() {
		return sessionVendorFldDataMapping;
	}

	public void setSessionVendorFldDataMapping(
			Map<String, FieldMappingDataDTO> sessionVendorFldDataMapping) {
		this.sessionVendorFldDataMapping = sessionVendorFldDataMapping;
	}

	public String getMapVenFldDataMapping() {
		return mapVenFldDataMapping;
	}

	public void setMapVenFldDataMapping(String mapVenFldDataMapping) {
		this.mapVenFldDataMapping = mapVenFldDataMapping;
	}

	public List<String> getMapCarFldDataMapping() {
		return mapCarFldDataMapping;
	}

	public void setMapCarFldDataMapping(List<String> mapCarFldDataMapping) {
		this.mapCarFldDataMapping = mapCarFldDataMapping;
	}

	public List<FieldMappingDataDTO> getFldMappingDataDTOs() {
		return fldMappingDataDTOs;
	}

	public void setFldMappingDataDTOs(List<FieldMappingDataDTO> fldMappingDataDTOs) {
		this.fldMappingDataDTOs = fldMappingDataDTOs;
	}

	public String getMapVenHeaderData() {
		return mapVenHeaderData;
	}

	public void setMapVenHeaderData(String mapVenHeaderData) {
		this.mapVenHeaderData = mapVenHeaderData;
	}

	public Long getSelectedTemplateID() {
		return selectedTemplateID;
	}

	public void setSelectedTemplateID(Long selectedTemplateID) {
		this.selectedTemplateID = selectedTemplateID;
	}

	public List<Vendor> getVendors() {
		return vendors;
	}

	public void setVendors(List<Vendor> vendors) {
		this.vendors = vendors;
	}

	public String getSelectedVendor() {
		return selectedVendor;
	}

	public void setSelectedVendor(String selectedVendor) {
		this.selectedVendor = selectedVendor;
	}

	public List<VendorCatalogTemplate> getTemplatesForOtherVendor() {
		return templatesForOtherVendor;
	}

	public void setTemplatesForOtherVendor(List<VendorCatalogTemplate> templatesForOtherVendor) {
		this.templatesForOtherVendor = templatesForOtherVendor;
	}

	public String getTemplateFrmAnotherVendor() {
		return templateFrmAnotherVendor;
	}

	public void setTemplateFrmAnotherVendor(String templateFrmAnotherVendor) {
		this.templateFrmAnotherVendor = templateFrmAnotherVendor;
	}

	public String getErrorString() {
		return errorString;
	}

	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}

	/**
	 * @param sessionCarAttributes the sessionCarAttributes to set
	 */
	public void setSessionCarAttributes(List<CarAttributesDTO> sessionCarAttributes) {
		this.sessionCarAttributes = sessionCarAttributes;
	}

	/**
	 * @return the sessionCarAttributes
	 */
	public List<CarAttributesDTO> getSessionCarAttributes() {
		return sessionCarAttributes;
	}

}
