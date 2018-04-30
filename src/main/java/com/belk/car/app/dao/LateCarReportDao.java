/**
 * Class Name : ReportDao.java 
 * Version Information : v1.0 
 * Date : 12/01/2009 
 * Copyright Notice : All rights are reserved to Syntel.
 */

package com.belk.car.app.dao;

import org.appfuse.dao.UniversalDao;

import com.belk.car.app.model.report.BaseReport;
import com.belk.car.app.model.report.LateCarMonitoringReport;


/**
 * Dao layer interface for late cars monitoring report
 * @author afusy12
 *
 */

public interface LateCarReportDao extends UniversalDao {
	
	
	/**
	 * Retrieve list of Late Car Count and details.
	 */
	LateCarMonitoringReport  getLateCarMonitoringReport(BaseReport report);


}