/**
 * Class Name : ContactsFormValidator.java 
 * Version Information : v1.0 
 * Date : 12/08/2009 
 * Copyright Notice : All rights are reserved to Syntel.
 */

package com.belk.car.app.webapp.validators;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.oma.Contacts;
import com.belk.car.app.service.ContactsManager;
import com.belk.car.app.webapp.forms.FSContactForm;

/**
 * Validates the Other Type Description field only if the Contact Type selected
 * is "Other"
 * 
 * @author afusy13
 */
public class ContactsFormValidator implements Validator,DropShipConstants {

	private transient final Log log = LogFactory
			.getLog(ContactsFormValidator.class);
	private ContactsManager contactsManager;

	@SuppressWarnings("unchecked")
	public boolean supports(Class arg0) {
		if (log.isDebugEnabled()) {
			log.debug("Inside ContactsFormValidator..");
		}
		return FSContactForm.class.isAssignableFrom(arg0);
	}

	/**
	 * Validates the Contact Type Field. If the field "Other Type Desc" is not
	 * filled, throws an field error back to JSP without submitting the form.
	 */
	public void validate(Object arg0, Errors errors) {
		FSContactForm contactForm = (FSContactForm) arg0;
		String otherContactTypeId = OTHER_CONTACT_TYPE_ID;
		if (log.isDebugEnabled()) {
			log.debug("Contact Type Value: "+ contactForm.getContactType().getContactTypeId());
		}
		if (contactForm.getContactType().getContactTypeId().equals("0")) {
			log.debug("There is a validation error.Contact Type field is not selected.");
			errors.rejectValue(CONTACT_TYPE_FIELD, "errors.contact.otherType");
		}
		else if ((contactForm.getContactType().getContactTypeId()).equals(otherContactTypeId)
				&& contactForm.getContacts().getOtherTypeDesc().length() == 0) {
				log.debug("Validation error occured. Other Type Description is not entered.");
			errors.rejectValue(CONTACT_OTHER_TYPE_DESC_FIELD, "errors.contact.otherTypeDesc");
		}
		int altPhoneAreaCode = contactForm.getAltPhoneAreaCode().length();
		int altPhoneNumber1 = contactForm.getAltPhoneNumber1().length();
		int altPhoneNumber2 = contactForm.getAltPhoneNumber2().length();
		
		if (altPhoneAreaCode > 0|| altPhoneNumber1 > 0 || altPhoneNumber2 > 0) {
			if (altPhoneAreaCode < 3) {
				errors.rejectValue(ALT_PHONE_AREA_CODE_FIELD, "errors.contact.altPhoneAreaCode.invalid");
			}
			if (altPhoneNumber1 < 3) {
				errors.rejectValue(ALT_PHONE_NUMBER1_FIELD, "errors.contact.altPhone3DigitCode.invalid");
			}
			if (altPhoneNumber2 < 4) {
				errors.rejectValue(ALT_PHONE_NUMBER2_FIELD, "errors.contact.altPhone4DigitCode.invalid");
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("Contact Id: "+ contactForm.getContacts().getContactId());
		}
		//	Check for Unique name for both
		checkForUniqueName(contactForm,errors);
	
	}

	/**
	 * Checks for the unique contact name.
	 * @param contactForm
	 * @param errors
	 */
	private void checkForUniqueName(FSContactForm contactForm, Errors errors) {
		//Logic for checking the unique First Name and Last Name.
		Contacts contacts = null;
		Long fulfillmentServiceId = null ;
		Long vendorId = null;
		String firstName = null;
		String lastName = null;
		Long contactId = null;
		if(null != contactForm.getContacts()){
			contacts = contactForm.getContacts();
			firstName = contacts.getFirstName();
			lastName = contacts.getLastName();
			contactId = contacts.getContactId();
		}
		if(StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName)){
				boolean isNameUnique = false;
			if(contactForm.getFulfillmentServiceId() != 0 && contactForm.getVendorId()!= null && contactForm.getVendorId() != 0){
				//This is a Vendor contact
				fulfillmentServiceId = contactForm.getFulfillmentServiceId();
				vendorId = contactForm.getVendorId();
			}else if(contactForm.getFulfillmentServiceId() != 0 && contactForm.getVendorId() == 0){
				//This is a fulfillment service contact
				fulfillmentServiceId = contactForm.getFulfillmentServiceId();
				vendorId = new Long(0);
			}else{
				//Problem occured as fulfillment Service is not selected."
				errors.rejectValue("", "INVALID","Please Select the Fulfillment Service First.");
			}
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("fulfillmentServiceId", fulfillmentServiceId);
			params.put("vendorId", vendorId);
			params.put("firstName", firstName);
			params.put("lastName", lastName);
			params.put("contactId", contactId);
			try{
				
				isNameUnique = contactsManager.checkUniqueName(params);
			}catch(Exception e){
				//Some severe exception has occured while checking for unique Contact.
				log.error(e);
				errors.rejectValue("", "INVALID","Unable to validate the Unique Contact Name.");
			}
			if(isNameUnique) {
				//Contact Already exists.
				errors.rejectValue("", "errors.contact.exist",
						new Object[] { firstName.concat(" ").concat(lastName) }, "already exisits!");
			}	
		}
	}

	/**
	 * @param contactsManager the contactsManager to set
	 */
	public void setContactsManager(ContactsManager contactsManager) {
		this.contactsManager = contactsManager;
	}

	/**
	 * @return the contactsManager
	 */
	public ContactsManager getContactsManager() {
		return contactsManager;
	}

}
