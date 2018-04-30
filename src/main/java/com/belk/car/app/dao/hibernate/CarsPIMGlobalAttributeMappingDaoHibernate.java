package com.belk.car.app.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.belk.car.app.dao.CarsPIMGlobalAttributeMappingDao;
import com.belk.car.app.model.CarsPIMGlobalAttributeMapping;

public class CarsPIMGlobalAttributeMappingDaoHibernate extends UniversalDaoHibernate implements CarsPIMGlobalAttributeMappingDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getPIMGlobalAttrMappingById(long pimAttrId) {
		List<Object[]> gblMappingDetails=null;

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		Criteria criteria;
		ProjectionList columns = Projections.projectionList();
		try {
			criteria = session.createCriteria(CarsPIMGlobalAttributeMapping.class, "gblAttrMapping");
			criteria.add(Restrictions.eq("gblAttrMapping.pimAttrId", pimAttrId));
			columns.add(Projections.property("gblAttrMapping.carTableNm"));
			columns.add(Projections.property("gblAttrMapping.columnNm"));
			columns.add(Projections.property("gblAttrMapping.carAttrId"));
			criteria.setProjection(columns);

			gblMappingDetails = criteria.list();
			
		} catch (Exception e) {
			if(log.isErrorEnabled()){
			    log.error("Hibernate Exception while retrieving PIMGlobalAttrMapping "+e);
			}

		}
		
		if(gblMappingDetails==null){
			gblMappingDetails = new ArrayList<Object[]>();
		}

		if(log.isDebugEnabled()){
			log.debug("return value of getPIMGlobalAttrMappingById() method  "+gblMappingDetails.size());
		}
		return gblMappingDetails;
	}
	
	/**
	 * Method to retrieve all the global attributes.
	 * 
	 * @return
	 */
	public List<CarsPIMGlobalAttributeMapping> getAllCarsPimGlobalAttributes(){
	    return getHibernateTemplate().find("from CarsPIMGlobalAttributeMapping");
	}
	
}
