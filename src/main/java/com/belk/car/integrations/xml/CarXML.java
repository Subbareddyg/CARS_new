package com.belk.car.integrations.xml;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


public class CarXML
{
	@XStreamAsAttribute
	public long id;


	public CarXML(long id)
	{
		super();
		this.id = id;
	}
}
