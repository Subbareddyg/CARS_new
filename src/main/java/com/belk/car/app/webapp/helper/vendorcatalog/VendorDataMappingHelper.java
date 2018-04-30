package com.belk.car.app.webapp.helper.vendorcatalog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.dto.CarAttributesDTO;
import com.belk.car.app.dto.CatalogHeaderDTO;
import com.belk.car.app.dto.CatalogMappedFieldDTO;
import com.belk.car.app.dto.CatalogVendorDTO;
import com.belk.car.app.dto.FieldMappingDataDTO;
import com.belk.car.app.model.vendorcatalog.CatalogGroupTemplate;
import com.belk.car.app.model.vendorcatalog.CatalogMasterAttribute;
import com.belk.car.app.model.vendorcatalog.CompositeKeyForDataMapping;
import com.belk.car.app.model.vendorcatalog.MasterAttributeMapping;
import com.belk.car.app.model.vendorcatalog.VendorCatalog;
import com.belk.car.app.model.vendorcatalog.VendorCatalogFieldMapping;
import com.belk.car.app.model.vendorcatalog.VendorCatalogHeader;
import com.belk.car.app.model.vendorcatalog.VendorCatalogTemplate;
import com.belk.car.app.service.ProductGroupManager;
import com.belk.car.app.service.VendorCatalogManager;
import com.belk.car.app.webapp.forms.VendorCatalogDataMappingForm;


/**
 * Helper class for the controller all the common functions are listed here for
 * the methods.
 * 
 */
public class VendorDataMappingHelper {

	private transient final Log log = LogFactory.getLog(VendorDataMappingHelper.class);

	// Session string value for the
	public static final String SESSION_VENDOR_HEADER_LIST = "sessionVendorHeaderList";
	public static final String SESSION_CAR_ATTRIBUTE = "sessionCarAttribute";
	public static final String SESSION_CATALOG_MAPPED_FIELD = "sessionCatalogMappedField";
	public static final String SESSION_VENDOR_FLD_MAP_DATA_LIST = "sesionVendorFldDataMapping";
	public static final String SESSION_VENDOR_FLD_MAPPED_DATA = "sesionFldDataMapped";
	public static final String SESSION_CARS_FLD_DATA_LIST = "sesionCarFldDataMapping";
	public static final String SESSION_SELECTED_PRODUCT_GROUPS="sessionSelectedProductGroups";
	//Template selected values
	public static final String EXISTING_TEMPLATE_FOR_VENDOR="sExistTemplateForVendor";
	public static final String GLOBAL_TEMPLATES="sGlobalTemplate";
	public static final String TEMPLATE_FROM_ANOTHER_VENDOR="sAnotherVendorTemplate";
	public static final String CREATE_NEW_TEMPLATE="newDataMap";
	public static final String TEMPLATE_SEL_FROM_ANOTHER_VENDOR="templateFrmAnotherVendor";
	
	

	// If you change the above variable please change them also in
	// the data mapping include JSPs

