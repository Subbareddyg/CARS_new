
package com.belk.car.app.dao.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.appfuse.model.User;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.belk.car.app.dao.DropshipDao;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarImage;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.Image;
import com.belk.car.app.model.ImageType;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.oma.FulfillmentServiceVendor;
import com.belk.car.app.model.oma.ItemRequestWorkflow;
import com.belk.car.app.model.oma.VFIRStyle;
import com.belk.car.app.model.oma.VFIRStyleSku;
import com.belk.car.app.model.vendorcatalog.MasterAttributeMapping;
import com.belk.car.app.model.vendorcatalog.VendorCatalog;
import com.belk.car.app.model.vendorcatalog.VendorCatalogFieldDataMapping;
import com.belk.car.app.model.vendorcatalog.VendorCatalogFieldMapping;
import com.belk.car.app.model.vendorcatalog.VendorCatalogHeader;
import com.belk.car.app.model.vendorcatalog.VendorCatalogImage;
import com.belk.car.app.model.vendorcatalog.VendorCatalogImport;
import com.belk.car.app.model.vendorcatalog.VendorCatalogNote;
import com.belk.car.app.model.vendorcatalog.VendorCatalogRecord;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSku;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSkuId;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSkuImage;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSkuMaster;
import com.belk.car.app.to.IdbDropshipDataTO;

/**
 * Implementation of interface DropshipDao.
 * Implements all the methids used in Job 17 and 18
 * 
 * @author afusy45
 */
/**
 * @author afusy07
 */
public class DropshipDaoHibernate extends UniversalDaoHibernate implements DropshipDao {

	/**
	 * Method to get value associated value for ID in DB
	 * 
	 * @param cls,id
	 * @return Object
	 * @see com.belk.car.app.dao.DropshipDao#getById(java.lang.Class,
	 *      java.io.Serializable)
	 */
	public Object getById(Class cls, Serializable id) {
		return this.getHibernateTemplate().get(cls, id);
	}

	/**
	 * Method to save IDB Feed to DB
	 * 
	 * @param ob
	 */
	@SuppressWarnings("unchecked")
	public void saveFeed(List<IdbDropshipDataTO> ob) {
		List newList = new ArrayList<IdbDropshipDataTO>();
		try {
			
			if (ob != null && !ob.isEmpty()) {
				java.util.Iterator i = ob.iterator();
				while(i.hasNext()){
					
					IdbDropshipDataTO idbdata = (IdbDropshipDataTO) i.next();
					IdbDropshipDataTO temp =getVendorUPCobject(idbdata.getVndrUPC());
					//temp.setVndrUPC(idbdata.getVndrUPC());
					temp.setClassName(idbdata.getClassName());
					temp.setClassNum(Long.valueOf(idbdata.getClassNum()));
					temp.setDeptName(idbdata.getDeptName());
					temp.setDeptNum(Long.valueOf(idbdata.getDeptNum()));
					temp.setVendorName(idbdata.getVendorName());
					temp.setVendorNumber(idbdata.getVendorNumber());
					temp.setVendorStyleID(idbdata.getVendorStyleID());
					temp.setBelkUPC(idbdata.getBelkUPC());
					temp.setStyleDesc(idbdata.getStyleDesc());
					temp.setVendorColorCode(idbdata.getVendorColorCode());
					temp.setColorDesc(idbdata.getColorDesc());
					temp.setVndrSizeCode(idbdata.getVndrSizeCode());
					temp.setVndrSize(idbdata.getVndrSize());
					temp.setVndrUPC(idbdata.getVndrUPC());
					temp.setUPCAddDate(idbdata.getUPCAddDate());
					temp.setIDBUPCUnitPrice(Double.valueOf(idbdata.getIDBUPCUnitPrice()));
					temp.setIDBUPCUnitCost(Double.valueOf(idbdata.getIDBUPCUnitCost()));
					temp.setIDBStyleUnitPrice(Double.valueOf(idbdata.getIDBStyleUnitPrice()));
					temp.setIDBStyleUnitCost(Double.valueOf(idbdata.getIDBStyleUnitCost()));
					temp.setSizeChartCode(idbdata.getSizeChartCode());
					temp.setStyleDropshipFlag(idbdata.getStyleDropshipFlag());
					temp.setSKUDropshipFlag(idbdata.getSKUDropshipFlag());
					temp.setDropshipFlagUpdated(idbdata.getDropshipFlagUpdated());
					temp.setNotOrderableFlag(idbdata.getNotOrderableFlag());
					temp.setUPCDiscountedFlag(idbdata.getUPCDiscountedFlag());
					temp.setUPCDiscountedDate(idbdata.getUPCDiscountedDate());
					temp.setCreatedBy("IDB Feed");
					temp.setCreatedDate(new Date());
					temp.setUpdatedBy("IDB Feed");
					temp.setUpdatedDate(new Date());
					//temp.setIdbDropshipDataTO(getVendorUPCobject(idbdata.getVndrUPC()));
					
				if(null!= temp){
					newList.add(temp);
				}else{
					newList.add(idbdata);
				}
			}//end while
			}
			
		}
			catch (DataAccessException de) {
				log.error("Exception setting data in temp"+de);
			}
			try{
			getHibernateTemplate().saveOrUpdateAll(newList);
			getHibernateTemplate().flush();
			}catch(DataAccessException dae){
				log.error("Error saving feed"+dae);
			}

	}
		
		/**
		 * Method to get IdbDropshipDataTo object by VendorUPC
		 * @author afusy45
		 * @param vendorUPC
		 * @return IdbDropshipDataTO
		 */
		public IdbDropshipDataTO getVendorUPCobject(String vendorUPC){
			IdbDropshipDataTO idbData = new IdbDropshipDataTO(); 
				List list = getHibernateTemplate().find("FROM IdbDropshipDataTO WHERE vndrUPC=?", vendorUPC);
				if(list.size()>0){
					idbData = (IdbDropshipDataTO) list.get(0);
				}
			return idbData;
		}
		
	/**
	 * Method to save Item request list to Item Request Table
	 * @author afusy45
	 * @param List
	 */
	public void saveItemRequestList(List itemRequestObjects){
		getHibernateTemplate().saveOrUpdateAll(itemRequestObjects);
		getHibernateTemplate().flush();
	}

