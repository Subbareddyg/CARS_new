/**
 * 
 */
package com.belk.car.app.webapp.forms;


/**
 * This class contains form fields mapped with vendor catalog setup form
 * @author afusya2
 */
public class SearchCatalogForm {
	private String vendorId;
	private String vendorName;
	private String catalogName;
	private String department;
        private String dateLastUpdatedStart;
	private String departmentOnly;
        private String dateLastUpdatedEnd;
        private String status;
        private String vendorNumber;
        private String tempStatus;


    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
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

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
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

    public String getVendorNumber() {
        return vendorNumber;
    }

    public void setVendorNumber(String vendorNumber) {
        this.vendorNumber = vendorNumber;
    }

    public String getTempStatus() {
        return tempStatus;
    }

    public void setTempStatus(String tempStatus) {
        this.tempStatus = tempStatus;
    }

	
	
}
