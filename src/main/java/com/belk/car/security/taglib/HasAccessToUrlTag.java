package com.belk.car.security.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.belk.car.security.RolesPermissionsAdapter;
import com.belk.car.security.UrlRepository;

/**
 * Check if User has access to a URL
 * 
 * @author vsundar
 */

public final class HasAccessToUrlTag extends TagSupport {

	// --------------------------------------------------- Instance Variables

	/**
	 * The key of the session-scope bean we look for.
	 */
	private String name = null;

	private String scope = null;

	private String id = null;

	// ----------------------------------------------------------- Properties

	/**
	 * Return the Url name.
	 */
	public String getName() {
		return (this.name);
	}

	/**
	 * Set the bean name.
	 * 
	 * @param name
	 *            The new bean name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return the bean name.
	 */
	public String getId() {
		return (this.id);
	}

	/**
	 * Set the bean name.
	 * 
	 * @param name
	 *            The new bean name
	 */
	public void setId(String id) {
		this.id = id;

	}

	/**
	 * Return the forward page.
	 */
	public String getScope() {
		return (this.scope);
	}

	/**
	 * Set the forward Section.
	 * 
	 * @param page
	 *            The new forward page
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	// ------------------------------------------------------- Public Methods

	/**
	 * Defer our checking until the end of this tag is encountered.
	 * 
	 * @exception JspException
	 *                if a JSP exception has occurred
	 */
	public int doStartTag() throws JspException {

		return (SKIP_BODY);

	}

	/**
	 * Check if User has access to URL
	 * 
	 * @exception JspException
	 *                if a JSP exception has occurred
	 */
	public int doEndTag() throws JspException {
		UrlRepository repository = (UrlRepository) this.pageContext.getAttribute(UrlRepository.URL_REPOSITORY_KEY, pageContext.APPLICATION_SCOPE);
		RolesPermissionsAdapter  permissions = new RolesPermissionsAdapter((HttpServletRequest)pageContext.getRequest()) ;

		pageContext.setAttribute(id, permissions.isAllowed(repository.getUrl(this.name)));

		this.release();
		
		return (EVAL_PAGE);
	}

	/**
	 * Release any acquired resources.
	 */
	public void release() {

		super.release();
		this.name = null;
		this.scope = null;
		this.id = null;

	}

}
