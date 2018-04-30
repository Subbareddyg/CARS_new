/**
 * Class Name : ContactsDaoHibernate.java 
 * Version Information : v1.0 
 * Date : 12/01/2009 
 * Copyright Notice : All rights are reserved to Syntel.
 */

package com.belk.car.app.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.appfuse.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.belk.car.app.dao.ContactsDao;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.oma.Address;
import com.belk.car.app.model.oma.ContactType;
import com.belk.car.app.model.oma.Contacts;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.FulfillmentServiceContact;
import com.belk.car.app.model.oma.State;
import com.belk.car.app.model.vendorcatalog.VendorCatalog;

/**
 * Hibernate Class for Contacts that handles the operations that requires data
 * from database.
 *  
 * @author AFUSY13
 */
public class ContactsDaoHibernate extends UniversalDaoHibernate implements ContactsDao {

	/**
	 * Method to get the contact details by passing Contact ID
	 * 
	 * @param id Contact Id
	 * @return Contacts Object Contacts bean object retrieved from Database
	 *         Table using Contact Id
	 */
	public Contacts getVendorContactInfoDetails(long id) {
		if (log.isDebugEnabled()) {
			log.debug("Inside getVendorContactInfoDetails() Method.Getting the Conatct Information for id:" + id);
		}
		Contacts contact = null;
		contact = (Contacts) getHibernateTemplate().get(Contacts.class, id);
		return contact;
	}

	/**
	 * Method to get the contact details by passing Contact ID
	 * 
	 * @param id Contact Id
	 * @return Contacts Object Contacts bean object retrieved from Database
	 *         Table using Contact Id
	 */
	public FulfillmentServiceContact getFSContactInfoDetails(long id) {
		if (log.isDebugEnabled()) {
			log.debug("Inside getFSContactInfoDetails() Method.Getting the Contact Information for id:" + id);
		}
		FulfillmentServiceContact contact = null;
		contact = (FulfillmentServiceContact) getHibernateTemplate().get(
				FulfillmentServiceContact.class, id);
		return contact;
	}

