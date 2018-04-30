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
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarDBPromotionChild;
import com.belk.car.app.model.CarOutfitChild;
import com.belk.car.app.model.DBPromotionCollectionSkus;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.service.DBPromotionManager;
import com.belk.car.app.util.DBPromotionUtil;
import com.belk.car.app.webapp.controller.MultiActionFormController;
import com.belk.car.app.webapp.forms.ChildCar;
import com.belk.car.app.webapp.forms.DBPromotionForm;
import com.belk.car.util.DateUtils;
import com.belk.car.util.SortingComparator;

public class ListDBPromotionController extends MultiActionFormController{
	private transient final Log log = LogFactory
			.getLog(ListDBPromotionController.class);
	public DBPromotionManager dbPromotionManager;

	
	
	public DBPromotionManager getDbPromotionManager() {
		return dbPromotionManager;
	}

	public void setDbPromotionManager(DBPromotionManager dbPromotionManager) {
		this.dbPromotionManager = dbPromotionManager;
	}

	

	public ModelAndView getAllDBPromotionCars(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("Getting all the DBPromotion Cars");
		User user = getLoggedInUser();
		if (user.getAdmin()) {
			request.getSession().setAttribute("displayRole", "admin");
		}
		// Removing session variables created in ChildCarFormController, RemoveChildCarController,ListDBPromotionController 
		request.getSession().removeAttribute("CHILD_CAR_LIST");
		request.getSession().removeAttribute("REMOVED_CHILD_CAR_LIST");
		List<Car> dbPromotionCars=new ArrayList<Car>();
		try {
			dbPromotionCars= dbPromotionManager.getAllDBPromotions();
			if(dbPromotionCars!=null){
				if(log.isInfoEnabled()){
					log.info("Found " +dbPromotionCars.size()+ " dbPromotion cars");
				}
			}
		}catch(Exception e){
			log.error("Exception in getAllDBPromotionCars",e);
		}
		log.debug("Before all the DBPromotion Cars");
		return new ModelAndView(getSuccessView(), "promotionList",dbPromotionCars);  //to be put in constants
	}
	
	/**
	 * Search an DBPromotion Car by dbPromotionName and or styleNumber
	 * 
	 * @param request : dbPromotionName and or styleNumber
	 * @param response
	 * @return MovdelAndView to the list of searched dbPromotion cars in Model
	 * @throws Exception
	 */
	public ModelAndView Search(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			String dbPromotionName = request.getParameter("dbPromotionName")==null?"": request.getParameter("dbPromotionName").trim();
			String styleNumber=request.getParameter("styleNumber")==null?"": request.getParameter("styleNumber").trim();
			if (log.isInfoEnabled()) {
				log.info("Searching car with - Promotion Name:" + dbPromotionName + " style Number :"+styleNumber);
			}
			Map<String, Object> model = new HashMap<String, Object>();
			// If both dbPromotionName and styleNumber are empty, showing error message
			if (("".equals(dbPromotionName)) && ("".equals(styleNumber)) ) {
					log.debug("dbPromotionName and styleNumber are emtpy");
					saveError(request, getText("DBPromotion Name/Style Number cannot be empty", request.getLocale()));
				    model.put("dbPromotionName", "");
				    model.put("styleNumber", "");
				 	return new ModelAndView(getSuccessView(), model);
			}
			model.put("dbPromotionName", dbPromotionName);
			model.put("styleNumber", styleNumber);
			List<Car> cars =null;
			try{
				cars = dbPromotionManager.serachDBPromotionCars(dbPromotionName,styleNumber);
				 if (cars!=null){
			        if(log.isInfoEnabled()){	
			        	log.info("found "+cars.size() + " cars as search result");
			        }
				 }
			}catch(Exception e){
				log.error("Exception in DBPromotion Search",e);
				e.printStackTrace();
			}
	        model.put(Constants.DBPROMOTION_LIST, cars);
			return new ModelAndView(getSuccessView(), model);
		}
	
