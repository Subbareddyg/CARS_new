package com.belk.car.app.dto;

import java.io.Serializable;

public class AttributeDTO implements Serializable {

	private static final long serialVersionUID = 43191238423907268L;
	Long attributeId;
	String attributeName;
	String value;

	public AttributeDTO() {

	}

	public Long getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(Long attributeId) {
		this.attributeId = attributeId;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
