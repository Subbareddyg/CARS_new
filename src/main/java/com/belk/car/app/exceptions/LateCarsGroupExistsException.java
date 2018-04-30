package com.belk.car.app.exceptions;

/**
 * Exception for Late Cars Group already exists.
 * @author AFUMXB4
 *
 */
public class LateCarsGroupExistsException extends Exception {    

    /**
     * Constructor for LateCarsGroupExistsException.
     *
     * @param message exception message
     */
    public LateCarsGroupExistsException(final String message) {
        super(message);
    }

}
