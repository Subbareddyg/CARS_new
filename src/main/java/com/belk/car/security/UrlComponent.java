/*
 * UrlComponent.java
 *
 * Created on January 28, 2001, 8:10 PM
 */
package com.belk.car.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * This class extends {@link UrlBase} and basically contains helper methods
 * for adding and fetching children and parents.
 *
 * @author vsundar
 * @version $Revision$ $Date$
 */
public class UrlComponent extends UrlBase implements Serializable, Component {
	//~ Static fields/initializers =============================================
	protected static UrlComponent[] _urlComponent = new UrlComponent[0];
	public static String ANONYMOUS = "Anonymous" ;

	private Pattern delimiters = Pattern.compile("(?<!\\\\),");

	//~ Instance fields ========================================================

	protected List<UrlComponent> urlComponents = Collections
			.synchronizedList(new ArrayList<UrlComponent>());
	protected UrlComponent parentUrl;
	private boolean last;
	private String breadCrumb;

	
	private List<String> rolesList ;
	
	//~ Methods ================================================================

	public void addUrlComponent(UrlComponent urlComponent) {
		if ((urlComponent.getName() == null)
				|| (urlComponent.getName().equals(""))) {
			urlComponent.setName(this.name + urlComponents.size());
		}

		if (!urlComponents.contains(urlComponent)) {
			urlComponents.add(urlComponent);
			urlComponent.setParent(this);
		}
	}

	public UrlComponent[] getUrlComponents() {
		return (UrlComponent[]) urlComponents.toArray(_urlComponent);
	}

	public void setUrlComponents(UrlComponent[] urlComponents) {
		for (int i = 0; i < urlComponents.length; i++) {
			UrlComponent component = urlComponents[i];
			this.urlComponents.add(component);
		}
	}

	public void setParent(UrlComponent parentUrl) {
		if (parentUrl != null) {
			// look up the parent and make sure that it has this url as a child
			if (!parentUrl.getComponents().contains(this)) {
				parentUrl.addUrlComponent(this);
			}
		}
		this.parentUrl = parentUrl;
	}

	public UrlComponent getParent() {
		return parentUrl;
	}

	/**
	 * Convenience method for Velocity templates
	 * @return urlComponents as a java.util.List
	 */
	public List getComponents() {
		return urlComponents;
	}

	/**
	 * This method compares all attributes, except for parent and children
	 *
	 * @param o the object to compare to
	 */
	public boolean equals(Object o) {
		if (!(o instanceof UrlComponent)) {
			return false;
		}
		UrlComponent m = (UrlComponent) o;
		// Compare using StringUtils to avoid NullPointerExceptions
		return StringUtils.equals(m.getAction(), this.action)
				&& StringUtils.equals(m.getAlign(), this.align)
				&& StringUtils.equals(m.getAltImage(), this.altImage)
				&& StringUtils.equals(m.getDescription(), this.description)
				&& StringUtils.equals(m.getForward(), this.forward)
				&& StringUtils.equals(m.getHeight(), this.height)
				&& StringUtils.equals(m.getImage(), this.image)
				&& StringUtils.equals(m.getLocation(), this.location)
				&& StringUtils.equals(m.getName(), this.name)
				&& StringUtils.equals(m.getOnclick(), this.onclick)
				&& StringUtils.equals(m.getOndblclick(), this.ondblclick)
				&& StringUtils.equals(m.getOnmouseout(), this.onmouseout)
				&& StringUtils.equals(m.getOnmouseover(), this.onmouseover)
				&& StringUtils.equals(m.getOnContextUrl(), this.onContextUrl)
				&& StringUtils.equals(m.getPage(), this.page)
				&& StringUtils.equals(m.getRoles(), this.roles)
				&& StringUtils.equals(m.getTarget(), this.target)
				&& StringUtils.equals(m.getTitle(), this.title)
				&& StringUtils.equals(m.getToolTip(), this.toolTip)
				&& StringUtils.equals(m.getWidth(), this.width)
				&& StringUtils.equals(m.getModule(), this.module);
	}

	/**
	 * Get the depth of the url
	 *
	 * @return Depth of url
	 */
	public int getUrlDepth() {
		return getUrlDepth(this, 0);
	}

	private int getUrlDepth(UrlComponent url, int currentDepth) {
		int depth = currentDepth + 1;

		UrlComponent[] subUrls = url.getUrlComponents();
		if (subUrls != null) {
			for (int a = 0; a < subUrls.length; a++) {
				int depthx = getUrlDepth(subUrls[a], currentDepth + 1);
				if (depth < depthx)
					depth = depthx;
			}
		}

		return depth;
	}

	/**
	 * Returns the last.
	 *
	 * @return boolean
	 */
	public boolean isLast() {
		return last;
	}

	/**
	 * Sets the last.
	 *
	 * @param last The last to set
	 */
	public void setLast(boolean last) {
		this.last = last;
	}

	/**
	 * Remove all children from a parent url item
	 */
	public void removeChildren() {
		for (Iterator iterator = this.getComponents().iterator(); iterator
				.hasNext();) {
			UrlComponent child = (UrlComponent) iterator.next();
			child.setParent(null);
			iterator.remove();
		}
	}

	public String getBreadCrumb() {
		return breadCrumb;
	}

	/**
	 * Build the breadcrumb trail leading to this urlComponent
	 *
	 * @param delimiter type of separator
	 */
	protected void setBreadCrumb(String delimiter) {
		if (getParent() == null) {
			breadCrumb = name;
			setChildBreadCrumb(delimiter);
		} else {
			UrlComponent parent = getParent();
			breadCrumb = parent.getBreadCrumb() + delimiter + name;
			setChildBreadCrumb(delimiter);
		}
	}

	private void setChildBreadCrumb(String delimiter) {
		List children = this.getComponents();
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			UrlComponent child = (UrlComponent) iterator.next();
			child.setBreadCrumb(delimiter);
		}
	}

	public String toString() {
		return "name: " + this.name;
	}

	public List<String> getRolesList() {
		if (rolesList == null) {
			rolesList = new ArrayList<String>() ;
		}
		return rolesList ;
	}
	
	public void setRoles(String roles) {
		this.roles = roles ;
		rolesList = new ArrayList<String>();
		if (StringUtils.isBlank(getRoles())) {
			rolesList.add(UrlComponent.ANONYMOUS) ;
		} else {
			// Get the list of roles this url allows
			String[] allowedRoles = delimiters.split(getRoles());
			for (int i = 0; i < allowedRoles.length; i++) {
				rolesList.add(allowedRoles[i].trim());
			}
		}
	}
}