	/**
	 * Gets the vendor catalog info for the selected catalog. This method
	 * initializes the form. IF some data is missing in the first go. Please
	 * check this method.
	 * 
	 * @param dataMapFormRequest
	 * @param productGroupManager
	 * @param vendorCatalogManager
	 * @return
	 */
	public VendorCatalogDataMappingForm getVendorCatalog(
			VendorCatalogDataMappingForm dataMapFormRequest,
			ProductGroupManager productGroupManager, VendorCatalogManager vendorCatalogManager, boolean checkFalg) {
		if(log.isDebugEnabled()){
			log.debug("Inside getVendorCatalog()..");
		}
		VendorCatalogDataMappingForm vendorCatalogDataMapForm = null;
		Long catalogId = dataMapFormRequest.getCatalogId();
		List<CatalogGroupTemplate> catalogGroupTemplates = null;
		if (catalogId != null) {
			VendorCatalog vCatalog = vendorCatalogManager.getCatalogInfo(catalogId);
			if (vCatalog != null) {
				vendorCatalogDataMapForm = new VendorCatalogDataMappingForm();
				vendorCatalogDataMapForm.setCatalogId(catalogId);
				vendorCatalogDataMapForm.setVendors(vendorCatalogManager.getOtherVendors(vCatalog.getVendor().getVendorId()));
				VendorCatalogTemplate vendorCatalogTemplate = null;
				
				if (dataMapFormRequest.getSelectedTemplateID() != null){
					vendorCatalogTemplate = vendorCatalogManager.getVendorCatalogTemplate(dataMapFormRequest.getSelectedTemplateID());
				}else {
					vendorCatalogTemplate =vCatalog.getVendorCatalogTemplate(); 
				}
				
				List<VendorCatalogHeader> vendorCatalogHeaders = vCatalog
						.getVendorCatalogHeaderList();
				List<CatalogHeaderDTO> catalogHeaderDTOs = getAsCatalogHeaderDTO(vendorCatalogHeaders);
				vendorCatalogDataMapForm.setVendorCatalogHeaders(catalogHeaderDTOs);
				vendorCatalogDataMapForm.setProductGroupList(productGroupManager
						.getAllActiveProductGroups());
				vendorCatalogDataMapForm.setProductGroupSelected(dataMapFormRequest
						.getProductGroupSelected());
				if (vendorCatalogTemplate != null
						&& vendorCatalogTemplate.getVendorCatalogTmplID() > 0) {
					// If there is an existing template value set the value in
					// the session
					catalogGroupTemplates = vendorCatalogTemplate.getCatalogGroupTemplate();
					vendorCatalogDataMapForm.setVendorCatalogTmplID(vendorCatalogTemplate
							.getVendorCatalogTmplID());
					if (catalogGroupTemplates != null && !catalogGroupTemplates.isEmpty()
							&& (!checkFalg)) {
						String[] selectedProducts = new String[catalogGroupTemplates.size()];
						int iCount = 0;
						for (CatalogGroupTemplate catalogGroupTemplate : catalogGroupTemplates) {
							selectedProducts[iCount++] = ""
									+ catalogGroupTemplate.getCompositeKeyForCatalogGroupID()
											.getProductGroupID();
						}
						vendorCatalogDataMapForm.setProductGroupSelected(selectedProducts);
					}

				}
				vendorCatalogDataMapForm.setVendorCatalog(vCatalog);
				//Fill the existing templates for the Vendor
				List<VendorCatalogTemplate> existingTemplateForVendor = vendorCatalogManager.getCatlogTemplateForVendor(vCatalog.getVendor().getVendorId());
				vendorCatalogDataMapForm.setExistingTemplateForVendor(existingTemplateForVendor);
				List<VendorCatalogTemplate> globalTemplates = vendorCatalogManager.getGlobalTemplates();
				vendorCatalogDataMapForm.setGlobalTemplate(globalTemplates);
				
			}
		}
		return vendorCatalogDataMapForm;
	}

	/**
	 * Sets the vendor catalog info on the vendor catalog form based on the
	 * catalog id passed on the vendor mapping form.
	 * 
	 * @param vendorCatalogDataMappingForm
	 * @param vendorCatalogManager
	 * @return
	 */
	public VendorCatalogDataMappingForm getVendorCatalog(
			VendorCatalogDataMappingForm vendorCatalogDataMappingForm,
			VendorCatalogManager vendorCatalogManager) {
		if(log.isDebugEnabled()){
			log.debug("Inside getVendorCatalog()..");
		}
		Long catalogId = vendorCatalogDataMappingForm.getCatalogId();
		if (catalogId != null) {
			VendorCatalog vCatalog = vendorCatalogManager.getCatalogInfo(catalogId);
			vendorCatalogDataMappingForm.setVendorCatalog(vCatalog);
		}

		return vendorCatalogDataMappingForm;
	}