	/**
	 * Returns list of all vendor upcs
	 * 
	 * @return List
	 * @param vendorUPCs
	 */
	@SuppressWarnings("unchecked")
	public List<IdbDropshipDataTO> getVendorUpcs(Set vendorUPCsSet) {
		ArrayList<Object> values = new ArrayList<Object>();
		StringBuffer buffer = new StringBuffer();

		/*Since more than 1000 VendorUpcs are possible in feed but not in "in" query 
		 * have to break records in multiples of 1000 and join by "OR" keyword*/

		int orderCount=0;
		java.util.Iterator i = vendorUPCsSet.iterator();
		StringBuilder result = new StringBuilder();
		buffer.append("FROM IdbDropshipDataTO WHERE");
		buffer.append(" vndrUPC ");
		buffer.append(" in (");

		buffer.append("'");
		buffer.append(i.next());
		//values.add(result);
		buffer.append("'");

		orderCount++;
		while (i.hasNext()) {

			if(orderCount%999==0){
				buffer.append(",").append("'");
				buffer.append(i.next().toString());
				//values.add(result);
				buffer.append(
				"'");


				buffer.append(")");

				if(i.hasNext()){	
					buffer.append("OR vndrUPC in (");
					buffer.append("'");
					buffer.append(i.next().toString());
					//values.add(result);
					buffer.append(
					"'");
					orderCount = 1;
				}else{
					buffer.append(
					")");
				}

			}else{

				buffer.append(",").append("'");
				buffer.append(i.next().toString());
				//values.add(result);
				buffer.append(
				"'");

				orderCount++;

			}



		}
		buffer.append(")");
		List<IdbDropshipDataTO> idbList = (List<IdbDropshipDataTO>) getHibernateTemplate().find(buffer.toString());
		getHibernateTemplate().flush();
		return idbList;

	}

	/**
	 * Method to save skus to style sku table
	 * @author afusy45
	 * @param styleSku VFIRStyleSku
	 */
	public void uploadSKUs(VFIRStyleSku styleSku) {
		this.getHibernateTemplate().merge(styleSku);
	}

	/**
	 * Method to save style to Style table
	 * @author afusy45
	 * @param style VFIRStyle
	 */
	public void uploadStyles(VFIRStyle style){
		this.getHibernateTemplate().merge(style);
	}

	/**
	 * Method to save data in VIFRWorkflowHist table
	 */
	public void saveWorkflowHist(ItemRequestWorkflow itemRequestWorkflow) {
		this.getHibernateTemplate().saveOrUpdate(itemRequestWorkflow);
        this.getHibernateTemplate().flush();
        this.getHibernateTemplate().clear();
	}

	/**
	 * Method to create new Vendor
	 * 
	 * @param vendor object
	 * @see com.belk.car.app.dao.DropshipDao#getFulfillmentServiceIdForVenID(java.lang.Long)
	 */
	public void createVendor(Vendor vendor) {
		this.getHibernateTemplate().saveOrUpdate(vendor);
        this.getHibernateTemplate().flush();
        this.getHibernateTemplate().clear();
	}

	/**
	 * Method to get list of fulfillment service IDs associated to vendor ID
	 * 
	 * @return List
	 * @param vendorID
	 */
	public List<FulfillmentServiceVendor> getFulfillmentServiceIdForVenID(Long vendorId) {

		List<FulfillmentServiceVendor> list = getHibernateTemplate().find(
				"FROM FulfillmentServiceVendor WHERE vendorID =?", vendorId);

		return list;


	}
	//Commented following method, as it may get used if departments associated with catalog are required.
	/**
	 * Method to get list of departments associated to vendor catalog
	 * @author afusy45
	 * @param Long
	 * @return List
	 *//*
	public List<VendorCatalogDepartment> getDeptForCatalogID(Long catalogID){

		VendorCatalog vendorCatalog = this.getCatalogForId(catalogID);
		List<VendorCatalogDepartment> list = getHibernateTemplate().find(
				"FROM VendorCatalogDepartment WHERE compositeKeyForVndrCatlDept.vendorCatalog=?", vendorCatalog);
		if (!list.isEmpty() && list.size() > 0) {
			return list;
		}
		return null;
	}*/

	/**
	 * Method to change status of catalog file to datamapping
	 * @author afusy45
	 * @param VendorCatalog object
	 * 
	 */
	public void changeStatusOfCatalogFile(VendorCatalog catalog){
		getHibernateTemplate().merge(catalog);
	}

	/**
	 * Method to get Buyer email IDs responsible for the departments associated to catalog ID
	 * @author afusy45
	 * @param Long
	 * @return List
	 */
	public List getBuyerMailIDs(Long catalogID){
		StringBuffer buffer = new StringBuffer("SELECT DISTINCT EMAIL_ADDR FROM car_user cu ");
		buffer.append(" inner join car_user_department cud ON cu.CAR_USER_ID=cud.CAR_USER_ID ");
		buffer.append(" inner join vendor_catalog_dept vcd ON cud.dept_id = vcd.dept_id ");
		buffer.append(" WHERE ");
		buffer.append("vcd.VENDOR_CATALOG_ID =:CATALOG_ID" );
		buffer.append(" AND ");
		buffer.append("cu.user_type_cd='BUYER'");
		Session session = getHibernateTemplate().getSessionFactory()
		.getCurrentSession();
		SQLQuery query = session.createSQLQuery(buffer.toString());
		query.setLong("CATALOG_ID", catalogID);
		return  query.list();


	}

	/**
	 * @param status
	 * @return List<VendorCatalog>
	 * @TODO Get catalogs with status-translating
	 */
	public List<VendorCatalog> getCatalogsWithStatus(String status) {

		List<VendorCatalog> vendorCatalogs = getHibernateTemplate().find(
				"FROM VendorCatalog WHERE statusCD=?", status);
		return vendorCatalogs;
	}


	/**
	 * @param templateId
	 * @param catalogId
	 * @param attrId1
	 * @param attrId2
	 * @param attrId3
	 * @return List<VendorCatalogRecord>
	 * @TODO To get records ordered by order number , given the attribute IDs
	 *       from MASTER_ATTRIBUTE_MAPPING table
	 */
	public List<VendorCatalogRecord> getRecordsForStandardAttributes(
			long templateId, long catalogId, long attrId1, long attrId2, long attrId3) {

		List<VendorCatalogRecord> recordList = new ArrayList<VendorCatalogRecord>();
		List<VendorCatalogRecord> recordListTotal = new ArrayList<VendorCatalogRecord>();

		StringBuffer queryBuf = new StringBuffer("select r.* ");

		queryBuf
		.append(" from vendor_catalog_record r ")
		.append(" inner join vendor_catalog_header h on r.vndr_catl_hdr_fld_num = h.vndr_catl_hdr_fld_num ")
		.append(" inner join master_attribute_mapping m on m.vndr_catl_hdr_fld_name = h.vndr_catl_hdr_fld_name ")
		.append(" where m.catalog_master_attr_id IN(:ATTR_ID1,:ATTR_ID2,:ATTR_ID3)")
		.append(" and m.catalog_template_id=:TEMPLATE_ID")
		.append(" and h.vendor_catalog_id=:CATALOG_ID1")
		.append(" and r.vendor_catalog_id=:CATALOG_ID2")
		.append(" order by r.VNDR_CATL_RECORD_NUM");

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();

		SQLQuery query = session.createSQLQuery(queryBuf.toString()).addEntity(
				VendorCatalogRecord.class);
		query.setLong("ATTR_ID1", attrId1);
		query.setLong("ATTR_ID2", attrId2);
		query.setLong("ATTR_ID3", attrId3);
		query.setLong("TEMPLATE_ID", templateId);
		query.setLong("CATALOG_ID1", catalogId);
		query.setLong("CATALOG_ID2", catalogId);
		recordList = query.list();
		if (recordList != null && !recordList.isEmpty()) {
			recordListTotal.addAll(recordList);
		}

		if (null != recordListTotal) {
			if (log.isDebugEnabled()) {
				log.debug("sixe of record list=" + recordListTotal.size());
			}

		}

		return recordList;

	}



