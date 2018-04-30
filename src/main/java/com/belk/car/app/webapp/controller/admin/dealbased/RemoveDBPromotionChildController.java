package com.belk.car.app.webapp.controller.admin.dealbased;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarDBPromotionChild;
import com.belk.car.app.model.DBPromotionCollectionSkus;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.service.DBPromotionManager;
import com.belk.car.app.util.DBPromotionUtil;
import com.belk.car.app.webapp.controller.MultiActionFormController;
import com.belk.car.app.webapp.forms.ChildCar;
import com.belk.car.util.DateUtils;
public class RemoveDBPromotionChildController extends MultiActionFormController{

	private transient final Log log = LogFactory.getLog(RemoveDBPromotionChildController.class);
	
	public DBPromotionManager dbPromotionManager;
	
	
	public DBPromotionManager getDbPromotionManager() {
		return dbPromotionManager;
	}

	public void setDbPromotionManager(DBPromotionManager dbPromotionManager) {
		this.dbPromotionManager = dbPromotionManager;
	}

	public RemoveDBPromotionChildController() {
	}
		
	/**
	 * Removing the promo child car from the list of child cars in session.
	 * This method called on clicking on remove child car link of create/edit deal based promotion page
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	public ModelAndView removeDBPromotionChild(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		
		String childCarId = ServletRequestUtils.getStringParameter(request, "ChildCarId");
		if(log.isDebugEnabled()){
			log.debug("Trying to remove promotion child car from session list"+ childCarId);
		}
		// removing deleted child CAR from db promotion CAR - 
		Car removeChildCar=dbPromotionManager.getChildCarForRemove(new Long(childCarId));
		List<ChildCar> viewChildCars=new ArrayList<ChildCar>(); // list to view the child cars on jsp
		Map<String, Object> model = new HashMap<String, Object>();
		List<Car> newChildCars=null; //list to maintain the newly added child cars
		
		List<Car> removedChildCars=null; //the list to maintain the removed child cars from an existing promotion car
		removedChildCars=(ArrayList<Car>)request.getSession().getAttribute("REMOVED_CHILD_CAR_LIST");
		
		// removedChildCars is empty first time, so create a new list and add removedChildCar
		if(removedChildCars == null){
			removedChildCars=new ArrayList<Car>();
		}
		removedChildCars.add(removeChildCar);
		
		List<Car> childCars=new ArrayList<Car>(); // list of child cars to pass to Save promotion
		childCars=(ArrayList<Car>)request.getSession().getAttribute("CHILD_CAR_LIST");
		
		// remove child car from both the list
		if(childCars!=null){
			newChildCars=new ArrayList<Car>(childCars);
			Collections.copy(newChildCars,childCars);
			childCars.remove(removeChildCar);
			newChildCars.remove(removeChildCar);
			if(log.isDebugEnabled()){
				log.debug("Child car id:"+  childCarId +" is removed from both list");
			} 
		}

		String dbPromotionCarId = ServletRequestUtils.getStringParameter(request, "dbPromotionCarId");
		
		// if editing an promotion, we have d b promotion car id
		// after being removed get the remaining promo child cars and check if it was not removed then add to view
		// remove the promotion child cars from newChildCars list to maintain newly added child cars
		if(dbPromotionCarId!=null && !"".equals(dbPromotionCarId) && !"null".equals(dbPromotionCarId)) {
			Car dbPromotionCar=dbPromotionManager.getCarFromId(new Long(dbPromotionCarId));
			Attribute attrChildProducts = dbPromotionManager.getAttributeByName(Constants.DBPROMOTION_DEFAULT_SKUS);
			//Get the attribute values..
			String attrChildProductValue=dbPromotionManager.getAttributeValue(dbPromotionCar,attrChildProducts);
			
			String[] childProductSKUs = null;
			List<String> lstchildProductSKUs = null;
			if(attrChildProductValue != null){
				childProductSKUs = attrChildProductValue.split(",");
				lstchildProductSKUs =  Arrays.asList(childProductSKUs);
			}
			Set<CarDBPromotionChild> carDBPromotionChild=dbPromotionCar.getCarDBPromotionChild();
			if(carDBPromotionChild != null){
				for(CarDBPromotionChild dbPromotion : carDBPromotionChild){
					Car childCar=dbPromotion.getChildCar();
					if(!removedChildCars.contains(childCar) && dbPromotion.getStatusCd().equals(Status.ACTIVE)){
							viewChildCars.add(dbPromotionManager.getViewChildCarDetails(childCar,dbPromotion,lstchildProductSKUs));
							newChildCars.remove(childCar);
					} 
				}
				if(log.isInfoEnabled()){
					log.info("child car id :" + childCarId +" is removed from the session list of promotion car " +dbPromotionCarId );
				}
			}else{
				log.error("Promotion Child CAR is not found");
			}
		}
		
		/* finally if there are any newly added child cars then add to view*/
		for(Car newCar:newChildCars){
			viewChildCars.add(dbPromotionManager.getViewChildCarDetails(newCar,new CarDBPromotionChild(),new ArrayList<String>()));
		}
		
