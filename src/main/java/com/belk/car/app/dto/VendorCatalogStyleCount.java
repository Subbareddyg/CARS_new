/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.app.dto;

/**
 *
 * @author AFUSY85
 */
public class VendorCatalogStyleCount implements java.io.Serializable {
    
	private static final long serialVersionUID = -9087415466027913457L;
	private String styleCount;
    private String vendorId;

    public String getStyleCount() {
        return styleCount;
    }

    public void setStyleCount(String styleCount) {
        this.styleCount = styleCount;
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
        final VendorCatalogStyleCount other = (VendorCatalogStyleCount) obj;
        if ((this.vendorId == null) ? (other.vendorId != null) : !this.vendorId.equals(other.vendorId)) {
            return false;
        }
        this.styleCount = other.styleCount;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (this.vendorId != null ? this.vendorId.hashCode() : 0);
        return hash;
    }

}
