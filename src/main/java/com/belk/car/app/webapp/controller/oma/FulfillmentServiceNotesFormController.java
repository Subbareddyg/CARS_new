
package com.belk.car.app.webapp.controller.oma;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.model.oma.FulfillmentServiceNotes;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.model.oma.VendorFulfillmentNotes;
import com.belk.car.app.service.FulfillmentServiceNotesManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.FulfillmentServiceNotesForm;

/**
 * Class for form actions
 * 
 * @author afusy45
 */
public class FulfillmentServiceNotesFormController extends BaseFormController
		implements
			DropShipConstants {

	private FulfillmentServiceNotesManager fulfillmentServiceNotesManager;

	public FulfillmentServiceNotesManager getFulfillmentServiceNotesManager() {
		return fulfillmentServiceNotesManager;
	}

	public void setFulfillmentServiceNotesManager(
			FulfillmentServiceNotesManager fulfillmentServiceNotesManager) {
		this.fulfillmentServiceNotesManager = fulfillmentServiceNotesManager;
	}

	public FulfillmentServiceNotesFormController() {
		setCommandName("fulfillmentServiceNotesForm");
		setCommandClass(FulfillmentServiceNotesForm.class);
	}

	/**
	 * Performs operations Add/Update/Save on click of Submit
	 * @Returns ModelAndView object
	 * @throws Exception 
	 * @return ModelAndView
	 * @param HttpServletResponse response
	 * @param Object command
	 * @param BindException errors
	 */
	public ModelAndView onSubmit(
			HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering  FSNotesForm 'onSubmit' method...");
		}
		boolean isEditAction = Boolean.FALSE;
		HttpSession session = request.getSession(false);
		boolean isFS = Boolean.TRUE;
		Enumeration params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String paramName = (String) params.nextElement();
			String paramValue = request.getParameter(paramName);
			log.info("paramname=" + paramName + "  paramValue=" + paramValue);
		}

		FulfillmentService fulfillmentService = (FulfillmentService) request.getSession()
				.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION);
		if(null == fulfillmentService){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		/* check if request is from vendor or fulfillment service */
		if (session.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION) != null
				&& session.getAttribute(VEN_INFO_FROM_SESSION) == null) {

			FulfillmentServiceNotes fulfillmentServiceNotes = new FulfillmentServiceNotes();
			log.debug("Vendor info doesnt exist in the session....");
			if (!StringUtils.isBlank(request.getParameter(FULFILLMENT_SERVICE_NOTE_ID))) {
				System.out.println("note id"+request.getParameter(FULFILLMENT_SERVICE_NOTE_ID));
				
				
				fulfillmentServiceNotes.setFulfillmentServiceNoteID((Long.parseLong(request
						.getParameter(FULFILLMENT_SERVICE_NOTE_ID))));
				
				//Getting notes object for note id for getting created date
				Map<String,Object> noteObjectDate = fulfillmentServiceNotesManager.getNoteByID((Long.parseLong(request
						.getParameter(FULFILLMENT_SERVICE_NOTE_ID))),isFS);
				//Set the created date in the new notes object
				fulfillmentServiceNotes.setCreatedDate((Date) noteObjectDate.get("CreatedDate"));
				fulfillmentServiceNotes.setCreatedBy((String) noteObjectDate.get("CreatedBy"));
				
				isEditAction = true;
			}
			fulfillmentServiceNotes.setFulfillmentServiceNotesDesc(request
					.getParameter(NOTE_DESCRIPTION));
			fulfillmentServiceNotes
					.setFulfillmentServiceNotesStatusCode(FULFILLMENT_SERVICE_NOTES_STATUS_ACTIVE);
			fulfillmentServiceNotes.setFulfillmentServiceNotesSubject(request
					.getParameter(FULFILLMENT_SERVICE_NOTE_SUBJECT));
			fulfillmentServiceNotes.setFulfillmentServiceID1(fulfillmentService);
			
			//this.setAuditInfo(request, fulfillmentServiceNotes);

			Map<String, Boolean> jsonModel = new HashMap<String, Boolean>();
			jsonModel.put(SUCCESS, true);
			JSONObject json = new JSONObject(jsonModel);
			try {
				this.setAuditInfo(request, fulfillmentServiceNotes);
				FulfillmentServiceNotes fulfillmentServiceNotes1 = fulfillmentServiceNotesManager
						.addNote(fulfillmentServiceNotes);
				this.setAuditInfo(request, fulfillmentServiceNotes1);
				json.put(NOTES_FIELDS, createNoteFields());
				if (isEditAction) {
					json.accumulate(NOTES_DATA, createNoteData(isEditAction,
							fulfillmentServiceNotes1.getFulfillmentServiceNoteID(),
							fulfillmentServiceNotes1.getFulfillmentServiceNotesSubject(),
							fulfillmentServiceNotes1.getFulfillmentServiceNotesDesc()));
				}
				else {
					json.accumulate(NOTES_DATA, createNoteData(isEditAction,
							fulfillmentServiceNotes1.getFulfillmentServiceNoteID(),
							fulfillmentServiceNotes1.getFulfillmentServiceNotesSubject(),
							fulfillmentServiceNotes1.getFulfillmentServiceNotesDesc(),
							fulfillmentServiceNotes1.getCreatedBy(),
							getFormatedDate(fulfillmentServiceNotes1.getCreatedDate())));
				}
				
			}
			catch (Exception e) {
				log.error("Exception while adding note to fulfillment service" + e);
			}
			if (log.isInfoEnabled()) {
				log.info("json=======in fulfillmentservice notes" + json);
				log.info("Save successful====>Note");
			}
			request.setAttribute(JSON_OBJ, json);
			session.setAttribute("saveMsg", "yes");
			

		}
		else if (null != session.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION)
				&& null != session.getAttribute(VEN_INFO_FROM_SESSION)) {

			if (log.isDebugEnabled()) {
				log.debug("inside else--if request from vendor....");
			}
			FulfillmentServiceVendor vendor = (FulfillmentServiceVendor) request.getSession()
					.getAttribute(VEN_INFO_FROM_SESSION);
			VendorFulfillmentNotes vendorFulfillmentNotes = new VendorFulfillmentNotes();
			if (!StringUtils.isBlank(request.getParameter(FULFILLMENT_SERVICE_NOTE_ID))) {
				log.info("Note ID=====>" + request.getParameter(FULFILLMENT_SERVICE_NOTE_ID));
				vendorFulfillmentNotes.setVendorFulfillmentNoteID(((Long.parseLong(request
						.getParameter(FULFILLMENT_SERVICE_NOTE_ID)))));
				isFS = Boolean.FALSE;
				Map<String,Object> noteObjectDate = fulfillmentServiceNotesManager.getNoteByID((Long.parseLong(request
						.getParameter(FULFILLMENT_SERVICE_NOTE_ID))),isFS);
				//Set the created date in the new notes object
				vendorFulfillmentNotes.setCreatedDate((Date) noteObjectDate.get("CreatedDate"));
				vendorFulfillmentNotes.setCreatedBy((String) noteObjectDate.get("CreatedBy"));
				isEditAction = true;
			}
			if (log.isDebugEnabled()) {
				log.debug("Ven info =>:" + vendor);
			}
			vendorFulfillmentNotes.setVendorFulfillmentNotesText((request
					.getParameter(NOTE_DESCRIPTION)));
			vendorFulfillmentNotes
					.setVendorFulfillmentNotesStatusCode(FULFILLMENT_SERVICE_NOTES_STATUS_ACTIVE);
			vendorFulfillmentNotes.setVendorFulfillmentNotesSubject((request
					.getParameter(FULFILLMENT_SERVICE_NOTE_SUBJECT)));
			vendorFulfillmentNotes.setFulfillmentServiceID(fulfillmentService);
			vendorFulfillmentNotes.setVendorID(vendor.getVndr());

			

			Map<String, Boolean> jsonModel = new HashMap<String, Boolean>();
			jsonModel.put(SUCCESS, true);
			JSONObject json = new JSONObject(jsonModel);
			try {
				this.setAuditInfo(request, vendorFulfillmentNotes);
				VendorFulfillmentNotes vendorFulfillmentNotes1 = fulfillmentServiceNotesManager
						.addVendorNote(vendorFulfillmentNotes);

				json.put(NOTES_FIELDS, createNoteFields());
				if (log.isDebugEnabled()) {
					log.debug("vendorFulfillmentNotes1.getVendorFulfillmentNoteID()..json"+ vendorFulfillmentNotes1.getVendorFulfillmentNoteID());
				}
				if (isEditAction) {
					json.accumulate(NOTES_DATA, createNoteData(isEditAction,
							vendorFulfillmentNotes1.getVendorFulfillmentNoteID(),
							vendorFulfillmentNotes1.getVendorFulfillmentNotesSubject(),
							vendorFulfillmentNotes1.getVendorFulfillmentNotesText()));
				}
				else {
					json.accumulate(NOTES_DATA, createNoteData(isEditAction,
							vendorFulfillmentNotes1.getVendorFulfillmentNoteID(),
							vendorFulfillmentNotes1.getVendorFulfillmentNotesSubject(),
							vendorFulfillmentNotes1.getVendorFulfillmentNotesText(),
							vendorFulfillmentNotes1.getCreatedBy(),
							getFormatedDate(vendorFulfillmentNotes1.getCreatedDate())));
				}
			}
			catch (Exception e) {
				log.error("Exception while adding note to vendor" + e);
			}
			if (log.isInfoEnabled()) {
				log.info("json=======in vendor" + json);
			}
			request.setAttribute(JSON_OBJ, json);
			session.setAttribute("saveMsg", "yes");
			

		}else if(null == session.getAttribute(FULFILLMENT_SERVICE_FROM_SESSION)
				&& null == session.getAttribute(VEN_INFO_FROM_SESSION)){
			return new ModelAndView("redirect:/mainMenu.html");
		}

		return new ModelAndView(AJAX_RETURN);
	}

	/**
	 * Method to create note fields
	 * 
	 * @return JSONArray
	 * @throws JSONException
	 */
	public JSONArray createNoteFields()
			throws JSONException {
		JSONArray carFields = new JSONArray();

		carFields.put(createNoteType(NOTEID_NOTE_FIELD, "long"));
		carFields.put(createNoteType(SUBJECT_NOTE_FIELD, "string"));
		carFields.put(createNoteType(NOTE_DESCRIPTION, "string"));
		return carFields;
	}

	/**
	 * Method to create note dataType
	 * 
	 * @param name
	 * @param type
	 * @return JSONObject
	 * @throws JSONException
	 */
	public JSONObject createNoteType(String name, String type)
			throws JSONException {
		JSONObject dataType = new JSONObject();
		dataType.put(NAME_DATATYPE_NOTES, name);
		dataType.put(NOTETYPE_DATATYPE, type);
		return dataType;
	}

	/**
	 * Method to create notes data
	 * 
	 * @param isEdit
	 * @param noteid
	 * @param subject
	 * @param notetext
	 * @return JSONArray
	 * @throws JSONException
	 */
	public JSONArray createNoteData(boolean isEdit, long noteid, String subject, String notetext)
			throws JSONException {
		JSONArray data = new JSONArray();
		data.put(isEdit);
		data.put(noteid);
		data.put(subject);
		data.put(notetext);
		return data;

	}

	/**
	 * For add method
	 * 
	 * @param isEdit
	 * @param noteid
	 * @param subject
	 * @param notetext
	 * @param dateCreated
	 * @param createdBy
	 * @return JSONArray
	 * @throws JSONException
	 */
	public JSONArray createNoteData(
			boolean isEdit, long noteid, String subject, String notetext, String createdBy,
			String dateCreated)
			throws JSONException {
		JSONArray data = new JSONArray();
		data.put(isEdit);
		data.put(noteid);
		data.put(subject);
		data.put(notetext);
		data.put(dateCreated);
		data.put(createdBy);
		return data;

	}

	/**
	 * To get formatted date
	 * 
	 * @param Date date
	 * @return String
	 */
	private String getFormatedDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		if (null != date) {
			return dateFormat.format(date);

		}
		return null;
	}

}
