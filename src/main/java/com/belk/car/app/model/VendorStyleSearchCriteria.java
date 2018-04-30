package com.belk.car.app.model;

import java.io.Serializable;

import org.appfuse.model.User;

public class VendorStyleSearchCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8049619733147495460L;
	
	private String vendorStyleName;
	private String vendorNumber;
	private String vendorStyleNumber;
	private long vendorStyleId;
	private boolean childrenOnly;
	private boolean patternsOnly;
	private String patternType;
	private String statusCd;
	private User user ;
	private boolean searchChildVendorStyle ;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getStatusCd() {
		return statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}

	public long getVendorStyleId() {
		return vendorStyleId;
	}

	public void setVendorStyleId(long vendorStyleId) {
		this.vendorStyleId = vendorStyleId;
	}

	public boolean showChildrenOnly() {
		return childrenOnly;
	}

	public void setChildrenOnly(boolean childrenOnly) {
		this.childrenOnly = childrenOnly;
	}

	public String getVendorStyleName() {
		return vendorStyleName;
	}

	public void setVendorStyleName(String vendorStyleName) {
		this.vendorStyleName = vendorStyleName;
	}

	public String getVendorNumber() {
		return vendorNumber;
	}

	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}

	public String getVendorStyleNumber() {
		return vendorStyleNumber;
	}

	public void setVendorStyleNumber(String vendorStyleNumber) {
		this.vendorStyleNumber = vendorStyleNumber;
	}

	public boolean isPatternsOnly() {
		return patternsOnly;
	}

	public void setPatternsOnly(boolean patternsOnly) {
		this.patternsOnly = patternsOnly;
	}

	public boolean alsoSearchChildVendorStyle() {
		return searchChildVendorStyle;
	}

	public void setSearchChildVendorStyle(boolean searchChildVendorStyle) {
		this.searchChildVendorStyle = searchChildVendorStyle;
	}

    public String getPatternType() {
        return patternType;
    }

    public void setPatternType(String patternType) {
        this.patternType = patternType;
    }
	
	
}
