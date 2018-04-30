package com.belk.car.app.webapp.controller.admin.size;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.SizeConversionMaster;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.SizeConversionManager;
import com.belk.car.app.webapp.controller.MultiActionFormController;
import com.belk.car.app.webapp.forms.SizeConversionForm;

/**
 * A controller class to control/navigate the following :
 * 1.Navigation to edit Size Conversion form request setting required values to form POJO
 * 2.Deliver Search mechanism.
 * 3.Remove Size Conversion. 
 * @author Yogesh.Vedak
 *
 */
public class ListSizeConversionController extends MultiActionFormController {

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
	
	
	
	 /**
		 * Edit Size Conversion Entry
		 * 
		 * @param request : scmId of SizeConversionMaster
		 * @param response
		 * @return ModelAndView with Size Conversion details ; empty or retrieved from database based on cmmId is present in the request or not
		 * @throws Exception
		 */
	 
		public ModelAndView editSizeConversion(HttpServletRequest request,HttpServletResponse response) throws Exception {
			long scmId = new Long(request.getParameter("scmId")).longValue();
			SizeConversionForm sizeConversionForm = new SizeConversionForm();
			SizeConversionMaster scMaster =  getSizeConversionManager().getSizeConversionByScmId(scmId);
				if(scMaster!=null){
					sizeConversionForm.setScmId(scMaster.getScmId());   //to change fields
					sizeConversionForm.setDeptCode(scMaster.getDepartment()!=null?scMaster.getDepartment().getDeptCd():"");
					sizeConversionForm.setClassNumber((scMaster.getClassification()!=null?String.valueOf(scMaster.getClassification().getBelkClassNumber()):""));
					sizeConversionForm.setVendorNumber(scMaster.getVendor() !=null?scMaster.getVendor().getVendorNumber():"");
					sizeConversionForm.setSizeName(scMaster.getSizeName());
					sizeConversionForm.setCoversionSizeName(scMaster.getCoversionSizeName());
					sizeConversionForm.setStatusCode(scMaster.getStatusCode());
					sizeConversionForm.setFacetSize1(scMaster.getFacetSize1());
					sizeConversionForm.setFacetSize2(scMaster.getFacetSize2());
					sizeConversionForm.setFacetSize3(scMaster.getFacetSize3());
					sizeConversionForm.setFacetSubSize1(scMaster.getFacetSubSize1());
					sizeConversionForm.setFacetSubSize2(scMaster.getFacetSubSize2());

					sizeConversionForm.setCreatedDate(scMaster.getCreatedDate());
					sizeConversionForm.setUpdatedDate(scMaster.getUpdatedDate());
					sizeConversionForm.setCreatedBy(scMaster.getCreatedBy());

					sizeConversionForm.setIsEditForm(Boolean.TRUE);
				}
			ModelAndView mv = new ModelAndView("admin/size/edit","sizeConversionForm",sizeConversionForm);
			return mv;
		}
	 
	 
	 /**
		 * List all the Size Conversions available.
		 * 
		 * @param request
		 * @param response
		 * @return ModealAndView with list of size Conversions in to the Model
		 * @throws Exception
		 */
	 public ModelAndView getAllSizeConversions(HttpServletRequest request, HttpServletResponse response)
				throws Exception {
		    if(log.isDebugEnabled()){
		    	log.debug("Getting all the Size Conversions ");
		    }
			User user = getLoggedInUser();
			if (user.getAdmin()) {
				request.getSession().setAttribute("displayRole", "admin");
			}
			List<SizeConversionMaster> sizeConversionList = new ArrayList<SizeConversionMaster>();  //SizeMaster would come here
			try {
				sizeConversionList =   getSizeConversionManager().getAllSizeConversionMapping();
				if(sizeConversionList!=null){
					 if(log.isDebugEnabled()){
						log.debug("Found " +sizeConversionList.size()+ " Size conversions");
					}
				}
			}catch(Exception e){
				log.error("Exception in getAllSizeConversions():",e);
			}
			return new ModelAndView(getSuccessView(), Constants.SIZECONVERSION_LIST,sizeConversionList);
		}

