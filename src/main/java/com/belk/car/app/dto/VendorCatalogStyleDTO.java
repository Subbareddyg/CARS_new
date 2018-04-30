/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.app.dto;

import java.io.Serializable;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author AFUSY85
 */ 
public class VendorCatalogStyleDTO implements Serializable{
   
    
    private static final long serialVersionUID = -7912890905070145893L;
    private String imagePath;
    private String vendorStyleId;
    private String description;
    private String status;
    private String catalogName;
    private String numSKUs;
    private String dateLastUpdated;
    private String lastUpdatedBy;
    private String lockedBy;
    private String catalogId;
    private String vendorCatalogId;
    private String vendorId;
    private String updatedDate;
    private String department;
    private String vendorUPC;
    private String userDerartmentOnly;
    private String dateUpdateStart;
    private String dateUpdateEnd;
    private String userId;
    private String recordNum;
    private String catalogTemplateId;
    private String catalogStatus;

    public String getVendorCatalogId() {
        return vendorCatalogId;
    }

    public void setVendorCatalogId(String vendorCatalogId) {
        this.vendorCatalogId = vendorCatalogId;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getDateLastUpdated() {
        return dateLastUpdated;
    }

    public void setDateLastUpdated(String dateLastUpdated) {
        this.dateLastUpdated = dateLastUpdated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(String lockedBy) {
        this.lockedBy = lockedBy;
    }

    public String getNumSKUs() {
        return numSKUs;
    }

    public void setNumSKUs(String numSKUs) {
        this.numSKUs = numSKUs;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVendorStyleId() {
        return vendorStyleId;
    }

    public void setVendorStyleId(String vendorStyleId) {
        this.vendorStyleId = vendorStyleId;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getDateUpdateEnd() {
        return dateUpdateEnd;
    }

    public void setDateUpdateEnd(String dateUpdateEnd) {
        this.dateUpdateEnd = dateUpdateEnd;
    }

    public String getDateUpdateStart() {
        return dateUpdateStart;
    }

    public void setDateUpdateStart(String dateUpdateStart) {
        this.dateUpdateStart = dateUpdateStart;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUserDerartmentOnly() {
        return userDerartmentOnly;
    }

    public void setUserDerartmentOnly(String userDerartmentOnly) {
        this.userDerartmentOnly = userDerartmentOnly;
    }

    public String getVendorUPC() {
        return vendorUPC;
    }

    public void setVendorUPC(String vendorUPC) {
        this.vendorUPC = vendorUPC;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRecordNum() {
        return recordNum;
    }

    public void setRecordNum(String recordNum) {
        this.recordNum = recordNum;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VendorCatalogStyleDTO other = (VendorCatalogStyleDTO) obj;
        if ((this.vendorStyleId == null) ? (other.vendorStyleId != null) : !this.vendorStyleId.equals(other.vendorStyleId)) {
            return false;
        }
        if ((this.vendorId == null) ? (other.vendorId != null) : !this.vendorId.equals(other.vendorId)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.vendorStyleId != null ? this.vendorStyleId.hashCode() : 0);
        hash = 41 * hash + (this.vendorId != null ? this.vendorId.hashCode() : 0);
        return hash;
    }
    

    


}
