/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.belk.car.integrations.xml;

import com.belk.car.integrations.xml.xstream.CarsEnumSingleValueConverter;
import com.belk.car.integrations.xml.xstream.CarsEnumTypeConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;


/**
 *
 * @author amuaxg1
 */
@XStreamAlias("carsMessage")
public class CarsMessageXML
{
	@XStreamAsAttribute
	protected String from;
	
	@XStreamAsAttribute
	protected String to;
	
	@XStreamConverter(CarsEnumTypeConverter.class)
	@XStreamAsAttribute
	protected Type type;

	public static enum Type
	{
		Update, PhotoRequests
	}


	
	
}
