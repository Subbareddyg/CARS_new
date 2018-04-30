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
public class PatternMergeCarsController extends BaseController {

	private transient final Log log = LogFactory.getLog(PatternMergeCarsController.class);

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

		//Check to see if there are CARS already associate with Pattern
		Car patternCar = null;
		CarSearchCriteria carCriteria = new CarSearchCriteria();
		carCriteria.setVendorNumber(pattern.getVendorNumber());
		carCriteria.setVendorStyleNumber(pattern.getVendorStyleNumber());
		carCriteria.setWorkflowStatus(WorkflowStatus.INITIATED);
		carCriteria.setStatusCd(Status.ACTIVE);
		carCriteria.setIgnoreUser(true);
		//Do not search based on the Pattern Child Search
		carCriteria.setSearchChildVendorStyle(false);
		List<Car> cars = getCarManager().searchCars(carCriteria);
		Map<String, VendorSku> skuMap = new HashMap<String, VendorSku>();
		Map<String, VendorSku> skuMapAdd = new HashMap<String, VendorSku>();
		if (cars != null && !cars.isEmpty()) {
			//Choose one of the CARS as the Pattern Cars
			patternCar = cars.get(0);
			for (VendorSku sku: patternCar.getVendorSkus()) {
				skuMap.put(sku.getBelkSku(), sku) ;
			}
		}

		
		
		for (VendorStyle vs : patternProducts) {
			carCriteria = new CarSearchCriteria();
			carCriteria.setVendorNumber(vs.getVendorNumber());
			carCriteria.setVendorStyleNumber(vs.getVendorStyleNumber());
			carCriteria.setWorkflowStatus(WorkflowStatus.INITIATED); 
			carCriteria.setStatusCd(Status.ACTIVE);
			carCriteria.setIgnoreUser(true);
			//Search based on the Pattern Child Search
			carCriteria.setSearchChildVendorStyle(false);

			//Search for Vendor Style Cars
			cars = getCarManager().searchCars(carCriteria);
			if (cars != null && !cars.isEmpty()) {
				for (Car car : cars) {
					//If NO CAR was found for Pattern then set the first Car as the Pattern CAR
					//Update the VendorStyle for the CAR to Pattern CAR
					if (patternCar == null) {
						patternCar = car;
						patternCar.setVendorStyle(pattern);
						for (VendorSku sku: patternCar.getVendorSkus()) {
							skuMap.put(sku.getBelkSku(), sku) ;
						}
						//this.getCarManager().save(patternCar);
					} else {
						//Delete the car
						car.setStatusCd(Status.DELETED);
					
						this.getCarManager().save(car);
						if(car.getCarImages() !=null && !car.getCarImages().isEmpty() ) {
							//Re-point all the uploaded CAR images into parent car_id
							this.getCarManager().loadChildCarImagesintoPatternCAR(car, patternCar);
						}
						//For all other CARS move them the new Pattern CAR
						//Move the SKU to the patternCar
						Set<VendorSku> skus = car.getVendorSkus();
						for (VendorSku sku : skus) {
							if (!skuMap.containsKey(sku.getBelkSku()) && !skuMapAdd.containsKey(sku.getBelkSku())) {
								skuMapAdd.put(sku.getBelkSku(), sku) ;
							}
						}
						//this.getCarManager().save(sku);
					}
				}
			}
		}
		
		if (patternCar != null) {
			if (!skuMapAdd.isEmpty() || !skuMap.isEmpty()) {
				if (!skuMapAdd.isEmpty()) {
					for (VendorSku sku : skuMapAdd.values()) {
						sku.setCar(patternCar) ;
						patternCar.getVendorSkus().add(sku) ;
					}
				}
				//Clear Car Samples
				patternCar.getCarSamples().clear() ;
				
				if (patternCar.getContentStatus().getCode().equals(ContentStatus.SENT_TO_CMP)) {
					patternCar.setContentStatus((ContentStatus) this.getCarLookupManager().getById(ContentStatus.class, ContentStatus.RESEND_TO_CMP));
				}
				this.getCarManager().save(patternCar) ;
			}
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pattern", pattern);
		map.put("patternProducts", patternProducts);

		return new ModelAndView("admin/pattern/detail", map);
	}

}
