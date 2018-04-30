package com.belk.car.app.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.appfuse.model.User;
import org.appfuse.service.impl.UniversalManagerImpl;

import com.belk.car.app.Constants;
import com.belk.car.app.dao.DBPromotionDao;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeValueProcessStatus;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarDBPromotionChild;
import com.belk.car.app.model.CollectionSkus;
import com.belk.car.app.model.DBPromotionCarAttribute;
import com.belk.car.app.model.DBPromotionCollectionSkus;
import com.belk.car.app.model.HtmlDisplayType;
import com.belk.car.app.model.PromoType;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.DBPromotionManager;
import com.belk.car.app.service.ProductManager;
import com.belk.car.app.service.WorkflowManager;
import com.belk.car.app.util.DBPromotionUtil;


import com.belk.car.app.webapp.forms.ChildCar;
public class DBPromotionManagerImpl extends UniversalManagerImpl implements DBPromotionManager{
	private CarManager carManager;
	private CarLookupManager lookupManager;
	private WorkflowManager workflowManager;
	private ProductManager productManager;
	private DBPromotionDao dbPromotionDao;

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

	
	
	/**
	 * This method is used to create the dbPromotion  car by calling createDBPromotion
	 * method  and passing following details for  dbPromotion car: name , description, 
	 * child cars and skus.
	 * 
	 * 
	 * @param name     	This variable contains DBPromotion name
	 * @param desc	    This variable contains dbPromotion car description.
	 * @param childCars List of cars
	 * @param orderCars This variable contains priority
	 * @param skus      List of skus
	 * 
	 * @return 	return car object	
	 */
	public Car createDBPromotion(String name, String desc, List<Car> childCars,
			List<String> orderCars, List<String> skus,String templTypeVal) {
		Car dbPromotioncar = null;
		try {
			DBPromotionUtil objDBPromotion = new DBPromotionUtil();
			objDBPromotion.setCarManager(carManager);
			objDBPromotion.setLookupManager(lookupManager);
			objDBPromotion.setDBPromotionDao(dbPromotionDao);
			objDBPromotion.setProductManager(productManager);
			objDBPromotion.setWorkflowManager(workflowManager);
			System.out.println("Before In DBPromotionalManagerImpl call method createDBPromotion");
			dbPromotioncar = objDBPromotion.createDBPromotion(name, desc, childCars, orderCars,skus,templTypeVal);
			System.out.println("After In DBPromotionalManagerImpl call method createDBPromotion");
		} catch (Exception e) {
			log.error("Could not create the dbPromotion CAR");
		}
		return dbPromotioncar;
	}
	
	/**
	 * This method is used to store the product code and sku code in collection_skus table 
	 * by calling addDBPromotionCollectionSkus method.
	 * 
	 * @param prodCode 	 This variable contains Product Code
	 * @param skuCode	 This variable contains Sku Code
	 */
	public void createDBPromotionCollectionSkus(String prodCode, String skuCode) {
		try {
			DBPromotionUtil objDBPromotion = new DBPromotionUtil();
			objDBPromotion.setCarManager(carManager);
			objDBPromotion.setLookupManager(lookupManager);
			objDBPromotion.setDBPromotionDao(dbPromotionDao);
			objDBPromotion.setProductManager(productManager);
			objDBPromotion.setWorkflowManager(workflowManager);
			objDBPromotion.addDBPromotionCollectionSkus(prodCode, skuCode);
		} catch (Exception e) {
			log.error("Could not create the dbPromotion CAR");
		}
	}
	
	/**
	 * This method performing following task:
	 * 1. Remove the existing Product code and sku code from COLLECTION_SKUS table
	 * 
	 * @param productCode      It contains product code. 
	 * 
	 */	
	public void removeDBPromotionCollectionSkus(String productCode) {
		try {
			DBPromotionUtil objDBPromotion = new DBPromotionUtil();
			objDBPromotion.setCarManager(carManager);
			objDBPromotion.setLookupManager(lookupManager);
			objDBPromotion.setDBPromotionDao(dbPromotionDao);
			objDBPromotion.setProductManager(productManager);
			objDBPromotion.setWorkflowManager(workflowManager);
			objDBPromotion.removeDBPromotionCollectionSkus(productCode);
		} catch (Exception e) {
			log.error("Could not create the dbPromotion CAR");
		}
	}
	
