
package com.belk.car.app.webapp.forms;

import java.io.Serializable;
import java.util.List;

import com.belk.car.app.model.oma.Address;

/**
 * @author afusy07-priyanka_gadia@syntelinc.com
 * @Date 12-dec-09
 */
public class FSVendorForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3006756839403860008L;
	private String vndrFulfillmentServId;
	private String safetyInvAmt = "0";
	private String safetyInvAmtTyp;
	private String vndrServLvl;
	private String isInvToRcpt;
	private String overrideDays;
	private String invoiceMethod;
	private String isLocked;
	private String vndrId;
	private String fulfillmentServId;
	private String createdBy;
	private String createdDt;
	private List<Address> shipAddressList;
	private String updtDt;
	private String updtBy;
	private String statusCd;
	private String venNum;
	private String venName;
	private String numSkus;
	private String numStyles;
	private String location;
	private String addr1;
	private String addr2;
	private String city;
	private String state;
	private String zip;
	private Boolean isExpeditedShipping;

	public String toString() {
		StringBuffer vendorForm = new StringBuffer();
		vendorForm.append("vndrFulfillmentServId=" + getVndrFulfillmentServId());
		vendorForm.append("\n" + "safetyInvAmt=" + getSafetyInvAmt());
		vendorForm.append("\n" + "vndrId=" + getVndrId());
		vendorForm.append("\n" + "safetyInvAmtTyp=" + getSafetyInvAmtTyp());
		vendorForm.append("\n" + "vndrServLvl=" + getVndrServLvl());
		vendorForm.append("\n" + "isExpeditedShipping=" + getIsExpeditedShipping());
		vendorForm.append("\n" + "isInvToRcpt=" + getIsInvToRcpt());
		vendorForm.append("\n" + "overrideDays=" + getOverrideDays());
		vendorForm.append("\n" + "isLocked=" + getIsLocked());
		vendorForm.append("\n" + "fulfillmentServId=" + getFulfillmentServId());
		vendorForm.append("\n" + "statusCd=" + getStatusCd());
		vendorForm.append("\n" + "numSkus=" + getNumSkus());
		vendorForm.append("\n" + "numStyles=" + getNumStyles());
		vendorForm.append("\n" + "venName=" + getVenName());
		vendorForm.append("\n" + "venNum=" + getVenNum());
		if (getShipAddressList() != null) {
			vendorForm.append("\n" + "shipAddressList=" + getShipAddressList().size());
		}
		return vendorForm.toString();
	}

	public String getInvoiceMethod() {
		return invoiceMethod;
	}

	public void setInvoiceMethod(String invoiceMethod) {
		this.invoiceMethod = invoiceMethod;
	}

	public String getVndrFulfillmentServId() {
		return vndrFulfillmentServId;
	}

	public void setVndrFulfillmentServId(String vndrFulfillmentServId) {
		this.vndrFulfillmentServId = vndrFulfillmentServId;
	}

	public String getSafetyInvAmt() {
		return safetyInvAmt;
	}

	public void setSafetyInvAmt(String safetyInvAmt) {
		this.safetyInvAmt = safetyInvAmt;
	}

	public String getSafetyInvAmtTyp() {
		return safetyInvAmtTyp;
	}

	public void setSafetyInvAmtTyp(String safetyInvAmtTyp) {
		this.safetyInvAmtTyp = safetyInvAmtTyp;
	}

	public String getVndrServLvl() {
		return vndrServLvl;
	}

	public void setVndrServLvl(String vndrServLvl) {
		this.vndrServLvl = vndrServLvl;
	}

	public String getIsInvToRcpt() {
		return isInvToRcpt;
	}

	public void setIsInvToRcpt(String isInvToRcpt) {
		this.isInvToRcpt = isInvToRcpt;
	}

	public String getOverrideDays() {
		return overrideDays;
	}

	public void setOverrideDays(String overrideDays) {
		this.overrideDays = overrideDays;
	}

	public String getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(String isLocked) {
		this.isLocked = isLocked;
	}

	public String getVndrId() {
		return vndrId;
	}

	public void setVndrId(String vndrId) {
		this.vndrId = vndrId;
	}

	public String getFulfillmentServId() {
		return fulfillmentServId;
	}

	public void setFulfillmentServId(String fulfillmentServId) {
		this.fulfillmentServId = fulfillmentServId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(String createdDt) {
		this.createdDt = createdDt;
	}

	public List<Address> getShipAddressList() {
		return shipAddressList;
	}

	public void setShipAddressList(List<Address> shipAddressList) {
		this.shipAddressList = shipAddressList;
	}

	public String getUpdtDt() {
		return updtDt;
	}

	public void setUpdtDt(String updtDt) {
		this.updtDt = updtDt;
	}

	public String getUpdtBy() {
		return updtBy;
	}

	public void setUpdtBy(String updtBy) {
		this.updtBy = updtBy;
	}

	public String getStatusCd() {
		return statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}

	public String getVenNum() {
		return venNum;
	}

	public void setVenNum(String venNum) {
		this.venNum = venNum;
	}

	public String getVenName() {
		return venName;
	}

	public void setVenName(String venName) {
		this.venName = venName;
	}

	public String getNumSkus() {
		return numSkus;
	}

	public void setNumSkus(String numSkus) {
		this.numSkus = numSkus;
	}

	public String getNumStyles() {
		return numStyles;
	}

	public void setNumStyles(String numStyles) {
		this.numStyles = numStyles;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAddr1() {
		return addr1;
	}

	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}

	public String getAddr2() {
		return addr2;
	}

	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public Boolean getIsExpeditedShipping() {
		return isExpeditedShipping;
	}

	public void setIsExpeditedShipping(Boolean isExpeditedShipping) {
		this.isExpeditedShipping = isExpeditedShipping;
	}

	
	

}
