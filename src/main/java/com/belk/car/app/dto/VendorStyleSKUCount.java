/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.app.dto;

/**
 *
 * @author AFUSY85
 */
public class VendorStyleSKUCount implements java.io.Serializable {
    
	private static final long serialVersionUID = 3640832055071827348L;
    private String skuCount;
    private String vendorStyleId;

    public String getSkuCount() {
        return skuCount;
    }

    public void setSkuCount(String skuCount) {
        this.skuCount = skuCount;
    }

    public String getVendorStyleId() {
        return vendorStyleId;
    }

    public void setVendorStyleId(String vendorStyleId) {
        this.vendorStyleId = vendorStyleId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VendorStyleSKUCount other = (VendorStyleSKUCount) obj;
        if ((this.vendorStyleId == null) ? (other.vendorStyleId != null) : !this.vendorStyleId.equals(other.vendorStyleId)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.vendorStyleId != null ? this.vendorStyleId.hashCode() : 0);
        return hash;
    }

    

    

    

   
}
