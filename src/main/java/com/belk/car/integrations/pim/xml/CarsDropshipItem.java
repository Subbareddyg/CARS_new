package com.belk.car.integrations.pim.xml;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "carsDropshipItem", propOrder = {
 "sku",
 "idbId",
 "vendorUpc",
 "retailPrice",
 "dropshipFlag",
 "differentiators"
})
public class CarsDropshipItem {

 protected String sku;
 @XmlElement(name = "idb_id")
 protected String idbId;
 @XmlElement(name = "vendor_upc", required = true)
 protected String vendorUpc;
 @XmlElement(name = "retail_price", required = true)
 protected BigDecimal retailPrice;
 @XmlElement(name = "dropship_flag", required = true)
 protected String dropshipFlag;
 @XmlElement(required = true)
 protected CarsDifferentiators differentiators;

 /**
  * Gets the value of the sku property.
  * 
  * @return
  *     possible object is
  *     {@link String }
  *     
  */
 public String getSku() {
     return sku;
 }

 /**
  * Sets the value of the sku property.
  * 
  * @param value
  *     allowed object is
  *     {@link String }
  *     
  */
 public void setSku(String value) {
     this.sku = value;
 }

 /**
  * Gets the value of the idbId property.
  * 
  * @return
  *     possible object is
  *     {@link String }
  *     
  */
 public String getIdbId() {
     return idbId;
 }

 /**
  * Sets the value of the idbId property.
  * 
  * @param value
  *     allowed object is
  *     {@link String }
  *     
  */
 public void setIdbId(String value) {
     this.idbId = value;
 }

 /**
  * Gets the value of the vendorUpc property.
  * 
  * @return
  *     possible object is
  *     {@link String }
  *     
  */
 public String getVendorUpc() {
     return vendorUpc;
 }

 /**
  * Sets the value of the vendorUpc property.
  * 
  * @param value
  *     allowed object is
  *     {@link String }
  *     
  */
 public void setVendorUpc(String value) {
     this.vendorUpc = value;
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