	/**
	 * This method is used to store the sku attribute in CarAttribute table by calling 
	 * updateDBPromotionCollectionSkuAttributes method and passing carid and attribute value.
	 * 
	 * @param skuCarId 	     This variable contains carid
	 * @param attrValueSkus	 This variable contains attribute vale of SKU
	 */
	public void updateDBPromotionAttributes(Long skuCarId,
			String attrValueSkus, String valueToBeUpdated) {
		try {
			DBPromotionUtil objDBPromotion = new DBPromotionUtil();
			objDBPromotion.setCarManager(carManager);
			objDBPromotion.setLookupManager(lookupManager);
			objDBPromotion.setDBPromotionDao(dbPromotionDao);
			objDBPromotion.setProductManager(productManager);
			objDBPromotion.setWorkflowManager(workflowManager);
			objDBPromotion.updateDBPromotionCollectionSkuAttributes(skuCarId, attrValueSkus,
					valueToBeUpdated);
		} catch (Exception e) {
			log.error("Could not create the dbPromotion CAR");
		}
	}
	
	/**
	 * This method is used to store the attribute value(isSearchable, effective
	 * start date , template type) in car attribute table by calling 
	 * addCollectionAttributes method and passing dbPromotionCarId and attribute value.
	 * 
	 * 
	 * @param dbPromotionCarId 		  This variable contains dbPromotionCarId. 
	 * @param attrValue 		  This variable contains attribute value.
	 */
	public void createProductAttributes(Long dbPromotionCarId, String attrValue,
			String attrParentProducts) {
		
		try {
			DBPromotionUtil objDBPromotion = new DBPromotionUtil();
			objDBPromotion.setCarManager(carManager);
			objDBPromotion.setLookupManager(lookupManager);
			objDBPromotion.setDBPromotionDao(dbPromotionDao);
			objDBPromotion.setProductManager(productManager);
			objDBPromotion.setWorkflowManager(workflowManager);
			objDBPromotion.addPromotionalAttributes(dbPromotionCarId, attrValue,
					attrParentProducts);
		} catch (Exception e) {
			log.error("Could not create the dbPromotion CAR");
		}
	}

