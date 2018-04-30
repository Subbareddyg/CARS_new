/**
 * 
 */
package com.belk.car.app.webapp.forms;


/**
 * This class contains form fields mapped with vendor catalog setup form
 * @author afusya2
 */
public class SearchStyleForm {
	private String vendorId;
	private String description;
	private String vendorStyle;
	private String department;
        private String dateLastUpdatedStart;
	private String departmentOnly;
        private String dateLastUpdatedEnd;
        private String status;
        private String vendorUPC;
        private String catalogId;
        private String vendorCatalogTemplaetId;
        private String tempStatus;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendorStyle() {
        return vendorStyle;
    }

    public void setVendorStyle(String vendorStyle) {
        this.vendorStyle = vendorStyle;
    }



    public String getDateLastUpdatedEnd() {
        return dateLastUpdatedEnd;
    }

    public void setDateLastUpdatedEnd(String dateLastUpdatedEnd) {
        this.dateLastUpdatedEnd = dateLastUpdatedEnd;
    }

    public String getDateLastUpdatedStart() {
        return dateLastUpdatedStart;
    }

    public void setDateLastUpdatedStart(String dateLastUpdatedStart) {
        this.dateLastUpdatedStart = dateLastUpdatedStart;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartmentOnly() {
        return departmentOnly;
    }

    public void setDepartmentOnly(String departmentOnly) {
        this.departmentOnly = departmentOnly;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorUPC() {
        return vendorUPC;
    }

    public void setVendorUPC(String vendorUPC) {
        this.vendorUPC = vendorUPC;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public String getVendorCatalogTemplaetId() {
        return vendorCatalogTemplaetId;
    }

    public void setVendorCatalogTemplaetId(String vendorCatalogTemplaetId) {
        this.vendorCatalogTemplaetId = vendorCatalogTemplaetId;
    }

    public String getTempStatus() {
        return tempStatus;
    }

    public void setTempStatus(String tempStatus) {
        this.tempStatus = tempStatus;
    }

	
	
}
