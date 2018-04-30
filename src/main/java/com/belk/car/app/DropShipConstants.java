package com.belk.car.app; 
 

public interface DropShipConstants  {

	/**
	 * The Contact Type attribute that holds the Contact's Other Type
	 */
	public static final String OTHER_TYPE = "Other";

	public static final String CONTACT_NOTES_MESSAGE = "Free form 255 characters field";
	
	public static final String CONTACT_FIRST_NAME ="contactFirstName";
	
	public static final String CONTACT_LAST_NAME ="contactLastName";
	
	public static final String CONTACT_STATUS="status";
	
	public static final String CONTACT_TYPES="FSContactTypes";
	
	public static final String CONTACT_STATES="States";
	
	public static final String CONTACT_OTHER_TYPE_ID="OtherContactTypeId";
	
	public static final String DISPLAY_ROLE="displayRole";
	
	public static final String ADMIN="admin";
	
	public static final String PARAM="param";
	
	public static final String READ_ONLY_MODE="readOnly";
	
	public static final String ID="id";
	
	public static final String ALT_PHONE_AREA_CODE_FIELD ="altPhoneAreaCode";
	
	public static final String ALT_PHONE_NUMBER1_FIELD="altPhoneNumber1";
	
	public static final String ALT_PHONE_NUMBER2_FIELD="altPhoneNumber2";
	
	public static final String CONTACT_TYPE_FIELD="contactType.contactTypeId";
	
	public static final String CONTACT_OTHER_TYPE_DESC_FIELD="contacts.otherTypeDesc";
	
	public static final String CONTACT_ADDRESS_LINE_1_FIELD="address.addr1";
	
	public static final String CONTACT_ADDRESS_ZIP_FIELD="address.zip";
	
	public static final String CONTACT_ADDRESS_STATE_FIELD="address.state";
	
	public static final String CONTACT_ADDRESS_CITY_FIELD="address.city";
	
	public static final String CONTACT_ADDRESS_LOCATION_FIELD="Loc";
	/** 
	 * Database status Inactive constants
	 */

	public static final String INACTIVE = "INACTIVE";
	
	public static final String HANDLING_FEES ="Handling Fee";
	
	public static final String RESTOCKING_FEES ="Restocking Fee";
	public static final String TAX_FEES ="Tax";
	public static final String SHIPPING_FEES ="Shipping";
	public static final String INSURANCE_FEES ="Insurance";
	public static final String CARS_ADMIN_USER = "carsadmin@belk.com";
	/*End of constants for IDB Feed*/

	//Treats the value as a same group value.
	public static final String FORSAMEGROUP = "=";

	//Treats them as a different group value
	public static final String NOTFORSAMEGROUP = "<>";

	// Constants for Vendor Fulfillment Services --By Priyanka Gadia

	public static  final String ADDR_IDS_FROM_SESSION = "addrIdsFromSession";

	public static final String YES = "Y";

	public static final String ADDR_ID = "addrId";

	public static final String VEN_ID = "venID";

	public static final String VEN_NUM = "venNum";

	public static final String VENDOR_NAME = "name";

	public static final String VENFSID = "venfsid";

	public static final String EDIT = "edit";

	public static final String MODE = "mode";

	public static final String SAVE_BTN = "save";

	public static final String VERIFY_MODE = "verify";

	public static final String FS_VENDOR_FORM = "fsVendorForm";

	public static final String CANCEL_BTN = "cancel";

	public static final String ADDR_LIST = "addrList";

	public static final String STATES = "states";

	public static final String LOGGEN_IN_USER = "LoggenInUser";

	public static final String PATH_TO_VENDOR_LIST_JSP = "oma/fulfillmentServicesVendor";

	public static final String VENDOR_ID = "vendorID";

	public static final String STATUS = "status";

	public static final String VENDOR_NAME_FROM_REQUEST = "vendorName";

	public static final String NO = "N";

	public static final String VENDOR_LIST_MODEL_NAME = "vendorList";

	public static final String VENDOR_FS_ID_FROM_REQUEST = "id";

