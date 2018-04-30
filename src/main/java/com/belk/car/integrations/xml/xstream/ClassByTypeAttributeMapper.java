/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.integrations.xml.xstream;

import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;
/**
 *
 * @author amuaxg1
 */
public class ClassByTypeAttributeMapper extends MapperWrapper 
{	
	public ClassByTypeAttributeMapper( Mapper wrappedMapper ) { super(wrappedMapper); }
	
	private static final String DEFAULT_CLASS_ATT_NAME = "class";
	
	
}
