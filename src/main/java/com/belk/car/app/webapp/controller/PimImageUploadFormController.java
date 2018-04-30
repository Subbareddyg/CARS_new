package com.belk.car.app.webapp.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.model.Car;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.VendorStyleType;
import com.belk.car.app.model.vendorimage.VendorImage;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.PIMFtpImageManager;
import com.belk.car.app.service.VendorImageManager;
import com.belk.car.product.integration.response.data.IntegrationResponseData;

/**
 * @author AFUTXD3
 * 
 */
public class PimImageUploadFormController extends BaseFormController {

	protected VendorImageManager vendorImageManager;
	protected VendorImage vendorImage;
	protected CarManager carManager;
	protected PIMFtpImageManager pimFtpImageManager;

	public void setPimFtpImageManager(PIMFtpImageManager pimFtpImageManager) {
		this.pimFtpImageManager = pimFtpImageManager;
	}

	public VendorImageManager getVendorImageManager() {
		return vendorImageManager;
	}

	public void setVendorImageManager(VendorImageManager vendorImageManager) {
		this.vendorImageManager = vendorImageManager;
	}

	public VendorImage getVendorImage() {
		return vendorImage;
	}

	public void setVendorImage(VendorImage vendorImage) {
		this.vendorImage = vendorImage;
	}

	public CarManager getCarManager() {
		return carManager;
	}

	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("enteringPIMImageUploadFormController 'ON CLICK GETIMAGE BUTTON'");
		}

		String carId = request.getParameter("carId");
		Map<String, Object> model = new HashMap<String, Object>();

		try {

		if (StringUtils.isNotBlank(carId)) { 
			Car car = this.carManager.getCarFromId(new Long(carId));
			
            /** PIM Phase 2: outfits/patterns require getGroup request for images **/
            VendorStyle vs = car.getVendorStyle();
            if (vs!=null && !VendorStyleType.PRODUCT.equals(vs.getVendorStyleType().getCode())
                    && !VendorStyleType.PATTERN_CONS_VS.equals(vs.getVendorStyleType().getCode())) {
                pimFtpImageManager.uploadOrDeletePimImagesByCarForGroups(car);
            }
            else {
                pimFtpImageManager.uploadOrDeletePimImagesByCar(car);
            }
			model.put("Upload Status","Success");
		}
		} catch (Throwable e) {
			if(log.isErrorEnabled())
				log.debug("Error occured"+e.getMessage());
			model.put("Upload Status","Failed");

		}
		return new ModelAndView("redirect:/editCarForm.html?carId=" + carId);

	}
}
