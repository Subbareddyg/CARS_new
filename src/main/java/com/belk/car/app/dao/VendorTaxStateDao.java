
package com.belk.car.app.dao;

import java.util.List;

import org.appfuse.dao.UniversalDao;

import com.belk.car.app.model.oma.DropshipTaxState;
import com.belk.car.app.model.oma.State;

public interface VendorTaxStateDao extends UniversalDao {

	List<DropshipTaxState> getAllVendorTaxStates();

	List<DropshipTaxState> getVendorTaxStatesSearch(
			String stateName, List<Object> values);

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
