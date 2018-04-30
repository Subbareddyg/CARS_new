package com.belk.car.app.webapp.controller.admin.supercolor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.model.ColorMappingMaster;
import com.belk.car.app.service.SuperColorManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.SuperColorForm;


/**
 * A controller class to control the following :
 * 1.ADD/EDIT FORM to add/edit Super Color entries 
 * 2.Deliver Search mechanism.
 * @author Yogesh.Vedak
 *
 */
public class SuperColorFormController extends BaseFormController{

	private transient final Log log = LogFactory.getLog(SuperColorFormController.class);
	
	private SuperColorManager superColorManager;
	public SuperColorManager getSuperColorManager() {
		return superColorManager;
	}

	public void setSuperColorManager(SuperColorManager superColorManager) {
		this.superColorManager = superColorManager;
	}	
	
	public SuperColorFormController() {
		setCommandName("superColorForm");
		setCommandClass(SuperColorForm.class);
	}
	
	public ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		   	if (request.getParameter("cancel") != null) {
				return new ModelAndView(getSuccessView());
			} 
			return super.processFormSubmission(request, response, command, errors);
	}
	
	public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		   	if(log.isDebugEnabled()){
				log.debug("entering SuperColor's 'onSubmit' method...");
			}
			try{
				SuperColorForm suprColorForm=(SuperColorForm)command;
				boolean isAddForm = request.getParameter("cmmId") == null?true:false;
				long cmmId =  isAddForm?0:new Long(request.getParameter("cmmId")).longValue();
				if(log.isDebugEnabled()){
					log.debug("cmmID  is:"+request.getParameter("cmmId"));
				}
				ColorMappingMaster cmmaster = cmmId==0?new ColorMappingMaster():getSuperColorManager().getSuperColorByCmmId(cmmId);
				if(isAddForm){
					cmmaster.setCreatedBy(getLoggedInUser().getUsername());
					cmmaster.setCreatedDate(new Date());
					cmmaster.setUpdatedBy(getLoggedInUser().getUsername());
					cmmaster.setUpdatedDate(new Date());
					getSuperColorManager().saveOrUpdateSuperColor(applyFormValues(cmmaster,suprColorForm));
				}else{
					if(isObjectEditedOnForm(suprColorForm,cmmaster)){
						cmmaster.setUpdatedBy(getLoggedInUser().getUsername());
						cmmaster.setUpdatedDate(new Date());
						getSuperColorManager().saveOrUpdateSuperColor(applyFormValues(cmmaster,suprColorForm));
					}
				}
				if(log.isDebugEnabled()){
					log.debug("colorCodeBegin: "+ suprColorForm.getColorCodeBegin());
					log.debug("colorCodeEnd: "+ suprColorForm.getColorCodeEnd());
					log.debug("superColorName: "+suprColorForm.getSuperColorName());
					log.debug("superColorCode:" + suprColorForm.getSuperColorCode());
				}
			}catch(Exception e){
				log.error(" SuperColorFormController: onSubmit: exception while creating Of super color: "+ e.getMessage());
				e.printStackTrace();
			}
			
			return new ModelAndView("redirect:supercolors.html?method=getAllSuperColors",Constants.SUPERCOLOR_LIST,getSuperColorManager().getAllSuperColorMapping());

		}
	
	public ColorMappingMaster applyFormValues(ColorMappingMaster cmmaster,SuperColorForm suprColorForm){
		cmmaster.setColorCodeBegin(StringUtils.leftPad(suprColorForm.getColorCodeBegin(), Constants.COLORCODE_MAXLEN, Constants.COLORCODE_LEFTPAD));
		cmmaster.setColorCodeEnd(StringUtils.leftPad(suprColorForm.getColorCodeEnd(), Constants.COLORCODE_MAXLEN, Constants.COLORCODE_LEFTPAD));
		cmmaster.setSuperColorCode(StringUtils.leftPad(suprColorForm.getSuperColorCode(), Constants.COLORCODE_MAXLEN,Constants.COLORCODE_LEFTPAD)); 
		cmmaster.setSuperColorName(suprColorForm.getSuperColorName());
		cmmaster.setStatusCode(Constants.SUPERCOLOR_STATUS_ACTIVE);
		return cmmaster;
	}
	
	public boolean isObjectEditedOnForm(SuperColorForm suprColorForm,ColorMappingMaster dbObject){
		if(!suprColorForm.getSuperColorCode().equals(dbObject.getSuperColorCode())){
			return true;
		}else if(!suprColorForm.getSuperColorName().equals(dbObject.getSuperColorName())){
			return true;
		}else if(!suprColorForm.getColorCodeBegin().equals(dbObject.getColorCodeBegin())){
			return true;
		}else if(!suprColorForm.getColorCodeEnd().equals(dbObject.getColorCodeEnd())){
			return true;
		}
	return false;
	}
}
