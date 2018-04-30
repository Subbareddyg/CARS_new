
package com.belk.car.app.dao.hibernate;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.hibernate.UniversalDaoHibernate;

import com.belk.car.app.dao.VendorTaxStateDao;
import com.belk.car.app.model.oma.DropshipTaxState;
import com.belk.car.app.model.oma.State;

/**
 * @author afusy07 priyanka_gadia@syntelinc.com
 * @Date 16-Dec-09
 */
public class VendorTaxStateDaoHibernate extends UniversalDaoHibernate
implements
VendorTaxStateDao {

	private transient final Log log = LogFactory
	.getLog(VendorTaxStateDaoHibernate.class);

	public void time() {
		//Commenting the following code, because if the time is to be calculated for execution, following comments can be removed.
		/*SessionFactory sessionFactory = getSessionFactory();
		Statistics stats = sessionFactory.getStatistics();
		log
				.debug("inside time----------------------------------------------------------------");

		stats.setStatisticsEnabled(true);
		String[] s = stats.getQueries();
		log.debug("s.length=" + s.length);
		int i = 0;
		for (i = 0; i < s.length; i++) {
			QueryStatistics queryStats = stats.getQueryStatistics(s[i]);
			log.debug("s" + i + "=" + s[i]);
			long time = queryStats.getExecutionMaxTime();
			log.debug("time for| " + s[i] + "|" + time);
			i++;
		}*/
	}


	/**
	 * @return
	 * @see com.belk.car.app.dao.VendorTaxStateDao#getAllVendorTaxStates()
	 * @Enclosing_Method  getAllVendorTaxStates
	 *  
	 * 
	 * @TODO Fetch all records for vendor states
	 */
	@SuppressWarnings("unchecked")
	public List<DropshipTaxState> getAllVendorTaxStates() {
		long start = Calendar.getInstance().getTimeInMillis();
		getHibernateTemplate().find("from DropshipTaxState");
		//time();
		long end = Calendar.getInstance().getTimeInMillis();
		long timeex = end - start;
		log.debug("time for|find DropshipTaxState|" + timeex);
		return getHibernateTemplate().find("from DropshipTaxState");
	}


	/**
	 * @param vendorTaxId
	 * @see com.belk.car.app.dao.VendorTaxStateDao#removeVendorTaxState(long)
	 * @Enclosing_Method  removeVendorTaxState
	 *  
	 * 
	 * @TODO Remove Vendor Tax state
	 */
	public void removeVendorTaxState(long vendorTaxId) {
		long start = Calendar.getInstance().getTimeInMillis();
		DropshipTaxState taxStateModel = (DropshipTaxState) getHibernateTemplate()
		.get(DropshipTaxState.class, vendorTaxId);
		long end = Calendar.getInstance().getTimeInMillis();
		long timeex = end - start;
		log.debug("time for|get(DropshipTaxState.class, vendorTaxId)|"
				+ timeex);
		if(taxStateModel!=null){
			taxStateModel.setStatus("INACTIVE");
			start = Calendar.getInstance().getTimeInMillis();
			getHibernateTemplate().saveOrUpdate(taxStateModel);
            getHibernateTemplate().flush();
		}
	}


	/**
	 * @param parameter
	 * @return
	 * @see com.belk.car.app.dao.VendorTaxStateDao#getStateByStateId(java.lang.String)
	 * @Enclosing_Method  getStateByStateId
	 *  
	 * 
	 * @TODO Get state model By passing state id
	 */
	public State getStateByStateId(String parameter) {
		long stateId = Long.parseLong(parameter);
		return (State) getHibernateTemplate().get(State.class, stateId);
	}


	/**
	 * @param vendorTaxStateModel
	 * @return
	 * @see com.belk.car.app.dao.VendorTaxStateDao#saveTaxStates(com.belk.car.app.model.oma.DropshipTaxState)
	 * @Enclosing_Method  saveTaxStates
	 *  
	 * 
	 * @TODO Save tax states
	 */
	public DropshipTaxState saveTaxStates(
			DropshipTaxState vendorTaxStateModel) {
		long start = Calendar.getInstance().getTimeInMillis();
		getHibernateTemplate().saveOrUpdate(vendorTaxStateModel);
        getHibernateTemplate().flush();
		long end = Calendar.getInstance().getTimeInMillis();
		long timeex = end - start;
		log.debug("time for|saveOrUpdate(vendorTaxStateModel)|" + timeex);

		log.debug("vendorTaxStateModel=" + " "
				+ vendorTaxStateModel.getTaxIssue() + " "
				+ vendorTaxStateModel.getVendorTaxStateId());
		return vendorTaxStateModel;

	}


	/**
	 * @param taxid
	 * @return
	 * @see com.belk.car.app.dao.VendorTaxStateDao#getVendorTaxStateByID(long)
	 * @Enclosing_Method  getVendorTaxStateByID
	 *  
	 * 
	 * @TODO  Get VendorTaxState model By passing VendorTaxState id
	 */
	public DropshipTaxState getVendorTaxStateByID(long taxid) {
		long start = Calendar.getInstance().getTimeInMillis();
		DropshipTaxState taxStateModel = (DropshipTaxState) getHibernateTemplate()
		.get(DropshipTaxState.class, taxid);
		long end = Calendar.getInstance().getTimeInMillis();
		long timeex = end - start;
		log.debug("time for|get(DropshipTaxState.class, taxid)|" + timeex);
		return taxStateModel;
	}


	/**
	 * @param query
	 * @param values
	 * @return
	 * @see com.belk.car.app.dao.VendorTaxStateDao#getVendorTaxStatesSearch(java.lang.String, java.util.List)
	 * @Enclosing_Method  getVendorTaxStatesSearch
	 *  
	 * 
	 * @TODO Get Vendor TAx States based on search criteria
	 */
	@SuppressWarnings("unchecked")
	public List<DropshipTaxState> getVendorTaxStatesSearch(
			String query, List<Object> values) {
		return getHibernateTemplate().find(query, values.toArray());

	}


	/**
	 * @param taxState
	 * @see com.belk.car.app.dao.VendorTaxStateDao#deleteTaxState(com.belk.car.app.model.oma.DropshipTaxState)
	 * @Enclosing_Method  deleteTaxState
	 * @Date Mar 10, 2010 
	 * @User afusy07 - Priyanka Gadia
	 * @TODO
	 */
	public void deleteTaxState(DropshipTaxState taxState) {
		getHibernateTemplate().delete(taxState);

	}

}