	public static final String FULFILLMENT_SERVICE_FROM_SESSION = "fulfillmentService";

	public static final String SUCCESS = "success";

	public static final String AJAX_RETURN = "ajaxReturn";

	public static final String JSON_OBJ = "jsonObj";

	public static final String ADDR_LIST_FROM_SESSION = "addrList";

	public static final String ZIP = "zip";

	public static final String STATE = "state";

	public static final String CITY = "city";

	public static final String ADDRESS_LINE2 = "addr2";

	public static final String ADDRESS_LINE1 = "addr1";

	public static final String LOCATION = "location";

	public static final String RETURN_METHODS = "returnMethods";

	public static final String VEN_INFO_FROM_SESSION = "vndrFulfillmentService";

	public static final String ACTIVE = "ACTIVE";

	public static final String TAX_DATA = "tax_data";

	public static final String TAX_FIELDS = "tax_fields";

	public static final String TYPE = "type";

	public static final String NAME = "name";

	public static final String STRING = "string";

	public static final String LONG = "long";

	public static final String TAX_STATE = "taxState";

	public static final String TAX_ISSUE = "taxIssue";

	public static final String TAX_ID = "taxid";

	public static final String TAX_STATE_NAME = "taxStateName";

	public static final String TAX_STATE_CODE = "taxStateCode";

	public static final String STATE_NAME = "stateName";

	public static final String VENDOR_TAX_STATES_MODEL = "vendorTaxStates";

	public static final String OMA_VENDOR_TAX_STATES_JSP = "oma/vendorTaxStates";

	public static final String TAX_ID_FROM_REQUEST = "taxId";

	public static final String ISSUE = "issue";

	/**
	 * List of Class Name constant which is used while doing explicitly
	 * cost form Super to concrete class for report requirement 
	 */
	public static final String VENDOR_SETUP_BY_MONTH = "VendorSetupByMonth";
	public static final String OMA_STYLES_SKUS = "OMAStylesSkus";
	public static final String OMA_HANDLING_FEES = "OMAHandlingFees";
	public static final String OMA_RESTOCKING_FEES = "OMARestockingFees";
	public static final String OMA_HANDLING_FEE_EXCEPTION = "OMAHandlingFeeExceptions";
	public static final String OMA_COST_EXCEPTION = "OMACostExceptions";

	// End of Constants for Vendor Fulfillment Services --By Priyanka Gadia 

	/**
	 * Constants for Fulfillment Service--added by Siddhi Shrishrimal
	 */
	public static final String MODE_ADD = "add";

	public static final String VIEW_ONLY_MODE = "viewOnly";

	public static final String FULFILLMENT_SERVICE_NAME = "fsName";

	public static final String FULFILLMENT_SERVICE_ID = "fsID";

	public static final String FULFILLMENT_METHOD_FS = "FulfillmentMethod";

	public static final String INTEGRATION_TYPE_FS = "IntegrationType";

	public static final String SAVED_SUCCESSFULLY_MESSAGE = "Saved Successfully!";

	public static final String SAVE_MSG_FULFILLMENTSERVICE = "saveMsg";

	public static final String FULFILLMENT_SERVICE_IN_SESSION = "fulfillmentService";

	public static final String FULFILLMENT_SERVICE_FORM = "fulfillmentServiceForm";

	public static final String FULFILLMENT_SERVICE_STATUS_CODE = "fulfillmentServiceStatusCode";

	public static final String FULFILLMENT_SERVICE_NAME_SEARCH = "fulfillmentServiceName";

	public static final String EMPTY_STRING = "";
	
	/**
	 * Constants required for Fulfillment Service as well as Contacts.
	 */
	public static final String ZIP_CODE_IS_REQUIRED_MSG = "Zip code is required.";

	public static final String CITY_IS_REQUIRED_MSG = "City is required.";

	public static final String ADDRESS_LINE1_IS_REQUIRED_MSG = "Address Line1 is required.";

	public static final String LOCATION_NAME_IS_REQUIRED_MSG = "Location Name is required.";

	public static final String STATE_IS_REQUIRED_MSG = "State is Required.";

