
package com.belk.car.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.appfuse.service.impl.UniversalManagerImpl;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

import com.belk.car.app.dao.VendorTaxStateDao;
import com.belk.car.app.model.oma.DropshipTaxState;
import com.belk.car.app.model.oma.State;
import com.belk.car.app.service.VendorTaxStateManager;

/**
 * @author afusy07 Priyanka Gadia Feb 9, 2010TODO
 */
public class VendorTaxStateManagerImpl extends UniversalManagerImpl
		implements
			VendorTaxStateManager {

	private VendorTaxStateDao vendorTaxStateDao;

	public void setVendorTaxStateDao(VendorTaxStateDao vendorTaxStateDao) {
		this.vendorTaxStateDao = vendorTaxStateDao;
	}

	public VendorTaxStateDao getVendorTaxStateDao() {
		return vendorTaxStateDao;
	}

	/**
	 * @return
	 * @see com.belk.car.app.service.VendorTaxStateManager#getAllVendorTaxStates()
	 * @Enclosing_Method getAllVendorTaxStates
	 * @TODO
	 */
	public List<DropshipTaxState> getAllVendorTaxStates() {
		return vendorTaxStateDao.getAllVendorTaxStates();

	}

	/**
	 * @param stateName
	 * @param status
	 * @return
	 * @see com.belk.car.app.service.VendorTaxStateManager#getVendorTaxStatesSearch(java.lang.String,
	 *      java.lang.String)
	 * @Enclosing_Method getVendorTaxStatesSearch
	 * @TODO
	 */
	public List<DropshipTaxState> getVendorTaxStatesSearch(String stateName, String status) {

		String likeFormat = "%%%1$s%%";
		ArrayList<String> query = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();

		StringBuffer sqlB = new StringBuffer("from DropshipTaxState vtax ");

		if (!StringUtils.isBlank(stateName)) {

			query.add("upper(vtax.state.stateName) LIKE ?");
			values.add(String.format(likeFormat, stateName.toUpperCase(Locale.US)).toString());
		}
		if (!StringUtils.isBlank(status) && !status.toUpperCase(Locale.US).equals("ALL")) {
			query.add("upper(vtax.status) LIKE ?");
			log.debug("status=" + status);
			log.debug("String.format(likeFormat,status.toUpperCase(Locale.US)).toString()"
					+ String.format(likeFormat, status.toUpperCase(Locale.US)).toString());
			values.add(status.toUpperCase(Locale.US));
		}

		if (!query.isEmpty()) {
			sqlB.append(" WHERE ");
			int i = 0;
			for (String s : query) {
				if (i > 0) {
					sqlB.append(" AND ");
				}
				sqlB.append(s);
				i++;
			}
		}

		sqlB.append(" order by vtax.state.stateName");
		return vendorTaxStateDao.getVendorTaxStatesSearch(sqlB.toString(), values);
	}

	/**
	 * @param vendorTaxId
	 * @see com.belk.car.app.service.VendorTaxStateManager#removeVendorTaxState(long)
	 * @Enclosing_Method removeVendorTaxState
	 * @TODO
	 */
	public void removeVendorTaxState(long vendorTaxId) {
		vendorTaxStateDao.removeVendorTaxState(vendorTaxId);

	}

	/**
	 * @param parameter
	 * @return
	 * @see com.belk.car.app.service.VendorTaxStateManager#getStateByStateId(java.lang.String)
	 * @Enclosing_Method getStateByStateId
	 * @TODO
	 */
	public State getStateByStateId(String parameter) {

		return vendorTaxStateDao.getStateByStateId(parameter);
	}

	/**
	 * @param vendorTaxStateModel
	 * @return
	 * @see com.belk.car.app.service.VendorTaxStateManager#saveTaxStates(com.belk.car.app.model.oma.DropshipTaxState)
	 * @Enclosing_Method saveTaxStates
	 * @TODO
	 */
	public DropshipTaxState saveTaxStates(DropshipTaxState vendorTaxStateModel) {
		return vendorTaxStateDao.saveTaxStates(vendorTaxStateModel);

	}

	/**
	 * @param taxid
	 * @return
	 * @see com.belk.car.app.service.VendorTaxStateManager#getVendorTaxStateByID(long)
	 * @Enclosing_Method getVendorTaxStateByID
	 * @TODO
	 */
	public DropshipTaxState getVendorTaxStateByID(long taxid) {
		return vendorTaxStateDao.getVendorTaxStateByID(taxid);
	}

	/**
	 * @param taxState
	 * @see com.belk.car.app.service.VendorTaxStateManager#deleteTaxState(com.belk.car.app.model.oma.DropshipTaxState)
	 * @Enclosing_Method  deleteTaxState
	 * @Date Mar 10, 2010 
	 * @User afusy07 - Priyanka Gadia
	 * @TODO
	 */
	public void deleteTaxState(DropshipTaxState taxState) {
		
		vendorTaxStateDao.deleteTaxState(taxState);
	}

}