	/** 
	 * Gets the Car attributes for the selected group.
	 * 
	 * @param dataMapFormRequest
	 * @param productGroupManager
	 * @param vendorCatalogManager
	 * @return
	 */
	public VendorCatalogDataMappingForm getCarAttributes(
			VendorCatalogDataMappingForm vendorCatalogDataMapForm,
			ProductGroupManager productGroupManager, VendorCatalogManager vendorCatalogManager) {
		if(log.isDebugEnabled()){
			log.debug("Inside getCarAttributes()..");
		}
		String[] productsGroupSelected = vendorCatalogDataMapForm.getProductGroupSelected();
		String productGroupSelected = getStringArrAsString(productsGroupSelected);
		if (vendorCatalogDataMapForm != null && productGroupSelected != null
				&& productGroupSelected.length() > 0) {
			vendorCatalogDataMapForm.setBlueMartiniAttributes(vendorCatalogManager
					.getBlueMartiniAttribute(productGroupSelected, vendorCatalogDataMapForm.getCatalogId()));
			vendorCatalogDataMapForm.setCatalogMasterAttribute(vendorCatalogManager
					.getCatalogMasterAttr(getListOfBMAttributes(vendorCatalogDataMapForm.getBlueMartiniAttributes())));
			vendorCatalogDataMapForm.setProductGroupSelected(productsGroupSelected);
			vendorCatalogDataMapForm.setSelectedProductGroups(productGroupSelected);
		}
		return vendorCatalogDataMapForm;
	}

