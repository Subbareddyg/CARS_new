/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.belk.car.integrations.xml.xstream;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.apache.commons.lang.StringUtils;


/**
 *
 * @author amuaxg1
 */
public class CarsEnumTypeConverter implements Converter
{
//	private final Class enumType;


	public CarsEnumTypeConverter() {}
	
//	public CarsEnumTypeConverter(Class type)
//	{
//		if (!canConvert(type)) {
//			throw new IllegalArgumentException("Converter can only handle defined enums");
//		}
//		enumType = type;
//	}


	public boolean canConvert(Class type)
	{
		return type.isEnum() || Enum.class.isAssignableFrom(type);
	}


	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context)
	{
		String marshalledValue;
		marshalledValue = source.toString();
		marshalledValue = StringUtils.uncapitalize(marshalledValue);
		writer.setValue(marshalledValue);
	}


	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context)
	{
		Class type = context.getRequiredType();
		// TODO: There's no test case for polymorphic enums.
		if (type.getSuperclass() != Enum.class) {
			type = type.getSuperclass(); // polymorphic enums
		}
		return Enum.valueOf(type, StringUtils.capitalize(reader.getValue()));
	}
}

