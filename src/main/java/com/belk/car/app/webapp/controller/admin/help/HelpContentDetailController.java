/**
 * 
 */
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

/**
 * @author antoniog
 *
 */
public class HelpContentDetailController extends BaseFormController implements Controller {

	private transient final Log log = LogFactory.getLog(HelpContentDetailController.class);

	private HelpContentManager manager;	

	/**
	 * @param manager the manager to set
	 */
	public void setHelpContentManager(HelpContentManager manager) {
		this.manager = manager;
	}



	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
        Long contentId = ServletRequestUtils.getLongParameter(request, "id");      
        if (contentId == null) {
        	log.debug("Content id was null. Redirecting...");
            return new ModelAndView("redirect:dashBoard.html");
        }
        
        HelpContent content = manager.getHelpContent(contentId.longValue());

        return new ModelAndView(this.getSuccessView(), "content", content);
	}

}