	private String getListOfBMAttributes(List<String> list) {
		if(log.isDebugEnabled()){
			log.debug("Inside getListOfBMAttributes()..");
		}
		StringBuffer buffer = new StringBuffer();
		if(null != list && !list.isEmpty()) {
			for(String atr : list) {
				// added code to the special characters Drop ship bug fix deployment 2012 
				atr=atr.replaceAll("'","''");
				buffer.append("'").append(atr).append("'").append(",");
			}
			buffer.deleteCharAt(buffer.length() - 1);
			return buffer.toString();
		}
		return null;
	}
	/**
	 * Gets the car attributes and mapped attributes if a template is already
	 * associated with the Catalog
	 * 
	 * @param vendorCatalogDataMapForm
	 * @param productGroupManager
	 * @param vendorCatalogManager
	 * @return
	 */
	public VendorCatalogDataMappingForm getTemplateCarAttributes(
			VendorCatalogDataMappingForm vendorCatalogDataMapForm,
			ProductGroupManager productGroupManager, VendorCatalogManager vendorCatalogManager) {
		if(log.isDebugEnabled()){
			log.debug("Inside getTemplateCarAttributes()..");
		}
		String[] productsGroupSelected = vendorCatalogDataMapForm.getProductGroupSelected();
		String productGroupSelected = getStringArrAsString(vendorCatalogDataMapForm
				.getProductGroupSelected());
		Long vendorCatalogID = vendorCatalogDataMapForm.getCatalogId();
		if (vendorCatalogDataMapForm != null && productGroupSelected != null
				&& productGroupSelected.length() > 0) {
			// Setting the existing mapped list values
			vendorCatalogDataMapForm.setVendorCatalogHeaders(vendorCatalogManager
					.getTemplateVendorCatalogHeaderList(vendorCatalogID, vendorCatalogDataMapForm.getVendorCatalogTmplID()));
			vendorCatalogDataMapForm.setBlueMartiniAttributes(vendorCatalogManager
					.getTemplateBlueMartiniAttribute(productGroupSelected, vendorCatalogID));
			vendorCatalogDataMapForm.setCatalogMasterAttribute(vendorCatalogManager
					.getTemplateCatalogMasterAttr(vendorCatalogID, getListOfBMAttributes(vendorCatalogDataMapForm.getBlueMartiniAttributes())));
			vendorCatalogDataMapForm.setProductGroupSelected(productsGroupSelected);
			vendorCatalogDataMapForm.setSelectedProductGroups(productGroupSelected);
			vendorCatalogDataMapForm.setCatalogFieldMappingList(vendorCatalogManager
					.getTemplateMappedValueList(vendorCatalogID, vendorCatalogDataMapForm.getVendorCatalogTmplID()));
		}
		return vendorCatalogDataMapForm;
	}
	/**
	 * Sets the Vendor Supplied Field values at page load.
	 * @param vendorCatalogDataMappingForm	{@link VendorCatalogDataMappingForm} Vendor Catalog Data mapping Form in which values to be set.
	 * @return vendorCatalogDataMappingForm	Vendor Catalog Data mapping Form with the required values.
	 */
	public VendorCatalogDataMappingForm setLoadValues(VendorCatalogDataMappingForm vendorCatalogDataMappingForm){
		if(log.isDebugEnabled()){
			log.debug("Inside setLoadValues()..");
		}
		List<CatalogHeaderDTO> tempCatalogHeader = vendorCatalogDataMappingForm.getVendorCatalogHeaders();
		log.debug(" vendor catalog headers.....:"+tempCatalogHeader);
		Method methodVendorCatalogHeaderId = null;
		try {
			methodVendorCatalogHeaderId = CatalogHeaderDTO.class.getDeclaredMethod("getVendorCatalogHeaderId");
		} catch (SecurityException e) {
			log.error("Security Exception ", e);
		} catch (NoSuchMethodException e) {
			log.error("NoSuchMethodException ", e);
		}
		//If Vendor Supplied document have Header columns.
		if (tempCatalogHeader != null && !tempCatalogHeader.isEmpty()) {
			//then ma
			Map<Long, CatalogHeaderDTO> vendorCatalogHeaderListMap = (Map<Long, CatalogHeaderDTO>) getAsMap(
			tempCatalogHeader, methodVendorCatalogHeaderId);
			if(log.isDebugEnabled()){
				log.debug("vendorCatalogHeaderListMap:"+vendorCatalogHeaderListMap);
			}
			
			vendorCatalogDataMappingForm.setSessionVendorCatalogHeader(vendorCatalogHeaderListMap);
		}
		return vendorCatalogDataMappingForm;
	}
	/**
	 * Gets the vendorCatalogDataMapping form and converts the necessary list
	 * value to a map value and sets it back in the vendorcatalogDataMapping
	 * form.
	 * 
	 * @param vendorCatalogDataMappingForm
	 * @return vendorCatalogDataMappingForm
	 */
	public VendorCatalogDataMappingForm getSessionCarAttributes(
			VendorCatalogDataMappingForm vendorCatalogDataMappingForm) {
		if(log.isDebugEnabled()){
			log.debug("Inside getSessionCarAttributes()..");
		}
		Method methodCatalogMasterAttrId = null;
		Method methodVendorCatalogHeaderId = null;
		List<CarAttributesDTO> list = new ArrayList<CarAttributesDTO>();
		try {
			methodCatalogMasterAttrId = CatalogMasterAttribute.class
					.getDeclaredMethod("getCatalogMasterAttrId");
			methodVendorCatalogHeaderId = CatalogHeaderDTO.class
					.getDeclaredMethod("getVendorCatalogHeaderId");
		} catch (SecurityException e) {
			log.error("Security Exception ", e);
		} catch (NoSuchMethodException e) {
			log.error("NoSuchMethodException ", e);
		}
		List<CatalogMasterAttribute> tempMasterAttribute = vendorCatalogDataMappingForm
				.getCatalogMasterAttribute();
		
		if (tempMasterAttribute != null && !tempMasterAttribute.isEmpty()) {
			Iterator<CatalogMasterAttribute> iterator = tempMasterAttribute.iterator();
			while (iterator.hasNext()) {
				CatalogMasterAttribute catalogMasterAttribute = (CatalogMasterAttribute) iterator
						.next();
				CarAttributesDTO attributesDTO = new CarAttributesDTO();
				attributesDTO.setAttributeKey(catalogMasterAttribute.getCatalogMasterAttrId());
				attributesDTO.setAttributeName(catalogMasterAttribute.getCatalogMasterAttrName());
				attributesDTO.setMasterAttribute("Y");
				list.add(attributesDTO);
			}
		}
		String[] productsGroupSelected = vendorCatalogDataMappingForm.getProductGroupSelected();
		String productGroupSelected = getStringArrAsString(productsGroupSelected);
		if (vendorCatalogDataMappingForm != null) {
			List<CatalogHeaderDTO> tempCatalogHeader = vendorCatalogDataMappingForm.getVendorCatalogHeaders();
			if(log.isDebugEnabled()){
				log.debug("tempCatalogHeader list.............:"+tempCatalogHeader);
			}
			if (tempCatalogHeader != null && !tempCatalogHeader.isEmpty()) {
				Map<Long, CatalogHeaderDTO> vendorCatalogHeaderListMap = (Map<Long, CatalogHeaderDTO>) getAsMap(tempCatalogHeader, methodVendorCatalogHeaderId);
				if(log.isDebugEnabled()){
					log.debug("vendorCatalogHeaderListMap.............:"+vendorCatalogHeaderListMap);
				}
				vendorCatalogDataMappingForm
						.setSessionVendorCatalogHeader(vendorCatalogHeaderListMap);
			}
		}
		// Setting the Blue Martini attributes in session as an Map
		List<String> blueMartiniAttributes = vendorCatalogDataMappingForm
				.getBlueMartiniAttributes();
		if (blueMartiniAttributes != null && !blueMartiniAttributes.isEmpty()) {
			Map<Integer, String> mapBMAttribute = new HashMap<Integer, String>(
					blueMartiniAttributes.size());
			for (int iCount = 0; iCount < blueMartiniAttributes.size(); iCount++) {
				CarAttributesDTO attributesDTO = new CarAttributesDTO();
				attributesDTO.setAttributeKey(new Long(iCount+1));
				attributesDTO.setAttributeName(blueMartiniAttributes.get(iCount));
				attributesDTO.setMasterAttribute("N");
				list.add(attributesDTO);
			}
		}
		if(null != list && !list.isEmpty()) {
			Collections.sort(list);
			vendorCatalogDataMappingForm.setSessionCarAttributes(list);
		}
		return vendorCatalogDataMappingForm;
	}

