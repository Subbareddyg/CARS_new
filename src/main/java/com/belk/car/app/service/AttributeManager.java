package com.belk.car.app.service;

import java.util.List;

import org.appfuse.service.UniversalManager;

import com.belk.car.app.dto.AttributeBMMappingDTO;
import com.belk.car.app.exceptions.AttributeValueExistsException;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeConfig;
import com.belk.car.app.model.AttributeDatatype;
import com.belk.car.app.model.AttributeLookupValue;
import com.belk.car.app.model.AttributeType;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.ClassAttribute;
import com.belk.car.app.model.DepartmentAttribute;
import com.belk.car.app.model.ProductTypeAttribute;
import com.belk.car.app.model.AttributeChangeTracking;

public interface AttributeManager extends UniversalManager{
	
	  List<Attribute> getAllAttributes();
	  
	  List<DepartmentAttribute> getDepartmentAttributes();
	  
	  List<ClassAttribute> getClassAttributes();
	  
	  List<ProductTypeAttribute> getProductTypeAttributes();

	  Attribute getAttribute(Long attributeId);

	  Attribute getAttribute(String name);
	  
	  Attribute save(Attribute attribute) throws AttributeValueExistsException;
	  
	  void remove(Attribute attribute);
	  
	  List<AttributeDatatype> getAttributeDataTypes();

	  List<AttributeType> getAttributeTypes();
	  
	  AttributeConfig save(AttributeConfig attributeConfig);
	  
	  List<AttributeConfig> getAllAttributeDisplayTypes();
	  
	  List<CarAttribute>getAllCarAttributeValuesForStatus(String attributeValueProcessStatus);
	  
	  AttributeConfig getAttributeConfig(Long attributeConfigId);
	  
	  AttributeDatatype getAttributeDataType(String attributeDataType);
	  
	  AttributeType getAttributeType(String attributeType);
	  
	  DepartmentAttribute getDepartmentAttribute(Long deptAttrId);
	  
	  ProductTypeAttribute getProductTypeAttribute(Long productTypeAttrId);
	  
	  ClassAttribute getClassificationAttribute(Long classificationAttId);
	  
	  List<Attribute>searchAttributes(String attributeName, Long classificationId, String blueMartiniName);

	  List<Attribute>searchAttributes(String attributeName, Long classificationId, String blueMartiniName, String productTypeName);
	  
	  boolean getAttributesByBlueMartiniNameAndType(String blueMartiniName,String attributeType);

	  void saveAttributeTrackingList(List<AttributeChangeTracking> attributeChangeTrackingList);

	  void saveAttributeTracking(AttributeChangeTracking attributeChangeTracking);

	  void executeAttributeSynchronizationUsingStoreProcedure();

	  List<String> getAttributeSynchRecordForBMIGeneration(int offset, int batchSize);

	  Long getTempAttributeSynchCount();
	  List<AttributeChangeTracking> getUnprocessedEditedAttrValues();
	  List<Long> getUnprocessedAttrIDs();
	  public AttributeChangeTracking getUnprocessedEditedAttrValueByOldValue(String newValue);
	  
	  AttributeLookupValue save(AttributeLookupValue attributeLookupValue);
	  
	  public List<AttributeBMMappingDTO> getAttributeBMMapping();
}
