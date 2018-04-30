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
import com.belk.car.app.dao.OutfitDao;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeValueProcessStatus;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarOutfitChild;
import com.belk.car.app.model.CollectionSkus;
import com.belk.car.app.model.HtmlDisplayType;
import com.belk.car.app.model.OutfitCarAttribute;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.OutfitManager;
import com.belk.car.app.service.ProductManager;
import com.belk.car.app.service.WorkflowManager;
import com.belk.car.app.util.CreateOutfitCar;
import com.belk.car.app.util.SaveOutfitCar;
import com.belk.car.app.webapp.forms.ChildCar;

/**
 * 
 * @author AFUSY12/ santosh_chaudhary@belk.com
 * @author AFUVXR1/ KARTHIK
 * 
 * Outfit manager is to handle create/save outfit car and collection car
 * 
 */
public class OutfitManagerImpl extends UniversalManagerImpl implements OutfitManager {
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
	 * This method is used to create the outfit  car by calling createOutfit
	 * method  and passing following details for  outfit car: name , description, 
	 * child cars and skus.
	 * 
	 * 
	 * @param name     	This variable contains Outfit name
	 * @param desc	    This variable contains outfit car description.
	 * @param childCars List of cars
	 * @param orderCars This variable contains priority
	 * @param skus      List of skus
	 * 
	 * @return 	return car object	
	 */
	public Car createOutfit(String name, String desc, List<Car> childCars,
			List<String> orderCars, List<String> skus,String templateType) {
		Car outfitcar = null;
		try {
			CreateOutfitCar objOutfit = new CreateOutfitCar();
			objOutfit.setCarManager(carManager);
			objOutfit.setLookupManager(lookupManager);
			objOutfit.setOutfitDao(outfitDao);
			objOutfit.setProductManager(productManager);
			objOutfit.setWorkflowManager(workflowManager);
			outfitcar = objOutfit.createOutfit(name, desc, childCars, orderCars,
					skus,templateType);
		} catch (Exception e) {
			log.error("Could not create the outfit CAR");
		}
		return outfitcar;
	}
	
	/**
	 * This method is used to store the product code and sku code in collection_skus table 
	 * by calling addCollectionSkus method.
	 * 
	 * @param prodCode 	 This variable contains Product Code
	 * @param skuCode	 This variable contains Sku Code
	 */
	public void createCollectionSkus(String prodCode, String skuCode) {
		try {
			CreateOutfitCar objOutfit = new CreateOutfitCar();
			objOutfit.setCarManager(carManager);
			objOutfit.setLookupManager(lookupManager);
			objOutfit.setOutfitDao(outfitDao);
			objOutfit.setProductManager(productManager);
			objOutfit.setWorkflowManager(workflowManager);
			objOutfit.addCollectionSkus(prodCode, skuCode);
		} catch (Exception e) {
			log.error("Could not create the outfit CAR");
		}
	}
	
	/**
	 * This method performing following task:
	 * 1. Remove the existing Product code and sku code from COLLECTION_SKUS table
	 * 
	 * @param productCode      It contains product code. 
	 * 
	 */	
	public void removeCollectionSkus(String productCode) {
		try {
			CreateOutfitCar objOutfit = new CreateOutfitCar();
			objOutfit.setCarManager(carManager);
			objOutfit.setLookupManager(lookupManager);
			objOutfit.setOutfitDao(outfitDao);
			objOutfit.setProductManager(productManager);
			objOutfit.setWorkflowManager(workflowManager);
			objOutfit.removeCollectionSkus(productCode);
		} catch (Exception e) {
			log.error("Could not create the outfit CAR");
		}
	}
	
