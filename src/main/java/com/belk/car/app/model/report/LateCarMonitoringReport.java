package com.belk.car.app.model.report;

import java.util.List;

import com.belk.car.app.dto.DmmDTO;
import com.belk.car.app.dto.GmmDTO;

public class LateCarMonitoringReport extends BaseReport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7245731604920229385L;
	
	private List<LateCarMonitoringData> lateCarMonitoringData ;
	private List<GmmDTO> gmmDTOData;
	private List<DmmDTO> dmmDTOData;
	

	public List<LateCarMonitoringData> getLateCarMonitoringData() {
		return lateCarMonitoringData;
	}

	public void setLateCarMonitoringData(
			List<LateCarMonitoringData> lateCarMonitoringData) {
		this.lateCarMonitoringData = lateCarMonitoringData;
	}

	/**
	 * @return the gmmDTOData
	 */
	public List<GmmDTO> getGmmDTOData() {
		return gmmDTOData;
	}

	/**
	 * @param gmmDTOData the gmmDTOData to set
	 */
	public void setGmmDTOData(List<GmmDTO> gmmDTOData) {
		this.gmmDTOData = gmmDTOData;
	}

	/**
	 * @return the dmmDTOData
	 */
	public List<DmmDTO> getDmmDTOData() {
		return dmmDTOData;
	}

	/**
	 * @param dmmDTOData the dmmDTOData to set
	 */
	public void setDmmDTOData(List<DmmDTO> dmmDTOData) {
		this.dmmDTOData = dmmDTOData;
	}

	
}
