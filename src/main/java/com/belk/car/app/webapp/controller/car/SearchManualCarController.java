package com.belk.car.app.webapp.controller.car;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.car.ManualCar;
import com.belk.car.app.model.car.ManualCarProcessStatus;
import com.belk.car.app.service.ManualCarManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class SearchManualCarController extends BaseFormController {
	
    private transient final Log log = LogFactory.getLog(SearchManualCarController.class);
    
    private ManualCarManager manualCarManager;
    
	public void setManualCarManager(ManualCarManager manualCarManager) {
		this.manualCarManager = manualCarManager;
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
        if (log.isDebugEnabled()) {
            log.debug("entering search manual car's 'handleRequest' method...");
        }

        ManualCarProcessStatus mcps = null;
		String processStatusCd = "";
		String vendorNo = "";
		String vendorStyleNo = "";
		if(request.getParameter("processStatusCd")!=null){
			processStatusCd = (String)request.getParameter("processStatusCd");
		}
		if(request.getParameter("vendorNo")!=null){
			vendorNo = (String)request.getParameter("vendorNo");
		}
		if(request.getParameter("vendorStyleNo")!=null){
			vendorStyleNo = (String)request.getParameter("vendorStyleNo");
		}

		//Create Query
		String likeFormat = "%%%1$s%%";
		StringBuffer sqlB = new StringBuffer("from ManualCar");
		ArrayList<String> query = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();
		
		query.add("statusCd = ?");
		values.add(Status.ACTIVE);
		
		if (StringUtils.isNotBlank(processStatusCd)) {
			query.add("processStatus like ?");
			mcps = manualCarManager.getMCProcessStatus(processStatusCd);
			values.add(mcps);
		}

		if (StringUtils.isNotBlank(vendorNo)) {
			query.add("vendorNumber like ?");
			values.add(String.format(likeFormat,vendorNo).toString());		
		}

		if (StringUtils.isNotBlank(vendorStyleNo)) {
			query.add("vendorStyleNumber like ?");
			values.add(String.format(likeFormat,vendorStyleNo).toString());		
		}

		if (!query.isEmpty()) {
			sqlB.append(" where ") ;
			int i=0;
			for(String s: query) {
				if (i>0) {
					sqlB.append(" AND ");
				}
				sqlB.append(s);
				i++;
			}
		}
		Object valueArray[] = values.toArray();

		if (log.isDebugEnabled())
			log.debug("Queries for: " + processStatusCd + " : " + vendorNo + " : " + vendorStyleNo);

		List<ManualCar> manualCars = new ArrayList<ManualCar>();
		manualCars = manualCarManager.searchManualCars(sqlB.toString(), valueArray);
		
		Map<String, Object> context = new HashMap<String, Object>();
        context.put("processStatusCd", processStatusCd);
        context.put("vendorNo", vendorNo);
        context.put("vendorStyleNo", vendorStyleNo);
        context.put("loggedInUser", getLoggedInUser());
        context.put(Constants.MANUAL_CAR_LIST, manualCars);
        context.put(Constants.MANUAL_CAR_PROCESS_STATUS_LIST, manualCarManager.getAllMCProcessStatus());
        
		return new ModelAndView(this.getSuccessView(), context);
    }
}
