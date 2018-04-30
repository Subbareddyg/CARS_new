package com.belk.car.app.webapp.controller.admin.pattern;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarSearchCriteria;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.VendorStyleSearchCriteria;
import com.belk.car.app.webapp.controller.BaseController;

public class RemovePatternController extends BaseController {

	private transient final Log log = LogFactory
			.getLog(RemovePatternController.class);
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Long patternId = ServletRequestUtils.getLongParameter(request, "patternId");
		if (patternId == null) {
			if (log.isDebugEnabled())
				log.debug("Pattern Id was null. Redirecting...");
			return new ModelAndView("redirect:dashBoard.html");
		}

		String action = request.getParameter("action");

		if ("remove".equals(action)) {
			VendorStyle pattern = this.getCarManager().getVendorStyle(patternId);

			VendorStyleSearchCriteria vsCriteria = new VendorStyleSearchCriteria() ;
			vsCriteria.setChildrenOnly(true);
			vsCriteria.setVendorStyleId(patternId) ;

			CarSearchCriteria csCriteria = new CarSearchCriteria() ;
			csCriteria.setIgnoreUser(true) ;
			csCriteria.setVendorNumber(pattern.getVendorNumber());
			csCriteria.setVendorNumber(pattern.getVendorStyleNumber());
			csCriteria.setStatusCd(Status.ACTIVE);
			csCriteria.setSearchChildVendorStyle(false) ;
			List<Car> cars = this.getCarManager().searchCars(csCriteria);
			boolean canDelete = true ;
			if (cars != null && !cars.isEmpty()) {
				//There are cars associated with the Pattern
				//What do we do?
				canDelete = true ;
				//Delete Associated CAR...
				for(Car car : cars) {
					car.setStatusCd(Status.DELETED) ;
					this.getCarManager().save(car) ;
				}
			} else {
				canDelete = true ;
			}

			if (canDelete) {
				pattern.setStatusCd(Status.DELETED);

				List<VendorStyle> childProducts = this.getCarManager().searchVendorStyle(vsCriteria);
				if (childProducts != null) {
					for (VendorStyle product : childProducts) {
						product.setParentVendorStyle(null) ;
						this.getCarManager().save(product) ;
					}
				}

				this.getCarManager().save(pattern) ;

				//this.getCarManager().remove(VendorStyle.class, new Long(patternId));
				//vs.setParentVendorStyle(null);
				//this.getCarManager().save(vs);
				//saveMessage(request, getText("department.attribute.deleted", pattern
				//		.getName(), request.getLocale()));
			}
		}

		return new ModelAndView(this.getSuccessView());
	}
}