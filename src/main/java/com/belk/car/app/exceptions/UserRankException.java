package com.belk.car.app.exceptions;

/**
 * @author afutxd3
 * 
 */
public class UserRankException extends Exception {
	private String code;
	private String msg;

	public UserRankException(Throwable t) {

	}

	public UserRankException(String errorCode, String errorMsg, Throwable t) {
		this.code = errorCode;
		this.msg = errorMsg;
	}

	public UserRankException(String errorCode, String errorMsg) {
		this.code = errorCode;
		this.msg = errorMsg;
	}
}
