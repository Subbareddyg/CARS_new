/**
 * @author afusy07-Priyanka Gadia
 * @Date Jul 20, 2010
 * @TODO TODO
 */
package com.belk.car.app.webapp.controller;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.service.QuartzJobManager;
import com.belk.car.app.service.QuartzJobManagerForDropship;
import com.belk.car.app.service.QuartzJobManagerForEmailNotification;
import com.belk.car.app.service.QuartzJobManagerForImportAndUpdateFacets;
import com.belk.car.app.service.QuartzJobManagerForLateCarReport;
import com.belk.car.app.service.QuartzJobManagerForPIMJobs;
import com.belk.car.app.service.QuartzJobManagerForVendorCatalog;
import com.belk.car.app.service.QuartzJobManagerForVendorImages;
import com.belk.car.app.service.SizeConversionJobManager;
import com.belk.car.app.service.SuperColorJobManager;
import com.belk.car.app.service.AttributeResynchJobManager;
import com.belk.car.app.service.impl.CarPrefillManagerImpl;
import com.belk.car.app.service.impl.QuartzJobmanagerForMCImageImpl;


/**
 * @author afusy07
 *Jul 20, 2010TODO
 */
public class ScheduleJobController extends MultiActionController
implements DropShipConstants {

	private static transient final Log log = LogFactory.getLog(ScheduleJobController.class);
	private QuartzJobManagerForDropship quartzJobManagerDropship;
	private QuartzJobManagerForVendorCatalog quartzJobManagerForVendorCatalog;
	private CarPrefillManagerImpl carPrefillJobManager;
	private QuartzJobManager quartzJobManager;
	private QuartzJobManagerForEmailNotification quartzJobManagerForEmailNotification  ;
	private QuartzJobManagerForLateCarReport quartzJobManagerForLateCarReport;

	private QuartzJobManagerForImportAndUpdateFacets quartzJobManagerForImportAndUpdateFacets;

	private QuartzJobmanagerForMCImageImpl quartzJobManagerForMCImage;
//	private SizeConversionJobManager sizeConversionJobManager;
//	private SuperColorJobManager superColorJobManager;
	private AttributeResynchJobManager attributeResynchJobManager;
	private QuartzJobManagerForVendorImages quartzJobManagerForVendorImages;
	private QuartzJobManagerForPIMJobs quartzJobManagerForPIMJobs;
	
	
	/**
	 * @return the quartzJobManagerForEmailNotification
	 */
	public QuartzJobManagerForEmailNotification getQuartzJobManagerForEmailNotification() {
		return quartzJobManagerForEmailNotification;
	}
	/**
	 * @param quartzJobManagerForEmailNotification the quartzJobManagerForEmailNotification to set
	 */
	public void setQuartzJobManagerForEmailNotification(
			QuartzJobManagerForEmailNotification quartzJobManagerForEmailNotification) {
		this.quartzJobManagerForEmailNotification = quartzJobManagerForEmailNotification;
	}
	// Added for CARS Faceted Navigation
//	public SuperColorJobManager getSuperColorJobManager() {
//		return superColorJobManager;
//	}
//	public void setSuperColorJobManager(SuperColorJobManager superColorJobManager) {
//		this.superColorJobManager = superColorJobManager;
//	}
//	public SizeConversionJobManager getSizeConversionJobManager() {
//		return sizeConversionJobManager;
//	}
//	public void setSizeConversionJobManager(
//			SizeConversionJobManager sizeConversionJobManager) {
//		this.sizeConversionJobManager = sizeConversionJobManager;
//	}
	public AttributeResynchJobManager getAttributeResynchJobManager() {
		return attributeResynchJobManager;
	}
	public void setAttributeResynchJobManager(
			AttributeResynchJobManager attributeResynchJobManager) {
		this.attributeResynchJobManager = attributeResynchJobManager;
	}
	public QuartzJobManagerForLateCarReport getQuartzJobManagerForLateCarReport() {
		return quartzJobManagerForLateCarReport;
	}
	public void setQuartzJobManagerForLateCarReport(
			QuartzJobManagerForLateCarReport quartzJobManagerForLateCarReport) {
		this.quartzJobManagerForLateCarReport = quartzJobManagerForLateCarReport;
	}
	public QuartzJobManagerForDropship getQuartzJobManagerDropship() {
		return quartzJobManagerDropship;
	}
	public void setQuartzJobManagerDropship(QuartzJobManagerForDropship quartzJobManagerDropship) {
		this.quartzJobManagerDropship = quartzJobManagerDropship;
	}
	public QuartzJobManagerForVendorCatalog getQuartzJobManagerForVendorCatalog() {
		return quartzJobManagerForVendorCatalog;
	}
	public void setQuartzJobManagerForVendorCatalog(
			QuartzJobManagerForVendorCatalog quartzJobManagerForVendorCatalog) {
		this.quartzJobManagerForVendorCatalog = quartzJobManagerForVendorCatalog;
	}
	public CarPrefillManagerImpl getCarPrefillJobManager() {
		return carPrefillJobManager;
	}
	public void setCarPrefillJobManager(CarPrefillManagerImpl carPrefillJobManager) {
		this.carPrefillJobManager = carPrefillJobManager;
	}
	public QuartzJobManager getQuartzJobManager() {
		return quartzJobManager;
	}
	public void setQuartzJobManager(QuartzJobManager quartzJobManager) {
		this.quartzJobManager = quartzJobManager;
	}
	public void setQuartzJobManagerForMCImage(QuartzJobmanagerForMCImageImpl quartzJobManagerForMCImage) {
		this.quartzJobManagerForMCImage = quartzJobManagerForMCImage;
	}
	
	public void setQuartzJobManagerForImportAndUpdateFacets(QuartzJobManagerForImportAndUpdateFacets quartzJobManagerForImportAndUpdateFacets) {
		this.quartzJobManagerForImportAndUpdateFacets = quartzJobManagerForImportAndUpdateFacets;
	}
	public void setQuartzJobManagerForVendorImages(
			QuartzJobManagerForVendorImages quartzJobManagerForVendorImages) {
		this.quartzJobManagerForVendorImages = quartzJobManagerForVendorImages;
	}
	public void setQuartzJobManagerForPIMJobs(QuartzJobManagerForPIMJobs quartzJobManagerForPIMJobs) {
		this.quartzJobManagerForPIMJobs = quartzJobManagerForPIMJobs;
	}
	public ModelAndView load(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		return new ModelAndView("oma/scheduleJobs");
	}
	
	public ModelAndView importVendorCatalog(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("importVendorCatalog called");
		try{
			quartzJobManagerForVendorCatalog.importVendorCatalogData();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/scheduleJobs");
	}
	
	public ModelAndView createDropshipCars(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("Import Dropship CARS Job  called");
		try{
		quartzJobManagerDropship.importDropshipCars();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/scheduleJobs");
	}

	public ModelAndView loadVendorCatalogDataIntoCARS(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("loadVendorCatalogDataIntoCARS called");
		try{
			quartzJobManagerForVendorCatalog.loadVendorCatalogDataIntoCARS();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/scheduleJobs");
	}
	public ModelAndView prefillCars(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("prefill cars called");
		try{
			carPrefillJobManager.prefillCars();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/scheduleJobs");
	}
	public ModelAndView importManualOrPoCars(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("Import Manual or PO CARS Job  called");
		try{
			quartzJobManager.importCars();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/scheduleJobs");
	}
	public ModelAndView resynchAttributes(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("Resynch Attributes Job  called");
		try{
			quartzJobManager.resynchAttributes();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/runJob");
	}
	
	public ModelAndView exportVenodrExpeditedShipping(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
				System.out.println("Export Vendor Expedited Shipping Job  called");
				try{
					quartzJobManagerDropship.exportVendorExpeditedShipping();
				}catch(Exception ex){
					ex.printStackTrace();
				}
				return new ModelAndView("oma/scheduleJobs");
			}
	
	public ModelAndView sendToCMP(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("Send CARs Data To CMP Job  called");
		try{
			quartzJobManager.exportCESInfoWithoutContent();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/scheduleJobs");
	}
	
	public ModelAndView methodInvokingExportProductsInTrend(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("Export product in Trend");
		try{
			quartzJobManager.exportProductsInTrend();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/scheduleJobs");
	}
	public ModelAndView methodInvokingSendUserNotification(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("Send User notification");
		try{
			quartzJobManager.sendUserCarNotification();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/scheduleJobs");
	}
//	public ModelAndView methodInvokingSendVendorNotification(HttpServletRequest request, HttpServletResponse response)
//	throws Exception {
//		System.out.println("Send Vendor notification");
//		try{
//			quartzJobManager.sendVendorCarNotification();
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
//		return new ModelAndView("oma/scheduleJobs");
//	}
	public ModelAndView methodInvokingCloseCars(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("Close CARS");
		try{
			quartzJobManager.closeCars();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/scheduleJobs");
	}
	public ModelAndView methodInvokingExportHexValues(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("Export Hex Values");
		try{
			quartzJobManager.exportHexValues();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/scheduleJobs");
	}
	public ModelAndView methodInvokingExportManualCars(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("Export manual CARS");
		try{
			quartzJobManager.exportManualCarsToFile();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/scheduleJobs");
	}
	public ModelAndView methodInvokingExportRRDFeeds(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("Export RRD Feed");
		try{
			quartzJobManager.exportRRDFeeds();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/scheduleJobs");
	}
//	public ModelAndView methodInvokingSendVendorCarEscalationList(HttpServletRequest request, HttpServletResponse response)
//	throws Exception {
//		System.out.println("Send vendor escalation list");
//		try{
//			quartzJobManager.sendVendorCarEscalationList();
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
//		return new ModelAndView("oma/scheduleJobs");
//	}
//	public ModelAndView invokeSendVendorSampleEscalationList(HttpServletRequest request, HttpServletResponse response)
//	throws Exception {
//		System.out.println("Send sample escalation list");
//		try{
//			quartzJobManager.sendVendorSampleEscalationList();
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
//		return new ModelAndView("oma/scheduleJobs");
//	}
	public ModelAndView methodInvokingImportRLRInfo(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("Import RLR info");
		try{
			quartzJobManager.importRLR();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/scheduleJobs");
	}
	public ModelAndView methodInvokingImportRRDFeeds(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("Import RRD info");
		try{
			quartzJobManager.importRRDFeeds();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/scheduleJobs");
	}
	public ModelAndView methodInvokingUnlockAllCars(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("Unlock CARS");
		try{
			quartzJobManager.unlockAllCars();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/scheduleJobs");
	}
	public ModelAndView methodInvokingSendLateCarReport(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("Send late cars report");
		try{
			quartzJobManagerForLateCarReport.sendLateCarReports();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/scheduleJobs");
	}
	public ModelAndView methodInvokingImportInventoryData(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		log.info("Import inventory data from BEL_INV_DATA@BM_EXTERNAL");
		try{
			quartzJobManager.updateTmpWithBel_Inv_Avail();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/scheduleJobs");
	}

	
	public ModelAndView methodInvokingImportAndUpdateFacetAttribute(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("Import and Update Facet Attributes into Car Attribute table " );
		System.out.println("----Start Time ---- ********---->> "+System.currentTimeMillis());
		try{
			quartzJobManagerForImportAndUpdateFacets.importAndUpdateCarFacetAttibutes();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		System.out.println("----End Time ---- ********---->> "+System.currentTimeMillis());
		return new ModelAndView("oma/scheduleJobs");
	}

	public ModelAndView methodInvokingLoadMCImage(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("load media compass images to belkmacl");
		try{
			quartzJobManagerForMCImage.loadTempImagesFromMC();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/scheduleJobs");
	}
	
	public ModelAndView methodInvokingImportMCImageFeed(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("import media compass temporary image feed and update DB tables");
		try{
			quartzJobManagerForMCImage.importMCTempImageFeed();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/scheduleJobs");
	}
	// Added for CARS Faceted Navigation
//	public ModelAndView methodInvokingResynchSizeValuesOfVendorSkus(HttpServletRequest request, HttpServletResponse response)
//	throws Exception {
//		log.info("Running the sizeconversionJobManager job");
//		try{
//			sizeConversionJobManager.resynchSizeValuesOfVendorSkus();
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
//		return new ModelAndView("oma/scheduleJobs");
//	}

	// Added for CARS Faceted Navigation
//	public ModelAndView methodInvokingResynchColorValuesOfVendorSkus(HttpServletRequest request, HttpServletResponse response)
//	throws Exception {
//		log.info("Running the superColorJobManager job");
//		try{
//			superColorJobManager.resynchSuperColor1OfVendorSkus();
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
//		return new ModelAndView("oma/scheduleJobs");
//	}

	// Added for CARS Faceted Navigation
	public ModelAndView methodInvokingAttributeResynch(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		log.info("Running the attributeResynchJobManager job");
		try{
			attributeResynchJobManager.resynchAttributes();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/scheduleJobs");
	}

	
	public ModelAndView methodInvokingSendVendorImagesFeedForMCToRRD(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info("Running the SendVendorImagesFeedForMCToRRD from front End");
		try{
			quartzJobManagerForVendorImages.sendVendorImagesFeedForMCToRRD();
		}catch(Exception ex){
			log.error("Exception while calling SendVendorImagesFeedForMCToRRD"+ ex);
		}
		return new ModelAndView("oma/scheduleJobs");
	}
	public ModelAndView methodInvokingSendVendorImagesUpdateFeedToRRD(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info("Running the sendVendorImagesUpdateFeedToRRD from front End");
		try{
			quartzJobManagerForVendorImages.sendVendorImagesUpdateFeedToRRD();
		}catch(Exception ex){
			log.error("Exception while calling sendVendorImagesUpdateFeedToRRD  " + ex);
		}
		return new ModelAndView("oma/scheduleJobs");
	}
	
	//methodInvokingAutomaticallyApproveImage
	public ModelAndView methodInvokingAutomaticallyApproveImage(HttpServletRequest request, HttpServletResponse reponse)
	throws Exception {
		try{
			Calendar cal = Calendar.getInstance();
			int day = cal.get(Calendar.DAY_OF_WEEK);
			if(day != 1 && day != 7 ){
				quartzJobManager.updateAutomaticallyApproveImage();
			}  else {
				log.info("----------------> This job runs only during Weekdays <-----------------"); 
			}
		} catch(Exception ex) {
			log.error("Exception occured while calling AutomaticallyApproveImage " + ex);
		}
		return new ModelAndView("oma/scheduleJobs");
	}
	
	public ModelAndView methodInvokingImportVendorImageHistoryFeed(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info("Running the importVendorImageHistoryFeed from front End");
		try{
			quartzJobManagerForVendorImages.importVendorImageHistoryFeed();
		}catch(Exception ex){
			log.error("Exception while calling importVendorImageHistoryFeed  " + ex);
		}
		return new ModelAndView("oma/scheduleJobs");
	}
	
	public ModelAndView methodInvokingimportVendorImageCheckFeedbackFromRRD(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info("Running the sendVendorImagesUpdateFeedToRRD from front End");
		try{
			
			quartzJobManagerForVendorImages.importVendorImageCheckFeedbackFromRRD();
		}catch(Exception ex){
			log.error("Exception while calling ReadRRDCreativecheckFeedBackDetails  " + ex);
		}
		return new ModelAndView("oma/scheduleJobs");
	}
	

	public ModelAndView methodInvokingSendImageRejectionMail(HttpServletRequest request, HttpServletResponse response) throws Exception{
		try{
			log.info("Running the sendImageRejectionMail Job ");
			quartzJobManagerForEmailNotification.sendVendorImageRejectionEmailNotifitcation();
		}catch(Exception ex){
			log.error("Exception while calling sendVendorImageRejectionEmailNotifitcation  " + ex);
		}
		return new ModelAndView("oma/scheduleJobs");
	}
	
	public ModelAndView importUserRankData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("Import User Rank Data called");
		try {
			quartzJobManager.importUserRankData();
		} catch (Exception ex) {
			log.error("Exception while calling importUserRankData Job  ",ex);
		}
		return new ModelAndView("oma/scheduleJobs");
	}
	
	public ModelAndView exportPWPandGWPAttributes(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("Export PWP & GWP Attributes:ShedulerJobController");
		try {
			quartzJobManager.exportPWPandGWPAttributes();
		} catch (Exception ex) {
			log.error("Exception while calling exportPWPandGWPAttributes Job  ",ex);
		}
		return new ModelAndView("oma/scheduleJobs");
	}
	
	public ModelAndView methodToProcessPIMAttributeUpdates(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		log.info("processPIMAttributeUpdates called");
		try {
			quartzJobManagerForPIMJobs.processPIMAttributeUpdates();
		} catch (Exception ex) {
			log.error("Exception while calling processPIMAttributeUpdates Job  ",ex);
		}
		return new ModelAndView("oma/scheduleJobs");
	}
        
    public ModelAndView methodInvokingSkuPackParentResync(HttpServletRequest request, 
		HttpServletResponse response) throws Exception {
    	log.info("executeSkuPackParentResyncProcedure called");
    	try {
    		quartzJobManagerForPIMJobs.executeSkuPackParentResyncProcedure();
    	} catch (Exception ex) {
    		log.error("Exception while calling processPIMAttributeUpdates Job  ",ex);
    	}
    	return new ModelAndView("oma/scheduleJobs");
	}
    
    public ModelAndView methodToProcessPIMAttributeUpdatesForClosedCars(HttpServletRequest request, 
        HttpServletResponse response) throws Exception {
        log.info("Invoking ProcessPIMAttributeUpdates For ClosedCars ");
        try {
            quartzJobManagerForPIMJobs.processPIMAttributeUpdatesForClosedCars();
        } catch (Exception ex) {
            log.error("Exception while calling processPIMAttributeUpdates for closed cars Job  ",ex);
        }
        return new ModelAndView("oma/scheduleJobs");
    }
}
