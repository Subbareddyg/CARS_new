package com.belk.car.app;

public class Constants {
	public static final String FLAG_YES = "Y";
	public static final String FLAG_NO = "N";

	public static final String METADATA = "metadata";
	public static final String ROLES = "roles";
	public static final String ROLE = "role";
	public static final String FIELDS = "fields";
	public static final String NAME = "name";
	public static final String LABEL = "label";
	public static final String OPTION = "option";
	public static final String VALUE = "value";
	public static final String OPTIONAL = "optional";
	public static final String FIELD = "field";
	public static final String FIELD_ORDER = "field-order";
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String TRANSITION = "transition";
	public static final String STATE = "state";
	public static final String STATES = "states";
	public static final String STATUS = "status";
	public static final String MASK = "mask";
	public static final String FIELD_XPATH = "/" + METADATA + "/" + FIELDS + "/" + FIELD;
	public static final String ROLE_XPATH = "/" + METADATA + "/" + ROLES + "/" + ROLE;
	public static final String STATE_XPATH = "/" + METADATA + "/" + STATES + "/" + STATE;
	public static final String FIELD_ORDER_XPATH = "/" + METADATA + "/" + FIELDS + "/" + FIELD_ORDER + "/" + FIELD;
	public static final String COPY = "copy";
	public static final String COPY_CAR = "copyCar";
	public static final String NOT_DEFINED = "notDefined";
	public static final String NONE = "none";

	/**
	 * This is for PIM Phase II
	 */
	public static final String GROUP_CREATE = "GroupCre";
	public static final String GROUP_MODIFY = "GroupMod";
	public static final String GROUP_DEACTIVATE = "GroupDis";
	public static final String GROUP_DELETE = "GroupDel";


	/**
	 * The request scope attribute that holds the manual car list
	 */
	public static final String MANUAL_CAR_LIST = "manualCarList";

	/**
	 *
	 */
	public static final String MANUAL_CAR_PROCESS_STATUS_LIST = "manualCarProcessStatusList";

	public static final String ATTR_SEARCH_RESULT_KEY = "ATTR_SEARCH_RESULT";

	public static final String PROD_SEARCH_RESULT_KEY = "PROD_SEARCH_RESULT";

	/**
	 * The request scope attribute that holds the attributes list
	 */
	public static final String ATTR_LIST = "attributesList";

	/**
	 * The request scope attribute that holds the products list
	 */
	public static final String PRODUCT_LIST = "productList";

	/**
	 * The request scope attribute that holds the products list
	 */
	public static final String WORKFLOW_LIST = "workflowList";

	/**
	 * The name of the ResourceBundle used in this application
	 */
	public static final String BUNDLE_KEY = "ApplicationResources";

	/**
	 * File separator from System properties
	 */
	public static final String FILE_SEP = System.getProperty("file.separator");

	/**
	 * User home from System properties
	 */
	public static final String USER_HOME = System.getProperty("user.home") + FILE_SEP;

	/**
	 * The name of the configuration hashmap stored in application scope.
	 */
	public static final String CONFIG = "appConfig";

	/**
	 * Session scope attribute that holds the locale set by the user. By setting this key
	 * to the same one that Struts uses, we get synchronization in Struts w/o having
	 * to do extra work or have two session-level variables.
	 */
	public static final String PREFERRED_LOCALE_KEY = "org.apache.struts2.action.LOCALE";

	/**
	 * The request scope attribute under which an editable user form is stored
	 */
	public static final String USER_KEY = "userForm";

	/**
	 * The request scope attribute that holds the user list
	 */
	public static final String USER_LIST = "userList";

	/**
	 * The request scope attribute that holds the vendor list
	 */
	public static final String Vendor_LIST = "userList";

	/**
	 * The request scope attribute that holds the classification list
	 */
	public static final String CLASSIFICATION_LIST = "classifications";

	/**
	 * The request scope attribute that holds the product type list
	 */
	public static final String PRODUCT_TYPE_LIST = "productTypes";

	/**
	 * The request scope attribute that holds the attribute list
	 */
	public static final String ATTRIBUTE_LIST = "attributes";

	/**
	 * The request scope attribute for indicating a newly-registered user
	 */
	public static final String REGISTERED = "registered";

