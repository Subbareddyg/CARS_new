/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.app.dto;

/**
 *
 * @author AFUSY85
 */
public class CatalogVendorDTO implements java.io.Serializable {
    
    
	private static final long serialVersionUID = -7317251022086291805L;
	private String name;
    private String vendorId;
    private long numCatalogs;
    private long numStyle;
    private String dateLastImport;
    private String status;
    private String dateLastImportStart;
    private String dateLastImportEnd;
    private long numSKUs;
    private String userDeptOnly;
    private String lastCatalogImported;
    private String catalogName;
    private String dateLastUpdateStart;
    private String dateLastUpdateEnd;
    private String department;
    private String isDisplayable;
    private String userId;
    private long vendorNumber;
    private String display;
    

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }
    public String getDateLastImport() {
    	String date;
        if(null!=dateLastImport) {
        	
        	 date =dateLastImport.substring(0,10);
           
        }
        else{
        	date=dateLastImport;
        }
        return date;
    }

    public void setDateLastImport(String dateLastImport) {
        this.dateLastImport = dateLastImport;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNumCatalogs() {
        return numCatalogs;
    }

    public void setNumCatalogs(long numCatalogs) {
        this.numCatalogs = numCatalogs;
    }

    public long getNumStyle() {
        return numStyle;
    }

    public void setNumStyle(long numStyle) {
        this.numStyle = numStyle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getDateLastImportEnd() {
        return dateLastImportEnd;
    }

    public void setDateLastImportEnd(String dateLastImportEnd) {
        this.dateLastImportEnd = dateLastImportEnd;
    }

    public String getDateLastImportStart() {
        return dateLastImportStart;
    }

    public void setDateLastImportStart(String dateLastImportStart) {
        this.dateLastImportStart = dateLastImportStart;
    }

    public long getNumSKUs() {
        return numSKUs;
    }

    public void setNumSKUs(long numSKUs) {
        this.numSKUs = numSKUs;
    }

    public String getUserDeptOnly() {
        return userDeptOnly;
    }

    public void setUserDeptOnly(String userDeptOnly) {
        this.userDeptOnly = userDeptOnly;
    }

    public String getLastCatalogImported() {
        return lastCatalogImported;
    }

    public void setLastCatalogImported(String lastCatalogImported) {
        this.lastCatalogImported = lastCatalogImported;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getDateLastUpdateEnd() {
        return dateLastUpdateEnd;
    }

    public void setDateLastUpdateEnd(String dateLastUpdateEnd) {
        this.dateLastUpdateEnd = dateLastUpdateEnd;
    }

    public String getDateLastUpdateStart() {
        return dateLastUpdateStart;
    }

    public void setDateLastUpdateStart(String dateLastUpdateStart) {
        this.dateLastUpdateStart = dateLastUpdateStart;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    
    public String toString() {
        return "";
    }

    public String getIsDisplayable() {
        return isDisplayable;
    }

    public void setIsDisplayable(String isDisplayable) {
        this.isDisplayable = isDisplayable;
        if("Y".equals(isDisplayable)) {
            setDisplay("Yes");
        } else {
            setDisplay("No");
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getVendorNumber() {
        return vendorNumber;
    }

    public void setVendorNumber(long vendorNumber) {
        this.vendorNumber = vendorNumber;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
    
    
}
