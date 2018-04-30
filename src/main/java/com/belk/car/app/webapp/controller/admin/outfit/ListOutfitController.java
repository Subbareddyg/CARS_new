package com.belk.car.app.webapp.controller.admin.outfit;

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
import com.belk.car.app.model.CarOutfitChild;
import com.belk.car.app.model.CollectionSkus;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.service.OutfitManager;
import com.belk.car.app.webapp.controller.MultiActionFormController;
import com.belk.car.app.webapp.forms.ChildCar;
import com.belk.car.app.webapp.forms.OutfitForm;
import com.belk.car.util.DateUtils;
import com.belk.car.util.SortingComparator;


/**
 * 
 * @author afunxy1
 * 
 * MultiAction controller for Listing, Search, Edit and Remove Outfit Cars.
 *
 */

public class ListOutfitController extends MultiActionFormController {
	private transient final Log log = LogFactory.getLog(ListOutfitController.class);
	public OutfitManager outfitManager;
	public OutfitManager getOutfitManager() {
		return outfitManager;
	}
	public void setOutfitManager(OutfitManager outfitManager) {
		this.outfitManager = outfitManager;
	}

	/**
	 * List all the Outfit Cars which are ACTIVE status.
	 * 
	 * @param request
	 * @param response
	 * @return ModealAndView with list of outfit cars in Model
	 * @throws Exception
	 */
	
	public ModelAndView getAllOutfitCars(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("Getting all the Outfit Cars");
		User user = getLoggedInUser();
		if (user.getAdmin()) {
			request.getSession().setAttribute("displayRole", "admin");
		}
		// Removing session variables created in ChildCarFormController, RemoveChildCarController,ListOutfitController 
		request.getSession().removeAttribute("CHILD_CAR_LIST");
		request.getSession().removeAttribute("REMOVED_CHILD_CAR_LIST");
		List<Car> outfitCars=new ArrayList<Car>();
		try {
			outfitCars= outfitManager.getAllOutfits();
			if(outfitCars!=null){
				if(log.isInfoEnabled()){
					log.info("Found " +outfitCars.size()+ " outfit cars");
				}
			}
		}catch(Exception e){
			log.error("Exception in getAllOutfitCars",e);
		}
		return new ModelAndView(getSuccessView(), Constants.OUTFIT_LIST,outfitCars);
	}
	
	/**
	 * Search an Outfit Car by outfitName and or styleNumber
	 * 
	 * @param request : outfitName and or styleNumber
	 * @param response
	 * @return MovdelAndView to the list of searched outfit cars in Model
	 * @throws Exception
	 */
	public ModelAndView Search(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			String outfitName = request.getParameter("outfitName")==null?"": request.getParameter("outfitName").trim();
			String styleNumber=request.getParameter("styleNumber")==null?"": request.getParameter("styleNumber").trim();
			if (log.isInfoEnabled()) {
				log.info("Searching car with - outfit Name:" + outfitName + " style Number :"+styleNumber);
			}
			Map<String, Object> model = new HashMap<String, Object>();
			// If both outfitName and styleNumber are empty, showing error message
			if (("".equals(outfitName)) && ("".equals(styleNumber)) ) {
					log.debug("outfitName and styleNumber are emtpy");
					saveError(request, getText("Outfit Name/Style Number cannot be empty", request.getLocale()));
				    model.put("outfitName", "");
				    model.put("styleNumber", "");
				 	return new ModelAndView(getSuccessView(), model);
			}
			model.put("outfitName", outfitName);
			model.put("styleNumber", styleNumber);
			List<Car> cars =null;
			try{
				cars = outfitManager.serachOutfitCars(outfitName,styleNumber);
				 if (cars!=null){
			        if(log.isInfoEnabled()){	
			        	log.info("found "+cars.size() + " cars as search result");
			        }
				 }
			}catch(Exception e){
				log.error("Exception in Outfit Search",e);
				e.printStackTrace();
			}
	        model.put(Constants.OUTFIT_LIST, cars);
			return new ModelAndView(getSuccessView(), model);
		}
	
	/**
	 * Remove an Outfit Car
	 * 
	 * @param request: CarId
	 * @param response
	 * @return redirecting ModelAndView to the list outfit cars 
	 * @throws Exception
	 */
	
