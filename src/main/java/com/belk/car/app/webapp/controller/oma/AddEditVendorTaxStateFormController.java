
package com.belk.car.app.webapp.controller.oma;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.oma.DropshipTaxState;
import com.belk.car.app.model.oma.State;
import com.belk.car.app.service.VendorFulfillmentServiceManager;
import com.belk.car.app.service.VendorTaxStateManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.VendorReturnForm;
import com.belk.car.util.DateUtils;

/**
 * @author afusy07 priyanka_gadia@syntelinc.com
 * @Date 16-Dec-2009 This class takes the tax values from request and saves to
 *       database. After saving creates a json object to update the content in
 *       display table.
 */
public class AddEditVendorTaxStateFormController extends BaseFormController
		implements
			DropShipConstants {

	/**
	 * 
	 */
	private static final String LAST_UPDATED_BY = "lastUpdatedBy";
	/**
	 * 
	 */
	private static final String DATE_LAST_UPDATED = "dateLastUpdated";
	/**
	 * 
	 */
	private static final String TAX_STATUS = "status";
	/**
	 * Setting the command class
	 */

	private VendorTaxStateManager vendorTaxStateManager;
	private VendorFulfillmentServiceManager vendorFulfillmentServiceManager;
	private static final String SHOW_MSG = "showMsg";

	public void setVendorTaxStateManager(VendorTaxStateManager vendorTaxStateManager) {
		this.vendorTaxStateManager = vendorTaxStateManager;
	}

	public AddEditVendorTaxStateFormController() {
		setCommandName("vendorReturnForm");
		setCommandClass(VendorReturnForm.class);
	}
	public VendorFulfillmentServiceManager getVendorFulfillmentServiceManager() {
		return vendorFulfillmentServiceManager;
	}

	public void setVendorFulfillmentServiceManager(
			VendorFulfillmentServiceManager vendorFulfillmentServiceManager) {
		this.vendorFulfillmentServiceManager = vendorFulfillmentServiceManager;
	}

	/**
	 * @param request
	 * @param response
	 * @return void
	 */
	public void getStates(HttpServletRequest request, HttpServletResponse response) {
		/*
		 * Retrieving the values for States Dropdown If present in session get
		 * it else take from DB
		 */
		HttpSession session = request.getSession();
		
			List<State> states = vendorFulfillmentServiceManager.getStatesForVendorTax();
			List<State> uselessStates=vendorFulfillmentServiceManager.getStates();
			if (states != null) {
				log.debug("no of states inactive or new= "+states.size());
				log.debug("no of states active= "+uselessStates.size());
				session.setAttribute(STATES, states);
			}
		
	}
	private transient final Log log = LogFactory.getLog(AddEditVendorTaxStateFormController.class);

	/**
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return
	 * @throws Exception
	 * @TODO
	 */
	public ModelAndView onSubmit(
			HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors)
			throws Exception {

		DropshipTaxState vendorTaxStateModel = new DropshipTaxState();
		if(log.isDebugEnabled()){
			log.debug("vendorTaxStateId=" + request.getParameter(TAX_ID));
		}
		State state = vendorTaxStateManager.getStateByStateId(request.getParameter(STATE));
		if (StringUtils.isNotBlank(request.getParameter(TAX_ID))) {
			vendorTaxStateModel.setVendorTaxStateId(Long.parseLong(request.getParameter(TAX_ID)));
		}
		else{
			List<DropshipTaxState> taxStateList=vendorTaxStateManager.getVendorTaxStatesSearch(state.getStateName(), "ALL");
			DropshipTaxState taxState=null;
			if(taxStateList !=null && taxStateList.size()>0){
				log.debug("Got the vendor tax state");
				taxState=taxStateList.get(0);
			}
			if(taxState!=null){
				log.debug("Got the existing tax state");
				vendorTaxStateManager.deleteTaxState(taxState);
			}
			else{
				log.debug("No exisiting tax state");
			}
		}
		if(log.isDebugEnabled()){
			log.debug("state code from request= " + request.getParameter(STATE));
		}
		// Get state from DB to set it in vendor tax state model
		
		vendorTaxStateModel.setState(state);
		vendorTaxStateModel.setStatus(Status.ACTIVE);
		vendorTaxStateModel.setTaxIssue(request.getParameter(ISSUE));
		setAuditInfo(request, vendorTaxStateModel);


		/**
		 * Creating the JSON object to be returned to ajax
		 */
		Map<String, Boolean> model2 = new HashMap<String, Boolean>();
		model2.put(SUCCESS, true);
		JSONObject json = new JSONObject(model2);

		try {
			// Saving the vendor tax model to database
			DropshipTaxState vendorTaxStateModel2 = vendorTaxStateManager
					.saveTaxStates(vendorTaxStateModel);
			if(log.isDebugEnabled()){
			log.debug("DropshipTaxState from DB=" + vendorTaxStateModel2.toString());
			}
			HttpSession session = request.getSession();
			session.setAttribute(SHOW_MSG, "yes");
			this.getStates(request, response);
			String taxIssue = vendorTaxStateModel2.getTaxIssue();
			long vendorTaxId = vendorTaxStateModel2.getVendorTaxStateId();
			Date dateLastUpdated = vendorTaxStateModel2.getUpdatedDate();
			String date = DateUtils.formatDate(dateLastUpdated, "MM/dd/yyyy");
			String lastUpdatedBy = vendorTaxStateModel2.getUpdatedBy();
			String status = vendorTaxStateModel2.getStatus();
			
			/**
			 * Setting the tax JSON object to be returned to ajax
			 */
			json.put(TAX_FIELDS, createTaxFields());
			json.accumulate(TAX_DATA, createNoteData(vendorTaxId, taxIssue, status, date,
					lastUpdatedBy));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		if(log.isDebugEnabled()){
		log.debug("json=======" + json);
		}
		request.setAttribute(JSON_OBJ, json);
		return new ModelAndView(AJAX_RETURN);
	}

	/**
	 * @return
	 * @throws JSONException
	 * @return JSONArray
	 * @TODO Create Json object
	 */
	public JSONArray createTaxFields()
			throws JSONException {
		JSONArray carFields = new JSONArray();

		carFields.put(createNoteType(TAX_ID, LONG));
		carFields.put(createNoteType(TAX_ISSUE, STRING));
		carFields.put(createNoteType(TAX_STATUS, STRING));
		carFields.put(createNoteType(DATE_LAST_UPDATED, STRING));
		carFields.put(createNoteType(LAST_UPDATED_BY, STRING));
		return carFields;
	}

	/**
	 * @param name
	 * @param type
	 * @return
	 * @throws JSONException
	 * @return JSONObject
	 * @TODO Create the note type for json object
	 */
	public JSONObject createNoteType(String name, String type)
			throws JSONException {
		JSONObject dataType = new JSONObject();
		dataType.put(NAME, name);
		dataType.put(TYPE, type);
		return dataType;
	}

	/**
	 * @param taxid
	 * @param noteText
	 * @param status
	 * @param date
	 * @param lastUpdatedBy
	 * @return
	 * @throws JSONException
	 * @return JSONArray
	 * @TODO Put data in json object
	 */
	public JSONArray createNoteData(
			long taxid, String noteText, String status, String date, String lastUpdatedBy)
			throws JSONException {
		JSONArray data = new JSONArray();
		data.put(taxid);
		data.put(noteText);
		data.put(status);
		data.put(date);
		data.put(lastUpdatedBy);
		return data;

	}

}
