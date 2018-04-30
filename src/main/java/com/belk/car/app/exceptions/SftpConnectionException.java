package com.belk.car.app.exceptions;

/**
 * @author AFUTXD3
 * 
 */
public class SftpConnectionException extends Exception {
	public SftpConnectionException(String msg) {
		super(msg);
	}

	public SftpConnectionException(String msg, Throwable w) {
		super(msg, w);
	}

}
