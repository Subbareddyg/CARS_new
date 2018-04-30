/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.integrations.xml.xstream;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import org.apache.commons.lang.StringUtils;
/**
 *
 * @author amuaxg1
 */
public class CarsEnumSingleValueConverter extends AbstractSingleValueConverter 
{
	private final Class enumType;
	
	public CarsEnumSingleValueConverter() {
		enumType = null;
	}

	public CarsEnumSingleValueConverter(Class type) {
		if (!Enum.class.isAssignableFrom(type) && type != Enum.class) {
			throw new IllegalArgumentException("Converter can only handle defined enums");
		}
		enumType = type;
	}

	public boolean canConvert(Class type) {
		if (enumType == null) return (Enum.class.isAssignableFrom(type) || type == Enum.class);
		else return enumType.isAssignableFrom(type);
	}

	@Override
	public String toString(Object obj) {
		return obj == null ? null : StringUtils.uncapitalize(obj.toString());
	}

	public Object fromString(String str) {
		return Enum.valueOf(enumType, StringUtils.capitalize(str));
	}
}
