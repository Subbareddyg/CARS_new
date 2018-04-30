package com.belk.car.app.model.report;

import java.util.List;

import com.belk.car.app.model.view.CarExecutiveSummaryView;

public class CarExecutiveSummaryReport extends BaseReport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1464222287934849020L;
	
	private List<CarExecutiveSummaryView> summaryData ;

	public List<CarExecutiveSummaryView> getSummaryData() {
		return summaryData;
	}

	public void setSummaryData(List<CarExecutiveSummaryView> summaryData) {
		this.summaryData = summaryData;
	}

}