	public ModelAndView removeOutfit(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		  Long outfitCarId = ServletRequestUtils.getLongParameter(request, "CarId");
		  log.info("Removeing Outfit Car: outfitCarId:" + outfitCarId );
		  // Get outfit Car object by outfit car id
		  Car outfitCar=outfitManager.getCarFromId(new Long(outfitCarId));
		  // Get the outfit Car Product Code as combination of vendor number and vendor style number
		  String strOutfitProductCode = outfitCar.getVendorStyle().getVendorNumber()+ outfitCar.getVendorStyle().getVendorStyleNumber();
		  User user = getLoggedInUser();
		  // Mark outfit Car status as DELETED and set updated by and updated date.
		  outfitCar.setStatusCd(Status.DELETED);
		  outfitCar.setUpdatedBy(user.getEmailAddress());
		  outfitCar.setUpdatedDate(new Date());
		  try{
			  // Save outfit Car
			  outfitManager.saveorUpdate(outfitCar);
		  }catch(Exception e){
			  log.error("Exception in remove Outfit Car ",e);
		  }
		  
		  // Get the child cars of outfit
		  Set<CarOutfitChild> carOutfitChild=outfitCar.getCarOutfitChild();
		  // for each child car update the status.
		  for(CarOutfitChild outfitChild : carOutfitChild){
			    // update the outfit child cars status to deleted
		        outfitChild.setStatusCd(Status.DELETED);
		        outfitChild.setUpdatedBy(user.getEmailAddress());
		        outfitChild.setUpdatedDate(new Date());
		        Car childCar = outfitChild.getChildCar();
		        // Get the OUTFIT_PARENT_PRODUCT attribute from child car and remove the Outfit product code from attribute value
		        CarAttribute ca = outfitManager.getCarAttributeByBMName(childCar, Constants.OUTFIT_PARENT_PRODUCTS);
		        CarAttribute caSkus = outfitManager.getCarAttributeByBMName(childCar, Constants.COLLECTION_PARENT_PRODUCTS);
		        CarAttribute caSearchable = outfitManager.getCarAttributeByBMName(childCar, Constants.COLLECTION_IS_PRODUCT_SEARCHABLE);
		        CarAttribute caType = outfitManager.getCarAttributeByBMName(childCar, Constants.COLLECTION_OUTFIT_TYPE);
		        CarAttribute caEffDate = outfitManager.getCarAttributeByBMName(childCar, Constants.COLLECTION_EFFECTIVE_DATE);
		        if(ca != null){
		        	deleteSkuAttribute(ca, strOutfitProductCode, user);
		        }
		        if(caSkus != null){
		        	deleteSkuAttribute(caSkus, strOutfitProductCode, user);
		        }
		        if(caSearchable != null){
		        	deleteSkuAttribute(caSearchable, strOutfitProductCode, user);
		        }
		        if(caType != null){
		        	deleteSkuAttribute(caType, strOutfitProductCode, user);
		        }
		        if(caEffDate != null){
		        	deleteSkuAttribute(caEffDate, strOutfitProductCode, user);
		        }
		        try{
		        	// Save outfitChild 
		        	outfitManager.saveorUpdate(outfitChild);
		        }catch(Exception e){
		        	log.error("Exception update outfitChild ",e);
		        }
		  }	  
		  log.info("Outfit with CarId:" + outfitCarId +" is removed by :"+user.getEmailAddress() );
		  return new ModelAndView("redirect:/admin/outfit/outfits.html?method=getAllOutfitCars");
	}
	
