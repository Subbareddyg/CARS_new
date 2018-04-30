package com.belk.car.app.dto;

import java.util.ArrayList;
import java.util.List;

import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.CarAttribute;

public class MissingAttributeValue {

	private Attribute attribute ;
	private String value ;
	private String oldValue;
	private String carAttributeId;
	private List<CarAttribute> carAttributes = new ArrayList<CarAttribute>();
	
	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public List<CarAttribute> getCarAttributes() {
		return carAttributes;
	}
	
	public void setCarAttributes(List<CarAttribute> carAttributes) {
		this.carAttributes = carAttributes;
	}
	
	public String getCarAttributeIds() {
		StringBuffer strB = new StringBuffer() ;
		if (this.carAttributes != null && !this.carAttributes.isEmpty()) {
			int listSize=carAttributes.size();
			int i=0;
			for(CarAttribute carAttribute: carAttributes) {
				i++;
				strB.append(carAttribute.getCarAttrId());
				if(i<listSize)
					strB.append(","); 
					
			}
		}
		return strB.toString() ;
	}

	/**
	 * @return the carAttributeId
	 */
	public String getCarAttributeId() {
		return carAttributeId;
	}

	/**
	 * @param carAttributeId the carAttributeId to set
	 */
	public void setCarAttributeId(String carAttributeId) {
		this.carAttributeId = carAttributeId;
	}

	/**
	 * @return the oldValue
	 */
	public String getOldValue() {
		return oldValue;
	}

	/**
	 * @param oldValue the oldValue to set
	 */
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
}