	/**
	 * Method to save dbPromotion car
	 */
	public void saveDBPromotion(Car DBPromotionCar, String name, String desc,
			List<Car> childCars, List<String> orderCars, List<String> skus) {
		try {
			DBPromotionUtil saveDBPromotionUtil = new DBPromotionUtil();
			saveDBPromotionUtil.setLookupManager(lookupManager);
			saveDBPromotionUtil.setDBPromotionDao(dbPromotionDao);
			saveDBPromotionUtil.saveDBPromotion(DBPromotionCar, name, desc, childCars,
					orderCars, skus);
		} catch (Exception e) {
			log.error("Could not create the dbPromotion CAR");
		}
	}
	/**
	 * Method to resync attributes on dbPromotion car
	 */
	public void resyncDBPromotionAttributes(Car dbPromotionCar, User user) {
		if (log.isInfoEnabled()) {
			log.info("In Resync attribute for DBPromotion CAR : "
					+ dbPromotionCar.getCarId());
		}
		try {
			List<CarAttribute> childAttrs = new ArrayList<CarAttribute>();
			Map<String, Set<Attribute>> dbPromotionAttrMap = new HashMap<String, Set<Attribute>>();
			Set<CarDBPromotionChild> carDBPromotionChilds = dbPromotionCar.getCarDBPromotionChild();
			AttributeValueProcessStatus checkRequired = lookupManager
					.getAttributeValueProcessStatus(AttributeValueProcessStatus.CHECK_REQUIRED);
			AttributeValueProcessStatus noCheckRequired = lookupManager
					.getAttributeValueProcessStatus(AttributeValueProcessStatus.NO_CHECK_REQUIRED);
			if (log.isDebugEnabled()) {
				log.debug("Generating the BlemartiniName, child cars attribute map for dbPromotion car ");
			}
			// Check and remove duplicate attribute in dbPromotion CAR
			// Having a duplicate attribute is very rare scenario, Still
			// handling it to avoid any further issues
			Map<String, CarAttribute> uniqueCarAttrMap = new HashMap<String, CarAttribute>();
			Set<CarAttribute> dbPromotionAttributes = dbPromotionCar.getCarAttributes();
			boolean isDuplicateAttrFound = false;
			for (CarAttribute dbPromotionCarAttribute : dbPromotionAttributes) {
						String strBmName = dbPromotionCarAttribute.getAttribute()
						.getBlueMartiniAttribute();
				if (uniqueCarAttrMap.get(strBmName) == null) {
					uniqueCarAttrMap.put(strBmName, dbPromotionCarAttribute);
				} else {
					
					System.out.println("Parent PYG:Self removing any duplicate:"+strBmName);
					log.error("Removing duplicate attribute :" + strBmName
							+ " in dbPromotion car " + dbPromotionCar.getCarId());
					isDuplicateAttrFound = true;
					dbPromotionAttributes.remove(dbPromotionCarAttribute);
				}
			}
			// Saving the CAR after removing duplicate attributes
			if (isDuplicateAttrFound) {
				dbPromotionCar.setCarAttributes(dbPromotionAttributes);
				dbPromotionDao.save(dbPromotionCar);
			}
			// Generating the Map of all child car attributes according to
			// bluemartini name <BlueMartiniName, <Set>Attribute>
			// considering there can be multiple attribute in child cars with
			// same bluemartini name.
			for (CarDBPromotionChild carDBPromotionChild : carDBPromotionChilds) {
				Car childCar = carDBPromotionChild.getChildCar();
				childAttrs = this.getAllDBPromotionCarAttributes(childCar);
				for (CarAttribute ca : childAttrs) {
					Set<Attribute> attrs = null;
					String attrBMName = ca.getAttribute()
							.getBlueMartiniAttribute();
					if ((attrs = dbPromotionAttrMap.get(attrBMName)) == null) {
						attrs = new HashSet<Attribute>();
					}
					attrs.add(ca.getAttribute());
					dbPromotionAttrMap.put(attrBMName, attrs);
				}
			}
			if (dbPromotionAttrMap.get(Constants.DBPROMOTION_PARENT_PRODUCTS) != null) {//each PYG child has this promo_parent_product attribute that is not supposed to be copied on parent
				dbPromotionAttrMap.remove(Constants.DBPROMOTION_PARENT_PRODUCTS);
			}
			// loop through all child cars attribute and and add the new
			// attributes to the dbPromotion CARS
			// also add/remove the attributes from PROMO_CAR_ATTRIBUTE table
			for (String strAttrBMName : dbPromotionAttrMap.keySet()) { //each child's car atts set:to check if its present on PYG parent
				Set<Attribute> BMNameAttributeSet = dbPromotionAttrMap
						.get(strAttrBMName);
				List<Attribute> BMNameAttributeList = new ArrayList<Attribute>(
						BMNameAttributeSet);
				// get the highest priority attributes, priority is decided
				// based upon attribute display type.
				List<Attribute> highPriorityAttributes = prioritiseAttributes(BMNameAttributeList);
				CarAttribute ca = this.getCarAttributeByBMName(dbPromotionCar,
						strAttrBMName);
				if (ca == null) {
					// If current bluemartini name CarAttribute doesnot exist
					// for dbPromotion car then add the new car attribute
					ca = new CarAttribute();
					// set the highest priority attribute in CAR_ATTRIBUTE table
					Attribute attribute = highPriorityAttributes.get(0);
					ca.setAttribute(attribute);
					ca.setCar(dbPromotionCar);
					if (attribute.getAttributeConfig().getHtmlDisplayType()
							.isAutocomplete()) {
						ca.setAttributeValueProcessStatus(checkRequired);
					} else {
						ca.setAttributeValueProcessStatus(noCheckRequired);
					}
					// Setting to blank for now
					ca.setAttrValue("");
					ca.setDisplaySeq((short) 0);
					ca.setStatusCd(Status.ACTIVE);
					ca.setHasChanged(Constants.FLAG_NO);
					ca.setIsChangeRequired(Constants.FLAG_YES);
					ca.setAuditInfo(user);
					dbPromotionCar.addCarAttribute(ca);
					for (Attribute attr : BMNameAttributeSet) {
						// create the entries in DBPromotion_CAR_ATTRIBUTE
						DBPromotionCarAttribute ofCarAttr = new DBPromotionCarAttribute();
						ofCarAttr.setAttribute(attr);
						ca.getDbPromotionCarAttributes().add(ofCarAttr);
						ofCarAttr.setCarAttributeForPromo(ca);
					}
					if (log.isDebugEnabled()) {
						log.debug("Adding the car attribute : "
								+ ca.getCarAttrId());
					}

				} else {  //remove existing atts from parent coz that r no more present on child and add that are newly set on child
					if (log.isDebugEnabled()) {
						log.debug("CAR attribute exist for current BM name : "
								+ ca.getAttribute().getBlueMartiniAttribute());
					}
					Set<Attribute> existingAttributeSet = new HashSet<Attribute>();
					Set<DBPromotionCarAttribute> removeDBPromotionCarAttributes = new HashSet<DBPromotionCarAttribute>();
					Set<DBPromotionCarAttribute> dbPromotionCarAttributes = ca.getDbPromotionCarAttributes();
					// loop to remove the attributes from PROMOTION_CAR_ATTRIBUTE
					for (DBPromotionCarAttribute dbPromotionCarAttribute : dbPromotionCarAttributes) {
						Attribute currentOFAttribute = dbPromotionCarAttribute.getAttribute();
						if (BMNameAttributeSet.contains(currentOFAttribute)) {
							existingAttributeSet.add(currentOFAttribute);
						} else {
							// if current DBPromotion_car_attribute does not exist in
							// any of the child cars attribute, Then remove it
							removeDBPromotionCarAttributes.add(dbPromotionCarAttribute);
						}
					}
					for (DBPromotionCarAttribute oFCarAttribute : removeDBPromotionCarAttributes) {//removed-coz thaose r no more present on child
						// remove the DBPromotion_car_attribute
						ca.getDbPromotionCarAttributes().remove(oFCarAttribute);
					}
					// loop to add the attributes to PROMOTION_CAR_ATTRIBUTE
					for (Attribute attribute : BMNameAttributeSet) {//adding - that were nwly set on child after it was already added to this parent 
						if (!existingAttributeSet.contains(attribute)) {
							// if current child cars attribute does not exist in
							// existing DBPromotion_car_attributes, Then add it
							DBPromotionCarAttribute ofCarAttr = new DBPromotionCarAttribute();
							ofCarAttr.setAttribute(attribute);
							ofCarAttr.setCarAttributeForPromo(ca);
							ca.getDbPromotionCarAttributes().add(ofCarAttr);
						}
					}
					// prioritise car_attributes , If current attribute of
					// CAR_ATTRIBUTE is not highest priority attribute the set
					// it
					if (!highPriorityAttributes.contains(ca.getAttribute())) {
						ca.setAttribute(highPriorityAttributes.get(0));
					}
				}
				dbPromotionDao.save(ca);
				if (log.isDebugEnabled()) {
					log.debug("Saving the CAR_ATTRIBUTE :"
							+ ca.getAttribute().getBlueMartiniAttribute());
				}
			}
			// remove CAR_ATTRIBUTES which are not present in child cars
			Set<String> BMNameSet = dbPromotionAttrMap.keySet();
			Set<CarAttribute> removeCarAttribute = new HashSet<CarAttribute>();
			for (CarAttribute ca : dbPromotionCar.getCarAttributes()) { //but skip DBPROMOTION_DEFAULT_SKUS and DBPROMOTION_CHILD_PROD_CODES attributes that are set on Parent
				String carAttrName =ca.getAttribute().getName();
				String attributeBMName = ca.getAttribute()
						.getBlueMartiniAttribute();
				if (!BMNameSet.contains(attributeBMName)
						&& DBPromotionUtil.isNotAmongPromotionGroupAttributes(ca)) {
					System.out.println(attributeBMName+"is being removed");
					removeCarAttribute.add(ca);
				}
			}
			for (CarAttribute carAttribute : removeCarAttribute) {
				// remove the Car_attribute
				if (log.isDebugEnabled()) {
					log.debug("removing the CAR_ATTRIBUTE :"
							+ carAttribute.getCarAttrId());
				}
				dbPromotionCar.getCarAttributes().remove(carAttribute);
			}
			dbPromotionDao.save(dbPromotionCar);
			if (log.isInfoEnabled()) {
				log.info("Saved the DBPromotion CAR : " + dbPromotionCar.getCarId());
			}
		} catch (Exception e) {
			log.error("Exception In recync dbPromotion car: ");
		}
	}
	/*
	 * This method returns the highest priority attributes, priority is based on
	 * attribute display type eg: Checkbox has highest priority
	 */
	public List<Attribute> prioritiseAttributes(List<Attribute> attrList) {
		if (attrList.size() <= 1) {
			return attrList;
		}
		Map<String, List<Attribute>> attributeTypeMap = new HashMap<String, List<Attribute>>();
		List<Attribute> sameTypeAttrList = null;
		for (Attribute attr : attrList) {
			String attrType = attr.getAttributeConfig().getHtmlDisplayType()
					.getHtmlDisplayTypeCd();
			if ((sameTypeAttrList = attributeTypeMap.get(attrType)) == null) {
				sameTypeAttrList = new ArrayList<Attribute>();
			}
			sameTypeAttrList.add(attr);
			attributeTypeMap.put(attrType, sameTypeAttrList);
		}
		if ((sameTypeAttrList = attributeTypeMap.get(HtmlDisplayType.CHECKBOX)) != null) {
			return sameTypeAttrList;
		} else if ((sameTypeAttrList = attributeTypeMap
				.get(HtmlDisplayType.TEXT_WITH_AUTOCOMPLETE)) != null) {
			return sameTypeAttrList;
		} else if ((sameTypeAttrList = attributeTypeMap
				.get(HtmlDisplayType.DROP_DOWN)) != null) {
			return sameTypeAttrList;
		} else if ((sameTypeAttrList = attributeTypeMap
				.get(HtmlDisplayType.RADIO_BUTTONS)) != null) {
			return sameTypeAttrList;
		} else if ((sameTypeAttrList = attributeTypeMap
				.get(HtmlDisplayType.HTML_EDITOR)) != null) {
			return sameTypeAttrList;
		} else {
			return attrList;
		}
	}
	/**
	 * This method is used to get the attribute template type list for corresponding attribute id
	 * from ATTRIBUTE_LOOKUP_VALUE table.
	 * 
	 * @return 	attribute lookup values.
	 */
	public List<String> getPromoTemplateTypes() {
		return dbPromotionDao.getPromoTemplateTypes();
	}
	
	
	public Car getChildCarById(Long carId) {
		return dbPromotionDao.getChildCarById(carId);
	}
	public void saveorUpdate(Object o) {
		dbPromotionDao.removeObject(o);
	}
	