	/**
	 * @param attrId
	 * @param templateId
	 * @param catalogId
	 * @return VendorCatalogHeader
	 * @TODO Get the header for attribute in MASTER_ATTRIBUTE_MAPPING table
	 */
	public VendorCatalogHeader getMappingForMasterAttribute(
			long attrId, long templateId, long catalogId) {

		ArrayList<Object> values = new ArrayList<Object>();
		List<MasterAttributeMapping> fieldMappingList = new ArrayList<MasterAttributeMapping>();
		List<VendorCatalogHeader> headerList = null;
		VendorCatalogHeader header = null;
		MasterAttributeMapping fieldMapping = null;
		StringBuffer sqlB = new StringBuffer("from MasterAttributeMapping map ");
		sqlB.append("where map.catalogTemplateId=?");
		sqlB.append(" and map.catalogMasterAttrId=?");
		sqlB.append(" and map.status='ACTIVE' ");
		values.add(templateId);
		values.add(attrId);
		fieldMappingList = getHibernateTemplate().find(sqlB.toString(), values.toArray());
		if (fieldMappingList != null && !fieldMappingList.isEmpty()) {
			fieldMapping = fieldMappingList.get(0);
			if (log.isDebugEnabled()) {
				log.debug("fieldMappingList size=" + fieldMappingList.size());
			}
		}
		if (fieldMapping != null) {
			if (log.isDebugEnabled()) {
				log.debug("fieldMapping  getCatalogMasterAttrId="
						+ fieldMapping.getCatalogMasterAttrId());
			}
			String fieldName = fieldMapping.getCatalogHeaderFieldName();
			if (log.isDebugEnabled()) {
				log.debug("fieldName=" + fieldName);
			}
			sqlB = new StringBuffer("from VendorCatalogHeader h ");
			sqlB.append("where h.vendorCatalogID.vendorCatalogID=?");
			sqlB.append(" and h.vendorCatalogHeaderFieldName=?");
			values = new ArrayList<Object>();
			values.add(catalogId);
			values.add(fieldName);
			headerList = getHibernateTemplate().find(sqlB.toString(), values.toArray());

		}
		VendorCatalogHeader header1=null;
		if (null != headerList && !headerList.isEmpty()) {
			if (log.isDebugEnabled()) {
				log.debug("headerList size=" + headerList.size());
			}
			header1= headerList.get(0);
		}
		else if (log.isDebugEnabled()) {
			log.debug("No headers found. Returning null");
		}
		return header1;

	}

	/**
	 * @param styleSkuList
	 * @TODO To save the style sku list in database
	 */
	public void saveStyleSkus(List<VendorCatalogStyleSku> styleSkuList) throws Exception {

		getHibernateTemplate().saveOrUpdateAll(styleSkuList);
		getHibernateTemplate().flush();
	}



	/**
	 * Retrieve all of the Attribute objects associated with the given CAR
	 * 
	 * @param carID
	 * @return a list of Attribute objects
	 */
	public List<Attribute> getAttributesforCar(long carId) {

		StringBuffer queryBuf = new StringBuffer();
		queryBuf.append(" SELECT * FROM Attribute WHERE attr_id in (");
		queryBuf.append(" SELECT DISTINCT(ca.attr_id) FROM class_attribute  ca");
		queryBuf.append(" WHERE ca.class_id IN (");
		queryBuf.append(" SELECT style.class_id  FROM vendor_style style");
		queryBuf.append(" where   style.vendor_style_id IN(");
		queryBuf.append(" SELECT DISTINCT(vendor_style_id) FROM vendor_sku");
		queryBuf.append(" WHERE car_id=:CAR_ID1").append(")");
		queryBuf.append(" )");
		queryBuf.append(" UNION ");

		queryBuf.append(" SELECT DISTINCT(pa.attr_id) FROM product_type_attribute  pa      ");
		queryBuf.append(" WHERE pa.product_type_id IN (");
		queryBuf.append(" SELECT style.product_type_id  FROM vendor_style style");
		queryBuf.append(" where   style.vendor_style_id IN(");
		queryBuf.append(" SELECT vendor_style_id FROM CAR");
		queryBuf.append(" WHERE car_id=:CAR_ID2").append(")");
		queryBuf.append(" )");
		queryBuf.append(" UNION ");

		queryBuf.append(" SELECT DISTINCT(d.attr_id) FROM department_attribute   d  ");
		queryBuf.append(" WHERE d.dept_id = (");
		queryBuf.append(" SELECT dept_id FROM CAR ");
		queryBuf.append(" WHERE car_id=:CAR_ID3").append(")");
		queryBuf.append(" )");

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		SQLQuery query = session.createSQLQuery(queryBuf.toString()).addEntity(
				Attribute.class);
		query.setLong("CAR_ID1", carId);
		query.setLong("CAR_ID2", carId);
		query.setLong("CAR_ID3", carId);

		List<Attribute> attrList = query.list();

		return attrList;
		// return getHibernateTemplate().find(queryBuf.toString());

	}

	/**
	 * @param vendorStyleNumber
	 * @return List<VendorCatalogStyleSkuMaster>
	 * @TODO To get the master recirds from master table for a vendor style
	 */
	public List<VendorCatalogStyleSkuMaster> getMasterRecordsForStyle(
			String vendorStyleNumber) {
		StringBuffer buffer = new StringBuffer("from VendorCatalogStyleSkuMaster v");
		if(vendorStyleNumber!=null){
			buffer.append(" where v.compositeKey.vendorStyleId=?");
		}
		buffer.append(" AND v.status = 'ACTIVE'");
		return getHibernateTemplate().find(buffer.toString(), vendorStyleNumber);

	}



	/**
	 * @param compositeKey
	 * @return VendorCatalogStyleSku
	 * @TODO To get the style sku based on the composite ID
	 */
	public VendorCatalogStyleSku getStyleSkuForMaster(VendorCatalogStyleSkuId compositeKey) {

		return (VendorCatalogStyleSku) getHibernateTemplate().get(VendorCatalogStyleSku.class,
				compositeKey);
	}

	/**
	 * @param recordNum
	 * @param vendorCatalogId
	 * @return List<VendorCatalogRecord>
	 * @TODO - To get the records from record table with a given record number
	 */
	public List<VendorCatalogRecord> getAllRecordsWithRecordNum(long recordNum, long vendorCatalogId) {

		StringBuffer buffer = new StringBuffer("from VendorCatalogRecord v");
		buffer.append(" where v.compositeKey.catalogId=?");
		buffer.append("and v.compositeKey.recordNumber=?");
		ArrayList<Object> values = new ArrayList<Object>();
		values.add(vendorCatalogId);
		values.add(recordNum);
		return getHibernateTemplate().find(buffer.toString(), values.toArray());
	}

