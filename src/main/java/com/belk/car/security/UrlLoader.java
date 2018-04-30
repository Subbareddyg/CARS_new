package com.belk.car.security;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.context.support.WebApplicationObjectSupport;

import com.belk.car.util.LoadableResourceException;

/**
 * This loader is available for those that use the Spring Framework.  To 
 * use it, simply configure it as follows in your applicationContext.xml file.
 * </p>
 * <pre>
 * &lt;bean id="url" class="com.belk.car.security.UrlLoader"&gt;
 *  &lt;property name="urlConfig"&gt;
 *      &lt;value&gt;/WEB-INF/url-config.xml&lt;/value&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * <p>The urlConfig property is an optional attribute.  It is set to 
 * /WEB-INF/url-config.xml by default.</p>
 * 
 * @author vsundar
 */
public class UrlLoader extends WebApplicationObjectSupport {
    private static Log log = LogFactory.getLog(UrlLoader.class);

    /** Configuration file for urls */
    private String urlConfig = "/WEB-INF/url-config.xml";

    /**
     * Set the Url configuration file
     * @param urlConfig the file containing the Urls/Items
     */
    public void setUrlConfig(String urlConfig) {
        this.urlConfig = urlConfig;
    }

    /**
     * Initialization of the Url Repository.
     * @throws org.springframework.context.ApplicationContextException if an error occurs
     */
    protected void initApplicationContext() throws ApplicationContextException {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Starting struts-url initialization");
            }

            UrlRepository repository = new UrlRepository();
            repository.setLoadParam(urlConfig);
            repository.setServletContext(getServletContext());

            try {
                repository.load();
                getServletContext().setAttribute(UrlRepository.URL_REPOSITORY_KEY, repository);

                if (log.isDebugEnabled()) {
                    log.debug("struts-url initialization successful");
                }
            } catch (LoadableResourceException lre) {
                throw new ServletException("Failure initializing struts-url: " +
                                           lre.getMessage());
            }
        } catch (Exception ex) {
            throw new ApplicationContextException("Failed to initialize Struts Url repository",
                                                  ex);
        }
    }
}
