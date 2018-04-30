package com.belk.car.app.webapp.controller.car;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.dto.CarsDTO;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarSearchCriteria;
import com.belk.car.app.model.Status;
import com.belk.car.app.service.WorkflowManager;
import com.belk.car.app.util.JSONUtil;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.CarSearchForm;

public class SearchResultController extends BaseFormController {

	private String viewHome;
	private String viewIndex;
	private WorkflowManager workflowManager;
	//Adding the parameter for Archive/ Unarchive button functionality
	private static final String ENABLE_ALL_BUTTONS = "enableBoth";

	public void setWorkflowManager(WorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}

	public SearchResultController() {
		setCommandName("carSearchForm");
		setCommandClass(CarSearchForm.class);
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		return new CarSearchForm();
	}
	

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		log.debug("Entering search results controller 'onSubmit' method...");

		CarSearchForm carSearchForm = (CarSearchForm) command;
		String isDboardRequest = request.getParameter("dboard");
		List<CarsDTO> cars = null;
		List<Car> carsSearch = null;
		boolean enableBothButtons = false; //archive and unarchive buttons

		if (logger.isDebugEnabled()) {
			logger.debug("Queries for: Car Id: " + carSearchForm.getCarId() + " : Department Code: "
					+ carSearchForm.getDeptCd() + " : Vendor Style Number: "
					+ carSearchForm.getVendorStyleNumber() + " : Class Number: "
					+ carSearchForm.getClassNumber() + " : Wofkflow Status: "
					+ carSearchForm.getWorkflowStatus() + " : Due Date From: "
					+ carSearchForm.getDueDateFrom() + " : Due Date To: "
					+ carSearchForm.getDueDateTo());
		}
		Map<String, String> queries = new HashMap<String, String>();
		CarSearchCriteria criteria = new CarSearchCriteria();
		try {
			BeanUtils.copyProperties(criteria, carSearchForm);
		} catch(IllegalAccessException ia) {
			log.error("Illegal Access Exception caught when copying properties");
		} catch(InvocationTargetException ite) {
			log.error("Invocation Target Exception caught when copying properties");
		}

		if(null == carSearchForm.getArchive()) {
			criteria.setArchive(null);
			enableBothButtons = true; 
			log.debug("***Both buttons should be enabled.****");
		}
		
		
		if (StringUtils.isBlank(criteria.getStatusCd()))
			criteria.setStatusCd(Status.ACTIVE);
		criteria.setUser(getLoggedInUser());
		criteria.setFromSearch(true) ;
		criteria.setVendorStyleStatusCd(Status.ACTIVE);
		//Setting flag to true to include outfit cars in search results.
		
		//Adding field for Copy Cars
		if(StringUtils.isNotBlank(request.getParameter("copyCars"))){
			//Allow only Buyer and Buyer to search for the cars of other dept in copy car.
			if ( criteria.getUser().isBuyer()) {
				criteria.setIgnoreUser(true);
			}
			criteria.setCopyCar("true".equals(request.getParameter("copyCars")));
		}else {
			criteria.setIncludeOutFitCars(true);
			criteria.setCopyCar(false);
			criteria.setIgnoreUser(false);
			criteria.setIncludePYGCars(true);
		}
		if(carSearchForm.getPromotionType() !=null && (carSearchForm.getPromotionType().equalsIgnoreCase("GWP")|| carSearchForm.getPromotionType().equalsIgnoreCase("PYG"))){
			criteria.setIncludePromoType(true);//
		}
		
		
		if (log.isDebugEnabled()) {
			logger.debug("Search Criteria: " + criteria) ;
		}
		request.getSession(true).setAttribute("lastSearchCriteria", criteria) ;
		if (logger.isDebugEnabled()) {
			logger.debug("Queries for: Car Id: " + criteria.getCarId() + " : Department Code: "
					+ criteria.getDeptCd() + " : Vendor Style Number: "
					+ criteria.getVendorStyleNumber() + " : Class Number: "
					+ criteria.getClassNumber() + " : Wofkflow Status: "
					+ criteria.getWorkflowStatus() + " : Due Date From: "
					+ criteria.getDueDateFrom() + " : Due Date To: "
					+ criteria.getDueDateTo());
		}
		if(criteria.isCopyCar()){
			carsSearch = getCarManager().searchCars(criteria);
		}
		if(getLoggedInUser().isBuyer() || getLoggedInUser().isVendor()){
			request.getSession(true).setAttribute("fromSearchPage", true) ;
		} else{
			request.getSession(true).setAttribute("fromSearchPage", false) ;	
		}
		Cookie cookie = new Cookie("cars_filter", "last_search");
		cookie.setMaxAge(365);
		response.addCookie(cookie);

		Map<String, Object> model = new HashMap<String, Object>();
		
		if (carsSearch == null) {
			model.put("noofcarsfound", "0");
		} else {
			model.put("noofcarsfound", carsSearch.size());
		}

		if (null == isDboardRequest) {
			model.put("jsonObj", JSONUtil.convertCarsToJSON(carsSearch,
					getLoggedInUser(), this.getCarLookupManager()));
		}
		model.put("DashBoard", carsSearch);
		model.put("userTypeCd", getLoggedInUser().getUserType().getUserTypeCd());
		model.put(ENABLE_ALL_BUTTONS, enableBothButtons);
		
		ModelAndView mv=null;
		if(this.isAjax(request)){
			mv = new ModelAndView(this.getAjaxView(), model);
		}
		else{
			mv = new ModelAndView(this.getSuccessView(), model);
		}
		return mv;

	}


	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws ServletRequestBindingException {

		Map<String, Object> context = new HashMap<String, Object>(); //the object to send to the jsp
		context.put("currentUserType", this.getLoggedInUser().getUserType().getUserTypeCd()) ;
		return context;
	}


	public void setViewHome(String viewHome) {
		this.viewHome = viewHome;
	}

	public void setViewIndex(String viewIndex) {
		this.viewIndex = viewIndex;
	}

}