	/**
	 * End of Constants for Fulfillment Service--added by Siddhi Shrishrimal
	 */

	/**Fulfillment service common constants*/
	public static final String ACTION_POSTED = "Saved & Posted";
	
	public static final String ACTION_REQUEST_SAVED = "Saved";
	
	public static final String ACTION_REQUEST_CREATED = "Created";
	
	public static final String USER_DEPT_NOT_SELECTED = "N";
	
	public static final String USER_DEPT_SELECTED = "Y";
	
	public static final String IDB_ALLOW_DROPSHIP_N = "N";

	public static final String IDB_ALLOW_DROPSHIP_Y = "Y";

	public static final String IDB_ALLOW_DROPSHIP_V = "V";

	public static final String DROP_SHIP_STATUS_INACTIVE = "INACTIVE";

	public static final String DROP_SHIP_STATUS_ACTIVE = "ACTIVE";
	
	public static final String REQUEST_NOT_APPROVED = "N";
	
	public static final String APPROVED_REQUEST = "Y";
	
	public static final String NO_OF_ACTIVE_STYLES_IS_ZERO = "0";

    public static final String NO_OF_ACTIVE_SKU_IS_ZERO = "0";
	
	public static final String DISPLAY_VALUE_ALL = "ALL";
	
	public static final String VALUE_AS_ALL = "all";
	
	public static final String ALL_COLOR_N = "N";
	
	public static final String ALL_SIZE_N = "N";
	
	public static final String FETCH_SKUS_OF_ANY_COLOR = "color";
	
	public static final String FETCH_SKUS_OF_ANY_SIZE = "size";
	
	public static final String ALL_COLOR_Y = "Y";
	
	public static final String ALL_SIZE_Y = "Y";
	
	public static final String SKU_IND_Y = "Y";
	
	public static final String QUOTE = "'";
	
	public static final String DEFAULT_SKUEXCEPTION_IND_VALUE = "N";
	
	public static final String DEFAULT_COLOR_DESC = "NO COLOR";
	
	public static final String DEFAULT_SIZE_DESC = "NO SIZE";
	
	public static final String STYLE_SKU_DETAILS_IN_MAIL = "styleDetails";
	
	public static final String USER_RECIEVING_EMAIL = "user";
	
	public static final String OVERRIDE_IN_SKU = "sku";
	
	public static final String OVERRIDE_IN_STYLE = "style";

	/** Style & sku screen constants*/
	public static final String STYLE_IS_SELECTED = "Y";

	public static final String STYLES_FROM_STYLE_SKU = "StylesFromStyleSku";

	public static final String STYLES_SKU_FORM = "stylesSkuForm";

	public static final String LOAD_ITEM_REQUEST = "item";


	public static final String USER_BUYER = "buyer";

	public static final String ITEM_REQUEST_EXCEPTION = "Exception";
	
	public static final String STYLE_SKU_FORM_IN_SESSION = "STYLESKUFORM";
	
	public static final String CHECK_ALL_STYLES_SELECTED = "checkall";

	/**Style property screen constants*/
	public static final String UPC_PARAMETER = "upc";

	public static final String ISTATUS_PARAMETER = "istatus";

	public static final String DSTATUS_PARAMETER = "dstatus";

	public static final String STYLE_DESC_PARAMETER = "desc";

	public static final String STYLEID_PARAMETER = "styleid";

	/**Request History Screen Constants*/
	public static final String REQUEST_HISTORY_DATE_FORMAT = "MM/dd/yyyy";

	public static final String REQUEST_HISTORY_FORM = "requestHistoryForm";
	
	public static final String REQUEST_HISTORY_FORM_IN_SESSION = "REQUESTHISTORYFORM";
	
	public static final String PAGINATION = "pagination";

	/**Item request constant declarations*/
	public static final String COMMA = ",";

	public static final String LABEL_UPC = "upc";

	public static final String LABEL_VENDOR_UPC = "vendor upc";

	public static final String LABEL_BELK_UPC = "belk upc";

	public static final String LABEL_VENDOR_STYLE = "vendor style";