	/**
	 * Gets the vendor catalog template object from the vendor catalog form.
	 * 
	 * @param vendorCatalogDataMappingForm
	 * @return
	 */
	public VendorCatalogTemplate getVendorCatalogTemplate(
			VendorCatalogDataMappingForm vendorCatalogDataMappingForm) {
		VendorCatalogTemplate vendorCatalogTemplate = vendorCatalogDataMappingForm
				.getVendorCatalogTemplate();
		return vendorCatalogTemplate;

	}

	/**
	 * Gets the vendor catalog object from the vendor catalog form
	 * 
	 * @param vendorCatalogDataMappingForm
	 * @return
	 */
	public VendorCatalog getVendorCatalog(VendorCatalogDataMappingForm vendorCatalogDataMappingForm) {
		return  vendorCatalogDataMappingForm.getVendorCatalog();
		

	}

	/**
	 * Gets the list value as a map value based on the method key specified.
	 * 
	 * @param list
	 *            : That is to be converted as a Map
	 * @param method
	 *            : The method of they key value
	 * @return : returns the newly created map
	 */
	public Map<?, ?> getAsMap(List<?> list, Method method) {
		if(log.isDebugEnabled()){
			log.debug("Inside getAsMap()..");
		}
		Map<Object, Object> map = new HashMap<Object, Object>();

		for (Object obj : list) {
			try {
				map.put(method.invoke(obj), obj);
			} catch (IllegalArgumentException e) {
				log.error("IllegalArgumentException at getAsMap", e);
			} catch (IllegalAccessException e) {
				log.error("IllegalAccessException at getAsMap", e);
			} catch (InvocationTargetException e) {
				log.error("InvocationTargetException at getAsMap", e);
			}
		}
		map = new TreeMap<Object, Object>(map);
		return map;

	}

