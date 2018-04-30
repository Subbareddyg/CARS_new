package com.belk.car.app.webapp.controller.admin.user;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.AccessDeniedException;
import org.apache.commons.lang.StringUtils;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.model.CarUserNote;
import com.belk.car.app.model.NoteType;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.UserForm;

public class UserNotesFormController extends BaseFormController {

	private CarLookupManager carLookupManager;

	private CarManager carManager;

	public void setCarLookupManager(CarLookupManager carLookupManager) {
		this.carLookupManager = carLookupManager;
	}

	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	public UserNotesFormController() {
		setCommandName("userForm");
		setCommandClass(UserForm.class);
	}

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		log.debug("entering  userNotesForm 'onSubmit' method...");

		UserForm userForm = (UserForm) command;
		Locale locale = request.getLocale();
		String note = userForm.getNotes();

		NoteType noteType = carLookupManager.getNoteType(NoteType.DEFAULT_USER_NOTE);
		CarUserNote carUserNote = new CarUserNote();
		carUserNote.setIsExternallyDisplayable(CarUserNote.YES);
		carUserNote.setNoteText(note);
		carUserNote.setCarUser(userForm.getUser());
		carUserNote.setNoteType(noteType);
		carUserNote.setStatusCd(CarUserNote.ACTIVE);
		this.setAuditInfo(request, carUserNote) ;

		userForm.getUser().addNote(carUserNote);
		
		try {
			carManager.saveUser(userForm.getUser());
		} catch (AccessDeniedException ade) {
			// thrown by UserSecurityAdvice configured in aop:advisor userManagerSecurity
			log.warn(ade.getMessage());
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		 Map model = new HashMap();
		 model.put("id",userForm.getUser().getId());
		return new ModelAndView(getSuccessView(),model);
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

		String userId = request.getParameter("id");
		UserForm userForm = new UserForm();
		User user;
		if (userId == null && !isAddUserNote(request)) {
			user = getUserManager().getUserByUsername(request.getRemoteUser());
		} else if (!StringUtils.isBlank(userId)) {
			user = getUserManager().getUser(userId);
		} else {
			user = new User();
			user.addRole(new Role(Constants.USER_ROLE));
		}
		userForm.setUser(user);
		return userForm;
	}


	protected boolean isAddUserNote(HttpServletRequest request) {
		String method = request.getParameter("method");
		return (method != null && method.equalsIgnoreCase("addUserNote"));
	}
	
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws ServletRequestBindingException {

		Map model = new HashMap();
		List<NoteType> test =  carLookupManager.getAllNoteTypes();
		model.put("noteTypes", test);
		return model;
	}
}
