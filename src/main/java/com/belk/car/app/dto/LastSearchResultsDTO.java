package com.belk.car.app.dto;

import java.io.Serializable;
import com.belk.car.app.model.Car;

public class LastSearchResultsDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7934879391498838071L;

	private Car cars;
	
	private String colorName;
	
	public Car getCar() {
		return cars;
	}
	public void setCar(Car car) {
		this.cars = car;
	}
	
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	
	
	
	
	

}
