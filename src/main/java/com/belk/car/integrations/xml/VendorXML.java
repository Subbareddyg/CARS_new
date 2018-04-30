package com.belk.car.integrations.xml;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


public class VendorXML
{
	@XStreamAsAttribute
	public String id;
	public String name;


	public VendorXML()
	{
		super();
	}


	public VendorXML(String id, String name)
	{
		super();
		this.id = id;
		this.name = name;
	}


	public String toString()
	{
		return new StringBuffer("Vendor[").append(", id:").append(id).append(", name:").append(name).append(" ]").toString();
	}
}
