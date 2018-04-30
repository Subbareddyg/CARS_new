
package com.belk.car.app.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.service.QuartzJobManager;
import com.belk.car.app.service.QuartzJobManagerForDropship;
import com.belk.car.app.service.QuartzJobManagerForVendorCatalog;
import com.belk.car.app.service.impl.CarPrefillManagerImpl;

/*
 * This controller is used to load the Initial Fulfillment Service Page with
 * load,search,ViewAll functionalities
 */
public class SelectAndRunJobController extends MultiActionController
implements
DropShipConstants {

	private static transient final Log log = LogFactory
	.getLog(SelectAndRunJobController.class);
	private QuartzJobManagerForDropship quartzJobManagerDropship;
	private QuartzJobManagerForVendorCatalog quartzJobManagerForVendorCatalog;
	private CarPrefillManagerImpl carPrefillJobManager;
	private QuartzJobManager quartzJobManager;

	public CarPrefillManagerImpl getCarPrefillJobManager() {
		return carPrefillJobManager;
	}

	public void setCarPrefillJobManager(CarPrefillManagerImpl carPrefillJobManager) {
		this.carPrefillJobManager = carPrefillJobManager;
	}

	public QuartzJobManagerForDropship getQuartzJobManagerDropship() {
		return quartzJobManagerDropship;
	}

	public void setQuartzJobManagerDropship(
			QuartzJobManagerForDropship quartzJobManagerDropship) {
		this.quartzJobManagerDropship = quartzJobManagerDropship;
	}

	public QuartzJobManagerForVendorCatalog getQuartzJobManagerForVendorCatalog() {
		return quartzJobManagerForVendorCatalog;
	}

	public void setQuartzJobManagerForVendorCatalog(
			QuartzJobManagerForVendorCatalog quartzJobManagerForVendorCatalog) {
		this.quartzJobManagerForVendorCatalog = quartzJobManagerForVendorCatalog;
	}

	/**
	 * @param quartzJobManager the quartzJobManager to set
	 */
	public void setQuartzJobManager(QuartzJobManager quartzJobManager) {
		this.quartzJobManager = quartzJobManager;
	}

	/**
	 * @return the quartzJobManager
	 */
	public QuartzJobManager getQuartzJobManager() {
		return quartzJobManager;
	}

	/*
	 * This method loads all the available fulfillment Services
	 */
	public ModelAndView load(HttpServletRequest request, HttpServletResponse response)
	throws Exception {


		return new ModelAndView("oma/runJob");
	}

	/*
	 * This method returns view of all available Fulfillment Services
	 */
	public ModelAndView importVendorCatalog(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("importVendorCatalog called");
		try{
			quartzJobManagerForVendorCatalog.importVendorCatalogData();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/runJob");
	}

	public ModelAndView readIDBFeed(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("readIDBFeed called");
		try{
			quartzJobManagerDropship.importIDBFeedForDropship();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/runJob");
	}

	public ModelAndView readReferenceFeed(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("readReferenceFeed called");
		try{
			quartzJobManagerDropship.importIDBCrossReferenceFeedForDropship();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/runJob");
	}

	public ModelAndView readPurgeFeed(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("readPurgemethod called");
		try{
			quartzJobManagerDropship.importIDBPurgeFeedForDropship();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/runJob");
	}
	public ModelAndView loadVendorCatalogDataIntoCARS(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("loadVendorCatalogDataIntoCARS called");
		try{
			quartzJobManagerForVendorCatalog.loadVendorCatalogDataIntoCARS();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/runJob");
	}
	public ModelAndView prefillCars(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("prefill cars called");
		try{
			carPrefillJobManager.prefillCars();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/runJob");
	}
	public ModelAndView sendToCMP(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("Send CARs Data To CMP Job  called");
		try{
			quartzJobManager.exportCESInfoWithoutContent();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/runJob");
	}
	public ModelAndView createDropshipCars(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("Import Dropship CARS Job  called");
		try{
			quartzJobManagerDropship.importDropshipCars();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/runJob");
	}
	public ModelAndView importManualOrPoCars(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		System.out.println("Import Manual or PO CARS Job  called");
		try{
			quartzJobManager.importCars();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return new ModelAndView("oma/runJob");
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
		return new ModelAndView("oma/runJob");
	}
	
}
