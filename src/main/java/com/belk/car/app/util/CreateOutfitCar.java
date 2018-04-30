package com.belk.car.app.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.CollectionSkus;
import com.belk.car.app.model.CollectionSkusId;
import com.belk.car.app.model.ContentStatus;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.HtmlDisplayType;
import com.belk.car.app.model.OutfitCarAttribute;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.model.SourceType;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.VendorStyleType;
import com.belk.car.app.model.WorkFlow;
import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.model.workflow.WorkflowType;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.ProductManager;
import com.belk.car.app.service.WorkflowManager;

/**
 * 
 * @author AFUSY12/ santosh_chaudhary@belk.com 
 *
 */
public class CreateOutfitCar {
	
	private static transient final Log log = LogFactory.getLog(CreateOutfitCar.class);
	private CarManager carManager;
	private CarLookupManager lookupManager;
	private WorkflowManager workflowManager;
	private ProductManager productManager;
	private OutfitDao outfitDao;
	
	
	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	public void setLookupManager(CarLookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}

	public void setWorkflowManager(WorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}

	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}

	public void setOutfitDao(OutfitDao outfitDao) {
		this.outfitDao = outfitDao;
	}
	
	/**
	 * This method is to create the outfit car
	 * @param name, desc, chilCars, isbmProductName
	 */
	public Car createOutfit(String name, String desc, List<Car> childCars, List<String> orderCars, List<String> skus,String templateType){
		
		if (log.isInfoEnabled()){
			log.info("Creating the outfit car");
		}
		
		//need at least 2 child cars for outift
		if(templateType.equalsIgnoreCase(Constants.ATTR_COLLECTION)){
			if(desc == null || childCars.size()<1){
				log.error("Can not create outfit car : Insufficient data \t Desc:"+ desc + "\t childcars size: "+ childCars.size());	
			  return null;
		}}
		
		else{
		if(desc == null || childCars.size()< 2){
		  log.error("Can not create outfit car : Insufficient data \t Desc:"+ desc + "\t childcars size: "+ childCars.size());	
		  return null;
		}}
		if(childCars.size()!= orderCars.size() && orderCars.size() != skus.size()){
			log.error("Can not create outfit car : Data mistmatch ");	
			return null;
		}
		name = name==null? "" : name.trim();	
		Car car = new Car();
		try{
			
			ProductType productType =null;
			WorkflowStatus initiated = (WorkflowStatus) this.lookupManager.getById(WorkflowStatus.class, WorkflowStatus.INITIATED);
			UserType buyer = (UserType) this.lookupManager.getById(UserType.class, UserType.BUYER);
			SourceType sourceOutfitCar = carManager.getSourceTypeForCode(SourceType.OUTFIT);
			WorkFlow defaultWorkflow = workflowManager.getWorkFlow(workflowManager.getWorkflowType(WorkflowType.CAR));
			ContentStatus contentInProgress = (ContentStatus) lookupManager.getById(ContentStatus.class, ContentStatus.IN_PROGRESS);
			VendorStyleType vendorStyleTypeProduct = (VendorStyleType) lookupManager.getById(VendorStyleType.class, VendorStyleType.OUTFIT);
			User currentUser = getLoggedInUser();
			Department dept = carManager.getDepartment(Constants.OUTFIT_DEPT_NUMBER);
			Classification classification = carManager.getClassification(Short.parseShort(Constants.OUTFIT_CLASS_NUMBER));
			List<ProductType> productTypes =  productManager.getProductTypes(classification.getClassId());
			if(productTypes!= null){
				productType = productTypes.get(0);
			}
			//get the vendor number for outfit car #9999999
			Vendor vendor = carManager.getVendor(Constants.OUTFIT_VENDOR_NUMBER);
			String vendorStyleNumber =null;
			
			//need unique number to generate the vendor style number, so using sequence
			//vendor style number is combination of seq number + first 4 digit of outfit car description
			long outfitStyleId = outfitDao.getNextCarSequence();
			String strAppendOutfitStyle = (name == null) ? "" : name.replaceAll("\\W+", "");
			if(name.length()<4){
				vendorStyleNumber= outfitStyleId+ "" + strAppendOutfitStyle;
			}else{
				vendorStyleNumber= outfitStyleId + strAppendOutfitStyle.substring(0, 4);
			}
			//create new vendor style for outfit
			VendorStyle vs = new VendorStyle();
			vs.setVendorStyleName(name);
			vs.setDescr(desc);
			vs.setVendorStyleNumber(vendorStyleNumber.toUpperCase().replace(" ", ""));
			vs.setVendor(vendor);
			vs.setProductType(productType);
			vs.setVendorNumber(vendor.getVendorNumber());
			vs.setStatusCd(Status.ACTIVE);
			vs.setAuditInfo(currentUser);
			vs.setVendorStyleType(vendorStyleTypeProduct);
			vs.setVendor(vendor);
			vs.setClassification(classification);
			vs.setProductType(productType);
			vs = carManager.createVendorStyle(vs); //Saving the vendor style
			if(log.isDebugEnabled()){
				log.debug(" Vednor style created for outfit  Vendor Style Id: "+ vs.getVendorStyleId());
			}
		
			//Set OUTFIT car details
			car.setVendorStyle(vs);
			car.setDepartment(dept);
			car.setSourceType(sourceOutfitCar);
			car.setWorkFlow(defaultWorkflow);
			car.setSourceId(currentUser.getEmailAddress());
			car.setCarUserByLoggedByUserId(currentUser);
			
			//ship date of outfit car is largest ship date from child cars
			Date shipDate = getMinShipdate(childCars);
			car.setExpectedShipDate(shipDate);
			
			//for outfit car due date is same as ship date
			car.setDueDate(shipDate);
			car.setEscalationDate(shipDate);
			car.setArchive(Constants.FLAG_NO);
			car.setCurrentWorkFlowStatus(initiated);
			
			//all ouftit cars are assigned to buyer by default
			car.setAssignedToUserType(buyer);
			car.setStatusCd(Status.ACTIVE);
			car.setIsProductTypeRequired(Constants.FLAG_NO);
			car.setIsUrgent(Constants.FLAG_NO);
			car.setContentStatus(contentInProgress);
			car.setLastWorkflowStatusUpdateDate(new Date());
			car.setTurninDate(null);
			car.setArchive(Constants.FLAG_NO);
			car.setLock(Constants.FLAG_NO);
			car.setAuditInfo(currentUser);
			
			//add child cars to outfit cars
			addChildCars(car, childCars, orderCars, skus, currentUser );
			
			//add outfit attributes to outfit cars 
			addOutfitAttributes(car, childCars, orderCars, skus, currentUser);
			
			//save outfit car
			outfitDao.save(car);
			
			if(log.isInfoEnabled()){
				log.info(" Outfit car cretaed car ID: "+ car.getCarId());
			}	
			
		}catch(Exception e){
			log.error("Error occured while creating the outfit car", e);
			e.printStackTrace();
		}
	return car;
	}
	
	/**
	 * This method creates the outfit_child_cars table entries for all child cars of outfit.
	 */
	public void addChildCars(Car car, List<Car> childCars, List<String> orderCars, List<String> skus, User user){
		Set<CarOutfitChild> carOutfitChilds = new HashSet<CarOutfitChild> ();
		for(int i=0; i<childCars.size(); i++){
			//create entry in Car_Outfit_Child table for each child car
			Car childCar = childCars.get(i);
			VendorSku defaultColorSku = outfitDao.getVendorSku(childCar.getCarId(), skus.get(i));
			CarOutfitChild outfitChild = new CarOutfitChild();
			outfitChild.setChildCar(childCar);
			outfitChild.setPriority(Integer.valueOf(orderCars.get(i)));
			outfitChild.setDefaultColorSku(defaultColorSku);
			outfitChild.setStatusCd(Status.ACTIVE);
			outfitChild.setAuditInfo(user);
			outfitChild.setOutfitCar(car);
			carOutfitChilds.add(outfitChild);
		}
		car.setCarOutfitChild(carOutfitChilds);
		if(log.isDebugEnabled()){
			log.debug(" Child cars added to outfit ");
		}
	}
	
	/**
	 * This method return the nearest ship date out of child cars 
	 */
	public Date getMinShipdate(List<Car> childCars){
		//returns the min ship date of child cars
		Date shipDate = null;
		for(Car c : childCars){
			if(shipDate == null || shipDate.after(c.getExpectedShipDate())){
				shipDate = c.getExpectedShipDate();
			}
		}
		return shipDate;
	}
	
	/**
	 * returns the current logged in user 
	 */
	public User getLoggedInUser() {
		User user = null;
		Authentication auth = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (auth.getPrincipal() instanceof UserDetails) {
			user = (User) auth.getPrincipal();
		}
	 return user;
	}	
	
	/**
	 * This method add's the appropriate attributes to outfit and child cars
	 * attribute on outfit cars are depends on child cars attributes
	 * Outfit car will have all the attribute of child cars which are marked as 'IS_OUTFIT : Y'
	 * two child cars can have attribute with same blue martini name (eg: Brand), to maintain this relationship we are using table Outfit_Car_Attribute
	 * for regular car we have only one attribute with one car_attribute however for outfit car we can have multiple attribute with one car_attribute  
	 */
	public void addOutfitAttributes(Car car, List<Car> childCars, List<String> orderCars, List<String> skus, User user){
		
		List<CarAttribute> childAttrs = new ArrayList<CarAttribute>();
		Map<String, Set<Attribute>> outfitAttrMap = new HashMap<String, Set<Attribute>>();
		AttributeValueProcessStatus checkRequired = lookupManager.getAttributeValueProcessStatus(AttributeValueProcessStatus.CHECK_REQUIRED);
		AttributeValueProcessStatus noCheckRequired = lookupManager.getAttributeValueProcessStatus(AttributeValueProcessStatus.NO_CHECK_REQUIRED);
		//create the map of is_outfit attributes by attribute_bluemartini_name (eg: Brand)
		for(Car c: childCars){
			childAttrs = outfitDao.getAllOutfitCarAttributes(c);
			for(CarAttribute ca: childAttrs){
					Set<Attribute> attrs = null; 
					String attrBMName = ca.getAttribute().getBlueMartiniAttribute();
					if((attrs=outfitAttrMap.get(attrBMName)) == null){
						attrs = new HashSet<Attribute>();
					}
					attrs.add(ca.getAttribute());
					outfitAttrMap.put(attrBMName, attrs);
			}
			
		}
		if(log.isDebugEnabled()){
			log.debug(" found "+ outfitAttrMap.size() + " attributes for outfit car ");
		}
		//Create the car_attribute for outfit car
		for(String attrBMName: outfitAttrMap.keySet()){
			Set<Attribute> attrs = outfitAttrMap.get(attrBMName);
			List<Attribute> attrList = new ArrayList<Attribute>(attrs);
			CarAttribute carAttribute = new CarAttribute();
			//Get the highest priority attribute from attrList, Priority is based on attribute type [eg: checkbox has highest priority]
			List<Attribute> attrPriorityList= this.prioritiseAttributes(attrList);
			Attribute attribute = attrPriorityList.get(0);
			carAttribute.setAttribute(attribute);
			carAttribute.setCar(car);

			if (attribute.getAttributeConfig().getHtmlDisplayType().isAutocomplete()) {
				carAttribute.setAttributeValueProcessStatus(checkRequired);
			} else {
				carAttribute.setAttributeValueProcessStatus(noCheckRequired);
			}
			// Setting to blank for now
			carAttribute.setAttrValue("");
			carAttribute.setDisplaySeq((short) 0);
			carAttribute.setHasChanged(Constants.FLAG_NO);
			carAttribute.setIsChangeRequired(Constants.FLAG_YES);
			carAttribute.setStatusCd(Status.ACTIVE);
			carAttribute.setAuditInfo(user);
			if(log.isDebugEnabled()){
				log.debug("outfit car attribute : "+ carAttribute.getCarAttrId());
			}
			
			//Each CarAttribute for outfit can have multiple attributes associated to it
			//create entry in Outfit_Car_Attribute table for each car-attribute 
			for(Attribute attr : attrList){
				OutfitCarAttribute ofCarAttr = new OutfitCarAttribute();
				ofCarAttr.setAttribute(attr);
				ofCarAttr.setCarAttribute(carAttribute);
				carAttribute.getOutfitCarAttributes().add(ofCarAttr);
			}
			car.addCarAttribute(carAttribute);
		}
		if(log.isDebugEnabled()){
			log.debug(" Created  OutfitCarAttribute entries ");
		}
		
	    /*
		* set OUTFIT_PARENT_PRODUCTS and OUTFIT_CHILD_PRODUCTS attribute for outfit car
		* OUTFIT_PARENT_PRODUCTS : this attribute is added to all child products of outfit car and its value will be outfit products code delimited by comma
		* OUTFIT_CHILD_PRODUCTS: this attribute is added to all outfit products and its values is child products default color skus delimited by comma
		* Both above attribute need to send to CMP along with outfit and child cars to indicate parent-child relation on belk.com 
		*/
		
		 {
			if(log.isDebugEnabled()){
				log.debug(" Adding  OUTFIT_PARENT_PRODUCTS and OUTFIT_CHILD_PRODUCTS attributes to outfit car");
			}
			Attribute attrParentProducts = outfitDao.getAttributeByName(Constants.OUTFIT_PARENT_PRODUCTS);
			Attribute attrChildProducts = outfitDao.getAttributeByName(Constants.OUTFIT_CHILD_PRODUCTS);
			String strOutfitProductName = car.getVendorStyle().getVendorNumber() + car.getVendorStyle().getVendorStyleNumber();
			String strOutfitChildProductsSku = null;
			
			for(Car c :childCars){
				CarAttribute parentProdCarAttr = outfitDao.findAttributeInCar(c, attrParentProducts);
				if(log.isDebugEnabled()){
					log.debug(" existing OUTFIT_PARENT_PRODUCTS attribute value: " +parentProdCarAttr);
				}
				if(parentProdCarAttr == null){
					parentProdCarAttr = new CarAttribute();
					parentProdCarAttr.setDisplaySeq((short) 0);
					parentProdCarAttr.setHasChanged(Constants.FLAG_NO);
					parentProdCarAttr.setIsChangeRequired(Constants.FLAG_YES);
					parentProdCarAttr.setStatusCd(Status.ACTIVE);
					parentProdCarAttr.setAttributeValueProcessStatus(noCheckRequired);
					parentProdCarAttr.setAuditInfo(user);
					parentProdCarAttr.setAttribute(attrParentProducts);
					parentProdCarAttr.setAttrValue(strOutfitProductName);
					parentProdCarAttr.setCar(c);
				}else{
					String strParentProducts="";
					if(parentProdCarAttr.getAttrValue()!=null){
						strParentProducts = parentProdCarAttr.getAttrValue().concat(",").concat(strOutfitProductName);
					} else {
						strParentProducts = strOutfitProductName;
					}
					parentProdCarAttr.setAttrValue(strParentProducts);
					parentProdCarAttr.setUpdatedBy(user.getEmailAddress());
					parentProdCarAttr.setUpdatedDate(new Date());
				}
				outfitDao.save(parentProdCarAttr);
			}
			
			strOutfitChildProductsSku = sortSkusAccordingToPriority(orderCars, skus);
			CarAttribute ChildProductsCarAttr = new CarAttribute();
			ChildProductsCarAttr.setAttribute(attrChildProducts);
			ChildProductsCarAttr.setAttrValue(strOutfitChildProductsSku);
			ChildProductsCarAttr.setDisplaySeq((short) 0);
			ChildProductsCarAttr.setHasChanged(Constants.FLAG_NO);
			ChildProductsCarAttr.setAttributeValueProcessStatus(noCheckRequired);
			ChildProductsCarAttr.setIsChangeRequired(Constants.FLAG_YES);
			ChildProductsCarAttr.setStatusCd(Status.ACTIVE);
			ChildProductsCarAttr.setAuditInfo(user);
			ChildProductsCarAttr.setCar(car);
			car.addCarAttribute(ChildProductsCarAttr);
		}
	}
	
	/**
	 * This method performing following things:
	 * 1. Get skuAttribute(if attrParentProductsParm parameter has null value) else  
	 * get ProductAttributes(if attrParentProductsParm parameter has some value) from attribute 
	 * table by passing attribute name.
	 * 2. Get the list of attribute of child cars from attribute table
	 * based on attribute name. 
	 * 3.  Add the attribute list to the CarAttribute table. 
	 * 
	 * @param skuCarid      It contain car id. 
	 * @param attrValueSkus It contain attribute value.
	 * @param attrParentProductsParm  It contain attribute name.
	 * 
	 */	
	
	public void addCollectionAttributes(Long skuCarid, String attrValueSkus ,String attrParentProductsParm){
        AttributeValueProcessStatus noCheckRequired = lookupManager.getAttributeValueProcessStatus(AttributeValueProcessStatus.NO_CHECK_REQUIRED);
        Attribute attrParentProducts;
        if(log.isDebugEnabled()){
			log.debug("Inside addCollectionAttributes method ");
		}
        attrParentProducts = outfitDao.getAttributeByName(attrParentProductsParm);
		/*create the map of is_outfit attributes by attribute_bluemartini_name (eg: Brand)*/
		User currentUser = getLoggedInUser();
		Car car = new Car();
			car.setCarId(skuCarid);
			CarAttribute parentProdCarAttr; 		
			parentProdCarAttr = new CarAttribute();
			parentProdCarAttr.setCar(car);
			parentProdCarAttr.setAttribute(attrParentProducts);
			parentProdCarAttr.setAttrValue(attrValueSkus);
			parentProdCarAttr.setHasChanged(Constants.FLAG_NO);
			parentProdCarAttr.setIsChangeRequired(Constants.FLAG_YES);
			parentProdCarAttr.setStatusCd(Status.ACTIVE);
			parentProdCarAttr.setDisplaySeq((short) 0);
			parentProdCarAttr.setAuditInfo(currentUser);
			parentProdCarAttr.setAttributeValueProcessStatus(noCheckRequired);
			outfitDao.save(parentProdCarAttr);
		
	}
	
	/**
	 * This method performing following things:
	 * 1. Set PROD_CODE and SKU_CODE in CollectionSkus Object
	 * 2. Save the Product Code and Sku Code in COLLECTION_SKUS table
	 * 
	 * @param prodCode      It contains product code. 
	 * @param attrValueSkus It contains sku code.
	 * 
	 */	
	public void addCollectionSkus(String prodCode, String skuCode){

		CollectionSkus collectionSkus;
		collectionSkus = new CollectionSkus();
		collectionSkus.setSkuCode(skuCode);
		collectionSkus.setProdCode(prodCode);
		
		/*Creating combined unique id*/
		CollectionSkusId id = new CollectionSkusId();
		id.setSkuCode(skuCode);
		id.setProdCode(prodCode);
		collectionSkus.setId(id);
		
		outfitDao.save(collectionSkus);
		
	}
	
	/**
	 * This method performing following task:
	 * 1. Remove the existing Product code and sku code from COLLECTION_SKUS table
	 * 
	 * @param productCode      It contains product code. 
	 * 
	 */	
	public void removeCollectionSkus(String productCode){
		outfitDao.removeCollectionSkus(productCode);
	}

	
	
	/**
	 * This method performing following things:
	 * 1. Get attribute list from attribute table by passing attribute name and store in
	 * attribute object.  
	 * 2. Get the attribute id of attribute from attribute object.
	 * 3. call updateCarAttributeValues method to update the attribute value based on
	 * attribute id and car id.
	 * 
	 * @param skuCarid          It contains car id. 
	 * @param attrName          It contains attribute name.
	 * @param valueToBeUpdated  It contains attribute value.
	 */
	public void updateCollectionSkuAttributes(Long skuCarid, String attrName, String valueToBeUpdated){
			Attribute attrObjName = outfitDao.getAttributeByName(attrName);
			Long attrObjId = attrObjName.getAttributeId();		
			updateCarAttributeValues(skuCarid, attrObjId, valueToBeUpdated);		
	}	
	
	
	/**
     * This method returns the highest priority attributes, priority is based on attribute display type
     * eg: Checkbox has highest priority
     * @param  attrList: List of attributes
     */
	public List<Attribute> prioritiseAttributes(List<Attribute> attrList){
    	if(attrList.size() <= 1){
    		return attrList;
    	}
    	Map <String, List<Attribute>>attributeTypeMap = new HashMap<String, List<Attribute>>();
    	List<Attribute> sameTypeAttrList =null;
    	
    	for(Attribute attr: attrList){
    		String attrType = attr.getAttributeConfig().getHtmlDisplayType().getHtmlDisplayTypeCd();
    		if((sameTypeAttrList= attributeTypeMap.get(attrType)) == null){
    			sameTypeAttrList = new ArrayList<Attribute>();
    		}
    		sameTypeAttrList.add(attr);
    		attributeTypeMap.put(attrType, sameTypeAttrList);
    	}
    	if((sameTypeAttrList=attributeTypeMap.get(HtmlDisplayType.CHECKBOX))!=null){
    		return sameTypeAttrList;
    	}else if((sameTypeAttrList=attributeTypeMap.get(HtmlDisplayType.TEXT_WITH_AUTOCOMPLETE)) != null){
    		return sameTypeAttrList;
    	}else if((sameTypeAttrList=attributeTypeMap.get(HtmlDisplayType.DROP_DOWN)) != null){
    		return sameTypeAttrList;
    	}else if((sameTypeAttrList=attributeTypeMap.get(HtmlDisplayType.RADIO_BUTTONS)) != null){
    		return sameTypeAttrList;
    	}else if((sameTypeAttrList=attributeTypeMap.get(HtmlDisplayType.HTML_EDITOR)) != null){
    		return sameTypeAttrList;
    	}else{
    		return attrList;
    	}
    }
	
	/**
	 * 
	 * @param skuPriority
	 * @param skus
	 * @return returns the String of Skus sorted according to priority
	 */
	public String sortSkusAccordingToPriority(List<String> skuPriority, List<String> skus){
		StringBuffer strSortedSkus = new StringBuffer("");
		Map<Integer, String> orderedSkus = new HashMap<Integer, String>();
		int maxPriority =0;
		for(int i=0; i<skuPriority.size(); i++){
			
			String x = skuPriority.get(i);
			int currentPriority = Integer.valueOf(x);
			orderedSkus.put(currentPriority, skus.get(i));
			if(maxPriority < currentPriority){
				maxPriority = currentPriority;
			}
		}
		
		for(int i=1; i<=maxPriority; i++){
			String sku = orderedSkus.get(i);
			if(sku != null){
				strSortedSkus.append(sku +",");
			}
		}
		strSortedSkus.deleteCharAt(strSortedSkus.length()-1);
		return strSortedSkus.toString();
	}
	
	/**
	 * This method update attribute values of car.
	 * It is performing following things:
	 * 1. Get attribute list from CarAttribute table by passing car object and based on Active 
	 * status.  
	 * 2. Add new attribute value to CarSkuAttribute object  and update it by calling 
	 * updateCarAttributeValue method.
	 * 
	 * @param outCarId         			 It contains car id. 
	 * @param attrIdToBeUpdated          It contains attribute Id.
	 * @param valueToBeUpdated           It contains attribute value.
	 */
	private void updateCarAttributeValues (Long outCarId, Long attrIdToBeUpdated, String valueToBeUpdated){
		Car outfitCar = carManager.getCar(outCarId);	
		List<CarAttribute> carAtrs = (List<CarAttribute>) carManager.getActiveAttributesForCar(outfitCar);
            if(carAtrs!=null && carAtrs.size() > 0){
			for(CarAttribute carAtr:carAtrs){
				long attrId=carAtr.getAttribute().getAttributeId();
				if (attrIdToBeUpdated == attrId)
				{
					carAtr.setAttrValue(valueToBeUpdated);
				}
				carManager.updateCarAttributeValue(carAtr);
				if(log.isDebugEnabled()){
					log.debug("Copy Deleted Sku attributes completed");
				}
			}}else {
			if(log.isDebugEnabled()){
				log.debug("No DeletedSkuAttributes found to copy...");
			}
		}
	}
}
