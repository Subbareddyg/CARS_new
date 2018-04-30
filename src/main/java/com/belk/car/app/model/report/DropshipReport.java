/**
 * 
 */
package com.belk.car.app.model.report;

import java.util.List;

import com.belk.car.app.model.view.DropshipReportFM;

/**
 * @author AFUSY38
 *
 */
public class DropshipReport extends BaseReport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4332480501088170618L;
	
	private List<DropshipReportFM> dropshipReportData ;

	/**
	 * @return the dropshipReportData
	 */
	public List<DropshipReportFM> getDropshipReportData() {
		return dropshipReportData;
	}

	/**
	 * @param dropshipReportData the dropshipReportData to set
	 */
	public void setDropshipReportData(List<DropshipReportFM> dropshipReportData) {
		this.dropshipReportData = dropshipReportData;
	}
	
}
