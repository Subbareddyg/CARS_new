package com.belk.car.app.dao.hibernate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.belk.car.app.Constants;
import com.belk.car.app.dao.SuperColorDao;
import com.belk.car.app.model.ColorMappingMaster;
import com.belk.car.app.model.SuperColorSynchDataHolderView;

/**
 * A DAO impl class using hibernate template to carry out Super color management database operations.
 * @author Yogesh.Vedak
 *
 */
public class SuperColorDaoHibernate extends UniversalDaoHibernate implements SuperColorDao{


	private final String ACTIVE_SCOLORS_HQL = "from ColorMappingMaster cmm where cmm.statusCode='"+Constants.SUPERCOLOR_STATUS_ACTIVE+"'";
	private final String ACTIVE_SCOLORS_SQL = "SELECT c1.* FROM color_mapping_master c1  WHERE 0=0 and c1.status_cd ='"+Constants.SUPERCOLOR_STATUS_ACTIVE+"'";
	private final String DELETED_SCOLORS_HQL = "from ColorMappingMaster cmm where cmm.statusCode='"+Constants.SUPERCOLOR_STATUS_DELETED+"'";

	/**
	 * This method returns all the 'active' Super Colors objects.
	 * @return List of {@link ColorMappingMaster}
	 * @author Yogesh.Vedak
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ColorMappingMaster> getAllSuperColorMapping() {
		List<ColorMappingMaster> colorMappingList = new ArrayList<ColorMappingMaster>();
		StringBuffer query= new StringBuffer(ACTIVE_SCOLORS_HQL);  
		colorMappingList =  getHibernateTemplate().find(query.toString());
		return colorMappingList;
	}

	/**
	 * This method adds the new supr Color object in to the system
	 * @param colorMapping
	 * @author Yogesh.Vedak
	 */
	@Override
	public void saveSuperColor(ColorMappingMaster colorMapping) {
		getHibernateTemplate().save(colorMapping);
		getHibernateTemplate().flush();
	}
	/**
	 * This method adds the new super Color object in to the system if its ID is not present else updates the existing based on the id present in to the object passed in an argument.
	 * @param colorMapping
	 * @author Yogesh.Vedak
	 */
	@Override
	public void saveOrUpdateSuperColor(ColorMappingMaster colorMapping) {
		getHibernateTemplate().saveOrUpdate(colorMapping);
		getHibernateTemplate().flush();

	}

	/**
	 * Searches active super colors based on super color name 'and' super color code values present in an arguments.
	 * @param superColorName
	 * @param superColorCode
	 * @return List of {@link ColorMappingMaster}
	 * @author Yogesh.Vedak
	 */
	@SuppressWarnings("unchecked")
	public List<ColorMappingMaster> searchSuperColors(String superColorName,String superColorCode) {
		final StringBuffer query = new StringBuffer(ACTIVE_SCOLORS_SQL);

		if(StringUtils.isNotBlank(superColorName)){
			query.append(" and ");
			query.append("lower(cmm_super_color_name) like lower('%"+superColorName+"%')");
		}
		if(StringUtils.isNotBlank(superColorCode)){
			query.append(" and ");
			query.append("c1.cmm_super_color_code = "+superColorCode);
		}
		List<ColorMappingMaster> serachSuperColorList = null;
		if (log.isDebugEnabled()){
			log.debug("Search Query:"+query.toString());
		}
		serachSuperColorList =  getSuperColorsByQuery(query.toString());
		return serachSuperColorList;
	}

	/**
	 * Searches active super colors based on super color name 'and' super color code values present in an arguments
	 * @param cmmId
	 * @return {@link ColorMappingMaster}
	 * @author Yogesh.Vedak
	 */
	@Override
	public ColorMappingMaster findSuperColorByCmmId(long cmmId) {
		ColorMappingMaster superColor = (ColorMappingMaster)getHibernateTemplate().get(ColorMappingMaster.class,cmmId);
		return superColor;
	}

	/**
	 * This method sets the status code to the specified value in the argument
	 * @param cmmId
	 * @param statusValue
	 * @author Yogesh.Vedak 
	 */

	@Override
	public void updateSuperColorStatus(long cmmId,String statusValue) {
		ColorMappingMaster cmmaster = findSuperColorByCmmId(cmmId);
		if(cmmaster != null){
			cmmaster.setStatusCode(statusValue);
			saveOrUpdateSuperColor(cmmaster);
		}
	}

