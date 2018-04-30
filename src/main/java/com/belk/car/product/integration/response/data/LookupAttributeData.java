package com.belk.car.product.integration.response.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "zIPH_Lkp_Red")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class LookupAttributeData {
	
	private List<AttributeLookupData> attributeLookupDatas;

	@XmlElement(name = "lookups")
	public List<AttributeLookupData> getAttributeLookupDatas() {
		return attributeLookupDatas;
	}

	public void setAttributeLookupDatas(
			List<AttributeLookupData> attributeLookupDatas) {
		this.attributeLookupDatas = attributeLookupDatas;
	}
	
	

}
