package com.belk.car.product.integration.exception;

/**
 * Class to handle all integration failures with the new PIM system.
 * 
 */
public class BelkProductIntegrationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BelkProductIntegrationException(Exception e) {
		super(e);
	}
	
	public BelkProductIntegrationException(String message, Exception e) {
		super(message, e);
	}
	
	public BelkProductIntegrationException(String message) {
		super(message);
	}
}
