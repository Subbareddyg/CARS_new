/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.belk.car.integrations.xml.xstream;

import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.converters.reflection.AbstractReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.mapper.Mapper;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author amuaxg1
 */
public class SubclassResolutionByAttributeConverter extends AbstractReflectionConverter
{
	public SubclassResolutionByAttributeConverter(Mapper mapper, ReflectionProvider reflectionProvider)
	{
		super(mapper, reflectionProvider);
	}
	
	public boolean canConvert(Class type)
	{
		return (type2DiffAttrName.keySet().contains(type) && resolutionMap.keySet().contains(type));
	}

	Map<Class, String> type2DiffAttrName = new HashMap<Class, String>();
	public void putDifferentiatingAttribute( Class generalType, String classTypeDifferentiatingAttributeName ) {
		type2DiffAttrName.put(generalType,classTypeDifferentiatingAttributeName);
	}
	public String getDifferentiatingAttribute(Class type) {
		return type2DiffAttrName.get(type);
	}
	
	Map<Class, Map<String, Class>> resolutionMap = new HashMap<Class, Map<String, Class>>();
	public void addSubclassResolution(Class generalType, String differentiatingAttributeValue, Class specificType) {
		Map<String, Class> innerMap = resolutionMap.get(generalType);
		if (innerMap == null) { innerMap = new HashMap<String, Class>(); resolutionMap.put(generalType, innerMap); }
		innerMap.put(differentiatingAttributeValue, specificType);
	}
	protected Class resolveSubclass(Class generalType, String differentiatingAttributeValue) {
		Map<String, Class> innerMap = resolutionMap.get(generalType);
		return (innerMap != null) ? innerMap.get(differentiatingAttributeValue) : null;
	}

	@Override
	protected Object instantiateNewInstance(HierarchicalStreamReader reader, UnmarshallingContext context)
	{
		Object currentObject = context.currentObject();
		if (currentObject != null) {return currentObject;}
		
		String readResolveValue = reader.getAttribute(mapper.aliasForAttribute("resolves-to"));
		if (readResolveValue != null) {
			return reflectionProvider.newInstance(mapper.realClass(readResolveValue));
		}
		else {
			return reflectionProvider.newInstance(findType(reader, context));
		}
	}
	
	protected Class findType( HierarchicalStreamReader reader, UnmarshallingContext context ) 
	{
		Class requiredType = context.getRequiredType();
		String differentiatingAttribute = getDifferentiatingAttribute(requiredType);
		String differentiatingAttributeValue = reader.getAttribute(differentiatingAttribute);
		
		Class resolvedType = resolveSubclass(requiredType, differentiatingAttributeValue);
		return resolvedType != null ? resolvedType : requiredType;
	}
}
