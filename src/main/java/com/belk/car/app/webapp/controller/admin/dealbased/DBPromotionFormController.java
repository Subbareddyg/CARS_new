package com.belk.car.app.webapp.controller.admin.dealbased;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import org.apache.commons.lang.StringUtils;

import com.belk.car.app.Constants;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.PromoType;
import com.belk.car.app.service.DBPromotionManager;
import com.belk.car.app.util.DBPromotionUtil;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.DBPromotionForm;

import edu.emory.mathcs.backport.java.util.Arrays;
public class DBPromotionFormController extends BaseFormController{
	private transient final Log log = LogFactory
			.getLog(DBPromotionFormController.class);
	public DBPromotionManager dbPromotionManager;

	

	public DBPromotionManager getDbPromotionManager() {
		return dbPromotionManager;
	}

	public void setDbPromotionManager(DBPromotionManager dbPromotionManager) {
		this.dbPromotionManager = dbPromotionManager;
	}

	public  DBPromotionFormController() {
		setCommandName("dbPromotionForm");
		setCommandClass(DBPromotionForm.class);
	}

	public ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		log.debug("entering dbPromotionForm 'processFormSubmission' method...");

		if (request.getParameter("cancel") != null) {
			return new ModelAndView(getSuccessView());
		}
		return super.processFormSubmission(request, response, command, errors);
	}
	
	/**
     * This onSubmit form controller is used to create/update DBPromotion 
     * car and save it. It is performing following things:
     * 1. Create the DBPromotion car by calling the createDBPromotion method passing 
     * DBPromotion name , DBPromotion description, list of child cars , order(priority) of car and list of skus
     * 2. Store the attribute value template type in car attribute table by calling 
	 * addCollectionProductAttributes method 
     * 3. Get the attribute value for each sku.
     * 4. Store the sku attributes in CarAttribute table by calling 
	 * createCollectionSkus method and passing sku's carid and attribute value.
     * 5. For existing DBPromotion car save the modification by calling saveDBPromotion method
     * by passing DBPromotion name , DBPromotion description, list of child cars , order(priority)
     * of car and list of skus.
     * 6. Get the attribute value for each sku.
     * 7. Update the sku attributes in CarAttribute table by calling 
	 * updateCollectionSkuAttributes method and passing sku's car id and attribute value.
	 * 
     * @param request   HttpServletRequest object
	 * @param response  HttpServletResponse object
	 * @param command	DBPromotionForm object
	 * @param errors
	 * 	
	 * @return model object containing attribute value redirect to DBPromotions.html
     */

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering dbPromotionForm 'onSubmit' method...");
		}
		try {
			String oneStepUPCs = request.getParameter("onestepschildcars");
			DBPromotionForm dbPromotionForm = (DBPromotionForm) command;
			String dbPromotionName = dbPromotionForm.getDbPromotionName();
			String dbPromotionDesc = dbPromotionForm.getDescription();
			String[] order = dbPromotionForm.getOrder();
			List<String> orderCars = (order == null ? new ArrayList<String>(): Arrays.asList(order));
			String[] defaultSku = dbPromotionForm.getSku();
			List<String> sku = defaultSku == null ? new ArrayList<String>(): Arrays.asList(defaultSku);
			List<Car> childCars = (ArrayList<Car>) request.getSession().getAttribute("CHILD_CAR_LIST");
			Long carId = ServletRequestUtils.getLongParameter(request, "CarId");
			if (childCars == null) {
				childCars = new ArrayList<Car>();
			}
			if (log.isDebugEnabled()) {
				log.debug("childCars: " + childCars);
				log.debug("dbPromotion name: " + dbPromotionName + "\t description: "
						+ dbPromotionDesc);
				log.debug("Child Car order: " + orderCars);
				log.debug("Child Car Sku:" + sku);
			}
			
			String templateType = request.getParameter("templateTypeVal"); //will come as PYG for now
	
			if (carId == null) {
				/* create dbPromotion car*/
				Car dbPromotionCar = dbPromotionManager.createDBPromotion(dbPromotionName,dbPromotionDesc, childCars, orderCars, sku,templateType);
				
				addPromoType(dbPromotionCar.getCarId());
				
				if (dbPromotionCar != null) {
					if (log.isDebugEnabled()) {
						log.debug("DBPromotion car created : "
								+ dbPromotionCar.getCarId());
					}
					Long dbPromotionCarId = dbPromotionCar.getCarId();
					String productCode = dbPromotionCar.getVendorStyle().getVendorNumber()+dbPromotionCar.getVendorStyle().getVendorStyleNumber();
					
					dbPromotionManager.createProductAttributes(dbPromotionCarId,templateType, Constants.DBPROMOTION_TEMP_TYPE);
					
					/* if template type="PYG"*/
					if (templateType.equalsIgnoreCase(Constants.TEMPL_TYPE_VAL_PYG)) {	
						String[] skuCount = dbPromotionForm.getSkuCount();
						for (int i = 0; i < skuCount.length; i++) {
							String chkSkuList = "chkSkuList" + i;
							String chkSkuListVal = request.getParameter(chkSkuList);
							if (chkSkuListVal != null) {
								String belkSku = "belkSku" + i;
								String belkSkuVal = request.getParameter(belkSku);
								dbPromotionManager.createDBPromotionCollectionSkus(productCode,belkSkuVal);
							}
						}
						
						//Creating IS_PYG on parent is commented as now IS_PYG set on child will be copied  to Parent as well because child car are supposed to be having IS_PYG set in them befor being added to any PYG parent
						//dbPromotionManager.createProductAttributes(dbPromotionCarId,Constants.DBPROMOTION_ATTR_IS_PYG_YES, Constants.DBPROMOTION_ATTR_IS_PYG);
						dbPromotionManager.createProductAttributes(dbPromotionCarId,setSingleStepChildAttribute(oneStepUPCs),Constants.DBPROMOTION_ONE_STEP_CHILD);
					}//templateType=PYG if loop ends
				}
			} else {
				/* save dbPromotion car*/
				Car DBPromotionCar = dbPromotionManager.getCarFromId(carId);
				dbPromotionManager.saveDBPromotion(DBPromotionCar, dbPromotionName, dbPromotionDesc,
						childCars, orderCars, sku);
				if (log.isDebugEnabled()) {
					log.debug("DBPromotion car Saved : " + DBPromotionCar.getCarId());
				}
				/* if template type="PYG", update the comma separated sku list*/
				if (templateType.equalsIgnoreCase(Constants.TEMPL_TYPE_VAL_PYG)) {
					Long dbPromotionCarId = DBPromotionCar.getCarId();
					String productCode = DBPromotionUtil.getProductCode(DBPromotionCar);
					dbPromotionManager.removeDBPromotionCollectionSkus(productCode);
					String[] skuCount = dbPromotionForm.getSkuCount();
					for (int i = 0; i < skuCount.length; i++) {
						String chkSkuList = "chkSkuList" + i;
						String chkSkuListVal = request.getParameter(chkSkuList);
						if (chkSkuListVal != null) {
							String belkSku = "belkSku" + i;
							String belkSkuVal = request.getParameter(belkSku);
							dbPromotionManager.createDBPromotionCollectionSkus(productCode,belkSkuVal);
						}
					}
					dbPromotionManager.updateDBPromotionAttributes(dbPromotionCarId,Constants.DBPROMOTION_ONE_STEP_CHILD,setSingleStepChildAttribute(oneStepUPCs));
					//in Track 2 following attribute could be needed to be updated . As of now it will always be PYG.  Yogesh V
					//dbPromotionManager.updateDBPromotionAttributes(dbPromotionCarId,Constants.DBPROMOTION_TEMP_TYPE, templateType);
					
				}
			}
			request.getSession().removeAttribute("CHILD_CAR_LIST");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(" DBPromotionFormController: onSubmit: exception while creating/saving a car: "
				+ e.getMessage());
		}
		return new ModelAndView("redirect:dbPromotions.html?method=getAllDBPromotionCars");
	}
	
	public void addPromoType(long carId){
		PromoType promoType = getDbPromotionManager().findPromoType(carId);
		getDbPromotionManager().savePromoType(DBPromotionUtil.updateOnPromoVal(promoType, PromoType.YES, PromoType.NO, carId));
		
	}
	
	
	
	public String setSingleStepChildAttribute(String oneStepCars){
		String oneStepChildCodes = "";
		if(!StringUtils.isBlank(oneStepCars)){
		if(oneStepCars.indexOf(",")<0){
			Car car =getDbPromotionManager().getCarFromId(new Long(oneStepCars).longValue());
			oneStepChildCodes = DBPromotionUtil.getProductCode(car);
		}else{
			String carIdArray [] = oneStepCars.split(",");
			for(String carId :carIdArray){
				Car car =getDbPromotionManager().getCarFromId(new Long(carId).longValue());
				if("".equals(oneStepChildCodes)){
					oneStepChildCodes = DBPromotionUtil.getProductCode(car);
				} else{
					oneStepChildCodes = oneStepChildCodes + ","+DBPromotionUtil.getProductCode(car);
				}
			}
		}
		}
		return oneStepChildCodes;
	}
	
	
}
