package com.belk.car.app.webapp.controller.admin.latecars;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.LateCarsGroup;
import com.belk.car.app.service.LateCarsGroupManager;
import com.belk.car.app.webapp.controller.MultiActionFormController;
import com.belk.car.util.GenericUtils;


public class LateCarsGroupController extends MultiActionFormController {

	private transient final Log log = LogFactory.getLog(LateCarsGroupController.class);

	public LateCarsGroupManager lateCarsGroupManager;
	
	public void setLateCarsGroupManager(LateCarsGroupManager lateCarsGroupManager) {
		this.lateCarsGroupManager = lateCarsGroupManager;
	}

	protected String detailView;
	
	public String getDetailView() {
		return detailView;
	}

	public void setDetailView(String detailView) {
		this.detailView = detailView;
	}
	
	protected String addLateCarsView;
	
	

	public String getAddLateCarsView() {
		return addLateCarsView;
	}

	public void setAddLateCarsView(String addLateCarsView) {
		this.addLateCarsView = addLateCarsView;
	}

	/**
	 * Gets all the Late CARS Groups as a list for displaying in the page.
	 */
	public ModelAndView getAllLateCarsGroup(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("In Late Cars Group Controller : Getting all the Late Cars Groups");
		User user = getLoggedInUser();
		if (user.getAdmin()) {
			request.getSession().setAttribute("displayRole", "admin");
		}
		return new ModelAndView(getSuccessView(), DropShipConstants.LATE_CARS_GROUP_LIST,
				lateCarsGroupManager.getAllLateCarsGroups());
	}
	
	

	/**
	 * Search based on Late CARS group name.
	 * 
	 * @param request
	 *            : Has the late CARS group name
	 * @param response
	 *            : Has the search list of the late CARS group name
	 * @return : Has the list of late CARS group names
	 * @throws Exception
	 */
	public ModelAndView Search(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String lateCarsGroupName = request.getParameter("lateCarsGroupName");
		log.debug("In Late CARS Controller : Search:" + lateCarsGroupName);
		if (lateCarsGroupName == null || lateCarsGroupName.trim().equalsIgnoreCase("")) {
			saveError(request, getText("Late CARS Group Name cannot be empty", request
					.getLocale()));
			lateCarsGroupName = "#";
			Map<String, Object> model = new HashMap<String, Object>();
	        model.put("lateCarsGroupName", "");
	        model.put(DropShipConstants.LATE_CARS_GROUP_LIST, lateCarsGroupManager.getLateCarsGroups(lateCarsGroupName));
			return new ModelAndView(getSuccessView(), model);
		}
		Map<String, Object> model = new HashMap<String, Object>();
        model.put("lateCarsGroupName", lateCarsGroupName);
        model.put( DropShipConstants.LATE_CARS_GROUP_LIST,  lateCarsGroupManager.getLateCarsGroups(GenericUtils.escapeSpecialCharacters(lateCarsGroupName)));
		return new ModelAndView(getSuccessView(), model);

	}

		
	/**
	 * This method is invoked when the add late care group type button is clicked
	 * on the late car group  screen.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	
	public ModelAndView addLateCarsGroupView(HttpServletRequest request, HttpServletResponse response,LateCarsGroup lateCarsGroupform)
	throws Exception {
		log.debug("In Late Cars Group Controller : adding new Late Cars Group View");
		request.setAttribute("lateCarsGroupform", lateCarsGroupform);
		return new ModelAndView(getAddLateCarsView());
	}
	
	
	/**
	 * This method is invoked for adding new late care group name 
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	
	public ModelAndView addLateCarsGroup(HttpServletRequest request, HttpServletResponse response, LateCarsGroup lateCarsGroupform)
	throws Exception {
		log.debug("In Late Cars Group Controller : adding new Late Cars Group name");
		String lateCarsGroupName=lateCarsGroupform.getName();
		log.debug("lateCarsGroupName from request:"+lateCarsGroupName);
		LateCarsGroup lcgroup=new LateCarsGroup();
		lcgroup.setName(lateCarsGroupName);
		List<LateCarsGroup> lateCarsGroupList= lateCarsGroupManager.getAllLateCarsGroups();
		for(LateCarsGroup lc : lateCarsGroupList ){
			if(lc.getName().equalsIgnoreCase(lateCarsGroupName)){
				log.debug("late care group already exists");
				errors.rejectValue("name","Late CAR Group Already Exists");
				saveError(request, errors.getFieldError().getCode());
				return addLateCarsGroupView(request,response,lateCarsGroupform);
			}
		}
		LateCarsGroup lcg=lateCarsGroupManager.save(lcgroup);
		log.debug("Late Car Group "+ lcg.getName()+" added");
		return new ModelAndView("redirect:/latecarsgroup/lateCarsGroup.html?method=getAllLateCarsGroup");
	}
	
	/**  
	 * Gets the detail of the late CARS group based on the id passed
	 * 
	 * @param request
	 *            : id of the group
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	public ModelAndView detail(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Long lateCarsGroupId = null;
		lateCarsGroupId = ServletRequestUtils.getLongParameter(request, "lateCarsGroupId");
		log.debug("In detail id:" + lateCarsGroupId);
		User user = getLoggedInUser();
		if (user.getAdmin()) {
			request.getSession().setAttribute("displayRole", "admin");
		}
		LateCarsGroup lateCarsGroup =lateCarsGroupManager.getLateCarsGroup(lateCarsGroupId);
		Map<String,LateCarsGroup> model = new HashMap<String, LateCarsGroup>();
		model.put("lateCarsGroupDetails", lateCarsGroup);
		return new ModelAndView(getDetailView(), model);
	}

	
	/**
	 * Remove the late CARS group
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	public ModelAndView remove(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Long lateCarsGroupId = ServletRequestUtils.getLongParameter(request, "lateCarsGroupId");
		log.debug("Remove late car group :" + lateCarsGroupId);
		if (lateCarsGroupId == null) {
			log.debug("Late CAR Group id was null. Redirecting...");
			return new ModelAndView("redirect:dashBoard.html");
		}
		lateCarsGroupManager.remove(lateCarsGroupId);
		return new ModelAndView("redirect:/latecarsgroup/lateCarsGroup.html?method=getAllLateCarsGroup", DropShipConstants.LATE_CARS_GROUP_LIST,
				lateCarsGroupManager.getAllLateCarsGroups());
	}
	
	/**
	 * Edit the late CARS group
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	public ModelAndView edit(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Long lateCarsParamId = ServletRequestUtils.getLongParameter(request, "lateCarsParamId");
		log.debug("Edit late car parameter id :" + lateCarsParamId);
		if (lateCarsParamId == null) {
			log.debug("Late CAR Group id was null. Redirecting...");
			return new ModelAndView("redirect:dashBoard.html");
		}
		
		return new ModelAndView(getSuccessView(), DropShipConstants.LATE_CARS_GROUP_LIST,
				lateCarsGroupManager.getAllLateCarsGroups());
	}
	
}
