package com.belk.car.app.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;

import com.belk.car.app.Constants;
import com.belk.car.app.dao.OutfitDao;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeValueProcessStatus;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarOutfitChild;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.service.CarLookupManager;

/**
 * 
 * @author AFUSY12/ santosh_chaudhary@belk.com 
 *  This is utility class to save outfit car after modification
 *  SaveOutfit will add or remove any child car from outfit and 
 *  modify the OUFIT_CHILD_PRODUCTS AND OUTFIT_PARENT_PRODUCT attributes
 */
public class SaveOutfitCar {

	private static transient final Log log = LogFactory.getLog(SaveOutfitCar.class);
	private CarLookupManager lookupManager;
	private OutfitDao outfitDao; 
	private Attribute attrParentProducts = null;
	private Attribute attrChildProducts =null;
	
	public void setLookupManager(CarLookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}

	public void setOutfitDao(OutfitDao outfitDao) {
		this.outfitDao = outfitDao;
	}
	
	/**
	 * Saves the outfit CAR after modification
	 * @param outfitCar
	 * @param name
	 * @param desc
	 * @param childCars
	 * @param orderCars
	 * @param skus
	 */
	public void saveOutfit(Car outfitCar, String name, String desc, List<Car> childCars, List<String> orderCars, List<String> skus,String templateType){

		boolean isVSModified = false;
		List<Car> outfitChildCars = new ArrayList<Car>();
		int index =0;
		
		if(templateType.equalsIgnoreCase(Constants.ATTR_COLLECTION)){
			if(desc == null || childCars.size()<1){
				log.error("Can not create outfit car : Insufficient data \t Desc:"+ desc + "\t childcars size: "+ childCars.size());	
			  return ;
		}}
		
		else{
		if(desc == null || childCars.size()< 2){
		  log.error("Can not create outfit car : Insufficient data \t Desc:"+ desc + "\t childcars size: "+ childCars.size());	
		  return ;
		}}
		
		try{
			VendorStyle outfitStyle = outfitCar.getVendorStyle();
			if(outfitStyle != null){
				
				//If Vendor Style Name is null or is not matching to the earlier saved one, then set it in 
				//Vendor Style object.
				if(outfitStyle.getVendorStyleName()== null || !outfitStyle.getVendorStyleName().equals(name)){
					outfitStyle.setVendorStyleName(name);
					isVSModified =true;
					if(log.isInfoEnabled()){
						log.info("Outift name changed: "+ name);
					}
				}
				//If Vendor Style Description is null or is not matching to the earlier saved one, then set it in 
				//Vendor Style object.
				if(outfitStyle.getDescr()== null || !outfitStyle.getDescr().equals(desc)){
					outfitStyle.setDescr(desc);
					isVSModified = true;
					if(log.isInfoEnabled()){
						log.info("Outift Description changed: "+ desc);
					}
				}
				//If Vendor style is modified then save object.
				if(isVSModified){
					outfitDao.save(outfitStyle);
				}
			}
			 
			attrParentProducts = outfitDao.getAttributeByName(Constants.OUTFIT_PARENT_PRODUCTS);
			attrChildProducts = outfitDao.getAttributeByName(Constants.OUTFIT_CHILD_PRODUCTS);
			User user = getLoggedInUser();
			 
			//Logic to check if any car is removed or still there also creating list of outfitChildCars
			for(CarOutfitChild outfitChild : outfitCar.getCarOutfitChild() ){
				if(outfitChild.getStatusCd().equals(Status.ACTIVE)) {
					Car outfitChildCar = outfitChild.getChildCar();
					if((index=childCars.indexOf(outfitChildCar)) != -1){
						outfitChildCars.add(outfitChildCar);
						VendorSku vendorSkuRetrievedFromForm = outfitDao.getVendorSku(outfitChildCar.getCarId(), skus.get(index));
						//update the outfit Child Car with new default color code.
						outfitChild.setDefaultColorSku(vendorSkuRetrievedFromForm);
						outfitChild.setPriority(Integer.valueOf(orderCars.get(index)));
						outfitDao.save(outfitChild);
						if(log.isInfoEnabled()){
							log.info("Setting the defalt color and priority for existing outfit child car ");
						}
					}else{
						//Removing CAR from outfit
						removeCarFromOutfit(outfitCar, outfitChild, user);
					}
				}
			}
			
			//Check for any newly added child CARs
			for(int i = 0; i < childCars.size(); i++){
				//create entry in Car_Outfit_Child table for each child car
				Car childCar = childCars.get(i);
				if(!outfitChildCars.contains(childCar)){
					//Adding the CAR to Outfit, Updating priority as well.
					addCarToOutfit(outfitCar, childCar, Integer.valueOf(orderCars.get(i)), skus.get(i), user );
				}
			}
			
			//Get the outfit child default color skus delimited by comma according to priority 
			// eg: sku1,sku2,sku3
			String strOutfitChildProductsSku = sortSkusAccordingToPriority(orderCars, skus);
			
			//Get the outfit car attribute attrChildProducts
			CarAttribute childProductsCarAttr = outfitDao.getCarAttributeByAttributeName(outfitCar,attrChildProducts.getName());
			
			//Modify the OUTFIT_CHILD_PRODUCT attribute
			childProductsCarAttr.setAttrValue(strOutfitChildProductsSku);
			childProductsCarAttr.setUpdatedBy(user.getEmailAddress());
			childProductsCarAttr.setUpdatedDate(new Date());
			if(log.isDebugEnabled()){
				log.debug(" Setting child product values to - " +strOutfitChildProductsSku);
			}
			outfitDao.save(childProductsCarAttr);
			
		}catch(Exception e){
			log.error("error occured while saving outfit car", e);
			e.printStackTrace();
			
		}
	}
	
	
	/**
	 * @param skuPriority
	 * @param skus
	 * @return skus's delimited by comma  eg: sku1,sku2,sku3
	 */
	public String sortSkusAccordingToPriority(List<String> skuPriority, List<String> skus){
		StringBuffer strSortedSkus = new StringBuffer("");
		Map<Integer, String> orderedSkus = new HashMap<Integer, String>();
		int maxPriority =0;
		//create the map of {priority , sku}
		for(int i=0; i<skuPriority.size(); i++){
			
			String x = skuPriority.get(i);
			int currentPriority = Integer.valueOf(x);
			orderedSkus.put(currentPriority, skus.get(i));
			if(maxPriority < currentPriority){
				maxPriority = currentPriority;
			}
		}
		//create the string of skus, looping through priority
		for(int i=1; i<=maxPriority; i++){
			String sku = orderedSkus.get(i);
			if(sku != null){
				strSortedSkus.append(sku +",");
			}
		}
		//Delete the last comma from string
		strSortedSkus.deleteCharAt(strSortedSkus.length()-1);
		if(log.isInfoEnabled()){
			log.info("Sorted Sku string : "+ strSortedSkus.toString());
		}
		return strSortedSkus.toString();
	}
	
	
	/**
	 * This method is to remove the outfitChild car from the outfitCar and modify the OUTFIT_PARENT_PRODUCTS attributes 
	 * @param outfitCar
	 * @param outfitChild
	 * @param user
	 */
	public void removeCarFromOutfit(Car outfitCar, CarOutfitChild outfitChild, User user ){
		//remove car code
		Car childCar = outfitChild.getChildCar();
		
		outfitChild.setStatusCd(Status.DELETED);
		outfitChild.setUpdatedBy(user.getEmailAddress());
		outfitChild.setUpdatedDate(new Date());
		outfitDao.save(outfitChild);
		
		if(log.isInfoEnabled()){
			log.info("Deleted the CAR_OUTFIT_CHILD car id: "+ childCar.getCarId() +"for outfit car Id : "+outfitCar.getCarId() );
		}
		
		String strOutfitProductName = outfitCar.getVendorStyle().getVendorNumber()+""+ outfitCar.getVendorStyle().getVendorStyleNumber(); 
		int  intStartIndex = -1;
		int intEndIndex = -1;
		StringBuffer strBuffProds =  new StringBuffer();
		
		CarAttribute carAttrParentProducts = outfitDao.findAttributeInCar(outfitCar, attrParentProducts);
		if(carAttrParentProducts != null){
			String strParentProds = carAttrParentProducts.getAttrValue();
			
			//below logic is to remove the outfit parent product name from child car attribute "OUTFIT_PARENT_PRODUCTS"
			if((intStartIndex = strParentProds.indexOf(strOutfitProductName)) >= 0){
				strBuffProds = new StringBuffer(strParentProds);
				intEndIndex = intStartIndex + strOutfitProductName.length()+1;
				if(intEndIndex > strBuffProds.length()){
					intEndIndex = strBuffProds.length();
				}else{
					intStartIndex = (intStartIndex-1 < 0 )? 0 : intStartIndex-1 ;
				}
				strBuffProds.delete(intStartIndex, intEndIndex);
				strParentProds =strBuffProds.toString();
			}
			carAttrParentProducts.setAttrValue(strParentProds);
			carAttrParentProducts.setUpdatedBy(user.getEmailAddress());
			carAttrParentProducts.setUpdatedDate(new Date());
			outfitDao.save(carAttrParentProducts);
		}
		if(log.isDebugEnabled()){
			log.debug("removed the parent product name from the OUTFIT_PARENT_PRODUCTS list  - child car Id: " + childCar.getCarId());
		}
		
	}
	
