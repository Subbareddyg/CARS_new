package com.belk.car.app.webapp.controller.car;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.car.ManualCarProcessStatus;
import com.belk.car.app.service.ManualCarManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class ManualCarController extends BaseFormController {
	
    private transient final Log log = LogFactory.getLog(ManualCarController.class);
    
    private ManualCarManager manualCarManager;
    
	public void setManualCarManager(ManualCarManager manualCarManager) {
		this.manualCarManager = manualCarManager;
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
        if (log.isDebugEnabled()) {
            log.debug("entering manual car's 'handleRequest' method...");
        }
        
        Map<String, Object> context = new HashMap<String, Object>();
        context.put(Constants.MANUAL_CAR_LIST, manualCarManager.getManualCars(ManualCarProcessStatus.INITIATED));
        context.put(Constants.MANUAL_CAR_PROCESS_STATUS_LIST, manualCarManager.getAllMCProcessStatus());
        
        return new ModelAndView(getSuccessView(), context);
    }
}