	/**
	 * @param recordModel
	 * @param templateId
	 * @return Attribute 
	 * @TODO To get teh attribute for  a record 
	 */
	public String getAttributeForRecord(VendorCatalogRecord recordModel, long templateId) {

		Long catalogId = recordModel.getCompositeKey().getCatalogId();
		Long headerNum = recordModel.getCompositeKey().getHeaderNum();
		StringBuffer buffer = new StringBuffer("SELECT m.* FROM vendor_catalog_fld_mapping m");
		buffer.append(" WHERE m.vendor_catalog_tmpl_id=:TEMPLATE_ID");
		buffer.append(" AND m.vndr_catl_hdr_fld_name=(");
		buffer.append(" SELECT vndr_catl_hdr_fld_name FROM vendor_catalog_header h");
		buffer.append(" WHERE h.vendor_catalog_id=:CATALOG_ID");
		buffer.append(" AND h.vndr_catl_hdr_fld_num=:HEADER_NUM").append(")");

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();

		List<VendorCatalogFieldMapping> recordList = null;

		SQLQuery query = session.createSQLQuery(buffer.toString()).addEntity(
				VendorCatalogFieldMapping.class);
		query.setLong("TEMPLATE_ID", templateId);
		query.setLong("CATALOG_ID", catalogId);
		query.setLong("HEADER_NUM", headerNum);
		
		recordList = query.list();
		String attr=null;
		if (recordList != null && !recordList.isEmpty()) {
			attr=recordList.get(0).getBlueMartiniAttrName();
		}

		return attr;
	}

	/**
	 * Method to get Vendor catalog object for catalog ID
	 * @author afusy45
	 */
	public VendorCatalog getCatalogForId(Long vendorCatalogId) {
		return (VendorCatalog) getHibernateTemplate().get(VendorCatalog.class, vendorCatalogId);
	}

	/**
	 * @param carId
	 * @return List<VendorSku>
	 * @TODO - To get the vendor skus for a car from vendor sku table.
	 */
	public List<VendorSku> getVendorSku(Long carId) {
		StringBuffer buffer = new StringBuffer("from VendorSku v");
		buffer.append(" where v.car.carId=?");
		ArrayList<Object> values = new ArrayList<Object>();
		values.add(carId);
		List<VendorSku> list = getHibernateTemplate().find(buffer.toString(), values.toArray());
		return list;

	}

	/**
	 * @param attributeIdFromMapping
	 * @return Attribute
	 * @TODO To get the attribute based on the attribute ID
	 */
	public Attribute getAttributeByID(String attributeIdFromMapping) {

		return (Attribute) getHibernateTemplate().get(Attribute.class, attributeIdFromMapping);
	}

	/**
	 * @param carSkuAttribute
	 * @TODO To save the car sku attribute
	 */
	public void saveOrUpdateCarSkuAttribute(CarSkuAttribute carSkuAttribute) {
		getHibernateTemplate().saveOrUpdate(carSkuAttribute);
		getHibernateTemplate().flush();
	}

	/*
	 * Method added by Priyanka Gadia
	 *  Retrieve the mapped record value for the given record
	 * @param: tempalte id
	 * @param: record models
	 * @return data field mapping
	 */
	public VendorCatalogFieldDataMapping getVendorCatalogFieldDataMapping(
			long templateIdMaster, VendorCatalogRecord recordModel) {
		Long catalogId = recordModel.getCompositeKey().getCatalogId();
		Long headerNum = recordModel.getCompositeKey().getHeaderNum();
		String recordVal=recordModel.getVendorCatalogFieldValue();
		//Added the code to replace all the single quotes to double single quotes so that it does not affect the quieries.
		if(null !=recordModel &&  recordModel.getVendorCatalogFieldValue()!=null){
			Pattern pattern = Pattern.compile("[']");
			
			Matcher match=pattern.matcher(recordModel.getVendorCatalogFieldValue());
			while (match.find()) {
				// Get the matching string
				recordVal = match.replaceAll("''");

			}
		}


		StringBuffer buffer = new StringBuffer("SELECT m.* FROM vndr_catl_fld_data_mapping m");
		buffer.append(" WHERE m.vendor_catalog_tmpl_id=:TEMPLATE_ID");
		buffer.append(" AND m.vndr_catl_field_value=:RECORD_VAL");
		buffer.append(" AND m.vndr_catl_hdr_fld_name=(");
		buffer.append(" SELECT vndr_catl_hdr_fld_name FROM vendor_catalog_header h");
		buffer.append(" WHERE h.vendor_catalog_id=:CATALOG_ID");
		buffer.append(" AND h.vndr_catl_hdr_fld_num=:HEADER_NUM").append(")");

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();

		List<VendorCatalogFieldDataMapping> mappingList = null;

		SQLQuery query =  session.createSQLQuery(buffer.toString()).addEntity(
				VendorCatalogFieldDataMapping.class);
		query.setLong("TEMPLATE_ID", templateIdMaster);
		query.setString("RECORD_VAL", recordVal);
		query.setLong("CATALOG_ID", catalogId);
		query.setLong("HEADER_NUM", headerNum);
		
		mappingList = query.list();
		VendorCatalogFieldDataMapping map=null;
		if (null != mappingList && !mappingList.isEmpty()) {
			map= mappingList.get(0);
		}

		return map;
	}