	public Car getCarFromId(Long carId) {
		return dbPromotionDao.getCarFromId(carId);
	}
	public Attribute getAttributeByName(String getAttributeByName) {
		return dbPromotionDao.getAttributeByName(getAttributeByName);
	}
	public CarAttribute findAttributeInCar(Car c, Attribute attr) {
		return dbPromotionDao.findAttributeInCar(c, attr);
	}
	public boolean getBMProductValue(Car c) {
		return dbPromotionDao.getBMProductValue(c);
	}
	public void assignDBPromotionToVendor(Car DBPromotionCar, String childCarId) {
		if (log.isInfoEnabled()) {
			log.info("Assign DBPromotion to Vendor :childCarId:" + childCarId);
		}
		Set<CarDBPromotionChild> dbPromotionChilds = DBPromotionCar.getCarDBPromotionChild();
		for (CarDBPromotionChild ofChild : dbPromotionChilds) {
			if (ofChild.getChildCar().getCarId() == Long.valueOf(childCarId)) {
				ofChild.setVendor(ofChild.getChildCar().getVendorStyle()
						.getVendor());
			} else {
				ofChild.setVendor(null);
			}
			dbPromotionDao.save(ofChild);
		}
	}
	public CarAttribute getCarAttributeByAttributeName(Car c,
			String attributeName) {
		return dbPromotionDao.getCarAttributeByAttributeName(c, attributeName);
	}
	public CarAttribute getCarAttributeByBMName(Car c, String strBMName) {
		return dbPromotionDao.getCarAttributeByBMName(c, strBMName);
	}
	
