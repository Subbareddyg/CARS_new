package com.belk.car.app.exceptions;

/**
 * Exception for Product Group already exists.
 * @author AFUSXS7
 *
 */
public class ProductGroupExistsException extends Exception {    

    /**
     * Constructor for ProductGroupExistsException.
     *
     * @param message exception message
     */
    public ProductGroupExistsException(final String message) {
        super(message);
    }

}