	public static final String MM_DD_YYYY = "MM/dd/yyyy";

	public static final String ALLOW_SALE_CLEARANCE = "y";

	public static final String EDIT_ITEMREQUEST = "edit";

	public static final String SAVE_POST = "post";

	public static final String BLANK = "";

	public static final String ITEMREQUEST_FALSE = "false";

	public static final String UNKNOWN_USER = "Unknown";

	public static final String ITEMREQUEST_FILE_FORMAT = ".xls";

	public static final String SHOW_STYLE_TABLE = "1";

	public static final String ITEMREQUEST_FILEFORMAT = "itemrequest.fileformat";

	public static final String ITEMREQUEST_FILE = "file";

	public static final String ITEMREQUEST_TRUE = "true";

	public static final String IS_POSTED = "isposted";

	public static final String MANUAL_ENTRY = "Manual Entry";

	public static final String REJECT_STATUS = "REJECT";

	public static final String REJECT = "reject";

	public static final String APPROVE_STATUS = "Approved";

	public static final String APPROVE = "approve";

	public static final String OPEN_STATUS = "OPEN";

	public static final String N = "N";

	public static final String ADD_ITEMREQUEST = "add";

	public static final String ITEMREQUEST_MODE = "mode";

	public static final String SOURCE_ITEM = "sourceItem";

	public static final String STYLE_POPULATION_METHODS = "StylePopulationMethods";

	public static final String ITEM_REQUEST_FORM = "itemRequestForm";

	public static final String ITEMREQUEST_VALIDREQUESTID = "itemrequest.validrequestid";

	public static final String INVALID = "invalid";

	public static final String LOCATION_NAME = "locationName";

	public static final String PREVIOUS_REQUEST_STYLE = "Previous Request";

	public static final String ITEMREQUEST_FAILEDSAVE = "itemrequest.failedsave";

	public static final String HIDE_STYLE_TABLE = "0";

	public static final String REQUEST_ID = "RequestId";

	public static final String VALID_STYLES = "ValidStyles";

	public static final String REQUEST_STYLES = "request styles >";

	public static final String USE_USER_DEPT = "USER_DEPT_CHECKED";

	public static final String USERDEPT_CHECKED = "Y";

	public static final String STYLE_REQUEST_ID = "styleid";

	public static final String REQUEST_STYLES_SAVE = "save";

	public static final String REQUEST_PROPERTIES = "< request properties";

	public static final String STYLE_ERROR = "errorPage.title";

	public static final String AWAITING_APPROVAL_STATUS = "AWAITING APPROVAL";

	public static final String UPLOADED_STYLES_IS_ZERO = "0";

	public static final String SAVED_SUCCESSFULLY = "Saved Successfully!";

	public static final String VALID = "VALID";

	public static final String LOAD_SKU_EXCEPTION = "SKU Exception >";

	public static final String REQUEST_OF_STYLE_TO_BE_REMOVED = "requestid";

	public static final String IS_REQUEST_APPROVE_REJECT = "isapprovereject";
	
	public static final String ITEM_REQUEST_FORM_IN_SESSION = "item_request_form_in_session";

	public static final String ADD_STYLES_ERRORS = "AddStylesErrors";

	public static final String ITEMREQUEST_INVALIDSTYLES_MESSAGE = "itemrequest.invalidstyles";

	public static final String SKU_REMOVE_SIZE = "size";

	public static final String SKU_REMOVE_COLOR = "color";

	public static final String SKU_REMOVE_STYLEID = "styleid";

	public static final String SKU_REMOVE_REQUESTID = "requestid";
	
	public static final String SKU_REMOVE_UPC = "upc";

	public static final String SKU_SAVE = "save";

	public static final String LOAD_UPDATE_HISTORY = "Update History >";

	public static final String LOAD_REQUEST_STYLES = "< Request Styles";

	public static final String ITEMREQUEST_REJECTREASON = "itemrequest.rejectreason";

	public static final String REQUEST_REJECT_REASON = "rejectReason";

	public static final String STATUS_OF_REQUEST = "statusofrequest";

	public static final String REMOVE_SKU = "remove";

