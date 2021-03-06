package com.belk.car.integrations.rrd.xml.vendorImage;

//
//This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
//See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
//Any modifications to this file will be lost upon recompilation of the source schema. 
//Generated on: 2013.01.24 at 10:19:01 AM IST 
//



import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
  "image"
})
@XmlRootElement(name = "carsMessage")
public class RRDImageValidationXML {

  @XmlElement(required = true)
  protected List<RRDImageValidationXML.RRDImageXML> image;
  @XmlAttribute
  protected String from;
  @XmlAttribute
  protected String to;
  @XmlAttribute
  protected String type;

  public List<RRDImageValidationXML.RRDImageXML> getImage() {
      if (image == null) {
          image = new ArrayList<RRDImageValidationXML.RRDImageXML>();
      }
      return this.image;
  }

  /**
   * Gets the value of the from property.
   * 
   * @return
   *     possible object is
   *     {@link String }
   *     
   */
  public String getFrom() {
      return from;
  }

  /**
   * Sets the value of the from property.
   * 
   * @param value
   *     allowed object is
   *     {@link String }
   *     
   */
  public void setFrom(String value) {
      this.from = value;
  }

  /**
   * Gets the value of the to property.
   * 
   * @return
   *     possible object is
   *     {@link String }
   *     
   */
  public String getTo() {
      return to;
  }

  /**
   * Sets the value of the to property.
   * 
   * @param value
   *     allowed object is
   *     {@link String }
   *     
   */
  public void setTo(String value) {
      this.to = value;
  }

  /**
   * Gets the value of the type property.
   * 
   * @return
   *     possible object is
   *     {@link String }
   *     
   */
  public String getType() {
      return type;
  }

  /**
   * Sets the value of the type property.
   * 
   * @param value
   *     allowed object is
   *     {@link String }
   *     
   */
  public void setType(String value) {
      this.type = value;
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = {
      "check",
      "reasons","reviewComments"
  })
  public static class RRDImageXML {

      @XmlElement(required = true)
      protected RRDImageValidationXML.RRDImageXML.Check check;
      @XmlElement(required = true)
      protected RRDImageValidationXML.RRDImageXML.Reasons reasons;
      @XmlElement(required = true)
      protected RRDImageValidationXML.RRDImageXML.ReviewComments reviewComments;
      @XmlAttribute(name = "image_id")
      protected Integer imageId;

      /**
       * Gets the value of the check property.
       * 
       * @return
       *     possible object is
       *     {@link CarsMessage.Image.Check }
       *     
       */
      public RRDImageValidationXML.RRDImageXML.Check getCheck() {
          return check;
      }

      /**
       * Sets the value of the check property.
       * 
       * @param value
       *     allowed object is
       *     {@link CarsMessage.Image.Check }
       *     
       */
      public void setCheck(RRDImageValidationXML.RRDImageXML.Check value) {
          this.check = value;
      }

      /**
       * Gets the value of the reasons property.
       * 
       * @return
       *     possible object is
       *     {@link CarsMessage.Image.Reasons }
       *     
       */
      public RRDImageValidationXML.RRDImageXML.Reasons getReasons() {
          return reasons;
      }

      /**
       * Sets the value of the reasons property.
       * 
       * @param value
       *     allowed object is
       *     {@link CarsMessage.Image.Reasons }
       *     
       */
      public void setReasons(RRDImageValidationXML.RRDImageXML.Reasons value) {
          this.reasons=value;
      }
      
      public RRDImageValidationXML.RRDImageXML.ReviewComments getReviewComments() {
          return reviewComments;
      }
     
      public void setReviewComments(RRDImageValidationXML.RRDImageXML.ReviewComments value) {
          this.reviewComments = value;
      }
      
      

      /**
       * Gets the value of the imageId property.
       * 
       * @return
       *     possible object is
       *     {@link Integer }
       *     
       */
      public Integer getImageId() {
          return imageId;
      }

      /**
       * Sets the value of the imageId property.
       * 
       * @param value
       *     allowed object is
       *     {@link Integer }
       *     
       */
      public void setImageId(Integer value) {
          this.imageId = value;
      }


      @XmlAccessorType(XmlAccessType.FIELD)
      @XmlType(name = "")
      public static class Check {

          @XmlAttribute
          protected String type;
          
          @XmlValue
          protected String value;

          /**
           * Gets the value of the type property.
           * 
           * @return
           *     possible object is
           *     {@link String }
           *     
           */
          public String getType() {
              return type;
          }

          /**
           * Sets the value of the type property.
           * 
           * @param value
           *     allowed object is
           *     {@link String }
           *     
           */
          public void setType(String value) {
              this.type = value;
          }
          
          public String getValue() {
              return value;
          }
          
          public void setValue(String value) {
              this.value = value;
          }

      }

      @XmlAccessorType(XmlAccessType.FIELD)
      @XmlType(name = "", propOrder = {
          "reason"
      })
      public static class Reasons {

          @XmlElement(required = true)
           protected List<String> reason;

          /**
           * Gets the value of the reason property.
           * 
           * @return
           *     possible object is
           *     {@link String }
           *     
           */
          public List<String> getReason() {
              return reason;
          }

          /**
           * Sets the value of the reason property.
           * 
           * @param value
           *     allowed object is
           *     {@link String }
           *     
           */
          public void setReason(List<String> value) {
              this.reason = value;
          }

      }
      @XmlAccessorType(XmlAccessType.FIELD)
      @XmlType(name = "", propOrder = {
          "comment"
      })
      public static class ReviewComments {

          @XmlElement(required = true)
          protected List<String> comment;

          /**
           * Gets the value of the reason property.
           * 
           * @return
           *     possible object is
           *     {@link String }
           *     
           */
          public List<String> getComment() {
              return comment;
          }

          /**
           * Sets the value of the reason property.
           * 
           * @param value
           *     allowed object is
           *     {@link String }
           *     
           */
          public void setComment(List<String> value) {
              this.comment = value;
          }

      }


  }

}
