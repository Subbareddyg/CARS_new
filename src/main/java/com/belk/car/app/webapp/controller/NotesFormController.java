/**
 * 
 */
package com.belk.car.app.webapp.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarNote;
import com.belk.car.app.service.CarManager;
import com.belk.car.util.DateUtils;
import com.belk.car.util.NotesComparator;


public class NotesFormController extends BaseFormController {
	private transient final Log log = LogFactory
	.getLog(NotesFormController.class);
	private CarManager mgr = null;

	public void setCarManager(CarManager carManager) {
		this.mgr = carManager;
	}

	public CarManager getCarManager(){
		return this.mgr;
	}
    private HashSet command;

    public NotesFormController() {
    	if (log.isInfoEnabled())
    		log.info("Got into NotesFormController");
        this.setCommandClass(Car.class);
        this.setCommandName("carNotesForm");
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

		String carId=request.getParameter("carId");
		return mgr.getCarFromId(Long.valueOf(carId));
	}

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, 
			Object command, 
			BindException errors)
			throws Exception {
				
		if (log.isInfoEnabled())
			log.info("Got into NotesFormController");

		Car car=(Car)command;

        String message=request.getParameter("carNotes.Message:");
        String carNoteTypeCd=request.getParameter("carNotes.noteTypeCd:");
        String isPrivate=request.getParameter("carNotes.isPrivate:");

        if (StringUtils.isBlank(carNoteTypeCd)){
        	carNoteTypeCd="CAR_NOTES";
        }
        
        boolean isPrivateFlag="Y".equalsIgnoreCase(isPrivate);//by default always false

        car = this.getCarManager().addCarNote(car, isPrivateFlag, message,
				carNoteTypeCd, this.getLoggedInUser().getUsername());
        
        this.getCarManager().save(car);


        JSONObject object= convertNotesToJSON(car, carNoteTypeCd);
        request.setAttribute("jsonObj", object);
		return new ModelAndView("ajaxReturn") ;
	}

	private JSONObject convertNotesToJSON(Car car,String noteTypeCode) {
		Map model2 = new HashMap();
		model2.put("success", new Boolean(true));
		JSONObject json = new JSONObject(model2);
		try {
			json.put("notes_fields", createNoteFields());
			List<CarNote> col = (List) this.mgr.getCarNotesByDate(car, 200, noteTypeCode);
			Collections.sort(col,new ReverseComparator(new NotesComparator()));
			for (CarNote cn : col) {
				json.accumulate("notes_data", createNoteData(cn.getNoteType().getNoteTypeCd(),
						                                     cn.getNoteText(),
						                                     DateUtils.formatDate(cn.getCreatedDate()),
						                                     cn.getCreatedBy()));
			}
		} catch (JSONException jex) {
		}
		return json;
	}

	public JSONArray createNoteFields() throws JSONException {
		JSONArray carFields = new JSONArray();
		carFields.put(createNoteType("noteType", "string"));
		carFields.put(createNoteType("noteText", "string"));
		carFields.put(createNoteType("noteDate", "string"));
		carFields.put(createNoteType("noteUser", "string"));
		return carFields;
	}

	public JSONObject createNoteType(String name, String type)
			throws JSONException {
		JSONObject dataType = new JSONObject();
		dataType.put("name", name);
		dataType.put("type", type);
		return dataType;
	}

	public JSONArray createNoteData(String noteType, 
			                        String noteText,
			                        String noteDate,
			                        String noteUser) throws JSONException {
		JSONArray data = new JSONArray();
		data.put(noteType);
		data.put(noteText);
		data.put(noteDate);
		data.put(noteUser);
		return data;

	}

	
}