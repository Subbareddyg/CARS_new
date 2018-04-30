/**
 * 
 */
package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the item pet catalog information. 
 * Current Path in xml response: /getItemRespone/Item_Catalog/pim_entry/Item_Pet_Ctg_Spec.
 */
@XmlRootElement(name ="Item_Pet_Ctg_Spec")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ItemPetCatalogData {

    private AuditData auditData;

    @XmlElement(name="Audit")
    public AuditData getAuditData() {
        return auditData;
    }

    public void setAuditData(AuditData auditData) {
        this.auditData = auditData;
    }   

}
