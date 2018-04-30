package com.belk.car.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "CONFIG", uniqueConstraints = @UniqueConstraint(columnNames = "PARAM"))
public class Config extends BaseAuditableModel implements java.io.Serializable {
	
        public static final String UPDATE_HEADER_BY_SKUS = "UPDATE_HEADER_BY_SKUS";
	public static final String CEO_REPORT_EMAIL_LIST = "CEO_REPORT_EMAIL_LIST";
	public static final String CARS_HEAD_EMAIL_REPORT_LIST = "CARS_HEAD_REPORT_EMAIL_LIST";
	public static final String MAIL_SERVER = "MAIL_SERVER";
	public static final String RLR_IMPORT_DIRECTORY = "RLR_IMPORT_DIRECTORY";
	public static final String CAR_IMPORT_DIRECTORY = "CAR_IMPORT_DIRECTORY";
	public static final String CAR_FILE_INPROCESS_DIRECTORY_NAME = "CAR_FILE_INPROCESS_DIRECTORY_NAME";
	public static final String CAR_FILE_ERROR_DIRECTORY_NAME = "CAR_FILE_ERROR_DIRECTORY_NAME";
	public static final String CAR_IMPORT_FILENAME = "CAR_IMPORT_FILENAME";
	public static final String CAR_IMPORT_USER = "CAR_IMPORT_USER";
	public static final String RRD_FTP_HOST = "RRD_FTP_HOST";
	public static final String RRD_FTP_PASSWORD = "RRD_FTP_PASSWORD";
	public static final String RRD_FTP_USERNAME = "RRD_FTP_USERNAME";
	public static final String RRD_IMPORT_FILENAME = "RRD_IMPORT_FILENAME";
	public static final String RRD_IMPORT_DIRECTORY = "RRD_IMPORT_DIRECTORY";
	public static final String RRD_EXPORT_DIRECTORY = "RRD_EXPORT_DIRECTORY";
	public static final String RRD_LOCAL_IMPORT_DIRECTORY = "RRD_LOCAL_IMPORT_DIRECTORY";
	public static final String RRD_LOCAL_EXPORT_DIRECTORY = "RRD_LOCAL_EXPORT_DIRECTORY";
	public static final String RRD_FILE_COMPLETED_DIRECTORY_NAME = "RRD_FILE_COMPLETED_DIRECTORY_NAME";
	public static final String RRD_LAST_TRANSFER_TIME = "RRD_LAST_TRANSFER_TIME";
	public static final String SYSTEM_USER = "SYSTEM_USER";
	public static final String SEND_EMAIL_USER = "SEND_EMAIL_USER";
	public static final String CAR_EXPORT_DIRECTORY = "CAR_EXPORT_DIRECTORY";
	public static final String CAR_EXPORT_FILENAME = "CAR_EXPORT_FILENAME";
	public static final String CAR_FILE_COMPLETED_DIRECTORY_NAME = "CAR_FILE_COMPLETED_DIRECTORY_NAME";
	public static final String INIT_NUMBER_OF_ESCALATION_DAYS= "INIT_NUMBER_OF_ESCALATION_DAYS";
	public static final String INIT_NUMBER_OF_DUE_DAYS= "INIT_NUMBER_OF_DUE_DAYS";
	public static final String CATALOG_UPLOAD_DIR = "CATALOG_UPLOAD_DIR";
	public static final String SEND_EMAIL_NOTIFICATIONS = "SEND_EMAIL_NOTIFICATIONS";
	public static final String CES_EXPORT_DIRECTORY = "CES_EXPORT_DIRECTORY";
	public static final String CES_EXPORT_FILENAME = "CES_EXPORT_FILENAME";
	public static final String CES_EXPORT_FTP_HOST = "CES_EXPORT_FTP_HOST";
	public static final String CES_EXPORT_FTP_USERNAME = "CES_EXPORT_FTP_USERNAME";
	public static final String CES_EXPORT_FTP_PASSWORD = "CES_EXPORT_FTP_PASSWORD";
	public static final String CES_EXPORT_FTP_REMOTE_DIRECTORY = "CES_EXPORT_FTP_REMOTE_DIRECTORY";

