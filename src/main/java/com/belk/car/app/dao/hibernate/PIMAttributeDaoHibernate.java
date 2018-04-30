package com.belk.car.app.dao.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.belk.car.app.dao.PIMAttributeDao;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.CarsPimItemUpdate;
import com.belk.car.app.model.PIMAttribute;
import com.belk.car.app.model.PoUnitDetail;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorSkuPIMAttribute;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.VendorStylePIMAttribute;


public class PIMAttributeDaoHibernate extends CachedQueryDaoHibernate implements PIMAttributeDao {

	public List<PIMAttribute> getAllPIMAttributes() {
		List<PIMAttribute> list = getHibernateTemplate().find("from PIMAttribute");
		return list;
	}

	public Object save(Object ob) {
		this.getHibernateTemplate().saveOrUpdate(ob);
        getHibernateTemplate().flush();
		return ob;
	}

	
	@Override
	public void saveVSPimAttributes(List<VendorStylePIMAttribute> vendorStylePIMAttributelist) {
		// TODO Auto-generated method stub
		this.getHibernateTemplate().saveOrUpdateAll(vendorStylePIMAttributelist);
        getHibernateTemplate().flush();
	}
	
	
	public List<PIMAttribute> searchPIMAttributes(String attributeName) {

		String sqlQuery = "from PIMAttribute pimattr where 0=0 ";
		StringBuffer sb = new StringBuffer(sqlQuery);

		if (StringUtils.isNotBlank(attributeName)) {
			sb.append(" and upper(pimattr.name) like '%")
				.append(StringUtils.replace(attributeName.toUpperCase(), "'", "''"))
				.append("%'");
		}
		sb.append( "order by pimattr.name");
		logger.debug("pim attribute search query ==> " + sb.toString() );

		List<PIMAttribute> list = getHibernateTemplate().find(sb.toString());
		
		return list;

	}


