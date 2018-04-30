/*
 * UrlRepository.java
 *
 * Created on January 29, 2001, 9:51 AM
 */
package com.belk.car.security;

//import net.sf.navigator.displayer.UrlDisplayerMapping;
//import net.sf.navigator.displayer.VelocityUrlDisplayer;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.belk.car.util.LoadableResource;
import com.belk.car.util.LoadableResourceException;

/**
 * Holder of Urls and their items. Can be populated programmatically.
 *
 * @author  vsundar
 */
public class UrlRepository implements LoadableResource, Serializable {
	//~ Static fields/initializers =============================================

	public static final String URL_REPOSITORY_KEY = "com.belk.car.security.URL_REPOSITORY";
	private static Log log = LogFactory.getLog(UrlRepository.class);

	//~ Instance fields ========================================================

	protected String config = null;
	protected String name = null;
	protected ServletContext servletContext = null;
	protected Map urls = new LinkedMap();
	protected LinkedMap displayers = new LinkedMap();
	protected LinkedMap templates = new LinkedMap();
	protected LinkedMap urlsByRole = new LinkedMap() ;

	private String breadCrumbDelimiter;

	//~ Methods ================================================================
	public Set getUrlNames() {
		return urls.keySet();
	}

	/**
	 * Convenience method for dynamic urls - returns the top-level urls
	 * only.
	 */
	public List<UrlComponent> getTopUrls() {
		List<UrlComponent> topUrls = new ArrayList<UrlComponent>();
		if (urls == null) {
			log.warn("No urls found in repository!");
			return topUrls;
		}

		for (Iterator it = urls.keySet().iterator(); it.hasNext();) {
			String name = (String) it.next();
			UrlComponent url = getUrl(name);
			if (url.getParent() == null) {
				topUrls.add(url);
			}
		}
		return topUrls;
	}

	public UrlComponent getUrl(String urlName) {
		return (UrlComponent) urls.get(urlName);
	}

	//    public UrlDisplayerMapping getUrlDisplayerMapping(String displayerName) {
	//        return (UrlDisplayerMapping) displayers.get(displayerName);
	//    }

	protected Digester initDigester() {
		Digester digester = new Digester();
		digester.setClassLoader(Thread.currentThread().getContextClassLoader());
		digester.push(this);

		//digester.setDebug(getDebug());
		// 1
		digester.addObjectCreate("UrlConfig/Urls/Url",
				"com.belk.car.security.UrlComponent", "type");
		digester.addSetProperties("UrlConfig/Urls/Url");
		digester.addSetNext("UrlConfig/Urls/Url", "addUrl");

		//        // 2
		//        digester.addObjectCreate("UrlConfig/Urls/Url/Item", "com.belk.car.security.UrlComponent", "type");
		//        digester.addSetProperties("UrlConfig/Urls/Url/Item");
		//        digester.addSetNext("UrlConfig/Urls/Url/Item", "addUrlComponent", "com.belk.car.security.UrlComponent");
		//
		//        // 3        
		//        digester.addObjectCreate("UrlConfig/Urls/Url/Item/Item", "com.belk.car.security.UrlComponent", "type");
		//        digester.addSetProperties("UrlConfig/Urls/Url/Item/Item");
		//        digester.addSetNext("UrlConfig/Urls/Url/Item/Item", "addUrlComponent", "com.belk.car.security.UrlComponent");
		//
		//        // 4
		//        digester.addObjectCreate("UrlConfig/Urls/Url/Item/Item/Item", "com.belk.car.security.UrlComponent", "type");
		//        digester.addSetProperties("UrlConfig/Urls/Url/Item/Item/Item");
		//        digester.addSetNext("UrlConfig/Urls/Url/Item/Item/Item", "addUrlComponent", "com.belk.car.security.UrlComponent");
		//
		//        // 5
		//        digester.addObjectCreate("UrlConfig/Urls/Url/Item/Item/Item/Item", "com.belk.car.security.UrlComponent", "type");
		//        digester.addSetProperties("UrlConfig/Urls/Url/Item/Item/Item/Item");
		//        digester.addSetNext("UrlConfig/Urls/Url/Item/Item/Item/Item", "addUrlComponent", "com.belk.car.security.UrlComponent");
		//
		//        // 6
		//        digester.addObjectCreate("UrlConfig/Urls/Url/Item/Item/Item/Item/Item", "com.belk.car.security.UrlComponent", "type");
		//        digester.addSetProperties("UrlConfig/Urls/Url/Item/Item/Item/Item/Item");
		//        digester.addSetNext("UrlConfig/Urls/Url/Item/Item/Item/Item/Item", "addUrlComponent", "com.belk.car.security.UrlComponent");
		//
		//        // 7
		//        digester.addObjectCreate("UrlConfig/Urls/Url/Item/Item/Item/Item/Item/Item", "com.belk.car.security.UrlComponent", "type");
		//        digester.addSetProperties("UrlConfig/Urls/Url/Item/Item/Item/Item/Item/Item");
		//        digester.addSetNext("UrlConfig/Urls/Url/Item/Item/Item/Item/Item/Item", "addUrlComponent", "com.belk.car.security.UrlComponent");
		//
		//        digester.addObjectCreate("UrlConfig/Displayers/Displayer", "net.sf.navigator.displayer.UrlDisplayerMapping", "mapping");
		//        digester.addSetProperties("UrlConfig/Displayers/Displayer");
		//        digester.addSetNext("UrlConfig/Displayers/Displayer", "addUrlDisplayerMapping", "net.sf.navigator.displayer.UrlDisplayerMapping");
		//        digester.addSetProperty("UrlConfig/Displayers/Displayer/SetProperty", "property", "value");

		return digester;
	}