	public static final String HEXVALUE_EXPORT_DIRECTORY = "HEXVALUE_EXPORT_DIRECTORY";
	public static final String HEXVALUE_EXPORT_FILENAME = "HEXVALUE_EXPORT_FILENAME";
	public static final String HEXVALUE_EXPORT_FTP_HOST = "HEXVALUE_EXPORT_FTP_HOST";
	public static final String HEXVALUE_EXPORT_FTP_USERNAME = "HEXVALUE_EXPORT_FTP_USERNAME";
	public static final String HEXVALUE_EXPORT_FTP_PASSWORD = "HEXVALUE_EXPORT_FTP_PASSWORD";
	public static final String HEXVALUE_EXPORT_FTP_REMOTE_DIRECTORY = "HEXVALUE_EXPORT_FTP_REMOTE_DIRECTORY";

	public static final String TREND_EXPORT_DIRECTORY = "TREND_EXPORT_DIRECTORY";
	public static final String TREND_EXPORT_FILENAME = "TREND_EXPORT_FILENAME";
	public static final String TREND_EXPORT_FTP_HOST = "TREND_EXPORT_FTP_HOST";
	public static final String TREND_EXPORT_FTP_USERNAME = "TREND_EXPORT_FTP_USERNAME";
	public static final String TREND_EXPORT_FTP_PASSWORD = "TREND_EXPORT_FTP_PASSWORD";
	public static final String TREND_EXPORT_FTP_REMOTE_DIRECTORY = "TREND_EXPORT_FTP_REMOTE_DIRECTORY";
	public static final String TREND_EXCLUSION_VALUE = "TREND_EXCLUSION_VALUE";

	public static final String REPORT_TEMPLATE_DIRECTORY = "REPORT_TEMPLATE_DIRECTORY";
	public static final String REPORT_OUTPUT_DIRECTORY = "REPORT_OUTPUT_DIRECTORY";
	
	/**Item Request styles upload directory*/
	public static final String STYLE_FILE_UPLOAD_DIRECTORY = "STYLE_FILE_UPLOAD_DIRECTORY";
	/**
	 * Constants for IDB Feed For Dropship
	 */
	public static final String DEV_DROPSHIP_FTP_HOST = "DEV_DROPSHIP_FTP_HOST";
	public static final String DEV_DROPSHIP_FTP_USER = "DEV_DROPSHIP_FTP_USER";
	public static final String DEV_DROPSHIP_FTP_PSWD = "DEV_DROPSHIP_FTP_PSWD";
	public static final String DEV_DROPSHIP_IMPORT_DIR = "DEV_DROPSHIP_IMPORT_DIR";
	/*End*/
	
	public static final String SCHEDULED_PROCESS_ERROR_NOTIFICATION_LIST = "SCHEDULED_PROCESS_ERROR_NOTIFICATION_LIST";
	public static final String CATALOG_PROCESS_ERROR_NOTIFICATION_LIST = "CATALOG_PROCESS_ERROR_NOTIFICATION_LIST";

