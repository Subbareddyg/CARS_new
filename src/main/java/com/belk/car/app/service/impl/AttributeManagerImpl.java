/**
 * 
 */
package com.belk.car.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.service.impl.UniversalManagerImpl;

import com.belk.car.app.dao.AttributeDao;
import com.belk.car.app.dto.AttributeBMMappingDTO;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeChangeTracking;
import com.belk.car.app.model.AttributeConfig;
import com.belk.car.app.model.AttributeDatatype;
import com.belk.car.app.model.AttributeLookupValue;
import com.belk.car.app.model.AttributeType;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.ClassAttribute;
import com.belk.car.app.model.DepartmentAttribute;
import com.belk.car.app.model.ProductTypeAttribute;
import com.belk.car.app.service.AttributeManager;

/**
 * @author antoniog
 *
 */
public class AttributeManagerImpl extends UniversalManagerImpl implements
		AttributeManager {

	private AttributeDao attributeDao;

	public void setAttributeDao(AttributeDao attributeDao) {
		this.attributeDao = attributeDao;
	}

	public List<Attribute> getAllAttributes() {
		return attributeDao.getAllAttributes();
	}

	public Attribute getAttribute(Long attributeId) {
		return attributeDao.getAttribute(attributeId);
	}

	public Attribute getAttribute(String name) {
		return attributeDao.getAttribute(name);
	}

	public List<AttributeDatatype> getAttributeDataTypes() {
		return attributeDao.getAttributeDataTypes();
	}

	public List<AttributeType> getAttributeTypes() {
		return attributeDao.getAttributeTypes();
	}

	public Attribute save(Attribute attribute) {
		return attributeDao.save(attribute);
	}

	public AttributeConfig save(AttributeConfig attributeConfig) {
		return attributeDao.save(attributeConfig);
	}

	public List<ClassAttribute> getClassAttributes() {
		return attributeDao.getClassAttributes();
	}

	public List<DepartmentAttribute> getDepartmentAttributes() {
		return attributeDao.getDepartmentAttributes();
	}

	public List<ProductTypeAttribute> getProductTypeAttributes() {
		return attributeDao.getProductTypeAttributes();
	}
	
	 public List<AttributeConfig> getAllAttributeDisplayTypes() {
		 return attributeDao.getAllAttributeDisplayTypes();
	 }

	public AttributeConfig getAttributeConfig(Long attributeConfigId) {
		return attributeDao.getAttributeConfig(attributeConfigId);
	}

	public AttributeDatatype getAttributeDataType(String attributeDataType) {
		return attributeDao.getAttributeDataType(attributeDataType);
	}

	public AttributeType getAttributeType(String attributeType) {
		return attributeDao.getAttributeType(attributeType);
	}

	public DepartmentAttribute getDepartmentAttribute(Long deptAttrId) {
		return attributeDao.getDepartmentAttribute(deptAttrId);
	}

	public ClassAttribute getClassificationAttribute(Long classificationAttId) {
		return attributeDao.getClassificationAttribute(classificationAttId);
	}

	public ProductTypeAttribute getProductTypeAttribute(Long productTypeAttrId) {
		return attributeDao.getProductTypeAttribute(productTypeAttrId);
	}

	public void remove(Attribute attribute) {
		attributeDao.remove(attribute);		
	}

	public List<Attribute> searchAttributes(String attributeName,
			Long classificationId, String blueMartiniName) {		
		return attributeDao.searchAttributes(attributeName, classificationId, blueMartiniName);
	}

	public List<Attribute> searchAttributes(String attributeName,
			Long classificationId, String blueMartiniName, String productTypeName) {		
		return attributeDao.searchAttributes(attributeName, classificationId, blueMartiniName, productTypeName);
	}

	public List<CarAttribute> getAllCarAttributeValuesForStatus(String  attributeValueProcessStatus) {
		return attributeDao.getAllCarAttributeValuesForStatus(attributeValueProcessStatus);
	}
	
	public boolean getAttributesByBlueMartiniNameAndType(String blueMartiniName,String attributeType) {
		return attributeDao.getAttributesByBlueMartiniNameAndType(blueMartiniName,attributeType);
	}

	public void saveAttributeTrackingList(List<AttributeChangeTracking> attributeChangeTrackingList){
		attributeDao.saveAttributeTrackingList(attributeChangeTrackingList);
	}
	
	public void saveAttributeTracking(AttributeChangeTracking attributeChangeTracking){
		attributeDao.saveAttributeTracking(attributeChangeTracking);
	}

	public void executeAttributeSynchronizationUsingStoreProcedure(){
		attributeDao.executeAttributeSynchronizationUsingStoreProcedure();
	}

	public List<String> getAttributeSynchRecordForBMIGeneration(int offset, int batchSize){
		return attributeDao.getAttributeSynchRecordForBMIGeneration(offset,batchSize);
	}

	public Long getTempAttributeSynchCount(){
		return attributeDao.getTempAttributeSynchCount();
	}

	@Override
	public List<AttributeChangeTracking> getUnprocessedEditedAttrValues() {
		List<AttributeChangeTracking> attValues = new ArrayList<AttributeChangeTracking>();
		attValues = attributeDao.getUnprocessedEditedAttrValues();
		
		return attValues;
	}
	public List<Long> getUnprocessedAttrIDs() {
		List<AttributeChangeTracking> attValues = getUnprocessedEditedAttrValues();
		List<Long> attrIDList = new ArrayList<Long>();
		for(AttributeChangeTracking atrChngTrack : attValues){
			attrIDList.add(atrChngTrack.getAttrId());
		}
		return attrIDList;
	}
	
	public AttributeChangeTracking getUnprocessedEditedAttrValueByOldValue(String newValue) {
		return attributeDao.getUnprocessedEditedAttrValueByOldValue( newValue) ;
	}
	
	public AttributeLookupValue save(AttributeLookupValue attributeLookupValue) {
        return attributeDao.save(attributeLookupValue);
    }

    @Override
    public List<AttributeBMMappingDTO> getAttributeBMMapping() {
        return attributeDao.getAttributeBMMapping();
    }
}
