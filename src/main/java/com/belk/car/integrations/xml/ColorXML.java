package com.belk.car.integrations.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


@XStreamAlias(value = "color")
public class ColorXML
{
	public ColorXML()
	{
		super();
	}


	public ColorXML(String code, String name)
	{
		super();
		this.code = code;
		this.name = name;
	}
	@XStreamAsAttribute
	public String code;
	public String name;
}
