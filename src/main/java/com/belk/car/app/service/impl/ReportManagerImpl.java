/**
 * Class Name : ReportManagerImpl.java
 * 
 * Version Information : v1.0
 * 
 * Date : 
 * 
 * Copyright Notice : All rights are reserved to Syntel.
 */

package com.belk.car.app.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.appfuse.service.impl.UniversalManagerImpl;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.dao.LateCarReportDao;
import com.belk.car.app.dao.ReportDao;
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
import com.belk.car.app.model.view.DropshipReportFS;
import com.belk.car.app.model.view.DropshipVendorView;
import com.belk.car.app.model.view.OMACostExceptions;
import com.belk.car.app.model.view.OMACostExceptionsView;
import com.belk.car.app.model.view.OMAHandlingFeeExceptions;
import com.belk.car.app.model.view.OMAHandlingFeeExceptionsView;
import com.belk.car.app.model.view.OMAHandlingFees;
import com.belk.car.app.model.view.OMAHandlingFeesView;
import com.belk.car.app.model.view.OMARestockingFees;
import com.belk.car.app.model.view.OMARestockingFeesView;
import com.belk.car.app.model.view.OMAStylesSKUs;
import com.belk.car.app.model.view.VendorSetupByMonth;
import com.belk.car.app.model.view.VendorStyleHexView;
import com.belk.car.app.service.ReportManager;

/**
 * Implements the ReportManager interface.
 * 
 * @author vsundar
 */
