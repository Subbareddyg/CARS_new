package com.belk.car.app.webapp.controller.admin.help;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.model.HelpContent;
import com.belk.car.app.model.Status;
import com.belk.car.app.service.HelpContentManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class HelpContentFormController extends BaseFormController {

	private HelpContentManager manager;

	/**
	 * @param manager
	 *            the manager to set
	 */
	public void setHelpContentManager(HelpContentManager manager) {
		this.manager = manager;
	}

	public HelpContentFormController() {
		setCommandName("helpContentForm");
		setCommandClass(HelpContent.class);
	}

	public ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		if (request.getParameter("cancel") != null) {
			if (!StringUtils.equals(request.getParameter("from"), "list")) {
				return new ModelAndView(getCancelView());
			} else {
				return new ModelAndView(getSuccessView());
			}
		}
		return super.processFormSubmission(request, response, command, errors);
	}

	public String getMainRedirectView() {
		return "redirect:mainMenu.html";
	}

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		if (log.isDebugEnabled())
			log.debug("entering contentForm 'onSubmit' method...");

		HelpContent contentForm = (HelpContent) command;
		try {
			manager.save(populateContent(request, contentForm));
		} catch (Exception e) {
			errors.rejectValue("contentKey", "errors.existing.conetnt.key",
					new Object[] { contentForm.getContentKey() },
					"duplicate help content type");
			return showForm(request, response, errors);
		}

		if (!StringUtils.equals(request.getParameter("from"), "list")) {
			saveMessage(request, getText("helpcontent.saved", contentForm
					.getContentKey(), request.getLocale()));
		}
		return new ModelAndView(getSuccessView());
	}

	private HelpContent populateContent(HttpServletRequest request,
			HelpContent contentForm) {

		HelpContent helpContent = null;
		if (contentForm == null) {// New Content
			helpContent = new HelpContent();
			helpContent.setStatusCd(Status.ACTIVE);
		} else {
			// helpContent = contentForm.getHelpContent(); // Update Product
			helpContent = contentForm;
		}
		// helpContent.setName(contentForm.getName());
		// helpContent.setStatusCd(contentForm.getStatusCd());
		// helpContent.setDescription(contentForm.getDescription());
		setAuditInfo(request, helpContent);
		return helpContent;
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

		HelpContent content = null;
		String action = this.getCurrentAction(request);
		String id = request.getParameter("id");
		String contentKey = request.getParameter("contentKey");
		String contentSection = request.getParameter("contentSection");
		if (StringUtils.isNotBlank(id) && !"0".equals(id)
				&& request.getParameter("cancel") == null) {
			content = manager.getHelpContent(Long.valueOf(id));
		} else if (StringUtils.isNotBlank(contentKey)
				&& StringUtils.isNotBlank(contentSection)) {
			content = manager.getHelpContent(contentKey, contentSection);
			if (content == null){
				content = new HelpContent();
				content.setContentKey(contentKey);
				content.setContentSection(contentSection);
				content.setStatusCd(Status.ACTIVE);
			}
		} 

		if (content == null){
			content = new HelpContent();
			content.setStatusCd(Status.ACTIVE);
		}

		return content;
	}

	protected boolean isAdd(HttpServletRequest request) {
		String method = request.getParameter("action");
		return (method != null && method.equalsIgnoreCase("addProduct"));
	}

}
