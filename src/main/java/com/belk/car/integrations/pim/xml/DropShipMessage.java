package com.belk.car.integrations.pim.xml;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "styleId",
    "vendorStyle",
    "vendorStyleName",
    "vendorNumber",
    "vendorName",
    "parentVendorNumber",
    "parentVendorName",
    "deptNumber",
    "classNumber",
    "dropshipFlag",
    "item",
    "packItem"
})

@XmlRootElement(name ="dropShipItems")
public class DropShipMessage {

    @XmlElement(name = "style_id", required = true)
    protected String styleId;
    @XmlElement(name = "vendor_style", required = true)
    protected String vendorStyle;
    @XmlElement(name = "vendor_style_name", required = true)
    protected String vendorStyleName;
    @XmlElement(name = "vendor_number", required = true)
    protected String vendorNumber;
    @XmlElement(name = "vendor_name", required = true)
    protected String vendorName;
    @XmlElement(name = "parent_vendor_number", required = true)
    protected String parentVendorNumber;
    @XmlElement(name = "parent_vendor_name", required = true)
    protected String parentVendorName;
    @XmlElement(name = "dept_number", required = true)
    protected BigInteger deptNumber;
    @XmlElement(name = "class_number", required = true)
    protected BigInteger classNumber;
    @XmlElement(name = "dropship_flag", required = true)
    protected String dropshipFlag;
    protected List<CarsDropshipItem> item;
    protected List<CarsDropshipPackItem> packItem;

    /**
     * Gets the value of the styleId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStyleId() {
        return styleId;
    }

    /**
     * Sets the value of the styleId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStyleId(String value) {
        this.styleId = value;
    }

    /**
     * Gets the value of the vendorStyle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVendorStyle() {
        return vendorStyle;
    }

    /**
     * Sets the value of the vendorStyle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVendorStyle(String value) {
        this.vendorStyle = value;
    }

    /**
     * Gets the value of the vendorStyleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVendorStyleName() {
        return vendorStyleName;
    }

    /**
     * Sets the value of the vendorStyleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVendorStyleName(String value) {
        this.vendorStyleName = value;
    }

    /**
     * Gets the value of the vendorNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVendorNumber() {
        return vendorNumber;
    }

    /**
     * Sets the value of the vendorNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVendorNumber(String value) {
        this.vendorNumber = value;
    }

    /**
     * Gets the value of the vendorName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVendorName() {
        return vendorName;
    }

    /**
     * Sets the value of the vendorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVendorName(String value) {
        this.vendorName = value;
    }

    /**
     * Gets the value of the parentVendorNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentVendorNumber() {
        return parentVendorNumber;
    }

    /**
     * Sets the value of the parentVendorNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentVendorNumber(String value) {
        this.parentVendorNumber = value;
    }

    /**
     * Gets the value of the parentVendorName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentVendorName() {
        return parentVendorName;
    }

    /**
     * Sets the value of the parentVendorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentVendorName(String value) {
        this.parentVendorName = value;
    }

    /**
     * Gets the value of the deptNumber property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getDeptNumber() {
        return deptNumber;
    }

    /**
     * Sets the value of the deptNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setDeptNumber(BigInteger value) {
        this.deptNumber = value;
    }

    /**
     * Gets the value of the classNumber property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getClassNumber() {
        return classNumber;
    }

    /**
     * Sets the value of the classNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setClassNumber(BigInteger value) {
        this.classNumber = value;
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

    
    public List<CarsDropshipItem> getItem() {
        if (item == null) {
            item = new ArrayList<CarsDropshipItem>();
        }
        return this.item;
    }

    
    public List<CarsDropshipPackItem> getPackItem() {
        if (packItem == null) {
            packItem = new ArrayList<CarsDropshipPackItem>();
        }
        return this.packItem;
    }

}
