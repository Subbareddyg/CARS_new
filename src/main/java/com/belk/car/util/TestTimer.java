////////////////////////////////////////////////////////////////////////////////
// TestTimer: $Source$
// TODO Class summary goes here
//
// Created : 31 oct. 2005 by jfsmart
// Last modified $Date$ by $Author$
// Revision: $Revision$
// Version : $ID$
// Copyright (c) 2005
////////////////////////////////////////////////////////////////////////////////

package com.belk.car.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.belk.car.app.webapp.controller.admin.user.UserManagerController;

/**
 * Simple utilities class for benchmarking unit tests. Use is a follows:
 *     TestTimer t = new TestTimer("My task");
 * 	   // Do something
 *     t.done();
 */
public class TestTimer {

	
	private transient final Log log = LogFactory
	.getLog(TestTimer.class);
	
	private long startTime;
	private String message; 

	/**
	 * Initiate a timer
	 */
	public TestTimer(String message) {
		startTime = System.currentTimeMillis();
		this.message = message;
	}
	
	/**
	 * Reset the timer for another timing session.
	 *
	 */
	public void reset() {
		startTime = System.currentTimeMillis();		
	}
	
	/**
	 * End the timing session and output the results.
	 */
	public void done() {

		System.out.println(message 
						+ " : "
				   		+ (System.currentTimeMillis() - startTime) 
				   		+ " ms.");		
	}
}
