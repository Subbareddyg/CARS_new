package com.belk.car.app.dao.hibernate;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.dao.VendorCatalogDao;
import com.belk.car.app.dto.CatalogHeaderDTO;
import com.belk.car.app.dto.CatalogMappedFieldDTO;
import com.belk.car.app.dto.CatalogVendorDTO;
import com.belk.car.app.dto.FieldMappingDataDTO;
import com.belk.car.app.dto.NameValue;
import com.belk.car.app.dto.VendorCatalogDTO;
import com.belk.car.app.dto.VendorCatalogSKUCount;
import com.belk.car.app.dto.VendorCatalogStyleCount;
import com.belk.car.app.dto.VendorCatalogStyleDTO;
import com.belk.car.app.dto.VendorStyleImageDTO;
import com.belk.car.app.dto.VendorStyleInfo;
import com.belk.car.app.dto.VendorStylePropertiesDTO;
import com.belk.car.app.dto.VendorStyleSKUCount;
import com.belk.car.app.dto.VendorUpcDTO;
import com.belk.car.app.model.AttributeLookupValue;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.vendorcatalog.CatalogGroupTemplate;
import com.belk.car.app.model.vendorcatalog.CatalogMasterAttribute;
import com.belk.car.app.model.vendorcatalog.CompositeKeyForDataFldMapping;
import com.belk.car.app.model.vendorcatalog.CompositeKeyForVndrCatRecord;
import com.belk.car.app.model.vendorcatalog.MasterAttributeMapping;
import com.belk.car.app.model.vendorcatalog.VendorCatalog;
import com.belk.car.app.model.vendorcatalog.VendorCatalogDepartment;
import com.belk.car.app.model.vendorcatalog.VendorCatalogFieldDataMapping;
import com.belk.car.app.model.vendorcatalog.VendorCatalogFieldMapping;
import com.belk.car.app.model.vendorcatalog.VendorCatalogFileFormat;
import com.belk.car.app.model.vendorcatalog.VendorCatalogImage;
import com.belk.car.app.model.vendorcatalog.VendorCatalogImageLocation;
import com.belk.car.app.model.vendorcatalog.VendorCatalogImport;
import com.belk.car.app.model.vendorcatalog.VendorCatalogNote;
import com.belk.car.app.model.vendorcatalog.VendorCatalogRecord;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSku;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSkuId;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSkuImage;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSkuMaster;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSkuMasterId;
import com.belk.car.app.model.vendorcatalog.VendorCatalogTemplate;
import com.belk.car.app.model.vendorcatalog.VendorCatalogUpdateAction;



public class VendorCatalogDaoHibernate extends CachedQueryDaoHibernate<VendorCatalog> implements VendorCatalogDao {


	/**
	 * Retrieve a VendorCatalog, based on the given catalog ID
	 * @return VendorCatalog
	 */
	public VendorCatalog getVendorCatalog(Long vendorCatalogID) {
		return (VendorCatalog) getHibernateTemplate().get(VendorCatalog.class, vendorCatalogID);
	}

	/**
	 * Method to get list of previous catalog files for specific vendor ID
	 * @author afusy45
	 * @param vendorId
	 * @return List<VendorCatalogImport>
	 */
	public List<VendorCatalogImport> getPreviousCatalogsList(Long vendorId){
		List<VendorCatalogImport> list = null;
		if (vendorId != null) {
			StringBuffer queryString = new StringBuffer();
			 SessionFactory sf = getHibernateTemplate().getSessionFactory();
	    		Session session = sf.getCurrentSession();
			queryString.append("SELECT * FROM vendor_catalog_import ")
			.append(" inner join VENDOR_CATALOG ON ")
			.append(" vendor_catalog_import.VENDOR_CATALOG_ID=vendor_catalog.VENDOR_CATALOG_ID ")
			.append(" AND VENDOR_ID=:VENDOR_ID AND IMAGE_FILE_SOURCE_CD != 4");
			
			SQLQuery sQuery = (SQLQuery) session.createSQLQuery(queryString.toString()).addEntity(VendorCatalogImport.class);
			sQuery.setLong("VENDOR_ID", vendorId);
			
			list = sQuery.list();
		}

		return list;
	}
	

	/**
	 * Retrieve a list of all Vendors
	 *
	 */
	public List<Vendor> getAllVendors() {
		return getHibernateTemplate().find("from Vendor");

	}


	/** Vendor Catalog Form Start */
	/**
	 *
	 * This method gets the vendor number
	 * @param  vendorNumber vendorNumber
	 * @return Vendor
	 */
	public Vendor getVendor(String vendorNumber) {
		if(log.isDebugEnabled()){
			log.debug("entering VendorCatalogDaoHibernate's getVendor method...");
		}
		List <Vendor> list = getHibernateTemplate().find("FROM Vendor WHERE vendorNumber = ?", vendorNumber);
		Vendor vendor =null;
		if(null != list && !list.isEmpty()) {//&& list.size() > 0
			vendor = list.get(0);


		}
		return vendor;
	}

	/**
	 * Retrieve a VendorCatalog, based on the given catalog ID
	 * @return VendorCatalog
	 */
	public VendorCatalogImport getVendorCatalogImpDetails(long vendorCatalogID) {
		VendorCatalog vndrCatalogId = getVendorCatalog(vendorCatalogID);
		if(log.isDebugEnabled()){
			log.debug("inside getVendorCatalogImpDetails"+vendorCatalogID);
		}
		Set<VendorCatalogImport> set = vndrCatalogId.getCatalogImport();
		Iterator<VendorCatalogImport> it = set.iterator();
		VendorCatalogImport catalogImport=null;
		while(it.hasNext()){
			catalogImport = it.next();

		}
		return catalogImport;
	}

	/**
	 * Retrieve a VendorCatalog, based on the given catalog ID
	 * @return VendorCatalog
	 */
	public List getVendorCatalogDeptDetails(VendorCatalog vendorCatalog) {
		if(log.isDebugEnabled()){
			log.debug("entering VendorCatalogDaoHibernate's getVendorCatalogDeptDetails method...");
		}
		List<Department> recordList = new ArrayList<Department>();
		List<VendorCatalogDepartment> list = getHibernateTemplate().find("from VendorCatalogDepartment WHERE compositeKeyForVndrCatlDept.vendorCatalog = ? and lower(statusCD)='active' ",vendorCatalog);
		if (list != null && !list.isEmpty()) {

			StringBuffer queryBuf = new StringBuffer("select r.* ");

			queryBuf
			.append(" from department r ")
			.append(" where")
			.append(" r.dept_id IN(");
			Iterator<VendorCatalogDepartment> itr=list.iterator();
			while(itr.hasNext()){
				VendorCatalogDepartment headerNum=itr.next();
				queryBuf.append(headerNum.getCompositeKeyForVndrCatlDept().getDeptId());
				if(itr.hasNext()){
					queryBuf.append(",");
				}
			}
			queryBuf.append(" )");
			SessionFactory sf = getHibernateTemplate().getSessionFactory();
			Session session = sf.getCurrentSession();
			try {
				recordList = session.createSQLQuery(queryBuf.toString()).addEntity(
						Department.class).list();
			}
			catch (Exception e) {
				log.error("Hibernate Exception", e);
			}
			/*finally {
			// Do nothing for now...
			// But may have to see if close connections
		}*/
			session.clear();
		}
		return recordList;
	}
	/**
	 * For Edit mode functionality
	 * Check for vendor catalog import id in edit mode
	 */
	public VendorCatalogImport getVendorCatalogImport(Long vendorCatalogID) {
		List<VendorCatalogImport> catalogImpList = getHibernateTemplate().find("from VendorCatalogImport where vendorCatalogID.vendorCatalogID=?",vendorCatalogID);
		VendorCatalogImport catlImport=null;
		if(null != catalogImpList && !catalogImpList.isEmpty()) {//catalogImpList.size() > 0 &&
			catlImport = (VendorCatalogImport)catalogImpList.get(0);
		}
		return catlImport;
	}
	/**
	 *
	 * This method gets the file format name from VendorCatalogFileFormat table
	 * @param  fileFormatName fileFormatName
	 * @return VendorCatalogFileFormat
	 */
	public VendorCatalogFileFormat getFileFormat(String fileFormatName) {
		if (log.isDebugEnabled()) {
			log.debug("entering VendorCatalogDaoHibernate's getFileFormat method...");
		}
		List <VendorCatalogFileFormat> list = getHibernateTemplate().find("FROM VendorCatalogFileFormat WHERE fileFormatName = ?", fileFormatName);
		VendorCatalogFileFormat fileFormat=null;
		if(null != list && !list.isEmpty()) {//&& list.size() > 0
			fileFormat = list.get(0);
			if (log.isDebugEnabled()) {
				log.debug("Format:::"+fileFormat);
			}

		}
		return fileFormat;
	}
	/**
	 *
	 * This method gets the update action description from VendorCatalogUpdateAction table
	 * @param  updateAction updateAction
	 * @return VendorCatalogUpdateAction
	 */
	public VendorCatalogUpdateAction getUpdateAction(String updateAction) {
		if (log.isDebugEnabled()) {
			log.debug("entering VendorCatalogDaoHibernate's getUpdateAction method...");
		}
		List <VendorCatalogUpdateAction> list = getHibernateTemplate().find("FROM VendorCatalogUpdateAction WHERE updateActionDesc = ?", updateAction);
		VendorCatalogUpdateAction updtAction=null;
		if(null != list && !list.isEmpty() ) {//&& list.size() > 0
			updtAction = list.get(0);
			if (log.isDebugEnabled()) {
				log.debug("Update Action:::"+updtAction);
			}

		}
		return updtAction;
	}
	/**
	 *
	 * This method gets the image location description from VendorCatalogImageLocation table
	 * @param  imageLocation imageLocation
	 * @return VendorCatalogImageLocation
	 */
	/*
	 * Updated as part of CARs Dropship 2012
	 */
	public VendorCatalogImageLocation getImageLocation(String imageLocation) {
		if (log.isDebugEnabled()) {
			log.debug("entering VendorCatalogDaoHibernate's getImageLocation method...imageLocation:"+imageLocation);
		}
		List <VendorCatalogImageLocation> list = getHibernateTemplate().find("FROM VendorCatalogImageLocation WHERE imageSourceName = ?", imageLocation);
		VendorCatalogImageLocation imgLocation=null;
		if(null != list && !list.isEmpty() ) {//&& list.size() > 0
			imgLocation = list.get(0);

		}
		return imgLocation;
	}
	/**
	 *
	 * This method save the vendor catalog form data into VendorCatalog table
	 * @param  vendorCatalog vendorCatalog
	 * @return VendorCatalog
	 */
	public VendorCatalog save(VendorCatalog vendorCatalog) {
		if (log.isDebugEnabled()) {
			log.debug("entering VendorCatalogDaoHibernate's save VendorCatalog method...");
		}
		getHibernateTemplate().merge(vendorCatalog);
		getHibernateTemplate().flush();
		return vendorCatalog;
	}
	/**
	 *
	 * This method save the vendor catalog imp form data into VendorCatalogImport table
	 * @param  vendorCatalogImport vendorCatalogImport
	 * @return void
	 */
	public void save(VendorCatalogImport vendorCatalogImport){
		if (log.isDebugEnabled()) {
			log.debug("entering VendorCatalogDaoHibernate's save VendorCatalogImport method...");
		}
		getHibernateTemplate().saveOrUpdate(vendorCatalogImport);
		getHibernateTemplate().flush();
	}


	/**
	 * This method saves vendor catalog departments into VendorCatalogDepartment table
	 *
	 */
	public void saveVndrCatlDept(List<VendorCatalogDepartment> vndrCatlDeptList){
		if (log.isDebugEnabled()) {
			log.debug("entering VendorCatalogDaoHibernate's saveVndrCatlDept method...");
		}
		getHibernateTemplate().saveOrUpdateAll(vndrCatlDeptList);
		getHibernateTemplate().flush();
	}
	/**
	 * This method gets departments for catalog ID
	 * Implementing Edit functionality
	 */
	public VendorCatalogDepartment getVendorCatalogDepartment(Long deptId, VendorCatalog vendorCatalog) {
		/*if (log.isDebugEnabled()) {
			log.debug("entering getVendorDepartment method  of VendorCatalogDaoHibernate...");
		}*/
		StringBuffer sb=new StringBuffer("from VendorCatalogDepartment where compositeKeyForVndrCatlDept.vendorCatalog=? and compositeKeyForVndrCatlDept.deptId=?");
		ArrayList<Object> values = new ArrayList<Object>();
		values.add(vendorCatalog);
		values.add(deptId);
		List<VendorCatalogDepartment> deptList =  getHibernateTemplate().find(sb.toString(), values.toArray());
		VendorCatalogDepartment dept=null;
		if(null !=deptList && !deptList.isEmpty()){
			dept= (VendorCatalogDepartment)deptList.get(0);
		}
		return dept;
	}



	/**
	 * This method saves vendor catalog image into VendorCatalogImage table
	 *
	 */
	public void saveVndrCatlImg(List<VendorCatalogImage> vendorCatalogImageList){
		if (log.isDebugEnabled()) {
			log.debug("entering VendorCatalogDaoHibernate's saveVndrCatlImg method...");
		}
		getHibernateTemplate().saveOrUpdateAll(vendorCatalogImageList);
		getHibernateTemplate().flush();
	}
	/**
	 *
	 * This method gets the vendor catalog template id from VendorCatalogTemplate table
	 * @param  Long vendorCatalogTmplId
	 * @return VendorCatalogTemplate
	 */
	public VendorCatalogTemplate getVendorCatalogTemplate(Long vendorCatalogTmplId) {
		if (log.isDebugEnabled()) {
			log.debug("entering VendorCatalogDaoHibernate's getVendorCatalogTemplate method...vendorCatalogTmplId:"+vendorCatalogTmplId);
		}
		VendorCatalogTemplate vendorCatalogTmpl =(VendorCatalogTemplate) getHibernateTemplate().get(VendorCatalogTemplate.class, vendorCatalogTmplId);
		if (vendorCatalogTmpl != null ) {
			if(log.isDebugEnabled()){
				log.debug("Vendor Catalog:"+vendorCatalogTmpl.getGlobal());
			}

		}
		return vendorCatalogTmpl;
	}

	/**
	 *
	 * This method gets the image location List from VendorCatalogImageLocation table
	 * @return ListVendorCatalogImageLocation
	 */
	public List<VendorCatalogImageLocation> getVendorCatalogImageLocations() {
		if(log.isDebugEnabled()){
			log.debug("entering VendorCatalogDaoHibernate's getVendorCatalogImageLocations method...");
		}
		List<VendorCatalogImageLocation> list = getHibernateTemplate().find("from VendorCatalogImageLocation ORDER BY imageLocationID DESC");

		return list;
	}

	/**
	 *
	 * This method gets the update action List from VendorCatalogUpdateAction table
	 * @return ListVendorCatalogUpdateAction
	 */
	public List<VendorCatalogUpdateAction> getVendorCatalogUpdateActions() {
		if (log.isDebugEnabled()) {
			log.debug("entering VendorCatalogDaoHibernate's getVendorCatalogUpdateActions method...");
		}
		List<VendorCatalogUpdateAction> list = getHibernateTemplate().find("from VendorCatalogUpdateAction");

		return list;

	}

	/**
	 *
	 * This method gets the file format List from VendorCatalogFileFormat table
	 * @return ListVendorCatalogFileFormat
	 */
	/*
	 * Updated as part of CARS Dropship 2012
	 * Added Active status
	 */
	public List<VendorCatalogFileFormat> getVendorCatalogFileFormats() {
		if (log.isDebugEnabled()) {
			log.debug("entering VendorCatalogDaoHibernate's getVendorCatalogFileFormats method...");
		}
		List<VendorCatalogFileFormat> list = getHibernateTemplate().find("from VendorCatalogFileFormat where statusCD='ACTIVE' ");

		return list;

	}
	/**
	 * This method gets image list for the vendorcatalog id
	 * @param long vendorCatalogID
	 * @return List
	 */
	public List <VendorCatalogImage> getVendorCatalogImageList(long vendorCatalogID){
		if (log.isDebugEnabled()) {
			log.debug("entering VendorCatalogDaoHibernate's getVendorCatalogFileFormats method...");
		}
		List imageNameList = new ArrayList();
		List<VendorCatalogImage> list = (List)getHibernateTemplate().find("FROM VendorCatalogImage WHERE vendorCatalogId = ?",vendorCatalogID);
		if (list != null && !list.isEmpty()) {
			for(VendorCatalogImage vndr: list){
				imageNameList.add(vndr.getImgName());
			}

		}
		return imageNameList;
	}
	/**
	 *  This method gets the catalog id from sequence
	 *  @return long
	 */
	public long getCatalogIdFromSeq(){
		if (log.isDebugEnabled()) {
			log.debug("entering VendorCatalogDaoHibernate's getCatalogIdFromSeq method");
		}
		long catalogId = 0;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		String query = "SELECT SEQ_VENDOR_CATALOG_ID.NEXTVAL FROM dual";
		List list = session.createSQLQuery(query).list();
		catalogId = Long.parseLong(list.get(0).toString());
		if (log.isDebugEnabled()) {
			log.debug("sequence for catalog id is..."+catalogId);
		}
		return catalogId;
	}
	/**
	 * This method gets catalog names for Vendor.
	 * Used for accepting unique catalog name.
	 * @param String vendorNumber
	 * @param String catalogName
	 * @return List VendorCatalog
	 */
	public List<VendorCatalog> getCatalogNameForVendor(String vendorNumber, String catalogName){
		if (log.isDebugEnabled()) {
			log.debug("entering VendorCatalogDaoHibernate's geCatalogNameForVendor method...");
		}
		List catalogNameList = new ArrayList();
		Vendor vendor = getVendor(vendorNumber);
		List<VendorCatalog> list = (List)getHibernateTemplate().find("FROM VendorCatalog WHERE vendor = ? and vendorCatalogName = ?",new Object[] {vendor, catalogName});

			return list;

	}
        