	/**
	 * The request scope attribute for indicating a newly-registered user
	 */
	public static final String NOT_ACIVATED = "notActivated";

	/**
	 * The name of the Administrator role, as specified in web.xml
	 */
	public static final String ADMIN_ROLE = "ROLE_ADMIN";

	/**
	 * The name of the Administrator role, as specified in web.xml
	 */
	public static final String BUYER_ROLE = "ROLE_BUYER";

	/**
	 * The name of the User role, as specified in web.xml
	 */
	public static final String USER_ROLE = "ROLE_USER";

	/**
	 * The name of the user's role list, a request-scoped attribute
	 * when adding/editing a user.
	 */
	public static final String USER_ROLES = "userRoles";

	/**
	 * The name of the user's type list, a request-scoped attribute
	 * when adding/editing a user.
	 */
	public static final String AVAILABLE_USER_TYPES = "availableUserTypes";

	/**
	 * The name of the available roles list, a request-scoped attribute
	 * when adding/editing a user.
	 */
	public static final String AVAILABLE_ROLES = "availableRoles";

	public static final String AVAILABLE_FEATURES = "availableFeatures";

	/**
	 * The name of the CSS Theme setting.
	 */
	public static final String CSS_THEME = "csstheme";

	public static final String HELP_CONTENT_LIST = "helpContentList";

	/**
	 * Start Drop Ship Constant List.
	 */

	public static final String OUTFIT_LIST = "outfitList";
    public static final String CATALOG_VENDOR_LIST = "catalogVendorList";
    public static final String OUTFIT_DEPT_NUMBER = "000";
    public static final String OUTFIT_CLASS_NUMBER = "0000";
    public static final String OUTFIT_PRODUCT_TYPE = "OUTFIT_PRODUCT_TYPE";
    public static final String OUTFIT_VENDOR_NUMBER = "9999999";
    public static final String OUTFIT_PARENT_PRODUCTS = "OUTFIT_PARENT_PRODUCTS";
    public static final String OUTFIT_CHILD_PRODUCTS = "OUTFIT_CHILD_PRODUCTS";

    //Deal Management Constants - Starts
    public static final String DBPROMOTION_LIST = "promotionList";
    public static final String DBPROMOTION_DEPT_NUMBER = "000";
    public static final String DBPROMOTION_CLASS_NUMBER = "0000";
    public static final String DBPROMOTION_PRODUCT_TYPE = "PROMO_PRODUCT_TYPE";
    public static final String DBPROMOTION_VENDOR_NUMBER = "7777777";
    public static final String DBPROMOTION_PARENT_PRODUCTS = "PROMO_PARENT_PRODUCTS";
    public static final String DBPROMOTION_DEFAULT_SKUS = "PROMO_DEFAULT_SKUS";
    public static final String DBPROMOTION_CHILD_PROD_CODES = "PROMO_CHILD_PROD_CODES";
    public static final String TEMPL_TYPE_VAL_PYG = "PYG";
    public static final String DBPROMOTION_ATTR_IS_PYG = "IS_PYG";
    public static final String DBPROMOTION_DEPARTMENT = "DealBased_Department";
    public static final String DBPROMOTION_CLASS = "DealBased_Class";
    public static final String ATTR_IS_GWP = "IS_GWP";
    public static final String DBPROMOTION_ATTR_IS_PYG_YES = "Yes";
    public static final String DBPROMOTION_FORM="dbPromotionForm";
    public static final String DBPROMOTION_TEMP_TYPE = "Promotion_Template_Type";
    public static final String DBPROMOTION_ONE_STEP_CHILD = "ONE_STEP_CHILD";
    public static final String PROMOTYPE_GWP = "GWP";
    public static final String PROMOTYPE_PYG = "PYG";
    public static final int MAX_ATTRVALUE_LIMIT = 2000;
    public static final int PRODCODE_LENGTH = 15;

  //Deal Management Constants - Ends

    public static final String VALUE_YES = "Yes";
    public static final String VALUE_NO = "No";

