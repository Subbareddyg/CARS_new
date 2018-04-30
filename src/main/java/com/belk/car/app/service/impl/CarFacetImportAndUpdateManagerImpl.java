package com.belk.car.app.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.appfuse.service.impl.UniversalManagerImpl;

import com.belk.car.app.Constants;
import com.belk.car.app.dao.CarDao;
import com.belk.car.app.dao.CarManagementDao;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeValueProcessStatus;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.ClassAttribute;
import com.belk.car.app.model.DepartmentAttribute;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.model.ProductTypeAttribute;
import com.belk.car.app.model.Status;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarFacetImportAndUpdateManager;;




public class CarFacetImportAndUpdateManagerImpl extends UniversalManagerImpl implements CarFacetImportAndUpdateManager {

	private CarDao carDao;
	
	private CarLookupManager lookupManager;
	
	private CarManagementDao carMgmtDao;
	
	public CarLookupManager getLookupManager() {
		return lookupManager;
	}

	public void setLookupManager(CarLookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}

	/**
	 * Method that allows setting the DAO to talk to the data store with.
	 * @param dao the dao implementation
	 */
	public void setCarDao(CarDao carDao) {
		this.carDao = carDao;
	}
	
	public CarDao getCarDao(CarDao carDao) {
		return carDao;
	}
	
	public CarManagementDao getCarMgmtDao() {
		return carMgmtDao;
	}

	public void setCarMgmtDao(CarManagementDao carMgmtDao) {
		this.carMgmtDao = carMgmtDao;
	}

	public CarFacetImportAndUpdateManagerImpl() {

	}

	public List<ClassAttribute> getAllClassificationAttributes(long classId) {
		return carDao.getAllClassificationAttributes(classId);
	}

	public List<DepartmentAttribute> getAllDepartmentAttributes(long deptId) {
		return carDao.getAllDepartmentAttributes(deptId);
	}
    
	public List<CarAttribute> getAllCarAttribute(long carId){
		return carDao.getAllCarAttribute(carId);
	}
	
	public Car getCarFromId(Long carNumber) {
		return carMgmtDao.getCarFromId(carNumber);
	}
	