	public void deleteSkuAttribute(CarAttribute ca, String strOutfitProductCode, User user)
	{
		try{
			if(ca != null){
	        	  String strParentProducts = ca.getAttrValue();
	              StringBuffer sb= new StringBuffer(); 
	              String resultAttrValue1 = null;
	              String resultAttrValue2 = null;
	              String resultAttrValue3 = null;
	              boolean isContains = strParentProducts.contains(strOutfitProductCode);
	              if(isContains){
	            	resultAttrValue1=strParentProducts.replaceAll(strOutfitProductCode,"");
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
	              // update the outfit child car attributes.
	              ca.setUpdatedBy(user.getEmailAddress());
			      ca.setUpdatedDate(new Date());
	              ca.setAttrValue(sb.toString());
	              try{
	            	  //Save child car's attribute
	            	  outfitManager.saveorUpdate(ca);
	              }catch(Exception e){
	            	  log.error("Exception in update child car attributes ",e);
	              }          
	        }
		}catch(Exception e){log.error("Exception in deleteSkuAttribute ",e);}
	}
	/**
	 * Edit Outfit Car method get all the child cars that are stored for the outfit car
	 * Get sku information for Collection car.
	 * 
	 * @param request 
	 * @param response
	 * @return ModelAndView with outfit car details and child car details
	 * @throws Exception
	 */
	
	
	@SuppressWarnings("unchecked")
	public ModelAndView editOutfit(HttpServletRequest request,HttpServletResponse response) throws Exception {
		request.getSession().removeAttribute("CHILD_CAR_LIST");
		request.getSession().removeAttribute("REMOVED_CHILD_CAR_LIST");
		Long outfitCarId = ServletRequestUtils.getLongParameter(request, "CarId");
		if (log.isInfoEnabled()) {
			log.info("Editing Outfit Car: outfitCarId: "+outfitCarId);
		}
		OutfitForm outfitForm=new OutfitForm();
		Map<String, Object> model = new HashMap<String, Object>();
		
		List<Car> childCars=new ArrayList<Car>();
		List<ChildCar> viewChildCars=new ArrayList<ChildCar>();
		List<ChildCar> viewChildCarsSkuList=new ArrayList<ChildCar>(); // child cars to View
		List<ChildCar> viewChildCarsSkuListArray[] = (ArrayList<ChildCar>[])new ArrayList[100];
		List<ChildCar> viewChildCarsSkuListArrayGrouped[] = (ArrayList<ChildCar>[])new ArrayList[100];
		try
		{
		Attribute attrChildProducts = outfitManager.getAttributeByName(Constants.OUTFIT_CHILD_PRODUCTS);
		Attribute templateType = outfitManager.getAttributeByName(Constants.COLLECTION_OUTFIT_TYPE);
		Attribute effectiveDate = outfitManager.getAttributeByName(Constants.COLLECTION_EFFECTIVE_DATE);
		Attribute isSearchable = outfitManager.getAttributeByName(Constants.COLLECTION_IS_PRODUCT_SEARCHABLE);
		if(outfitCarId!=null && !"".equals(outfitCarId))
		{
			Car outfitCar=outfitManager.getCarFromId(new Long(outfitCarId));
			/*Get the attribute values of an outfit Car*/
			String attrChildProductValue=outfitManager.getAttributeValue(outfitCar,attrChildProducts);
			String[] childProductSKUs = null;
			List<String> lstchildProductSKUs = null;
			/* child product sku as a list to send to getViewChildCarDetails, for priority,color sku*/
			if(attrChildProductValue != null){
				childProductSKUs = attrChildProductValue.split(",");
				lstchildProductSKUs =  Arrays.asList(childProductSKUs);
			}
			if(outfitCar!=null ) {
				/* set the values of outfit Form to view.*/ 
				outfitForm.setOutfitName(outfitCar.getVendorStyle().getVendorStyleName());
				outfitForm.setDescription(outfitCar.getVendorStyle().getDescr());
				/* Get all the outfit child cars*/ 
				Set<CarOutfitChild> carOutfitChild=outfitCar.getCarOutfitChild();
				if(carOutfitChild != null){		
				String templateTypeVal=outfitManager.getAttributeValue(outfitCar,templateType);
				model.put("templateType", templateTypeVal);
				if(templateTypeVal != null && templateTypeVal.equalsIgnoreCase("COLLECTION"))
				{
					String productCode = outfitCar.getVendorStyle().getVendorNumber()+outfitCar.getVendorStyle().getVendorStyleNumber();
					
					//String attrChildSkuValue=outfitManager.getAttributeValue(outfitCar,collectionSkus);
					String effectiveDateVal=outfitManager.getAttributeValue(outfitCar,effectiveDate);
				    String isSearchableVal=outfitManager.getAttributeValue(outfitCar,isSearchable);
					model.put("effectiveDate", effectiveDateVal);
					model.put("isSearchable", isSearchableVal);
					List<String> lstSelCollectionSKUs = new ArrayList<String>();
					List<CollectionSkus> collSkus = outfitManager.getCollectionSkus(productCode);

					for(CollectionSkus collectSkus: collSkus){
						String strCollSkus = collectSkus.getSkuCode();
						lstSelCollectionSKUs.add(strCollSkus);
					}
					/*  sku as a list to send to getViewChildCarDetails, for priority,color sku*/
					if (request.getSession().getAttribute("CHILD_CAR_SKU_LIST") != null)
					{
						viewChildCarsSkuList = (ArrayList<ChildCar>)request.getSession().getAttribute("CHILD_CAR_SKU_LIST");
					}
					viewChildCarsSkuList = getChildCarSkus(carOutfitChild);
					request.getSession().setAttribute("CHILD_CAR_SKU_LIST",viewChildCarsSkuList);
					long tempSkuCarId = 0;
					int groupCarCounter = -1;
					int childCarCounter = 0;
					boolean executeOnce = false;
					String allChildCarIds = "";
					for(ChildCar vSku: viewChildCarsSkuList){
						ChildCar childCarSkuList= new ChildCar();
						long skuCarId = vSku.getSkuCarid();
						Car carObj = outfitManager.getCarFromId(skuCarId);
						long carStyleId = carObj.getVendorStyle().getVendorStyleId();
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
									for (ChildCar vTempSku : viewChildCarsSkuList) {
										skuCarIdForGroup = vTempSku.getSkuCarid();

										Car carObjTemp = outfitManager.getCarFromId(skuCarIdForGroup);
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
						if(lstSelCollectionSKUs != null && lstSelCollectionSKUs.contains(vSku.getSkuID()))
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
								Car carObj = outfitManager.getCarFromId(cc.getSkuCarid());
								long carStyleId = carObj.getVendorStyle().getVendorStyleId();
								boolean isOldStyleId = false;
								for (int styleIdArrayCount = 0; styleIdArrayCount < styleIdArray.length; styleIdArrayCount++) {
									if (styleIdArray[styleIdArrayCount] != 0) {
//										styleIdCounter++;
										if (styleIdArray[styleIdArrayCount] == carStyleId) {
											viewChildCarsSkuListArrayGrouped[styleIdArrayCount].add(cc);
//											groupCounter = styleIdArrayCount;
											isOldStyleId = true;
										}
									}
								}
								if (isOldStyleId == false){
									viewChildCarsSkuListArrayGrouped[styleIdCounter] = new ArrayList<ChildCar>();
									viewChildCarsSkuListArrayGrouped[styleIdCounter].add(cc);
									styleIdArray[styleIdCounter] = carStyleId;
								}
								styleIdCounter = getArraySize(styleIdArray);
					    	}
					    }
					}
					for (int childCarCount = 0; childCarCount < styleIdCounter; childCarCount++){
						Collections.sort(viewChildCarsSkuListArrayGrouped[childCarCount],new SortingComparator());
						model.put("viewChildCarsSkuList_"+childCarCount, viewChildCarsSkuListArrayGrouped[childCarCount]);
					}

				    //int arrayLength = viewChildCarsSkuListArray.length;

					if (groupCarCounter != -1)
					{
//						model.put("viewChildCarsSkuList_"+groupCarCounter, viewChildCarsSkuListArray[groupCarCounter]);

					}
					model.put("viewChildCarsSkuListArray", viewChildCarsSkuListArrayGrouped);
					model.put("viewChildCarsSkuList", viewChildCarsSkuList);
				}
				/* For each outfit child if it was ACTIVE, get the car details and add to ViewChildCars and childCars.*/
				for(CarOutfitChild outfitChild : carOutfitChild){
					Car childCar=outfitChild.getChildCar();
					if (log.isDebugEnabled()) {
						log.debug("Outfit Child Car Id :"+ childCar.getCarId() + " Status : "+ outfitChild.getStatusCd());
					}
					if(outfitChild.getStatusCd().equals(Status.ACTIVE)){
						viewChildCars.add(outfitManager.getViewChildCarDetails(childCar,outfitChild,lstchildProductSKUs));
						childCars.add(childCar);
//						Collections.sort(viewChildCars,new SortingComparator());
					}
				}
				} else{
					if (log.isDebugEnabled()) {
					 log.debug("Outfit Child CARs are null");
					}
				}
			}
			else
			{
				if (log.isDebugEnabled()) {
					log.debug("Outfit Car Id :"+outfitCarId+" Not found");
				}
			}		
		}
		model.put("outfitForm", outfitForm);
		}catch(Exception e){log.error("Exception in adding model object in editOutfit method ",e);}
		ModelAndView mv=new ModelAndView("admin/outfit/edit",model);
		request.getSession().setAttribute("CHILD_CAR_LIST",childCars);
		mv.addObject("viewChildCars", viewChildCars);
		return mv;
	}

	/**
	 * This  method performing following things:
	 * 1. It is called from editOutfit method, when the template type is collection. 
	 * 2. This method will fetch all child car sku details in childCarSkuList 
	 *   
	 * @param carOutfitChild  this contains the car id
	 * @return list of child car skus
	 */
	@SuppressWarnings("unchecked")
	public List<ChildCar> getChildCarSkus(Set<CarOutfitChild> carOutfitChild)
	throws Exception {	
		if (log.isDebugEnabled()) {
			 log.debug("Inside getChildCarSkus method");
		}
		List<ChildCar> viewChildCarsSkuList=new ArrayList<ChildCar>(); // child cars to View
		List<Car> skuCar=new ArrayList<Car>();	
		try
		{
		for(CarOutfitChild outfitChild : carOutfitChild){
			if(outfitChild.getStatusCd().equals(Status.ACTIVE)){
				Car childCar=outfitChild.getChildCar();
					VendorStyle vendorStyle = childCar.getVendorStyle();	
			skuCar = outfitManager.getChildCarSkus(vendorStyle);
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
