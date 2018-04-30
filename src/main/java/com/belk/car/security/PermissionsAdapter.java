/*
 * PermissionsAdapter.java
 *
 * Created on April 29, 2002, 10:15 PM
 */

package com.belk.car.security;

/**
 * Defines a pluggable adapter into the menu framework that is used for 
 * checking permissions on urls. 
 *
 * @author  vsundar
 */
public interface PermissionsAdapter {
    
    /**
     * If the url is allowed, this should return true.
     *
     * @return whether or not the url is allowed.
     */
    public boolean isAllowed(UrlComponent url);
    
}
