
package com.belk.car.app.webapp.forms;

import com.belk.car.app.model.oma.FulfillmentServiceVendorReturn;

public class VendorReturnForm {

	private String dropShipRma;
	private String returnMethod;
	private String returnMethodType;
	private String locName;
	private String addr1;
	private String addr2;
	private String city;
	private String state;
	private String zip;
	private String venId;
	private String fsId;
	private String addrId;
	private String returnId;
	private String createdBy;
	private FulfillmentServiceVendorReturn returnModel;

	public String getVenId() {
		return venId;
	}

	public void setVenId(String venId) {
		this.venId = venId;
	}

	public String getFsId() {
		return fsId;
	}

	public void setFsId(String fsId) {
		this.fsId = fsId;
	}

	public String getDropShipRma() {
		return dropShipRma;
	}

	public void setDropShipRma(String dropShipRma) {
		this.dropShipRma = dropShipRma;
	}

	public String getReturnMethod() {
		return returnMethod;
	}

	public void setReturnMethod(String returnMethod) {
		this.returnMethod = returnMethod;
	}

	public String getLocName() {
		return locName;
	}

	public void setLocName(String locName) {
		this.locName = locName;
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

	public void setAddrId(String addrId) {
		this.addrId = addrId;
	}

	public String getAddrId() {
		return addrId;
	}

	public void setReturnId(String returnId) {
		this.returnId = returnId;
	}

	public String getReturnId() {
		return returnId;
	}

	public void setReturnMethodType(String returnMethodType) {
		this.returnMethodType = returnMethodType;
	}

	public String getReturnMethodType() {
		return returnMethodType;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setReturnModel(FulfillmentServiceVendorReturn returnModel) {
		this.returnModel = returnModel;
	}

	public FulfillmentServiceVendorReturn getReturnModel() {
		return returnModel;
	}

}
