package com.belk.car.app.exceptions;

public class ProductTypeExistsException extends Exception {    

    /**
     * Constructor for ProductTypeExistsExceptionProductTypeExistsException.
     *
     * @param message exception message
     */
    public ProductTypeExistsException(final String message) {
        super(message);
    }

}
