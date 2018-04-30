package com.belk.car.integrations.pim.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "carsDifferentiators", propOrder = {
 "colorCode",
 "colorName",
 "sizeCode",
 "sizeName"
})
public class CarsDifferentiators {

 @XmlElement(name = "color_code", required = true)
 protected String colorCode;
 @XmlElement(name = "color_name", required = true)
 protected String colorName;
 @XmlElement(name = "size_code", required = true)
 protected String sizeCode;
 @XmlElement(name = "size_name", required = true)
 protected String sizeName;

 /**
  * Gets the value of the colorCode property.
  * 
  * @return
  *     possible object is
  *     {@link String }
  *     
  */
 public String getColorCode() {
     return colorCode;
 }

 /**
  * Sets the value of the colorCode property.
  * 
  * @param value
  *     allowed object is
  *     {@link String }
  *     
  */
 public void setColorCode(String value) {
     this.colorCode = value;
 }

 /**
  * Gets the value of the colorName property.
  * 
  * @return
  *     possible object is
  *     {@link String }
  *     
  */
 public String getColorName() {
     return colorName;
 }

 /**
  * Sets the value of the colorName property.
  * 
  * @param value
  *     allowed object is
  *     {@link String }
  *     
  */
 public void setColorName(String value) {
     this.colorName = value;
 }

 /**
  * Gets the value of the sizeCode property.
  * 
  * @return
  *     possible object is
  *     {@link String }
  *     
  */
 public String getSizeCode() {
     return sizeCode;
 }

 /**
  * Sets the value of the sizeCode property.
  * 
  * @param value
  *     allowed object is
  *     {@link String }
  *     
  */
 public void setSizeCode(String value) {
     this.sizeCode = value;
 }

 /**
  * Gets the value of the sizeName property.
  * 
  * @return
  *     possible object is
  *     {@link String }
  *     
  */
 public String getSizeName() {
     return sizeName;
 }

 /**
  * Sets the value of the sizeName property.
  * 
  * @param value
  *     allowed object is
  *     {@link String }
  *     
  */
 public void setSizeName(String value) {
     this.sizeName = value;
 }

}
