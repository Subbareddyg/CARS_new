package com.belk.car.app.dto;

import java.io.Serializable;



public class vendorStylePIMAttributeDTO implements Serializable{
	
	private static final long serialVersionUID = 931963172840907268L;
	Long PIMAttributeId;
	String pimAttributeName;
	String value;
	
	
	
	public Long getPIMAttributeId() {
		return PIMAttributeId;
	}
	public void setPIMAttributeId(Long pIMAttributeId) {
		PIMAttributeId = pIMAttributeId;
	}
	public String getPimAttributeName() {
		return pimAttributeName;
	}
	public void setPimAttributeName(String pimAttributeName) {
		this.pimAttributeName = pimAttributeName;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
