package com.belk.car.app.model.report;

import java.util.List;

import com.belk.car.app.model.view.CarAttributeSummaryView;
import com.belk.car.app.model.view.CarAttributeView;

public class CarAttributeReport extends BaseReport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3165022648509394748L;

	private List<CarAttributeView> carAttributeList;
	private List<CarAttributeSummaryView> carAttributeSummaryList;

	public List<CarAttributeView> getCarAttributeList() {
		return carAttributeList;
	}

	public void setCarAttributeList(List<CarAttributeView> carAttributeList) {
		this.carAttributeList = carAttributeList;
	}

	public List<CarAttributeSummaryView> getCarAttributeSummaryList() {
		return carAttributeSummaryList;
	}

	public void setCarAttributeSummaryList(List<CarAttributeSummaryView> carAttributeSummaryList) {
		this.carAttributeSummaryList = carAttributeSummaryList;
	}

}
