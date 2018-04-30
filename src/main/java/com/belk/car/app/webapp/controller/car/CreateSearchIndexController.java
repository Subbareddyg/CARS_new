package com.belk.car.app.webapp.controller.car;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.webapp.controller.BaseController;
import com.belk.car.lucene.SearchWorker;

public class CreateSearchIndexController extends BaseController {

	protected SearchWorker searchWorker;
	
	/**
	 * @param searchWorker the searchWorker to set
	 */
	public void setSearchWorker(SearchWorker searchWorker) {
		this.searchWorker = searchWorker;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Integer numIndexed = searchWorker.index();

		Map<String, Object> context = new HashMap<String, Object>(); //the object to send to the jsp
		context.put("numIndexed", numIndexed);

		return new ModelAndView(getSuccessView(), "context", context);
	}

}
