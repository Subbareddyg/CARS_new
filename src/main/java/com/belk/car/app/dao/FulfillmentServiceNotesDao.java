/**
 * 
 */
package com.belk.car.app.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import com.belk.car.app.model.oma.FulfillmentServiceNotes;
import com.belk.car.app.model.oma.VendorFulfillmentNotes;

/**
 * Method declarations to be used in DaoHibernate layer
 *
 */
public interface FulfillmentServiceNotesDao{

	/**
	 * Returns list of all the notes for that fulfillment service from table
	 * @param Fulfillment Service ID
	 * @return List
	 * @throws DataAccessException
	 */	
	public List<FulfillmentServiceNotes> getFulfillmentServicesNotes (String fsID);
	
	/**
	 * Returns search result when searched by status,Subject and Fulfillment service ID
	 * @return List
	 * @param Subject, Status, FsID
	 */
	public List<FulfillmentServiceNotes> searchNoteBySubject(String subject, String fsID);
	
	/**
	 * Add/Update Note to fulfillment Notes Table
	 * @param Fulfillment Service Notes object
	 * @throws DataAccessException
	 */
	public FulfillmentServiceNotes addNote(FulfillmentServiceNotes fulfillmentServiceNotes);
	
	public Map<String,Object> getNoteByID(Long fulfillmentServiceNoteID, boolean isFS);
	
	/**
	 * Add/Update note to vendor fulfillment note table
	 * @param VendorFulfillmentNotes
	 */
	public VendorFulfillmentNotes addVendorNote(VendorFulfillmentNotes vendorFulfillmentNotes);
	
	/**
	 * Returns list of notes for the specific vendor ID
	 * @param Vendor ID
	 * @return List
	 * @throws DataAccessException
	 */
	public List<VendorFulfillmentNotes> getVendorFulfillmentNotes(String venID);
	
	/**
	 * Returns search result when searched by status,Subject and Vendor ID
	 * @return List
	 * @param Subject, Status, vendor ID
	 */
	public List<VendorFulfillmentNotes> searchNoteByVendorID(String subject, String venID);
	
}
