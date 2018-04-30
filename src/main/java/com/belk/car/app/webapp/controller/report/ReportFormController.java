/**
 * Class Name : ReportFormController.java
 * 
 * Version Information : v1.0
 * 
 * Date : 12/18/2009
 * 
 * Copyright Notice : All rights are reserved to Syntel.
 */
package com.belk.car.app.webapp.controller.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarSearchCriteria;
import com.belk.car.app.model.Config;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.report.CarAttributeReport;
import com.belk.car.app.model.report.CarCmpStatusReport;
import com.belk.car.app.model.report.CarDetailReport;
import com.belk.car.app.model.report.CarExecutiveSummaryReport;
import com.belk.car.app.model.report.CarSummaryReport;
import com.belk.car.app.model.report.DropshipReport;
import com.belk.car.app.model.report.LateCarMonitoringReport;
import com.belk.car.app.model.report.ReportType;
import com.belk.car.app.model.report.ReportUtility;
import com.belk.car.app.model.view.CarExecutiveSummaryView;
import com.belk.car.app.service.ReportManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class ReportFormController extends BaseFormController {

	/**
	 * 
	 */
	private static final String WITH_SUB_TOTAL = "WithSubTotal";
	/**
	 * 
	 */
	private static final String OMA_ = "OMA_";
	/**
	 * 
	 */
	private static final String RADIO_BUTTON = "group1";
	/**
	 * 
	 */
	private static final String SUBTOTAL = "SUBTOTAL";

	public ReportFormController() {
		setCommandName("reportForm");
		setCommandClass(ReportForm.class);
	}

	private ReportManager reportManager ;
	
	public void setReportManager(ReportManager reportManager) {
		this.reportManager = reportManager;
	}

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {


		ReportType reportType = reportManager.getReportType(request.getParameter("reportType"));
		String subTotal=request.getParameter(RADIO_BUTTON);
		
		//If user has selected oma reports and selected the redio button for subtotal then append SUBTOTAL to the report code.
		if(subTotal!=null && reportType.getCode().indexOf(OMA_)>-1 ){
			if(WITH_SUB_TOTAL.equals(subTotal)){
				reportType = reportManager.getReportType(request.getParameter("reportType")+"_SUBTOTAL");	
			}
			
		}
		
		if (reportType != null) {
			
			Config templateConfig = (Config) this.getCarLookupManager().getById(Config.class, Config.REPORT_TEMPLATE_DIRECTORY) ;
			Config outputConfig = (Config) this.getCarLookupManager().getById(Config.class, Config.REPORT_OUTPUT_DIRECTORY) ;
			
			
	        XLSTransformer transformer = new XLSTransformer();
	        
	        String xlsTemplateFileName =  templateConfig.getValue() + reportType.getTemplateName() + "." + reportType.getTemplateExt();
	        String outputFileName =   outputConfig.getValue() + reportType.getTemplateName() + "_" + Calendar.getInstance().getTimeInMillis() +  "." + reportType.getTemplateExt();
	       // Uncomment the following code while deploying application locally.
	       //String xlsTemplateFileName =  "C:/Apps/data/reports/template/" + reportType.getTemplateName() + "." + reportType.getTemplateExt();
	       //String outputFileName =   "C:/Apps/data/reports/data/" + reportType.getTemplateName() + "_" + Calendar.getInstance().getTimeInMillis() +  "." + reportType.getTemplateExt();
	        
	        Map<String, Object> dataMap = new HashMap<String, Object>();
	        if (reportType.getCode().equals(ReportType.OPEN_CAR_SUMMARY_BY_WORKFLOW_STATUS)) {
		        CarSummaryReport summaryReport = new CarSummaryReport() ;
		        summaryReport.setReportDate(Calendar.getInstance().getTime()) ;
		        summaryReport.setName(reportType.getReportName()) ;
		        summaryReport.setReportedBy(this.getLoggedInUser().getFullName());
		        summaryReport.setCurrentUser(this.getLoggedInUser());
		        if (StringUtils.isNotBlank(request.getParameter("StartExpectedShipDate"))){
		        	summaryReport.getCriteria().put("StartExpectedShipDate", request.getParameter("StartExpectedShipDate")) ;
		        }
		        if (StringUtils.isNotBlank(request.getParameter("EndExpectedShipDate"))){
		        	summaryReport.getCriteria().put("EndExpectedShipDate", request.getParameter("EndExpectedShipDate")) ;
		        }
		        if (StringUtils.isNotBlank(request.getParameter("UserDepartmentOnly"))) {
		        	summaryReport.getCriteria().put("UserDepartmentOnly", request.getParameter("UserDepartmentOnly")) ;
		        }
		        //Added for archived CAR changes.
		        if(StringUtils.isNotBlank(request.getParameter("IncludeArchiveCars"))){
		        	summaryReport.getCriteria().put("IncludeArchiveCars", request.getParameter("IncludeArchiveCars")) ;
		        }
		        summaryReport.setSummaryData(reportManager.getCarSummary(summaryReport)) ;	
		        dataMap.put("reportData", summaryReport);
	        } 
	        
	     else  if (reportType.getCode().equals(ReportType.LATE_CAR_MONITORING_REPORT)) {
		        LateCarMonitoringReport lateCarReport = new LateCarMonitoringReport() ;
		        
		        lateCarReport.setReportDate(Calendar.getInstance().getTime());
		        lateCarReport.setName(reportType.getReportName()) ;
		        lateCarReport.setReportedBy(this.getLoggedInUser().getFullName());
		        lateCarReport.setCurrentUser(this.getLoggedInUser());
		        
		        if (StringUtils.isNotBlank(request.getParameter("StartDueDate"))){
		        	lateCarReport.getCriteria().put("StartDueDate", request.getParameter("StartDueDate")) ;
		        }
		        if (StringUtils.isNotBlank(request.getParameter("EndDueDate"))){
		        	lateCarReport.getCriteria().put("EndDueDate", request.getParameter("EndDueDate")) ;
		        }
		        if (StringUtils.isNotBlank(request.getParameter("UserDepartmentOnly"))) {
		        	lateCarReport.getCriteria().put("UserDepartmentOnly", request.getParameter("UserDepartmentOnly")) ;
		        }
		        if (StringUtils.isNotBlank(request.getParameter("DeptNumberText"))) {
		        	lateCarReport.getCriteria().put("DeptNumberText", request.getParameter("DeptNumberText")) ;
		        }
		        //Added for archived CAR changes.
		        if(StringUtils.isNotBlank(request.getParameter("IncludeArchiveCars"))){
		        	lateCarReport.getCriteria().put("IncludeArchiveCars", request.getParameter("IncludeArchiveCars")) ;
		        }
		        //Added for fetching the #late car's and #sku's at GMM and DMM level	
		        LateCarMonitoringReport reportData= reportManager.getLateCarMonitoringReport(lateCarReport);
		        lateCarReport.setDmmDTOData(reportData.getDmmDTOData());
		        lateCarReport.setGmmDTOData(reportData.getGmmDTOData());
		        lateCarReport.setLateCarMonitoringData(reportData.getLateCarMonitoringData()) ;
		        
		        dataMap.put("reportData", lateCarReport);
	        } 
	        else if (reportType.getCode().equals(ReportType.CAR_EXECUTIVE_SUMMARY)) {
		        CarExecutiveSummaryReport summaryReport = new CarExecutiveSummaryReport() ;
		        summaryReport.setReportDate(Calendar.getInstance().getTime()) ;
		        summaryReport.setName(reportType.getReportName()) ;
		        summaryReport.setReportedBy(this.getLoggedInUser().getFullName());
		        summaryReport.setCurrentUser(this.getLoggedInUser());
		        if (StringUtils.isNotBlank(request.getParameter("StartExpectedShipDate"))){
		        	summaryReport.getCriteria().put("StartExpectedShipDate", request.getParameter("StartExpectedShipDate")) ;
		        }
		        if (StringUtils.isNotBlank(request.getParameter("EndExpectedShipDate"))){
		        	summaryReport.getCriteria().put("EndExpectedShipDate", request.getParameter("EndExpectedShipDate")) ;
		        }
		        if (StringUtils.isNotBlank(request.getParameter("UserDepartmentOnly"))) {
		        	summaryReport.getCriteria().put("UserDepartmentOnly", request.getParameter("UserDepartmentOnly")) ;
		        }
		        if (StringUtils.isNotBlank(request.getParameter("IncludeClosedCars"))) {
		        	summaryReport.getCriteria().put("IncludeClosedCars", request.getParameter("IncludeClosedCars")) ;
		        }
		        //Added for archived CAR changes.
		        if(StringUtils.isNotBlank(request.getParameter("IncludeArchiveCars"))){
		        	summaryReport.getCriteria().put("IncludeArchiveCars", request.getParameter("IncludeArchiveCars")) ;
		        }
		        
		        summaryReport.setSummaryData(reportManager.getCarExecutiveSummary(summaryReport)) ;
		        List<CarExecutiveSummaryView> reportData = reportManager
				.getCarExecutiveSummary(summaryReport);
		        if (this.getLoggedInUser().getUserTypeCd()
								.equalsIgnoreCase(UserType.VENDOR)) {
				
					java.util.Iterator it = reportData.iterator();
				
					while (it.hasNext()) {
						CarExecutiveSummaryView reportCar = (CarExecutiveSummaryView) it
								.next();
						if (!(reportCar
								.getAssignedToUser().equalsIgnoreCase(
										UserType.VENDOR))) {
							reportCar.setAssignedToUser(" ");
							reportCar.setWorkflowStatus(" ");
						}
					}
				}
		        summaryReport.setSummaryData(reportData);
		        dataMap.put("reportData", summaryReport);
	        } 
	        else if (reportType.getCode().equals(ReportType.CAR_SAMPLE_COORDINATOR_TEMPLATE)) {
		        CarExecutiveSummaryReport summaryReport = new CarExecutiveSummaryReport() ;
		        summaryReport.setReportDate(Calendar.getInstance().getTime()) ;
		        summaryReport.setName(reportType.getReportName()) ;
		        summaryReport.setReportedBy(this.getLoggedInUser().getFullName());
		        summaryReport.setCurrentUser(this.getLoggedInUser());
		        if (StringUtils.isNotBlank(request.getParameter("StartExpectedShipDate"))){
		        	summaryReport.getCriteria().put("StartExpectedShipDate", request.getParameter("StartExpectedShipDate")) ;
		        }
		        if (StringUtils.isNotBlank(request.getParameter("EndExpectedShipDate"))){
		        	summaryReport.getCriteria().put("EndExpectedShipDate", request.getParameter("EndExpectedShipDate")) ;
		        }
		        if (StringUtils.isNotBlank(request.getParameter("StartTurninDate"))){
		        	summaryReport.getCriteria().put("StartTurninDate", request.getParameter("StartTurninDate")) ;
		        }
		        if (StringUtils.isNotBlank(request.getParameter("EndTurninDate"))){
		        	summaryReport.getCriteria().put("EndTurninDate", request.getParameter("EndTurninDate")) ;
		        }
		        if (StringUtils.isNotBlank(request.getParameter("UserDepartmentOnly"))) {
		        	summaryReport.getCriteria().put("UserDepartmentOnly", request.getParameter("UserDepartmentOnly")) ;
		        }
		        if (StringUtils.isNotBlank(request.getParameter("IncludeClosedCars"))) {
		        	summaryReport.getCriteria().put("IncludeClosedCars", request.getParameter("IncludeClosedCars")) ;
		        }
		        //Added for archived CAR changes.
		        if(StringUtils.isNotBlank(request.getParameter("IncludeArchiveCars"))){
		        	summaryReport.getCriteria().put("IncludeArchiveCars", request.getParameter("IncludeArchiveCars")) ;
		        }
		        
		        summaryReport.setSummaryData(reportManager.getCarExecutiveSummary(summaryReport)) ;	
		        dataMap.put("reportData", summaryReport);
	        } 
	        else if (reportType.getCode().equals(ReportType.OPEN_CAR_DETAIL)) {
		        CarDetailReport detailReport = new CarDetailReport() ;
		        detailReport.setReportDate(Calendar.getInstance().getTime()) ;
		        detailReport.setName(reportType.getReportName()) ;
		        detailReport.setReportedBy(this.getLoggedInUser().getFullName());
		        detailReport.setCurrentUser(this.getLoggedInUser());
		        CarSearchCriteria searchCriteria= new CarSearchCriteria() ;
		        searchCriteria.setStatusCd(Status.ACTIVE) ;
		        searchCriteria.setIgnoreUser(true) ;
		        if (StringUtils.isNotBlank(request.getParameter("StartExpectedShipDate"))) {
		        	detailReport.getCriteria().put("StartExpectedShipDate", request.getParameter("StartExpectedShipDate")) ;
		        	searchCriteria.setExpShipDateFrom(request.getParameter("StartExpectedShipDate"));
		        }
		        
		        if (StringUtils.isNotBlank(request.getParameter("EndExpectedShipDate"))) {
		        	detailReport.getCriteria().put("EndExpectedShipDate", request.getParameter("EndExpectedShipDate")) ;
		        	searchCriteria.setExpShipDateTo(request.getParameter("EndExpectedShipDate"));
		        }

		        if (StringUtils.isNotBlank(request.getParameter("UserDepartmentOnly"))) {
		        	detailReport.getCriteria().put("UserDepartmentOnly", request.getParameter("UserDepartmentOnly")) ;
		        	searchCriteria.setIgnoreUser(!("true".equals(request.getParameter("UserDepartmentOnly"))));
		        	if (!searchCriteria.isIgnoreUser()) {
		        		searchCriteria.setUser(this.getLoggedInUser());
		        	}
		        }

		        detailReport.setCars(this.getCarManager().searchCars(searchCriteria)) ;	
		        dataMap.put("reportData", detailReport);
	        } 
	        else if (reportType.getCode().equals(ReportType.CAR_ATTRIBUTE_VALUE)) {
		        CarAttributeReport attrReport = new CarAttributeReport() ;
		        attrReport.setReportDate(Calendar.getInstance().getTime()) ;
		        attrReport.setName(reportType.getReportName()) ;
		        attrReport.setReportedBy(this.getLoggedInUser().getFullName());
		        attrReport.setCurrentUser(this.getLoggedInUser());
		        if (StringUtils.isNotBlank(request.getParameter("StartExpectedShipDate"))) {
		        	attrReport.getCriteria().put("StartExpectedShipDate", request.getParameter("StartExpectedShipDate")) ;
		        }
		        
		        if (StringUtils.isNotBlank(request.getParameter("EndExpectedShipDate"))) {
		        	attrReport.getCriteria().put("EndExpectedShipDate", request.getParameter("EndExpectedShipDate")) ;
		        }

		        if (StringUtils.isNotBlank(request.getParameter("UserDepartmentOnly"))) {
		        	attrReport.getCriteria().put("UserDepartmentOnly", request.getParameter("UserDepartmentOnly")) ;
		        }
		        //Added for archived CAR changes.
		        if(StringUtils.isNotBlank(request.getParameter("IncludeArchiveCars"))){
		        	attrReport.getCriteria().put("IncludeArchiveCars", request.getParameter("IncludeArchiveCars")) ;
		        }
		        if (StringUtils.isNotBlank(request.getParameter("attributeName"))) {
			        attrReport.getCriteria().put("AttributeName", request.getParameter("attributeName")) ;
		        } else {
			        attrReport.getCriteria().put("AttributeName", "Trend") ;
		        }

		        attrReport.setCarAttributeList(this.reportManager.getCarAttributeValues(attrReport)) ;	
		        attrReport.setCarAttributeSummaryList(this.reportManager.getCarAttributeSummaryValues(attrReport)) ;	
		        dataMap.put("reportData", attrReport);
	        } 
	        else if (reportType.getCode().equals(ReportType.CAR_INFO_BASED_ON_LAST_SEARCH)) {
	        	CarDetailReport detailReport = new CarDetailReport() ;
	        	List<Car> carList=new ArrayList<Car>();
		        detailReport.setReportDate(Calendar.getInstance().getTime()) ;
		        detailReport.setName(reportType.getReportName()) ;
		        detailReport.setReportedBy(this.getLoggedInUser().getFullName());
		        detailReport.setCurrentUser(this.getLoggedInUser());

	        	CarSearchCriteria searchCriteria = (CarSearchCriteria) request.getSession(true).getAttribute("lastSearchCriteria") ;
		        if (searchCriteria == null) {
		        	detailReport.getCriteria().put("Search Criteria", "No Search criteria provided, please perform a search before executing this report!!!") ;
			        detailReport.setCars(new ArrayList<Car>()) ;	
	        	} else {
	        		searchCriteria.setReportName(reportType.getReportName());
		        	detailReport.getCriteria().put("Search Criteria", searchCriteria) ;
		        	List<Object> lastSearchList=this.getCarManager().searchCarsForLastSearchReport(searchCriteria);
		 		    if(lastSearchList != null){
		        	for(Object lastSearchObj:lastSearchList){
		 		    	Car searchCar = null;
		 		    	searchCar= (Car) Array.get(lastSearchObj, 0);
		 		    	String colorName=(String)Array.get(lastSearchObj, 1);
		 		    	searchCar.setColorName(colorName);
		 		    	if (this.getLoggedInUser().getUserTypeCd().equalsIgnoreCase(UserType.VENDOR)) {
		 		    	if (!( searchCar.getAssignedToUserType().getUserTypeCd().equalsIgnoreCase(UserType.VENDOR))) {
								//searchCar.getAssignedToUserType().setUserTypeCd(" ");
								//searchCar.getWorkFlow().setName(" ");
								searchCar.setAssignedToUserType(null);
								searchCar.setCurrentWorkFlowStatus(null);
							}
		        	   } 
		 		    	carList.add(searchCar);
		 		     } 
		           }
		        	  detailReport.setCars(carList) ;	
	        	}
		       dataMap.put("reportData", detailReport);
	         } 
	        /*  Following code is required when car summary statistics report is to be pulled.
	         	else if (reportType.getCode().equals(ReportType.OPEN_CAR_SUMMARY_STATISTICS)) {
		        CarSummaryStatisticsReport summaryReport = new CarSummaryStatisticsReport() ;
		        summaryReport.setReportDate(Calendar.getInstance().getTime()) ;
		        summaryReport.setName(reportType.getReportName()) ;
		        summaryReport.setReportedBy(this.getLoggedInUser().getFullName());
		        summaryReport.setCurrentUser(this.getLoggedInUser());
		        if (StringUtils.isNotBlank(request.getParameter("StartExpectedShipDate"))) {
		        	summaryReport.getCriteria().put("StartExpectedShipDate", request.getParameter("StartExpectedShipDate")) ;
		        }
		        
		        if (StringUtils.isNotBlank(request.getParameter("EndExpectedShipDate"))) {
		        	summaryReport.getCriteria().put("EndExpectedShipDate", request.getParameter("EndExpectedShipDate")) ;
		        }

		        if (StringUtils.isNotBlank(request.getParameter("UserDepartmentOnly"))) {
		        	summaryReport.getCriteria().put("UserDepartmentOnly", request.getParameter("UserDepartmentOnly")) ;
		        }

		        summaryReport.setCarSummaryList(reportManager.getCarSummaryStatistics(summaryReport)) ;	
		        dataMap.put("reportData", summaryReport);	        	
	        } 
	        // Following code is required when car Pending Images report is to be pulled.
	        else if (reportType.getCode().equals(ReportType.CAR_PENDING_IMAGES)) {
	        	CarPendingImagesReport pendingImagesReport = new CarPendingImagesReport() ;
	        	pendingImagesReport.setReportDate(Calendar.getInstance().getTime()) ;
	        	pendingImagesReport.setName(reportType.getReportName()) ;
	        	pendingImagesReport.setReportedBy(this.getLoggedInUser().getFullName());
		        pendingImagesReport.setCurrentUser(this.getLoggedInUser());
		        if (StringUtils.isNotBlank(request.getParameter("StartExpectedShipDate"))) {
		        	pendingImagesReport.getCriteria().put("StartExpectedShipDate", request.getParameter("StartExpectedShipDate")) ;
		        }
		        
		        if (StringUtils.isNotBlank(request.getParameter("EndExpectedShipDate"))) {
		        	pendingImagesReport.getCriteria().put("EndExpectedShipDate", request.getParameter("EndExpectedShipDate")) ;
		        }
		        
		        if (StringUtils.isNotBlank(request.getParameter("UserDepartmentOnly"))) {
		        	pendingImagesReport.getCriteria().put("UserDepartmentOnly", request.getParameter("UserDepartmentOnly")) ;
		        }

		        pendingImagesReport.getCriteria().put("OnlyCompletedContent", "true") ;
		        pendingImagesReport.getCriteria().put("OnlyOpenCars", "true") ;

		        pendingImagesReport.setPendingImages(reportManager.getCarPendingImages(pendingImagesReport)) ;	
		        dataMap.put("reportData", pendingImagesReport);	        	
	        } */ 
	        else if (reportType.getCode().equals(ReportType.CAR_CMP_STATUS)) {
	        	CarCmpStatusReport carCmpStatusReport = new CarCmpStatusReport() ;
	        	carCmpStatusReport.setReportDate(Calendar.getInstance().getTime()) ;
	        	carCmpStatusReport.setName(reportType.getReportName()) ;
	        	carCmpStatusReport.setReportedBy(this.getLoggedInUser().getFullName());
		        carCmpStatusReport.setCurrentUser(this.getLoggedInUser());
		        if (StringUtils.isNotBlank(request.getParameter("StartExpectedShipDate"))) {
		        	carCmpStatusReport.getCriteria().put("StartExpectedShipDate", request.getParameter("StartExpectedShipDate")) ;
		        }
		        
		        if (StringUtils.isNotBlank(request.getParameter("EndExpectedShipDate"))) {
		        	carCmpStatusReport.getCriteria().put("EndExpectedShipDate", request.getParameter("EndExpectedShipDate")) ;
		        }
		        
		        if (StringUtils.isNotBlank(request.getParameter("UserDepartmentOnly"))) {
		        	carCmpStatusReport.getCriteria().put("UserDepartmentOnly", request.getParameter("UserDepartmentOnly")) ;
		        }
		        //Added for archived CAR changes.
		        if(StringUtils.isNotBlank(request.getParameter("IncludeArchiveCars"))){
		        	carCmpStatusReport.getCriteria().put("IncludeArchiveCars", request.getParameter("IncludeArchiveCars")) ;
		        }

		        carCmpStatusReport.setCarCmpStatus(reportManager.getCarCmpStatus(carCmpStatusReport)) ;	
		        dataMap.put("reportData", carCmpStatusReport);	        	
	        }
	       //==================== New Report Type Added by Syntel For Dropship ===============//
	        else if (reportType.getCode().equals(ReportType.OMA_VENDOR_SETUP_BY_MONTH)
	        		||reportType.getCode().equals(ReportType.OMA_VENDOR_SETUP_BY_MONTH_SUBTOTAL)) {
	        	
	        	ReportUtility reportUtility = new ReportUtility();
	        	DropshipReport dropshipReport = reportUtility.getDropShipReport(
	        														request,this.getLoggedInUser(), reportType.getReportName());
	        	dropshipReport.setDropshipReportData(reportManager.getVendorSetupByMonthData(dropshipReport));
	        	dataMap.put("reportData", dropshipReport);
	        }
	        else if (reportType.getCode().equals(ReportType.OMA_STYLES_SKUS)
	        		||reportType.getCode().equals(ReportType.OMA_STYLES_SKUS_SUBTOTAL)) {
	        	ReportUtility reportUtility = new ReportUtility();
	        	DropshipReport dropshipReport = reportUtility.getDropShipReport(
	        														request,this.getLoggedInUser(), reportType.getReportName());
	        	dropshipReport.setDropshipReportData(reportManager.getOMAStylesSKUsData(dropshipReport));
	        	dataMap.put("reportData", dropshipReport);
	        }
	        else if (reportType.getCode().equals(ReportType.OMA_RESTOCKING_FEES)
	        		||reportType.getCode().equals(ReportType.OMA_RESTOCKING_FEES_SUBTOTAL)) {
	        	ReportUtility reportUtility = new ReportUtility();
	        	DropshipReport dropshipReport = reportUtility.getDropShipReport(
	        														request,this.getLoggedInUser(), reportType.getReportName());
	        	dropshipReport.setDropshipReportData(reportManager.getOMARestockingFeesData(dropshipReport));
	        	dataMap.put("reportData", dropshipReport);
	        }
	        else if (reportType.getCode().equals(ReportType.OMA_HANDLING_FEES)
	        		||reportType.getCode().equals(ReportType.OMA_HANDLING_FEES_SUBTOTAL)) {
	        	ReportUtility reportUtility = new ReportUtility();
	        	DropshipReport dropshipReport = reportUtility.getDropShipReport(
	        														request,this.getLoggedInUser(), reportType.getReportName());
	        	dropshipReport.setDropshipReportData(reportManager.getOMAHandlingFeesData(dropshipReport));
	        	dataMap.put("reportData", dropshipReport);
	        }
	        else if (reportType.getCode().equals(ReportType.OMA_COST_EXCEPTION)
	        		||reportType.getCode().equals(ReportType.OMA_COST_EXCEPTION_SUBTOTAL)) {
	        	ReportUtility reportUtility = new ReportUtility();
	        	DropshipReport dropshipReport = reportUtility.getDropShipReport(
	        														request,this.getLoggedInUser(), reportType.getReportName());
	        	dropshipReport.setDropshipReportData(reportManager.getOMACostExceptionsData(dropshipReport));
	        	dataMap.put("reportData", dropshipReport);
	        }
	        else if (reportType.getCode().equals(ReportType.OMA_HANDLING_FEES_EXCEPTION)
	        		||reportType.getCode().equals(ReportType.OMA_HANDLING_FEES_EXCEPTION_SUBTOTAL)) {
	        	ReportUtility reportUtility = new ReportUtility();
	        	DropshipReport dropshipReport = reportUtility.getDropShipReport(
	        														request,this.getLoggedInUser(), reportType.getReportName());
	        	dropshipReport.setDropshipReportData(reportManager.getOMAHandlingFeeExceptionsData(dropshipReport));
	        	dataMap.put("reportData", dropshipReport);
	        }
	       
	        log.info("Transforming data to XLS ");
	        transformer.transformXLS(xlsTemplateFileName, dataMap, outputFileName);
	        transformer.setJexlInnerCollectionsAccess( true );
			File f1 = new File(outputFileName);
	
			if (f1.exists() && f1.canRead()) {
				// set the content type
				log.info(" Writing the XLS data to outputstream");
				response.setContentType("application/vnd.ms-excel");
				// set the header and also the Name by which user will be prompted to save
				
				/* added the cache control/pragma to private so that report can be downloaded from IE
				 * NOTE: IE cannot download the report from secure site (HTTPS) if cache-control is enabled or publicly accessed 
				*/
				response.setHeader("Cache-Control","private");
				response.setHeader("Pragma","private");
				response.setHeader("Content-Disposition", "attachment; filename=\""+reportType.getTemplateName()+"\"");
				
		
				// Open an input stream to the file and post the
				// file contents thru the
				// servlet output stream to the client m/c
		
				InputStream in = new FileInputStream(f1);
				byte[] bytearray = new byte[(int) f1.length()];
				ServletOutputStream outs = response.getOutputStream();
				
				in.read(bytearray);
				outs.write(bytearray);
				log.info(" Downloaded the report successfully ");
				outs.flush();
				outs.close();
				in.close();
			}
		}
        
		return null ;
	}

	
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

		ReportForm reportForm = new ReportForm();
		if (!isFormSubmission(request)) {
			
			return reportForm;
		} 
		return super.formBackingObject(request);
	}

	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws ServletRequestBindingException {
		Map<String, Object> model = new HashMap<String, Object>();
		List<ReportType> reportTypes=reportManager.getReportTypes();
	
		List<ReportType> reportTypesNew=new ArrayList<ReportType>();
		for(ReportType reportType:reportTypes){
			if(reportType.getCode().indexOf(SUBTOTAL)<=-1){
				//Removed Buyer restriction to see the report
				/*if (this.getLoggedInUser().getUserTypeCd().equals("BUYER")) {
					if (reportType.getCode().equals("CAR_EXECUTIVE_SUMMARY")
							|| reportType.getCode().equals("CAR_INFO_BASED_ON_LAST_SEARCH") 
							||  reportType.getCode().equals("LATE_CAR_MONITORING_REPORT") ) {
						reportTypesNew.add(reportType);
					}
				} else {*/
				
				if( !reportType.getCode().equals("LATE_CAR_MONITORING_REPORT")){
					reportTypesNew.add(reportType);
				}
				//}
			}
		}
	
		model.put("reportTypes", reportTypesNew);
		if (request.getSession().getAttribute("attributesCache")==null) {
			request.getSession().setAttribute("attributesCache", reportManager.getAttributes());
		}
		model.put("UserTypeCd", this.getLoggedInUser().getUserTypeCd());
		model.put("attributes", request.getSession().getAttribute("attributesCache"));
		return model;
	}
}