	/**
	 * Returns the List of Super colors Objects based on sql-query(as string) passed.
	 * @param query
	 * @return {@link ColorMappingMaster}
	 * @author Yogesh.Vedak
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ColorMappingMaster> getSuperColorsByQuery(final String query){
		List<ColorMappingMaster> serachedSuperColorList = null;
		if (log.isDebugEnabled()) {
			log.debug("Query:"+query);
		}
		serachedSuperColorList = (List<ColorMappingMaster>)getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException {
						SQLQuery sq =session.createSQLQuery(query).addEntity(ColorMappingMaster.class);
						return sq.list();
					}
				});

		return serachedSuperColorList;

	}


	/**
	 * This Method returns the Super COlor Object based on the color name provided in the argument
	 * @param superColorName
	 * @return {@link ColorMappingMaster}
	 * @@author Yogesh.Vedak
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ColorMappingMaster findSuperColorByColorName(String superColorName) {
		ColorMappingMaster color = null;
		superColorName=StringUtils.upperCase(superColorName);
		if(log.isDebugEnabled()){
			log.debug("SuperColorName:"+superColorName);
		}
		List<ColorMappingMaster> scolorList = findSuperColorsByCriteria("superColorName",superColorName);
		if(scolorList!=null && !scolorList.isEmpty()){
			color  = (ColorMappingMaster) scolorList.get(0);
		}
		return color;
	}


	/**
	 * This methods returns that super color whose begin and end range consists the the color code value passed in the argument. Color names are unique
	 * @param beginOrEndRangeCode
	 * @return {@link ColorMappingMaster}
	 * @author Yogesh.Vedak
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ColorMappingMaster> getAvailableColorMappingsContainingRangeCode(String beginOrEndRangeCode) {
		Object[] arrCriteria= {beginOrEndRangeCode};
		StringBuffer query = new StringBuffer(ACTIVE_SCOLORS_HQL);
		query.append(" and ");
		query.append("( to_number(?) BETWEEN cmm.colorCodeBegin AND cmm.colorCodeEnd ) ");
		if(log.isDebugEnabled()){
			log.debug("Query for Avilable color mapping:"+ query.toString());
		}
		List<ColorMappingMaster> scolorList = (List<ColorMappingMaster>)getHibernateTemplate().find(query.toString(), arrCriteria);
		return scolorList;
	}


	/**
	 * This Method returns the Super Color Object based on the super Color Code provided in the argument. Color codes are unique.
	 * @param superColorCode
	 * @return {@link ColorMappingMaster}
	 * @@author Yogesh.Vedak
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ColorMappingMaster findSuperColorByColorCode(String superColorCode) {
		ColorMappingMaster color = null;
		List<ColorMappingMaster> scolorList = findSuperColorsByCriteria("superColorCode",superColorCode);
		if(scolorList!=null && !scolorList.isEmpty()){
			color  = (ColorMappingMaster) scolorList.get(0);
		}
		return color;
	}


	/**
	 *This method accepts hibernateProperty name and its value as first and second argument respectively and returns the 'active' super color matched .
	 *@param criteriaPropertyName
	 *@param criteriaValue
	 *@return {@link ColorMappingMaster}
	 *@author Yogesh.Vedak 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ColorMappingMaster> findSuperColorsByCriteria(String criteriaPropertyName , String criteriaValue) {
		Object[] arrCriteria= {criteriaValue};
		if(log.isDebugEnabled()){
			log.debug("criteriaPropertyName:"+criteriaPropertyName);
			log.debug("criteriaValue:"+criteriaValue);
		}
		StringBuffer query =  new StringBuffer(ACTIVE_SCOLORS_HQL);
		query.append(" and ");
		if("superColorName".equals(criteriaPropertyName)){
			query.append(" upper(cmm."+criteriaPropertyName+") = ? ");
		} else {
			query.append("cmm."+criteriaPropertyName+" = ? ");
		}
		if(log.isDebugEnabled()){
			log.debug("findSuperColorsByCriteria Query:"+query.toString());
		}
		List<ColorMappingMaster> scolorList = (List<ColorMappingMaster>)getHibernateTemplate().find(query.toString(), arrCriteria);
		return scolorList;
	}


	/**
	 * This method removes super color object from the system permanently . 
	 * @param superColor
	 * @author Yogesh.Vedak
	 */
	@Override
	public void removeSuperColor(ColorMappingMaster superColor) {
		getHibernateTemplate().delete(superColor);
		getHibernateTemplate().flush();
	}

	/**
	 * This method removes super color object from the system permanently . 
	 * @param cmmId
	 * @author Yogesh.Vedak
	 * 
	 */
	@Override
	public void removeSuperColor(long cmmId) {
		removeSuperColor(findSuperColorByCmmId(cmmId));

	}