	@Override
	public PIMAttribute getPIMAttributeByID(String pimId) {
		
		Long pimAttId = Long.parseLong(pimId);
		
		Object ob = getHibernateTemplate().find(
				"from PIMAttribute pimattr where pimAttrId=?", pimAttId);
		Collection<PIMAttribute> col = (Collection) ob;
		for (PIMAttribute pimAttribute : col) {
			return pimAttribute;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
    @Override
	public PIMAttribute getPIMAttributeByName(String attributeName) {
		
        List<PIMAttribute> list = new ArrayList<PIMAttribute>();

        String sql = "SELECT * FROM PIM_ATTRIBUTE where lower(NAME)='"+ attributeName.toLowerCase()+"'";
        SessionFactory sf = getHibernateTemplate().getSessionFactory();
        Session session = sf.getCurrentSession();
        try{
            SQLQuery sQuery = (SQLQuery)session.createSQLQuery(sql).addEntity(PIMAttribute.class);
            list = sQuery.list();       
        }catch(Exception e){
            log.error("Exception while retrieving pim attribute for given car sku id "+attributeName, e);
        }
        if(list == null){
            list = new ArrayList<PIMAttribute>();
        }
        
        if(list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
        
	}
	
	@Override
	public int updatePIMAttributes(String query) {
		// TODO Auto-generated method stub
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		SQLQuery query2 = session.createSQLQuery(query);
		int updatecnt=query2.executeUpdate();
		return updatecnt;
	}
	
	//This metod saves the PIM attribute object & get the saved object from DB and return it back. 
	@Override
	public PIMAttribute savePimAttribute(PIMAttribute patt) {
		
			this.getHibernateTemplate().saveOrUpdate(patt);
            getHibernateTemplate().flush();
			Object ob = getHibernateTemplate().find(
				"from PIMAttribute pimattr where name=?", patt.getName());
			Collection<PIMAttribute> col = (Collection) ob;
			for (PIMAttribute pimAttribute : col) {
				return pimAttribute;
			}
			
		return null;
	}

	@Override
	public void delete(Object ob) {
		
		this.getHibernateTemplate().delete(ob);
		
	}

	@Override
	public VendorStyle getvendorStyle(long orinNumber) {
		Object ob = getHibernateTemplate().find(
				"from VendorStyle where orinNumber=?", orinNumber);
			Collection<VendorStyle> col = (Collection) ob;
			for (VendorStyle vendorStyle : col) {
				return vendorStyle;
			}
			
		return null;
	}
	
	@Override
    public VendorStyle getLastUpdatedVendorStyle(long orinNumber) {
	         Object ob = getHibernateTemplate().find(
                "from VendorStyle where orinNumber=?", orinNumber);
            Collection<VendorStyle> col = (Collection) ob;
            
            Date maxDate = null;
            VendorStyle lastUpdatedVendorStyle = null;
            for (VendorStyle vendorStyle : col) {
                if (maxDate==null || maxDate.before(vendorStyle.getUpdatedDate())) {
                    maxDate = vendorStyle.getUpdatedDate();
                    lastUpdatedVendorStyle = vendorStyle;
                }
            }
            return lastUpdatedVendorStyle;
    }

	@Override
	public void saveVendoorStyle(VendorStyle vendorStyle) {
		
			this.getHibernateTemplate().merge(vendorStyle);
			this.getHibernateTemplate().flush();
			
		
		
	}
	
	public void saveVendorSku(VendorSku vendorSku){
	    this.getHibernateTemplate().merge(vendorSku);
	    this.getHibernateTemplate().flush();
	}

	@Override
	public void deletevspalist(List<VendorStylePIMAttribute> vsPimAttList) {
		// TODO Auto-generated method stub
		this.getHibernateTemplate().deleteAll(vsPimAttList);
		this.getHibernateTemplate().flush();
	}
	
	@Override
    public void deletevskupalist(List<VendorSkuPIMAttribute> vskuPimAttList) {
        // TODO Auto-generated method stub
        this.getHibernateTemplate().deleteAll(vskuPimAttList);
        this.getHibernateTemplate().flush();
    }

	public List<PoUnitDetail> getSkuExistInPOUnitDetailsDao(String belkSKU){
		List<PoUnitDetail> poList=null;
		poList=getHibernateTemplate().find(
				"from PoUnitDetail where BELK_SKU = ?",belkSKU);
		if(poList==null)
			poList=new ArrayList<PoUnitDetail>();
		
		return poList;
		
	}
	
    /**
     * Return all items of specified TYPE that have been updated from PIM, where
     * the processed_by_cars flag is "N"
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<CarsPimItemUpdate> getAllUpdatedItems(final int batchSize) {
        List<CarsPimItemUpdate> result = new ArrayList<CarsPimItemUpdate>();
        try {
            Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
            Query q = session.createQuery(
                    "from CarsPimItemUpdate cpiu where cpiu.processedByCar = ?");
            q.setString(0, CarsPimItemUpdate.FLAG_NO);
            q.setMaxResults(batchSize);
            q.setFetchSize(batchSize);
            result = q.list();
        } catch (Exception e) {
            log.error("Hibernate Exception", e);
        }
        return result;
    }

    /**
     * Mark List of CarsPimItemUpdate objects as Processed_by_cars = "Y".
     */
    @Override
    public void setProcessedByCarFlagForAllItems(List<CarsPimItemUpdate> items, String newValue) {
        if (items != null && !items.isEmpty()) {
            for (CarsPimItemUpdate item : items) {
                item.setProcessedByCar(newValue);
                item.setUpdatedTimestamp(new Date());
                getHibernateTemplate().merge(item);
            }
            getHibernateTemplate().flush();
        }
    }
    
    /**
     * Mark all flags of CarsPimItemUpdate objects to null.
     * @param items
     */
    public void resetFlagsForPimItemUpdate(CarsPimItemUpdate item) {
        if (item != null) {
            item.setDropshipUpdate(null);
            item.setMerchHierarchyFlag(null);
            item.setUpdatedTimestamp(new Date());
            getHibernateTemplate().saveOrUpdate(item);
            getHibernateTemplate().flush();
        }
    }
    
    /**
     * Return the total count of CarsPimItemUpdate objects processed today.
     */
    public int getProcessedPimItemUpdateCount() {
        Long count = Long.valueOf(0);
        try {
            Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
            Query q = session.createQuery(
                    "select count(cpiu.id) from CarsPimItemUpdate cpiu where cpiu.processedByCar = ?" +
                    " and trunc(cpiu.updatedTimestamp)=trunc(sysdate)");
            q.setString(0, CarsPimItemUpdate.FLAG_YES);
            count = (Long) q.uniqueResult();
        } catch (Exception e) {
            log.error("Hibernate Exception", e);
        }
        return count.intValue();
    }

    /**
     * Method to retrieve List of CarAttribute objects for a given car and carAttribute id.
     */
    @SuppressWarnings("unchecked")
    public List<CarAttribute> getCarAttributesByAttrId(Car car,long attrId)throws Exception{

        List<CarAttribute> list = new ArrayList<CarAttribute>();
        if(car == null){
            return list;
        }
        long carId = car.getCarId();
        String sql = "SELECT * FROM CAR_ATTRIBUTE WHERE CAR_ID=:CAR_ID AND ATTR_ID=:ATTR_ID";
        SessionFactory sf = getHibernateTemplate().getSessionFactory();
        Session session = sf.getCurrentSession();
        try{
            SQLQuery sQuery = (SQLQuery)session.createSQLQuery(sql).addEntity(CarAttribute.class)
                    .setParameter("CAR_ID", carId)
                    .setParameter("ATTR_ID", attrId);
            list = sQuery.list();       
        }catch(Exception e){
            log.error("Exception while retrieving car attributes for given carid "+carId+" and attrId "+attrId, e);
        }
        if(list == null){
            list = new ArrayList<CarAttribute>();
        }
        return list;
    }
    
    /**
     * Method to retrieve List of CarSkuAttribute objects for a given vendorSku and Attribute id.
     */
    @SuppressWarnings("unchecked")
    public List<CarSkuAttribute> getCarSkuAttributesByAttrId(VendorSku vSku,long attrId)throws Exception{

        List<CarSkuAttribute> list = new ArrayList<CarSkuAttribute>();
        if(vSku == null){
            return list;
        }
        long carSkuId = vSku.getCarSkuId();
        String sql = "SELECT * FROM CAR_SKU_ATTRIBUTE WHERE CAR_SKU_ID=:CAR_SKU_ID AND ATTR_ID=:ATTR_ID";
        SessionFactory sf = getHibernateTemplate().getSessionFactory();
        Session session = sf.getCurrentSession();
        try{
            SQLQuery sQuery = (SQLQuery)session.createSQLQuery(sql).addEntity(CarSkuAttribute.class)
                    .setParameter("CAR_SKU_ID", carSkuId)
                    .setParameter("ATTR_ID", attrId);
            list = sQuery.list();       
        }catch(Exception e){
            log.error("Exception while retrieving car sku attributes for given car sku id "+carSkuId+" and attrId "+attrId, e);
        }
        if(list == null){
            list = new ArrayList<CarSkuAttribute>();
        }
        return list;
    }
    
    /**
     * Method to retrieve attribute for a given Id.
     * 
     * @param attrId
     * @return
     */
    public Attribute getAttributeById(Long attrId) {        
        return (Attribute) getHibernateTemplate().get(Attribute.class, attrId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public VendorStyle getVendorStyleByGroupId(Long groupId) {
        List<VendorStyle> vendorStyles = getHibernateTemplate().find(
                "FROM VendorStyle vs where vs.groupId = ?", groupId);
        
        if (vendorStyles!=null && !vendorStyles.isEmpty()) {
            return vendorStyles.get(0);
        }
        return null;
    }

}
