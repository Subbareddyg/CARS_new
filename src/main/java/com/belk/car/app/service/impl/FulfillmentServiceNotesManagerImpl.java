/**
 * 
 */
package com.belk.car.app.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.appfuse.service.impl.UniversalManagerImpl;
import org.springframework.dao.DataAccessException;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.dao.FulfillmentServiceNotesDao;
import com.belk.car.app.model.oma.FulfillmentServiceNotes;
import com.belk.car.app.model.oma.VendorFulfillmentNotes;
import com.belk.car.app.service.FulfillmentServiceNotesManager;

/**
 * @author 
 * Implementation of FulfillmentServiceNotesManager interface
 */
public class FulfillmentServiceNotesManagerImpl extends UniversalManagerImpl implements FulfillmentServiceNotesManager,DropShipConstants {

	private FulfillmentServiceNotesDao fulfillmentServiceNotesDao;

	/**
	 * @param set fulfillmentServiceNotesDao
	 */
	public void setFulfillmentServiceNotesDao(FulfillmentServiceNotesDao fulfillmentServiceNotesDao) {
		this.fulfillmentServiceNotesDao = fulfillmentServiceNotesDao;
	}

	/**
	 * @return the fulfillmentServiceNotesDAO
	 */
	public FulfillmentServiceNotesDao getFulfillmentServiceNotesDao() {
		return fulfillmentServiceNotesDao;
	}


	/**
	 * Returns list of all the notes for that fulfillment service from table
	 * @param Fulfillment Service ID
	 * @return List
	 * @throws DataAccessException
	 */
	public List<FulfillmentServiceNotes> getFulfillmentServicesNotes(String fsID) throws DataAccessException {
		return fulfillmentServiceNotesDao.getFulfillmentServicesNotes(fsID);
	}

	
	/**
	 * Returns search result when searched by status,Subject and Fulfillment service ID
	 * @return List FulfillmentServiceNotes
	 * @param Subject, Status, FsID
	 */
	public List<FulfillmentServiceNotes> searchNoteBySubject(String subject, String fsID){
		return fulfillmentServiceNotesDao.searchNoteBySubject(subject,fsID);
	}
	
	/**
	 * Returns search result when searched by status,Subject and Vendor ID
	 * @return List
	 * @param Subject, Status, vendor ID
	 */
	public List<VendorFulfillmentNotes> searchNoteByVendorID(String subject,String venID){
		return fulfillmentServiceNotesDao.searchNoteByVendorID(subject,venID);
	}
	
	/**
	 * Add/Update Note to fulfillment Notes Table
	 * @param Fulfillment Service Notes object
	 * @throws DataAccessException
	 */
	public FulfillmentServiceNotes addNote(FulfillmentServiceNotes fulfillmentServiceNotes) throws DataAccessException{	
		return fulfillmentServiceNotesDao.addNote(fulfillmentServiceNotes);
	}
	
	/**
	 * Get the Note object for specific Note ID
	 * @author afusy45
	 * @param Note ID Long
	 * @return FulfillmentServiceNote object
	 */
	public Map<String,Object> getNoteByID(Long fulfillmentServiceNoteID, boolean isFS){
		return fulfillmentServiceNotesDao.getNoteByID(fulfillmentServiceNoteID, isFS);
	}

	/**
	 * Add/Update note to vendor fulfillment note table
	 * @param VendorFulfillmentNotes
	 * @return VendorFulfillmentNotes
	 */
	public VendorFulfillmentNotes addVendorNote(VendorFulfillmentNotes vendorFulfillmentNotes){
		return fulfillmentServiceNotesDao.addVendorNote(vendorFulfillmentNotes);
	}
	
	/**
	 * Returns list of notes for the specific vendor ID
	 * @param Vendor ID
	 * @return List
	 * @throws DataAccessException
	 */
	public List<VendorFulfillmentNotes> getVendorFulfillmentNotes(String venID){
		return fulfillmentServiceNotesDao.getVendorFulfillmentNotes(venID);
	}
}
