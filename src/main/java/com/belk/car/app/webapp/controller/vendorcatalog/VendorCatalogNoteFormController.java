package com.belk.car.app.webapp.controller.vendorcatalog;

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
import com.belk.car.app.model.vendorcatalog.VendorCatalog;
import com.belk.car.app.model.vendorcatalog.VendorCatalogNote;
import com.belk.car.app.service.VendorCatalogManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.VendorCatalogNotesForm;

/**
 * Purpose: Form controller for adding/updating vendor catalog notes Project:
 * EC-2009-00092 - FY 10 eCommerce Drop-Ship Phase I Initial Requirements:
 * BR.002 Description: This class is used for adding/updating vendor catalog
 * notes.
 * 
 * @author afusya2
 */
public class VendorCatalogNoteFormController extends BaseFormController
		implements
			DropShipConstants {
	private VendorCatalogManager vendorCatalogManager;

	/**
	 * @return the vendorCatalogManager
	 */
	public VendorCatalogManager getVendorCatalogManager() {
		return vendorCatalogManager;
	}

	/**
	 * @param vendorCatalogManager the vendorCatalogManager to set
	 */
	public void setVendorCatalogManager(VendorCatalogManager vendorCatalogManager) {
		this.vendorCatalogManager = vendorCatalogManager;
	}

	public VendorCatalogNoteFormController() {
		setCommandName("vendorCatalogNotesForm");
		setCommandClass(VendorCatalogNotesForm.class);
	}

	/**
	 * Performs operations Add/Update/Save on click of Submit
	 * @Returns ModelAndView object
	 * @throws Exception
	 *            
	 */
	@SuppressWarnings("unchecked") public ModelAndView onSubmit(
			HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors)
			throws Exception {
		if(log.isDebugEnabled()){
		log.debug("entering  vendorCatalogNotesForm 'onSubmit' method...");
		}
		boolean isEditAction = false;

		/**
		 * Creating the JSON object to be returned to ajax
		 */
		VendorCatalogNote vendorCatalogNewNote = new VendorCatalogNote();
		Enumeration params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String paramName = (String) params.nextElement();
			String paramValue = request.getParameter(paramName);
			if(log.isDebugEnabled()){
			log.debug("paramname=" + paramName + "  paramValue=" + paramValue);
			}
		}

		// HttpSession session = request.getSession();
		// Locale locale = request.getLocale();
		if (!StringUtils.isBlank(request.getParameter(VENDOR_CATALOG_NOTEID))) {
			log.debug("request.getParameter(noteid)..."
					+ request.getParameter(VENDOR_CATALOG_NOTEID));
			vendorCatalogNewNote.setCatalogNoteID(Long.parseLong(request
					.getParameter(VENDOR_CATALOG_NOTEID)));
			isEditAction = true;
		}
		/** get the vendor catalog id */
		HttpSession session = request.getSession(false);
		VendorCatalog vendorCatalog = null;
		if(null != session.getAttribute(VENDOR_CATALOG)){
		vendorCatalog = (VendorCatalog) request.getSession().getAttribute(
				VENDOR_CATALOG);
		}else {
			//User session is timed out.
			return new ModelAndView("redirect:/mainMenu.html");
		}
		vendorCatalogNewNote.setNoteText(request.getParameter(NOTE_TEXT));
		vendorCatalogNewNote.setStatusCD(ACTIVE);
		vendorCatalogNewNote.setSubject(request.getParameter(NOTE_SUBJECT).trim());
		if(log.isDebugEnabled()){
		log.debug("note text..." + request.getParameter(NOTE_TEXT));
		log.debug("note subject..." + request.getParameter(NOTE_SUBJECT).trim());
		}
		vendorCatalogNewNote.setVendorCatalog(vendorCatalog);

		this.setAuditInfo(request, vendorCatalogNewNote);

		Map<String, Boolean> jsonModel = new HashMap<String, Boolean>();
		jsonModel.put(SUCCESS, Boolean.TRUE);
		// jsonModel.put(SUCCESS, new Boolean(true));
		JSONObject json = new JSONObject(jsonModel);

		try {
			/** add new note to data base */
			VendorCatalogNote vendorCatalogNewnote = vendorCatalogManager
					.addNote(vendorCatalogNewNote);
			json.put(NOTES_FIELDS, createNoteFields());

			if (isEditAction) {
				json.accumulate(NOTES_DATA, createNoteData(isEditAction, vendorCatalogNewnote
						.getCatalogNoteID(), vendorCatalogNewnote.getSubject(),
						vendorCatalogNewnote.getNoteText()));
			}
			else {
				json.accumulate(NOTES_DATA, createNoteData(isEditAction, vendorCatalogNewnote
						.getCatalogNoteID(), vendorCatalogNewnote.getSubject(),
						vendorCatalogNewnote.getNoteText(), vendorCatalogNewnote.getCreatedBy(),
						getFormatedDate(vendorCatalogNewnote.getCreatedDate())));
			}
		}
		catch (Exception e) {
			//e.printStackTrace();
			if (log.isErrorEnabled()){
				log.error("Failed to add vendor catalog note..", e);
			}
		}
		if(log.isDebugEnabled()){
		log.debug("json=======in vendorcatalog notes" + json);
		log.debug("vendor catalog note save Successful!!!");
		}
		request.setAttribute(JSON_OBJ, json);
		session.setAttribute(SHOW_MSG, "yes");
		return new ModelAndView(AJAX_RETURN);
	}

	public JSONArray createNoteFields()
			throws JSONException {
		JSONArray carFields = new JSONArray();
		carFields.put(createNoteType(NOTEID_NOTE_FIELD, "long"));
		carFields.put(createNoteType(SUBJECT_NOTE_FIELD, "string"));
		carFields.put(createNoteType(NOTE_DESCRIPTION, "string"));
		return carFields;
	}

	public JSONObject createNoteType(String name, String type)
			throws JSONException {
		JSONObject dataType = new JSONObject();
		dataType.put(NAME_DATATYPE_NOTES, name);
		dataType.put(NOTETYPE_DATATYPE, type);
		return dataType;
	}

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
	 * @param noteid
	 * @param subject
	 * @param notetext
	 * @param dateCreated
	 * @param createdBy
	 * @return
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
	 * @param date
	 * @return
	 */
	private String getFormatedDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		String currentDate=null;
		if (null != date) {
			 currentDate = dateFormat.format(date);
			
		}
		return currentDate;
	}

	
}