	/**
	 * 
	 * Sets the following session values from the form values
	 * SESSION_VENDOR_HEADER_LIST, SESSION_CATALOG_MASTER_ATTRIBUTE
	 * ,SESSION_BLUE_MARTINI_ATTRIBUTE SESSION_CATALOG_MAPPED_FIELD
	 * 
	 * @param session
	 * @param vendorCatalogDataMappingForm
	 * @return
	 */
	public HttpSession setSessionValues(HttpSession session,
			VendorCatalogDataMappingForm vendorCatalogDataMappingForm) {
		if(log.isDebugEnabled()){
			log.debug("Inside setSessionValues()..");
		}
		session.setAttribute(SESSION_VENDOR_HEADER_LIST, vendorCatalogDataMappingForm
				.getSessionVendorCatalogHeader());
		session.setAttribute(SESSION_CAR_ATTRIBUTE, vendorCatalogDataMappingForm.getSessionCarAttributes());
		session.setAttribute(SESSION_CATALOG_MAPPED_FIELD, vendorCatalogDataMappingForm
				.getCatalogFieldMappingList());
		session.setAttribute(SESSION_VENDOR_FLD_MAP_DATA_LIST, vendorCatalogDataMappingForm
				.getSessionVendorFldDataMapping());
		session.setAttribute(SESSION_CARS_FLD_DATA_LIST, vendorCatalogDataMappingForm
				.getSessionCarFldDataMapping());
		session.setAttribute(SESSION_VENDOR_FLD_MAPPED_DATA, vendorCatalogDataMappingForm
				.getFldMappingDataDTOs());
		//Adding product groups selected to the session to retrieve the appropriate Car attribute values.
		session.setAttribute(SESSION_SELECTED_PRODUCT_GROUPS, vendorCatalogDataMappingForm.getSelectedProductGroups());
		if(log.isDebugEnabled()){
			log.debug("Selected product Groups are:"+ vendorCatalogDataMappingForm.getSelectedProductGroups());
		}
		return session;
	}

	/**
	 * Function gets the master attribute mapping and vendor catalog mapping
	 * seperately from the catalog mapped field dto
	 * 
	 * @param vendorCatalogDataMappingForm
	 * @return
	 */
	public VendorCatalogDataMappingForm getVendorCatalogFieldMapping(
			VendorCatalogDataMappingForm vendorCatalogDataMappingForm, VendorCatalogManager vendorCatalogManager) {
		if(log.isDebugEnabled()){
			log.debug("Inside getVendorCatalogFieldMapping()..");
		}
		List<CatalogMappedFieldDTO> lstCatalogMappedFieldDTO = vendorCatalogDataMappingForm
				.getCatalogFieldMappingList();
		if(log.isDebugEnabled()){
			log.debug("inside getVendorCatalogFieldMapping method lstCatalogMappedFieldDTO :"+lstCatalogMappedFieldDTO);
		}
		
		List<VendorCatalogFieldMapping> lstVendorCatalogFieldMapping = new ArrayList<VendorCatalogFieldMapping>();
		List<MasterAttributeMapping> lstMasterAttributeMapping = new ArrayList<MasterAttributeMapping>();

		VendorCatalogFieldMapping vendorCatalogFieldMapping = null;
		VendorCatalogHeader vendorCatalogHeader = null;
		CompositeKeyForDataMapping compositekeyForDataMapping = null;
		MasterAttributeMapping masterAttributeMapping = null;
		for (CatalogMappedFieldDTO catalogMappedFieldDTO : lstCatalogMappedFieldDTO) {
			if (catalogMappedFieldDTO.isMasterRecord()) {
				masterAttributeMapping = new MasterAttributeMapping();
				List<MasterAttributeMapping> list = vendorCatalogManager.getMasterMappingAttribute(vendorCatalogDataMappingForm
						.getVendorCatalogTmplID(), catalogMappedFieldDTO.getVendorSuppliedField());
				if (list.size() > 0) {
					masterAttributeMapping = list.get(0);
				}
				if(catalogMappedFieldDTO.getMasterAttributeID() != null){
					masterAttributeMapping.setCatalogMasterAttrId(catalogMappedFieldDTO.getMasterAttributeID());
				}
				masterAttributeMapping.setCatalogTemplateId(vendorCatalogDataMappingForm
						.getVendorCatalogTmplID());
				masterAttributeMapping.setCatalogHeaderFieldName(catalogMappedFieldDTO
						.getVendorSuppliedField());
				masterAttributeMapping.setStatus(DropShipConstants.ACTIVE);

				lstMasterAttributeMapping.add(masterAttributeMapping);
			} else {
				vendorCatalogFieldMapping = new VendorCatalogFieldMapping();
				vendorCatalogHeader = new VendorCatalogHeader();
				compositekeyForDataMapping = new CompositeKeyForDataMapping();
				vendorCatalogHeader.setVendorCatalogHeaderId(catalogMappedFieldDTO
						.getVendorSuppliedFieldID());
				compositekeyForDataMapping.setVendorCatalogtTemplateId(vendorCatalogDataMappingForm
						.getVendorCatalogTmplID());
				compositekeyForDataMapping.setVendorCatalogHeaderFieldName(catalogMappedFieldDTO
						.getVendorSuppliedField());
				vendorCatalogFieldMapping.setCompositeKey(compositekeyForDataMapping);
				vendorCatalogFieldMapping.setBlueMartiniAttrName(catalogMappedFieldDTO
						.getMappingAttribute());
				vendorCatalogFieldMapping.setVendorCatalogHeader(vendorCatalogHeader);

				lstVendorCatalogFieldMapping.add(vendorCatalogFieldMapping);
			}

		}
		vendorCatalogDataMappingForm.setLstMasterAttributeMapping(lstMasterAttributeMapping);
		vendorCatalogDataMappingForm.setLstVendorCatalogFieldMapping(lstVendorCatalogFieldMapping);
		
		return vendorCatalogDataMappingForm;

	}

