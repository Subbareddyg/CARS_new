/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.belk.car.app.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 *
 * @author afupxs5
 */
@Entity
@Table(name="VENDORSKU_PIM_ATTRIBUTE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VendorSkuPIMAttribute extends BaseAuditableModel implements Serializable {
    


    /**
     * 
     */
    private static final long serialVersionUID = 1403029187172515285L;

    VendorSkuPIMAttributeId id;
    
    private VendorSku vendorSkuDetails;
    
    private PIMAttribute pimAttributeDetails;
    
    private String attributeValue;
    
    @EmbeddedId
    @AttributeOverrides( {
            @AttributeOverride(name = "vendorSkuId", column = @Column(name = "VENDOR_SKU_ID", nullable = false, precision = 12, scale = 0)),
            @AttributeOverride(name = "pimAttrId", column = @Column(name = "PIM_ATTR_ID", nullable = false, precision = 12, scale = 0)) })
    public VendorSkuPIMAttributeId getId() {
        return id;
    }

    public void setId(VendorSkuPIMAttributeId id) {
        this.id = id;
    }
    
    /**
     *
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="VENDOR_SKU_ID", referencedColumnName = "CAR_SKU_ID", nullable = false,insertable = false, updatable = false)
    public VendorSku getVendorSkuDetails() {
        return vendorSkuDetails;
    }

    public void setVendorSkuDetails(VendorSku vendorSkuDetails) {
        this.vendorSkuDetails = vendorSkuDetails;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="PIM_ATTR_ID", nullable = false,insertable = false, updatable = false)
    public PIMAttribute getPimAttributeDetails() {
        return pimAttributeDetails;
    }

    public void setPimAttributeDetails(PIMAttribute pimAttributeDetails) {
        this.pimAttributeDetails = pimAttributeDetails;
    }

    @Column(name="PIM_ATTR_VALUE", nullable = true)
    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    @Column(name = "CREATED_BY", nullable = false, length = 100)
    @Override
    public String getCreatedBy() {
            return this.createdBy;
    }

    @Column(name = "UPDATED_BY", nullable = false, length = 100)
    @Override
    public String getUpdatedBy() {
            return this.updatedBy;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE", nullable = false)
    @Override
    public Date getCreatedDate() {
            return this.createdDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_DATE", nullable = false)
    @Override
    public Date getUpdatedDate() {
            return this.updatedDate;
    }
}
