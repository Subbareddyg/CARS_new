package com.belk.car.app.dao.hibernate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.belk.car.app.Constants;
import com.belk.car.app.dao.CarsPIMAttributeMappingDao;
import com.belk.car.app.model.CarsPIMAttributeMapping;

public class CarsPIMAttributeMappingDaoHibernate extends UniversalDaoHibernate implements CarsPIMAttributeMappingDao {

	/**
	 * Implementation to retrieve the list of Cars PIM mapping attributes.
	 */
	@SuppressWarnings("unchecked")
	public List<CarsPIMAttributeMapping> getCarsPIMMappingDetails(long classId, long deptId, long productTypeId) {
		List<CarsPIMAttributeMapping> cars = new ArrayList<CarsPIMAttributeMapping>();
		StringBuffer sb = new StringBuffer("FROM CarsPIMAttributeMapping T1 WHERE ");
		if (deptId > 0) {
			sb.append(" T1.id.deptId = :departmentId");
		}

		if (classId > 0) {
			sb.append(" AND T1.id.classId = :clasId");
		}

		if (productTypeId > 0) {
			sb.append(" AND T1.id.prodTypeId = :prodTypId");
		}
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			Query q = session.createQuery(sb.toString());
			q.setLong("departmentId", deptId);
			q.setLong("clasId", classId);
			q.setLong("prodTypId", productTypeId);
			if (log.isDebugEnabled()) {
				log.debug("Query inside getCarsPIMMappingDetails() method  " + q.getQueryString());
			}
			// executing the query
			cars = q.list();
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("Hibernate Exception "+e);
			}
		}

		if (cars == null) {
			cars = new ArrayList<CarsPIMAttributeMapping>();
		}

		if (log.isDebugEnabled()) {
			log.debug("return value of getCarsPIMMappingDetails() method  " + cars.size());
		}
		return cars;
	}

	/* 
	 *  This method is used to delete all mapping for all removed dept/class/product type for an attribute 
	 */
	@Override
	public String removeCARSPIMMappingAttribtues(long carAttrId, String classOrDeptOrPtId, String type) {
		if (log.isDebugEnabled()) {
			log.debug(" removeCARSPIMMappingAttribtues  attr : " +carAttrId +type+":"+classOrDeptOrPtId);
		}
		String success="";
		SessionFactory sf=null;
		Session session =null;
		try {
			
		
		 sf 	= getHibernateTemplate().getSessionFactory();
		 session = sf.openSession();
		Transaction tx= session.beginTransaction();
		 tx.begin();
		if (classOrDeptOrPtId != null  && type.equals(Constants.REMOVE_ATTR_DEPT)) {
		
			String sqlString = "DELETE FROM CarsPIMAttributeMapping T1 WHERE T1.id.deptId=:deptId AND T1.id.attrId= :attrId";
			  Query query = session.createQuery(sqlString);
			  query.setLong("deptId", new Long(classOrDeptOrPtId).longValue());
			  query.setLong("attrId", carAttrId);
			  query.executeUpdate();
			  
		}

		else if (classOrDeptOrPtId != null && type.equals(Constants.REMOVE_ATTR_CLASS)) {
			String sqlString = "DELETE FROM CarsPIMAttributeMapping T1 WHERE T1.id.classId=:classId AND T1.id.attrId= :attrId";
			  Query query = session.createQuery(sqlString);
			  query.setLong("classId", new Long(classOrDeptOrPtId).longValue());
			  query.setLong("attrId", carAttrId);
			  query.executeUpdate();
		}

		else if (classOrDeptOrPtId != null && type.equals(Constants.REMOVE_ATTR_PRODCUT_TYPE)) {
			String sqlString = "DELETE FROM CarsPIMAttributeMapping T1 WHERE T1.id.prodTypeId=:prodTypeId AND T1.id.attrId= :attrId";
			  Query query = session.createQuery(sqlString);
			  query.setLong("prodTypeId", new Long(classOrDeptOrPtId).longValue());
			  query.setLong("attrId", carAttrId);
			  query.executeUpdate();
		}
		else if(String.valueOf(carAttrId) != null && type ==null) {
			String sqlString = "DELETE FROM CarsPIMAttributeMapping T1 WHERE  T1.id.attrId= :attrId";
			  Query query = session.createQuery(sqlString);
			  query.setLong("attrId", carAttrId);
			  query.executeUpdate();
		}
		session.beginTransaction().commit();
		} catch (Throwable e) {
			log.error(" Failed to remove attribtue attr : " +carAttrId +type+":"+classOrDeptOrPtId,e);

		} finally {
			if(session !=null) {
				try {
					session.close();
				} catch (Exception e2) {
					if(log.isErrorEnabled()) {
					log.error(" Failed to remove attribtue attr : " +carAttrId +type+":"+classOrDeptOrPtId,e2);
					}
				}
			}
		}
		return success;
	}

	@Override
	public String addCarsPimMappingAttribtues(long carAttrId, List<String> classOrDeptOrPtId, String type,
			String attributeType) {
		String success="";
		
		StringBuffer sb = new StringBuffer("FROM CarsPIMAttributeMapping T1 WHERE ");
		if (classOrDeptOrPtId != null && !classOrDeptOrPtId.isEmpty()
				&& attributeType.equalsIgnoreCase(Constants.DEPARTMENT_LIST)) {
			sb.append(" T1.id.deptId = :deptId");
			sb.append(" AND T1.id.attrId = :attrId");
			success=saveorUpdateAttribtues("deptId", sb.toString(), carAttrId, classOrDeptOrPtId);
		}

		if (classOrDeptOrPtId != null && !classOrDeptOrPtId.isEmpty()
				&& attributeType.equalsIgnoreCase(Constants.CLASSIFICATION_LIST)) {
			sb.append(" T1.id.classId = :classId");
			sb.append(" AND T1.id.attrId = :attrId");
			success=saveorUpdateAttribtues("classId", sb.toString(), carAttrId, classOrDeptOrPtId);
		}

		if (classOrDeptOrPtId != null && !classOrDeptOrPtId.isEmpty()
				&& attributeType.equalsIgnoreCase(Constants.PRODUCT_TYPE_LIST)) {
			sb.append(" T1.id.prodTypeId = :prodTypId");
			sb.append(" AND T1.id.attrId = :attrId");
			success=saveorUpdateAttribtues("prodTypId", sb.toString(), carAttrId, classOrDeptOrPtId);
		}

		return success;
	}

	/**
	 * This method is used to create mapping for newly added dept/class/product type for an attribute 
	 * @param classordeptorpt
	 * @param query
	 * @param attrId
	 * @param classOrDeptOrPtIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String  saveorUpdateAttribtues(String classordeptorpt, String query, long attrId,
			List<String> classOrDeptOrPtIds) {
		String success="SUCCESS";
		SessionFactory sf=null;
		Session session=null;
		try {
			sf = getHibernateTemplate().getSessionFactory();
			session = sf.openSession();
			 session.beginTransaction().begin();

			for (String clsordeptorptId : classOrDeptOrPtIds) {
				List<CarsPIMAttributeMapping> cars = Collections.EMPTY_LIST;

				Query q = session.createQuery(query);
				q.setLong(classordeptorpt, new Long(clsordeptorptId).longValue());
				q.setLong("attrId",attrId);
				// executing the query
				cars = q.list();
				if (cars.isEmpty()) {
					// Add newly added attribtue in pim mapping table.
					if (classordeptorpt.equalsIgnoreCase("deptId")) {

						// Call procedure to insert the data
						Query storedata = session.createSQLQuery("CALL ADD_DEPT_CLASS_PT_MAPPING(:DEPT_ID, :ATTR_ID,:ATTR_TYPE_LEVEL)")
								.setParameter("DEPT_ID", new Long(clsordeptorptId).longValue())
								.setParameter("ATTR_ID", attrId)
								.setParameter("ATTR_TYPE_LEVEL", "ADD_DEPT");
						storedata.executeUpdate();
					
					} else if (classordeptorpt.equalsIgnoreCase("classId")) {
						// Call procedure to insert the data
						Query storedata = session.createSQLQuery("CALL ADD_DEPT_CLASS_PT_MAPPING(:CLASS_ID, :ATTR_ID,:ATTR_TYPE_LEVEL)")
								.setParameter("CLASS_ID", new Long(clsordeptorptId).longValue())
								.setParameter("ATTR_ID", attrId)
								.setParameter("ATTR_TYPE_LEVEL", "ADD_CLASS");
						storedata.executeUpdate();

					} else if (classordeptorpt.equalsIgnoreCase("prodTypId")) {

						// Call procedure to insert the data
						Query storedata = session.createSQLQuery("CALL ADD_DEPT_CLASS_PT_MAPPING(:PT_ID, :ATTR_ID,:ATTR_TYPE_LEVEL)")
								.setParameter("PT_ID", new Long(clsordeptorptId).longValue())
								.setParameter("ATTR_ID", attrId)
								.setParameter("ATTR_TYPE_LEVEL", "ADD_PRODUCT_TYPE");
						storedata.executeUpdate();
					}

				} else {
					cars.clear();
				}

				if (log.isDebugEnabled()) {
					log.debug("Query inside getCarsPIMMappingDetails() method  " + q.getQueryString());
				}

			}
			session.beginTransaction().commit();

		} catch (Throwable e) {
		
				log.error("Error occured while adding the class/dept/pt attribtue mapping  into cars_pim mapping",e);
			
			success="FAILED";
		}
		finally {
			if(session !=null) {
				try {
					session.close();
				} catch (Exception e2) {
					if(log.isErrorEnabled()) {
					log.error("Error occured while adding the class/dept/pt attribtue mapping  into cars_pim mapping",e2);
					}
				}
			}
		}
		return success;

	}

}
