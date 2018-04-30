package com.belk.car.app.model.view;

import java.io.Serializable;

public class CarAttributeSummaryView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3306315718237191073L;

	private String attrName;
	private String attrValue;
	private Integer count;

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

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}
