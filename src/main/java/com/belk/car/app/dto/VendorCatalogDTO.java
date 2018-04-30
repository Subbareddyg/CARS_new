/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.app.dto;

/**
 *
 * @author AFUSY85
 */
public class VendorCatalogDTO {
    private String vendorCatalogID;
    private String vendorCatalogName;
    private String source;
    private String statusCD;
    private String updatedDate;
    private String updatedBy;
    private String lockedBy;

    public String getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(String lockedBy) {
        this.lockedBy = lockedBy;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatusCD() {
        return statusCD;
    }

    public void setStatusCD(String statusCD) {
        this.statusCD = statusCD;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getVendorCatalogID() {
        return vendorCatalogID;
    }

    public void setVendorCatalogID(String vendorCatalogID) {
        this.vendorCatalogID = vendorCatalogID;
    }

    public String getVendorCatalogName() {
        return vendorCatalogName;
    }

    public void setVendorCatalogName(String vendorCatalogName) {
        this.vendorCatalogName = vendorCatalogName;
    }
    

}
