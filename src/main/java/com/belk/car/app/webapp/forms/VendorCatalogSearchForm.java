/**
 * 
 */
package com.belk.car.app.webapp.forms;

/**
 * @author 
 *
 */
public class VendorCatalogSearchForm {
	private String vendorNumber;
	private String vendorName;
	private String dateLastImportStart;
	private String departmentOnly;
        private String dateLastImportEnd;
        private String status;

    public String getDateLastImportStart() {
        return dateLastImportStart;
    }

    public void setDateLastImportStart(String dateLastImportStart) {
        this.dateLastImportStart = dateLastImportStart;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   /**
    * 
    * @return
    */
    public String getDateLastImportEnd() {
        return dateLastImportEnd;
    }
    /**
     *
     * @param dateLastImportEnd
     */
    public void setDateLastImportEnd(String dateLastImportEnd) {
        this.dateLastImportEnd = dateLastImportEnd;
    }
     /**
      *
      * @return
      */
    public String getDepartmentOnly() {
        return departmentOnly;
    }
     /**
      * 
      * @param departmentOnly
      */
    public void setDepartmentOnly(String departmentOnly) {
        this.departmentOnly = departmentOnly;
    }
     /**
      * 
      * @return
      */
    public String getVendorName() {
        return vendorName;
    }
     /**
      * 
      * @param vendorName
      */
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
     /**
      * 
      * @return
      */
    public String getVendorNumber() {
        return vendorNumber;
    }
     /**
      * 
      * @param vendorNumber
      */
    public void setVendorNumber(String vendorNumber) {
        this.vendorNumber = vendorNumber;
    }

}
