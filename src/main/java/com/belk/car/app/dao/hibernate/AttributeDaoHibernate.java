package com.belk.car.app.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.belk.car.app.dao.AttributeDao;
import com.belk.car.app.dto.AttributeBMMappingDTO;
import com.belk.car.app.exceptions.DaoException;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeChangeTracking;
import com.belk.car.app.model.AttributeConfig;
import com.belk.car.app.model.AttributeDatatype;
import com.belk.car.app.model.AttributeLookupValue;
import com.belk.car.app.model.AttributeType;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.ClassAttribute;
import com.belk.car.app.model.ContentStatus;
import com.belk.car.app.model.DepartmentAttribute;
import com.belk.car.app.model.ProductTypeAttribute;

public class AttributeDaoHibernate extends CachedQueryDaoHibernate implements AttributeDao {

	public List<Attribute> getAllAttributes() {
		List<Attribute> list = getHibernateTemplate().find("from Attribute");
		return list;
	}

	public Attribute getAttribute(Long attrId) {		
		return (Attribute) getHibernateTemplate().get(Attribute.class, attrId);
	}

	public Attribute getAttribute(String name) {
		List<Attribute> attrs = getHibernateTemplate().find("from Attribute where name=?", name);
		if (attrs != null && !attrs.isEmpty()) {
			return attrs.get(0);
		}
		return null;
	}

	public List<AttributeDatatype> getAttributeDataTypes() {
		return doCachedQueryCall("AttributeDatatype");
	}

	public List<AttributeType> getAttributeTypes() {
		return getHibernateTemplate().find("from AttributeType");
	}

	public Attribute save(Attribute attribute) {
		getHibernateTemplate().saveOrUpdate(attribute);
		getHibernateTemplate().flush();
		return attribute;
	}

	public AttributeConfig save(AttributeConfig config) {
		getHibernateTemplate().saveOrUpdate(config);
		getHibernateTemplate().flush();
		return config;
	}

	public List<ClassAttribute> getClassAttributes() {
		return getHibernateTemplate().find("from ClassAttribute");
	}

	public List<DepartmentAttribute> getDepartmentAttributes() {
		return getHibernateTemplate().find("from DepartmentAttribute");
	}

	public List<ProductTypeAttribute> getProductTypeAttributes() {
		return getHibernateTemplate().find("from ProductTypeAttribute");
	}

	public List<AttributeConfig> getAllAttributeDisplayTypes() {
		return getHibernateTemplate().find("from AttributeConfig ac order by ac.displayName");
	}

	public AttributeConfig getAttributeConfig(Long attributeConfigId) {
		return (AttributeConfig) getHibernateTemplate().get(AttributeConfig.class, attributeConfigId);
	}

	public AttributeDatatype getAttributeDataType(String attributeDataType) {
		if (log.isDebugEnabled())
			log.debug("Retrieving data type for code:" + attributeDataType);

		List dataTypeList = getHibernateTemplate().find("FROM AttributeDatatype adt WHERE adt.attrDatatypeCd = ?", attributeDataType);

		if (dataTypeList == null || dataTypeList.isEmpty()) {
			throw new DaoException("data Type '" + attributeDataType + "' not found...");
		} else {
			return (AttributeDatatype) dataTypeList.get(0);
		}
	}

	public AttributeType getAttributeType(String attributeType) {
		List attributeTypeList = getHibernateTemplate().find("FROM AttributeType adt WHERE adt.attrTypeCd = ?", attributeType);

		if (attributeTypeList == null || attributeTypeList.isEmpty()) {
			throw new DaoException("attribute Type '" + attributeType + "' not found...");
		} else {
			return (AttributeType) attributeTypeList.get(0);
		}
	}

	public DepartmentAttribute getDepartmentAttribute(Long deptAttrId) {
		return (DepartmentAttribute) getHibernateTemplate().get(DepartmentAttribute.class, deptAttrId);
	}

