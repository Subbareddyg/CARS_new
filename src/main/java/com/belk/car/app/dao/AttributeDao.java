package com.belk.car.app.dao;

import java.util.List;

import org.appfuse.dao.UniversalDao;

import com.belk.car.app.dto.AttributeBMMappingDTO;
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

public interface AttributeDao extends CachedQueryDao {

	List<Attribute> getAllAttributes();

	List<DepartmentAttribute> getDepartmentAttributes();

	List<ClassAttribute> getClassAttributes();

	List<ProductTypeAttribute> getProductTypeAttributes();

	Attribute getAttribute(Long attrId);
	
	Attribute getAttribute(String name);

	Attribute save(Attribute attribute);
	
	void remove(Attribute attribute);

	List<AttributeDatatype> getAttributeDataTypes();

	List<AttributeType> getAttributeTypes();

	AttributeConfig save(AttributeConfig config);

	List<AttributeConfig> getAllAttributeDisplayTypes();

	AttributeConfig getAttributeConfig(Long attributeConfigId);

	AttributeDatatype getAttributeDataType(String attributeDataType);

	AttributeType getAttributeType(String attributeType);
	
	DepartmentAttribute getDepartmentAttribute(Long deptAttrId);
	
	ProductTypeAttribute getProductTypeAttribute(Long productTypeAttrId);
	  
	ClassAttribute getClassificationAttribute(Long classificationAttId);
	
	List<Attribute >searchAttributes(String attributeName, Long classificationId, String blueMartiniName);
	
	List<Attribute> searchAttributes(String attributeName, Long classificationId, String blueMartiniName, String productTypeName) ;

	List<CarAttribute>getAllCarAttributeValuesForStatus(String attributeValueProcessStatus);
	
	boolean getAttributesByBlueMartiniNameAndType(String blueMartiniName,String attributeType);

	void saveAttributeTrackingList(List<AttributeChangeTracking> attributeChangeTrackingList);

	void saveAttributeTracking(AttributeChangeTracking attributeChangeTracking);

	void executeAttributeSynchronizationUsingStoreProcedure();

	List<String> getAttributeSynchRecordForBMIGeneration(int offset, int batchSize);

	Long getTempAttributeSynchCount();
	List<AttributeChangeTracking> getUnprocessedEditedAttrValues();
	public AttributeChangeTracking getUnprocessedEditedAttrValueByOldValue(String newValue) ;
	
	AttributeLookupValue save(AttributeLookupValue attributeLookupValue);

    public List<AttributeBMMappingDTO> getAttributeBMMapping();
}
