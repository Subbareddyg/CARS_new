/**
 * Class Name : ReportType.java
 * 
 * Version Information : v1.0
 * 
 * Date : 12/15/2009
 * 
 * Copyright Notice :All rights are reserved to Syntel.
 */
package com.belk.car.app.model.report;

import java.io.Serializable;
/**
 * Class to list the available report types.
 * @author vsundar
 *
 */
public class ReportType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7164384047728264544L;

	public static final String OPEN_CAR_SUMMARY_BY_WORKFLOW_STATUS = "OPEN_CAR_SUMMARY_BY_WORKFLOW_STATUS";
	public static final String OPEN_CAR_SUMMARY_STATISTICS = "OPEN_CAR_SUMMARY_STATISTICS";
	public static final String OPEN_CAR_DETAIL = "OPEN_CAR_DETAIL";
	public static final String CAR_INFO_BASED_ON_LAST_SEARCH = "CAR_INFO_BASED_ON_LAST_SEARCH";
	public static final String CAR_ATTRIBUTE_VALUE = "CAR_ATTRIBUTE_VALUE";
	public static final String CAR_PENDING_IMAGES = "CAR_PENDING_IMAGES";
	public static final String CAR_CMP_STATUS = "CAR_CMP_STATUS";
	public static final String CAR_EXECUTIVE_SUMMARY = "CAR_EXECUTIVE_SUMMARY";
	public static final String CAR_SAMPLE_COORDINATOR_TEMPLATE = "CAR_SAMPLE_COORDINATOR_TEMPLATE";
	public static final String OMA_VENDOR_SETUP_BY_MONTH_SUBTOTAL = "OMA_VENDOR_SETUP_BY_MONTH_SUBTOTAL";
	public static final String OMA_VENDOR_SETUP_BY_MONTH = "OMA_VENDOR_SETUP_BY_MONTH";
	public static final String OMA_STYLES_SKUS_SUBTOTAL = "OMA_STYLES_SKUS_SUBTOTAL";
	public static final String OMA_STYLES_SKUS = "OMA_STYLES_SKUS";
	public static final String OMA_HANDLING_FEES = "OMA_HANDLING_FEES";
	public static final String OMA_HANDLING_FEES_SUBTOTAL = "OMA_HANDLING_FEES_SUBTOTAL";
	public static final String OMA_HANDLING_FEES_EXCEPTION = "OMA_HANDLING_FEES_EXCEPTION";
	public static final String OMA_HANDLING_FEES_EXCEPTION_SUBTOTAL = "OMA_HANDLING_FEES_EXCEPTION_SUBTOTAL";
	public static final String OMA_COST_EXCEPTION = "OMA_COST_EXCEPTION";
	public static final String OMA_COST_EXCEPTION_SUBTOTAL = "OMA_COST_EXCEPTION_SUBTOTAL";
	public static final String OMA_RESTOCKING_FEES = "OMA_RESTOCKING_FEES";
	public static final String OMA_RESTOCKING_FEES_SUBTOTAL = "OMA_RESTOCKING_FEES_SUBTOTAL";
	// Added for late monitoring report
	public static final String LATE_CAR_MONITORING_REPORT = "LATE_CAR_MONITORING_REPORT";
	
	
	private String code;
	private String name;
	private String templateName;
	private String templateExt;
	private String templateType ;
	private String reportName;

	public ReportType() {
	}

	public ReportType(String code, String name, String templateName, String templateExt, String templateType, String reportName) {
		super();
		this.code = code;
		this.name = name;
		this.templateName = templateName;
		this.templateExt = templateExt;
		this.templateType = templateType;
		this.reportName = reportName;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getTemplateExt() {
		return templateExt;
	}

	public void setTemplateExt(String templateExt) {
		this.templateExt = templateExt;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

}