	/*Constants for Vendor Catalog Setup Form--added by Vikas Alle*/
	/**
	 * VendorCatalogSetup/Vendor Catalog Notes Constants
	 */
	public static final String FILE_NAME = "fileName";

	public static final String IMAGE_DIRECTORY = "imageDirectory";

	public static final String UNDERSCORE = "_";

	public static final String SLASH = "/";

	public static final String FTPDETAILS = "ftpDetail"; 

	public static final String FTP_PROPERTIES = "ftp.properties";
	
	public static final String PROPERTY_URL_PATH = "urlPath";

	public static final String FTPPASSWORD = "userPassword";

	public static final String FTPUSER = "userName";

	public static final String FTPHOST = "hostName";

	public static final String FTPPATH = "imagePath";

	public static final String SWATCH_IMAGE_NAME = "SWATCH_IMAGE_NAME";

	public static final String MAIN_IMAGE_NAME = "MAIN_IMAGE_NAME";

	public static final String ALTERNATE_IMAGE_5 = "ALTERNATE_IMAGE_5";

	public static final String ALTERNATE_IMAGE_4 = "ALTERNATE_IMAGE_4";

	public static final String ALTERNATE_IMAGE_3 = "ALTERNATE_IMAGE_3";

	public static final String ALTERNATE_IMAGE_2 = "ALTERNATE_IMAGE_2";

	public static final String ALTERNATE_IMAGE_1 = "ALTERNATE_IMAGE_1";

	public static final String XML_FILES_XML = "XML Files(*.xml)";

	public static final String MICROSOFT_OFFICE_EXCEL_FILE = "Microsoft Office Excel File";

	public static final String UPLOAD_FORM_FILE = "uploadForm.file";

	public static final String TEXT_FILES_TXT_CSV = "Text Files(*.txt,*.csv)";

	public static final String FILE_FORMAT_LIST = "fileFormatList";

	public static final String UPDATE_ACTION_LIST = "updateActionList";

	public static final String IMAGE_LOCATION = "imageLocation";

	public static final String VENDOR_CATALOG_FORM = "vendorCatalogForm";

	public static final String VENDOR_CATALOG = "vendorCatalog";

	public static final String VNDR_NUMBER = "vendorNumber";

	public static final String VNDR_NAME = "vendorName";

	public static final String VENDOR_CATALOG_NOTE = "vendorCatalogNote";

	public static final String SUBJECT = "subject";

	public static final String NOTE_SUBJECT = "noteSubject";

	public static final String NOTE_TEXT = "note";

	public static final String VENDOR_CATALOG_NOTEID = "noteidnew";
	
	public static final String SHOW_MSG = "showMsg";

	public static final String Y = "Y";

	public static final String RETRIEVE_FROM_VENDOR_FTP_SITE = "Retrieve from Vendor FTP site";

	public static final String SELECT_OPT = "Select Option";

	public static final String UPLOAD_FROM_CD = "Upload Files";

	public static final String FTP = "ftp";
	
	public static final String HTTP = "http";

	public static final String XML = ".xml";

	public static final String XLS = ".xls";

	public static final String TXT = ".txt";

	public static final String CSV = ".csv";

	public static final String DOT = ".";

	public static final String DEPARTMENTS = "departments";

	public static final String SELECTED_DEPT = "selectedDept";

	public static final String NEW_DEPT_LIST = "newDeptList";

	public static final String USER_DEPARTMENTS = "userDepartments";

	public static final String USER = "user";

	public static final String ADD_DEPARTMENT = "addDepartment";

	public static final String METHOD = "method";

	public static final String ENABLE = "enable";

	public static final String VENDR_ID = "venID";

	public static final String VEN_NAME = "venName";

	public static final String SAVE_BUTTON = "Save";

	public static final String SAVE = "save";

	public static final String VERIFY_BUTTON = "Verify";

	public static final String VERIFY = "verify";
	
	public static final String ADD_VENDOR_CATALOG = "add";

	public static final String EDIT_VENDOR_CATAOLOG = "edit";

	public static final String VIEW_ONLY = "viewOnly";