	/**
	 *  This method gets the current value of catalog id from dump
	 *  @return long
	 */
	public long getCurrCatalogId(){
		if (log.isDebugEnabled()) {
			log.debug("entering VendorCatalogDaoHibernate's getCurrCatalogId method");
		}
		long catalogId = 0;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		String query = "SELECT SEQ_VENDOR_CATALOG_ID.CURRVAL FROM dual";
		List list = session.createSQLQuery(query).list();
		catalogId = Long.parseLong(list.get(0).toString());
		if (log.isDebugEnabled()) {
			log.debug("sequence for catalog id is..."+catalogId);
		}
		return catalogId;
	}
	/** Vendor Catalog Form End */

	/** Vendor Catalog Note Start */
	/**
	 *
	 * This method gets the vendor catalog notes List from VendorCatalogNote table
	 * @param  long		vendorCatalogId
	 * @return ListVendorCatalogNote
	 */
	public List<VendorCatalogNote> getVendorCatalogNotesList (Long vendorCatalogId)
	{
		if (log.isDebugEnabled()) {
			log.debug("entering VendorCatalogDaoHibernate's getVendorCatalogNotesList method...vendorCatalogId:"+vendorCatalogId);
		}
		List<VendorCatalogNote> vendorCatalogNoteList = new ArrayList<VendorCatalogNote>();
		vendorCatalogNoteList=getHibernateTemplate().find("FROM VendorCatalogNote where vendor_catalog_id = ?",vendorCatalogId);

			return vendorCatalogNoteList;

	}
	/**
	 *
	 * This method gets the catalog notes by noteID from VendorCatalogNote table
	 * @param  Long		id
	 * @return VendorCatalogNote
	 */
	public VendorCatalogNote  getVendorCatalogNoteByID(Long id){

		return (VendorCatalogNote) getHibernateTemplate().get(VendorCatalogNote.class, id);
	}
	/**
	 *
	 * This method finds the catalog notes for subject entered from VendorCatalogNote table
	 * @param  String	noteSubject
	 * @param  long		vendorCatalogId
	 * @return ListVendorCatalogNote
	 */
	
	public List<VendorCatalogNote> searchNotes(String noteSubject, long vendorCatalogId) {
		if(log.isDebugEnabled()){
			log.debug("entering VendorCatalogDaoHibernate's searchNotes method...");
		}
		String likeFormat = "%%%1$s%%";
		ArrayList<String> query = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();
		VendorCatalog vendorCatalog=new VendorCatalog();
		vendorCatalog.setVendorCatalogID((new Long(vendorCatalogId)));
		StringBuffer sqlB = new StringBuffer("from VendorCatalogNote vcn where vendorCatalog="+vendorCatalog.getVendorCatalogID());
		if ((noteSubject != null) && !(noteSubject.equals(""))) {
			query.add("upper(vcn.subject) LIKE ?");
			values.add(String.format(likeFormat,noteSubject.toUpperCase(Locale.US)).toString());
		}

		if (!query.isEmpty()) {

			int i=0;
			for(String s: query) {

				sqlB.append(" AND ");

				sqlB.append(s);
				i++;
			}
		}
		sqlB.append(" order by vcn.catalogNoteID");
		return getHibernateTemplate().find(sqlB.toString(), values.toArray());	

	}
	
	/*public List<VendorCatalogNote> searchNotes(VendorCatalogNote vendorCatalogNote) {
		List<VendorCatalogNote> notesList=getHibernateTemplate().findByExample(vendorCatalogNote);
		log.debug("notesList"+notesList);
		return notesList;
	}*/
	/**
	 *
	 * This method adds a note into VendorCatalogNote table
	 * @param  VendorCatalogNote	vendorCatalogNote
	 * @return VendorCatalogNote
	 */
	public VendorCatalogNote addNote(VendorCatalogNote vendorCatalogNote) {
		if (log.isDebugEnabled()) {
			log.debug("entering VendorCatalogDaoHibernate's vendor catalog addNote method...");
		}
		getHibernateTemplate().saveOrUpdate(vendorCatalogNote);
		getHibernateTemplate().flush();
		return vendorCatalogNote;
	}
	/** Vendor Catalog Note End */