    public static final String COLLECTION_PARENT_PRODUCTS = "Collection_Skus";
    public static final String COLLECTION_IS_PRODUCT_SEARCHABLE = "IS_PRODUCT_SEARCHABLE";
    public static final String COLLECTION_OUTFIT_TYPE = "Template_Type";
    public static final String COLLECTION_EFFECTIVE_DATE = "Prod_Active_Start_Date";

    public static final String BELK = "Belk";
    public static final String MC_IMAGE_RECEIVED = "RECEIVED";
    public static final String MC_IMAGE_DOWNLOADED = "DOWNLOADED";


    /**
   	 * Start Faceted  Constant List.
   	 */
       public static final String SUPERCOLOR_LIST = "superColorList";
       public static final String SIZECONVERSION_LIST = "sizeConversionList";
       public static final String CRITERIA_COLORNAME = "superColorName";
       public static final String CRITERIA_COLORCODE = "superColorCode";
       public static final String SUPERCOLOR_NAME = "Super Color Name";
       public static final String SUPERCOLOR_CODE = " Super Color Code";
       public static final String SUPERCOLOR_BEGINCODE = "Super Color Begin Code";
       public static final String SUPERCOLOR_ENDCODE = " Super Color End Code";
       public static final String SUPERCOLOR_STATUS_PENDING = "P";
       public static final String SUPERCOLOR_STATUS_DELETED = "D";
       public static final String SUPERCOLOR_STATUS_ACTIVE = "A";
       public static final String INDEX_NOTSELECTED = "-1";
       public static final int COLORCODE_MAXLEN = 3;
       public static final String COLORCODE_LEFTPAD = "0";
       public static final String ATTR_SUPERCOLOR1 = "Super_Color_1";
       public static final String ATTR_SHADEDESC = "Swatch_Description";
       public static final String ATTR_SKU_START_DATE = "Sku_Active_Start_Date";
       public static final String SKIP_ATTRIBUTES_FROM_DISPLAY = "Swatch_Description,Sku_Active_Start_Date";

	   //attribute tracking
	   public static final String ATTRIBUTE_VALUE_DISPLAY = "attributeValues";
	   public static final String EDIT_VALUE_DISPLAY = "Edit";
	   public static final String DELETE_VALUE_DISPLAY = "Delete";
	   public static final String ADD_VALUE_DISPLAY = "Add";
	   public static final String DEPARTMENT_LIST = "departments";
       public static final String ATTRVAL_DELIMITER="~@~";
       public static String NEW_LINE = "\r\n";
       // for size conversion
       public static final String SIZECONV_STATUS_ACTIVE = 	"A";
       public static final String SIZECONV_STATUS_DELETED = "D";
       public static final String SIZECONV_STATUS_PENDING = "P";
       public static final String SIZE_NAME = "Size Name";
       public static final String SIZE_CONVERSIONNAME = "Size Conversion Name";
       public static final String FACET_SIZE_1 = "Size_Component_1";
       public static final String FACET_SIZE_2 = "Size_Component_2";
       public static final String FACET_SIZE_3 = "Size_Component_3";
       public static final String FACET_SUB_SIZE_1 = "Size_Sub_Component_1";
       public static final String FACET_SUB_SIZE_2 = "Size_Sub_Component_2";

       // for size conversion from PIM
       public static final String PIM_FACET_SIZE_1 = "facet_size_1";
       public static final String PIM_FACET_SIZE_2 = "facet_size_2";
       public static final String PIM_FACET_SIZE_3 = "facet_size_3";
       public static final String PIM_FACET_SUB_SIZE_1 = "facet_sub_size_1";
       public static final String PIM_FACET_SUB_SIZE_2 = "facet_sub_size_2";

       //size color common
       public static final String SCHEDULER_CREATEDBY = "BatchJob";
       public static final String SCHEDULER_UPDATEDBY = "BatchJob";
       public static final String RULECHANGED_TRUE = "T";
       public static final String RULECHANGED_FALSE = "F";

