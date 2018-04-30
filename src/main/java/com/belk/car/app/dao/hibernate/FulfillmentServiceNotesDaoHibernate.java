/**
 * @author afusy45
 * Implementation of FulfillmentServiceNotesDao interface
 */
package com.belk.car.app.dao.hibernate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.belk.car.app.dao.FulfillmentServiceNotesDao;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.FulfillmentServiceNotes;
import com.belk.car.app.model.oma.VendorFulfillmentNotes;

@SuppressWarnings("unchecked")
public class FulfillmentServiceNotesDaoHibernate extends CachedQueryDaoHibernate implements FulfillmentServiceNotesDao {


	/**
	 * Returns list of all the notes for that fulfillment service from table
	 * @param Fulfillment Service ID
	 * @return List
	 * @throws DataAccessException
	 */
	public List<FulfillmentServiceNotes> getFulfillmentServicesNotes (String fsID)
	{
		List<FulfillmentServiceNotes> fulfillmentServiceList = new ArrayList<FulfillmentServiceNotes>();
		FulfillmentService fulfillmentService = new FulfillmentService();
		fulfillmentService.setFulfillmentServiceID(new Long(fsID));

		fulfillmentServiceList=getHibernateTemplate().find("FROM FulfillmentServiceNotes where fulfillmentServiceID1=?",fulfillmentService);
		return fulfillmentServiceList;
	}

	/**
	 * Returns list of notes for the specific vendor ID
	 * @param Vendor ID
	 * @return List
	 * @throws DataAccessException
	 */
	public List<VendorFulfillmentNotes> getVendorFulfillmentNotes(String venID)
	{
		List<VendorFulfillmentNotes> vendorFulfillmentNotesList = new ArrayList<VendorFulfillmentNotes>();
		Vendor vendor=new Vendor();
		vendor.setVendorId(new Long(venID));

		return getHibernateTemplate().find("FROM VendorFulfillmentNotes where vendorID=?",vendor);

	}

	/**
	 * Add/Update note to vendor fulfillment note table
	 * @param VendorFulfillmentNotes
	 */
	public VendorFulfillmentNotes addVendorNote(VendorFulfillmentNotes vendorFulfillmentNotes)
	{
		getHibernateTemplate().saveOrUpdate(vendorFulfillmentNotes);
        getHibernateTemplate().flush();
		return vendorFulfillmentNotes;
	}

	/**
	 * Returns search result when searched by status,Subject and Fulfillment service ID
	 * @return List
	 * @param Subject, Status, FsID
	 */

	public List<FulfillmentServiceNotes> searchNoteBySubject(String subject,String fsID) {
		String likeFormat = "%%%1$s%%";
		ArrayList<String> query = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();
		FulfillmentService fulfillmentService=new FulfillmentService();
		fulfillmentService.setFulfillmentServiceID(new Long(fsID));
		StringBuffer sqlB = new StringBuffer("from FulfillmentServiceNotes fs where fulfillmentServiceID1="+fulfillmentService.getFulfillmentServiceID());
		if ((subject != null) && !(subject.equals(""))) {
			query.add("upper(fs.fulfillmentServiceNotesSubject) LIKE ?");
			values.add(String.format(likeFormat,subject.toUpperCase(Locale.US)).toString());
		}

		if (!query.isEmpty()) {

			int i=0;
			for(String s: query) {

				sqlB.append(" AND ");

				sqlB.append(s);
				i++;
			}
		}
		sqlB.append(" order by fs.fulfillmentServiceNoteID");
		return getHibernateTemplate().find(sqlB.toString(), values.toArray());	

	}

	/**
	 * Returns search result when searched by status,Subject and Vendor ID
	 * @return List
	 * @param Subject, Status, vendor ID
	 */
	public List<VendorFulfillmentNotes> searchNoteByVendorID(String subject, String venID){
		String likeFormat = "%%%1$s%%";
		ArrayList<String> query = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();
		Vendor vendor=new Vendor();
		vendor.setVendorId((new Long(venID)));
		StringBuffer sqlB = new StringBuffer("from VendorFulfillmentNotes fs where vendorID="+vendor.getVendorId());

		if (subject != null && !(subject.equals(""))) {
			query.add("upper(fs.vendorFulfillmentNotesSubject) LIKE ?");
			values.add(String.format(likeFormat,subject.toUpperCase(Locale.US)).toString());
		}

		if (!query.isEmpty()) {
			int i=0;
			for(String s: query) {

				sqlB.append(" AND ");

				sqlB.append(s);
				i++;
			}
		}
		sqlB.append(" order by fs.vendorFulfillmentNoteID");
		return getHibernateTemplate().find(sqlB.toString(), values.toArray());


	}

	/**
	 * Add/Update Note to fulfillment Notes Table
	 * @param Fulfillment Service Notes object
	 * @throws DataAccessException
	 */
	public FulfillmentServiceNotes addNote(FulfillmentServiceNotes fulfillmentServiceNotes) {
		getHibernateTemplate().saveOrUpdate(fulfillmentServiceNotes);
        getHibernateTemplate().flush();
		return fulfillmentServiceNotes;
	}
	
	public Map<String,Object> getNoteByID(Long fulfillmentServiceNoteID, boolean isFS){
		
		Map<String,Object> newMap = new HashMap<String, Object>();
		if(isFS){
		List<FulfillmentServiceNotes> notesObject = new ArrayList<FulfillmentServiceNotes>();
		notesObject = getHibernateTemplate().find("FROM FulfillmentServiceNotes where fulfillmentServiceNoteID=?",fulfillmentServiceNoteID);
		newMap.put("CreatedDate",notesObject.get(0).getCreatedDate());
		newMap.put("CreatedBy",notesObject.get(0).getCreatedBy());
		getHibernateTemplate().evict(notesObject.get(0));
		}else{
			List<VendorFulfillmentNotes> notesObject = new ArrayList<VendorFulfillmentNotes>();
			notesObject = getHibernateTemplate().find("FROM VendorFulfillmentNotes where vendorFulfillmentNoteID=?",fulfillmentServiceNoteID);
			newMap.put("CreatedDate",notesObject.get(0).getCreatedDate());
			newMap.put("CreatedBy",notesObject.get(0).getCreatedBy());
			getHibernateTemplate().evict(notesObject.get(0));
		}
		return newMap;
	}

}
