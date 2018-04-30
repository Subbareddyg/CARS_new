
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.app.dto;

import java.io.Serializable;

/**
 *
 * @author AFUSY85
 */
public class VendorUpcDTO  implements Serializable{
    /**
	 * 
	 */
   private static final long serialVersionUID = -7310631226839072668L;
   private String vendorUPC;
    private String description;
    private String color;
    private String status;
    private String catalog;
    private String updatedDate;
    private String updatedBy;
    private String vendorCatalogId;
    private String vendorStyleId;
    private String recordNum;
    private String catalogName;
    private String indx;
    private String catalogTemplateId;
    private String vendorUpcHeaderNbr;
    private String colorHeaderNbr;
    private String oldVendorUpc;
    private String oldColor;
    private String vendorId;

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        String temp="";
        if(null!=description) {
            temp=temp+description;
        }
        if(null!=this.color) {
            temp=temp+" " + this.color;
        }
        return temp;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecordNum() {
        return recordNum;
    }

    public void setRecordNum(String recordNum) {
        this.recordNum = recordNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getIndx() {
        return indx;
    }

    public void setIndx(String indx) {
        this.indx = indx;
    }

    public String getCatalogTemplateId() {
        return catalogTemplateId;
    }

    public void setCatalogTemplateId(String catalogTemplateId) {
        this.catalogTemplateId = catalogTemplateId;
    }

    public String getColorHeaderNbr() {
        return colorHeaderNbr;
    }

    public void setColorHeaderNbr(String colorHeaderNbr) {
        this.colorHeaderNbr = colorHeaderNbr;
    }

    public String getVendorUpcHeaderNbr() {
        return vendorUpcHeaderNbr;
    }

    public void setVendorUpcHeaderNbr(String vendorUpcHeaderNbr) {
        this.vendorUpcHeaderNbr = vendorUpcHeaderNbr;
    }

    public String getOldColor() {
        return oldColor;
    }

    public void setOldColor(String oldColor) {
        this.oldColor = oldColor;
    }

    public String getOldVendorUpc() {
        return oldVendorUpc;
    }

    public void setOldVendorUpc(String oldVendorUpc) {
        this.oldVendorUpc = oldVendorUpc;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public VendorUpcDTO(VendorUpcDTO obj) {
        super();
        this.vendorUPC = obj.vendorUPC;
        this.description = obj.description;
        this.color = obj.color;
        this.status = obj.status;
        this.catalog = obj.catalog;
        this.updatedDate = obj.updatedDate;
        this.updatedBy = obj.updatedBy;
        this.vendorCatalogId = obj.vendorCatalogId;
        this.vendorStyleId = obj.vendorStyleId;
        this.recordNum = obj.recordNum;
        this.catalogName = obj.catalogName;
        this.indx = obj.indx;
        this.catalogTemplateId = obj.catalogTemplateId;
        this.vendorUpcHeaderNbr = obj.vendorUpcHeaderNbr;
        this.colorHeaderNbr = obj.colorHeaderNbr;
        this.oldVendorUpc = obj.oldVendorUpc;
        this.oldColor = obj.oldColor;
        this.vendorId = obj.vendorId;
    }

    public VendorUpcDTO() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VendorUpcDTO other = (VendorUpcDTO) obj;
        if ((this.vendorUPC == null) ? (other.vendorUPC != null) : !this.vendorUPC.equals(other.vendorUPC)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.vendorUPC != null ? this.vendorUPC.hashCode() : 0);
        return hash;
    }

    



}