	public ClassAttribute getClassificationAttribute(Long classificationAttId) {
		return (ClassAttribute) getHibernateTemplate().get(ClassAttribute.class, classificationAttId);
	}

	public ProductTypeAttribute getProductTypeAttribute(Long productTypeAttrId) {
		return (ProductTypeAttribute) getHibernateTemplate().get(ProductTypeAttribute.class, productTypeAttrId);
	}

	public void remove(Attribute attribute) {
		getHibernateTemplate().delete(attribute);
	}

	public List<Attribute> searchAttributes(String attributeName, Long classificationId, String blueMartiniName) {
		return searchAttributes(attributeName, classificationId, blueMartiniName, null);
	}

	public boolean getAttributesByBlueMartiniNameAndType(String blueMartiniName,String attributeType) {
		boolean isPresentFl=false;
	    List<Attribute> attributeList=getHibernateTemplate().find("FROM Attribute attr where upper(attr.blueMartiniAttribute) ='"+blueMartiniName.toUpperCase()+"'");
		if(attributeList != null && attributeList.size() > 0){
			isPresentFl=true;
		}
		return isPresentFl;
	}
	
	public List<Attribute> searchAttributes(String attributeName, Long classificationId, String blueMartiniName, String productTypeName) {

		List<Attribute> attributeList = null;
		String sqlQuery = "select attr.* from attribute attr where 0=0 ";
		StringBuffer sb = new StringBuffer(sqlQuery);

		if (StringUtils.isNotBlank(attributeName)) {
			sb.append(" and upper(attr.name) like '%")
				.append(StringUtils.replace(attributeName.toUpperCase(), "'", "''"))
				.append("%'");
		}
		
		if (StringUtils.isNotBlank(blueMartiniName)) {
			sb.append(" and upper(attr.blue_martini_attribute) like '%")
				.append(StringUtils.replace(blueMartiniName.toUpperCase(), "'", "''"))
				.append("%'");
		}

		if (classificationId != null) {
			sb.append(" and attr.attr_id in ")
				.append(" (select ca.attr_id from class_attribute ca inner join classification c on c.class_id = ca.class_id ")
				.append(" where c.belk_class_number = ").append(classificationId).append(")");
		}
		
		if (StringUtils.isNotBlank(productTypeName)) {
			sb.append(" and attr.attr_id in ")
				.append(" (select pta.attr_id from product_type_attribute pta inner join product_type pt on pt.product_type_id = pta.product_type_id ")
				.append(" where upper(pt.name) like '%")
				.append(StringUtils.replace(productTypeName.toUpperCase(), "'", "''"))
				.append("%')");
		}

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			attributeList = session.createSQLQuery(sb.toString()).addEntity(Attribute.class).list();
		} catch (Exception e) {
			log.error("Hibernate Exception", e);
		} finally {
			//Do nothing for now...
			//But may have to see if close connections
		}

		if (attributeList == null) {
			attributeList = new ArrayList<Attribute>();
		}