	public static final String REMOVED_DEPT_LIST = "removedDeptList";

	public static final String ADDED_DEPT_LIST = "addedDeptList";

	public static final String IMPORTING = "Importing";

	public static final String UNMAPPED = "Unmapped";
	
	public static final String FTP_USER_NAME = "ftpUserName";

	public static final String FTP_URL = "ftpUrl";

	/**
	 * Fulfillment Service/Vendor Notes Constants
	 */
	public static final String NOTES_FIELDS = "notes_fields";

	public static final String NOTES_DATA = "notes_data";

	public static final String NOTEID_NOTE_FIELD = "noteid";

	public static final String SUBJECT_NOTE_FIELD = "subject";

	public static final String NOTETYPE_DATATYPE = "type";

	public static final String NAME_DATATYPE_NOTES = "name";

	public static final String FULFILLMENT_SERVICE_NOTE_SUBJECT = "noteSubject";

	public static final String FULFILLMENT_SERVICE_NOTES_STATUS_ACTIVE = "Active";

	public static final String NOTE_DESCRIPTION = "note";

	public static final String FULFILLMENT_SERVICE_NOTE_ID = "noteidnew";

	/**
	 * The name of the source type list, a application-scoped attribute
	 * when searching a car.
	 */ 
	public static final String AVAILABLE_SOURCE_TYPES = "sourceType";

	public static final String VENDOR_FEES_FIELDS = "vendorFeesFields";

	public static final String VENDOR_FEES_DESC = "feeDescription";

	public static final String VENDOR_FEES_ALLOWABLE ="allowable";

	public static final String VENDOR_FEES_PER_ORDER_AMOUNT = "perOrderAmount";

	public static final String VENDOR_FEES_PER_ITEM_AMOUNT = "perItemAmount";

	/**
	 * The request scope attribute that holds the products groups
	 */
	public static final String PRODUCT_GROUP_LIST = "productGroupList";

	public static final String SHIPPING_OPTIONS_SIZE_SESSION = "shippingOptionsSize";

	public static final String SHIPPING_OPTIONS_MODEL_LIST_SESSION = "shippingOptionsModelList";

	public static final String ADDED_ADDRESS = "addedAddress";

	/**
	 * The session Scope attributes that holds information about Vendor Request Fees.
	 */
	public static final String FEES_LAST_UPDATED = "lastUpdatedFeesDate";

	public static final String FEES_LAST_MODIFIED_BY = "lastFeeModifiedBy";

	

	public static final String ERRORS ="errors";




	public static final String DATE_FORMAT = "yyyy-MM-dd";

	public static final String OTHER_CONTACT_TYPE_ID =  "3";


	/*Constants for IDB Feed*/
	public static final String OPEN_WORKFLOW_STATUS = "OPEN";
	public static final String DROP_SHIP_IN_IDB_NOT_IN_ECOMMERCE = "Drop Ship In IDB Not In ECommerce";
	public static final String IDB_COST_CHANGE_SOURCE = "IDB Cost Change";
	public static final String IDB_FEED_SOURCE = "IDB Feed";
	public static final String UTF_8 = "UTF-8";
	public static final String YYYY_MM_DD_FORMAT = "yyyy-MM-dd";
	public static final String STYLE_DROPSHIP_FLAG_Y = "Y";
	public static final String STYLE_DROPSHIP_FLAG_N = "N";
	public static final String STYLE_DROPSHIP_FLAG_V = "V";
	public static final String EMPTY_STRING_VENDOR = " ";
	public static final String STATUS_CD_ACTIVE = "ACTIVE";
	public static final String STATUS_CODE_INACTIVE = "INACTIVE";
	public static final String CARSDEV_USER = "CARSDEV";
	public static final String ADD_EDIT_STYLES_SKUS_ACTION = "Add/Edit Styles/SKUs";
	public static final String REMOVE_STYLES_SKUS_ACTION = "Remove Styles/SKUs";
	public static final String USER_ID = "userId";
	public static final String SEARCH_STYLE_FORM = "searchStyleForm";
	
