/**
 * 
 */
package com.belk.car.app.service;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.appfuse.service.UniversalManager;

import com.belk.car.app.model.oma.Address;
import com.belk.car.app.model.oma.FulfillmentIntegrationType;
import com.belk.car.app.model.oma.FulfillmentMethod;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.FulfillmentServiceNotes;
import com.belk.car.app.model.oma.ItemSource;
import com.belk.car.app.model.oma.StylePopulationMethod;
import com.belk.car.app.model.oma.VendorFulfillmentNotes;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.model.vendorcatalog.VendorCatalogNote;
import com.belk.car.app.webapp.forms.FulfillmentServiceForm;
import com.belk.car.app.webapp.forms.ItemRequestForm;

/**
 * @author 
 * Service Layer with method declarations
 *
 */
public interface FulfillmentServiceNotesManager extends UniversalManager {


	public List<FulfillmentServiceNotes> getFulfillmentServicesNotes(String fsID); 
	
	public List<FulfillmentServiceNotes> searchNoteBySubject(String subject,String fsID);
	
	 public FulfillmentServiceNotes addNote(FulfillmentServiceNotes fulfillmentServiceNotes);
	 
	 public Map<String,Object> getNoteByID(Long fulfillmentServiceNoteID, boolean isFS);
	 
	 public VendorFulfillmentNotes addVendorNote(VendorFulfillmentNotes vendorFulfillmentNotes);
	 
	 public List<VendorFulfillmentNotes> getVendorFulfillmentNotes(String venID);
	 
	 public List<VendorFulfillmentNotes> searchNoteByVendorID(String subject,String venID);
	
	
}