	public void removeOutfitFromAttribute(Car outfitCar, CarOutfitChild outfitChild, User user ){
		//remove car code
		Car childCar = outfitChild.getChildCar();
		
		outfitChild.setStatusCd(Status.DELETED);
		outfitChild.setUpdatedBy(user.getEmailAddress());
		outfitChild.setUpdatedDate(new Date());
		outfitDao.save(outfitChild);
		
		if(log.isInfoEnabled()){
			log.info("Deleted the CAR_OUTFIT_CHILD car id: "+ childCar.getCarId() +"for outfit car Id : "+outfitCar.getCarId() );
		}
		
		String strOutfitProductName = ""; 
		int  intStartIndex = -1;
		int intEndIndex = -1;
		StringBuffer strBuffProds =  new StringBuffer();
		
		CarAttribute carAttrParentProducts = outfitDao.findAttributeInCar(outfitCar, attrParentProducts);
		if(carAttrParentProducts != null){
			String strParentProds = carAttrParentProducts.getAttrValue();
			
			//below logic is to remove the outfit parent product name from child car attribute "OUTFIT_PARENT_PRODUCTS"
			if((intStartIndex = strParentProds.indexOf(strOutfitProductName)) >= 0){
				strBuffProds = new StringBuffer(strParentProds);
				intEndIndex = intStartIndex + strOutfitProductName.length()+1;
				if(intEndIndex > strBuffProds.length()){
					intEndIndex = strBuffProds.length();
				}else{
					intStartIndex = (intStartIndex-1 < 0 )? 0 : intStartIndex-1 ;
				}
				strBuffProds.delete(intStartIndex, intEndIndex);
				strParentProds =strBuffProds.toString();
			}
			carAttrParentProducts.setAttrValue(strParentProds);
			carAttrParentProducts.setUpdatedBy(user.getEmailAddress());
			carAttrParentProducts.setUpdatedDate(new Date());
			outfitDao.save(carAttrParentProducts);
		}
		if(log.isDebugEnabled()){
			log.debug("removed the parent product name from the OUTFIT_PARENT_PRODUCTS list  - child car Id: " + childCar.getCarId());
		}
		
	}
	
	
	/**
	 * This method is to add the childCAR to the OutfitCar and modify the OUTFIT_CHILD_PRODUCTS/OUTFIT_PARENT_PRODUCTS attributes
	 * @param outfitCar
	 * @param childCar
	 * @param piority
	 * @param  sku
	 * @param user
	 */
	public void addCarToOutfit(Car outfitCar, Car childCar, int priority, String sku, User user ){
		//add car code
		CarOutfitChild outfitChild = new CarOutfitChild();
		outfitChild.setOutfitCar(outfitCar);
		outfitChild.setChildCar(childCar);
		VendorSku defaultColorSku = outfitDao.getVendorSku(childCar.getCarId(), sku);
		outfitChild.setPriority(priority);
		outfitChild.setDefaultColorSku(defaultColorSku);
		outfitChild.setStatusCd(Status.ACTIVE);
		outfitChild.setAuditInfo(user);
		outfitDao.save(outfitChild);
		if(log.isInfoEnabled()){
			log.info("Created the new CAR_OUTFIT_CHILD - child car id : "+ childCar.getCarId() +" for outfit car ID : "+ outfitCar.getCarId() );
		}
		
		//Get the outfit Product Name
		String strOutfitProductName = outfitCar.getVendorStyle().getVendorNumber()+""+ outfitCar.getVendorStyle().getVendorStyleNumber(); 
		
		//add outfit product name to OUTFIT_PARENT_PRODUCTS attribute value of a child car.
		CarAttribute carAttrParentProducts = outfitDao.findAttributeInCar(childCar, attrParentProducts);
		if(carAttrParentProducts == null){
			//If no parent was set, create new car_attribute.
			carAttrParentProducts = new CarAttribute();
			carAttrParentProducts.setDisplaySeq((short) 0);
			carAttrParentProducts.setHasChanged(Constants.FLAG_NO);
			carAttrParentProducts.setIsChangeRequired(Constants.FLAG_YES);
			carAttrParentProducts.setStatusCd(Status.ACTIVE);
			AttributeValueProcessStatus noCheckRequired = lookupManager.getAttributeValueProcessStatus(AttributeValueProcessStatus.NO_CHECK_REQUIRED);
			carAttrParentProducts.setAttributeValueProcessStatus(noCheckRequired);
			
			carAttrParentProducts.setAuditInfo(user);
			carAttrParentProducts.setAttribute(attrParentProducts);
			carAttrParentProducts.setAttrValue(strOutfitProductName);
			carAttrParentProducts.setCar(childCar);
		}else{
			//If Parent was set, then compare Parent Product String.
			String strParentProds = carAttrParentProducts.getAttrValue();
			if(strParentProds == null || !strParentProds.contains(strOutfitProductName)){
				if(strParentProds != null && strParentProds.length()>1){
					strParentProds = strParentProds.concat(","+strOutfitProductName);
				}else{
					strParentProds = strOutfitProductName;
				}
			}
			//set Attribute value back.
			carAttrParentProducts.setAttrValue(strParentProds);
			carAttrParentProducts.setUpdatedBy(user.getEmailAddress());
			carAttrParentProducts.setUpdatedDate(new Date());
		}
		//Save the attribute.
		outfitDao.save(carAttrParentProducts);
		
		if(log.isDebugEnabled()){
			log.debug("Added the parent product name in OUTFIT_PARENT_PRODUCTS attribute of child car");
		}
	}
	
	
	/**
	 * 	
	 * @return returns the current logged in user
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