	public String getAttributeValue(Car car, Attribute attrChildProducts) {
		return dbPromotionDao.getAttributeValue(car, attrChildProducts);
	}
	
	public long getCarAttributeId(Car car,Attribute isSearchableData){
		return dbPromotionDao.getCarAttributeId(car,isSearchableData);
	}
	
	public List<DBPromotionCollectionSkus> getDBPromotionCollectionSkus(String productCode) {
		return dbPromotionDao.getDBPromotionCollectionSkus(productCode);
	}

	/**
	 * This method is used to get the child car sku list from 
	 * car table for corresponding vendorstyle id.
	 * 
	 * @param  venStyleId   passing vendorstyle object. 
	 * @return child car list. 
	 */
	public List<Car> getChildCarSkus(VendorStyle venStyleId) {
		return dbPromotionDao.getChildCarSkus(venStyleId);
	}
	/**
	 * Method is used to display the child car details on form
	 * like: Car Id, Style Number, Product Name, Brand Name, Order, Default Sku 
	 * 
	 * @param car	                It contain car object.
	 * @param DBPromotionChild           It contains dbPromotion child car
	 * @param lstchildProductSKUs	It contain list of sku
	 * 
	 * @return Returns the Child CAR object in the way to display it on form. 
	 */
	public ChildCar getViewChildCarDetails(Car car, CarDBPromotionChild dbPromotionChild,
			List<String> lstchildProductSKUs) {
		System.out.println(car.getCarId()+"List Skys prd"+lstchildProductSKUs);
		
		ChildCar childCar = new ChildCar();
		childCar.setCarId(car.getCarId());
		childCar.setStyleNumber(car.getVendorStyle().getVendorStyleNumber());
		childCar.setProductName(car.getVendorStyle().getVendorStyleName());
		CarAttribute carAttr = getCarAttributeByBMName(car, "Brand");
		if (carAttr != null) {
			childCar.setBrandName(carAttr.getAttrValue());
		} else {
			childCar.setBrandName("");
		}
		Set<VendorSku> carVendorSku = car.getVendorSkus();
		System.out.println("Child's Vendor_SKUs size"+carVendorSku.size());
		Map<String, String> skuMap = new HashMap<String, String>();
		// Add the SKU List.
		for (VendorSku vSku : carVendorSku) {
			// If the color is empty or null, set color as NO COLOR
			if (vSku.getColorName() == null || "".equals(vSku.getColorName())) {
				vSku.setColorName("NO COLOR");
			}
			// Put first only that SKU which is present in child product sku
			// list
			if (lstchildProductSKUs.contains(vSku.getBelkSku())) {
				System.out.println("lYes stchildProductSKUs contains currnt Vend_Sku");
				skuMap.put(vSku.getColorName(), vSku.getBelkSku());

			}
			// And now put the other SKUs.
			if (!skuMap.containsKey(vSku.getColorName())) {
				System.out.println("putting Vned_sku in map ");
				skuMap.put(vSku.getColorName(), vSku.getBelkSku());
			}
		}
		System.out.println("DB fit Child Car SkuColorMap:" + skuMap);
		if (log.isDebugEnabled()) {
			log.debug("Child Car SkuColorMap:" + skuMap);
		}
		childCar.setColorSkuMap(skuMap);
		if (log.isDebugEnabled()) {
			log.debug("Child Car Priority:" + dbPromotionChild.getPriority());
		}
		childCar.setOrder(dbPromotionChild.getPriority());
		VendorSku defaultColorSku = dbPromotionChild.getDefaultColorSku();
		if (defaultColorSku != null) {
			System.out.println("Childs's default sku got is"+defaultColorSku.getColorName());
			if (log.isDebugEnabled()) {
				log.debug("Child Car Belk sku:" + defaultColorSku.getBelkSku()
						+ " and Color:" + defaultColorSku.getColorName());
				log.debug("Child Car sku id:" + defaultColorSku.getCarSkuId());
			}
			childCar.setSku(defaultColorSku.getColorName());
		} else {
			System.out.println("Childs's default sku got is null so setting empty");
			childCar.setSku("");
		}
		
		childCar.setStyleTypeCd(car.getVendorStyle().getVendorStyleType()
				.getCode());
		
		System.out.println("childCar to view got set with d style code is "+childCar.getStyleNumber());
		return childCar;
	}

	





