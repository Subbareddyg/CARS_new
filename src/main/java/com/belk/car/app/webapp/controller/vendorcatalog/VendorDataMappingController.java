package com.belk.car.app.webapp.controller.vendorcatalog;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.dto.CarAttributesDTO;
import com.belk.car.app.dto.CatalogHeaderDTO;
import com.belk.car.app.dto.CatalogMappedFieldDTO;
import com.belk.car.app.dto.FieldMappingDataDTO;
import com.belk.car.app.model.AttributeLookupValue;
import com.belk.car.app.model.ProductGroup;
import com.belk.car.app.model.vendorcatalog.CatalogGroupTemplate;
import com.belk.car.app.model.vendorcatalog.CompositeKeyForCatalogGroupTemplate;
import com.belk.car.app.model.vendorcatalog.CompositeKeyForDataFldMapping;
import com.belk.car.app.model.vendorcatalog.MasterAttributeMapping;
import com.belk.car.app.model.vendorcatalog.VendorCatalog;
import com.belk.car.app.model.vendorcatalog.VendorCatalogFieldDataMapping;
import com.belk.car.app.model.vendorcatalog.VendorCatalogFieldMapping;
import com.belk.car.app.model.vendorcatalog.VendorCatalogHeader;
import com.belk.car.app.model.vendorcatalog.VendorCatalogImport;
import com.belk.car.app.model.vendorcatalog.VendorCatalogTemplate;
import com.belk.car.app.service.ProductGroupManager;
import com.belk.car.app.service.VendorCatalogManager;
import com.belk.car.app.webapp.controller.MultiActionFormController;
import com.belk.car.app.webapp.forms.VendorCatalogDataMappingForm;
import com.belk.car.app.webapp.helper.vendorcatalog.VendorDataMappingHelper;

/**
 * Gets the catalog id and does the mapping for that catalog.
 * 
 */
public class VendorDataMappingController extends MultiActionFormController implements DropShipConstants{

	private transient final Log log = LogFactory.getLog(VendorDataMappingController.class);

	private VendorCatalogManager vendorCatalogManager;
	private ProductGroupManager productGroupManager;
	private VendorDataMappingHelper vendorDataMappingHelper = new VendorDataMappingHelper();

	/**
	 * Getter for VendorCatalog Manager
	 * 
	 * @return : Current VendorCatalogManager
	 */
	public VendorCatalogManager getVendorCatalogManager() {
		return vendorCatalogManager;
	}

	/**
	 * Setter for VendorCatalog Manager
	 * 
	 * @param vendorCatalogManager
	 */
	public void setVendorCatalogManager(VendorCatalogManager vendorCatalogManager) {
		this.vendorCatalogManager = vendorCatalogManager;
	}

	public ProductGroupManager getProductGroupManager() {
		return productGroupManager;
	}

	public void setProductGroupManager(ProductGroupManager productGroupManager) {
		this.productGroupManager = productGroupManager;
	}

