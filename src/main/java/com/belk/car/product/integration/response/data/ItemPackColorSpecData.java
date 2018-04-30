package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Item_PackColor_Spec")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ItemPackColorSpecData {
	
	private Long packId;
	
	private String colorCode;

	@XmlElement(name = "Pack_Id")
	public Long getPackId() {
		return packId;
	}

	public void setPackId(Long packId) {
		this.packId = packId;
	}

	@XmlElement(name = "Color_Code")
	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	
	

}
