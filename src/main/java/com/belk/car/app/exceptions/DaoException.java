/*
 * Copyright 2004 RxAPS, Inc.
 * Created on Jun 21, 2004 at 3:38:37 PM by Nick Marchalleck
 *
 */
package com.belk.car.app.exceptions;

import org.springframework.dao.DataAccessException;


public class DaoException extends DataAccessException {

	/**
	 * @param arg0
	 */
	public DaoException(String arg0) {
		super(arg0);
	}
	
	/**
	 * @param arg0, arg1
	 */
	public DaoException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}


}
