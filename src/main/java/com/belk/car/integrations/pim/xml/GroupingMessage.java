package com.belk.car.integrations.pim.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
                "type",
		"carsGroupDtl"
})

@XmlRootElement(name = "groupMessage")
public class GroupingMessage {

	@XmlElement(required = true)
	protected String type;

	@XmlElement(name = "groupDtl", required = true)
	protected CarsGroupingDtl carsGroupDtl;


	public String getType() {
		return type;
	}


	public void setType(String value) {
		this.type = value;
	}

	public CarsGroupingDtl getCarsGroupDtl() {
		return this.carsGroupDtl;
	}

        
}
