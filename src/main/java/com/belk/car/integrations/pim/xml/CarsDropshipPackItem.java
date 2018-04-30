package com.belk.car.integrations.pim.xml;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "carsDropshipPackItem", propOrder = {
 "pack",
 "idbPack",
 "packUpc",
 "retailPrice",
 "dropshipFlag",
 "differentiators"
})
public class CarsDropshipPackItem {

 protected String pack;
 @XmlElement(name = "idb_pack")
 protected String idbPack;
 @XmlElement(name = "pack_upc", required = true)
 protected String packUpc;
 @XmlElement(name = "retail_price", required = true)
 protected BigDecimal retailPrice;
 @XmlElement(name = "dropship_flag", required = true)
 protected String dropshipFlag;
 @XmlElement(required = true)
 protected CarsDifferentiators differentiators;

 /**
  * Gets the value of the pack property.
  * 
  * @return
  *     possible object is
  *     {@link String }
  *     
  */
 public String getPack() {
     return pack;
 }

 /**
  * Sets the value of the pack property.
  * 
  * @param value
  *     allowed object is
  *     {@link String }
  *     
  */
 public void setPack(String value) {
     this.pack = value;
 }

 /**
  * Gets the value of the idbPack property.
  * 
  * @return
  *     possible object is
  *     {@link String }
  *     
  */
 public String getIdbPack() {
     return idbPack;
 }

 /**
  * Sets the value of the idbPack property.
  * 
  * @param value
  *     allowed object is
  *     {@link String }
  *     
  */
 public void setIdbPack(String value) {
     this.idbPack = value;
 }

 /**
  * Gets the value of the packUpc property.
  * 
  * @return
  *     possible object is
  *     {@link String }
  *     
  */
 public String getPackUpc() {
     return packUpc;
 }

 /**
  * Sets the value of the packUpc property.
  * 
  * @param value
  *     allowed object is
  *     {@link String }
  *     
  */
 public void setPackUpc(String value) {
     this.packUpc = value;
 }

 /**
  * Gets the value of the retailPrice property.
  * 
  * @return
  *     possible object is
  *     {@link BigDecimal }
  *     
  */
 public BigDecimal getRetailPrice() {
     return retailPrice;
 }

 /**
  * Sets the value of the retailPrice property.
  * 
  * @param value
  *     allowed object is
  *     {@link BigDecimal }
  *     
  */
 public void setRetailPrice(BigDecimal value) {
     this.retailPrice = value;
 }

 /**
  * Gets the value of the dropshipFlag property.
  * 
  * @return
  *     possible object is
  *     {@link String }
  *     
  */
 public String getDropshipFlag() {
     return dropshipFlag;
 }

 /**
  * Sets the value of the dropshipFlag property.
  * 
  * @param value
  *     allowed object is
  *     {@link String }
  *     
  */
 public void setDropshipFlag(String value) {
     this.dropshipFlag = value;
 }

 /**
  * Gets the value of the differentiators property.
  * 
  * @return
  *     possible object is
  *     {@link CarsDifferentiators }
  *     
  */
 public CarsDifferentiators getDifferentiators() {
     return differentiators;
 }

 /**
  * Sets the value of the differentiators property.
  * 
  * @param value
  *     allowed object is
  *     {@link CarsDifferentiators }
  *     
  */
 public void setDifferentiators(CarsDifferentiators value) {
     this.differentiators = value;
 }

}