	/**
	 * @param carSkuId
	 * @param attributeIdFromMapping
	 * @return CarSkuAttribute
	 * @throws ClassCastException
	 * @TODO To get the car sku attributes based on the attribute and car sku id 
	 */
	public CarSkuAttribute getCarSkuAttribute(Long carSkuId, String attributeIdFromMapping) throws ClassCastException {

		StringBuffer buffer = new StringBuffer("from CarSkuAttribute c");
		buffer.append(" where c.vendorSku.carSkuId="+carSkuId);
		buffer.append("and c.attribute.attributeId="+attributeIdFromMapping);
		
		List<CarSkuAttribute> list=getHibernateTemplate().find(buffer.toString());
		if(list!=null && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	/**
	 * Method to save errors to vendor catalog note table
	 */
	public void saveVendorCatalogNote(List<VendorCatalogNote> catalogNoteList) {
		try {
			this.getHibernateTemplate().saveOrUpdateAll(catalogNoteList);
			getHibernateTemplate().flush();
		}
		catch (DataAccessException de) {
			de.printStackTrace();
		}
	}

	/**
	 * Method to save header fields in Vendor Catalog Header table
	 */
	public void saveHeader(List<VendorCatalogHeader> headerList) {
		try {
			VendorCatalogHeader catalogHeader = new VendorCatalogHeader();
			catalogHeader = headerList.get(0);
			Long catalogID = catalogHeader.getVendorCatalogID().getVendorCatalogID();

			StringBuffer buffer = new StringBuffer("DELETE FROM vendor_catalog_header vch");
			buffer.append(" WHERE vch.VENDOR_CATALOG_ID=:CATALOG_ID");
			SessionFactory sf = getHibernateTemplate().getSessionFactory();
			Session session = sf.getCurrentSession();

			List<VendorCatalogHeader> headerListAfterDelete = null;
			try {
				SQLQuery query = session.createSQLQuery(buffer.toString());
				query.setLong("CATALOG_ID", catalogID);
				query.executeUpdate();

			}
			catch (Exception e) {
				log.error("Exception deleting from header table", e);
			}

			getHibernateTemplate().saveOrUpdateAll(headerList);
			getHibernateTemplate().flush();
		}
		catch (DataAccessException de) {
			log.debug("Problem saving header");
			de.printStackTrace();
		}
	}

	/**
	 * Method to save record values in vendor catalog record table
	 */
	public void saveRecord(List<VendorCatalogRecord> recordList) {
		getHibernateTemplate().saveOrUpdateAll(recordList);
		getHibernateTemplate().flush();
	}

	/**
	 * Method to delete record for vendor catalog ID from record table
	 * 
	 * @param vendorCatalogID
	 */
	public void deleteRecord(Long vendorCatalogID) {
		StringBuffer buffer = new StringBuffer("DELETE FROM vendor_catalog_record vch");
		buffer.append(" WHERE vch.VENDOR_CATALOG_ID=:CATALOG_ID");
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();

		try {
			SQLQuery query = session.createSQLQuery(buffer.toString());
			query.setLong("CATALOG_ID", vendorCatalogID);
			query.executeUpdate();
		}
		catch (Exception e) {
			log.info("Exception deleting from record table", e);
		}
	}

	/**
	 * Method to save images
	 * 
	 * @param vendorCatalogImage object
	 */
	public void saveImageData(List<VendorCatalogImage> vendorCatalogImageList) {

		getHibernateTemplate().saveOrUpdateAll(vendorCatalogImageList);
		getHibernateTemplate().flush();
	}

	/**
	 * @param vendorCatalogID
	 * @return VendorCatalogImport
	 * @TODO To get import information for vendor catalog
	 */
	public VendorCatalogImport getVendorCatalogImportForCatalog(Long vendorCatalogID) {
		StringBuffer buffer = new StringBuffer("from VendorCatalogImport v");
		buffer.append(" where v.vendorCatalogID.vendorCatalogID=?");
		List<VendorCatalogImport> catlImport = getHibernateTemplate().find(buffer.toString(),
				vendorCatalogID);
		VendorCatalogImport importCatl=null;
		if (null != catlImport && !catlImport.isEmpty()) {
			importCatl= catlImport.get(0);
		}
		return importCatl;
	}



	/**
	 * @param vendorCatalogID
	 * @return List of VendorCatalogStyleSku
	 * @TODO Method to fetch records form VENDOR_CATALOG_STY_SKU Used while
	 *       filling VNDR_CATL_STY_SKU_MAST table
	 */
	public List<VendorCatalogStyleSku> getStyelSkusWithCatalogId(Long vendorCatalogID) {
		StringBuffer buffer = new StringBuffer("from VendorCatalogStyleSku v");
		buffer.append(" where v.compositeKey.vendorCatalogId=?");
		return (List<VendorCatalogStyleSku>) getHibernateTemplate().find(buffer.toString(),
				vendorCatalogID);
	}

	/**
	 * @param VendorCatalogStyleSkuMaster List
	 * @TODO Method to save MASTER records from VNDR_CATL_STY_SKU_MAST table
	 *       Used while filling VNDR_CATL_STY_SKU_MAST table .
	 */
	public void saveOrUpdateMasterList(List<VendorCatalogStyleSkuMaster> tempMasterList) {

		this.getHibernateTemplate().saveOrUpdateAll(tempMasterList);
		getHibernateTemplate().flush();
	}



	/**
	 * @return List<VendorCatalogStyleSkuMaster>
	 *  Method to fetch MASTER records from VNDR_CATL_STY_SKU_MAST table
	 *       Used while filling VNDR_CATL_STY_SKU_MAST table using overwrite
	 *       action
	 */
	public List<VendorCatalogStyleSkuMaster> getAllMasterRecords(long vendorId) {
                StringBuffer buffer = new StringBuffer("from VendorCatalogStyleSkuMaster v");
		buffer.append(" where v.compositeKey.vendorId=?");
		buffer.append(" AND v.status = 'ACTIVE'");
		return getHibernateTemplate().find(buffer.toString(), vendorId);
	}

	/**
	 * @param styleSkuImageModel
	 * @TODO Method to SAVE images from VNDR_CATL_STY_SKU_IMAGE table Used while
	 *       filling VNDR_CATL_STY_SKU_IMAGE table
	 */
	public void saveImageForStyleSku(VendorCatalogStyleSkuImage styleSkuImageModel) {
		this.getHibernateTemplate().save(styleSkuImageModel);
        this.getHibernateTemplate().flush();
	}

	/**
	 * @param master
	 * @return List<VendorCatalogStyleSkuImage>
	 * @TODO Method to fetch images from VNDR_CATL_STY_SKU_IMAGE table for given
	 *       catalog, style and sku
	 */
	public List<VendorCatalogStyleSkuImage> getImagesForStyleSku(VendorCatalogStyleSkuMaster master) throws ClassCastException {
		StringBuffer buffer = new StringBuffer("from VendorCatalogStyleSkuImage i");
		buffer.append(" where i.vendorCatalogId=?");
		buffer.append("and i.vendorStyleId=?");
		buffer.append("and i.vendorUpc=?");
		buffer.append("and i.status='ACTIVE'");
		List<Object> values = new ArrayList<Object>();
		values.add(master.getVendorCatalogId());
		values.add(master.getCompositeKey().getVendorStyleId());
		values.add(master.getCompositeKey().getVendorUPC());
		return (List<VendorCatalogStyleSkuImage>) getHibernateTemplate().find(buffer.toString(),
				values.toArray());

	}

	/**
	 * @param imagesExisting
	 * @TODO Method to disable existing images from VNDR_CATL_STY_SKU_IMAGE
	 *       table Used while filling VNDR_CATL_STY_SKU_IMAGE table
	 */
	public void deleteImages(List<VendorCatalogStyleSkuImage> imagesExisting) {

		getHibernateTemplate().saveOrUpdateAll(imagesExisting);
		getHibernateTemplate().flush();
	}

	/**
	 * @param catalogId
	 * @param headerList
	 * @return List<VendorCatalogRecord>
	 * @TODO Method to get records for the header numbers Used while filling
	 *       VENDOR_CATALOG_STY_SKU table
	 */
	public List<VendorCatalogRecord> getRecordsForHeaderNums(long catalogId, List<Long> headerList) {
		List<VendorCatalogRecord> recordList = new ArrayList<VendorCatalogRecord>();

		StringBuffer queryBuf = new StringBuffer("select r.* ");

		queryBuf.append(" from vendor_catalog_record r ").append(" where").append(
		" r.vndr_catl_hdr_fld_num IN(");
		Iterator<Long> itr = headerList.iterator();
		while (itr.hasNext()) {
			Long headerNum = itr.next();
			queryBuf.append(headerNum);
			if (itr.hasNext()) {
				queryBuf.append(",");

			}
		}

		queryBuf.append(" ) and ").append("r.vendor_catalog_id=").append(catalogId);

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();

		recordList = session.createSQLQuery(queryBuf.toString()).addEntity(
				VendorCatalogRecord.class).list();

		return recordList;
	}

	/* Method to get Vendor Catalog Info for Catlog Id and Catalog Name */
	public List<VendorCatalogImport> getVendorCatalogImportInfo(Long catalogId) {

		VendorCatalog vendorCatalog = new VendorCatalog();
		vendorCatalog.setVendorCatalogID(catalogId);

		StringBuffer sqlB = new StringBuffer("from VendorCatalogImport vci where vendorCatalogID="
				+ vendorCatalog.getVendorCatalogID());

		return getHibernateTemplate().find(sqlB.toString());

	}

	/**
	 * @return Map<Car, Set<VendorSku>>
	 * @TODO Method to get skus for CAR with in-progress status Used while
	 *       saving car images from catalog
	 */
	public Map<Car, Set<VendorSku>> getCarAndSkus() {
		StringBuffer buffer = new StringBuffer("from Car ca where ca.currentWorkFlowStatus = 'INITIATED' and ca.statusCd= 'ACTIVE'" +
		" and ca.vendorStyle.vendorStyleNumber in (select v.compositeKey.vendorStyleId from  VendorCatalogStyleSkuMaster  v )");
		List<Car> carList = getHibernateTemplate().find(buffer.toString());

		Map<Car, Set<VendorSku>> carSkuMap = new HashMap<Car, Set<VendorSku>>();
		// Construct a map for car and vendor skus for that car
		for (Car car : carList) {
			Set<VendorSku> carSkus = car.getVendorSkus();
			Set<VendorSku> carSkusNew = new HashSet<VendorSku>();
			for (VendorSku sku : carSkus) {
				sku.setVendorStyle(sku.getVendorStyle());
				carSkusNew.add(sku);
			}
			/*
			 * if(car.getCarId()==546) log.debug("car Skus=" +
			 * carSkusNew.size());
			 */
			carSkuMap.put(car, carSkusNew);
		}

		return carSkuMap;
	}

	/**
	 * @return Map for car id and corresponding images
	 *  Method to get all active images for CAR with in-progress status
	 *       Used while saving car images from catalog
	 */
	public Map<Long, List<Image>> getCARSInProgress() {



		StringBuffer buffer = new StringBuffer("from Car ca where ca.currentWorkFlowStatus = 'INITIATED' and ca.statusCd= 'ACTIVE'" +
		" and ca.vendorStyle.vendorStyleNumber in (select v.compositeKey.vendorStyleId from  VendorCatalogStyleSkuMaster  v )");

		List<Car> carList = getHibernateTemplate().find(buffer.toString());

		Map<Long, List<Image>> carImageMap = new HashMap<Long, List<Image>>();
		// Construct a map for car and its active images.
		for (Car car : carList) {

			List<CarImage> carImages = car.getAllActiveCarImages();
			List<Image> imageList = new ArrayList<Image>();
			for (CarImage carImage : carImages) {
				imageList.add(carImage.getImage());
			}
			carImageMap.put(car.getCarId(), imageList);
		}
		return carImageMap;
	}

	/**
	 * @return List<ImageType>
	 * @TODO Get all image types
	 */
	public List<ImageType> getImageTypes() {
		return this.getHibernateTemplate().find("from ImageType");
	}

	/**
	 * @param carImageListNew
	 * @TODO Save or update car images in CAR_IMAGE table
	 */
	public void saveOrUpdateCarImages(List<CarImage> carImageListNew) {
		getHibernateTemplate().saveOrUpdateAll(carImageListNew);
		getHibernateTemplate().flush();
	}

	/**
	 * @param imageListNew
	 * @return List<Image>
	 * @TODO Method to save images in IMAGE table Used while saving car images
	 *       from catalog
	 */
	public List<Image> saveOrUpdateImages(List<Image> imageListNew) {
		log.debug("size of image list="+imageListNew.size());
		List<Image> imagesNew= new ArrayList<Image>();
		for(Image i:imageListNew){
			getHibernateTemplate().saveOrUpdate(i);
			imagesNew.add(i);
		}
		
		getHibernateTemplate().flush();
		return imagesNew;
	}


	/**
	 * @return List<VendorCatalogStyleSkuImage>
	 * @TODO Method to get all ACTIVE images for each record in master table.
	 *       Used while saving car images from catalog
	 */
	public List<VendorCatalogStyleSkuImage> getAllImagesforMaster() {
		StringBuffer sqlB = new StringBuffer(
		"select i.* ")

		.append("  from VNDR_CATL_STY_SKU_IMAGE i")
		.append(" inner Join VNDR_CATL_STY_SKU_MAST m")
		.append(" on i.vendor_catalog_id=m.vendor_catalog_id")
		.append(" where i.status_cd='ACTIVE'")
		.append(" GROUP BY i.VENDOR_CATALOG_ID,i.VENDOR_STYLE_ID ")
		.append(
		",i.VENDOR_UPC,i.IMAGE_TYPE ,i.IMAGE_FILE_NAME,i.VNDR_CATL_STY_SKU_IMAGE_ID")
		.append(",i.updated_date,i.created_date,i.status_cd,i.CREATED_BY,i.UPDATED_BY");

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();


		List<VendorCatalogStyleSkuImage> recordList = new ArrayList<VendorCatalogStyleSkuImage>();
		recordList = session.createSQLQuery(sqlB.toString()).addEntity(
				VendorCatalogStyleSkuImage.class).list();

		if (null != recordList && log.isDebugEnabled()) {

			log.debug("recordList=-" + recordList.size());

		}

		return recordList;
	}


	/**
	 * @param vendorStyleId
	 * @return Long department id
	 * @TODO To get the department id for vendor style
	 */
	public Long getDepartmentIdForVendorStyle(VendorStyle vendorStyle) {
		Long deptId=null;
		if (vendorStyle != null) {
			vendorStyle=(VendorStyle) getById(VendorStyle.class, vendorStyle.getVendorStyleId());
			deptId= vendorStyle.getClassification().getDepartment().getDeptId();
		}
		return deptId;
	}

	/**
	 * @param catalogListNew
	 * @TODO To save the catalog to complete
	 */
	public void saveCatalogsToComplete(List<VendorCatalog> catalogListNew) {
		getHibernateTemplate().saveOrUpdateAll(catalogListNew);
		getHibernateTemplate().flush();
	}

	public VendorSku checkUpcExists(String upc)
	throws Exception {
		List<VendorSku> list = getHibernateTemplate().find("FROM VendorSku WHERE belkUpc=?", upc);
		VendorSku sku=null;
		if (null != list && !list.isEmpty()) {
			sku=list.get(0);
		}
		return sku;
	}

	public Classification getClassificationFromNumber(Short classNumber)
	throws Exception {
		List<Classification> list = getHibernateTemplate().find(
				"FROM Classification WHERE belkClassNumber=?", classNumber);
		Classification clasz=null;
		if (null != list && !list.isEmpty()) {
			clasz= list.get(0);
		}
		return clasz;
	}

	public boolean checkVendorStyleExists(String vendorStyleNumber)
	throws Exception {
		List<VendorStyle> list = getHibernateTemplate().find(
				"FROM VendorStyle WHERE vendorStyleNumber=?", vendorStyleNumber);
		boolean flag=false;
		if (null != list && !list.isEmpty()) {
			flag= true;
		}
		return flag;
	}

	public void saveEntityObject(Object entity)
	throws Exception {
		getHibernateTemplate().save(entity);
		getHibernateTemplate().flush();
	}

	public Department getDepartmentFromDeptCode(String deptCd)
	throws Exception {
		List<Department> list = getHibernateTemplate().find("FROM Department WHERE deptCd=?",
				deptCd);
		Department dept=null;
		if (null != list && !list.isEmpty()) {
			dept= list.get(0);
		}
		return dept;
	}

	public VendorStyle getVendorStyleFromNumber(String styleNumber)
	throws Exception {
		List<VendorStyle> list = getHibernateTemplate().find(
				"FROM VendorStyle WHERE vendorStyleNumber=?", styleNumber);
		VendorStyle style=null;
		if (null != list && !list.isEmpty()) {
			style= list.get(0);
		}
		return style;
	}

	public User getUserBuyer()
	throws Exception {
		UserType userType = (UserType) this.get(UserType.class, UserType.BUYER);
		List<User> list = getHibernateTemplate().find("FROM User WHERE userType=?",
				userType);
		User  user=null;
		if (null != list && !list.isEmpty()) {
			user= list.get(0);
		}
		return user;
	}

	public void saveList(List<Object> list)
	throws Exception {
		getHibernateTemplate().saveOrUpdateAll(list);
		getHibernateTemplate().flush();
	}

	/**
	 * @param catalog
	 * @return List of records imported.
	 * @TODO To get the number of records imported.
	 */
	public List<BigDecimal> getNumberOfRecordsImported() {


		StringBuilder query = new StringBuilder(
		"SELECT VENDOR_CATALOG_ID  FROM  vendor_catalog_record")
		.append(" GROUP BY VENDOR_CATALOG_ID, VNDR_CATL_RECORD_NUM ORDER BY VENDOR_CATALOG_ID");

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		List<BigDecimal> recordList = new ArrayList<BigDecimal>();
		recordList = session.createSQLQuery(query.toString()).list();

		return recordList;

	}

	/**
	 * @param catalog
	 * @return List of records imported.
	 * @TODO To get the number of records imported in style sku table.
	 */
	public List<BigDecimal> getNumberOfRecordsImportedInStyleSku() {

		StringBuilder query = new StringBuilder(
		"SELECT VENDOR_CATALOG_ID  FROM  vendor_catalog_sty_sku");

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		List<BigDecimal> recordList = new ArrayList<BigDecimal>();
		recordList = session.createSQLQuery(query.toString()).list();

		return recordList;

	}

	/**
	 * @param carId
	 * @param attrId
	 * @TODO Get the car attribute based on the car id and car attribute id. 
	 */
	public CarAttribute getCarAttribute(Long carId, Long attrId) {
		StringBuffer buffer = new StringBuffer("from CarAttribute c");
		buffer.append(" where c.car.carId=?");
		buffer.append("and c.attribute.attributeId=?");
		List<Object> values = new ArrayList<Object>();
		values.add(carId);
		values.add(attrId);
		List<CarAttribute> carAttrList= (List<CarAttribute>) getHibernateTemplate().find(buffer.toString(), values.toArray());
		CarAttribute carAttr=null;
		if(!carAttrList.isEmpty()){
			carAttr=carAttrList.get(0);
		}
		return carAttr;

	}

	/**
	 * @param vendorCatalogID - Latest catalog ID for vensor style
	 * @param vendorStyleId-Vendor Style ID
	 * @TODO Get Latest record from catalog with latest UPC 
	 */
	public List<VendorCatalogRecord> getRecordsForLatestStyleUPC(Long vendorCatalogID, String vendorStyleNumber) {
		StringBuilder query = new StringBuilder(
		"SELECT * FROM vendor_catalog_record WHERE VENDOR_CATALOG_ID=:CATALOG_ID1")
		.append(" AND VNDR_CATL_RECORD_NUM=")
		.append(" ( SELECT Max(RECORD_NUMBER) FROM vendor_catalog_sty_sku")
		.append(" WHERE VENDOR_CATALOG_ID=:CATALOG_ID2")
		.append(" AND  VENDOR_STYLE_ID=:STYLE_NUM")
		.append(" )");
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();

		SQLQuery query2 = session.createSQLQuery(query.toString()).addEntity(
				VendorCatalogRecord.class);
		query2.setLong("CATALOG_ID1", vendorCatalogID);
		query2.setLong("CATALOG_ID2", vendorCatalogID);
		query2.setString("STYLE_NUM", vendorStyleNumber);
		
		return query2.list();

	}

	/**
	 * @param carAttribute
	 * @TODO Save or update car attribute.
	 */
	public void saveOrUpdateCarAttribute(CarAttribute carAttribute) {
		getHibernateTemplate().saveOrUpdate(carAttribute);
		getHibernateTemplate().flush();
	}

	/** Get unmapped attribute for cars 
	 * @param vendorCatalogID
	 * @param templateId
	 * @return List<VendorCatalogHeader>
	 * @Enclosing_Method  getUnmappedAttribute
	 */
	public List<VendorCatalogHeader> getUnmappedAttribute(Long vendorCatalogID, long templateId) {
		StringBuilder query = new StringBuilder("SELECT * FROM vendor_catalog_header WHERE vendor_catalog_id=:CATALOG_ID")
		.append(" AND vendor_catalog_header_id NOT IN (")
		.append(" SELECT VENDOR_CATALOG_HEADER_ID FROM vendor_catalog_fld_mapping WHERE")
		.append(" VENDOR_CATALOG_TMPL_ID=:TEMPLATE_ID1")
		.append(")")
		.append("AND VNDR_CATL_HDR_FLD_NAME  NOT IN ( SELECT VNDR_CATL_HDR_FLD_NAME ")
		.append("FROM master_attribute_mapping  WHERE CATALOG_TEMPLATE_ID=:TEMPLATE_ID2").append("  )");
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		
		SQLQuery query2 = session.createSQLQuery(query.toString()).addEntity(
				VendorCatalogHeader.class);
		query2.setLong("CATALOG_ID", vendorCatalogID);
		query2.setLong("TEMPLATE_ID1", templateId);
		query2.setLong("TEMPLATE_ID2", templateId);

		return query2.list();
	}

	/**
	 * @param vendorStyleNumber
	 * @param vendor
	 * @return VendorStyle
	 * @TODO To return vendor style based on vendor style number
	 */
	public VendorStyle getVendorStyleByNumber(String vendorStyleNumber, Vendor vendor) {
		Object arr[] = { vendorStyleNumber, vendor };
		Object ob = getHibernateTemplate().find("from VendorStyle where vendorStyleNumber=? and vendor=?", arr);
		Collection<VendorStyle> col = (Collection) ob;
		for (VendorStyle vendorStyle : col) {
			return vendorStyle;
		}
		return null;
	}


	public List<Car> getPatternCars() {

		HibernateTemplate template = getHibernateTemplate();
		return (List<Car>) template.find("from Car ca where ca.currentWorkFlowStatus = 'INITIATED' and ca.statusCd= 'ACTIVE' and ca.vendorStyle.parentVendorStyle is not null");
	}

	public Car getParentStyleCar(long parentStyleId) {
		List carList = null;
		Car parentStyleCar =null;
		HibernateTemplate template = getHibernateTemplate();
		carList = template.find("from Car ca where ca.vendorStyle.vendorStyleId=" + parentStyleId);

		if (carList != null && !carList.isEmpty()) {

			return (Car) carList.get(0);
		}
		return parentStyleCar;
	}

	/**
	 * @param vendorStyleForComparison
	 * @TODO
	 */
	public void saveStyle(VendorStyle vendorStyleForComparison) {
		HibernateTemplate template = getHibernateTemplate();
		if(vendorStyleForComparison.getParentVendorStyle()!=null){
			VendorStyle parentVendorStyle=vendorStyleForComparison.getParentVendorStyle();
			parentVendorStyle.setDescr(vendorStyleForComparison.getDescr());
			parentVendorStyle.setVendorStyleName(vendorStyleForComparison.getVendorStyleName());
			template.saveOrUpdate(parentVendorStyle);
		}
		template.saveOrUpdate(vendorStyleForComparison);
		
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see com.belk.car.app.dao.DropshipDao#getAttributeByName(java.lang.String)
	 * Method to get the attribute by Name
	 */
	public Attribute getAttributeByName(String attrName){
		List list = getHibernateTemplate().find("from Attribute where name='" + attrName + "' and statusCd='ACTIVE'");
		Attribute attr = null;
		if(list!=null && list.size()>0){
			attr  = (Attribute) list.get(0);
		}
		return attr;
	}

	/*
	 * (non-Javadoc)
	 * @see com.belk.car.app.dao.DropshipDao#getCarsByVendorStyle(java.lang.String)
	 * Method get list of CARS by vendor Style
	 */
	/*
	 * Updated to get the list of CARS for the pattern cars
	 */
	public List<Car> getCarsByVendorStyle(String vendorStyleNo){
		VendorStyle vs=null;
		try{
			vs=getVendorStyleFromNumber(vendorStyleNo);
		}
		catch(Exception e){
		}
		log.debug(" Getting the CARS for the vendor style #:"+vendorStyleNo);
		log.debug("Parent vendor style:"+vs.getParentVendorStyle());
		StringBuffer query = new StringBuffer();
		if(vs!=null && vs.getParentVendorStyle()!=null) {
			log.debug(" Parent vendor style #:"+vs.getParentVendorStyle().getVendorStyleNumber());
			query.append("  SELECT * FROM CAR C ");
			query.append("  INNER JOIN VENDOR_STYLE VS ON VS.PARENT_VENDOR_STYLE_ID=C.VENDOR_STYLE_ID AND VS.STATUS_CD='ACTIVE' ");
			query.append("  AND C.CURRENT_WORKFLOW_STATUS='INITIATED' AND VS.VENDOR_STYLE_NUMBER =:VENDORSTYLENO ");
		} else {
			log.debug(" Parent vendor style is null ");
			query.append("  SELECT * FROM CAR C  ");
			query.append(" INNER JOIN VENDOR_STYLE VS ON VS.VENDOR_STYLE_ID=C.VENDOR_STYLE_ID AND VS.STATUS_CD='ACTIVE' ");
			query.append(" AND C.CURRENT_WORKFLOW_STATUS='INITIATED' AND VS.VENDOR_STYLE_NUMBER =:VENDORSTYLENO ");
		}
		
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();

		SQLQuery cars = session.createSQLQuery(query.toString()).addEntity(Car.class);
		cars.setString("VENDORSTYLENO", vendorStyleNo);
		
		return cars.list();

	}

	/*
	 * (non-Javadoc)
	 * @see com.belk.car.app.dao.DropshipDao#getLatestCatalogStyles()
	 * Method to get the latest catalog styles
	 */
//	Set<Foo> foo = new HashSet<Foo>(myList);


	public List<VendorCatalogStyleSkuMaster> getLatestCatalogStyles(){

		StringBuffer query = new StringBuffer(" select * from VNDR_CATL_STY_SKU_MAST where status_cd='ACTIVE' and to_date(updated_date)=to_date(current_date) ");
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();

		SQLQuery result = session.createSQLQuery(query.toString()).addEntity(
				VendorCatalogStyleSkuMaster.class);
		
		return result.list();
	}
	/*
	 * Method to get the list of fulfillment service vendor details
	 */
	public List<FulfillmentServiceVendor> getVendorExpditedShippingValues(){
		List<FulfillmentServiceVendor> list = getHibernateTemplate().find("from FulfillmentServiceVendor where  statusCd='ACTIVE'");
		
		return list;
	}
	
	
	/**
	 * This method returns the FulfillmentServiceVendor objects updated since the date provided .
	 * @param strDateLastRun Date as String
	 * @return {@link List} of {@link FulfillmentServiceVendor} 
	 * @author afuyxv2
	 */
	public List<FulfillmentServiceVendor> getLatestVendorExpditedShippingValues(String strDateLastRun){
		StringBuffer query = new StringBuffer(" select * from FULFMNT_SERVICE_VENDOR ");
		query.append(" where ");
		query.append(" status_cd='ACTIVE' ");
		query.append(" and ");
		query.append("to_date(updated_date) ");
		query.append(" between ");
		query.append(" to_date('"+strDateLastRun+"') ");
		query.append(" and ");
		query.append(" to_date(current_date)");
		return getVendorExpditedShippingValuesByQuery(query.toString());
	}
	
	/**
	 * This method returns the List of FulfillmentServiceVendor objects based on the 'SQL' query provided .
	 * @param sql SQL query as a string
	 * @return {@link List} of {@link FulfillmentServiceVendor}
	 * @author Yogesh Vedak (afuyxv2)
	 */
	public List<FulfillmentServiceVendor> getVendorExpditedShippingValuesByQuery(String sql){
		String query = sql;
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		SQLQuery result = session.createSQLQuery(query.toString()).addEntity(
				FulfillmentServiceVendor.class);
		return result.list();
	}

}
