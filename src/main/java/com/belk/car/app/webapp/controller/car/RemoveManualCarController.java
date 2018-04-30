package com.belk.car.app.webapp.controller.car;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.model.Status;
import com.belk.car.app.model.car.ManualCar;
import com.belk.car.app.service.ManualCarManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class RemoveManualCarController extends BaseFormController implements Controller{

    private ManualCarManager manualCarManager;
    
	public void setManualCarManager(ManualCarManager manualCarManager) {
		this.manualCarManager = manualCarManager;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String attrId = request.getParameter("id");
		if (!StringUtils.isBlank(attrId)) {
			ManualCar manualCar = manualCarManager.getManualCar(Long.valueOf(attrId));
			manualCar.setStatusCd(Status.DELETED);
			if (manualCar != null) {				
				manualCarManager.save(manualCar);
			}
		}
		return new ModelAndView(getSuccessView());
	}
}