	/**
	 * Method to search the Contact based on parameters supplied
	 * 
	 * @param params Map<String,Object> Parameters that are passed for search
	 *            criteria
	 *@return List<Contacts> List of Contacts Beans retrieved from search query
	 */
	@SuppressWarnings("unchecked")
	public List searchContacts(Map<String, Object> params) {
		if (log.isDebugEnabled()) {
			log.debug("Inside Hibernate class.. search() method..");
		}
		// List<Contacts> contactList = null;
		ArrayList<String> query = new ArrayList<String>();
		List searchResult = null;
		String likeFormat = "%%%1$s%%";
		ArrayList<Object> values = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer();
		// StringBuffer query = new StringBuffer();

		if (null != params.get("vendorId")) {
			sql.append(" FROM Contacts ");
			query.add(" VENDOR_ID = ?");
			values.add(params.get("vendorId"));
		}
		else {
			sql.append(" FROM FulfillmentServiceContact ");
		}

		if (null != params.get("firstName")
				&& !params.get("firstName").toString().equalsIgnoreCase("")) {
			query.add(" upper(FIRST_NAME) LIKE ?");
			values.add(String.format(likeFormat, params.get("firstName").toString().toUpperCase())
					.toString());
		}
		if (null != params.get("lastName")
				&& !params.get("lastName").toString().equalsIgnoreCase("")) {
			query.add(" upper(LAST_NAME) LIKE ?");
			values.add(String.format(likeFormat, params.get("lastName").toString().toUpperCase())
					.toString());
		}
		if (null != params.get("status") && !params.get("status").toString().equalsIgnoreCase("")
				&& !(params.get("status").toString()).equalsIgnoreCase("ALL")) {
			query.add(" STATUS_CD = ?");
			values.add(params.get("status").toString());
		}
		if (null != params.get("fsId")) {
			query.add(" FULFMNT_SERVICE_ID = ?");
			values.add(params.get("fsId"));
		}

		if (!query.isEmpty()) {
			sql.append(" WHERE ");
			int i = 0;
			for (String s : query) {
				if (i > 0) {
					sql.append(" AND ");
				}
				sql.append(s);
				i++;
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("Query generated :"+sql.toString());
		}
		// contactList
		searchResult = getHibernateTemplate().find(sql.toString(), values.toArray());
		return searchResult;

	}

	/**
	 * Locks/Unlocks the contact
	 * 
	 * @param contactID long ID of Contacts bean action String action to be
	 *            executed (lock/unlock) userId String Logged in User's Id
	 *            (Required to Lock the record for that user)
	 */

	public void lockUnlockContact(long contactID, String action, String userId, long vendorId) {
		if (log.isDebugEnabled()) {
			log.debug("Inside lockUnlockContact..Hibernate ..contactId:" + contactID + " action: "+ action);
		}
		Contacts contact = null;
		FulfillmentServiceContact fsContact = null;
		Date currentDate = new Date();

		if (action != null && action.equalsIgnoreCase("lock")) {
			if (log.isDebugEnabled()) {
				log.debug("Inside lockUnlockContact ....action :lock");
			}
			if (vendorId != 0) {
				contact = getVendorContactInfoDetails(contactID);
				contact.setLockedBy(userId);
				contact.setUpdatedBy(userId);
				contact.setUpdatedDate(currentDate);
				this.getHibernateTemplate().saveOrUpdate(contact);
			}
			else {
				fsContact = getFSContactInfoDetails(contactID);
				fsContact.setLockedBy(userId);
				fsContact.setUpdatedBy(userId);
				fsContact.setUpdatedDate(currentDate);
				this.getHibernateTemplate().saveOrUpdate(fsContact);
			}
		}
		else if (action != null && action.equalsIgnoreCase("unlock")) {
			if (log.isDebugEnabled()) {
				log.debug("Inside lockUnlockContact ....action :unlock");
			}
			if (vendorId != 0) {
				contact = getVendorContactInfoDetails(contactID);
				contact.setLockedBy(null);
				contact.setUpdatedBy(userId);
				contact.setUpdatedDate(currentDate);
				this.getHibernateTemplate().saveOrUpdate(contact);
			}
			else {
				fsContact = getFSContactInfoDetails(contactID);
				fsContact.setLockedBy(null);
				fsContact.setUpdatedBy(userId);
				fsContact.setUpdatedDate(currentDate);
				this.getHibernateTemplate().saveOrUpdate(fsContact);
			}
		}
		else {
			log.error("Invalid action:" + action);
		}
		if (log.isDebugEnabled()) {
			log.debug("Updated values in Database");
		}
        getHibernateTemplate().flush();
	}

	/**
	 * Gets all the Contact Types from database table
	 * 
	 * @return ContactType List List of contact Types available in Database
	 *         Table
	 */
	@SuppressWarnings("unchecked")
	public List<ContactType> getAllContactTypes() {
		if (log.isDebugEnabled()) {
			log.debug("Inside getAkkContactTypes() method.");
		}
		List<ContactType> contactTypeList = null;
		contactTypeList = getHibernateTemplate().find("FROM ContactType ORDER BY CONTACT_TYPE_CD");
		return contactTypeList;
	}

	/**
	 * Method to update existing / save new Contact in the database.
	 * 
	 * @param cntc Contacts Object to be updated/inserted
	 * @return cntc Contacts Saved Contact object (Containing Contact ID)
	 */
	public Contacts saveOrUpdateVendorFSContact(Contacts contacts)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Inside DaoHibernate... saveOrUpdateContact()...saving contact.");
		}
		getHibernateTemplate().saveOrUpdate(contacts);
		getHibernateTemplate().flush();
		if (log.isDebugEnabled()) {
			log.debug("saved the contact successfully with Id :" + contacts.getContactId());
		}
		return contacts;
	}

	/**
	 * Gets the fulfillment Service using Fulfillment Service ID
	 * 
	 * @param id long Fulfillment Service Id
	 * @return fulfillmentService {@link FulfillmentService} bean fetched from
	 *         database
	 */
	public FulfillmentService getFulfillmentService(long id) {
		if (log.isDebugEnabled()) {
			log.debug("Inside getFulfillmentService() Method.");
		}
		FulfillmentService fulfillmentService = null;
		fulfillmentService = (FulfillmentService) getHibernateTemplate().get(
				FulfillmentService.class, id);
		return fulfillmentService;
	}

	/**
	 * Gets the Contact Type using Contact Type ID
	 * 
	 * @param id long ContactType Id
	 * @return contactType {@link ContactType} bean fetched from database
	 */
	public ContactType getContactType(long id) {
		if (log.isDebugEnabled()) {
			log.debug("Inside getContactType() Method.");
		}
		ContactType contactType = null;
		contactType = (ContactType) getHibernateTemplate().get(ContactType.class, id);
		return contactType;
	}

	/**
	 * Gets all available states in database
	 * 
	 * @return List of all States beans
	 */
	@SuppressWarnings("unchecked")
	public List<State> getAllStates() {
		if (log.isDebugEnabled()) {
			log.debug("Inside getAllStates().");
		}
		List<State> state = null;
		state = getHibernateTemplate().find("From State");
		return state;
	}

	/**
	 * Gets all the contacts available either for the fulfillment Service or for
	 * Vendor Fulfillment Service
	 * 
	 * @param fulfillmentServiceId Long Fulfillment Service ID vendorId Long
	 *            Vendor ID
	 * @return Contacts List Available Contacts List
	 */
	@SuppressWarnings("unchecked")
	public List<Contacts> getAllContacts(Long fulfillmentServiceId, Long vendorId) {
		if (log.isDebugEnabled()) {
			log.debug("Inside getAllContacts() Method.");
		}
		List<Contacts> contactList = null;
		if (vendorId == null) {
			contactList = getHibernateTemplate().find("FROM Contacts where FULFMNT_SERVICE_ID=?",
					fulfillmentServiceId);
			
		}
		else {
			Object arr[] = { fulfillmentServiceId, vendorId };
			contactList = getHibernateTemplate().find(
					"FROM Contacts where FULFMNT_SERVICE_ID=? and VENDOR_ID=?", arr);
		}
		return contactList;
	}

	/**
	 * Gets the vendor using vendorId
	 * 
	 * @param vendorId Long Vendor Id
	 * @return {@link Vendor} Vendor Object for the provided vendor Id
	 */
	public Vendor getVendor(Long vendorId) {
		if (log.isDebugEnabled()) {
			log.debug("Inside getVendor() Method.");
		}
		Vendor vendor = null;
		vendor = (Vendor) getHibernateTemplate().find("FROM Vendor where VENDOR_ID=?", vendorId);
		return vendor;
	}

	/**
	 * Saves the Address Object
	 * 
	 * @param address {@link Address} Object to be saved or Updated
	 * @return address {@link Address} Committed object with Address Id
	 */
	public Address saveOrUpdateAddress(Address address) {
		if (log.isDebugEnabled()) {
			log.debug("Inside daoHibernate..saveAddress()");
		}
		address.setStatus("ACTIVE");
		getHibernateTemplate().saveOrUpdate(address);
		getHibernateTemplate().flush();
		return address;
	}

	/**
	 * Gets the Details of Vendor Contact by ID.
	 * 
	 * @param id ID of the Vendor Contact.
	 * @return contact {@link Contacts} Vendor Contact bean having contact ID as
	 *         parameter, id.
	 */
	public Contacts getContactInfoDetails(long id) {
		if (log.isDebugEnabled()) {
			log.debug("Getting the Conatct Information for id:" + id);
		}
		Contacts contact = null;
		contact = (Contacts) getHibernateTemplate().get(Contacts.class, id);
		return contact;
	}

	/**
	 * Saves or Updates the Fulfillment Service Contacts.
	 * 
	 * @param fsContact {@link FulfillmentServiceContact} Contact to be Saved or
	 *            Updated.
	 * @return fsContact {@link FulfillmentServiceContact} Committed Contact
	 *         bean.
	 */
	public FulfillmentServiceContact saveOrUpdateFSContact(FulfillmentServiceContact fsContact) {
		if (log.isDebugEnabled()) {
			log.debug("Inside saveOrUpdateFSContact() Method.");
		}
		//Save the fulfillment Service Contact
		getHibernateTemplate().saveOrUpdate(fsContact);
		//Flush the bean in database.
		getHibernateTemplate().flush();
		//Return the saved bean back as bean will contain the ID for inserted FS Contacts.
		return fsContact;
	}

	/**
	 * Gets all the Fulfillment Service Contacts under that fulfillment Service.
	 * 
	 * @param fsId Long Fulfillment Service Id, for which contacts need to be
	 *            searched.
	 * @return fsContactList List<FulfillmentServiceContact> Resulted List of
	 *         fulfillment Service Contacts.
	 */
	public List<FulfillmentServiceContact> getAllFSContacts(Long fsId) {
		if (log.isDebugEnabled()) {
			log.debug("Inside getAllFSContacts() Method.");
		}
		//Get the list of Contacts having fulfillment Service id as parameter fsId.
		List<FulfillmentServiceContact> fsContactList = (List<FulfillmentServiceContact>) getHibernateTemplate()
				.find("FROM FulfillmentServiceContact where FULFMNT_SERVICE_ID=?", fsId);
		//Return the Fulfillment Service contacts list.
		return fsContactList;
	}
	
	/**
	 * Checks for the same First Name and Last name exists previously.
	 * @param params Map<String, Object> Map onject containing the parameters to be checked.
	 * @return boolean value, as per the result
	 */
	public boolean checkUniqueName(Map<String, Object> params) {
		if (log.isDebugEnabled()) {
			log.debug("Inside checkUniqueName() Method.");
		}
		ArrayList<String> query = new ArrayList<String>();
		List searchResult = null;
		boolean isVendorContact = false;
		ArrayList<Object> values = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer();
		//Check for type of Contact, i.e Vendor Contact or Fulfillment Service Contact.
		if (null != params.get("vendorId") && !params.get("vendorId").toString().equals("0")) {
			sql.append(" FROM Contacts ");
			query.add(" VENDOR_ID = ?");
			values.add(params.get("vendorId"));
			isVendorContact = true;
		}
		else {
			sql.append(" FROM FulfillmentServiceContact ");
		}
		//If firstName is not null or blank, then add First Name in query. 
		if (null != params.get("firstName")
				&& !params.get("firstName").toString().equalsIgnoreCase("")) {
			query.add(" upper(FIRST_NAME) = ?");
			values.add(params.get("firstName").toString().toUpperCase());
		}
		//If firstName is not null or blank, then add First Name parameter in query.
		if (null != params.get("lastName")
				&& !params.get("lastName").toString().equalsIgnoreCase("")) {
			query.add(" upper(LAST_NAME) = ?");
			values.add(params.get("lastName").toString().toUpperCase());
		}
		//If LastName is not null or blank, then add Last Name parameter in query.
		if (null != params.get("fulfillmentServiceId") ) {
			query.add(" FULFMNT_SERVICE_ID = ?");
			values.add(params.get("fulfillmentServiceId"));
		}
		  
		if (!query.isEmpty()) {
			sql.append(" WHERE ");
			int i = 0;
			for (String s : query) {
				if (i > 0) {
					sql.append(" AND ");
				}
				sql.append(s);
				i++;
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("Query : "+sql);
		}
		// Get the ContactList
		searchResult = getHibernateTemplate().find(sql.toString(), values.toArray());
		Long contactId = null;
		//Following logic is to verify the Contact is existing or a new one.
		if (null != searchResult && !searchResult.isEmpty()) {
			if(isVendorContact){
				Contacts contacts = (Contacts) searchResult.get(0);
				contactId = contacts.getContactId();
				getHibernateTemplate().evict(contacts);
			}else{
				FulfillmentServiceContact fulfillmentServiceContact = (FulfillmentServiceContact) searchResult.get(0);
				contactId = fulfillmentServiceContact.getContactId();
				getHibernateTemplate().evict(fulfillmentServiceContact);
			}
			if (log.isDebugEnabled()) {
				log.debug("Contact Id from db:"+contactId + " And the contact ID from params :"+ (Long) params.get("contactId"));
			}
			if(contactId.equals((Long)params.get("contactId"))){
				return false;
			}else{
				return true;
			}
		}else{
			return false;
		}
		
	}
	/**
	 * Gets the Created Date For Vendor Contacts
	 * @param	contactId	long	Contact for which the Create date is to be found.
	 * @return	{@link Date}	Created Date of the contact.
	 */
	public Date getVendorContactCreatedDate(long contactId) {
		if (log.isDebugEnabled()) {
			log.debug("Inside getVendorContactInfoDetails() Method.Getting the Conatct Information for id:" + contactId);
		}
		Contacts contact = null;
		Date createdDate = new Date();
		contact = (Contacts) getHibernateTemplate().get(Contacts.class, contactId);
		if(contact != null){
			createdDate = contact.getCreatedDate();
			getHibernateTemplate().evict(contact);
			return createdDate;
		}else {
			return new Date();
		}
		
	}
	/**
	 * Gets the Created Date For Fulfillment service  Contacts.
	 * @param	contactId	long	Contact for which the Create date is to be found.
	 * @return	{@link Date}	Created Date of the contact.
	 */
	public Date getFSContactCreatedDate(long contactId) {
		if (log.isDebugEnabled()) {
			log.debug("Inside getFSContactInfoDetails() Method.Getting the Contact Information for id:" + contactId);
		}
		FulfillmentServiceContact contact = null;
		Date createdDate = new Date();
		contact = (FulfillmentServiceContact) getHibernateTemplate().get(
				FulfillmentServiceContact.class, contactId);
		if(contact != null){
			createdDate = contact.getCreatedDate();
			getHibernateTemplate().evict(contact);
			return createdDate;
		}else {
			return new Date();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.belk.car.app.dao.ContactsDao#lockContact(java.lang.String, org.appfuse.model.User)
	 * Method to lock the Contact by user
	 */
	public void lockContact(String contactId,User user){
	      if(log.isDebugEnabled()) {
	    	  	log.debug("Start 'lockContact' method contactId:"+contactId +" Username:"+user.getUsername());
       	}
		 	try {
				FulfillmentServiceContact contact = (FulfillmentServiceContact) getHibernateTemplate().get(FulfillmentServiceContact.class, Long.parseLong(contactId));
				contact.setLockedBy(user.getUsername());
				contact.setAuditInfo(user);
				this.getHibernateTemplate().saveOrUpdate(contact);
				getHibernateTemplate().flush();
			} catch(Exception e) {
			   log.error("Hibdernate Exception in lockContact -" + e)	;
			}
		     if(log.isDebugEnabled()) {
		        	 log.debug("End 'lockContact' method");
	        }
    }
	
	/*
	 * (non-Javadoc)
	 * @see com.belk.car.app.dao.ContactsDao#unlockContacts(java.lang.String)
	 * Method to unlock the Contact by user
	 */
			
	public void unlockContacts(String userName){
		 try{
			   SessionFactory sf = getHibernateTemplate().getSessionFactory();
			   Session session = sf.getCurrentSession();
			   String query = "UPDATE FULFMNT_SERVICE_CONTACT SET LOCKED_BY=NULL WHERE LOCKED_BY= :USER";
			   int updateRows =  session.createSQLQuery(query).setString("USER", userName).executeUpdate();
			   log.info(updateRows+" Contacts Unlocked for the user:"+userName);
      }catch(Exception e){
      	log.error("Got exception while unlock the Contacts : " + e.getMessage());
      }
		
	}
}