	/**
	 * This method is used to store the sku attribute in CarAttribute table by calling 
	 * updateCollectionSkuAttributes method and passing carid and attribute value.
	 * 
	 * @param skuCarId 	     This variable contains carid
	 * @param attrValueSkus	 This variable contains attribute vale of SKU
	 */
	public void updateCollectionSkuAttributes(Long skuCarId,
			String attrValueSkus, String valueToBeUpdated) {
		try {
			CreateOutfitCar objOutfit = new CreateOutfitCar();
			objOutfit.setCarManager(carManager);
			objOutfit.setLookupManager(lookupManager);
			objOutfit.setOutfitDao(outfitDao);
			objOutfit.setProductManager(productManager);
			objOutfit.setWorkflowManager(workflowManager);
			objOutfit.updateCollectionSkuAttributes(skuCarId, attrValueSkus,
					valueToBeUpdated);
		} catch (Exception e) {
			log.error("Could not create the outfit CAR");
		}
	}
	
	/**
	 * This method is used to store the attribute value(isSearchable, effective
	 * start date , template type) in car attribute table by calling 
	 * addCollectionAttributes method and passing outfiltCarId and attribute value.
	 * 
	 * 
	 * @param outfiltCarId 		  This variable contains outfiltCarId. 
	 * @param attrValue 		  This variable contains attribute value.
	 */
	public void createProductAttributes(Long outfiltCarId, String attrValue,
			String attrParentProducts) {
		
		try {
			CreateOutfitCar objOutfit = new CreateOutfitCar();
			objOutfit.setCarManager(carManager);
			objOutfit.setLookupManager(lookupManager);
			objOutfit.setOutfitDao(outfitDao);
			objOutfit.setProductManager(productManager);
			objOutfit.setWorkflowManager(workflowManager);
			objOutfit.addCollectionAttributes(outfiltCarId, attrValue,
					attrParentProducts);
		} catch (Exception e) {
			log.error("Could not create the outfit CAR");
		}
	}