public class ReportManagerImpl extends UniversalManagerImpl
		implements
			ReportManager {

	
	private ReportDao reportDao;
	
	private LateCarReportDao lateCarReportDao;
	

	public void setReportDao(ReportDao reportDao) {
		this.reportDao = reportDao;
	}


	public LateCarReportDao getLateCarReportDao() {
		return lateCarReportDao;
	}


	public void setLateCarReportDao(LateCarReportDao lateCarReportDao) {
		this.lateCarReportDao = lateCarReportDao;
	}


	public List<CarSummaryData> getCarSummary(BaseReport report) {
		return reportDao.getCarSummary(report);
	}
		
	public List<CarExecutiveSummaryView> getCarExecutiveSummary(
			BaseReport report) {
		return reportDao.getCarExecutiveSummary(report);
	}

	public List<CarPendingImagesView> getCarPendingImages(BaseReport report) {
		return reportDao.getCarPendingImages(report);
	}

	public List<CarCmpStatusView> getCarCmpStatus(BaseReport report) {
		return reportDao.getCarCmpStatus(report);
	}

	public List<CarSummaryStatisticsData> getCarSummaryStatistics(
			BaseReport report) {
		return reportDao.getCarSummaryStatistics(report);
	}

	public List<VendorStyleHexView> getVendorStyleHexValues() {
		return reportDao.getVendorStyleHexValues();
	}

	public List<CarAttributeView> getCarAttributeValues(BaseReport report) {
		return reportDao.getCarAttributeValues(report);
	}

	public List<CarAttributeSummaryView> getCarAttributeSummaryValues(
			BaseReport report) {
		return reportDao.getCarAttributeSummaryValues(report);
	}

	public List<AttributeView> getAttributes() {
		return reportDao.getAttributes();
	}

	// ==================== New Method Added by Syntel For Dropship
	// ===============//

	/**
	 * @param BaseReport report
	 * @return List<DropshipReportFM> This method is used to deal with data
	 *         layer and get the data for respective report and group the data
	 *         as per requirement.
	 */
	public List<DropshipReportFM> getVendorSetupByMonthData(BaseReport report) {
		List<VendorSetupByMonth> vendorSetupByMonthList = reportDao
				.getVendorSetupByMonth(report);
		return getDropshipReportFMList(vendorSetupByMonthList,
				DropShipConstants.VENDOR_SETUP_BY_MONTH);
	}

	/**
	 * @param BaseReport report
	 * @return List<DropshipReportFM> This method is used to deal with data
	 *         layer and get the data for respective report and group the data
	 *         as per requirement.
	 */
	public List<DropshipReportFM> getOMAStylesSKUsData(BaseReport report) {
		List<OMAStylesSKUs> omaStylesSkusList = reportDao
				.getOMAStylesSKUs(report);
		return getDropshipReportFMList(omaStylesSkusList,
				DropShipConstants.OMA_STYLES_SKUS);
	}

	/**
	 * @param BaseReport report
	 * @return List<DropshipReportFM> This method is used to deal with data
	 *         layer and get the data for respective report and group the data
	 *         as per requirement.
	 */
	public List<DropshipReportFM> getOMAHandlingFeesData(BaseReport report) {
		List<OMAHandlingFees> omaHandlingFeesList = reportDao
				.getOMAHandlingFees(report);
		List<DropshipReportFM> vendorFMList = getDropshipReportFMList(
				omaHandlingFeesList, DropShipConstants.OMA_HANDLING_FEES);
		setOMAHandlingFeesViewList(vendorFMList);
		return vendorFMList;
	}

	/**
	 * @param BaseReport report
	 * @return List<DropshipReportFM> This method is used to deal with data
	 *         layer and get the data for respective report and group the data
	 *         as per requirement.
	 */
	public List<DropshipReportFM> getOMARestockingFeesData(BaseReport report) {
		List<OMARestockingFees> omaRestockingFeesList = reportDao
				.getOMARestockingFees(report);
		List<DropshipReportFM> vendorFMList = getDropshipReportFMList(
				omaRestockingFeesList, DropShipConstants.OMA_RESTOCKING_FEES);
		setOMARestockingFeesViewList(vendorFMList);
		return vendorFMList;
	}

	/**
	 * @param BaseReport report
	 * @return List<DropshipReportFM> This method is used to deal with data
	 *         layer and get the data for respective report and group the data
	 *         as per requirement.
	 */
	public List<DropshipReportFM> getOMAHandlingFeeExceptionsData(
			BaseReport report) {
		List<OMAHandlingFeeExceptions> omaHandlingFeeExceList = reportDao
				.getOMAHandlingFeeExceptions(report);
		List<DropshipReportFM> vendorFMList = getDropshipReportFMList(
				omaHandlingFeeExceList,
				DropShipConstants.OMA_HANDLING_FEE_EXCEPTION);
		setHandlingFeeExceptionsViewList(vendorFMList);
		return vendorFMList;
	}

	/**
	 * @param BaseReport report
	 * @return List<DropshipReportFM> This method is used to deal with data
	 *         layer and get the data for respective report and group the data
	 *         as per requirement.
	 */
	public List<DropshipReportFM> getOMACostExceptionsData(BaseReport report) {
		List<OMACostExceptions> omaCostExceptionsList = reportDao
				.getOMACostExceptions(report);
		List<DropshipReportFM> vendorFMList = getDropshipReportFMList(
				omaCostExceptionsList, DropShipConstants.OMA_COST_EXCEPTION);
		setCostExceptionsViewList(vendorFMList);
		return vendorFMList;
	}

	/**
	 * @param vendorViewList
	 * @param childClassName
	 * @return List<DropshipReportFM> This method is used to Group Report Data
	 *         base on Fulfillment Method Name and Fulfillment Service Name.
	 */
	@SuppressWarnings("unchecked")
	private List<DropshipReportFM> getDropshipReportFMList(
			List vendorViewList, String childClassName) {

		List<DropshipReportFM> vendorFMList = new ArrayList<DropshipReportFM>();
		DropshipReportFM setupByMonthFM = null;
		DropshipReportFS setupByMonthFS = null;
		String curMethodName = "";
		String preMethodName = "";
		String curServiceName = "";
		String preServiceName = "";
		boolean fmNameChanged = false;
		boolean fsNameChanged = false;
		DropshipVendorView vendorView;
		Iterator iterator = vendorViewList.iterator();
		while (iterator.hasNext()) {

			vendorView = (DropshipVendorView) iterator.next();
			curMethodName = vendorView.getFullfilmentMethodName();
			/*
			 * Block to compare Vendor Fullfilment Method Name in order to
			 * construct new DropshipReportFM object if curMethodName is not
			 * equal to preMethodName
			 */
			if (!curMethodName.trim().equals(preMethodName.trim())) {
				if (fsNameChanged) {
					setupByMonthFM.setDropshipReportFS(setupByMonthFS);
					fsNameChanged = false;
					curServiceName = "";
					preServiceName = "";
				}
				if (fmNameChanged) {
					vendorFMList.add(setupByMonthFM);
					fmNameChanged = false;
				}
				setupByMonthFM = new DropshipReportFM();
				setupByMonthFM.setFullfilmentMethodName(curMethodName);
				preMethodName = curMethodName;
				fmNameChanged = true;
			}
			/*
			 * Block to compare Vendor Fullfilment Service Name in order to
			 * construct new DropshipReportFS object if curFSname is not equal
			 * to preFSname
			 */
			curServiceName = vendorView.getFullfilmentSeviceName();
			if (!curServiceName.trim().equals(preServiceName.trim())) {
				if (fsNameChanged) {
					setupByMonthFM.setDropshipReportFS(setupByMonthFS);
					fsNameChanged = false;
				}
				setupByMonthFS = new DropshipReportFS(childClassName);
				setupByMonthFS.setFullfilmentServiceName(curServiceName);
				preServiceName = curServiceName;
				fsNameChanged = true;
			}
			setCommonObject(setupByMonthFS, vendorView, childClassName);
		}
		if (fmNameChanged) {
			setupByMonthFM.setDropshipReportFS(setupByMonthFS);
			vendorFMList.add(setupByMonthFM);
			fmNameChanged = false;
			fsNameChanged = false;
		}

		return vendorFMList;
	}

	/**
	 * @param setupByMonthFS
	 * @param vendorView
	 * @param childClassName
	 * @return void This method is used to type cast Super class object to
	 *         Concrete class in order to put Concrete object into their
	 *         respective List defined in DropshipReportFS class.
	 */
	private void setCommonObject(
			DropshipReportFS dropshipReportFS, DropshipVendorView vendorView,
			String childClassName) {
		if (childClassName.equals(DropShipConstants.VENDOR_SETUP_BY_MONTH)) {
			dropshipReportFS
					.setVendorSetupByMonth((VendorSetupByMonth) vendorView);
		}
		else if (childClassName.equals(DropShipConstants.OMA_STYLES_SKUS)) {
			dropshipReportFS.setOmaStylesSKUs((OMAStylesSKUs) vendorView);
		}
		else if (childClassName.equals(DropShipConstants.OMA_HANDLING_FEES)) {
			dropshipReportFS.setOmaHandlingFees((OMAHandlingFees) vendorView);
		}
		else if (childClassName.equals(DropShipConstants.OMA_RESTOCKING_FEES)) {
			dropshipReportFS
					.setOmaRestockingFees((OMARestockingFees) vendorView);
		}
		else if (childClassName
				.equals(DropShipConstants.OMA_HANDLING_FEE_EXCEPTION)) {
			dropshipReportFS
					.setOmaHandlingFeeExceptions((OMAHandlingFeeExceptions) vendorView);
		}
		else if (childClassName.equals(DropShipConstants.OMA_COST_EXCEPTION)) {
			dropshipReportFS
					.setOmaCostExceptions((OMACostExceptions) vendorView);
		}
	}

	/**
	 * @param vendorFMList
	 * @return void This method is used to group report data based on Vendor
	 *         Name after grouping data based on Fulfillment Method Name and
	 *         Fulfillment Service Name for the report OMA Handling Fees by
	 *         Vendor.
	 */
	private void setOMAHandlingFeesViewList(List<DropshipReportFM> vendorFMList) {
		String curVendorName = "";
		String preVendorName = "";
		boolean venNameChnaged = false;
		OMAHandlingFeesView handlingFeesView = null;

		for (DropshipReportFM byMonthFM : vendorFMList) {
			for (DropshipReportFS byMonthFS : byMonthFM.getDropshipReportFS()) {
				curVendorName = "";
				preVendorName = "";
				venNameChnaged = false;
				for (OMAHandlingFees handlingFees : byMonthFS
						.getOmaHandlingFees()) {
					curVendorName = handlingFees.getVendorName();

					if (!curVendorName.trim().equals(preVendorName.trim())) {
						if (venNameChnaged) {
							byMonthFS.setOmaHandlingFeesView(handlingFeesView);
							venNameChnaged = false;
						}
						handlingFeesView = new OMAHandlingFeesView();
						handlingFeesView.setVendorName(curVendorName);
						handlingFeesView.setVendorNbr(handlingFees
								.getVendorNbr());
						venNameChnaged = true;
						preVendorName = curVendorName;

					}
					handlingFeesView.setOmaHandlingFees(handlingFees);
				}
				byMonthFS.setOmaHandlingFeesView(handlingFeesView);
			}
		}
	}

	/**
	 * @param vendorFMList
	 * @return void This method is used to group report data based on Vendor
	 *         Name after grouping data based on Fulfillment Method Name and
	 *         Fulfillment Service Name for the report OMA Re-stocking Fees by
	 *         Vendor.
	 */
	private void setOMARestockingFeesViewList(
			List<DropshipReportFM> vendorFMList) {
		String curVendorName = "";
		String preVendorName = "";
		boolean venNameChnaged = false;
		OMARestockingFeesView resockingFeesView = null;

		for (DropshipReportFM byMonthFM : vendorFMList) {
			for (DropshipReportFS byMonthFS : byMonthFM.getDropshipReportFS()) {
				curVendorName = "";
				preVendorName = "";
				venNameChnaged = false;
				for (OMARestockingFees restockingFees : byMonthFS
						.getOmaRestockingFees()) {
					curVendorName = restockingFees.getVendorName();

					if (!curVendorName.trim().equals(preVendorName.trim())) {
						if (venNameChnaged) {
							byMonthFS
									.setOmaRestockingFeesView(resockingFeesView);
							venNameChnaged = false;
						}
						resockingFeesView = new OMARestockingFeesView();
						resockingFeesView.setVendorName(curVendorName);
						resockingFeesView.setVendorNbr(restockingFees
								.getVendorNbr());
						venNameChnaged = true;
						preVendorName = curVendorName;

					}
					resockingFeesView.setOmaRetockingFees(restockingFees);
				}
				byMonthFS.setOmaRestockingFeesView(resockingFeesView);
			}
		}
	}

	/**
	 * @param vendorFMList
	 * @return void This method is used to group report data based on Vendor
	 *         Name after grouping data based on Fulfillment Method Name and
	 *         Fulfillment Service Name for the report OMA Cost Exceptions by
	 *         Vendor.
	 */

	private void setCostExceptionsViewList(List<DropshipReportFM> vendorFMList) {
		String curVendorName = "";
		String preVendorName = "";
		boolean venNameChnaged = false;
		OMACostExceptionsView costExceptionsView = null;

		for (DropshipReportFM byMonthFM : vendorFMList) {
			for (DropshipReportFS byMonthFS : byMonthFM.getDropshipReportFS()) {
				curVendorName = "";
				preVendorName = "";
				venNameChnaged = false;
				for (OMACostExceptions costExceptions : byMonthFS
						.getOmaCostExceptions()) {
					curVendorName = costExceptions.getVendorName();

					if (!curVendorName.trim().equals(preVendorName.trim())) {
						if (venNameChnaged) {
							byMonthFS
									.setOmaCostExceptionsView(costExceptionsView);
							venNameChnaged = false;
						}
						costExceptionsView = new OMACostExceptionsView();
						costExceptionsView.setVendorName(curVendorName);
						costExceptionsView.setVendorNbr(costExceptions
								.getVendorNbr());
						venNameChnaged = true;
						preVendorName = curVendorName;

					}
					costExceptionsView.setOmaCostException(costExceptions);
				}
				byMonthFS.setOmaCostExceptionsView(costExceptionsView);
			}
		}
	}

	/**
	 * @param vendorFMList
	 * @return void This method is used to group report data based on Vendor
	 *         Name after grouping data based on Fulfillment Method Name and
	 *         Fulfillment Service Name for the report OMA Handling Fee
	 *         Exceptions by Vendor.
	 */
	private void setHandlingFeeExceptionsViewList(
			List<DropshipReportFM> vendorFMList) {
		String curVendorName = "";
		String preVendorName = "";
		boolean venNameChnaged = false;
		OMAHandlingFeeExceptionsView handlingFeeExceptionsView = null;

		for (DropshipReportFM byMonthFM : vendorFMList) {
			for (DropshipReportFS byMonthFS : byMonthFM.getDropshipReportFS()) {
				curVendorName = "";
				preVendorName = "";
				venNameChnaged = false;
				for (OMAHandlingFeeExceptions handlingFeeExceptions : byMonthFS
						.getOmaHandlingFeeExceptions()) {
					curVendorName = handlingFeeExceptions.getVendorName();

					if (!curVendorName.trim().equals(preVendorName.trim())) {
						if (venNameChnaged) {
							byMonthFS
									.setOmaHandlingFeeExceView(handlingFeeExceptionsView);
							venNameChnaged = false;
						}
						handlingFeeExceptionsView = new OMAHandlingFeeExceptionsView();
						handlingFeeExceptionsView.setVendorName(curVendorName);
						handlingFeeExceptionsView
								.setVendorNbr(handlingFeeExceptions
										.getVendorNbr());
						venNameChnaged = true;
						preVendorName = curVendorName;

					}
					handlingFeeExceptionsView
							.setOmaHandlingFeeException(handlingFeeExceptions);
				}
				byMonthFS.setOmaHandlingFeeExceView(handlingFeeExceptionsView);
			}
		}
	}

	public List<ReportType> getReportTypes() {
		List<ReportType> reportTypes = new ArrayList<ReportType>();
		
		reportTypes.add(new ReportType(ReportType.CAR_EXECUTIVE_SUMMARY,
				"Car Executive Summary Report", "car_executive_summary", "xls",
				"Excel", "Car Executive Summary Report"));
		reportTypes.add(new ReportType(
				ReportType.CAR_INFO_BASED_ON_LAST_SEARCH,
				"Car Info Based On Last Search", "car_info", "xls", "Excel",
				"Car Listing Report"));
		reportTypes.add(new ReportType(ReportType.CAR_ATTRIBUTE_VALUE,
				"Car Attribute Report", "car_attr_value", "xls", "Excel",
				"Car Attribute Report"));
		reportTypes.add(new ReportType(ReportType.CAR_CMP_STATUS,
				"CAR --> CMP Status - Report", "car_cmp_status", "xls",
				"Excel", "Car CMP Status Report"));
		reportTypes.add(new ReportType(
				ReportType.CAR_SAMPLE_COORDINATOR_TEMPLATE,
				"CAR Generate Sample Coordinator Template",
				"car_sample_coordinator_template", "xls", "Excel",
				"CAR Generate Sample Coordinator Template"));
		
		// Added for late car monitoring report by Syntel
	/*	reportTypes.add(new ReportType(ReportType.LATE_CAR_MONITORING_REPORT,
				"Late Car Monitoring Report", "late_car_monitoring_report", "xls",
				"Excel", "Late Car Monitoring Report"));*/
		

		// ==================== New Report Type Added by Syntel For Dropship
		// ===============//
		reportTypes.add(new ReportType(ReportType.OMA_VENDOR_SETUP_BY_MONTH,
				"Vendors Setup By Month ",
				"vendor_set_up_by_month", "xls", "Excel",
				"Vendors Setup By Month w/o Sub Total"));
		reportTypes.add(new ReportType(
				ReportType.OMA_VENDOR_SETUP_BY_MONTH_SUBTOTAL,
				"Vendors Setup By Month with Sub Total",
				"vendor_set_up_by_month_with_subtotal", "xls", "Excel",
				"Vendors Setup By Month with Sub Total"));
		reportTypes.add(new ReportType(ReportType.OMA_STYLES_SKUS,
				"OMA Style/SKUs", "oma_styles_skus", "xls",
				"Excel", "OMA Style/SKUs w/o Sub Total"));
		reportTypes.add(new ReportType(ReportType.OMA_STYLES_SKUS_SUBTOTAL,
				"OMA Style/SKUs with Sub Total",
				"oma_styles_skus_with_subtotal", "xls", "Excel",
				"OMA Style/SKUs with Sub Total"));
		reportTypes.add(new ReportType(ReportType.OMA_HANDLING_FEES,
				"OMA Handling Fees ",
				"oma_handling_fees_by_vendor", "xls", "Excel",
				"OMA Handling Fees w/o Sub Total"));
		reportTypes.add(new ReportType(ReportType.OMA_HANDLING_FEES_SUBTOTAL,
				"OMA Handling Fees with Sub Total",
				"oma_handling_fees_by_vendor_with_subtotal", "xls", "Excel",
				"OMA Handling Fees with Sub Total"));
		reportTypes.add(new ReportType(ReportType.OMA_HANDLING_FEES_EXCEPTION,
				"OMA Handling Fee Exceptions ",
				"oma_handling_fee_exceptions", "xls", "Excel",
				"OMA Handling Fee Exceptions w/o Sub Total"));
		reportTypes.add(new ReportType(
				ReportType.OMA_HANDLING_FEES_EXCEPTION_SUBTOTAL,
				"OMA Handling Fee Exceptions with Sub Total",
				"oma_handling_fee_exceptions_with_subtotal", "xls", "Excel",
				"OMA Handling Fee Exceptions with Sub Total"));
		reportTypes.add(new ReportType(ReportType.OMA_COST_EXCEPTION,
				"OMA Cost Exceptions ", "oma_cost_exceptions",
				"xls", "Excel", "OMA Cost Exceptions w/o Sub Total"));
		reportTypes.add(new ReportType(ReportType.OMA_COST_EXCEPTION_SUBTOTAL,
				"OMA Cost Exceptions with Sub Total",
				"oma_cost_exceptions_with_subtotal", "xls", "Excel",
				"OMA Cost Exceptions with Sub Total"));
		reportTypes.add(new ReportType(ReportType.OMA_RESTOCKING_FEES,
				"OMA Restocking Fees ", "oma_restocking_fees",
				"xls", "Excel", "OMA Restocking Fees w/o Sub Total"));
		reportTypes.add(new ReportType(ReportType.OMA_RESTOCKING_FEES_SUBTOTAL,
				"OMA Restocking Fees with Sub Total",
				"oma_restocking_fees_with_subtotal", "xls", "Excel",
				"OMA Restocking Fees with Sub Total"));

		return reportTypes;
	}

	public ReportType getReportType(String code) {
		List<ReportType> reportTypes = this.getReportTypes();
		ReportType type = null;
		if (reportTypes != null && !reportTypes.isEmpty()) {
			for (ReportType reportType : reportTypes) {
				if (reportType.getCode().equals(code)) {
					type = reportType;
					break;
				}
			}
		}
		return type;
	}

	/**
	 * @author afusy45
	 * Method to drop TMP table
	 */
	public void deleteTmpSKUAvailTable(){
		reportDao.deleteTmpSKUAvailTable();
	}
	
	/**
	 * @author afusy45
	 * Method to create Temp Table
	 */
	public void createTmpSkuAvail(String avail_query){
		reportDao.createTmpSkuAvail(avail_query);
	}
	
	/**
	 * @author afusyq3-Syntel
	 * Method to fetch the late CAR details
	 */
	
	public LateCarMonitoringReport getLateCarMonitoringReport(BaseReport report){
		return lateCarReportDao.getLateCarMonitoringReport(report);
	}
	
	
}