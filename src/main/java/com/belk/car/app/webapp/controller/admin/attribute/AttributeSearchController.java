package com.belk.car.app.webapp.controller.admin.attribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.PIMAttribute;
import com.belk.car.app.service.AttributeManager;
import com.belk.car.app.service.PIMAttributeManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class AttributeSearchController extends BaseFormController {

	private AttributeManager attributeManager;
	private PIMAttributeManager pimAttributeManager;
	/**
	 * @param attributeManager
	 *            the attributeManager to set
	 */
	public void setAttributeManager(AttributeManager attributeManager) {
		this.attributeManager = attributeManager;
	}

	public void setPimAttributeManager(PIMAttributeManager pimAttributeManager) {
		this.pimAttributeManager = pimAttributeManager;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {
			logger.info("Entered handleRequest() method in AttributeSearchController ");

			List<PIMAttribute> pimAttributes = new ArrayList<PIMAttribute>();
			Map<String, Object> model = new HashMap<String, Object>();
			String action = "";
			String pimAttName = "";
			String message = "";
			action = request.getParameter("action");
			pimAttName = request.getParameter("pimAttributeName");
			User loggedUser = getLoggedInUser();
			boolean isDataGovernanceAdmin = loggedUser.isDataGovernanceAdmin();			
System.out.println("isDataGovernanceAdmin===>"+isDataGovernanceAdmin);
System.out.println("action==>"+action);
			if (logger.isDebugEnabled())
				logger.debug("action==>" + action);
			if (Constants.SEARCHPIMATTRIBUTE.equals(action)) {
				System.out.println("insite search pim attributes==>"+pimAttName);
				model.put("loggedInUser", loggedUser);
				String lastSerch = pimAttName;
				pimAttributes = pimAttributeManager
						.getPIMAttributesFromCars(pimAttName);
				model.put("pimAttributes", pimAttributes);
				model.put("pimAttributeName", pimAttName);
				model.put("lastSerch", lastSerch);
				model.put("action", action);
				model.put("isDataGovernanceAdmin", isDataGovernanceAdmin);
				model.put(Constants.ATTR_LIST,
						attributeManager.getAllAttributes());
				return new ModelAndView(this.getSuccessView(), model);

			} else if (Constants.SAVEPIMATTRIBUTE.equals(action)) {
				
				String lastSerch = request.getParameter("lastSerch");
				System.out.println("insite search pim attributes==>"+lastSerch);
				String selectedPIMAttributeOld = request
						.getParameter("selectedPIMAttributeOld");
				String selectedPIMAttributeNew = request
						.getParameter("selectedPIMAttributeNew");
				int saved = doSavePimAttributes(selectedPIMAttributeOld,
						selectedPIMAttributeNew);
				if (saved > 0)
					message = saved + " PIM attribute saved successfully.";
				pimAttributes = pimAttributeManager
						.getPIMAttributesFromCars(lastSerch);
				if (logger.isDebugEnabled())
					logger.debug("Total number of pim attributes saved: ====>"
							+ saved);
				model.put("loggedInUser", loggedUser);
				model.put("message", message);
				model.put("action", action);
				model.put("pimAttributeName", lastSerch);
				model.put("lastSerch", lastSerch);
				model.put("pimAttributes", pimAttributes);
				model.put("isDataGovernanceAdmin", isDataGovernanceAdmin);
				model.put(Constants.ATTR_LIST,attributeManager.getAllAttributes());
				return new ModelAndView(this.getSuccessView(), model);
			} else {
				
				System.out.println("got inside attribute search");
				String attributeName = request.getParameter("attributeName");
				String blueMartiniName = request
						.getParameter("blueMartiniName");
				String productTypeName = request
						.getParameter("productTypeName");
				String classificationId = request
						.getParameter("classificationId");
				Long classId = null;
				System.out.println("attributeName-" + attributeName
						+ "-blueMartiniName-" + blueMartiniName
						+ "-productTypeName-" + productTypeName
						+ "-classificationId-" + classificationId);
				// Set the value to session of future use for RE-SEARCH
				this.saveSearchCriteria(request, "attributeSearchCriteria");

				if (StringUtils.isNotBlank(classificationId)) {
					try {
						classId = Long.parseLong(classificationId);
					} catch (NumberFormatException nfe) {
						log.error("Error formatting : " + classificationId);
					}
				}

				List<Attribute> attributes = attributeManager.searchAttributes(
						attributeName, classId, blueMartiniName,
						productTypeName);

				Map<String, Object> context = new HashMap<String, Object>();
				context.put("attributeName", attributeName);
				context.put("classificationId", classificationId);
				context.put("blueMartiniName", blueMartiniName);
				context.put("loggedInUser", loggedUser);
				context.put(Constants.ATTR_LIST, attributes);
				context.put("isDataGovernanceAdmin", isDataGovernanceAdmin);

				return new ModelAndView(this.getSuccessView(), context);
			}
		} catch (Exception e) {
			log.error("Exception occured in AttributeSearchController: "+e.getMessage());
			return new ModelAndView(this.getSuccessView());
		}

	}

	private int doSavePimAttributes(String selectedPIMAttributeOld,
			String selectedPIMAttributeNew) {
		User user = getLoggedInUser();
		int saved =0;
		if(logger.isDebugEnabled())
		logger.debug("Entered doSavePimAttributes method in AttributeSearchController ");
		List<String> oldPIMlist = new ArrayList<String>();
		List<String> newPIMlist = new ArrayList<String>();
		if(!StringUtils.isEmpty(selectedPIMAttributeOld)){
			oldPIMlist = new ArrayList<String>(Arrays.asList(selectedPIMAttributeOld.split(",")));}
			
		if(!StringUtils.isEmpty(selectedPIMAttributeNew)){
			newPIMlist = new ArrayList<String>(Arrays.asList(selectedPIMAttributeNew.split(",")));
		}
		if(logger.isDebugEnabled())
		logger.debug("newPIMlist size==>"+newPIMlist.size()+"oldPIMlist size==>"+oldPIMlist.size());
		List<String> tempNewPIMList = new ArrayList<String>(newPIMlist);
		
		newPIMlist.removeAll(oldPIMlist);
		oldPIMlist.removeAll(tempNewPIMList);
		
		
		if(newPIMlist.size()>0){
			saved+=pimAttributeManager.updatePIMAttributes(newPIMlist.toString(), Constants.FLAG_YES,user.getUsername());
		}
		if(oldPIMlist.size()>0){
		saved+=pimAttributeManager.updatePIMAttributes(oldPIMlist.toString(), Constants.FLAG_NO,user.getUsername());
		}	
	
		return saved;

	}
}
