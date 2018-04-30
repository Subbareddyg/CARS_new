package com.belk.car.app.webapp.controller.admin.dealbased;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.json.JSONObject;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarDBPromotionChild;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.service.DBPromotionManager;
import com.belk.car.app.util.DBPromotionUtil;
import com.belk.car.app.webapp.controller.MultiActionFormController;
import com.belk.car.app.webapp.forms.ChildCar;
import com.belk.car.app.webapp.forms.DBPromotionForm;
public class EditDBPromotionController extends MultiActionFormController{

	private transient final Log log = LogFactory.getLog(EditDBPromotionController.class);

	public DBPromotionManager dbPromotionManager;
	
	

	
	
	public DBPromotionManager getDbPromotionManager() {
		return dbPromotionManager;
	}


	public void setDbPromotionManager(DBPromotionManager dbPromotionManager) {
		this.dbPromotionManager = dbPromotionManager;
	}


	/**
	 * Edit DBPromotion Car
	 * 
	 * @param request : CarId(DBPromotion)
	 * @param response
	 * @return ModelAndView with DBPromotion car details and child car details
	 * @throws Exception
	 */
	
	public ModelAndView getSkusForCar(HttpServletRequest request,HttpServletResponse response) throws Exception {
	log.info("INside EditDBPromotionSkuController.getSkusForCar");
	
	
	Long dbPromotionCarId = ServletRequestUtils.getLongParameter(request, "CarId");
	Long childCarId = ServletRequestUtils.getLongParameter(request, "childCarId");
	log.info("Editing DBPromotion Car: DBPromotionCarId11: "+dbPromotionCarId);

	DBPromotionForm dbPromotionForm=new DBPromotionForm();
	Map<String, Object> model = new HashMap<String, Object>();
	// List to maintain child cars to save ie. placing in session.
	List<Car> childCars=new ArrayList<Car>();
	// List of child cars to view purpose ie. passing in Model.
	List<ChildCar> viewChildCars=new ArrayList<ChildCar>();
	List<ChildCar> viewChildCarsSkuList=new ArrayList<ChildCar>(); // child cars to View
	List<ChildCar> viewChildCarsSkuListArray[] = (ArrayList<ChildCar>[])new ArrayList[100];
	Attribute attrChildProducts = dbPromotionManager.getAttributeByName(Constants.DBPROMOTION_DEFAULT_SKUS);
	
	if(dbPromotionCarId!=null && !"".equals(dbPromotionCarId))
	{
		Car dbPromotionCar=dbPromotionManager.getCarFromId(new Long(dbPromotionCarId));
		Car childCarSku = dbPromotionManager.getCarFromId(new Long(childCarId));
		
		//Get the attribute values of an DBPromotion Car
		String attrChildProductValue=dbPromotionManager.getAttributeValue(dbPromotionCar,attrChildProducts);
		String[] childProductSKUs = null;
		List<String> lstchildProductSKUs = null;
		// child product sku as a list to send to getViewChildCarDetails, for priority,color sku
		if(attrChildProductValue != null){
			childProductSKUs = attrChildProductValue.split(",");
			lstchildProductSKUs =  Arrays.asList(childProductSKUs);
		}
		
		if(dbPromotionCar!=null ) {
			// set the values of DBPromotion Form to view. 
			dbPromotionForm.setDbPromotionName(dbPromotionCar.getVendorStyle().getVendorStyleName());
			dbPromotionForm.setDescription(dbPromotionCar.getVendorStyle().getDescr());
			// Get all the DBPromotion child cars 
			Set<CarDBPromotionChild> carDBPromotionChild=dbPromotionCar.getCarDBPromotionChild();
			Set<CarDBPromotionChild> carChildSku=childCarSku.getCarDBPromotionChild();
			if(carDBPromotionChild != null){
				
			String templateTypeVal=Constants.TEMPL_TYPE_VAL_PYG;
			model.put("templateType", templateTypeVal);
			if(templateTypeVal != null && templateTypeVal.equalsIgnoreCase(Constants.TEMPL_TYPE_VAL_PYG))
			{
				String attrChildSkuValue=  DBPromotionUtil.getDBPromoSkusValuesInString(getDbPromotionManager().getDBPromotionCollectionSkus(DBPromotionUtil.getProductCode(dbPromotionCar))) ; //dbPromotionManager.getAttributeValue(dbPromotionCar,dbPromotionCollectionSkus);
				String[] dbPromotionCollectionSKUs = null;
				List<String> lstSelDBPromotionCollectionSKUs = null;
				//  sku as a list to send to getViewChildCarDetails, for priority,color sku
				if(attrChildSkuValue != null){
					dbPromotionCollectionSKUs = attrChildSkuValue.split(",");
					lstSelDBPromotionCollectionSKUs =  Arrays.asList(dbPromotionCollectionSKUs);
					dbPromotionForm.setSelDBPromotionCollectionSkus(attrChildSkuValue);
					log.info("-----Inside collection if attrChildSkuValue: "+attrChildSkuValue);
					request.setAttribute("attrChildSkuValue", attrChildSkuValue);
				}
					
				log.info("-----Inside collection if childCar: "+carDBPromotionChild);
				if (request.getSession().getAttribute("CHILD_CAR_SKU_LIST") != null)
				{
					viewChildCarsSkuList = (ArrayList<ChildCar>)request.getSession().getAttribute("CHILD_CAR_SKU_LIST");
				}
				viewChildCarsSkuList = getChildCarSkus(childCarSku);
				request.getSession().setAttribute("CHILD_CAR_SKU_LIST",viewChildCarsSkuList);
				
				long tempSkuCarId = 0;
				int groupCarCounter = -1;
				for(ChildCar vSku: viewChildCarsSkuList){
	
					ChildCar childCarSkuList= new ChildCar();
					long skuCarId = vSku.getSkuCarid();
					if (tempSkuCarId == 0 || tempSkuCarId != skuCarId)
					{
						if (groupCarCounter != -1 && tempSkuCarId != 0 && tempSkuCarId != skuCarId)
						{
							model.put("viewChildCarsSkuList_"+groupCarCounter, viewChildCarsSkuListArray[groupCarCounter]);
						}
						tempSkuCarId = skuCarId;
						groupCarCounter++;
						viewChildCarsSkuListArray[groupCarCounter] = new ArrayList<ChildCar>();
					}
	
					childCarSkuList.setSkuCarid(skuCarId);
					childCarSkuList.setVendorStyle(vSku.getVendorStyle());
					childCarSkuList.setSkuStyleName(vSku.getSkuStyleName());
					childCarSkuList.setVendorUpc(vSku.getVendorUpc());
					childCarSkuList.setSkuColor(vSku.getSkuColor());
					childCarSkuList.setColorName(vSku.getColorName());
					childCarSkuList.setSizeName(vSku.getSizeName());
					childCarSkuList.setSkuID(vSku.getSkuID());
					childCarSkuList.setBelkSku(vSku.getBelkSku());
					childCarSkuList.setVendorName(vSku.getVendorName());
					
					if(lstSelDBPromotionCollectionSKUs != null && lstSelDBPromotionCollectionSKUs.contains(vSku.getSkuID()))
						childCarSkuList.setSkuSelValues("1");
					else
						childCarSkuList.setSkuSelValues("0");
	
					log.info("---childCarSkuList.getSkuSelValues() =  "+childCarSkuList.getSkuSelValues());
					viewChildCarsSkuListArray[groupCarCounter].add(childCarSkuList);
				}
				if (groupCarCounter != -1)
				{
					model.put("viewChildCarsSkuList_"+groupCarCounter, viewChildCarsSkuListArray[groupCarCounter]);
				}
				model.put("viewChildCarsSkuListArray", viewChildCarsSkuListArray);
				model.put("viewChildCarsSkuList", viewChildCarsSkuList);
			}
	
				// For each DBPromotion child if it was ACTIVE, get the car details and add to ViewChildCars and childCars.
				for(CarDBPromotionChild dbPromotionChild : carDBPromotionChild){
					Car childCar=dbPromotionChild.getChildCar();
					if (log.isDebugEnabled()) {
						log.debug("DBPromotion Child Car Id :"+ childCar.getCarId() + " Status : "+ dbPromotionChild.getStatusCd());
					}
					if(dbPromotionChild.getStatusCd().equals(Status.ACTIVE)){
						viewChildCars.add(dbPromotionManager.getViewChildCarDetails(childCar,dbPromotionChild,lstchildProductSKUs));
						childCars.add(childCar);
					}
				}
			} else{
				 log.debug("DBPromotion Child CARs are null");
			}
		}
		else
		{
			if (log.isDebugEnabled()) {
				log.debug("DBPromotion Car Id :"+dbPromotionCarId+" Not found");
			}
		}
	
	}
	ModelAndView mv=new ModelAndView(getSuccessView(),model);
	// add childCars to session for add/remove child cars in edit and save.
	request.getSession().setAttribute("CHILD_CAR_LIST",childCars);
	// viewChildCars in model to view
	mv.addObject("viewChildCars", viewChildCars);
	return mv;
	}
	
	
	public List<ChildCar> getChildCarSkus(Car childCar) throws Exception {
		List<ChildCar> viewChildCarsSkuList=new ArrayList<ChildCar>(); // child cars to View
				Set<VendorSku> carVendorSku=childCar.getVendorStyle().getVendorSkus();
				log.info("----getBelkSku111 size = "+ carVendorSku.size());
				for(VendorSku vSku:carVendorSku){
					log.info("----getBelkSku111 = "+ vSku.getBelkSku()+" | "+vSku.getColorName()+" | "+vSku.getSizeName()+" | "+vSku.getCar());
					ChildCar childCarSkuList= new ChildCar();
					childCarSkuList.setSkuCarid(childCar.getCarId());
					childCarSkuList.setVendorName(vSku.getVendorStyle().getVendor().getName());
					long venStyleId = vSku.getVendorStyle().getVendorStyleId();
					String strVenStyleId = (venStyleId != 0)?(new Long(venStyleId)).toString ():"";
					childCarSkuList.setVendorStyle(strVenStyleId);
					childCarSkuList.setSkuStyleName(vSku.getVendorStyle().getVendorStyleName());
					childCarSkuList.setVendorUpc(vSku.getBelkUpc());
					childCarSkuList.setSkuColor(vSku.getColorCode());
					childCarSkuList.setColorName(vSku.getColorName());
					childCarSkuList.setSizeName(vSku.getSizeName());
					childCarSkuList.setSkuID(vSku.getBelkSku());
					childCarSkuList.setBelkSku(vSku.getBelkSku());
					viewChildCarsSkuList.add(childCarSkuList);
			}
			if (log.isDebugEnabled()) {
				log.debug("child cars:"+childCar);
				
			}
			return viewChildCarsSkuList;
	}
	
	/**
	 * Get the login user
	 */
	public User getLoggedInUser() {
		User user = null;
		Authentication auth = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (auth.getPrincipal() instanceof UserDetails) {
			user = (User) auth.getPrincipal();
		}
	 return user;
	}
}
