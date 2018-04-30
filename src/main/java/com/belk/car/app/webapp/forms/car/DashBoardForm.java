/**
 * 
 */
package com.belk.car.app.webapp.forms.car;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.list.LazyList;

import com.belk.car.app.dto.CarsDTO;

/**
 * @author afusy01
 *
 */
public class DashBoardForm implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8911867212703057218L;
	
	private List<CarsDTO> styleDetails = LazyList.decorate(new ArrayList(), FactoryUtils.instantiateFactory(CarsDTO.class));
	private String userRole;

	/**
	 * @param styleDetails the styleDetails to set
	 */
	public void setStyleDetails(List<CarsDTO> styleDetails) {
		this.styleDetails = styleDetails;
	}

	/**
	 * @return the styleDetails
	 */
	public List<CarsDTO> getStyleDetails() {
		return styleDetails;
	}

	/**
	 * @param userRole the userRole to set
	 */
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	/**
	 * @return the userRole
	 */
	public String getUserRole() {
		return userRole;
	}
}