	/**
	 * Adds a new url.  This is called when parsing the url xml definition.
	 * @param url The url component to add.
	 */
	public void addUrl(UrlComponent url) {
		if (urls.containsKey(url.getName())) {
			if (log.isDebugEnabled()) {
				log.warn("Url '" + url.getName()
						+ "' already exists in repository");
			}
			List children = (getUrl(url.getName())).getComponents();
			if (children != null && url.getComponents() != null) {
				for (Iterator it = children.iterator(); it.hasNext();) {
					UrlComponent child = (UrlComponent) it.next();
					url.addUrlComponent(child);
				}
			}
		}
		urls.put(url.getName(), url);
		
		List urlsInRole = null;
		for (String role : url.getRolesList()) {
			
			if (urlsByRole.containsKey(role)) {
				urlsInRole = (List) urlsByRole.get(role) ;
			} else {
				urlsInRole = new ArrayList() ;
			}
			urlsInRole.add(url) ;
			urlsByRole.put(role, urlsInRole);
		}
	}

	/**
	 * Allows easy removal of a url by its name.
	 * @param name
	 */
	public void removeUrl(String name) {
		urls.remove(name);
	}

	/**
	 * Allows easy removal of all urls, suggested use for users wanting to reload urls without having to perform
	 * a complete reload of the UrlRepository
	 */
	public void removeAllUrls() {
		urls.clear();
	}

	//    public void addUrlDisplayerMapping(UrlDisplayerMapping displayerMapping) {
	//        displayers.put(displayerMapping.getName(), displayerMapping);
	//        if (displayerMapping.getType().equals("net.sf.navigator.displayer.VelocityUrlDisplayer")) {
	//            if (servletContext == null) {
	//                log.error("ServletContext not set - can't initialize Velocity");
	//            } else {
	//                VelocityUrlDisplayer.initialize(servletContext);
	//            }
	//        }
	//    }

	/**
	 * This method is so url repositories can retrieve displayers from the
	 * default repository specified in url-config.xml
	 * @return the displayers specified in this repository
	 */
	public LinkedMap getDisplayers() {
		return displayers;
	}

	/**
	 * Allow the displayers to be set as a whole.  This should only be used
	 * when copying the displayers from the default repository to a newly
	 * created repository.
	 * @param displayers
	 */
	public void setDisplayers(LinkedMap displayers) {
		this.displayers = displayers;
	}

	public void load() throws LoadableResourceException {
		if (getServletContext() == null) {
			throw new LoadableResourceException(
					"no reference to servlet context found");
		}

		InputStream input = null;
		Digester digester = initDigester();

		try {
			input = getServletContext().getResourceAsStream(config);
			digester.parse(input);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LoadableResourceException("Error parsing resource file: "
					+ config + " nested exception is: " + e.getMessage());
		} finally {
			try {
				input.close();
			} catch (Exception e) {
			}
		}
	}

	public void reload() throws LoadableResourceException {
		urls.clear();
		displayers.clear();
		load();
	}

	public void setLoadParam(String loadParam) {
		config = loadParam;
	}

	public String getLoadParam() {
		return config;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext context) {
		this.servletContext = context;
	}

