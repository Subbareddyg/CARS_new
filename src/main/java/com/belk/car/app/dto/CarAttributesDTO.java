package com.belk.car.app.dto;

import java.io.Serializable;
import java.util.Comparator;


public class CarAttributesDTO implements Serializable, Comparable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5306241791745332452L;
	private Long attributeKey;
	private String attributeName;
	private String masterAttribute;

	public CarAttributesDTO(){}

	public int compareTo(Object arg0) {
		String name = ((CarAttributesDTO) arg0).getAttributeName();
		return this.attributeName.compareToIgnoreCase(name);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof CarAttributesDTO))
			return false;
		CarAttributesDTO other = (CarAttributesDTO) obj;
		if (attributeKey == null) {
			if (other.attributeKey != null)
				return false;
		} else if (!attributeKey.equals(other.attributeKey))
			return false;
		if (masterAttribute == null) {
			if (other.masterAttribute != null)
				return false;
		} else if (!masterAttribute.equals(other.masterAttribute))
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attributeKey == null) ? 0 : attributeKey.hashCode());
		result = prime * result
				+ ((masterAttribute == null) ? 0 : masterAttribute.hashCode());
		return result;
	}
	/**
	 * @return the attributeKey
	 */
	public Long getAttributeKey() {
		return attributeKey;
	}

	/**
	 * @param attributeKey the attributeKey to set
	 */
	public void setAttributeKey(Long attributeKey) {
		this.attributeKey = attributeKey;
	}

	/**
	 * @return the attributeName
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * @param attributeName the attributeName to set
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	/**
	 * @return the masterAttribute
	 */
	public String getMasterAttribute() {
		return masterAttribute;
	}

	/**
	 * @param masterAttribute the masterAttribute to set
	 */
	public void setMasterAttribute(String masterAttribute) {
		this.masterAttribute = masterAttribute;
	}
}
