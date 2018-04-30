package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the specific item_stylecolor_spec in the style color request.
 * Current Path in xml response: /getItemRespone/Item_Catalog/pim_entry/entry/Item_StyleColor_Spec.
 */
@XmlRootElement(name = "Item_StyleColor_Spec")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ItemStyleColorSpecData {
	
	private Long styleId;
	
	private String colorCode;

	@XmlElement(name = "Style_Id")
	public Long getStyleId() {
		return styleId;
	}

	public void setStyleId(Long styleId) {
		this.styleId = styleId;
	}

	@XmlElement(name = "Color_Code")
	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	
	

}
