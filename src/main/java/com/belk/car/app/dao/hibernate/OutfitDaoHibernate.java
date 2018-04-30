package com.belk.car.app.dao.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.belk.car.app.dao.OutfitDao;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeLookupValue;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CollectionSkus;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;

/**
 * 
 * @author AFUVXR1/ KARTHIK
 */
public class OutfitDaoHibernate extends UniversalDaoHibernate implements OutfitDao {

	
	public long getNextCarSequence(){
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		BigDecimal styleId= (BigDecimal) session.createSQLQuery("SELECT outfit_style_seq.nextval FROM dual").uniqueResult();
		return styleId.longValue();
	}
	@SuppressWarnings("unchecked")
	public List<Car> getAllOutfits() {
	
		return getHibernateTemplate().find("FROM Car WHERE sourceType.sourceTypeCd='OUTFIT' AND statusCd='ACTIVE' ");
	}
	@SuppressWarnings("unchecked")
	public List<Car> serachOutfitCars(String outfitName,String styleNumber) {
		
		final StringBuffer query = new StringBuffer("  SELECT c1.*   FROM car c1  WHERE car_id IN ( ");
					 query.append(" SELECT co.outfit_car_id   FROM   car c INNER JOIN car_outfit_child co ON  co.child_car_id= c.car_id ");
					 query.append(" INNER JOIN vendor_style vs  ON vs.vendor_style_id = c.vendor_style_id  WHERE 0=0 ");

		if (StringUtils.isNotBlank(outfitName)) {
			
			query.append(" AND LOWER(vendor_style_name)  LIKE LOWER('%"+outfitName +"%')  ");
		}
		
		if (StringUtils.isNotBlank(styleNumber)) {
			query.append(" AND  LOWER(vendor_style_number) LIKE LOWER('%"+styleNumber +"%')  ");
		}
		query.append(" AND co.status_cd='ACTIVE' ) AND c1.status_cd='ACTIVE' ");
		
		query.append("   UNION   SELECT c2.*  FROM car c2 INNER JOIN vendor_style vs2  ON  vs2.vendor_style_id = c2.vendor_style_id WHERE ");
		
		query.append("  c2.source_type_cd ='OUTFIT' ");
		
		if (StringUtils.isNotBlank(outfitName)) {
			
			query.append(" AND LOWER(vs2.vendor_style_name)  LIKE LOWER('%"+outfitName +"%')  ");
		}
		
		if (StringUtils.isNotBlank(styleNumber)) {
			
			query.append(" AND  LOWER(vs2.vendor_style_number) LIKE LOWER('%"+styleNumber +"%')  ");
		}
		query.append(" AND c2.status_cd='ACTIVE' ");
		
		List<Car> serachCarList=null;
		if (log.isDebugEnabled()) 
			log.debug("Search Query:"+query.toString());
		
		serachCarList = (List<Car>)getHibernateTemplate().execute(
							new HibernateCallback() {
								public Object doInHibernate(Session session) throws HibernateException {
									SQLQuery sq =session.createSQLQuery(query.toString()).addEntity(Car.class);
									return sq.list();
								}
							});
		return serachCarList;	
	}	
	@SuppressWarnings("unchecked")
	public Car getChildCarById(Long childCarId){
		//Check the childCarId is active AND not an outfit car
		List list = getHibernateTemplate().find("FROM Car WHERE statusCd='ACTIVE' AND carId =" + childCarId );
		Car car = null;
		if(list!=null && list.size()>0){
			car  = (Car) list.get(0);
		}
		return car;
	}
	public void removeObject(Object ob){
		getHibernateTemplate().saveOrUpdate(ob);
		getHibernateTemplate().flush();
	}
	
	public Object save(Object ob) {
		this.getHibernateTemplate().saveOrUpdate(ob);
		this.getHibernateTemplate().flush();
		return ob;
	}
	
	@SuppressWarnings("unchecked")
	public Attribute getAttributeByName(String attrName){
		List list = getHibernateTemplate().find("FROM Attribute WHERE name='" + attrName + "' AND statusCd='ACTIVE'");
		Attribute attr = null;
		if(list!=null && list.size()>0){
			attr  = (Attribute) list.get(0);
		}
		return attr;
	}
	