        /** Get the List of the Vendors who has a catalog setup in CARS
         * getCatalogVendors
         * @param searchCriteria
         * @return
         */
	public List<CatalogVendorDTO> getCatalogVendors(CatalogVendorDTO searchCriteria) {
                if(log.isDebugEnabled()){
                    log.debug("Start getCatalogVendors");
                }
                //SQL query
		List<CatalogVendorDTO> vendorList = new ArrayList<CatalogVendorDTO>();
		StringBuffer query = new StringBuffer();
		query.append("select ");
		query.append("ven.vendor_number as vendorNumber, ven.vendor_id as vendorId,") ;
		query.append("ven.name as name,") ;
		query.append("ven.is_displayable as isDisplayable, ") ;
		query.append(" SUM(DECODE(vencat.STATUS_CD,'DELETED',0,1)) as numCatalogs,") ;
		query.append("max(to_char(vcimp.imported_date,'mm/dd/yyyy')) as dateLastImport ");
		query.append("from ") ;
		query.append("vendor ven inner join vendor_catalog vencat on ven.vendor_id = vencat.vendor_id ") ;
		query.append("left outer join vendor_catalog_import  vcimp on vencat.vendor_catalog_id = vcimp.vendor_catalog_id ") ;
                //Get Search Criteria.
		query.append(getSearchCriteriaSQLStringForCatlogVendors(searchCriteria));
		query.append("GROUP BY ven.vendor_number, ven.name, ven.is_displayable,ven.vendor_id") ;
		if(log.isDebugEnabled()){
			log.debug("Search query :"+ query.toString());
		}
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query.toString()).
                                          addScalar("vendorNumber",Hibernate.LONG).
                                          addScalar("vendorId",Hibernate.STRING).
                                          addScalar("name").addScalar("isDisplayable").
                                          addScalar("numCatalogs",Hibernate.LONG).
                                          addScalar("dateLastImport",Hibernate.STRING).
                                          setResultTransformer(Transformers.aliasToBean(CatalogVendorDTO.class));
			vendorList = sQuery.list();
		} catch (Exception e) {
			log.error("Hibernate Exception caught in getCatalogVendors");
			e.printStackTrace();

		}
		List <VendorCatalogStyleCount> styleCountList =  getStyleCount();
		List <VendorCatalogSKUCount> skuCountList =  getSKUCount();
		Iterator itr = vendorList.iterator();
		while(itr.hasNext()) {
			CatalogVendorDTO catalogVendorDTO = (CatalogVendorDTO) itr.next();
			VendorCatalogStyleCount catalogStyleCount = new VendorCatalogStyleCount();
			VendorCatalogSKUCount catalogSKUCount =  new VendorCatalogSKUCount();
			catalogSKUCount.setVendorId(catalogVendorDTO.getVendorId());
			catalogVendorDTO.setNumStyle(0);
			catalogVendorDTO.setNumSKUs(0);

			catalogStyleCount.setVendorId(catalogVendorDTO.getVendorId());

			// catalogStyleCount.setStyleCount("0");
			if(styleCountList.contains(catalogStyleCount)) {
                            int ind = styleCountList.indexOf(catalogStyleCount);
                            if(ind>=0) {
                                VendorCatalogStyleCount temp =  styleCountList.get(ind);
				catalogVendorDTO.setNumStyle(Long.parseLong(temp.getStyleCount()));
                            }
				
			}
			catalogSKUCount.setSkuCount("0");
			if(null != skuCountList && !skuCountList.isEmpty() && skuCountList.contains(catalogSKUCount)){
                                        
					VendorCatalogSKUCount temp = skuCountList.get(skuCountList.indexOf(catalogSKUCount));
					catalogVendorDTO.setNumSKUs(Long.parseLong(catalogSKUCount.getSkuCount()));

			}
		}
                if(log.isDebugEnabled()){
                    log.debug("End getCatalogVendors");
                }
		return vendorList;
	}
        /**
         * Get Search Criteria SQLString For Catlog Vendors.
         * @param searchCriteriaDTO CatalogVendorDTO
         * @return
         */
	private String getSearchCriteriaSQLStringForCatlogVendors(CatalogVendorDTO searchCriteriaDTO) {
		String serachCriteria = "";
		Long vendorNumber = searchCriteriaDTO.getVendorNumber();
		String name= searchCriteriaDTO.getName();
		String displayAble = searchCriteriaDTO.getIsDisplayable();
		String startDate = searchCriteriaDTO.getDateLastImportStart();
		String endDate= searchCriteriaDTO.getDateLastImportEnd();
		String userDeptOnly = searchCriteriaDTO.getUserDeptOnly();
		String userId= searchCriteriaDTO.getUserId();
		
		if(null != vendorNumber && vendorNumber!=0) { //&& !StringUtils.isBlank(vendorNumber)
			serachCriteria = serachCriteria  + "ven.vendor_number ='" +  vendorNumber+"'";

		}
		if(null != name && !StringUtils.isBlank(name)) {
			if( !StringUtils.isBlank(serachCriteria)) {
				serachCriteria = serachCriteria + " and ";
			}
			serachCriteria = serachCriteria  + "UPPER(ven.name) like '%" +  name.toUpperCase() +"%' ";

		}
		if(null != displayAble && !StringUtils.isBlank(displayAble) && !displayAble.equals("All")) {
			if( !StringUtils.isBlank(serachCriteria)) {
				serachCriteria = serachCriteria + " and ";
			}

			serachCriteria = serachCriteria  + "ven.is_displayable ='" +  displayAble +"'";

		}
		if(null != startDate && !StringUtils.isBlank(startDate) && null != endDate && !StringUtils.isBlank(endDate)) {
			if( !StringUtils.isBlank(serachCriteria)) {
				serachCriteria = serachCriteria + " and ";
			}
			serachCriteria = serachCriteria  + "vcimp.imported_date between to_date('" + startDate + "','mm/dd/yyyy')  and  to_date('" +
                                                           endDate +"','mm/dd/yyyy')+1";

		}
                //Check if User has selected search for user department only
		if(null != userDeptOnly  && userDeptOnly.equals("true")) {
			if( !StringUtils.isBlank(serachCriteria)) {
				serachCriteria = serachCriteria + " and ";
			}

			serachCriteria = serachCriteria + "ven.vendor_id in (select distinct vendor_id  from  vendor_catalog vcat , vendor_catalog_dept vcatdept ";
                        serachCriteria = serachCriteria + "where vcat.vendor_catalog_id = vcatdept.vendor_catalog_id and vcatdept.dept_id in ( select car_user_dept.dept_id ";
                        serachCriteria = serachCriteria +"from car_user_department car_user_dept where car_user_dept.car_user_id =" +userId +"))";
		}
		if( !StringUtils.isBlank(serachCriteria)) {
			serachCriteria = " where " + serachCriteria + " ";
		}
                if(log.isDebugEnabled()) {
                log.debug("serachCriteria-->"+ serachCriteria);
                }

		return serachCriteria;
	}
        /**
         * Get Vendor Info to be display on Header section of the screens.
         * @param vendorId
         * @return CatalogVendorDTO
         */
	public CatalogVendorDTO getVednorInfo(String vendorId) {
                if(log.isDebugEnabled()) {
                log.debug("Start 'getVednorInfo' method");
                }
		CatalogVendorDTO catalogVendorDTO =  new CatalogVendorDTO();
		List<CatalogVendorDTO> catalogList = new ArrayList<CatalogVendorDTO>();
		StringBuffer query = new StringBuffer();
		query.append("select ");
		query.append("ven.name as name,ven.vendor_number as vendorNumber, ") ;
		query.append("ctl.vendor_catalog_name as catalogName,") ;
		query.append("to_char(imp.imported_date,'mm/dd/yyyy') as dateLastImport,ven.is_displayable as isDisplayable ") ;
		query.append("from ") ;
		query.append("vendor ven, vendor_catalog ctl, vendor_catalog_import imp ") ;
		query.append("where  ctl.vendor_catalog_id= imp.vendor_catalog_id and imp.imported_date = ") ;
		query.append("(select max(imported_Date) from vendor_catalog_import im where imp.vendor_catalog_id= im.vendor_catalog_id) ") ;
		query.append("and ven.vendor_id =:VENDOR_ID and ctl.vendor_id=:VENDOR_ID " ) ;
		query.append(" and ctl.status_cd <>'DELETED' ");
		query.append(" order by imported_date desc") ;


		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query.toString()).
                                          addScalar("name").addScalar("vendorNumber",Hibernate.LONG).
                                          addScalar("catalogName").addScalar("dateLastImport",Hibernate.STRING).
                                          addScalar("isDisplayable",Hibernate.STRING).
                                          setResultTransformer(Transformers.aliasToBean(CatalogVendorDTO.class));
			sQuery.setString("VENDOR_ID", vendorId);
			catalogList = sQuery.list();
		} catch (Exception e) {
			log.error("Hibernate Exception caught in getVednorInfo");
			e.printStackTrace();

		}
		//--------start
		//If catalog list is empty then
		if(catalogList.isEmpty()){
			query = new StringBuffer();
			query.append("select ");
			query.append("ven.name as name,ven.vendor_number as vendorNumber, ") ;
			query.append("ctl.vendor_catalog_name as catalogName,") ;
			query.append("imp.imported_date as dateLastImport,ven.is_displayable as isDisplayable ") ;
			query.append("from ") ;
			query.append("vendor ven, vendor_catalog ctl, vendor_catalog_import imp ") ;
			query.append("where  ctl.vendor_catalog_id= imp.vendor_catalog_id  ") ;
			query.append("and ven.vendor_id =:VENDOR_ID and ctl.vendor_id= :VENDOR_ID" ) ;
			query.append(" and ctl.status_cd<>'DELETED' ");
			try {
				SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query.toString()).
                                                   addScalar("name").addScalar("vendorNumber",Hibernate.LONG).
                                                   addScalar("catalogName").
                                                   addScalar("dateLastImport",Hibernate.STRING).
                                                   addScalar("isDisplayable",Hibernate.STRING).
                                                   setResultTransformer(Transformers.aliasToBean(CatalogVendorDTO.class));
				sQuery.setString("VENDOR_ID", vendorId);
				catalogList = sQuery.list();
			} catch (Exception e) {
				log.error("Hibernate Exception caught in getVednorInfo");
				e.printStackTrace();

			}
		}
		//-------end
		if(!catalogList.isEmpty() ) {
			catalogVendorDTO = catalogList.get(0);
		}
                 if(log.isDebugEnabled()) {
                log.debug("End 'getVednorInfo' method");
                }

		return catalogVendorDTO;
	}
        /**
         * Search Vendor Catalogs accroing the search Criteria.
         * @param searchCriteria CatalogVendorDTO
         * @return List<VendorCatalogDTO>
         */


	public List<VendorCatalogDTO> searchVendorCatalogs(CatalogVendorDTO searchCriteria) {
                 if(log.isDebugEnabled()) {
                log.debug("Start 'searchVendorCatalogs' method");
                }
                List<VendorCatalogDTO> vendorCatalogList = new ArrayList<VendorCatalogDTO>();
                //Get Search criteria SQL string accorting to search Criteria.
                String sqlString = getSearchCriteriaSQLStringForVendorCatalogs(searchCriteria);

                SessionFactory sf = getHibernateTemplate().getSessionFactory();
    		Session session = sf.getCurrentSession();
    		try {

                    SQLQuery sQuery = (SQLQuery) session.createSQLQuery(sqlString)
                                       .addScalar("vendorCatalogID", Hibernate.STRING)
                                       .addScalar("vendorCatalogName",  Hibernate.STRING)
                                       .addScalar("source", Hibernate.STRING)
                                       .addScalar("statusCD",  Hibernate.STRING)
                                       .addScalar("updatedDate",  Hibernate.STRING)
                                       .addScalar("updatedBy",  Hibernate.STRING)
                                       .addScalar("lockedBy",  Hibernate.STRING)
                                       .setResultTransformer(Transformers.aliasToBean(VendorCatalogDTO.class)) ;
                   vendorCatalogList = sQuery.list();
    		} catch (Exception e) {
    			log.error("Hibernate Exception caught in searchVendorCatalogs() -"
    					, e);

    		}
                if(log.isDebugEnabled()) {
                log.debug("End 'searchVendorCatalogs' method");
                }
		return vendorCatalogList;
        }
        /**
         * Get Search Criteria SQL String.
         * @param searchCriteriaDTO CatalogVendorDTO
         * @return String
         */
        private String getSearchCriteriaSQLStringForVendorCatalogs(CatalogVendorDTO searchCriteriaDTO) {
            if(log.isDebugEnabled()) {
                log.debug("End 'getSearchCriteriaSQLStringForVendorCatalogs' method");
                }
            String serachCriteria = "";
            String vendorId = searchCriteriaDTO.getVendorId();
            String catalogName= searchCriteriaDTO.getCatalogName();
            String status = searchCriteriaDTO.getStatus();
            String startDate = searchCriteriaDTO.getDateLastUpdateStart();
            String endDate= searchCriteriaDTO.getDateLastUpdateEnd();
            String deptOnly =searchCriteriaDTO.getUserDeptOnly();
            String userId = searchCriteriaDTO.getUserId();
            String department = searchCriteriaDTO.getDepartment();

            StringBuffer query = new StringBuffer();
            query.append("select vcat.vendor_catalog_id as vendorCatalogID,vcat.vendor_catalog_name as vendorCatalogName,vcat.status_cd as statusCD,");
            query.append("vcat.source as source,vcat.updated_by as updatedBy, to_char(vcat.updated_date,'mm/dd/yyyy') as updatedDate,vcat.locked_by as lockedBy ");
            query.append("from vendor_catalog vcat ");
            serachCriteria =  "where vendor_id =" + vendorId ;
            if(null != catalogName && !StringUtils.isBlank(catalogName)) {

                serachCriteria = serachCriteria  + " and UPPER(vcat.vendor_catalog_name) like '%" +  catalogName.toUpperCase(Locale.US) +"%' ";

            }
            
            serachCriteria = serachCriteria  + " and vcat.status_cd<>'DELETED' ";
            
             if(null != status && !StringUtils.isBlank(status) && !status.equals("All")) {
                if( !StringUtils.isBlank(serachCriteria)) {
                    serachCriteria = serachCriteria + " and ";
                }

                serachCriteria = serachCriteria  + "vcat.status_cd ='" +  status +"'";

            }
             if(null != startDate && !StringUtils.isBlank(startDate) && null != endDate && !StringUtils.isBlank(endDate)) {
                if( !StringUtils.isBlank(serachCriteria)) {
                    serachCriteria = serachCriteria + " and ";
                }
                serachCriteria = serachCriteria  + "vcat.updated_date between to_date('" + startDate +"','mm/dd/yyyy')" + " and to_date('" +
                                 endDate + "','mm/dd/yyyy')+1";

            }
            query.append(serachCriteria);
            if(null!=deptOnly && deptOnly.equals("true")) {
              query.append(" and  vcat.vendor_catalog_id in (select distinct (vcatdept.vendor_catalog_id)  from vendor_catalog_dept vcatdept " +
                           "inner join car_user_department  user_dept on ");
              query.append("vcatdept.dept_id = user_dept.dept_id where user_dept.car_user_id="+userId+")");
            }

            if(null !=department && !StringUtils.isBlank(department)) {
               query.append(" and  vcat.vendor_catalog_id in (select distinct (vcatdept.vendor_catalog_id)  from vendor_catalog_dept vcatdept ");
                query.append(" where  vcatdept.dept_id in (select dept_id from department where dept_cd in ('" + department +"')))");
            }

            if(log.isDebugEnabled()) {
                log.debug(query.toString());
                log.debug("End 'getSearchCriteriaSQLStringForVendorCatalogs' method");
                }
            return query.toString();
        }
        /**
         * Get The List of Open Catalogs Using Search Criteria.
         * @return List<VendorCatalog>
         */
	public List<VendorCatalog> searchOpenCatalogs(CatalogVendorDTO searchCriteria) {
                if(log.isDebugEnabled()) {
                log.debug("Start 'searchOpenCatalogs' method");
                }
		List<VendorCatalog> vendorCatalogList = new ArrayList<VendorCatalog>();
		vendorCatalogList=getHibernateTemplate().find(getSearchCriteriaSQLStringForOpenCatalogs(searchCriteria));
                // If User has Performaed search on User Department Only Then
                if(searchCriteria.getUserDeptOnly() !=null && searchCriteria.getUserDeptOnly().equals("true")) {
                List <Long> catalogIdList = new ArrayList<Long>();
                SessionFactory sf = getHibernateTemplate().getSessionFactory();
                String query ="select distinct (vcatdept.vendor_catalog_id) as vendorCatalogId from vendor_catalog_dept vcatdept inner join car_user_department  user_dept on vcatdept.dept_id = user_dept.dept_id where user_dept.car_user_id=:USER_ID";
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sqlQuery =(SQLQuery) session.createSQLQuery(query).addScalar("vendorCatalogId", Hibernate.LONG);
			sqlQuery.setString("USER_ID",searchCriteria.getUserId());
			
			catalogIdList = sqlQuery.list();
		} catch (Exception e) {
			log.error("Hibernate Exception caught in searchOpenCatalogs() -"
					+  e);

		}
                List <VendorCatalog> removeVendorCatalogList = new ArrayList <VendorCatalog>();
                Iterator itr = vendorCatalogList.iterator();
                while(itr.hasNext()) {
                    VendorCatalog catalog =  (VendorCatalog) itr.next();
                    if(!catalogIdList.contains(catalog.getVendorCatalogID())) {
                        removeVendorCatalogList.add(catalog);
                    }

                }
                //Remove All the Catalogs which are not associated with User Departments.
                vendorCatalogList.removeAll(removeVendorCatalogList);
                }
                 if(log.isDebugEnabled()) {
                log.debug("End 'searchOpenCatalogs' method Vendor Catalog List Size- >" + vendorCatalogList.size());
                }
		return vendorCatalogList;
	}
        /**
         * Get The List of All Open Catalogs.
         * @return List<VendorCatalog>
         */
	public List<VendorCatalog> getOpenCatalogs() {
                if(log.isDebugEnabled()) {
                log.debug("Start 'getOpenCatalogs' method");
                }
		List<VendorCatalog> openCatalogList = new ArrayList<VendorCatalog>();
		openCatalogList=getHibernateTemplate().find("FROM VendorCatalog where statusCD !='Complete' and statusCD !='Cancelled'"); //  where statusCD in ('Importing','Data Mapping')
                if(log.isDebugEnabled()) {
                log.debug("End 'getOpenCatalogs' method");
                }
		return openCatalogList;

	}
        /**
         * Get The Search Critera SQL String for Open Catalog Search.
         * @param searchCriteriaDTO CatalogVendorDTO
         * @return String
         */
	private String getSearchCriteriaSQLStringForOpenCatalogs(CatalogVendorDTO searchCriteriaDTO) {
             if(log.isDebugEnabled()) {
                log.debug("Start 'getSearchCriteriaSQLStringForOpenCatalogs' method");
                }
		String serachCriteria = "";
		String vendorId = searchCriteriaDTO.getVendorId();
		String vendorName= searchCriteriaDTO.getName();
		String status = searchCriteriaDTO.getStatus();

		if(null != vendorId && !StringUtils.isBlank(vendorId)) {
			serachCriteria = serachCriteria  + "vendor.vendorNumber ='" +  vendorId+"'";

		}
		if(null != vendorName && !StringUtils.isBlank(vendorName)) {
			if( !StringUtils.isBlank(serachCriteria)) {
				serachCriteria = serachCriteria + " and ";
			}
			serachCriteria = serachCriteria  + "upper(vendor.name) like '%" +  vendorName.toUpperCase() +"%' ";
		}

		if(null != status && !StringUtils.isBlank(status) && !status.equals("All")) {
			if( !StringUtils.isBlank(serachCriteria)) {
				serachCriteria = serachCriteria + " and ";
			}

			serachCriteria = serachCriteria  + "statusCD ='" +  status +"'";

		}
		if( !StringUtils.isBlank(serachCriteria)) {
			serachCriteria = " FROM VendorCatalog where statusCD !='Complete' and statusCD !='Cancelled' and  " + serachCriteria + " ";
		}
		else {
			serachCriteria = " FROM VendorCatalog where statusCD !='Complete' and statusCD !='Cancelled'";
		}
		 if(log.isDebugEnabled()) {
                log.debug("serachCriteria->" + serachCriteria);
                log.debug("Start 'getSearchCriteriaSQLStringForOpenCatalogs' method");
                }

		return serachCriteria;
	}

        /**
         *  Lock/Unlock a Vendor Catalog.
         * @param catalogVendorDTO CatalogVendorDTO
         * @param vendorCatalogId String
         * @param user User
         * @param mode String
         * @return List<VendorCatalogDTO>
         */
	/*public List<VendorCatalogDTO> lockUnlockCatalog(CatalogVendorDTO catalogVendorDTO,String vendorCatalogId,User user,String mode) {
                if(log.isDebugEnabled()) {
                log.debug("Start 'lockUnlockCatalog' method");
                }
		List<VendorCatalogDTO> vendorCatalogList = new ArrayList<VendorCatalogDTO>();
		try {
			VendorCatalog catalog = (VendorCatalog) getHibernateTemplate().get(VendorCatalog.class, Long.parseLong(vendorCatalogId));
			if(DropShipConstants.LOCK.equals(mode)) {
				catalog.setLockedBy(user.getUsername());
			} else {
				catalog.setLockedBy(null);
			}
			catalog.setAuditInfo(user);
			this.getHibernateTemplate().saveOrUpdate(catalog);
			getHibernateTemplate().flush();
                        //Get The List of Vendor Catalogs using search criteria.
			vendorCatalogList = searchVendorCatalogs(catalogVendorDTO);
		} catch(Exception e) {
                    log.error("Hibernate Exception in lockUnlockCatalog -" + e);
			//e.printStackTrace();
		}
                if(log.isDebugEnabled()) {
                log.debug("End 'lockUnlockCatalog' method");
                }
		return vendorCatalogList;
	}*/
        /**
         * Lock/Unlock a Open Catalog.
         * @param catalogVendorDTO CatalogVendorDTO
         * @param vendorCatalogId String
         * @param user User
         * @param mode String
         * @return List<VendorCatalog>
         */
	/*public List<VendorCatalog> lockUnlockOpenCatalog(CatalogVendorDTO catalogVendorDTO,String vendorCatalogId,User user,String mode) {
                if(log.isDebugEnabled()) {
                log.debug("Start 'lockUnlockOpenCatalog' method");
                }
		List<VendorCatalog> vendorCatalogList = new ArrayList<VendorCatalog>();
		try {

                        vendorCatalogList = searchOpenCatalogs(catalogVendorDTO);
			VendorCatalog catalog = (VendorCatalog) getHibernateTemplate().get(VendorCatalog.class, Long.parseLong(vendorCatalogId));
			if(DropShipConstants.LOCK.equals(mode)) {
				catalog.setLockedBy(user.getUsername());
			} else {
				catalog.setLockedBy(null);
			}
			catalog.setAuditInfo(user);
			this.getHibernateTemplate().saveOrUpdate(catalog);
			getHibernateTemplate().flush();
		} catch(Exception e) {
		   log.error("Hibdernate Exception in lockUnlockOpenCatalog -" + e)	;
                   // e.printStackTrace();
		}
                 if(log.isDebugEnabled()) {
                log.debug("End 'lockUnlockOpenCatalog' method");
                }
		return vendorCatalogList;
	}*/

	/**
	 * Returns the catalog level info based on the catalog id that is passed.
	 * @param CatalogId
	 * @return
	 */
	public VendorCatalog getCatalogInfo(long catalogId){
		return (VendorCatalog) getHibernateTemplate().get(VendorCatalog.class, catalogId);
	}

	public VendorCatalogTemplate getVendorTemplate(long VendorId){
		return (VendorCatalogTemplate) getHibernateTemplate().find("from VendorCatalogTemplate where ", "tt");
	}

	/**
	 * Gets all the attributes that are related to the product types and classifications
	 * that are related based on the product group.
	 *
	 * @param productGroupId
	 * @param Long
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getBlueMartiniAttribute(String productGroupIds, Long catalogID){

		List<String> bmAttribute = new ArrayList<String>(0);
		StringBuffer query= new StringBuffer();
		query.append("select * from (select atr.blue_martini_attribute from attribute atr ")
		.append(" inner join product_type_attribute pta on atr.attr_id=pta.attr_id ")
		.append(" inner join PROD_GROUP_PROD_TYPE pgt on pgt.product_type_id= pta.product_type_id ")
		.append(" and pgt.status_cd='ACTIVE' where atr.status_cd='ACTIVE' and pgt.product_group_id in (")
		.append(productGroupIds)
		.append(") UNION ")
		.append(" select atr.blue_martini_attribute from attribute atr ")
		.append(" inner join class_attribute ca on atr.attr_id= ca.attr_id ")
		.append(" inner join product_type_class ptc on ptc.class_id= ca.class_id ")
		.append(" inner join PROD_GROUP_PROD_TYPE pgt on ptc.product_type_id= pgt.product_type_id ")
		.append(" and pgt.status_cd='ACTIVE' where atr.status_cd='ACTIVE' and pgt.product_group_id in ( ")
		.append(productGroupIds)
		.append(") UNION ")
		.append(" select atr.blue_martini_attribute from attribute atr ")
		.append(" inner join department_attribute da ON atr.attr_id = da.attr_id ")
		.append(" inner join vendor_catalog_dept vcd ON vcd.dept_id = da.dept_id AND vcd.status_cd='ACTIVE'")
		.append(" WHERE atr.status_cd='ACTIVE' AND vcd.vendor_catalog_id=")
		.append(catalogID)
		.append(")");


		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sqlQuery = session.createSQLQuery(query.toString());
			bmAttribute = sqlQuery.list();
		} catch (Exception e) {
			log.error("Hibernate Exception caught in getBlueMartiniAttribute() productGroup-"
					+ productGroupIds, e);

		}
		return bmAttribute;

	}
        
               /**
         * Get The count of styles for each Catalog Vendor.
         * @return List<VendorCatalogStyleCount>
         */
	private List<VendorCatalogStyleCount> getStyleCount() {
		if(log.isDebugEnabled()) {
                    log.debug("Start 'getStyleCount' method");
                }
                StringBuffer query= new StringBuffer();
		List <VendorCatalogStyleCount>  styleCountList =  new ArrayList <VendorCatalogStyleCount> ();
		query.append("select count(sty) as styleCount,vendor_id as vendorId from (select distinct(vendor_style_id)" +
                             " as sty, vendor_id from vndr_catl_sty_sku_mast) group by vendor_id ");
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query.toString()).
                                addScalar("styleCount",Hibernate.STRING).
                                addScalar("vendorId",Hibernate.STRING).
                                setResultTransformer(Transformers.aliasToBean(VendorCatalogStyleCount.class));
			styleCountList = sQuery.list();
		} catch (Exception e) {
			log.error("Hibernate Exception caught in getStyleCount");
			e.printStackTrace();

		}
                if(log.isDebugEnabled()) {
                    log.debug("End 'getStyleCount' method");
                }
		return styleCountList;
	}
        /**
         * Get SKU count for each Catalog Vendor.
         * @return List<VendorCatalogSKUCount>
         */
	private List<VendorCatalogSKUCount> getSKUCount() {
                 if(log.isDebugEnabled()) {
                    log.debug("Start 'getSKUCount' method");
                }
		StringBuffer query= new StringBuffer();
		List <VendorCatalogSKUCount>  skuCountList =  new ArrayList <VendorCatalogSKUCount> ();
		query.append("select count(sku) as skuCount,vendor_id as vendorId from (select distinct(vendor_upc) as sku, vendor_id " +
                             "from vndr_catl_sty_sku_mast where vendor_upc !='0') group by vendor_id ");
		//this.getHibernateTemplate().find(query.toString());
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query.toString()).
                                          addScalar("skuCount",Hibernate.STRING).
                                          addScalar("vendorId",Hibernate.STRING).
                                          setResultTransformer(Transformers.aliasToBean(VendorCatalogSKUCount.class));
			skuCountList = sQuery.list();
		} catch (Exception e) {
			log.error("Hibernate Exception caught in getSKUCount");
			e.printStackTrace();

		}
		if(null != skuCountList && !skuCountList.isEmpty()){
			VendorCatalogSKUCount catalogSKUCount =(VendorCatalogSKUCount) skuCountList.get(0);

			if(log.isDebugEnabled()){
				log.debug("Count-> "+ catalogSKUCount.getSkuCount());
				log.debug("Count Vendpor id-> "+ catalogSKUCount.getVendorId());
			}
		}
                  if(log.isDebugEnabled()) {
                    log.debug("End 'getSKUCount' method");
                }
		return skuCountList;
	}

	/**
         * Get Search Criteria SQL String For Vendor Style Details.
         * @param styleDTO VendorCatalogStyleDTO
         * @return String
         */
	private String getSearchCriteriaForVendorStyleDetails(VendorCatalogStyleDTO styleDTO) {
              if(log.isDebugEnabled()) {
                   log.debug("Start 'getSearchCriteriaForVendorStyleDetails' method");
                }
                String vendorId = styleDTO.getVendorId();
		String vendorStyleId = styleDTO.getVendorStyleId();
		String vendorCatalogId = styleDTO.getVendorCatalogId();
		String userDerartmentOnly =styleDTO.getUserDerartmentOnly();
		String dateUpdateStart = styleDTO.getDateUpdateStart();
		String dateUpdateEnd = styleDTO.getDateUpdateEnd();
		String userId =styleDTO.getUserId();
		String description =styleDTO.getDescription();
		String department = styleDTO.getDepartment();

		StringBuffer query= new StringBuffer();
		query.append("select vcat.vendor_catalog_name as vendorCatalogName , vcss.updated_by as updatedBy, ");
                query.append("to_char(vcss.updated_date,'mm/dd/yyyy') as updatedDate, vcss.locked_by as lockedBy,");
                query.append("max (vcss.updated_date), vcssi.image_file_name as imagePath,vcss.description as description,");
                query.append("vcss.record_number as recordNum,vcat.vendor_catalog_tmpl_id as catalogTemplateId,vcss.vendor_upc as vendorUPC,");
                query.append("vcat.status_cd as catalogStatus ");
		query.append("from vendor_catalog vcat inner join vendor_catalog_sty_sku vcss on vcat.vendor_catalog_id = vcss.vendor_catalog_id ");
		query.append("inner join vndr_catl_sty_sku_image vcssi on vcssi.vendor_style_id = vcss.vendor_style_id ");
		query.append("where vcat.vendor_id =" +vendorId +  " and  vcat.vendor_catalog_id = " + vendorCatalogId);
                query.append(" and vcss.vendor_style_id='" +vendorStyleId +  "' and vcssi.image_type ='MAIN' ");
		String serachCriteria = "";

		if(null != description && !StringUtils.isBlank(description)) {
			serachCriteria = serachCriteria  + "vcss.description like'%" +  description +"%' ";

		}

		if(null != dateUpdateStart && !StringUtils.isBlank(dateUpdateStart) && null != dateUpdateEnd
                        && !StringUtils.isBlank(dateUpdateEnd)) {
			if( !StringUtils.isBlank(serachCriteria)) {
				serachCriteria = serachCriteria + " and ";
			}
			serachCriteria = serachCriteria  + "vcss.updated_date between to_date('" + dateUpdateStart +"','mm/dd/yyyy')" +
                                         " and to_date('" + dateUpdateEnd + "','mm/dd/yyyy')+1";

		}
		if(null != department && !StringUtils.isBlank(department)) {
			if( !StringUtils.isBlank(serachCriteria)) {
				serachCriteria = serachCriteria + " and ";
			}
			serachCriteria = serachCriteria  + "vcss.dept_id=" +  department;

		}
		if(null != userDerartmentOnly && userDerartmentOnly.equals("true")) {
			if( !StringUtils.isBlank(serachCriteria)) {
				serachCriteria = serachCriteria + " and ";
			}

			serachCriteria = serachCriteria + "vcss.dept_id";
                        serachCriteria = serachCriteria +  " in ( select car_user_dept.dept_id from car_user_department car_user_dept where ";
                        serachCriteria = serachCriteria +"car_user_dept.car_user_id =" +userId +")";
		}
		if(!StringUtils.isBlank(serachCriteria)) {
			query.append(" and " + serachCriteria);
		}

		query.append("group by vcat.vendor_catalog_name,vcss.updated_by , vcss.updated_date, vcss.locked_by,vcssi.image_file_name ," +
                             "vcss.description,vcss.record_number,vcat.vendor_catalog_tmpl_id ,vcss.vendor_upc ,vcat.status_cd ");
		query.append("order by vcss.updated_date desc");
                 if(log.isDebugEnabled()) {
                     log.debug("Sql Query-> " + query.toString());
                     log.debug("End 'getSearchCriteriaForVendorStyleDetails' method");
                     log.debug("End 'getSearchCriteriaForVendorStyleDetails' method");
                }
		return query.toString();
	}


	/**
	 * Gets the Master catalog attribute from the table.
	 * @return
	 */
	public List<CatalogMasterAttribute> getCatalogMasterAttr(String filter){
		if (null == filter) {
			return getHibernateTemplate().find("from CatalogMasterAttribute");
		} else {
			return getHibernateTemplate().find("from CatalogMasterAttribute WHERE catalogMasterAttrName NOT IN(" + filter + ")");
		}
	}
        /**
         * Get Styles Of a particular Vendor Catalog.
         * @param styleDTO VendorCatalogStyleDTO
         * @return List <VendorCatalogStyleDTO>
         */
	public List <VendorCatalogStyleDTO> getVendorCatalogStyles(VendorCatalogStyleDTO styleDTO) {
             if(log.isDebugEnabled()) {
                    log.debug("Start 'getVendorCatalogStyles' method");
                }
		List <VendorCatalogStyleDTO> vendorCatalogStyleList =  new ArrayList <VendorCatalogStyleDTO> ();
                List <VendorCatalogStyleDTO> catalogStyleList =  new ArrayList <VendorCatalogStyleDTO> ();
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();

		String query = getSearchCriteriaForVendorCatalogStyle(styleDTO);
		try {

			SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query).
                                          addScalar("vendorStyleId",  Hibernate.STRING).
                                          addScalar("description", Hibernate.STRING).
                                          addScalar("numSKUs",  Hibernate.STRING).
                                          addScalar("dateLastUpdated",  Hibernate.STRING).
                                          addScalar("dateLastUpdated",  Hibernate.STRING).
			                  addScalar("lastUpdatedBy", Hibernate.STRING).
                                          addScalar("lockedBy", Hibernate.STRING).
                                          addScalar("imagePath", Hibernate.STRING).
                                          addScalar("vendorCatalogId", Hibernate.STRING).
                                          addScalar("recordNum", Hibernate.STRING).
                                          addScalar("vendorUPC", Hibernate.STRING).
                                          addScalar("status", Hibernate.STRING).
                                          setResultTransformer(Transformers.aliasToBean(VendorCatalogStyleDTO.class)) ;
			vendorCatalogStyleList = sQuery.list();
			Iterator itr = vendorCatalogStyleList.iterator();
                        //Get the details of each vendor Styles
			while(itr.hasNext()) {
                            VendorCatalogStyleDTO catalogStyleDTO =  (VendorCatalogStyleDTO) itr.next();
				if(!catalogStyleList.contains(catalogStyleDTO)) {
                                    catalogStyleList.add(catalogStyleDTO);
                                }

			}
		} catch (Exception e) {
			log.error("Hibernate Exception caught in getVendorCatalogStyles -" + e);
		}
                if(log.isDebugEnabled()) {
                    log.debug("End 'getVendorCatalogStyles' method");
                }


		return catalogStyleList;
	}

        /**
         * Get search criteria SQL String for Vendor Catalog Style.
         * @param styleDTO VendorCatalogStyleDTO
         * @return String
         */
	private String getSearchCriteriaForVendorCatalogStyle(VendorCatalogStyleDTO styleDTO) {
		 if(log.isDebugEnabled()) {
                    log.debug("Start 'getSearchCriteriaForVendorCatalogStyle' method");
                }
                String vendorId = styleDTO.getVendorId();
		String vendorStyleId = styleDTO.getVendorStyleId();
		String vendorCatalogId = styleDTO.getVendorCatalogId();
		String userDerartmentOnly =styleDTO.getUserDerartmentOnly();
		String dateUpdateStart = styleDTO.getDateUpdateStart();
		String dateUpdateEnd = styleDTO.getDateUpdateEnd();
		String userId =styleDTO.getUserId();
		String description =styleDTO.getDescription();
		String department = styleDTO.getDepartment();
                String vendorUpc = styleDTO.getVendorUPC();
                String status = styleDTO.getStatus();

		StringBuffer query= new StringBuffer();
		query.append("select distinct(vcss.vendor_style_id) as vendorStyleId,vcss.description,count(vcss.vendor_upc) as numSKUs,");
                query.append(" max(to_char(vcss.updated_date,'mm/dd/yyyy')) as dateLastUpdated,vcss.updated_by as lastUpdatedBy ,");
		query.append(" vcss.locked_by as lockedBy ,vcssi.image_file_name as imagePath, vcss.vendor_catalog_id as vendorCatalogId,");
                query.append("vcss.record_number as recordNum,vcss.vendor_upc as vendorUPC,vcssm.status_cd as status ");
		query.append("from vendor_catalog_sty_sku vcss right outer join vndr_catl_sty_sku_image vcssi on vcss.vendor_style_id = vcssi.vendor_style_id ");
		query.append("left outer join vndr_catl_sty_sku_mast vcssm on vcssm.vendor_style_id = vcss.vendor_style_id and vcssm.vendor_upc = vcssm.vendor_upc ");
		query.append("where vcssi.image_type ='MAIN' and vcss.vendor_catalog_id =" + vendorCatalogId + " and vcssi.VENDOR_CATALOG_ID ="+vendorCatalogId);
		String serachCriteria = "";


		if(null != vendorStyleId && !StringUtils.isBlank(vendorStyleId)) {
			serachCriteria = serachCriteria  + "UPPER(vcss.vendor_style_id) like'%" +  vendorStyleId.toUpperCase(Locale.US)+"%' " ;

		}
		if(null != description && !StringUtils.isBlank(description)) {
			if( !StringUtils.isBlank(serachCriteria)) {
				serachCriteria = serachCriteria + " and ";
			}
			serachCriteria = serachCriteria  + "UPPER(vcss.description) like'%" +  description.toUpperCase(Locale.US) +"%' " ;

		}

		if(null != dateUpdateStart && !StringUtils.isBlank(dateUpdateStart)
                        && null != dateUpdateEnd && !StringUtils.isBlank(dateUpdateEnd)) {
			if( !StringUtils.isBlank(serachCriteria)) {
				serachCriteria = serachCriteria + " and ";
			}
			serachCriteria = serachCriteria  + "vcss.updated_date between to_date('" + dateUpdateStart +"','mm/dd/yyyy')" +
                                         " and to_date('" + dateUpdateEnd + "','mm/dd/yyyy')+1";

		}

		if(null != department && !StringUtils.isBlank(department)) {
			if( !StringUtils.isBlank(serachCriteria)) {
				serachCriteria = serachCriteria + " and ";
			}
			serachCriteria = serachCriteria  + "vcss.dept_id in (select dept_id from department where dept_cd in ('" + department +"')) ";

		}
		if(null != userDerartmentOnly && userDerartmentOnly.equals("true")) {
			if( !StringUtils.isBlank(serachCriteria)) {
				serachCriteria = serachCriteria + " and ";
			}

			serachCriteria = serachCriteria + "vcss.dept_id in ( select car_user_dept.dept_id from car_user_department ";
                        serachCriteria = serachCriteria +"car_user_dept where car_user_dept.car_user_id =" +userId +")";

		}
                if(null!=vendorUpc && !StringUtils.isBlank(vendorUpc)) {
                  if( !StringUtils.isBlank(serachCriteria)) {
				serachCriteria = serachCriteria + " and ";
			}

                  serachCriteria = serachCriteria  + "vcss.vendor_upc='" + StringUtils.leftPad(vendorUpc, 13, DropShipConstants.ZERO) +"'";
                }
                if(null!=status && !StringUtils.isBlank(status)) {
                  if( !StringUtils.isBlank(serachCriteria)) {
				serachCriteria = serachCriteria + " and ";
			}
                  serachCriteria = serachCriteria  + "vcss.status_cd='" +  status+"'";
                }
		if(!StringUtils.isBlank(serachCriteria)) {
			query.append(" and " + serachCriteria);
		}

		query.append(" group by vcss.vendor_style_id,vcss.description,vcss.updated_by,vcss.locked_by,vcssi.image_file_name," +
                             "vcss.vendor_catalog_id,vcss.record_number,vcss.vendor_upc ,vcssm.status_cd ");
                if(log.isDebugEnabled()) {
                    log.debug("Sql Query-> " + query.toString());
                    log.debug("Start 'getSearchCriteriaForVendorCatalogStyle' method");
                }

		return query.toString();
	}
        /**
         * Lock/Unlock Vendor Catalog Style.
         * @param catalogStyleDTO VendorCatalogStyleDTO
         * @param vendorStyleId String
         * @param vendorCatalogId String
         * @param user User
         * @param mode String
         * @return List<VendorCatalogStyleDTO>
         */
	public List<VendorCatalogStyleDTO> lockUnlockVendorStyles(VendorCatalogStyleDTO styleDTO,String vendorStyleId,
                                                    String vendorCatalogId,String userId,String mode) {
		if(log.isDebugEnabled()) {
                    log.debug("Start 'lockUnlockVendorStyles' method");
                }
                String locked_by = null;
		String query = null;
		if(DropShipConstants.LOCK.equals(mode)) {
			locked_by =userId;
			query = "update vendor_catalog_sty_sku set locked_by ='"+locked_by +"', updated_by ='"+userId+"',updated_date = SYSDATE where vendor_catalog_id="+vendorCatalogId+" and vendor_style_id='"+vendorStyleId+"' ";
		} else {
			query = "update vendor_catalog_sty_sku set locked_by =null, updated_by ='"+userId+"',updated_date = SYSDATE where vendor_catalog_id="+vendorCatalogId+" and vendor_style_id='"+vendorStyleId+"' ";
		}
		List<VendorCatalogStyleDTO> vendorStyleList = new ArrayList<VendorCatalogStyleDTO>();

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();

		//Need to change this method
		try {
                        //Updaet the Locked by field of Vendor Catalog Style.
			int temp= session.createSQLQuery(query).executeUpdate();
			//Fetch Vendor Catalog Styles List from Database.
                        vendorStyleList = getVendorStyles(styleDTO);
		} catch(Exception e) {
                    log.error("Hibernate Exception in  lockUnlockVendorStyles method -" + e);
		}
                if(log.isDebugEnabled()) {
                    log.debug("End 'lockUnlockVendorStyles' method");
                }
		return vendorStyleList;
	}
	//******************************** START METHOD FOR STYLE PROERTIES SCREEN*****************************

	/**\
	 *  Method to get the master attribute mapping for a particular catalog.
	 *  @return List<NameValue>
	 */

	public List<NameValue> getMasterAttributeMapping(String catalogTemaplateId,String vendorCatalogId) {
                 if(log.isDebugEnabled()) {
                    log.debug("Start 'getMasterAttributeMapping' method");
                }
		List <NameValue> mastAttributeMappingList = new ArrayList <NameValue> ();
		StringBuffer query= new StringBuffer();
		query.append("select cma.catalog_master_attr_name as attributeName,  vch.vndr_catl_hdr_fld_num as headerNumber ");
		query.append("from master_attribute_mapping  mam inner join catalog_master_attribute cma " );
                query.append("on cma.catalog_master_attr_id = mam.catalog_master_attr_id and mam.status_cd='ACTIVE' ");
		query.append("inner join vendor_catalog_header vch on mam.vndr_catl_hdr_fld_name= vch.vndr_catl_hdr_fld_name ");
                query.append("where mam.catalog_template_id = :CATALOG_TEMPLATE_ID" );
                query.append(" and mam.catalog_master_attr_id = 2 and vch.vendor_catalog_id= :VENDOR_CATALOG_ID");
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query.toString()).
                                  addScalar("attributeName",  Hibernate.STRING).
                                  addScalar("headerNumber", Hibernate.STRING).
                                  setResultTransformer(Transformers.aliasToBean(NameValue.class)) ;
		sQuery.setString("CATALOG_TEMPLATE_ID", catalogTemaplateId);
		sQuery.setString("VENDOR_CATALOG_ID", vendorCatalogId);
		mastAttributeMappingList = sQuery.list();
                 if(log.isDebugEnabled()) {
                    log.debug("End 'getMasterAttributeMapping' method");
                }
		return mastAttributeMappingList;

	}
	/**\
	 *  Method to get the catalog attribute mapping for a particular catalog.
	 *  @return List<NameValue>
	 */
	public List<NameValue> getCatalogAttributeMapping(String catalogTemaplateId) {
                 if(log.isDebugEnabled()) {
                    log.debug("Start 'getCatalogAttributeMapping' method");
                }
		List <NameValue> mastAttributeMappingList = new ArrayList <NameValue> ();
		StringBuffer query= new StringBuffer();
		query.append("select vcfm.vndr_catl_hdr_fld_name as attributeName,vch.vndr_catl_hdr_fld_num as headerNumber," );
                query.append(" vcfm.blue_martini_attribute as blueMartiniAttribute from vendor_catalog_fld_mapping vcfm inner join ");
		query.append(" vendor_catalog_header vch on vch.vendor_catalog_header_id = vcfm.vendor_catalog_header_id");
                query.append(" where vcfm.vendor_catalog_tmpl_id = :CATALOG_TEMPLATE_ID ");
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query.toString()).
                                  addScalar("attributeName",  Hibernate.STRING).
                                  addScalar("headerNumber", Hibernate.STRING).
                                  addScalar("blueMartiniAttribute", Hibernate.STRING).
                                  setResultTransformer(Transformers.aliasToBean(NameValue.class)) ;
		sQuery.setString("CATALOG_TEMPLATE_ID", catalogTemaplateId);
		mastAttributeMappingList = sQuery.list();
                  if(log.isDebugEnabled()) {
                    log.debug("End 'getCatalogAttributeMapping' method");
                }
		return mastAttributeMappingList;

	}
        /**
         * Get a particular Record Values of a catalog from Database.
         * @param vendorCatalogId String
         * @param recordNumber String
         * @return List<String>
         */
	public List<String>  getCatalogRecord(String vendorCatalogId,String recordNumber) {
                if(log.isDebugEnabled()) {
                    log.debug("Start 'getCatalogRecord' method");
                }
		List <String> recordValueList = new ArrayList <String> ();
		StringBuffer query= new StringBuffer();
		query.append("select vndr_catl_field_value from vendor_catalog_record where vendor_catalog_id = :VENDOR_CATALOG_ID"+
                               " and vndr_catl_record_num = :RECORD_NUM order by vndr_catl_hdr_fld_num asc");
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query.toString()) ;
		sQuery.setString("VENDOR_CATALOG_ID", vendorCatalogId);
		sQuery.setString("RECORD_NUM", recordNumber);
		recordValueList = sQuery.list();
                if(log.isDebugEnabled()) {
                    log.debug("End 'getCatalogRecord' method");
                }
		return recordValueList;

	}
        /**
         * Get the List of Images for a particular Style Or Sku.
         * @param vendorCatalogId String
         * @param vendorStyleId String
         * @param vendorUPC String
         * @return List<VendorStyleImageDTO>
         */
	public List<VendorStyleImageDTO> getStyleSKUImages(String vendorCatalogId,String vendorStyleId,String vendorUPC) {
		if(log.isDebugEnabled()) {
                    log.debug("Start 'getStyleSKUImages' method");
                }
                List <VendorStyleImageDTO> imageList = new ArrayList <VendorStyleImageDTO> ();
		StringBuffer query= new StringBuffer();
		query.append("select image_type as imageType, image_file_name as imageName,status_cd as ");
                query.append("status,to_char(updated_date,'mm/dd/yyyy')  as updatedDate , updated_by as updatedBy , ");
		query.append("vendor_catalog_id as vendorCatalogId , vendor_style_id as vendorStyleId , vendor_upc as vendorUPC ," );
                query.append(" vndr_catl_sty_sku_image_id as imageId from vndr_catl_sty_sku_image where vendor_catalog_id ="+ vendorCatalogId);
		query.append(" and vendor_style_id = '" + vendorStyleId + "' and status_cd='ACTIVE' ");
		if(vendorUPC !=null && !StringUtils.isBlank(vendorUPC)) {
			query.append(" and vendor_upc='" + StringUtils.leftPad(vendorUPC, 13, DropShipConstants.ZERO)+"'");
		}
		query.append(" order by updated_date");
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query.toString()).
                                   addScalar("imageType",  Hibernate.STRING).
                                   addScalar("imageName", Hibernate.STRING).
                                   addScalar("status", Hibernate.STRING).
                                   addScalar("updatedDate", Hibernate.STRING).
                                   addScalar("updatedBy", Hibernate.STRING).
                                   addScalar("vendorCatalogId", Hibernate.STRING).
                                   addScalar("vendorStyleId", Hibernate.STRING).
                                   addScalar("vendorUPC", Hibernate.STRING).
                                   addScalar("imageId", Hibernate.STRING).
                                   setResultTransformer(Transformers.aliasToBean(VendorStyleImageDTO.class)) ;
		imageList = sQuery.list();
                if(log.isDebugEnabled()) {
                    log.debug("Sql Query-> " + query.toString());
                    log.debug("Image List Size " + query.toString());
                    log.debug("End 'getStyleSKUImages' method");
                }
		return imageList;

	}
        /**
         * Get The List of UPC for a particular Style.
         * @param vendorId String
         * @param vendorStyleId String
         * @return List<VendorUpcDTO>
         */
	public List<VendorUpcDTO> getUPCList(String vendorId,String vendorStyleId) {
		if(log.isDebugEnabled()) {
                    log.debug("Start 'getUPCList' method");
                }
                List <VendorUpcDTO> imageList = new ArrayList <VendorUpcDTO> ();
		StringBuffer query= new StringBuffer();
		query.append("select vcssm.vendor_upc as vendorUPC,vcssm.color as color, vcssm.status_cd as status, vcat.vendor_catalog_name ");
                query.append("as catalogName,vcss.description as description,to_char(vcss.updated_date,'mm/dd/yyyy') as updatedDate,  ");
		query.append("vcss.updated_by as updatedBy, vcss.record_number as recordNum,vcssm.vendor_catalog_id as vendorCatalogId, ");
		query.append("vcssm.vendor_style_id as vendorStyleId,rownum as indx,vcat.vendor_catalog_tmpl_id as catalogTemplateId  ") ;
		query.append("from vndr_catl_sty_sku_mast vcssm inner join vendor_catalog vcat on vcssm.vendor_catalog_id = vcat.vendor_catalog_id ");
		query.append("left outer join vendor_catalog_sty_sku vcss on vcss.vendor_catalog_id = vcssm.vendor_catalog_id ");
                query.append("and vcss.vendor_style_id = vcssm.vendor_style_id and vcss.vendor_upc = vcssm.vendor_upc  where vcssm.vendor_upc !='0'");
		query.append(" and vcssm.vendor_style_id =:VENDORSTYLE_ID and vcssm.vendor_id = :VENDOR_ID order by vcss.updated_date");
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query.toString()).
                                   addScalar("vendorUPC",  Hibernate.STRING).
                                   addScalar("color", Hibernate.STRING).
                                   addScalar("status", Hibernate.STRING).
                                   addScalar("catalogName", Hibernate.STRING).
                                   addScalar("description", Hibernate.STRING).
                                   addScalar("updatedDate", Hibernate.STRING).
                                   addScalar("updatedBy", Hibernate.STRING).
                                   addScalar("recordNum", Hibernate.STRING).
		                   addScalar("vendorCatalogId", Hibernate.STRING).
                                   addScalar("vendorStyleId", Hibernate.STRING).
                                   addScalar("indx", Hibernate.STRING).
                                   addScalar("catalogTemplateId", Hibernate.STRING).
                                   setResultTransformer(Transformers.aliasToBean(VendorUpcDTO.class)) ;
		sQuery.setString("VENDORSTYLE_ID", vendorStyleId);
		sQuery.setString("VENDOR_ID", vendorId);		
		imageList = sQuery.list();
                if(log.isDebugEnabled()) {
                    log.debug("End 'getUPCList' method");
                }
		return imageList;

	}
        /**
         * getCountForPreviousUploads
         * @param param
         * @return
         */
	public int getCountForPreviousUploads(Map<String, Object> param) {
		log.debug("Inside getCountForPreviousUploads() method in Dao Hibernate");
		long vendor_catelog_id =0 ;
		String vendor_style_id ="0";
		String vendor_upc="";
		String imageType="";
		ArrayList<String> query = new ArrayList<String>();

		StringBuffer sql = new StringBuffer();
		sql.append("from VendorCatalogStyleSkuImage where ");
		if(null != param.get("Vendor_Catalog_Id")){
			vendor_catelog_id = Long.parseLong(param.get("Vendor_Catalog_Id").toString());
			sql.append(" VENDOR_CATALOG_ID='"+vendor_catelog_id+"' ");
		}
		if(null != param.get("Vendor_Style_Id")){
			sql.append(" AND VENDOR_STYLE_ID='"+vendor_style_id+"' ");
		}
		if(null != param.get("Vendor_UPC")){
			vendor_upc = (param.get("Vendor_UPC").toString());
			sql.append(" AND VENDOR_UPC='"+vendor_upc+"' ");
		}
		if(null != param.get("imageType")){
			imageType = (String) param.get("imageType");
			sql.append(" AND IMAGE_TYPE='"+imageType+"' ");
		}
		if(log.isDebugEnabled()){
			log.debug("Query generated:"+ sql.toString());
		}
		return getHibernateTemplate().find(sql.toString()).size();
	}
	/**
	 * Saves or updates the passed vendor template
	 * @param vendorCatalogTemplate
	 * @return
	 */
	public VendorCatalogTemplate saveTemplate(VendorCatalogTemplate vendorCatalogTemplate) {
		if(log.isDebugEnabled()){
			log.debug("entering VendorCatalogDaoHibernate's save VendorCatalog method...");
		}
		getHibernateTemplate().saveOrUpdate(vendorCatalogTemplate);
		getHibernateTemplate().flush();
		return vendorCatalogTemplate;
	}
        /**
         * Save Vendor Catalog Style Sku Image
         * @param vendorCatalogStyleSkuImage
         * @return
         */
	public VendorCatalogStyleSkuImage saveVendorCatalogStyleSkuImageObject(VendorCatalogStyleSkuImage vendorCatalogStyleSkuImage) {
		if(log.isDebugEnabled()) {
                    log.debug("Start 'saveVendorCatalogStyleSkuImageObject' method");
                }
		getHibernateTemplate().saveOrUpdate(vendorCatalogStyleSkuImage);
		getHibernateTemplate().flush();

                if(log.isDebugEnabled()) {
                    log.debug("End 'saveVendorCatalogStyleSkuImageObject' method");
                }
                return vendorCatalogStyleSkuImage;
	}

	/**
	 * Saves the vendor catalog data mapping to the Vendor catalog template
	 */
	public List<VendorCatalogFieldMapping> saveVendorCatalogFieldMapping(List<VendorCatalogFieldMapping> lstVendorCatalogFieldMapping){
		List<VendorCatalogFieldMapping> retVendorCatalogFieldMapping = new ArrayList<VendorCatalogFieldMapping>();
		for(VendorCatalogFieldMapping vendorCatalogFieldMapping : lstVendorCatalogFieldMapping){
			getHibernateTemplate().saveOrUpdate(vendorCatalogFieldMapping);
			retVendorCatalogFieldMapping.add(vendorCatalogFieldMapping);
		}
		getHibernateTemplate().flush();
		return retVendorCatalogFieldMapping;
	}
        /**
         *  Save the Vendor Style properties.
         * @param stylePropertiesDTO VendorStylePropertiesDTO
         * @param user User
         * @return boolean
         */
	public boolean saveStyleProperties(VendorStylePropertiesDTO stylePropertiesDTO,User user) {
                if(log.isDebugEnabled()) {
                    log.debug("Start 'saveStyleProperties' method");
                }
		boolean flag= false;
		String vendorCatalogId = stylePropertiesDTO.getVendorCatalogId();
		String recordNumber = stylePropertiesDTO.getRecordNum();
		String mappedFieldValues[] = stylePropertiesDTO.getMappedFieldsArr();
		//Update the Style level attaributes
		int len=0;
		int cnt=0;
		if(mappedFieldValues !=null) {
			len=stylePropertiesDTO.getMappedFieldsArr().length;
			String mappedAttributeHeaderNbr[] = stylePropertiesDTO.getMappedFieldSeq().split(",");
			while(cnt<len) {

				updateRecord(vendorCatalogId,recordNumber, mappedAttributeHeaderNbr[cnt], mappedFieldValues[cnt]);
				cnt++;
			}
                        saveStyleMaster(stylePropertiesDTO,user);

		}
		cnt=0;
		String unMappedFields[] = stylePropertiesDTO.getUnmappedFields();
		if(unMappedFields!=null) {
			len = unMappedFields.length;
			String unMappedAttributeHeaderNbr[] = stylePropertiesDTO.getUnMappedFieldSeq().split(",");
			while(cnt<len) {

				updateRecord(vendorCatalogId,recordNumber, unMappedAttributeHeaderNbr[cnt], unMappedFields[cnt]);
				cnt++;
			}
		}
		if(log.isDebugEnabled()){
			log.debug("Check UPC to edit or Not --> "+ stylePropertiesDTO.getUpcDetails());
		}
		VendorUpcDTO upcDTO = stylePropertiesDTO.getUpcDetails();
		// Check is there any UPC is to be update
		if(upcDTO !=null) {
			String vendorUpc= upcDTO.getVendorUPC().trim();
			String oldVendorUpc = upcDTO.getOldVendorUpc().trim();
			String color =upcDTO.getColor().trim();
			String oldColor= upcDTO.getOldColor().trim();
			//check whether the suer has changed value for vendor upc or color
			if(!vendorUpc.equals(oldVendorUpc) || !color.equals(oldColor)) {
				//update vendor upc
                                if(log.isDebugEnabled()) {
				log.debug("Call Update UPC method");
                                }
				flag= updateUpcDetails(upcDTO,user);
			} else {
				 if(log.isDebugEnabled()) {
                                log.debug("Nothing to Update at UPC level");
                                }
			}
		}
                 if(log.isDebugEnabled()) {
                    log.debug("End 'saveStyleProperties' method");
                }

		return flag;

	}
        /**
         * Update a particular vendor Catalog record.
         * @param vendorCatalogId String
         * @param recordNumber String
         * @param vendorCataloHeaderNbr String
         * @param val String
         */
	public void updateRecord(String vendorCatalogId,String recordNumber,String vendorCataloHeaderNbr,String val)  {
		if(log.isDebugEnabled()) {
                    log.debug("Start 'updateRecord' method");
                }
		CompositeKeyForVndrCatRecord compositeKeyForVndrCatRecord = new CompositeKeyForVndrCatRecord();
		compositeKeyForVndrCatRecord.setCatalogId(Long.parseLong(vendorCatalogId));
		compositeKeyForVndrCatRecord.setHeaderNum(Long.parseLong(vendorCataloHeaderNbr));
		compositeKeyForVndrCatRecord.setRecordNumber(Long.parseLong(recordNumber));

		VendorCatalogRecord catalogRecord =  new VendorCatalogRecord();
		catalogRecord =  (VendorCatalogRecord)this.getHibernateTemplate().get(VendorCatalogRecord.class, compositeKeyForVndrCatRecord);
		String fieldval= catalogRecord.getVendorCatalogFieldValue();
		catalogRecord.setVendorCatalogFieldValue(val);
		this.getHibernateTemplate().saveOrUpdate(catalogRecord);
		getHibernateTemplate().flush();
		if(log.isDebugEnabled()) {
                    log.debug("End 'updateRecord' method");
                }


	}
        /**
         * Get the Header Number of the coulms having vales of Vendor UPC and Color.
         * @param catalogTemplateId String
         * @return
         */
	public List<NameValue> getUPCHeaderNumber(String catalogTemplateId) {
                if(log.isDebugEnabled()) {
                    log.debug("Start 'getUPCHeaderNumber' method");
                }
		List <NameValue> headerNumberList = new ArrayList <NameValue> ();
		StringBuffer query= new StringBuffer();
		query.append("select vch.vendor_catalog_header_id as headerId, vch.vndr_catl_hdr_fld_num as headerNumber ");
                query.append("from master_attribute_mapping mam inner join vendor_catalog_header vch on mam.vndr_catl_hdr_fld_name = ");
		query.append(" vch.vndr_catl_hdr_fld_name where catalog_master_attr_id in(11,3) and catalog_template_id = :CATALOG_TMPL_ID order by catalog_master_attr_id desc");
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query.toString()).
                                   addScalar("headerId",Hibernate.STRING).
		                   addScalar("headerNumber",Hibernate.STRING).
                                   setResultTransformer(Transformers.aliasToBean(NameValue.class)) ;
		sQuery.setString("CATALOG_TMPL_ID", catalogTemplateId);
		headerNumberList = sQuery.list();
                if(log.isDebugEnabled()) {
                    log.debug("End 'getUPCHeaderNumber' method");
                }
		return headerNumberList;

	}

	/**
	 * Saves the groups associated with the Vendor catalog templates
	 */
	public CatalogGroupTemplate saveGroupTemplate(CatalogGroupTemplate catalogGroupTemplate){
		if(log.isDebugEnabled()){
			log.debug("Saving group template after association ");
		}
		getHibernateTemplate().saveOrUpdate(catalogGroupTemplate);
		getHibernateTemplate().flush();
		return catalogGroupTemplate;
	}

	/**
	 * Saves the passed master attribute list to the master attribute mapping.
	 * @param lstMasterAttributeMapping
	 * @return
	 */
	public List<MasterAttributeMapping> saveMasterMapping(List<MasterAttributeMapping> lstMasterAttributeMapping, Long templateId){
		if (null != templateId) {
			if(log.isDebugEnabled()){
				log.debug("inside saving Master Mapping Attribute list:"+lstMasterAttributeMapping);
			}
			List<MasterAttributeMapping> list = this
					.getTemplateMasterAttributeMapping(templateId);
			for (MasterAttributeMapping mapping : list) {
				if(!lstMasterAttributeMapping.contains(mapping)){
					if(log.isDebugEnabled()){
						log.debug("Mapping mapping attribute inactive:"+mapping.getCatalogMasterMappingId());
					}
					mapping.setStatus("INACTIVE");
				}
			}

			if (null != list && !list.isEmpty()) {
				getHibernateTemplate().saveOrUpdateAll(list);
			}
		}
		if(log.isDebugEnabled()){
			log.debug("saving Master Attribute Mapping list:"+lstMasterAttributeMapping);
		}
		if (lstMasterAttributeMapping != null && !lstMasterAttributeMapping.isEmpty()) {
		if(log.isDebugEnabled()){	
				log.debug("saving Master Attribute Mapping");
		}
			getHibernateTemplate().saveOrUpdateAll(lstMasterAttributeMapping);
		}
		getHibernateTemplate().flush();
		return lstMasterAttributeMapping;
	}

        /**
         * Update a particular UPC of a Vendor Catalog Style.
         * @param upcDto VendorUpcDTO
         * @param user User
         * @return boolean
         */
	public boolean updateUpcDetails(VendorUpcDTO upcDto,User user)  {
		if(log.isDebugEnabled()) {
                    log.debug("Start 'updateUpcDetails' method");
                }

		// Step 1 Update Master Tabel record
		//A. Check whether any record exists in Master table with new vendor upc and color cobmination.
                String vendorUpc = "0";
                
                if(!upcDto.getVendorUPC().equals(DropShipConstants.ZERO)) {
                    vendorUpc = StringUtils.leftPad(upcDto.getVendorUPC(), 13,DropShipConstants.ZERO);
                }
		VendorCatalogStyleSkuMaster catalogStyleSkuMaster1 =  new VendorCatalogStyleSkuMaster();
		VendorCatalogStyleSkuMasterId catalogStyleSkuMasterId1 =  new VendorCatalogStyleSkuMasterId();
		catalogStyleSkuMasterId1.setVendorId(Long.parseLong(upcDto.getVendorId()));
                catalogStyleSkuMasterId1.setVendorStyleId(upcDto.getVendorStyleId());
		catalogStyleSkuMasterId1.setVendorUPC(vendorUpc);
		// If already esists then need to check whether to update only vendor catalog id or show an error message.
		catalogStyleSkuMaster1 = (VendorCatalogStyleSkuMaster)this.getHibernateTemplate().get(VendorCatalogStyleSkuMaster.class, catalogStyleSkuMasterId1);
		if(catalogStyleSkuMaster1 !=null && !upcDto.getOldVendorUpc().equals(vendorUpc)) {
                           return false;
                       
		} else {
			//Step 2  Get the previous record from master table and update it.
			VendorCatalogStyleSkuMasterId catalogStyleSkuMasterId =  new VendorCatalogStyleSkuMasterId();
			VendorCatalogStyleSkuMaster catalogStyleSkuMaster  = new VendorCatalogStyleSkuMaster();
			catalogStyleSkuMasterId.setVendorId(Long.parseLong(upcDto.getVendorId()));
			//catalogStyleSkuMasterId.setVendorStyleId(Long.parseLong(upcDto.getVendorStyleId()));
                        catalogStyleSkuMasterId.setVendorStyleId(upcDto.getVendorStyleId());
			catalogStyleSkuMasterId.setVendorUPC(upcDto.getOldVendorUpc());
			catalogStyleSkuMaster.setColor(upcDto.getOldColor());
			catalogStyleSkuMaster = (VendorCatalogStyleSkuMaster)this.getHibernateTemplate().get(VendorCatalogStyleSkuMaster.class, catalogStyleSkuMasterId);
			catalogStyleSkuMasterId.setVendorUPC(vendorUpc);
			catalogStyleSkuMaster.setCompositeKey(catalogStyleSkuMasterId);
			catalogStyleSkuMaster.setColor(upcDto.getColor());
			catalogStyleSkuMaster.setVendorCatalogId(Long.parseLong(upcDto.getVendorCatalogId()));
			catalogStyleSkuMaster.setUpdatedDate(new Date());
			this.getHibernateTemplate().saveOrUpdate(catalogStyleSkuMaster);
			if(log.isDebugEnabled()) {
                        log.debug("Mater Table Updated");
                        }
		}
		//Step 2 Update the Vendor catalog style sku table.

		// Check whether the combination of vendor upc and color is already there in this particular catalog.
		VendorCatalogStyleSku catalogStyleSku =  new VendorCatalogStyleSku();
		VendorCatalogStyleSkuId catalogStyleSkuId =  new VendorCatalogStyleSkuId();
		catalogStyleSkuId.setVendorCatalogId(Long.parseLong(upcDto.getVendorCatalogId()));
		//catalogStyleSkuId.setVendorStyleId(Long.parseLong(upcDto.getVendorStyleId()));  Vendor Style Id long to String
                catalogStyleSkuId.setVendorStyleId(upcDto.getVendorStyleId());
		catalogStyleSkuId.setVendorUPC(vendorUpc);
		catalogStyleSku = (VendorCatalogStyleSku) this.getHibernateTemplate().get(VendorCatalogStyleSku.class, catalogStyleSkuId);
		if(catalogStyleSku!=null && !upcDto.getOldVendorUpc().equals(vendorUpc)) {
			return false;
		} else {
			//Update teh existing reord
			VendorCatalogStyleSku catalogStyleSku1 =  new VendorCatalogStyleSku();
			VendorCatalogStyleSkuId catalogStyleSkuId1 =  new VendorCatalogStyleSkuId();
			catalogStyleSkuId1.setVendorCatalogId(Long.parseLong(upcDto.getVendorCatalogId()));
                        catalogStyleSkuId1.setVendorStyleId(upcDto.getVendorStyleId());
			catalogStyleSkuId1.setVendorUPC(vendorUpc);
			catalogStyleSku1.setColor(upcDto.getOldColor());
			catalogStyleSku1 = (VendorCatalogStyleSku) this.getHibernateTemplate().get(VendorCatalogStyleSku.class, catalogStyleSkuId1);
			catalogStyleSku1.getCompositeKey().setVendorUPC(upcDto.getVendorUPC());
			catalogStyleSku1.setColor(upcDto.getColor());
			catalogStyleSku1.setAuditInfo(user);
			this.getHibernateTemplate().saveOrUpdate(catalogStyleSku1);
			if(log.isDebugEnabled()) {
                        log.debug("Style SKU Table Updated");
                        }

		}
		getHibernateTemplate().flush();
		//Step 3 Update the Vendor Catalog Record Table.

		//Update Vendor UPC
		updateRecord(upcDto.getVendorCatalogId(),upcDto.getRecordNum(), upcDto.getVendorUpcHeaderNbr(), upcDto.getVendorUPC());

		//Update Color
		updateRecord(upcDto.getVendorCatalogId(),upcDto.getRecordNum(), upcDto.getColorHeaderNbr(), upcDto.getColor());
		if(log.isDebugEnabled()) {
                    log.debug("End 'updateUpcDetails' method");
                }
		return true;


	}
        /**
         * Remove a Image associated with a Vendor Catalog Style/ SKU
         * @param imageId String
         * @param user User
         * @return boolean
         */
	public boolean removeImage(String imageId,User user) {
                 if(log.isDebugEnabled()) {
                    log.debug("Start 'removeImage' method");
                }
		VendorCatalogStyleSkuImage catalogStyleSkuImage =  new VendorCatalogStyleSkuImage();
		catalogStyleSkuImage = (VendorCatalogStyleSkuImage)this.getHibernateTemplate().get(VendorCatalogStyleSkuImage.class, new Long(imageId));
		catalogStyleSkuImage.setStatus(DropShipConstants.INACTIVE);
		catalogStyleSkuImage.setAuditInfo(user);
		this.getHibernateTemplate().saveOrUpdate(catalogStyleSkuImage);
		getHibernateTemplate().flush();
        if(log.isDebugEnabled()) {
            log.debug("End 'removeImage' method");
        }
		return false;
	}


	/**
	 * Gets the template master catalog attribute. Gets all the attributes that are not assigned
	 * for the template.
	 *
	 * @return
	 */
	public List<CatalogMasterAttribute> getTemplateCatalogMasterAttr(Long vendorCatalogID, String filter){
		List<CatalogMasterAttribute> catalogMasterAttributes = new ArrayList<CatalogMasterAttribute>(0);
		StringBuffer sqlQuery = new StringBuffer("select cma.CATALOG_MASTER_ATTR_ID as catalogMasterAttrId, ");
		sqlQuery
		.append(" cma.CATALOG_MASTER_ATTR_NAME as catalogMasterAttrName, ")
		.append(" cma.CATALOG_MASTER_ATTR_DESCR as catalogMasterAttrDescr, cma.STATUS_CD as status, ")
		.append(" cma.CREATED_BY as createdBy, cma.CREATED_DATE as createdDate, ")
		.append(" cma.UPDATED_BY as updatedBy, cma.UPDATED_DATE as updatedDate from catalog_master_attribute cma")
		.append(" where cma.catalog_master_attr_id not in(SELECT MAM.catalog_master_attr_id ")
		.append(" FROM VENDOR_CATALOG VC JOIN VENDOR_CATALOG_HEADER VCH ON (VC.VENDOR_CATALOG_ID=VCH.VENDOR_CATALOG_ID)")
		.append(" JOIN MASTER_ATTRIBUTE_MAPPING MAM ON (MAM.CATALOG_TEMPLATE_ID=VC.VENDOR_CATALOG_TMPL_ID AND MAM.VNDR_CATL_HDR_FLD_NAME=VCH.VNDR_CATL_HDR_FLD_NAME) and MAM.status_cd='ACTIVE' and VCH.vendor_catalog_id=:vendorCatalogID)");
		if(null != filter) {
			sqlQuery.append(" AND cma.CATALOG_MASTER_ATTR_NAME NOT IN (" + filter + ")");
		}

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery query = (SQLQuery)session.createSQLQuery(sqlQuery.toString())
			.addScalar("catalogMasterAttrId", Hibernate.LONG)
			.addScalar("catalogMasterAttrName")
			.addScalar("catalogMasterAttrDescr")
			.addScalar("status")
			.addScalar("createdBy")
			.addScalar("updatedBy")
			.addScalar("createdDate", Hibernate.DATE)
			.addScalar("updatedDate", Hibernate.DATE)
			.setResultTransformer(Transformers.aliasToBean(CatalogMasterAttribute.class));
			query.setLong("vendorCatalogID", vendorCatalogID);
			catalogMasterAttributes = query.list();

		} catch (Exception e) {
			log.error("Hibernate Exception caught in getTemplateCatalogMasterAttr() product ids-" + vendorCatalogID);
			log
			.error("Hibernate Exception caught in updateOldTypes() productGroup-"
					+ vendorCatalogID);
		}

		return catalogMasterAttributes;
	}

	/**
	 * Gets all the attributes that are related to the product types and classifications
	 * that are related based on the product group except that have already been mapped to
	 * the catalog template
	 *
	 * @param productGroupId, Vendor catalog id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getTemplateBlueMartiniAttribute(String productGroupIds, Long vendorCatalogFldMapping){

		List<String> bmAttribute = new ArrayList<String>(0);
		StringBuffer query= new StringBuffer(" select blue_martini_Attribute as bmname from (");
		query.append("select atr.blue_martini_attribute from attribute atr ")
		.append(" inner join product_type_attribute pta on atr.attr_id=pta.attr_id ")
		.append(" inner join PROD_GROUP_PROD_TYPE pgt on pgt.product_type_id= pta.product_type_id ")
		.append(" and pgt.status_cd='ACTIVE' where pgt.product_group_id in (")
		.append(productGroupIds)
		.append(") UNION ")
		.append(" select atr.blue_martini_attribute from attribute atr ")
		.append(" inner join class_attribute ca on atr.attr_id= ca.attr_id ")
		.append(" inner join product_type_class ptc on ptc.class_id= ca.class_id ")
		.append(" inner join PROD_GROUP_PROD_TYPE pgt on ptc.product_type_id= pgt.product_type_id ")
		.append(" and pgt.status_cd='ACTIVE' where pgt.product_group_id in ( ")
		.append(productGroupIds)
		//Getting the already selected blue martini attributes
		.append(") UNION ")
		.append(" select atr.blue_martini_attribute from attribute atr ")
		.append(" inner join department_attribute da ON atr.attr_id = da.attr_id ")
		.append(" inner join vendor_catalog_dept vcd ON vcd.dept_id = da.dept_id AND vcd.status_cd='ACTIVE'")
		.append(" WHERE atr.status_cd='ACTIVE' AND vcd.vendor_catalog_id=")
		.append(vendorCatalogFldMapping)
		.append(") where blue_martini_Attribute not in (")
		.append(" select blue_martini_attribute from ")
		.append(" vendor_catalog_fld_mapping vcfm, vendor_catalog_header vch where")
		.append(" vch.vendor_catalog_header_id = vcfm.vendor_catalog_header_id")
		.append(" and vch.vendor_catalog_id=:vendorCatalogFldMapping)");

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sqlQuery = session.createSQLQuery(query.toString());
			sqlQuery.setLong("vendorCatalogFldMapping",vendorCatalogFldMapping);
			bmAttribute = sqlQuery.list();
		} catch (Exception e) {
			log.error("Hibernate Exception caught in getBlueMartiniAttribute() productGroup-"
					+ productGroupIds, e);

		}
		return bmAttribute;

	}

	/**
	 * Returns the master attributes associated with the catalogs.
	 * @param catalogTemplateID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MasterAttributeMapping> getTemplateMasterAttributeMapping(Long catalogTemplateID){
		return (List<MasterAttributeMapping>) getHibernateTemplate().find("from MasterAttributeMapping where catalogTemplateId=? and status='ACTIVE' ", catalogTemplateID);
	}

	/**
	 * Gets the Vendor Catalog header values that have not yet been mapped.
	 * @param vendorCatalogID
	 * @param vendorCatalogTemplateID.
	 * @return List<CatalogHeaderDTO>
	 */
	public List<CatalogHeaderDTO> getTemplateVendorCatalogHeaderList(Long vendorCatalogID, Long vendorCatalogTemplateID){
		List<CatalogHeaderDTO> vendorCatalogHeadList = new ArrayList<CatalogHeaderDTO>(0);
		StringBuffer sqlQuery = new StringBuffer("select vch.vendor_catalog_header_id as vendorCatalogHeaderId, ");
		sqlQuery
		.append(" vch.vendor_catalog_id as vendorCatalogID, vch.VNDR_CATL_HDR_FLD_NUM as vendorCatalogFieldNum, ")
		.append(" vch.vndr_catl_hdr_fld_name as vendorCatalogHeaderFieldName ")
		.append(" from vendor_catalog_header vch ")
		.append(" WHERE vch.vndr_catl_hdr_fld_name NOT IN (SELECT mam.vndr_catl_hdr_fld_name FROM master_attribute_mapping mam WHERE mam.catalog_template_id =:vendorCatalogTemplateID AND mam.status_cd='ACTIVE' ) ")
		.append(" AND vch.vendor_catalog_header_id NOT IN (SELECT vcfm.vendor_catalog_header_id FROM vendor_catalog_fld_mapping vcfm WHERE vcfm.VENDOR_CATALOG_TMPL_ID =:vendorCatalogTemplateID)")
		.append(" AND  vch.vendor_catalog_id=:vendorCatalogID   order by vch.vndr_catl_hdr_fld_name");

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery query = (SQLQuery)session.createSQLQuery(sqlQuery.toString())
			.addScalar("vendorCatalogHeaderId", Hibernate.LONG)
			.addScalar("vendorCatalogID", Hibernate.LONG)
			.addScalar("vendorCatalogFieldNum", Hibernate.LONG)
			.addScalar("vendorCatalogHeaderFieldName")
			.setResultTransformer(Transformers.aliasToBean(CatalogHeaderDTO.class));
			query.setLong("vendorCatalogID", vendorCatalogID);
			query.setLong("vendorCatalogTemplateID", vendorCatalogTemplateID);
			vendorCatalogHeadList = query.list();
		} catch (Exception e) {
			log.error("Hibernate Exception caught in getTemplateVendorCatalogHeaderList() vendorCatalogID-" + vendorCatalogID);
			log
			.error("Hibernate Exception caught in updateOldTypes() productGroup-"
					+ vendorCatalogID);
		}

		return vendorCatalogHeadList;

	}

	/**
	 * Gets the existing mapped value for displaying it in the page.
	 * @param vendorCatalogID
	 * @param vendorCatalogTemplateID Long
	 * @return
	 */
	public List<CatalogMappedFieldDTO> getTemplateMappedValueList(Long vendorCatalogID, Long vendorCatalogTemplateID){
		List<CatalogMappedFieldDTO> mappedFieldDTOList = new ArrayList<CatalogMappedFieldDTO>(0);
		StringBuffer sqlQuery = new StringBuffer("select rownum as rowNumber, vhi as vendorSuppliedFieldID, ");
		sqlQuery
		.append(" vchfn as vendorSuppliedField,isMaster as isMasterRecord, mastID as masterAttributeID, cmastID as masterMappingId,  ")
		.append("  attrName as mappingAttribute, 'Y' as fromDatabase,  vchfnu as vendorSuppliedFieldNum from (")
		.append(" select vch.vendor_catalog_header_id as vhi, vch.vndr_catl_hdr_fld_name as vchfn, 'Y' as isMaster, ")
		.append(" cma.catalog_master_attr_id as mastID, mam.catalog_master_attr_id as cmastID, cma.catalog_master_attr_name as attrName, ")
		.append(" vch.vndr_catl_hdr_fld_num as vchfnu  from vendor_catalog_header vch ")
		.append(" inner join master_attribute_mapping mam on mam.vndr_catl_hdr_fld_name= vch.vndr_catl_hdr_fld_name")
		.append(" inner join catalog_master_attribute cma on cma.catalog_master_attr_id= mam.catalog_master_attr_id ")
		.append(" where vch.vendor_catalog_id=:vendorCatalogID AND mam.CATALOG_TEMPLATE_ID=:vendorCatalogTemplateID AND mam.status_cd='ACTIVE' ")
		.append(" union ")
		.append(" select vch.vendor_catalog_header_id as vhi, vch.vndr_catl_hdr_fld_name as vchfn, 'N' as isMaster,")
		.append(" null as mastID, null as cmastID, vcfm.blue_martini_attribute as attrName, vch.vndr_catl_hdr_fld_num as vchfnu")
		.append(" from vendor_catalog_header vch ")
		.append(" inner join vendor_catalog_fld_mapping vcfm on  vch.vendor_catalog_header_id= vcfm.vendor_catalog_header_id ")
		.append(" where vch.vendor_catalog_id=:vendorCatalogID AND vcfm.VENDOR_CATALOG_TMPL_ID=:vendorCatalogTemplateID order by isMaster desc ")
		.append(" )");

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery query = (SQLQuery)session.createSQLQuery(sqlQuery.toString())
			.addScalar("rowNumber", Hibernate.INTEGER)
			.addScalar("vendorSuppliedFieldID", Hibernate.LONG)
			.addScalar("vendorSuppliedField")
			.addScalar("isMasterRecord", Hibernate.YES_NO)
			.addScalar("masterMappingId", Hibernate.LONG)
			.addScalar("masterAttributeID", Hibernate.LONG)
			.addScalar("mappingAttribute")
			.addScalar("masterAttributeID", Hibernate.LONG)
			.addScalar("vendorSuppliedFieldNum", Hibernate.LONG)
			.setResultTransformer(Transformers.aliasToBean(CatalogMappedFieldDTO.class));
			query.setLong("vendorCatalogID", vendorCatalogID);
			query.setLong("vendorCatalogTemplateID", vendorCatalogTemplateID);
			mappedFieldDTOList = query.list();

		} catch (Exception e) {
			log.error("Hibernate Exception caught in getTemplateVendorCatalogHeaderList() vendorCatalogID-" + vendorCatalogID);
			log
			.error("Hibernate Exception caught in updateOldTypes() productGroup-"
					+ vendorCatalogID);
		}

		return mappedFieldDTOList;

	}
	/**
	 * Gets the Attribute lookup String based on the value based by the look up value
	 * @param blueMartiniAttribute
	 * @param lookUpValue
	 * @return
	 */
	public List<String> getCarFldMappingDatavalue(String blueMartiniAttribute, String lookUpValue) {

		List<String> fldMappingValue = new ArrayList<String>(0);
		StringBuffer query = new StringBuffer();
		query
		.append("SELECT distinct(value) from attribute_lookup_value ")
		.append(" where status_cd='ACTIVE' and attr_id in( ")
		.append(
		" select attr_id from attribute where status_Cd='ACTIVE' and blue_martini_attribute  like :BLUEMARTINI_ATTR")
		.append(") and  upper(value) like :LOOKUP_VALUE  order by value");
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sqlQuery = session.createSQLQuery(query.toString());
			sqlQuery.setString("BLUEMARTINI_ATTR", blueMartiniAttribute);
			sqlQuery.setString("LOOKUP_VALUE", lookUpValue);
			fldMappingValue = sqlQuery.list();
		} catch (Exception e) {
			log.error(
					"Hibernate Exception caught in getCarFldMappingDatavalue() blueMartiniAttribute-"
					+ blueMartiniAttribute + "lookup value:" + lookUpValue  , e);

		}
		return fldMappingValue;

	}


	/**
	 * Gets all the attributes based on the blue martini value selected
	 * @param blueMartiniAttribute
	 * @param lookUpValue
	 * @return
	 */
	public List<String> getCarFldMappingDatavalue(String blueMartiniAttribute) {

		List<String> fldMappingValue = new ArrayList<String>(0);
		StringBuffer query = new StringBuffer();
		query
		.append("SELECT distinct(value) from attribute_lookup_value ")
		.append(" where status_cd='ACTIVE'  and attr_id in( ")
		.append(
		" select attr_id from attribute where status_Cd='ACTIVE' and blue_martini_attribute  like :BLUEMARTINI_ATTR ) order by value");

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sqlQuery = session.createSQLQuery(query.toString());
			sqlQuery.setString("BLUEMARTINI_ATTR", blueMartiniAttribute);
			fldMappingValue = sqlQuery.list();
		} catch (Exception e) {
			log.error(
					"Hibernate Exception caught in getCarFldMappingDatavalue() blueMartiniAttribute-"
					+ blueMartiniAttribute , e);

		}
		return fldMappingValue;

	}



	/**
	 * Gets the vendor field data mapping values based on the catalog id and catalog header field number
	 * passed from the data mapping header.
	 * @param vendorCataloID
	 * @param catalogHeaderFldNum
	 * @return
	 */
	public List<String> getVendorFldMappingDataValue(Long vendorCataloID, Long catalogHeaderFldNum){

		List<String> fldMappingValue = new ArrayList<String>(0);
		StringBuffer query = new StringBuffer();
		query
		.append("select distinct(vndr_catl_field_value) from   vendor_catalog_record ")
		.append(" where vendor_catalog_id=:vendorCataloID and ")
		.append(" vndr_catl_hdr_fld_num =:catalogHeaderFldNum order by vndr_catl_field_value");

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sqlQuery = session.createSQLQuery(query.toString());
			sqlQuery.setLong("vendorCataloID", vendorCataloID);
			sqlQuery.setLong("catalogHeaderFldNum", catalogHeaderFldNum);
			fldMappingValue = sqlQuery.list();
		} catch (Exception e) {
			log.error(
					"Hibernate Exception caught in getCarFldMappingDatavalue() vendorCataloID-"
					+ vendorCataloID + "catalogHeaderFldNum:" + catalogHeaderFldNum  , e);

		}
		return fldMappingValue;


	}

	/**
	 * Gets the attribute lookup value based on the attribute selected in the form.
	 */
	public List<AttributeLookupValue> getAttributeLookUpValues(String attributeValue){
		return getHibernateTemplate().find(" from AttributeLookupValue where statusCd='ACTIVE' and value=?",attributeValue);
	}


	/**
	 * Saves the attribute lookup value
	 * @param vendorCatalogFldDataMapping
	 * @return
	 */
	public VendorCatalogFieldDataMapping saveDataFieldMapping(VendorCatalogFieldDataMapping vendorCatalogFldDataMapping) {
		if(log.isDebugEnabled()){
			log.debug("entering VendorCatalogDaoHibernate's save saveAttribute method...");
		}
		getHibernateTemplate().saveOrUpdate(vendorCatalogFldDataMapping);
		getHibernateTemplate().flush();
		return vendorCatalogFldDataMapping;
	}

	public boolean saveVendorProperties(String vendorId,String display) {
		if(log.isDebugEnabled()){
			log.debug("Inside saveVendorProperties method");
		}
		Vendor vendor  = new Vendor();
		vendor = (Vendor) this.getHibernateTemplate().get(Vendor.class, Long.parseLong(vendorId));
		vendor.setIsDisplayable(display);
		this.getHibernateTemplate().saveOrUpdate(vendor);
		getHibernateTemplate().flush();
		if(log.isDebugEnabled()) {
			log.debug("End saveVendorProperties method");
		}
		return true;
	}

	/**
	 * Gets the existing saved field data mapping value from the database based on the
	 * catalog id and field header name.
	 * @param vendorCatalogID
	 * @param vendorFieldHeader
	 * @return
	 */
	public List<FieldMappingDataDTO> getSavedFieldMappingData(Long vendorCatalogID, String vendorFieldHeader){
		List<FieldMappingDataDTO> fieldMappingDataDTOs = new ArrayList<FieldMappingDataDTO>(0);
		StringBuffer sqlQuery = new StringBuffer("select  distinct(vcr.vndr_catl_field_value) as vendorField, ");
		sqlQuery
		.append(" vcfd.vndr_catl_hdr_fld_name as vendorHeaderField, decode(vcfd.belk_attr_lookup_value_id,null,'N','Y') as mapped, ")
		.append(" atr.value as carValue ")
		.append(" from   vendor_catalog_record vcr ")
		.append(" left outer join vndr_catl_fld_data_mapping vcfd on vcr.vndr_catl_field_value= vcfd.vndr_catl_field_value ")
		.append(" left outer join attribute_lookup_value atr on vcfd.belk_attr_lookup_value_id= atr.attr_lookup_value_id ")
		.append(" inner join vendor_catalog_header vch on vch.vndr_catl_hdr_fld_num = vcr.vndr_catl_hdr_fld_num ")
		.append(" inner join vendor_catalog vc on vcr.vendor_catalog_id=vc.vendor_catalog_id and vcfd.vendor_catalog_tmpl_id=vc.vendor_catalog_tmpl_id ")
		.append(" and vch.vndr_catl_hdr_fld_name=:vendorFieldHeader" )
		.append(" where vcr.vendor_catalog_id=:vendorCatalogID" )
		.append(" order by vcr.vndr_catl_field_value" );

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery query = (SQLQuery)session.createSQLQuery(sqlQuery.toString())
			.addScalar("vendorHeaderField")
			.addScalar("vendorField")
			.addScalar("mapped", Hibernate.YES_NO)
			.addScalar("carValue")
			.setResultTransformer(Transformers.aliasToBean(FieldMappingDataDTO.class));
			query.setString("vendorFieldHeader", vendorFieldHeader);
			query.setLong("vendorCatalogID", vendorCatalogID);
			fieldMappingDataDTOs = query.list();

		} catch (Exception e) {
			log.error("Hibernate Exception caught in getSavedFieldMappingData() vendorCatalogID-" + vendorCatalogID);
			log.error("Hibernate Exception caught in getSavedFieldMappingData() vendorFieldHeader-"+ vendorFieldHeader);
		}

		return fieldMappingDataDTOs;


	}

	/**
	 * Gets the vendor catalog templates for a given vendor based on the
	 * vendor id passed.
	 *
	 * @param vendorID
	 * @return
	 */
	public List<VendorCatalogTemplate> getCatlogTemplateForVendor(Long vendorID){
		List<VendorCatalogTemplate> vendorCatalogTemplates = new ArrayList<VendorCatalogTemplate>(0);
		StringBuffer sqlQuery = new StringBuffer("select distinct(vct.vendor_catalog_tmpl_id) as vendorCatalogTmplID, ");
		sqlQuery
		.append(" vct.vendor_catalog_tmpl_name as vendorCatalogName, vct.is_global as global,vct.locked_by as isLocked")
		.append(" from vendor_catalog_template vct")
		.append(" inner join vendor_catalog vc on vc.vendor_catalog_tmpl_id = vct.vendor_catalog_tmpl_id and ")
		.append(" vc.vendor_id=:vendorID" );

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery query = (SQLQuery)session.createSQLQuery(sqlQuery.toString())
			.addScalar("vendorCatalogTmplID",Hibernate.LONG)
			.addScalar("vendorCatalogName")
			.addScalar("global",Hibernate.YES_NO)
			.addScalar("isLocked")

			.setResultTransformer(Transformers.aliasToBean(VendorCatalogTemplate.class));
			query.setLong("vendorID", vendorID);
			vendorCatalogTemplates = query.list();

		} catch (Exception e) {
			log.error("Hibernate Exception caught in getCatlogTemplateForVendor() vendorID-" + vendorID);
		}

		return vendorCatalogTemplates;
	}

	/**
	 * Gets the globally defined templates
	 * @return
	 */
	public List<VendorCatalogTemplate > getGlobalTemplates(){
		return getHibernateTemplate().find("from VendorCatalogTemplate where global=?",true);
	}
	public   String getVendorCatalogTemplateId(String vendorCatalogId) {
		String vendorCatalogTemplateId=null;
		VendorCatalog  vendorCatalog=null;
		vendorCatalog=(VendorCatalog)getHibernateTemplate().get(VendorCatalog.class, new Long(vendorCatalogId));
		if(vendorCatalog.getVendorCatalogTemplate()!=null) {
			vendorCatalogTemplateId = ""+vendorCatalog.getVendorCatalogTemplate().getVendorCatalogTmplID();
		}
		return vendorCatalogTemplateId;
	}

	/**
	 * Gets the vendors which has templates other than the vendor passed.
	 * @param vendorID
	 * @return
	 */
	public List<Vendor> getOtherVendors(Long vendorID){
		List<Vendor> vendors = new ArrayList<Vendor>(0);
		StringBuffer sqlQuery = new StringBuffer("select DISTINCT(ven.vendor_id ) as vendorId, ");

		sqlQuery
		.append("  ven.name as name")
		.append(" from vendor ven")
		.append(" inner join vendor_catalog vnc on vnc.vendor_id= ven.vendor_id ")
		.append(" inner join vendor_catalog_template vct on vnc.vendor_catalog_tmpl_id= vct.vendor_catalog_tmpl_id" )
		.append(" where ven.status_cd='ACTIVE' and ven.vendor_id not in(:vendorID)" );

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery query = (SQLQuery)session.createSQLQuery(sqlQuery.toString())
			.addScalar("vendorId",Hibernate.LONG)
			.addScalar("name")
			.setResultTransformer(Transformers.aliasToBean(Vendor.class));
			query.setLong("vendorID", vendorID);
			vendors = query.list();

		} catch (Exception e) {
			log.error("Hibernate Exception caught in getOtherVendors() vendorID-" + vendorID);
		}

		return vendors;

	}

	/**
	 * Gets the number of templates with the same name. It does not
	 * matter what case it is.
	 * @param templateName
	 * @return
	 */
	public int getCountOfTemplateName(String templateName){

		int count=2; //Set the default value greater than 0 to make sure no error
		StringBuffer query= new StringBuffer();
		StringBuffer queryCatalog = new StringBuffer();
		query.append("select count(*) from VendorCatalogTemplate")
		.append(" where upper(vendorCatalogName)= upper( :TEMPLATE_NAME )");

		queryCatalog.append("select count(*) from VendorCatalog")
		.append(" where upper(vendorCatalogName)= upper( :TEMPLATE_NAME )");

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {

			Query sqlQuery = session.createQuery(query.toString());
			sqlQuery.setString("TEMPLATE_NAME", templateName);
			count = ((Number) sqlQuery.uniqueResult()).intValue();

			Query sqlQueryCatalog = session.createQuery(queryCatalog.toString());
			sqlQueryCatalog.setString("TEMPLATE_NAME", templateName);
			count = count + ((Number) sqlQueryCatalog.uniqueResult()).intValue();

		} catch (Exception e) {
			log.error("Hibernate Exception caught in getCountOfTemplateName() templateName-"
					+ templateName, e);

		}
		return count;

	}

	/**
	 *
	 * @param vendorStyleId String
	 * @param vendorCatalogId String
	 * @param vendorUpc String
	 * @return VendorStyleInfo
	 */
	public VendorStyleInfo getStyleInfo(String vendorStyleId,String vendorCatalogId,String vendorUpc){
		List <VendorStyleInfo> styleInfoList =  new ArrayList();
		VendorStyleInfo styleInfo =null;
		String temp = vendorUpc;
		if(!"0".equals(vendorUpc))
		{
			temp = StringUtils.leftPad(vendorUpc, 13, "0");
		}
		StringBuffer sqlQuery = new StringBuffer("select vcss.vendor_style_id as vendorStyleId,vcss.description as description,vcss.vendor_catalog_id as vendorCatalogId,  ");
		sqlQuery.append("vcat.vendor_catalog_name as lastCatalogImported,to_char(vci.imported_date,'mm/dd/yyyy') as dateLastImport,vcss.vendor_upc as vendorUpc ");
		sqlQuery.append("from vendor_catalog_sty_sku vcss left outer join vendor_catalog vcat on vcat.vendor_catalog_id = vcss.vendor_catalog_id ");
		sqlQuery.append("left outer join vendor_catalog_import vci on vcss.vendor_catalog_id = vci.vendor_catalog_id ");
		sqlQuery.append("where vcss.vendor_catalog_id = :VENDOR_CATALOG_ID and vcss.vendor_style_id = :VENDOR_STYLE_ID and vcss.vendor_upc= :TEMP");

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery query = (SQLQuery)session.createSQLQuery(sqlQuery.toString())
			.addScalar("vendorStyleId",Hibernate.STRING)
			.addScalar("description",Hibernate.STRING)
			.addScalar("vendorCatalogId",Hibernate.STRING)
			.addScalar("lastCatalogImported",Hibernate.STRING)
			.addScalar("dateLastImport",Hibernate.STRING)
			.addScalar("vendorUpc",Hibernate.STRING)
			.setResultTransformer(Transformers.aliasToBean(VendorStyleInfo.class));
			query.setString("VENDOR_CATALOG_ID", vendorCatalogId);
			query.setString("VENDOR_STYLE_ID", vendorStyleId);
			query.setString("TEMP", temp);
			styleInfoList = query.list();
			if(!styleInfoList.isEmpty()) {
				styleInfo = styleInfoList.get(0);
			}

		} catch (Exception e) {
			log.error("Hibernate Exception caught in getStyleInfo() vendorStyleId-" + vendorStyleId);
		}

		return styleInfo;

	}
	/**
	 * This Method returns the Master Mapping Attribute.
	 * @param Long
	 * @param Long
	 * */
	public List<MasterAttributeMapping> getMasterMappingAttribute(Long catalogTemplateID, String fieldName) {
		Object [] catalog = {catalogTemplateID, fieldName};
		return getHibernateTemplate().find("from MasterAttributeMapping where catalogTemplateId =? and catalogHeaderFieldName=? and status='ACTIVE' ",
				catalog);
	}

         /**
         *Get all the Vendor Catalog Styles.
         * @param catalogStyleDTO VendorCatalogStyleDTO
         * @return List<VendorCatalogStyleDTO>
         */
	public List <VendorCatalogStyleDTO> getVendorStyles(VendorCatalogStyleDTO styleDTO) {
		if(log.isDebugEnabled()) {
                log.debug("Start 'getVendorStyles' method ->" + System.currentTimeMillis());
                }
		List <VendorCatalogStyleDTO> vendorCatalogStyleList =  new ArrayList <VendorCatalogStyleDTO> ();
                List <VendorCatalogStyleDTO> catalogStyleList =  new ArrayList <VendorCatalogStyleDTO> ();


		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		String query = getSearchCriteriaForVendorStyle(styleDTO);
		try {

			SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query).
                                          addScalar("dateLastUpdated", Hibernate.STRING).
                                          addScalar("vendorStyleId",  Hibernate.STRING).
                                          addScalar("vendorCatalogId", Hibernate.STRING).
                                          addScalar("vendorId",  Hibernate.STRING).
                                          addScalar("status",  Hibernate.STRING).
                                          addScalar("catalogName", Hibernate.STRING).
                                          addScalar("lastUpdatedBy",  Hibernate.STRING).
                                          addScalar("lockedBy",  Hibernate.STRING).
                                          addScalar("imagePath", Hibernate.STRING).
                                          addScalar("description", Hibernate.STRING).
                                          addScalar("recordNum", Hibernate.STRING).
                                          addScalar("catalogTemplateId", Hibernate.STRING).
                                          addScalar("vendorUPC", Hibernate.STRING).
                                          addScalar("catalogStatus", Hibernate.STRING).
                                          setResultTransformer(Transformers.aliasToBean(VendorCatalogStyleDTO.class)) ;



			vendorCatalogStyleList = sQuery.list();
			Iterator itr = vendorCatalogStyleList.iterator();
                        List <VendorStyleSKUCount> styleSKUCountList =  getStyleSKUCount(styleDTO.getVendorId());
                        //Get the details of each vendor Styles
                        int ind =0;
			while(itr.hasNext()) {
                            VendorCatalogStyleDTO catalogStyleDTO =  (VendorCatalogStyleDTO) itr.next();
				if(!catalogStyleList.contains(catalogStyleDTO)) {
                                    VendorStyleSKUCount sKUCount =  new VendorStyleSKUCount();
                                    sKUCount.setVendorStyleId(catalogStyleDTO.getVendorStyleId());
                                    ind = styleSKUCountList.indexOf(sKUCount);
                                    if(ind>=0) {
                                    catalogStyleDTO.setNumSKUs(styleSKUCountList.get(ind).getSkuCount());
                                    } else {
                                     catalogStyleDTO.setNumSKUs(DropShipConstants.ZERO);
                                    }
                                    
                                    catalogStyleList.add(catalogStyleDTO);
                                }

			}
		} catch (Exception e) {
			log.error("Hibernate Exception caught in getVendorStyles- " + e);
			e.printStackTrace();

		}
                return catalogStyleList;

        }


        /**
         * Get Search Criteria SQL String For VendorStyle
         * @param styleDTO VendorCatalogStyleDTO
         * @return String
         */
	private String getSearchCriteriaForVendorStyle(VendorCatalogStyleDTO styleDTO) {
		if(log.isDebugEnabled()) {
                log.debug("Start 'getSearchCriteriaForVendorStyle' method");
                }
                StringBuffer query= new StringBuffer();
		String vendorId = styleDTO.getVendorId();
		String vendorStyleId = styleDTO.getVendorStyleId();
		String vendorUPC =  styleDTO.getVendorUPC();
		String status =  styleDTO.getStatus();
		String userDerartmentOnly =styleDTO.getUserDerartmentOnly();
		String dateUpdateStart = styleDTO.getDateUpdateStart();
		String dateUpdateEnd = styleDTO.getDateUpdateEnd();
		String userId =styleDTO.getUserId();
		String description =styleDTO.getDescription();
		String department = styleDTO.getDepartment();


                query.append("select max(to_char(vcssm.updated_date,'mm/dd/yyyy')) as dateLastUpdated ,vcssm.vendor_style_id as vendorStyleId ,");
                query.append("vcssm.vendor_catalog_id as vendorCatalogId,vcssm.vendor_id as vendorId,vcssm.status_cd as status ,");
                query.append("vcat.vendor_catalog_name as catalogName,vcss.updated_by as lastUpdatedBy,vcss.locked_by as lockedBy,");
                query.append("vcssi.image_file_name as imagePath,vcss.description as description ,vcss.record_number as recordNum,");
                query.append("vcat.vendor_catalog_tmpl_id as catalogTemplateId,vcssm.vendor_upc as vendorUPC,vcat.status_cd as catalogStatus ");
                query.append("from vndr_catl_sty_sku_mast vcssm left outer join vendor_catalog vcat ");
                query.append("on vcssm.vendor_id = vcat.vendor_id and vcssm.vendor_catalog_id = vcat.vendor_catalog_id ");
                query.append("left outer join vendor_catalog_sty_sku vcss on vcssm.vendor_catalog_id = vcss.vendor_catalog_id and ");
                query.append("vcssm.vendor_style_id = vcss.vendor_style_id and vcssm.vendor_upc =  vcss.vendor_upc ");
                query.append("left outer join vndr_catl_sty_sku_image vcssi on vcssm.vendor_catalog_id = vcssi.vendor_catalog_id ");
                query.append("and vcssi.vendor_style_id = vcssm.vendor_style_id  and vcssm.vendor_upc = vcssi.vendor_upc ");
                query.append("where vcssm.vendor_id ="+ vendorId + " AND vcat.status_cd != 'DELETED' ");

                if(null!=vendorStyleId && StringUtils.isNotBlank(vendorStyleId)) {
                    query.append(" and UPPER(vcssm.vendor_style_id) like '%"+ vendorStyleId.toUpperCase(Locale.US)+"%'");
                }
                if(null!=vendorUPC && StringUtils.isNotBlank(vendorUPC)) {
                    query.append(" and vcssm.vendor_upc ='"+ vendorUPC+"'");
                }
                if(null!=status && StringUtils.isNotBlank(status)) {
                    query.append(" and vcssm.status_cd ='"+ status+"'");
                }
                if(null!=status && StringUtils.isNotBlank(status)) {
                    query.append(" and vcssm.status_cd ='"+ status+"'");
                }
                if(null != dateUpdateStart && !StringUtils.isBlank(dateUpdateStart) && null != dateUpdateEnd
                        && !StringUtils.isBlank(dateUpdateEnd)) {
			query.append(" and vcssm.updated_date between to_date('" + dateUpdateStart +"','mm/dd/yyyy')");
                        query.append(" and to_date('" + dateUpdateEnd + "','mm/dd/yyyy')+1");

		}

                if(null != department && !StringUtils.isBlank(department)) {
			query.append(" and vcss.dept_id in (select dept_id from department where dept_cd in ('" + department +"'))");

		}
                if(null != description && !StringUtils.isBlank(description)) {
                          query.append(" and  UPPER(vcss.description) like'%" +  description.toUpperCase(Locale.US) +"%' ");

		}
                if(null != userDerartmentOnly && userDerartmentOnly.equals("true")) {
                        query.append(" and vcss.dept_id ");
                        query.append(" in ( select car_user_dept.dept_id from car_user_department car_user_dept where car_user_dept.car_user_id =" +userId +")");
		}

                query.append(" group by vcssm.vendor_style_id, vcssm.vendor_catalog_id,vcssm.vendor_id,vcssm.status_cd,vcat.vendor_catalog_name,");
                query.append("vcss.updated_by,vcss.locked_by,vcssi.image_file_name,vcss.description,vcss.record_number,vcat.vendor_catalog_tmpl_id,");
                query.append("vcssm.vendor_upc,vcat.status_cd,vcssm.updated_date order by vcssm.updated_date");
                String serachCriteria = query.toString();

                    if(log.isDebugEnabled()) {
                    log.debug("Sql Query-> " + serachCriteria);
                    log.debug("End 'getSearchCriteriaForVendorStyle' method");
                }
		return serachCriteria;
	}
	/**
	 *
	 * This method gets the vendor catalog template  from VendorCatalogTemplate table
	 * @param  String vendorCatalogTmplName
	 * @return VendorCatalogTemplate
	 */
	public VendorCatalogTemplate getVendorCatalogTemplate(String vendorCatalogTmplName) {
		if(log.isDebugEnabled()){
			log.debug("entering VendorCatalogDaoHibernate's getVendorCatalogTemplate method...vendorCatalogTmplName:"+vendorCatalogTmplName);
		}
		VendorCatalogTemplate vendorCatalogTmpl = null;
		List<VendorCatalogTemplate> list = getHibernateTemplate().find(" from VendorCatalogTemplate  where vendorCatalogName = ?", vendorCatalogTmplName);
		if (list != null && list.size() > 0) {
			vendorCatalogTmpl = list.get(0);
		}	
		if (vendorCatalogTmpl != null ) {
			if(log.isDebugEnabled()){
				log.debug("Vendor Catalog:"+vendorCatalogTmpl.getGlobal());
			}

		}
		return vendorCatalogTmpl;
	}
	/**
	 * This method removes the mapping records from the database.
	 * @param catalogTemplate VendorCatalogTemplate.
	 * */
	public void removeMappingAttribute(VendorCatalogTemplate catalogTemplate) {
		List<MasterAttributeMapping> masterMappingList = getHibernateTemplate().find("from MasterAttributeMapping where catalogTemplateId = ?", catalogTemplate.getVendorCatalogTmplID());
		List<VendorCatalogFieldMapping> fieldMappingList =  getHibernateTemplate().find("from VendorCatalogFieldMapping where compositeKey.vendorCatalogtTemplateId = ?", catalogTemplate.getVendorCatalogTmplID());
		List<VendorCatalogFieldDataMapping> dataFieldMappingList = getHibernateTemplate().find("from VendorCatalogFieldDataMapping where compositeKey.vendorCatalogtTemplateId = ?", catalogTemplate.getVendorCatalogTmplID());
		if (dataFieldMappingList != null && dataFieldMappingList.size() > 0) {
			getHibernateTemplate().deleteAll(dataFieldMappingList);
		}
		if (fieldMappingList != null && fieldMappingList.size() > 0) {
			getHibernateTemplate().deleteAll(fieldMappingList);
		}
		if (dataFieldMappingList != null && dataFieldMappingList.size() > 0) {
			getHibernateTemplate().deleteAll(masterMappingList);
		}
		
	}
        /**
         * 
         * @param stylePropertiesDTO
         * @param user
         */
        private void saveStyleMaster(VendorStylePropertiesDTO stylePropertiesDTO,User user) {
            //update the master table.
                        String updateColor = stylePropertiesDTO.getUpdateColor();
                        String updateDesc = stylePropertiesDTO.getUpdateDescription();
                        String desc = null;
                        String color =null;
                        String mappedFieldValues[] = stylePropertiesDTO.getMappedFieldsArr();
                        if(null!=updateColor && updateColor.equals(DropShipConstants.TRUE)) {
                           color = mappedFieldValues[Integer.parseInt(stylePropertiesDTO.getColorFieldNo())] ;
                        }

                         if(null!=updateDesc && updateDesc.equals(DropShipConstants.TRUE)) {
                           desc = mappedFieldValues[Integer.parseInt(stylePropertiesDTO.getDescriptionFieldNo())] ;
                        }
                        //Update Color in Vendor Catalog Style Sku Master Table.
                        String temp ="0";
                        if(!stylePropertiesDTO.getVendorUpc().equals(DropShipConstants.ZERO)) {
                            temp = StringUtils.leftPad(stylePropertiesDTO.getVendorUpc(), 13, "0");
                        }

                        if(log.isDebugEnabled()) {
                        log.debug("Vendor Upc-> "+ temp);
                        log.debug("Vendor Id-> "+ stylePropertiesDTO.getVendorId());
                        log.debug("Vendor Style Id -> "+ stylePropertiesDTO.getVendorStyleId());
                        }
                        VendorCatalogStyleSkuMasterId catalogStyleSkuMasterId =  new VendorCatalogStyleSkuMasterId();
			VendorCatalogStyleSkuMaster catalogStyleSkuMaster  = new VendorCatalogStyleSkuMaster();
			catalogStyleSkuMasterId.setVendorId(Long.parseLong(stylePropertiesDTO.getVendorId()));
                        catalogStyleSkuMasterId.setVendorStyleId(stylePropertiesDTO.getVendorStyleId());
			catalogStyleSkuMasterId.setVendorUPC(temp);
			catalogStyleSkuMaster = (VendorCatalogStyleSkuMaster)this.getHibernateTemplate().get(VendorCatalogStyleSkuMaster.class, catalogStyleSkuMasterId);
			catalogStyleSkuMaster.setCompositeKey(catalogStyleSkuMasterId);
                         if(null!=updateColor && updateColor.equals(DropShipConstants.TRUE)) {
			catalogStyleSkuMaster.setColor(color);
                         }
			catalogStyleSkuMaster.setVendorCatalogId(Long.parseLong(stylePropertiesDTO.getVendorCatalogId()));
			catalogStyleSkuMaster.setUpdatedDate(new Date());
			this.getHibernateTemplate().saveOrUpdate(catalogStyleSkuMaster);
                        if(log.isDebugEnabled()) {
                        log.debug("Style SKU Master Table Updated");
                        }

                        //Update the existing reord
			VendorCatalogStyleSku catalogStyleSku1 =  new VendorCatalogStyleSku();
			VendorCatalogStyleSkuId catalogStyleSkuId1 =  new VendorCatalogStyleSkuId();
			catalogStyleSkuId1.setVendorCatalogId(Long.parseLong(stylePropertiesDTO.getVendorCatalogId()));
                        catalogStyleSkuId1.setVendorStyleId(stylePropertiesDTO.getVendorStyleId());
			catalogStyleSkuId1.setVendorUPC(temp);
			catalogStyleSku1 = (VendorCatalogStyleSku) this.getHibernateTemplate().get(VendorCatalogStyleSku.class, catalogStyleSkuId1);
			catalogStyleSku1.getCompositeKey().setVendorUPC(temp);
			catalogStyleSku1.setColor(color);
                        catalogStyleSku1.setDescription(desc);
			catalogStyleSku1.setAuditInfo(user);
			this.getHibernateTemplate().saveOrUpdate(catalogStyleSku1);
            getHibernateTemplate().flush();
			if(log.isDebugEnabled()) {
                log.debug("Style SKU Table Updated");
            }
        }
	
	/**
	 * @param cls -Class
	 * @param id - ID of the class
	 * @return - Object
	 * @TODO
	 */
	@SuppressWarnings("unchecked")
	public Object getById(Class cls, Serializable id) {
		return this.getHibernateTemplate().get(cls, id);
	}


        /**
         * Get SKU count for each Catalog Vendor.
         * @return List<VendorCatalogSKUCount>
         */
	private List<VendorStyleSKUCount> getStyleSKUCount(String vendorId) {
                 if(log.isDebugEnabled()) {
                    log.debug("Start 'getStyleSKUCount' method");
                }
		StringBuffer query= new StringBuffer();
		List <VendorStyleSKUCount>  skuCountList =  new ArrayList <VendorStyleSKUCount> ();
		query.append("select count(distinct vendor_upc ) as skuCount, vendor_style_id as vendorStyleId from vndr_catl_sty_sku_mast " +
                             "where vendor_upc !='0' and vendor_id = :VENDOR_ID group by vendor_style_id");
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query.toString()).
                                          addScalar("skuCount",Hibernate.STRING).
                                          addScalar("vendorStyleId",Hibernate.STRING).
                                          setResultTransformer(Transformers.aliasToBean(VendorStyleSKUCount.class));
			sQuery.setString("VENDOR_ID", vendorId);
			skuCountList = sQuery.list();
		} catch (Exception e) {
			log.error("Hibernate Exception caught in getStyleSKUCount");
			e.printStackTrace();

		}
                  if(log.isDebugEnabled()) {
                    log.debug("Sql Query->" + query.toString());
                    log.debug("End 'getStyleSKUCount' method");
                }
		return skuCountList;
	}

		public VendorCatalogFieldDataMapping getMappingObjectById(
				CompositeKeyForDataFldMapping key) {
			List <VendorCatalogFieldDataMapping> list = getHibernateTemplate()
			.find(
					"FROM VendorCatalogFieldDataMapping WHERE compositeKey.vendorCatalogtTemplateId=? " +
					"AND compositeKey.vendorCatalogHeaderFieldName=? AND compositeKey.vendorCatalogFieldValue=?",
					new Object[] { key.getVendorCatalogtTemplateId(), key.getVendorCatalogHeaderFieldName(), key.getVendorCatalogFieldValue() });
			if(null != list && !list.isEmpty()) {
				return list.get(0);
			}
			return null;
		}

		public List<String> getCarFldMappingDatavalue(
				String blueMartiniAttribute, String[] selectedProductGroups, Long catalogId) {

	        List<String> fldMappingValue = new ArrayList<String>(0);

	        String temp="";

	        for (int cnt=0;cnt<selectedProductGroups.length;cnt++) {
	            if(StringUtils.isNotBlank(temp)) {
	                temp=temp+",";
	            }
	            temp= temp+ selectedProductGroups[cnt];
	        }
	        //Query containing product type attributes.
	        StringBuffer query = new StringBuffer();
	        query.append("select distinct(alv.value) from attribute atr inner join attribute_lookup_value alv on atr.attr_id = alv.attr_id  where atr.status_cd='ACTIVE' and ").
	              append(" blue_martini_attribute like :BLUE_MARTINI_ATTRIBUTE  and atr.attr_id in ( select pta.attr_id from product_group pg inner join prod_group_prod_type pgpt ").
	              append(" on pg.product_group_id=pgpt.product_group_id inner join product_type pt on pgpt.product_type_id = pt.product_type_id ").
	              append(" inner join product_type_attribute pta on pt.product_type_id = pta.product_type_id where pg.product_group_id in ( "+temp+" ) and pt.status_cd='ACTIVE') order by alv.value");
	        if(log.isDebugEnabled()){
	        	log.debug("Query:" + query);
	        }
			SessionFactory sf = getHibernateTemplate().getSessionFactory();
			Session session = sf.getCurrentSession();
			try {
				SQLQuery sqlQuery = session.createSQLQuery(query.toString());
				sqlQuery.setString("BLUE_MARTINI_ATTRIBUTE", blueMartiniAttribute);
				//Here the temp variable cannot be parameterized. It gives SQL Grammer Exception. 
				//sqlQuery.setString("TEMP", temp);   
				fldMappingValue = sqlQuery.list();
			} catch (Exception e) {
				log.error(
						"Hibernate Exception caught in getCarFldMappingDatavalue() blueMartiniAttribute-"
						+ blueMartiniAttribute , e);

			}
			return fldMappingValue;

		}
		
		/*
		 * (non-Javadoc)
		 * @see com.belk.car.app.dao.VendorCatalogDao#lockCatalog(java.lang.String, org.appfuse.model.User)
		 * Lock the Catalog for the user
		 */
		public void lockCatalog(String vendorCatalogId,User user) {
		      if(log.isDebugEnabled()) {
		    	  	log.debug("Start 'lockCatalog' method vendorCatalogId:"+vendorCatalogId +" Username:"+user.getUsername());
	         	}
			 	try {
					VendorCatalog catalog = (VendorCatalog) getHibernateTemplate().get(VendorCatalog.class, Long.parseLong(vendorCatalogId));
					catalog.setLockedBy(user.getUsername());
					catalog.setAuditInfo(user);
					this.getHibernateTemplate().saveOrUpdate(catalog);
					getHibernateTemplate().flush();
				} catch(Exception e) {
				   log.error("Hibdernate Exception in lockCatalog -" + e)	;
				}
			     if(log.isDebugEnabled()) {
			        	 log.debug("End 'lockCatalog' method");
		        }
	    }
		
		/*
		 * (non-Javadoc)
		 * @see com.belk.car.app.dao.VendorCatalogDao#unlockCatalogs(java.lang.String)
		 * Unlock the cataog for the user
		 */
				
		public void unlockCatalogs(String userName){
			 try{
				   SessionFactory sf = getHibernateTemplate().getSessionFactory();
				   Session session = sf.getCurrentSession();
				   String query = "UPDATE VENDOR_CATALOG SET LOCKED_BY=NULL WHERE LOCKED_BY= :USER";
				   int updateRows =  session.createSQLQuery(query).setString("USER", userName).executeUpdate();
				   log.info(updateRows+" Catalogs Unlocked for the user:"+userName);
	        }catch(Exception e){
	        	log.error("Got exception while unlocking all the Catalogs : " + e.getMessage());
	        }
			
		}
		
		/*
		 * (non-Javadoc)
		 * @see com.belk.car.app.dao.VendorCatalogDao#removeVendorCatalog(java.lang.String)
		 * Remove the catalog which is in Importing status
		 */
				
		public void removeVendorCatalog(String vendorCatalogId){
				if(log.isDebugEnabled()) {
		    	  	log.debug("removeVendorCatalog id:"+vendorCatalogId);
	         	}
			 	try {
			 		VendorCatalog vendorCatalog=getVendorCatalog(Long.parseLong(vendorCatalogId));
					vendorCatalog.setStatusCD("DELETED");
			 		this.getHibernateTemplate().saveOrUpdate(vendorCatalog);
			 		getHibernateTemplate().flush();
			 	} catch(Exception e) {
				   log.error("Hibdernate Exception in removeVendorCatalog -" + e)	;
				}
		     if(log.isDebugEnabled()) {
		        	 log.debug("End 'removeVendorCatalog' method");
		     }
		}
}


