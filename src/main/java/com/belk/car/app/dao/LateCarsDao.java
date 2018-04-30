package com.belk.car.app.dao;

import java.util.List;

import org.appfuse.dao.UniversalDao;

import com.belk.car.app.dto.LateCarsSummaryDTO;
public interface LateCarsDao extends UniversalDao {

	/**
	 * Retrieve a list of Late Cars.
	 */

	public List<LateCarsSummaryDTO>  getAllLateCarsForBuyer();
	public List<LateCarsSummaryDTO> getAllLateCarsForOtherUser();


}