	 /**
		 * Search Size Conversion by Size Name and  Department Code and Class Number and Facet Size and Facet SubSize
		 *  If all Five inputs are empty or If invalid Department Code or Class Number is entered it will flash an error to enter valid ones for search.
		 * @param request : sizeName and departmentCode and classNumber
		 * @param response
		 * @return MovdelAndView of the list for searched Size Conversions 
		 * @throws Exception
		 */
		
	 public ModelAndView Search(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String sizeName = request.getParameter("sizeName")==null?"": request.getParameter("sizeName").trim();  // to get sizeName if present in a request to be passed to manager's search method:sizeSuperColorManager.searchSizeConversions(sizeName,deptId,classId).
			String conversionName=request.getParameter("conversionName")==null?"": request.getParameter("conversionName").trim();
			String deptCode = request.getParameter("deptCode")==null?"": request.getParameter("deptCode").trim();
			String classNumber = request.getParameter("classNumber")==null?"": request.getParameter("classNumber").trim();
			String vendorNumber = request.getParameter("vendorCode")==null?"": request.getParameter("vendorCode").trim();
			String facetSize = request.getParameter("facetSize")==null?"": request.getParameter("facetSize").trim();
			String facetSubSize = request.getParameter("facetSubSize")==null?"": request.getParameter("facetSubSize").trim();
			if(log.isDebugEnabled()){
			 log.debug("SizeName:"+sizeName +"  Converson Name:"+ conversionName 
					 +" DeptCode:"+ deptCode +" Class Number:"+classNumber 
					 +" vendorNumber:"+ vendorNumber +" facetSize:"+facetSize+" facetSubSize:"+facetSubSize);	
			}
			String deptId = "";
			String classId ="";
			String vendorId="";
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("sizeName", sizeName);
			model.put("conversionName", conversionName);
			model.put("deptCode", deptCode);
			model.put("classNumber", classNumber);
			model.put("vendorNumber", vendorNumber);
			model.put("facetSize", facetSize);
			model.put("facetSubSize", facetSubSize);
			if(StringUtils.isNotBlank(deptCode)){
			Department  dept = carManager.getDepartment(deptCode);    // to get deptId from deptCode present in a request so deptId would be passed to manager's search method:sizeSuperColorManager.searchSizeConversions(sizeName,deptId,classId). 
				if(dept != null){
					deptId = new Long(dept.getDeptId()).toString();
				}else{
					if(log.isDebugEnabled()){
						log.debug(" Invalid Dept Code:"+deptCode);
					}
					return displayBackError(request,"Invalid Dept Code:"+deptCode+" was enetered to search.",model);
				}
			}
			if(StringUtils.isNotBlank(vendorNumber)){
				Vendor  vend = carManager.getVendor(vendorNumber);    // to get vendorId from vendorCode present in a request so vendorId would be passed to manager's search method:sizeSuperColorManager.searchSizeConversions(sizeName,deptId,classId). 
					if(vend != null){
						vendorId = new Long(vend.getVendorId()).toString();
					}else{
						if(log.isDebugEnabled()){
							log.debug(" Invalid Vedor Code:"+vendorNumber);
						}
						return displayBackError(request,"Invalid Vendor Code:"+vendorNumber+" was enetered to search.",model);
				}
			}
			if(StringUtils.isNotBlank(classNumber)){
				try{	
					short  shrotClassNumber= new Short(classNumber).shortValue();
					 Classification classification  =  carManager.getClassification(shrotClassNumber); // to get classId from classNumber present in a request so classId would be passed to manager's search method: sizeSuperColorManager.searchSizeConversions(sizeName,deptId,classId). 
					 if(classification != null){
						 classId = new Long(classification.getClassId()).toString();
					 }else{
						 if(log.isDebugEnabled()){
								log.debug("Invalid Class Number :"+classNumber);
							}
					 return displayBackError(request,"Invalid Class Number :"+classNumber+" was entered to search.",model);
					 }
				}catch(Exception e){
					if(log.isDebugEnabled()){
						log.debug("Exception converting Class Number to short. Out of range: "+classNumber);
					}
					return displayBackError(request,"Invalid Class Number :"+classNumber+" was entered to search.",model);
				}
			}
						
			if (("".equals(sizeName)) && ("".equals(deptId)) && ("".equals(classId)) &&  ("".equals(conversionName)) &&  ("".equals(vendorId))&&  ("".equals(facetSize))&&  ("".equals(facetSubSize))) {
					return displayBackError(request,"Size Name/Conversion Name/Department/Class/Vendor/Facet Size/Facet SubSize cannot be empty",model);
				}
		
			List<SizeConversionMaster> sizeconversions =null;
			try{
				sizeconversions =  getSizeConversionManager().searchSizeConversions(sizeName,conversionName,deptId,classId,vendorId,facetSize,facetSubSize);
				 if (sizeconversions!=null){
			        if(log.isInfoEnabled()){	
			        	log.info("Found "+sizeconversions.size() + " Size conversions(s) as search result");
			        }
				 }
			}catch(Exception e){
					log.error("Exception in Size Conversion Search",e);
					e.printStackTrace();
			}
			model.put(Constants.SIZECONVERSION_LIST, sizeconversions);
			return new ModelAndView(getSuccessView(), model);
		}
		
