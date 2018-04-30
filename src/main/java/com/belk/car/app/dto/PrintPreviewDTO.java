/**
 * 
 */
package com.belk.car.app.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.acegisecurity.userdetails.User;

import com.belk.car.app.model.Car;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.WorkflowTransitionInfo;
import com.belk.car.app.webapp.forms.ChildCar;

/**
 * @author afusy01
 *
 */
public class PrintPreviewDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -747426007400444847L;
	
	private Car car;
	private WorkflowTransitionInfo workflowTransition;
	private List<VendorStyle> vendorStyle;
	private List<ChildCar> childCar;
	
	public Car getCar() {
		return car;
	}
	public void setCar(Car car) {
		this.car = car;
	}
	public List<VendorStyle> getVendorStyle() {
		return vendorStyle;
	}
	public void setVendorStyle(List<VendorStyle> vendorStyles) {
		this.vendorStyle = vendorStyles;
	}
	public List<ChildCar> getChildCar() {
		return childCar;
	}
	public void setChildCar(List<ChildCar> viewChildCars) {
		this.childCar = viewChildCars;
	}
	public WorkflowTransitionInfo getWorkflowTransition() {
		return workflowTransition;
	}
	public void setWorkflowTransition(WorkflowTransitionInfo workflowTransition) {
		this.workflowTransition = workflowTransition;
	}
	

	
	
	
}
