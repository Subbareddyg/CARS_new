/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.integrations.rrd.dao;

/**
 *
 * @author amuaxg1
 */
public class ReturnInstructionsDAO {
	
	long carID;
	public long getCarID() { return this.carID; }
	public void setCarID(long carID) { this.carID = carID; }
	
	String returnInstructions;
	public String getReturnInstructions() { return this.returnInstructions; }
	public void setReturnInstructions(String returnInstructions) { this.returnInstructions = returnInstructions; }
	
}
