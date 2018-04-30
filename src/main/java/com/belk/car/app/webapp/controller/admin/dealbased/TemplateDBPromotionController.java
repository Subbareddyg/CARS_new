package com.belk.car.app.webapp.controller.admin.dealbased;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import com.belk.car.app.service.DBPromotionManager;
import com.belk.car.app.webapp.controller.MultiActionFormController;
import com.belk.car.app.webapp.controller.admin.outfit.TemplateOutfitController;
import com.belk.car.app.webapp.forms.DBPromotionForm;
import com.belk.car.app.Constants;

public class TemplateDBPromotionController extends MultiActionFormController{

	private transient final Log log = LogFactory
			.getLog(TemplateDBPromotionController.class);

	public DBPromotionManager dbPromotionManager;


	public DBPromotionManager getDbPromotionManager() {
		return dbPromotionManager;
	}

	public void setDbPromotionManager(DBPromotionManager dbPromotionManager) {
		this.dbPromotionManager = dbPromotionManager;
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
		List<String> templateTypeList = dbPromotionManager.getPromoTemplateTypes();
		String[] templateType = (String[]) templateTypeList.toArray(new String[0]);
		System.out.println("The Template List:"+templateType[0]);
		DBPromotionForm dbPromotionForm = new DBPromotionForm();
		dbPromotionForm.setTemplateType(templateType);
		model.put(Constants.ATTR_TEMPLATE_TYPE, templateType);
		model.put(Constants.DBPROMOTION_FORM, dbPromotionForm);
		System.out.println("getSuccessView() in TemplControlr"+getSuccessView()+"getTemplateType");
		return new ModelAndView(getSuccessView(), model);
	}
}