	/**
	 * Gets the string[] and returns the String
	 * 
	 * @param stringArrayValue
	 * @return
	 */
	public String getStringArrAsString(String[] stringArrayValue) {
		if(log.isDebugEnabled()){
			log.debug("Inside getStringArrAsString()..");
		}
		String stringValue = "";
		if (stringArrayValue != null && stringArrayValue.length > 0) {
			stringValue = Arrays.toString(stringArrayValue);
			stringValue = stringValue.replaceAll("\\[", "");
			stringValue = stringValue.replaceAll("\\]", "");
		}
		return stringValue;

	}

	/**
	 * Gets the database value and transfers it to the DTO
	 * 
	 * @param vendorCatalogHeaders
	 * @return
	 */
	public List<CatalogHeaderDTO> getAsCatalogHeaderDTO(
			List<VendorCatalogHeader> vendorCatalogHeaders) {
		if(log.isDebugEnabled()){
			log.debug("Inside getAsCatalogHeaderDTO()..");
		}
		List<CatalogHeaderDTO> catalogHeaderDTOs = null;
		if (vendorCatalogHeaders != null && !vendorCatalogHeaders.isEmpty()) {
			catalogHeaderDTOs = new ArrayList<CatalogHeaderDTO>(vendorCatalogHeaders.size());
			CatalogHeaderDTO catalogHeaderDTO = null;
			for (VendorCatalogHeader vendorCatalogHeader : vendorCatalogHeaders) {
				catalogHeaderDTO = new CatalogHeaderDTO(vendorCatalogHeader);
				log.debug("Mapping Header: "+ vendorCatalogHeader.getVendorCatalogHeaderFieldName());
				catalogHeaderDTOs.add(catalogHeaderDTO);
			}
		}
		return catalogHeaderDTOs;
	}

