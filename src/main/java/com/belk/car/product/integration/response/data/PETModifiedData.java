/**
 * 
 */
package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * Pojo to hold Last modified on Information.
 * Current Path in xml response: /getItemRespone/Item_Catalog/pim_entry/entry/Item_Pet_Ctg_Spec/Audit/Last_Modified_On
 * 
 */
@XmlRootElement(name = "Last_Modified_On")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class PETModifiedData {

    private String format;
    
    private String value;

    @XmlAttribute(name = "format")
    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @XmlValue
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    
}
