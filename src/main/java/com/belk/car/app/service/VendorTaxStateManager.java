
package com.belk.car.app.service;

import java.util.List;

import org.appfuse.service.UniversalManager;

import com.belk.car.app.model.oma.State;
import com.belk.car.app.model.oma.DropshipTaxState;

public interface VendorTaxStateManager extends UniversalManager {

	// Returns all tax states
	List<DropshipTaxState> getAllVendorTaxStates();

	List<DropshipTaxState> getVendorTaxStatesSearch(
			String stateName, String status);

	void removeVendorTaxState(long vendorTaxId);

	State getStateByStateId(String parameter);

	DropshipTaxState saveTaxStates(DropshipTaxState vendorTaxStateModel);

	DropshipTaxState getVendorTaxStateByID(long taxid);

	/**
	 * @param taxState
	 * @return void
	 * @Enclosing_Method  deleteTaxState
	 * @Date Mar 10, 2010 
	 * @User afusy07 - Priyanka Gadia
	 * @TODO
	 */
	void deleteTaxState(DropshipTaxState taxState);

}
