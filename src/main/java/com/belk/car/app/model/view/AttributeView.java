package com.belk.car.app.model.view;

import java.io.Serializable;

public class AttributeView implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1841980841463717597L;

	private String attrName;
	private String attrValue;

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

}