	/**
	 * Remove an DBPromotion Car
	 * 
	 * @param request: CarId
	 * @param response
	 * @return redirecting ModelAndView to the list dbPromotion cars 
	 * @throws Exception
	 */
	
	public ModelAndView removeDBPromotion(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		  Long dbPromotionCarId = ServletRequestUtils.getLongParameter(request, "CarId");
		  log.info("Removeing DBPromotion Car: dbPromotionCarId:" + dbPromotionCarId );
		  // Get dbPromotion Car object by dbPromotion car id
		  Car dbPromotionCar=dbPromotionManager.getCarFromId(new Long(dbPromotionCarId));
		  // Get the dbPromotion Car Product Code as combination of vendor number and vendor style number
		  String strDBPromotionProductCode = dbPromotionCar.getVendorStyle().getVendorNumber()+ dbPromotionCar.getVendorStyle().getVendorStyleNumber();
		  User user = getLoggedInUser();
		  // Mark dbPromotion Car status as DELETED and set updated by and updated date.
		  dbPromotionCar.setStatusCd(Status.DELETED);
		  dbPromotionCar.setUpdatedBy(user.getEmailAddress());
		  dbPromotionCar.setUpdatedDate(new Date());
		  try{
			  // Save dbPromotion Car
			  dbPromotionManager.saveorUpdate(dbPromotionCar);
		  }catch(Exception e){
			  log.error("Exception in remove DBPromotion Car ",e);
		  }
		  
		  // Get the child cars of dbPromotion
		  Set<CarDBPromotionChild> carDBPromotionChild=dbPromotionCar.getCarDBPromotionChild();
		  // for each child car update the status.
		  for(CarDBPromotionChild dbPromotionChild : carDBPromotionChild){
			    // update the dbPromotion child cars status to deleted
		        dbPromotionChild.setStatusCd(Status.DELETED);
		        dbPromotionChild.setUpdatedBy(user.getEmailAddress());
		        dbPromotionChild.setUpdatedDate(new Date());
		        Car childCar = dbPromotionChild.getChildCar();
		        // Get the dbPromotion_PARENT_PRODUCT attribute from child car and remove the DBPromotion product code from attribute value
		        CarAttribute ca = dbPromotionManager.getCarAttributeByBMName(childCar, Constants.DBPROMOTION_PARENT_PRODUCTS);//removing parent's prod code from child
		        //CarAttribute caType = dbPromotionManager.getCarAttributeByBMName(childCar, Constants.DBPROMOTION_TEMP_TYPE);
		        if(ca != null){
		        	deleteSkuAttribute(ca, strDBPromotionProductCode, user);
		        }
		        try{
		        	// Save dbPromotionChild 
		        	dbPromotionManager.saveorUpdate(dbPromotionChild);
		        }catch(Exception e){
		        	log.error("Exception update dbPromotionChild ",e);
		        }
		  }	  
		  log.info("DBPromotion with CarId:" + dbPromotionCarId +" is removed by :"+user.getEmailAddress() );
		  return new ModelAndView("redirect:/admin/dbpromotion/dbPromotions.html?method=getAllDBPromotionCars");
	}		
	
