package com.belk.car.app.dao.hibernate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.belk.car.app.Constants;
import com.belk.car.app.dao.SizeConversionDao;
import com.belk.car.app.model.SizeConversionMaster;
import com.belk.car.app.model.SizeSynchDataHolderView;
import com.belk.car.app.model.VendorSku;

/**
 * A DAO impl class using hibernate template to carry out Size Conversion rule
 * management database operations.
 * 
 * @author Yogesh.Vedak
 */
public class SizeConversionDaoHibernate extends UniversalDaoHibernate implements
		SizeConversionDao {
	private final String QUERY_FROM_CLAUSE = "from SizeConversionMaster scm where scm.statusCode='"
			+ Constants.SIZECONV_STATUS_ACTIVE + "'";
	private final String QUR_DELETED_RECORDS = "from SizeConversionMaster scm where scm.statusCode='"
			+ Constants.SIZECONV_STATUS_DELETED + "'";
	private final String QUERY_SEL_CLAUSE = "SELECT c1.* FROM size_conversion_master c1  WHERE 0=0 and c1.status_cd='"
			+ Constants.SIZECONV_STATUS_ACTIVE + "'";
	private final String NONDELETED_SIZE_HQL = "from SizeConversionMaster scm where scm.statusCode in ('"
			+ Constants.SIZECONV_STATUS_ACTIVE
			+ "' ,'"
			+ Constants.SIZECONV_STATUS_PENDING
			+ "') ORDER BY scm.statusCode DESC,scm.createdDate DESC";

	/**
	 * This method returns all active and pending size conversion rules
	 * available
	 * 
	 * @return List of {@link SizeConversionMaster}
	 * @author Yogesh.Vedak
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SizeConversionMaster> getAllSizeConversionMapping() {
		List<SizeConversionMaster> sizeConversionList = new ArrayList<SizeConversionMaster>();
		StringBuffer query = new StringBuffer(NONDELETED_SIZE_HQL); // QUERY_FROM_CLAUSE//
																	// to
																	// include
																	// Pending
																	// records
																	// this
																	// NONDELETED_SCOLORS_HQL
																	// query can
																	// be used
																	// to
																	// display
																	// size
																	// rules on
																	// List
																	// pending
																	// first.
																	// This
																	// method is
																	// not used
																	// anywhere
																	// except
																	// the
																	// places to
																	// show list
																	// in jsp
		sizeConversionList = getHibernateTemplate().find(query.toString());
		return sizeConversionList;
	}

	/**
	 * This methods returns active size conversion rules based on size
	 * Name,departmentNumber and classNumber,vendorNumber,facetSize,facetSubSize
	 * provided
	 * 
	 * @return List of {@link SizeConversionMaster}
	 * @author Yogesh.Vedak
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SizeConversionMaster> searchSizeConversions(String sizeName,
			String conversionName, String departmentNumber, String classNumber,
			String vendorNumber, String facetSize, String facetSubSize) {
		final StringBuilder query = new StringBuilder(
				"SELECT c1.* FROM size_conversion_master c1  WHERE 0=0 and c1.status_cd in ('");
				query.append(Constants.SIZECONV_STATUS_ACTIVE).append("' ,'");
				query.append(Constants.SIZECONV_STATUS_PENDING).append("')");
		if (StringUtils.isNotBlank(sizeName)) {
			sizeName=sizeName.replaceAll("'", "''");
			query.append(" and ");
			query.append("lower(scm_size_name) like lower('%" + sizeName
					+ "%')");
		}
		if (StringUtils.isNotBlank(conversionName)) {
			conversionName=conversionName.replaceAll("'", "''");
			query.append(" and ");
			query.append("lower(scm_conversion_size_name) like lower('%"
					+ conversionName + "%')");
		}
		if (StringUtils.isNotBlank(departmentNumber)) {
			query.append(" and ");
			query.append("c1.scm_dept_id =" + departmentNumber);
		}
		if (StringUtils.isNotBlank(classNumber)) {
			query.append(" and ");
			query.append("c1.scm_class_id =" + classNumber);
		}
		if (StringUtils.isNotBlank(vendorNumber)) {
			query.append(" and ");
			query.append("c1.scm_vendor_id =" + vendorNumber);
		}
		List<SizeConversionMaster> serachSizeConvList = null;
		if (log.isDebugEnabled()) {
			log.debug("Search SizeConversions Query:" + query.toString());
		}
		if (StringUtils.isNotBlank(facetSize)) {
			query.append(" and ");
			query.append("(lower(facet_size_1) like lower('%" + facetSize
					+ "%') or lower(facet_size_2) like lower('%" + facetSize
					+ "%') or lower(facet_size_3) like lower('%" + facetSize + "%'))");
		}
		if (StringUtils.isNotBlank(facetSubSize)) {
			query.append(" and ");
			query.append("(lower(facet_sub_size_1) like lower('%" + facetSubSize
					+ "%') or lower(facet_sub_size_2) like lower('%" + facetSubSize
					+ "%'))");
		}

		if (log.isDebugEnabled()) {
			log.debug("Search SizeConversions Final Query:" + query.toString());
		}

		serachSizeConvList = getSizeConversionsByQuery(query.toString());
		return serachSizeConvList;
	}

	@Override
	public void createSizeConversion(SizeConversionMaster sizeConversionMaster) {
		getHibernateTemplate().save(sizeConversionMaster);
		getHibernateTemplate().flush();
	}

	@Override
	public void updateSizeConversion(SizeConversionMaster sizeConversionMaster) {
		getHibernateTemplate().saveOrUpdate(sizeConversionMaster);
		getHibernateTemplate().flush();
		getHibernateTemplate().clear();
	}

	@Override
	public void updateSizeConversionStatus(long scmId, String statusValue) {
		SizeConversionMaster scmaster = findSizeConversionByScmId(scmId);
		if (scmaster != null) {
			scmaster.setStatusCode(statusValue);
		}
		updateSizeConversion(scmaster);
	}

	@Override
	public void updateSizeConversionListStatus(List<String> scmIdList,
			String statusValue) {
		try {
			Session session = getHibernateTemplate().getSessionFactory()
					.openSession();
			@SuppressWarnings("deprecation")
			Connection con = session.connection();
			boolean initialAutocommitSetting = con.getAutoCommit();
			con.setAutoCommit(false);
			String sqlQuery = "update size_conversion_master set status_cd = ?,updated_by = ?,updated_date = sysdate where scm_id = ? ";
			PreparedStatement stmt = con.prepareStatement(sqlQuery);
			for (String scmIdRow : scmIdList) {
				stmt.setString(1, statusValue);
				stmt.setString(2, Constants.SCHEDULER_UPDATEDBY);
				stmt.setLong(3, new Long(scmIdRow).longValue());
				stmt.addBatch();
			}
			int[] updatesCount = stmt.executeBatch();
			con.commit();
			con.setAutoCommit(initialAutocommitSetting);
		} catch (SQLException ex) {
			log.error("SQLException: error while updating the 'Delete' status for the size conversion list "
					+ scmIdList.toString());

		}
		// return updatesCount; //
	}

	@Override
	public void updateRuleChangedFlag(long scmId, String flagValue) {
		SizeConversionMaster scmaster = findSizeConversionByScmId(scmId);
		if (scmaster != null) {
			scmaster.setRuleChanged(flagValue);
		}
		updateSizeConversion(scmaster);
	}

	/**
	 * This method returns Size conversion rule Object by ID
	 * 
	 * @param scmId
	 * @return {@link SizeConversionMaster}
	 */
	@Override
	public SizeConversionMaster findSizeConversionByScmId(long scmId) {
		return (SizeConversionMaster) getHibernateTemplate().get(
				SizeConversionMaster.class, scmId);
	}

	/**
	 * This method accepts sql query and returns the List of Size Conversion
	 * Rules
	 * 
	 * @param query
	 * @return List of {@link SizeConversionMaster}
	 * 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<SizeConversionMaster> getSizeConversionsByQuery(
			final String query) {
		List<SizeConversionMaster> serachedSizeConvList = null;
		serachedSizeConvList = (List<SizeConversionMaster>) getHibernateTemplate()
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						SQLQuery sq = session.createSQLQuery(query).addEntity(
								SizeConversionMaster.class);
						if(log.isInfoEnabled()){
						log.info("------SizeConversionDaoHibernate.getSizeConversionsByQuery--------> "+sq.getQueryString());
						}
						return sq.list();
					}
				});

		return serachedSizeConvList;

	}

	/**
	 * This method completely removes from the system the size conversion rule
	 * object provided.
	 * 
	 * @author Yogesh.Vedak
	 */
	@Override
	public void removeSizeConversionFromSystem(
			SizeConversionMaster sizeConversionMaster) {
		getHibernateTemplate().delete(sizeConversionMaster);

	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public SizeConversionMaster findSizeConversionBySizeName(String sizeName) {
		SizeConversionMaster scmaster = null;
		List<SizeConversionMaster> scmasterList = findSizeConversionByCriteria(
				"sizeName", sizeName);
		if (scmasterList != null && !scmasterList.isEmpty()) {
			scmaster = (SizeConversionMaster) scmasterList.get(0);
		}
		return scmaster;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SizeConversionMaster findSizeConversionByConversionSizeName(
			String conversionSizeName) {
		SizeConversionMaster scmaster = null;
		List<SizeConversionMaster> scmasterList = findSizeConversionByCriteria(
				"coversionSizeName", conversionSizeName);
		if (scmasterList != null && !scmasterList.isEmpty()) {
			scmaster = (SizeConversionMaster) scmasterList.get(0);
		}
		return scmaster;
	}

	// EC:1602 CARS Size Name Issue
	@SuppressWarnings("unchecked")
	@Override
	public SizeConversionMaster findConversionName(String sizeName,
			String conversionSizeName, String vendorNumber, String deptCode,
			String strClassNum) {
		SizeConversionMaster scmaster = null;
		List<SizeConversionMaster> scmasterList = searchConversionName(
				sizeName, conversionSizeName, vendorNumber, deptCode,
				strClassNum);
		if (scmasterList != null && !scmasterList.isEmpty()) {
			scmaster = (SizeConversionMaster) scmasterList.get(0);
		}
		return scmaster;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SizeConversionMaster> findSizeConversionByCriteria(
			String criteriaPropertyName, String criteriaValue) {
		Object[] arrCriteria = { criteriaValue };
		StringBuffer query = new StringBuffer(QUERY_FROM_CLAUSE);
		query.append(" and ");
		query.append("scm." + criteriaPropertyName + "= ? ");
		List<SizeConversionMaster> scmasterList = (List<SizeConversionMaster>) getHibernateTemplate()
				.find(query.toString(), arrCriteria);
		return scmasterList;
	}

	// EC:1602 CARS Size Name Issue
	@SuppressWarnings("unchecked")
	@Override
	public List<SizeConversionMaster> searchConversionName(String sizeName,
			String criteriaValue, String vendorCriteriaValue,
			String deptCriteriaValue, String classCriteriaValue) {
		List<SizeConversionMaster> conversionList = new ArrayList<SizeConversionMaster>();
		StringBuffer cQuery = new StringBuffer();
		cQuery.append("SELECT * from size_conversion_master scm where scm.STATUS_CD='A'");

		if (sizeName != null && !sizeName.isEmpty() && !sizeName.equals("")) {
			sizeName=sizeName.replaceAll("'", "''");
			cQuery.append(" AND scm.SCM_SIZE_NAME = '" + sizeName + "'");
		} else {
			cQuery.append(" AND scm.SCM_SIZE_NAME IS NULL");
		}
		if (criteriaValue != null && !criteriaValue.isEmpty()
				&& !criteriaValue.equals("")) {
			criteriaValue=criteriaValue.replaceAll("'", "''");
			cQuery.append(" AND scm.SCM_CONVERSION_SIZE_NAME = '"
					+ criteriaValue + "'");
		} else {
			cQuery.append(" AND scm.SCM_CONVERSION_SIZE_NAME IS NULL");
		}
		if (vendorCriteriaValue != null && !vendorCriteriaValue.isEmpty()
				&& !vendorCriteriaValue.equals("")) {
			cQuery.append(" AND scm.SCM_VENDOR_ID ='" + vendorCriteriaValue
					+ "'");
		} else {
			cQuery.append(" AND scm.SCM_VENDOR_ID IS NULL");
		}
		if (deptCriteriaValue != null && !deptCriteriaValue.isEmpty()
				&& !deptCriteriaValue.equals("")) {
			cQuery.append(" AND scm.SCM_DEPT_ID ='" + deptCriteriaValue + "'");
		} else {
			cQuery.append(" AND scm.SCM_DEPT_ID IS NULL");
		}
		if (classCriteriaValue != null && !classCriteriaValue.isEmpty()
				&& !classCriteriaValue.equals("")) {
			cQuery.append(" AND scm.SCM_CLASS_ID='" + classCriteriaValue + "'");
		} else {
			cQuery.append(" AND scm.SCM_CLASS_ID IS NULL");
		}

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sqlQuery = (SQLQuery) session.createSQLQuery(
					cQuery.toString()).addEntity(SizeConversionMaster.class);
			conversionList = sqlQuery.list();
		} catch (Exception e) {
			log.error("Hibernate Exception caught in CarCmpStatusView: "
					+ e.getMessage());
		}
		return conversionList;
	}

	/**
	 * This method returns those Size Conversion rule whose Rule_changed=T. ie
	 * the ones which have been changed or added currently.
	 * 
	 * @return List of {@link SizeConversionMaster}
	 * @author Yogesh.Vedak
	 * 
	 */
	@Override
	public List<SizeConversionMaster> getChangedSizeConversions() {
		return findSizeConversionByCriteria("ruleChanged",
				Constants.RULECHANGED_TRUE);
	}

	/**
	 * Returns the Size conversion rule based on size name, classId deptId, and
	 * vendorId provided in arguments. The returned size conversion rule would
	 * be the unique rule object that is present in the system for values
	 * provided in arguments.
	 * 
	 * @return {@link SizeConversionMaster}
	 * @author Yogesh.Vedak
	 * 
	 */
	@Override
	public SizeConversionMaster getSizeConversion(String sizeName,
			String classId, String deptId, String vendorId) {
		SizeConversionMaster sizeConversionRule = null;
		if(log.isInfoEnabled()){
		log.info("------Inside getSizeConversion() start------");
		}

		StringBuffer query = new StringBuffer(
				"SELECT c1.* FROM size_conversion_master c1  WHERE 0=0 and c1.status_cd in "
						+ "   ('" + Constants.SIZECONV_STATUS_ACTIVE + "' ,'"
						+ Constants.SIZECONV_STATUS_PENDING + "')");

		// size name
		query.append(" and ");
		if (sizeName != null && !sizeName.isEmpty() && !sizeName.equals("")) {
			sizeName=sizeName.replaceAll("'", "''");
		}
		query.append("c1.scm_size_name = '" + sizeName + "'");
		//--vimal--
		// query with classId
		if (StringUtils.isNotBlank(classId)) {
			query.append(" and ");
			query.append("c1.scm_class_id =" + classId);
		} else {
			query.append(" and ");
			query.append("c1.scm_class_id is null ");
		}
		// query with deptId
		if (StringUtils.isNotBlank(deptId)) {
			query.append(" and ");
			query.append("c1.scm_dept_id =" + deptId);
		} else {
			query.append(" and ");
			query.append("c1.scm_dept_id is null ");
		}
		// query with vendor
		if (StringUtils.isNotBlank(vendorId)) {
			query.append(" and ");
			query.append("c1.scm_vendor_id =" + vendorId);
		} else {
			query.append(" and ");
			query.append("c1.scm_vendor_id is null ");
		}
		if(log.isInfoEnabled()){
		log.info("------Inside getSizeConversion() query formation------"+query.toString());
		}
		List<SizeConversionMaster> sizeConversions = getSizeConversionsByQuery(query
				.toString());
		if ((sizeConversions != null) && !sizeConversions.isEmpty()) {
			sizeConversionRule = sizeConversions.get(0);
		}
		
		if(log.isInfoEnabled()){
		log.debug("------Inside getSizeConversion() end------");
		}

		return sizeConversionRule;
	}

	/**
	 * This method returns the specified list of VIEW objects which holds the
	 * data to be applied on sku ,for updating vendor sku's size name and its
	 * facets value present in it.
	 * 
	 * @return List of {@link SizeSynchDataHolderView}
	 * @author Yogesh.Vedak
	 */
	// @BatchSize(size=20)
	@SuppressWarnings("unchecked")
	@Override
	public List<SizeSynchDataHolderView> getVendorSkuSizeUpdationData(
			final int offset, final int batchSize) {
		List<SizeSynchDataHolderView> serachedSizeConvList = new ArrayList<SizeSynchDataHolderView>();
		StatelessSession session = getHibernateTemplate().getSessionFactory()
				.openStatelessSession();
		Transaction tx = session.beginTransaction();
		Query setQuery = session.getNamedQuery("size_view");
		// Query setQuery = session.createQuery("FROM V_SKU_SIZE_RULE");
		setQuery.setFirstResult(offset);
		setQuery.setMaxResults(batchSize);
		setQuery.setFetchSize(batchSize);
		ScrollableResults serachedSizeConvSet = setQuery
				.scroll(ScrollMode.FORWARD_ONLY);
		while (serachedSizeConvSet.next()) {
			SizeSynchDataHolderView sizeView = (SizeSynchDataHolderView) serachedSizeConvSet
					.get(0);
			serachedSizeConvList.add(sizeView);
			getHibernateTemplate().evict(sizeView);
		}
		tx.commit();
		session.close();
		return serachedSizeConvList;
	}
	
    /**
     * This method returns the specified list of VIEW objects for the given
     * skus.
     */
    @Override
    public List<SizeSynchDataHolderView> getVendorSkuSizeUpdationDataForSkus(Set<VendorSku> skus) {
        List<SizeSynchDataHolderView> searchedSizeConvList = new ArrayList<SizeSynchDataHolderView>();

        List<Long> skuIds = new ArrayList<Long>();
        for (VendorSku sku : skus) {
            if (sku != null) {
                skuIds.add(sku.getCarSkuId());
            }
        }

        try {
            StatelessSession session = getHibernateTemplate().getSessionFactory().openStatelessSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("Select * from V_SKU_SIZE_RULE where car_sku_id in (:skuIds)");
            q.setParameterList("skuIds", skuIds);

            ScrollableResults searchedSizeConvSet = q.scroll(ScrollMode.FORWARD_ONLY);
            while (searchedSizeConvSet.next()) {
                SizeSynchDataHolderView sizeView = (SizeSynchDataHolderView) searchedSizeConvSet.get(0);
                searchedSizeConvList.add(sizeView);
                getHibernateTemplate().evict(sizeView);
            }
            tx.commit();
            session.close();
        } catch (HibernateException e) {
            log.error("Error in query: " + e.getMessage());
        }
        return searchedSizeConvList;
    }

	/**
	 * This methods removes size conversion entries from the system permanently
	 * which were soft-deleted i.e. the ones which were marked 'Deleted' as
	 * their status .
	 * 
	 * @author Yogesh.Vedak
	 */
	@SuppressWarnings("unchecked")
	public void removeDeletedSizeConversionsFromSystem() {
		List<SizeConversionMaster> sizeRulesToDelete = (List<SizeConversionMaster>) getHibernateTemplate()
				.find(QUR_DELETED_RECORDS);
		log.debug("Removed Size rule objects from system" + sizeRulesToDelete);
		for (SizeConversionMaster sizeRule : sizeRulesToDelete) {
			removeSizeConversionFromSystem(findSizeConversionByScmId(sizeRule
					.getScmId()));
			log.debug("Removing Size Conversions from the database. record Id:"
					+ sizeRule.getScmId() + " and size name is :"
					+ sizeRule.getSizeName());
		}
		// folllowing is sql approach if in real time too many records were
		// marked deleted then to avoid hibernate outOfmemory
		// Integer cnt =
		// executeSQLQuery("Delete size_conversion_master where Status_cd='D'");
	}

	/**
	 * @auther Yogesh.Vedak
	 * @param enetityName
	 * @return
	 */
	private Long getRowCount(final String entityName) {
		Long cnt = (Long) getHibernateTemplate().find(
				"select count(*) from " + entityName).get(0);
		return cnt;
	}

	public Long getSizeSynchViewRowCount() {
		Long count = getRowCount("SizeSynchDataHolderView");
		getHibernateTemplate().evict(SizeSynchDataHolderView.class);
		return count;
	}

	Integer executeSQLQuery(final String query) {
		Integer updatedRecordCount = (Integer) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Transaction tx = session.beginTransaction();
						SQLQuery sq = session.createSQLQuery(query.toString());
						return sq.executeUpdate();

					}
				});
		return updatedRecordCount;
	}

	public int[] executeSizeRuleChangedFalseBatch(List<String> scmIdList)
			throws SQLException {
		Session session = getHibernateTemplate().getSessionFactory()
				.openSession();
		Connection con = session.connection();
		boolean initialAutocommitSetting = con.getAutoCommit();
		con.setAutoCommit(false);
		String sqlQuery = "update size_conversion_master set scm_rule_changed = ?,updated_by = ?,updated_date = sysdate where scm_id = ? ";
		PreparedStatement stmt = con.prepareStatement(sqlQuery);
		for (String scmIdRow : scmIdList) {
			stmt.setString(1, Constants.RULECHANGED_FALSE);
			stmt.setString(2, Constants.SCHEDULER_UPDATEDBY);
			stmt.setLong(3, new Long(scmIdRow).longValue());
			stmt.addBatch();
		}
		int[] updatesCount = stmt.executeBatch();
		con.commit();
		// con.close();
		con.setAutoCommit(initialAutocommitSetting);
		return updatesCount; //
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.belk.car.app.dao.SizeConversionDao#executeNewSizeRuleBatchInsert(
	 * java.util.List) Modified this method by adding a validation whether the
	 * size rule is already added to the system or not.
	 */
	@Override
	public int[] executeNewSizeRuleBatchInsert(
			List<SizeConversionMaster> ruleList) throws SQLException {
		Session session = getHibernateTemplate().getSessionFactory()
				.openSession();
		Connection con = session.connection();
		boolean initialAutocommitSetting = con.getAutoCommit();
		con.setAutoCommit(false);
		// insert (scm_id, scm_dept_id, scm_class_id, scm_vendor_id,
		// scm_size_name, scm_conversion_size_name, status_cd, created_by,
		// updated_by, created_date, updated_date,
		// facet_size_1,facet_size_2,facet_size_3,facet_sub_size_1,
		// facet_sub_size_2, scm_rule_changed)
		String queryInsert = "Insert into size_conversion_master values (CMM_SEQ.NEXTVAL,NULL,NULL,NULL,?,?,'P',?,?,SYSDATE,SYSDATE,"
				+ "   NULL,NULL,NULL,NULL,NULL,'F')";
		PreparedStatement stmt = con.prepareStatement(queryInsert);
		List<SizeConversionMaster> addedSizes = new ArrayList<SizeConversionMaster>();
		for (SizeConversionMaster scmRow : ruleList) {
			// The following check is for not inserting the same size with
			// associated departments,class and vendor
			// The ruleList contains all the rules which are not having the size
			// conversion name and some are new or some are old.
			String sizeName = scmRow.getSizeName();
			String deptId = scmRow.getDepartment().getDeptIdAsString();
			String classId = scmRow.getClassification()
					.getClassificationIdAsString();
			String vendorId = scmRow.getVendor().getVendorIdAsString();
			
			if(log.isInfoEnabled()){
			log.info("Inside SizeConversionDaoHibernate.executeNewSizeRuleBatchInsert()--> CAR_ID:" + scmRow.getCarId() + 
					" SCM_ID:" + scmRow.getScmId() +" SIZE_NAME:" + sizeName + " SIZE_CONVERSION_NAME:" + scmRow.getCoversionSizeName() + 
						" DEPT_ID:" + deptId + " CLASS_ID:" + classId + " VENDOR_ID:" + vendorId);
			}

			SizeConversionMaster tempSize = new SizeConversionMaster();
			tempSize.setSizeName(sizeName);
			tempSize.setDepartment(scmRow.getDepartment());
			tempSize.setClassification(scmRow.getClassification());
			tempSize.setVendor(scmRow.getVendor());

			if ((getSizeConversion(sizeName, classId, deptId, vendorId) == null) // size
																					// name
																					// does
																					// not
																					// have
																					// any
																					// assigned
																					// department,class,vendor
					&& (StringUtils.isNotBlank(sizeName)) // size name is not
															// empty
					&& (getSizeConversion(sizeName, "", "", "") == null) // size
																			// name
																			// not
																			// exists
																			// in
																			// mapping
																			// table
					&& !addedSizes.contains(tempSize)) // Not added to the list
			{
				if (log.isDebugEnabled()) {
					log.debug("inserting new entry for size name:"
							+ scmRow.getSizeName());
				}
				stmt.setString(1, scmRow.getSizeName());
				stmt.setString(2, scmRow.getSizeName());// Setting conversion
														// name as size name
				stmt.setString(3, Constants.SCHEDULER_CREATEDBY);
				stmt.setString(4, Constants.SCHEDULER_UPDATEDBY);
				stmt.addBatch();
				addedSizes.add(tempSize);
			} else {
				if (log.isDebugEnabled()) {
					log.debug("The size name already added into the size conversion master table:"
							+ sizeName);
				}
			}
		}
		int[] updatesCount = stmt.executeBatch();
		con.commit();
		// con.close();
		con.setAutoCommit(initialAutocommitSetting);
		return updatesCount; //
	}

}
