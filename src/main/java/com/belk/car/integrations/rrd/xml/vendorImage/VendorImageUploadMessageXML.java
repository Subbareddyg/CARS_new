package com.belk.car.integrations.rrd.xml.vendorImage;

//
//This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
//See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
//Any modifications to this file will be lost upon recompilation of the source schema. 
//Generated on: 2013.01.03 at 02:06:16 PM EST 
//

/**
 * @author AFUSY12
 * JAXB annotation file for vendor image XML file
 * 
 */
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "carsMessage", propOrder = {
 "vendorImage"
})
@XmlRootElement(name = "carsMessage")
public class VendorImageUploadMessageXML {

 @XmlElement(required = true)
 protected List<VendorImageUploadMessageXML.VendorImageSummeryXML> vendorImage;
 @XmlAttribute
 protected String from;
 @XmlAttribute
 protected String to;
 @XmlAttribute
 protected String type;


 public List<VendorImageUploadMessageXML.VendorImageSummeryXML> getVendorImage() {
     if (vendorImage == null) {
         vendorImage = new ArrayList<VendorImageUploadMessageXML.VendorImageSummeryXML>();
     }
     return this.vendorImage;
 }

 public String getFrom() {
     return from;
 }  
 public void setFrom(String value) {
     this.from = value;
 }

 public String getTo() {
     return to;
 }   
 public void setTo(String value) {
     this.to = value;
 }

 public String getType() {
     return type;
 }   
 public void setType(String value) {
     this.type = value;
 }



 @XmlAccessorType(XmlAccessType.FIELD)
 @XmlType(name = "VendorImage", propOrder = {
     "name",
     "uploadedByUser",
     "status"
 })
 public static class VendorImageSummeryXML {

     @XmlElement(required = true)
     protected String name;
     @XmlElement(required = true)
     protected String uploadedByUser;
     @XmlElement(required = true)
     protected String status;
     @XmlAttribute(name = "image_id")
     protected Long imageId;

     
     public String getName() {
         return name;
     } 
     public void setName(String value) {
         this.name = value;
     }

     public String getUploadedByUser() {
         return uploadedByUser;
     }  
     public void setUploadedByUser(String value) {
         this.uploadedByUser = value;
     }

    
     public String getStatus() {
         return status;
     }       
     public void setStatus(String value) {
         this.status = value;
     }

    
     public Long getImageId() {
         return imageId;
     }
     public void setImageId(Long value) {
         this.imageId = value;
     }

 }

}
