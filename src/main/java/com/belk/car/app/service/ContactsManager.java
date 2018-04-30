/**
 * Class Name : ContactsManager.java 
 * Version Information : v1.0 
 * Date : 12/01/2009 
 * Copyright Notice : All rights are reserved to Syntel.
 */

package com.belk.car.app.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.appfuse.model.User;
import org.appfuse.service.UniversalManager;

import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.oma.ContactType;
import com.belk.car.app.model.oma.Contacts;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.State;

/**
 * Manager interface Class for Contacts.
 * 
 * @author
 */
public interface ContactsManager extends UniversalManager {
	/**
	 * Saves or Updated the Contact(Fulfillment Service Contact/ Vendor Contact).
	 * @param cntc
	 * @param isVendorContact
	 * @return
	 * @throws Exception
	 */
	public Contacts saveOrUpdateContact(Contacts cntc, boolean isVendorContact)
			throws Exception;

	public List<ContactType> getAllContactTypes();

	public List<Contacts> getAllContacts(Long fsId, Long vendorId);

	public Vendor getVendor(Long vendorId);

	public Contacts getContactInfoDetails(long srcID, boolean isVendorContact);

	public void lockUnlockContact(long contactID, String action, String userId, long vendorId);

	public List<Contacts> searchContacts(Map<String, Object> params);

	public FulfillmentService getFulfillmentService(long id);

	public ContactType getContactType(long id);

	public List<State> getAllStates();

	public boolean checkUniqueName(Map<String, Object> params);

	public Date getContactCreatedDateById(long contactId, boolean isVendorContact);
	
	// added methods for dropship phase 2
	public void lockContact(String contactId,User user);
    public void unlockContacts(String userName);
}
