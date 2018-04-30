package com.belk.car.app.webapp.controller.admin.outfit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import com.belk.car.app.service.OutfitManager;
import com.belk.car.app.webapp.controller.MultiActionFormController;
import com.belk.car.app.webapp.forms.OutfitForm;
import com.belk.car.app.Constants;
/**
 * MultiActionForm Controller to display Template Type to edit Outfit car.
 * 
 * @author KARTHIK/ANTO
 */
public class TemplateOutfitController extends MultiActionFormController {

	private transient final Log log = LogFactory
			.getLog(TemplateOutfitController.class);

	public OutfitManager outfitManager;

	public OutfitManager getOutfitManager() {
		return outfitManager;
	}

	public void setOutfitManager(OutfitManager outfitManager) {
		this.outfitManager = outfitManager;
	}


	/**
	 * GetTemplateType method retrieves the template type of car from ATTRIBUTE_LOOKUP_VALUE table based 
	 * on the attribute name by passing the attribute Id.  
	 * 
	 * @param  request   contains HttpServletRequest object.
	 * @param  response  contains HttpServletResponse object.
	 * @return model object contains template type of cars. 
	 * @throws Exception object contains the runtime exception
	 */
	public ModelAndView getTemplateType(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()){ 
			log.debug("entering 'getTemplateType' method in TemplateOutfitController...");
		}
		Map<String, Object> model = new HashMap<String, Object>();
		List<String> templateTypeList = outfitManager.getAttributeValueById();
		
		// Removing the template type COLLECTION from the list, as part of CARS to PIM groupings conversion (PIM-44) 
		if (this.getLoggedInUser() != null && !this.getLoggedInUser().isSuperAdmin()) {
			templateTypeList.remove(Constants.ATTR_COLLECTION);
		}
		String[] templateType = (String[]) templateTypeList
				.toArray(new String[0]);
		OutfitForm outfitForm = new OutfitForm();
		outfitForm.setTemplatetype(templateType);
		model.put(Constants.ATTR_TEMPLATE_TYPE, templateType);
		model.put(Constants.OUTFIT_FORM, outfitForm);
		return new ModelAndView(getSuccessView(), model);
	}
}