	/**
	 * Method getUrl.  Get a subUrl beneath a root or parent url.  Will drill-down as deep as requested
	 * @param urlName - e.g grandParent.parent.url
	 * @param delimiter - e.g. '.'
	 * @return UrlComponent
	 */
	public UrlComponent getUrl(String urlName, String delimiter) {
		UrlComponent parent = null;
		StringTokenizer st = new StringTokenizer(urlName, delimiter);
		boolean firstUrl = true;

		while (st.hasMoreTokens()) {
			if (firstUrl) {
				parent = this.getUrl(st.nextToken());
				firstUrl = false;
			} else {
				UrlComponent child = null;
				String name = st.nextToken();
				for (int a = 0; a < parent.getComponents().size(); a++) {
					if (name.equals(((UrlComponent) parent.getComponents().get(
							a)).getName())) {
						child = (UrlComponent) parent.getComponents().get(a);
						a = parent.getComponents().size();
					}
				}
				if (child != null) {
					parent = child;
				} else {
					parent = null;
					break;
				}
			}
		}

		return parent;
	}

	/**
	 * Method getUrlDepth.
	 * Get the depth of the deepest sub-url within the requested top url
	 * @param urlName - name of the top url to check the url depth 
	 * @return int.  If no urlName found return -1
	 */
	public int getUrlDepth(String urlName) {

		UrlComponent url = this.getUrl(urlName);
		if (url == null)
			return -1;
		if (url.getUrlComponents() == null)
			return 1;
		return url.getUrlDepth();
	}

	/**
	 * Method getUrlDepth.
	 * Get the depth of the deepest sub-url throughout all urls held in the repository
	 * @return int.  If no urls return -1.
	 */
	public int getUrlDepth() {
		int currentDepth = 0;

		List topUrls = this.getTopUrls();

		if (topUrls == null)
			return -1;
		for (Iterator url = topUrls.iterator(); url.hasNext();) {
			int depth = ((UrlComponent) url.next()).getUrlDepth();
			if (currentDepth < depth)
				currentDepth = depth;
		}
		return currentDepth;
	}

	/**
	 * Method getTopUrlsAsArray.  Get urls as array rather than a List
	 * @return UrlComponent[]
	 */
	public UrlComponent[] getTopUrlsAsArray() {
		List urlList = this.getTopUrls();
		UrlComponent[] urls = new UrlComponent[urlList.size()];
		for (int a = 0; a < urlList.size(); a++) {
			urls[a] = (UrlComponent) urlList.get(a);
		}

		return urls;
	}

	/**
	 * Get a List of all the top urls' names
	 * @return List
	 */
	public List getTopUrlsNames() {
		List urls = this.getTopUrls();
		ArrayList names = new ArrayList();
		for (Iterator iterator = urls.iterator(); iterator.hasNext();) {
			UrlComponent url = (UrlComponent) iterator.next();
			names.add(url.getName());
		}
		return names;
	}

	public void setBreadCrumbDelimiter(String string) {
		breadCrumbDelimiter = string;
	}

	public void buildBreadCrumbs() {
		if (breadCrumbDelimiter == null) {
			throw new NullPointerException("No breadCrumbDelimiter present");
		}
		ArrayList urls = (ArrayList) this.getTopUrls();
		for (Iterator iterator = urls.iterator(); iterator.hasNext();) {
			UrlComponent url = (UrlComponent) iterator.next();
			url.setBreadCrumb(breadCrumbDelimiter);
		}
	}

	public void buildBreadCrumbs(String delimiter) {
		this.breadCrumbDelimiter = delimiter;
		buildBreadCrumbs();
	}
	
	public List<String> getRoles() {
		List<String> roles = new ArrayList();
		if (urlsByRole == null) {
			log.warn("No roles found in repository!");
			return roles;
		}

		for (Iterator it = urlsByRole.keySet().iterator(); it.hasNext();) {
			roles.add((String) it.next());
		}
		return roles;
	}
	
	public List getUrlsInRole(String role) {
		if (urlsByRole.containsKey(role)) {
			return (List) urlsByRole.get(role);
		} else
			return new ArrayList() ;
	}
	
	public String getUrlByRole() {
		StringBuffer strB = new StringBuffer();
		for (String role : getRoles()) {
			strB.append("<ul>");
			strB.append(role);
			List urls = getUrlsInRole(role) ;
			for (Iterator iterator = urls.iterator(); iterator.hasNext();) {
				UrlComponent comp = (UrlComponent) iterator.next();
				strB.append("<li>").append(comp.name).append("</li>");
			}
			strB.append("</ul>");
		}
		return strB.toString();
	}
}
