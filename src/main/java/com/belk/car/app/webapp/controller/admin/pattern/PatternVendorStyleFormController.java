package com.belk.car.app.webapp.controller.admin.pattern;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.exceptions.AttributeValueExistsException;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.VendorStyleSearchCriteria;
import com.belk.car.app.model.VendorStyleType;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.PatternForm;

public class PatternVendorStyleFormController extends BaseFormController {

	public PatternVendorStyleFormController() {
		setCommandName("patternForm");
		setCommandClass(PatternForm.class);
	}

	public ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		if (request.getParameter("cancel") != null) {
			if (StringUtils.isBlank(request.getParameter("patternId"))) {
				return new ModelAndView(getListView());
			} else {
				Map<String, String> model = new HashMap<String, String>();
				model.put("patternId", request.getParameter("patternId"));
				return new ModelAndView(getCancelView(), model);
			}
		}

		return super.processFormSubmission(request, response, command, errors);
	}

	public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		if (log.isDebugEnabled())
			log.debug("entering attributeForm 'onSubmit' method...");

		PatternForm patternForm = (PatternForm) command;

		String patternId = request.getParameter("patternId");
		String nextView = getSuccessView();
		VendorStyle pattern = null;
		try {
			pattern = populateAndSavePattern(request, patternForm);
		} catch (DataIntegrityViolationException dvex) {
			errors.rejectValue("pattern", "errors.invalid.pattern", new Object[] { patternForm.getVendorStyleName() }, dvex.getMessage());
			return showForm(request, response, errors);
		} catch (Exception e) {
			errors.rejectValue("pattern", "errors.existing.pattern", new Object[] { patternForm.getVendorStyleName() }, "duplicate pattern!");
			return showForm(request, response, errors);
		}

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("patternId", patternId);

		long longPatternId = Long.parseLong(patternId);
		VendorStyle elPattern = getCarManager().getVendorStyle(longPatternId);
		model.put("pattern", elPattern);

		VendorStyleSearchCriteria criteria = new VendorStyleSearchCriteria();
		criteria.setVendorStyleId(longPatternId);
		criteria.setChildrenOnly(true);
		List<VendorStyle> patternProducts = getCarManager().searchVendorStyle(criteria);
		model.put("patternProducts", patternProducts);

		return new ModelAndView(nextView, model);
	}

	private VendorStyle populateAndSavePattern(HttpServletRequest request, PatternForm patternForm) throws AttributeValueExistsException,
			DataIntegrityViolationException {

		VendorStyle pattern = this.getCarManager().getVendorStyle(patternForm.getPatternId());
		VendorStyle vs = this.getCarManager().getVendorStyle(patternForm.getVendorNumber().toUpperCase(), patternForm.getVendorStyleNumber().toUpperCase());
		if (vs == null) {
			vs = new VendorStyle();

			//Have to create Vendor if it does not exist
			Vendor vendor = this.getCarManager().getVendor(patternForm.getVendorNumber());

			if (vendor == null) {
				vendor = new Vendor();
				vendor.setVendorNumber(StringUtils.trim(patternForm.getVendorNumber()).toUpperCase());
				vendor.setName(StringUtils.trim(patternForm.getVendorNumber()).toUpperCase());
				vendor.setDescr(patternForm.getVendorNumber());
				vendor.setStatusCd(Status.ACTIVE);
				vendor.setContactEmailAddr("vendor@belk.com");
                                vendor.setIsDisplayable("Y");
				this.setAuditInfo(request, vendor);
				vendor = this.getCarManager().createVendor(vendor);
			}
			vs.setVendor(vendor);
			vs.setVendorStyleNumber(StringUtils.trim(patternForm.getVendorStyleNumber()).toUpperCase());
			vs.setVendorStyleName(" ");
			vs.setVendorNumber(patternForm.getVendorNumber());
			vs.setStatusCd(Status.ACTIVE);
			vs.setClassification(pattern.getClassification());
			vs.setProductType(pattern.getProductType());
			vs.setDescr(vs.getVendorStyleName());
			vs.setVendorStyleType((VendorStyleType) this.getCarLookupManager().getById(VendorStyleType.class, VendorStyleType.PRODUCT));
			vs.setParentVendorStyle(pattern);

			setAuditInfo(request, vs);

		} else {
			if (vs.getParentVendorStyle() == null) {
				vs.setParentVendorStyle(pattern);
				setAuditInfo(request, vs);
			} else {
				new DataIntegrityViolationException("Vendor Number + Style Number already in a Pattern");
			}
		}
		vs = carManager.createVendorStyle(vs);
		return vs;
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		PatternForm patternForm = new PatternForm();
		patternForm.setPatternId(Long.parseLong(request.getParameter("patternId")));
		VendorStyle pattern = this.getCarManager().getVendorStyle(patternForm.getPatternId());
		if (pattern != null)
			patternForm.setVendorNumber(pattern.getVendorNumber());
		return patternForm;
	}

}
