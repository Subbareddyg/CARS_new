package com.belk.car.app.model.report;

import java.util.List;

import com.belk.car.app.model.Car;

public class CarDetailReport extends BaseReport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3116454148512377506L;

	private List<Car> cars ;
	
	private String colorName;
	
	public List<Car> getCars() {
		return cars;
	}

	public void setCars(List<Car> cars) {
		this.cars = cars;
	}
	
	/**
	 * @return the colorName
	 */
	public String getColorName() {
		return colorName;
	}

	/**
	 * @param colorName the colorName to set
	 */
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
}