	//Items added for vendor image
	public static final String RRD_VENDORIMAGE_EXPORT_DIRECTORY="RRD_VENDORIMAGE_EXPORT_DIRECTORY";
	public static final String RRD_VENDORIMAGE_LOCAL_EXPORT_DIRECTORY = "RRD_VENDORIMAGE_LOCAL_EXPORT_DIRECTORY";
	public static final String VENDORIMAGE_UPDATEFEED_LAST_RUN_TIME = "VENDORIMAGE_UPDATEFEED_LAST_RUN_TIME";
	public static final String RRD_VENDORIMAGE_HISTORY_IMPORT_DIRECTORY= "RRD_VENDORIMAGE_HISTORY_IMPORT_DIRECTORY";
	public static final String RRD_VENDORIMAGE_HISTOY_LOCAL_IMPORT_DIRECTORY= "RRD_VENDORIMAGE_HISTOY_LOCAL_IMPORT_DIRECTORY";
	public static final String RRD_VENDORIMAGE_CHECK_FEEDBACK_IMPORT_DIRECTORY= "RRD_VENDORIMAGE_CHECK_FEEDBACK_IMPORT_DIRECTORY";
	public static final String RRD_VENDORIMAGE_CHECK_FEEDBACK_LOCAL_IMPORT_DIRECTORY= "RRD_VENDORIMAGE_CHECK_FEEDBACK_LOCAL_IMPORT_DIRECTORY";
	public static final String RRD_VENDORIMAGE_UPDATE_EXPORT_DIRECTORY = "RRD_VENDORIMAGE_UPDATE_EXPORT_DIRECTORY";
	public static final String RRD_VENDORIMAGE_UPDATE_LOCAL_EXPORT_DIRECTORY="RRD_VENDORIMAGE_UPDATE_LOCAL_EXPORT_DIRECTORY";
	
	
	//public static final String RRD_MC_TEMP_IMAGE_DOWNLOAD_DIR = "RRD_MC_TEMP_IMAGE_DOWNLOAD_DIR";
//	public static final String LOCAL_MC_TEMP_IMAGE_DOWNLOAD_DIR="LOCAL_MC_TEMP_IMAGE_DOWNLOAD_DIR";
//	public static final String BELK_TEMP_IMAGE_DOWNLOAD_DIR = "BELK_TEMP_IMAGE_DOWNLOAD_DIR";
	

	
	/*
	 * Constants for vendor expedited shipping export to BM
	 */
	public static final String VNDR_EXPEDITED_SHIPPING_DIRECTORY = "VNDR_EXPEDITED_SHIPPING_DIRECTORY";
	public static final String VNDR_EXPEDITED_SHIPPING_FILENAME = "VNDR_EXPEDITED_SHIPPING_FILENAME";
	public static final String VNDR_EXPEDITED_SHIPPING_FTP_HOST = "VNDR_EXPEDITED_SHIPPING_FTP_HOST";
	public static final String VNDR_EXPEDITED_SHIPPING_FTP_USERNAME = "VNDR_EXPEDITED_SHIPPING_FTP_USERNAME";
	public static final String VNDR_EXPEDITED_SHIPPING_FTP_PASSWORD = "VNDR_EXPEDITED_SHIPPING_FTP_PASSWORD";
	public static final String VNDR_EXPEDITED_SHIPPING_REMOTE_DIRECTORY = "VNDR_EXPEDITED_SHIPPING_REMOTE_DIRECTORY";
	
	//Added for the job - importing the MC Temporary images feed
	public static final String RRD_MC_TEMP_IMAGE_DOWNLOAD_DIR = "RRD_MC_TEMP_IMAGE_DOWNLOAD_DIR";
	public static final String LOCAL_MC_TEMP_IMAGE_DOWNLOAD_DIR="LOCAL_MC_TEMP_IMAGE_DOWNLOAD_DIR";
	public static final String BELK_TEMP_IMAGE_DOWNLOAD_DIR = "BELK_TEMP_IMAGE_DOWNLOAD_DIR";
	
	//Added for Facet Attributes Data cleanup
	public static final String BELK_FACET_ATTRIBUTE_LOCAL_DIR = "BELK_FACET_ATTRIBUTE_LOCAL_DIR";
	public static final String BELK_FACET_ATTRIBUTE_LOCAL_GENERATED_DIR = "BELK_FACET_ATTRIBUTE_LOCAL_GENERATED_DIR";
	
