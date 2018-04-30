/**
 * 
 */
package com.belk.car.app.webapp.controller.admin.statistics;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.model.statistics.CacheStats;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.webapp.controller.BaseController;

/**
 * @author antoniog
 *
 */
public class CacheStatisticsController extends BaseController {

	private transient final Log log = LogFactory
			.getLog(CacheStatisticsController.class);

	private CarLookupManager carLookupManager;

	/**
	 * @param carLookupManager the carLookupManager to set
	 */
	public void setCarLookupManager(CarLookupManager carLookupManager) {
		this.carLookupManager = carLookupManager;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map model = new HashMap();
		CacheStats stats = carLookupManager.getCacheStatistics();
		model.put("statistics",stats);
		return new ModelAndView(getSuccessView(), model);
	}

}
