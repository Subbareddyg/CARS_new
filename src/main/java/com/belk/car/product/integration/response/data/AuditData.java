package com.belk.car.product.integration.response.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Pojo to hold Audit Information.
 * Current Path in xml response: /getItemRespone/Item_Catalog/pim_entry/entry/Item_Pet_Ctg_Spec
 * 
 */
@XmlRootElement(name = "Audit")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class AuditData {

    private PETModifiedData createdOn;

    private String createdBy;

    private PETModifiedData lastModifiedOn;

    private String lastModifiedBy;

    @XmlElement(name = "Last_Modified_On")
    public PETModifiedData getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(PETModifiedData lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    @XmlElement(name = "Created_On")
    public PETModifiedData getCreatedOn() {
        return createdOn;
    }
    
    public void setCreatedOn(PETModifiedData createdOn) {
        this.createdOn = createdOn;
    }
    
    @XmlElement(name = "Created_By")
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    @XmlElement(name = "Last_Modified_By")
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }
    
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }   

}
