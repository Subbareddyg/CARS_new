/**
 * 
 */
package com.belk.car.app.webapp.controller.admin.pattern;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarSearchCriteria;
import com.belk.car.app.model.ContentStatus;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.VendorStyleSearchCriteria;
import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.webapp.controller.BaseController;

/**
 * @author antoniog
 *
 */
public class PatternConvertCarsController extends BaseController {

	private transient final Log log = LogFactory.getLog(PatternConvertCarsController.class);

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Long patternId = ServletRequestUtils.getLongParameter(request, "patternId");
		if (patternId == null) {
			if (log.isDebugEnabled())
				log.debug("patternId was null. Redirecting...");
			return new ModelAndView("redirect:dashBoard.html");
		}

		VendorStyle pattern = getCarManager().getVendorStyle(patternId);

		//Get all the pattern's children
		VendorStyleSearchCriteria criteria = new VendorStyleSearchCriteria();
		criteria.setVendorStyleId(patternId);
		criteria.setChildrenOnly(true);
		List<VendorStyle> patternProducts = getCarManager().searchVendorStyle(criteria);

		CarSearchCriteria carCriteria = null;//new CarSearchCriteria();
		for (VendorStyle vs : patternProducts) {
			carCriteria = new CarSearchCriteria();
			carCriteria.setVendorNumber(vs.getVendorNumber());
			carCriteria.setVendorStyleNumber(vs.getVendorStyleNumber());
			carCriteria.setStatusCd(Status.ACTIVE);
			carCriteria.setIgnoreUser(true);
			//Search based on the Pattern Child Search
			carCriteria.setSearchChildVendorStyle(false);

			//Search for Vendor Style Cars
			List<Car> cars = getCarManager().searchCars(carCriteria);
			if (cars != null && !cars.isEmpty()) {
				for (Car car : cars) {
					//If NO CAR was found for Pattern then set the first Car as the Pattern CAR
					//Update the VendorStyle for the CAR to Pattern CAR
					car.setVendorStyle(pattern);
					this.getCarManager().save(car);
				}
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pattern", pattern);
		map.put("patternProducts", patternProducts);

		return new ModelAndView("admin/pattern/detail", map);
	}

}
