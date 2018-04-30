package com.belk.car.security;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.belk.car.util.LoadableResourceException;


/**
 * This class is designed for use in applications that don't use Struts
 * but do want to use Struts Url.  You simply need to configure this
 * listener in your web.xml file with the following syntax:</p>
 * <pre>
 *   &lt;!--
 *   - Loads the url-config.xml for struts-url at startup,
 *   - by default from "/WEB-INF/url-config.xml".
 *   - To override this, add a context-param named "urlConfigLocation"
 *   - web.xml file.
 *  --&gt;
 * &lt;listener&gt;
 *  &lt;listener-class&gt;com.belk.car.security.UrlContextListener&lt;/listener-class&gt;
 * &lt;/listener&gt;
 * </pre>
 * 
 * @author Matt Raible
 */
public class UrlContextListener implements ServletContextListener {
    private static Log log = LogFactory.getLog(UrlContextListener.class);
    private ServletContext ctx;

    /** Configuration file for urls */
    private String urlConfig = "/WEB-INF/url-config.xml";

    /**
     * Initialization of the Url Repository.
     */
    public void contextInitialized(ServletContextEvent sce) {
        ctx = sce.getServletContext();

        if (log.isDebugEnabled()) {
            log.debug("Starting struts-url initialization");
        }
        
        // check for urlConfigLocation context-param
        String override = 
            sce.getServletContext().getInitParameter("urlConfigLocation");
        if (override != null) {
            if (log.isDebugEnabled()) {
                log.debug("using urlConfigLocation: " + override);
            }
            this.urlConfig = override;
        }
        
        UrlRepository repository = new UrlRepository();
        repository.setLoadParam(urlConfig);
        repository.setServletContext(ctx);

        try {
            repository.load();
            ctx.setAttribute(UrlRepository.URL_REPOSITORY_KEY, repository);

            if (log.isDebugEnabled()) {
                log.debug("struts-url initialization successfull");
            }
        } catch (LoadableResourceException lre) {
            log.fatal("Failure initializing struts-url: " + lre.getMessage());
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        if (log.isDebugEnabled()) {
            log.debug("destroying struts-url...");
        }

        sce.getServletContext().removeAttribute(UrlRepository.URL_REPOSITORY_KEY);
        urlConfig = null;
        ctx = null;
    }
}