	 // Added for CARS Faceted Navigation
	public static final String COLORBMI_EXPORT_DIRECTORY = "COLORBMI_EXPORT_DIRECTORY";
	public static final String COLORBMI_EXPORT_FILENAME = "COLORBMI_EXPORT_FILENAME";
	public static final String SIZEBMI_EXPORT_DIRECTORY = "SIZEBMI_EXPORT_DIRECTORY";
	public static final String SIZEBMI_EXPORT_FILENAME = "SIZEBMI_EXPORT_FILENAME";
	public static final String BMI_EXPORT_FTP_HOST = "BMI_EXPORT_FTP_HOST";
	public static final String BMI_EXPORT_FTP_USERNAME = "BMI_EXPORT_FTP_USERNAME";
	public static final String BMI_EXPORT_FTP_PASSWORD = "BMI_EXPORT_FTP_PASSWORD";
	public static final String BMI_EXPORT_FTP_REMOTE_DIRECTORY = "BMI_EXPORT_FTP_REMOTE_DIRECTORY";
	public static final String BMI_FILE_COMPLETED_DIR_NAME = "BMI_FILE_COMPLETED_DIR_NAME";
	public static final String BMI_FILE_ERR_DIR_NAME = "BMI_FILE_ERR_DIR_NAME";
	public static final String SCHEDULED_PROCESS_ERROR_NOTIFICATION_LIST_BMIWRITE = "SCHEDULED_PROCESS_ERROR_NOTIFICATION_LIST_BMIWRITE";
	public static final String SCHEDULED_PROCESS_NEW_SIZE_NOTIFICATION_LIST = "SCHEDULED_PROCESS_NEW_SIZE_NOTIFICATION_LIST";
	public static final String SEND_SIZEJOB_EMAIL_NOTIFICATIONS = "SEND_SIZEJOB_EMAIL_NOTIFICATIONS";
	//Attribute Resynch
	public static final String ATTR_RESYNCH_BMI_EXPORT_DIRECTORY = "ATTR_RESYNCH_BMI_EXPORT_DIRECTORY";
	public static final String ATTR_RESYNCH_BMI_EXPORT_FILENAME = "ATTR_RESYNCH_BMI_EXPORT_FILENAME";
	public static final String BMI_EXPORT_FTP_ATTRIBUTE_REMOTE_DIRECTORY = "BMI_EXPORT_FTP_ATTRIBUTE_REMOTE_DIRECTORY";

	//SDF phase2
	public static final String VNDR_EXPEDSHIP_JOB_LASTRUN = "VNDR_EXPEDSHIP_JOB_LASTRUN";
	
	//CARS INFRASTRUTURE USERRANK JOB MIGRATION (ORACLE JOB to JAVA JOB)
	public static final String USERRANK_IMPORT_DIRECTORY = "USERRANK_IMPORT_DIRECTORY";
	public static final String USERRANK_FILE_INPROCESS_DIRECTORY_NAME = "USERRANK_FILE_INPROCESS_DIRECTORY_NAME";
	public static final String USERRANK_FILE_ERROR_DIRECTORY_NAME = "USERRANK_FILE_ERROR_DIRECTORY_NAME";
	public static final String USERRANK_IMPORT_FILENAME = "USERRANK_IMPORT_FILENAME";
	public static final String USERRANK_FILE_COMPLETED_DIRECTORY_NAME="USERRANK_FILE_COMPLETED_DIRECTORY_NAME";
	public static final String PWP_GWP_EXPORT_DIR ="PWP_GWP_EXPORT_DIR";
	public static final String PWP_GWP_EXPORT_FTP_HOST ="PWP_GWP_EXPORT_FTP_HOST";
	public static final String PWP_GWP_EXPORT_FTP_USER_ID ="PWP_GWP_EXPORT_FTP_USER_ID";
	public static final String PWP_GWP_EXPORT_FTP_PASSWORD ="PWP_GWP_EXPORT_FTP_PASSWORD";
	public static final String PWP_GWP_EXPORT_REMOTE_DIR ="PWP_GWP_EXPORT_REMOTE_DIR";
	
	
	public static final String SCHEDULED_PROCESS_CAR_DETAILS_EXPORT_RRD_FAILED = "SCHEDULED_PROCESS_CAR_DETAILS_EXPORT_RRD_FAILED" ; /**CARS-193/SUP-776 **/
	
