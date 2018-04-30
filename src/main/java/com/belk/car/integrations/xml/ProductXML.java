package com.belk.car.integrations.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


@XStreamAlias(value = "product")
public class ProductXML
{
	@XStreamAlias(value = "type")
	@XStreamAsAttribute
	public String productType;
	@XStreamAlias(value = "name")
	public String productName;
	public VendorXML vendor;
	public Style style;
	public String brand;
	public DepartmentXML department;
	@XStreamAlias(value = "class")
	public ProductXML.Class productClass;


	@XStreamAlias(value = "class")
	public static class Class
	{
		@XStreamAsAttribute
		public long id;
		public String name;
		
		public Class() { super(); } 
		public Class(long id, String name)
		{
			this.id = id;
			this.name = name;
		}

		public String toString()
		{
			return new StringBuffer("Vendor[").append(", id:").append(id).append(", name:").append(name).append(" ]").toString();
		}
	}

	@XStreamAlias(value = "style")
	public static class Style
	{
		@XStreamAsAttribute
		String id;

		public Style() {}
		public Style(String id) { this.id = id; } 

		public String toString()
		{
			return new StringBuffer("Vendor[").append(", id:").append(id).append(" ]").toString();
		}
	}

	public String toString()
	{
		return new StringBuffer("ProductInfo[").append(" vendor:").append(vendor).append(", style:").append(style).append(", productName:").append(productName).append(", productType:").append(productType).append(", brand:").append(brand).append(", department:").append(department).append(", class:").append(productClass).append(" ]").toString();
	}

}