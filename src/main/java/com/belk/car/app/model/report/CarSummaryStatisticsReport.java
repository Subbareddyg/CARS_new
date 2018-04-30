package com.belk.car.app.model.report;

import java.util.List;

import com.belk.car.app.model.Car;

public class CarSummaryStatisticsReport extends BaseReport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7181166365771850173L;

	private List<CarSummaryStatisticsData> carSummaryList ;

	public List<CarSummaryStatisticsData> getCarSummaryList() {
		return carSummaryList;
	}

	public void setCarSummaryList(List<CarSummaryStatisticsData> carSummaryList) {
		this.carSummaryList = carSummaryList;
	}

}
