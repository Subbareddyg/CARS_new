/*
 * RolesPermissionsAdapter.java
 *
 * Created on December 7, 2002 2:25 PM
 */

package com.belk.car.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * This class used container-managed security to check access
 * to urls.  The roles are set in url-config.xml.
 *
 * @author vsundar
 */
public class RolesPermissionsAdapter implements PermissionsAdapter {
    //private Pattern delimiters = Pattern.compile("(?<!\\\\),");
    private HttpServletRequest request;

    public RolesPermissionsAdapter(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * If the url is allowed, this should return true.
     *
     * @return whether or not the url is allowed.
     */
    public boolean isAllowed(UrlComponent url) {
        if (StringUtils.isBlank(url.getRoles())) {
            return true; // no roles define, allow everyone
        } else {
            // Get the list of roles this url allows
        	for(String role: url.getRolesList()) {
        		if(request.isUserInRole(role)) {
        			return true;
        		}
        	}
        }
        return false;
    }


}