	public void resyncAttributes(Car car, User loggedInUser,Map attributeXMlMap,Set<String> attrNotProcessed)  {
		List<DepartmentAttribute> departmentAttributes = this.getAllDepartmentAttributes(car.getDepartment().getDeptId());
		List<ClassAttribute> classificationAttributes = this.getAllClassificationAttributes(car.getVendorStyle().getClassification()
				.getClassId());

		Map<String, Attribute> attributeMap = new HashMap<String, Attribute>();
		Map<String, String> attributeDefaultValueMap = new HashMap<String, String>();
		String strValue="";
		log.debug("\t Adding department attribute");
		for (DepartmentAttribute deptAttr : departmentAttributes) {
			if (deptAttr.getAttribute().isActive() && deptAttr.getAttribute().isFacetAttribute()) {
				attributeMap.put(deptAttr.getAttribute().getBlueMartiniAttribute(), deptAttr.getAttribute());
				attrNotProcessed.remove(deptAttr.getAttribute().getBlueMartiniAttribute().toUpperCase());
				strValue=(String)attributeXMlMap.get(deptAttr.getAttribute().getBlueMartiniAttribute().toUpperCase());
				if(strValue != null && strValue.length() > 0){
					attributeDefaultValueMap.put(deptAttr.getAttribute().getBlueMartiniAttribute(),strValue);
				}else{
					attributeDefaultValueMap.put(deptAttr.getAttribute().getBlueMartiniAttribute(), StringUtils.defaultString(deptAttr.getDefaultAttrValue()));
				}
			}
		}

		ProductType productType = car.getVendorStyle().getProductType() ;
		log.debug("\t Adding product type attribute");
		//Setup Product Type Attributes
		if (productType != null) {
			for (ProductTypeAttribute ptAttr : productType.getProductTypeAttributes()) {
				if (ptAttr.getAttribute().isActive() && ptAttr.getAttribute().isFacetAttribute()) {
					if (attributeMap.containsKey(ptAttr.getAttribute().getBlueMartiniAttribute())) {
						attributeMap.remove(ptAttr.getAttribute().getBlueMartiniAttribute());
						attributeDefaultValueMap.remove(ptAttr.getAttribute().getBlueMartiniAttribute());
					}
					attributeMap.put(ptAttr.getAttribute().getBlueMartiniAttribute(), ptAttr.getAttribute());
					attrNotProcessed.remove(ptAttr.getAttribute().getBlueMartiniAttribute().toUpperCase());
					strValue=(String)attributeXMlMap.get(ptAttr.getAttribute().getBlueMartiniAttribute().toUpperCase());
					if(strValue != null && strValue.length() > 0){
						attributeDefaultValueMap.put(ptAttr.getAttribute().getBlueMartiniAttribute(),strValue);
					}else{
						attributeDefaultValueMap.put(ptAttr.getAttribute().getBlueMartiniAttribute(), StringUtils.defaultString(ptAttr.getDefaultAttrValue()));
					}
				}
			}
		}
		
		log.debug("\t Adding classification attribute");
		for (ClassAttribute classAttr : classificationAttributes) {
			if (classAttr.getAttribute().isActive() && classAttr.getAttribute().isFacetAttribute()) {
				if (attributeMap.containsKey(classAttr.getAttribute().getBlueMartiniAttribute())) {
					attributeMap.remove(classAttr.getAttribute().getBlueMartiniAttribute());
					attributeDefaultValueMap.remove(classAttr.getAttribute().getBlueMartiniAttribute());
				}
				attributeMap.put(classAttr.getAttribute().getBlueMartiniAttribute(), classAttr.getAttribute());
				attrNotProcessed.remove(classAttr.getAttribute().getBlueMartiniAttribute().toUpperCase());
				strValue=(String)attributeXMlMap.get(classAttr.getAttribute().getBlueMartiniAttribute().toUpperCase());
				if(strValue != null && strValue.length() > 0){
					attributeDefaultValueMap.put(classAttr.getAttribute().getBlueMartiniAttribute(),strValue);
				}else{
					attributeDefaultValueMap.put(classAttr.getAttribute().getBlueMartiniAttribute(), StringUtils.defaultString(classAttr.getDefaultAttrValue()));
				}
			}
		}
		log.debug("\t Adding car attribute");
		Set<Attribute> attrInCar = new HashSet<Attribute>() ;
		Set<CarAttribute> removeCarAttribute = new HashSet<CarAttribute>() ;
		Collection <Attribute> attributeMapValues = attributeMap.values();
		for (CarAttribute carAttr: car.getCarAttributes()) {
			attrInCar.add(carAttr.getAttribute()) ;
			if (!containsAttribute(attributeMapValues, carAttr.getAttribute()) && carAttr.getAttribute().isFacetAttribute()) {
				//removeCarAttribute.add(carAttr) ;//commented this code as this is not the complete rsync process
			}else{
				if(carAttr.getAttribute().isFacetAttribute()){
					strValue=(String)attributeXMlMap.get(carAttr.getAttribute().getBlueMartiniAttribute().toUpperCase());
					if(strValue != null && strValue.length() > 0){
						carAttr.setAttrValue(strValue);
						carAttr.setAuditInfo(loggedInUser);
					}
				}
			}
		}
		
		AttributeValueProcessStatus checkRequired = this.getLookupManager().getAttributeValueProcessStatus(AttributeValueProcessStatus.CHECK_REQUIRED);
		AttributeValueProcessStatus noCheckRequired = this.getLookupManager().getAttributeValueProcessStatus(AttributeValueProcessStatus.NO_CHECK_REQUIRED);
		log.debug("\t Synchronizing attribute");
		
		if (!removeCarAttribute.isEmpty()) {
			log.info("removing "+ removeCarAttribute.size() + "  attributes from CAR");
			for (CarAttribute ca : removeCarAttribute) {
				car.getCarAttributes().remove(ca) ;
			}
		}
		
		for (Attribute attribute : attributeMap.values()) {
			if(!containsAttribute(attrInCar,attribute)) {
				CarAttribute carAttribute = new CarAttribute();
				carAttribute.setAttribute(attribute);
				carAttribute.setCar(car);
				if (attribute.getAttributeConfig().getHtmlDisplayType().isAutocomplete()) {
					carAttribute.setAttributeValueProcessStatus(checkRequired) ;
				} else {
					carAttribute.setAttributeValueProcessStatus(noCheckRequired) ;										
				}
	        	// Setting to blank for now. Need to get it from the association
				carAttribute.setAttrValue(attributeDefaultValueMap.get(attribute.getBlueMartiniAttribute()));
				carAttribute.setDisplaySeq((short) 0);
				carAttribute.setHasChanged(Constants.FLAG_NO);
				carAttribute.setIsChangeRequired(Constants.FLAG_YES);
				carAttribute.setStatusCd(Status.ACTIVE);
				carAttribute.setAuditInfo(loggedInUser);
				car.getCarAttributes().add(carAttribute);
				log.info("added new attribute to car: "+ car.getCarId() + "   attr_id : "+ attribute.getAttributeId());
			}
		}
	}
	
	public boolean containsAttribute(Collection<Attribute> attrSet, Attribute a){
	    if(a != null){
		for(Attribute attr: attrSet){
			if(attr.getAttributeId() == a.getAttributeId()){
				log.debug("containsAttribute: "+ a.getAttributeId());
				return true;
			}
		  }
	    }
		return false;
    }

}
