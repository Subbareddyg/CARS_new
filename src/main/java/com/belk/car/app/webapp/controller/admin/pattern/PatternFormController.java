package com.belk.car.app.webapp.controller.admin.pattern;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.exceptions.AttributeValueExistsException;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarSearchCriteria;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.ContentStatus;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.VendorStyleType;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.PatternForm;
import com.belk.car.util.PasswordGenerator;

public class PatternFormController extends BaseFormController {

	public PatternFormController() {
		setCommandName("patternForm");
		setCommandClass(PatternForm.class);
	}

	public ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		
		if (request.getParameter("cancel") != null) {
			if (StringUtils.isBlank(request.getParameter("patternId"))) {
				return new ModelAndView(getListView());
			} else {
				Map<String, String> model = new HashMap<String, String>();
				model.put("patternId", request.getParameter("patternId"));
				return new ModelAndView(getCancelView(),model);
			}
		}

		return super.processFormSubmission(request, response, command, errors);
	}


	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		if (log.isDebugEnabled())
			log.debug("entering patternForm 'onSubmit' method...");

		PatternForm patternForm = (PatternForm) command;

		//String patternId = request.getParameter("patternId");
		String nextView = getSuccessView();
		VendorStyle pattern = null ;
		try {
			pattern =populateAndSavePattern(request, patternForm);
		}
		catch (DataIntegrityViolationException dvex) {
			errors.rejectValue("pattern", "errors.invalid.pattern",
					new Object[] { patternForm.getVendorStyleName()}, dvex.getMessage());

			return showForm(request, response, errors);
		}
		catch (Exception e) {
			errors.rejectValue("pattern", "errors.existing.pattern",
					new Object[] { patternForm.getVendorStyleName()}, "duplicate pattern!");

			return showForm(request, response, errors);
		}

		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("patternId", String.valueOf(pattern.getVendorStyleId()));
		return new ModelAndView(nextView, model);
	}

	private VendorStyle populateAndSavePattern(HttpServletRequest request,
			PatternForm patternForm) throws AttributeValueExistsException, DataIntegrityViolationException {


		Classification classification = null; 
		if (patternForm.getClassNumber() != 0) {
			classification = this.getCarManager().getClassification(patternForm.getClassNumber());
			if (classification == null) {
				throw new DataIntegrityViolationException("Invalid class number!!") ;
			}
			//removed this check to ensure that Lori can create Vendor Style for her classes when the 5 Character rule fails
			/*else if (PatternProcessingRule.FIVE_CHAR_STYLE_DESC.equals(classification.getPatternProcessingRule().getCode())) {
				throw new DataIntegrityViolationException("Class already has a pattern rule assigned!!") ;			
			}*/
		}
		
		
		if (this.getLoggedInUser() != null) {
			User user = (User) this.getCarLookupManager().getById(User.class, this.getLoggedInUser().getId());
			if(!user.getDepartments().contains(classification.getDepartment())) {
				throw new DataIntegrityViolationException("Cannot create a Pattern for Department/Class you are not assigned to!!");
			}
		}

		//Have to create Vendor if it does not exist
		Vendor vendor = this.getCarManager().getVendor(patternForm.getVendorNumber()) ;		
		if (vendor == null) {
			vendor = new Vendor();
			vendor.setVendorNumber(patternForm.getVendorNumber().toUpperCase()) ;
			vendor.setName(patternForm.getVendorNumber().toUpperCase()) ;
			vendor.setDescr(patternForm.getVendorNumber().toUpperCase()) ;
			vendor.setStatusCd(Status.ACTIVE) ;
			vendor.setContactEmailAddr("vendor@belk.com");
			this.setAuditInfo(request, vendor) ;
                        vendor.setIsDisplayable("Y");
			vendor = this.getCarManager().createVendor(vendor) ;
		}
		
		VendorStyle pattern = null;
		VendorStyleType oldVSType = null ; 
		if (patternForm.getPatternId() == null || patternForm.getPatternId() == 0) {
			pattern = new VendorStyle() ;
			String patternStyleNumber = StringUtils.substring(patternForm.getVendorStyleName().replaceAll("[^a-zA-Z]", ""), 0, 5).toUpperCase(); 
			//Get First 5 Characters & append a random code
			pattern.setVendorStyleNumber(patternStyleNumber + classification.getBelkClassNumber() + PasswordGenerator.getPassword(2).toUpperCase());
		} else {
			pattern = this.getCarManager().getVendorStyle(patternForm.getPatternId()) ;
			oldVSType = pattern.getVendorStyleType() ;
		}

		pattern.setVendor(vendor);

		pattern.setVendorStyleName(patternForm.getVendorStyleName()) ;
		pattern.setVendorNumber(patternForm.getVendorNumber().toUpperCase());
		pattern.setStatusCd(Status.ACTIVE) ;
		pattern.setClassification(classification);
		pattern.setDescr(patternForm.getDescr());
		pattern.setVendorStyleType((VendorStyleType) this.getCarLookupManager().getById(VendorStyleType.class, patternForm.getVendorStyleTypeCode()));

		if (StringUtils.isNotBlank(patternForm.getProductTypeId())) {
			pattern.setProductType(this.getCarLookupManager().getProductTypeById(new Long(patternForm.getProductTypeId())));
		}
		setAuditInfo(request, pattern);

		carManager.createVendorStyle(pattern);
		
		//Check if Pattern Type has Changed
		if (oldVSType != null && !oldVSType.getCode().equals(pattern.getVendorStyleType().getCode())) {
			//Update CAR SENT_TO_CMP To RESEND_DATA_TO_CMP 
			//Get Car for Pattern Vendor Style which have been SENT_TO_CMP
			//Update CAR Content Status to mark RESEND_DATA_TO_CMP
			CarSearchCriteria criteria = new CarSearchCriteria() ;
			criteria.setIgnoreClosedCars(false) ;
			criteria.setIgnoreUser(true) ;
			criteria.setStatusCd(Status.ACTIVE) ;
			criteria.setVendorNumber(pattern.getVendorNumber());
			criteria.setVendorStyleNumber(pattern.getVendorStyleNumber());
			criteria.setContentStatus(ContentStatus.SENT_TO_CMP) ;
			List<Car> cars = this.getCarManager().searchCars(criteria);
			if (cars != null && !cars.isEmpty()) {
				ContentStatus cStatus = (ContentStatus) this.getCarLookupManager().getById(ContentStatus.class, ContentStatus.RESEND_DATA_TO_CMP);
				for(Car car: cars) {
					if (car.getContentStatus().getCode().equals(ContentStatus.SENT_TO_CMP)) {
						car.setContentStatus(cStatus);
						this.getCarManager().save(car) ;
					}
				}
			}
		}
		
		return pattern;

	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

		HttpSession session = request.getSession(false);
		if(session != null && session.getAttribute("patternTypes") == null) {
			session.setAttribute("patternTypes", carLookupManager.getPatternTypes(this.getLoggedInUser().isSuperAdmin()));
		}

		VendorStyle pattern = null;
		PatternForm patternForm = new PatternForm();
		if (!isFormSubmission(request)) {
			String patternId = request.getParameter("patternId");
			if (!StringUtils.isBlank(patternId)
					&& !"".equals(request.getParameter("version"))) {
				pattern = this.getCarManager().getVendorStyle(Long.valueOf(patternId));
				if (pattern != null) {
					patternForm = populatePatternForm(patternForm, pattern);
				}
			} else {
				return patternForm;
			}

			return patternForm;
		} else if (request.getParameter("patternId") != null
				&& !"".equals(request.getParameter("patternId"))
				&& request.getParameter("cancel") == null) {
			// populate attribute object from database, so all fields don't need
			// to
			// be hidden fields in form
			//pattern = carManager.getVendorStyle(Long.valueOf(request
			//		.getParameter("patternId")));
			
			return patternForm;
		}

		return super.formBackingObject(request);
	}

	/**
	 * 
	 * @param patternForm
	 * @param pattern
	 * @return
	 */
	private PatternForm populatePatternForm(PatternForm patternForm,
			VendorStyle pattern) {

		if(pattern.getVendorStyleId()>0){
			patternForm.setPatternId(new Long(pattern.getVendorStyleId())) ;
		}
		
		if(pattern.getClassification().getBelkClassNumber()>0){
			patternForm.setClassNumber(new Short(pattern.getClassification().getBelkClassNumber()));
		}

		if (pattern.getProductType() != null) {
			patternForm.setProductTypeId(pattern.getProductType().getProductIdAsString()) ;
		}

		patternForm.setStatusCd(pattern.getStatusCd()) ;
		patternForm.setVendorNumber(pattern.getVendorNumber()) ;
		patternForm.setVendorStyleName(pattern.getVendorStyleName());
		patternForm.setDescr(pattern.getDescr()) ;
		patternForm.setVendorStyleTypeCode(pattern.getVendorStyleType().getCode());
		return patternForm;
	}

	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws ServletRequestBindingException {

		Map<String, Object> model = new HashMap<String, Object>();
		
		PatternForm formObject = (PatternForm)command;
		Short classNumber = formObject.getClassNumber();
		if(classNumber!=null){
			model.put("classProductTypes", this.getCarManager().getProductTypesByClass(classNumber));
		}
		return model;
	}

}
