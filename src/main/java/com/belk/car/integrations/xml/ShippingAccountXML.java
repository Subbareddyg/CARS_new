package com.belk.car.integrations.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToStringConverter;

@XStreamAlias(value = "shippingAccount")
public class ShippingAccountXML
{
	@XStreamAsAttribute
	public String id;

	@XStreamAsAttribute
	@XStreamAlias(value = "carrier")
	public String carrierName;

	public ShippingAccountXML() {}
	public ShippingAccountXML(String id, String carrierName) { this.id = id; this.carrierName = carrierName; }

	@Override
	public String toString()
	{
		return new StringBuffer("Vendor[").append(", id:").append(id).append(", carrierName:").append(carrierName).append(" ]").toString();
	}
}