	/*--------------------------------------------BR.003----------------------------------------------------*/
	public static final String NO_IMAGE_AVAILABLE_LOGO = "http://s7d4.scene7.com/is/image/Belk/No-Image-available?$P_THUMB$";
	public static final String YYYY_M_MDD_H_HMMSS_FORCATALOG = "yyyyMMddHHmmss";
	public static final String VENDOR_CATALOG_DATAMAPPING_LINK = "/vendorCatalog/datamapping/vendorCatalogDataMapping.html?method=getVendorCatalog&cid=";
	public static final String SAVE_COMPLETE_TEMPLATE ="saveCompleteTemplate";
	public static final String SAVED_AND_COMPLETED ="Mapping saved and completed successfully!";
	public static final String SAVE_TEMPLATE ="saveTemplate";
	public static final String TEMPALTE_SAVED ="Mapping Saved Successfully!";
	public static final String FIELD_MAPPING_ERROR ="Please validate the catalog uploaded or field mapping.";
	/*--------------------------------------------BR.003----------------------------------------------------*/

	/*---------------------------------------------BR.005-----------------------------------------------------*/
	public static final int ALTERNATE_IMAGE_5_ATTRIBUTE = 10;
	public static final int ALTERNATE_IMAGE_4_ATTRIBUTE = 9;
	public static final int ALTERNATE_IMAGE_3_ATTRIBUTE = 8;
	public static final int ALTERNATE_IMAGE_2_ATTRIBUTE = 7;

	public static final int SWATCH_IMAGE_ATTRIBUTE = 5;
	public static final int MAIN_IMAGE_ATTRIBUTE = 4;
	public static final long ALTERNATE_IMAGE_1_ATTRIBUTE = 6;
	public static final int COLOR_ATTRIBUTE_ID = 3;
	public static final int VENDOR_UPC_ATTRIBUTE_ID = 11;
	public static final int STYLE_ATTRIBUTE_ID = 1;
	public static final String VENDOR_CATALOG_TRANSALTION_COMPLETE = "CTL_TRNSLATION_CMPLT";
	public static final String VENDOR_CATALOG_TRANSALTION_ERROR = "CTL_TRNSLATION_ERR";
	public static final int OVERWRITE_ACTION_ID = 3;
	public static final int APPEND_ACTION_ID = 2;
	public static final int UPDATE_ACTION_ID = 1;
	public static final int STYLE_DESC_ATTRIBUTE_ID = 2;

	/*-------------------------------------------------BR.005-------------------------------------------------*/

        /* ----------------------------------------------BR.001 -------------------------------------------------*/
        public static final String ZERO = "0";
        public static final String VENDOR_STYLE_LIST_PAGE = "vendorCatalog/vendorStyleList";
	public static final String VENDOR_CATALOG_STYLE_LIST_PAGE = "vendorCatalog/vendorCatalogStyleList";
	public static final String VENDOR_STYLE_LIST = "vendorStyleList";
        public static final String VENDOR_STYLE_PROPERTIES = "vendorCatalog/VendorStyleProperties";
        public static final String EDIT_VENDOR_STYLE_PROPERTIES = "vendorCatalog/editVendorStyleProperties";
        public static final String VENDOR_PROPERTIES = "vendorCatalog/vendorProperties";
        public static final String VENDOR_PROPERTIES_FORM = "VendorPropertiesForm";
        public static final String FALSE= "false";
        public static final String VENDOR_CATALOG_VENDOR_ID="VendorCatalogVendorId";
        public static final String VENDOR_CATALOG_ID="vendorCatalogID";
        public static final String VENDOR_STYLE_ID="vendorStyleId";
        public static final String RECORD_NUMBER="recordNum";
        public static final String CATALOG_TEMPLATE_ID="catalogTemplateId";
        public static final String VENDOR_UPC="vendorUpc";
        public static final String STYLE_PROPERTIES="StyleProperties";
        public static final String LOCK="lock";
        public static final String CATALOG_INFO="catalogInfo";
        public static final String VENDOR_STYLE_SEARCH_CRITERIA="VendorStyleSearchCriteria";
        public static final String MAPPED_ATTRIBUTE_LIST="mappedAttributeList";
        public static final String UNMAPPED_ATTRIBUTE_LIST="unMappedAttributeList";
        public static final String STYLE_IMAGE_LIST="styleImageList";
        public static final String VENDOR_UPC_LIST ="vendorUPCList";
        public static final String EDIT_VENDOR_STYLE_PROPERTIES_FORM ="editVendorStylePropertiesForm";
        public static final String INDEX ="index";
        public static final String UPC_DETAILS ="upcDetails";
        public static final String SKU_IMAGE_LIST ="skuImageList";
        public static final String MAIN ="MAIN";
        public static final String SWATCH ="SWATCH";
        public static final String ALTERNATE ="ALT";
        public static final String ALTERNATE5 ="ALT5";
        public static final String VENDOR_CATALOG_STYLE_SEARCH_CRITERIA="VendorCatalogStyleSearchCriteria";
        public static final String VENDOR_STYLE_INFO="vendorStyleInfo";
        public static final String TRUE="true";
        public static final String VENDOR_STYLE_DESC="Vendor Style Description";
        public static final String COLOR="Color";
        public static final String IS_ADMIN="isAdmin";