	/**
	 * Method to save outfit car
	 */
	public void saveOutfit(Car OutfitCar, String name, String desc,
			List<Car> childCars, List<String> orderCars, List<String> skus,String templateType) {
		try {
			SaveOutfitCar saveOutfitUtil = new SaveOutfitCar();
			saveOutfitUtil.setLookupManager(lookupManager);
			saveOutfitUtil.setOutfitDao(outfitDao);
			saveOutfitUtil.saveOutfit(OutfitCar, name, desc, childCars,
					orderCars, skus,templateType);
		} catch (Exception e) {
			log.error("Could not create the outfit CAR");
		}
	}
	/**
	 * Method to resync attributes on outfit car
	 */
	public void resyncOutfitAttributes(Car outfitCar, User user) {
		if (log.isInfoEnabled()) {
			log.info("In Resync attribute for OUTFIT CAR : "
					+ outfitCar.getCarId());
		}
		try {
			List<CarAttribute> childAttrs = new ArrayList<CarAttribute>();
			Map<String, Set<Attribute>> outfitAttrMap = new HashMap<String, Set<Attribute>>();
			Set<CarOutfitChild> carOutfitChilds = outfitCar.getCarOutfitChild();
			AttributeValueProcessStatus checkRequired = lookupManager
					.getAttributeValueProcessStatus(AttributeValueProcessStatus.CHECK_REQUIRED);
			AttributeValueProcessStatus noCheckRequired = lookupManager
					.getAttributeValueProcessStatus(AttributeValueProcessStatus.NO_CHECK_REQUIRED);
			if (log.isDebugEnabled()) {
				log.debug("Generating the BlemartiniName, child cars attribute map for outfit car ");
			}
			// Check and remove duplicate attribute in outfit CAR
			// Having a duplicate attribute is very rare scenario, Still
			// handling it to avoid any further issues
			Map<String, CarAttribute> uniqueCarAttrMap = new HashMap<String, CarAttribute>();
			Set<CarAttribute> outfitAttributes = outfitCar.getCarAttributes();
			boolean isDuplicateAttrFound = false;
			for (CarAttribute outfitCarAttribute : outfitAttributes) {
						String strBmName = outfitCarAttribute.getAttribute()
						.getBlueMartiniAttribute();
				if (uniqueCarAttrMap.get(strBmName) == null) {
					uniqueCarAttrMap.put(strBmName, outfitCarAttribute);
				} else {
					log.error("Removing duplicate attribute :" + strBmName
							+ " in outfit car " + outfitCar.getCarId());
					isDuplicateAttrFound = true;
					outfitAttributes.remove(outfitCarAttribute);
				}
			}
			// Saving the CAR after removing duplicate attributes
			if (isDuplicateAttrFound) {
				outfitCar.setCarAttributes(outfitAttributes);
				outfitDao.save(outfitCar);
			}
			// Generating the Map of all child car attributes according to
			// bluemartini name <BlueMartiniName, <Set>Attribute>
			// considering there can be multiple attribute in child cars with
			// same bluemartini name.
			for (CarOutfitChild carOutfitChild : carOutfitChilds) {
				Car childCar = carOutfitChild.getChildCar();
				childAttrs = this.getAllOutfitCarAttributes(childCar);
				for (CarAttribute ca : childAttrs) {
					Set<Attribute> attrs = null;
					String attrBMName = ca.getAttribute()
							.getBlueMartiniAttribute();
					if ((attrs = outfitAttrMap.get(attrBMName)) == null) {
						attrs = new HashSet<Attribute>();
					}
					attrs.add(ca.getAttribute());
					outfitAttrMap.put(attrBMName, attrs);
				}
			}
			if (outfitAttrMap.get(Constants.OUTFIT_PARENT_PRODUCTS) != null) {
				outfitAttrMap.remove(Constants.OUTFIT_PARENT_PRODUCTS);
			}
			// loop through all child cars attribute and and add the new
			// attributes to the outfit CARS
			// also add/remove the attributes from OUTFIT_CAR_ATTRIBUTE table
			for (String strAttrBMName : outfitAttrMap.keySet()) {
				Set<Attribute> BMNameAttributeSet = outfitAttrMap
						.get(strAttrBMName);
				List<Attribute> BMNameAttributeList = new ArrayList<Attribute>(
						BMNameAttributeSet);
				// get the highest priority attributes, priority is decided
				// based upon attribute display type.
				List<Attribute> highPriorityAttributes = prioritiseAttributes(BMNameAttributeList);
				CarAttribute ca = this.getCarAttributeByBMName(outfitCar,
						strAttrBMName);
				if (ca == null) {
					// If current bluemartini name CarAttribute doesnot exist
					// for outfit car then add the new car attribute
					ca = new CarAttribute();
					// set the highest priority attribute in CAR_ATTRIBUTE table
					Attribute attribute = highPriorityAttributes.get(0);
					ca.setAttribute(attribute);
					ca.setCar(outfitCar);
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
					outfitCar.addCarAttribute(ca);
					for (Attribute attr : BMNameAttributeSet) {
						// create the entries in OUTFIT_CAR_ATTRIBUTE
						OutfitCarAttribute ofCarAttr = new OutfitCarAttribute();
						ofCarAttr.setAttribute(attr);
						ca.getOutfitCarAttributes().add(ofCarAttr);
						ofCarAttr.setCarAttribute(ca);
					}
					if (log.isDebugEnabled()) {
						log.debug("Adding the car attribute : "
								+ ca.getCarAttrId());
					}

				} else {
					if (log.isDebugEnabled()) {
						log.debug("CAR attribute exist for current BM name : "
								+ ca.getAttribute().getBlueMartiniAttribute());
					}
					Set<Attribute> existingAttributeSet = new HashSet<Attribute>();
					Set<OutfitCarAttribute> removeOutfitCarAttributes = new HashSet<OutfitCarAttribute>();
					Set<OutfitCarAttribute> outfitCarAttributes = ca.getOutfitCarAttributes();
					// loop to remove the attributes from OUTFIT_CAR_ATTRIBUTE
					for (OutfitCarAttribute outfitCarAttribute : outfitCarAttributes) {
						Attribute currentOFAttribute = outfitCarAttribute.getAttribute();
						if (BMNameAttributeSet.contains(currentOFAttribute)) {
							existingAttributeSet.add(currentOFAttribute);
						} else {
							// if current outfit_car_attribute does not exist in
							// any of the child cars attribute, Then remove it
							removeOutfitCarAttributes.add(outfitCarAttribute);
						}
					}
					for (OutfitCarAttribute oFCarAttribute : removeOutfitCarAttributes) {
						// remove the Outfit_car_attribute
						ca.getOutfitCarAttributes().remove(oFCarAttribute);
					}
					// loop to add the attributes to OUTFIT_CAR_ATTRIBUTE
					for (Attribute attribute : BMNameAttributeSet) {
						if (!existingAttributeSet.contains(attribute)) {
							// if current child cars attribute does not exist in
							// existing outfit_car_attributes, Then add it
							OutfitCarAttribute ofCarAttr = new OutfitCarAttribute();
							ofCarAttr.setAttribute(attribute);
							ofCarAttr.setCarAttribute(ca);
							ca.getOutfitCarAttributes().add(ofCarAttr);
						}
					}
					// prioritise car_attributes , If current attribute of
					// CAR_ATTRIBUTE is not highest priority attribute the set
					// it
					if (!highPriorityAttributes.contains(ca.getAttribute())) {
						ca.setAttribute(highPriorityAttributes.get(0));
					}
				}
				outfitDao.save(ca);
				if (log.isDebugEnabled()) {
					log.debug("Saving the CAR_ATTRIBUTE :"
							+ ca.getAttribute().getBlueMartiniAttribute());
				}
			}
			// remove CAR_ATTRIBUTES which are not present in child cars
			Set<String> BMNameSet = outfitAttrMap.keySet();
			Set<CarAttribute> removeCarAttribute = new HashSet<CarAttribute>();
			for (CarAttribute ca : outfitCar.getCarAttributes()) {
				String attributeBMName = ca.getAttribute()
						.getBlueMartiniAttribute();
				if (!BMNameSet.contains(attributeBMName)
						&& !attributeBMName
								.equals(Constants.OUTFIT_CHILD_PRODUCTS)) {
					removeCarAttribute.add(ca);
				}
			}
			for (CarAttribute carAttribute : removeCarAttribute) {
				// remove the Car_attribute
				if (log.isDebugEnabled()) {
					log.debug("removing the CAR_ATTRIBUTE :"
							+ carAttribute.getCarAttrId());
				}
				outfitCar.getCarAttributes().remove(carAttribute);
			}
			outfitDao.save(outfitCar);
			if (log.isInfoEnabled()) {
				log.info("Saved the OUTFIT CAR : " + outfitCar.getCarId());
			}
		} catch (Exception e) {
			log.error("Exception In recync outfit car: ");
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
	public List<String> getAttributeValueById() {
		return outfitDao.getAttrTemplateType();
	}
	public List<Car> getAllOutfits() {
		return outfitDao.getAllOutfits();
	}
	public List<Car> serachOutfitCars(String outfitName, String styleNumber) {
		return outfitDao.serachOutfitCars(outfitName, styleNumber);
	}
	public Car getChildCarById(Long carId) {
		return outfitDao.getChildCarById(carId);
	}
	public void saveorUpdate(Object o) {
		outfitDao.removeObject(o);
	}
	public void removeCarOufitChild(CarOutfitChild outfitChild) {
		outfitDao.removeObject(outfitChild);
	}
	public Car getCarFromId(Long carId) {
		return outfitDao.getCarFromId(carId);
	}
	public Attribute getAttributeByName(String getAttributeByName) {
		return outfitDao.getAttributeByName(getAttributeByName);
	}
	public CarAttribute findAttributeInCar(Car c, Attribute attr) {
		return outfitDao.findAttributeInCar(c, attr);
	}
	public boolean getBMProductValue(Car c) {
		return outfitDao.getBMProductValue(c);
	}
	public void assignOutfitToVendor(Car OutfitCar, String childCarId) {
		if (log.isInfoEnabled()) {
			log.info("Assign Outfit to Vendor :childCarId:" + childCarId);
		}
		Set<CarOutfitChild> outfitChilds = OutfitCar.getCarOutfitChild();
		for (CarOutfitChild ofChild : outfitChilds) {
			if (ofChild.getChildCar().getCarId() == Long.valueOf(childCarId)) {
				ofChild.setVendor(ofChild.getChildCar().getVendorStyle()
						.getVendor());
			} else {
				ofChild.setVendor(null);
			}
			outfitDao.save(ofChild);
		}
	}
	public CarAttribute getCarAttributeByAttributeName(Car c,
			String attributeName) {
		return outfitDao.getCarAttributeByAttributeName(c, attributeName);
	}
	public CarAttribute getCarAttributeByBMName(Car c, String strBMName) {
		return outfitDao.getCarAttributeByBMName(c, strBMName);
	}
	public List<CarAttribute> getAllOutfitCarAttributes(Car car) {
		return outfitDao.getAllOutfitCarAttributes(car);
	}
	public String getAttributeValue(Car car, Attribute attrChildProducts) {
		return outfitDao.getAttributeValue(car, attrChildProducts);
	}
	
	public long getCarAttributeId(Car car,Attribute isSearchableData){
		return outfitDao.getCarAttributeId(car,isSearchableData);
	}
	
	public List<CollectionSkus> getCollectionSkus(String productCode) {
		return outfitDao.getCollectionSkus(productCode);
	}

	/**
	 * This method is used to get the child car sku list from 
	 * car table for corresponding vendorstyle id.
	 * 
	 * @param  venStyleId   passing vendorstyle object. 
	 * @return child car list. 
	 */
	public List<Car> getChildCarSkus(VendorStyle venStyleId) {
		return outfitDao.getChildCarSkus(venStyleId);
	}
	/**
	 * Method is used to display the child car details on form
	 * like: Car Id, Style Number, Product Name, Brand Name, Order, Default Sku 
	 * 
	 * @param car	                It contain car object.
	 * @param outfitChild           It contains outfit child car
	 * @param lstchildProductSKUs	It contain list of sku
	 * 
	 * @return Returns the Child CAR object in the way to display it on form. 
	 */
	public ChildCar getViewChildCarDetails(Car car, CarOutfitChild outfitChild,
			List<String> lstchildProductSKUs) {
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
				skuMap.put(vSku.getColorName(), vSku.getBelkSku());

			}
			// And now put the other SKUs.
			if (!skuMap.containsKey(vSku.getColorName())) {
				skuMap.put(vSku.getColorName(), vSku.getBelkSku());
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("Child Car SkuColorMap:" + skuMap);
		}
		childCar.setColorSkuMap(skuMap);
		if (log.isDebugEnabled()) {
			log.debug("Child Car Priority:" + outfitChild.getPriority());
		}
		childCar.setOrder(outfitChild.getPriority());
		VendorSku defaultColorSku = outfitChild.getDefaultColorSku();
		if (defaultColorSku != null) {
			if (log.isDebugEnabled()) {
				log.debug("Child Car Belk sku:" + defaultColorSku.getBelkSku()
						+ " and Color:" + defaultColorSku.getColorName());
				log.debug("Child Car sku id:" + defaultColorSku.getCarSkuId());
			}
			childCar.setSku(defaultColorSku.getColorName());
		} else {
			childCar.setSku("");
		}
		
		childCar.setStyleTypeCd(car.getVendorStyle().getVendorStyleType()
				.getCode());
		return childCar;
	}

	@Override
	public Car getChildCarForRemove(Long carId) {
		return outfitDao.getChildCarForRemove(carId);
	}
}