		request.getSession().setAttribute("CHILD_CAR_LIST",childCars);
		request.getSession().setAttribute("REMOVED_CHILD_CAR_LIST",removedChildCars);
		
		/* Remove Sku information from Sku List*/ 
		String templateType = ServletRequestUtils.getStringParameter(request, "templateTypeVal");
		if (templateType.equalsIgnoreCase("PYG")) {
			List<String> lstSelDBPromotionCollectionSKUs = null;
			if(dbPromotionCarId!=null && !"".equals(dbPromotionCarId) && !"null".equals(dbPromotionCarId) ) 
			{ 
				Car dbPromotionCar=dbPromotionManager.getCarFromId(new Long(dbPromotionCarId));
				List<DBPromotionCollectionSkus> dbPromCollSkus = dbPromotionManager.getDBPromotionCollectionSkus(DBPromotionUtil.getProductCode(dbPromotionCar));
				
				
				//Attribute dbPromotionCollectionSkus = dbPromotionManager.getAttributeByName(Constants.COLLECTION_PARENT_PRODUCTS); //to be done as  dbPromotionManager.getDBPromotionCollectionSkus(productCode) 
				String attrChildSkuValue=  DBPromotionUtil.getDBPromoSkusValuesInString(dbPromCollSkus); //dbPromotionManager.getAttributeValue(dbPromotionCar,dbPromotionCollectionSkus);
				String[] dbPromotionCollectionSKUs = null;
				if(attrChildSkuValue != null){
					dbPromotionCollectionSKUs = attrChildSkuValue.split(",");
					lstSelDBPromotionCollectionSKUs =  Arrays.asList(dbPromotionCollectionSKUs);
					request.setAttribute("attrChildSkuValue", attrChildSkuValue);
				}
			}
			
			List<ChildCar> viewChildCarsSkuList=new ArrayList<ChildCar>(); // child cars to View
			List<ChildCar> viewChildCarsSkuListArray[] = (ArrayList<ChildCar>[])new ArrayList[100];
			
			if (request.getSession().getAttribute("CHILD_CAR_SKU_LIST") != null)
			{
				viewChildCarsSkuList = (ArrayList<ChildCar>)request.getSession().getAttribute("CHILD_CAR_SKU_LIST");
			}
			viewChildCarsSkuList = getChildCarSkus(request, response);
			request.getSession().setAttribute("CHILD_CAR_SKU_LIST",viewChildCarsSkuList);
			
			long tempSkuCarId = 0;
			int groupCarCounter = -1;
			int childCarCounter = 0;
			for(ChildCar vSku: viewChildCarsSkuList){
	
				ChildCar childCarSkuList= new ChildCar();
				long skuCarId = vSku.getSkuCarid();
				Car carObj = dbPromotionManager.getCarFromId(skuCarId);
				long carStyleId = carObj.getVendorStyle().getVendorStyleId();
				if (tempSkuCarId == 0 || tempSkuCarId != carStyleId)
				{
					if (groupCarCounter != -1 && tempSkuCarId != 0 && tempSkuCarId != carStyleId)
					{
						model.put("viewChildCarsSkuList_"+groupCarCounter, viewChildCarsSkuListArray[groupCarCounter]);
					}
					tempSkuCarId = carStyleId;
					groupCarCounter++;
					viewChildCarsSkuListArray[groupCarCounter] = new ArrayList<ChildCar>();
				}
	
				viewChildCarsSkuList.get(childCarCounter).setParentCarStyleId(new Long(carStyleId));
				childCarSkuList.setSkuCarid(skuCarId);
				childCarSkuList.setCompDt(vSku.getCompDt());
				childCarSkuList.setVendorStyle(vSku.getVendorStyle());
				childCarSkuList.setSkuStyleName(vSku.getSkuStyleName());
				childCarSkuList.setVendorUpc(vSku.getVendorUpc());
				childCarSkuList.setSkuColor(vSku.getSkuColor());
				childCarSkuList.setColorName(vSku.getColorName());
				childCarSkuList.setSizeName(vSku.getSizeName());
				childCarSkuList.setSkuID(vSku.getSkuID());
				childCarSkuList.setBelkSku(vSku.getBelkSku());
				childCarSkuList.setVendorName(vSku.getVendorName());
				
				List<String> lstSelBelkUpcColl = null;
				String selectedUpc = request.getParameter("selectedUpc");
				String selBelkUpcArr[] = selectedUpc.split(",");
				lstSelBelkUpcColl =  Arrays.asList(selBelkUpcArr);
				if(dbPromotionCarId!=null && !"".equals(dbPromotionCarId) && !"null".equals(dbPromotionCarId) ) 
				{
					if(lstSelBelkUpcColl.contains(vSku.getBelkSku()))
						childCarSkuList.setSkuSelValues(Constants.SELECT_SKUCHECKBOX);
					else
						childCarSkuList.setSkuSelValues(Constants.UNSELECT_SKUCHECKBOX);
				}
				else
				{
					if(lstSelBelkUpcColl.contains(vSku.getBelkSku()))
						childCarSkuList.setSkuSelValues(Constants.SELECT_SKUCHECKBOX);
				}
				viewChildCarsSkuListArray[groupCarCounter].add(childCarSkuList);
				childCarCounter++;
			}
			if (groupCarCounter != -1)
			{
				model.put("viewChildCarsSkuList_"+groupCarCounter, viewChildCarsSkuListArray[groupCarCounter]);
			}
			model.put("viewChildCarsSkuListArray", viewChildCarsSkuListArray);
			model.put("viewChildCarsSkuList", viewChildCarsSkuList);
		}
		model.put("viewChildCars", viewChildCars);
		return new ModelAndView(getSuccessView(),model);
	}
	
	
	
	public List<ChildCar> getChildCarSkus(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		
		
		
		List<Car> childCars=new ArrayList<Car>(); // Cars to pass Save promotion 
		List<ChildCar> viewChildCars=new ArrayList<ChildCar>(); // child cars to View
		List<Car> newChildCars=null; // for maintaining newly added child cars
		Map<String, Object> model = new HashMap<String, Object>();
		List<Car> removedChildCars=null;// for maintaining the removed child car list from an existing promotion
		List<ChildCar> viewChildCarsSkuList=new ArrayList<ChildCar>(); // child cars to View
		List<Car> skuCar=new ArrayList<Car>();
		
		
		
			// Child Car can not be a promotion Car
			if(request.getSession().getAttribute("CHILD_CAR_LIST")!=null)
			{
				childCars=(ArrayList<Car>)request.getSession().getAttribute("CHILD_CAR_LIST");
				if(childCars!=null){	
						newChildCars=new ArrayList<Car>(childCars);
						Collections.copy(newChildCars,childCars);
						// commenting generalized made code ..-"viewChildCarsSkuList"= DBPromotionUtil.getCarSkuListForDisplay(childCars);
						
						for(Car childCar:childCars){
							VendorStyle vendorStyle = childCar.getVendorStyle();
							
							skuCar = dbPromotionManager.getChildCarSkus(vendorStyle);
							log.info("skuCar length = "+skuCar.size());
							for(Car childCarSku:skuCar){
							
								Set<VendorSku> carVendorSku=childCarSku.getVendorSkus();
								String styleNumStr = childCarSku.getVendorStyle().getVendorStyleNumber();
							for(VendorSku vSku:carVendorSku){
								ChildCar childCarSkuList= new ChildCar();
								childCarSkuList.setSkuCarid(childCarSku.getCarId());
								childCarSkuList.setCompDt(DateUtils.formatDate(childCarSku.getDueDate()));
								childCarSkuList.setVendorName(vSku.getVendorStyle().getVendor().getName());
//								long venStyleId = vSku.getVendorStyle().getVendorStyleId();
//								String strVenStyleId = (venStyleId != 0)?(new Long(venStyleId)).toString():"";
								childCarSkuList.setVendorStyle(styleNumStr);
								childCarSkuList.setSkuStyleName(vSku.getVendorStyle().getVendorStyleName());
								childCarSkuList.setVendorUpc(vSku.getBelkUpc());
								childCarSkuList.setSkuColor(vSku.getColorCode());
								childCarSkuList.setColorName(vSku.getColorName());
								childCarSkuList.setSizeName(vSku.getSizeName());
								childCarSkuList.setSkuID(vSku.getBelkSku());
								childCarSkuList.setBelkSku(vSku.getBelkSku());
								viewChildCarsSkuList.add(childCarSkuList);
							}
							}
						}
				
			}
			}
			if (log.isDebugEnabled()) {
				log.debug("child cars:"+childCars);
				log.debug("view child cars size:"+viewChildCars.get(0));
			}
						
			return viewChildCarsSkuList;
		
		
	}
	
	
	
}