	/**
	 * This method opens the uploaded catalog file in the new browser.
	 * @param request
	 * @param response
	 * @param vendorCatalogDataMappingForm
	 * @return null
	 * */
	public ModelAndView openCatalogFile(HttpServletRequest request, HttpServletResponse response,
			VendorCatalogDataMappingForm vendorCatalogDataMappingForm) throws Exception {
		Properties properties = (Properties) getServletContext().getAttribute(
				DropShipConstants.FTPDETAILS);
		
		String filePath;
		//Getting vendor for folder structure.
		//Add logic to check the status of the catalog.
		String catalogStatus;
		String catalogVendorNumber;
		String catalogId;
		String catalogFileName;
		VendorCatalogImport vendorCatalogImport ;
		if(StringUtils.isNotBlank(request.getParameter("catalogStatus"))){
			catalogStatus = request.getParameter("catalogStatus");
		}else{
			catalogStatus = IMPORTING;
		}
		//catalogVendor
		if(StringUtils.isNotBlank(request.getParameter("catalogVendor"))){
			catalogVendorNumber = request.getParameter("catalogVendor");
		}else{
			catalogVendorNumber = "0000";
		}
		//Create the File Path
		if(catalogStatus.equalsIgnoreCase(IMPORTING)){
			filePath = properties.getProperty("catalogFilePath");
			filePath = filePath +"/"+ catalogVendorNumber;
		}else{
			filePath = properties.getProperty("catalogFilePathArchive"); ///carsint/data/catalogs/dev/archive
			filePath = filePath +"/"+ catalogVendorNumber; ///carsint/data/catalogs/dev/archive/catalogVendorNumber
		}
		if (log.isDebugEnabled()){
			log.debug("File Path :"+filePath);
		}
		
		//Get the catalog Id 
		if(StringUtils.isNotBlank(request.getParameter("catalogId"))){
			catalogId = request.getParameter("catalogId");
		}else{
			catalogId = "";
		}
		//Get the Import Vendor Catalog to get the file name
		if(StringUtils.isNotBlank(catalogId)){
			vendorCatalogImport = vendorCatalogManager.getVendorCatalogImportDetails((Long.parseLong(catalogId)));
			filePath = filePath + "/" + catalogId + "/" + vendorCatalogImport.getVendorCatalogFileName();
			catalogFileName = vendorCatalogImport.getVendorCatalogFileName();
		}else{
			filePath = filePath + "/" + request.getParameter("fileName");
			catalogFileName = request.getParameter("fileName");
		}
		if (log.isDebugEnabled()){
			log.debug("Complete File Path :"+filePath);
		}
		try {
			InputStream inputStream = new FileInputStream(filePath);
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ catalogFileName);
			ServletOutputStream outStream = response.getOutputStream();
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
			inputStream.close();
			outStream.close();
		} catch (FileNotFoundException e) {
			String errString = "File " + request.getParameter("fileName") + " does not exist on the server.";
			if (request.getParameter("vendorCatalog") != null
					&& request.getParameter("vendorCatalog").equalsIgnoreCase("true")) {
				saveError(request, errString);
				return new ModelAndView("redirect:/vendorCatalog/vendorCatalogSetupForm.html?&mode="+request.getSession().getAttribute(DropShipConstants.MODE)+"&vcID="+request.getSession().getAttribute("vcID")+"&fileName="+request.getParameter("fileName"));
				
			} else {
				List<String> errors = new ArrayList<String>();
				errors.add(errString);	
				request.setAttribute("error", errors);
				request.setAttribute("cid" , request.getSession().getAttribute("CATALOG_ID"));
				return getVendorCatalog(request, response);
			}
		}
		return null;
	}
	/**
	 * InitialLoad of the Data mapping screen. Based on the Catalog Id
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getVendorCatalog(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView("redirect:/mainMenu.html");
		VendorCatalogDataMappingForm requestDataMapForm = new VendorCatalogDataMappingForm();
		String catalogID = request.getParameter("cid");
		if (StringUtils.isBlank(catalogID) || null == catalogID){
			if(null != request.getAttribute("cid")) {
				catalogID = (String) request.getAttribute("cid");
			} else {
				catalogID = (String) request.getSession().getAttribute("CATALOG_ID_FOR_MAPPING");
			}
		}
		request.getSession().setAttribute("CATALOG_ID_FOR_MAPPING", catalogID);
		if (StringUtils.isNotBlank(catalogID)) {
			if (log.isDebugEnabled()){
				log.debug("GetVendorCatalog catalogID: " + catalogID);
			}
			if (catalogID != null && catalogID.length() > 0) {

				requestDataMapForm.setCatalogId(Long.parseLong(catalogID));
				requestDataMapForm = vendorDataMappingHelper.getVendorCatalog(requestDataMapForm,
						productGroupManager, vendorCatalogManager, false);
				request.getSession().setAttribute("CATALOG_ID", catalogID);
				if (requestDataMapForm.getVendorCatalogTmplID() != null) {
					// A template is already assigned to this catalog so getting
					// values for it.
					requestDataMapForm = vendorDataMappingHelper.getTemplateCarAttributes(
							requestDataMapForm, productGroupManager, vendorCatalogManager);
					requestDataMapForm = vendorDataMappingHelper
							.getSessionCarAttributes(requestDataMapForm);
					request.getSession().setAttribute("CATALOG_TEMPLATE_ID", requestDataMapForm.getVendorCatalogTmplID());

				} else {
					request.getSession().removeAttribute("CATALOG_TEMPLATE_ID");
				}
				modelAndView = new ModelAndView(getSuccessView(), "vendorCatalogDataMappingForm",
						requestDataMapForm);
			}

		}
		//Added this method call to fix the issue of setting the Vendor supplied field values at page load. 
		requestDataMapForm = vendorDataMappingHelper.setLoadValues(requestDataMapForm);
		// Sets the session attribute to null;
		HttpSession session = vendorDataMappingHelper.setSessionValues(request.getSession(),
				requestDataMapForm);
		if(null == session.getAttribute("vendorCatalogInfo")) {
			vendorDataMappingHelper.populateVendorCatalogInfoInSession(
					session,
					vendorCatalogManager, catalogID);
		}
		request.setAttribute("scrollPos", 0);
		return modelAndView;
	}

	/**
	 * Get the cars attributes for the data mapping based on the product groups
	 * selected.
	 * 
	 * @param request
	 * @param response
	 * @param vendorCatalogDataMappingForm
	 * @return
	 */
	public ModelAndView getCarAttributes(HttpServletRequest request, HttpServletResponse response,
			VendorCatalogDataMappingForm vendorCatalogDataMappingForm) {
		// Gets the Vendor Catalog Informaiton according to the catalog Id.
		VendorCatalogDataMappingForm retVendorCatalogDataMappingForm = vendorDataMappingHelper
				.getVendorCatalog(vendorCatalogDataMappingForm, productGroupManager,
						vendorCatalogManager, true);
		//Get Car attributes for the selected Product Group.
		retVendorCatalogDataMappingForm = vendorDataMappingHelper.getCarAttributes(
				retVendorCatalogDataMappingForm, productGroupManager, vendorCatalogManager);
		//Set the selected product groups in session for retrieving the Car Attributes.
		
		//Set the values in MAP
		retVendorCatalogDataMappingForm = vendorDataMappingHelper
				.getSessionCarAttributes(retVendorCatalogDataMappingForm);
		//Set the values in Session, so as to access the values easily for next operations. 
		vendorDataMappingHelper.setSessionValues(request.getSession(),
				retVendorCatalogDataMappingForm);

		return new ModelAndView("vendorCatalog/datamapping/inc_mappingrules",
				"vendorCatalogDataMappingForm", retVendorCatalogDataMappingForm);
	}

	/**
	 * Saves the catalog information related to catalog template.
	 * 
	 * @param request
	 * @param response
	 * @param vendorCatalogDataMapForm
	 * @return
	 */
	public ModelAndView saveTemplate(HttpServletRequest request, HttpServletResponse response,
			VendorCatalogDataMappingForm vendorCatalogDataMappingForm) {

		vendorCatalogDataMappingForm = saveCatalogTemplate(request, response,
				vendorCatalogDataMappingForm, false);

		ModelAndView modelAndView = new ModelAndView(getSuccessView(),
				"vendorCatalogDataMappingForm", vendorCatalogDataMappingForm);
		return checkErrorValues(request, response, vendorCatalogDataMappingForm, modelAndView, SAVE_TEMPLATE);
	}

	@Override
	protected void bind(HttpServletRequest request, Object command) throws Exception {
		ServletRequestDataBinder binder = createBinder(request, command);
		binder.bind(request);
		errors = binder.getBindingResult();

	}

	/**
	 * When the >> symbol is clicked on the page this function is invoked and
	 * values are mapped to the session
	 * 
	 * @param request
	 * @param response
	 * @param vendorCatalogDataMappingForm
	 * @return
	 */
	public ModelAndView mapAttributes(HttpServletRequest request, HttpServletResponse response,
			VendorCatalogDataMappingForm vendorCatalogDataMappingForm) {
		if(log.isDebugEnabled()){
			log.debug("mapAttributes");
		}
		
		// Subbu: //To do validation errors
		HttpSession session = request.getSession();
		//Check whether the user session is timed out.
		if(session.getAttribute(VendorDataMappingHelper.SESSION_VENDOR_HEADER_LIST) == null 
				|| session.getAttribute(VendorDataMappingHelper.SESSION_CAR_ATTRIBUTE) == null
				|| session.getAttribute(VendorDataMappingHelper.SESSION_CATALOG_MAPPED_FIELD) == null)
		{
			return new ModelAndView("redirect:/mainMenu.html");
		}
		Map<Long, CatalogHeaderDTO> vendorHeader = (Map<Long, CatalogHeaderDTO>) session
				.getAttribute(VendorDataMappingHelper.SESSION_VENDOR_HEADER_LIST);
		/*Map<Long, CatalogMasterAttribute> catalogMaster = (Map<Long, CatalogMasterAttribute>) session
				.getAttribute(VendorDataMappingHelper.SESSION_CATALOG_MASTER_ATTRIBUTE);
		Map<Integer, String> sessionBlueMartiniAttributes = (Map<Integer, String>) session
				.getAttribute(VendorDataMappingHelper.SESSION_BLUE_MARTINI_ATTRIBUTE);*/
		List<CarAttributesDTO> sessionCarAttributes = (List<CarAttributesDTO>) session.getAttribute(VendorDataMappingHelper.SESSION_CAR_ATTRIBUTE); 
		List<CatalogMappedFieldDTO> mappedFieldDTO = (List<CatalogMappedFieldDTO>) session
				.getAttribute(VendorDataMappingHelper.SESSION_CATALOG_MAPPED_FIELD);
		//Get the selected product groups
		String selectedPrdGrp = (String)session.getAttribute("sessionSelectedProductGroups");
		log.debug("New String==>"+selectedPrdGrp);
		//(List<String>)session.getAttribute("sessionSelectedProductGroups");
		String[] selectedProductGroups = selectedPrdGrp.split(",");
		// Empty the map field data every time we try to map the vendor and car
		// attributes
		session.setAttribute(VendorDataMappingHelper.SESSION_VENDOR_FLD_MAP_DATA_LIST, null);

		// Get the values from the session and store in form for persisting the
		// data mapped columns
		List<FieldMappingDataDTO> fldMappingDTOs = (List<FieldMappingDataDTO>) session
				.getAttribute(VendorDataMappingHelper.SESSION_VENDOR_FLD_MAPPED_DATA);
		// Getting the catalog information
		vendorCatalogDataMappingForm = vendorDataMappingHelper.getVendorCatalog(
				vendorCatalogDataMappingForm, vendorCatalogManager);
		Long vendorSuppliedId = vendorCatalogDataMappingForm.getMapVendorSuppliedFieldID();
		Long masterMappingID = vendorCatalogDataMappingForm.getMapMasterMappingId();
		Long blueMartiniMapId = vendorCatalogDataMappingForm.getMapBlueMartiniKey();
		String blueMartiniAttribute = vendorCatalogDataMappingForm.getMapBlueMartiniAttribute();
		CatalogMappedFieldDTO catalogFieldMapped = new CatalogMappedFieldDTO();

		String errString = "";
		if (log.isDebugEnabled()){
			log.debug("Vendor Supplied Field->" + vendorSuppliedId);
			log.debug("Master Attribute Id->" + masterMappingID);
			log.debug("Blue Martini Attribute Id->" + blueMartiniMapId);
		}
		/**
		 * validation for whether user has selected the Vendor Supplied Fields
		 * or CARS Attribute.
		 * */
		if (vendorSuppliedId == null) {
			errString = "Please select Vendor Supplied Field for mapping.";
		}
		if (masterMappingID == null && (blueMartiniMapId == null || blueMartiniMapId == 0)) {
			errString = "Please select CARS attribute for mapping.";
		}
		if (vendorSuppliedId == null && masterMappingID == null
				&& (blueMartiniMapId == null || blueMartiniMapId == 0)) {
			errString = "Please select Vendor Supplied Field and CARS attribute for mapping.";
		}
		if (StringUtils.isNotBlank(errString)) {
			saveError(request, getText(errString, request.getLocale()));
			// Setting the value back in the form to set it back in the session
			return new ModelAndView("vendorCatalog/datamapping/inc_mappingrules",
					"vendorCatalogDataMappingForm", vendorCatalogDataMappingForm);
		}
		// Holds the vendor mapped information
		Map<String, FieldMappingDataDTO> fldDataMapping = null;
		// Holds the corresponding cars data field values
		List<String> carsFldMappingValues = null;

		int mappedRowNum = 0;
		if (mappedFieldDTO != null && !mappedFieldDTO.isEmpty()) {
			mappedRowNum = mappedFieldDTO.size();
		} else {
			mappedFieldDTO = new ArrayList<CatalogMappedFieldDTO>();
		}
		// Getting the vendor header field
		CatalogHeaderDTO selVendorCatalogHeader = null;
		if (vendorSuppliedId != null) {
			selVendorCatalogHeader = vendorHeader.get(vendorSuppliedId);
			catalogFieldMapped.setRowNumber(mappedRowNum);
			catalogFieldMapped.setVendorSuppliedFieldID(vendorSuppliedId);
			catalogFieldMapped.setVendorSuppliedField(selVendorCatalogHeader
					.getVendorCatalogHeaderFieldName());
			catalogFieldMapped.setVendorSuppliedFieldNum(selVendorCatalogHeader
					.getVendorCatalogFieldNum());
			// Removing the value from the display map
			vendorHeader.remove(vendorSuppliedId);
		}
		// Getting the master attribute field
		if (masterMappingID != null && masterMappingID != -1) {
			CarAttributesDTO attributesDTO = new CarAttributesDTO();
			attributesDTO.setAttributeKey(masterMappingID);
			attributesDTO.setMasterAttribute("Y");
			CarAttributesDTO carAttributesDTO = sessionCarAttributes.get(sessionCarAttributes.indexOf(attributesDTO));
			catalogFieldMapped.setMasterMappingId(masterMappingID);
			catalogFieldMapped.setMappingAttribute(carAttributesDTO.getAttributeName());
			catalogFieldMapped.setMasterAttributeID(carAttributesDTO.getAttributeKey());
			catalogFieldMapped.setMasterRecord(true);
			// Remove the catalog master attribute from the session
			sessionCarAttributes.remove(attributesDTO);
		} else if (blueMartiniMapId != null) {
			CarAttributesDTO attributesDTO = new CarAttributesDTO();
			attributesDTO.setAttributeKey(blueMartiniMapId);
			attributesDTO.setMasterAttribute("N");
			CarAttributesDTO carAttributesDTO = sessionCarAttributes.get(sessionCarAttributes.indexOf(attributesDTO));
			catalogFieldMapped.setMasterMappingId(blueMartiniMapId);
			catalogFieldMapped.setMappingAttribute(carAttributesDTO.getAttributeName());
			catalogFieldMapped.setMasterAttributeID(carAttributesDTO.getAttributeKey());
			catalogFieldMapped.setMasterRecord(false);
			// Remove the catalog master attribute from the session
			sessionCarAttributes.remove(attributesDTO);

			/*
			 * Getting the field data mapping values. 1. First getting the
			 * Vendor supplied fields 2. Getting the matching car attribute
			 * values 3. Convert then into a map for ease of access. 4. Set the
			 * values back in session for accessing in the JSP PS : Performance
			 * can be definitely improved by getting only the relative values
			 * based on the first few field of vendor field value
			 */
			if (selVendorCatalogHeader != null) {
				/* 1. Getting the vendor supplied values. */
				List<String> vendorFldMappingValues = vendorCatalogManager
						.getVendorFldMappingDataValue(selVendorCatalogHeader.getVendorCatalogID(),
								selVendorCatalogHeader.getVendorCatalogFieldNum());
				// vendorFldMappingValues);
				/* 2. Getting the matching car attribute values */
				carsFldMappingValues = vendorCatalogManager
						.getCarFldMappingDatavalue(blueMartiniAttribute,selectedProductGroups, vendorCatalogDataMappingForm.getCatalogId());
				/* 3. Convert them into a map for ease of access. */
				// Since this is initializing the mapped value is false
				fldDataMapping = vendorDataMappingHelper.getFldDataMappingMap(
						vendorFldMappingValues, carsFldMappingValues, selVendorCatalogHeader
								.getVendorCatalogHeaderFieldName(), false,null); // added null and new parameter for the drop ship deployment 2012

			}

		}
		// Setting the value back in the form to set it back in the session
		vendorCatalogDataMappingForm.setSessionVendorCatalogHeader(vendorHeader);
		vendorCatalogDataMappingForm.setSessionCarAttributes(sessionCarAttributes);
		vendorCatalogDataMappingForm.setSessionCarFldDataMapping(carsFldMappingValues);
		/* 4. Set the values back in session for accessing in the JSP */
		vendorCatalogDataMappingForm.setSessionVendorFldDataMapping(fldDataMapping);
		// Setting the current value in the list for putting it back in session
		mappedFieldDTO.add(catalogFieldMapped);
		// Set the mapped data fld values in form to put it back in session
		// Collect them for saving finally
		if(log.isDebugEnabled()){
			log.debug("fldDataMapping: "+fldDataMapping);
			log.debug("carsFldMappingValues: "+carsFldMappingValues);
			log.debug("fldMappingDTOs: "+fldMappingDTOs);
			log.debug("mappedFieldDTO: "+mappedFieldDTO);
		}
		vendorCatalogDataMappingForm.setFldMappingDataDTOs(fldMappingDTOs);
		vendorCatalogDataMappingForm.setCatalogFieldMappingList(mappedFieldDTO);
		session = vendorDataMappingHelper.setSessionValues(session, vendorCatalogDataMappingForm);
		return new ModelAndView("vendorCatalog/datamapping/inc_mappingrules",
				"vendorCatalogDataMappingForm", vendorCatalogDataMappingForm);
	}

	/**
	 * Saves the template values that are currently in the session.
	 * 
	 * @param request
	 * @param response
	 * @param vendorCatalogDataMappingForm
	 * @return
	 */
	private VendorCatalogDataMappingForm saveCatalogTemplate(HttpServletRequest request,
			HttpServletResponse response,
			VendorCatalogDataMappingForm vendorCatalogDataMappingForm, boolean isComplete) {
		if(log.isDebugEnabled()){
			log.debug("Save Catalog Templage isComplete:"+isComplete);
		}
		HttpSession session = request.getSession();
		List<CatalogMappedFieldDTO> catalogMappedFieldDTO = (List<CatalogMappedFieldDTO>) session.getAttribute(VendorDataMappingHelper.SESSION_CATALOG_MAPPED_FIELD);
		
		if(log.isDebugEnabled()){
			log.debug(" catalogMappedFieldDTO: "+catalogMappedFieldDTO);
		}
		
		List<FieldMappingDataDTO> fldMappingDTOs = (List<FieldMappingDataDTO>) session.getAttribute(VendorDataMappingHelper.SESSION_VENDOR_FLD_MAPPED_DATA);
		if(log.isDebugEnabled()){
			log.debug(" fldMappingDTOs: "+fldMappingDTOs);
		}
		
		vendorCatalogDataMappingForm.setCatalogFieldMappingList(catalogMappedFieldDTO);
		String selectedProductGroups = vendorCatalogDataMappingForm.getSelectedProductGroups();
		vendorCatalogDataMappingForm.setProductGroupList(productGroupManager.getAllActiveProductGroups());

		if (StringUtils.isBlank(selectedProductGroups)) {
			// Return back saying the product groups need to be selected.
			String errString = "Please select a product Group";
			vendorCatalogDataMappingForm.setErrorString(errString);
			vendorCatalogDataMappingForm.setCatalogId(Long.valueOf((String) session.getAttribute("CATALOG_ID")));
			// Setting the value back in the form to set it back in the session
			return vendorCatalogDataMappingForm;

		}

		VendorCatalog vendorCatalog = vendorCatalogManager.getVendorCatalog(Long.valueOf((String) session.getAttribute("CATALOG_ID")));
		List<ProductGroup> productGroups = productGroupManager.getProductGroups(selectedProductGroups);
		Long vendorCatalogTemplateID = vendorCatalogDataMappingForm.getVendorCatalogTmplID();
		if (session.getAttribute("CATALOG_TEMPLATE_ID") != null) {
			vendorCatalogTemplateID = (Long) session.getAttribute("CATALOG_TEMPLATE_ID");
		}
		String vendorCatalogTemplateName = vendorCatalogDataMappingForm.getVendorCatalogTemplateName();

		if (StringUtils.isBlank(vendorCatalogTemplateName) && vendorCatalog != null) {
			vendorCatalogTemplateName = vendorCatalog.getVendorCatalogName();
		}
		boolean isGlobalTemplate = vendorCatalogDataMappingForm.isGlbTemplate();
		VendorCatalogTemplate vendorCatalogTemplate = null;
		if (vendorCatalogTemplateID != null && vendorCatalog != null 
				&& (vendorCatalog.getVendorCatalogTemplate() != null && vendorCatalog.getVendorCatalogTemplate().getVendorCatalogTmplID() > 0)) {
			if (!isGlobalTemplate) {
				vendorCatalogTemplate = vendorCatalogManager
					.getVendorCatalogTemplate(vendorCatalogTemplateName);
				if (vendorCatalogTemplate == null) {
					vendorCatalogTemplate = new VendorCatalogTemplate();
				} 
			} else {
				vendorCatalogTemplate = vendorCatalogManager
					.getVendorCatalogTemplate(vendorCatalogTemplateID);
			}
		} else {
			vendorCatalogTemplate = new VendorCatalogTemplate();
		}
		if (vendorCatalogTemplate.getVendorCatalogTmplID() > 0) {
			getVendorCatalogManager().removeMappingAttribute(vendorCatalogTemplate);
		}
		vendorCatalogTemplate.setVendorCatalogName(vendorCatalogTemplateName);
		vendorCatalogTemplate.setGlobal(isGlobalTemplate);
		setAuditInfo(request, vendorCatalogTemplate);
		try{
			vendorCatalogTemplate = vendorCatalogManager.saveTemplate(vendorCatalogTemplate);
		}catch(DataAccessException dae){
			log.error("Data acess Exception occured:"+ dae);
			String errString = FIELD_MAPPING_ERROR;
			vendorCatalogDataMappingForm.setErrorString(errString);
			request.getSession().setAttribute("edit", true);
			vendorCatalogDataMappingForm.setVendorCatalog(vendorCatalog);
			return vendorCatalogDataMappingForm;
		}
		vendorCatalogDataMappingForm.setVendorCatalogTmplID(vendorCatalogTemplate.getVendorCatalogTmplID());
		List<CatalogGroupTemplate> lstCatalogGroupTemplate = new ArrayList<CatalogGroupTemplate>();
		CatalogGroupTemplate catalogGroupTemplate = null;
		CompositeKeyForCatalogGroupTemplate compositeKeyForCatalogGroupID = null;
		for (ProductGroup productGroup : productGroups) {
			catalogGroupTemplate = new CatalogGroupTemplate();
			compositeKeyForCatalogGroupID = new CompositeKeyForCatalogGroupTemplate();
			compositeKeyForCatalogGroupID.setProductGroupID(productGroup.getProductGroupId());
			compositeKeyForCatalogGroupID.setVendorCatalogTmplID(vendorCatalogTemplate
					.getVendorCatalogTmplID());
			catalogGroupTemplate.setCompositeKeyForCatalogGroupID(compositeKeyForCatalogGroupID);
			catalogGroupTemplate.setStatusCD(DropShipConstants.ACTIVE);
			setAuditInfo(request, catalogGroupTemplate);
			catalogGroupTemplate = vendorCatalogManager.saveGroupTemplate(catalogGroupTemplate);
			lstCatalogGroupTemplate.add(catalogGroupTemplate);
		}
		// Loops through the catalogMappedFieldDTO in session and gets the
		// values for save
		vendorCatalogDataMappingForm = vendorDataMappingHelper.getVendorCatalogFieldMapping(vendorCatalogDataMappingForm, getVendorCatalogManager());
		
		List<VendorCatalogFieldMapping> lstVendorCatalogFieldMapping = vendorCatalogDataMappingForm.getLstVendorCatalogFieldMapping();
		if (lstVendorCatalogFieldMapping != null && !lstVendorCatalogFieldMapping.isEmpty()) {
			if(log.isDebugEnabled()){
				log.debug("list of vendor cataog field mapping for save: "+lstVendorCatalogFieldMapping);
			}
			lstVendorCatalogFieldMapping = vendorCatalogManager.saveVendorCatalogFieldMapping(lstVendorCatalogFieldMapping);
		}
		List<MasterAttributeMapping> lstMasterAttributeMapping = vendorCatalogDataMappingForm.getLstMasterAttributeMapping();
		
		if(log.isDebugEnabled()){
			log.debug("list of master attribute mapping for save: "+lstMasterAttributeMapping);
		}
		
		if(null != vendorCatalog.getVendorCatalogTemplate()) {
			lstMasterAttributeMapping = vendorCatalogManager.saveMasterMapping(lstMasterAttributeMapping, vendorCatalog.getVendorCatalogTemplate().getVendorCatalogTmplID());
		} else {
			lstMasterAttributeMapping = vendorCatalogManager.saveMasterMapping(lstMasterAttributeMapping, null);
		}
		/* Saving the field data mapping values */
		if (fldMappingDTOs != null && !fldMappingDTOs.isEmpty()) {
			VendorCatalogFieldDataMapping vendorCatalogFieldDataMapping = null;
			CompositeKeyForDataFldMapping compositeKey = null;
			for (FieldMappingDataDTO fldMappedDTO : fldMappingDTOs) {
				compositeKey = new CompositeKeyForDataFldMapping(vendorCatalogTemplate
						.getVendorCatalogTmplID(), fldMappedDTO.getVendorHeaderField(),
						fldMappedDTO.getVendorField());
				
				vendorCatalogFieldDataMapping = this.vendorCatalogManager.getMappingObjectById(compositeKey);
				if(null == vendorCatalogFieldDataMapping) {
					vendorCatalogFieldDataMapping = new VendorCatalogFieldDataMapping();
					vendorCatalogFieldDataMapping.setCompositeKey(compositeKey);
				}
				setAuditInfo(request, vendorCatalogFieldDataMapping);
				// Get the bluemartini attribute name
				List<AttributeLookupValue> attributeLookUpValues = vendorCatalogManager
						.getAttributeLookUpValues(fldMappedDTO.getCarValue());
				if (attributeLookUpValues != null && attributeLookUpValues.size() > 0) {
					vendorCatalogFieldDataMapping.setBlkAttrLookupValue(attributeLookUpValues
							.get(0));
				}
				vendorCatalogManager.saveDataFieldMapping(vendorCatalogFieldDataMapping);
			}
			session.setAttribute(VendorDataMappingHelper.SESSION_VENDOR_FLD_MAP_DATA_LIST, null);
			session.setAttribute(VendorDataMappingHelper.SESSION_CARS_FLD_DATA_LIST, null);
		}

		if (vendorCatalogTemplate != null) {
			vendorCatalog.setVendorCatalogTemplate(vendorCatalogTemplate);
			// Setting this here to use it in helper class
			vendorCatalogDataMappingForm.setVendorCatalogTmplID(vendorCatalogTemplate
					.getVendorCatalogTmplID());
			setAuditInfo(request, vendorCatalog);

			if (isComplete) {
				vendorCatalog.setStatusCD(DropShipConstants.CATALOG_STATUS_TRANSLATING);
			}
			vendorCatalog = vendorCatalogManager.save(vendorCatalog);
			vendorCatalogDataMappingForm.setVendorCatalog(vendorCatalog);
		}
		if (lstCatalogGroupTemplate != null) {
			String[] selectedProducts = new String[lstCatalogGroupTemplate.size()];
			int iCount = 0;
			for (CatalogGroupTemplate catalogGroup : lstCatalogGroupTemplate) {
				selectedProducts[iCount++] = ""
						+ catalogGroup.getCompositeKeyForCatalogGroupID().getProductGroupID();
			}
			vendorCatalogDataMappingForm.setProductGroupSelected(selectedProducts);
		}
		VendorCatalogDataMappingForm retVendorCatalogDataMappingForm = vendorDataMappingHelper
				.getCarAttributes(vendorCatalogDataMappingForm, productGroupManager,
						vendorCatalogManager);
		return retVendorCatalogDataMappingForm;
	}

	/**
	 * This is an Ajax form submit this deals only with the field data mapping.
	 * 1. Get the field data map value as well as the selected value from the
	 * session. <br>
	 * 2. If the session value is empty then return to the car attribute <br>
	 * 3. Get the vendor and car fld data value <br>
	 * 4. Get the object from the map based on the vendor value. <br>
	 * 5. Set the mapped boolean to true and set the mapped String value in the
	 * object and set the object the map <br>
	 * 6. Set the map back in the session <br>
	 * 7. Setting the values back in the associated Session for displaying and
	 * saving.
	 * 
	 * @param request
	 * @param response
	 * @param vendorCatalogDataMappingForm
	 * @return
	 */
	public ModelAndView mapFieldData(HttpServletRequest request, HttpServletResponse response,
			VendorCatalogDataMappingForm vendorCatalogDataMappingForm) {

		HttpSession session = request.getSession();
		//Check whether the user session is timed out.
		if(null == session.getAttribute(VendorDataMappingHelper.SESSION_VENDOR_FLD_MAP_DATA_LIST) 
				|| null == session.getAttribute(VendorDataMappingHelper.SESSION_VENDOR_FLD_MAPPED_DATA)
				|| null == session.getAttribute(VendorDataMappingHelper.SESSION_CATALOG_MAPPED_FIELD)){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		/* 1. Get the field data map value in the session. */
		Map<String, FieldMappingDataDTO> fieldMappingDTO = (Map<String, FieldMappingDataDTO>) session
				.getAttribute(VendorDataMappingHelper.SESSION_VENDOR_FLD_MAP_DATA_LIST);
		List<FieldMappingDataDTO> lstFldDataDTOs = (List<FieldMappingDataDTO>) session
				.getAttribute(VendorDataMappingHelper.SESSION_VENDOR_FLD_MAPPED_DATA);

		List<CatalogMappedFieldDTO> catalogMappedFieldDTOs = (List<CatalogMappedFieldDTO>) session
				.getAttribute(VendorDataMappingHelper.SESSION_CATALOG_MAPPED_FIELD);
		
		if (fieldMappingDTO != null && fieldMappingDTO.size() < 1) {
			/* 2. If the session value is empty then return to the car attribute */
			return mapAttributes(request, response, vendorCatalogDataMappingForm);
		}
		/* 3. Get the vendor and car fld data value */
		String venFldMapValue = vendorCatalogDataMappingForm.getMapVenFldDataMapping();
		List<String> carFldMapValue = vendorCatalogDataMappingForm.getMapCarFldDataMapping();
		// Get the header value for comparision
		String venHeaderData = vendorCatalogDataMappingForm.getMapVenHeaderData();
		if (log.isDebugEnabled()){
			log.debug("venFldMapValue--" + venFldMapValue);
			log.debug("carFldMapValue--" + carFldMapValue);
		}
		if (!StringUtils.isBlank(venFldMapValue) && carFldMapValue != null && carFldMapValue.size() > 0) {
			/* 4. Get the object from the map based on the vendor value. */
			FieldMappingDataDTO fieldMappedDataDTO = fieldMappingDTO.get(venFldMapValue);
			if (fieldMappedDataDTO != null && catalogMappedFieldDTOs != null) {
				/*
				 * 5. Set the mapped boolean to true and set the mapped String
				 * value in the object and set the object the map
				 */
				fieldMappedDataDTO.setCarValue(carFldMapValue.get(Integer.parseInt(request.getParameter("count"))));
				fieldMappedDataDTO.setMapped(true);
				fieldMappingDTO.put(venFldMapValue, fieldMappedDataDTO);
				if (lstFldDataDTOs == null || (lstFldDataDTOs != null && lstFldDataDTOs.size() < 1)) {
					lstFldDataDTOs = new ArrayList<FieldMappingDataDTO>();
				}
				lstFldDataDTOs.add(fieldMappedDataDTO);

				/* 6. Set the map back in the session */
				session.setAttribute(VendorDataMappingHelper.SESSION_VENDOR_FLD_MAP_DATA_LIST,
						fieldMappingDTO);
				session.setAttribute(VendorDataMappingHelper.SESSION_VENDOR_FLD_MAPPED_DATA,
						lstFldDataDTOs);

				// Get the last element in the array that was the one mapped now
				int position = catalogMappedFieldDTOs.size() - 1;
				CatalogMappedFieldDTO catalogMappedFieldDTO = catalogMappedFieldDTOs.get(position);
				if (!venHeaderData.equals(catalogMappedFieldDTO.getVendorSuppliedField())) {
					catalogMappedFieldDTO = null;
					position = -1;
					// If the last element is not the element then loop through
					// and get the element
					for (CatalogMappedFieldDTO tmpMappedFieldDTO : catalogMappedFieldDTOs) {
						if (venHeaderData.equals(tmpMappedFieldDTO.getVendorSuppliedField())) {
							catalogMappedFieldDTO = tmpMappedFieldDTO;
							position++;
							break;
						}
						position++;
					}
				}
				if (catalogMappedFieldDTO != null && position > -1) {
					catalogMappedFieldDTO.setFieldMappingDTO(fieldMappingDTO);
					catalogMappedFieldDTO.setLstFldDataDTOs(lstFldDataDTOs);
					catalogMappedFieldDTO.setFromDatabase(false);
					catalogMappedFieldDTOs.set(position, catalogMappedFieldDTO);
					// Setting the value back in the session
					session.setAttribute(VendorDataMappingHelper.SESSION_CATALOG_MAPPED_FIELD,
							catalogMappedFieldDTOs);
				}
			}
		}
		return new ModelAndView("vendorCatalog/datamapping/inc_mappingFldrules",
				"vendorCatalogDataMappingForm", vendorCatalogDataMappingForm);

	}

	/**
	 * Gets the selected row number and sets the fld data mapping back in the
	 * session getFldDataMappingAsMap 1. Get the row number that was selected in
	 * the JSP <br>
	 * 2. Checks for value greater than or equal to 0 <br>
	 * 3. Get the mapped values from the session <br>
	 * 4. Get the field data mapped value of the selected Row <br>
	 * 5. If the value is null then set an empty value in session. <br>
	 * 6. Check if the value is coming from database. If from database execute
	 * the query. <br>
	 * 8. if coming from database, switch the database value in catalog mapped
	 * field to false <br>
	 * 9. If not from Database then pick the value from the session
	 * 
	 * @param request
	 * @param response
	 * @param vendorCatalogDataMappingForm
	 * @return
	 */
	public ModelAndView selectMappedData(HttpServletRequest request, HttpServletResponse response,
			VendorCatalogDataMappingForm vendorCatalogDataMappingForm) {

		/* 1. Get the row number that was selected in the JSP */
		int selectedRowNum = vendorCatalogDataMappingForm.getMapRowNumber();
		HttpSession session = request.getSession();
		//Check whether the user session is timed out.
		if(null == session.getAttribute(VendorDataMappingHelper.SESSION_CATALOG_MAPPED_FIELD)){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		Map<String, FieldMappingDataDTO> sessionFieldMappedDataDTOs = null;
		List<String> sessionLstFieldMappingDTO = null;
		Method methodVendorCatalogHeaderId = null;
		try {
			methodVendorCatalogHeaderId = CatalogHeaderDTO.class
					.getDeclaredMethod("getVendorCatalogHeaderId");
		} catch (SecurityException e) {
			log.error("Security Exception ", e);
		} catch (NoSuchMethodException e) {
			log.error("NoSuchMethodException ", e);
		}
		/* 2. Checks for value greater than or equal to 0 */
		if (selectedRowNum > 0) {
			// Gets the position of the object
			selectedRowNum = selectedRowNum - 1;
			/* 3. Get the mapped values from the session */
			List<CatalogMappedFieldDTO> catalogMappedFieldDTOs = (List<CatalogMappedFieldDTO>) session
					.getAttribute(VendorDataMappingHelper.SESSION_CATALOG_MAPPED_FIELD);
			
			/* 4. Get the value of the selected Row value */
			CatalogMappedFieldDTO catalogMappedFieldDTO = catalogMappedFieldDTOs
					.get(selectedRowNum);
			
			if (catalogMappedFieldDTO != null && !catalogMappedFieldDTO.isMasterRecord()) {
				Map<String, FieldMappingDataDTO> fieldMappedDataDTOs = null;
				/* 6. Check if the value is coming from database. */
				if (catalogMappedFieldDTO.isFromDatabase()) {
					//Get the selected product groups
					String selectedPrdGrp = (String)session.getAttribute("sessionSelectedProductGroups");
					if (log.isDebugEnabled()){
						log.debug("New String==>"+selectedPrdGrp);
					}
					String[] selectedProductGroups = selectedPrdGrp.split(",");
					VendorCatalog vCatalog = vendorCatalogManager.getCatalogInfo(Long.valueOf((String) session.getAttribute("CATALOG_ID")));
					List<VendorCatalogHeader> vendorCatalogHeaders = vCatalog.getVendorCatalogHeaderList();
					List<CatalogHeaderDTO> catalogHeaderDTOs = vendorDataMappingHelper.getAsCatalogHeaderDTO(vendorCatalogHeaders);
					Map<Long, CatalogHeaderDTO> vendorCatalogHeaderListMap = (Map<Long, CatalogHeaderDTO>) vendorDataMappingHelper.getAsMap(
							catalogHeaderDTOs, methodVendorCatalogHeaderId);
					CatalogHeaderDTO selVendorCatalogHeader = vendorCatalogHeaderListMap.get(catalogMappedFieldDTO.getVendorSuppliedFieldID());
					List<String> vendorFldMappingValues = vendorCatalogManager.getVendorFldMappingDataValue(selVendorCatalogHeader.getVendorCatalogID(), selVendorCatalogHeader.getVendorCatalogFieldNum());
					List<String> carsFldMappingValues = vendorCatalogManager.getCarFldMappingDatavalue(catalogMappedFieldDTO.getMappingAttribute(),selectedProductGroups, vendorCatalogDataMappingForm.getCatalogId());
					if (vendorCatalogDataMappingForm.getVendorCatalogTmplID() == null && session.getAttribute("CATALOG_TEMPLATE_ID") != null) {
						vendorCatalogDataMappingForm.setVendorCatalogTmplID((Long) session.getAttribute("CATALOG_TEMPLATE_ID"));
					}
					
					// afunxy1: added code to fetch the saved mapping data value for drop ship deployment 2012
					List<FieldMappingDataDTO> savedFieldMappingData=null;
					if(vendorCatalogDataMappingForm.getCatalogId()!=null && selVendorCatalogHeader.getVendorCatalogHeaderFieldName() !=null){
						savedFieldMappingData=vendorCatalogManager.getSavedFieldMappingData(vendorCatalogDataMappingForm.getCatalogId(), selVendorCatalogHeader.getVendorCatalogHeaderFieldName());
					}
					
					fieldMappedDataDTOs = vendorDataMappingHelper.getFldDataMappingMap(vendorFldMappingValues, carsFldMappingValues, selVendorCatalogHeader
							.getVendorCatalogHeaderFieldName(), false,savedFieldMappingData);
					catalogMappedFieldDTO.setFromDatabase(true);
					catalogMappedFieldDTO.setFieldMappingDTO(fieldMappedDataDTOs);
					catalogMappedFieldDTOs.set(selectedRowNum, catalogMappedFieldDTO);
					// Set this value back in the session.
					session.setAttribute(VendorDataMappingHelper.SESSION_CATALOG_MAPPED_FIELD,
							catalogMappedFieldDTOs);
				} else {
					/* 7. If not pick the value from the session */
					fieldMappedDataDTOs = catalogMappedFieldDTO.getFieldMappingDTO();
					List<String> list = new ArrayList<String> ();
					if (fieldMappedDataDTOs != null) {
						Iterator<String> iterator = fieldMappedDataDTOs.keySet().iterator();
						while (iterator.hasNext()) {
							String string = iterator.next();
							FieldMappingDataDTO dataDTO = fieldMappedDataDTOs.get(string);
							if (dataDTO != null) {
								if (dataDTO.getCarValue() == null)  {
									list.add("");
								} else {
									list.add(dataDTO.getCarValue());
								}
							}
						}
						vendorCatalogDataMappingForm.setMapCarFldDataMapping(list);
					}
				}
				if (fieldMappedDataDTOs != null && !fieldMappedDataDTOs.isEmpty()) {
					sessionFieldMappedDataDTOs = fieldMappedDataDTOs;
					// Set the sessionCarFieldValue to non empty to display
					// in JSP
					sessionLstFieldMappingDTO = Arrays.asList("" + selectedRowNum);
				}

			}
		}
		// If it is null or has value set it back in the session
		session.setAttribute(VendorDataMappingHelper.SESSION_VENDOR_FLD_MAP_DATA_LIST,
				sessionFieldMappedDataDTOs);
		session.setAttribute(VendorDataMappingHelper.SESSION_CARS_FLD_DATA_LIST,
				sessionLstFieldMappingDTO);

		return new ModelAndView("vendorCatalog/datamapping/inc_mappingFldrules",
				"vendorCatalogDataMappingForm", vendorCatalogDataMappingForm);

	}

	/**
	 * This Method saves the error message in to session
	 */
	public void saveError(HttpServletRequest request, String error) {
		List<String> errors = new ArrayList<String>();
		errors.add(error);
		request.setAttribute("errors", errors);
	}

	public ModelAndView unMapAttributes(HttpServletRequest request, HttpServletResponse response,
			VendorCatalogDataMappingForm vendorCatalogDataMappingForm) {
		if(log.isDebugEnabled()){
			log.debug("unMapAttributes");
		}
		/* 1. Get the row number that was selected in the JSP */
		int selectedRowNum = vendorCatalogDataMappingForm.getMapRowNumber();
		if(log.isDebugEnabled()){
			log.debug("selectedRowNum :"+selectedRowNum);
		}
		HttpSession session = request.getSession();
		//Check whether the user session is timed out.
		if(session.getAttribute(VendorDataMappingHelper.SESSION_CATALOG_MAPPED_FIELD) == null ){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		// Empty the map field data every time we try to map the vendor and car
		// attributes
		session.setAttribute(VendorDataMappingHelper.SESSION_VENDOR_FLD_MAP_DATA_LIST, null);
		/* 2. Checks for value greater than or equal to 0 */
		if (selectedRowNum > 0) {
			// Gets the position of the object
			selectedRowNum = selectedRowNum - 1;
			/* 3. Get the mapped values from the session */
			List<CatalogMappedFieldDTO> catalogMappedFieldDTOs = (List<CatalogMappedFieldDTO>) session
					.getAttribute(VendorDataMappingHelper.SESSION_CATALOG_MAPPED_FIELD);
			if(log.isDebugEnabled()){
				log.debug("catalogMappedFieldDTOs list :"+catalogMappedFieldDTOs);
			}
						
			List<FieldMappingDataDTO> fieldMappingDTOs = (List<FieldMappingDataDTO>) session
			.getAttribute(VendorDataMappingHelper.SESSION_VENDOR_FLD_MAPPED_DATA);
			if(log.isDebugEnabled()){
				log.debug("fieldMappingDTOs list :"+fieldMappingDTOs);
			}
			
					
			/* 4. Get the value of the selected Row value */
			CatalogMappedFieldDTO catalogMappedFieldDTO = catalogMappedFieldDTOs.get(selectedRowNum);
			if(log.isDebugEnabled()){
				log.debug("catalogMappedFieldDTO  :"+catalogMappedFieldDTO.toString() );
			}
			
			FieldMappingDataDTO dataDTO = new FieldMappingDataDTO();
			if(log.isDebugEnabled()){
				log.debug("catalogMappedFieldDTO  vendor supplied field :"+catalogMappedFieldDTO.getVendorSuppliedField() );
			}
			dataDTO.setVendorHeaderField(catalogMappedFieldDTO.getVendorSuppliedField());
			if (catalogMappedFieldDTO != null) {
				Map<Long, CatalogHeaderDTO> vendorHeader = (Map<Long, CatalogHeaderDTO>) session.getAttribute(VendorDataMappingHelper.SESSION_VENDOR_HEADER_LIST);
				
				List<CarAttributesDTO> sessionCarAttributes = (List<CarAttributesDTO>) session.getAttribute(VendorDataMappingHelper.SESSION_CAR_ATTRIBUTE);
				
				if ((vendorHeader == null || (vendorHeader != null && vendorHeader.size() < 1))
						|| (sessionCarAttributes == null || (sessionCarAttributes != null && sessionCarAttributes.size() < 1))) {
					return getCarAttributes(request, response, vendorCatalogDataMappingForm);
				}
				// Getting the vendor header field
				CatalogHeaderDTO vendorCatalogHeader = new CatalogHeaderDTO();
				vendorCatalogHeader.setVendorCatalogFieldNum(catalogMappedFieldDTO.getVendorSuppliedFieldNum());
				vendorCatalogHeader.setVendorCatalogHeaderFieldName(catalogMappedFieldDTO.getVendorSuppliedField());
				vendorCatalogHeader.setVendorCatalogHeaderId(catalogMappedFieldDTO.getVendorSuppliedFieldID());
				
				vendorCatalogHeader.setVendorCatalogID(Long.valueOf((String) session.getAttribute("CATALOG_ID")));
				if(log.isDebugEnabled()){
					log.debug("CatalogHeaderDTO:"+vendorCatalogHeader);
				}
				
				vendorHeader.put(vendorCatalogHeader.getVendorCatalogHeaderId(),vendorCatalogHeader);
				
				session.setAttribute(VendorDataMappingHelper.SESSION_VENDOR_HEADER_LIST, vendorHeader);

				if (catalogMappedFieldDTO.isMasterRecord()) {
					CarAttributesDTO attributesDTO = new CarAttributesDTO();
					attributesDTO.setAttributeKey(catalogMappedFieldDTO.getMasterAttributeID());
					attributesDTO.setAttributeName(catalogMappedFieldDTO.getMappingAttribute());
					attributesDTO.setMasterAttribute("Y");
					sessionCarAttributes.add(attributesDTO);
				} else {
					CarAttributesDTO attributesDTO = new CarAttributesDTO();
					attributesDTO.setAttributeKey(catalogMappedFieldDTO.getMasterAttributeID());
					attributesDTO.setAttributeName(catalogMappedFieldDTO.getMappingAttribute());
					attributesDTO.setMasterAttribute("N");
					sessionCarAttributes.add(attributesDTO);
				}
				Collections.sort(sessionCarAttributes);
				session.setAttribute(VendorDataMappingHelper.SESSION_CAR_ATTRIBUTE,	sessionCarAttributes);
				// Remove the mapped value from the session
				catalogMappedFieldDTOs.remove(catalogMappedFieldDTO);
				if(log.isDebugEnabled()){
					log.debug("final catalogMappedFieldDTOs:"+catalogMappedFieldDTOs);
				}
				
				session.setAttribute(VendorDataMappingHelper.SESSION_CATALOG_MAPPED_FIELD,catalogMappedFieldDTOs);
				
				if(log.isDebugEnabled()){
					log.debug("before fieldMappingDTOs:"+fieldMappingDTOs);
				}
				
				while(fieldMappingDTOs.indexOf(dataDTO) >= 0) {
					fieldMappingDTOs.remove(dataDTO);
				}
				if(log.isDebugEnabled()){
					log.debug("remvoing dataDTO :"+dataDTO);
					log.debug("after fieldMappingDTOs:"+fieldMappingDTOs);
				}
				
				session.setAttribute(VendorDataMappingHelper.SESSION_VENDOR_FLD_MAPPED_DATA, fieldMappingDTOs);
			}
		} else {
			saveError(request, getText("Please select mapped Field.", request.getLocale()));
		}
		return new ModelAndView("vendorCatalog/datamapping/inc_mappingrules",
				"vendorCatalogDataMappingForm", vendorCatalogDataMappingForm);
	}

	/**
	 * gets the template that is being selected for this catalog
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView changeTemplates(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView("redirect:/mainMenu.html");
		VendorCatalogDataMappingForm requestDataMapForm = new VendorCatalogDataMappingForm();
		String catalogID = request.getParameter("catalogIDVal");
		String selectedTemplate = request.getParameter("selectedTemplate");
		String selectedValue = request.getParameter("selectedValue");
		String newTemplate = "0";
		if (StringUtils.isNotBlank(catalogID)) {
			if (log.isDebugEnabled()){
				log.debug("GetVendorCatalog catalogID: " + catalogID);
			}
			if (catalogID != null && catalogID.length() > 0) {

				requestDataMapForm.setCatalogId(Long.parseLong(catalogID));
				if (VendorDataMappingHelper.CREATE_NEW_TEMPLATE.equals(selectedTemplate)) {
					// Set the template value to -1 so new mapping values can
					// begin.
					selectedTemplate = newTemplate;
				} else if (VendorDataMappingHelper.EXISTING_TEMPLATE_FOR_VENDOR
						.equals(selectedValue)) {
					requestDataMapForm.setsExistTemplateForVendor(selectedValue);
				} else if (VendorDataMappingHelper.GLOBAL_TEMPLATES.equals(selectedValue)) {
					requestDataMapForm.setsGlobalTemplate(selectedValue);
				} else if (VendorDataMappingHelper.TEMPLATE_FROM_ANOTHER_VENDOR
						.equals(selectedValue)) {
					requestDataMapForm.setsAnotherVendorTemplate(selectedValue);
				} else if (VendorDataMappingHelper.TEMPLATE_SEL_FROM_ANOTHER_VENDOR
						.equals(selectedValue)) {
					requestDataMapForm.setTemplateFrmAnotherVendor(selectedValue);
				}
				if (selectedTemplate != null && selectedTemplate.length() > 0) {
					requestDataMapForm.setSelectedTemplateID(Long.parseLong(selectedTemplate));
				}

				requestDataMapForm = vendorDataMappingHelper.getVendorCatalog(requestDataMapForm,
						productGroupManager, vendorCatalogManager, false);
				if (requestDataMapForm.getVendorCatalogTmplID() != null) {
					// A templte is already assigned to this catalog so getting
					// values for it.
					requestDataMapForm = vendorDataMappingHelper.getTemplateCarAttributes(
							requestDataMapForm, productGroupManager, vendorCatalogManager);
					requestDataMapForm = vendorDataMappingHelper
							.getSessionCarAttributes(requestDataMapForm);
					request.getSession().setAttribute("CATALOG_TEMPLATE_ID", requestDataMapForm.getVendorCatalogTmplID());

				}
				requestDataMapForm = vendorDataMappingHelper.setLoadValues(requestDataMapForm);
				
				if(StringUtils.isNotBlank(request.getParameter("mapOption"))){
					String mapOption = request.getParameter("mapOption");
					if(mapOption.equalsIgnoreCase("NEW")){
						if(log.isDebugEnabled()){
							log.debug("User selected the Create New Data Map Option");
						}
						// Sets the session attribute to null;
						HttpSession session = vendorDataMappingHelper.setSessionValues(request.getSession(),
								requestDataMapForm);
						if(null == session.getAttribute("vendorCatalogInfo")) {
							vendorDataMappingHelper.populateVendorCatalogInfoInSession(
									session,
									vendorCatalogManager, catalogID);
						}
					}
				}
				modelAndView = new ModelAndView("vendorCatalog/datamapping/inc_mappingBody",
						"vendorCatalogDataMappingForm", requestDataMapForm);
			}
		}
		// Sets the session attribute to null;
		HttpSession session = vendorDataMappingHelper.setSessionValues(request.getSession(),
				requestDataMapForm);
		return modelAndView;
	}

	/**
	 * Saves and completes the catalog information related to catalog template.
	 * 
	 * @param request
	 * @param response
	 * @param vendorCatalogDataMapForm
	 * @return
	 */
	public ModelAndView saveCompleteTemplate(HttpServletRequest request,
			HttpServletResponse response, VendorCatalogDataMappingForm vendorCatalogDataMappingForm) {
		vendorCatalogDataMappingForm = saveCatalogTemplate(request, response,
				vendorCatalogDataMappingForm, true);
		ModelAndView modelAndView = new ModelAndView(getSuccessView(),
				"vendorCatalogDataMappingForm", vendorCatalogDataMappingForm);
		return checkErrorValues(request, response, vendorCatalogDataMappingForm, modelAndView, SAVE_COMPLETE_TEMPLATE);
	}

	/**
	 * Returns all the templates for the vendor selected
	 * 
	 * @param request
	 * @param response
	 * @param vendorCatalogDataMappingForm
	 * @return
	 */
	public ModelAndView getOtherVendorTemplates(HttpServletRequest request,
			HttpServletResponse response, VendorCatalogDataMappingForm vendorCatalogDataMappingForm) {
		String selVendorID = request.getParameter("selVendorID");
		if (StringUtils.isNotBlank(selVendorID)) {
			Long vendorID = Long.parseLong(selVendorID);
			List<VendorCatalogTemplate> tmpVendorCatalogTemplate = vendorCatalogManager
					.getCatlogTemplateForVendor(vendorID);
			vendorCatalogDataMappingForm.setTemplatesForOtherVendor(tmpVendorCatalogTemplate);
		}
		return new ModelAndView("vendorCatalog/datamapping/inc_OtherVendors",
				"vendorCatalogDataMappingForm", vendorCatalogDataMappingForm);

	}

	/**
	 * Checks if there are error values in the form. If there are errors then
	 * puts them in the request and removes it from the form.
	 * 
	 * If there are no error returns the passed model and view value.
	 * 
	 * @param request
	 * @param response
	 * @param vendorCatalogDataMappingForm
	 * @param modelAndView
	 * @param action 
	 * @return
	 */
	private ModelAndView checkErrorValues(HttpServletRequest request, HttpServletResponse response,
			VendorCatalogDataMappingForm vendorCatalogDataMappingForm, ModelAndView modelAndView, String action) {
		String errorValue = vendorCatalogDataMappingForm.getErrorString();
		if (!StringUtils.isBlank(errorValue)) {
			if (log.isDebugEnabled()){
				log.debug("Error is not blank. There might be some errors.");
			}
			saveError(request, errorValue);
			vendorCatalogDataMappingForm.setErrorString(StringUtils.EMPTY);
			try {
				request.setAttribute("cid", vendorCatalogDataMappingForm.getCatalogId());
				return getVendorCatalog(request, response);
			} catch (Exception e) {
				log.error("Error in saveCompleteTemplate when trying to throw an error message");
			}
		}else {
			if (log.isDebugEnabled()){
				log.debug("There are no errors found.");
			}
			if(action.equals(SAVE_COMPLETE_TEMPLATE)){
				log.debug("Completing ##");
				errorValue=SAVED_AND_COMPLETED;
			}else if(action.equals(SAVE_TEMPLATE)){
				log.debug("Savinging ##");
				errorValue=TEMPALTE_SAVED;
			}else{
				log.debug("else ##");
				errorValue=ACTION_REQUEST_SAVED;
			}
			saveError(request, errorValue);
		}
		//Check for scrolling
		if(StringUtils.isNotBlank(request.getParameter("scrollPos"))){
			log.debug("&&&&&&&&&Scroll Pos:"+ request.getParameter("scrollPos"));
			request.setAttribute("scrollPos", request.getParameter("scrollPos"));
		}
		else{
			request.setAttribute("scrollPos", 0);
		}
		return modelAndView;
	}
	
	/**
	 * Gets the global template name and checks if it is unique
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView chkUniqueName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView("vendorCatalog/datamapping/inc_popuperror");
		String templateName = request.getParameter("globalTemplateName");
		String isGlobalTemplate = request.getParameter("isGlobalTemplate");
		List<CatalogMappedFieldDTO> catalogMappedFieldDTOs = (List<CatalogMappedFieldDTO>) request.getSession()
		.getAttribute(VendorDataMappingHelper.SESSION_CATALOG_MAPPED_FIELD);
		Map<String, Boolean> jsonModel = new HashMap<String, Boolean>();
		int count = 0;
		for (CatalogMappedFieldDTO catalogMappedFieldDTO : catalogMappedFieldDTOs) {
				if (catalogMappedFieldDTO.getMappingAttribute() != null 
						&& (catalogMappedFieldDTO.getMappingAttribute().equalsIgnoreCase("Vendor Style Description")
								|| catalogMappedFieldDTO.getMappingAttribute().equalsIgnoreCase("Vendor Style Number"))) {
					count++;
				}
		}
		boolean error=false;
		String errorString = "You can specify a template name only for global template";
		if (count < 2) {
			error =true;
			errorString = "AtLeast Vendor Style Number and Vendor Style Description of CARS attribute must be mapped";
			saveError(request, errorString);
			request.setAttribute("tmplNameError", error);
			//return new ModelAndView(DropShipConstants.AJAX_RETURN);
		} else if(!StringUtils.isBlank(isGlobalTemplate) && DropShipConstants.YES.equals(isGlobalTemplate)){
			
			if(!StringUtils.isBlank(templateName)){
				int templateCount = vendorCatalogManager.getCountOfTemplateName(templateName);
				if(templateCount > 0){
					error=true;
					errorString = "The template with the name '" + templateName +"' already exists. " +
							"Please choose another name";
				}
			}else{
				//Else no template name
				error=true;
				errorString = "Please enter a template name";
			}
		}
		if(error){
			jsonModel.put(DropShipConstants.SUCCESS, false);
			JSONObject json = new JSONObject(jsonModel);
			json.accumulate("error_message", errorString);
			request.setAttribute(DropShipConstants.JSON_OBJ, json);
		} else {
			jsonModel.put(DropShipConstants.SUCCESS, true);
			JSONObject json = new JSONObject(jsonModel);
			request.setAttribute(DropShipConstants.JSON_OBJ, json);
		}
		request.setAttribute("tmplNameError", error);
		return new ModelAndView(DropShipConstants.AJAX_RETURN);
	}

}
