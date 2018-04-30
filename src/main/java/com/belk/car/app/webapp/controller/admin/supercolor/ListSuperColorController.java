package com.belk.car.app.webapp.controller.admin.supercolor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.appfuse.model.User;
import org.json.JSONObject;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.model.ColorMappingMaster;
import com.belk.car.app.service.SuperColorManager;
import com.belk.car.app.webapp.controller.MultiActionFormController;
import com.belk.car.app.webapp.forms.SuperColorForm;


/**
 * A controller class to control/navigate the following :
 * 1.Navigation to Super Color edit form request setting required values to form POJO.
 * 2.Deliver Search mechanism.
 * 3.Remove Super Color  
 * @author Yogesh.Vedak
 *
 */
public class ListSuperColorController extends MultiActionFormController {

	
	private SuperColorManager superColorManager;
	
	public SuperColorManager getSuperColorManager() {
		return superColorManager;
	}

	public void setSuperColorManager(SuperColorManager superColorManager) {
		this.superColorManager = superColorManager;
	}

	
	
	 /**
		 * Edit Super Color
		 * 
		 * @param request : cmmId of ColorMappingMaster
		 * @param response
		 * @return ModelAndView with SuperColor details empty or from database based on cmmId is present in the request or not
		 * @throws Exception
		 */
	 
		public ModelAndView editSuperColor(HttpServletRequest request,HttpServletResponse response) throws Exception {
			long cmmId = new Long(request.getParameter("cmmId")).longValue();
			SuperColorForm superColorForm = new SuperColorForm();
			if(log.isDebugEnabled()){
				log.debug("In editColor method . Is cmmID there?"+cmmId);
			}
			ColorMappingMaster cmMaster = getSuperColorManager().getSuperColorByCmmId(cmmId);
			if(cmMaster!=null){
					//just quick question for you nagi: can we plan to write converter for such these following conversions so that this following logic would be shifted there??...by Yogesh
					superColorForm.setCmmId(cmMaster.getCmmId());
					superColorForm.setColorCodeBegin(cmMaster.getColorCodeBegin());
					superColorForm.setColorCodeEnd(cmMaster.getColorCodeEnd());
					superColorForm.setSuperColorCode(cmMaster.getSuperColorCode());
					superColorForm.setSuperColorName(cmMaster.getSuperColorName());
					superColorForm.setStatusCode(cmMaster.getStatusCode());
					superColorForm.setIsEditForm(Boolean.TRUE);
				}
			ModelAndView mv = new ModelAndView("admin/supercolor/edit","superColorForm",superColorForm);
			return mv;
		}
	 
	 
	 /**
		 * List all the Super colors available that are set "active" in a Status.
		 * 
		 * @param request
		 * @param response
		 * @return ModealAndView with list of super colors in to the Model
		 * @throws Exception
		 */
	 public ModelAndView getAllSuperColors(HttpServletRequest request, HttpServletResponse response)
				throws Exception {
		 
		 	if(log.isDebugEnabled()){
		 		log.debug("Getting all the Super Colors ");
		 	}
			
			User user = getLoggedInUser();
			if (user.getAdmin()) {
				request.getSession().setAttribute("displayRole", "admin");
			}
			List<ColorMappingMaster> superColorList = new ArrayList<ColorMappingMaster>();
			try {
				superColorList =  getSuperColorManager().getAllSuperColorMapping();
				if(superColorList!=null){
					if(log.isInfoEnabled()){
						log.info("Found " +superColorList.size()+ " super colors");
					}
				}
			}catch(Exception e){
				log.error("Exception in gets",e);
			}
			return new ModelAndView(getSuccessView(), Constants.SUPERCOLOR_LIST,superColorList);
		}

	 /**
		 * Search Super Color  by superColorName and or superColorCode
		 * 
		 * @param request : superColorName and or superColorCode
		 * @param response
		 * @return MovdelAndView of the list for searched super colors 
		 * @throws Exception
		 */
		public ModelAndView Search(HttpServletRequest request, HttpServletResponse response)
				throws Exception {
				
				String superColorName = request.getParameter("superColorName")==null?"": request.getParameter("superColorName").trim();
				String superColorCode=request.getParameter("superColorCode")==null?"": request.getParameter("superColorCode").trim();
				if(log.isDebugEnabled()){
					log.debug("Searching super color in Controller superColorName:"+superColorName+" superColorCode:"+superColorCode);
				}
				
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("superColorName", superColorName);
				model.put("superColorCode", superColorCode);
				
				if (("".equals(superColorName)) && ("".equals(superColorCode)) ) {
					 return displayBackError(request,"All fields viz .Super Color Name/Code cannot be empty",model);
				}
				List<ColorMappingMaster> scolors =null;
				try{
					scolors = getSuperColorManager().searchSuperColors(superColorName,superColorCode);
					 if (scolors!=null){
				        if(log.isInfoEnabled()){	
				        	log.info("found "+scolors.size() + " super color(s) as search result");
				        }
					 }
				}catch(Exception e){
					log.error("Exception in Super Color Search",e);
					e.printStackTrace();
				}
		        model.put(Constants.SUPERCOLOR_LIST, scolors);
				return new ModelAndView(getSuccessView(), model);
			}
		
		
	/**
	 * Remove Super Color. (Its a soft delete in dao currently as a requirement. ie to change status as deleted)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
		
		public ModelAndView removeSuperColor(HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
			  Long cmmId = ServletRequestUtils.getLongParameter(request, "cmmId");
			  
			  User user = getLoggedInUser();
			  if(log.isDebugEnabled()){
				  log.debug("Removeing Super Color with cmmId:" + cmmId );
			  }
			  try{
				  getSuperColorManager().deleteSuperColor(cmmId);
			  }catch(Exception e){
				  log.error("Exception in remove Super Color ",e);
			  }
			  if(log.isInfoEnabled()){
				  log.info("Outfit with CarId:" + cmmId +" is removed by :"+user.getEmailAddress() );
			  }
			  return new ModelAndView("redirect:/admin/supercolor/supercolors.html?method=getAllSuperColors");
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
			log.debug(errorMsg);
		}
	saveError(request, getText(errorMsg, request.getLocale()));
	return new ModelAndView(getSuccessView(), model);
	}

	/*
	 * Method for find the super color1 name for the given color code.
	 * This is from ajax call from car Edit screen for color code. 
	 */
	
	public ModelAndView getSuperColor(HttpServletRequest request,HttpServletResponse response){
		String colorCode = request.getParameter("colorCode")==null?"": request.getParameter("colorCode").trim();
		if(log.isDebugEnabled()){
			log.debug("color code changed to:"+colorCode);
		}
		String superColor1=getSuperColorManager().findSuperColorNameForColorCode(colorCode);
		if(log.isDebugEnabled()){
			log.debug("super Color1 for changed color code:"+superColor1);
		}
		Map<String, Object> model = new HashMap<String, Object>();
		if(!"".equals(superColor1)){
			model.put("superColor1",superColor1);
			request.setAttribute("jsonObj", new JSONObject(model));
		} else {
			model.put("superColor1","Error");
			request.setAttribute("jsonObj", new JSONObject(model));
			if(log.isDebugEnabled()){
				log.debug("Super color code not found for color code:"+colorCode);
			}
		}
		return new ModelAndView(getAjaxView());
		
		
	}
}
