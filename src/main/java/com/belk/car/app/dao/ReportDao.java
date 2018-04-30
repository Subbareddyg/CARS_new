/**
 * Class Name : ReportDao.java 
 * Version Information : v1.0 
 * Date : 12/01/2009 
 * Copyright Notice : All rights are reserved to Syntel.
 */

package com.belk.car.app.dao;

import java.util.List;

import org.appfuse.dao.UniversalDao;

import com.belk.car.app.model.report.BaseReport;
import com.belk.car.app.model.report.CarSummaryData;
import com.belk.car.app.model.report.CarSummaryStatisticsData;

import com.belk.car.app.model.view.AttributeView;
import com.belk.car.app.model.view.CarAttributeSummaryView;
import com.belk.car.app.model.view.CarAttributeView;
import com.belk.car.app.model.view.CarCmpStatusView;
import com.belk.car.app.model.view.CarExecutiveSummaryView;
import com.belk.car.app.model.view.CarPendingImagesView;
import com.belk.car.app.model.view.OMACostExceptions;
import com.belk.car.app.model.view.OMAHandlingFeeExceptions;
import com.belk.car.app.model.view.OMAHandlingFees;
import com.belk.car.app.model.view.OMARestockingFees;
import com.belk.car.app.model.view.OMAStylesSKUs;
import com.belk.car.app.model.view.VendorSetupByMonth;
import com.belk.car.app.model.view.VendorStyleHexView;

/**
 * An interface to declare the method signatures for Reports which performs
 * database operations.
 * 
 * @author AFUSY13
 */
public interface ReportDao extends UniversalDao {

	List<CarSummaryData> getCarSummary(BaseReport report);

	List<CarExecutiveSummaryView> getCarExecutiveSummary(BaseReport report);

	List<CarSummaryStatisticsData> getCarSummaryStatistics(BaseReport report);

	List<CarPendingImagesView> getCarPendingImages(BaseReport report);

	List<CarCmpStatusView> getCarCmpStatus(BaseReport report);

	List<VendorStyleHexView> getVendorStyleHexValues();

	List<CarAttributeView> getCarAttributeValues(BaseReport report);

	List<AttributeView> getAttributes();

	List<CarAttributeSummaryView> getCarAttributeSummaryValues(BaseReport report);

	// ==================== New Method Added by Syntel For Dropship
	// ===============//
	/**
	 * @param report
	 * @return List of VendorSetupByMonth
	 */
	List<VendorSetupByMonth> getVendorSetupByMonth(BaseReport report);

	/**
	 * @param report
	 * @return List of OMAStylesSKUs
	 */
	List<OMAStylesSKUs> getOMAStylesSKUs(BaseReport report);

	/**
	 * @param report
	 * @return List of OMAHandlingFees
	 */
	List<OMAHandlingFees> getOMAHandlingFees(BaseReport report);

	/**
	 * @param report
	 * @return List of OMARestockingFees
	 */
	List<OMARestockingFees> getOMARestockingFees(BaseReport report);

	/**
	 * @param report
	 * @return List of OMAHandlingFeeExceptions
	 */
	List<OMAHandlingFeeExceptions> getOMAHandlingFeeExceptions(BaseReport report);

	/**
	 * @param report
	 * @return List of OMACostExceptions
	 */
	List<OMACostExceptions> getOMACostExceptions(BaseReport report);


	/**
	 * @author afusy45
	 * Method to drop TMPSKUAvail table
	 * CAR CMP status report enhancement
	 */
	void deleteTmpSKUAvailTable();
	
	/**
	 * @author afusy45
	 * Method to create Temp Table
     * CAR CMP status report enhancement
	 */
	
	void createTmpSkuAvail(String avail_query);

}
