package com.belk.car.security.taglib;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.util.ExpressionEvaluationUtils;

import com.belk.car.security.RolesPermissionsAdapter;
import com.belk.car.security.UrlComponent;
import com.belk.car.security.UrlRepository;

/**
 * Check if User has access to a URL
 * 
 * @author vsundar
 */

public final class SecureAnchorTag extends TagSupport {

	// --------------------------------------------------- Instance Variables

	/**
	 * The key of the session-scope bean we look for.
	 */
	private static final String  FORWARD_SLASH = "/";
	
	private transient final Log log = LogFactory
	.getLog(SecureAnchorTag.class);
	
	private String name = null;

	private String scope = null;

	private String href = null;

	private String cssStyle = null;
	
	private String elementName = null;
	
	private String onclick = null;

	private boolean allowAccessToUndefinedUrl = true;

	private boolean hideUnaccessibleLinks = true;

	private boolean localized = false;

	private String title = null;

	private Object arguments;

	//protected RequestContext requestContext;

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
		UrlRepository repository = (UrlRepository) this.pageContext
				.getAttribute(UrlRepository.URL_REPOSITORY_KEY,
						pageContext.APPLICATION_SCOPE);

		RolesPermissionsAdapter permissions = new RolesPermissionsAdapter(
				(HttpServletRequest) pageContext.getRequest());

		UrlComponent comp = repository.getUrl(this.name);
		String format = null;
		
		if (StringUtils.isNotBlank(cssStyle)) {
			format = "<a href=\"%1$s\" id=\"%5$s\" name=\"%5$s\" onclick=\"%6$s\" title=\"%3$s\" class=\"%4$s\">%2$s</a>";
		} else {
			format = "<a href=\"%1$s\" id=\"%4$s\" name=\"%4$s\" onclick=\"%5$s\" title=\"%3$s\">%2$s</a>";
		}
		
		if (elementName == null)
			elementName = "" ;
		if (onclick == null)
			onclick = "" ;

		if (comp == null) {
			// Undefined == Anonymous Access
			if (isAllowAccessToUndefinedUrl()) {
				if (!StringUtils.isBlank(this.href)) {
					if (StringUtils.isBlank(title)) {
						write(String.format(format, href, name, name));
					} else {
						String aText = ExpressionEvaluationUtils
								.evaluateString("title", title, pageContext);
						write(String.format(format, href, aText, aText));
					}
				}
			} else if (isHideUnaccessibleLinks()) {
				// Do Not Have Access To URL and User Does Not Want to Display Unaccessible Link
				// Do Nothing
			}
		} else { // Url Is Defined in the Configuration File
			Object[] argumentsArray = null;
			if (this.arguments instanceof String) {
				argumentsArray = StringUtils
						.split((String) this.arguments, ',');
				for (int i = 0; i < argumentsArray.length; i++) {
					argumentsArray[i] = ExpressionEvaluationUtils
							.evaluateString("argument[" + i + "]",
									(String) argumentsArray[i], pageContext);

				}
			} else if (this.arguments instanceof Object[]) {
				argumentsArray = (Object[]) this.arguments;
			} else if (this.arguments instanceof Collection) {
				argumentsArray = ((Collection) this.arguments).toArray();
			} else if (this.arguments != null) {
				// assume a single argument object
				argumentsArray = new Object[] { this.arguments };
			}

			// Undefined == Anonymous Access
			if (StringUtils.isBlank(this.href)) {
				this.href = comp.getPage();
			}

			if (StringUtils.isBlank(this.title)) {
				if (StringUtils.isBlank(comp.getTitle())) {
					this.title = comp.getName();
				} else {
					this.title = comp.getTitle();
				}
			} else {
				this.title = ExpressionEvaluationUtils.evaluateString("title",
						title, pageContext);

			}

			if (isLocalized()) {
				this.title = getMessageSource().getMessage(this.title, null,
						this.title, pageContext.getRequest().getLocale());
			}

			if (argumentsArray != null) {
				href = String.format(comp.getPage(), argumentsArray);
			}
			


			if (permissions.isAllowed(comp)) {
				String contextPath = ((HttpServletRequest)this.pageContext.getRequest()).getContextPath();
				/*
				if(getRequestContext()!=null) {
					try {
						contextPath = getRequestContext().getContextPath();
					}catch(Exception e) { 
						log.error("Context path was null");
						contextPath = "/car";
					}
					
				} else {
					contextPath = "/car";
					log.debug("Context path is null.. using default");
				}
				*/
				if(StringUtils.isNotBlank(contextPath) && !contextPath.equals(FORWARD_SLASH)) {
					if(this.href.startsWith(FORWARD_SLASH) ) {
						this.href =  contextPath + this.href;
					}					
				}
				if (StringUtils.isNotBlank(cssStyle)) {
					write(String.format(format, this.href, this.title,
							this.title, this.cssStyle, this.elementName,this.onclick));
				} else {
					write(String.format(format, this.href, this.title,
							this.title,this.elementName,this.onclick));
				}
			} else {
				if (!isHideUnaccessibleLinks()) {
					write(this.title);
				}
			}
		}

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
		//if (this.requestContext == null) {
		RequestContext requestContext = null ;
			try {
				requestContext = new RequestContext(
						(HttpServletRequest) pageContext.getRequest());
			} catch (RuntimeException ex) {
				log.error("Runtime Exception");
			//throw ex;
		} catch (Exception ex) {				
			pageContext.getServletContext().log("Exception in custom tag",
					ex);
		}
		//}
		return requestContext;
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
		this.name = null;
		this.scope = null;
		this.id = null;

	}

	public Object getArguments() {
		return arguments;
	}

	public void setArguments(Object arguments) {
		this.arguments = arguments;
	}

	public boolean isAllowAccessToUndefinedUrl() {
		return allowAccessToUndefinedUrl;
	}

	public void setAllowAccessToUndefinedUrl(boolean allowAccessToUndefinedUrl) {
		this.allowAccessToUndefinedUrl = allowAccessToUndefinedUrl;
	}

	public boolean isHideUnaccessibleLinks() {
		return hideUnaccessibleLinks;
	}

	public void setHideUnaccessibleLinks(boolean hideUnaccessibleLinks) {
		this.hideUnaccessibleLinks = hideUnaccessibleLinks;
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

	/**
	 * @return the elementName
	 */
	public String getElementName() {
		return elementName;
	}

	/**
	 * @param elementName the elementName to set
	 */
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	/**
	 * @return the onclick
	 */
	public String getOnclick() {
		return onclick;
	}

	/**
	 * @param onclick the onclick to set
	 */
	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	

}