	@SuppressWarnings("unchecked")
	public CarAttribute findAttributeInCar(Car c, Attribute attr){
		Object[] arr= {c, attr};
		List list = getHibernateTemplate().find("FROM CarAttribute ca WHERE ca.car= ? AND ca.attribute= ? AND statusCd='ACTIVE'", arr);
		CarAttribute carAttr = null;
		if(list!=null && list.size()>0){
			carAttr  = (CarAttribute) list.get(0);
		}
		return carAttr;
	}
	
	@SuppressWarnings("unchecked")
	public CarAttribute getCarAttributeByAttributeName(Car c, String attributeName){
		Object[] arr= {c, attributeName};
		List list = getHibernateTemplate().find("FROM CarAttribute ca WHERE ca.car= ? AND ca.attribute.name=? AND ca.statusCd='ACTIVE'", arr);
		CarAttribute carAttr = null;
		if(list!=null && list.size()>0){
			carAttr  = (CarAttribute) list.get(0);
		}
		return carAttr;
	}
	
	@SuppressWarnings("unchecked")
	public CarAttribute getCarAttributeByBMName(Car c, String strBMName){
		Object[] arr= {c, strBMName};
		List list = getHibernateTemplate().find("FROM CarAttribute ca WHERE ca.car= ? AND ca.attribute.blueMartiniAttribute=? AND ca.statusCd='ACTIVE'", arr);
		CarAttribute carAttr = null;
		if(list!=null && list.size()>0){
			carAttr  = (CarAttribute) list.get(0);
		}
		return carAttr;
	}
	
	public Car getCarFromId(Long carId) {
		return (Car) this.getHibernateTemplate().get(Car.class, carId);
	}
	