	public List<Car> getAllDBPromotions() {
		
		
		return dbPromotionDao.getAllDBPromotions();
	}

	public List<Car> serachDBPromotionCars(String dbPromotionName,
			String styleNumber) {
	return dbPromotionDao.serachDBPromotionCars(dbPromotionName, styleNumber);
	}

	public void removeCarDBPromotionChild(CarDBPromotionChild dbPromotionChild) {
		dbPromotionDao.removeObject(dbPromotionChild);
	}

	

	

	public List<CarAttribute> getAllDBPromotionCarAttributes(Car car) {
		// TODO Auto-generated method stub
		System.out.println("Nothing in the method getAllDBPromotionCarAttributes() ");
		return dbPromotionDao.getAllDBPromotionCarAttributes(car);		
	}


	public Car getChildCarForRemove(Long carId) {
		return dbPromotionDao.getChildCarForRemove(carId);
	}

	public DBPromotionDao getDbPromotionDao() {
		return dbPromotionDao;
	}

	public void setDbPromotionDao(DBPromotionDao dbPromotionDao) {
		this.dbPromotionDao = dbPromotionDao;
	}

	public List<String> getAttributeValueById() {
		return dbPromotionDao.getAttrTemplateType();
		
	}

	@Override
	public PromoType findPromoType(long carId) {
		return dbPromotionDao.findPromoType(carId);
	}
	public void savePromoType(PromoType promoType){
		
		dbPromotionDao.save(promoType);
	}
	
	
}
