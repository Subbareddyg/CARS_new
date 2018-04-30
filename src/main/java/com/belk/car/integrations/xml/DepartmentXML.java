package com.belk.car.integrations.xml;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


public class DepartmentXML
{
	@XStreamAsAttribute
	public String code;   //Modified by Santosh
	public String name;


	public DepartmentXML()
	{
		super();
	}


	public DepartmentXML(String code, String name)  //modified by santosh
	{
		super();
		this.code = code;						//modified by santosh
		this.name = name;
	}


	public String toString()
	{
		return new StringBuffer("Vendor[").append(", code:").append(code).append(", name:").append(name).append(" ]").toString(); //modified by santosh
	}
}
