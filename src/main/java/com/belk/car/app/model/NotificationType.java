package com.belk.car.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "NOTIFICATION_TYPE", uniqueConstraints = @UniqueConstraint(columnNames = "NOTIFICATION_TYPE_CD"))
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, include="all")
public class NotificationType extends BaseAuditableModel implements
		java.io.Serializable {

	public static String CHANGE_PASSWORD = "CHANGE_PASSWORD";
	public static String FORGOT_PASSWORD = "FORGOT_PASSWORD";
	public static String NEW_REGISTRATION = "NEW_REGISTRATION";
	public static String CAR_SUMMARY = "CAR_SUMMARY";
	public static String VENDOR_CAR_SUMMARY = "V_CAR_SUMMARY";
	public static String VENDOR_CAR_ESCALATION = "V_CAR_ESCALATION";
	public static String VENDOR_SAMPLE_ESCALATION = "V_SAMPLE_ESCALATION";
	public static String VENDOR_REGISTRATION = "V_REGISTRATION";
	public static String DEFAULT_EMAIL = "carsadmin@belk.com";
	public static String SYSTEM_FAILURE = "SYSTEM_FAILURE";
	public static String PIM_IMG_UPLOAD_FAIL = "PIM_IMG_UPLOAD_FAIL";
	public static String SKU_SYNCHJOB_FAILURE = "SKU_SYNCHJOB_FAILURE";
	public static String NEW_SIZERULES_ADDED = "NEW_SIZERULES_ADDED";
	public static String VENDOR_CATALOG_TRANSALTION_COMPLETE="CTL_TRNSLATION_CMPLT";
	public static String ITMRQST_PST_APPROVAL = "ITMRQST_PST_APPROVAL";
	public static String RQST_SKUPST_APPROVAL = "RQST_SKUPST_APPROVAL";
	public static String VENDOR_CATALOG_IMPORT_VALIDATION_ERR ="VC_IMP_VALIDATN_ERR";
	public static String VENDOR_CATALOG_IMPORT_PROCESSING_ERR = "VC_IMP_PROCESNG_ERR";
	public static String VENDOR_CATALOG_IMPORT_NOTIFICATION = "VC_IMP_NOTIFICATION";
	public static String NEW_VENDOR_CATALOG_READY_FOR_DATA_MAPPING = "NW_CTLG_DATA_MAPPING";
	public static String CAR_GENERATION = "CAR_GENERATION";
	public static String LATE_CAR_BUYER = "LATE_CAR_BUYER";
	public static String LATE_CAR_DMM = "LATE_CAR_DMM";
	public static String LATE_CAR_GMM = "LATE_CAR_GMM";
	public static String LATE_CAR_HEAD = "LATE_CAR_HEAD";
	public static String LATE_CAR_CEO = "LATE_CAR_CEO";
	
	public static String NEW_SIZES_ADDED="NEW_SIZES_ADDED";
	
	public static String ACTION_REQUESTED_FTP = "ACTION_REQUESTED_FTP";
	public static String ACTION_REQUIRED_CARS = "ACTION_REQUIRED_CARS";
	public static String VENDOR_MQ_FAILED = "VENDOR_MQ_FAILED";
	
	public static String CAR_FAILED_EXPORTRRD="CAR_FAILED_EXPORTRRD"; /**CARS-193/SUP-776 **/
	/**
	 * 
	 */
	private static final long serialVersionUID = -1439666288607388267L;
	private String code;
	private String name;
	private String descr;

	private String subject;
	private String fromEmailAddress;
	private String fromName;

	public NotificationType() {
	}

	@Id
	@Column(name = "NOTIFICATION_TYPE_CD", unique = true, nullable = false, length = 20)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCR", nullable = false, length = 200)
	public String getDescription() {
		return this.descr;
	}

	public void setDescription(String descr) {
		this.descr = descr;
	}

	@Column(name = "EMAIL_SUBJECT", nullable = false, length = 200)
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Column(name = "EMAIL_FROM_ADDR", nullable = false, length = 200)
	public String getFromEmailAddress() {
		return fromEmailAddress;
	}

	public void setFromEmailAddress(String fromEmailAddress) {
		this.fromEmailAddress = fromEmailAddress;
	}

	@Column(name = "EMAIL_FROM_NAME", nullable = false, length = 100)
	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

}
