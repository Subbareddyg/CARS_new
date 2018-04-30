/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.belk.car.app.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author afupxs5
 */
@Embeddable
public class VendorSkuPIMAttributeId implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 6598756367872983779L;

    private long vendorSkuId;
    
    private long pimAttributeId;
    
    public VendorSkuPIMAttributeId(){}

    public VendorSkuPIMAttributeId(long vendorSkuId, long pimAttrId ){
        this.vendorSkuId = vendorSkuId;
        this.pimAttributeId = pimAttrId;
    }
    
    @Column(name="VENDOR_SKU_ID", nullable = false, precision = 12, scale = 0)
    public long getVendorSkuId() {
        return vendorSkuId;
    }

    public void setVendorSkuId(long vendorSkuId) {
        this.vendorSkuId = vendorSkuId;
    }

    @Column(name="PIM_ATTR_ID", nullable = false, precision = 12, scale = 0)
    public long getPimAttributeId() {
        return pimAttributeId;
    }

    public void setPimAttributeId(long pimAttributeId) {
        this.pimAttributeId = pimAttributeId;
    } 
    
    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof VendorSkuPIMAttributeId))
            return false;
        VendorSkuPIMAttributeId castOther = (VendorSkuPIMAttributeId) other;

        return (this.getPimAttributeId() == castOther.getPimAttributeId())
                && (this.getVendorSkuId() == castOther.getVendorSkuId());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + (int) this.getPimAttributeId();
        result = 37 * result + (int) this.getVendorSkuId();
        return result;
    }
}
