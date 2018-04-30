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
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;

import com.belk.car.app.Constants;
import com.belk.car.app.dao.DBPromotionDao;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeValueProcessStatus;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarDBPromotionChild;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.ContentStatus;
import com.belk.car.app.model.DBPromotionCarAttribute;
import com.belk.car.app.model.DBPromotionCollectionSkus;
import com.belk.car.app.model.DBPromotionCollectionSkusId;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.HtmlDisplayType;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.model.PromoType;
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

import com.belk.car.app.webapp.forms.ChildCar;
import com.belk.car.util.DateUtils;


public class DBPromotionUtil {
	
	private static transient final Log log = LogFactory.getLog(DBPromotionUtil.class);
	private CarManager carManager;
	private CarLookupManager lookupManager;
	private WorkflowManager workflowManager;
	private ProductManager productManager;
	private DBPromotionDao dbPromotionDao;
	//private Attribute attrParentProducts = null;
	private Attribute attrPromoDefaultSkus=null;
	
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

	public void setDBPromotionDao(DBPromotionDao dbPromotionDao) {
		this.dbPromotionDao = dbPromotionDao;
	}
	
	/**
	 * This method is to create the DB Promotion car
	 * @param name, desc, chilCars, isbmProductName
	 * 
	 */
	public Car createDBPromotion(String name, String desc, List<Car> childCars, List<String> orderCars, List<String> skus,String templTypVal){
		
		if (log.isInfoEnabled()){
			log.info("Creating the DB Promotion car");
		}
		
		//need at least 2 child cars for outift
		if(desc == null || childCars.size()< 1 ){
		  log.error("Can not create DB Promotion car : Insufficient data \t Desc:"+ desc + "\t childcars size: "+ childCars.size());	
		  return null;
		}
		if(childCars.size()!= orderCars.size() && orderCars.size() != skus.size()){
			log.error("Can not create DBPromotion car : Data mistmatch ");	
			return null;
		}
		name = name==null? "" : name.trim();	
		Car car = new Car();
		try{
			Attribute attrIsGWP = dbPromotionDao.getAttributeByName(Constants.ATTR_IS_GWP);
			ProductType productType =null;
			WorkflowStatus initiated = (WorkflowStatus) this.lookupManager.getById(WorkflowStatus.class, WorkflowStatus.INITIATED);
			UserType buyer = (UserType) this.lookupManager.getById(UserType.class, UserType.BUYER);
			SourceType sourceDBPromotionCar = carManager.getSourceTypeForCode(getSourceTypeByTemplate(templTypVal));
			WorkFlow defaultWorkflow = workflowManager.getWorkFlow(workflowManager.getWorkflowType(WorkflowType.CAR));
			ContentStatus contentInProgress = (ContentStatus) lookupManager.getById(ContentStatus.class, ContentStatus.IN_PROGRESS);
			VendorStyleType vendorStyleTypeProduct = (VendorStyleType) lookupManager.getById(VendorStyleType.class, getVendorStyleTypeByTemplate(templTypVal));
			User currentUser = getLoggedInUser();
			Department dept = carManager.getDepartment(Constants.DBPROMOTION_DEPT_NUMBER);
			Classification classification = carManager.getClassification(Short.parseShort(Constants.DBPROMOTION_CLASS_NUMBER));
			List<ProductType> productTypes =  productManager.getProductTypes(classification.getClassId());
			if(productTypes!= null){
				productType = productTypes.get(0);
			}
			//get the vendor number for DBPromotion car #8888888
			Vendor vendor = carManager.getVendor(Constants.DBPROMOTION_VENDOR_NUMBER);
			String vendorStyleNumber =null;
			
			//need unique number to generate the vendor style number, so using sequence
			//vendor style number is combination of seq number + first 4 digit of DBPromotion car description
			long dbPromotionStyleId = dbPromotionDao.getNextCarSequence();
			String strAppendDBPromotionStyle = (name == null) ? "" : name.replaceAll("\\W+", "");
			if(name.length()<4){
				vendorStyleNumber= dbPromotionStyleId+ "" + strAppendDBPromotionStyle;
			}else{
				vendorStyleNumber= dbPromotionStyleId + strAppendDBPromotionStyle.substring(0, 4);
			}
			//create new vendor style for DBPromotion
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
				log.debug(" Vednor style created for DBPromotion  Vendor Style Id: "+ vs.getVendorStyleId());
			}
			//Set DBPromotion car details
			car.setVendorStyle(vs);
			car.setDepartment(dept);
			car.setSourceType(sourceDBPromotionCar);
			car.setWorkFlow(defaultWorkflow);
			car.setSourceId(currentUser.getEmailAddress());
			car.setCarUserByLoggedByUserId(currentUser);
			
			//ship date of DBPromotion car is largest ship date from child cars
			Date shipDate = getMinShipdate(childCars);
			car.setExpectedShipDate(shipDate);
			
			//for DBPromotion car due date is same as ship date
			car.setDueDate(shipDate);
			car.setEscalationDate(shipDate);
			car.setArchive(Constants.FLAG_NO);
			car.setCurrentWorkFlowStatus(initiated);
			
			//all promo cars are assigned to buyer by default
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
			
			//update promo entries for child
			for(Car child : childCars){
				
				CarAttribute caIsGwp = dbPromotionDao.findAttributeInCar(child, attrIsGWP);
				String isGWPVal = caIsGwp!=null?caIsGwp.getAttrValue():"No";
				PromoType promoRec = dbPromotionDao.findPromoType(child.getCarId());
				dbPromotionDao.save(updateOnPromoVal(promoRec,PromoType.YES,isGWPVal,child.getCarId()));
			}
			//add child cars to DBPromotion cars
			addChildCars(car, childCars, orderCars, skus, currentUser );
			//add DBPromotion attributes to DBPromotion cars 
			addDBPromotionAttributes(car, childCars, orderCars, skus, currentUser);
			//save DBPromotion car
			dbPromotionDao.save(car);
			if(log.isInfoEnabled()){
				log.info(" DBPromotion car cretaed car ID: "+ car.getCarId());
			}	
			
		}catch(Exception e){
			log.error("Error occured while creating the DBPromotion car", e);
			e.printStackTrace();
		}
	return car;
	}
	
	
	//new withut ono to one 
	public static PromoType updateOnPromoVal(PromoType promTyp,String isPygVal,String isGWPVal,long carId){
		String gwpVal = StringUtils.isBlank(isGWPVal)?PromoType.NO:isGWPVal;
		String pygVal = StringUtils.isBlank(isPygVal)?PromoType.NO:isPygVal;
		
		try{
			if(promTyp==null){
				promTyp = new PromoType();
				promTyp.setIsGWP(gwpVal);
				promTyp.setIsPYG(pygVal);
				promTyp.setCarId(carId);
				
			}else{
					promTyp.setIsGWP(gwpVal);
					promTyp.setIsPYG(pygVal);
			}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		return promTyp;
	}
	
	public static PromoType createPromoType(Car car){
		PromoType promTyp = new PromoType();
		promTyp.setIsPYG("Yes");
		promTyp.setIsGWP("No");
		return promTyp;
	}
	
	/**
	 * This method creates the DBPromotion_child_cars table entries for all child cars of DBPromotion.
	 */
	public void addChildCars(Car car, List<Car> childCars, List<String> orderCars, List<String> skus, User user){
		Set<CarDBPromotionChild> carDBPromotionChilds = new HashSet<CarDBPromotionChild> ();
		for(int i=0; i<childCars.size(); i++){
			//create entry in Car_DBPromotion_Child table for each child car
			Car childCar = childCars.get(i);
			VendorSku defaultColorSku = dbPromotionDao.getVendorSku(childCar.getCarId(), skus.get(i));
			CarDBPromotionChild dbPromotionChild = new CarDBPromotionChild();
			dbPromotionChild.setChildCar(childCar);
			dbPromotionChild.setPriority(Integer.valueOf(orderCars.get(i)));
			dbPromotionChild.setDefaultColorSku(defaultColorSku);
			dbPromotionChild.setStatusCd(Status.ACTIVE);
			dbPromotionChild.setAuditInfo(user);
			dbPromotionChild.setDbPromotionCar(car);
			carDBPromotionChilds.add(dbPromotionChild);
			
		}
		car.setCarDBPromotionChild(carDBPromotionChilds);
		if(log.isDebugEnabled()){
			log.debug(" Child cars added to DBPromotion ");
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
	 * This method add's the appropriate attributes to DBPromotion and child cars
	 * attribute on DBPromotion cars are depends on child cars attributes
	 * DBPromotion car will have all the attribute of child cars which are marked as 'IS_PYG: Y'
	 * two child cars can have attribute with same blue martini name (eg: Brand), to maintain this relationship we are using table DBPromotion_Car_Attribute
	 * for regular car we have only one attribute with one car_attribute however for DBPromotion car we can have multiple attribute with one car_attribute  
	 */
	public void addDBPromotionAttributes(Car car, List<Car> childCars, List<String> orderCars, List<String> skus, User user){
		
		List<CarAttribute> childAttrs = new ArrayList<CarAttribute>();
		Map<String, Set<Attribute>> dbPromotiontAttrMap = new HashMap<String, Set<Attribute>>();
		AttributeValueProcessStatus checkRequired = lookupManager.getAttributeValueProcessStatus(AttributeValueProcessStatus.CHECK_REQUIRED);
		AttributeValueProcessStatus noCheckRequired = lookupManager.getAttributeValueProcessStatus(AttributeValueProcessStatus.NO_CHECK_REQUIRED);
		//create the map of is_DBPromotion attributes by attribute_bluemartini_name (eg: Brand)
		for(Car c: childCars){
			//below code to be uncommented once IS_PYG is added in Attribute Table. Currently empty List object is assigned to sustain the execution ahead
			childAttrs =   dbPromotionDao.getAllDBPromotionCarAttributes(c); // new ArrayList<CarAttribute>();  //
			for(CarAttribute ca: childAttrs){
					Set<Attribute> attrs = null; 
					String attrBMName = ca.getAttribute().getBlueMartiniAttribute();
					if((attrs=dbPromotiontAttrMap.get(attrBMName)) == null){
						attrs = new HashSet<Attribute>();
					}
					attrs.add(ca.getAttribute());//adding in same set of earlier 
					dbPromotiontAttrMap.put(attrBMName, attrs);
			}
			
		}
		if(log.isDebugEnabled()){
			log.debug(" found "+ dbPromotiontAttrMap.size() + " attributes for DBPromotion car ");
		}
		//Create the car_attribute for DBPromotion car
		for(String attrBMName: dbPromotiontAttrMap.keySet()){
			Set<Attribute> attrs = dbPromotiontAttrMap.get(attrBMName);
			List<Attribute> attrList = new ArrayList<Attribute>(attrs);
			CarAttribute carAttribute = new CarAttribute();
			//Get the highest priority attribute from attrList, Priority is based on attribute type [eg: checkbox has highest priority]
			List<Attribute> attrPriorityList= this.prioritiseAttributes(attrList);
			Attribute attribute = attrPriorityList.get(0); //0 is being taken as anyhow der is one element of type 'ATTRIBUTE'
			carAttribute.setAttribute(attribute);
			carAttribute.setCar(car);

			if (attribute.getAttributeConfig().getHtmlDisplayType().isAutocomplete()) {
				carAttribute.setAttributeValueProcessStatus(checkRequired);
			} else {
				carAttribute.setAttributeValueProcessStatus(noCheckRequired);
			}
			// Setting to blank for now except for IS_PYG attribute to which it is Yes
			carAttribute.setAttrValue(getDefaultValueForDefaultAttributes(attribute));
			carAttribute.setDisplaySeq((short) 0);
			carAttribute.setHasChanged(Constants.FLAG_NO);
			carAttribute.setIsChangeRequired(Constants.FLAG_YES);
			carAttribute.setStatusCd(Status.ACTIVE);
			carAttribute.setAuditInfo(user);
			if(log.isDebugEnabled()){
				log.debug("DBPromotion car attribute : "+ carAttribute.getCarAttrId());
			}
			
			//Each CarAttribute for DBPromotion can have multiple attributes associated to it
			//create entry in DBPromotion_Car_Attribute table for each car-attribute 
			for(Attribute attr : attrList){
				DBPromotionCarAttribute ofCarAttr = new DBPromotionCarAttribute();
				ofCarAttr.setAttribute(attr);
				ofCarAttr.setCarAttributeForPromo(carAttribute);
				carAttribute.getDbPromotionCarAttributes().add(ofCarAttr);
			}
			car.addCarAttribute(carAttribute);
		}
		if(log.isDebugEnabled()){
			log.debug(" Created  DBPromotionCarAttribute entries ");
		}
		
	    /*
		* set DBPROMOTION_PARENT_PRODUCTS and DBPROMOTION_DEFAULT_SKUS attribute for DBPromotion car
		* DBPromotion_PARENT_PRODUCTS : this attribute is added to all child products of DBPromotion car and its value will be DBPromotion products code delimited by comma
		* DBPromotion_DEFAULT_SKUS: this attribute is added to all DBPromotion parent products and its values is child products default color skus delimited by comma
		* Both above attribute need to send to CMP along with DBPromotion and child cars to indicate parent-child relation on belk.com 
		*/
		
		 {
			if(log.isDebugEnabled()){
				log.debug(" Adding  DBPROMOTION_PARENT_PRODUCTS and DBPROMOTION_DEFAULT_SKUS attributes to PYG parent car");
			}
			Attribute atrParentProdCodes = dbPromotionDao.getAttributeByName(Constants.DBPROMOTION_PARENT_PRODUCTS);
			Attribute atrDefaultSkus = dbPromotionDao.getAttributeByName(Constants.DBPROMOTION_DEFAULT_SKUS);
			Attribute atrIsPyg = dbPromotionDao.getAttributeByName(Constants.DBPROMOTION_ATTR_IS_PYG);
			
			Attribute atrChildProdCodes = dbPromotionDao.getAttributeByName(Constants.DBPROMOTION_CHILD_PROD_CODES);
			String strDBPromotionProductName = car.getVendorStyle().getVendorNumber() + car.getVendorStyle().getVendorStyleNumber();
			String strDBPromotionChildProductsSku = null;
			for(Car c :childCars){
				CarAttribute childCarAttrParentProdCodes = dbPromotionDao.findAttributeInCar(c, atrParentProdCodes);
				if(log.isDebugEnabled()){
					log.debug(" existing child's DBPROMOTION_PARENT_PRODUCTS attribute value: " +childCarAttrParentProdCodes);
				}

				if(childCarAttrParentProdCodes == null){
					//childCarAttrParentProdCodes = prepareCarAttribute(atrParentProdCodes, strDBPromotionProductName, noCheckRequired, car, user);
					childCarAttrParentProdCodes = new CarAttribute();
					childCarAttrParentProdCodes.setAttribute(atrParentProdCodes);
					childCarAttrParentProdCodes.setAttrValue(strDBPromotionProductName);
					childCarAttrParentProdCodes.setDisplaySeq((short) 0);
					childCarAttrParentProdCodes.setHasChanged(Constants.FLAG_NO);
					childCarAttrParentProdCodes.setAttributeValueProcessStatus(checkRequired);
					childCarAttrParentProdCodes.setIsChangeRequired(Constants.FLAG_YES);
					childCarAttrParentProdCodes.setStatusCd(Status.ACTIVE);
					childCarAttrParentProdCodes.setAuditInfo(user);
					childCarAttrParentProdCodes.setCar(c);	
				}else{
				childCarAttrParentProdCodes.setAttrValue(appendValue(childCarAttrParentProdCodes, strDBPromotionProductName));
				childCarAttrParentProdCodes.setUpdatedBy(user.getEmailAddress());
				childCarAttrParentProdCodes.setUpdatedDate(new Date());
				}
				dbPromotionDao.save(childCarAttrParentProdCodes);
				removePromoAttributesForAssociatedCars(c, user);	
				}
			
			strDBPromotionChildProductsSku = sortSkusAccordingToPriority(orderCars, skus);
			car.addCarAttribute(prepareCarAttribute(atrDefaultSkus,strDBPromotionChildProductsSku,noCheckRequired,car,user));//adding PROMO_DEFAULT_SKUS on parent
			String sortedChildProdCodes = sortProdsAccordingToPriority(orderCars, getProdCodeList(childCars));
			car.addCarAttribute(prepareCarAttribute(atrChildProdCodes,sortedChildProdCodes,noCheckRequired,car,user));//adding PROMO_CHILD_PROD_CODES on parent
			
			
		}
	}
	
	public static String getDefaultValueForDefaultAttributes(Attribute attr){
		String attrVal = "";
		if(attr.getName().equals(Constants.DBPROMOTION_ATTR_IS_PYG)){
			attrVal = Constants.DBPROMOTION_ATTR_IS_PYG_YES;
		}
		return attrVal;
	} 
	
	
	public static CarAttribute prepareCarAttribute(Attribute attr,String attrValue,AttributeValueProcessStatus isCheckRequired,Car car,User user){
		CarAttribute ChildProductsCarAttr = new CarAttribute();
		ChildProductsCarAttr.setAttribute(attr);
		ChildProductsCarAttr.setAttrValue(attrValue);
		ChildProductsCarAttr.setDisplaySeq((short) 0);
		ChildProductsCarAttr.setHasChanged(Constants.FLAG_NO);
		ChildProductsCarAttr.setAttributeValueProcessStatus(isCheckRequired);
		ChildProductsCarAttr.setIsChangeRequired(Constants.FLAG_YES);
		ChildProductsCarAttr.setStatusCd(Status.ACTIVE);
		ChildProductsCarAttr.setAuditInfo(user);
		ChildProductsCarAttr.setCar(car);
		return ChildProductsCarAttr;
	}
	
	public static String appendValue(CarAttribute carAttr,String valueTobeAppeneded){
		String strParentProducts="";
		if(carAttr.getAttrValue()!=null && !"".equals(carAttr.getAttrValue())){
			strParentProducts = carAttr.getAttrValue().concat(",").concat(valueTobeAppeneded);
		} else {
			strParentProducts = valueTobeAppeneded;
		}
		return strParentProducts;
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
	
	public void addPromotionalAttributes(Long skuCarid, String attrValueSkus ,String attrParentProductsParm){
        AttributeValueProcessStatus noCheckRequired = lookupManager.getAttributeValueProcessStatus(AttributeValueProcessStatus.NO_CHECK_REQUIRED);
        Attribute attrParentProducts;
        if(log.isDebugEnabled()){
			log.debug("Inside addCollectionAttributes method ");
		}
        attrParentProducts = dbPromotionDao.getAttributeByName(attrParentProductsParm);
		/*create the map of is_DBPromotion attributes by attribute_bluemartini_name (eg: Brand)*/
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
			dbPromotionDao.save(parentProdCarAttr);
		
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
	public void addDBPromotionCollectionSkus(String prodCode, String skuCode){

		DBPromotionCollectionSkus dbPromotionSollectionSkus;
		dbPromotionSollectionSkus = new DBPromotionCollectionSkus();
		dbPromotionSollectionSkus.setSkuCode(skuCode);		
		dbPromotionSollectionSkus.setProdCode(prodCode);
		
		/*Creating combined unique id*/
		DBPromotionCollectionSkusId id = new DBPromotionCollectionSkusId();
		id.setSkuCode(skuCode);
		id.setProdCode(prodCode);
		dbPromotionSollectionSkus.setId(id);
		dbPromotionDao.save(dbPromotionSollectionSkus);
	}
	
	/**
	 * This method performing following task:
	 * 1. Remove the existing Product code and sku code from COLLECTION_SKUS table
	 * 
	 * @param productCode      It contains product code. 
	 * 
	 */	
	public void removeDBPromotionCollectionSkus(String productCode){
		dbPromotionDao.removeDBPromotionCollectionSkus(productCode);
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
	public void updateDBPromotionCollectionSkuAttributes(Long skuCarid, String attrName, String valueToBeUpdated){
			Attribute attrObjName = dbPromotionDao.getAttributeByName(attrName);
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
		Car dbPromotionCar = carManager.getCar(outCarId);	
		List<CarAttribute> carAtrs = (List<CarAttribute>) carManager.getActiveAttributesForCar(dbPromotionCar);
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
	
	//Save Util Class
	
	/**
	 * Saves the DBPromotion CAR after modification
	 * @param dbPromotionCar
	 * @param name
	 * @param desc
	 * @param childCars
	 * @param orderCars
	 * @param skus
	 */
	public void saveDBPromotion(Car dbPromotionCar, String name, String desc, List<Car> childCars, List<String> orderCars	, List<String> skus){

		boolean isVSModified = false;
		List<Car> sbPromotionChildCars = new ArrayList<Car>();
		int index =0;
		
		if(childCars == null || childCars.size() < 1){
			log.error("Cannot save DBPromotion car: insufficicent data " + dbPromotionCar.getCarId());
			return;
		}
		
		try{
			VendorStyle dbPromotionStyle = dbPromotionCar.getVendorStyle();
			if(dbPromotionStyle != null){
				//If Vendor Style Name is null or is not matching to the earlier saved one, then set it in 
				//Vendor Style object.
				if(dbPromotionStyle.getVendorStyleName()== null || !dbPromotionStyle.getVendorStyleName().equals(name)){
					dbPromotionStyle.setVendorStyleName(name);
					isVSModified =true;
					if(log.isInfoEnabled()){
						log.info("Outift name changed: "+ name);
					}
				}
				//If Vendor Style Description is null or is not matching to the earlier saved one, then set it in 
				//Vendor Style object.
				if(dbPromotionStyle.getDescr()== null || !dbPromotionStyle.getDescr().equals(desc)){
					dbPromotionStyle.setDescr(desc);
					isVSModified = true;
					if(log.isInfoEnabled()){
						log.info("DB Promotion Description changed: "+ desc);
					}
				}
				//If Vendor style is modified then save object.
				if(isVSModified){
					dbPromotionDao.save(dbPromotionStyle);
				}
			}
			attrPromoDefaultSkus = dbPromotionDao.getAttributeByName(Constants.DBPROMOTION_DEFAULT_SKUS);
			User user = getLoggedInUser();
			 
			//Logic to check if any car is removed or still there also creating list of DBPromotionChildCars
			for(CarDBPromotionChild dbPromotionChild : dbPromotionCar.getCarDBPromotionChild() ){
				if(dbPromotionChild.getStatusCd().equals(Status.ACTIVE)) {
					Car dbPromotionChildCar = dbPromotionChild.getChildCar();
					if((index=childCars.indexOf(dbPromotionChildCar)) != -1){
						sbPromotionChildCars.add(dbPromotionChildCar);
						VendorSku vendorSkuRetrievedFromForm = dbPromotionDao.getVendorSku(dbPromotionChildCar.getCarId(), skus.get(index));
						//update the DBPromotion Child Car with new default color code.
						dbPromotionChild.setDefaultColorSku(vendorSkuRetrievedFromForm);
						dbPromotionChild.setPriority(Integer.valueOf(orderCars.get(index)));
						dbPromotionDao.save(dbPromotionChild);
						if(log.isInfoEnabled()){
							log.info("Setting the defalt color and priority for existing DBPromotion child car ");
						}
					}else{
						//Removing CAR from dbPromotion
						removeCarFromDBPromotion(dbPromotionCar, dbPromotionChild, user);
					}
				}
			}
			//Check for any newly added child CAR
			for(int i = 0; i < childCars.size(); i++){
				//create entry in Car_DBPromotion_Child table for each child car
				Car childCar = childCars.get(i);
				if(!sbPromotionChildCars.contains(childCar)){
					//Adding the CAR to DBPromotion, Updating priority as well.
					addCarToDBPromotion(dbPromotionCar, childCar, Integer.valueOf(orderCars.get(i)), skus.get(i), user );
				}
			}
			
			//Get the DBPromotion child default color skus delimited by comma according to priority 
			// eg: sku1,sku2,sku3
			String strDBPromotionChildProductsSku = sortSkusAccordingToPriority(orderCars, skus);
			
			//Get the DBPromotion car attribute PROMO_DEFAULT_SKUS
			CarAttribute childProductsCarAttr = dbPromotionDao.getCarAttributeByAttributeName(dbPromotionCar,attrPromoDefaultSkus.getName());
			//Modify the PROMO_DEFAULT_SKUS attribute
			setUpdatesOnCarAttribute(childProductsCarAttr, strDBPromotionChildProductsSku,user);
			//Get the DBPromotion car attribute PROMO_CHILD_PROD_CODES
			CarAttribute childProdCodesCarAttr = dbPromotionDao.getCarAttributeByAttributeName(dbPromotionCar,dbPromotionDao.getAttributeByName(Constants.DBPROMOTION_CHILD_PROD_CODES).getName());
			//Modify the PROMOTION_CHILD_PROD_CODES attribute
			String sortedChildProdCodes = sortProdsAccordingToPriority(orderCars, getProdCodeList(childCars));
			//setUpdatesOnCarAttribute(childProdCodesCarAttr, arrangeProdByLatestChildOrder(childCars,orderCars),user); //working but need priority code inisde
			setUpdatesOnCarAttribute(childProdCodesCarAttr, sortedChildProdCodes,user);
			if(log.isDebugEnabled()){
				log.debug(" Setting child product values to - " +strDBPromotionChildProductsSku);
			}
			dbPromotionDao.save(childProductsCarAttr);//saved PROMO_DEFAULT_SKUS
			dbPromotionDao.save(childProdCodesCarAttr);//saved PROMO_CHILD_PROD_CODES
		}catch(Exception e){
			log.error("error occured while saving DBPromotion car", e);
			e.printStackTrace();
		}
	}
	
	public static List<String> getProdCodeList(List<Car> childCars){
		List<String> prodCodeList = new ArrayList<String>();
		for(Car car: childCars)
		{
			String prodCode = car.getVendorStyle().getVendor().getVendorNumber()+car.getVendorStyle().getVendorStyleNumber();
			prodCodeList.add(prodCode);
		}
		return prodCodeList;
	}
	
	
	
	public static void setUpdatesOnCarAttribute(CarAttribute carAttr,String updatedAttrValue,User user){
		carAttr.setAttrValue(updatedAttrValue);
		carAttr.setUpdatedBy(user.getEmailAddress());
		carAttr.setUpdatedDate(new Date());
		//dbPromotionDao.save(carAttr);   //lets keep this dao variable static if  time permits else leave it as same outfit/collection-mangment's design as suggested 
	}
	
	
	
	/**
	 * @param skuPriority
	 * @param skus
	 * @return skus's delimited by comma  eg: sku1,sku2,sku3
	 */
	public static String sortSkusAccordingToPriority(List<String> skuPriority, List<String> skus){
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
	
	public static String sortProdsAccordingToPriority(List<String> skuPriority, List<String> skus){
		
		return sortSkusAccordingToPriority(skuPriority,skus);
	}
	/**
	 * This method is to remove the dbPromotionChild car from the dbPromotionCar and modify the PROMOTION_PARENT_PRODUCTS attributes 
	 * @param dbPromotionCar
	 * @param dbPromotionChild
	 * @param user
	 */
	public void removeCarFromDBPromotion(Car dbPromotionCar, CarDBPromotionChild dbPromotionChild, User user ){
		//remove car code
		Car childCar = dbPromotionChild.getChildCar();
		
		dbPromotionChild.setStatusCd(Status.DELETED);
		dbPromotionChild.setUpdatedBy(user.getEmailAddress());
		dbPromotionChild.setUpdatedDate(new Date());
		dbPromotionDao.save(dbPromotionChild);
		
		if(log.isInfoEnabled()){
			log.info("Deleted the CAR_PROMOTION_CHILD car id: "+ childCar.getCarId() +"for DBPromotion car Id : "+dbPromotionCar.getCarId() );
		}
		
		String strDBPromotionProductName = dbPromotionCar.getVendorStyle().getVendorNumber()+""+ dbPromotionCar.getVendorStyle().getVendorStyleNumber(); 
		int  intStartIndex = -1;
		int intEndIndex = -1;
		StringBuffer strBuffProds =  new StringBuffer();
		Attribute attrPromoParentProducts = dbPromotionDao.getAttributeByName(Constants.DBPROMOTION_PARENT_PRODUCTS);
		CarAttribute carAttrParentProducts = dbPromotionDao.findAttributeInCar(dbPromotionChild.getChildCar(), attrPromoParentProducts);
		if(carAttrParentProducts != null){
			String strParentProds = carAttrParentProducts.getAttrValue();
			//below logic is to remove the DBPromotion parent product name from child car attribute "PROMOTION_PARENT_PRODUCTS"
			if((intStartIndex = strParentProds.indexOf(strDBPromotionProductName)) >= 0){
				strBuffProds = new StringBuffer(strParentProds);
				intEndIndex = intStartIndex + strDBPromotionProductName.length()+1;
				if(intEndIndex > strBuffProds.length()){
					intEndIndex = strBuffProds.length();
					intStartIndex = (intStartIndex-1 < 0 )? 0 : intStartIndex-1 ;
				}/*else{
					//intStartIndex = (intStartIndex-1 < 0 )? 0 : intStartIndex-1 ;
				}*/
				strBuffProds.delete(intStartIndex, intEndIndex);
				strParentProds =strBuffProds.toString();
			}
			carAttrParentProducts.setAttrValue(strParentProds);
			carAttrParentProducts.setUpdatedBy(user.getEmailAddress());
			carAttrParentProducts.setUpdatedDate(new Date());
			dbPromotionDao.save(carAttrParentProducts);
		}
		if(log.isDebugEnabled()){
			log.debug("removed the parent product name from the PROMOTION_PARENT_PRODUCTS list  - child car Id: " + childCar.getCarId());
		}
		
	}
	
	/**
	 * This method is to add the childCAR to the DBPromotionCar and modify the PROMOTION_CHILD_PRODUCTS/PROMOTION_PARENT_PRODUCTS attributes
	 * @param dbPromotionCar
	 * @param childCar
	 * @param piority
	 * @param  sku
	 * @param user
	 */
	public void addCarToDBPromotion(Car dbPromotionCar, Car childCar, int priority, String sku, User user ){
		//add car code
		CarDBPromotionChild dbPromotionChild = new CarDBPromotionChild();
		dbPromotionChild.setDbPromotionCar(dbPromotionCar);
		dbPromotionChild.setChildCar(childCar);
		VendorSku defaultColorSku = dbPromotionDao.getVendorSku(childCar.getCarId(), sku);
		dbPromotionChild.setPriority(priority);
		dbPromotionChild.setDefaultColorSku(defaultColorSku);
		dbPromotionChild.setStatusCd(Status.ACTIVE);
		dbPromotionChild.setAuditInfo(user);
		dbPromotionDao.save(dbPromotionChild);
		
		//Add Entry in Promo_type for this child as true.
		Attribute attrIsGWP = dbPromotionDao.getAttributeByName(Constants.ATTR_IS_GWP);
		CarAttribute caIsGwp = dbPromotionDao.findAttributeInCar(childCar, attrIsGWP);
		String isGWPVal = caIsGwp!=null?caIsGwp.getAttrValue():"No";
		PromoType promoRec = dbPromotionDao.findPromoType(childCar.getCarId());
		dbPromotionDao.save(updateOnPromoVal(promoRec,PromoType.YES,isGWPVal,childCar.getCarId()));
		if(log.isInfoEnabled()){
			log.info("Created the new CAR_PROMOTION_CHILD - child car id : "+ childCar.getCarId() +" for dbPromotion car ID : "+ dbPromotionCar.getCarId() );
		}
		//Get the DBPromotion Product CodeName 
		String strDBPromotionProductName =getProductCode(dbPromotionCar);
		//add DBPromotion product name to PTOMOTION_PARENT_PRODUCTS attribute value of a child car.
		Attribute attrPromoParentProducts = dbPromotionDao.getAttributeByName(Constants.DBPROMOTION_PARENT_PRODUCTS);
		CarAttribute carAttrParentProducts = dbPromotionDao.findAttributeInCar(childCar, attrPromoParentProducts);
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
			carAttrParentProducts.setAttribute(attrPromoParentProducts);
			carAttrParentProducts.setAttrValue(strDBPromotionProductName);
			carAttrParentProducts.setCar(childCar);
		}else{

			//If Parent was set, then compare Parent Product String.
			String strParentProds = carAttrParentProducts.getAttrValue();
			if(strParentProds == null || !strParentProds.contains(strDBPromotionProductName)){
				if(strParentProds != null && strParentProds.length()>1){
					strParentProds = strParentProds.concat(","+strDBPromotionProductName);
				}else{
					strParentProds = strDBPromotionProductName;
				}
			}
			//set Attribute value back.
			carAttrParentProducts.setAttrValue(strParentProds);
			carAttrParentProducts.setUpdatedBy(user.getEmailAddress());
			carAttrParentProducts.setUpdatedDate(new Date());
		}
		//Save the attribute.
		dbPromotionDao.save(carAttrParentProducts);
		removePromoAttributesForAssociatedCars(childCar, user);
		
		if(log.isDebugEnabled()){
			log.debug("Added the parent product name in PROMOTION_PARENT_PRODUCTS attribute of child car");
		}
	}
	public static List<ChildCar> getChildCarSkuListForDisplay(Set<CarDBPromotionChild> dbPromoChildCarList){
		List <Car> childCarsList= new ArrayList<Car>();
		for(CarDBPromotionChild promoChild : dbPromoChildCarList){
			if(promoChild.getStatusCd().equals(Status.ACTIVE)){
			childCarsList.add(promoChild.getChildCar());
			}
		}
		return getCarSkuListForDisplay(childCarsList);
	}
	
	public static String getProductCode(Car car){
		return car==null?"":car.getVendorStyle().getVendorNumber()+""+car.getVendorStyle().getVendorStyleNumber();
	}
	
	public static String getDBPromoSkusValuesInString(List<DBPromotionCollectionSkus> skus){
		StringBuffer comaSeprtedVals = new StringBuffer();
		for(DBPromotionCollectionSkus sku : skus){
			if("".equals(comaSeprtedVals)){
			comaSeprtedVals.append(sku.getSkuCode());
			}else{
				comaSeprtedVals.append(",").append(sku.getSkuCode());
			}
			
		}
		return comaSeprtedVals.toString();
	}
	
	
	public static  List<ChildCar>getCarSkuListForDisplay(List<Car> childCarsList){
		List<ChildCar> viewChildCarsSkuList = new ArrayList<ChildCar>();
		for(Car childCar:childCarsList){// shift to util method
			if(childCar.getStatusCd().equals(Status.ACTIVE)){
			VendorStyle vendorStyle = childCar.getVendorStyle();
			//skuCar = dbPromotionManager.getChildCarSkus(vendorStyle);
			//for(Car childCarSku:skuCar){
				Set<VendorSku> carVendorSku=childCar.getVendorSkus();
				String styleNumStr = vendorStyle.getVendorStyleNumber();
				for(VendorSku vSku:carVendorSku){
					ChildCar childCarSkuList= new ChildCar();	
					childCarSkuList.setSkuCarid(childCar.getCarId());
					childCarSkuList.setCompDt(DateUtils.formatDate(childCar.getDueDate()));
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
				}
			//} not needed forloop of vendr style
			}
		}		
		return viewChildCarsSkuList;
	}
	
	public static boolean isNotAmongPromotionGroupAttributes(CarAttribute ca){
		boolean isAmongPromoGroupAttrs= false;
		if(ca !=null){
		String carAttrName= ca.getAttribute().getName();
		 isAmongPromoGroupAttrs = !carAttrName.equals(Constants.DBPROMOTION_DEFAULT_SKUS) && !carAttrName.equals(Constants.DBPROMOTION_ATTR_IS_PYG) && !carAttrName.equals(Constants.DBPROMOTION_CHILD_PROD_CODES) && !carAttrName.equals(Constants.DBPROMOTION_TEMP_TYPE);
		}
		return isAmongPromoGroupAttrs;
	}
	
	public static String getSourceTypeByTemplate(String templateType){
		String srcTyp = "";
		if(StringUtils.isNotBlank(templateType)){
			if(templateType.equalsIgnoreCase(Constants.TEMPL_TYPE_VAL_PYG)){
				srcTyp = SourceType.PYG;
				
			}
		}
		return srcTyp;
	}
	
	public static String getVendorStyleTypeByTemplate(String templateType){
		String vsTyp = "";
		if(StringUtils.isNotBlank(templateType)){
			if(templateType.equalsIgnoreCase(Constants.TEMPL_TYPE_VAL_PYG)){
				vsTyp = VendorStyleType.PYG;
			}
		}
		return vsTyp;
	}
	
	public static Set<CarDBPromotionChild> getActiveChildCars(Car car){
		Set<CarDBPromotionChild>  childCars = car.getCarDBPromotionChild();
		Set<CarDBPromotionChild>  activeChildCars = new HashSet<CarDBPromotionChild>();
		for(CarDBPromotionChild childCar:childCars){
			if(childCar.getStatusCd().equals("ACTIVE")){
			activeChildCars.add(childCar);
			}
		}
		return activeChildCars;
	}
	
	public static int getMaxCharLimitForAttributeValue(){
		return Constants.MAX_ATTRVALUE_LIMIT;
	}
	
	public static boolean isLimitExceeded(int charsAllocated,int valueToBeAdded){
		return (charsAllocated+valueToBeAdded) > getMaxCharLimitForAttributeValue(); 
	}
	
	public static int getMaxProductCodeLength(){
		return Constants.PRODCODE_LENGTH; 
	}
	
	/**
	 * Checks if childCar has any other CARs with the same style which are associated with PROMOTION_PARENT_PRODUCTS 
	 * attribute and removes the association of the attribute with such CARs 
	 * @param childCar
	 * @param user
	 */
	
	public void removePromoAttributesForAssociatedCars(Car childCar, User user){
		VendorStyle vendorStyle = childCar.getVendorStyle();
		List<Car> carsForStyle = carManager.getAllCarForStyle(vendorStyle.getVendorStyleId());
		
		if (null != carsForStyle && 1< carsForStyle.size())
		{
			Attribute attrPromoParentProducts = dbPromotionDao.
					getAttributeByName(Constants.DBPROMOTION_PARENT_PRODUCTS);
			for(Car car : carsForStyle) {
				CarAttribute carAttrParentProducts = dbPromotionDao.findAttributeInCar(car, attrPromoParentProducts);
				if(null != carAttrParentProducts && null == carAttrParentProducts.getAttrValue()){
					dbPromotionDao.remove(carAttrParentProducts);
				}
			}
		}
	}

}

