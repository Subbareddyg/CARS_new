package com.belk.car.app.webapp.controller.car;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.AccessDeniedException;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.model.car.ManualCar;
import com.belk.car.app.model.car.ManualCarProcessStatus;
import com.belk.car.app.service.ManualCarManager;
import com.belk.car.app.service.QuartzJobManagerForPIMJobs;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.car.ManualCarForm;

public class EditManualCarController extends BaseFormController {

    private ManualCarManager manualCarManager;
    private QuartzJobManagerForPIMJobs quartzJobManagerForPIMJobs;
   
    
	public QuartzJobManagerForPIMJobs getQuartzJobManagerForPIMJobs() {
		return quartzJobManagerForPIMJobs;
	}

	public void setQuartzJobManagerForPIMJobs(
			QuartzJobManagerForPIMJobs quartzJobManagerForPIMJobs) {
		this.quartzJobManagerForPIMJobs = quartzJobManagerForPIMJobs;
	}

	public void setManualCarManager(ManualCarManager manualCarManager) {
		this.manualCarManager = manualCarManager;
	}

	public EditManualCarController() {
		setCommandName("manualCarForm");
		setCommandClass(ManualCarForm.class);
	}

	public ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		if (request.getParameter("cancel") != null) {
			return new ModelAndView(getSuccessView());
		}

		return super.processFormSubmission(request, response, command, errors);
	}

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		if(log.isDebugEnabled())
			log.debug("entering manualCarForm 'onSubmit' method...");

		ManualCarForm manualCarForm = (ManualCarForm) command;		
		try {
				ManualCar manualCar = populateManualCar(request, manualCarForm);
				manualCarManager.save(manualCar);			
				ManualCar manualCarJob = (ManualCar) quartzJobManagerForPIMJobs.processManualCAR(manualCar);		  
		} catch (AccessDeniedException ade) {
			log.warn(ade.getMessage());
			response.sendError(HttpServletResponse.SC_FORBIDDEN);			
			return null;
		}

		return new ModelAndView(getSuccessView());
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

		if (!isFormSubmission(request)) {
			String manCarId = request.getParameter("id");
			if (!StringUtils.isBlank(manCarId)) {
				ManualCar manualCar = manualCarManager.getManualCar(Long.valueOf(manCarId));
				if (manualCar != null) {					
					ManualCarForm manualCarForm = new ManualCarForm();
					return populateManualCarForm(manualCarForm,manualCar);
				}
			}
		}else if (request.getParameter("id") != null && !"".equals(request.getParameter("id"))
				&& !request.getParameter("id").equals("0") && request.getParameter("cancel") == null) {

			ManualCar manualCar = manualCarManager.getManualCar(Long.valueOf(request.getParameter("id")));
			ManualCarForm manualCarForm = new ManualCarForm();
			manualCarForm.setManualCar(manualCar);
			return manualCarForm;
		}else{
			return new ManualCarForm();
		}
		
		return super.formBackingObject(request);
	}

	private ManualCar populateManualCar(HttpServletRequest request, ManualCarForm manualCarForm) {		
		ManualCar manualCar = null;	
			if(manualCarForm.getManualCar() == null){			
				manualCar = new ManualCar();
				ManualCarProcessStatus m = manualCarManager.getMCProcessStatus(ManualCarProcessStatus.INITIATED);
				manualCar.setProcessStatus(m);
				manualCar.setStatusCd(ManualCar.ACTIVE);
				}else{
				manualCar = manualCarForm.getManualCar();			
				//manualCar.setStatusCd(manualCarForm.getStatusCd());
			}
			manualCar.setVendorNumber(manualCarForm.getVendorNumber());
			manualCar.setVendorStyleNumber(manualCarForm.getVendorStyleNumber());
			manualCar.setColorDescription(manualCarForm.getColorDescription());
			manualCar.setSizeDescription(manualCarForm.getSizeDescription());		
			if (manualCarForm.getIsPack().equalsIgnoreCase("Y"))
			{
				manualCar.setIsPack(ManualCar.FLAG_YES);
			}
			else{
				manualCar.setIsPack(ManualCar.FLAG_NO);
			}
			this.setAuditInfo(request, manualCar);	 
		return manualCar;
	}

    private ManualCarForm populateManualCarForm(ManualCarForm manualCarForm,ManualCar manualCar) {    	
		manualCarForm.setManualCarId(manualCar.getManualCarId());
		manualCarForm.setVendorNumber(manualCar.getVendorNumber());
		manualCarForm.setVendorStyleNumber(manualCar.getVendorStyleNumber());
		manualCarForm.setColorDescription(manualCar.getColorDescription());
		manualCarForm.setSizeDescription(manualCar.getSizeDescription());
		manualCarForm.setStatusCd(manualCar.getStatusCd());
		manualCarForm.setIsPack(manualCar.getIsPack());
		manualCarForm.setProcessStatus(manualCar.getProcessStatus().getStatusCd());		
		return manualCarForm;
	}
}
