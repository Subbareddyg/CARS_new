package com.belk.car.app.model.report;

import java.util.List;

import com.belk.car.app.model.view.CarCmpStatusView;

public class CarCmpStatusReport extends BaseReport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6860569694613319914L;
	
	private List<CarCmpStatusView> carCmpStatus ;

	public List<CarCmpStatusView> getCarCmpStatus() {
		return carCmpStatus;
	}

	public void setCarCmpStatus(List<CarCmpStatusView> carCmpStatus) {
		this.carCmpStatus = carCmpStatus ;
	}

}
