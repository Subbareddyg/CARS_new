/**
 * 
 */
package com.belk.car.app.webapp.forms;

/**
 * @author 
 *
 */
public class CatalogVendorsForm {
	private String vendorNumber;
	private String vendorName;
	private String dateLastImportStart;
	private String departmentOnly;
        private String dateLastImportEnd;
        private String status;
        private String display;


    /**
     * Get Date Last Import Start.
     * @return String
     */
    public String getDateLastImportStart() {
        return dateLastImportStart;
    }
    /**
     * Set Date Last Import Start.
     * @param dateLastImportStart String
     */
    public void setDateLastImportStart(String dateLastImportStart) {
        this.dateLastImportStart = dateLastImportStart;
    }
    /**
     * Get Status.
     * @return String
     */
    public String getStatus() {
        return status;
    }
    /**
     * Set Status
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

   /**
    * Get Date Last Import End.
    * @return String
    */
    public String getDateLastImportEnd() {
        return dateLastImportEnd;
    }
    /**
     * Set Date Last Import End.
     * @param dateLastImportEnd
     */
    public void setDateLastImportEnd(String dateLastImportEnd) {
        this.dateLastImportEnd = dateLastImportEnd;
    }
     /**
      * Get User Department Only
      * @return String
      */
    public String getDepartmentOnly() {
        return departmentOnly;
    }
     /**
      * Set User Department Only.
      * @param departmentOnly
      */
    public void setDepartmentOnly(String departmentOnly) {
        this.departmentOnly = departmentOnly;
    }
     /**
      * Get Vendor Name
      * @return String
      */
    public String getVendorName() {
        return vendorName;
    }
     /**
      * Set Vendor Name.
      * @param vendorName
      */
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
     /**
      * Get Vendor Number.
      * @return String
      */
    public String getVendorNumber() {
        return vendorNumber;
    }
     /**
      * Set vendor number
      * @param vendorNumber
      */
    public void setVendorNumber(String vendorNumber) {
        this.vendorNumber = vendorNumber;
    }
    /**
     * Get Display
     * @return String
     */
    public String getDisplay() {
        return display;
    }
    /**
     * Set Display.
     * @param display
     */
    public void setDisplay(String display) {
        this.display = display;
    }




}
