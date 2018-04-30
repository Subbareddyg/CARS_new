/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.app.dto;

/**
 *
 * @author AFUSY85
 */
public class VendorStyleInfo {
    private String vendorStyleId;
    private String description;
    private String vendorNumber;
    private String vendorCatalogId;
    private String lastCatalogImported;
    private String vendorName;
    private String dateLastImport;
    private String vendorUpc;

    public String getDateLastImport() {
        return dateLastImport;
    }

    public void setDateLastImport(String dateLastImport) {
        this.dateLastImport = dateLastImport;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastCatalogImported() {
        return lastCatalogImported;
    }

    public void setLastCatalogImported(String lastCatalogImported) {
        this.lastCatalogImported = lastCatalogImported;
    }

    public String getVendorCatalogId() {
        return vendorCatalogId;
    }

    public void setVendorCatalogId(String vendorCatalogId) {
        this.vendorCatalogId = vendorCatalogId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorStyleId() {
        return vendorStyleId;
    }

    public void setVendorStyleId(String vendorStyleId) {
        this.vendorStyleId = vendorStyleId;
    }

    public String getVendorUpc() {
        return vendorUpc;
    }

    public void setVendorUpc(String vendorUpc) {
        this.vendorUpc = vendorUpc;
    }

    public String getVendorNumber() {
        return vendorNumber;
    }

    public void setVendorNumber(String vendorNumber) {
        this.vendorNumber = vendorNumber;
    }

   

}
