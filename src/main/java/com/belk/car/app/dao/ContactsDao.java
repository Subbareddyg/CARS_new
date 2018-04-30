/**
 * Class Name : ContactsDao.java 
 * Version Information : v1.0 
 * Date : 12/01/2009 
 * Copyright Notice : All rights are reserved to Syntel.
 */

package com.belk.car.app.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.appfuse.dao.UniversalDao;
import org.appfuse.model.User;

import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.oma.Address;
import com.belk.car.app.model.oma.ContactType;
import com.belk.car.app.model.oma.Contacts;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.FulfillmentServiceContact;
import com.belk.car.app.model.oma.State;

/**
 * Interface to declare the methods that needs to get the data from database
 * tables.
 */
public interface ContactsDao extends UniversalDao {

	public Contacts saveOrUpdateVendorFSContact(Contacts cntc) throws Exception;

	public Address saveOrUpdateAddress(Address address);

	public Contacts getVendorContactInfoDetails(long id);

	public List<ContactType> getAllContactTypes();

	public Vendor getVendor(Long vendorId);

	public List<Contacts> getAllContacts(Long fsId, Long vendorId);

	// public Contacts updateContactDetails(Contacts cntc) ;

	@SuppressWarnings("unchecked")
	public List searchContacts(Map<String, Object> params);

	public void lockUnlockContact(long contactID, String action, String userId, long vendorId);

	public FulfillmentService getFulfillmentService(long id);

	public ContactType getContactType(long id);

	public List<State> getAllStates(); 

	public FulfillmentServiceContact saveOrUpdateFSContact(FulfillmentServiceContact fsContact);

	public FulfillmentServiceContact getFSContactInfoDetails(long contactId);

	public List<FulfillmentServiceContact> getAllFSContacts(Long fsId);

	public boolean checkUniqueName(Map<String, Object> params);

	public Date getVendorContactCreatedDate(long contactId);

	public Date getFSContactCreatedDate(long contactId);
	
	// Added methods to lock/unlock contacts
	// replacing the phase 1 functionality in dropship phase2
	public void lockContact(String contactId,User user);
    public void unlockContacts(String userName);
}