	/**
	 * This method returns the Super Color Code of the Super Color based on the color Code provided in an argument. 
	 * This argument value's  checked against the begin and end color code range of super colors and the matched range in which it seems to belong is the  SuperColor of whose color code will be returned.
	 * @param skuColorCode
	 * @return {@link String} superColorCode
	 */
	@Override
	public String findSuperColorCodeForColorCode(String skuColorCode) {
		String superColorCode="";
		List<ColorMappingMaster> scolorList = null;
		scolorList = getAvailableColorMappingsContainingRangeCode(skuColorCode);
		if(scolorList !=null && !scolorList.isEmpty()){
			ColorMappingMaster color =scolorList.get(0);
			superColorCode = color.getSuperColorCode();
		}
		return superColorCode;
	}

	public String findSuperColorNameForColorCode(String skuColorCode){
		String superColorName="";
		List<ColorMappingMaster> scolorList = null;
		scolorList = getAvailableColorMappingsContainingRangeCode(skuColorCode);
		if(scolorList !=null && !scolorList.isEmpty()){
			ColorMappingMaster color =scolorList.get(0);
			superColorName = color.getSuperColorName();
		}
		return superColorName;
	}

	/**
	 * This method returns Super Color based on beginColorCode provided in argument
	 * @param beginCode
	 * @return {@link ColorMappingMaster}
	 * @author Yogesh.Vedak
	 */
	@Override
	public ColorMappingMaster getSuperColorByBeginCode(String beginCode) {
		ColorMappingMaster cmmaster = null;
		List<ColorMappingMaster> cmmasterList = findSuperColorsByCriteria("colorCodeBegin", beginCode);
		if(cmmasterList!=null && !cmmasterList.isEmpty()){
			cmmaster  = (ColorMappingMaster)cmmasterList.get(0);
		}
		return cmmaster;
	}

	/**
	 * This method returns Super Color based on endCode provided in argument
	 * @param endCode
	 * @return {@link ColorMappingMaster}
	 * @author Yogesh.Vedak
	 */
	@Override
	public ColorMappingMaster getSuperColorByEndCode(String endCode) {
		ColorMappingMaster cmmaster = null;
		List<ColorMappingMaster> cmmasterList = findSuperColorsByCriteria("colorCodeEnd", endCode);
		if(cmmasterList!=null && cmmasterList.size()>0){
			cmmaster  = (ColorMappingMaster)cmmasterList.get(0);
		}
		return cmmaster;
	}


	/**
	 * This method returns those Super Colors whose Rule_changed=T. ie the ones which have been changed or added currently 
	 * @return List of {@link ColorMappingMaster}
	 * @author Yogesh.Vedak
	 * 
	 */
	@Override
	public List<ColorMappingMaster> getChangedSuperColorMappings() {
		return findSuperColorsByCriteria("ruleChanged", Constants.RULECHANGED_TRUE);
	}


	/**
	 * This method sets the Super Color's flag as true denoting it has changed.
	 * @param cmmId
	 * @param flagCode
	 * @author Yogesh.Vedak
	 */
	@Override
	public void updateRuleChangedFlag(long cmmId, String flagCode) {
		ColorMappingMaster master = findSuperColorByCmmId(cmmId);
		if(master !=null){
			master.setRuleChanged(flagCode);
		}
		saveOrUpdateSuperColor(master);
	}

	/**
	 * This methods removes super colors from the system permanently which were soft-deleted i.e. the ones which were marked 'Deleted' as their status .
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void removeDeletedSuperColorsFromSystem(){
		List<ColorMappingMaster> colorsTodelete = (List<ColorMappingMaster>)getHibernateTemplate().find(DELETED_SCOLORS_HQL);
		for(ColorMappingMaster color : colorsTodelete){
			removeSuperColor(color.getCmmId());
			log.debug("Removing Super Color from the database. record Id:"+color.getCmmId()+" and super color code :"+color.getSuperColorCode());
		}
	}

	/**
	 * This method returns the specified list of VIEW objects which holds the data to be applied on sku ,for updating vendor sku's SUPER_COLOR_1 attribute value . 
	 * @return List of {@link SuperColorSynchDataHolderView}
	 */
	@SuppressWarnings("unchecked")
	private List<SuperColorSynchDataHolderView> getDataForUpdateSuperColor(int rowsOffset,int batchSize) {
		List<SuperColorSynchDataHolderView> serachedColorDataList = new ArrayList<SuperColorSynchDataHolderView>();
		StatelessSession session = getHibernateTemplate().getSessionFactory().openStatelessSession();
		Transaction tx = session.beginTransaction();
		//Query setQuery = session.getNamedQuery("supercolor_view");
		Query setQuery = session.createQuery("from SuperColorSynchDataHolderView where colorRuleId is not null");
		setQuery.setFirstResult(rowsOffset); 
		setQuery.setMaxResults(batchSize);
		setQuery.setFetchSize(batchSize);
		ScrollableResults serachedSizeConvSet  = setQuery.scroll(ScrollMode.FORWARD_ONLY);
		while ( serachedSizeConvSet.next() ) {
			SuperColorSynchDataHolderView colorSynchView = (SuperColorSynchDataHolderView) serachedSizeConvSet.get(0);
			serachedColorDataList.add(colorSynchView);
			getHibernateTemplate().evict(colorSynchView);
		}
		serachedSizeConvSet.close();
		tx.commit();
		session.close();
		return serachedColorDataList;
	}

