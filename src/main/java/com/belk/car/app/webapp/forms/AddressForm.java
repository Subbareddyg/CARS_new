
package com.belk.car.app.webapp.forms;

import java.io.Serializable;

public class AddressForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4537432641898898766L;
	private String location;
	private String addr1;
	private String addr2;
	private String city;
	private String state;
	private String zip;

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

}
