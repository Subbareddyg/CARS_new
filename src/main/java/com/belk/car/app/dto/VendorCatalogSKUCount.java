/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.app.dto;

/**
 *
 * @author AFUSY85
 */
public class VendorCatalogSKUCount implements java.io.Serializable {
    
	private static final long serialVersionUID = 3640832055071827348L;
	private String skuCount;
    private String vendorId;

    public String getSkuCount() {
        return skuCount;
    }

    public void setSkuCount(String skuCount) {
        this.skuCount = skuCount;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VendorCatalogSKUCount other = (VendorCatalogSKUCount) obj;
        if ((this.vendorId == null) ? (other.vendorId != null) : !this.vendorId.equals(other.vendorId)) {
            return false;
        }
        this.skuCount = other.skuCount;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (this.vendorId != null ? this.vendorId.hashCode() : 0);
        return hash;
    }
}
