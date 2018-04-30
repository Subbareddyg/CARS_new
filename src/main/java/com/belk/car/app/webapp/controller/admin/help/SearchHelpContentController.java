package com.belk.car.app.webapp.controller.admin.help;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.service.HelpContentManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class SearchHelpContentController extends BaseFormController {

	private transient final Log log = LogFactory
			.getLog(SearchHelpContentController.class);

	private HelpContentManager manager;

	/**
	 * @param productManager the productManager to set
	 */
	public void setHelpContentManager(HelpContentManager manager) {
		this.manager = manager;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering help content 'handleRequest' method...");
		}

		return new ModelAndView(getSuccessView(), Constants.HELP_CONTENT_LIST,
				manager.getAllHelpContent());
	}

}
