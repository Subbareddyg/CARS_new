package com.belk.car.app.webapp.controller.admin.help;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.model.HelpContent;
import com.belk.car.app.service.HelpContentManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class RemoveHelpContentController extends BaseFormController implements
		Controller {

	private transient final Log log = LogFactory
			.getLog(RemoveHelpContentController.class);

	private HelpContentManager manager;

	/**
	 * @param manager the manager to set
	 */
	public void setHelpContentManager(HelpContentManager manager) {
		this.manager = manager;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Long contentId = ServletRequestUtils.getLongParameter(request, "id");
		if (contentId == null) {
			log.debug("Help Content id was null. Redirecting...");
			// TODO - Change to a property view
			return new ModelAndView("redirect:dashBoard.html");
		}

		String action = getCurrentAction(request);

		if ("remove".equals(action)) {
			HelpContent content = manager.getHelpContent(contentId.longValue());
			if (content != null) {
				//manager.remove(content);
				saveMessage(request, getText("content.deleted", contentId + "",
						request.getLocale()));
			} else {
				saveMessage(request, getText("content.deletion.problem",
						contentId + "", request.getLocale()));
			}
		}
		return new ModelAndView(getSuccessView());

	}

}