package com.belk.car.app.webapp.controller.car;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.model.car.ManualCar;
import com.belk.car.app.service.ManualCarManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class ManualCarDetailsController extends BaseFormController implements Controller {

	private transient final Log log = LogFactory.getLog(ManualCarDetailsController.class);

    private ManualCarManager manualCarManager;
    
	public void setManualCarManager(ManualCarManager manualCarManager) {
		this.manualCarManager = manualCarManager;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
        Long manualCarId = ServletRequestUtils.getLongParameter(request, "id");      
        if (manualCarId == null) {
        	log.debug("Manual Car Id was null. Redirecting...");
            return new ModelAndView("redirect:dashBoard.html");
        }

        ManualCar manualCar = manualCarManager.getManualCar(Long.valueOf(manualCarId));
        return new ModelAndView(getSuccessView(), "manualCar", manualCar);
	}
}
