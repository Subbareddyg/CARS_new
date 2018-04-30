/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.app.dto;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author AFUSY85
 */
public class VendorCatalogStyleDetailsDTO {
    private String vendorCatalogName;
    private String updatedBy;
    private String updatedDate;
    private String lockedBy;
    private String imagePath;
    private String recordNum;
    private String vendorUPC;
    private String catalogTemplateId;
    private String catalogStatus;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    private String description;


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(String lockedBy) {
            this.lockedBy = lockedBy;
        
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

    public String getVendorCatalogName() {
        return vendorCatalogName;
    }

    public void setVendorCatalogName(String vendorCatalogName) {
        this.vendorCatalogName = vendorCatalogName;
    }

    public String getRecordNum() {
        return recordNum;
    }

    public void setRecordNum(String recordNum) {
        this.recordNum = recordNum;
    }

    public String getVendorUPC() {
        return vendorUPC;
    }

    public void setVendorUPC(String vendorUPC) {
        this.vendorUPC = vendorUPC;
    }

    public String getCatalogTemplateId() {
        return catalogTemplateId;
    }

    public void setCatalogTemplateId(String catalogTemplateId) {
        this.catalogTemplateId = catalogTemplateId;
    }

    public String getCatalogStatus() {
        return catalogStatus;
    }

    public void setCatalogStatus(String catalogStatus) {
        this.catalogStatus = catalogStatus;
    }

    

}
