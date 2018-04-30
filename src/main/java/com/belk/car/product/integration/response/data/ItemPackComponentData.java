package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Item_Complex_Pack_Spec")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ItemPackComponentData {
	
	private String componentIndicator;
	
	private String componentId;
	
	private Integer componentQuantity;
	
	private String componentStyle;
	
	private String componentSize;
	
	private String componentColor;

	@XmlAttribute(name = "occ")
	public String getComponentIndicator() {
		return componentIndicator;
	}

	public void setComponentIndicator(String componentIndicator) {
		this.componentIndicator = componentIndicator;
	}

	@XmlElement(name = "Id")
	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	@XmlElement(name = "Quantity")
	public Integer getComponentQuantity() {
		return componentQuantity;
	}

	public void setComponentQuantity(Integer componentQuantity) {
		this.componentQuantity = componentQuantity;
	}

	@XmlElement(name = "Style")
	public String getComponentStyle() {
		return componentStyle;
	}

	public void setComponentStyle(String componentStyle) {
		this.componentStyle = componentStyle;
	}

	@XmlElement(name = "Size")
	public String getComponentSize() {
		return componentSize;
	}

	public void setComponentSize(String componentSize) {
		this.componentSize = componentSize;
	}

	@XmlElement(name = "Color")
	public String getComponentColor() {
		return componentColor;
	}

	public void setComponentColor(String componentColor) {
		this.componentColor = componentColor;
	}
}
