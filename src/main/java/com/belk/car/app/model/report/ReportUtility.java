/**
 * Class Name : ReportUtility.java
 * 
 * Version Information : v1.0
 * 
 * Date : 
 * 
 * Copyright Notice :
 */
package com.belk.car.app.model.report;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;

/**
 * The ReportUtility class provides the utility operations required for report
 * objects
 * 
 * @author afusy13
 * 
 */
public class ReportUtility {

	
	private static final String VENDORS_SETUP_BY_MONTH_WITH_SUB_TOTAL = "Vendors Setup By Month with Sub Total";
	private static final String VENDORS_SETUP_BY_MONTH_W_O_SUB_TOTAL = "Vendors Setup By Month w/o Sub Total";

	/**
	 * Gets the Drop Ship Report Object with pre-filled required values.
	 * 
	 * @param request
	 * @param currentUser
	 * @param reportName
	 * @return dropshipReport
	 */
	public DropshipReport getDropShipReport(HttpServletRequest request,
			User currentUser, String reportName) {
		DropshipReport dropshipReport = new DropshipReport();
		setCommonReportProperties(dropshipReport, currentUser, reportName);
	
		if(reportName.equals(VENDORS_SETUP_BY_MONTH_W_O_SUB_TOTAL) || reportName.equals(VENDORS_SETUP_BY_MONTH_WITH_SUB_TOTAL)){
			addCriteria(dropshipReport, request, true);
		}
		return dropshipReport;
	}

	/**
	 * Sets the common Report Properties
	 * 
	 * @param baseReport
	 * @param currentUser
	 * @param reportName
	 */
	private void setCommonReportProperties(BaseReport baseReport,
			User currentUser, String reportName) {
		baseReport.setReportedBy(currentUser.getFullName());
		baseReport.setCurrentUser(currentUser);
		baseReport.setName(reportName);
		baseReport.setReportDate(Calendar.getInstance().getTime());
	}

	/**
	 * Adds the criteria required values to the base Report object
	 * 
	 * @param baseReport
	 * @param request
	 * @param dateCriteria
	 */
	private void addCriteria(BaseReport baseReport, HttpServletRequest request,
			boolean dateCriteria) {

		if (dateCriteria) {
			if (StringUtils.isNotBlank(request.getParameter("startDate"))) {
				baseReport.getCriteria().put("startDate",
						request.getParameter("startDate"));
			}
			if (StringUtils.isNotBlank(request.getParameter("endDate"))) {
				baseReport.getCriteria().put("endDate",
						request.getParameter("endDate"));
			}
		}
		/*
		 * if(dateCriteria){ baseReport.getCriteria().put("startDate",
		 * "12/07/2009") ; //baseReport.getCriteria().put("endDate",
		 * "12/08/2009") ;
		 * 
		 * }
		 */

	}

}
