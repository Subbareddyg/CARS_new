package com.belk.car.app.webapp.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.model.HelpContent;
import com.belk.car.app.service.HelpContentManager;

public class HelpContentController extends BaseFormController {

	private HelpContentManager contentManager;
	
	
	public void setContentManager(HelpContentManager contentManager) {
		this.contentManager = contentManager;
	}

	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String referer = request.getHeader("referer");
		String key = request.getParameter("key");
		if (StringUtils.isBlank(key)) {
			if(StringUtils.isNotBlank(referer)) {
				key = new URL(referer).getPath();
			} else {
				key = HelpContent.NO_HELP_CONTENT_KEY ;
			}
		}

		String section = request.getParameter("section");		
		List<HelpContent> content = null ;
		if(StringUtils.isNotBlank(section)) {
			content = new ArrayList<HelpContent>();
			HelpContent hc = contentManager.getHelpContent(key, section);
			if(hc!=null) {
				content.add(hc);
			}
			else{
				content = contentManager.getHelpContent(HelpContent.NO_HELP_CONTENT_KEY);
			}
		} else {
			section = "PAGE";
			content = contentManager.getHelpContent(key);
			if (content.isEmpty()) {
				content = contentManager.getHelpContent(HelpContent.NO_HELP_CONTENT_KEY);
			}
		}

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("contentKey", key);
		model.put("contentSection", section);
		model.put("helpContent", content);

		return new ModelAndView(this.getSuccessView(), model);
	}
}