	// Configs for PIM Integration
	public static final String PIM_INTEGRATION_CUTOVER_DATE = "PIM_INTEGRATION_CUTOVER_DATE";
	public static final String PIM_UPDATE_IGNORE_CLOSED_CARS = "PIM_UPDATE_IGNORE_CLOSED_CARS";
	public static final String PIM_UPDATE_LOCK_PATH = "PIM_UPDATE_LOCK_PATH";
	public static final String PIM_UPDATE_MAX_RECORDS = "PIM_UPDATE_MAX_RECORDS";
	public static final String PIM_UPDATE_MAX_THREADS = "PIM_UPDATE_MAX_THREADS";
	public static final String PIM_UPDATE_BATCH_SIZE = "PIM_UPDATE_BATCH_SIZE";
	public static final String PIM_UPDATE_DAILY_MAX_THRESHOLD = "PIM_UPDATE_DAILY_MAX_THRESHOLD";
    public static final String INVALID_BRAND_CHARACTERS = "INVALID_BRAND_CHARACTERS";
	//Constants related to PIMAttributeUpdatesFor Closed cars  
    public static final String PIM_ATTRIBUTE_UPDATE_EXPORT_DIRECTORY = "PIM_ATTRIBUTE_UPDATE_EXPORT_DIRECTORY";
    public static final String PIM_ATTRIBUTE_UPDATE_EXPORT_FILENAME = "PIM_ATTRIBUTE_UPDATE_EXPORT_FILENAME";
    public static final String PIM_ATTRIBUTE_UPDATE_EXPORT_FTP_HOST = "PIM_ATTRIBUTE_UPDATE_EXPORT_FTP_HOST";
    public static final String PIM_ATTRIBUTE_UPDATE_EXPORT_FTP_USERNAME = "PIM_ATTRIBUTE_UPDATE_EXPORT_FTP_USERNAME";
    public static final String PIM_ATTRIBUTE_UPDATE_EXPORT_FTP_PASSWORD = "PIM_ATTRIBUTE_UPDATE_EXPORT_FTP_PASSWORD";
    public static final String PIM_ATTRIBUTE_UPDATE_EXPORT_FTP_REMOTE_DIRECTORY = "PIM_ATTRIBUTE_UPDATE_EXPORT_FTP_REMOTE_DIRECTORY";
    public static final String PIM_ATTRIBUTE_UPDATE_DATA_FILE_COMPLETED_DIR_NAME = "PIM_ATTRIBUTE_UPDATE_DATA_FILE_COMPLETED_DIR_NAME";
        // Configs for multiple CARS-CMP feeds
        public static final String CMP_EXPORT_LOCK_PATH = "CMP_EXPORT_LOCK_PATH";
        public static final String ATTR_RESYNC_LOCK_PATH = "ATTR_RESYNC_LOCK_PATH";
        public static final String CLOSE_CARS_LOCK_PATH = "CLOSE_CARS_LOCK_PATH";
        public static final String CLOSED_CAR_PIM_ATTR_LOCK_PATH = "CLOSED_CAR_PIM_ATTR_LOCK_PATH";
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 260571687824491776L;
	private String param;
	private String value;

	public Config() {
	}


	@Id
	@Column(name = "PARAM", unique = true, nullable = false)
	public String getParam() {
		return this.param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	@Column(name = "VALUE", nullable = false)
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}

	@Column(name = "UPDATED_BY", nullable = false, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}
}
