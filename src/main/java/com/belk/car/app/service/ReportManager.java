/**
 * Class Name : ReportManager.java
 * 
 * Version Information : v1.0
 * 
 * Date : 
 * 
 * Copyright Notice : All rights are reserved to Syntel.
 */

package com.belk.car.app.service;

import java.util.List;

import org.appfuse.service.UniversalManager;

import com.belk.car.app.dto.DmmDTO;
import com.belk.car.app.dto.GmmDTO;
import com.belk.car.app.model.report.BaseReport;
import com.belk.car.app.model.report.CarSummaryData;
import com.belk.car.app.model.report.CarSummaryStatisticsData;
import com.belk.car.app.model.report.LateCarMonitoringReport;
import com.belk.car.app.model.report.ReportType;
import com.belk.car.app.model.view.AttributeView;
import com.belk.car.app.model.view.CarAttributeSummaryView;
import com.belk.car.app.model.view.CarAttributeView;
import com.belk.car.app.model.view.CarCmpStatusView;
import com.belk.car.app.model.view.CarExecutiveSummaryView;
import com.belk.car.app.model.view.CarPendingImagesView;
import com.belk.car.app.model.view.DropshipReportFM;
import com.belk.car.app.model.view.VendorStyleHexView;

/**
 * Manager Class to declare the service layered methods for reports.
 * 
 * @author AFUSY13
 */
public interface ReportManager extends UniversalManager {

	List<CarSummaryData> getCarSummary(BaseReport report);
	
	/* Methods added for Late CAR monitoring report */
	
	/**
	 * @param BaseReport report
	 * @return List<LateCarMonitoringData>
	 */
	LateCarMonitoringReport getLateCarMonitoringReport(BaseReport report);
	
	List<CarExecutiveSummaryView> getCarExecutiveSummary(BaseReport report);

	List<CarSummaryStatisticsData> getCarSummaryStatistics(BaseReport report);

	List<CarPendingImagesView> getCarPendingImages(BaseReport report);

	List<CarCmpStatusView> getCarCmpStatus(BaseReport report);

	List<VendorStyleHexView> getVendorStyleHexValues();

	List<CarAttributeView> getCarAttributeValues(BaseReport report);

	List<CarAttributeSummaryView> getCarAttributeSummaryValues(BaseReport report);

	List<AttributeView> getAttributes();

	List<ReportType> getReportTypes();

	ReportType getReportType(String code);

	// ==================== New Method Added by Syntel For Dropship
	// ===============//
	/**
	 * @param BaseReport report
	 * @return List<DropshipReportFM>
	 */
	List<DropshipReportFM> getVendorSetupByMonthData(BaseReport report);

	/**
	 * @param BaseReport report
	 * @return List<DropshipReportFM>
	 */
	List<DropshipReportFM> getOMAStylesSKUsData(BaseReport report);

	/**
	 * @param BaseReport report
	 * @return List<DropshipReportFM>
	 */
	List<DropshipReportFM> getOMAHandlingFeesData(BaseReport report);

	/**
	 * @param BaseReport report
	 * @return List<DropshipReportFM>
	 */
	List<DropshipReportFM> getOMARestockingFeesData(BaseReport report);

	/**
	 * @param BaseReport report
	 * @return List<DropshipReportFM>
	 */
	List<DropshipReportFM> getOMAHandlingFeeExceptionsData(BaseReport report);

	/**
	 * @param BaseReport report
	 * @return List<DropshipReportFM>
	 */
	List<DropshipReportFM> getOMACostExceptionsData(BaseReport report);
	
	/**
	 * @author afusy45
	 * @return void
	 */
	void deleteTmpSKUAvailTable();
	
	/**
	 * @author afusy45
	 * Method to create temp table
	 */
	void createTmpSkuAvail(String avail_query);
}
