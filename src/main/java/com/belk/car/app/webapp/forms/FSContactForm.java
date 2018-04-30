/**
 * Class Name : FSContactForm.java 
 * Version Information : v1.0 
 * Date : 12/01/2009 
 * Copyright Notice : All rights are reserved to Syntel.
 */
package com.belk.car.app.webapp.forms;

import org.apache.commons.lang.StringUtils;

import com.belk.car.app.model.oma.Address;
import com.belk.car.app.model.oma.ContactType;
import com.belk.car.app.model.oma.Contacts;
import com.belk.car.app.model.oma.FulfillmentService;


/**
 * Form for add/edit the Contact for Fulfillment Service/ Vendor Fulfillment Service.
 * @author afusy13
 *
 */
public class FSContactForm {
	private Contacts contacts;
	private Address address;
	private FulfillmentService fulfillmentService; 
	private ContactType contactType; 
	
	
	//private List<ContactType> contactType = new ArrayList<ContactType>(); 
	private Long vendorId;
	private Long fulfillmentServiceId;
	private String phoneNo;
	private String phoneAreaCode;
	private String phoneNumber1;
	private String phoneNumber2;
	
	private String altPhoneNo;
	private String altPhoneAreaCode;
	private String altPhoneNumber1;
	private String altPhoneNumber2;
	
	
	public FSContactForm(){
		//Need to instantiate the model classes as at the time of loading JSP
		//in case of Add Contact, the blank fields need to be mapped.
		contacts = new Contacts();
		address = new Address();
		contactType = new ContactType();
	}
	public Contacts getContacts() {
		return contacts;
	}
	public void setContacts(Contacts contacts) {
		this.contacts = contacts;
		breakDownPhone(contacts.getPhoneNbr(), false);
		breakDownPhone(contacts.getAltPhoneNbr(), true);
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public FulfillmentService getFulfillmentService() {
		return fulfillmentService;
	}
	public void setFulfillmentService(FulfillmentService fulfillmentService) {
		this.fulfillmentService = fulfillmentService;
	}
	public ContactType getContactType() {
		return contactType;
	}
	public void setContactType(ContactType contactType) {
		this.contactType = contactType;
	}
	public String getPhoneAreaCode() {
		return phoneAreaCode;
	}
	public Long getVendorId() {
		return vendorId;
	}
	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
	/**
	 * @param fulfillmentServiceId the fulfillmentServiceId to set
	 */
	public void setFulfillmentServiceId(Long fulfillmentServiceId) {
		this.fulfillmentServiceId = fulfillmentServiceId;
	}
	/**
	 * @return the fulfillmentServiceId
	 */
	public Long getFulfillmentServiceId() {
		return fulfillmentServiceId;
	}
	public void setPhoneAreaCode(String phoneAreaCode) {
		this.phoneAreaCode = phoneAreaCode;
	}
	public String getPhoneNumber1() {
		return phoneNumber1;
	}
	public void setPhoneNumber1(String phoneNumber1) {
		this.phoneNumber1 = phoneNumber1;
	}
	public String getPhoneNumber2() {
		return phoneNumber2;
	}
	public void setPhoneNumber2(String phoneNumber2) {
		this.phoneNumber2 = phoneNumber2;
	}
	public String getAltPhoneAreaCode() {
		return altPhoneAreaCode;
	}
	public void setAltPhoneAreaCode(String altPhoneAreaCode) {
		this.altPhoneAreaCode = altPhoneAreaCode;
	}
	public String getAltPhoneNumber1() {
		return altPhoneNumber1;
	}
	public void setAltPhoneNumber1(String altPhoneNumber1) {
		this.altPhoneNumber1 = altPhoneNumber1;
	}
	public String getAltPhoneNumber2() {
		return altPhoneNumber2;
	}
	public void setAltPhoneNumber2(String altPhoneNumber2) {
		this.altPhoneNumber2 = altPhoneNumber2;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		
		this.phoneNo = phoneNo;
	}
	public String getAltPhoneNo() {
		return altPhoneNo;
	}
	public void setAltPhoneNo(String altPhoneNo) {
		this.altPhoneNo = altPhoneNo;
	}
	/**
	 * Convenient method to break down the phone into several fields
	 * 
	 * @param phone2
	 * @param isAltPhone
	 *            determines which phone number to break
	 */
	private void breakDownPhone(String phone2, boolean isAltPhone) {
		if (StringUtils.isNotBlank(phone2)) {
			if (phone2.length() >= 10 && !isAltPhone) {
				setPhoneAreaCode(phone2.substring(0, 3));
				setPhoneNumber1(phone2.substring(3, 6));
				setPhoneNumber2(phone2.substring(6, 10));
			} else if (phone2.length() >= 10) {
				setAltPhoneAreaCode(phone2.substring(0, 3));
				setAltPhoneNumber1(phone2.substring(3, 6));
				setAltPhoneNumber2(phone2.substring(6, 10));
			}
		}
	}
	
	
}