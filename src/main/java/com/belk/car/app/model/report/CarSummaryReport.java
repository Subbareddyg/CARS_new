package com.belk.car.app.model.report;

import java.util.List;

public class CarSummaryReport extends BaseReport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1464222287934849020L;
	
	private List<CarSummaryData> summaryData ;

	public List<CarSummaryData> getSummaryData() {
		return summaryData;
	}

	public void setSummaryData(List<CarSummaryData> summaryData) {
		this.summaryData = summaryData;
	}

}
