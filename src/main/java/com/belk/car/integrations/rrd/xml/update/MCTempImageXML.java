package com.belk.car.integrations.rrd.xml.update;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "tempSamplePhoto"
})
@XmlRootElement(name = "carsMessage")
public class MCTempImageXML {

    @XmlElement(required = true)
    protected List<MCTempImageXML.TempSamplePhoto> tempSamplePhoto;
    @XmlAttribute
    protected String from;
    @XmlAttribute
    protected String to;
    @XmlAttribute
    protected String type;

   
    public List<MCTempImageXML.TempSamplePhoto> getTempSamplePhoto() {
        if (tempSamplePhoto == null) {
            tempSamplePhoto = new ArrayList<MCTempImageXML.TempSamplePhoto>();
        }
        return this.tempSamplePhoto;
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
        "imageFileName",
        "caRid",
        "sampleID"
    })
    public static class TempSamplePhoto {

        @XmlElement(required = true)
        protected String imageFileName;
        @XmlElement(name = "CARid")
        protected int caRid;
        protected int sampleID;

        /**
         * Gets the value of the imageFileName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getImageFileName() {
            return imageFileName;
        }

        /**
         * Sets the value of the imageFileName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setImageFileName(String value) {
            this.imageFileName = value;
        }

        /**
         * Gets the value of the caRid property.
         * 
         */
        public int getCARid() {
            return caRid;
        }

        /**
         * Sets the value of the caRid property.
         * 
         */
        public void setCARid(int value) {
            this.caRid = value;
        }

        /**
         * Gets the value of the sampleID property.
         * 
         */
        public int getSampleID() {
            return sampleID;
        }

        /**
         * Sets the value of the sampleID property.
         * 
         */
        public void setSampleID(int value) {
            this.sampleID = value;
        }

    }

}