	/**
	 * This method returns the specified list of VIEW objects which holds the data to be applied on sku ,for "inserting" vendor sku's SUPER_COLOR_1 attribute .
	 * @return List of {@link SuperColorSynchDataHolderView}
	 */
	private List<SuperColorSynchDataHolderView> getDataForInsertSuperColor(int rowsOffset,int batchSize) {
		List<SuperColorSynchDataHolderView> serachedColorDataList = new ArrayList<SuperColorSynchDataHolderView>();
		StatelessSession session = getHibernateTemplate().getSessionFactory().openStatelessSession();
		Transaction tx = session.beginTransaction();
		Query setQuery = session.createQuery("from SuperColorSynchDataHolderView where colorRuleId is null");
		setQuery.setFirstResult(rowsOffset); 
		setQuery.setMaxResults(batchSize);
		setQuery.setFetchSize(batchSize);
		ScrollableResults serachedSizeConvSet  = setQuery.scroll(ScrollMode.FORWARD_ONLY);
		while ( serachedSizeConvSet.next() ) {
			SuperColorSynchDataHolderView colorSynchView = (SuperColorSynchDataHolderView) serachedSizeConvSet.get(0);
			serachedColorDataList.add(colorSynchView);
			getHibernateTemplate().evict(colorSynchView);
		}
		serachedSizeConvSet.close();
		tx.commit();
		session.close();
		return serachedColorDataList;
	} 

	private ScrollableResults getResultsUsingHQL(String hql,int rowsOffset,int batchSize) {
		StatelessSession session = getHibernateTemplate().getSessionFactory().openStatelessSession();
		Transaction tx = session.beginTransaction();
		Query setQuery = session.createQuery(hql);
		setQuery.setFirstResult(rowsOffset); 
		setQuery.setMaxResults(batchSize);
		setQuery.setFetchSize(batchSize);
		ScrollableResults searchedColorMappingSet  = setQuery.scroll(ScrollMode.FORWARD_ONLY);
		tx.commit();
		session.close();
		return searchedColorMappingSet;
	}
	
	
	
	public List<SuperColorSynchDataHolderView> getVendorSkuSuperColorUpdationData(int rowsOffset,int batchSize,boolean forInsertSuperColor) {
		if(forInsertSuperColor){
			return getDataForInsertSuperColor(rowsOffset,batchSize);
		}else{
			return getDataForUpdateSuperColor(rowsOffset,batchSize);
		}
	}

	/**
	 * @auther Yogesh.Vedak
	 * @param enetityName
	 * @return
	 */
	private Long getRowCount(final String hql){
		Long cnt = (Long) getHibernateTemplate().find(hql).get(0);
		return cnt;
	}
	
	/**
	 * This Method Gives the count of the data rows for which Super_color_1 atribute is to be updated(resych) or inserted newly as a whole based on a boolean-value as true or false respectively. 
	 * @return List of {@link SuperColorSynchDataHolderView}
	 */
	public Long getSuperColorSynchViewRowCount(boolean inserNewSuperColorCase){
		if(inserNewSuperColorCase){
			String hql = "select count(*) from SuperColorSynchDataHolderView where colorRuleId = null";
			return getRowCount(hql);
		}else{
			String hql = "select count(*) from SuperColorSynchDataHolderView where colorRuleId is not null ";
			return getRowCount(hql);
		}

	}

	/**
	 * This Method updates the super color rules changed status to false in a batch based on a list passed of their  Ids ie cmm-id
	 * @return List of {@link SuperColorSynchDataHolderView}
	 * @auther Yogesh.Vedak
	 */
	public int[] executeSuperColorRuleChangedBatch(List<String> cmmIdList) throws SQLException{
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Connection con =session.connection();
		boolean initialAutocommitSetting =  con.getAutoCommit();
		con.setAutoCommit(false);
		String sqlQuery="update color_mapping_master set cmm_rule_changed = ? where cmm_id = ? ";
		PreparedStatement stmt = con.prepareStatement(sqlQuery);
		for (String cmmIdRow: cmmIdList) {
			stmt.setString(1, Constants.RULECHANGED_FALSE);
			stmt.setLong(2, new Long(cmmIdRow).longValue());  
			stmt.addBatch();
		}
		int [] updatesCount=stmt.executeBatch(); 
		con.commit();
		con.setAutoCommit(initialAutocommitSetting );
		return updatesCount; //
	}

}