	public void deleteSkuAttribute(CarAttribute ca, String strDBPromotionProductCode, User user)
	{
		try{
			if(ca != null){
	        	  String strParentProducts = ca.getAttrValue();
	              StringBuffer sb= new StringBuffer(); 
	              String resultAttrValue1 = null;
	              String resultAttrValue2 = null;
	              String resultAttrValue3 = null;
	              boolean isContains = strParentProducts!=null?strParentProducts.contains(strDBPromotionProductCode):false;
	              if(isContains){
	            	resultAttrValue1=strParentProducts.replaceAll(strDBPromotionProductCode,"");
	              	resultAttrValue2=resultAttrValue1.replaceAll(",,",",");
	              	if(resultAttrValue2.startsWith(","))
	              	 {
	              		 resultAttrValue3= resultAttrValue2.substring(1);
	              		 sb.append(resultAttrValue3);
	              	 } else
	              		  { sb.append(resultAttrValue2); }
	              	
	              	 if(sb.toString().endsWith(","))
	              	 {
	              		sb.setLength(sb.length() - 1);  
	              	 }
	              }
	              // update the dbPromotion child car attributes.
	              ca.setUpdatedBy(user.getEmailAddress());
			      ca.setUpdatedDate(new Date());
	              ca.setAttrValue(sb.toString());
	              try{
	            	  //Save child car's attribute
	            	  dbPromotionManager.saveorUpdate(ca);
	              }catch(Exception e){
	            	  log.error("Exception in update child car attributes ",e);
	              }          
	        }
		}catch(Exception e){log.error("Exception in deleteSkuAttribute ",e);}
	}
	/**
	 * Edit DBPromotion Car method get all the child cars that are stored for the dbPromotion car
	 * Get sku information for Collection car.
	 * 
	 * @param request 
	 * @param response
	 * @return ModelAndView with dbPromotion car details and child car details
	 * @throws Exception
	 */
	
	
	@SuppressWarnings("unchecked")
	public ModelAndView editDBPromotion(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		
		request.getSession().removeAttribute("CHILD_CAR_LIST");
		request.getSession().removeAttribute("REMOVED_CHILD_CAR_LIST");
		Long dbPromotionCarId = ServletRequestUtils.getLongParameter(request, "CarId");
		if (log.isInfoEnabled()) {
			log.info("Editing DBPromotion Car: dbPromotionCarId: "+dbPromotionCarId);
		}
		DBPromotionForm dbPromotionForm=new DBPromotionForm();
		Map<String, Object> model = new HashMap<String, Object>();
		
		List<Car> childCars=new ArrayList<Car>();
		List<ChildCar> viewChildCars=new ArrayList<ChildCar>();
		List<ChildCar> viewChildCarsSkuList=new ArrayList<ChildCar>(); // child cars to View
		List<ChildCar> viewChildCarsSkuListArray[] = (ArrayList<ChildCar>[])new ArrayList[100];
		List<ChildCar> viewChildCarsSkuListArrayGrouped[] = (ArrayList<ChildCar>[])new ArrayList[100];
		try
		{
		Attribute attrChildProducts = dbPromotionManager.getAttributeByName(Constants.DBPROMOTION_DEFAULT_SKUS);
		Attribute templateType = dbPromotionManager.getAttributeByName(Constants.DBPROMOTION_TEMP_TYPE);
		if(dbPromotionCarId!=null && !"".equals(dbPromotionCarId))
		{
			Car dbPromotionCar=dbPromotionManager.getCarFromId(new Long(dbPromotionCarId));
			/*Get the attribute values of an dbPromotion Car*/
			String attrChildProductValue=dbPromotionManager.getAttributeValue(dbPromotionCar,attrChildProducts);
			String[] childProductSKUs = null;
			List<String> lstchildProductSKUs = null;
			/* child product sku as a list to send to getViewChildCarDetails, for priority,color sku*/
			if(attrChildProductValue != null){
				childProductSKUs = attrChildProductValue.split(",");
				lstchildProductSKUs =  Arrays.asList(childProductSKUs);
			}
			if(dbPromotionCar!=null ) {
				/* set the values of dbPromotion Form to view.*/ 
				dbPromotionForm.setDbPromotionName(dbPromotionCar.getVendorStyle().getVendorStyleName());
				dbPromotionForm.setDescription(dbPromotionCar.getVendorStyle().getDescr());
				/* Get all the dbPromotion child cars*/ 
				Set<CarDBPromotionChild> carDBPromotionChild=dbPromotionCar.getCarDBPromotionChild();
				if(carDBPromotionChild != null){		
				String templateTypeVal=dbPromotionManager.getAttributeValue(dbPromotionCar,templateType);
				model.put("templateType", templateTypeVal);
				if(templateTypeVal != null && templateTypeVal.equalsIgnoreCase(Constants.TEMPL_TYPE_VAL_PYG))
				{
					String productCode = dbPromotionCar.getVendorStyle().getVendorNumber()+dbPromotionCar.getVendorStyle().getVendorStyleNumber();
					List<String> lstSelDBPromotionCollectionSKUs = new ArrayList<String>();
					List<DBPromotionCollectionSkus> collSkus = dbPromotionManager.getDBPromotionCollectionSkus(productCode);
					for(DBPromotionCollectionSkus collectSkus: collSkus){
						String strCollSkus = collectSkus.getSkuCode();
						lstSelDBPromotionCollectionSKUs.add(strCollSkus);
					}
					/*  sku as a list to send to getViewChildCarDetails, for priority,color sku*/
					if (request.getSession().getAttribute("CHILD_CAR_SKU_LIST") != null)
					{
						viewChildCarsSkuList = (ArrayList<ChildCar>)request.getSession().getAttribute("CHILD_CAR_SKU_LIST");
					}
					viewChildCarsSkuList = getChildCarSkus(carDBPromotionChild); //not using generalized single style logic
					request.getSession().setAttribute("CHILD_CAR_SKU_LIST",viewChildCarsSkuList);
					long tempSkuCarId = 0;
					int groupCarCounter = -1;
					int childCarCounter = 0;
					boolean executeOnce = false;
					String allChildCarIds = "";
					for(ChildCar vSku: viewChildCarsSkuList){
						ChildCar childCarSkuList= new ChildCar();
						long skuCarId = vSku.getSkuCarid();
						Car carObj = dbPromotionManager.getCarFromId(skuCarId);
						long carStyleId = carObj.getVendorStyle().getVendorStyleId(); //carObj.getCarId(); //////fix for GROUPING
						if (tempSkuCarId == 0 || tempSkuCarId != carStyleId)
						{
							if (groupCarCounter != -1 && tempSkuCarId != 0 && tempSkuCarId != carStyleId)
							{
//								model.put("viewChildCarsSkuList_"+groupCarCounter, viewChildCarsSkuListArray[groupCarCounter]);
							}
							tempSkuCarId = carStyleId;
							groupCarCounter++;
							viewChildCarsSkuListArray[groupCarCounter] = new ArrayList<ChildCar>();
						}
								if (executeOnce == false) {
									long skuCarIdForGroup = 0;
									int tempGroupCarCounter = -1;
									long tempCarIdForGroup = 0;
									long styleIdArray[] = new long[100];
									int groupCounter = 0;
									for (ChildCar vTempSku : getChildCarSkus(carDBPromotionChild)) {
										skuCarIdForGroup = vTempSku.getSkuCarid();
										Car carObjTemp = dbPromotionManager.getCarFromId(skuCarIdForGroup);
										long carStyleIdTemp = carObjTemp.getVendorStyle().getVendorStyleId();
										boolean isOldStyleId = false;
										for (int styleIdArrayCount = 0; styleIdArrayCount < styleIdArray.length; styleIdArrayCount++) {
											if (styleIdArray[styleIdArrayCount] != 0) {
												if (styleIdArray[styleIdArrayCount] == carStyleIdTemp) {
													groupCounter = styleIdArrayCount;
													isOldStyleId = true;
												}
											}
										}
										if (skuCarIdForGroup == 0
												|| (tempCarIdForGroup != carStyleIdTemp && isOldStyleId == false)) {
											
											tempCarIdForGroup = carStyleIdTemp;
											tempGroupCarCounter++;
											styleIdArray[tempGroupCarCounter] = carStyleIdTemp;
											groupCounter = tempGroupCarCounter;
										}

										allChildCarIds += "" + skuCarIdForGroup
												+ "-" + groupCounter + ",";
									}
									executeOnce = true;
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
						childCarSkuList.setAllChildCarIds(allChildCarIds);
						if(lstSelDBPromotionCollectionSKUs != null && lstSelDBPromotionCollectionSKUs.contains(vSku.getSkuID()))
							childCarSkuList.setSkuSelValues(Constants.SELECT_SKUCHECKBOX);
						else
							childCarSkuList.setSkuSelValues(Constants.UNSELECT_SKUCHECKBOX);	
						    viewChildCarsSkuListArray[groupCarCounter].add(childCarSkuList);
						    childCarCounter++;
				 	}
					long styleIdArray[] = new long[100];
					int styleIdCounter = 0;
					for (int childCarCount = 0; childCarCount <= groupCarCounter; childCarCount++){
					    int childCarSkusLength = viewChildCarsSkuListArray[childCarCount].size();
					    for (int childSkuCount = 0; childSkuCount < childCarSkusLength; childSkuCount++){
					    	ChildCar cc = viewChildCarsSkuListArray[childCarCount].get(childSkuCount);
					    	if (cc != null && cc.getSkuCarid() != 0){
								Car carObj = dbPromotionManager.getCarFromId(cc.getSkuCarid());
								long carStyleId = carObj.getVendorStyle().getVendorStyleId();
								boolean isOldStyleId = false;
								for (int styleIdArrayCount = 0; styleIdArrayCount < styleIdArray.length; styleIdArrayCount++) {
									
									if (styleIdArray[styleIdArrayCount] != 0) {//ie checking if value at this index is not 0
//										styleIdCounter++;
										if (styleIdArray[styleIdArrayCount] == carStyleId) {
											viewChildCarsSkuListArrayGrouped[styleIdArrayCount].add(cc);
//											groupCounter = styleIdArrayCount;
											isOldStyleId = true;
										}
									}
								}
								styleIdCounter = getArraySize(styleIdArray);
								if (isOldStyleId == false){
									viewChildCarsSkuListArrayGrouped[styleIdCounter] = new ArrayList<ChildCar>();
									viewChildCarsSkuListArrayGrouped[styleIdCounter].add(cc);
									styleIdArray[styleIdCounter] = carStyleId;
								}
					    	}
					    }
					}
					styleIdCounter = getArraySize(styleIdArray);
					if(styleIdCounter == 0){
						model.put("viewChildCarsSkuList_"+0, viewChildCarsSkuListArrayGrouped[0]);
					}
					int dbPromCarChildCount =DBPromotionUtil.getActiveChildCars(dbPromotionCar).size();
					for (int childCarCount = 0; childCarCount < dbPromCarChildCount; childCarCount++){ //<=0 fix for single sku car in 
						Collections.sort(viewChildCarsSkuListArrayGrouped[childCarCount],new SortingComparator());
						model.put("viewChildCarsSkuList_"+childCarCount, viewChildCarsSkuListArrayGrouped[childCarCount]);
					}
					if (groupCarCounter != -1)
					{
//						model.put("viewChildCarsSkuList_"+groupCarCounter, viewChildCarsSkuListArray[groupCarCounter]);

					}
					model.put("viewChildCarsSkuListArray", viewChildCarsSkuListArrayGrouped);
					model.put("viewChildCarsSkuList", viewChildCarsSkuList);
				}
				/* For each dbPromotion child if it was ACTIVE, get the car details and add to ViewChildCars and childCars.*/
				for(CarDBPromotionChild dbPromotionChild : carDBPromotionChild){
					Car childCar=dbPromotionChild.getChildCar();
					if (log.isDebugEnabled()) {
						log.debug("DBPromotion Child Car Id :"+ childCar.getCarId() + " Status : "+ dbPromotionChild.getStatusCd());
					}
					if(dbPromotionChild.getStatusCd().equals(Status.ACTIVE)){
						viewChildCars.add(dbPromotionManager.getViewChildCarDetails(childCar,dbPromotionChild,lstchildProductSKUs));
						childCars.add(childCar);
//						Collections.sort(viewChildCars,new SortingComparator());
					}
				}
				} else{
					if (log.isDebugEnabled()) {
					 log.debug("DBPromotion Child CARs are null");
					}
				}
			}
			else
			{
				if (log.isDebugEnabled()) {
					log.debug("DBPromotion Car Id :"+dbPromotionCarId+" Not found");
				}
			}		
		}
		model.put("dbPromotionForm", dbPromotionForm);
		}catch(Exception e){log.error("Exception in adding model object in editDBPromotionotion method ",e);}
		ModelAndView mv=new ModelAndView("admin/dbpromotion/edit",model);
		request.getSession().setAttribute("CHILD_CAR_LIST",childCars);
		mv.addObject("viewChildCars", viewChildCars);
		return mv;
	}

	/**
	 * This  method performing following things:
	 * 1. It is called from editOutfit method, when the template type is pyg
	 * 2. This method will fetch all child car sku details in childCarSkuList 
	 *   
	 * @param carOutfitChild  this contains the car id
	 * @return list of child car skus
	 */
	@SuppressWarnings("unchecked")
	public List<ChildCar> getChildCarSkus(Set<CarDBPromotionChild> cardbPromotionChild)
	throws Exception {	
		if (log.isDebugEnabled()) {
			 log.debug("Inside getChildCarSkus method");
		}
		List<ChildCar> viewChildCarsSkuList=new ArrayList<ChildCar>(); // child cars to View
		List<Car> skuCar=new ArrayList<Car>();	
		try
		{
		for(CarDBPromotionChild dbPromotionChild : cardbPromotionChild){
			if(dbPromotionChild.getStatusCd().equals(Status.ACTIVE)){
				Car childCar=dbPromotionChild.getChildCar();
					VendorStyle vendorStyle = childCar.getVendorStyle();	
			skuCar = getDbPromotionManager().getChildCarSkus(vendorStyle);
				for(Car childCarSku:skuCar){
					Set<VendorSku> carVendorSku=childCarSku.getVendorSkus();
					String styleNumStr = childCarSku.getVendorStyle().getVendorStyleNumber();
					for(VendorSku vSku:carVendorSku){
						ChildCar childCarSkuList= new ChildCar();
						childCarSkuList.setSkuCarid(childCarSku.getCarId());
						childCarSkuList.setCompDt(DateUtils.formatDate(childCarSku.getDueDate()));
						childCarSkuList.setVendorName(vSku.getVendorStyle().getVendor().getName());
						childCarSkuList.setVendorStyle(styleNumStr);
						childCarSkuList.setSkuStyleName(vSku.getVendorStyle().getVendorStyleName());						
						childCarSkuList.setVendorUpc(vSku.getBelkUpc());
						childCarSkuList.setSkuColor(vSku.getColorCode());
						childCarSkuList.setColorName(vSku.getColorName());
						childCarSkuList.setSizeName(vSku.getSizeName());
						childCarSkuList.setSkuID(vSku.getBelkSku());
						childCarSkuList.setBelkSku(vSku.getBelkSku());
						viewChildCarsSkuList.add(childCarSkuList);
						//Collections.sort(viewChildCarsSkuList,new SortingComparator());
					}
			}
		}
		}
		}catch(Exception e){log.error("Exception in getChildCarSkus method ",e);}
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

	public int getArraySize(long[] inpArray) {
		int arrSize = 0;
		if (inpArray != null){
			for(int i = 0; i< inpArray.length; i++){
				if (inpArray[i] != 0){
					arrSize++;
				}
			}
		}
		return arrSize;
	}

}