       //Quartz Job size/color-synch constants
       public static final String BATCHJOB_PROPERTYFILE_BASENAME = "batchjobconfig";
       //color
       public static final String ATTR_NULL_VAL = "<null>";
       public static final String ELEMENT_S = "S";
       public static final String  ELEMENT_SKU = "SKU";
       public static final String  ELEMENT_OBJ_ATTR = "OBJECT_ATTRIBUTE";
       public static final String SCOLOR1_SYNCH_BATCHSIZE= "scolor1synch.batchsize";
       public static final String BATCH_SIZE_DELETED_COLORS_SYNCH= "deletedcolorsynch.batchsize";
       //size
       public static final String SIZE_SYNCH_BATCHSIZE= "sizesynch.batchsize";
       public static final String MAX_SIZERECORDS_THRESHOLD = "sizesynch.batchthreshold";
       // added constants for color attributes
       public static final String ATTR_SUPERCOLOR2 = "Super_Color_2";
       public static final String ATTR_SUPERCOLOR3 = "Super_Color_3";
       public static final String ATTR_SUPERCOLOR4 = "Super_Color_4";
       public static final String CONVERTED_SIZE_NAME="size_desc";
       public static final String ATTR_TEMPLATE_TYPE ="templateType";
       public static final String OUTFIT_FORM="outfitForm";
       public static final String ATTR_SEARCHABLE="searchable";
       public static final String ATTR_EFFECTIVE_DATE="effectiveDate";
       public static final String ATTR_COLLECTION="COLLECTION";
       public static final String SELECTED_UPC="selectedUpc";
       public static final String SELECT_SKUCHECKBOX = "1";
       public static final String UNSELECT_SKUCHECKBOX = "0";
       public static final String ISSEARCHABLE_VALUE = "Y";

       //added for VIP
       public static final String PENDING="PENDING";
       public static final String REQUESTED="REQUESTED";
       public static final String APPROVED ="APPROVED";
       public static final String VENDORIMAGE ="VENDORIMAGE";
       public static final String VENDORIMAGE_TRANSFERRED_EXTN = ".SENT";

       //added for PIM
       public static final String SEARCHPIMATTRIBUTE ="searchPIMAttribute";
       public static final String SAVEPIMATTRIBUTE ="savePIMAttribute";
       public static final String SYSTEM_USER ="systemUser";
       public static final String SOURCE_PO_CAR ="sourcePOCar";
       public static final String SOURCE_DROPSHIP_CAR="sourceDropshipCar";
       public static final String SOURCE_MANUAL_CAR ="sourceManualCar";
       public static final String DEFAULT_WORKFLOW ="defaultWorkflow";
       public static final String INITIATED ="initiated";
       public static final String WITH_VENDOR ="withVendor";
       public static final String BUYER ="buyer";
       public static final String VENDOR_TYPE ="vendorType";
       public static final String CHECK_REQUIRED ="checkRequired";
       public static final String NO_CHECK_REQUIRED ="noCheckRequired";
       public static final String CAR_NOTE_TYPE ="carNoteType";
       public static final String CONTENT_IN_PROGRESS ="contentInProgress";
       public static final String VENDOR_STYLE_TYPE_PRODUCT="vendorStyleTypeProduct";
       public static final String MANUAL_CAR_STATUS_COMPLETED ="completed";
       public static final String MANUAL_CAR_STATUS_COMPLETE_WITH_ERROR ="completeWithError";
       public static final String NOTIFICATION_TYPE_SYSTEM_FAILURE = "type";
       public static final String SEND_EMAIL_NOTIFICATIONS = "sendNotifications";
       public static final String SCHEDULED_PROCESS_ERROR_NOTIFICATION_LIST = "emailNotificationList";
       public static final String GET_ITEM_FAILURE_TEXT = "Unable to retrieve attribute values from PIM.Please try again by clicking on Retrieve PIM Data button.";

       public static String CAR_ID ="carId";

       //Constants added for PIM implementation
       public static String PO_HDR_MOD ="POHdrMod";
       public static String PO_CRE ="poCre";
       public static String PO_DTL_CRE ="PODTLCre";
       public static String IS_DROPSHIP ="IS_DROPSHIP";
       public static String SDF_ONLINE_ONLY ="SDF_Online Only";
       public static String DEPARTMENT ="Department";
       public static String CLASS ="Class";
       public static String COMPLEX_PACK ="Complex Pack";
       public static String SIZE ="SIZE";
       public static String COLOR ="COLOR";