	/**
	 * Gets the vendor field mapping values and cars field mapping values and
	 * form a Map
	 * 
	 * @param vendorFldMappingValues
	 * @param carsFldMappingValues
	 * @return
	 */
	public Map<String, FieldMappingDataDTO> getFldDataMappingMap(
			List<String> vendorFldMappingValues, List<String> carsFldMappingValues, String vendorHeaderField,
			boolean isMapped,List<FieldMappingDataDTO> savedFieldMappingData) {
		if(log.isDebugEnabled()){
			log.debug("Inside getFldDataMappingMap()..");
		}
		FieldMappingDataDTO existingFieldDTO=null;
		if(savedFieldMappingData!=null){
			for(FieldMappingDataDTO fieldDTO:savedFieldMappingData){
				if(log.isDebugEnabled()){
					log.debug("fieldDTO....."+fieldDTO);
					log.debug("isMapped...."+fieldDTO.isMapped());
				}
				if(fieldDTO.isMapped()){
					existingFieldDTO=fieldDTO;
				}
			}
		}
		Map<String, FieldMappingDataDTO> fldDataMapping = null;
		if (vendorFldMappingValues != null && !vendorFldMappingValues.isEmpty()) {
			fldDataMapping = new HashMap<String, FieldMappingDataDTO>(vendorFldMappingValues.size());

			for (String vendorFldMapValue : vendorFldMappingValues) {
				FieldMappingDataDTO fieldMappingDataDTO = new FieldMappingDataDTO();
				fieldMappingDataDTO.setVendorField(vendorFldMapValue);
				fieldMappingDataDTO.setVendorHeaderField(vendorHeaderField);
				fieldMappingDataDTO.setMapped(isMapped);
				if(existingFieldDTO!=null){
					if(log.isDebugEnabled()){
						log.debug("setting CarValue....."+existingFieldDTO.getCarValue());
					}
					fieldMappingDataDTO.setCarValue(existingFieldDTO.getCarValue());
				}
				fieldMappingDataDTO.setCarValueList(carsFldMappingValues);
				fldDataMapping.put(vendorFldMapValue, fieldMappingDataDTO);
			}
		}
		return fldDataMapping;
	}

	/**
	 * Gets the value of FieldMappingDataDTO as a list and returns as a Map for use in the JSP.
	 * @param fieldMappingDataDTOs
	 * @return
	 */
	public Map<String, FieldMappingDataDTO> getFldDataMappingAsMap(List<FieldMappingDataDTO> fieldMappingDataDTOs, List<String> carValueList){
		if(log.isDebugEnabled()){
			log.debug("Inside getFldDataMappingAsMap()..");
		}
		Method methodVendorField = null;
		Map<String, FieldMappingDataDTO> tmpFieldMappingDataDTOs = null;
		if (fieldMappingDataDTOs != null && !fieldMappingDataDTOs.isEmpty()) {
			tmpFieldMappingDataDTOs = new HashMap<String, FieldMappingDataDTO>(fieldMappingDataDTOs.size());
			for (FieldMappingDataDTO fieldMappedDTO : fieldMappingDataDTOs){
				if(!fieldMappedDTO.isMapped()){
					fieldMappedDTO.setCarValueList(carValueList);
				}
				tmpFieldMappingDataDTOs.put(fieldMappedDTO.getVendorField(), fieldMappedDTO);
			}
		}
		return tmpFieldMappingDataDTOs;

	}

	public void populateVendorCatalogInfoInSession(HttpSession session, VendorCatalogManager manager, String catalogId) {
		if(log.isDebugEnabled()){
			log.debug("Inside populateVendorCatalogInfoInSession()..");
		}
		VendorCatalog vCatalog = manager.getCatalogInfo(new Long(catalogId));
		try {
			CatalogVendorDTO vendorDTO = this.populateCatalogDTO(manager, vCatalog.getVendor().getVendorNumber());
			session.setAttribute("vendorCatalogInfo", vendorDTO);
			session.setAttribute("vendorCatalog", vCatalog);
		} catch (Exception e) {
			log.error(e);
		}
	}
	
	private CatalogVendorDTO populateCatalogDTO(VendorCatalogManager manager, String vendorNo) throws Exception {
		if(log.isDebugEnabled()){
			log.debug("Inside populateCatalogDTO()..");
		}
		List<CatalogVendorDTO> list = manager.getAllVendors(vendorNo);
		CatalogVendorDTO catalogVendorDTO = list.get(0);
		return catalogVendorDTO;
	}
}
