package com.belk.car.app.webapp.controller.admin.pattern;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarSearchCriteria;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.webapp.controller.BaseController;

public class RemoveVendorStyleController extends BaseController {

	private transient final Log log = LogFactory
			.getLog(RemoveVendorStyleController.class);
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Long patternId = ServletRequestUtils.getLongParameter(request, "patternId");
		Long vendorStyleId = ServletRequestUtils.getLongParameter(request, "vendorStyleId");
		if (patternId == null || vendorStyleId == null) {
			log.debug("Pattern Id or Vendor Style Id was null. Redirecting...");
			return new ModelAndView("redirect:dashBoard.html");
		}

		String action = request.getParameter("action");

		if ("remove".equals(action)) {
			VendorStyle pattern = this.carManager.getVendorStyle(patternId) ;
			VendorStyle vs = this.getCarManager().getVendorStyle(vendorStyleId);

			CarSearchCriteria criteria = new CarSearchCriteria() ;
			criteria.setVendorNumber(vs.getVendorNumber()) ;
			criteria.setVendorStyleNumber(vs.getVendorStyleNumber()) ;
			criteria.setSearchChildVendorStyle(true) ;
			criteria.setIgnoreUser(true);
			criteria.setStatusCd(Status.ACTIVE);
			
			List<Car> carsWithPattern = this.carManager.searchCars(criteria) ;

			if (carsWithPattern != null && !carsWithPattern.isEmpty()) {
				for(Car car: carsWithPattern) {
					//Only Vendor Style in the Pattern and re-associate the CARs Vendor Style
					if (car.getVendorStyle().isPattern()) {
						if (car.getVendorStyles().size() == 1) {
							car.setVendorStyle(vs) ;
							this.carManager.save(car) ;
						} else {
							//Remove SKU from all the Vendor Style associated with this Pattern
							Set<VendorSku> skus = car.getVendorSkus() ;
							if (skus != null) {
								Set<VendorSku> removeSkus = new HashSet<VendorSku>();
								for (VendorSku sku : skus) {
									if (sku.getVendorStyle().getVendorStyleId() == vendorStyleId) { 
										removeSkus.add(sku);
									}
								}
	
								if (!removeSkus.isEmpty()) {
									car.getVendorSkus().removeAll(removeSkus);
									car.resetVendorStyles();
									this.carManager.save(car) ;
								}
							}
						}
					}
				}
			}
			
			vs.setParentVendorStyle(null);
			this.getCarManager().save(vs);
			//saveMessage(request, getText("department.attribute.deleted", pattern
			//		.getName(), request.getLocale()));
		}

		return new ModelAndView("redirect:/admin/pattern/detail.html?patternId="+patternId);
	}
}