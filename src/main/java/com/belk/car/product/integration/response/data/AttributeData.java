package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.namespace.QName;

/**
 * 
 * POJO to hold the attribute name and value for all the specific attribute sections.
 * Currently it cannot be mapped as part of the xml as the names of the attribute
 * are dynamic in nature.
 *
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
public class AttributeData {
	
	@XmlTransient
	private QName qName;
	
	private String attributeName;
	
	private String attributeValue;

	public QName getQName() {
		return qName;
	}

	public void setQName(QName qName) {
		this.qName = qName;
	}

	@XmlElement(name="*")
	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
	
	@Override
	public boolean equals(Object obj) {
	    // TODO Auto-generated method stub
	    if(this==null && obj==null){
	        return true;
	    }
	    
	    if(obj == null){
	        return false;
	    }
	    if(obj instanceof AttributeData)
	    {
	        AttributeData temp = (AttributeData) obj;
	        if(this.attributeName.equals(temp.attributeName)){
	            return true;
	        }
	    }
	    return false;
	}

	@Override
	public int hashCode() {
	    // TODO Auto-generated method stub
        System.out.println("this.attributeName.hashCode() in hasCOde method  "+this.attributeName.hashCode());
	    return (this.attributeName.hashCode());
	}
	

}
