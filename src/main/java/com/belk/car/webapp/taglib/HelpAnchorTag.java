package com.belk.car.webapp.taglib;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.web.servlet.support.RequestContext;

/**
 * Generate the Help URL
 * 
 * @author vsundar
 */

public final class HelpAnchorTag extends TagSupport {

	// --------------------------------------------------- Instance Variables

	/**
	 * The key of the session-scope bean we look for.
	 */
	private static final String  FORWARD_SLASH = "/";

	private String id = null;

	private String scope = null;

	private String href = "/help.html";

	private String cssStyle = "context_help" ;

	private String title = "help.title";

	private String key = null ;
	
	private String section = null;

	private boolean localized = true ;

	protected RequestContext requestContext;

	// ----------------------------------------------------------- Properties

	/**
	 * Return the Url name.
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
	 * Return the bean name.
	 */
	public String getHref() {
		return (this.href);
	}

	/**
	 * Set the bean name.
	 * 
	 * @param name
	 *            The new bean name
	 */
	public void setHref(String href) {
		this.href = href;

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

		String format = "<a href=\"%1$s\" class=\"%4$s\">%2$s</a>"; //title=\"%3$s\" -> behaves weird with title= &nbsp;
																	//maybe we should separate title and text properties
		if (isLocalized()) {
			this.title = getMessageSource().getMessage(this.title, null,
					this.title, pageContext.getRequest().getLocale());
		}

		if (StringUtils.isBlank(key)) {
			key = ((HttpServletRequest)pageContext.getRequest()).getServletPath();
		}
		
		if (StringUtils.isBlank(section)) {
			section = "" ;
		}

		String contextPath = ((HttpServletRequest)this.pageContext.getRequest()).getContextPath();
		if(StringUtils.isNotBlank(contextPath) && !contextPath.equals(FORWARD_SLASH)) {
			if(this.href.startsWith(FORWARD_SLASH) ) {
				this.href =  contextPath + this.href;
			}					
		}

		StringBuffer strB = new StringBuffer();
		strB.append(this.href).append("?key=").append(key).append("&section=").append(section);
		this.href = strB.toString();
		
		write(String.format(format, this.href, this.title,
				this.title, this.cssStyle));

		this.release();

		return (SKIP_BODY);

	}

	/**
	 * Use the application context itself for default message resolution.
	 */
	protected MessageSource getMessageSource() {
		return (MessageSource) getRequestContext().getWebApplicationContext();
	}

	/**
	 * Return default exception message.
	 */
	protected String getNoSuchMessageExceptionDescription(
			NoSuchMessageException ex) {
		return ex.getMessage();
	}

	protected RequestContext getRequestContext() {
		if (this.requestContext == null) {
			try {
				this.requestContext = new RequestContext(
						(HttpServletRequest) pageContext.getRequest());
			} catch (RuntimeException ex) {
				//throw ex;
			} catch (Exception ex) {
				pageContext.getServletContext().log("Exception in custom tag",
						ex);
			}
		}
		return this.requestContext;
	}

	private void write(String text) {
		try {
			pageContext.getOut().write(text);
		} catch (IOException ioex) {

		}
	}

	/**
	 * Release any acquired resources.
	 */
	public void release() {

		super.release();
		this.id = null;
		this.scope = null;
		this.href = "/help.html";
		this.title="help.title";
		this.key = null;
		this.cssStyle = "context_help";
		this.section = null;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isLocalized() {
		return localized;
	}

	public void setLocalized(boolean localized) {
		this.localized = localized;
	}

	public String getCssStyle() {
		return cssStyle;
	}

	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