		return attributeList;

	}

	public List<CarAttribute> getAllCarAttributeValuesForStatus(String attributeValueProcessStatus) {		
		StringBuffer sqlB = new StringBuffer("from CarAttribute ca ");
		sqlB
		.append(" inner join fetch ca.attribute as attr")
		.append(" inner join fetch ca.car as car")
		.append(" where ca.attributeValueProcessStatus.processStatusCd=?")
		.append(" and ca.attrValue is not null")
		.append(" and car.contentStatus.code!=?");
		return getHibernateTemplate().find(sqlB.toString(), new String[] { attributeValueProcessStatus, ContentStatus.IN_PROGRESS });		
	}

	
	public void saveAttributeTrackingList(List<AttributeChangeTracking> attributeChangeTrackingList){
		for(AttributeChangeTracking atc : attributeChangeTrackingList){
			saveAttributeTracking(atc);
		}
	}

	public void saveAttributeTracking(AttributeChangeTracking attributeChangeTracking){
		try{
		//System.out.println("hibernate save"+attributeChangeTracking.toString());
		getHibernateTemplate().saveOrUpdate(attributeChangeTracking);
		getHibernateTemplate().flush();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public void executeAttributeSynchronizationUsingStoreProcedure(){
		System.out.println("Procedure executing---");
		String query = "{ Call RESYNC_ATTRIBUTES.PROCESS_ALL() }";
		executeSQLQuery(query);
		System.out.println("after done with procedur");
	}

	public List<String>  getAttributeSynchRecordForBMIGeneration(final int offset,final int batchSize){
		String  sqlQuery = "select id||'|0|'||a.car_id||'|'||c.vendor_number||c.vendor_style_number||'|'||a.attr_id||'|'||a.attr_name||'|'||(case when a.attr_old_value is not null then a.attr_old_value else '<NULL>'end)||'|'||(case when a.attr_value is not null then a.attr_value else '<NULL>'end) from tmp_attribute_resync a, vendor_style c, car d where a.car_id = d.car_id and d.vendor_style_id = c.vendor_style_id  and (a.action = 'D' or a.action='U') ";	
		log.debug("Query formed in getAttributeSynchRecordForBMIGeneration() is: "+sqlQuery);
		return getColumnValuesByColumnIndex(sqlQuery, offset, batchSize, 0);
	}

	private List<String> getColumnValuesByColumnIndex(String sql, final int offset,final int batchSize,int columnIndex){
		List<String> searchedList = new ArrayList<String>();
		StatelessSession session = getHibernateTemplate().getSessionFactory().openStatelessSession();
		Transaction tx = session.beginTransaction();
		SQLQuery setQuery = session.createSQLQuery(sql);
		setQuery.setFirstResult(offset); 
		setQuery.setMaxResults(batchSize);
		setQuery.setFetchSize(batchSize);
		ScrollableResults serachedSkusSet  = setQuery.scroll(ScrollMode.FORWARD_ONLY);
		while ( serachedSkusSet.next() ) {
			String column = (String) serachedSkusSet.get(columnIndex);
			searchedList.add(column);
		}
		tx.commit();
		session.close();
		return searchedList;
	}

	private Integer executeSQLQuery(final String query){
		Integer updatedRecordCount = (Integer)getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException {
						Transaction tx =  session.beginTransaction();
						SQLQuery sq =session.createSQLQuery(query.toString());
						int cnt = sq.executeUpdate();
						log.debug("Query in executeSQLQuery() methd: "+query);
						session.flush();
						log.debug("Flush Count"+cnt);
						return cnt;
					}
				});
		return updatedRecordCount;
	}

	/**
	 * This Method Gives the count of the data rows for tmp_attribute_resynch table
	 * @return Long
	 */
	public Long getTempAttributeSynchCount(){
			String hql = "select count(*) from tmp_attribute_resync";
			return getRowCount(hql);
			
	}

	private Long getRowCount(final String hql){
		StatelessSession session = getHibernateTemplate().getSessionFactory().openStatelessSession();
		Transaction tx = session.beginTransaction();
		SQLQuery setQuery = session.createSQLQuery(hql);
		//System.out.println("setQuery------------"+setQuery.list().toString());
		List<Object> list = setQuery.list();
		tx.commit();
		session.close();
		Long counst = 0L;
		for (Object obj : list) {
			counst = new Long(obj.toString());
		}
		System.out.println("counst="+counst);
		return counst;
		
	}

	@Override
	public List<AttributeChangeTracking> getUnprocessedEditedAttrValues() {
		List<AttributeChangeTracking> attrValueList =null;
		String sql = "select * from attribute_change_tracking where ACT_ACTION='Edit' and ACT_TYPE='attributeValues' and ACT_PROCESSED='N'";
		return getUnprocessedAttrChangesByQuery(sql);
	}
	
	public List<AttributeChangeTracking> getUnprocessedAttrChangesByQuery(String sql) {
		List<AttributeChangeTracking> attrValueList =null;
		
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			attrValueList = session.createSQLQuery(sql).addEntity(AttributeChangeTracking.class).list();
		} catch (Exception e) {
			System.out.println("exception occurred in AttributeDAOHibernate.getUnprocessedAttrChangesByQuery() :"+sql);
			log.error("Hibernate Exception", e);
		} finally {
			
		}
		return attrValueList;
	}
	
	public AttributeChangeTracking getUnprocessedEditedAttrValueByOldValue(String newValue) {
		List<AttributeChangeTracking> attrValueList =null;
		String sql = "select * from attribute_change_tracking where ACT_ACTION='Edit' and ACT_TYPE='attributeValues' and ACT_PROCESSED='N' and ACT_NEW_VALUE='"+newValue+"'";
		
		List<AttributeChangeTracking>  existingACTs= getUnprocessedAttrChangesByQuery(sql);
		for(AttributeChangeTracking act : existingACTs){
			System.out.println("existingActgot is"+act);
			return act;
		}
		System.out.println("..returning null?");
		return null;
	}

    @Override
    public AttributeLookupValue save(AttributeLookupValue attributeLookupValue) {
        getHibernateTemplate().saveOrUpdate(attributeLookupValue);
        getHibernateTemplate().flush();
        return attributeLookupValue;
    }
	
    /**
     * Method to retrieve the car attribute name to bm attribute name mapping
     *  including display name and type.
     */
    @SuppressWarnings("unchecked")
    public List<AttributeBMMappingDTO> getAttributeBMMapping(){
        List<Object[]> mapping = null;
        List<AttributeBMMappingDTO> dtoList = new ArrayList<AttributeBMMappingDTO>();
        StringBuffer sb = new StringBuffer();
        sb.append("select AttributeName, BM_NAME,TYPE,DISPLAY_NAME from ((");
        sb.append("select replace(trim(a.name),' ','_') AttributeName,a.BLUE_MARTINI_ATTRIBUTE BM_NAME,cast('CAR ATTRIBUTE' as varchar2(20)) as TYPE,ac.DISPLAY_NAME DISPLAY_NAME from ATTRIBUTE a,attribute_config ac where a.ATTR_CONFIG_ID = ac.ATTR_CONFIG_ID) ");
        sb.append(" union ");
        sb.append("(select replace(trim(pa.name),' ','_') AttributeName,NVL(pa.BM_ATTR_NAME,pa.name) BM_NAME,cast('PIM ATTRIBUTE' as varchar2(20)) as TYPE,pa.name DISPLAY_NAME from PIM_ATTRIBUTE pa where pa.PIM_ATTR_ID not in (select pim_attr_id from PIM_ATTR_EXCLUSION_LIST) and lower(trim((pa.name)))not in (select lower(trim((name))) from attribute)");
        sb.append(")) order by TYPE");
        if(log.isDebugEnabled()){
            log.debug("query inside getAttributeBMMapping "+sb.toString());
        }
        SessionFactory sf = getHibernateTemplate().getSessionFactory();
        Session session = sf.getCurrentSession();
        try{
            SQLQuery query = session.createSQLQuery(sb.toString());
            mapping = query.list();
            if(log.isDebugEnabled()){
                log.debug("size of results from db "+mapping.size());
            }
            for(Object[] result : mapping){
                AttributeBMMappingDTO dto = new AttributeBMMappingDTO();
                dto.setAttributeName((String)result[0]);
                dto.setBmAttributeName(StringUtils.trim((String)result[1]).replaceAll("[^a-zA-Z0-9]", "_"));
                dto.setType((String)result[2]);
                dto.setDisplayName((String)result[3]);
                dtoList.add(dto);
            }
        }catch(Exception e){
            log.error("Error while retrieving Attribute name to BM Attribute name mapping "+e);
        }
        if(log.isDebugEnabled()){
            log.debug("size of dto list returned from  getAttributeBMMapping "+dtoList.size());
        }
        return dtoList;
    }
}
