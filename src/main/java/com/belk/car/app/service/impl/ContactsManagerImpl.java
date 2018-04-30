/**
 * Class Name : ContactsManagerImpl.java 
 * Version Information : v1.0 
 * Date : 12/01/2009 
 * Copyright Notice : All rights are reserved to Syntel.
 */

package com.belk.car.app.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.appfuse.service.impl.UniversalManagerImpl;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.dao.ContactsDao;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.oma.Address;
import com.belk.car.app.model.oma.ContactType;
import com.belk.car.app.model.oma.Contacts;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.FulfillmentServiceContact;
import com.belk.car.app.model.oma.State;
import com.belk.car.app.service.ContactsManager;

/**
 * Implements the methods declared in manager interface of Contact.
 * 
 * @author AFUSY13
 */
public class ContactsManagerImpl extends UniversalManagerImpl
		implements
			ContactsManager, DropShipConstants {

	private ContactsDao contactsDao;

	public void setContactsDao(ContactsDao contactsDao) {
		this.contactsDao = contactsDao;
	}

	/**
	 * Gets the contacts bean details using the contact Id.
	 * @param	contactId	long	 Id of the Contact.
	 * @param	isVendorContact	boolean		True only if contactId is of Vendor Contact. And False if contactId is of Fulfillment Service Contact.
	 * @return	{@link Contacts}	Contact bean form database having Id as contactId
	 */
	public Contacts getContactInfoDetails(long contactId,boolean isVendorContact) {
		if(log.isInfoEnabled()){
			log.info("Inside getContactInfoDetails method.");
		}
		Contacts contacts = null;
		if(isVendorContact){
			if(log.isDebugEnabled()){
				log.debug("Contact is Vendor Contact..");
			}
			contacts = contactsDao.getVendorContactInfoDetails(contactId);
		}
		else{
			if(log.isDebugEnabled()){
				log.debug("Contact is Fufillment Service Contact..");
			}
			
			FulfillmentServiceContact fsContact = contactsDao.getFSContactInfoDetails(contactId);
		contacts = populateVendorContact(fsContact);	
		}
		return contacts;
	}

	/**
	 * Search the Contact according to the parameters passed.
	 * 
	 * @param params Map<String,Object> parameters for search
	 * @return Contacts List<Contacts> List of search results
	 */
	@SuppressWarnings("unchecked")
	public List<Contacts> searchContacts(Map<String, Object> params) {
		if(log.isInfoEnabled()){
			log.info("Inside searchContacts() method of ContactManagerImpl..");
		}
		boolean isVendorContact = false;
		List<FulfillmentServiceContact> fulfillmentServiceContactsList = new ArrayList<FulfillmentServiceContact>();
		List<Contacts> vendorContactsList = new ArrayList<Contacts>();
		if (null != params.get("vendorId")) {
			isVendorContact = true;
		}
		if(isVendorContact){
			vendorContactsList = (List<Contacts>) contactsDao.searchContacts(params);
		} else{
			fulfillmentServiceContactsList = (List<FulfillmentServiceContact>) contactsDao.searchContacts(params);
			for(FulfillmentServiceContact fsContact : fulfillmentServiceContactsList){
				vendorContactsList.add(populateVendorContact(fsContact));
			}
		}
		return vendorContactsList;
	}
	
	/**
	 * Populates the fields of VendorContact from Fulfillment Service contact.
	 * this is required as on JSP, the mapping is done on Vendor contact. 
	 * @param fsContact	{@link FulfillmentServiceContact} Fulfillment Service Contact Bean
	 * @return	({@link Contacts}
	 */
	private Contacts populateVendorContact(FulfillmentServiceContact fsContact){
		if(log.isDebugEnabled()){
			log.info("Inside populateVendorContact() method..");
		}
		Contacts vendorContact = new Contacts();
		vendorContact.setContactId(fsContact.getContactId());
		vendorContact.setFulfillmentService(fsContact.getFulfillmentService());
		vendorContact.setContactType(fsContact.getContactType());
		vendorContact.setAddress(fsContact.getAddress());
		vendorContact.setVendor(null);
		vendorContact.setEmailAddress(fsContact.getEmailAddress());
		vendorContact.setFirstName(fsContact.getFirstName());
		vendorContact.setLastName(fsContact.getLastName());
		vendorContact.setContactName(fsContact.getContactName());
		vendorContact.setOtherTypeDesc(fsContact.getOtherTypeDesc());
		vendorContact.setJobTitle(fsContact.getJobTitle());
		vendorContact.setPhoneNbr(fsContact.getPhoneNbr());
		vendorContact.setAltPhoneNbr(fsContact.getAltPhoneNbr());
		vendorContact.setNotes(fsContact.getNotes());
		vendorContact.setStatus(fsContact.getStatus());
		vendorContact.setLockedBy(fsContact.getLockedBy());
		vendorContact.setCreatedBy(fsContact.getCreatedBy());
		vendorContact.setCreatedDate(fsContact.getCreatedDate());
		vendorContact.setUpdatedBy(fsContact.getUpdatedBy());
		vendorContact.setUpdatedDate(fsContact.getUpdatedDate());
		return vendorContact;
	}

	/**
	 * Locks/unlocks the contact according to the action parameter
	 * 
	 * @param contactID long contactId to lock action String Action to be taken
	 *            (Lock/Unlock) usetId String User Id to be updated in locked by
	 *            column
	 */
	public void lockUnlockContact(long contactID, String action, String userId, long vendorId) {
		contactsDao.lockUnlockContact(contactID, action, userId, vendorId);

	}

	/**
	 * Gets all the Contact types from database
	 * 
	 * @return ContactType List<ContactType> List of all Contact Types.
	 */
	public List<ContactType> getAllContactTypes() {
		return contactsDao.getAllContactTypes();
	}

	/**
	 * Saves the Contact, if Contact is new or updated the Contact if Contact is
	 * existing.
	 * 
	 * @param contacts Contacts Contact to be saved or updated.
	 */
	public Contacts saveOrUpdateContact(Contacts contacts, boolean isVendorContact)
			throws Exception {
		if(log.isInfoEnabled()){
			log.info("Inside ContactsManagerImpl ..saveOrUpdateContact()..");
		}
		Address address = contacts.getAddress();
		if (StringUtils.isNotBlank(address.getAddr1())
				|| StringUtils.isNotBlank(address.getAddr2())
				|| StringUtils.isNotBlank(address.getCity())
				|| StringUtils.isNotBlank(address.getState())
				|| StringUtils.isNotBlank(address.getZip())) {
			
			if(!StringUtils.isNotBlank(address.getLocName())){
				address.setLocName(StringUtils.isNotBlank(address.getLocName()) ? address.getLocName():CONTACT_ADDRESS_LOCATION_FIELD);
				
			}else{
				log.debug("Address contains the location name.");
			}
			if(log.isInfoEnabled()){
				log.info("Saving the address..");
			}
			address = contactsDao.saveOrUpdateAddress(address);
			contacts.setAddress(address);
		}
		else {
			contacts.setAddress(null);
		}
		if(isVendorContact){
			contactsDao.saveOrUpdateVendorFSContact(contacts);
		}else{
			FulfillmentServiceContact fsContact = null;
			fsContact = populateFSContact(contacts);
			contactsDao.saveOrUpdateFSContact(fsContact);
			contacts = populateVendorContact(fsContact);
		}
		if(log.isDebugEnabled()){
			log.debug("Contact is saved with Id:"+ contacts.getContactId());
		}
		return contacts;
	}
	/**
	 * Populates the fulfillment service contact attributes from Vendor Contact bean.
	 * @param contacts	{@link Contacts}	Vendor Contact Bean.
	 * @return fsContact	{@link FulfillmentServiceContact}	Fulfillment Service Contact bean with mapped values.
	 */
	private FulfillmentServiceContact populateFSContact(
			Contacts contacts) {
		if(log.isInfoEnabled()){
			log.info("Inside populateFSContact method.");
		}
		FulfillmentServiceContact fsContact = new FulfillmentServiceContact();
		fsContact.setContactId(contacts.getContactId());
		fsContact.setFulfillmentService(contacts.getFulfillmentService());
		fsContact.setContactType(contacts.getContactType());
		fsContact.setAddress(contacts.getAddress());
		//vendorContact.setVendor(null);
		fsContact.setEmailAddress(contacts.getEmailAddress());
		fsContact.setFirstName(contacts.getFirstName());
		fsContact.setLastName(contacts.getLastName());
		fsContact.setContactName(contacts.getContactName());
		fsContact.setOtherTypeDesc(contacts.getOtherTypeDesc());
		fsContact.setJobTitle(contacts.getJobTitle());
		fsContact.setPhoneNbr(contacts.getPhoneNbr());
		fsContact.setAltPhoneNbr(contacts.getAltPhoneNbr());
		fsContact.setNotes(contacts.getNotes());
		fsContact.setStatus(contacts.getStatus());
		fsContact.setLockedBy(contacts.getLockedBy());
		fsContact.setCreatedBy(contacts.getCreatedBy());
		fsContact.setCreatedDate(contacts.getCreatedDate());
		fsContact.setUpdatedBy(contacts.getUpdatedBy());
		fsContact.setUpdatedDate(contacts.getUpdatedDate());
		return fsContact;
	}

	/**
	 * Gets the fulfillment service with the Id passed
	 * 
	 * @param fulfillmentServiceId long fulfillment Service Id
	 * @return Fulfillment Service bean with the passed Id.
	 */
	public FulfillmentService getFulfillmentService(long fulfillmentServiceId) {
		return contactsDao.getFulfillmentService(fulfillmentServiceId);
	}

	/**
	 * Gets the Contact Type by Id
	 * 
	 * @param contactTypeId long Id of Contact Type
	 * @return ContactType bean with the passed contactTypeId
	 */
	public ContactType getContactType(long contactTypeId) {
		return contactsDao.getContactType(contactTypeId);
	}

	/**
	 * Gets all the states available in the database table.
	 * 
	 * @return List of States bean
	 */
	public List<State> getAllStates() {
		return contactsDao.getAllStates();
	}

	/**
	 * Gets all the Vendor contact added for the particular fulfillmenServiceId and
	 * vendor Id
	 * 
	 * @param fsId Long Fulfillment Service Id vendorId Long Vendor Id
	 * @return List of Contacts
	 */
	public List<Contacts> getAllContacts(Long fsId, Long vendorId) {
		if(log.isInfoEnabled()){
			log.debug("Inside getAllContacts method.");
		}
		List<Contacts> list = null;
		if(null != vendorId && vendorId.longValue() != 0){
			list = contactsDao.getAllContacts(fsId, vendorId);
		}else{
			List<FulfillmentServiceContact> fulfillmentServiceContactsList = new ArrayList<FulfillmentServiceContact>();
			List<Contacts> vendorContactsList = new ArrayList<Contacts>();
			fulfillmentServiceContactsList = contactsDao.getAllFSContacts(fsId);
			for(FulfillmentServiceContact fsContact : fulfillmentServiceContactsList){
				vendorContactsList.add(populateVendorContact(fsContact));
			}
			list = vendorContactsList;
		}
		return list;
	}

	/**
	 * Gets the Vendor from passed vendorId Parameter
	 * 
	 * @param vendorId Long Id of the Vendor
	 * @return Vendor bean having the vendorId.
	 */
	public Vendor getVendor(Long vendorId) {
		return contactsDao.getVendor(vendorId);
	}
	/**
	 * Checks for the unique name Combination.
	 * @param params Map<String, Object> parameters to check for unique name
	 * @return boolean 
	 */
	public boolean checkUniqueName(Map<String, Object> params) {
		return contactsDao.checkUniqueName(params);
	}
	/**
	 * Gets the created Date by Contact Id
	 * @param contactId long Id of Contact record
	 * @param isVendorContact boolean boolean value to specify whether this contact is Vendor Contact or Fulfillment service Contact.
	 * @return createdDate Date Created date of the Contact record.
	 */
	public Date getContactCreatedDateById(long contactId, boolean isVendorContact) {
		//Contacts contacts = null;
		Date createdDate =new Date();
		if(isVendorContact){
			if(log.isDebugEnabled()){
				log.debug("Contact is Vendor Contact..");
			}
			createdDate = contactsDao.getVendorContactCreatedDate(contactId);
		}
		else{
			if(log.isDebugEnabled()){
				log.debug("Contact is Fufillment Service Contact..");
			}
			createdDate = contactsDao.getFSContactCreatedDate(contactId);
		}
		
			return createdDate;
		
	}
	// added methods for dropship phase 2
	public void lockContact(String contactId,User user){
		contactsDao.lockContact(contactId,user);
	}
    public void unlockContacts(String userName){
    	contactsDao.unlockContacts(userName);
    }

}