       //JIRA CARS-56
       public static String READY_FOR_REVIEW_WFS="READY_FOR_REVIEW";
       public static String INITIATED_WFS="INITIATED";
       public static String WAITING_FOR_SAMPLE_WFS="WAITING_FOR_SAMPLE";
       public static String VENDOR_MORE_INFO="Send to vendor for more information";
       public static String VENDOR_TO_COMPLETE="Send to Vendor to complete";
       public static String CLOSED_WFS="CLOSED";
       public static String WEB_MERCHANT="WEB_MERCHANT";

       //PIM Integration constants
       public static String CAR_ATTRIBUTE = "CAR_ATTRIBUTE";
       public static String CAR_SKU_ATTRIBUTE = "CAR_SKU_ATTRIBUTE";
       public static String VENDOR = "VENDOR";
       public static String VENDOR_STYLE = "VENDOR_STYLE";
       public static String VENDOR_SKU = "VENDOR_SKU";
       public static String ATTR_VALUE_COLUMN = "ATTR_VALUE";
       public static String PRODUCT_TYPE = "PRODUCT";
       public static String SKU_TYPE = "SKU";
       public static String FLAG_N = "N";
       public static String FLAG_Y = "Y";
       public static String DISPLAY_SEQ_ZERO = "0";
       public static String COLOR_NAME_COLUMN = "COLOR_NAME";
       public static String SIZE_NAME_COLUMN = "SIZE_NAME";
       public static String COlOR_DESC = "color_desc";
       public static String SIZE_DESC = "size_desc";
       public static String PIM_CARS_BRAND_ATTRIBUTE = "Cars_Brand";
       public static String PIM_CARS_BM_BRAND_ATTRIBUTE = "Brand";
       public static String GROUPING = "grouping";
       public static String DUMMY_PRODUCT_NAME = "Dummy Style";
       public static String PIM_DATE_FORMAT = "yyyy-MM-dd";
       public static String VENDOR_CONTACT = "vendor@belk.com";
       public static int PRIORITY_START_VALUE = 1;
       public static String ATTR_PRODUCT_NAME = "Product_Name";
       public static String ATTR_PRODUCT_DESCR = "Product_Description";

       // PIM IMAGE FLOW CONSTANTS
       public static final String NO_COLOR="NO COLOR";
       public static final String DEFAULT_COLOR_CODE="000";
       public static final String PIMIMAGESTAG="Images";
       public static final String PIMDELETEDIMGTAG="Deleted_Images";
       public static final String PIMSTYLE_COLOR_ATTR="Pim_Style_Color_Attr_List";
	   public static final Object IS_STYLE_OR_PACK_SERVICE_HAS_NO_RESPONSE = "Style or Pack server does not retrun any vaules!!!";
	   public static final String NO_RESPONSE = "Service did not return any values!!!";
	   public static final String REMOVE_ATTR_DEPT="REMOVE_DEPT";
	   public static final String REMOVE_ATTR_CLASS="REMOVE_CLASS";
	   public static final String REMOVE_ATTR_PRODCUT_TYPE="REMOVE_PT";
	   public static final String PRODUCT = "PRODUCT";
	   public static final String STYLE = "Style";
	   public static final String PACK_COLOR="PackColor";
	   public static final String STYLE_COLOR="StyleColor";
	   public static final String ITEM_UDA_SPEC = "Item_UDA_Spec";
	   public static final String DIRECT_SHIP_FLAG = "Direct_Ship_Flag";
	   public static final String BOTH = "BOTH";
       public static final String PROCESS_CREATE = "CreateCar";
       public static final String PROCESS_UPDATE_ITEM = "UpdateCar";
       public static final String PROCESS_RETRIEVE_ITEM = "RetrieveCar";
       public static final String NA = "N/A";
       public static final String CHECKBOX = "CHECKBOX";
       public static final String VENDOR_STYLE_NUMBER_COLUMN = "VENDOR_STYLE_NUMBER";
       public static final String VENDOR_STYLE_NAME_COLUMN = "VENDOR_STYLE_NAME";
       public static final String DESCR = "DESCR";

       //Jira SUP-673
       public static String BELK_PINEVILLE_STUDIO="Belk Pineville Studio";
}