        /**
	 Catlog Vendors List
	 */
	public static final String CATALOG_VENDOR_LIST = "catalogVendorList";

	/**
	 Vendor Catlog  List
	 */
	public static final String VENDOR_CATALOG_LIST = "vendorCatalogList";


	public static final String  VENDOR_CATALOG_LIST_PAGE= "vendorCatalog/vendorCatalogList";

	public static final String  OPEN_CATALOG_LIST_PAGE= "vendorCatalog/openCatalogRequest";


	public static final String VENDOR_Id= "vendorId";
	public static final String NUM_OF_SKUS = "numSKUs";
	public static final String NUM_OF_STYLES = "numStyles";
	public static final String VENDOR_INFO = "vendorInfo";
	public static final String SEARCH_CATALOG_FORM = "searchCatalogForm";
	public static final String CATALOG_VENDOR_FORM ="catalogVendorsForm";
        public static final String ROLE_ADMIN ="ROLE_ADMIN";
        public static final String ROLE_BUYER ="ROLE_BUYER";
        public static final String VENDOR_CATALOG_INFO ="vendorCatalogInfo";


	
	
	//After data mapping sets the status to translating
	public static final String CATALOG_STATUS_TRANSLATING ="Translating";
	public static final String CATALOG_STATUS_DATAMAPPING = "Data Mapping";

	public static final String STYLE_STATUS_ACTIVE = "ACTIVE";
	public static final String CAR_STATUS_ACTIVE = "ACTIVE";
	public static final String ATTRIBUTE_STATUS_ACTIVE = "ACTIVE";
	public static final String USER_CREATING_CAR = CARS_ADMIN_USER;
	public static final String NOTES_STATUS_ACTIVE = "ACTIVE";

	public static final String TRANSLATING = "Translating";
	public static final String noteSubject = "Processing error while translation";
	public static final String MAPPED = "mapped";
	public static final String UNMAPPED_IMAGE_INITIAL_FOLDER = "Unmapped";
        
	public static final String CATALOG_REQUEST_DISPLAY_TABLE_PAGINATION_ID="d-6682707-p";
	
	


	public static final String LATE_CARS_GROUP_LIST = "lateCarsGroupList";
	
	public static final String LATE_CARS_GROUP_DETAIL_LIST = "lateCarsDetailList";
	
	public static final String VENDORINFO_INSERT = "I";
	public static final String VENDORINFO_UPDATE = "U";
	public static final String EXPORT_EXPDSHIPPING_JOB = "Expedited-Shipping-Job";
	public static final String EXPORT_EXPDSHIPPING_JOB_LASTDATE_FORMATE = "dd-MMM-yy";

	public static final String EXPDTD_SHIPPING_FTP_UNAME="exportExpShippingUserName";
	public static final String EXPDTD_SHIPPING_FTP_PWORD="exportExpShippingPassword";





}