	public boolean getBMProductValue(Car c){
		Attribute att=getAttributeByName("IS_BM_PRODUCT");
		CarAttribute catAtt=findAttributeInCar(c,att);
		if(catAtt !=null && "Yes".equals(catAtt.getAttrValue())){
			return true;
		}else {
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<CarAttribute> getAllOutfitCarAttributes(Car c){
		Object[] arr= {c};
		List list =  (List<CarAttribute>)getHibernateTemplate().find("FROM CarAttribute ca WHERE ca.car= ? AND ca.attribute.isOutfit='Y' AND ca.statusCd='ACTIVE'", arr);
		if(list==null){
			return new ArrayList<CarAttribute>();
		}
		return list;
	}
	
		
	@SuppressWarnings("unchecked")
	public VendorSku getVendorSku(long carId, String strBelkSku){
		
		Object[] bindData= {carId, strBelkSku};
		List list = getHibernateTemplate().find("FROM VendorSku vs WHERE vs.car.carId = ? AND vs.belkSku = ? AND vs.statusCd='ACTIVE'", bindData);
		VendorSku sku = null;
		if(list!=null && list.size()>0){
			sku  = (VendorSku) list.get(0);
		}
		return sku;
	}
	
	/**
	 * 
	 * This method used to get the vendor sku 
	 * from the VendorSku table corresponding to the car id  
	 * 
	 * @param car 	passing car object.
	 * @return 		It returns vendorsku object
	 */
	@SuppressWarnings("unchecked")
	public VendorSku getVendorSkus(Car car){
			
			Object[] bindData= {car};
		List list = getHibernateTemplate().find("FROM VendorSku vs WHERE vs.car.carId in(?) AND vs.statusCd='ACTIVE'", bindData);
		VendorSku sku = null;
		if(list!=null && list.size()>0){
			sku  = (VendorSku) list.get(0);
		}
		return sku;
	}

	@SuppressWarnings("unchecked")
	public String getAttributeValue(Car car, Attribute attrChildProducts) {
		Object[] bindData= {car, attrChildProducts};
		List<CarAttribute> list = (List<CarAttribute>)getHibernateTemplate().find("FROM CarAttribute ca WHERE ca.car= ? AND ca.attribute= ?", bindData);
		String childProductSKUs = null;
		if(list!=null && list.size()>0){
			CarAttribute ca  = (CarAttribute) list.get(0);
			childProductSKUs = ca.getAttrValue();
		}
		return childProductSKUs;
	}
	
	@SuppressWarnings("unchecked")
	public long getCarAttributeId(Car car,Attribute attrChildProducts) {
		Object[] bindData= {car, attrChildProducts};
		List<CarAttribute> list = (List<CarAttribute>)getHibernateTemplate().find("FROM CarAttribute ca WHERE ca.car= ? AND ca.attribute= ?", bindData);
		long childProductSKUs=0;
		if(list!=null && list.size()>0){
			CarAttribute ca  = (CarAttribute) list.get(0);
			childProductSKUs = ca.getCarAttrId();
		}
		return childProductSKUs;
	}

	@SuppressWarnings("unchecked")
	public Car getChildCarForRemove(Long childCarId){
		List list = getHibernateTemplate().find("FROM Car WHERE carId =" + childCarId );
		Car car = null;
		if(list!=null && list.size()>0){
			car  = (Car) list.get(0);
		}
		return car;
	}
	
	/**
	 * 
	 * This method used to get the Attribute Id 
	 * from ATTRIBUTE table based on the name as OUTFIT_Type.	
	 * 
	 * @return 	It returns AttributeId.
	 */
	public long getOutfitTypeId() {		
		Attribute attributeValue = null;
		Long atttributeId = null;
		List list = getHibernateTemplate().find("FROM Attribute WHERE name =\'OUTFIT_Type\'");
		Iterator itr = list.iterator();
		if (itr.hasNext()) {
			attributeValue = (Attribute) itr.next();
			atttributeId = (attributeValue != null ? attributeValue.getAttributeId() : null);
		}
		return (atttributeId != null ? atttributeId.longValue() : null);
	}
	
	/**
	 * 
	 * This method used to get attribute value
	 * for corresponding attribute id from ATTRIBUTE_LOOKUP_VALUE table.
	 * 
	 * @return 	template type list
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAttrTemplateType() {
		AttributeLookupValue attrLookupValue = null;
		List<String> attrList = new ArrayList();
		long atttributeId = getOutfitTypeId();
		List list = getHibernateTemplate().find("FROM AttributeLookupValue WHERE attrId =" + atttributeId);
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			attrLookupValue = (AttributeLookupValue) itr.next();
			attrList.add((attrLookupValue != null ? attrLookupValue.getValue(): null));
		}
		return attrList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Car> getChildCarSkus(VendorStyle venStyleId){
		Object[] bindData= {venStyleId};
		List<Car> car=  getHibernateTemplate().find("FROM Car WHERE archive <> 'T' AND vendorStyle =?", bindData );
		if(car.size() > 0){
			return car;
		}		
		return null;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CollectionSkus> getCollectionSkus(String productCode){
		Object[] bindData= {productCode};
		List<CollectionSkus> list=  getHibernateTemplate().find("FROM CollectionSkus WHERE prodCode =?", bindData );
		
		return list;
		
	}
	
	/**
	 * 
	 * This method used to remove the SKUs from COLLECTION_SKUS table. 
	 * 
	 * @param productCode 	passing product code.
	 * 
	 */
	public void removeCollectionSkus(String productCode) {
		try {
			StringBuffer buffer = new StringBuffer("DELETE FROM COLLECTION_SKUS COLL");
			buffer.append(" WHERE COLL.PROD_CODE=:PRODUCT_CD");
			SessionFactory sf = getHibernateTemplate().getSessionFactory();
			Session session = sf.getCurrentSession();

			try {
				SQLQuery query = session.createSQLQuery(buffer.toString());
				query.setString("PRODUCT_CD", productCode);
				query.executeUpdate();
				getHibernateTemplate().flush();
				

			}
			catch (Exception e) {
				log.error("Exception deleting from header table", e);
			}

		}
		catch (DataAccessException de) {
			log.debug("Problem saving header");
			de.printStackTrace();
		}
	}
	
}