		/**
		 * Remove Size Conversion. In dao it would soft delete ie change the status as deleted.
		 * 
		 * @param request: scmId
		 * @param response
		 * @return redirecting ModelAndView to the list size Conversions 
		 * @throws Exception
		 */
		
		public ModelAndView removeSizeConversion(HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			  Long scmId = ServletRequestUtils.getLongParameter(request, "scmId");
			  User user = getLoggedInUser();
			  if(log.isDebugEnabled()){
				  log.debug("Removeing Size Conversion with scmId:" + scmId );
			  }
			  try{
				  getSizeConversionManager().deleteSizeConversion(scmId);
			  }catch(Exception e){
				  log.error("Exception in remove Size Conversion ",e);
			  }
			  if(log.isDebugEnabled()){
				  log.debug("Size Conversion with ScmId:" + scmId +" is removed by :"+user.getEmailAddress() );
			  }
			  return new ModelAndView("redirect:/admin/size/sizeconversions.html?method=getAllSizeConversions");
		}

		/**
		 * Remove Size Conversion. In dao it would soft delete ie change the status as deleted.
		 * 
		 * @param request: scmId
		 * @param response
		 * @return redirecting ModelAndView to the list size Conversions 
		 * @throws Exception
		 */
		
		public ModelAndView deletSizeConversions(HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			  //Long scmId = ServletRequestUtils.getLongParameter(request, "scmId");
			  
			  String scmIds = ServletRequestUtils.getStringParameter(request, "sizeRulesList");
			  String searchList = ServletRequestUtils.getStringParameter(request, "searchList");	
			  User user = getLoggedInUser();
			  List<String> scmIdList = new ArrayList<String>();
			  if (StringUtils.isNotBlank(scmIds)) {
				String[] scmIdArray=scmIds.split(",");
					if (scmIdArray.length != 0){ 
						for(int i=0;i<scmIdArray.length;i++){
							//if(
							scmIdList.add(scmIdArray[i]);
							if(log.isDebugEnabled()){
								log.debug("Removeing Size Conversion with scmId:" + scmIdArray[i] );
							}
						}//for loop
					try{
						getSizeConversionManager().deleteBatchSizeConversionList(scmIdList);
						}catch(Exception e){
								log.error("Exception in remove Size Conversion ",e);
						}
						if(log.isDebugEnabled()){
							log.debug("Size Conversion with ScmId:" + scmIdList.toString() +" is removed by :"+user.getEmailAddress() );
						}
					}
				}
				if(searchList != null && searchList.indexOf("=") > -1)
					return new ModelAndView("redirect:/admin/size/sizeconversions.html?method=Search"+searchList);
			return new ModelAndView("redirect:/admin/size/sizeconversions.html?method=getAllSizeConversions");
			}

		
		/**
		 * Sets the error in to the session and returns back to the search form with given error Message.
		 * @param request
		 * @param errorMsg
		 * @param model
		 * @return
		 */

		public ModelAndView displayBackError(HttpServletRequest request,String errorMsg,Map<String, Object> model){
			if(log.isDebugEnabled()){
				 	log.debug("displayBackError :"+errorMsg);
			}
			saveError(request, getText(errorMsg, request.getLocale()));
		    return new ModelAndView(getSuccessView(), model);
		}

}
