package com.belk.car.app.webapp.forms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VendorCreationForm implements Serializable {

	private String emailAddress;
	private String lastName;
	private String firstName;
	private String vendorId;
	private List<String> departments = new ArrayList<String>();
	private List<String> vendors = new ArrayList<String>();
	
	
		
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}	
	public List<String> getDepartments() {
		return departments;
	}
	public void setDepartments(List<String> departments) {
		this.departments = departments;
	}
	public List<String> getVendors() {
		return vendors;
	}
	public void setVendors(List<String> vendors) {
		this.vendors = vendors;
	}
	public void addVendor(String vendor) {
		getVendors().add(vendor);
	}
	public void addDepartment(String department) {
		getDepartments().add(department);
	}
}
