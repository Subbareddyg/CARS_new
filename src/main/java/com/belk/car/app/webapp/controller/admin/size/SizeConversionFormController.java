package com.belk.car.app.webapp.controller.admin.size;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.model.SizeConversionMaster;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.SizeConversionManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.SizeConversionForm;


/**
 * 
 * @author Yogesh.Vedak
 *
 */
public class SizeConversionFormController extends BaseFormController{

	private transient final Log log = LogFactory.getLog(SizeConversionFormController.class);
	
	private SizeConversionManager sizeConversionManager;
	
	private CarManager carManager;

	public SizeConversionManager getSizeConversionManager() {
		return sizeConversionManager;
	}
	public void setSizeConversionManager(SizeConversionManager sizeConversionManager) {
		this.sizeConversionManager = sizeConversionManager;
	}
	public CarManager getCarManager() {
		return carManager;
	}
	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}
	
	
	
	public SizeConversionFormController() {
		setCommandName("sizeConversionForm");
		setCommandClass(SizeConversionForm.class);
	}
	
	
	public ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
			if(log.isDebugEnabled()){
		    	log.debug("Entering SizeConversionForm 'processFormSubmission' method..."+request.getParameter("scmId"));
		    }
			if (request.getParameter("cancel") != null) {
				return new ModelAndView(getSuccessView());
			} 
			return super.processFormSubmission(request, response, command, errors);
	}
	
	public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
			throws Exception {
			if(log.isDebugEnabled()){
				log.debug("entering SizeConversionForm 'onSubmit' method...");
			}
				String searchList = request.getParameter("searchList");
				if(log.isDebugEnabled()){
					log.debug("entering SizeConversionForm 'onSubmit' method for search..."+searchList);
				}
	
			try{
				SizeConversionForm sizeConversionForm=(SizeConversionForm)command;
				boolean isAddForm= request.getParameter("scmId") == null?true:false;
				long scmId = isAddForm?0:new Long(request.getParameter("scmId")).longValue();
				if(log.isDebugEnabled()){
					log.debug("scmID  is:"+request.getParameter("scmId"));
				}
				SizeConversionMaster scmaster = scmId==0?new SizeConversionMaster():getSizeConversionManager().getSizeConversionByScmId(scmId);
				
				//FNY14 chances of six different values for dept,vendor,class
				String vendorNumberArray = sizeConversionForm.getVendorNumber();
				String deptCodeArray = sizeConversionForm.getDeptCode();
				String strClassNumArray = sizeConversionForm.getClassNumber();
				String vendorNumber = "";
				String deptCode = "";
				String strClassNum = "";
				int vendorSize =0;
				int deptSize = 0;
				int classSize = 0;
				String[] vendorArray=null;
				String[] deptArray=null;
				String[] classArray=null;
				String[] fieldArray=null;
				boolean isMultipleVendor = false;
				boolean isMultipleClass = false;
				boolean isMultipleDept = false;
				//vendor array
				if(StringUtils.isNotBlank(vendorNumberArray))
						vendorArray=vendorNumberArray.split(",");
						
				if(StringUtils.isNotBlank(deptCodeArray))
						deptArray=deptCodeArray.split(",");
				
				if(StringUtils.isNotBlank(strClassNumArray))
						classArray=strClassNumArray.split(",");
						
						
				vendorSize = vendorArray ==null ? 0 : vendorArray.length;
				deptSize = deptArray ==null ? 0 : deptArray.length;
				classSize = classArray ==null ? 0 : classArray.length;
				vendorNumber = vendorSize == 0 ? "" : vendorArray[0];
				deptCode = deptSize == 0 ? "" : deptArray[0];
				strClassNum = classSize == 0 ? "" : classArray[0];
				
				//find the muliple values field
				if(vendorSize > 1){
					isMultipleVendor=true; 
					fieldArray = vendorArray;
					
				}else if(deptSize > 1){
					isMultipleDept = true; 
					fieldArray = deptArray;
					
				}else if(classSize > 1){
					isMultipleClass =true;
					fieldArray = classArray;
					
				}

				//loop for multipiple values in ONE field
				if(isMultipleVendor) {
					for(int i=0;i<fieldArray.length;i++){
							sizeConversionForm = setSizeConversionForm(sizeConversionForm,fieldArray[i],deptCode,strClassNum);
							updateSizeConversion(isAddForm,sizeConversionForm,scmaster);
					}		
				}else if(isMultipleDept) {
					for(int i=0;i<fieldArray.length;i++){
							sizeConversionForm = setSizeConversionForm(sizeConversionForm,vendorNumber,fieldArray[i],strClassNum);
							updateSizeConversion(isAddForm,sizeConversionForm,scmaster);
					}
				}else if(isMultipleClass) {
					for(int i=0;i<fieldArray.length;i++){
							sizeConversionForm = setSizeConversionForm(sizeConversionForm,vendorNumber,deptCode,fieldArray[i]);
							updateSizeConversion(isAddForm,sizeConversionForm,scmaster);
					}
				}else {//No Mutiple values so only one mapping rule			
					//System.out.println("Validator -------> ONE mapping rule");			
					sizeConversionForm = setSizeConversionForm(sizeConversionForm,vendorNumber,deptCode,strClassNum);
					updateSizeConversion(isAddForm,sizeConversionForm,scmaster);
				}
				
				
		}catch(Exception e){
			log.error("In SizeConversionFormController  onSubmit() method: exception while creating Of Size Conversion entry: "+ e.getMessage());
				e.printStackTrace();
		}
			if(searchList != null && searchList.indexOf("=") > -1)
					return new ModelAndView("redirect:/admin/size/sizeconversions.html?method=Search&"+searchList);
			return new ModelAndView("redirect:sizeconversions.html?method=getAllSizeConversions",Constants.SIZECONVERSION_LIST,getSizeConversionManager().getAllSizeConversionMapping());
	}

	public void updateSizeConversion(boolean isAddForm,SizeConversionForm sizeConversionForm,SizeConversionMaster scmaster ) {
		
		try{

				if(isAddForm){
					scmaster.setCreatedBy(getLoggedInUser().getUsername());
					scmaster.setStatusCode(Constants.SIZECONV_STATUS_ACTIVE);
					scmaster.setCreatedDate(new Date());
					scmaster.setUpdatedBy(getLoggedInUser().getUsername());
					scmaster.setUpdatedDate(new Date());
					scmaster.setScmId(0);
					getSizeConversionManager().saveOrUpdateSizeConversion(applyFormValues(scmaster,sizeConversionForm));
					}else{
						scmaster.setUpdatedBy(getLoggedInUser().getUsername());
						scmaster.setUpdatedDate(new Date());
						//The status maintains 'A' because the status will be P from job.
						scmaster.setStatusCode(Constants.SIZECONV_STATUS_ACTIVE);
						if(isObjectEditedOnForm(sizeConversionForm,scmaster)){
							getSizeConversionManager().saveOrUpdateSizeConversion(applyFormValues(scmaster,sizeConversionForm));
						}
					}
				if(log.isDebugEnabled()){
					log.debug("Dept Code: "+ sizeConversionForm.getDeptCode());
					log.debug("Class Number: "+sizeConversionForm.getClassNumber());
					log.debug("Vendor Number:" + sizeConversionForm.getVendorNumber());
					log.debug("Size Name: "+ sizeConversionForm.getSizeName());
					log.debug("Conversion Size Name: "+sizeConversionForm.getCoversionSizeName());
					log.debug("Status Cd:" + sizeConversionForm.getStatusCode());
				}
			}catch(Exception e){
				log.error("In SizeConversionFormController  onSubmit() method: exception while creating Of Size Conversion entry: "+ e.getMessage());
				e.printStackTrace();
			}
	
	}

	public SizeConversionForm setSizeConversionForm(SizeConversionForm sizeConversionForm, String vendorNumber, String deptCode, String strClassNum){
		
		sizeConversionForm.setVendorNumber(vendorNumber);
		sizeConversionForm.setDeptCode(deptCode);
		sizeConversionForm.setClassNumber(strClassNum);
		
		return sizeConversionForm;
	}

	public SizeConversionMaster applyFormValues(SizeConversionMaster scmaster,SizeConversionForm sizeConversionForm){
		scmaster.setDepartment(carManager.getDepartment(sizeConversionForm.getDeptCode()));
		String classNumber = sizeConversionForm.getClassNumber().equals("")?null:sizeConversionForm.getClassNumber();
		scmaster.setClassification(classNumber == null?null:carManager.getClassification(new Short(classNumber).shortValue()));
		scmaster.setVendor(carManager.getVendor(sizeConversionForm.getVendorNumber()));
		//scmaster.setScmId(scmId);
		scmaster.setSizeName(sizeConversionForm.getSizeName());
		scmaster.setCoversionSizeName(sizeConversionForm.getCoversionSizeName());
		scmaster.setFacetSize1(sizeConversionForm.getFacetSize1());
		scmaster.setFacetSize2(sizeConversionForm.getFacetSize2());
		scmaster.setFacetSize3(sizeConversionForm.getFacetSize3());
		scmaster.setFacetSubSize1(sizeConversionForm.getFacetSubSize1());
		scmaster.setFacetSubSize2(sizeConversionForm.getFacetSubSize2());
		if(log.isDebugEnabled()){
			log.debug("Object with ScmId:"+scmaster.getScmId()+" in apply method with status "+ scmaster.getStatusCode());
		}
		return scmaster;
	}
	
	public boolean isObjectEditedOnForm(SizeConversionForm sizeConversionForm,SizeConversionMaster dbObject){
		if(!sizeConversionForm.getSizeName().equals(dbObject.getSizeName())){
			return true;
		}else if(!sizeConversionForm.getCoversionSizeName().equals(dbObject.getCoversionSizeName())){
			return true;
		}else if(!"".equals(sizeConversionForm.getDeptCode()) && !carManager.getDepartment(sizeConversionForm.getDeptCode()).equals(dbObject.getDepartment())){
			return true;
		}else if(!"".equals(sizeConversionForm.getClassNumber()) && !carManager.getClassification(new Short(sizeConversionForm.getClassNumber()).shortValue()).equals(dbObject.getClassification())){
			return true;
		}else if(!"".equals(sizeConversionForm.getVendorNumber()) && !carManager.getVendor(sizeConversionForm.getVendorNumber()).equals(dbObject.getVendor())){
			return true;
		}else if(!sizeConversionForm.getFacetSize1().equals(dbObject.getFacetSize1())){
			return true;
		}else if(!sizeConversionForm.getFacetSize2().equals(dbObject.getFacetSize2())){
			return true;
		}else if(!sizeConversionForm.getFacetSubSize1().equals(dbObject.getFacetSubSize1())){
			return true;
		}else if(!sizeConversionForm.getFacetSubSize2().equals(dbObject.getFacetSubSize2())){
			return true;
		}
	return false;
	}
	
}
