/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.app.dto;

/**
 *
 * @author AFUSY85
 */
public class VendorStyleImageDTO {

    private String status;
    private String updatedDate;
    private String createdDate;
    private String vendorCatalogId;
    private String vendorStyleId;
    private String imagePath;
    private String imageName;
    private String vendorUPC;
    private String imageId;
    private String updatedBy;

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }
    private String imageType;

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getVendorCatalogId() {
        return vendorCatalogId;
    }

    public void setVendorCatalogId(String vendorCatalogId) {
        this.vendorCatalogId = vendorCatalogId;
    }

    public String getVendorStyleId() {
        return vendorStyleId;
    }

    public void setVendorStyleId(String vendorStyleId) {
        this.vendorStyleId = vendorStyleId;
    }

    public String getVendorUPC() {
        return vendorUPC;
    }

    public void setVendorUPC(String vendorUPC) {
        this.vendorUPC = vendorUPC;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }




}
